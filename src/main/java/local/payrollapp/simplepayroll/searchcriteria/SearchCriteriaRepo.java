package local.payrollapp.simplepayroll.searchcriteria;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class SearchCriteriaRepo implements ISearchCriteriaRepo{
	
	boolean initSelect = Boolean.FALSE;
	boolean initWhere = Boolean.FALSE;
	boolean initFrom = Boolean.FALSE;
	boolean isSqlString = false;
	String selectStr = "";
	String fromStr = "";
	String whereStr = "";
	
//	private final JdbcClient _jdbcClient;
	
	private static final Logger log = LoggerFactory.getLogger(SearchCriteriaRepo.class);
	
	//global variables
	final String ANY = "any";
	final double ZERO_DOUBLE = 0.0;
	final CharSequence START = "0001-01-01";
	final LocalDate DEFAULT = LocalDate.parse(START);
	
//	public SearchCriteriaRepo(JdbcClient jdbcClient) {
//		this._jdbcClient = jdbcClient;
//	}
	
	@Override
	public List<SearchCriteria> findAllByCriteria(SearchCriteria search) {
		String sqlStr = buildSqlQuery(search);
//		List<SearchCriteria> searchResults = _jdbcClient.sql(sqlStr)
//				.query(SearchCriteria.class).list();
		return null;
	}

	@Override
	public String buildSqlQuery(SearchCriteria searchCriteria) {
		reInitSqlQueries();
		Boolean empChecked = searchCriteria.getEmpChecked();
		Boolean payChecked = searchCriteria.getPaystubChecked();
		Boolean jobsiteChecked = searchCriteria.getJobsiteChecked();
		
		//this should not happen, but just in case, log it and system should throw 500.
		if(!empChecked && !payChecked && !jobsiteChecked) {
			log.error("ERROR: No Selections were made. Cannot build 'FROM' Clause. Exception will occur.");
			return "";
		}
		
		fromStr = buildFromClause(empChecked, payChecked, jobsiteChecked);
		
		if(searchCriteria.getEmpId() != null) {
			sqlStringBuilder("EMPLOYEE.id", searchCriteria.getEmpId());
		}
		
		if(searchCriteria.getFullName() != null && payChecked) {
			String[] name = searchCriteria.getFullName().split(" ");
			String fName = name[0].equals(ANY) ? "" : name[0];
			String lName = name[1].equals(ANY) ? "" : name[1];
			
			if(!fName.equals("") && !lName.equals("")) {
				sqlStringBuilder("PAYSTUB.full_name", searchCriteria.getFullName());
			}
			else if(!fName.equals("") && lName.equals("")) {
				sqlStringBuilder("PAYSTUB.full_name", fName);
			}
			else {
				sqlStringBuilder("PAYSTUB.full_name", lName);
			}
		}
		else if(searchCriteria.getFirstName() != null 
				&& searchCriteria.getLastName() != null 
				&& searchCriteria.getFullName() == null 
				&& !payChecked) {
			sqlStringBuilder("EMPLOYEE.first_name", searchCriteria.getFirstName());
			sqlStringBuilder("EMPLOYEE.last_name", searchCriteria.getLastName());
		}
		
		if(searchCriteria.getPhone() != null) {
			sqlStringBuilder("EMPLOYEE.phone", searchCriteria.getPhone());
		}
		
		if(searchCriteria.getPaystubNum() != null) {
			sqlStringBuilder("PAYSTUB.paystub_num", searchCriteria.getPaystubNum());
		}
		
		if(searchCriteria.getPay() != null) {
			sqlStringBuilder("PAYSTUB.pay", searchCriteria.getPay());
		}
		
		if(searchCriteria.getHoursWorked() != null) {
			sqlStringBuilder("PAYSTUB.hours_worked", searchCriteria.getHoursWorked());
		}
		
		//TODO: allow user to look at paystubs based on a start and end date
		//TODO: create a new issue for this.
		if(searchCriteria.getDayWorked() != null) {
			LocalDate startDate = searchCriteria.getDayWorked();
			LocalDate endDate = searchCriteria.getEndDayWorked();
			buildDateQuery(startDate, endDate);
		}
		
		if(searchCriteria.getJobsiteName() != null) {
			sqlStringBuilder("JOBSITE.jobsite_name", searchCriteria.getJobsiteName());
		}
		
		if(searchCriteria.getIsContract() != null) {
			sqlStringBuilder("JOBSITE.is_contract", searchCriteria.getIsContract());
		}
		
		//TODO: allow user to select active status for each table individually.
		if(searchCriteria.getActive() != null) {
			if(empChecked) {
				sqlStringBuilder("EMPLOYEE.active", searchCriteria.getActive());
			}
			
			if(payChecked) {
				sqlStringBuilder("PAYSTUB.active", searchCriteria.getActive());
			}
			
			if(jobsiteChecked) {
				sqlStringBuilder("JOBSITE.active", searchCriteria.getActive());
			}
		}
		
		String queryString = initWhere ? selectStr + fromStr + whereStr + ";" : selectStr + fromStr + ";";
		log.info(queryString);
		return queryString;
	}

	
	private void sqlStringBuilder(String colName, Object scVal) {
		if(initSelect) {
			selectStr += String.format(" , %s", colName);
		}
		else {
			selectStr += String.format("%s", colName);
			initSelect = true;
		}

		if(!scVal.toString().contains(ANY) && !scVal.equals(ZERO_DOUBLE) && !scVal.equals(DEFAULT)) {
			if(initWhere) {
				whereStr += buildWhereClause(colName, scVal, initWhere);
			}
			else {
				whereStr = String.format(" WHERE %s", buildWhereClause(colName, scVal, initWhere));
				initWhere = true;
			}
		}
	}
	
	private String buildFromClause(Boolean empChecked, Boolean payChecked, Boolean jobChecked) {
		log.info(String.format("Tables Selected: Employee: %s, Paysubt: %s, Jobsite: %s", empChecked.toString(), payChecked.toString(), jobChecked.toString()));

		if(!empChecked && !payChecked && !jobChecked) {
			//should never enter here, but just in case.
			log.error("ERROR: No Selections were made. Cannot build 'From' clause. Exception will occur.");
			return "";
		}
		
		StringBuilder fromClause = new StringBuilder(" FROM ");
		
		if(empChecked) {
			fromClause.append("EMPLOYEE");
			if(payChecked) {
				fromClause.append(" INNER JOIN PAYSTUB ON EMPLOYEE.id = PAYSTUB.employee_id ");
				if(jobChecked) {
					fromClause.append(" INNER JOIN JOBSITE ON PAYSTUB.jobsite = JOBSITE.jobsite_name ");
				}
			}
			else if(jobChecked) {
				//fallback:  just join all tables to prevent issues
				fromClause
					.append(" INNER JOIN PAYSTUB ON EMPLOYEE.id = PAYSTUB.employee_id ")
					.append(" INNER JOIN JOBSITE ON PAYSTUB.jobsite = JOBSITE.jobsite_name ");
			}
		}
		else if(payChecked) {
			fromClause.append("PAYSTUB");
			if(jobChecked) {
				fromClause.append(" INNER JOIN JOBSITE ON PAYSTUB.jobsite = JOBSITE.jobsite_name ");
			}
		}
		else if (jobChecked) {
			fromClause.append("JOBSITE");
		}
		else {
			log.error("Unknown Error Occurred. Could not build 'FROM' clause");
			return "";
		}
		
		return fromClause.toString();
	}
	
	/**
	 * 
	 * @param colName
	 * @param scVal
	 * @param initWhere
	 * @return where clause based on columns selected and values entered in UI.
	 */
	private String buildWhereClause(String colName, Object scVal, boolean initWhere) {
		List<String> whereParams = new ArrayList<>();
		StringBuilder whereBuilder = new StringBuilder();
		if(scVal instanceof String) {
			whereParams.add(colName);
			whereParams.add(scVal.toString());
			
			if(initWhere) {
				whereBuilder.append(" AND UPPER(?) LIKE UPPER('%?%')");
			}
			else {
				whereBuilder.append("UPPER(?) LIKE UPPER('%?%')");
			}
		}
		
		else if(scVal instanceof Double) {
			whereParams.add(colName);
			whereParams.add(scVal.toString());

			if(initWhere) {
				whereBuilder.append(" AND ? = ?");
			}
			else {
				whereBuilder.append("? = ?");
			}
		}
		
		else if(scVal instanceof LocalDate) {
			whereParams.add(colName);
			whereParams.add(scVal.toString());
			
			if(initWhere) {
				whereBuilder.append(" AND ? LIKE '%?%'");
			}
			else {
				whereBuilder.append("? LIKE '%?%'");
			}
		}
		
		else if(scVal instanceof Boolean) {
			Boolean val = (Boolean) scVal;
			whereParams.add(colName);
			
			if(initWhere) {
				if(val) {
					whereParams.add("1");
				}
				else {
					whereParams.add("0");
				}
				whereBuilder.append(" AND ? = ?");
			}
			else {
				
				if(val) {
					whereParams.add("1");
				}
				else {
					whereParams.add("0");
				}
				whereBuilder.append("? = ?");
			}
		}
		else {
			log.warn(String.format("'WHERE' condition for %s was not added...", colName));
			return "";
		}
		
		return mapParams(whereBuilder.toString(), whereParams);
	}
	
	
	private void buildDateQuery(LocalDate startDate, LocalDate endDate) {
		List<String> params = new ArrayList<>();
		StringBuilder dateBuilder = new StringBuilder();
		
		if(initSelect) {
			selectStr += ", PAYSTUB.day_worked";
		}
		else {
			selectStr += "PAYSTUB.day_worked";
			initSelect = true;
		}
		
		if(!startDate.equals(DEFAULT)) {
			if(endDate.equals(DEFAULT)) {
				endDate = startDate;
			}
			
			if(!startDate.equals(endDate)) {
				if(!initWhere) {
					dateBuilder.append(" WHERE day_worked BETWEEN SYMMETRIC '?'::date AND '?'::date");
					params.add(startDate.toString());
					params.add(endDate.toString());
					initWhere = true;
				}
				else {
					dateBuilder.append(" AND day_worked BETWEEN SYMMETRIC '?'::date AND '?'::date");
					params.add(startDate.toString());
					params.add(endDate.toString());
				}
			}
			else {
				if(!initWhere) {
					dateBuilder.append(" WHERE day_worked = '?'::date");
					params.add(startDate.toString());
					initWhere = true;
				}
				else {
					dateBuilder.append(" AND day_worked = '?'::date");
					params.add(startDate.toString());
				}
			}
			
			whereStr += mapParams(dateBuilder.toString(), params);
		}
	}
	
	/*
	 * reset all query params
	 */
	private void reInitSqlQueries() {
		initSelect = false;
		initWhere = false;
		selectStr = "SELECT ";
		fromStr = "";
		whereStr = "";
	}
	
	private String mapParams(String sqlStr, List<String> params) {
		for(String param : params) {
			sqlStr = sqlStr.replaceFirst("\\?", param);
		}
		
		return sqlStr;
	}
}
