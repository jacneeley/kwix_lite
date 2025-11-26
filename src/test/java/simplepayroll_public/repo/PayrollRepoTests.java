package simplepayroll_public.repo;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import local.payrollapp.simplepayroll.components.MockDatabase;
import local.payrollapp.simplepayroll.paystub.Paystub;
import local.payrollapp.simplepayroll.paystub.PaystubRepo;

@ExtendWith(MockitoExtension.class)
public class PayrollRepoTests {
	@Mock
	private MockDatabase mockDb = new MockDatabase();
	
	@Autowired
	private PaystubRepo stubRepo = new PaystubRepo(mockDb);
	
	@Test
	public void PaystubRepo_CreatePaystub_FindByIdAndActive() throws Exception {
		String id = UUID.randomUUID().toString();
		Paystub stub = new Paystub(
				id,
				"GH9999",
				"George Harrison",
				"Other",
				150,
				12,
				Boolean.TRUE,
				LocalDate.now(),
				LocalDate.now(),
				LocalDate.now());
		stubRepo.CreatePaystub(stub);
		Assertions.assertThat(stubRepo.findByIdAndActive(id, true)).isNotNull();
	}
	
	@Test
	public void PaystubRepo_UpdatePaystubANDSoftDelete() throws Exception {
		String id = UUID.randomUUID().toString();
		Paystub stub = new Paystub(
				id,
				"GH9999",
				"George Harrison",
				"Other",
				150,
				12,
				Boolean.TRUE,
				LocalDate.now(),
				LocalDate.now(),
				LocalDate.now());
		stubRepo.CreatePaystub(stub);
		Assertions.assertThat(stubRepo.findByIdAndActive(id, true)).isNotNull();
		
		Paystub updatedStub = new Paystub(
				stub.getPaystubNum(),
				stub.getEmployeeId(),
				stub.getFullName(),
				stub.getJobsite(),
				stub.getPay(),
				stub.getHoursWorked(),
				Boolean.FALSE,
				stub.getDayWorked(),
				stub.getCreateAt(),
				LocalDate.now());
		stubRepo.UpdatePaystub(updatedStub, updatedStub.getPaystubNum());
		Assertions.assertThat(stubRepo.findByIdAndActive(updatedStub.getPaystubNum(), updatedStub.isActive())).isNotNull();
		
		Assertions.assertThat(
				stubRepo.findByIdAndActive(
						updatedStub.getPaystubNum(), updatedStub.isActive())
							.get()
							.getPaystubNum().equals(id)
				).isTrue();
	}
	
	@Test
	public void PaystubRepo_DeletePaystub() throws Exception {
		String id = UUID.randomUUID().toString();
		Paystub stub = new Paystub(
				id,
				"GH9999",
				"George Harrison",
				"Other",
				150,
				12,
				Boolean.TRUE,
				LocalDate.now(),
				LocalDate.now(),
				LocalDate.now());
		stubRepo.CreatePaystub(stub);
		Assertions.assertThat(stubRepo.findByIdAndActive(id, true)).isNotNull();
		
		stubRepo.DeletePaystub(id);
		Assertions.assertThat(stubRepo.findByIdAndActive(id, true).isEmpty()).isTrue();
	}
}
