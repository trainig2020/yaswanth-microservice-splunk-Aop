package com.springboot.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass=true)  
public class AopLoggingApplication {

	public static void main(String[] args) {
		SpringApplication.run(AopLoggingApplication.class, args);
	}

}
