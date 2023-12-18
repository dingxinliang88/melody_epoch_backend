package io.github.dingxinliang88.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.constants.EmailConstant;
import io.github.dingxinliang88.exception.BizException;
import io.github.dingxinliang88.manager.JwtTokenManager;
import io.github.dingxinliang88.mapper.BandMapper;
import io.github.dingxinliang88.mapper.FanMapper;
import io.github.dingxinliang88.mapper.MemberMapper;
import io.github.dingxinliang88.mapper.UserMapper;
import io.github.dingxinliang88.pojo.dto.JwtToken;
import io.github.dingxinliang88.pojo.dto.fan.EditFanReq;
import io.github.dingxinliang88.pojo.dto.member.EditMemberReq;
import io.github.dingxinliang88.pojo.dto.user.*;
import io.github.dingxinliang88.pojo.enums.UserRoleType;
import io.github.dingxinliang88.pojo.po.Band;
import io.github.dingxinliang88.pojo.po.Fan;
import io.github.dingxinliang88.pojo.po.Member;
import io.github.dingxinliang88.pojo.po.User;
import io.github.dingxinliang88.pojo.vo.fan.FanInfoVO;
import io.github.dingxinliang88.pojo.vo.member.MemberInfoVO;
import io.github.dingxinliang88.pojo.vo.user.UserAuthType;
import io.github.dingxinliang88.pojo.vo.user.UserInfoVO;
import io.github.dingxinliang88.pojo.vo.user.UserLoginVO;
import io.github.dingxinliang88.service.UserService;
import io.github.dingxinliang88.utils.RedisUtil;
import io.github.dingxinliang88.utils.SysUtil;
import io.github.dingxinliang88.utils.ThrowUtil;
import io.github.dingxinliang88.utils.UserHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static io.github.dingxinliang88.constants.UserConstant.CODE_LOGIN;
import static io.github.dingxinliang88.constants.UserConstant.USER_AUTH_TYPE_PREFIX;

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
    private BandMapper bandMapper;

    @Resource
    private MemberMapper memberMapper;

    @Resource
    private FanMapper fanMapper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private JwtTokenManager jwtTokenManager;

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
    public Integer userEmailRegister(EmailRegisterReq req, HttpServletRequest request) {
        String email = req.getEmail();
        String code = req.getCode();
        String password = req.getPassword();
        String checkedPassword = req.getCheckedPassword();
        Integer type = req.getType();

        ThrowUtil.throwIf(!password.equals(checkedPassword), StatusCode.PASSWORD_NOT_MATCH);

        // 判断当前账号是否已经被注册
        User userFromDb = userMapper.queryByEmail(email);
        ThrowUtil.throwIf(userFromDb != null, StatusCode.ACCOUNT_ALREADY_EXISTS);

        return transactionTemplate.execute(status -> {
            try {
                // 加锁 + 判断当前账号是否已经被注册
                User user;
                synchronized (email.intern()) {
                    User u = userMapper.queryByEmail(email);
                    ThrowUtil.throwIf(u != null, StatusCode.ACCOUNT_ALREADY_EXISTS);

                    // 获取服务器中的 code
                    String serverCode = (String) redisUtil.get(EmailConstant.CAPTCHA_KEY + email);
                    assert serverCode != null;
                    ThrowUtil.throwIf(!serverCode.equals(code), StatusCode.CODE_NOT_MATCH);

                    // 加密
                    String salt = SysUtil.genUserPwdSalt();
                    String encryptPwd = SysUtil.encryptedPwd(salt, password);

                    // 保存数据
                    user = new User(type, encryptPwd, salt);
                    user.setEmail(email);
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
    public String userAccLogin(AccLoginReq req, HttpServletRequest request) {
        String account = req.getAccount();
        String password = req.getPassword();

        // 使用账号字符串作为锁对象，确保同一账号的操作是原子的
        synchronized (account.intern()) {
            User user = userMapper.queryByAccount(account);

            ThrowUtil.throwIf(user == null, StatusCode.NOT_FOUND_ERROR, "账号不存在！");
            // 校验密码
            String encryptPwd = SysUtil.encryptedPwd(user.getSalt(), password);
            String encryptPwdFromDb = user.getPassword();
            ThrowUtil.throwIf(!encryptPwdFromDb.equals(encryptPwd), StatusCode.PASSWORD_NOT_MATCH, "密码错误！");

            UserLoginVO userLoginVO = UserLoginVO.builder()
                    .userId(user.getUserId())
                    .nickname(user.getNickname())
                    .type(user.getType())
                    .build();

            UserHolder.setUser(userLoginVO);

            // 保存用户登陆态
            String token = jwtTokenManager.genAccessToken(userLoginVO);
            String refreshToken = jwtTokenManager.genRefreshToken(userLoginVO);
            JwtToken jwtToken = new JwtToken(token, refreshToken);
            jwtTokenManager.save2Redis(jwtToken, userLoginVO);

            return token;
        }
    }

    @Override
    @Transactional(rollbackFor = BizException.class)
    public String userEmailLogin(EmailLoginReq req, HttpServletRequest request) {
        String email = req.getEmail();
        Integer loginType = req.getLoginType();

        synchronized (email.intern()) {
            ThrowUtil.throwIf(SysUtil.getCurrUser() != null, StatusCode.BAD_REQUEST, "已经登录！");
            UserLoginVO userLoginVO;
            if (CODE_LOGIN.equals(loginType)) {
                userLoginVO = handleEmailCodeLogin(req);
            } else {
                userLoginVO = handleEmailPwdLogin(req);
            }

            UserHolder.setUser(userLoginVO);

            // 保存用户登陆态
            String token = jwtTokenManager.genAccessToken(userLoginVO);
            String refreshToken = jwtTokenManager.genRefreshToken(userLoginVO);
            JwtToken jwtToken = new JwtToken(token, refreshToken);
            jwtTokenManager.save2Redis(jwtToken, userLoginVO);

            // 删除code
            redisUtil.delete(EmailConstant.CAPTCHA_KEY + email);

            return token;
        }
    }

    @Override
    public UserInfoVO getCurrUser(HttpServletRequest request) {
        UserLoginVO loginVO = SysUtil.getCurrUser();
        UserInfoVO userInfoVO = userMapper.queryUserInfoByUserId(loginVO.getUserId());

        Integer type = userInfoVO.getType();
        // 乐队成员信息
        if (UserRoleType.MEMBER.getType().equals(type)) {
            MemberInfoVO memberInfoVO = memberMapper.queryMemberInfoByMemberId(loginVO.getUserId());
            userInfoVO.setMemberInfoVO(memberInfoVO);
        } else if (UserRoleType.FAN.getType().equals(type)) {
            FanInfoVO fanInfoVO = fanMapper.queryFanInfoByFanId(loginVO.getUserId());
            userInfoVO.setFanInfoVO(fanInfoVO);
        }
        return userInfoVO;
    }

    @Override
    public Boolean userLogout(HttpServletRequest request) {
        jwtTokenManager.revokeToken(SysUtil.getCurrUser());
        UserHolder.removeUser();
        return Boolean.TRUE;
    }

    @Override
    public UserAuthType getUserAuthType(HttpServletRequest request) {
        UserLoginVO currUser = SysUtil.getCurrUser();
        Integer userId = currUser.getUserId();
        // 查询缓存信息
        String authKey = USER_AUTH_TYPE_PREFIX + userId;
        Object authJsonObj = redisUtil.get(authKey);
        if (authJsonObj != null) {
            return JSONUtil.toBean(authJsonObj.toString(), UserAuthType.class);
        }
        UserAuthType userAuthType = new UserAuthType();
        if (UserRoleType.ADMIN.getType().equals(currUser.getType())) {
            userAuthType.setIsAdmin(Boolean.TRUE);
        } else if (UserRoleType.MEMBER.getType().equals(currUser.getType())) {
            Band band = bandMapper.queryByLeaderId(userId, true);
            userAuthType.setIsLeader(band != null);
        } else if (UserRoleType.FAN.getType().equals(currUser.getType())) {
            userAuthType.setIsFan(Boolean.TRUE);
        }
        // 添加权限缓存
        redisUtil.setExpiredDays(authKey, JSONUtil.toJsonStr(userAuthType), 15);
        return userAuthType;
    }

    @Override
    public Boolean editUserInfo(EditUserReq req, HttpServletRequest request) {
        Integer userId = req.getUserId();
        String nickname = req.getNickname();
        return transactionTemplate.execute(status -> {
            try {
                Boolean updateRes = userMapper.updateNickNameByUserId(userId, nickname);
                if (UserRoleType.MEMBER.getType().equals(req.getType())) {
                    EditMemberReq editMemberReq = req.getEditMemberReq();
                    updateRes = memberMapper.updateInfoByMemberId(editMemberReq);
                } else if (UserRoleType.FAN.getType().equals(req.getType())) {
                    EditFanReq editFanReq = req.getEditFanReq();
                    updateRes = fanMapper.updateInfoByFanId(editFanReq);
                }
                return updateRes;
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
        });
    }

    @Override
    public Boolean bindEmail(BindEmailReq req, HttpServletRequest request) {
        String email = req.getEmail();
        String code = req.getCode();

        synchronized (email.intern()) {
            // 获取服务器中的 code
            String serverCode = (String) redisUtil.get(EmailConstant.CAPTCHA_KEY + email);
            assert serverCode != null;
            ThrowUtil.throwIf(!serverCode.equals(code), StatusCode.CODE_NOT_MATCH);
            return userMapper.updateEmailByUserId(SysUtil.getCurrUser().getUserId(), email);
        }

    }


    private UserLoginVO handleEmailPwdLogin(EmailLoginReq req) {
        String email = req.getEmail();
        String password = req.getPassword();

        User userInfo;
        synchronized (email.intern()) {
            userInfo = userMapper.queryByEmail(email);
            String salt = userInfo.getSalt();
            String encryptedPwd = SysUtil.encryptedPwd(salt, password);
            ThrowUtil.throwIf(!userInfo.getPassword().equals(encryptedPwd), StatusCode.PASSWORD_NOT_MATCH);
        }

        return UserLoginVO.builder()
                .userId(userInfo.getUserId())
                .nickname(userInfo.getNickname())
                .type(userInfo.getType())
                .build();
    }

    private UserLoginVO handleEmailCodeLogin(EmailLoginReq req) {
        String email = req.getEmail();
        String code = req.getCode();

        ThrowUtil.throwIf(code == null, StatusCode.PARAMS_ERROR, "验证码不能为空");

        User userInfo;
        // 加锁
        synchronized (email.intern()) {
            // 根据用户的邮箱号查询用户是否存在，不存在即注册，存在即更新
            userInfo = userMapper.queryByEmail(email);
            ThrowUtil.throwIf(userInfo == null, StatusCode.NOT_FOUND_ERROR, "用户不存在，请先注册");
            // 获取服务器中的 code
            String serverCode = (String) redisUtil.get(EmailConstant.CAPTCHA_KEY + email);
            assert serverCode != null;
            ThrowUtil.throwIf(!serverCode.equals(code), StatusCode.CODE_NOT_MATCH);
        }

        return UserLoginVO.builder()
                .userId(userInfo.getUserId())
                .nickname(userInfo.getNickname())
                .type(userInfo.getType())
                .build();
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
