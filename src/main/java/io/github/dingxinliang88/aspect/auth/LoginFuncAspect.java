package io.github.dingxinliang88.aspect.auth;

import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.pojo.vo.UserLoginVO;
import io.github.dingxinliang88.utils.ThrowUtil;
import io.github.dingxinliang88.utils.UserHolder;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * 校验登录方法切面
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Aspect
@Component
public class LoginFuncAspect {

    @Before("@annotation(LoginFunc)")
    public void aroundAdvice() {
        UserLoginVO currUser = UserHolder.getUser();
        ThrowUtil.throwIf(currUser == null, StatusCode.NOT_LOGIN_ERROR, "未登录");
    }
}
