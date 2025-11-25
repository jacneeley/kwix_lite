package local.payrollapp.simplepayroll.jobsite;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import local.payrollapp.simplepayroll.exceptions.ElementNotFoundException;
import local.payrollapp.simplepayroll.paystub.PaystubSrv;

@Service
public class JobsiteSrv implements IJobsiteSrv{
	private static final Logger log = LoggerFactory.getLogger(JobsiteSrv.class);
	private final JobsiteRepo _jobsiteRepo;
	
	JobsiteSrv(JobsiteRepo jobsiteRepo){
		this._jobsiteRepo = jobsiteRepo;
	}
	
	@Override
	public Optional<Jobsite> findJobsiteById(String id, boolean active) {
		try {
			return _jobsiteRepo.findJobsiteById(id, active);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new ElementNotFoundException("Paystubs could not be found...");
		}
	}

	@Override
	public List<Jobsite> findAllJobsitesByActive(boolean active) {
		try {
			return _jobsiteRepo.findAllJobsitesByActive(active);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new ElementNotFoundException("Paystubs could not be found...");
		}
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
		try {
			_jobsiteRepo.CreateJobsite(js);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new ElementNotFoundException("Paystubs could not be found...");
		}
	}

	@Override
	public void UpdateJobsite(Jobsite jobsite) {
		try {
			_jobsiteRepo.UpdateJobsite(jobsite, jobsite.getId());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new ElementNotFoundException("Paystubs could not be found...");
		}
	}

	@Override
	public void DeleteJobsite(String id) {
		try {
			_jobsiteRepo.DeleteJobsite(id);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new ElementNotFoundException("Paystubs could not be found...");
		}
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
}