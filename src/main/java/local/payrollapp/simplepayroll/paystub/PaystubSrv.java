package local.payrollapp.simplepayroll.paystub;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import local.payrollapp.simplepayroll.exceptions.ElementNotFoundException;

@Service
public class PaystubSrv implements IPaystubSrv{
	private static final Logger log = LoggerFactory.getLogger(PaystubSrv.class);
	private final PaystubRepo _stubRepo; 
	
	public PaystubSrv(PaystubRepo stubRepo){
		this._stubRepo = stubRepo;
	}
	
	public List<Paystub> findAllEmployeePaystubs(String employeeId){
		try {
			return _stubRepo.findEmployeePaystubs(employeeId);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new ElementNotFoundException("Paystubs could not be found for the given employeeId.");
		}
	}
	
	public List<Paystub> findAllActivePaystubs() {
		try {
			return _stubRepo.findAllPaystubsByActive(true);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new ElementNotFoundException("Paystubs could not be found...");
		}
	}
	
	@Override
	public List<Paystub> getPaystubsForEmployee(String id) {
		try {
			return _stubRepo.findEmployeePaystubsByActive(id, true);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new ElementNotFoundException("Employee Paystubs could not be found...");
		}
	}
	
	public List<Paystub> getAllDeletedPaystubs() {
		try {
			return _stubRepo.findAllPaystubsByActive(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new ElementNotFoundException("Paystubs could not be found...");
		}
	}

	@Override
	public Optional<Paystub> findByIdAndActive(String id) {
		try {
			if(!_stubRepo.findByIdAndActive(id, false).isEmpty()) {
				throw new ElementNotFoundException("Paystub could not be found...");
			}
			
			return _stubRepo.findByIdAndActive(id, true);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new ElementNotFoundException("Paystubs could not be found...");
		}
	}
	
	public Optional<Paystub> findByIdAndInactive(String id) {
//		return _paystubs.get(id);
		try {
			if(!_stubRepo.findByIdAndActive(id, false).isEmpty()) {
				throw new ElementNotFoundException("Paystub could not be found...");
			}
			
			return _stubRepo.findByIdAndActive(id, false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new ElementNotFoundException("Paystub could not be found...");
		}
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
		try {
			_stubRepo.CreatePaystub(stub);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	@Override
	public void UpdatePaystub(Paystub paystub) {
		try {
			_stubRepo.UpdatePaystub(paystub, paystub.getPaystubNum());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
	
	@Override
	public void UpdatePaystubs(Paystub paystub) {//update paystubs in bulk to maintain history if employee id changes.
		try {
			_stubRepo.UpdatePaystubs(paystub, paystub.getPaystubNum());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	@Override
	public void DeletePaystub(String id) {
		try {
			_stubRepo.DeletePaystub(id);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
	
	public void clear() {
		this._stubRepo.clear();
	}
}