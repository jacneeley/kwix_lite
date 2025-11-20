package local.payrollapp.simplepayroll.paystub;

import java.time.LocalDate;

public record PaystubResponse(
		String paystubNum,
		String employeeId,
		String fullName,
		String jobsite,
		double pay,
		double hoursWorked,
		boolean active,
		LocalDate dayWorked,
		LocalDate createAt,
		LocalDate updateAt) {}