package com.springboot.microservice.aop;


import java.util.Map;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import com.splunk.Receiver;
import com.splunk.Service;
import com.springboot.microservice.aspect.LoggingAspect;
import com.springboot.microservice.aspect.SplunkConnection;

@Aspect
@Component
public class LoggingAop {
	
	
	 Map<String,Object> conn = SplunkConnection.getService();
	
	 Service service;
	 
	 Receiver rec;
	 
	 LoggingAspect log = new LoggingAspect();
	 
    /**
     * Pointcut that matches all repositories, services and Web REST endpoints.
     */
    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
        " || within(@org.springframework.stereotype.Service *)" +
        " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Pointcut that matches all Spring beans in the application's main packages.
     */
    @Pointcut("within(com.springboot.microservice..*)" +
        " || within(com.springboot.microservice.service..*)" +
        " || within(com.springboot.microservice.controller..*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    } 
        
    @Before("applicationPackagePointcut() && springBeanPointcut()")
    public void beforeAdviceMethod(JoinPoint joinPoint) {
    	log.beforeAdviceMethods(joinPoint);
    }
   
    @After("applicationPackagePointcut() && springBeanPointcut()")
    public void afterAdviceMethod(JoinPoint joinPoint) {
    	log.afterAdviceMethods(joinPoint);
    }
    
}


