package com.qing.community.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.SimpleDateFormat;
import java.util.Date;


@Component
@Aspect
public class ServiceLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    //定义切点
    //业务层所有方法
    @Pointcut("execution(* com.qing.community.service.*.*(..))")
    public void pointCut() {
    }

    //在业务层插入前置通知
    @Before("pointCut()")
    public void before(JoinPoint joinPoint) {
        // 用户[1.2.3.4],在[xxx],访问了[com.qing.community.service.xxx()].
        //获取当前request，通过request获取当前用户ip
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = request.getRemoteHost();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //获取连接点类名+方法名
        String target = joinPoint.getSignature().getDeclaringTypeName() +
                '.' + joinPoint.getSignature().getName();
        logger.info(String.format("用户[%s],在[%s],访问了[%s].", ip, time, target));

    }



}
