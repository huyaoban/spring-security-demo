package com.huyaoban.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.huyaoban.security.mapper.UserMapper;
import com.huyaoban.security.model.User;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userMapper.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("用户不存在");
		}

		// 转换权限,以逗号分隔
		user.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoles()));

		return user;
	}

	/**
	 * 自行实现权限的转换，以分号分隔
	 * 
	 * @param roles
	 * @return
	 */
	private List<GrantedAuthority> generateAuthorities(String roles) {
		List<GrantedAuthority> authorities = new ArrayList<>();

		if (StringUtils.isEmpty(roles)) {
			String[] roleArray = roles.split(";");
			for (String role : roleArray) {
				authorities.add(new SimpleGrantedAuthority(role));
			}
		}

		return authorities;
	}

}
