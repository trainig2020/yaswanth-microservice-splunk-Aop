package com.springboot.microservice.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.splunk.Receiver;
import com.splunk.Service;
import com.springboot.microservice.aspect.SplunkConnection;
import com.springboot.microservice.model.Employee;
import com.springboot.microservice.model.ListOfEmployees;
import com.springboot.microservice.service.EmployeeService;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
	
	Map<String,Object> conn = SplunkConnection.getService();
	
	 Service service;
	 
	 Receiver rec;
    
	@Autowired
	private EmployeeService employeeService;
	
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	
	@GetMapping("/GetAll/{edid}")
	public ListOfEmployees getAllEmployees(@PathVariable int edid){
		logger.info("Getting All Employees");
		service = Service.connect(conn);
		rec = service.getReceiver();
		rec.log("main", "In Employee controller Getting All Employees and getting logs in splunk");
		return employeeService.getAllEmployess(edid);
	}
	
	@GetMapping("/{empid}")
	public Optional<Employee> getEmployee(@PathVariable int empid) {
		logger.info("Getting All Employees");
		return employeeService.GetEmployee(empid);
	}
	
	@PostMapping("/{edid}/addEmployee")
	public void addEmployee(@PathVariable int edid,@RequestBody Employee employee) {
		service = Service.connect(conn);
		rec = service.getReceiver();
		rec.log("main", "In Employee controller Inserting a Employee and getting logs in splunk");
		logger.info("Adding an employee to the database");
		employee.setEdid(edid);
		employeeService.addEmployee(employee);
	}
	
	@PutMapping("/{edid}/updateEmployee/{empid}")
	public void updateEmployee(@PathVariable int edid, @RequestBody Employee employee,@PathVariable int empid) {
		logger.info("upadating an employee");
		service = Service.connect(conn);
		rec = service.getReceiver();
		rec.log("main", "In Employee controller Updating an Employee  getting logs in splunk");
		employee.setEdid(edid);
		employee.setEmpid(empid);
		employeeService.updateEmployee(employee);
	}
	@DeleteMapping("/deleteEmployee/{edid}")
	public void deleteEmployee(@PathVariable int edid) {
		logger.info("Deleting all employees based on deptid");
		service = Service.connect(conn);
		rec = service.getReceiver();
		rec.log("main", "In Employee controller Deleting all Employees  getting logs in splunk");
		employeeService.deleteEmployee(edid);
	}
	@DeleteMapping("/deleteAll/{edid}/{empid}")
	public void deleteEmployeeByEdidAndEmpid(@PathVariable int edid,@PathVariable int empid) {
		logger.info("Deletind An Employee Based On empid and deptid");
		service = Service.connect(conn);
		rec = service.getReceiver();
		rec.log("main", "In Employee controller Deleting a  Single Employee  getting logs in splunk");
		employeeService.deleteEmployeeByEdid(edid, empid);
	}
}
