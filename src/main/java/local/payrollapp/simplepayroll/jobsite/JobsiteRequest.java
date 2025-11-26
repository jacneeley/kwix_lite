package local.payrollapp.simplepayroll.jobsite;

import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class JobsiteRequest {
	private String id;
	
	@NotEmpty(message = "Jobsite cannot be empty.")
	@Size(min=1, max=100, message="Jobsite name must be between 1 and 100 characters")
	private String jobsiteName;
	
	private boolean contract;
	
	private boolean active;
	
	private LocalDate createdAt;
	
	private LocalDate updatedAt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJobsiteName() {
		return jobsiteName;
	}

	public void setJobsiteName(String jobsiteName) {
		this.jobsiteName = jobsiteName;
	}

	public boolean isContract() {
		return contract;
	}

	public void setContract(boolean contract) {
		this.contract = contract;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDate getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDate updatedAt) {
		this.updatedAt = updatedAt;
	}
}
