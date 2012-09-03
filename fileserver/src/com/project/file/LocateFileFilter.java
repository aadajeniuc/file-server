package com.project.file;

import com.project.util.FileUtils;
import com.project.util.JavaScriptUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

public class LocateFileFilter
	implements Filter {

	private String contextRoot;

	public LocateFileFilter() {
	}

	public void init(FilterConfig filterConfig) throws javax.servlet.ServletException {

		contextRoot = filterConfig.getServletContext().getInitParameter("fileRoot").replace('\\', '/');

		if(contextRoot.endsWith(System.getProperty("file.separator"))){
			contextRoot = contextRoot.substring(0, contextRoot.length() - 1);
			filterConfig.getServletContext().log("Remove training slashed in the HttpFileServers contextRoot parameter");
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws java.io.IOException,
		javax.servlet.ServletException {
		String fileUrl = getMeaningfullURL((HttpServletRequest) request);
		request.setAttribute(HttpFileServer.URL_FILE, fileUrl);
		request.setAttribute(HttpFileServer.FS_FILE, urlToPathSafe(fileUrl, contextRoot));
		chain.doFilter(request, response);
	}

	/**
	 * This method does any relative to absolute path conversions to stop a hacker
	 * entering ../ on the url and navigating the file system.
	 * This should do nothing if links are clicked from the JSPs
	 * @param fileUrl a url escaped relative file reference
	 * @return a non  url escaped file refence
	 * @throws java.io.IOException
	 */
	public static String urlToPathSafe(String fileUrl, String contextRoot) throws IOException {
		String in = JavaScriptUtils.unescape(fileUrl);
		String out = contextRoot + File.separatorChar + in;
		// this line converts whatever we get to a File if it is not in the context root we complain
		// this should deal with all .. attacks that return a valid Java File

		String absolutePath = FileUtils.toDirectPath(out);

		if (absolutePath.indexOf(contextRoot) != 0) {
			throw new IllegalArgumentException("Hack attempt?");
		}
		return absolutePath;
	}


	/**
	 * Translate a path to a string that can form part of a POST
	 * Parameter or URL path.
	 * @param fileName the absolute path of a file with trailing slash
	 * @param contextRoot the base/root directory of the file system being shown
	 * @return a string that can be appended to a url which the filter will be able to parse (a urlencoded  relative path)
	 */
	public static String pathToUrlSafe(String fileName, String contextRoot) {
		if (fileName.length() == contextRoot.length()) {
			return "";
		}
		// mangle only the bit of the root past the context, discard the rest
		return JavaScriptUtils.escape(fileName.substring(contextRoot.length() + 1).replace('\\', '/'));
	}

	/**
	 * this enables us to put meaningfull stuff on the end of the url but BEFORE the ?
	 * this means /fs/servlet/fs/blahblah\blah.java can be stripped to return the correct file
	 * and the browser will call the download 'blah.java'
	 * This method now converts file references to unix style references
	 */
	public static String getMeaningfullURL(HttpServletRequest request) {
		String in = request.getRequestURI();
		StringBuffer requestRoot = new StringBuffer(16);
		requestRoot.append(request.getContextPath());
		requestRoot.append(request.getServletPath());
		requestRoot.append('/');
		//System.out.println("requestRoot:"+requestRoot);  // there is a bug in tomcat for this!!! first time logon
		//System.out.println("getRequestURI:"+request.getRequestURI());
		if (in.length() > requestRoot.length()) {
			String fileEndOfUrl = request.getRequestURI().substring(requestRoot.length());

			return fileEndOfUrl.replace('\\', '/');
		}
		else {
			return "";
		}
	}

	/**
	 * Returns the parent directory of a file or directory.
	 * This uses string manipulation so it does not matter if the string is URL encoded or not.
	 * @param filePath relative file reference of the file as a string (using unix slashes / )
	 * @return relative file reference of the file's parent or / if the parent is above context root
	 */
	public static String parentRelPath(String filePath) {
		String parentPath;
		int lastSlash = filePath.lastIndexOf('/');
		if (lastSlash != -1) {
			parentPath = filePath.substring(0, lastSlash);
		}
		else {
			parentPath = "/";
		}
		return parentPath;
	}

	/**
	 * Returns the parent directory of a file or directory.
	 * This uses string manipulation so it does not matter if the string is URL encoded or not.
	 * @param filePath absolute file reference of the file as a string (using unix slashes / )
	 * @return relative file reference of the file's parent or the context root if the parent would be above context root
	 */
	public static String parentAbsPath(String filePath, String contextRoot) {
//		filePath = removeMultipleSlashes(filePath);
		String parentPath;
		int lastSlash = filePath.lastIndexOf('/');
		parentPath = filePath.substring(0, lastSlash);
		if (parentPath.length() < contextRoot.length()) {
			parentPath = contextRoot;
		}
		return parentPath;

	}

	public void destroy() {
		contextRoot = null;
	}


}

