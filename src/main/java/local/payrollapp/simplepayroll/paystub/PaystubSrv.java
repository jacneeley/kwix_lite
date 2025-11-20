package local.payrollapp.simplepayroll.paystub;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class PaystubSrv implements IPaystubSrv{
	
	private final PaystubRepo _stubRepo; 
	
	PaystubSrv(PaystubRepo stubRepo){
		this._stubRepo = stubRepo;
	}
	
	public List<Paystub> findAllEmployeePaystubs(String id){
		return _stubRepo.findEmployeePaystubs(id);
	}
	
	public List<Paystub> findAllActivePaystubs() {
		return _stubRepo.findAllPaystubsByActive(true);
	}
	
	@Override
	public List<Paystub> getPaystubsForEmployee(String id) {
		return _stubRepo.findEmployeePaystubsByActive(id, true);
	}
	
	public List<Paystub> getAllDeletedPaystubs() {
		return _stubRepo.findAllPaystubsByActive(false);
	}

	@Override
	public Optional<Paystub> findByIdAndActive(String id) {
		return _stubRepo.findByIdAndActive(id, true);
	}
	
	public Optional<Paystub> findByIdAndInactive(String id) {
//		return _paystubs.get(id);
		return _stubRepo.findByIdAndActive(id, false);
	}

	@Override
	public void CreatePaystub(Paystub paystub) {
		Paystub stub = new Paystub(
				paystub.generateId(),
				paystub.getEmployeeId(),
				paystub.getFullName(),
				paystub.getJobsite(),
				paystub.getPay(),
				paystub.getHoursWorked(),
				paystub.isActive(),
				paystub.getDayWorked(),
				paystub.getCreateAt(),
				paystub.getUpdateAt());
		_stubRepo.CreatePaystub(stub);
	}

	@Override
	public void UpdatePaystub(Paystub paystub) {
		_stubRepo.UpdatePaystub(paystub, paystub.getPaystubNum());
	}
	
	@Override
	public void UpdatePaystubs(Paystub paystub) {//update paystubs in bulk to maintain history if employee id changes.
		_stubRepo.UpdatePaystubs(paystub, paystub.getPaystubNum());
	}

	@Override
	public void DeletePaystub(String id) {
		_stubRepo.DeletePaystub(id);
	}
	
	public void clear() {
		this._stubRepo.clear();
	}
}