<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@include file="include.jsp" %>
<%@page import="com.project.file.HttpFileServer" %>
<%@page import="com.project.file.LocateFileFilter" %>
<%@page import="com.project.file.WebFile" %>
<%@page import="com.project.util.JavaScriptUtils" %>
<%@ page import="java.util.ArrayList" %>
<%
    ArrayList files = (ArrayList) request.getAttribute("files");
    ArrayList directories = (ArrayList) request.getAttribute("directories");
    String fileUrl = (String) request.getAttribute(HttpFileServer.URL_FILE);
    String currentDir = JavaScriptUtils.unescape(fileUrl);
    String upOneDir = LocateFileFilter.parentRelPath(currentDir);
    WebFile file = null;
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
    </head>

    <body>

        <%@include file="htmlhead.jsp" %>

        <div id="fs-directories-desc">
            Directory listing for:
            <span id="fs-directory-name"><%=fileUrl%></span>
        </div>
        <div id="fs-directories">
            <table>
                <tr>
                    <th></th>
                    <th>Name</th>
                    <th>File Size</th>
                    <th>Last Modified</th>
                    <th>Mime</th>
                </tr>
                <tr>
                    <td>
                        <a href="<%=root%>servlet/fs/<%=upOneDir%>">[Up one directory]..
                        </a>
                    </td>
                    <td/>
                    <td/>
                    <td/>
                    <td/>
                </tr>
                <% for (int i = 0; i < directories.size(); i++) {
                    file = (WebFile) directories.get(i);
                %>
                <tr>
                    <td>

                        <a href="<%=root%>servlet/delete/<%=file.getUrl()%>">[Delete directory]</a>

                        <a href="<%=root%>servlet/download/<%=file.getUrl()%>.zip">[Download Zip of directory]</a>
                        [Directory]

                    </td>
                    <td>
                        <a href="<%=root%>servlet/fs/<%=file.getUrl()%>">
                                <%=file.getName()%></a>
                    </td>

                    <td/><%-- file size --%>
                    <td><%=file.getLastModified()%></td>
                    <td/><%-- mime --%>
                </tr>
                <%}%>

                <tr id="fsc-spc">
                    <td colspan="5"/>
                </tr>

                <% for (int i = 0; i < files.size(); i++) {
                    file = (WebFile) files.get(i);
                %>
                <tr>
                    <td>

                        <a href="<%=root%>servlet/delete/<%=file.getUrl()%>">[Delete file]</a>
                    </td>
                    <td>
                        <a href="<%=root%>servlet/fs/<%=file.getUrl()%>">
                                <%=file.getName()%></a>
                    </td>
                    <td><%=file.getFileSize()%></td>
                    <td><%=file.getLastModified()%></td>
                    <td><%=file.getMime()%></td>
                </tr>
                <%}%>
            </table>
        </div>

        <%@include file="directoriesfoot.jsp" %>

    </body>
</html>
