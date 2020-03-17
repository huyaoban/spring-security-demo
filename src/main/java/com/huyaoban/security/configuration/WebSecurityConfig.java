package com.huyaoban.security.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * 注入数据源
	 */
	@Autowired
	private DataSource dataSource;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 假设在/admin/api/下的内容是系统后台管理相关的API，在/app/api/下的内容是面向客户端公开访问的API，在/user/api/下的内容是
		// 用户操作自身数据相关的API；显然，/admin/api必须拥有管理员权限才能进行操作，而/user/api必须在用户登录后才能进行操作。
		http.authorizeRequests().antMatchers("/admin/api/**").hasRole("ADMIN").antMatchers("/user/api/**")
				.hasRole("USER").antMatchers("/app/api/**").permitAll().anyRequest().authenticated().and().formLogin()
				.loginPage("/myLogin.html").loginProcessingUrl("/login").permitAll().and().csrf().disable();
	}

	/**
	 * 让Spring Security使用数据库来管理用户
	 */
	@Bean
	public UserDetailsService userDetailsService() {
		JdbcUserDetailsManager manager = new JdbcUserDetailsManager();
		// 设置数据源
		manager.setDataSource(dataSource);

		// 创建2个用户，设置密码，并配置角色
		if (!manager.userExists("user")) {
			manager.createUser(User.withUsername("user").password("123").roles("USER").build());
		}
		if (!manager.userExists("admin")) {
			manager.createUser(User.withUsername("admin").password("123").roles("USER", "ADMIN").build());
		}

		return manager;
	}

}
