package local.payrollapp.simplepayroll.searchcriteria;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller
public class SearchCriteriaController {
	
	private final SearchCriteriaService _searchCriteriaService;
	private static final Logger log = LoggerFactory.getLogger(SearchCriteriaController.class);
	
	private List<SearchResults> searchResults = new ArrayList<SearchResults>();
	private List<String> headers = new ArrayList<String>();
	
	public SearchCriteriaController(SearchCriteriaService scService) {
		this._searchCriteriaService = scService;
	}
	
	public List<SearchResults> findAllByCriteria(SearchRequest criteria){
		SearchCriteria newCriteria = this.getCriteria(criteria);
		log.info("searching for: " + newCriteria.toString() + "...");
		List<SearchCriteria> searchResponse = _searchCriteriaService.findAllByCriteria(newCriteria);
		log.info("found: " + searchResponse.size() + " results");
		List<SearchResults> searchResults = new ArrayList<SearchResults>();
		for (SearchCriteria resp: searchResponse) {
			SearchResults response = getResponse(resp);
			searchResults.add(response);
		}
		return searchResults;
	}
	
	public void buildHeader(SearchResults result){
		List<String> headers = new ArrayList<String>();
		if(result instanceof SearchResults(
				String empId, String empName, String phone, 
				Double pay,String paystubNum, Double hoursWorked, 
				LocalDate dayWorked, String jobsiteName, Boolean isContract, 
				Boolean active
		)) {
			if(empId != null) headers.add("Employee ID");
			if(empName != null) headers.add("Name");
			if(phone != null) headers.add("Phone");
			if(pay != null) headers.add("Pay");
			if(paystubNum != null) headers.add("Paystub");
			if(hoursWorked != null) headers.add("Hours Worked");
			if(dayWorked != null) headers.add("Day Worked");
			if(jobsiteName != null) headers.add("Jobsite Name");
			if(isContract != null) headers.add("Contract?");
			if(active != null) headers.add("Status");
		}
		this.headers = headers;
	}
	
	private SearchCriteria getCriteria(SearchRequest request) {
		String empId = request.getEmpIdSelected() ? request.getEmpId() : null;
		String empPhone = request.getPhoneSelected() ? request.getPhone() : null;
		
		Double pay;
		if(request.getPaySelected()) {
			pay = request.getPay().equals("any") ? 0.0 : Double.parseDouble(request.getPay());
		}
		else {
			pay = null;
		}
		
		String payStubNum = request.getPaystubNumSelected() ? request.getPaystubNum() : null;
		
		Double hoursWorked;
		if(request.getHoursSelected()) {
			hoursWorked = request.getHoursWorked().equals("any") ? 0.0 : Double.parseDouble(request.getHoursWorked());
		}
		else {
			hoursWorked = null;
		}
		
		LocalDate dayWorked;
		LocalDate endDayWorked;
		if(request.getDaySelected()) {
			CharSequence start = "1970-01-01";
			if(request.getDayWorked().equals("any")) {
				dayWorked = LocalDate.parse(start);
				endDayWorked = dayWorked;
			}
			else {
				dayWorked = LocalDate.parse(request.getDayWorked());
				endDayWorked = request.getEndDayWorked().isBlank() ? dayWorked : LocalDate.parse(request.getEndDayWorked());
			}
		}
		else {
			dayWorked = null;
			endDayWorked = null;
		}
		
		String jobsite = request.getJobsiteSelected() ? request.getJobsiteName() : null;
		Boolean contract = request.getContractSelected() ? request.getIsContract() : null;
		Boolean status = request.getStatusSelected() ? request.getActive() : null;
		SearchCriteria newCriteria;
		
		if(request.getPaystubChecked()) {
			String fullName = request.getNameSelected() ? request.getEmpFName() + " " + request.getEmpLName() : null;
			newCriteria = new SearchCriteria(
					request.getEmpChecked(),
					empId,
					fullName,
					empPhone,
					request.getPaystubChecked(),
					pay,
					payStubNum,
					hoursWorked,
					dayWorked,
					endDayWorked,
					request.getJobsiteChecked(),
					jobsite,
					contract,
					status);
		}
		else {
			String firstName = request.getNameSelected() ? request.getEmpFName() : null;
			String lastName = request.getNameSelected() ? request.getEmpLName() : null;
			newCriteria = new SearchCriteria(
					request.getEmpChecked(),
					empId,
					firstName,
					lastName,
					empPhone,
					request.getPaystubChecked(),
					pay,
					payStubNum,
					hoursWorked,
					dayWorked,
					endDayWorked,
					request.getJobsiteChecked(),
					jobsite,
					contract,
					status);
		}
		
		return newCriteria;
	}
	
	private SearchResults getResponse(SearchCriteria criteria) {
		if(criteria.getFullName() != null) {
			SearchResults criteriaResponse = new SearchResults(
					criteria.getEmpId(),
					criteria.getFullName(),
					criteria.getPhone(),
					criteria.getPay(),
					criteria.getPaystubNum(),
					criteria.getHoursWorked(),
					criteria.getDayWorked(),
					criteria.getJobsiteName(),
					criteria.getIsContract(),
					criteria.getActive());
			return criteriaResponse;
		}
		String fullName = null;
		if(criteria.getFirstName() != null && criteria.getLastName() != null) {
			fullName = String.format("%s %s", criteria.getFirstName(), criteria.getLastName());
		}
		SearchResults criteriaResponse = new SearchResults(
				criteria.getEmpId(),
				fullName,
				criteria.getPhone(),
				criteria.getPay(),
				criteria.getPaystubNum(),
				criteria.getHoursWorked(),
				criteria.getDayWorked(),
				criteria.getJobsiteName(),
				criteria.getIsContract(),
				criteria.getActive());
		return criteriaResponse;
	}

	public List<SearchResults> getSearchResults() {
		return searchResults;
	}

	public void setSearchResults(List<SearchResults> searchResults) {
		this.searchResults = searchResults;
	}

	public List<String> getHeaders() {
		return headers;
	}
}