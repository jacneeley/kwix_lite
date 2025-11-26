package local.payrollapp.simplepayroll.searchcriteria;

import java.util.List;

public interface ISearchCriteriaRepo {
	List<SearchCriteria> findAllByCriteria(SearchCriteria search);
	String buildSqlQuery(SearchCriteria searchCriteria);
	//void sqlStringBuilder(String colName, String scVal);
}