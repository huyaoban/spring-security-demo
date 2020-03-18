package com.huyaoban.security.configuration;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import com.huyaoban.security.handler.MyAuthenticationFailureHandler;
import com.huyaoban.security.service.MyUserDetailsService;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> myWebAuthenticationDetailsSource;

	@Autowired
	private MyUserDetailsService myUserDetailsService;

	@Autowired
	private DataSource dataSource;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
		jdbcTokenRepository.setDataSource(dataSource);
		jdbcTokenRepository.setCreateTableOnStartup(true);

		http.authorizeRequests().antMatchers("/admin/api/**").hasAnyAuthority("ROLE_ADMIN").antMatchers("/user/api/**")
				// 开放captcha.jpg的访问权限
				.hasRole("USER").antMatchers("/app/api/**", "/captcha.jpg").permitAll().anyRequest().authenticated()
				.and().formLogin().authenticationDetailsSource(myWebAuthenticationDetailsSource)
				.loginPage("/myLogin.html").loginProcessingUrl("/auth/form").permitAll()
				// 启用自动登陆,记住我
				.failureHandler(new MyAuthenticationFailureHandler()).and().rememberMe()
				.tokenRepository(jdbcTokenRepository)
				.userDetailsService(myUserDetailsService).and().csrf().disable();

	}


}
