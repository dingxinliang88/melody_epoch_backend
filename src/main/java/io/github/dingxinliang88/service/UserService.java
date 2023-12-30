package io.github.dingxinliang88.service;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.constants.EmailConstant;
import io.github.dingxinliang88.exception.BizException;
import io.github.dingxinliang88.manager.JwtTokenManager;
import io.github.dingxinliang88.manager.query.SearchFacade;
import io.github.dingxinliang88.mapper.BandMapper;
import io.github.dingxinliang88.mapper.FanMapper;
import io.github.dingxinliang88.mapper.MemberMapper;
import io.github.dingxinliang88.mapper.UserMapper;
import io.github.dingxinliang88.pojo.dto.JwtToken;
import io.github.dingxinliang88.pojo.dto.QueryReq;
import io.github.dingxinliang88.pojo.dto.fan.EditFanReq;
import io.github.dingxinliang88.pojo.dto.member.EditMemberReq;
import io.github.dingxinliang88.pojo.dto.user.*;
import io.github.dingxinliang88.pojo.enums.EmailLoginType;
import io.github.dingxinliang88.pojo.enums.UserRoleType;
import io.github.dingxinliang88.pojo.po.Band;
import io.github.dingxinliang88.pojo.po.Fan;
import io.github.dingxinliang88.pojo.po.Member;
import io.github.dingxinliang88.pojo.po.User;
import io.github.dingxinliang88.pojo.vo.SearchVO;
import io.github.dingxinliang88.pojo.vo.fan.FanInfoVO;
import io.github.dingxinliang88.pojo.vo.member.MemberInfoVO;
import io.github.dingxinliang88.pojo.vo.user.UserAuthType;
import io.github.dingxinliang88.pojo.vo.user.UserInfoVO;
import io.github.dingxinliang88.pojo.vo.user.UserLoginVO;
import io.github.dingxinliang88.utils.RedisUtil;
import io.github.dingxinliang88.utils.SysUtil;
import io.github.dingxinliang88.utils.ThrowUtil;
import io.github.dingxinliang88.utils.UserHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

import java.util.List;
import java.util.stream.Collectors;

import static io.github.dingxinliang88.constants.UserConstant.USER_AUTH_TYPE_PREFIX;

