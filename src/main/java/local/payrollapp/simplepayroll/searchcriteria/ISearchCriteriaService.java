package local.payrollapp.simplepayroll.searchcriteria;

import java.util.List;

public interface ISearchCriteriaService {
	List<SearchCriteria> findAllByCriteria(SearchCriteria search);
}
