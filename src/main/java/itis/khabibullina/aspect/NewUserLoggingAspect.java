package itis.khabibullina.aspect;

import itis.khabibullina.dto.CreateUserRequestDto;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class NewUserLoggingAspect {

    public static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class.getName());

    @Pointcut("@annotation(itis.khabibullina.aspect.annotation.NewUserLogging)")
    public void logUserCreation() {

    }

    @Before("logUserCreation()")
    public void log(JoinPoint joinPoint) {
        if (joinPoint.getArgs().length < 1) {
            LOGGER.info("Args length is 0");
            return;
        }

        Object arg = joinPoint.getArgs()[0];
        if (arg == null) {
            LOGGER.info("CreateUserRequestDto is null");
            return;
        }

        CreateUserRequestDto createUserRequestDto = (CreateUserRequestDto) arg;
        if (isValidUser(createUserRequestDto)) {
            LOGGER.info("CreateUserRequestDto with email {} is correct", createUserRequestDto.getEmail());
        } else {
            LOGGER.info("CreateUserRequestDto with email {} is wrong", createUserRequestDto.getEmail());

        }
    }

    private boolean isValidUser(CreateUserRequestDto createUserRequestDto) {
        String name = createUserRequestDto.getName();
        String email = createUserRequestDto.getEmail();
        String password = createUserRequestDto.getPassword();
        return !name.isBlank() && !email.isBlank() && !password.isBlank();
    }
}
