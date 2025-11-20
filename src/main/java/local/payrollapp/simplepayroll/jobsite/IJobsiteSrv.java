package local.payrollapp.simplepayroll.jobsite;

import java.util.List;
import java.util.Optional;

public interface IJobsiteSrv {
	Optional<Jobsite> findJobsiteById(String id, boolean active);
	List<Jobsite> findAllJobsitesByActive (boolean active);
	void CreateJobsite(Jobsite jobsite);
	void UpdateJobsite(Jobsite jobsite);
	void DeleteJobsite(String id);
}