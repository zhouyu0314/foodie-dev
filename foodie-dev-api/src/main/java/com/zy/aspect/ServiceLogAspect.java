package com.zy.aspect;

import com.zy.controller.HelloController;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLogAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceLogAspect.class);

    /**
     * AOP通知：
     * 1.前置通知：在方法调用前执行
     * 2.后置通知：在方法正常调用之后执行
     * 3.环绕通知：在方法调用之前和之后，都分别可以执行的通知
     * 4.异常通知：如果在方法调用过程中发生异常，则通知
     * 5.最终通知：在方法调用之后执行
     */

    public Object recordTimeLog(ProceedingJoinPoint joinPoint) {
        LOGGER.info("===== 开始执行 {}.{} =====",
                joinPoint.getTarget().getClass(),
                joinPoint.getSignature().getName());
    }
}
