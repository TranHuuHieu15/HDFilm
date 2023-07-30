package com.poly.service.impl;

import javax.servlet.ServletContext;

import com.poly.entity.User;
import com.poly.service.EmailService;
import com.poly.util.SendMailUtil;

public class EmailServiceImpl implements EmailService {

	private static final String EMAIL_WELCOME_SUBJECT = "Welcome to Online Entertainment!";
	private static final String EMAIL_FORGOT_PASSWORD = "Online Entertainment - New Password";

	@Override
	public void sendMail(ServletContext context, User nguoiNhan, String type) {
		// dữu liệu lấy trong trang web.xml
		String host = context.getInitParameter("host");
		String port = context.getInitParameter("port");
		String user = context.getInitParameter("user");
		String pass = context.getInitParameter("pass");

		try {
			String content = null;
			String subject = null;
			switch (type) {
				case "welcome": {
					subject = EMAIL_WELCOME_SUBJECT;
					content = "Dear " + nguoiNhan.getUsername() + ", hope you have a good time!";
					break;
				}
				case "forgot": {
					subject = EMAIL_FORGOT_PASSWORD;
					content = "Dear " + nguoiNhan.getUsername() + ", your new password here: " + nguoiNhan.getPassword();
					break;
				}
				default:
					subject = "Online Entertainment";
					content = "Maybe this mail is wrong, don't care about it";
			}
			SendMailUtil.sendEmail(host, port, user, pass, nguoiNhan.getEmail(), subject, content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
