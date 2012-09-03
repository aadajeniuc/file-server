package com.project.file;


import com.project.util.FileUtils;
import com.project.util.JavaScriptUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * This Servlet deletes a file or directory
 *
 */

public class FileDeleteServlet
	extends HttpServlet {

	private String contextRoot;

	public FileDeleteServlet() {

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,java.io.IOException {
		doGet(request,response);
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
		java.io.IOException {
		try{
			String fileUrl = (String) request.getAttribute(HttpFileServer.URL_FILE);
			String realPath = (String) request.getAttribute(HttpFileServer.FS_FILE);
			//System.out.println("fileUrl"+fileUrl);

			File fileSystemFile = new File(realPath);
			if (fileSystemFile.exists()) {
				String confirm = request.getParameter("confirm");
				if (confirm != null && confirm.equals("true")) {

					if (fileSystemFile.isDirectory()) {
						FileUtils.deleteRecursive(fileSystemFile);
						super.log(request.getUserPrincipal().getName() + ":directory deleted:" + fileSystemFile.getAbsolutePath());
					}
					else {
						fileSystemFile.delete();
						super.log(request.getUserPrincipal().getName() + ":file deleted:" + fileSystemFile.getAbsolutePath());
					}

					// from here redirect back to parent directory
					String parantUrl = LocateFileFilter.parentRelPath(fileUrl);
					String parentAbs = LocateFileFilter.parentAbsPath(realPath, contextRoot);
					request.setAttribute(HttpFileServer.URL_FILE, parantUrl);
					request.setAttribute(HttpFileServer.FS_FILE, parentAbs);
					// forward
					request.getRequestDispatcher("/servlet/fs/").forward(request, response);

				}
				else {
					request.setAttribute("msg_title", "Confirm");
					request.setAttribute("msg_html", "<span class=\"normaltext\">Confirm the deletion of <b>" +
										 JavaScriptUtils.unescape(fileUrl) +
										 "</b></span>");
					request.setAttribute("msg_jsp_part", "confirm.jsp");
					request.getRequestDispatcher("/message.jsp").forward(request, response);
				}
			}
			else {
				request.setAttribute("msg_title", "Error");
				request.setAttribute("msg_html", "File Not Found");
				request.setAttribute("msg_jsp_part", "back.jsp");
				request.getRequestDispatcher("/message.jsp").forward(request, response);
			}
		}catch (Exception ex){
			log("",ex);
			throw new ServletException(ex);
		}
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		contextRoot = config.getServletContext().getInitParameter("fileRoot").replace('\\', '/');
	}

}
