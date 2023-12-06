package io.github.dingxinliang88.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.constants.EmailConstant;
import io.github.dingxinliang88.mapper.UserMapper;
import io.github.dingxinliang88.pojo.dto.user.AccRegisterReq;
import io.github.dingxinliang88.pojo.dto.user.EmailLoginReq;
import io.github.dingxinliang88.pojo.po.User;
import io.github.dingxinliang88.service.UserService;
import io.github.dingxinliang88.utils.SysUtil;
import io.github.dingxinliang88.utils.ThrowUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

        // 加密
        String salt = SysUtil.genUserPwdSalt();
        String encryptPwd = SysUtil.encryptedPwd(salt, password);

        // 保存数据
        User user = new User(account, encryptPwd, salt, type);
        userMapper.insert(user);

        return user.getUserId();
    }

    @Override
    public Integer emailRegisterLogin(EmailLoginReq req, HttpServletRequest request) {
        String email = req.getEmail();
        String code = req.getCode();
        Integer type = req.getType();

        // 获取服务器中的 code
        String serverCode = (String) redisTemplate.opsForValue().get(EmailConstant.CAPTCHA_KEY + email);

        assert serverCode != null;
        ThrowUtil.throwIf(!serverCode.equals(code), StatusCode.CODE_NOT_MATCH);

        // 根据用户的邮箱号查询用户是否存在，不存在即注册，存在即更新
        // TODO 根据用户类别去插入到不同的表中（fan、member）
        // 将code作为盐值和密码存入即可

        // 保存用户Session

        return null;
    }


}
