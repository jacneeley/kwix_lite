package local.payrollapp.simplepayroll.appusers;

import jakarta.validation.constraints.NotEmpty;

public class AppUserRequest{
	@NotEmpty(message = "Username cannot be empty.")
	private String userId;
	
	@NotEmpty(message = "Password cannot be empty.")
	private String userPw;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPw() {
		return userPw;
	}

	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}
}
