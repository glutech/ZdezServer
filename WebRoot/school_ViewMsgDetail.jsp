<%@page import="cn.com.zdez.vo.SchoolMsgVo"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%!SchoolMsgVo sMsgVo = new SchoolMsgVo();
	int currentPage = 0;
	String keyword = "";
	String temp = "";
	String filePath = "";
	int type = 0;%>
<%
	if (session.getAttribute("schoolUserLoginSucessFlag") == null) {
%>
<jsp:forward page="index.jsp"></jsp:forward>
<%
	} else {
		sMsgVo = (SchoolMsgVo) request.getAttribute("schoolMsg");
		currentPage = (Integer) session.getAttribute("currentPage");
		type = (Integer) session.getAttribute("type");
		filePath = "/zdezServer/attached/html/schoolMsg"
				+ Integer.toString(sMsgVo.getSchoolMsgId()) + ".html";
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

	<%@ include file="headerBarSchool.jsp"%>

	<div class="breadcrumbs_container">
		<article class="breadcrumbs"> <a>信息管理</a>
		<div class="breadcrumb_divider"></div>
		<a class="current">信息查看</a> </article>
	</div>

	<%@ include file="sideBarSchool.jsp"%>

	<section id="main" class="column"> <article
		class="module width_full"> <header>
	<h3>信息详情</h3>
	</header>
	<div class="tab_container">
		<div id="tab1" class="tab_content">
			<table class="tablesorter" cellspacing="0">
				<tr>
					<td style="width:40pt">标题</td>
					<td><%=sMsgVo.getTitle()%></td>
				</tr>
				<tr>
					<td>内容</td>
					<td><iframe src="<%=filePath%>" frameborder="0" width="800" height="500"></iframe>
					</td>
				</tr>
				<tr>
					<td>日期</td>
					<td><%=sMsgVo.getDate()%></td>
				</tr>
				<%
					temp = "";
					for (int i = 0, count = sMsgVo.getDestGrade().size(); i < count; i++) {
						temp += sMsgVo.getDestGrade().get(i) + ", ";
					}
				%>

				<tr>
					<td>目的年级</td>
					<td><%=temp%></td>
				</tr>
				<%
					temp = "";
					for (int i = 0, count = sMsgVo.getDestDepartment().size(); i < count; i++) {
						temp += sMsgVo.getDestDepartment().get(i) + ", ";
					}
				%>
				<tr>
					<td>目的学院</td>
					<td><%=temp%></td>
				</tr>
				<%
					temp = "";
					for (int i = 0, count = sMsgVo.getDestMajor().size(); i < count; i++) {
						temp += sMsgVo.getDestMajor().get(i) + ", ";
					}
				%>
				<tr>
					<td>目的专业</td>
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
		<a href="School_SendedMsg?currentPage=<%=currentPage%>"> <input
			type="submit" value="返回"> </a>
		<%
			} else {
				String keyword = (String) session.getAttribute("keyword");
		%>

		<a
			href="School_SendedMsgQuery?currentPage=<%=currentPage%>&keyword=<%=keyword%>">
			<input type="submit" value="返回"> </a>
		<%
			}
		%>
	</div>
	</footer> </article> <!-- end of addproduct article -->

	<div class="clear"></div>

	<div class="spacer"></div>
	</section>
</body>

</html>
