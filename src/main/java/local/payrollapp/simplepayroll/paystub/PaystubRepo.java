package local.payrollapp.simplepayroll.paystub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import local.payrollapp.simplepayroll.components.MockDatabase;

@Repository
public class PaystubRepo implements IPaystubRepo{
	
	private final MockDatabase _mockDB;
	
	public PaystubRepo(MockDatabase mockDb) {
		this._mockDB = mockDb;
	}
	
	@Override
	public List<Paystub> findEmployeePaystubs(String employeeId) {
		
		List<Paystub> stubs = _mockDB.getPaystubs().entrySet()
				.stream()
				.filter(p -> p.getValue() != null)
				.filter(p -> p.getValue().getEmployeeId() == employeeId)
				.map(Map.Entry::getValue)
				.collect(Collectors.toList());
		return stubs;
//		return _jdbcClient.sql("SELECT * FROM PAYSTUB WHERE employee_id = ?")
//		.param(id)
//		.query(Paystub.class).list();
	}
	
	@Override
	public List<Paystub> findEmployeePaystubsByActive(String employeeId, boolean active) {
		//TODO: figure out how to filter by 2 predicates
		List<Paystub> stubs = _mockDB.getPaystubs().entrySet()
				.stream()
				.filter(p -> p.getValue() != null)
				.filter(p -> employeeId.equals(p.getValue().getEmployeeId()))
				.filter(p -> p.getValue().isActive() == active)
				.map(Map.Entry::getValue)
				.collect(Collectors.toList());
		return stubs;
		//		List<Paystub> allActive = _jdbcClient.sql("SELECT * FROM PAYSTUB WHERE active = ? AND employee_id = ?")
//				.params(this.getBitVal(active), id)
//				.query(Paystub.class).list();
//		return allActive;
	}
	
	@Override
	public List<Paystub> findAllPaystubsByActive(boolean active) {
		List<Paystub> stubs = _mockDB.getPaystubs().entrySet()
				.stream()
				.filter(p -> p.getValue() != null)
				.filter(p -> p.getValue().isActive() == active)
				.map(Map.Entry::getValue)
				.collect(Collectors.toList());
		return stubs;
		//		List<Paystub> allActive = _jdbcClient.sql("SELECT * FROM PAYSTUB WHERE active = ?")
//				.params(this.getBitVal(active))
//				.query(Paystub.class).list();
//		return allActive;
	}

	@Override
	public Optional<Paystub> findByIdAndActive(String id, boolean active) {
		Paystub stub = _mockDB.getPaystubs().get(id);
		if(stub.isActive() == active) {
			return Optional.of(stub);
		}
		return Optional.empty();
//		return _jdbcClient.sql("SELECT * FROM PAYSTUB WHERE active = ? AND paystub_num = ?")
//				.params(this.getBitVal(active), id)
//				.query(Paystub.class)
//				.optional();
	}

	@Override
	public void CreatePaystub(Paystub paystub) {
		this._mockDB.getPaystubs().put(paystub.getPaystubNum(), paystub);
	}

	@Override
	public void UpdatePaystub(Paystub paystub, String id) {
		this._mockDB.getPaystubs().put(paystub.getPaystubNum(), paystub);
	}

	public void clear() {
		this._mockDB.getPaystubs().clear();
	}
	
	@Override
	public void UpdatePaystubs(Paystub paystub, String id) {
		this._mockDB.getPaystubs().put(paystub.getPaystubNum(), paystub);
		/*
		 * TODO: 
		 * get a list of paystubs using employee id via stream
		 * re-map paystubs, then put into hashmap.
		 * this situation occurs when we edit employee data, so all paystubs related to that employee need to be updated.
		 */
		//		var updated = _jdbcClient.sql("UPDATE PAYSTUB SET "
//				+ "employee_id=?,"
//				+ "full_name=?,"
//				+ "jobsite=?,"
//				+ "pay=?,"
//				+ "hours_worked=?,"
//				+ "active=?,"
//				+ "day_worked=?,"
//				+ "create_at=?,"
//				+ "update_at=?"
//				+ " WHERE id=?")
//				.params(List.of(paystub.getEmployeeId(), paystub.getFullName(), paystub.getJobsite(), paystub.getPay(),paystub.getHoursWorked(),
//						this.getBitVal(paystub.isActive()), paystub.getDayWorked(), paystub.getCreateAt(), paystub.getUpdateAt(),
//						id))
//				.update();
//		Assert.state(updated == 1, "Failed to update: " + paystub.toString());
		
	}

	@Override
	public void DeletePaystub(String id) {
		this._mockDB.getPaystubs().remove(id);
	}
}