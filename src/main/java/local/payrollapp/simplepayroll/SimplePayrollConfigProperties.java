package local.payrollapp.simplepayroll;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("sp")
public record SimplePayrollConfigProperties(
		String dbUrl, String dbUsername, String dbPassword, String sheetPath
		) {}
