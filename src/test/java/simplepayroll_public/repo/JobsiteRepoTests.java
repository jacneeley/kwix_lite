package simplepayroll_public.repo;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import local.payrollapp.simplepayroll.components.MockDatabase;
import local.payrollapp.simplepayroll.jobsite.Jobsite;
import local.payrollapp.simplepayroll.jobsite.JobsiteRepo;

@ExtendWith(MockitoExtension.class)
public class JobsiteRepoTests {
	@Mock
	private MockDatabase mockDb = new MockDatabase();
	
	@Autowired
	private JobsiteRepo jobsiteRepo = new JobsiteRepo(mockDb);
	
	@Test
	public void createJobsite_ReturnJobsite() throws Exception {
		Long generatedId = 1000L + (long) (Math.random() * (10000L - 1000L));
		Jobsite jobsite = new Jobsite(
				generatedId.toString(),
				"LINDSEY",
				Boolean.FALSE,
				Boolean.TRUE,
				LocalDate.now(),
				LocalDate.now());
		
		jobsiteRepo.CreateJobsite(jobsite);
		Assertions.assertThat(jobsite).isNotNull();
		Assertions.assertThat(jobsiteRepo.findJobsiteById(jobsite.getId(), Boolean.TRUE).get()).isNotNull();
	}
	
	@Test
	public void findAllActiveJobsites() throws Exception {
		Long generatedId = 1000L + (long) (Math.random() * (10000L - 1000L));
		Jobsite jobsite = new Jobsite(
				generatedId.toString(),
				"LINDSEY",
				Boolean.FALSE,
				Boolean.TRUE,
				LocalDate.now(),
				LocalDate.now());
		jobsiteRepo.CreateJobsite(jobsite);
		
		generatedId = 1000L + (long) (Math.random() * (10000L - 1000L));
		Jobsite jobsite1 = new Jobsite(
				generatedId.toString(),
				"NATHAN",
				Boolean.FALSE,
				Boolean.FALSE,
				LocalDate.now(),
				LocalDate.now());
		jobsiteRepo.CreateJobsite(jobsite1);
		
		generatedId = 1000L + (long) (Math.random() * (10000L - 1000L));
		Jobsite jobsite2 = new Jobsite(
				generatedId.toString(),
				"TEXAN",
				Boolean.FALSE,
				Boolean.TRUE,
				LocalDate.now(),
				LocalDate.now());
		jobsiteRepo.CreateJobsite(jobsite2);
		
		Assertions.assertThat(jobsiteRepo.findAllJobsitesByActive(Boolean.TRUE).size() == 2).isTrue();
	}
	
	@Test
	public void jobsiteSoftDeleteAndUpdate() throws Exception {
		Long generatedId = 1000L + (long) (Math.random() * (10000L - 1000L));
		Jobsite jobsite = new Jobsite(
				generatedId.toString(),
				"LINDSEY",
				Boolean.FALSE,
				Boolean.TRUE,
				LocalDate.now(),
				LocalDate.now());
		jobsiteRepo.CreateJobsite(jobsite);
		Assertions.assertThat(jobsite).isNotNull();
		Assertions.assertThat(jobsiteRepo.findJobsiteById(jobsite.getId(), Boolean.TRUE).get()).isNotNull();
		
		Jobsite updatedJobsite = new Jobsite(
				jobsite.getId(),
				jobsite.getJobsiteName(),
				jobsite.isContract(),
				Boolean.FALSE,
				jobsite.getCreatedAt(),
				LocalDate.now());
		jobsiteRepo.UpdateJobsite(updatedJobsite, updatedJobsite.getId());
		Assertions.assertThat(jobsiteRepo.findJobsiteById(updatedJobsite.getId(), Boolean.FALSE).get().getId().equals(generatedId.toString())).isTrue();
		Assertions.assertThat(jobsiteRepo.findAllJobsitesByActive(Boolean.FALSE).size() == 1).isTrue();
	}
	
	@Test
	public void jobsiteDeletion() throws Exception {
		Long generatedId = 1000L + (long) (Math.random() * (10000L - 1000L));
		Jobsite jobsite = new Jobsite(
				generatedId.toString(),
				"LINDSEY",
				Boolean.FALSE,
				Boolean.TRUE,
				LocalDate.now(),
				LocalDate.now());
		jobsiteRepo.CreateJobsite(jobsite);
		Assertions.assertThat(jobsite).isNotNull();
		Assertions.assertThat(jobsiteRepo.findJobsiteById(jobsite.getId(), Boolean.TRUE).get()).isNotNull();
		
		jobsiteRepo.DeleteJobsite(jobsite.getId());
		Assertions.assertThat(jobsiteRepo.findJobsiteById(jobsite.getId(), Boolean.TRUE).isEmpty()).isTrue();
	}
}
