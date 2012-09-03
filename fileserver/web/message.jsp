<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@include file="include.jsp" %>
<html>
    <head>
        <title>do not bookmark this page</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
    </head>

    <body bgcolor="#FFFFFF" text="#000000">

        <%@include file="htmlhead.jsp" %>
        <%
            String msg_title = (String) request.getAttribute("msg_title");
            if (msg_title == null) {
                msg_title = "Unknown error";
            }
            String msg_html = (String) request.getAttribute("msg_html");
            if (msg_html == null) {
                msg_html = "An unexpected error has occurred. This may be caused by other users altering files you are trying to access.";
            }
            String jspName = (String) request.getAttribute("msg_jsp_part");

        %>

        <div>
            <div><%=msg_title%></div>
            <span><%=msg_html%></span>
            <%
                if (jspName != null) {
            %>
            <jsp:include page="<%=jspName%>" flush="true"/>
            <%
                }
            %>
        </div>
    </body>
</html>
