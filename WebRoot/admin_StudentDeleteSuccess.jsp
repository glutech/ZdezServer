<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%!int currentPage = 0;
	int type = 0;
	String keyword = "";%>
<%
	if (session.getAttribute("adminUserLoginSucessFlag") == null) {
%>
<jsp:forward page="index.jsp"></jsp:forward>
<%
	} else {
		currentPage = (Integer) session.getAttribute("currentPage");
		type = (Integer) session.getAttribute("type");
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
<script src="js/jquery-1.5.2.min.js" type="text/javascript">
	
</script>
<script src="js/hideshow.js" type="text/javascript">
	
</script>
<script src="js/jquery.tablesorter.min.js" type="text/javascript">
	
</script>
<script type="text/javascript" src="js/jquery.equalHeight.js">
	
</script>
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

	<%@ include file="headerBarAdmin.jsp"%>

	<div class="breadcrumbs_container">
		<article class="breadcrumbs"> <a>学生管理</a>
		<div class="breadcrumb_divider"></div>
		<a class="current">删除成功</a> </article>
	</div>

	<%@ include file="sideBarAdmin.jsp"%>

	<section id="main" class="column">

	<h4 class="alert_info">删除成功！</h4>
	<hr>
	<fieldset
		style="width: 96%; text-align:center; margin-left: 5%; margin-right: 5%;">
		<%
			if (type == 1) {
		%>

		<a href="Admin_StudentManage?currentPage=<%=currentPage%>"><input
			align="middle" type="button" name="goback" value="返回" /> </a>
		<%
			} else {
				String keyword = (String) session.getAttribute("keyword");
		%>
		<a
			href="Admin_StudentManageQuery?currentPage=<%=currentPage%>&keyword=<%=keyword%>"><input
			align="middle" type="button" name="goback" value="返回" /> </a>
		<%
			}
		%>
	</fieldset>

	<div class="clear"></div>

	<div class="spacer"></div>
	</section>


</body>

</html>