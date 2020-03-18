package com.huyaoban.security.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.google.code.kaptcha.Producer;

/**
 * 验证码控制器
 * 
 * @author jerry.hu
 *
 */
@Controller
public class CaptchaController {

	@Autowired
	private Producer captchaProducer;

	@GetMapping("/captcha.jpg")
	public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 设置响应内容的类型
		response.setContentType("image/jpeg");

		// 创建验证码文本
		String capText = captchaProducer.createText();
		// 将验证码文本设置到Session
		request.getSession().setAttribute("captcha", capText);

		// 创建验证码图片
		BufferedImage bi = captchaProducer.createImage(capText);
		// 将图片验证码通过响应流输出
		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(bi, "jpg", out);

		// 推送流并关闭流
		try {
			out.flush();
		} finally {
			out.close();
		}
	}

}
