package com.huyaoban.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.huyaoban.security.exception.VerificationCodeException;
import com.huyaoban.security.handler.MyAuthenticationFailureHandler;

/**
 * 验证码过滤器
 * 
 * @author jerry.hu
 *
 */
public class VerificationCodeFilter extends OncePerRequestFilter {

	private AuthenticationFailureHandler authenticationFailureHandler = new MyAuthenticationFailureHandler();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// 非登陆请求不校验验证码
		if (!"/auth/form".equals(request.getRequestURI())) {
			filterChain.doFilter(request, response);
		} else {
			try {
				verificationCode(request);
				filterChain.doFilter(request, response);
			} catch (VerificationCodeException e) {
				authenticationFailureHandler.onAuthenticationFailure(request, response, e);
			}
		}

	}

	public void verificationCode(HttpServletRequest request) throws VerificationCodeException {
		String requestCode = request.getParameter("captcha");
		HttpSession session = request.getSession();
		String savedCode = (String) session.getAttribute("captcha");
		if (!StringUtils.isEmpty(savedCode)) {
			// 无论失败还是成功都应该清除验证码.客户端应该再登陆失败时刷新验证码
			session.removeAttribute("captcha");
		}

		if (StringUtils.isEmpty(requestCode) || StringUtils.isEmpty(savedCode) || !requestCode.equals(savedCode)) {
			throw new VerificationCodeException();
		}
	}

}
