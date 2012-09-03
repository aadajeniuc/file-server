<%

request.getSession(true).invalidate();

request.getRequestDispatcher("/servlet/fs").forward(request,response);

%>