package local.payrollapp.simplepayroll.paystub;

import java.util.List;
import java.util.Optional;

public interface IPaystubRepo {
	List<Paystub> findEmployeePaystubs(String id) throws Exception;
	List<Paystub> findEmployeePaystubsByActive(String id, boolean active) throws Exception;
	List<Paystub> findAllPaystubsByActive(boolean active) throws Exception;
	Optional<Paystub> findByIdAndActive(String Id, boolean active) throws Exception;
	void CreatePaystub(Paystub paystub) throws Exception;
	void UpdatePaystub(Paystub paystub, String id) throws Exception;
	void UpdatePaystubs(Paystub paystub, String id) throws Exception;
	void DeletePaystub(String id) throws Exception;
}