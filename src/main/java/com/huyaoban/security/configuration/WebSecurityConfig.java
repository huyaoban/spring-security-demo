package com.huyaoban.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

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

	/**
	 * Spring
	 * Security支持各种来源的用户数据，包括内存、数据库、LDAP等。它们被抽象为一个UserDetailsService接口，任何实现了这个接口的
	 * 对象都可以作为认证数据源。InMemoryUserDetailsMananger是UserDetailsService接口中的一个实现类，它将用户数据源保存在内存里，在一些不需要
	 * 引入数据库这种重数据源的系统中很有帮助。
	 */
	@Bean
	public UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

		// 创建2个用户，设置密码，并配置角色
		manager.createUser(User.withUsername("user").password("123").roles("USER").build());
		manager.createUser(User.withUsername("admin").password("123").roles("ADMIN").build());

		return manager;
	}

}
