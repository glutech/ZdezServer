<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<header id="header">
	<hgroup>
		<h1 class="site_title">
			<a href="admin.jsp">系统管理员</a>
		</h1>
		<h2 class="section_title">“找得着”校园通知系统</h2>
		<div class="btn_view_site">
			<a href="http://">查看网站</a>
		</div>
	</hgroup>
</header>
<!-- end of header bar -->

<section id="secondary_bar">
	<div class="user">
		<p>
			用户名：<%=session.getAttribute("uname")%></p>
		<a class="logout_user" href="Logout" title="Logout">退出</a>
	</div>