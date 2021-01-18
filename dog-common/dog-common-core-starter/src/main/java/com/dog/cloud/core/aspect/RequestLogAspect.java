package com.dog.cloud.core.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求日志
 *
 * @author KING
 */
@Aspect
@Component
@Slf4j
public class RequestLogAspect {

    /**
     * 申明一个切点 里面是 execution表达式
     * 指定哪些需要拦截 哪些不需要拦截
     */
    @Pointcut("execution(* com.dog.cloud..*.*controller.*Controller.*(..))")
    private void requestLogPointCut() {
    }

    /**
     * 请求method前打印内容
     *
     * @return
     */
    @Around(value = "requestLogPointCut()")
    public Object methodBefore(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes requestAttributes =
            (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String url = request.getRequestURL().toString();
        // 打印请求内容
        log.info("===============请求内容 start===============");
        log.info("请求地址：{}", url);
        log.info("请求方式: {}", request.getMethod());
        log.info("请求类方法：{}", joinPoint.getSignature());
        log.info("===============请求内容 end===============");
        return joinPoint.proceed();
    }
}