<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"><%@include file="include.jsp"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
</head>

<body>

<%@include file="htmlhead.jsp"%>

<div id="fs-login">
	<div>Login</div>

	<form action="j_security_check" method="post" id="fs-login-form">
		<table>
			<tr>
				<th>
				username
				</th>
				<td>
					<input type="text" name="j_username" id="j_username"/>
				</td>
			</tr>
			<tr>
				<th>
				password
				</th>
				<td>
					<input type="password" name="j_password" id="j_password"/>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>
					<input type="submit" value="Login" name="Submit" id="submit"/>
				</td>
			</tr>
		</table>
	</form>
</div>

</body>
</html>
