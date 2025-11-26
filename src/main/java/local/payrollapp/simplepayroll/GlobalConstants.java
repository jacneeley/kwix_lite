package local.payrollapp.simplepayroll;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class GlobalConstants {
	public static String TMP = "/data/sheets/";
	
	public static final String CSV_SEP = ",";
	
	public static final String NONE = "none";
	
	public static final String EXPIRED = "Link has expired...";
	
	public static final String EMPTY_STR = "";
	
	public static String USER1;
    public static String USER2;
    public static String PWD;
    public static String PWD2;
	
	@Value("${app.users.user1")
	public String user1;
	
	@Value("${app.users.user2}")
	public String user2;
	
	@Value("${app.passwords.pwd}")
	public String pwd;
	
	@Value("${app.passwords.pwd2}")
	public String pwd2;
	
	@PostConstruct
    public void init() {
        USER1 = this.user1;
        USER2 = this.user2;
        PWD = this.pwd;
        PWD2 = this.pwd2;
    }
}