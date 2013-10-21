<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	if (session.getAttribute("schoolUserLoginSucessFlag") == null) {
%>
<jsp:forward page="index.jsp"></jsp:forward>
<%
	}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta charset="utf-8" />
<title>首页</title>

<link rel="stylesheet" href="css/layout.css" type="text/css"
	media="screen" />
<!--[if lt IE 9]>
	<link rel="stylesheet" href="css/ie.css" type="text/css" media="screen" />
	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->
<script src="js/jquery-1.5.2.min.js" type="text/javascript"></script>
<script src="js/hideshow.js" type="text/javascript"></script>
</head>
<body>

	<%@ include file="headerBarSchool.jsp"%>

	<div class="breadcrumbs_container">
		<article class="breadcrumbs"> <a>信息管理</a>
		<div class="breadcrumb_divider"></div>
		<a class="current">发送成功</a> </article>
	</div>

	<%@ include file="sideBarSchool.jsp"%>

	<section id="main" class="column">

	<h4 class="alert_info"><%=request.getAttribute("errorMsg")==null?"信息已经发送成功":request.getAttribute("errorMsg") %></h4>
	<hr>
	<fieldset
		style="width: 10%; text-align:center; margin-left: 40%;">
		<a href="School_NewMsgHref"><input align="middle"
			type="submit" name="goback" value="返回" /> </a>
	</fieldset>

	<div class="clear"></div>

	<div class="spacer"></div>
	</section>


</body>

</html>


