package com.huyaoban.security.configuration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
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
				// 设置注销登录，注销请求URL为/myLogout，默认为/logout
				.userDetailsService(myUserDetailsService).and().csrf().disable().logout().logoutUrl("/myLogout")
				// 注销成功后跳转到的URL
				// 注销成功的处理方式，不同于logoutSuccessUrl的重定向，logoutSuccessHandler更加灵活
				.logoutSuccessUrl("/app/api/hello").logoutSuccessHandler(new LogoutSuccessHandler() {

					@Override
					public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) throws IOException, ServletException {

					}
					// 失效用户的HttpSession，注销成功删除指定的cookie，清除用户的验证信息
				}).invalidateHttpSession(true).deleteCookies("cookie1", "cookie2").clearAuthentication(true)
				// 用于注销的处理句柄，允许自定义一些清理策略
				// 事实上LogoutSuccessHandler也能做到
				.addLogoutHandler(new LogoutHandler() {

					@Override
					public void logout(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) {

					}

				});
		
		// logoutSuccessHandler会清空logoutSuccessUrl属性导致注销登录成功后没有跳转，把logoutSuccessHandler去掉或者在logoutSuccessHandler中实现跳转，可查看LogoutConfigurer

	}


}
