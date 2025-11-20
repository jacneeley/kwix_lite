package local.payrollapp.simplepayroll.jobsite;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import local.payrollapp.simplepayroll.exceptions.ElementNotFoundException;
import local.payrollapp.simplepayroll.paystub.PayrollController;

@Controller
public class JobsiteController {
	
	private final JobsiteSrv _jobsiteSrv;
	private static final Logger log = LoggerFactory.getLogger(JobsiteController.class);
	
	JobsiteController(JobsiteSrv jobsiteSrv){
		this._jobsiteSrv = jobsiteSrv;
	}
	
	public List<JobsiteResponse> getAllJobsitesByActive(boolean active){
		List<Jobsite> sites = _jobsiteSrv.findAllJobsitesByActive(active);
		List<JobsiteResponse> result = new ArrayList<JobsiteResponse>();
		for (Jobsite site : sites) {
			JobsiteResponse response = getResponse(site);
			result.add(response);
		}
		return result;
	}
	
	public JobsiteResponse getJobsiteById(String id, boolean active){
		Jobsite site = _jobsiteSrv.findJobsiteById(id, active).orElseThrow(() -> new ElementNotFoundException(String.format("jobsite: %s could not be found...", id)));
		JobsiteResponse response = getResponse(site);
		return response;
		
	}
	
	public JobsiteResponse createJobsite(JobsiteRequest request) {
		Jobsite site = new Jobsite(
				"",
				request.getJobsiteName().toUpperCase(),
				request.isContract(),
				Boolean.TRUE,
				LocalDate.now(),
				LocalDate.now());
		_jobsiteSrv.CreateJobsite(site);
		JobsiteResponse response = getResponse(site);
		return response;
	}
	
	public JobsiteResponse updateJobsite(String id, JobsiteRequest request) {
		Jobsite oldSite = _jobsiteSrv.findJobsiteById(id, true).orElseThrow(() -> new ElementNotFoundException(String.format("jobsite: %s could not be found...", id)));
		Jobsite updatedSite = new Jobsite(
				oldSite.getId(),
				request.getJobsiteName(),
				request.isContract(),
				request.isActive(),
				oldSite.getCreatedAt(),
				LocalDate.now());
		_jobsiteSrv.UpdateJobsite(updatedSite);
		JobsiteResponse response = getResponse(updatedSite);
		return response;
	}
	
	public void deleteJobsite(String id) {
		try {
			_jobsiteSrv.DeleteJobsite(id);
		} catch(ElementNotFoundException ex) {
			log.error(ex.toString());
			ex.printStackTrace();
		}
	}
	
	public void markJobsiteForDelete(String id) {
		
	}
	
	public void restoreJobsite(String id) {
		Jobsite site = _jobsiteSrv.findJobsiteById(id, false).orElseThrow(() -> new ElementNotFoundException(String.format("Deleted Jobsite: %s could not be found...", id)));
		Jobsite restoreSite = new Jobsite(
				site.getId(),
				site.getJobsiteName(),
				site.isContract(),
				Boolean.TRUE,
				site.getCreatedAt(),
				LocalDate.now());
		_jobsiteSrv.UpdateJobsite(restoreSite);
	}
	
	private JobsiteResponse getResponse(Jobsite jobsite) {
		JobsiteResponse siteResponse = new JobsiteResponse(
				jobsite.getId(),
				jobsite.getJobsiteName(),
				jobsite.isContract(),
				jobsite.isActive(),
				jobsite.getCreatedAt(),
				jobsite.getUpdatedAt());
		return siteResponse;
	}
	
	public List<String> getJobsiteNames(){
		return _jobsiteSrv.getJobsiteNames();
	}
	
	public void clear() {
		this._jobsiteSrv.clear();
	}
}
