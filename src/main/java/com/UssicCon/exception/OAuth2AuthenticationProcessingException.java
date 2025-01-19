package com.UssicCon.exception;

import org.springframework.security.core.AuthenticationException;

@SuppressWarnings("serial")
public class OAuth2AuthenticationProcessingException extends AuthenticationException {
	
	public OAuth2AuthenticationProcessingException(String msg) {
		super(msg);
	}
}
