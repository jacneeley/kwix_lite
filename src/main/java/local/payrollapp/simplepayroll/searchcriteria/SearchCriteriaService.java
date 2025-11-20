package local.payrollapp.simplepayroll.searchcriteria;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class SearchCriteriaService implements ISearchCriteriaService {
	
	private final SearchCriteriaRepo _searchCriteriaRepo;
	
	public SearchCriteriaService(SearchCriteriaRepo scRepo) {
		this._searchCriteriaRepo = scRepo;
	} 
	
	@Override
	public List<SearchCriteria> findAllByCriteria(SearchCriteria search) {
		return _searchCriteriaRepo.findAllByCriteria(search);
	}
}
