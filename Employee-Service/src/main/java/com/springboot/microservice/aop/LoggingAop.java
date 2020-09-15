//package com.springboot.microservice.aspect;
//
//import java.util.Arrays;
//import java.util.Map;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import com.splunk.Receiver;
//import com.splunk.Service;
//import com.springboot.microservice.controller.SplunkConnection;
//
//@Aspect
//@Component
//public class LoggingAspect {
//	
//	
//	 Map<String,Object> conn = SplunkConnection.getService();
//	
//	 Service service;
//	 
//	 Receiver rec;
//
//    private final Logger log = LoggerFactory.getLogger(this.getClass());
//
//    /**
//     * Pointcut that matches all repositories, services and Web REST endpoints.
//     */
//    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
//        " || within(@org.springframework.stereotype.Service *)" +
//        " || within(@org.springframework.web.bind.annotation.RestController *)")
//    public void springBeanPointcut() {
//        // Method is empty as this is just a Pointcut, the implementations are in the advices.
//    }
//
//    /**
//     * Pointcut that matches all Spring beans in the application's main packages.
//     */
//    @Pointcut("within(com.springboot.microservice..*)" +
//        " || within(com.springboot.microservice.service..*)" +
//        " || within(com.springboot.microservice.controller..*)")
//    public void applicationPackagePointcut() {
//        // Method is empty as this is just a Pointcut, the implementations are in the advices.
//    }
//
//    /**
//     * Advice that logs methods throwing exceptions.
//     *
//     * @param joinPoint join point for advice
//     * @param e exception
//     */
//    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
//    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
//        log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
//            joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL");
//        
//    }
//
//    /**
//     * Advice that logs when a method is entered and exited.
//     *
//     * @param joinPoint join point for advice
//     * @return result
//     * @throws Throwable throws IllegalArgumentException
//     */
//    @Around("applicationPackagePointcut() && springBeanPointcut()")
//    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
//        if (log.isDebugEnabled()) {
//            log.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
//                joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));    
//        }
//        try {
//            Object result = joinPoint.proceed();
//            if (log.isDebugEnabled()) {
//                log.debug("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
//                    joinPoint.getSignature().getName(), result);
//            }
//            return result;
//        } catch (IllegalArgumentException e) {
//            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
//                joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
//            throw e;
//        }
//    }
//}



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
    
//    @Before("applicationPackagePointcut() && springBeanPointcut()")
//    public void beforeAdviceMethods(JoinPoint joinPoint) {
//    	service = Service.connect(conn);
//		rec = service.getReceiver();
//		String pacakage = "Before method:" + joinPoint.getSignature().getDeclaringTypeName();
//		String method = joinPoint.getSignature().getName();
//		String args = Arrays.toString(joinPoint.getArgs());
//		rec.log("main", pacakage+method+args);
//    }
    
//  @After("applicationPackagePointcut() && springBeanPointcut()")
//  public void afterAdviceMethods(JoinPoint joinPoint) {
//  	service = Service.connect(conn);
//		rec = service.getReceiver();
//		String pacakage = "After method:" + joinPoint.getSignature().getDeclaringTypeName();
//		String method = joinPoint.getSignature().getName();
//		//String result = (String) joinPoint.proceed();
//		rec.log("main", pacakage+method);
//  }
    
    LoggingAspect log = new LoggingAspect();
    
    @Before("applicationPackagePointcut() && springBeanPointcut()")
    public void beforeAdviceMethod(JoinPoint joinPoint) {
    	log.beforeAdviceMethods(joinPoint);
    }
   
    @After("applicationPackagePointcut() && springBeanPointcut()")
    public void afterAdviceMethod(JoinPoint joinPoint) {
    	log.afterAdviceMethods(joinPoint);
    }
    
}






