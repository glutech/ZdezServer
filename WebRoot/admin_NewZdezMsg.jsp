<%@page import="cn.com.zdez.po.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="cn.com.zdez.service.*"%>

<%
	if(session.getAttribute("adminUserLoginSucessFlag") == null) {
%>
<jsp:forward page="index.jsp"></jsp:forward>
<%
	}
%>
<%!String genSelect(List lis, String formFieldName, String id) {
		StringBuffer buf = new StringBuffer();
		buf.append("<select id='" + id + "' name='" + formFieldName
				+ "' multiple='multiple' style='width: 200px'>");

		if (id == "depart" || id.equals("depart")) {
			for (int i = 0, count = lis.size(); i < count; i++) {
				Department depart = (Department) lis.get(i);
				buf.append("<option value='" + depart.getId() + "'");
				buf.append(">" + depart.getName() + "</option>");
			}
		} else if (id == "major" || id.equals("major")) {
			for (int i = 0, count = lis.size(); i < count; i++) {
				Major major = (Major) lis.get(i);
				buf.append("<option value='" + major.getId() + "'");
				buf.append(">" + major.getName() + "</option>");
			}
		} else if (id == "school" || id.equals("school")) {
			for (int i = 0, count = lis.size(); i < count; i++) {
				School sSys = (School) lis.get(i);
				buf.append("<option value='" + sSys.getId() + "'");
				buf.append(">" + sSys.getName() + "</option>");
			}
		} else if (id == "grade" || id.equals("grade")) {
			for (int i = 0, count = lis.size(); i < count; i++) {
				Grade grade = (Grade) lis.get(i);
				buf.append("<option value='" + grade.getId() + "'");
				buf.append(">" + grade.getDescription() + "</option>");
			}
		} else if (id == "schoolSys" || id.equals("schoolSys")) {
			SchoolSys s = new SchoolSys();
			for (int i = 0, count = lis.size(); i < count; i++) {
				SchoolSys ss = (SchoolSys) lis.get(i);
				buf.append("<option value='" + ss.getId() + "'");
				buf.append(">" + ss.getSysName() + "</option>");
			}
		} else {
			int count = lis.size();
			for (int i = 0; i < count; i++) {
				Student stu = (Student) lis.get(i);
				buf.append("<option value='" + stu.getId() + "'");
				buf.append(">" + stu.getName() + "</option>");
			}
		}

		buf.append("</select>");
		return buf.toString();
	}%>
<%
	List<School> schoolList = null;
	List<SchoolSys> schoolSysList = null;
	List<Grade> gradeList = null;
	List<Department> departmentList = null;
	List<Major> majorList = null;
	
	schoolList = (List<School>)session.getAttribute("schoolList");
	schoolSysList = (List<SchoolSys>)session.getAttribute("schoolSysList");
	gradeList = (List<Grade>)session.getAttribute("gradeList");
	departmentList = (List<Department>)session.getAttribute("departmentList");
	majorList = (List<Major>)session.getAttribute("majorList");
	String rootPath = pageContext.getServletContext().getRealPath("/") + "attached/";
	session.setAttribute("rootPath", rootPath);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html lang="en">

<head>
<meta charset="utf-8" />
<title>新建通知</title>

<link rel="stylesheet" href="css/layout.css" type="text/css"
	media="screen" />
<%--for kindeditor --%>
<link rel="stylesheet" href="kindeditor/themes/default/default.css" />
<link rel="stylesheet" href="kindeditor/plugins/code/prettify.css" />
<script charset="utf-8" src="kindeditor/kindeditor.js"></script>
<script charset="utf-8" src="kindeditor/lang/zh_CN.js"></script>
<script charset="utf-8" src="kindeditor/plugins/code/prettify.js"></script>
<%--for kindeditor end --%>
<script src="js/jquery-1.5.2.min.js" type="text/javascript"></script>
<script src="js/hideshow.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="css/jquery.multiselect.css" />
<link rel="stylesheet" type="text/css" href="css/jquery-ui.css" />
<!-- <link rel="stylesheet" type="text/css"
	href="http://ajax.googleapis.com/ajax/libs/jqueryui/1/themes/ui-lightness/jquery-ui.css" /> -->
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery.multiselect1.js"></script>
<script type="text/javascript" src="js/ajax-zdez.js"></script>
<script src="js/jquery.blockUI.js"></script>
<script>
	$(function() {

		$("select").multiselect({
			selectedList : 4
		});

	});
</script>
<%--for kindeditor --%>
<script>
	KindEditor.ready(function(K) {
		var editor1 = K.create('textarea[name="zdezmessagecontent"]', {
			cssPath : 'kindeditor/plugins/code/prettify.css',
			uploadJson : 'kindeditor/jsp/upload_json.jsp',
			fileManagerJson : 'kindeditor/jsp/file_manager_json.jsp',
			allowFileManager : true,
			afterCreate : function() {
				var self = this;
				K.ctrl(document, 13, function() {
					self.sync();
					document.forms['example'].submit();
				});
				K.ctrl(self.edit.doc, 13, function() {
					self.sync();
					document.forms['example'].submit();
				});
			}
		});
		prettyPrint();
	});
</script>
<%--for kindeditor --%>

