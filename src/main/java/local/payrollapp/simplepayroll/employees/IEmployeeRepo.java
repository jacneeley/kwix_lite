package local.payrollapp.simplepayroll.employees;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

public interface IEmployeeRepo{
	Optional<Employee> findById(String id) throws Exception;
	List<Employee> findAllByActive(boolean active) throws Exception;
	Optional<Employee> findByIdAndActive(String id, boolean active) throws Exception;
	void CreateEmployee(Employee employee) throws Exception;
	void UpdateEmployee(Employee employee, String id) throws Exception;
	void DeleteEmployee(String id) throws Exception;
}
