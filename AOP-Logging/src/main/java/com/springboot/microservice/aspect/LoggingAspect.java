package com.springboot.microservice.aspect;

import java.util.Arrays;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import com.splunk.Receiver;
import com.splunk.Service;

@Aspect
@Component
public class LoggingAspect {

	Map<String, Object> conn = SplunkConnection.getService();

	Service service;

	Receiver rec;

	/**
	 * Pointcut that matches all repositories, services and Web REST endpoints.
	 */
	@Pointcut("within(@org.springframework.stereotype.Repository *)"
			+ " || within(@org.springframework.stereotype.Service *)"
			+ " || within(@org.springframework.web.bind.annotation.RestController *)")
	public void springBeanPointcut() {
		// Method is empty as this is just a Pointcut, the implementations are in the
		// advices.
	}

	/**
	 * Pointcut that matches all Spring beans in the application's main packages.
	 */
	@Pointcut("within(com.springboot.microservice..*)" + " || within(com.springboot.microservice.service..*)"
			+ " || within(com.springboot.microservice.controller..*)")
	public void applicationPackagePointcut() {
		// Method is empty as this is just a Pointcut, the implementations are in the
		// advices.
	}

	@Before("applicationPackagePointcut() && springBeanPointcut()")
	public void beforeAdviceMethods(JoinPoint joinPoint) {
		service = Service.connect(conn);
		rec = service.getReceiver();
		String pacakage = "Before method:" + joinPoint.getSignature().getDeclaringTypeName();
		String method = joinPoint.getSignature().getName();
		String args = Arrays.toString(joinPoint.getArgs());
		rec.log("main", pacakage + " " + method + " " + args);
	}

	@After("applicationPackagePointcut() && springBeanPointcut()")
	public void afterAdviceMethods(JoinPoint joinPoint) {
		service = Service.connect(conn);
		rec = service.getReceiver();
		String pacakage = "After method:" + joinPoint.getSignature().getDeclaringTypeName();
		String method = joinPoint.getSignature().getName();
		// String result = (String) joinPoint.proceed();
		rec.log("main", pacakage + " " + method);
	}

}