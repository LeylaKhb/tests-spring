package itis.khabibullina.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;


@Aspect
@Component
public class LoggingAspect {

    public static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class.getName());

    @Pointcut("@annotation(itis.khabibullina.aspect.annotation.Loggable)")
    public void logExecutionTime() {

    }

    @Around("logExecutionTime()")
    public Object log(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        String className = methodSignature.getDeclaringType().getName();
        String methodName = methodSignature.getName();

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        stopWatch.stop();

        LOGGER.info("Execution time of {}.{} = {} ms", className, methodName, stopWatch.getTotalTimeMillis());

        return result;
    }


}

