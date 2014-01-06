<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="cn.com.zdez.po.SchoolAdmin"%>
<%!
	String filePath = "";
%>
<%
	if (session.getAttribute("schoolUserLoginSucessFlag") == null) {
%>
<jsp:forward page="index.jsp"></jsp:forward>
<%
	} else {
		filePath = "/zdezServer/attached/html/anno";
	}
%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

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
<!-- <script src="js/jquery-1.5.2.min.js" type="text/javascript"></script> -->
<script src="js/jquery-1.8.0.min.js" type="text/javascript"></script>
<script src="js/hideshow.js" type="text/javascript"></script>
<script src="js/jquery.tablesorter.min.js" type="text/javascript"></script>
<script type="text/javascript" src="js/jquery.equalHeight.js"></script>
<script type="text/javascript" src="js/layer.min.js"></script>
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
<style type="text/css">
section#main.column>article.module.width_quarter {
	float: none;
	margin: 5% auto;
	width: 80%;
}
</style>
<style>
#box {
	width: 600px;
	margin: 0 auto;
	background: #fff;
	border: 1px solid #ccc;
	padding: 20px;
	-webkit-border-radius: 10px;
	-moz-border-radius: 10px;
	display: none;
}

#box p {
	margin-bottom: 20px;
}
</style>

<script type="text/javascript">
	$(function() {
		$('.announcementList').on('click', function() {
			var $this = $(this);
			$.layer({
				type   : 2,
				title  : $this.attr('title'),
				iframe : { src : $this.attr('href') },
				area   : ['640px', '480px'],
				offset : ['100px', '']
			});
			return false;
		});
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
<!-- 	<h4 class="alert_info">欢迎登录校园通信息发布系统.</h4> -->
	<!-- 	<div align="center"> --> 
	<article class="module width_quarter">
	<header>
	<h3>博客公告</h3>
	</header>
	<div class="message_list">
		<div class="module_content">
			<c:forEach var="anno" items="${annoList}">
				<div class="message" style="display: block;">
					<p style="margin-top: 1.5em;">
						<a style="font-size: 1.2em; color: #333;" href="<%=filePath %><c:out value="${anno.id}"></c:out>.html"
							title="<c:out
								value="${anno.title}"></c:out>"
							class="announcementList"
							id="viewAnnoDetails_<c:out value="${anno.id }"></c:out>"><c:out
								value="${anno.title}"></c:out></a>
					</p>
					<p align="right" style="margin-bottom: 0.5em;">
						<c:out value="${anno.sign}"></c:out>&nbsp;&nbsp;<c:out
								value="${anno.date}"></c:out>
					</p>
				</div>
			</c:forEach>
		</div>
	</div>
	</article> <!-- end of messages article --> <!-- 	</div> -->

	<div class="clear"></div>

	<div class="spacer"></div>
	</section>
	<div id="box">
		<h3>Mission</h3>
		<p>Mozilla's mission is to promote openness, innovation, and
			opportunity on the web. We do this by creating great software, like
			the Firefox browser, and building movements, like Drumbeat, that give
			people tools to take control of their online lives.</p>
		<p>As a non-profit organization, we define success in terms of
			building communities and enriching people’s lives instead of
			benefiting our shareholders (guess what: we don’t even have
			shareholders). We believe in the power and potential of the Internet
			and want to see it thrive for everyone, everywhere.</p>
		<p>Building a better Internet is an ambitious goal, but we believe
			that it is possible when people who share our passion get involved.
			Coders, artists, writers, testers, surfers, students,
			grandparents—anyone who uses and cares about the web can help make it
			even better. Find out how you can help.</p>
	</div>

</body>

</html>


