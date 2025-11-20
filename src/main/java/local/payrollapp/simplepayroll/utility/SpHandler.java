package local.payrollapp.simplepayroll.utility;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import local.payrollapp.simplepayroll.employees.EmployeeRequest;
import local.payrollapp.simplepayroll.searchcriteria.SearchRequest;

@Component
public class SpHandler {
	
	private static final Logger log = LoggerFactory.getLogger(SpHandler.class);
	
	private List<ObjectError> errList = new ArrayList<ObjectError>();
	private ObjectError error; 
	private List<String> readibleErr = new ArrayList<String>();
	private boolean hasError;
	
	/**
	 * @return the error
	 */
	public ObjectError getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(ObjectError error) {
		this.error = error;
	}

	/**
	 * @return the errList
	 */
	public List<ObjectError> getErrList() {
		return errList;
	}

	/**
	 * @param errList the errList to set
	 */
	public void setErrList(List<ObjectError> errList) {
		this.errList = errList;
	}

	/**
	 * @return the readibleErr
	 */
	public List<String> getReadibleErr() {
		return readibleErr;
	}

	/**
	 * @param readibleErr the readibleErr to set
	 */
	public void setReadibleErr(List<String> readibleErr) {
		this.readibleErr = readibleErr;
	}
	
	/**
	 * @return the hasError
	 */
	public boolean isHasError() {
		return hasError;
	}

	/**
	 * @param hasError the hasError to set
	 */
	public void setHasError(boolean hasError) {
		this.hasError = hasError;
	}

	public SpHandler() { super(); }
	
	public void requestHandler(Object request, BindingResult result) {
		if(result.hasErrors()) {
		//class has predefined constraints
		List<String> errList = this.HumanReadibleErrors(result.getAllErrors());
		this.setReadibleErr(errList);
		this.setHasError(true);
		String errs = String.join(",", errList);
		log.error(String.format("Error(s) %s: ", errs));
		}
		else if(request instanceof SearchRequest) {
			SearchRequest req = (SearchRequest) request;
			this.validSearchRequest(req);
		}
		else if(request instanceof EmployeeRequest) {
			EmployeeRequest req = (EmployeeRequest) request;
			String phone = req.getPhone() != null ? req.getPhone() : "";
			if(!phone.isBlank()) {
				this.checkEmpPhone(req.getPhone());
			}
		}
		else if(allNull(request)) {
			List<String> objErrList = new ArrayList<String>();
			this.setError(new ObjectError(String.format("Error with %s", request.getClass().getName()),"invalid request..."));
			objErrList.add(this.error.getDefaultMessage());
			this.readibleErr = objErrList;
			this.hasError = true;
		}
		else {
			this.hasError = false;
		}
	}
	
	public void responseHandler(Object response) {
		if(allNull(response)) {
			List<String> errList = new ArrayList<String>();
			this.setError(new ObjectError(String.format("Error with %s:", response.getClass().getName()),"invalid request..."));
			errList.add(this.error.getDefaultMessage());
			this.readibleErr = errList;
			this.hasError = true;
			String errs = String.join(",", errList);
			log.error(String.format("Error(s): %s ", errs));
		}
		else {
			this.hasError = false;
		}
	}
	
	//overloaded to account for many records
	public <T> void responseHandler(List<T> responses) {
		List<ObjectError> objErrList = this.errList;
		if(responses.size() == 0) {
			objErrList.add(new ObjectError(String.format("Error with %s record:", responses.getClass().getName()),"Response was empty. Try another Search."));
			this.hasError = true;
		}
		
		for(Object response : responses) {
			if(allNull(response)) {
				objErrList.add(new ObjectError(String.format("Error with %s record:", response.getClass().getName()),"Response was empty. Try another Search."));
				this.hasError = true;
				break;
			}
			else {
				this.hasError = false;
				break;
			}
		}

		if(hasError) {
			this.readibleErr = this.HumanReadibleErrors(this.errList);
			String errs = String.join(",", this.readibleErr);
			log.error(String.format("Error(s): %s",errs));
		}
	}
	
