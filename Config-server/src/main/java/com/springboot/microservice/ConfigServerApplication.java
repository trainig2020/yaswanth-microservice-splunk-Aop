package com.springboot.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

import com.splunk.Args;
import com.splunk.HttpService;
import com.splunk.Receiver;
import com.splunk.SSLSecurityProtocol;
import com.splunk.Service;
import com.splunk.ServiceArgs;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
		
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
        logArgs.put("sourcetype", "config-splunk");
        receiver.log("main", logArgs, "Config server has been started and developing the database connections");
	}

}
