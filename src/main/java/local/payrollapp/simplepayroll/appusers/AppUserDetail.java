package local.payrollapp.simplepayroll.appusers;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetail { //implements UserDetailsService{
	/**
	 * this class would be used to manage sign-in page,
	 * but that is no needed in this demo version.
	 */
/*
	private final IAppUserRepo _userRepo;
	
	public AppUserDetail(IAppUserRepo userRepo) {
		this._userRepo = userRepo;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			//List<AppUsers> users = _userRepo.findAll();
			Optional<AppUser> appUser = _userRepo.findByUsername(username);
			AppUser user = appUser.get();
			Set<SimpleGrantedAuthority> auth = user.getRoles().stream()
					.map((role) -> new SimpleGrantedAuthority(role))
					.collect(Collectors.toSet());
			return new org.springframework.security.core.userdetails.User(username,user.getUserPw(),auth);
		}
		catch (Exception e) {
			throw e;
		}
	} */
} 