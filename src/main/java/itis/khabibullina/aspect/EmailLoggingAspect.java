package itis.khabibullina.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class EmailLoggingAspect {

    public static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class.getName());

    @Pointcut("@annotation(itis.khabibullina.aspect.annotation.Loggable)")
    public void logEmailSending() {

    }

    @Around("logEmailSending()")
    public Object log(ProceedingJoinPoint joinPoint) {

        Object result;
        String to = (String) joinPoint.getArgs()[0];
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        LOGGER.info("Sent email to {} with temporal password {}", to, result);
        return result;
    }
}
