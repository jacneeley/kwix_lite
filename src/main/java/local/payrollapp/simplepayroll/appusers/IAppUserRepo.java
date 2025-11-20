package local.payrollapp.simplepayroll.appusers;

import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface IAppUserRepo {
	Optional<AppUser> findByUsername(String username);
	void save(AppUser appUser);
}