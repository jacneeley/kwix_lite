package local.payrollapp.simplepayroll.employees;


import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import local.payrollapp.simplepayroll.exceptions.ElementNotFoundException;
import local.payrollapp.simplepayroll.paystub.Paystub;
import local.payrollapp.simplepayroll.paystub.PaystubSrv;
import local.payrollapp.simplepayroll.view.AppController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmployeeSrv implements IEmpSrv{
	private static final Logger log = LoggerFactory.getLogger(EmployeeSrv.class);
	
	
	private final EmployeeRepo _empRepo;
	private final PaystubSrv _stubSrv;
	//private static final Logger log = LoggerFactory.getLogger(EmployeeSrv.class);
	
	public EmployeeSrv(EmployeeRepo empRepo, PaystubSrv stubSrv) {
		this._empRepo = empRepo;
		this._stubSrv = stubSrv;
	}
	
	@Override
	public List<Employee> getAllEmps() {
		try {
			return _empRepo.findAllByActive(true);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new ElementNotFoundException("No employees were found.");
		}
	}
	
	@Override
	public Optional<Employee> getEmp(String id) {
		try {
			return _empRepo.findByIdAndActive(id, true);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new ElementNotFoundException("no employee was found for the given criteria.");
		}
	}
	
	@Override
	public Optional<Employee> getInactiveEmp(String id){
		try {
			return _empRepo.findByIdAndActive(id, false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new ElementNotFoundException("no employee was found for the given criteria.");
		}
	}
	
	@Override
	public List<Employee> getAllDeletedEmps() {
		try {
			return _empRepo.findAllByActive(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new ElementNotFoundException("no employeee were found for the given criteria.");
		}
	}

	@Override
	public void createEmp(Employee employee) {
		try {
			Employee newEmpWithGeneratedId = new Employee(
					employee.generateId(),
					employee.getfirstName(),
					employee.getlastName(),
					employee.getPhone(),
					employee.getPay(),
					employee.isActive(),
					employee.getCreateAt(),
					employee.getUpdateAt());
			employee = null;
			_empRepo.CreateEmployee(newEmpWithGeneratedId);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			log.error("employeeId or employee was null.");
		}
	}

	@Override
	public void updateEmp(String oldId, Employee employee){
		try {
			String oldPhone = _empRepo.findById(employee.getId()).get().getPhone();
			if(employee.getPhone().equals(oldPhone)) {
				_empRepo.UpdateEmployee(employee, employee.getId());
			}
			else if(!employee.getPhone().equals(oldPhone)) {
				
				//if phone is changed than a new id must be created.
				this.createEmp(employee);
				this.deleteEmp(employee.getId());
				
				List<Paystub> empStubs = _stubSrv.findAllEmployeePaystubs(oldId);
				if(!empStubs.isEmpty()) {
					for(Paystub stub : empStubs) {
						Paystub updatedStub = new Paystub(
								stub.getPaystubNum(),
								employee.generateId(),
								stub.getFullName(),
								stub.getJobsite(),
								stub.getPay(),
								stub.getHoursWorked(),
								stub.isActive(),
								stub.getDayWorked(),
								stub.getCreateAt(),
								stub.getUpdateAt());
						_stubSrv.UpdatePaystubs(updatedStub);
					}
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
			throw new ElementNotFoundException("no employee was found for the given criteria.");
		}
	}

	@Override
	public void deleteEmp(String id) {
		try {
			_empRepo.DeleteEmployee(id);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new ElementNotFoundException("could not delete employee because employeeId was invalid.");
		}
	}
	
	public void clear() {
		_empRepo.clear();
	}
	
	// @PostConstruct 
	// private void init() {
	// 	Employee emp = new Employee("Arthur", "Sparks", "999-999-9999", 99.99, true, LocalDate.now(), LocalDate.now());
	// 	Employee emp2 = new Employee("Jacob", "Neeley", "888-888-8888", 99.99, false, LocalDate.now(), LocalDate.now());
	// 	this.createEmp(emp);
	// 	// log.info("created: " + emp.toString());
	// 	this.createEmp(emp2);
	// 	// log.info("created: " + emp2.toString());
	// }
}