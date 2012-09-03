package com.project.file;

import com.project.util.ZipExtractor;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;

/**
 * Used to upload a zip of new files
 */

public class ZipUploadServlet
	extends HttpServlet {

	private String contextRoot;
	private String tempDir = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,java.io.IOException {
		doGet(request,response);
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
		java.io.IOException {
		try {

            MultipartRequest mpr = new MultipartRequest(tempDir);
			File tempData = mpr.getFile(request);

			String directory = (String) request.getAttribute(HttpFileServer.FS_FILE);
			ZipExtractor zipper = new ZipExtractor(new File(directory), tempData);
			ArrayList results = zipper.extract(true);
			if (results.size() == 0) {
				request.setAttribute("msg_title", "Error?");
				request.setAttribute("msg_html", "No files were extracted, was the file a valid zip or jar file?");
				request.setAttribute("msg_jsp_part", "back.jsp");
				request.getRequestDispatcher("/message.jsp").forward(request, response);
			}

			log(request.getUserPrincipal().getName()+":Zip file uploaded and extracted:"+directory+tempData.getName());
			tempData.delete();
			request.getRequestDispatcher("/servlet/fs/").forward(request, response);
		}
		catch (ServletException ex) {
			log("",ex);
			throw ex;
		}
		catch (Exception ex) {
			log("",ex);
			request.setAttribute("msg_title", "Error?");
			request.setAttribute("msg_html", "Error unpacking the zip file, was it a valid zip file");
			request.setAttribute("msg_jsp_part", "back.jsp");
			request.getRequestDispatcher("/message.jsp").forward(request, response);
		}
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		if (contextRoot == null) {
				contextRoot = config.getServletContext().getInitParameter("fileRoot").replace('\\', '/');
				tempDir = config.getServletContext().getInitParameter("tempDir");
		}
	}

}
