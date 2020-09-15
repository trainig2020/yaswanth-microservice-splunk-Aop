package com.springboot.microservice.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.springboot.microservice.model.Department;
import com.springboot.microservice.model.Employee;
import com.springboot.microservice.model.ListOfDepartments;
import com.springboot.microservice.model.ListOfEmployees;
import com.springboot.microservice.service.DepartmentService;
import com.springboot.microservice.service.RemoteEmployeeService;

@RestController
@RequestMapping("/Department")
public class DepartmentController {
	
	
	 Map<String,Object> conn = SplunkConnection.getService();
		
	 Service service;
	 
	 Receiver rec;
	
    
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private RemoteEmployeeService remoteEmployeeService;
	
	@Value("${message}")
	private String message;
	
	@RequestMapping("/message")
	public String getMess() {
		return message;
	}
	
	private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);
	
	@RequestMapping("/GetAll")
	public ListOfDepartments getAllDepartments(){
		logger.info("Getting All Departments");
		service = Service.connect(conn);
		rec = service.getReceiver();
		rec.log("main", "In Department controller Getting All Departments and getting logs in splunk");
		List<Department> lst = departmentService.getAllDepartments();
		ListOfDepartments listOfDepartments = new ListOfDepartments();
		listOfDepartments.setDeptList(lst);
	  return listOfDepartments;	
	}
	
	@RequestMapping("/{deptid}")
	public Optional<Department> getDepartment(@PathVariable("deptid") int deptid) {
		logger.info("Getting a single Department based on id");
		return departmentService.getDepartment(deptid);
	}
	
	@PostMapping("/AddDepartment")
	public void addDepartment(@RequestBody Department department) {
		logger.info("Adding Department to the database");
		service = Service.connect(conn);
		rec = service.getReceiver();
		rec.log("main", "In Department controller Inserting a Department and getting logs in splunk");
		departmentService.addDepartment(department);
	}
	
	@PutMapping("/updateDepartment/{deptid}")
	public void updateDepartment(@RequestBody Department department, @PathVariable int deptid) {
		logger.info("Updating a Department to the database");
		service = Service.connect(conn);
		rec = service.getReceiver();
		rec.log("main", "In Department controller Updating a Department and getting logs in splunk");
		department.setDeptid(deptid);
		departmentService.updateDepartment(department);
	}
	
	@DeleteMapping("/DeleteDepartment/{deptid}")
	public void deleteDepartment(@PathVariable int deptid) {
		logger.info("Deleting a Department from the database based on departmentId");
		service = Service.connect(conn);
		rec = service.getReceiver();
		rec.log("main", "In Department controller Deleting a Department and getting logs in splunk");
		departmentService.deleteDepartment(deptid);
		remoteEmployeeService.deleteEmployee(deptid);
	}
	
	@GetMapping("{edid}/employees")
	public ListOfEmployees getAllEmployees(@PathVariable int edid){
		logger.info("Getting All The Employees using restTemplate");
		service = Service.connect(conn);
		rec = service.getReceiver();
		rec.log("main", "In Department controller Getting all Employees through resttemplate call and getting logs in splunk");
	   ListOfEmployees lst =remoteEmployeeService.getAllEmployees(edid);
	   System.out.println(lst.getListOfEmployee().size());
	   return lst;
	}
	
	@GetMapping("/employess/{empid}")
	public Employee getEmployee(@PathVariable int empid) {
		logger.info("Getting a employee from the database based on employeeId");
		return remoteEmployeeService.getEmployee(empid);
	}
	
	@PostMapping("/employees/{edid}/addEmployee")
	public void addEmployee(@RequestBody Employee employee , @PathVariable int edid) {
		logger.info("Adding a Employee to the database ");
		service = Service.connect(conn);
		rec = service.getReceiver();
		rec.log("main", "In Department controller Inserting a Employee through resttemplate call  and getting logs in splunk");
		employee.setEdid(edid);
		remoteEmployeeService.addEmployee(employee,edid);
	}
	
	@PutMapping("/employees/{edid}/updateEmployee/{empid}")
	public void updateEmployee(@RequestBody Employee employee,@PathVariable int edid, @PathVariable int empid) {
		logger.info("Updating a Employee in the database ");
		service = Service.connect(conn);
		rec = service.getReceiver();
		rec.log("main", "In Department controller Updating a Employee through resttemplate call  and getting logs in splunk");
		employee.setEdid(edid);
		employee.setEmpid(empid);
		remoteEmployeeService.updateEmployee(employee,edid,empid);
	}
	@DeleteMapping("/employees/deleteEmployee/{edid}")
	public void deleteEmployee(@PathVariable int edid) {
		logger.info("Deleting a All Employee in the database based on DepartmentId ");
		service = Service.connect(conn);
		rec = service.getReceiver();
		rec.log("main", "In Department controller Deleting all Employees through resttemplate call  and getting logs in splunk");
		remoteEmployeeService.deleteEmployee(edid);
	}
	@DeleteMapping("/employees/{edid}/deleteEmployee/{empid}")
	public void deleteSingleEmployee(@PathVariable int edid,@PathVariable int empid) {
		logger.info("Deleting a  single Employee in the database based on employeeId and DepartmentID");
		service = Service.connect(conn);
		rec = service.getReceiver();
		rec.log("main", "In Department controller Deleting a single Employee through resttemplate call  and getting logs in splunk");
		remoteEmployeeService.deleteSingleEmployee(edid, empid);
	}
	
}
