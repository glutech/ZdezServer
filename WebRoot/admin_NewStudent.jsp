<%@page import="cn.com.zdez.po.Grade"%>
<%@page import="cn.com.zdez.po.Major"%>
<%@page import="cn.com.zdez.po.Department"%>
<%@page import="cn.com.zdez.po.School"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%!
List<School> schoolList = new ArrayList<School>();
List<Department> departmentList = new ArrayList<Department>();
List<Major> majorList = new ArrayList<Major>();
List<Grade> gradeList = new ArrayList<Grade>();
%>
<%
	if (session.getAttribute("adminUserLoginSucessFlag") == null) {
%>
<jsp:forward page="index.jsp"></jsp:forward>
<%
	} else {
		schoolList = (List<School>) session.getAttribute("schoolList");
		departmentList = (List<Department>) session.getAttribute("departmentList");
		majorList = (List<Major>) session.getAttribute("majorList");
		gradeList = (List<Grade>) session.getAttribute("gradeList");
	}
%>
<%!String genSelect(List lis, String id, String formFieldName) {
		StringBuffer buf = new StringBuffer();

		buf.append("<option value='0'");
		buf.append(">请选择</option>");
		
		if (id == "school" || id.equals("school")) {
			for (int i = 0, count = lis.size(); i < count; i++) {
				School s = (School) lis.get(i);
				buf.append("<option value='" + s.getId() + "'");
				buf.append(">" + s.getName() + "</option>");
			}
		} else if (id == "department" || id.equals("department")) {
			for (int i = 0, count = lis.size(); i < count; i++) {
				Department d = (Department) lis.get(i);
				buf.append("<option value='" + d.getId() + "'");
				buf.append(">" + d.getName() + "</option>");
			}
		} else if (id == "major" || id.equals("major")) {
			for (int i = 0, count = lis.size(); i < count; i++) {
				Major major = (Major) lis.get(i);
				buf.append("<option value='" + major.getId() + "'");
				buf.append(">" + major.getName() + "</option>");
			}
		} else if (id == "grade" || id.equals("grade")) {
			for (int i = 0, count = lis.size(); i < count; i++) {
				Grade grade = (Grade) lis.get(i);
				buf.append("<option value='" + grade.getId() + "'");
				buf.append(">" + grade.getDescription() + "</option>");
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
		<article class="breadcrumbs"> <a>用户管理</a>
		<div class="breadcrumb_divider"></div>
		<a class="current">新建学生用户</a> </article>
	</div>

	<%@ include file="sideBarAdmin.jsp"%>

	<section id="main" class="column"> <article
		class="module width_full"> <header>
	<h3>新建学生用户</h3>
	</header>
	<form action="Admin_NewStudent" method="post"
		onsubmit="return validate_form(this)">
		<div class="module_content">
			<table style="width:100%;float:left;">
				<tr>
					<td>
						<fieldset
							style="width: 90%; height:100%; float: left; margin-left: 5%;">
							<label style="font-size:12px">用户名</label>
							<p>
								<input type="text" style="width:50%;" name="username"
									id="username" >
							</p>
						</fieldset>
					</td>
					<td>
						<fieldset
							style="width: 90%; height:100%; float: left; margin-left: 5%;">
							<label style="font-size:12px">姓名</label>
							<p>
								<input type="text" style="width:50%;" name="name" id="name">
							</p>
						</fieldset>
					</td>
				</tr>
				<tr>
					<td>
						<fieldset
							style="width: 90%; height:100%; float: left; margin-left: 5%;">
							<label style="font-size:12px">密码</label>
							<p>
								<input type="password" style="width:50%;" name="psw" id="psw">
							</p>
						</fieldset>
					</td>
					<td>
						<fieldset
							style="width: 90%; height:100%; float: left; margin-left: 5%;">
							<label style="font-size:12px">密码确认</label>
							<p>
								<input type="password" style="width:50%;" name="pswCfm"
									id="pswCfm">
							</p>
						</fieldset>
					</td>
				</tr>
				<tr>
					<td>
						<fieldset
							style="width: 90%; height:100%; float: left; margin-left: 5%;">
							<label style="font-size:12px">性别</label>
							<p>
								<select id="gender" name="gender" style="width: 200px">
									<option value="M">男</option>
									<option value="F">女</option>
								</select>
							</p>
						</fieldset>
					</td>
					<td>
						<fieldset
							style="width: 90%; height:100%; float: left; margin-left: 5%;">
							<label style="font-size:12px">是否是教职人员</label>
							<p>
								<select id="isTeacher" name="isTeacher" style="width: 200px">
									<option value="0">否</option>
									<option value="1">是</option>
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
							<label style="font-size:12px">毕业年度（教职人员此处任意选择）</label>
							<p>
								<select id="grade" name="grade" style="width: 200px">
									<%=genSelect(gradeList, "grade", "grade")%>
								</select>
							</p>
						</fieldset>
					</td>
				</tr>
				<tr>
					<td>
						<fieldset
							style="width: 90%; height:100%; float: left; margin-left: 5%;">
							<label style="font-size:12px">学院（部门）</label>
							<p>
								<select id="department" name="department" style="width: 200px"
									onchange="buildSelect(this.value, 'major', '2')">
									<%=genSelect(departmentList, "department", "department")%>
								</select>
							</p>
						</fieldset>
					</td>
					<td>
						<fieldset
							style="width: 90%; height:100%; float: left; margin-left: 5%;">
							<label style="font-size:12px">专业（部门）</label>
							<p>
								<select id="major" name="major" style="width: 200px">
									<%=genSelect(majorList, "major", "major")%>
								</select>
							</p>
						</fieldset>
					</td>
				</tr>
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
<script type="text/javascript">
	function validate_required(field, alerttxt) {
		with (field) {
			if (value == null || value == "" || value == "0") {
				alert(alerttxt);
				return false
			} else {
				return true
			}
		}
	}

	function validate_length(field, length, alerttxt) {
		with (field) {
			if (value.length < length) {
				alert(alerttxt);
				return false
			} else {
				return true
			}
		}
	}

	function validate_isEqual(field1, field2, alerttxt) {
		with (field1, field2) {
			if (field1.value != field2.value) {
				alert(alerttxt);
				return false
			} else {
				return true
			}
		}
	}

	function validate_form(thisform) {
		with (thisform) {
			if (validate_required(username, "请输入用户名！") == false) {
				username.focus();
				return false
			}
			if (validate_required(name, "请输入姓名！") == false) {
				name.focus();
				return false
			}
			if (validate_required(psw, "请输入密码！") == false) {
				psw.focus();
				return false
			}
			if (validate_required(pswCfm, "请输入确认密码！") == false) {
				pswCfm.focus();
				return false
			}
			if (validate_required(school, "请选择学校！") == false) {
				school.focus();
				return false
			}
			if (validate_required(grade, "请选择毕业年度！") == false) {
				grade.focus();
				return false
			}
			if (validate_required(department, "请选择学院！") == false) {
				department.focus();
				return false
			}
			if (validate_required(major, "请选择专业！") == false) {
				major.focus();
				return false
			}
			if (validate_isEqual(psw, pswCfm, "两次输入密码不一致！") == false) {
				return false
			}
			if (validate_length(username, 5, "用户名长度不能少于5个字符！") == false) {
				username.focus();
				return false
			}
			if (validate_length(psw, 6, "用户名长度不能少于6个字符！") == false) {
				psw.focus();
				return false
			}
		}
	}

</script>
</html>
