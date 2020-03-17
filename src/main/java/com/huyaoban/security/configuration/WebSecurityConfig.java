package com.huyaoban.security.configuration;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 认证所有请求,认证通过的用户可访问
		// 设置登录页,登陆页不设访问权限
		// 关闭跨域
		http.authorizeRequests().anyRequest().authenticated().and().formLogin().loginPage("/myLogin.html")
				// 指定处理登录请求的路径，form表单中的action，指定登录成功时的处理逻辑
				.loginProcessingUrl("/login").successHandler(new AuthenticationSuccessHandler() {
					@Override
					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) throws IOException, ServletException {
						response.setContentType("application/json;charset=utf-8");

						// 登录成功，输出登录结果
						PrintWriter out = response.getWriter();
						out.write("{\"error_code\":\"0\", \"message\":\"欢迎登录系统\"}");
					}

					// 指定登录失败时的处理逻辑
				}).failureHandler(new AuthenticationFailureHandler() {
					@Override
					public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
							AuthenticationException exception) throws IOException, ServletException {
						response.setContentType("application/json;charset=utf-8");
						response.setStatus(401);

						// 输出失败原因
						PrintWriter out = response.getWriter();
						out.write("{\"error_code\":\"401\", \"name\":\"" + exception.getClass() + "\", \"message\":\""
								+ exception.getMessage() + "\"}");
					}

				}).permitAll()
				.and().csrf().disable();
	}

}
