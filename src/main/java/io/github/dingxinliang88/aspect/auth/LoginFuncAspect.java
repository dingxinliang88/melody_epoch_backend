package io.github.dingxinliang88.aspect.auth;

import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.pojo.vo.UserLoginVO;
import io.github.dingxinliang88.utils.SysUtil;
import io.github.dingxinliang88.utils.ThrowUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

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
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 获取当前登录用户
        UserLoginVO currUser = SysUtil.getCurrUser(request);

        ThrowUtil.throwIf(currUser == null, StatusCode.NOT_LOGIN_ERROR, "未登录");

        // return pjp.proceed();
    }
}
