package local.payrollapp.simplepayroll.jobsite;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class JobsiteSrv implements IJobsiteSrv{
	
	private final JobsiteRepo _jobsiteRepo;
	
	JobsiteSrv(JobsiteRepo jobsiteRepo){
		this._jobsiteRepo = jobsiteRepo;
	}
	
	@Override
	public Optional<Jobsite> findJobsiteById(String id, boolean active) {
		return _jobsiteRepo.findJobsiteById(id, active);
	}

	@Override
	public List<Jobsite> findAllJobsitesByActive(boolean active) {
		return _jobsiteRepo.findAllJobsitesByActive(active);
	}

	@Override
	public void CreateJobsite(Jobsite jobsite) {
		Jobsite js = new Jobsite(
				jobsite.generateId(),
				jobsite.getJobsiteName(),
				jobsite.isContract(),
				jobsite.isActive(),
				jobsite.getCreatedAt(),
				jobsite.getUpdatedAt());
		_jobsiteRepo.CreateJobsite(js);
	}

	@Override
	public void UpdateJobsite(Jobsite jobsite) {
		_jobsiteRepo.UpdateJobsite(jobsite, jobsite.getId());
	}

	@Override
	public void DeleteJobsite(String id) {
		_jobsiteRepo.DeleteJobsite(id);
	}
	
	public List<String> getJobsiteNames() {
		List<Jobsite> sites = this.findAllJobsitesByActive(true);
		List<String> siteNames = new ArrayList<String>();
		for (Jobsite site : sites) {
			siteNames.add(site.getJobsiteName());
		}
		return siteNames; 
	}
	
	public void clear() {
		this._jobsiteRepo.clear();
	}
	
//	@PostConstruct
//	private void init() {
//		boolean isEmpty = this.findAllJobsitesByActive(true).isEmpty();
//		
//		if(isEmpty) {
//			String[] siteNames = {
//					"HATHAWAY","CATINA","HIGHRD",
//					"VASSAR","HIGHLAND","LENNOX","OTHER"
//				};
//			for(int i = 0; i < siteNames.length; i++) {
//				Jobsite newSite = new Jobsite(
//						"",
//						siteNames[i],
//						Boolean.FALSE,
//						Boolean.TRUE,
//						LocalDate.now(),
//						LocalDate.now());
//				this.CreateJobsite(newSite);
//			}
//		}
//	}
}