package ru.fds.tavrzcms3.aspect;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Log4j2
public class LogPostMappingAspect {

    @Pointcut("@annotation(ru.fds.tavrzcms3.annotation.LogModificationDB) && args(user,..)")
    public void methodsWithUser(User user) { }

    @After("methodsWithUser(user)")
    public void afterCall(JoinPoint joinPoint, User user){
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
}
