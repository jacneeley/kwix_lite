package local.payrollapp.simplepayroll.components;

import java.time.Instant;
import java.util.HashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import local.payrollapp.simplepayroll.GlobalConstants;
import local.payrollapp.simplepayroll.employees.Employee;
import local.payrollapp.simplepayroll.jobsite.Jobsite;
import local.payrollapp.simplepayroll.paystub.Paystub;

@SessionScope
@Component
public class MockDatabase {
	private final HashMap<String, Paystub> paystubs = new HashMap<>();
	
	private final HashMap<String, Employee> employees = new HashMap<>();
	
	private final HashMap<String, Jobsite> jobsites = new HashMap<>();
	
	public MockDatabase() {}

	public HashMap<String, Paystub> getPaystubs(){
		return this.paystubs;
	}
	
	public HashMap<String, Employee> getEmployees(){
		return this.employees;
	}
	
	public HashMap<String, Jobsite> getJobsites(){
		return this.jobsites;
	}
}
