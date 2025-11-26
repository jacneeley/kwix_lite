package local.payrollapp.simplepayroll.jobsite;

import java.util.List;
import java.util.Optional;

public interface IJobsiteRepo {
	Optional<Jobsite> findJobsiteById(String id, boolean active) throws Exception;
	List<Jobsite> findAllJobsitesByActive(boolean active) throws Exception;
	void CreateJobsite(Jobsite jobsite) throws Exception;
	void UpdateJobsite(Jobsite jobsite , String id) throws Exception;
	void DeleteJobsite(String id) throws Exception;
}