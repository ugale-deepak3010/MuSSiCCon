package com.UssicConMuSSiCCon.auth.authhandler;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Log4j2
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		System.out.println("-------OAuthSuccessHandler start---------");
		log.info("OAuthSuccessHandler :: onAuthenticationSuccess :: start");
		
		setDefaultTargetUrl("/home");
		setAlwaysUseDefaultTargetUrl(true);
		
		log.info("OAuthSuccessHandler :: onAuthenticationSuccess :: end");
		
		super.onAuthenticationSuccess(request, response, authentication);
	}
}