<script type="text/javascript">
	$(function() {

		$('body').append(
				"<img src='images/busy.gif' style='width:0; height:0;' />")

		$('form')
				.submit(
						function() {

							if (validate_form()) {

								$
										.blockUI({
											message : '<table><tr><td><img src="images/busy.gif" width="80" height="80" /></td> <td><span >信息发送中，请稍等...</span></td></tr></table>'
										});

								return true;

							} else {
								return false;
							}
						});
	});
</script>

</head>
<body link="#FF0000" vlink="#0000FF" alink="#ff0000"
	onload="initSelArray('school','schoolSys','depart','major')">

	<%@ include file="headerBarAdmin.jsp"%>

	<div class="breadcrumbs_container">
		<article class="breadcrumbs"> <a>信息管理</a>
		<div class="breadcrumb_divider"></div>
		<a class="current">新建信息</a> </article>
	</div>

	<%@ include file="sideBarAdmin.jsp"%>

	<section id="main" class="column"> <article
		class="module width_full"> <header>
	<h3>学校名称</h3>
	</header>
	<form action="Admin_NewZdezMsg"	method="post">
		<div class="module_content">
			<div class="tab_container">
				<table style="width:100%;float:left;">
					<tbody>
						<tr>
							<td>
								<fieldset
									style="width: 90%; height:100%; float: left; margin-left: 5%;">
									<label style="font-size:12px"> 学校 </label>
									<p>
										<%=genSelect(schoolList, "xuexiao[]", "school")%>
									</p>
								</fieldset>
							</td>
							<td>
								<fieldset
									style="width: 90%; height:100%; float: left; margin-left: 5%;">
									<label style="font-size:12px"> 学业层次 </label>
									<p>
										<%=genSelect(schoolSysList, "xueyecengci[]", "schoolSys")%>
									</p>
								</fieldset></td>
						</tr>
						<tr>
							<td>
								<fieldset
									style="width: 90%; height:100%; float: left; margin-left: 5%;">
									<label style="font-size:12px"> 毕业年度 </label>
									<p>
										<%=genSelect(gradeList, "nianji[]", "grade")%>
									</p>
								</fieldset></td>
							<td>
								<fieldset
									style="width: 90%; height:100%; float: left; margin-left: 5%;">
									<label style="font-size:12px"> 学院 </label>
									<p>
										<%=genSelect(departmentList, "xueyuan[]", "depart")%>
									</p>
								</fieldset>
							</td>
						</tr>
						<tr>
							<td>
								<fieldset
									style="width: 90%; height:100%; float: left; margin-left: 5%;">
									<label style="font-size:12px"> 专业 </label>
									<p>
										<%=genSelect(majorList, "zhuanye[]", "major")%>
									</p>
								</fieldset></td>
							<td>
								<fieldset
									style="width: 90%; height:100%; float: left; margin-left: 5%;">
									<label style="font-size:12px"> 性别 </label>
									<p>
										<select id='gender' name='gender[]' multiple='multiple'
											style='width: 200px'>
											<option value='M'>男</option>
											<option value='F'>女</option>
										</select>
									</p>
								</fieldset></td>
						</tr>
						<tr>
							<td colspan="2">
								<fieldset style="width: 96%; float: left; margin-left: 2%;">
									<label style="font-size:12px"> 信息标题 </label> <input type="text"
										style="width: 96%;" name="zdezmessagetitle"
										id="zdezmessagetitle" />
								</fieldset></td>
						</tr>
						<tr>
							<td colspan="2">
								<fieldset style="width: 96%;  margin-left: 2%;">
									<label style="font-size:12px"> 信息内容 </label>
									<p></p>
									<textarea rows="20" name="zdezmessagecontent"
										id="zdezmessagecontent" style="font-size:12px">
							</textarea>
								</fieldset></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="clear"></div>
		</div>
		<footer>
		<div class="submit_link">
			<input type="submit" value="提交发送">
		</div>
		</footer>
	</form>

	</article> <!-- end of addproduct article -->

	<div class="spacer"></div>
	</section>
</body>
<script type="text/javascript">
	function show() {
		if (document.getElementById("checkbox1").checked) {
			document.getElementById("teachers").style.display = "";//显示
		} else {
			document.getElementById("teachers").style.display = "none";//隐藏 
		}
	}

	function validate_form() {
		var msg = "";

		/* alert("schoolSys:" + schoolSys.value + "/grade:" + grade.value
				+ "/depart:" + depart.value + "/major:" + major.value
				+ "/title:" + schoolmessagetitle.value + "/content:" + schoolmessagecontent.value
				+ "/checkbox:" + checkbox1.value + "/teacher:" + teacher.value);
		 */
		if (zdezmessagecontent == null
				|| zdezmessagecontent.value.length == 0) {
			msg = "请输入消息内容";
			alert(msg);
			return false;
		}
		if (zdezmessagetitle == null || zdezmessagetitle.value.length == 0) {
			msg = "请输入消息的标题";
			alert(msg);
			return false;
		}
		if (school == null || school.value.length == 0) {
			msg = "请选择至少一个学校";
			alert(msg);
			return false;
		}
		if (depart == null || depart.value.length == 0) {
			msg = "请选择至少一个学院";
			alert(msg);
			return false;
		}
		if (major == null || major.value.length == 0) {
			msg = "请选择至少一个专业";
			alert(msg);
			return false;
		}
		if (grade == null || grade.value.length == 0) {
			msg = "请至少选择一个毕业年度";
			alert(msg);
			return false;
		}

		return true;
	}
</script>

</html>