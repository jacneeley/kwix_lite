package local.payrollapp.simplepayroll.appusers;

import java.time.Instant;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import local.payrollapp.simplepayroll.GlobalConstants;

@SessionScope
@Component
public class MockAppUsers {
	private static final Logger log = LoggerFactory.getLogger(MockAppUsers.class);
	
	private String tmpUser;
	
	private final HashMap<String, Object[]> userCredentials = new HashMap<>();
	
	public HashMap<String, Object[]> getUserCredentials() {
		return userCredentials;
	}
	
	public MockAppUsers() {}
	
	public String getUserCredentialDetails(String tmpId) {
		Object[] userDetails = this.userCredentials.get(tmpId);
		if(!(userDetails[1] instanceof Instant)) {
			Exception ex = new Exception("userDetails[1] was not an instancecof 'Instant'.");
			log.error(ex.getMessage());
			return "";
		}
		
		Instant expiration = (Instant) userDetails[1];
		if(Instant.now().compareTo(expiration) < 0) {
			return userDetails[0].toString();
		}
		this.userCredentials.remove(tmpId);
		this.tmpUser = "";
		return GlobalConstants.EXPIRED;
	}
	
	public String getTmpUser() {
		return tmpUser;
	}

	public void setTmpUser(String tmpUser) {
		this.tmpUser = tmpUser;
	}
}
