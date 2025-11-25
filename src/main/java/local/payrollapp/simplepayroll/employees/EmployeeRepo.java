package local.payrollapp.simplepayroll.employees;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;


import local.payrollapp.simplepayroll.components.MockDatabase;

@Repository
public class EmployeeRepo implements IEmployeeRepo {
	
	private final MockDatabase _mockDB;
	
	public EmployeeRepo(MockDatabase mockDb) {
		this._mockDB = mockDb;
	}
	
	@Override
	public Optional<Employee> findById(String id) throws Exception {
		return Optional.ofNullable(_mockDB.getEmployees().get(id));
	}
	
	@Override
	public List<Employee> findAllByActive(boolean active) throws Exception {
		List<Employee> allActive = _mockDB.getEmployees().entrySet()
				.stream()
				.filter(e -> e.getValue().isActive() == active)
				.map(Map.Entry::getValue)
				.collect(Collectors.toList());
		return allActive;
		//		List<Employee> allActive = _jdbcClient.sql("SELECT * FROM EMPLOYEE WHERE active = :Active")
//				.param("Active", activeValue)
//				.query(Employee.class).list();
	}

	@Override
	public Optional<Employee> findByIdAndActive(String id, boolean active) throws Exception {
		Employee emp = _mockDB.getEmployees().get(id);
		if(emp == null) {
			return Optional.empty();
		}
		
		if(emp.isActive() == active) {
			return Optional.of(emp);
		}
		return Optional.empty();
	}

	@Override
	public void CreateEmployee(Employee employee) throws Exception {
		if(employee == null || employee.getId() == null) {
			throw new NullPointerException();
		}
		this._mockDB.getEmployees().put(employee.getId(), employee);
	}

	@Override
	public void UpdateEmployee(Employee employee, String id) throws Exception {
		if(employee == null || id == null || id.isBlank()) {
			throw new Exception();
		}
		this._mockDB.getEmployees().put(id, employee);
	}

	@Override
	public void DeleteEmployee(String id) throws Exception {
		if(id == null || id.isBlank()) {
			throw new Exception();
		}
		this._mockDB.getEmployees().remove(id);
	}
	
	public void clear() {
		this._mockDB.getEmployees().clear();
	}
}
