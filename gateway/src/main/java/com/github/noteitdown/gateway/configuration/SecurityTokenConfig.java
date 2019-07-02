package com.github.noteitdown.gateway.configuration;

import com.github.noteitdown.common.security.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {

	private final JwtProperties jwtProperties;

	@Autowired
	public SecurityTokenConfig(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
	}

	@Override
  	protected void configure(HttpSecurity http) throws Exception {
    	   http
		.csrf().disable()
	 	    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		    .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
		.and()
		   .addFilterAfter(new JwtTokenAuthenticationFilter(jwtProperties), UsernamePasswordAuthenticationFilter.class)
		.authorizeRequests()
		   .antMatchers(HttpMethod.POST, jwtProperties.getUri()).permitAll()
		   .anyRequest().authenticated();
	}
}