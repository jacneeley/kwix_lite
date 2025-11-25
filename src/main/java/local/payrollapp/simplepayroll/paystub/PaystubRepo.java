package local.payrollapp.simplepayroll.paystub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import local.payrollapp.simplepayroll.components.MockDatabase;
import local.payrollapp.simplepayroll.view.AppController;

@Repository
public class PaystubRepo implements IPaystubRepo{
	private final MockDatabase _mockDB;
	
	public PaystubRepo(MockDatabase mockDb) {
		this._mockDB = mockDb;
	}
	
	@Override
	public List<Paystub> findEmployeePaystubs(String employeeId) throws Exception {
		List<Paystub> stubs = _mockDB.getPaystubs().entrySet()
				.stream()
				.filter(p -> p.getValue() != null)
				.filter(p -> p.getValue().getEmployeeId() == employeeId)
				.map(Map.Entry::getValue)
				.collect(Collectors.toList());
		return stubs;
	}
	
	@Override
	public List<Paystub> findEmployeePaystubsByActive(String employeeId, boolean active) throws Exception {
		List<Paystub> stubs = _mockDB.getPaystubs().entrySet()
				.stream()
				.filter(p -> p.getValue() != null)
				.filter(p -> employeeId.equals(p.getValue().getEmployeeId()))
				.filter(p -> p.getValue().isActive() == active)
				.map(Map.Entry::getValue)
				.collect(Collectors.toList());
		return stubs;
	}
	
	@Override
	public List<Paystub> findAllPaystubsByActive(boolean active) throws Exception {
		List<Paystub> stubs = _mockDB.getPaystubs().entrySet()
				.stream()
				.filter(p -> p.getValue() != null)
				.filter(p -> p.getValue().isActive() == active)
				.map(Map.Entry::getValue)
				.collect(Collectors.toList());
		return stubs;
	}

	@Override
	public Optional<Paystub> findByIdAndActive(String id, boolean active) throws Exception {
		Paystub stub = _mockDB.getPaystubs().get(id);
		if(stub == null) {
			return Optional.empty();
		}
		
		if(stub.isActive() == active) {
			return Optional.of(stub);
		}
		return Optional.empty();
	}

	@Override
	public void CreatePaystub(Paystub paystub) throws Exception {
		this._mockDB.getPaystubs().put(paystub.getPaystubNum(), paystub);
	}

	@Override
	public void UpdatePaystub(Paystub paystub, String id) throws Exception {
		this._mockDB.getPaystubs().put(paystub.getPaystubNum(), paystub);
	}
	
	@Override
	public void UpdatePaystubs(Paystub paystub, String id) throws Exception {
		this._mockDB.getPaystubs().put(paystub.getPaystubNum(), paystub);
	}

	@Override
	public void DeletePaystub(String id) throws Exception {
		this._mockDB.getPaystubs().remove(id);
	}
	
	public void clear() {
		this._mockDB.getPaystubs().clear();
	}
}