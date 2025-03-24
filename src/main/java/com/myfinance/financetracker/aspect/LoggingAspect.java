package com.myfinance.financetracker.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // Логирование перед выполнением метода
    @Before("execution(* com.myfinance.financetracker.controller.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        if (logger.isInfoEnabled()) { // Проверяем, включен ли уровень INFO
            logger.info("Executing method: {}", joinPoint.getSignature().toShortString());
        }
    }

    // Логирование после успешного выполнения метода
    @AfterReturning(pointcut = "execution(* com.myfinance.financetracker.controller.*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        if (logger.isInfoEnabled()) { // Проверяем, включен ли уровень INFO
            logger.info("Method {} executed successfully. Result: {}", joinPoint.getSignature().toShortString(), result);
        }
    }

    // Логирование ошибок
    @AfterThrowing(pointcut = "execution(* com.myfinance.financetracker.controller.*.*(..))", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        if (logger.isErrorEnabled()) { // Проверяем, включен ли уровень ERROR
            logger.error("Error in method: {}. Error: {}", joinPoint.getSignature().toShortString(), error.getMessage());
        }
    }
}