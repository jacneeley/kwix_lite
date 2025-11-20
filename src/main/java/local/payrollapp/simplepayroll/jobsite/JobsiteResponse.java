package local.payrollapp.simplepayroll.jobsite;

import java.time.LocalDate;

public record JobsiteResponse (
	String id,
	String jobsiteName,
	Boolean contract,
	boolean active,
	LocalDate createdAt,
	LocalDate updatedAt) {}