package local.payrollapp.simplepayroll.appusers;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
public class AppUserRepo implements IAppUserRepo{
	
	/**
	 * this class won't do anything in the demo sandbox
	 * so that clients can sign into their users accounts.
	 */
	
	private final HashMap<String, AppUser> users = new HashMap<>();
	
	@Override
	public Optional<AppUser> findByUsername(String username) {
		return Optional.empty();
		//		return _jdbcClient.sql("SELECT * FROM APP_USERS WHERE USERNAME = ?")
//				.param(username)
//				.query(AppUser.class)
//				.optional();
	}

	@Override
	public void save(AppUser appUser) {
//		var saved = _jdbcClient.sql("INSERT INTO APP_USERS("
//				+ "id"
//				+ "username"
//				+ "user_pw"
//				+ "user_roles"
//				+ ")"
//				+ "values(?,?,?,?)")
//				.params(List.of(appUser.getId(), appUser.getUsername(), 
//						appUser.getUserPw(), appUser.getRoles()))
//				.update();
//		Assert.state(saved==1, "Failed to save User: " + appUser.getUsername());
		
	}
}