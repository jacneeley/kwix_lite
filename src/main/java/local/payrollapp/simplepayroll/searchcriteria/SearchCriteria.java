package local.payrollapp.simplepayroll.searchcriteria;

import java.time.LocalDate;

public class SearchCriteria {
	private Boolean empChecked = false;
	private String id;
	private String firstName;
	private String lastName;
	private String fullName;
	private String phone;
	private Boolean paystubChecked = false;
	private Double pay;
	private String paystubNum;
	private Double hoursWorked;
	private LocalDate dayWorked;
	private LocalDate endDayWorked;
	private Boolean jobsiteChecked = false;
	private String jobsiteName;
	private Boolean isContract;
	private Boolean active;
	
	/**
	 * @return the empId
	 */
	public String getEmpId() {
		return id;
	}
	
	/**
	 * @param empId the empId to set
	 */
	public void setEmpId(String id) {
		this.id = id;
	}
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	/**
	 * @return the pay
	 */
	public Double getPay() {
		return pay;
	}
	
	/**
	 * @param pay the pay to set
	 */
	public void setPay(Double pay) {
		this.pay = pay;
	}
	
	/**
	 * @return the paystubNum
	 */
	public String getPaystubNum() {
		return paystubNum;
	}
	
	/**
	 * @param paystubNum the paystubNum to set
	 */
	public void setPaystubNum(String paystubNum) {
		this.paystubNum = paystubNum;
	}

	/**
	 * @return the hoursSelected
	 */
	public Double getHoursWorked() {
		return hoursWorked;
	}
	
	/**
	 * @param hoursWorked the hoursWorked to set
	 */
	public void setHoursWorked(Double hoursWorked) {
		this.hoursWorked = hoursWorked;
	}

	/**
	 * @return the dayWorked
	 */
	public LocalDate getDayWorked() {
		return dayWorked;
	}
	
	/**
	 * @param dayWorked the dayWorked to set
	 */
	public void setDayWorked(LocalDate dayWorked) {
		this.dayWorked = dayWorked;
	}
	
	/**
	 * @return the endDayWorked
	 */
	public LocalDate getEndDayWorked() {
		return endDayWorked;
	}

	/**
	 * @param endDayWorked the endDayWorked to set
	 */
	public void setEndDayWorked(LocalDate endDayWorked) {
		this.endDayWorked = endDayWorked;
	}
  
	/**
	 * @return the jobsiteName
	 */
	public String getJobsiteName() {
		return jobsiteName;
	}
	
	/**
	 * @param jobsiteName the jobsiteName to set
	 */
	public void setJobsiteName(String jobsiteName) {
		this.jobsiteName = jobsiteName;
	}
	
	/**
	 * @return the isContract
	 */
	public Boolean getIsContract() {
		return isContract;
	}
	
	/**
	 * @param isContract the isContract to set
	 */
	public void setIsContract(Boolean isContract) {
		this.isContract = isContract;
	}
	
	/**
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
	}
	
	/**
	 * @param active the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	
	
	/**
	 * @return the empChecked
	 */
	public Boolean getEmpChecked() {
		return empChecked;
	}

	/**
	 * @param empChecked the empChecked to set
	 */
	public void setEmpChecked(Boolean empChecked) {
		this.empChecked = empChecked;
	}

	/**
	 * @return the paystubChecked
	 */
	public Boolean getPaystubChecked() {
		return paystubChecked;
	}

	/**
	 * @param paystubChecked the paystubChecked to set
	 */
	public void setPaystubChecked(Boolean paystubChecked) {
		this.paystubChecked = paystubChecked;
	}

	/**
	 * @return the jobsiteChecked
	 */
	public Boolean getJobsiteChecked() {
		return jobsiteChecked;
	}

	/**
	 * @param jobsiteChecked the jobsiteChecked to set
	 */
	public void setJobsiteChecked(Boolean jobsiteChecked) {
		this.jobsiteChecked = jobsiteChecked;
	}

	/**
	 * no-arg constructor
	 */
	public SearchCriteria() {
		super();
	}

	/**
	 * @param empChecked
	 * @param id
	 * @param fullName
	 * @param phone
	 * @param paystubChecked
	 * @param pay
	 * @param paystubNum
	 * @param hoursWorked
	 * @param dayWorked
	 * @param jobsiteChecked
	 * @param jobsiteName
	 * @param isContract
	 * @param active
	 */

