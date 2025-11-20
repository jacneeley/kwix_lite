package local.payrollapp.simplepayroll.appusers;

import java.util.List;
import java.util.UUID;

public class AppUser {
	private Long id;
	
	private String username;
	
	private String userPw;
	
	private List<String> userRoles;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public String getUserPw() {
		return userPw;
	}
	public List<String> getRoles(){
		return userRoles;
	}
	
	public AppUser() {}
	
	public AppUser(String username, String userPw, List<String> userRoles) {
		super();
		this.username = username;
		this.userPw = userPw;
		this.userRoles = userRoles;
	}
}
