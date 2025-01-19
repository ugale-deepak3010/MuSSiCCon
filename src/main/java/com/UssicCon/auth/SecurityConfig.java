package com.UssicCon.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.UssicCon.auth.authhandler.LocalSuccessHandler;
import com.UssicCon.auth.authhandler.OAuthSuccessHandler;
import com.UssicCon.auth.oauth.OAuth2UserService;
import com.UssicCon.service.impl.LocalUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final LocalSuccessHandler localSuccessHandler;
    private final OAuthSuccessHandler oAuthSuccessHandler;
    private final LocalUserDetailsService localUserDetailsService;
    private final OAuth2UserService oAuth2UserService;

    public SecurityConfig(LocalSuccessHandler localSuccessHandler, OAuthSuccessHandler oAuthSuccessHandler, LocalUserDetailsService localUserDetailsService, OAuth2UserService oAuth2UserService) {
        this.localSuccessHandler = localSuccessHandler;
        this.oAuthSuccessHandler = oAuthSuccessHandler;
        this.localUserDetailsService = localUserDetailsService;
        this.oAuth2UserService = oAuth2UserService;
    }
    
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		System.out.println("------SecurityConfig configure----");
		
		PasswordEncoder bCrypt = passwordEncoder();
		auth.userDetailsService(localUserDetailsService).passwordEncoder(bCrypt);

		auth.inMemoryAuthentication().withUser("admin1").password(bCrypt.encode("1")).roles("ADMIN");
		auth.inMemoryAuthentication().withUser("admin2").password(bCrypt.encode("2")).roles("ADMIN");
		auth.inMemoryAuthentication().withUser("admin3").password(bCrypt.encode("3")).roles("ADMIN");
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		System.out.println("---------SecurityConfig HttpSecurity ---------");
		
		http.csrf().disable()
			.authorizeRequests()
			.antMatchers("/login", "/signup", "/webjars/**", "/assets/**","/xpotify-data/**")
			.permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.formLogin()
			.loginPage("/login")
			.successHandler(localSuccessHandler)
			.and()
			.headers(headers -> headers.addHeaderWriter(new StaticHeadersWriter("X-Content-Security-Policy", "script-src 'self'")))
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.invalidateHttpSession(true)
			.logoutSuccessUrl("/login")
			.and()
			.oauth2Login()
			.userInfoEndpoint()
			.userService(oAuth2UserService)
			.and()
			.loginPage("/login")
			.successHandler(oAuthSuccessHandler)
			.and()
			.exceptionHandling()
			.authenticationEntryPoint(new AuthenticationEntryPoint("/login"));
	}
}