	private void validSearchRequest(SearchRequest request) {
		List<ObjectError> objErrList = this.getErrList();
		
		if(!request.getEmpChecked() && !request.getPaySelected() && !request.getJobsiteChecked()) {
			objErrList.add(new ObjectError("Error with Search:","No tables selected. You must 'check' atleast one table"));
			this.setHasError(true);
		}
    
		if(request.getEmpIdSelected() && request.getEmpId().isBlank()) {
			objErrList.add(new ObjectError("Error with Employee ID:","You must provide an ID, otherwise use \"any\""));
			this.setHasError(true);
		}
    
		if(request.getNameSelected() && request.getEmpFName().isBlank() && request.getEmpLName().isBlank()) {
			objErrList.add(new ObjectError("Error with Employee Name:","You must provide an Employee Name, otherwise use \"any\""));
			if(!hasError) {
				this.setHasError(true);
			}
		}

		if(request.getPhoneSelected() && request.getPhone().isBlank()) {
			objErrList.add(new ObjectError("Error with Phone:","You must provide a Phone number, otherwise use \"any\""));
			if(!hasError) {
				this.setHasError(true);
			}
		}
    
		if(request.getPaySelected() && request.getPay().isBlank()) {
			objErrList.add(new ObjectError("Error with Pay:","You must enter a value using $0.00, otherwise use \"any\""));
			if(!hasError) {
				this.setHasError(true);
			}
		}

		if(request.getPaystubNumSelected() && request.getPaystubNum().isBlank()) {
			objErrList.add(new ObjectError("Error with Paystub Number:","Cannot be blank. You must enter a paystub number, otherwise use \"any\""));
			if(!hasError) {
				this.setHasError(true);
			}
		}

		if(request.getHoursSelected() && request.getHoursWorked().isBlank()) {
			objErrList.add(new ObjectError("Error with Hours Worked:","Cannot be blank. You must enter a value using 0.0, otherwise use \"any\""));
			if(!hasError) {
				this.setHasError(true);
			}
		}

		if(request.getDaySelected() && request.getDayWorked().isBlank()) {
			objErrList.add(new ObjectError("Error with Day Worked:","Cannot be blank. You must enter a value using dd/mm/yyyy, otherwise use \"any\""));
			if(!hasError) {
				this.setHasError(true);
			}
		}

		if(request.getJobsiteSelected() && request.getJobsiteName().isBlank()) {
			objErrList.add(new ObjectError("Error with Jobsite:","Cannot be blank. You must enter a jobsite, otherwise use \"any\""));
			if(!hasError) {
				this.setHasError(true);
			}
		}

		if(request.getContractSelected() && request.getIsContract() == null) {
			objErrList.add(new ObjectError("Error with Contract:","If you selected contract status you must choose active or inactive."));
			if(!hasError) {
				this.setHasError(true);
			}
		}

		if(request.getStatusSelected() && request.getActive() == null) {
			objErrList.add(new ObjectError("Error with Active:","If you selected active status you must choose active or inactive."));
			if(!hasError) {
				this.setHasError(true);
			}
		}

		if(hasError) {
			this.readibleErr = this.HumanReadibleErrors(objErrList);
			String errs = String.join(",", this.readibleErr);
			log.error(String.format("Error(s): %s",errs));
		}
	}
	
	public void clear() {
		this.errList.clear();
		this.readibleErr.clear();
		this.error = null;
		this.hasError = false;
	}

	private boolean allNull(Object classObj) {
		return Arrays.stream(
				classObj.getClass()
				.getDeclaredFields())
				.peek(f -> f.setAccessible(true))
				.map(f -> {
					try {
						return getFieldValue(f, classObj);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					return f;
				})
				.allMatch(Objects::isNull);
	}
	
	private Object getFieldValue(Field field, Object target) throws IllegalAccessException {
		try {
			Object obj = field.get(target);
			return obj;
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}
	
	private void checkEmpPhone(String phone) {
		List<ObjectError> objErrList = this.getErrList();
		if(phone.length() > 0 && phone.length() < 12) {
			objErrList.add(new ObjectError("Incomplete Phone Number:","The employee phone created has too few digits. Expected: ###-###-####"));
			this.hasError = Boolean.TRUE;
			this.readibleErr = this.HumanReadibleErrors(objErrList);
		}
	}
	
	private String formatErr(String with, String msg) {
		return String.format("Error with %s record: %s", with, msg);
	}
	
	private List<String> HumanReadibleErrors(List<ObjectError> errors) {
		List<String> errs = new ArrayList<String>();
		for(ObjectError error : errors) {
			errs.add(error.getDefaultMessage());
		}
		return errs;
	}
}