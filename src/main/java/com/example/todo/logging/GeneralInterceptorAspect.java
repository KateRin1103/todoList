package com.example.todo.logging;

import com.example.todo.entity.Note;
import com.example.todo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.kafka.annotation.KafkaListener;
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
        if (object != null)
            log.info("After method " + joinPoint.getSignature().toShortString() +
                    " invoked: " + object.toString());
        return object;
    }

    @KafkaListener(topics="msg", groupId = "msg_group_id")
    public void orderListenerUser(ConsumerRecord<Long, Object> record){
        log.info("Sent =" + String.valueOf(record.value()));
        log.info("-> with key=" + String.valueOf(record.key()));
        log.info("-> partition=" + String.valueOf(record.partition()));
    }
}
