<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
</section>
<!-- end of secondary bar -->
<aside id="sidebar" class="column">
	<h3>信息管理</h3>
	<ul class="toggle">
		<li class="icn_categories"><a
			href="Admin_ViewSchoolMsg?currentPage=1">学校信息</a>
		</li>
		<%--<li class="icn_new_article"><a href="Admin_UnitInfor.do">企业信息</a>
		</li>--%>
	</ul>

	<h3>用户管理</h3>
	<ul class="toggle">
		<li class="icn_profile"><a href="Admin_NewStudentHref">新建学生用户</a>
		</li>
		<li class="icn_profile"><a
			href="Admin_StudentManage?currentPage=1">查看学生用户</a>
		</li>
		<li class="icn_profile"><a href="Admin_NewSchoolAdminHref">新建学校用户</a>
		</li>
		<li class="icn_profile"><a
			href="Admin_SchoolAdminManage?currentPage=1">查看学校用户</a>
		</li>
		<%--<li class="icn_profile"><a href="Admin_Unit.do">企业用户</a></li>--%>
	</ul>
	<h3>新闻资讯管理</h3>
	<ul class="toggle">
		<li class="icn_new_article"><a href="Admin_NewNewsHref">新建消息</a>
		</li>
		<li class="icn_categories"><a href="Admin_ViewNews?currentPage=1">已发信息</a>
		</li>
	</ul>
	<h3>“找得着”信息管理</h3>
	<ul class="toggle">
		<li class="icn_new_article"><a href="Admin_NewZdezMsgHref">新建消息</a>
		</li>
		<li class="icn_categories"><a
			href="Admin_ViewZdezMsg?currentPage=1">已发信息</a>
		</li>
	</ul>
	<h3>用户统计</h3>
	<ul class="toggle">
		<li class="icn_view_users"><a href="Admin_StatisticsServlet">查看在线人数</a>
		<li class="icn_view_users"><a href="Admin_StatisticsDetails">查看在线人数详情</a>
		</li>
	</ul>

	<footer>
		<hr />
		<p>
			<strong>Copyright &copy; 2013 校园通信息系统</strong>
		</p>

	</footer>
</aside>
<!-- end of sidebar -->