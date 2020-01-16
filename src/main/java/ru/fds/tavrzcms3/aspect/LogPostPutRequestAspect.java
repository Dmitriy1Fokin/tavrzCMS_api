package ru.fds.tavrzcms3.aspect;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogPostPutRequestAspect {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping) && args(user,..)")
    public void methodsWithPost(User user) { }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping) && args(user,..)")
    public void methodsWithPut(User user) { }

    @After("methodsWithPost(user)")
    public void afterCallPost(JoinPoint joinPoint, User user){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("User: ").append(user.getUsername()).append(". ")
                .append("Authorities: ").append(user.getAuthorities()).append(". ")
                .append("Method: ").append(joinPoint.toLongString()).append(". ")
                .append("args: [ ");
        for(Object o : joinPoint.getArgs()){
            stringBuilder.append("[ ").append(o.toString()).append(" ]");
        }
        stringBuilder.append(" ]");

        log.info(stringBuilder.toString());
    }

    @After("methodsWithPut(user)")
    public void afterCallPut(JoinPoint joinPoint, User user){
        afterCallPost(joinPoint, user);
    }
}
