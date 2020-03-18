package com.huyaoban.security.provider;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.huyaoban.security.exception.VerificationCodeException;

//UsernamePasswordAuthenticationToken
//UsernamePasswordAuthenticationFilter
//Principal
//Authentication
//AuthenticationProvider
//ProviderManager
//DaoAuthenticationProvider
//由于只是在常规的认证之上加上了图形验证码验证,其他流程并没有变化,所以只需继承DaoAuthenticationProvider, 并稍作增添即可
@Component
public class MyAuthenticationProvider extends DaoAuthenticationProvider {

	public MyAuthenticationProvider(UserDetailsService userDetailsService) {
		this.setUserDetailsService(userDetailsService);
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		// 获取详细信息
		VerificationCodeWebAuthenticationDetails details = (VerificationCodeWebAuthenticationDetails) authentication
				.getDetails();
		// 一旦发现验证码不正确就立刻抛出相应异常信息
		if (!details.getImageCodeIsRight()) {
			throw new VerificationCodeException();
		}

		// 调用父类方法完成密码验证
		super.additionalAuthenticationChecks(userDetails, authentication);
	}

}
