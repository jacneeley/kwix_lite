package local.payrollapp.simplepayroll.searchcriteria;

public class SearchRequest {
	private Boolean empChecked = false;
	private Boolean empIdSelected = false;
	private String empId;
	private Boolean nameSelected = false;
	private String empFName;
	private String empLName;
	private Boolean phoneSelected = false;
	private String phone;
	private Boolean paystubChecked = false;
	private Boolean paySelected = false;
	private String pay;
	private Boolean paystubNumSelected = false;
	private String paystubNum;
	private Boolean hoursSelected = false;
	private String hoursWorked;
	private Boolean daySelected = false;
	private String dayWorked;
	private String endDayWorked;
	private Boolean jobsiteChecked = false;
	private Boolean jobsiteSelected = false;
	private String jobsiteName;
	private Boolean contractSelected = false;
	private Boolean isContract;
	private Boolean statusSelected = false;
	private Boolean active;
	
	/**
	 * @return the empIdSelected
	 */
	public Boolean getEmpIdSelected() {
		return empIdSelected;
	}
	
	/**
	 * @param empIdSelected the empIdSelected to set
	 */
	public void setEmpIdSelected(Boolean empIdSelected) {
		this.empIdSelected = empIdSelected;
	}
	
	/**
	 * @return the empId
	 */
	public String getEmpId() {
		return empId;
	}
	
	/**
	 * @param empId the empId to set
	 */
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	
	/**
	 * @return the nameSelected
	 */
	public Boolean getNameSelected() {
		return nameSelected;
	}
	
	/**
	 * @param nameSelected the nameSelected to set
	 */
	public void setNameSelected(Boolean nameSelected) {
		this.nameSelected = nameSelected;
	}
	
	/**
	 * @return the empFName
	 */
	public String getEmpFName() {
		return empFName;
	}
	
	/**
	 * @param empFName the empFName to set
	 */
	public void setEmpFName(String empFName) {
		this.empFName = empFName;
	}
	
	/**
	 * @return the empLName
	 */
	public String getEmpLName() {
		return empLName;
	}
	
	/**
	 * @param empLName the empLName to set
	 */
	public void setEmpLName(String empLName) {
		this.empLName = empLName;
	}
	
	/**
	 * @return the phoneSelected
	 */
	public Boolean getPhoneSelected() {
		return phoneSelected;
	}
	
	/**
	 * @param phoneSelected the phoneSelected to set
	 */
	public void setPhoneSelected(Boolean phoneSelected) {
		this.phoneSelected = phoneSelected;
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
	 * @return the paySelected
	 */
	public Boolean getPaySelected() {
		return paySelected;
	}
	
	/**
	 * @param paySelected the paySelected to set
	 */
	public void setPaySelected(Boolean paySelected) {
		this.paySelected = paySelected;
	}
	
	/**
	 * @return the pay
	 */
	public String getPay() {
		return pay;
	}
	
	/**
	 * @param pay the pay to set
	 */
	public void setPay(String pay) {
		this.pay = pay;
	}
	
	/**
	 * @return the paystubNumSelected
	 */
	public Boolean getPaystubNumSelected() {
		return paystubNumSelected;
	}
	
	/**
	 * @param paystubNumSelected the paystubNumSelected to set
	 */
	public void setPaystubNumSelected(Boolean paystubNumSelected) {
		this.paystubNumSelected = paystubNumSelected;
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
	public Boolean getHoursSelected() {
		return hoursSelected;
	}
	
	/**
	 * @param hoursSelected the hoursSelected to set
	 */
	public void setHoursSelected(Boolean hoursSelected) {
		this.hoursSelected = hoursSelected;
	}
	
	/**
	 * @return the daySelected
	 */
	public Boolean getDaySelected() {
		return daySelected;
	}

	/**
	 * @param daySelected the daySelected to set
	 */
	public void setDaySelected(Boolean daySelected) {
		this.daySelected = daySelected;
	}

	/**
	 * @return the hoursWorked
	 */
	public String getHoursWorked() {
		return hoursWorked;
	}
	
	/**
	 * @param hoursWorked the hoursWorked to set
	 */
	public void setHoursWorked(String hoursWorked) {
		this.hoursWorked = hoursWorked;
	}
	
	/**
	 * @return the dayWorked
	 */
	public String getDayWorked() {
		return dayWorked;
	}

	/**
	 * @param dayWorked the dayWorked to set
	 */
	public void setDayWorked(String dayWorked) {
		this.dayWorked = dayWorked;
	}

	public String getEndDayWorked() {
		return endDayWorked;
	}

	public void setEndDayWorked(String endDayWorked) {
		this.endDayWorked = endDayWorked;
	}
  
	/**
	 * @return the jobsiteSelected
	 */
	public Boolean getJobsiteSelected() {
		return jobsiteSelected;
	}
	
	/**
	 * @param jobsiteSelected the jobsiteSelected to set
	 */
	public void setJobsiteSelected(Boolean jobsiteSelected) {
		this.jobsiteSelected = jobsiteSelected;
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
	 * @return the contractSelected
	 */
	public Boolean getContractSelected() {
		return contractSelected;
	}
	
	/**
	 * @param contractSelected the contractSelected to set
	 */
	public void setContractSelected(Boolean contractSelected) {
		this.contractSelected = contractSelected;
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
	 * @return the statusSelected
	 */
	public Boolean getStatusSelected() {
		return statusSelected;
	}
	
	/**
	 * @param statusSelected the statusSelected to set
	 */
	public void setStatusSelected(Boolean statusSelected) {
		this.statusSelected = statusSelected;
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
}