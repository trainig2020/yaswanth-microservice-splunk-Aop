//package com.springboot.microservice.controller;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import com.splunk.HttpService;
//import com.splunk.SSLSecurityProtocol;
//import com.splunk.Service;
//import com.splunk.ServiceArgs;
//
//public class SplunkConnection {
//    
//  public static Map<String,Object> getService() {
//	  HttpService.setSslSecurityProtocol(SSLSecurityProtocol.TLSv1_2);
//      // Create a map of arguments and add login parameters
//	  Map<String, Object> logconn = new HashMap<String,Object>(); 
//	  logconn.put("username", "yaswanth3898");
//	  logconn.put("password", "peace@3898");
//	  logconn.put("host", "LAPTOP-JMMCFRFU");
//	  logconn.put("port", 8089);
//	  
////      ServiceArgs loginArgs = new ServiceArgs();
////      loginArgs.setUsername("yaswanth3898");
////      loginArgs.setPassword("peace@3898");
////      loginArgs.setHost("LAPTOP-JMMCFRFU");
////      loginArgs.setPort(8089);
//      
//      // Create a Service instance and log in with the argument map
//      //Service service = Service.connect(loginArgs);
//	return logconn;
//  }
//	
//}
