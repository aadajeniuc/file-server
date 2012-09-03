package com.project.file;

import com.project.util.ZipCompressor;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * <p>Zips up an entire directory and streams it to the client. </p>
 *
 */

public class ZipDownloadServlet
	extends HttpServlet {

	private String contextRoot;
	private static int BUFFER_SIZE = 1024;
	private String tempDir = null;
	private static final String CONTENT_TYPE = "application/zip";

	private static int i = 0;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
		doGet(request,response);
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		try{
			// will have '.zip' on end so browsers that dont respect the mime respect the extension still open the file correctly
			String directory = (String) request.getAttribute(HttpFileServer.FS_FILE);
			String currentDir = directory.substring(0, directory.length() - 4); // strip '.zip'
			String name = "download" + ++i;
			File zip = new File(tempDir, name);
			zip.deleteOnExit(); // incase of exceptions
			try {
				ZipCompressor zipper = new ZipCompressor(new File(currentDir), zip);
				zipper.compress(false);
			}
			catch (java.util.zip.ZipException e) {
				request.setAttribute("msg_title", "Error");
				request.setAttribute("msg_html",
									 "Could not create the zip on the server. The directory must contains files to create a zip");
				request.setAttribute("msg_jsp_part", "back.jsp");
				request.getRequestDispatcher("/message.jsp").forward(request, response);
				return;
			}

			// Stream the file
			response.setContentLength((int)zip.length());
			OutputStream out = response.getOutputStream();
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(zip));
			byte[] bytes = new byte[BUFFER_SIZE];
			int len = 0; // num of bytes read
			while ( (len = bis.read(bytes)) >= 0) {
				out.write(bytes, 0, len);
			}
			out.close();
			bis.close();
			zip.delete();
			log(request.getUserPrincipal().getName() + ":Zip file streamed:" + currentDir);
		}catch (Exception ex){
			log("",ex);
			throw new ServletException(ex);
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
