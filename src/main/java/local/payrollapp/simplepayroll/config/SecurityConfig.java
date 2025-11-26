package local.payrollapp.simplepayroll.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import local.payrollapp.simplepayroll.GlobalConstants;
import local.payrollapp.simplepayroll.appusers.MockAppUsers;
import local.payrollapp.simplepayroll.components.OttsHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	private final MockAppUsers _appUsers;
	
	SecurityConfig(MockAppUsers appUsers){
		this._appUsers = appUsers;
	}
	
	@SuppressWarnings("removal")
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		
		OttsHandler ottsHandler = new OttsHandler(this._appUsers);
		
		return http
				.authorizeHttpRequests(auth -> auth
				.requestMatchers("styles/global.css").permitAll()
				.requestMatchers("/ott/sent").permitAll()
				.requestMatchers("/login/ott").permitAll()
				.requestMatchers("/login/token").permitAll()
				.anyRequest().authenticated()
				)
				.headers(headers -> headers.frameOptions().sameOrigin())
				.formLogin(form -> form.loginPage("/login").permitAll())
				.oneTimeTokenLogin(ott -> ott
						.loginProcessingUrl("/login/ott")
						.tokenGeneratingUrl("/login/ott")
						.tokenGenerationSuccessHandler(ottsHandler)
				)
				.logout(logout -> logout
						.logoutUrl("/logout").permitAll())
				.build();
	}
	
	@Bean
	InMemoryUserDetailsManager imudm() {
		String pwd1 = new StringBuilder().append("{noop}").append(GlobalConstants.PWD).toString();
		String pwd2 = new StringBuilder().append("{noop}").append(GlobalConstants.PWD2).toString();
		var admin = User.withUsername(GlobalConstants.USER1)
				.password(pwd1)
				.build();
		var user = User.withUsername("guest")
				.password(pwd2)
				.build();
		return new InMemoryUserDetailsManager(admin, user);
	}
}
