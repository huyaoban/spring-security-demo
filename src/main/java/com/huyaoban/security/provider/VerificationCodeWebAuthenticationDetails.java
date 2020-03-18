package com.huyaoban.security.provider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.util.StringUtils;

public class VerificationCodeWebAuthenticationDetails extends WebAuthenticationDetails {
	private static final long serialVersionUID = -8158649114373857383L;

	private boolean imageCodeIsRight = false;

	private String imageCode;

	private String savedImageCode;

	public boolean getImageCodeIsRight() {
		return this.imageCodeIsRight;
	}

	public VerificationCodeWebAuthenticationDetails(HttpServletRequest request) {
		super(request);
		
		this.imageCode = request.getParameter("captcha");
		HttpSession session = request.getSession();
		this.savedImageCode = (String) session.getAttribute("captcha");
		if (!StringUtils.isEmpty(this.savedImageCode)) {
			// 无论失败还是成功都应该清除验证码.客户端应该再登陆失败时刷新验证码
			session.removeAttribute("captcha");

			// 当验证码正确时设置状态
			if (!StringUtils.isEmpty(imageCode) && imageCode.equals(savedImageCode)) {
				this.imageCodeIsRight = true;
			}
		}
	}

}
