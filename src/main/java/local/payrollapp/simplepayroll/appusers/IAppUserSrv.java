package local.payrollapp.simplepayroll.appusers;

import java.util.Optional;

public interface IAppUserSrv {
	void saveUser(AppUserDto userDto);
	Optional<AppUser> findUserByUserId(String id);
}