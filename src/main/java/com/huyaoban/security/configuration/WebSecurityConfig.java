package com.huyaoban.security.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

@EnableWebSecurity(debug = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private FindByIndexNameSessionRepository sessionRepository;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/admin/api/**").hasAnyAuthority("ROLE_ADMIN").antMatchers("/user/api/**")
				.hasRole("USER").antMatchers("/app/api/**").permitAll().anyRequest().authenticated()
				// 最大会话数设置为1，那么新登录的会话会踢掉旧的会话
				.and().formLogin().and().sessionManagement().maximumSessions(1)
				// 使用Session提供的会话注册表
				.sessionRegistry(sessionRegistry());

	}

	@Bean
	public UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(User.withUsername("user").password("123").roles("USER").build());
		manager.createUser(User.withUsername("admin").password("123").roles("USER", "ADMIN").build());

		return manager;
	}

	/**
	 * 启用会话创建和销毁的事件监听器，用于管理会话的创建和销毁
	 * 
	 * @return
	 */
	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}

	@Bean
	public SpringSessionBackedSessionRegistry sessionRegistry() {
		return new SpringSessionBackedSessionRegistry(sessionRepository);
	}
}
