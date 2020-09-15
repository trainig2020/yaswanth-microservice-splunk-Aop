package com.springboot.microservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.splunk.Receiver;
import com.splunk.Service;
import com.springboot.microservice.aspect.SplunkConnection;
import com.springboot.microservice.model.Department;
import com.springboot.microservice.model.Employee;
import com.springboot.microservice.model.ListOfDepartments;
import com.springboot.microservice.model.ListOfEmployees;

@RestController
public class DeptEmpViewController {
	
	
	 Map<String,Object> conn = SplunkConnection.getService();
	
	 Service service;
	 
	 Receiver rec;
	
	@Autowired
	private RestTemplate restTemplate;
	
	private static final Logger logger = LoggerFactory.getLogger(DeptEmpViewController.class);
    
	//@SuppressWarnings("unchecked")
	@RequestMapping(value = "/DeptList",method = RequestMethod.GET)
	 public ModelAndView  getAllDepartments(HttpServletRequest request,HttpServletResponse response,@RequestParam(required = false) Integer page) {
		 //System.out.println("In Controller");
		 //logger.info("Log : In Controller Getting all the Departments");
//		 service = Service.connect(conn);
//		 rec = service.getReceiver();
//		 rec.log("main", "In controller Getting All Departments and getting logs in splunk");
		 ListOfDepartments deptlist  =  getListOfDepartments();
		 System.out.println(deptlist.getDeptList().get(0));
		 List<Department> lstdept = new ArrayList<>();
		 
		 for(int i = 0; i < deptlist.getDeptList().size(); i++) {
			 lstdept.add(deptlist.getDeptList().get(i));
		 }
		 for (Department department : lstdept) {
			System.out.println(department.getDeptid()+department.getDepthead());
		}
		 
		 HttpSession session = request.getSession();
		 session.setAttribute("DeptList", lstdept);
		 session.setAttribute("page", page);
		 ModelAndView modelAndView = new ModelAndView("home1");
		 //modelAndView.addObject("DeptList", lstdept);
		 
		 PagedListHolder<Department> pagedListHolder = new PagedListHolder<Department>(lstdept);
		 
	     pagedListHolder.setPageSize(3);
	        
	     modelAndView.addObject("maxPages", pagedListHolder.getPageCount());
	     
	     System.out.println( pagedListHolder.getPageCount());
	     
	     if (page == null || page < 1 || page > pagedListHolder.getPageCount())
	    	 page = 1;
	        modelAndView.addObject("page", page);       
	     if (page == null || page < 1 || page > pagedListHolder.getPageCount()) {
	    	    
	            pagedListHolder.setPage(0);
	            modelAndView.addObject("DeptList", pagedListHolder.getPageList());
	            
	     } else if (page <= pagedListHolder.getPageCount()) {
	            pagedListHolder.setPage(page - 1);
	            modelAndView.addObject("DeptList", pagedListHolder.getPageList());
	     }
		 modelAndView.addObject("homepage", "main");
		 return modelAndView;
	 }
	
	
	public ListOfDepartments getListOfDepartments() {
		return restTemplate.getForObject("http://gateway-service/Department/GetAll", ListOfDepartments.class);
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/NewDepartment" ,method =RequestMethod.GET )
	 public ModelAndView newDepartment(HttpServletRequest request,HttpServletResponse response) {
//		 service = Service.connect(conn);
//		 rec = service.getReceiver();
//		 rec.log("main", "In controller Creating a new Department  getting log in splunk");
		 String Register = "newform";
		 HttpSession session1 = request.getSession();
		 List<Department> Newlst = (List<Department>) session1.getAttribute("DeptList");
		 session1.setAttribute("DeptList", Newlst);
		 //Integer page = (Integer) session1.getAttribute("page");
		 //System.out.println("page"+page);
		 ModelAndView modelAndView = new ModelAndView();
         PagedListHolder<Department> pagedListHolder = new PagedListHolder<Department>(Newlst);
	     pagedListHolder.setPageSize(3);
	     Integer page = pagedListHolder.getPageCount();
	     System.out.println(page);
		 session1.setAttribute("pageAdd", page);
	        
	     modelAndView.addObject("maxPages", pagedListHolder.getPageCount());
	     
	     System.out.println( pagedListHolder.getPageCount());
	     
	     if (page == null || page < 1 || page > pagedListHolder.getPageCount())
	    	 page = 1;
	        modelAndView.addObject("page", page);       
	     if (page == null || page < 1 || page > pagedListHolder.getPageCount()) {
	    	    
	            pagedListHolder.setPage(0);
	            modelAndView.addObject("DeptList", pagedListHolder.getPageList());
	            
	     } else if (page <= pagedListHolder.getPageCount()) {
	            pagedListHolder.setPage(page - 1);
	            modelAndView.addObject("DeptList", pagedListHolder.getPageList());
	     }
	     //modelAndView.addObject("DeptList", lst);
		 modelAndView.addObject("Register", Register);
		 modelAndView.addObject("createdept", "newdept");
		 modelAndView.setViewName("home1");
		 Department department = new Department();
		 modelAndView.addObject("department", department);
		 return modelAndView;
	 }
	
	 
	 @SuppressWarnings("unchecked")
	@RequestMapping(value = "/CreateDepartment", method = RequestMethod.POST)
	 public ModelAndView insertDepartment(@ModelAttribute Department department,HttpServletRequest request) {
		 //logger.info("Adding a new department into the database");
	     //addDepartment(department);
//		 service = Service.connect(conn);
//		 rec = service.getReceiver();
//		 rec.log("main", "In controller Inserting a new Department  getting log in splunk");
		 Department dept = restTemplate.postForObject("http://gateway-service/Department/AddDepartment",department,Department.class);
	     HttpSession session6 = request.getSession();
	     List<Department> lst = (List<Department>) session6.getAttribute("DeptList");
	     Integer page = (Integer) session6.getAttribute("pageAdd");
	     PagedListHolder<Department> plh= new PagedListHolder<Department>(lst);
	     int pagerowvalue =  plh.getNrOfElements();
	        System.out.println("Page in Dept "+page);
	          if((pagerowvalue %3) ==0) {
	              return new ModelAndView("redirect:/DeptList?page="+(page+1));
	          }
	          else {
	             return new ModelAndView("redirect:/DeptList?page="+page);
	          }
	 }
	 
	 
	public Department addDepartment(Department department) {
		return restTemplate.postForObject("http://gateway-service/Department/AddDepartment",department,Department.class);
	}
	
	
	 
	 @RequestMapping(value = "/UpdateDepartment")
	 public ModelAndView updateDepartment(@ModelAttribute Department department, HttpServletRequest request,HttpServletResponse response) {
		 int deptid =Integer.parseInt(request.getParameter("deptid"));
//		 logger.info("Updating adepartment in the database");
//		 service = Service.connect(conn);
//		 rec = service.getReceiver();
//		 rec.log("main", "In controller Updating a Department  getting log in splunk");
	     upadateDept(department, deptid);
		 //restTemplate.put("http://gateway-service/Department/updateDepartment/"+deptid,department);
	     HttpSession session7 = request.getSession();
	     Integer page = (Integer) session7.getAttribute("page");
	     if (page != null) {
			 return new ModelAndView("redirect:/DeptList?page="+page);
		 }
		 return new ModelAndView("redirect:/DeptList");
	 }
	 
	 
	public void upadateDept(Department department, int deptid) {
	  restTemplate.put("http://gateway-service/Department/updateDepartment/"+deptid,department);
	}	 
	 
	
	
	 @RequestMapping(value = "/DeleteDepartment")
	 public ModelAndView deleteDepartment(HttpServletRequest request,HttpServletResponse response) {
		 int deptid =Integer.parseInt(request.getParameter("deptid"));
//		 logger.info("Deleting a department in the database");
//		 service = Service.connect(conn);
//		 rec = service.getReceiver();
//		 rec.log("main", "In controller Deleting a  Department  getting log in splunk");
		 deleteDept(deptid);
		 HttpSession session10 = request.getSession();
		 Integer page = (Integer) session10.getAttribute("page");
	     if (page != null) {
			 return new ModelAndView("redirect:/DeptList?page="+page);
		 }
		 return new ModelAndView("redirect:/DeptList");
	 }
	 
	 
	public void deleteDept(int deptid) {
		restTemplate.delete("http://gateway-service/Department/DeleteDepartment/"+deptid);
	}
	
	 
	 @SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetDepartment",method = RequestMethod.GET)
	 public ModelAndView getDepartmentId(HttpServletRequest request,HttpServletResponse response) {
		int deptid =  Integer.parseInt(request.getParameter("deptid"));
		HttpSession session2 = request.getSession();
		List<Department> lst = (List<Department>) session2.getAttribute("DeptList");
		ModelAndView modelAndView = new ModelAndView("home1");
		//modelAndView.addObject("DeptList", lst);
		Integer page = (Integer) session2.getAttribute("page");
        PagedListHolder<Department> pagedListHolder = new PagedListHolder<Department>(lst);
		 
	     pagedListHolder.setPageSize(3);
	        
	     modelAndView.addObject("maxPages", pagedListHolder.getPageCount());
	     
	     System.out.println( pagedListHolder.getPageCount());
	     
	     if (page == null || page < 1 || page > pagedListHolder.getPageCount())
	    	 page = 1;
	        modelAndView.addObject("page", page);       
	     if (page == null || page < 1 || page > pagedListHolder.getPageCount()) {
	    	    
	            pagedListHolder.setPage(0);
	            modelAndView.addObject("DeptList", pagedListHolder.getPageList());
	            
	     } else if (page <= pagedListHolder.getPageCount()) {
	            pagedListHolder.setPage(page - 1);
	            modelAndView.addObject("DeptList", pagedListHolder.getPageList());
	     }
		modelAndView.addObject("departmentid", deptid);
		return  modelAndView;		
	 }
	 
	 
	 @SuppressWarnings("unchecked")
	@RequestMapping(value = "/showdepartments",method = RequestMethod.GET)
	 public ModelAndView showDepartments(HttpServletRequest request,HttpServletResponse response,@RequestParam(required = false) Integer page) {
		 HttpSession session3 = request.getSession();
		 List<Department> lstdept1 = (List<Department>) session3.getAttribute("DeptList");
		 session3.setAttribute("DeptListemp", lstdept1);
		 session3.setAttribute("pageEmp", page);
		 ModelAndView modelAndView = new ModelAndView("home1");
   	 modelAndView.addObject("DeptListemp", lstdept1);
		 int deptid =  lstdept1.get(0).getDeptid();
		 modelAndView.addObject("name", "names");
		 if (page != null) {
			 return new ModelAndView("redirect:/emplist?deptid="+deptid+"&page="+page);
		 }
		 return new ModelAndView("redirect:/emplist?deptid="+deptid);
	 }
	 
	 
	 @SuppressWarnings("unchecked")
	@RequestMapping(value = "/emplist")
		public ModelAndView getAllEmployees(HttpServletRequest request,HttpServletResponse response,@RequestParam(required = false) Integer page) {
			int deptid =Integer.parseInt(request.getParameter("deptid"));
			List<Employee> lstemp = new ArrayList<>();
//			logger.info("Log : In Controller Getting all the Employees");
//			service = Service.connect(conn);
//			rec = service.getReceiver();
//			rec.log("main", "In controller Getting a new Employee  getting log in splunk");
			ListOfEmployees  lst = getAllDepartments(deptid);
			for (int i = 0; i < lst.getListOfEmployee().size(); i++) {
				lstemp.add(lst.getListOfEmployee().get(i));
			}
			HttpSession httpSession = request.getSession();
			httpSession.setAttribute("EmpList", lstemp);
			
			httpSession.setAttribute("page1", page);
			
			//Integer page = (Integer) httpSession.getAttribute("page");
			
			List<Department> lstdept1 = (List<Department>) httpSession.getAttribute("DeptList");
			ModelAndView modelAndView = new ModelAndView("home1");
			modelAndView.addObject("DeptListemp", lstdept1);
			//modelAndView.addObject("EmpList", lstemp);
			
			PagedListHolder<Employee> pagedListHolder = new PagedListHolder<Employee>(lstemp);
			 
		     pagedListHolder.setPageSize(2);
		        
		     modelAndView.addObject("maxPages", pagedListHolder.getPageCount());
		     
		     System.out.println( pagedListHolder.getPageCount());
		     
		     if (page == null || page < 1 || page > pagedListHolder.getPageCount())
		    	 page = 1;
		        modelAndView.addObject("page", page);       
		     if (page == null || page < 1 || page > pagedListHolder.getPageCount()) {
		    	    
		            pagedListHolder.setPage(0);
		            modelAndView.addObject("EmpList", pagedListHolder.getPageList());
		            
		     } else if (page <= pagedListHolder.getPageCount()) {
		            pagedListHolder.setPage(page - 1);
		            modelAndView.addObject("EmpList", pagedListHolder.getPageList());
		     }
			modelAndView.addObject("homepage", "emppage");
			modelAndView.addObject("DeptId", deptid);
			modelAndView.addObject("name", "names");		
			return modelAndView;
			}

	 
	public ListOfEmployees getAllDepartments(int deptid) {
		return restTemplate.getForObject("http://gateway-service/Department/"+deptid+"/employees", ListOfEmployees.class);
	}
		
		@SuppressWarnings("unchecked")
		@RequestMapping(value = "/newEmployee", method = RequestMethod.GET)
		public ModelAndView newContact(HttpServletRequest request,HttpServletResponse response) {
//			service = Service.connect(conn);
//			rec = service.getReceiver();
//			rec.log("main", "In controller Creating a new Employee  getting log in splunk");
			String Register  = "NewForm";
			HttpSession session1 = request.getSession();
			List<Employee> lst =(List<Employee>)session1.getAttribute("EmpList");
			ModelAndView model = new ModelAndView("home1");
			//Integer page = (Integer) session1.getAttribute("page1");
			PagedListHolder<Employee> pagedListHolder = new PagedListHolder<Employee>(lst);
			pagedListHolder.setPageSize(2);
			 Integer page = pagedListHolder.getPageCount();
			 session1.setAttribute("pageAdd", page);
		     
		        
		     model.addObject("maxPages", pagedListHolder.getPageCount());
		     
		     System.out.println( pagedListHolder.getPageCount());
		     
		     if (page == null || page < 1 || page > pagedListHolder.getPageCount())
		    	 page = 1;
		     model.addObject("page", page);       
		     if (page == null || page < 1 || page > pagedListHolder.getPageCount()) {
		    	    
		            pagedListHolder.setPage(0);
		            model.addObject("EmpList", pagedListHolder.getPageList());
		            
		     } else if (page <= pagedListHolder.getPageCount()) {
		            pagedListHolder.setPage(page - 1);
		            model.addObject("EmpList", pagedListHolder.getPageList());
		     }
			
			//model.addObject("EmpList", lst);
			model.addObject("Register", Register);
			model.addObject("insertEmployee", "newemployee");
			model.addObject("homepage", "emppage");		
			return model;	
		}

		
		@SuppressWarnings("unchecked")
		@RequestMapping(value = "/saveEmployee", method = RequestMethod.POST)
		public ModelAndView saveEmployee(@ModelAttribute Employee employee,HttpServletRequest request,HttpServletResponse response) {
			int edid =  Integer.parseInt(request.getParameter("edid"));
//			logger.info("Log : In Controller Adding  new employee to the database");
//			service = Service.connect(conn);
//			rec = service.getReceiver();
//			rec.log("main", "In controller Inserting a new Employee  getting log in splunk");
			restTemplate.postForObject("http://gateway-service/Department/employees/"+edid+"/addEmployee", employee, Employee.class);
			HttpSession session8 = request.getSession();
			List<Employee> lst =(List<Employee>) session8.getAttribute("EmpList");
			Integer page = (Integer) session8.getAttribute("pageAdd");
			PagedListHolder<Employee> plh= new PagedListHolder<Employee>(lst);
		     int pagerowvalue =  plh.getNrOfElements();
		        System.out.println("Page in Dept "+page);
			 if (pagerowvalue%2==0) {
				 return new ModelAndView("redirect:/emplist?deptid="+edid+"&page="+(page+1));
			 }
			 else {
			return new ModelAndView("redirect:/emplist?deptid="+edid+"&page="+(page));	
			 }
		}
		

		public Employee addEmployee(Employee employee, int edid) {
			return restTemplate.postForObject("http://gateway-service/Department/employees/"+edid+"/addEmployee", employee, Employee.class);
		}

		
		@RequestMapping(value = "/deleteEmployee")
		public ModelAndView deleteEmployee(HttpServletRequest request,HttpServletResponse response) {
			int employeeId = Integer.parseInt(request.getParameter("id"));
			int edid =  Integer.parseInt(request.getParameter("did"));
//			logger.info("Log : In Controller Deleting  a employee in the database");
//			service = Service.connect(conn);
//			 rec = service.getReceiver();
//			 rec.log("main", "In controller deleting a Employee  getting log in splunk");
			deleteEmp(employeeId, edid);
			//restTemplate.delete("http://gateway-service/Department/employees/"+edid+"/deleteEmployee/"+employeeId);
			//return new ModelAndView("redirect:/emplist?deptid="+edid);
			HttpSession session11 = request.getSession();
			Integer page = (Integer) session11.getAttribute("page1");
			 if (page != null) {
				 return new ModelAndView("redirect:/emplist?deptid="+edid+"&page="+page);
			 }
			return new ModelAndView("redirect:/emplist?deptid="+edid);
		}
		

		public void deleteEmp(int employeeId, int edid) {
			restTemplate.delete("http://gateway-service/Department/employees/"+edid+"/deleteEmployee/"+employeeId);
		}

		
		@SuppressWarnings("unchecked")
		@RequestMapping(value = "/getEmployee", method = RequestMethod.GET)
		public ModelAndView editContact(HttpServletRequest request,HttpServletResponse response) {
			int employeeId = Integer.parseInt(request.getParameter("id"));
			int did =  Integer.parseInt(request.getParameter("did"));
			HttpSession session2 = request.getSession();
			List<Employee> lst =(List<Employee>) session2.getAttribute("EmpList");
			session2.setAttribute("EmpList", lst);
			ModelAndView model = new ModelAndView("home1");
			
			Integer page = (Integer) session2.getAttribute("page1");
			PagedListHolder<Employee> pagedListHolder = new PagedListHolder<Employee>(lst);
			 
		     pagedListHolder.setPageSize(2);
		        
		     model.addObject("maxPages", pagedListHolder.getPageCount());
		     
		     System.out.println( pagedListHolder.getPageCount());
		     
		     if (page == null || page < 1 || page > pagedListHolder.getPageCount())
		    	 page = 1;
		     model.addObject("page", page);       
		     if (page == null || page < 1 || page > pagedListHolder.getPageCount()) {
		    	    
		            pagedListHolder.setPage(0);
		            model.addObject("EmpList", pagedListHolder.getPageList());
		            
		     } else if (page <= pagedListHolder.getPageCount()) {
		            pagedListHolder.setPage(page - 1);
		            model.addObject("EmpList", pagedListHolder.getPageList());
		     }
			model.addObject("homepage", "emppage");
			//model.addObject("EmpList", lst);
			model.addObject("employeeid", employeeId);
			model.addObject("Did", did);
			return model;
		}
		
		
		@RequestMapping(value = "/updateEmployee")
		public ModelAndView updateEmployee(@ModelAttribute Employee employee,HttpServletRequest request,HttpServletResponse response) {
			int employeeId = Integer.parseInt(request.getParameter("empid"));
			int did =  Integer.parseInt(request.getParameter("edid"));
//			System.out.println("In Method Update");
//			logger.info("Log : In Controller Updating  a employee in the database");
//			service = Service.connect(conn);
//			 rec = service.getReceiver();
//			 rec.log("main", "In controller Upadating a  Employee  getting log in splunk");
			updateEmp(employee, employeeId, did);
			//restTemplate.put("http://gateway-service/Department/employees/"+did+"/updateEmployee/"+employeeId, employee);
			//System.out.println("In Method Update");
			HttpSession session9 = request.getSession();
			Integer page = (Integer) session9.getAttribute("page1");
			 if (page != null) {
				 return new ModelAndView("redirect:/emplist?deptid="+did+"&page="+page);
			 }
			return new ModelAndView("redirect:/emplist?deptid="+did);
		}

		
		public void updateEmp(Employee employee, int employeeId, int did) {
			System.out.println("From Test Case");
			restTemplate.put("http://gateway-service/Department/employees/"+did+"/updateEmployee/"+employeeId, employee);
		}
	
	
}
