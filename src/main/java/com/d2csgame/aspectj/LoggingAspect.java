package com.d2csgame.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.d2csgame.server..*Controller.*(..))")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethod(joinPoint, "Controller");
    }

    @Around("execution(* com.d2csgame.server..*Repository.*(..))")
    public Object logRepositoryMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethod(joinPoint, "Repository");
    }

    @Around("execution(* com.d2csgame.server..service.impl.*.*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethod(joinPoint, "Service");
    }

    @Around("execution(* com.d2csgame..*.*(..))"
            + " && !execution(* com.d2csgame..*Repository.*(..))"
            + " && !execution(* com.d2csgame..*Service.*(..))"
            + " && !execution(* com.d2csgame.aspectj.LoggingAspect.*(..))"
            + " && !execution(* com.d2csgame.security..*(..))"
            + " && !execution(* com.d2csgame..*Controller.*(..))")
    public Object logClassAndMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();

        String methodName = joinPoint.getSignature().getName();

        log.info("Class: {}, Method: {}", className, methodName);

        return joinPoint.proceed();
    }

    private Object logMethod(ProceedingJoinPoint joinPoint, String layer) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();

        Object[] args = joinPoint.getArgs();
        log.info("[{}] Entering method: {}.{}() with arguments = {}", layer, className, methodName, Arrays.toString(args));

        Object result = joinPoint.proceed();

        log.info("[{}] Method {}.{}() executed and returned: {}", layer, className, methodName, result);

        return result;
    }

    @Around("execution(* com.d2csgame..*.*(..)) && !within(com.d2csgame.aspectj..*) && !within(com.d2csgame.security..*)")
    public Object traceErrorMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Throwable t) {
            String methodName = joinPoint.getSignature().getName();
            String className = joinPoint.getTarget().getClass().getSimpleName();

            log.error("Exception in {}.{}() with cause = {} and message = {}",
                    className, methodName, (t.getCause() != null ? t.getCause() : "NULL"), t.getMessage());
            throw t;
        }
    }
}
