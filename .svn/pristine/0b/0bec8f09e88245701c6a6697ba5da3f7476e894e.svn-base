<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="java.text.*"%>
<%!String keyword = "";%>
<%
	if (session.getAttribute("adminUserLoginSucessFlag") == null) {
%>
<jsp:forward page="index.jsp"></jsp:forward>
<%
	} else if (session.getAttribute("keyword") != null) {
		keyword = (String) session.getAttribute("keyword");
	}
%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="en">

<head>
<meta charset="utf-8" />
<title>首页</title>

<link rel="stylesheet" href="css/layout.css" type="text/css"
	media="screen" />
<link rel="stylesheet" href="css/bootstrap.css" type="text/css"
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
<!-- 分页 -->
<script type="text/javascript" language="javascript">
	$(document).ready(function() {

		///////选择跳转页面select中选中后台返回的值
		var objSelect = document.getElementById("select_type");
		var v = document.getElementById("hid1");
		for ( var i = 0; i < 11; i++) {
			if (objSelect.options[i].value == v.value) {
				objSelect.options[i].selected = true;
				break;
			}
		}
	});

	////////选择的页面跳转
	function oK() {
		var ctype = document.getElementById("select_type");
		var index = ctype.selectedIndex; // 选中索引
		var currentPage = ctype.options[index].value; // 选中值
		window.location.href = "Admin_ViewSchoolMsgQuery?currentPage="
				+ currentPage + "&keyword=" + keyword;
	}
</script>
</head>


<body>

	<%@ include file="headerBarAdmin.jsp"%>

	<div class="breadcrumbs_container">
		<article class="breadcrumbs">
			<a>信息管理</a>
			<div class="breadcrumb_divider"></div>
			<a class="current">已发信息</a>
		</article>
	</div>

	<%@ include file="sideBarAdmin.jsp"%>

	<section id="main" class="column">

		<article class="module width_full">
			<!-- module width_3_quarter -->
			<form class="quick_search" action="Admin_ViewSchoolMsgQuery"
				method="get">
				<input type="hidden" name="currentPage" value=1 /> <input size="5"
					type="text" value="查询信息"
					onfocus="if(!this._haschanged){this.value=''};this._haschanged=true;"
					name="keyword"> <input value="查询" class="login"
					type="submit" />
			</form>


			<header>
				<h3 class="tabs_involved">信息列表
			</header>
			<div class="tab_container">
				<div id="tab1" class="tab_content">
					<table class="tablesorter" cellspacing="0">
						<thead>
							<tr>

								<th>信息标题</th>
								<th>发送时间</th>
								<th>学校</th>
								<th>已收到/发送数</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<c:forEach var="msg" items="${schoolMsgList}">
									<tr>
										<td><c:out value="${msg.title}" />
										</td>
										<td><c:out value="${msg.date}"></c:out>
										</td>
										<td><c:out value="${msg.schoolName}"></c:out>
										</td>
										<td><c:out value="${msg.receivedNum}"></c:out>/<c:out
												value="${msg.receiverNum}"></c:out>
										</td>
										<td><a
											href="Admin_ViewSchoolMsgDetail?schoolMsgId=${msg.schoolMsgId}">
												<input type="image" src="images/icn_alert_info.png"
												title="查看"> </a>
										</td>
									</tr>
								</c:forEach>
							</tr>
						</tbody>
						<thead>
							<%
								int allpage = Integer.parseInt(request.getAttribute("allpage")
										.toString());
								if (allpage == 0) {
									allpage = 1;
								}
								int all = Integer.parseInt(request.getAttribute("all").toString());
								int currentPage = Integer.parseInt(request.getAttribute(
										"currentPage").toString());
								//查看通知详细信息后返回的时候用到
								session.setAttribute("currentPage", currentPage);
								//用于判断是否是查询，根据type返回到不同的jsp页面
								session.setAttribute("type", 2);
							%>
							<tr valign="middle">
								<td colspan="2" class="bg_tr">共 <font color="red"><%=all%></font>&nbsp;
									&nbsp;条信息&nbsp; &nbsp;当前第&nbsp;<%=currentPage%>/<%=allpage%>&nbsp;页</td>
								<td colspan="3" class="bg_tr" align="right"><a
									href="Admin_ViewSchoolMsgQuery?currentPage=1&keyword=<%=keyword%>">首页&nbsp;</a>
									<%
										if (currentPage > 1) {
									%> <a
									href="Admin_ViewSchoolMsgQuery?currentPage=<%=currentPage - 1%>&keyword=<%=keyword%>">上一页&nbsp;</a>
									<%
										} else {
									%>上一页&nbsp;<%
										}
									%> <%
 	if (currentPage < allpage) {
 %> <a
									href="Admin_ViewSchoolMsgQuery?currentPage=<%=currentPage + 1%>&keyword=<%=keyword%>">下一页&nbsp;</a>
									<%
										} else {
									%>下一页&nbsp;<%
										}
									%> <a
									href="Admin_ViewSchoolMsgQuery?currentPage=<%=allpage%>&keyword=<%=keyword%>">尾页&nbsp;</a>
									<input type="hidden" id="hid1" name="hid1"
									value="<%=currentPage%>" /> <!-- 类型id --> 转到 <select
									id="select_type" name="select_type" onchange="oK()">
										<%
											for (int z = 1; z <= allpage; z++) {
										%>
										<option value=<%=z%>><%=z%></option>
										<%
											}
										%>
								</select> 页</td>
							</tr>
						</thead>
					</table>
				</div>
				<!-- end of #tab1 -->

			</div>
			<!-- end of .tab_container -->
		</article>
		<!-- end of product manager article -->

	</section>

</body>

</html>
