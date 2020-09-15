package com.springboot.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

import com.splunk.Args;
import com.splunk.HttpService;
import com.splunk.Receiver;
import com.splunk.SSLSecurityProtocol;
import com.splunk.Service;
import com.splunk.ServiceArgs;

@SpringBootApplication
@EnableEurekaClient
//@EnableHystrix
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
		
		HttpService.setSslSecurityProtocol(SSLSecurityProtocol.TLSv1_2);
        // Create a map of arguments and add login parameters
        ServiceArgs loginArgs = new ServiceArgs();
        loginArgs.setUsername("yaswanth3898");
        loginArgs.setPassword("peace@3898");
        loginArgs.setHost("LAPTOP-JMMCFRFU");
        loginArgs.setPort(8089);
        
        // Create a Service instance and log in with the argument map
        Service service = Service.connect(loginArgs);

        
        Receiver receiver=service.getReceiver();
        Args logArgs=new Args();
        logArgs.put("sourcetype", "gateway");
        receiver.log("main", logArgs, "Gateway service has been started.");
	}

}
