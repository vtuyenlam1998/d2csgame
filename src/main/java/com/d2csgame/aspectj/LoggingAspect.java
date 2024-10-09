package com.d2csgame.aspectj;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Around("execution(* com.d2csgame.server..*Controller.*(..))")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethod(joinPoint, "Controller");
    }

    @Around("execution(* com.d2csgame.server..*Repository.*(..))")
    public Object logRepositoryMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethod(joinPoint, "Repository");
    }

    @Around("execution(* com.d2csgame.server..service.impl.*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethod(joinPoint, "Service");
    }

    @Around("execution(* com.d2csgame..*(..))"
            + " && !execution(* com.d2csgame..*Repository.*(..))"
            + " && !execution(* com.d2csgame..*Service.*(..))"
            + " && !execution(* com.d2csgame.aspectj.LoggingAspect.*(..))"
            + " && !execution(* com.d2csgame.security.*(..))"
            + " && !execution(* com.d2csgame..*Controller.*(..))")
    public Object logClassAndMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        // Lấy tên class
        String className = joinPoint.getSignature().getDeclaringTypeName();

        // Lấy tên method
        String methodName = joinPoint.getSignature().getName();

        // Log thông tin
        log.info("Class: {}, Method: {}", className, methodName);

        // Tiếp tục thực thi method
        return joinPoint.proceed();
    }

    // Phương thức log dùng chung cho các pointcut
    private Object logMethod(ProceedingJoinPoint joinPoint, String layer) throws Throwable {
        // Lấy tên class và method
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();

        // Lấy các tham số đầu vào
        Object[] args = joinPoint.getArgs();
        log.info("[{}] Entering method: {}.{}() with arguments = {}", layer, className, methodName, Arrays.toString(args));

        // Thực thi phương thức
        Object result = joinPoint.proceed();

        // Log kết quả trả về
        log.info("[{}] Method {}.{}() executed and returned: {}", layer, className, methodName, result);

        return result;
    }
}
