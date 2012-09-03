package com.project.file;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class LogoutServlet
	extends HttpServlet {

	private String contextRoot = null;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			request.setAttribute(HttpFileServer.URL_FILE, "/");
			request.setAttribute(HttpFileServer.FS_FILE, contextRoot);
			request.getSession(true).invalidate();
			super.log(request.getUserPrincipal().getName() + ":User logged out");

			request.setAttribute("msg_title", "Logged out");
			request.setAttribute("msg_html", "You have logged out [<a href=\"servlet/fs/\">click here</a>] to login again");
			request.getRequestDispatcher("/message.jsp").forward(request, response);
		}catch (Exception ex){
			log("",ex);
			throw new ServletException(ex);
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		if (contextRoot == null) {
				contextRoot = config.getServletContext().getInitParameter("fileRoot").replace('\\', '/');
		}
	}
}
