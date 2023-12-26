package io.github.dingxinliang88.aspect.auth;

import cn.hutool.core.util.StrUtil;
import io.github.dingxinliang88.biz.StatusCode;
import io.github.dingxinliang88.exception.BizException;
import io.github.dingxinliang88.pojo.enums.UserRoleType;
import io.github.dingxinliang88.pojo.vo.user.UserLoginVO;
import io.github.dingxinliang88.utils.SysUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 权限校验AOP
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Aspect
@Component
public class AuthCheckAspect {

    @Around("@annotation(authCheck)")
    public Object doAuthCheck(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String role = authCheck.role();

        if (StrUtil.isEmpty(role)) {
            throw new BizException(StatusCode.NO_AUTH_ERROR, "权限错误！");
        }

        if (SysUtil.isBanned()) {
            throw new BizException(StatusCode.NO_AUTH_ERROR, "账号异常！");
        }

        UserRoleType roleType = UserRoleType.getByDesc(role);
        UserLoginVO currUser = SysUtil.getCurrUser();
        if (!roleType.getType().equals(currUser.getType())) {
            throw new BizException(StatusCode.NO_AUTH_ERROR, "权限错误！");
        }
        return joinPoint.proceed();
    }
}
