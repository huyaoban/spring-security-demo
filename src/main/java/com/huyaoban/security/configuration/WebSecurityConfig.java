package com.huyaoban.security.configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 认证所有请求,认证通过的用户可访问
		// 设置登录页,登陆页不设访问权限
		// 关闭跨域
		http.authorizeRequests().anyRequest().authenticated().and().formLogin().loginPage("/myLogin.html")
				// 指定处理登录请求的路径，form表单中的action
				.loginProcessingUrl("/login").permitAll()
				.and().csrf().disable();
	}

}
