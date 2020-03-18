package com.huyaoban.security.exception;

import org.springframework.security.core.AuthenticationException;

public class VerificationCodeException extends AuthenticationException {

	private static final long serialVersionUID = 5161096762324075054L;

	public VerificationCodeException() {
		super("图形验证码校验失败");
	}

}