/**
 * Default User Service Implementation
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

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
    private SearchFacade searchFacade;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private JwtTokenManager jwtTokenManager;

    /**
     * 账号密码注册
     *
     * @param req 注册信息封装体
     * @return user id
     */
    public Integer userAccRegister(AccRegisterReq req) {
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
                status.setRollbackOnly();
                throw e;
            }
        });

    }

    /**
     * 邮箱密码验证码注册
     *
     * @param req 注册信息封装体
     * @return user id
     */
    public Integer userEmailRegister(EmailRegisterReq req) {
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
                    user.setAccount(email);
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
                status.setRollbackOnly();
                throw e;
            }
        });
    }

    /**
     * 账号密码登录
     *
     * @param req 登录信息封装体
     * @return user info
     */
    public String userAccLogin(AccLoginReq req) {
        String account = req.getAccount();
        String password = req.getPassword();

        // 使用账号字符串作为锁对象，确保同一账号的操作是原子的
        synchronized (account.intern()) {
            User user = userMapper.queryByAccount(account);

            ThrowUtil.throwIf(user == null, StatusCode.NOT_FOUND_ERROR, "账号不存在！");
            ThrowUtil.throwIf(SysUtil.isBanned(user), StatusCode.NO_AUTH_ERROR, "账号异常！");
            // 校验密码
            ThrowUtil.throwIf(!SysUtil.checkPwd(user, password), StatusCode.PASSWORD_NOT_MATCH, "密码错误！");

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

    /**
     * 邮箱验证码登录
     *
     * @param req 登录信息封装体
     * @return user info
     */
    @Transactional(rollbackFor = BizException.class)
    public String userEmailLogin(EmailLoginReq req) {
        String email = req.getEmail();
        Integer loginType = req.getLoginType();

        try {
            synchronized (email.intern()) {
                ThrowUtil.throwIf(SysUtil.getCurrUser() != null, StatusCode.BAD_REQUEST, "已经登录！");
                UserLoginVO userLoginVO;
                if (EmailLoginType.CODE_LOGIN.getCode().equals(loginType)) {
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

                return token;
            }
        } finally {
            // 删除code
            redisUtil.delete(EmailConstant.CAPTCHA_KEY + email);
        }
    }

    /**
     * 获取当前登录对象
     *
     * @return current login user info
     */
    public UserInfoVO getCurrUser() {
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

    /**
     * 用户登出
     *
     * @return true - 登出成功
     */
    public Boolean userLogout() {
        jwtTokenManager.revokeToken(SysUtil.getCurrUser());
        UserHolder.removeUser();
        return Boolean.TRUE;
    }

    /**
     * 获取当前登录用户的角色（队长、乐迷、管理员）
     *
     * @return user type vo
     */
    public UserAuthType getUserAuthType() {
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

    /**
     * 修改用户信息
     *
     * @param req 修改用户请求
     * @return true - 修改成功
     */
    public Boolean editUserInfo(EditUserReq req) {
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

    /**
     * 用户绑定邮箱
     *
     * @param req 绑定邮箱请求
     * @return true - 绑定成功
     */
    public Boolean bindEmail(BindEmailReq req) {
        String email = req.getEmail();
        String code = req.getCode();

        try {
            synchronized (email.intern()) {
                // 获取服务器中的 code
                String serverCode = (String) redisUtil.get(EmailConstant.CAPTCHA_KEY + email);
                assert serverCode != null;
                ThrowUtil.throwIf(!serverCode.equals(code), StatusCode.CODE_NOT_MATCH);
                return userMapper.updateEmailByUserId(SysUtil.getCurrUser().getUserId(), email);
            }
        } finally {
            // 删除code
            redisUtil.delete(EmailConstant.CAPTCHA_KEY + email);
        }
    }

    // -----------------------------
    // admin functions
    // -----------------------------

    /**
     * 封禁用户
     *
     * @param req 封禁用户请求
     * @return true - 封禁成功
     */
    public Boolean banUser(BanUserReq req) {
        Integer userId = req.getUserId();
        User user = userMapper.queryByUserId(userId);
        ThrowUtil.throwIf(user == null, StatusCode.NOT_FOUND_ERROR, "查询无果！");

        Integer type = user.getType();
        ThrowUtil.throwIf(UserRoleType.ADMIN.getType().equals(type), StatusCode.NO_AUTH_ERROR, "权限错误！");

        ThrowUtil.throwIf(SysUtil.isBanned(user), StatusCode.DUPLICATE_DATA, "已被封禁！");

        Integer bannedType = SysUtil.genBannedType(type);
        Boolean res = userMapper.updateTypeByUserId(user.getUserId(), bannedType);

        // 强制下线用户
        jwtTokenManager.revokeToken(user);

        return res;
    }

    /**
     * 解禁用户
     *
     * @param req 封禁用户请求
     * @return true - 解禁成功
     */
    public Boolean unbanUser(BanUserReq req) {
        Integer userId = req.getUserId();
        User user = userMapper.queryByUserId(userId);
        ThrowUtil.throwIf(user == null, StatusCode.NOT_FOUND_ERROR, "查询无果！");

        Integer type = user.getType();
        ThrowUtil.throwIf(UserRoleType.ADMIN.getType().equals(type), StatusCode.NO_AUTH_ERROR, "权限错误！");

        ThrowUtil.throwIf(!SysUtil.isBanned(user), StatusCode.DUPLICATE_DATA, "未被封禁！");

        Integer unbannedType = SysUtil.genUnbannedType(type);
        return userMapper.updateTypeByUserId(user.getUserId(), unbannedType);
    }

    /**
     * 分页获取用户信息
     *
     * @param current 页码
     * @param size    每页数量
     * @return user info vo page
     */
    public Page<UserInfoVO> getUserInfoVOByPage(Integer current, Integer size) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getIsDelete, 0);
        Page<User> userPage = userMapper.selectPage(new Page<>(current, size), queryWrapper);
        return convertUserInfoVOPage(userPage);
    }

    /**
     * 用户搜索
     *
     * @param req 用户搜索条件
     * @return search vo
     */
    public SearchVO queryInfo(QueryReq req) {
        return searchFacade.doSearch(req);
    }


    // ------------------------------
    // private util functions
    // ------------------------------

    private UserLoginVO handleEmailPwdLogin(EmailLoginReq req) {
        String email = req.getEmail();
        String password = req.getPassword();

        User userInfo;
        synchronized (email.intern()) {
            userInfo = userMapper.queryByEmail(email);
            ThrowUtil.throwIf(SysUtil.isBanned(userInfo), StatusCode.NO_AUTH_ERROR, "当前账号已被封禁");
            ThrowUtil.throwIf(!SysUtil.checkPwd(userInfo, password), StatusCode.PASSWORD_NOT_MATCH);
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
        try {
            // 加锁
            synchronized (email.intern()) {
                // 根据用户的邮箱号查询用户是否存在，不存在即注册，存在即更新
                userInfo = userMapper.queryByEmail(email);
                ThrowUtil.throwIf(userInfo == null, StatusCode.NOT_FOUND_ERROR, "用户不存在，请先注册");
                ThrowUtil.throwIf(SysUtil.isBanned(userInfo), StatusCode.NO_AUTH_ERROR, "当前账号已被封禁");
                // 获取服务器中的 code
                String serverCode = (String) redisUtil.get(EmailConstant.CAPTCHA_KEY + email);
                assert serverCode != null;
                ThrowUtil.throwIf(!serverCode.equals(code), StatusCode.CODE_NOT_MATCH);
            }
        } finally {
            // 删除code
            redisUtil.delete(EmailConstant.CAPTCHA_KEY + email);
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

    private Page<UserInfoVO> convertUserInfoVOPage(Page<User> userPage) {
        Page<UserInfoVO> userInfoVOPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal(), userPage.searchCount());

        List<UserInfoVO> userInfoVOList = userPage.getRecords().stream().map(user -> {
            UserInfoVO userInfoVO = UserInfoVO.userToVO(user);
            Integer type = user.getType();
            // 设置类别
            if (SysUtil.isBanned(user)) {
                userInfoVO.setIsBanned(Boolean.TRUE);
                type = SysUtil.genUnbannedType(user.getType());
                userInfoVO.setType(type);
            }

            // 设置类别信息
            if (UserRoleType.MEMBER.getType().equals(type)) {
                MemberInfoVO memberInfoVO = memberMapper.queryMemberInfoByMemberId(user.getUserId());
                userInfoVO.setMemberInfoVO(memberInfoVO);
            } else if (UserRoleType.FAN.getType().equals(type)) {
                FanInfoVO fanInfoVO = fanMapper.queryFanInfoByFanId(user.getUserId());
                userInfoVO.setFanInfoVO(fanInfoVO);
            }
            return userInfoVO;
        }).collect(Collectors.toList());

        userInfoVOPage.setRecords(userInfoVOList);
        return userInfoVOPage;
    }


}
