package com.huyaoban.security.configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 假设在/admin/api/下的内容是系统后台管理相关的API，在/app/api/下的内容是面向客户端公开访问的API，在/user/api/下的内容是
		// 用户操作自身数据相关的API；显然，/admin/api必须拥有管理员权限才能进行操作，而/user/api必须在用户登录后才能进行操作。
		http.authorizeRequests().antMatchers("/admin/api/**").hasRole("ADMIN").antMatchers("/user/api/**")
				.hasRole("USER").antMatchers("/app/api/**").permitAll().anyRequest().authenticated().and().formLogin()
				.loginPage("/myLogin.html").loginProcessingUrl("/login").permitAll().and().csrf().disable();
	}


}
