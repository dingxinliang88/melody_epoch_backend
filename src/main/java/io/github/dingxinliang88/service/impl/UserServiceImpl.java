package io.github.dingxinliang88.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.constants.EmailConstant;
import io.github.dingxinliang88.mapper.UserMapper;
import io.github.dingxinliang88.pojo.dto.user.AccLoginReq;
import io.github.dingxinliang88.pojo.dto.user.AccRegisterReq;
import io.github.dingxinliang88.pojo.dto.user.EmailLoginReq;
import io.github.dingxinliang88.pojo.po.User;
import io.github.dingxinliang88.pojo.vo.UserLoginVO;
import io.github.dingxinliang88.service.UserService;
import io.github.dingxinliang88.utils.SysUtil;
import io.github.dingxinliang88.utils.ThrowUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Integer userAccRegister(AccRegisterReq req, HttpServletRequest request) {
        String account = req.getAccount();
        String password = req.getPassword();
        String checkedPassword = req.getCheckedPassword();
        Integer type = req.getType();

        ThrowUtil.throwIf(!password.equals(checkedPassword), StatusCode.PASSWORD_NOT_MATCH);

        // TODO 加锁 + 判断当前账号是否已经被注册
        // 加密
        String salt = SysUtil.genUserPwdSalt();
        String encryptPwd = SysUtil.encryptedPwd(salt, password);

        // 保存数据
        User user = new User(account, encryptPwd, salt, type);
        user.setNickname(SysUtil.genUserNickName());
        userMapper.insert(user);

        return user.getUserId();
    }

    @Override
    public UserLoginVO userAccLogin(AccLoginReq req, HttpServletRequest request) {
        String account = req.getAccount();
        String password = req.getPassword();
        Integer type = req.getType();

        // TODO 加锁
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

    @Override
    public UserLoginVO getCurrUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(LOGIN_STATE_KEY);
        return (UserLoginVO) userObj;
    }

    @Override
    public UserLoginVO userEmailLogin(EmailLoginReq req, HttpServletRequest request) {
        String email = req.getEmail();
        String code = req.getCode();
        Integer type = req.getType();

        // TODO 加锁
        // 获取服务器中的 code
        String serverCode = (String) redisTemplate.opsForValue().get(EmailConstant.CAPTCHA_KEY + email);

        assert serverCode != null;
        ThrowUtil.throwIf(!serverCode.equals(code), StatusCode.CODE_NOT_MATCH);

        // 根据用户的邮箱号查询用户是否存在，不存在即注册，存在即更新
        User userInfo = userMapper.queryByEmail(email);
        if (userInfo == null) {
            // 第一次登录，先注册
            userInfo = regByEmail(email, code, type);
            // TODO 根据用户类别去插入到不同的表中（fan、member）
        }

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .userId(userInfo.getUserId())
                .nickname(userInfo.getNickname())
                .type(userInfo.getType())
                .build();

        // 保存用户Session
        request.getSession().setAttribute(LOGIN_STATE_KEY, userLoginVO);

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


}
