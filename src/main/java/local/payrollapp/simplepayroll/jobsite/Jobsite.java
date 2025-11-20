package local.payrollapp.simplepayroll.jobsite;

import java.time.LocalDate;

public class Jobsite {
	private String id;
	private String jobsiteName;
	private Boolean isContract;
	private boolean active;
	private LocalDate createdAt;
	private LocalDate updatedAt;
	
	public String getId() {
		return id;
	}
	
	public String getJobsiteName() {
		return jobsiteName;
	}
	
	public Boolean isContract() {
		return isContract;
	}

	public boolean isActive() {
		return active;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public LocalDate getUpdatedAt() {
		return updatedAt;
	}

	public String generateId() {
	    long generatedId = 1000L + (long) (Math.random() * (10000L - 1000L));
	    return String.valueOf(generatedId);
	}

	public Jobsite(String id, String jobsiteName, Boolean isContract, boolean active, LocalDate createdAt,
			LocalDate updatedAt) {
		super();
		this.id = id;
		this.jobsiteName = jobsiteName;
		this.isContract = isContract;
		this.active = active;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "Jobsite [id=" + id + ", jobsite name=" + jobsiteName + ", isContract=" + isContract + ", active=" + active
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
}