<%@page import="cn.com.zdez.po.Major"%>
<%@page import="cn.com.zdez.po.Department"%>
<%@page import="cn.com.zdez.po.School"%>
<%@page import="cn.com.zdez.po.SchoolAdmin"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%!SchoolAdmin sAdmin = new SchoolAdmin();
List<School> schoolList = new ArrayList<School>();
List<Department> departmentList = new ArrayList<Department>();
List<Major> majorList = new ArrayList<Major>();
int schoolId = 0;
int departmentId = 0;
int majorId = 0;
%>
<%
	if (session.getAttribute("adminUserLoginSucessFlag") == null) {
%>
<jsp:forward page="index.jsp"></jsp:forward>
<%
	} else {
		sAdmin = (SchoolAdmin) request.getAttribute("schoolAdmin");
		schoolList = (List<School>) session.getAttribute("schoolList");
		departmentList = (List<Department>) session.getAttribute("departmentList");
		majorList = (List<Major>) session.getAttribute("majorList");
		schoolId = (Integer) request.getAttribute("schoolId");
		departmentId = (Integer) request.getAttribute("departmentId");
		majorId = (Integer) request.getAttribute("majorId");
	}
%>
<%!String genSelect(List lis, String id, String formFieldName) {
		StringBuffer buf = new StringBuffer();

		if (id == "school" || id.equals("school")) {
			for (int i = 0, count = lis.size(); i < count; i++) {
				School s = (School) lis.get(i);
				if (schoolId == s.getId()) {
					buf.append("<option selected='selected' value='"
							+ s.getId() + "'");
				} else {
					buf.append("<option value='" + s.getId() + "'");
				}
				buf.append(">" + s.getName() + "</option>");
			}
		} else if (id == "department" || id.equals("department")) {
			for (int i = 0, count = lis.size(); i < count; i++) {
				Department d = (Department) lis.get(i);
				if(d.getId() == departmentId) {
					buf.append("<option selected='selected' value='" + d.getId() + "'");
				}else {
					buf.append("<option value='" + d.getId() + "'");
				}
				buf.append(">" + d.getName() + "</option>");
			}
		} else if (id == "major" || id.equals("major")) {
			for (int i = 0, count = lis.size(); i < count; i++) {
				Major major = (Major) lis.get(i);
				if (major.getId() == majorId) {
					buf.append("<option selected='selected' value='"
							+ major.getId() + "'");
				} else {
					buf.append("<option value='" + major.getId() + "'");
				}
				buf.append(">" + major.getName() + "</option>");
			}
		} 

		return buf.toString();
	}%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html lang="en">

<head>
<meta charset="utf-8" />
<title>找得着</title>

<link rel="stylesheet" href="css/layout.css" type="text/css"
	media="screen" />
<script src="js/jquery-1.5.2.min.js" type="text/javascript"></script>
<script src="js/hideshow.js" type="text/javascript"></script>
<script src="js/jquery.tablesorter.min.js" type="text/javascript"></script>
<script type="text/javascript" src="js/jquery.equalHeight.js"></script>
<link rel="stylesheet" type="text/css" href="css/layer.css" />
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
<link rel="stylesheet" type="text/css" href="css/jquery.multiselect.css" />
<link rel="stylesheet" type="text/css" href="css/style2.css" />
<link rel="stylesheet" type="text/css" href="css/prettify.css" />
<!-- <link rel="stylesheet" type="text/css" href="css/jquery-ui.css" /> -->
<link rel="stylesheet" type="text/css"
	href="http://ajax.googleapis.com/ajax/libs/jqueryui/1/themes/ui-lightness/jquery-ui.css" />
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/ajax-single-select.js"></script>
<script type="text/javascript">
	$(function() {
		$('.column').equalHeight();
	});
</script>
</head>
<body link="#FF0000" vlink="#0000FF" alink="#ff0000"
	onload="initSelArray('school','department','major')">

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
	<form action="Admin_ModifySchoolAdminInfo" method="post">
		<div class="module_content">
			<table style="width:100%;float:left;">
				<tr>
					<td>
						<fieldset
							style="width: 90%; height:100%; float: left; margin-left: 5%;">
							<label style="font-size:12px">姓名</label>
							<p>
								<input type="hidden" style="width:50%;" name="stuId" id="stuId"
									value="<%=sAdmin.getUsername()%>"> <input type="text"
									style="width:50%;" name="name" id="name"
									value="<%=sAdmin.getName()%>">
							</p>
						</fieldset>
					</td>
					<td>
						<fieldset
							style="width: 90%; height:100%; float: left; margin-left: 5%;">
							<label style="font-size:12px">权限</label>
							<p>
								<select id="auth" name="auth" style="width: 200px">
									<%
										if (sAdmin.getAuth() == 1) {
									%>

									<option value="1" selected="selected">学校级</option>
									<option value="2">学院级</option>
									<option value="3">专业级</option>
									<%
										} else if (sAdmin.getAuth() == 2) {
									%>
									<option value="1">学校级</option>
									<option value="2" selected="selected">学院级</option>
									<option value="3">专业级</option>
									<%
										} else {
									%>

									<option value="1">学校级</option>
									<option value="2">学院级</option>
									<option value="3" selected="selected">专业级</option>
									<%
										}
									%>
								</select>
							</p>
						</fieldset>
					</td>
				</tr>
				<tr>
					<td>
						<fieldset
							style="width: 90%; height:100%; float: left; margin-left: 5%;">
							<label style="font-size:12px">学校</label>
							<p>
								<select id="school" name="school" style="width: 200px"
									onchange="buildSelect(this.value, 'department', '1')">
									<%=genSelect(schoolList, "school", "school")%>
								</select>
							</p>
						</fieldset>
					</td>
					<td>
						<fieldset
							style="width: 90%; height:100%; float: left; margin-left: 5%;">
							<label style="font-size:12px">学院</label>
							<p>
								<select id="department" name="department" style="width: 200px"
									onchange="buildSelect(this.value, 'major', '2')">
									<%=genSelect(departmentList, "department", "department")%>
								</select>
							</p>
						</fieldset>
					</td>
				</tr>
				<tr>
					<td>
						<fieldset
							style="width: 90%; height:100%; float: left; margin-left: 5%;">
							<label style="font-size:12px">专业</label>
							<p>
								<select id="major" name="major" style="width: 200px">
									<%=genSelect(majorList, "major", "major")%>
								</select>
							</p>
						</fieldset>
					</td><%--
					<td>
						<fieldset
							style="width: 90%; height:100%; float: center; margin-left: 5%;">
							<p>*学校级帐号不必选择学院与专业，学校必选。<br>
							学院级帐号不必选择专业，学校与学院必选。<br>
							专业级帐号，学校、学院、专业三项必选</p>
						</fieldset></td>
				--%></tr>
			</table>
			<div class="clear"></div>
		</div>
		<footer>
		<div class="submit_link">
			<input type="submit" value="提交">
		</div>
		</footer>
	</form>
	</article> <!-- end of addproduct article -->
	<div class="clear"></div>

	<div class="spacer"></div>
	</section>
</body>

</html>
