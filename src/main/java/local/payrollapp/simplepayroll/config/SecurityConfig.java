package local.payrollapp.simplepayroll.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import local.payrollapp.simplepayroll.appusers.MockAppUsers;
import local.payrollapp.simplepayroll.components.OttsHandler;

import org.springframework.security.config.Customizer;

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
}
