package local.payrollapp.simplepayroll.view;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import local.payrollapp.simplepayroll.GlobalConstants;
import local.payrollapp.simplepayroll.appusers.MockAppUsers;
import local.payrollapp.simplepayroll.components.MockDatabase;
import local.payrollapp.simplepayroll.employees.EmployeeController;
import local.payrollapp.simplepayroll.employees.EmployeeRequest;
import local.payrollapp.simplepayroll.employees.EmployeeResponse;
import local.payrollapp.simplepayroll.exceptions.ElementNotFoundException;
import local.payrollapp.simplepayroll.jobsite.JobsiteController;
import local.payrollapp.simplepayroll.jobsite.JobsiteRequest;
import local.payrollapp.simplepayroll.jobsite.JobsiteResponse;
import local.payrollapp.simplepayroll.paystub.ExtendedPaystubSrv;
import local.payrollapp.simplepayroll.paystub.PayrollController;
import local.payrollapp.simplepayroll.paystub.PaystubRequest;
import local.payrollapp.simplepayroll.paystub.PaystubResponse;
import local.payrollapp.simplepayroll.searchcriteria.SearchCriteriaController;
import local.payrollapp.simplepayroll.searchcriteria.SearchRequest;
import local.payrollapp.simplepayroll.searchcriteria.SearchResults;
import local.payrollapp.simplepayroll.utility.CsvWriter;
import local.payrollapp.simplepayroll.utility.PageUtility;
import local.payrollapp.simplepayroll.utility.SpHandler;

@Controller
@RequestMapping("/")
public class AppController {
	
	private final EmployeeController _empCtrlr;
	private final PayrollController _payrollCtrlr;
	private final ExtendedPaystubSrv _extStubSrv;
	private final JobsiteController _jobsiteCtrlr;
	private final SearchCriteriaController _searchCriteriaController;
	private final MockAppUsers _appUsers;
	private final CsvWriter _writer;
	private final PageUtility _pageUtil;
	private final SpHandler _spHandler;
	private static final Logger log = LoggerFactory.getLogger(AppController.class);
	
	private List<Object> writableResponses;
	private LinkedList<String> pageNav;
	private List<String> errList = new ArrayList<String>();
	private boolean isErr = false;
	
	private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
	
	AppController(EmployeeController empCtrlr, PayrollController payrollCtrlr, ExtendedPaystubSrv extStubSrv,
			JobsiteController siteCtrlr, SearchCriteriaController scCtrlr, MockAppUsers appUsers,CsvWriter writer, 
			PageUtility pageUtil, SpHandler spHandler){
		this._empCtrlr = empCtrlr;
		this._payrollCtrlr = payrollCtrlr;
		this._extStubSrv = extStubSrv;
		this._jobsiteCtrlr = siteCtrlr;
		this._searchCriteriaController = scCtrlr;
		this._appUsers = appUsers;
		this._writer = writer;
		this._pageUtil = pageUtil;
		this._spHandler = spHandler;
	}
	
	@PostConstruct
	private void init() {
		reset();
	}
	
	private void resetErrs(){
		//reset
		this.errList.clear();
		this.isErr = Boolean.FALSE;
	}
	
	@GetMapping("/")
	public String viewIndex() {
		if(_pageUtil.length() > 1) {
			_pageUtil.clear();
		}
		_pageUtil.visit("/");
		return "index";
	}

