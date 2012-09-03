<%@page import = "com.project.file.*"%>
<%@include file="include.jsp"%><%

String fileUrl = (String)request.getAttribute(HttpFileServer.URL_FILE);

String parentUrl = LocateFileFilter.parentRelPath(fileUrl);

%>
<div>
        <form name="this form">
          <input type="button" name="Submit" value="Back" onclick="document.location.href='<%=root%>servlet/fs/<%=parentUrl%>'"/>
        </form>
</div>