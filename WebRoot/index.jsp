<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String rootPath = pageContext.getServletContext().getRealPath("/") + "attached/";
	session.setAttribute("rootPath", rootPath);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<title>“找得着”-云南博客科技有限公司</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<link rel="stylesheet" type="text/css" href="css/login.css">
<script type="text/javascript" src="js/boxOver.js"></script>
<script type="text/javascript" src="js/jquery-1.7.1.js"></script>
<script type="text/javascript" src="js/verifyCode.js"></script>
</head>

<body>
	<div id="container">
		<h1>“找得着”用户登录入口</h1>
		<div id="box">
			<p align="center"><%=request.getAttribute("message") == null ? "" : request
					.getAttribute("message")%></p>
			<br />
			<form action="LoginCheck" name="login" method="post">
				<p class="main" align="center">
					<label>用户名: </label> <input name="username" value="<%=request.getAttribute("uname")==null?"":request.getAttribute("uname")%>"> <label>密码:
					</label> <input name="password" type="password" value="<%=request.getAttribute("passwd")==null?"":request.getAttribute("passwd")%>">
				</p>
				<br /> <br />
				<p class="main">
					<label> 验证码: </label> <input id="veryCode" name="veryCode"
						type="text" width="10%"/> &nbsp;&nbsp;&nbsp;<img id="imgObj" alt="验证码" src="VerifyCode" /> <a
						href="#" onclick="changeImg()">换一张</a><br />
				</p>
				<p class="space">
					<span><input type="checkbox" name="keep">记住用户名</span> <span><input
						value="登录" class="login" type="submit"> </span>
				</p>
			</form>
		</div>
	</div>

</body>
</html>