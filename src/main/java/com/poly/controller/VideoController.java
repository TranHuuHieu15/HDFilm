package com.poly.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.poly.constant.SessionAtr;
import com.poly.entity.History;
import com.poly.entity.User;
import com.poly.entity.Video;
import com.poly.service.HistoryService;
import com.poly.service.VideoService;
import com.poly.service.impl.HistoryServiceImpl;
import com.poly.service.impl.VideoServiceImpl;

/**
 * Servlet implementation class VideoController
 */
@WebServlet(urlPatterns = "/video")
public class VideoController extends HttpServlet {

	private static final long serialVersionUID = -7161994045448170677L;

	private VideoService videoService = new VideoServiceImpl();
	private HistoryService historyService = new HistoryServiceImpl();
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// http://localhost:8080/asmJava4/video?action=watch&id={href}
		// http://localhost:8080/asmJava4/video?action=like&id={href}

		String actionParam = request.getParameter("action");
		String href = request.getParameter("id");
		HttpSession session = request.getSession();

		switch (actionParam) {
			case "watch": {
				doGetWatch(session, href, request, response);
				break;
			}
			case "like": {
				doGetLike(session, href, request, response);
				break;
			}
		}
	}

	// http://localhost:8080/asmJava4/video?action=watch&id={href}
	private void doGetWatch(HttpSession session, String href, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Video video = videoService.findByHref(href);
		request.setAttribute("video", video);
		
		// tạo cờ để đánh dấu true flase thuận tiện cho việc sử dụng ajax
		User currentUser = (User) session.getAttribute(SessionAtr.CURRENT_USER);
		if (currentUser != null) {
			History history = historyService.create(currentUser, video);
			request.setAttribute("flagLikedBtn", history.getIsLiked());
		}
		
		request.getRequestDispatcher("/views/user/video-detail.jsp").forward(request, response);
	}

	// http://localhost:8080/asmJava4/video?action=like&id={href}
	private void doGetLike(HttpSession session, String href, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json"); // vì ajax nên dùng cái ni để conver hắn
		User currentUser = (User) session.getAttribute(SessionAtr.CURRENT_USER);
		boolean result = historyService.updateLikeOrUnlike(currentUser, href);
		if (result == true) {
			// 204 là succeed but not data
			response.setStatus(204);
		}else {
			// 400 là fail but not data
			response.setStatus(400);
		}
	}

}
