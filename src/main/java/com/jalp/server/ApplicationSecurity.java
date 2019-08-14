package com.jalp.server;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@Configuration
@EnableWebSecurity
@Import(SecurityProblemSupport.class)
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private SecurityProblemSupport problemSupport;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http//
				.csrf().disable()
				// enable stateless session
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				// handle an authorized attempts
				.exceptionHandling().accessDeniedHandler(problemSupport).authenticationEntryPoint(problemSupport).and()
				// Add a filter to validate the tokens with every request
				.addFilterAfter(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class)
				// authorization requests config
				.authorizeRequests()
				// allow all who are accessing "auth" service
				.antMatchers(HttpMethod.POST, "/token").permitAll()
				// must be an admin if trying to access admin area (authentication is also
				// required here)
				.antMatchers("/gallery" + "/admin/**").hasRole("ADMIN")
				// Any other request must be authenticated
				.anyRequest().authenticated();
	}

	@Autowired
	private DataSource dataSource;

	@Bean
	public JdbcUserDetailsManager userDetailsManager() {
		JdbcUserDetailsManager manager = new JdbcUserDetailsManager();
		manager.setDataSource(dataSource);

		return manager;
	}

	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public JwtRequestFilter authenticationTokenFilterBean() throws Exception {
		return new JwtRequestFilter();
	}

}
