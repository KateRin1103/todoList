package com.example.todo.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class GeneralInterceptorAspect {

    @Pointcut("execution(* com.example.todo.*.*.*(..))")
    public void loggingPointCut() {
    }

    @Around("loggingPointCut()")
    public Object before(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Before method invoked::" + joinPoint.getSignature());
        Object object = joinPoint.proceed(joinPoint.getArgs());
        log.info("After method " + joinPoint.getSignature().toShortString() + " invoked: " + object.toString());
        return object;
    }
}
