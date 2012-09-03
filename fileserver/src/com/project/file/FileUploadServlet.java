package com.project.file;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class FileUploadServlet extends HttpServlet {

    private String contextRoot;
    private String tempDir;
    private static int MV_BUFFER_SIZE = 4 * 1024; // 4Kb

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        doGet(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        try {
            String message = "";
            MultipartRequest mpr = new MultipartRequest(tempDir);

            File tempData = mpr.getFile(request);
            System.out.println("uploaded temp file:" + tempData.getAbsolutePath() + "file exists:" + tempData.exists());
            String directory = (String) request.getAttribute(HttpFileServer.FS_FILE);
            File file = new File(directory, tempData.getName());

            fileSystemMove(tempData, file);
            super.log(request.getUserPrincipal().getName() + ":File created:" + file.getAbsolutePath());

            request.getRequestDispatcher("/servlet/fs/").forward(request, response);
        } catch (Exception ex) {
            log("", ex);
            request.setAttribute("msg_title", "Error?");
            request.setAttribute("msg_html", "Error uploading the file, was it a valid file?");
            request.setAttribute("msg_jsp_part", "back.jsp");
            request.getRequestDispatcher("/message.jsp").forward(request, response);
        }
    }

    public void init(ServletConfig config) throws javax.servlet.ServletException {
        super.init(config);
        if (contextRoot == null) {
            contextRoot = config.getServletContext().getInitParameter("fileRoot");
            tempDir = config.getServletContext().getInitParameter("tempDir");
        }
    }

    public static void fileSystemMove(File from, File to) throws IOException {
        if (from.renameTo(to)) {
            System.out.println("File renamed to:" + to.getAbsolutePath());
            return;
        } else if (from.isFile()) {
            FileInputStream fis = new FileInputStream(from);
            FileOutputStream fos = new FileOutputStream(to);
            int read = -1;
            byte[] buffer = new byte[MV_BUFFER_SIZE];
            while ((read = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, read);
            }
            fos.close();
            fis.close();
        }
    }
}
