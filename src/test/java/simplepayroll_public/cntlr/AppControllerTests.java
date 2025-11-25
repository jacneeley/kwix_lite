package simplepayroll_public.cntlr;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import local.payrollapp.simplepayroll.GlobalConstants;
import local.payrollapp.simplepayroll.SimplePayroll;
import local.payrollapp.simplepayroll.appusers.MockAppUsers;
import local.payrollapp.simplepayroll.components.MockDatabase;
import local.payrollapp.simplepayroll.employees.Employee;
import local.payrollapp.simplepayroll.employees.EmployeeController;
import local.payrollapp.simplepayroll.employees.EmployeeRepo;
import local.payrollapp.simplepayroll.employees.EmployeeRequest;
import local.payrollapp.simplepayroll.employees.EmployeeResponse;
import local.payrollapp.simplepayroll.employees.EmployeeSrv;
import local.payrollapp.simplepayroll.jobsite.Jobsite;
import local.payrollapp.simplepayroll.jobsite.JobsiteController;
import local.payrollapp.simplepayroll.jobsite.JobsiteRequest;
import local.payrollapp.simplepayroll.jobsite.JobsiteResponse;
import local.payrollapp.simplepayroll.jobsite.JobsiteSrv;
import local.payrollapp.simplepayroll.paystub.ExtendedPaystubSrv;
import local.payrollapp.simplepayroll.paystub.PayrollController;
import local.payrollapp.simplepayroll.paystub.Paystub;
import local.payrollapp.simplepayroll.paystub.PaystubRepo;
import local.payrollapp.simplepayroll.paystub.PaystubRequest;
import local.payrollapp.simplepayroll.paystub.PaystubResponse;
import local.payrollapp.simplepayroll.paystub.PaystubSrv;
import local.payrollapp.simplepayroll.searchcriteria.SearchCriteriaController;
import local.payrollapp.simplepayroll.utility.CsvWriter;
import local.payrollapp.simplepayroll.utility.PageUtility;
import local.payrollapp.simplepayroll.utility.SpHandler;
import local.payrollapp.simplepayroll.view.AppController;
import java.util.Date;

@WebMvcTest(AppController.class)
@ContextConfiguration(classes = AppController.class)
public class AppControllerTests {
	@Autowired
	private MockMvc mockMvc;
	
    @MockitoBean
    private EmployeeController employeeController; // This will be auto-mocked

    @MockitoBean
    private PayrollController payrollController; // This will be auto-mocked

    @MockitoBean
    private JobsiteController jobsiteController; // This will be auto-mocked
	
    @MockitoBean
    private ExtendedPaystubSrv extendedPaystubSrv;
    
    @MockitoBean
    private SearchCriteriaController sCC;
    
    @MockitoBean
    private MockAppUsers appUsers;
    
    @MockitoBean 
    private CsvWriter csvWriter;
    
    @MockitoBean
    private PageUtility pageUtil;
    
    @MockitoBean
    private SpHandler spHandler;
    
	private Jobsite jobsite;
	private JobsiteRequest jobsiteReq;
	private JobsiteResponse jobsiteResp;
	
	private Paystub paystub;
	private PaystubResponse paystubResp;
	private PaystubRequest paystubReq;

    @MockitoBean
    private EmployeeSrv employeeSrv; // Mock the service layer

    private Employee employee;
    private EmployeeRequest empReq;
    private EmployeeResponse empResponse;
	
	public void initEmployee() {
		empReq = new EmployeeRequest();
		empReq.setfirstName("George");
		empReq.setlastName("Harrison");
		empReq.setPhone("999-999-9999");
		empReq.setPay(150.0);
		
		employee = new Employee(
				"GH9999",
				empReq.getfirstName(),
				empReq.getlastName(),
				empReq.getPhone(),
				empReq.getPay(),
				Boolean.TRUE,
				LocalDate.now(),
				LocalDate.now());
		
		empResponse = new EmployeeResponse(
				employee.getId(),
				employee.getfirstName(),
				employee.getlastName(),
				employee.getPhone(),
				employee.getPay(),
				employee.isActive(),
				employee.getCreateAt(),
				employee.getUpdateAt());
    }
	
	public void initJobsite() {
		jobsiteReq = new JobsiteRequest();
		jobsiteReq.setId("aaaaaaa");
		jobsiteReq.setJobsiteName("NATHAN");
		jobsiteReq.setContract(Boolean.FALSE);
		jobsiteReq.setActive(Boolean.TRUE);
		
		jobsite = new Jobsite(
				jobsiteReq.getId(),
				jobsiteReq.getJobsiteName(),
				jobsiteReq.isContract(),
				jobsiteReq.isActive(),
				LocalDate.now(),
				LocalDate.now());
		
		jobsiteResp = new JobsiteResponse(
				jobsite.getId(),
				jobsite.getJobsiteName(),
				jobsite.isContract(),
				jobsite.isActive(),
				jobsite.getCreatedAt(),
				jobsite.getUpdatedAt());
	}
	
