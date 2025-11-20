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
	public Optional<Jobsite> findJobsiteById(String jobsiteId, boolean active) {
		Jobsite jobsite = this._mockDB.getJobsites().get(jobsiteId);
		if(jobsite.isActive() == active) {
			return Optional.of(jobsite);
		}
		return Optional.empty();
		//		return _jdbcClient.sql("SELECT * FROM JOBSITE WHERE id = ? AND active = ?")
//				.params(id, activeValue)
//				.query(Jobsite.class)
//				.optional();
	}

	@Override
	public List<Jobsite> findAllJobsitesByActive(boolean active) {
		List<Jobsite> stubs = this._mockDB.getJobsites().entrySet()
				.stream()
				.filter(p -> p.getValue().isActive() == active)
				.map(Map.Entry::getValue)
				.collect(Collectors.toList());
		return stubs;
		//		List<Jobsite> allActive = _jdbcClient.sql("SELECT * FROM JOBSITE WHERE active = ?")
//				.param(activeValue)
//				.query(Jobsite.class).list();
//		return allActive;
	}

	@Override
	public void CreateJobsite(Jobsite jobsite) {
		this._mockDB.getJobsites().put(jobsite.getId(), jobsite);
	}

	@Override
	public void UpdateJobsite(Jobsite jobsite, String id) {
		this._mockDB.getJobsites().put(jobsite.getId(), jobsite);
	}

	@Override
	public void DeleteJobsite(String id) {
		this._mockDB.getJobsites().remove(id);
	}
	
	public void clear() {
		this._mockDB.getJobsites().clear();
	}
}
