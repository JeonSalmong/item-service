package apps.itemservice.config.aop;

import apps.itemservice.config.aop.trace.LogTrace;
import apps.itemservice.config.aop.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class LogTraceAop {
    private final LogTrace logTrace;
    public LogTraceAop(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    @Around("execution(* apps.itemservice.web..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceStatus status = null;
         log.info("target={}", joinPoint.getTarget()); //실제 호출 대상
         log.info("getArgs={}", joinPoint.getArgs()); //전달인자
         log.info("getSignature={}", joinPoint.getSignature()); //join point 시그니처
        try {
            String message = joinPoint.getSignature().toShortString();
            status = logTrace.begin(message);
            //로직 호출
            Object result = joinPoint.proceed();
            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
