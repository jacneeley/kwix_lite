package local.payrollapp.simplepayroll.utility;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.stereotype.Service;

import local.payrollapp.simplepayroll.GlobalConstants;
import local.payrollapp.simplepayroll.paystub.PaystubResponse;

@Service
public class CsvWriter {
	private String file;
	private String fileName;
	private String headers;
	
	public <T> void writeToCSV(List<T> table) throws IOException{
		String path = GlobalConstants.TMP;
		this.setFile(path);
		String name = String.format("%s_%s.csv", "simple_payroll_search_results",LocalDate.now().toString());
		this.setFileName(name);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.getFile() + this.getFileName()), "UTF-8"));
		try {
			StringBuffer headerLine = new StringBuffer();
			Object target = table.getClass();
			Object tableObj = table.get(0);
			Field[] fields = tableObj.getClass().getDeclaredFields();
			buildHeader(tableObj, fields, target);
			String headers = this.getHeaders();
			headerLine.append(headers);
			bw.write(headerLine.toString());
			bw.newLine();
			for(var tablerow : table) {
				StringBuffer rowLine = new StringBuffer();
				fields = tablerow.getClass().getDeclaredFields();
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					Method accessor = tablerow.getClass().getMethod(field.getName());
					Object value = accessor.invoke(tablerow);
					if(value != null) {
						if (i < fields.length - 1) {
							rowLine.append(value.toString()).append(GlobalConstants.CSV_SEP);
						}
						else {
							rowLine.append(value.toString());
						}
					}
				}
				bw.write(rowLine.toString());
				bw.newLine();
			}
			bw.flush();
			bw.close();
		}
		
		catch (UnsupportedEncodingException e) { 
        	bw.close(); 
        	e.printStackTrace(); 
    	}
        catch (FileNotFoundException e){ 
        	bw.close(); 
        	e.printStackTrace(); 
    	}
        catch (IOException e) { 
        	bw.close(); 
        	e.printStackTrace(); 
    	}
		catch (IllegalAccessException e) {
			bw.close();
			e.printStackTrace();
		}
		catch(Exception e) {
			bw.close();
			e.printStackTrace();
		}
	}

	public void writePaystubsToCSV(List<PaystubResponse> stubList) throws IOException {
		String path = GlobalConstants.TMP;
		this.setFile(path);
		this.setFileName(LocalDate.now().toString() + "_paystubs.csv");
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.getFile() + this.getFileName()), "UTF-8"));
		try {
			Double totalHrs = 0.0;
			Double totalPay = 0.0;
			StringBuffer headerLine = new StringBuffer();
			String header = "id,employeeId,fullName,jobsite,pay,hoursWorked,active,dayWorked,createAt,updateAt,";
			headerLine.append(header);
			bw.write(headerLine.toString());
            bw.newLine();
			for (PaystubResponse stub : stubList) {
				StringBuffer stubLine = new StringBuffer();
				stubLine.append(stub.paystubNum());
				stubLine.append(GlobalConstants.CSV_SEP);
				stubLine.append(stub.employeeId());
				stubLine.append(GlobalConstants.CSV_SEP);
				stubLine.append(stub.fullName());
				stubLine.append(GlobalConstants.CSV_SEP);
				stubLine.append(stub.jobsite());
				stubLine.append(GlobalConstants.CSV_SEP);
				stubLine.append(stub.pay());
				totalPay += (stub.pay() * stub.hoursWorked());
				totalHrs += stub.hoursWorked();
				stubLine.append(GlobalConstants.CSV_SEP);
				stubLine.append(stub.hoursWorked());
				stubLine.append(GlobalConstants.CSV_SEP);
				stubLine.append(stub.active());
				stubLine.append(GlobalConstants.CSV_SEP);
				stubLine.append(stub.dayWorked());
				stubLine.append(GlobalConstants.CSV_SEP);
				stubLine.append(stub.createAt());
				stubLine.append(GlobalConstants.CSV_SEP);
				stubLine.append(stub.updateAt());
				stubLine.append(GlobalConstants.CSV_SEP);
				bw.write(stubLine.toString());
                bw.newLine();
			}
			bw.newLine();
			String total = "total hours, labor expense,";
			StringBuffer totalLine = new StringBuffer();
			totalLine.append(total);
			bw.write(totalLine.toString());
            bw.newLine();
            
            String totals = totalHrs.toString() + GlobalConstants.CSV_SEP + totalPay.toString(); 
			StringBuffer totalsLine = new StringBuffer();
			totalsLine.append(totals);
			bw.write(totalsLine.toString());
            
            bw.flush();
            bw.close();
		} 
		catch (UnsupportedEncodingException e) { 
        	bw.close(); 
        	e.printStackTrace(); 
    	}
        catch (FileNotFoundException e){ 
        	bw.close(); 
        	e.printStackTrace(); 
    	}
        catch (IOException e) { 
        	bw.close(); 
        	e.printStackTrace(); 
    	}
	}
	
	private <T> void buildHeader(Object tableRow, Field[] fields, Object target) throws IllegalAccessException {
		try {
			List<String> headers = new ArrayList<String>();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				Method accessor = tableRow.getClass().getMethod(field.getName());
				Object value = accessor.invoke(tableRow);
				if(value != null) {
					headers.add(field.getName());
				}
			}
			String headerStr = String.join(GlobalConstants.CSV_SEP, headers);
			this.setHeaders(headerStr);
			
		}
		catch (NullPointerException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the headers
	 */
	public String getHeaders() {
		return headers;
	}

	/**
	 * @param headers the headers to set
	 */
	public void setHeaders(String headers) {
		this.headers = headers;
	}
	
}