package com.poly.controller.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.poly.constant.SessionAtr;
import com.poly.entity.User;
import com.poly.service.UserService;
import com.poly.service.impl.UserServiceImpl;

@WebServlet({ "/admin/user" })
public class UserController extends HttpServlet {

	private static final long serialVersionUID = 6059419205172657974L;
	UserService userService = new UserServiceImpl();
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		User currentUser = (User) session.getAttribute(SessionAtr.CURRENT_USER);

		if (currentUser != null && currentUser.getIsAdmin() == Boolean.TRUE) {
			String action = req.getParameter("action");
			switch (action) {
				case "view": {
					doGetOverView(req, resp);
					break;
				}
				case "delete": {
					doGetDelete(req, resp);
					break;
				}
				case "add": {
					req.setAttribute("isEdit", false);
					doGetAdd(req, resp);
					break;
				}
				case "edit": {
					req.setAttribute("isEdit", true);
					doGetEdit(req, resp);
					break;
				}
			}
		}else {
			resp.sendRedirect("index");
		}
	}
	// TODO: /asmJava4/admin/user?action=edit&username={username}
	private void doGetEdit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String username = req.getParameter("username");
		User user = userService.findByUsername(username);
		req.setAttribute("user", user);
		
		req.getRequestDispatcher("/views/admin/user-edit.jsp").forward(req, resp);
	}
	// TODO: /asmJava4/admin/user?action=add
	private void doGetAdd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		req.getRequestDispatcher("/views/admin/user-edit.jsp").forward(req, resp);
	}
	// TODO: /asmJava4/admin/user?action=view
	private void doGetOverView(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<User> users = userService.findAll();
		req.setAttribute("users", users);
		req.getRequestDispatcher("/views/admin/user-overview.jsp").forward(req, resp);
	}
	// TODO: /asmJava4/admin/user?action=delete&username={delete}
	private void doGetDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		resp.setContentType("application/json");
		String username = req.getParameter("username");
		
		User userDeleted = userService.delete(username);
		
		if (userDeleted != null) {
			resp.setStatus(204);
		}else {
			resp.setStatus(400);
		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		User currentUser = (User) session.getAttribute(SessionAtr.CURRENT_USER);
		
		if (currentUser != null && currentUser.getIsAdmin() == Boolean.TRUE) {
			String action = req.getParameter("action");
			switch (action) {
				case "add": {
					doPostAdd(req, resp);
					break;
				}
				case "edit": {
					doPostEdit(req, resp);
					break;
				}
			}	
		}else {
			resp.sendRedirect("/index");
		}
	}
	private void doPostEdit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		String username = req.getParameter("newUsername");
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		String usernameOrigin = req.getParameter("usernameOrigin");
		
		User user = userService.findByUsername(usernameOrigin);
		user.setEmail(email);
		user.setPassword(password);
		user.setUsername(username);
		
		User userReturn = userService.update(user);
		if (userReturn != null) {
			resp.setStatus(204);
		} else {
			resp.setStatus(400);
		}
	}
	private void doPostAdd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		String username = req.getParameter("username");
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		
//		User user = new User();
//		user.setEmail(email);
//		user.setPassword(password);
//		user.setUsername(username);
		
		User userReturn = userService.create(username, password, email);
		if (userReturn != null) {
			resp.setStatus(204);
		} else {
			resp.setStatus(400);
		}
	}
	
}
