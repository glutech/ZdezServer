<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

    <!-- 修复 hide/show 在IE下未正确对齐 wu.kui2@gmail.com -->
    <style>
        aside#sidebar > h3 {
            position: relative;
        }
        aside#sidebar > h3 > a {
            position: absolute;
            right: 0;
            top: 0;
        }
        aside#sidebar > p {
            font-weight: bold;
            font-size: 14px;
        }
        aside#sidebar > p > a {
            color: #1c94c4;
        }
    </style>
    <!-- 修复 hide/show 在IE下未正确对齐 -->
    <aside id="sidebar" class="column">
        <h3>信息管理</h3>
        <ul class="toggle">
            <li class="icn_categories"><a href="School_SendedMsg?currentPage=1">已发信息</a>
            </li>
            <li class="icn_new_article"><a href="School_NewMsgHref">新建信息</a>
            </li>
        </ul>
        <h3>账户管理</h3>
        <ul class="toggle">
            <%--<li class="icn_categories"><a href="School_AdminInfoModify">个人信息修改</a>
            </li>
            --%>
            <li class="icn_new_article"><a href="school_AdminPasswdModify.jsp">密码修改</a>
            </li>
        </ul>
        <hr />
        <br /> <br /> <br /> <br />
        <p>
            <a href="http://www.zdez.com.cn/download.html" title="下载找得着手机客户端" target="_blank">下载找得着手机客户端</a>
        </p>
        <br /> <br />
        <p>为了更好地使用本系统<br />请使用下列浏览器进行访问<br />
            &nbsp;&nbsp;&nbsp;&nbsp;<a href="http://windows.microsoft.com/zh-cn/internet-explorer/download-ie" style="font-weight: normal;" target="_blank" title="前往微软官方网站升级 IE 浏览器">IE8 或更新版本</a><br/>
            &nbsp;&nbsp;&nbsp;&nbsp;<a href="https://www.google.com/intl/zh-CN/chrome/browser" style="font-weight: normal;" target="_blank" title="前往谷歌官方网站安装 Chrome 浏览器">谷歌浏览器</a><br />
            &nbsp;&nbsp;&nbsp;&nbsp;<a href="http://www.firefox.com.cn/#desktop" style="font-weight: normal;" target="_blank" title="前往火狐官方网站安装 Firefox 浏览器">火狐浏览器</a><br />
        </p>
        <br /> <br /> <br /> <br />
        <footer>
            <hr />
            <p><strong><a href="http://www.zdez.com.cn" target="_blank" title="博客科技">博客科技（昆明）有限公司</a></strong></p>
            <p>&copy;&nbsp;2006-2014&nbsp;ZDEZ.COM.CN</p>
        </footer>
    </aside>
    <!-- end of sidebar -->