	public SearchCriteria(Boolean empChecked, String id, String fullName, String phone, Boolean paystubChecked,
			Double pay, String paystubNum, Double hoursWorked, LocalDate dayWorked, Boolean jobsiteChecked,
			String jobsiteName, Boolean isContract, Boolean active) {
		super();
		this.empChecked = empChecked;
		this.id = id;
		this.fullName = fullName;
		this.phone = phone;
		this.paystubChecked = paystubChecked;
		this.pay = pay;
		this.paystubNum = paystubNum;
		this.hoursWorked = hoursWorked;
		this.dayWorked = dayWorked;
		this.jobsiteChecked = jobsiteChecked;
		this.jobsiteName = jobsiteName;
		this.isContract = isContract;
		this.active = active;
	}

	/**
	 * @param empChecked
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param phone
	 * @param paystubChecked
	 * @param pay
	 * @param paystubNum
	 * @param hoursWorked
	 * @param dayWorked
	 * @param jobsiteChecked
	 * @param jobsiteName
	 * @param isContract
	 * @param active
	 */
	public SearchCriteria(Boolean empChecked, String id, String firstName, String lastName, String phone,
			Boolean paystubChecked, Double pay, String paystubNum, Double hoursWorked, LocalDate dayWorked,
			Boolean jobsiteChecked, String jobsiteName, Boolean isContract, Boolean active) {
		super();
		this.empChecked = empChecked;
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.paystubChecked = paystubChecked;
		this.pay = pay;
		this.paystubNum = paystubNum;
		this.hoursWorked = hoursWorked;
		this.dayWorked = dayWorked;
		this.jobsiteChecked = jobsiteChecked;
		this.jobsiteName = jobsiteName;
		this.isContract = isContract;
		this.active = active;
	}

	/**
	 * @param empChecked
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param fullName
	 * @param phone
	 * @param paystubChecked
	 * @param pay
	 * @param paystubNum
	 * @param hoursWorked
	 * @param dayWorked
	 * @param endDayWorked
	 * @param jobsiteChecked
	 * @param jobsiteName
	 * @param isContract
	 * @param active
	 */
	public SearchCriteria(Boolean empChecked, String id, String firstName, String lastName,
			String phone, Boolean paystubChecked, Double pay, String paystubNum, Double hoursWorked,
			LocalDate dayWorked, LocalDate endDayWorked, Boolean jobsiteChecked, String jobsiteName, Boolean isContract,
			Boolean active) {
		super();
		this.empChecked = empChecked;
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.paystubChecked = paystubChecked;
		this.pay = pay;
		this.paystubNum = paystubNum;
		this.hoursWorked = hoursWorked;
		this.dayWorked = dayWorked;
		this.endDayWorked = endDayWorked;
		this.jobsiteChecked = jobsiteChecked;
		this.jobsiteName = jobsiteName;
		this.isContract = isContract;
		this.active = active;
	}
	
	/**
	 * @param empChecked
	 * @param id
	 * @param fullName
	 * @param phone
	 * @param paystubChecked
	 * @param pay
	 * @param paystubNum
	 * @param hoursWorked
	 * @param dayWorked
	 * @param endDayWorked
	 * @param jobsiteChecked
	 * @param jobsiteName
	 * @param isContract
	 * @param active
	 */
	public SearchCriteria(Boolean empChecked, String id, String fullName, String phone, Boolean paystubChecked,
			Double pay, String paystubNum, Double hoursWorked, LocalDate dayWorked, LocalDate endDayWorked,
			Boolean jobsiteChecked, String jobsiteName, Boolean isContract, Boolean active) {
		super();
		this.empChecked = empChecked;
		this.id = id;
		this.fullName = fullName;
		this.phone = phone;
		this.paystubChecked = paystubChecked;
		this.pay = pay;
		this.paystubNum = paystubNum;
		this.hoursWorked = hoursWorked;
		this.dayWorked = dayWorked;
		this.endDayWorked = endDayWorked;
		this.jobsiteChecked = jobsiteChecked;
		this.jobsiteName = jobsiteName;
		this.isContract = isContract;
		this.active = active;
	}

	@Override
	public String toString() {
		fullName = fullName == "" ? firstName + " " + lastName : fullName;
		return "SearchCriteria [empId=" + id + ", name=" + fullName + ", phone=" + phone + ", pay=" + pay
				+ ", paystubNum=" + paystubNum + ", hoursWorked=" + hoursWorked + ", dayWorked=" + dayWorked
				+ ", jobsiteName=" + jobsiteName + ", isContract=" + isContract + ", active=" + active + "]";
	}
}