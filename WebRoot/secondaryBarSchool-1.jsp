<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<section id="secondary_bar">
    <div class="user">
        <p>用户名：<%=session.getAttribute("uname")%></p>
        <a class="logout_user" href="Logout" title="退出登录">退出</a>
    </div>