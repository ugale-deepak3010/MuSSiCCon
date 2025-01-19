package com.UssicCon.auth;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class AuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

	public AuthenticationEntryPoint(String loginFormUrl) {
		
		super(loginFormUrl);
		System.out.println("-------AuthenticationEntryPoint--------");
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		System.out.println("------AuthenticationEntryPoint commence-----------");
		
		System.err.println(request.getRequestURI());
		
		System.out.println("------AuthenticationEntryPoint commence-----------");
		

		String ajaxHeader = request.getHeader("X-Requested-With");
		
		if ("XMLHttpRequest".equals(ajaxHeader)) {
			
			log.error("response error");
			
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Ajax Request Denied (Session Expired)");
			
		} else {
			System.out.println("------AuthenticationEntryPoint commence else-----------");
			super.commence(request, response, authException);
		}
	}
}
