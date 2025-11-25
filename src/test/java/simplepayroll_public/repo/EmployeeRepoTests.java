package simplepayroll_public.repo;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import local.payrollapp.simplepayroll.components.MockDatabase;
import local.payrollapp.simplepayroll.employees.Employee;
import local.payrollapp.simplepayroll.employees.EmployeeRepo;

@ExtendWith(MockitoExtension.class)
public class EmployeeRepoTests {
	@Mock
	private MockDatabase mockDb = new MockDatabase();
	
	@Autowired
	private EmployeeRepo employeeRepo = new EmployeeRepo(mockDb);
	
	@Test
	public void EmployeeRepo_SaveEmployee_ReturnEmployee() throws Exception {
		Employee employee = new Employee(
				"GH9999",
				"George",
				"Harrison",
				"999-999-9999",
				150,
				Boolean.TRUE,
				LocalDate.now(),
				LocalDate.now());
		
		employeeRepo.CreateEmployee(employee);
		Assertions.assertThat(employeeRepo.findById(employee.getId()).get()).isNotNull();
	}
	
	@Test
	public void EmployeeRepo_FindByIdAndActive() throws Exception {
		Employee employee1 = new Employee(
				"GH9999",
				"George",
				"Harrison",
				"999-999-9999",
				150,
				Boolean.TRUE,
				LocalDate.now(),
				LocalDate.now());
		employeeRepo.CreateEmployee(employee1);
		
		Employee employee2 = new Employee(
				"RS8888",
				"Ringo",
				"Starr",
				"888-888-8888",
				150,
				Boolean.FALSE,
				LocalDate.now(),
				LocalDate.now());
		employeeRepo.CreateEmployee(employee2);
		
		Assertions.assertThat(employeeRepo.findByIdAndActive(employee1.getId(), true).get()).isNotNull();
		Assertions.assertThat(employeeRepo.findByIdAndActive(employee2.getId(), false).get()).isNotNull();
	}
	
	@Test
	public void EmployeeRepo_DeleteEmployee_ReturnNull() throws Exception {
		Employee employee = new Employee(
				"GH9999",
				"George",
				"Harrison",
				"999-999-9999",
				150,
				Boolean.TRUE,
				LocalDate.now(),
				LocalDate.now());
		
		employeeRepo.CreateEmployee(employee);
		Assertions.assertThat(employeeRepo.findById(employee.getId()).get()).isNotNull();
		
		employeeRepo.DeleteEmployee(employee.getId());
		Assertions.assertThat(employeeRepo.findById(employee.getId()).isEmpty()).isTrue();
	}
}
