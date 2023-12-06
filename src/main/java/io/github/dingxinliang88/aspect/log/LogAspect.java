package io.github.dingxinliang88.aspect.log;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * 使用AOP记录日志
 *
 * @author <a href="https://github.com/dingxinliang88">codejuzi</a>
 */
@Aspect
@Component
public class LogAspect {

    private final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(* io.github.dingxinliang88.controller.*.*(..))")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object doLog(ProceedingJoinPoint pjp) throws Throwable {
        // 开始计时
        StopWatch stopWatch = new StopWatch("log aspect");
        stopWatch.start("log aspect [" + pjp.getSignature().getName() + "]");

        // 获取请求路径
        RequestAttributes reqAttr = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest req = ((ServletRequestAttributes) reqAttr).getRequest();
        String reqUri = req.getRequestURI();

        // 生成唯一的请求ID
        String reqId = UUID.randomUUID().toString();

        // 获取请求参数
        Object[] args = pjp.getArgs();
        String reqParams = String.format("[%s]", StringUtils.join(args, ","));

        // 获取请求IP
        String remoteHost = req.getRemoteAddr();

        // 输出请求日志
        logger.info("=========> request start, id: {}, path: {}, ip: {}, params: {}",
                reqId, reqUri, remoteHost, reqParams);

        // 执行原方法
        Object result = pjp.proceed(args);

        // 停止计时
        stopWatch.stop();
        // 输出响应日志
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        logger.info("<======== request stop, id: {}, cost: {}ms", reqId, totalTimeMillis);

        // 返回执行结果
        return result;
    }
}
