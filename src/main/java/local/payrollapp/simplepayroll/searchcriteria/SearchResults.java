package local.payrollapp.simplepayroll.searchcriteria;

import java.time.LocalDate;

public record SearchResults(
		 String empId,
		 String empName,
		 String phone,
		 Double pay,
		 String paystubNum,
		 Double hoursWorked,
		 LocalDate dayWorked,
		 String jobsiteName,
		 Boolean isContract,
		 Boolean active) {

	public SearchResults{}
}