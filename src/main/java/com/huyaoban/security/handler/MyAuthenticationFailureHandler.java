package com.huyaoban.security.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException e) throws IOException, ServletException {
		response.setContentType("application/json;charset=utf-8");
		response.setStatus(401);
		PrintWriter out = response.getWriter();
		out.write(
				"{\"error_code\":\"401\", \"name\":\"" + e.getClass() + "\", \"message\": \"" + e.getMessage() + "\"}");
	}

}
