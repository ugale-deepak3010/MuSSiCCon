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
public class LocalSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		System.out.println("--------LocalSuccessHandler--------");
		
		log.info("LocalSuccessHandler :: onAuthenticationSuccess :: start");
		log.info("Logged in user => " + authentication.getPrincipal().toString());
		
		if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
			
			getRedirectStrategy().sendRedirect(request, response, "/home/");
			
		} else
			
			getRedirectStrategy().sendRedirect(request, response, "/admin/");

		super.onAuthenticationSuccess(request, response, authentication);
	}
}
