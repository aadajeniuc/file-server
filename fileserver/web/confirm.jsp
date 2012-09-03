<%@ page import="com.project.file.*" %>
<%@include file="include.jsp"%><%

String fileUrl = (String)request.getAttribute(HttpFileServer.URL_FILE);
String parentUrl = LocateFileFilter.parentRelPath(fileUrl);

%>
<div>
	<form style="display:inline" name="this_form" action="<%=root%>servlet/delete/<%=fileUrl%>" method="get">
		<input type="hidden" name="confirm" value="true"/>
		<input type="submit" name="Submit" value="OK"/>
	</form>
	<form style="display:inline" name="this_form" action="<%=root%>servlet/fs/<%=parentUrl%>" method="get">
		<input type="submit" name="Submit" value="Cancel"/>
	</form><br/><br/>
</div>