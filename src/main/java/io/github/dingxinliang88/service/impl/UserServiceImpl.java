package io.github.dingxinliang88.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.constants.EmailConstant;
import io.github.dingxinliang88.exception.BizException;
import io.github.dingxinliang88.mapper.FanMapper;
import io.github.dingxinliang88.mapper.MemberMapper;
import io.github.dingxinliang88.mapper.UserMapper;
import io.github.dingxinliang88.pojo.dto.user.AccLoginReq;
import io.github.dingxinliang88.pojo.dto.user.AccRegisterReq;
import io.github.dingxinliang88.pojo.dto.user.EmailLoginReq;
import io.github.dingxinliang88.pojo.enums.UserRoleType;
import io.github.dingxinliang88.pojo.po.Fan;
import io.github.dingxinliang88.pojo.po.Member;
import io.github.dingxinliang88.pojo.po.User;
import io.github.dingxinliang88.pojo.vo.UserLoginVO;
import io.github.dingxinliang88.service.UserService;
import io.github.dingxinliang88.utils.SysUtil;
import io.github.dingxinliang88.utils.ThrowUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static io.github.dingxinliang88.constants.UserConstant.LOGIN_STATE_KEY;
import static io.github.dingxinliang88.constants.UserConstant.USER_ROLE_SET;

/**
 * Default User Service Implementation
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private MemberMapper memberMapper;

    @Resource
    private FanMapper fanMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Override
    @Transactional(rollbackFor = BizException.class)
    public Integer userAccRegister(AccRegisterReq req, HttpServletRequest request) {
        String account = req.getAccount();
        String password = req.getPassword();
        String checkedPassword = req.getCheckedPassword();
        Integer type = req.getType();

        ThrowUtil.throwIf(!password.equals(checkedPassword), StatusCode.PASSWORD_NOT_MATCH);

        // 判断当前账号是否已经被注册
        User userFromDb = userMapper.queryByAccount(account);
        ThrowUtil.throwIf(userFromDb != null, StatusCode.ACCOUNT_ALREADY_EXISTS);
        return transactionTemplate.execute(status -> {
            try {
                // 加锁 + 判断当前账号是否已经被注册
                User user;
                synchronized (account.intern()) {
                    // Rest of your code here
                    User u = userMapper.queryByAccount(account);
                    ThrowUtil.throwIf(u != null, StatusCode.ACCOUNT_ALREADY_EXISTS);

                    // 加密
                    String salt = SysUtil.genUserPwdSalt();
                    String encryptPwd = SysUtil.encryptedPwd(salt, password);

                    // 保存数据
                    user = new User(account, encryptPwd, salt, type);
                    user.setNickname(SysUtil.genUserNickName());
                    userMapper.insert(user);

                    // 根据用户类别去插入到不同的表中（fan、member）
                    if (UserRoleType.FAN.getType().equals(type)) {
                        insert2Fan(user.getUserId(), user.getNickname());
                    } else if (UserRoleType.MEMBER.getType().equals(type)) {
                        insert2Member(user.getUserId(), user.getNickname());
                    }
                    return user.getUserId();
                }
            } catch (Exception e) {
                // Mark the transaction as rollback-only in case of an exception
                status.setRollbackOnly();
                throw e;
            }
        });

    }

    @Override
    public UserLoginVO userAccLogin(AccLoginReq req, HttpServletRequest request) {
        String account = req.getAccount();
        String password = req.getPassword();
        Integer type = req.getType();

        // 使用账号字符串作为锁对象，确保同一账号的操作是原子的
        synchronized (account.intern()) {
            User user = userMapper.queryByAccount(account);

            ThrowUtil.throwIf(user == null, StatusCode.NOT_FOUND_ERROR, "账号不存在！");
            ThrowUtil.throwIf(!USER_ROLE_SET.contains(type), StatusCode.ROLE_NOT_FOUND);
            // 校验密码
            String encryptPwd = SysUtil.encryptedPwd(user.getSalt(), password);
            String encryptPwdFromDb = user.getPassword();
            ThrowUtil.throwIf(!encryptPwdFromDb.equals(encryptPwd), StatusCode.PASSWORD_NOT_MATCH, "密码错误！");

            UserLoginVO userLoginVO = UserLoginVO.builder()
                    .userId(user.getUserId())
                    .nickname(user.getNickname())
                    .type(user.getType())
                    .build();
            // 保存用户登陆态
            request.getSession().setAttribute(LOGIN_STATE_KEY, userLoginVO);

            return userLoginVO;
        }
    }

    @Override
    public UserLoginVO getCurrUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(LOGIN_STATE_KEY);
        return (UserLoginVO) userObj;
    }

    @Override
    @Transactional(rollbackFor = BizException.class)
    public UserLoginVO userEmailLogin(EmailLoginReq req, HttpServletRequest request) {
        String email = req.getEmail();
        String code = req.getCode();
        Integer type = req.getType();


        // 获取服务器中的 code
        String serverCode = (String) redisTemplate.opsForValue().get(EmailConstant.CAPTCHA_KEY + email);

        assert serverCode != null;
        ThrowUtil.throwIf(!serverCode.equals(code), StatusCode.CODE_NOT_MATCH);
        User userInfo;
        // 加锁
        synchronized (email.intern()) {
            // 根据用户的邮箱号查询用户是否存在，不存在即注册，存在即更新
            userInfo = userMapper.queryByEmail(email);
            if (userInfo == null) {
                // 第一次登录，先注册
                userInfo = regByEmail(email, code, type);
                // 根据用户类别去插入到不同的表中（fan、member）
                if (UserRoleType.FAN.getType().equals(type)) {
                    insert2Fan(userInfo.getUserId(), userInfo.getNickname());
                } else if (UserRoleType.MEMBER.getType().equals(type)) {
                    insert2Member(userInfo.getUserId(), userInfo.getNickname());
                }
            }
        }

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .userId(userInfo.getUserId())
                .nickname(userInfo.getNickname())
                .type(userInfo.getType())
                .build();

        // 保存用户Session
        request.getSession().setAttribute(LOGIN_STATE_KEY, userLoginVO);
        // 删除code
        redisTemplate.delete(EmailConstant.CAPTCHA_KEY + email);

        return userLoginVO;
    }


    private User regByEmail(String email, String code, Integer type) {
        // 将code作为盐值和密码存入即可
        User user = new User(email, type);
        user.setSalt(code);
        user.setPassword(SysUtil.encryptedPwd(code, code));
        user.setNickname(SysUtil.genUserNickName());
        userMapper.insert(user);

        return user;
    }

    private void insert2Member(Integer memberId, String name) {
        Member member = new Member(memberId, name);
        memberMapper.insert(member);
    }

    private void insert2Fan(Integer fanId, String name) {
        Fan fan = new Fan(fanId, name);
        fanMapper.insert(fan);
    }


}
