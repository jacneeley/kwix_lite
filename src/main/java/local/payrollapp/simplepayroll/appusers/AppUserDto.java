package local.payrollapp.simplepayroll.appusers;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class AppUserDto {
	
	private Long id;
	@NotEmpty(message = "User name cannot be empty.")
	@Size(min=6, max=6, message="User name must be 6 characters.")
	private String username;
	@NotEmpty(message = "Password cannot be empty.")
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
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserPw() {
		return userPw;
	}
	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}
	
	public List<String> getUserRoles() {
		return userRoles;
	}
	
	public void setUserRoles(List<String> userRoles) {
		this.userRoles = userRoles;
	}
}
