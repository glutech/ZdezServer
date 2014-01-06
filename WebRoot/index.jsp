<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    String rootPath = pageContext.getServletContext().getRealPath("/") + "attached/";
    session.setAttribute("rootPath", rootPath);
%>

<!DOCTYPE html>
<html lang="zh-cn">
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
<!-- 一些基于兼容性考虑和效果，一些调整 wu.kui2@gmail.com -->
<script type="text/javascript">
    $(function() {
        // 检测浏览器。IE6、IE7明确不支持，IE8-IE11、Chrome31、Firefox24明确支持，其他未测试
        // 鉴于无法准确检测使用内核，仅作提示、不作强制禁止使用
	    var version = navigator.userAgent.match(/MSIE\s*(\d+?.\d+|\d+?)/i);
	    version = version && version[1];
	    version && parseInt(version);
	    if(version == 6 || version == 7) {
	       alert("\n系统不支持当前浏览器！\n\n\n\n若您正在使用浏览器的兼容模式，请将其关闭。\n\n\n我们建议您，参考页尾信息更新浏览器！");
	    }
    });
</script>
<style type="text/css">
/* 修正左边栏随页宽变动 */
body { 
    background: #161616 url('./images/rep.png') 0px -60px repeat-x;
}
#container {
    padding-top: 200px;
}
</style>
<!-- 一些基于兼容性考虑和效果，一些调整  -->
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
                    <label>用户名: </label>
                    <input name="username" value="<%=request.getAttribute("uname")==null?"":request.getAttribute("uname")%>" />
                    <label>密码: </label>
                    <input name="password" type="password" value="<%=request.getAttribute("passwd")==null?"":request.getAttribute("passwd")%>" />
                </p>
                <br /> <br />
                <p class="main">
                    <label> 验证码: </label>
                    <input id="veryCode" name="veryCode" type="text" width="10%" />
                    <img id="imgObj" alt="验证码" src="VerifyCode" style="float: left; padding-left: 1em;"/>
                    <a href="#" onclick="changeImg()" style="float: left; height: 30px; line-height: 30px; padding-left: 1em; color: #ccc;">换一张</a>
                    <br />
                </p>
                <!--
                <p class="space" style="text-align: center;">
                    <span><input style="float:none; margin: auto 1em; margin-right: 0.5em;" type="checkbox" name="keep" />记住用户名</span>
                </p>
                -->
                <p class="space" style="text-align: center;">
                    <span><input style="float: none; margin: auto 1em;" value="登录" class="login" type="submit"> </span>
                </p>
            </form>
            <div style="font-size: 14px; font-style: italic; color: #CCC; text-align: center; margin-top: 2em;">
                <p>*&nbsp;系统支持&nbsp;<a href="http://windows.microsoft.com/zh-cn/internet-explorer/download-ie" style="color: #EEE;" target="_blank" title="前往微软官方网站升级 IE 浏览器">IE8或更新版本</a>、<a href="https://www.google.com/intl/zh-CN/chrome/browser" style="color: #EEE;" target="_blank" title="前往谷歌官方网站安装 Chrome 浏览器">谷歌</a>、<a href="http://www.firefox.com.cn/#desktop" style="color: #EEE;" target="_blank" title="前往火狐官方网站安装 Firefox 浏览器">火狐</a>&nbsp;等浏览器</p>
            </div>
        </div>
    </div>

</body>
</html>