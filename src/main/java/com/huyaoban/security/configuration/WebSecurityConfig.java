package com.huyaoban.security.configuration;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/admin/api/**").hasAnyAuthority("ROLE_ADMIN").antMatchers("/user/api/**")
				.hasRole("USER").antMatchers("/app/api/**").permitAll().anyRequest().authenticated().and().formLogin();

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("admin").password("{noop}123").roles("ADMIN", "USER").and()
				.withUser("user").password("{noop}123").roles("USER");
	}

}
