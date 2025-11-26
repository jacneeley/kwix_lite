package local.payrollapp.simplepayroll;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import local.payrollapp.simplepayroll.employees.Employee;
import local.payrollapp.simplepayroll.employees.EmployeeRepo;
import local.payrollapp.simplepayroll.employees.EmployeeSrv;

//TODO: test with unused imports, then test without them.

@SpringBootApplication
@EnableConfigurationProperties(SimplePayrollConfigProperties.class)
public class SimplePayroll {
	
	private static final Logger log = LoggerFactory.getLogger(SimplePayroll.class);
	
	public static void main(String[] args) {
		SpringApplication.run(SimplePayroll.class, args);
		log.info("App Startup Successful!");
	}
}
