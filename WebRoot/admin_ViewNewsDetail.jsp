<%@page import="cn.com.zdez.vo.NewsVo"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%!NewsVo newVo = new NewsVo();
	int currentPage = 0;
	String keyword = "";
	String temp = "";
	String filePath = "";
	int type = 0;%>
<%
	if (session.getAttribute("adminUserLoginSucessFlag") == null) {
%>
<jsp:forward page="index.jsp"></jsp:forward>
<%
	} else {
		newVo = (NewsVo) request.getAttribute("news");
		currentPage = (Integer) session.getAttribute("currentPage");
		type = (Integer) session.getAttribute("type");
		filePath = "/zdezServer/attached/html/news"
				+ Integer.toString(newVo.getId()) + ".html";
	}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html lang="en">

<head>
<meta charset="utf-8" />
<title>找得着</title>

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
<link rel="stylesheet" type="text/css" href="css/layer.css" />
<SCRIPT type="text/javascript" src="js/jquery-1.3.2.js"></SCRIPT>
<script type="text/javascript">
	$(function() {
		$('.column').equalHeight();
	});
</script>
</head>
<body>

	<%@ include file="headerBarAdmin.jsp"%>

	<div class="breadcrumbs_container">
		<article class="breadcrumbs"> <a>信息管理</a>
		<div class="breadcrumb_divider"></div>
		<a class="current">信息查看</a> </article>
	</div>

	<%@ include file="sideBarAdmin.jsp"%>

	<section id="main" class="column"> <article
		class="module width_full"> <header>
	<h3>信息详情</h3>
	</header>
	<div class="tab_container">
		<div id="tab1" class="tab_content">
			<table class="tablesorter" cellspacing="0">
				<tr>
					<td style="width:40pt">标题</td>
					<td><%=newVo.getTitle()%></td>
				</tr>
				<tr>
					<td>内容</td>
					<td><iframe src="<%=filePath%>" frameborder="0" width="800" height="500"></iframe></td>
				</tr>
				<tr>
					<td>日期</td>
					<td><%=newVo.getDate()%></td>
				</tr>
				<%
					temp = "";
					for (int i = 0, count = newVo.getDestSchools().size(); i < count; i++) {
						temp += newVo.getDestSchools().get(i) + ", ";
					}
				%>

				<tr>
					<td>目的学校</td>
					<td><%=temp%></td>
				</tr>
			</table>
		</div>
	</div>
	<footer>
	<div class="submit_link">
		<%
			if (type == 1) {
		%>
		<a href="Admin_ViewNews?currentPage=<%=currentPage%>"> <input
			type="submit" value="返回"> </a>
		<%
			} else {
				String keyword = (String) session.getAttribute("keyword");
		%>

		<a
			href="Admin_ViewNewsQuery?currentPage=<%=currentPage%>&keyword=<%=keyword%>">
			<input type="submit" value="返回"> </a>
		<%
			}
		%>
	</div>
	</footer>
	</article> <!-- end of addproduct article -->

	<div class="clear"></div>

	<div class="spacer"></div>
	</section>
</body>

</html>
