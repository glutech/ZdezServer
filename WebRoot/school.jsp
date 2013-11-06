<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="cn.com.zdez.po.SchoolAdmin"%>
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
		<article class="breadcrumbs"> <a>信息管理</a>
		<div class="breadcrumb_divider"></div>
		<a class="current">欢迎</a> </article>
	</div>

	<%@ include file="sideBarSchool.jsp"%>

	<section id="main" class="column">

<!-- 	<h4 class="alert_warning">系统将于xxxx年xx月xx日晚xx点-xx点更新，请各位老师避免在该时段使用本系统，给您带来的不便，敬请谅解！</h4> -->
	<h4 class="alert_info">欢迎登录校园通信息发布系统.</h4>

	<div class="clear"></div>

	<div class="spacer"></div>
	</section>

</body>

</html>


