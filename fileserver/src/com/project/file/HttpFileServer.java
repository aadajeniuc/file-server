package com.project.file;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class HttpFileServer extends HttpServlet {

    private String contextRoot;
    public static final String URL_FILE = "file.path"; // key used for the request attribute with the file URL in it (url encoded)
    public static final String FS_FILE = "file.fs.path"; // key used for the request attribute with the file file system path in it

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Translate a posted parameter into a path. Relative paths are used
     * so that it it is not possible to browse outside the root.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String fileUrl = (String) request.getAttribute(HttpFileServer.URL_FILE);
            if (fileUrl == null || fileUrl.equals("")) {
                showDirectory(contextRoot, request, response);
                return;
            }
            String realPath = (String) request.getAttribute(HttpFileServer.FS_FILE);
            File fileSystemFile = new File(realPath);
            if (!fileSystemFile.exists()) {
                throw new IllegalArgumentException("File does not exist:" + fileSystemFile.getAbsolutePath());
            } else if (!fileSystemFile.isDirectory()) {
                streamFile(fileSystemFile, request, response);
                return;
            } else { // this is a directory move call
                showDirectory(realPath, request, response);
                return;
            }
        } catch (Exception ex) {
            log("", ex);
            throw new ServletException(ex);
        }

    }

    public void showDirectory(String realPath, HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        ArrayList files = new ArrayList();
        ArrayList directories = new ArrayList();
        File dir = new File(realPath);
        File[] list = dir.listFiles();
        WebFile webFile = null;

        ServletContext ctx = this.getServletConfig().getServletContext();
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        String mime = null;
        for (int i = 0; i < list.length; i++) {
            mime = ctx.getMimeType(list[i].getName().toLowerCase());
            if (mime == null) {
                mime = "unknown";
            }
            webFile = new WebFile(list[i].getName(),
                    LocateFileFilter.pathToUrlSafe(list[i].getAbsolutePath(), contextRoot),
                    df.format(new Date(list[i].lastModified())),
                    mime,
                    list[i].length(),
                    list[i]);

            if (list[i].isDirectory()) {
                directories.add(webFile);
            } else {
                files.add(webFile);
            }
        }

        // sort alphabetically
        Collections.sort(directories);
        Collections.sort(files);
        request.setAttribute("directories", directories);
        request.setAttribute("files", files);
        request.getRequestDispatcher("/directories.jsp").forward(request, response);
    }

    public void streamFile(File fileSystemFile, HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        // do some IO (this is modelled on StaticInteceptor by Tomcat crew)
        String absPath = fileSystemFile.toString();
        ServletContext ctx = this.getServletConfig().getServletContext();
        String mimeType = ctx.getMimeType(absPath);

        if (mimeType == null) {
            mimeType = "text/plain";
        }

        response.setContentType(mimeType);
        response.setContentLength((int) fileSystemFile.length());

        FileInputStream in = null;
        try {
            in = new FileInputStream(fileSystemFile);

            OutputStream out = response.getOutputStream();
            byte[] buf = new byte[1024];
            int read = 0;

            while ((read = in.read(buf)) != -1) {
                out.write(buf, 0, read);
            }

        } finally { // close the stream what ever but let exceptions be thrown
            if (in != null) {
                in.close();
            }
        }

    }

    public void init(ServletConfig config) throws javax.servlet.ServletException {
        super.init(config);
        if (contextRoot == null) {
            contextRoot = config.getServletContext().getInitParameter("fileRoot").replace('\\', '/');
        }
    }
}
