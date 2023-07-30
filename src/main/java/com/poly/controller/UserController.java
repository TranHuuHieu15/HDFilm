package com.poly.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.poly.constant.SessionAtr;
import com.poly.entity.User;
import com.poly.service.EmailService;
import com.poly.service.UserService;
import com.poly.service.impl.EmailServiceImpl;
import com.poly.service.impl.UserServiceImpl;

/**
 * Servlet implementation class UserController
 */
@WebServlet(urlPatterns = { "/login", "/logout", "/register", "/forgotPass", "/changePass" })
public class UserController extends HttpServlet {

	private static final long serialVersionUID = 910241445867414681L;

	private UserService userService = new UserServiceImpl();
	private EmailService emailService = new EmailServiceImpl();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();

		String path = request.getServletPath(); // http://localhost:8080/asmJava4/login>user....>>> path = "/login"
		switch (path) {
		case "/login": {
			doGetLogin(request, response);
			break;
		}
		case "/logout": {
			doGetLogout(session, request, response);
			break;
		}
		case "/register": {
			doGetResgister(request, response);
			break;
		}
		case "/forgotPass": {
			doGetForgotPass(request, response);
			break;
		}
		}
	}

	private void doGetLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/views/user/login.jsp").forward(request, response);
	}

	private void doGetResgister(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/views/user/register.jsp").forward(request, response);
	}

	private void doGetLogout(HttpSession session, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		session.removeAttribute(SessionAtr.CURRENT_USER);
		response.sendRedirect("index");
	}

	private void doGetForgotPass(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/views/user/forgot-pass.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// khi đăng nhập thành công thì lưu vào session
		HttpSession session = req.getSession();
		String path = req.getServletPath(); // http://localhost:8080/asmJava4/login>user....>>> path = "/login"
		switch (path) {
		case "/login": {
			doPostLogin(session, req, resp);
			break;
		}
		case "/register": {
			doPostResgister(session, req, resp);
			break;
		}
		case "/forgotPass": {
			doPostForgotPass(req, resp);
			break;
		}
		case "/changePass": {
			doPostChangePass(session, req, resp);
			break;
		}
		}
	}

	private void doPostChangePass(HttpSession session, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("application/json");
		String currentEmail = req.getParameter("currentEmail");
		String currentPass = req.getParameter("currentPass");
		String newPass = req.getParameter("newPass");
//		System.out.println("Email hien tai "+currentEmail);
//		System.out.println("pass hien tai "+currentPass);
//		System.out.println("pass moi "+newPass);
		User currentUser = (User) session.getAttribute(SessionAtr.CURRENT_USER);
		
		if (currentUser.getPassword().equals(currentPass)) {
			currentUser.setPassword(newPass);
			currentUser.setEmail(currentEmail);
			User updatedUser = userService.update(currentUser);
			if (updatedUser != null) {
				session.setAttribute(SessionAtr.CURRENT_USER, updatedUser);
				resp.setStatus(204);
			} else {
				System.out.println("Loi roi");
				resp.setStatus(400);
			}
		} else {
			System.out.println("Loi nha bro");
			resp.setStatus(400);
		}
	}

	private void doPostForgotPass(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("application/json");
		String email = req.getParameter("email");
		User userWithNewPass = userService.resetPassword(email);
		System.out.println(userWithNewPass);
		if (userWithNewPass != null) {
			emailService.sendMail(getServletContext(), userWithNewPass, "forgot");
			resp.setStatus(204);
		} else {
			resp.setStatus(400);
		}
	}

	private void doPostLogin(HttpSession session, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		User user = userService.login(username, password);
		if (user != null && user.getIsAdmin() == Boolean.TRUE) {
			session.setAttribute(SessionAtr.CURRENT_USER, user);
			// nếu đăng nhập thành công thì chuyển đến trang index
			response.sendRedirect("admin");
		} else if (user != null) {
			session.setAttribute(SessionAtr.CURRENT_USER, user);
			// nếu đăng nhập thành công thì chuyển đến trang index
			response.sendRedirect("index");
		} else {
			// Nếu đăng nhập thất bại thì hiện lại trang login
			response.sendRedirect("login");
		}
	}

	private void doPostResgister(HttpSession session, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		User user = userService.create(username, password, email);

		if (user != null) {
			emailService.sendMail(getServletContext(), user, "welcome");
			session.setAttribute(SessionAtr.CURRENT_USER, user);
			response.sendRedirect("index");
		} else {
			// nếu đăng ký thất bại thì tiếp tục trả về trang register
			response.sendRedirect("register");
		}
	}
}
