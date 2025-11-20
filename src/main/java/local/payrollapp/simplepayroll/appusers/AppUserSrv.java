package local.payrollapp.simplepayroll.appusers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class AppUserSrv implements IAppUserSrv{
	private final AppUserRepo _userRepo;
	
	public AppUserSrv(AppUserRepo userRepo) {
		this._userRepo = userRepo;
	}
	
	@Override
	public void saveUser(AppUserDto userDto) {
		AppUser appuser = new AppUser(
					//userDto.getId(),
					userDto.getUsername(),
					userDto.getUserPw(),
					userDto.getUserRoles()
				);
		_userRepo.save(appuser);
	}

	@Override
	public Optional<AppUser> findUserByUserId(String username) {
		return _userRepo.findByUsername(username);
	}
	
//	private PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder(); 
//	}
	
//	@PostConstruct 
//	private void init() {
//		PasswordEncoder encoder = passwordEncoder();
//		List<String> roles = new ArrayList<String>();
//		roles.add("USER");
//		roles.add("ADMIN");
//		
//		AppUserDto newUser = new AppUserDto();
//		//newUser.setId(Long.valueOf(5593));
//		newUser.setUsername("AS5993");
//		newUser.setUserPw(encoder.encode("3orrest-4ump"));
//		newUser.setUserRoles(List.of(roles.get(0)));
//		saveUser(newUser);
//		
//		AppUserDto admin = new AppUserDto();
//		//newUser.setId(Long.valueOf(1001));
//		admin.setUsername("Admin");
//		admin.setUserPw(encoder.encode("INFO-23192"));
//		admin.setUserRoles(List.of(roles.get(1)));
//		saveUser(admin);
//	}
}