	@GetMapping("/help")
	public String viewHelp(Model model){
		_pageUtil.visit("/help");
		model.addAttribute("pageUtil", _pageUtil);
		return "help";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/login/token")
	public String showOttRequest() {
		return "token";
	}
	
	@GetMapping("/logout")
	public String logout(Authentication auth, HttpServletRequest req, HttpServletResponse resp) {
		resetAll();
		this.logoutHandler.logout(req, resp, auth);
		return "redirect:/login";
	}
	
	@GetMapping("/ott/sent")
	public String sent(Model model) {
		if(_appUsers.getTmpUser().isBlank() || _appUsers.getTmpUser() == null) {
			log.error("token generation failed.");
			return "redirect:/login";
		}
		String usrToken = _appUsers.getUserCredentialDetails(_appUsers.getTmpUser());
		model.addAttribute("expired", GlobalConstants.EXPIRED);
		model.addAttribute("usrToken", usrToken);
		return "sent";
	}
	
	/* employees */
	@GetMapping("/employees")
	public String getEmployees(Model model) {
		_pageUtil.visit("/employees");
		this.checkPrevPageToReset("/postemployees");
		List<EmployeeResponse> emps = _empCtrlr.getAllEmployees();
		EmployeeRequest emp = new EmployeeRequest();
		
		if(!emps.isEmpty()) {
			this.setWritableResponses(emps);
		}
		
		model.addAttribute("pageUtil", _pageUtil);
		model.addAttribute("isError", this.isErr);
		model.addAttribute("errMessages", this.errList);
		model.addAttribute("employees",emps);
		model.addAttribute("employee",emp);
		return "employees";
	}
	
	@GetMapping("/employee/{id}")
	public String getEmployee(Model model, @PathVariable("id") String id) throws ElementNotFoundException {
		_pageUtil.visit("/employee/" + id);
		this.checkPrevPageToReset("/putemployees");
		EmployeeResponse emp = _empCtrlr.getEmployee(id);
		model.addAttribute("pageUtil", _pageUtil);
		model.addAttribute("isError", this.isErr);
		model.addAttribute("errMessages", this.errList);
		model.addAttribute("employee", emp);
		return "employee";
	}
	
	@PostMapping("/employees")
	public RedirectView createEmployee(@Valid @ModelAttribute("employee") EmployeeRequest request, BindingResult result, RedirectAttributes redirectAttributes) {
		_pageUtil.visit("/postemployees");
		String msg = "";
		SpHandler employeeHandler = new SpHandler();
		employeeHandler.requestHandler(request, result);
		
		if(employeeHandler.isHasError()) {
			msg = String.format("Failed to add %s %s:",request.getfirstName(), request.getlastName());
			this.errList = employeeHandler.getReadibleErr();
			this.isErr = Boolean.TRUE;
			redirectAttributes.addFlashAttribute("userMessage", msg);
			return new RedirectView("/employees", true);
		}
		
		this.resetErrs();
//		_pageUtil.removeByUrl("/postEmployees");
		EmployeeResponse newEmp = _empCtrlr.createEmployee(request);
		msg = "Employee: " + newEmp.getFullName() + " was added.";
		redirectAttributes.addFlashAttribute("userMessage", msg);
		return new RedirectView("/employees", true);
	}
	
	@PostMapping("/employee/{id}")
	public RedirectView updateEmployee(@PathVariable("id") String id, @Valid @ModelAttribute("employee") EmployeeRequest request, BindingResult result, RedirectAttributes redirectAttributes) {
		_pageUtil.visit("/putemployee");
		String msg = "";
		String redirect = "";
		RedirectView redirectView;
		SpHandler employeeHandler = new SpHandler();
		employeeHandler.requestHandler(request, result);
		
		if(employeeHandler.isHasError()) {
			msg = String.format("Failed to update %s %s:",request.getfirstName(), request.getlastName());
			this.errList = employeeHandler.getReadibleErr();
			this.isErr = true;
			redirect = "/employee/" + id;
			redirectAttributes.addFlashAttribute("userMessage", msg);
			return new RedirectView(redirect, true);
		}

		this.resetErrs();
//		_pageUtil.removeByUrl("/postEmployees");
		EmployeeResponse updatedEmp = _empCtrlr.updateEmployee(id, request);
		msg = ((!updatedEmp.active()) ? "Employee was deleted successfully." : "Employee was updated successfully.");
		redirect = "/employees";
		redirectView = new RedirectView(redirect, true);
		redirectAttributes.addFlashAttribute("userMessage", msg);
		return redirectView;
	}
	
	@GetMapping("/deletedemployees")
	public String getDeletedEmployees(Model model) {
		_pageUtil.visit("/deletedemployees");
		String prevPage = _pageUtil.moveBackToPrev();
		List<EmployeeResponse> emps = _empCtrlr.getAllDeletedEmployees();
		model.addAttribute("prevPage", prevPage);
		model.addAttribute("employees",emps);
		return "deletedemployees";
	}
	
	@GetMapping("/restoreemployee/{id}")
	public RedirectView restoreEmployee(RedirectAttributes redirectAttributes, @PathVariable("id") String id) throws ElementNotFoundException {
		_empCtrlr.restoreEmployee(id);
		String msg = String.format("successfully restored employee: %s!", id);
		redirectAttributes.addFlashAttribute("userMessage", msg);
		RedirectView redirectView = new RedirectView("/deletedemployees", true);
		return redirectView;
	}
	
	@GetMapping("/deletedemployees/{id}")
	public RedirectView employeeToBeDeleted(RedirectAttributes redirectAttributes, @PathVariable("id") String id) {
		_pageUtil.visit("/deletedemployees/" + id);
		_empCtrlr.deleteEmployee(id);
		String msg = String.format("successfully deleted Employee: %s!", id);
		redirectAttributes.addFlashAttribute("userMessage", msg);
		RedirectView redirectView = new RedirectView("/deletedemployees", true);
		return redirectView;
	}
	
	/* payroll */
	@GetMapping("/payroll")
	public String getAllEmployeesForPayroll(Model model) {
		_pageUtil.visit("/payroll");
		List<EmployeeResponse> emps = _empCtrlr.getAllEmployees();
		List<PaystubResponse> stubs = _payrollCtrlr.getAllPaystubs();
		model.addAttribute("pageUtil", _pageUtil);
		model.addAttribute("empController", _empCtrlr);
		model.addAttribute("payrollCtrlr", _payrollCtrlr);
		model.addAttribute("employees",emps);
		model.addAttribute("paystubs",stubs);
		return "payroll";
	}
	
	@GetMapping("/paystubs/{id}")
	public String getAllPaystubsByEmployeeId(Model model, @PathVariable("id") String id) throws ElementNotFoundException{
		_pageUtil.visit(String.format("/paystubs/%s", id.toString()));
		this.checkPrevPageToReset("/postpaystub");
		List<String> sites = _jobsiteCtrlr.getJobsiteNames();
		EmployeeResponse emp = _empCtrlr.getEmployee(id);
		PaystubRequest stub = new PaystubRequest();
		this.setEmployeeDetails(stub, emp.id(), emp.getFullName(), emp.pay());
		List<PaystubResponse> stubs = _payrollCtrlr.getPaystubsForEmployee(id);
		//model.addAllAttributes(null)
		model.addAttribute("jobsites", sites);
		model.addAttribute("employee", emp);
		model.addAttribute("paystubs", stubs);
		model.addAttribute("paystub", stub);
		model.addAttribute("pageUtil", _pageUtil);
		model.addAttribute("extStubSrv", _extStubSrv);
		model.addAttribute("payrollCtrlr", _payrollCtrlr);
		return "paystubs";
	}
	
	@GetMapping("/paystub/{id}")
	public String getPaystub(Model model, @PathVariable("id") String id) throws ElementNotFoundException{
		_pageUtil.visit(String.format("/paystub/%s", id.toString()));
		this.checkPrevPageToReset(String.format("/putpaystub/%s", id.toString()));
		List<String> sites = _jobsiteCtrlr.getJobsiteNames();
		PaystubResponse stub = _payrollCtrlr.getPaystub(id);
		model.addAttribute("jobsites", sites);
		model.addAttribute("payrollCtrlr", _payrollCtrlr);
		model.addAttribute("pageUtil", _pageUtil);
		model.addAttribute("paystub", stub);
		return "paystub";
	}
	
	@PostMapping("/paystubs/{id}")
	public RedirectView createPaystub(@Valid @ModelAttribute("paystub") PaystubRequest request, BindingResult result, RedirectAttributes redirectAttributes) {
		_pageUtil.visit("/postpaystubs");
		String msg = "";
		RedirectView redirectView;
		SpHandler paystubHandler = new SpHandler();
		paystubHandler.requestHandler(request, result);
		
		if(paystubHandler.isHasError()) {			
			msg = String.format("Failed to add paystub for employee: %s. %s", request.getFullName(), paystubHandler.getReadibleErr());
			redirectView = new RedirectView("/paystubs/" + request.getEmployeeId(), true);
			redirectAttributes.addFlashAttribute("userMessage", msg);
			return redirectView;
		}
		
		this.resetErrs();
		//_pageUtil.removeByUrl("/postpaystubs");
		PaystubResponse newStub = _payrollCtrlr.createPaystub(request);
		msg = "Paystub for Employee: " + newStub.fullName() + " was added.";
		redirectView = new RedirectView("/paystubs/" + newStub.employeeId(), true);		
		redirectAttributes.addFlashAttribute("userMessage", msg);
		return redirectView;
	}
	
	@PostMapping("/paystub/{id}")
	public RedirectView updatePaystub(@PathVariable("id") String id, @Valid @ModelAttribute("paystub") PaystubRequest request, BindingResult result, RedirectAttributes redirectAttributes) {
		_pageUtil.visit(String.format("/putpaystub/%s", id.toString()));
		List<String> errMsgs;
		String msg = "";
		RedirectView redirectView;
		SpHandler paystubHandler = new SpHandler();
		paystubHandler.requestHandler(request, result);
		
		if(paystubHandler.isHasError()) {
			errMsgs = paystubHandler.getReadibleErr();
			redirectView = new RedirectView("/paystub/" + id, true);
			redirectAttributes.addFlashAttribute("errMessages", errMsgs);
			return redirectView;
		}
		
		PaystubResponse updatedStub = _payrollCtrlr.updatePaystub(id, request);
		
		if(!updatedStub.active()) {
			msg = "Paystub Was Deleted Successfully.";
			redirectView = new RedirectView("/paystubs/" + updatedStub.employeeId(), true);
		}
		else {
			msg = "Paystub was updated.";
			redirectView = new RedirectView("/paystub/" + updatedStub.paystubNum(), true);
		}
		
		redirectAttributes.addFlashAttribute("userMessage", msg);
		return redirectView;
	}
	
	@GetMapping("/deletedpaystubs")
	public String getDeletedPaystubs(Model model ) {
		_pageUtil.visit("/deletedpaystubs");
		String prevPage = _pageUtil.moveBackToPrev();
		List<PaystubResponse> stubs = _payrollCtrlr.getDeletedPaystubs();
		model.addAttribute("prevPage", prevPage);
		model.addAttribute("paystubs",stubs);
		model.addAttribute("payrollCtrlr", _payrollCtrlr);
		model.addAttribute("prevPage", prevPage);
		return "deletedpaystubs";
	}
	
	@GetMapping("/restorepaystub/{id}")
	public RedirectView restorePaystub(RedirectAttributes redirectAttributes, @PathVariable("id") String id) throws ElementNotFoundException {
		_payrollCtrlr.restorePaystub(id);
		String msg = String.format("successfully restored paystub: %s!", id);
		redirectAttributes.addFlashAttribute("userMessage", msg);
		RedirectView redirectView = new RedirectView("/deletedpaystubs", true);
		return redirectView;
	}
	
	@GetMapping("/deletedpaystubs/{id}")
	public RedirectView paystubToBeDeleted(RedirectAttributes redirectAttributes, @PathVariable("id") String id) {
		_payrollCtrlr.deletePaystub(id);
		String msg = String.format("successfully deleted paystub: %s!", id);
		redirectAttributes.addFlashAttribute("userMessage", msg);
		RedirectView redirectView = new RedirectView("/deletedpaystubs", true);
		return redirectView;
	}
	
	/* Jobsite */
	@GetMapping("/jobsites")
	public String getAllJobsites(Model model) {
		_pageUtil.visit("/jobsites");
		this.checkPrevPageToReset("/postjobsite");
		List<JobsiteResponse> sites = this._jobsiteCtrlr.getAllJobsitesByActive(true);
		JobsiteRequest newSite = new JobsiteRequest();
		model.addAttribute("pageUtil", _pageUtil);
		model.addAttribute("jobsite",newSite);
		model.addAttribute("jobsites",sites);
		return "jobsites";
	}
	
	@GetMapping("/jobsite/{id}")
	public String getActiveJobsiteById(Model model, @PathVariable("id") String id) throws ElementNotFoundException {
		_pageUtil.visit(String.format("/jobsite/%s", id.toString()));
		this.checkPrevPageToReset(String.format("/putjobsite/%s", id.toString()));
		JobsiteResponse site = this._jobsiteCtrlr.getJobsiteById(id, true);
		model.addAttribute("jobsite", site);
		return "jobsite";
	}
	
	@PostMapping("/jobsites")
	public RedirectView createJobsite(@Valid @ModelAttribute("jobsite") JobsiteRequest request, BindingResult result, RedirectAttributes redirectAttributes) {
		_pageUtil.visit("/postjobsite");
		List<String> errMsgs;
		String msg = "";
		RedirectView redirectView;
		SpHandler jobsiteHandler = new SpHandler();
		jobsiteHandler.requestHandler(request, result);
		
		if(jobsiteHandler.isHasError()) {
			errMsgs = jobsiteHandler.getReadibleErr();
			redirectView = new RedirectView("/jobsites", true);
			redirectAttributes.addFlashAttribute("errMessages", errMsgs);
			return redirectView;
		}
		
		this.resetErrs();
		//_pageUtil.removeByUrl("/postjobsite");
		JobsiteResponse newSite = this._jobsiteCtrlr.createJobsite(request);
		msg = "Jobsite: " + newSite.jobsiteName() + " was added.";
		redirectView = new RedirectView("/jobsites", true);
		redirectAttributes.addFlashAttribute("userMessage", msg);
		return redirectView;
	}
	
	@PostMapping("/jobsite/{id}")
	public RedirectView updateJobsite(@PathVariable("id") String id, @Valid @ModelAttribute("jobsite") JobsiteRequest request, BindingResult result, RedirectAttributes redirectAttributes) {
		_pageUtil.visit(String.format("/putjobsite/%s", id));
		List<String> errMsgs;
		String msg = "";
		String redirect = "";
		RedirectView redirectView;
		SpHandler jobsiteHandler = new SpHandler();
		jobsiteHandler.requestHandler(request, result);
		
		if(jobsiteHandler.isHasError()) {
			errMsgs = jobsiteHandler.getReadibleErr();
			redirect = String.format("/putjobsite/%s", id);
			redirectAttributes.addFlashAttribute("errMessages", errMsgs);
			return new RedirectView(redirect, true);
		}
		JobsiteResponse updatedSite = this._jobsiteCtrlr.updateJobsite(id, request);
		msg = ((!updatedSite.active()) ? "Jobsite was deleted successfully." : "Jobsite was updated successfully.");
		redirect = "/jobsites";
		redirectView = new RedirectView(redirect, true);
		redirectAttributes.addFlashAttribute("userMessage", msg);
		return redirectView;
	}
	
	@GetMapping("/deletedjobsites")
	public String getAllDeletedJobsites(Model model) {
		_pageUtil.visit("/deletedjobsites/");
		String prevPage = _pageUtil.moveBackToPrev();
		List<JobsiteResponse> deletedSites = this._jobsiteCtrlr.getAllJobsitesByActive(false);
		model.addAttribute("prevPage", prevPage);
		model.addAttribute("jobsites",deletedSites);
		return "deletedjobsites";
	}
	
	@GetMapping("/deletedjobsite/{id}")
	public RedirectView jobsiteToBeDeleted(RedirectAttributes redirectAttribute, @PathVariable("id") String id) {
		String siteName = _jobsiteCtrlr.getJobsiteById(id, false).jobsiteName();
		_jobsiteCtrlr.deleteJobsite(id);
		String msg = String.format("Successfully deleted jobsite: %s!", siteName);
		redirectAttribute.addFlashAttribute("userMessage", msg);
		return new RedirectView("/deletedjobsites", true);
	}
	
	@GetMapping("/restorejobsite/{id}")
	public RedirectView jobsiteToBeRestored(RedirectAttributes redirectAttribute, @PathVariable("id") String id) {
		_jobsiteCtrlr.restoreJobsite(id);
		String siteName = _jobsiteCtrlr.getJobsiteById(id, true).jobsiteName();
		String msg = String.format("Successfully restored jobsite: %s", siteName);
		redirectAttribute.addFlashAttribute("userMessage", msg);
		return new RedirectView("/deletedjobsites", true);
	}
	
	/* search */
	@GetMapping("/search")
	public String viewSearchPage(Model model) {
		_pageUtil.visit("/search");
		this.checkPrevPageToReset("/postsearch");
		SearchRequest searchRequest = new SearchRequest();
		List<SearchResults> searchResults = _searchCriteriaController.getSearchResults();
		List<String> header = _searchCriteriaController.getHeaders();
		model.addAttribute("pageUtil", _pageUtil);
		model.addAttribute("searchRequest", searchRequest);
		model.addAttribute("searchResults", searchResults);
		model.addAttribute("headers", header);
		model.addAttribute("dateFormatter", DateTimeFormatter.ofPattern("E, MMM dd yyyy"));
		model.addAttribute("isError", this.isErr);
		model.addAttribute("errMessages", this.errList);
		return "search";
	}
	
	@PostMapping("/search")
	public RedirectView searchResults(@Valid @ModelAttribute("searchRequest") SearchRequest request, BindingResult result, RedirectAttributes redirectAttributes) {
		_pageUtil.visit("/postsearch");
		String msg = "";
		SpHandler searchHandler = new SpHandler();
		searchHandler.requestHandler(request, result);

		if(searchHandler.isHasError()) {
			this.errList = searchHandler.getReadibleErr();
			this.isErr = true;
			return new RedirectView("/search", true);
		}
		
		List<SearchResults> response = _searchCriteriaController.findAllByCriteria(request);
		searchHandler.responseHandler(response);
		
		if(searchHandler.isHasError()) {
			this.errList = searchHandler.getReadibleErr();
			this.isErr = true;
			return new RedirectView("/search", true);
		}
    
		this.resetErrs();
		_searchCriteriaController.setSearchResults(response);
		_searchCriteriaController.buildHeader(response.get(0));
		msg = String.format("Found: %d records that match the criteria.", response.size());
		this.setWritableResponses(response);
		redirectAttributes.addFlashAttribute("userMessage", msg);
		return new RedirectView("/search", true);
	}
	
	/* csv writer */
//	@GetMapping("/export")
	public RedirectView writeToCsv(RedirectAttributes redirectAttributes) {
		String msg = "";
		String returnPage = _pageUtil.getCurr();
		RedirectView redirectView;
		try {
			SpHandler exportHandler = new SpHandler();
			List<Object> responses = this.writableResponses;
			exportHandler.responseHandler(responses);
			if(exportHandler.isHasError()) {
				throw new Exception("Internal Server Error...");
			}
			_writer.writeToCSV(responses);
			redirectView = new RedirectView("/download/" + _writer.getFileName(), true);
			return redirectView;
		} 
		catch( IOException e) {
			e.printStackTrace();
		}
		catch( Exception e) {
			e.printStackTrace();
		}
		msg = "An unexpected error occurred. Could not download table data...";
		redirectAttributes.addFlashAttribute("userMessage", msg);
		redirectView = new RedirectView(returnPage, true);
		return redirectView;
	}
	
	//@GetMapping("/export/payroll")
	public RedirectView writePaystubsToCsv(RedirectAttributes redirectAttributes) throws IOException {
		String msg = "";
		String returnPage = _pageUtil.getCurr();
		RedirectView redirectView;
		try {
			List<PaystubResponse> stubs = _payrollCtrlr.getAllPaystubs();
			_writer.writePaystubsToCSV(stubs);
			msg = String.format("CSV Created Successfully!");
			redirectAttributes.addFlashAttribute("userMessage", msg);
			redirectView = new RedirectView("/download/" + _writer.getFileName(), true);
			return redirectView;
		} catch(Exception e) {
			e.printStackTrace();
		}
		msg = "An unexpected error occurred. Could not download table data...";
		redirectAttributes.addFlashAttribute("userMessage", msg);
		redirectView = new RedirectView(returnPage, true);
		return redirectView;
	}
	
	/* utility methods */
	private void setEmployeeDetails(PaystubRequest request, String id, String fullName, double pay ) {
		request.setEmployeeId(id);
		request.setFullName(fullName);
		request.setPay(pay);
	}
	
	private void checkPrevPageToReset(String expected) {
		String prev = _pageUtil.getPrev();
		if(!prev.equals(expected)) {
			this.reset();
		}
		_pageUtil.removeByUrl(expected);
	}
  
	@SuppressWarnings("unchecked")
	private <T> void setWritableResponses(List<T> responses) {
		this.writableResponses = (List<Object>) responses;
	}
	
	public void reset() {
		this.resetErrs();
		_spHandler.clear();
		if(this.writableResponses != null) {
			this.writableResponses.clear();
		}
	}
	
	public void resetAll() {
		reset();
		_pageUtil.clear();
		_empCtrlr.clear();
		_jobsiteCtrlr.clear();
		_payrollCtrlr.clear();
	}
}