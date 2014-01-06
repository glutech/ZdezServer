<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	if (session.getAttribute("adminUserLoginSucessFlag") == null) {
%>
<jsp:forward page="index.jsp"></jsp:forward>
<%
	}
%>

<%
	String rootPath = pageContext.getServletContext().getRealPath("/") + "attached/";
	session.setAttribute("rootPath", rootPath);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta charset="utf-8" />
<title>首页</title>
<link rel="stylesheet" href="css/layout.css" type="text/css"
	media="screen" />
<!--[if lt IE 9]>
	<link rel="stylesheet" href="css/ie.css" type="text/css" media="screen" />
	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->
<%--for kindeditor --%>
<link rel="stylesheet" href="kindeditor/themes/default/default.css" />
<link rel="stylesheet" href="kindeditor/plugins/code/prettify.css" />
<script charset="utf-8" src="kindeditor/kindeditor1.js"></script>
<script charset="utf-8" src="kindeditor/lang/zh_CN.js"></script>
<script charset="utf-8" src="kindeditor/plugins/code/prettify.js"></script>
<%--for kindeditor end --%>
<script src="js/jquery-1.5.2.min.js" type="text/javascript"></script>
<script src="js/hideshow.js" type="text/javascript"></script>
<script src="js/jquery.tablesorter.min.js" type="text/javascript"></script>
<script type="text/javascript" src="js/jquery.equalHeight.js"></script>

<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery.blockUI.js"></script>

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
											message : '<table><tr><td><img src="images/busy.gif" width="80" height="80" /></td> <td><span >公告发布中，请稍等...</span></td></tr></table>'
										});

								return true;

							} else {
								return false;
							}
						});
	});
</script>

<%--for kindeditor --%>
<script>
	KindEditor.ready(function(K) {
		var editor1 = K.create('textarea[name="anno_content"]', {
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
		// 为编辑器保留引用方便外部调用
		window.__CURRENT_PAGE_KINDEDITOR__ = editor1;
	});
</script>
<%--for kindeditor --%>
</head>
<body link="#FF0000" vlink="#0000FF" alink="#ff0000">

	<%@ include file="headerBarAdmin.jsp"%>

	<div class="breadcrumbs_container">
		<article class="breadcrumbs"> <a>公告管理</a>
		<div class="breadcrumb_divider"></div>
		<a class="current">新建公告</a> </article>
	</div>

	<%@ include file="sideBarAdmin.jsp"%>

	<section id="main" class="column"> <article
		class="module width_full"> <header>
	<h3>新建公告</h3>
	</header>
	<form action="Admin_NewAnnouncement" method="post">
		<div class="module_content">
			<fieldset>
				<label>公告标题</label> <input type="text" name="anno_title" id="anno_title">
			</fieldset>
			<fieldset>
				<label> 公告内容 </label>
				<textarea rows="20" name="anno_content" id="anno_content"></textarea>
			</fieldset>
			<fieldset style="width:38%; float:right;">
				<label> 署名 </label> <input type="text" name="anno_sign"
					id="anno_sign">
			</fieldset>
			<div class="clear"></div>
		</div>
		<footer>
		<div class="submit_link">
			<input type="submit" value="发布" class="alt_btn">
		</div>
		</footer>
	</form>

	</article> <!-- end of addproduct article -->

	<div class="spacer"></div>
	</section>
</body>
<script type="text/javascript">
	function validate_form() {
		
		 var $anno_title = $('#anno_title'), $anno_sign = $('#anno_sign');
		 if (!$anno_title.val()) {
			 alert('请输入公告标题');
			 $anno_title.focus();
			 return false;
		 }
		 if (!window.__CURRENT_PAGE_KINDEDITOR__.html()) {
			 alert('请输入公告内容');
			 window.__CURRENT_PAGE_KINDEDITOR__.focus();
	         return false;
		 }
		 if (!$anno_sign.val()) {
			 alert('请输入署名');
			 $anno_sign.focus();
			 return false;
		 }
		return true;
	}
</script>
</html>