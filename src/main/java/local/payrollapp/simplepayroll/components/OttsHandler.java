package local.payrollapp.simplepayroll.components;

import java.io.IOException;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ott.DefaultOneTimeToken;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.authentication.ott.RedirectOneTimeTokenGenerationSuccessHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import local.payrollapp.simplepayroll.appusers.MockAppUsers;

@Component
public class OttsHandler implements OneTimeTokenGenerationSuccessHandler {
	private static Logger log = LoggerFactory.getLogger(OttsHandler.class);
	private final OneTimeTokenGenerationSuccessHandler redirectHandler = new RedirectOneTimeTokenGenerationSuccessHandler("/ott/sent");
	private final MockAppUsers _appUsers;
	
	public OttsHandler(MockAppUsers appUsers) {
		this._appUsers = appUsers;
	}
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, OneTimeToken oneTimeToken)
			throws IOException, ServletException {
		
		oneTimeToken = new DefaultOneTimeToken(
				oneTimeToken.getTokenValue(),
				oneTimeToken.getUsername(),
				oneTimeToken.getExpiresAt().plus(Duration.ofMinutes(25)));
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(UrlUtils.buildFullRequestUrl(request))
				.replacePath(request.getContextPath())
				.replaceQuery(null)
				.fragment(null)
				.path("login/ott")
				.queryParam("token", oneTimeToken.getTokenValue());
		
		String magicLink = builder.toUriString();
		
		String token = magicLink.split("=")[1];
		Object[] tmpUserInfo = {token, oneTimeToken.getExpiresAt()};
		String tmpUser = this.generateTmpUser();
		this._appUsers.getUserCredentials().put(tmpUser, tmpUserInfo);
		this._appUsers.setTmpUser(tmpUser);
		
		log.info("magicLink: " + magicLink);
		log.info("Expires at: " + oneTimeToken.getExpiresAt());
		
		this.redirectHandler.handle(request, response, oneTimeToken);
	}
	
//	private String getEmail(String email) {
//		//get email from db or something
//		log.info("retrieving email from user: \t{}", email);
//		return "someemail@email.com";
//	}
	
	private String generateTmpUser() {
		double max = 999;
		double min = 1;
		double randomId = (Math.random() * (max - min)) + min;
		return new StringBuilder().append("guest_").append(randomId).toString();
	}

}
