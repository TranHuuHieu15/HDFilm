package com.poly.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
 * Servlet implementation class HomeControllerServlet
 */
@WebServlet({ "/index", "/favorites", "/history" })
public class HomeControllerServlet extends HttpServlet {

	private static final int VIDEO_MAX_PAGE_SIZE = 8;
	private VideoService videoService = new VideoServiceImpl();
	private HistoryService historyService = new HistoryServiceImpl();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getServletPath();
		HttpSession session = request.getSession();
		switch (path) {
		case "/index": {
			doGetIndex(request, response);
			break;
		}
		case "/favorites": {
			doGetFavorites(session, request, response);
			break;
		}
		case "/history": {
			doGetHistory(session, request, response);
			break;
		}
		}

	}

	// Link để phần trang localhost:8080/asmJava4/index?page={page}
	private void doGetIndex(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//Đếm số lượng count
		List<Video> countVideo = videoService.findAll();
		//10 video, muốn chia mỗi trang có 8 video -> tổng cộng có 2 trang 10/8 = 1.2 làm trong lên 2
		int maxPage = (int) Math.ceil(countVideo.size() / (double)VIDEO_MAX_PAGE_SIZE);
		request.setAttribute("maxPage", maxPage);
		
		
		List<Video> videos;
		String pageNumber = request.getParameter("page");
		if (pageNumber == null || Integer.valueOf(pageNumber) > maxPage) {
			 videos = videoService.findAll(1, VIDEO_MAX_PAGE_SIZE); 
			 request.setAttribute("currentPage", 1);
		} else {
			videos = videoService.findAll(Integer.valueOf(pageNumber), VIDEO_MAX_PAGE_SIZE);
			request.setAttribute("currentPage", Integer.valueOf(pageNumber));
		}

		request.setAttribute("videos", videos);
		request.getRequestDispatcher("/views/user/index.jsp").forward(request, response);
	}

	private void doGetFavorites(HttpSession session, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = (User) session.getAttribute(SessionAtr.CURRENT_USER);
		List<History> histories = historyService.findByUserAndIsLiked(user.getUsername());
		List<Video> videos = new ArrayList<>();

		histories.forEach(item -> videos.add(item.getVideo()));
		// Hàng 65 tương tự vòng lặp for dưới đây
//			for (int i = 0; i < histories.size(); i++) {
//				videos.add(histories.get(i).getVideo());
//			}

		request.setAttribute("videos", videos);
		request.getRequestDispatcher("/views/user/favorites.jsp").forward(request, response);
	}

	private void doGetHistory(HttpSession session, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = (User) session.getAttribute(SessionAtr.CURRENT_USER);
		List<History> histories = historyService.findByUser(user.getUsername());
		List<Video> videos = new ArrayList<>();
		histories.forEach(item -> videos.add(item.getVideo()));
		// Hàng 65 tương tự vòng lặp for dưới đây
//			for (int i = 0; i < histories.size(); i++) {
//				videos.add(histories.get(i).getVideo());
//			}
		request.setAttribute("videos", videos);
		request.getRequestDispatcher("/views/user/history.jsp").forward(request, response);
	}
}
