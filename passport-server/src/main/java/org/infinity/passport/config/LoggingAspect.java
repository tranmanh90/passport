package org.infinity.passport.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

/**
 * Aspect for logging execution of service and controller Spring components.
 * Spring AOP中Aspect的高级用法
 * https://www.toutiao.com/i6603273843035341316/
 */
@Aspect
@Profile("!" + ApplicationConstants.SPRING_PROFILE_NO_AOP_LOGGING)
@Configuration
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("within(" + ApplicationConstants.BASE_PACKAGE + ".service..*) || within(" + ApplicationConstants.BASE_PACKAGE + ".controller..*)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }
        try {
            Object result = joinPoint.proceed();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), result);
            }
            return result;
        } catch (IllegalArgumentException e) {
            LOGGER.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }
    }
}
