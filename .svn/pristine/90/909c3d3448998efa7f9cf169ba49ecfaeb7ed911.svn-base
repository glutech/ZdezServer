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
<script src="js/jquery.tablesorter.min.js" type="text/javascript"></script>
<script type="text/javascript" src="js/jquery.equalHeight.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$(".tablesorter").tablesorter();
	});
	$(document).ready(function() {

		//When page loads...
		$(".tab_content").hide(); //Hide all content
		$("ul.tabs li:first").addClass("active").show(); //Activate first tab
		$(".tab_content:first").show(); //Show first tab content

		//On Click Event
		$("ul.tabs li").click(function() {

			$("ul.tabs li").removeClass("active"); //Remove any "active" class
			$(this).addClass("active"); //Add "active" class to selected tab
			$(".tab_content").hide(); //Hide all tab content

			var activeTab = $(this).find("a").attr("href"); //Find the href attribute value to identify the active tab + content
			$(activeTab).fadeIn(); //Fade in the active ID content
			return false;
		});

	});
</script>
<script type="text/javascript">
	$(function() {
		$('.column').equalHeight();
	});
</script>

</head>

<body>

	<%@ include file="headerBarSchool.jsp"%>

	<div class="breadcrumbs_container">
		<article class="breadcrumbs"> <a>学校信息管理员</a>
		<div class="breadcrumb_divider"></div>
		<a class="current">密码修改</a></article>
	</div>

	<%@ include file="sideBarSchool.jsp"%>

	<section id="main" class="column">

	<h4 class="alert_info">密码修改</h4>
	<br>
	<br>
	<br>
	<br>
	<br>
	<div align="center">
		<form action="School_AdminPSWModify" method="post">
			<table>
				<tr>
					<th></th>
					<th><%=request.getAttribute("message") == null ? "" : request
					.getAttribute("message")%></th>
				</tr>
				<tr>
					<th>原&nbsp;密&nbsp;码：</th>
					<th><input type="password" name="password"></th>
				</tr>
				<tr>
					<th>新&nbsp;密&nbsp;码：</th>
					<th><input type="password" name="newPassword"></th>
				</tr>
				<tr>
					<th>密码确认：</th>
					<th><input type="password" name="confirmNewPassword">
					</th>
				</tr>
				<tr>
					<th></th>
					<th><input type="submit" value="提交"></th>
				</tr>
			</table>
		</form>
	</div>
	<div class="clear"></div>

	<div class="spacer"></div>
	</section>


</body>

</html>


