package com.poly.controller.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.constant.SessionAtr;
import com.poly.dto.UserDto;
import com.poly.dto.VideoLikedInfo;
import com.poly.entity.User;
import com.poly.service.StatsService;
import com.poly.service.UserService;
import com.poly.service.impl.StatsServiceImpl;
import com.poly.service.impl.UserServiceImpl;

/**
 * Servlet implementation class HomeControllerServlet
 */
@WebServlet(name = "HomeControllerOfAdmin", urlPatterns = { "/admin","/admin/favorites" })
public class HomeControllerServlet extends HttpServlet {

	private static final long serialVersionUID = -6424669436112388755L;
	
	private StatsService statsService = new StatsServiceImpl();
	private UserService userService = new UserServiceImpl();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute(SessionAtr.CURRENT_USER);
		
		if (currentUser != null && currentUser.getIsAdmin() == Boolean.TRUE) {
			
			String path = request.getServletPath();
			switch (path) {
				case "/admin": {
					doGetHome(request, response);
					break;
				}
				case "/admin/favorites": {
					doGetFavorites(request, response);
					break;
				}
			}
				
		} else {
			response.sendRedirect("index");
		}
		
	}

	private void doGetHome(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<VideoLikedInfo> videos = statsService.findVideoLikedInfo();
		request.setAttribute("videos", videos);
		
		request.getRequestDispatcher("/views/admin/home.jsp").forward(request, response);
	}

	
	//localhost:8080/asmJava4/admin/favorite?href={videoHref}
	private void doGetFavorites(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		
		String videoHref = request.getParameter("href");
		List<UserDto > users = userService.findUsersLikedVideoByVideoHref(videoHref);
		if (users.isEmpty()) {
			response.setStatus(400);
		}else {
			ObjectMapper mapper = new ObjectMapper();
			String dataResponse = mapper.writeValueAsString(users);
			response.setStatus(200); // chổ ni 200 vì có data nếu ko có thì dùng 204
			out.print(dataResponse);
			out.flush(); // đẩy nó ra
		}
	}
}