	public void initPaystub() {
		initJobsite();
		initEmployee();
		
		LocalDate dayworked = LocalDate.now();
		
		paystubReq = new PaystubRequest();
		paystubReq.setEmployeeId(employee.getId());
		paystubReq.setFullName(employee.getFullName());
		/* i messed up really bad making this `date` instead of `localdate`...
		 * i dont wanna fix it because it could break other things. oh well...
		 */
		paystubReq.setDayWorked(
				Date.from(                     				 // Convert from modern java.time class to troublesome old legacy class.  DO NOT DO THIS unless you must, to inter operate with old code not yet updated for java.time.
						dayworked                            // `LocalDate` class represents a date-only, without time-of-day and without time zone nor offset-from-UTC. 
					    .atStartOfDay(                       // Let java.time determine the first moment of the day on that date in that zone. Never assume the day starts at 00:00:00.
					        ZoneId.of( "America/Chicago" )   // Specify time zone using proper name in `continent/region` format, never 3-4 letter pseudo-zones such as “PST”, “CST”, “IST”. 
					    )                                    // Produce a `ZonedDateTime` object. 
					    .toInstant()                         // Extract an `Instant` object, a moment always in UTC.
					)
		);
		paystubReq.setHoursWorked(8.0);
		paystubReq.setPay(25.00);
		paystubReq.setActive(Boolean.FALSE);
		
		
		paystub = new Paystub(
				"aaaaaaaa123456",
				paystubReq.getEmployeeId(),
				paystubReq.getFullName(),
				paystubReq.getJobsite(),
				paystubReq.getPay(),
				paystubReq.getHoursWorked(),
				paystubReq.isActive(),
				dayworked, //GOOD LORD WHY IS THE DATA TYPE DIFFERENT I HATE MYSELF
				LocalDate.now(),
				LocalDate.now());
		
		paystubResp = new PaystubResponse(
				paystub.getPaystubNum(),
				paystub.getEmployeeId(),
				paystub.getFullName(),
				paystub.getJobsite(),
				paystub.getPay(),
				paystub.getHoursWorked(),
				paystub.isActive(),
				paystub.getDayWorked(),
				paystub.getCreateAt(),
				paystub.getUpdateAt());
	}
	
	@Test
	@WithMockUser
	public void View_CreateEmployee_RedirectToCreated() throws Exception{
		initEmployee();
		when(employeeController.createEmployee(ArgumentMatchers.any(EmployeeRequest.class))).thenReturn(empResponse);
		
		ResultActions response = mockMvc.perform(post("/employees")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("firstName", empReq.getfirstName())
				.param("lastName", empReq.getlastName())
				.param("pay", String.valueOf(empReq.getPay()))
				.param("phone", empReq.getPhone())
				.with(csrf()));
		
		response.andDo(print());
		
		verify(employeeController, times(1)).createEmployee(ArgumentMatchers.any(EmployeeRequest.class));
		
		response.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/employees"));
	}
	
	//TODO: write some tests for jobsite and paystub and call it a day
	@Test
	@WithMockUser
	public void View_CreateJobsite_RedirectToCreated() throws Exception {
		initJobsite();
		when(jobsiteController.createJobsite(ArgumentMatchers.any(JobsiteRequest.class))).thenReturn(jobsiteResp);
		
		ResultActions response = mockMvc.perform(post("/jobsites")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("jobsiteName", jobsiteReq.getJobsiteName())
				.param("contract", String.valueOf(jobsiteReq.isContract()))
				.with(csrf()));
		
		response.andDo(print());
		
		verify(jobsiteController, times(1)).createJobsite(ArgumentMatchers.any(JobsiteRequest.class));
		
		response.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/jobsites"));
	}
	
	@Test
	@WithMockUser
	public void View_CreatePaystub_RedirectedToCreated() throws Exception {
		initPaystub();
		
		when(employeeController.createEmployee(ArgumentMatchers.any(EmployeeRequest.class))).thenReturn(empResponse);
				
		ResultActions response = mockMvc.perform(post("/employees")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("firstName", empReq.getfirstName())
				.param("lastName", empReq.getlastName())
				.param("pay", String.valueOf(empReq.getPay()))
				.param("phone", empReq.getPhone())
				.with(csrf()));
		
		response.andDo(print());
		
		verify(employeeController, times(1)).createEmployee(ArgumentMatchers.any(EmployeeRequest.class));
		
		response.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/employees"));
		
		/*create dependencies for paystub */
		//jobsite
		when(jobsiteController.createJobsite(ArgumentMatchers.any(JobsiteRequest.class))).thenReturn(jobsiteResp);
		
		response = mockMvc.perform(post("/jobsites")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("jobsiteName", jobsiteReq.getJobsiteName())
				.param("contract", String.valueOf(jobsiteReq.isContract()))
				.with(csrf()));
		
		response.andDo(print());
		
		verify(jobsiteController, times(1)).createJobsite(ArgumentMatchers.any(JobsiteRequest.class));
		
		response.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/jobsites"));
		
		//paystub
		when(payrollController.createPaystub(ArgumentMatchers.any(PaystubRequest.class))).thenReturn(paystubResp);

		String endpoint = new StringBuilder().append("/paystubs/").append(employee.getId()).toString();
		response = mockMvc.perform(post(endpoint)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("employeeId", employee.getId())
				.param("fullName", paystubReq.getFullName())
				.param("jobsite", jobsite.getJobsiteName())
				.param("pay", String.valueOf(paystubReq.getPay()))
				.param("hoursWorked", String.valueOf(8.0))
				.param("day", String.valueOf(paystubReq.getDayWorked()))
				.with(csrf()));
		
		response.andDo(print());
		
		verify(payrollController, times(1)).createPaystub(ArgumentMatchers.any(PaystubRequest.class));
		
		response.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl(endpoint));
	}
}
