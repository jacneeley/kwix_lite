package local.payrollapp.simplepayroll.jobsite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import local.payrollapp.simplepayroll.components.MockDatabase;

@Repository
public class JobsiteRepo implements IJobsiteRepo{
	
	private final MockDatabase _mockDB;
	
	public JobsiteRepo(MockDatabase mockDb) {
		this._mockDB = mockDb;
	}
	
	@Override
	public Optional<Jobsite> findJobsiteById(String jobsiteId, boolean active) throws Exception {
		Jobsite jobsite = this._mockDB.getJobsites().get(jobsiteId);
		if(jobsite == null) {
			return Optional.empty();
		}
		if(jobsite.isActive() == active) {
			return Optional.of(jobsite);
		}
		return Optional.empty();
	}

	@Override
	public List<Jobsite> findAllJobsitesByActive(boolean active) throws Exception {
		List<Jobsite> stubs = this._mockDB.getJobsites().entrySet()
				.stream()
				.filter(p -> p.getValue().isActive() == active)
				.map(Map.Entry::getValue)
				.collect(Collectors.toList());
		return stubs;
	}

	@Override
	public void CreateJobsite(Jobsite jobsite) throws Exception {
		this._mockDB.getJobsites().put(jobsite.getId(), jobsite);
	}

	@Override
	public void UpdateJobsite(Jobsite jobsite, String id) throws Exception {
		this._mockDB.getJobsites().put(jobsite.getId(), jobsite);
	}

	@Override
	public void DeleteJobsite(String id) throws Exception {
		this._mockDB.getJobsites().remove(id);
	}
	
	public void clear() {
		this._mockDB.getJobsites().clear();
	}
}
