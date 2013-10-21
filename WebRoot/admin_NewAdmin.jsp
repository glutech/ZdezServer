<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
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
<link rel="stylesheet" type="text/css" href="css/jquery.multiselect.css" />
<link rel="stylesheet" type="text/css" href="css/style2.css" />
<link rel="stylesheet" type="text/css" href="css/prettify.css" />
<!-- <link rel="stylesheet" type="text/css" href="css/jquery-ui.css" /> -->
<link rel="stylesheet" type="text/css"
	href="http://ajax.googleapis.com/ajax/libs/jqueryui/1/themes/ui-lightness/jquery-ui.css" />
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/ajax-single-select.js"></script>
</head>
<body link="#FF0000" vlink="#0000FF" alink="#ff0000"
	onload="initSelArray('school','department','major')">

	<%@ include file="headerBarAdmin.jsp"%>

	<div class="breadcrumbs_container">
		<article class="breadcrumbs"> <a>用户管理</a>
		<div class="breadcrumb_divider"></div>
		<a class="current">新建管理员</a> </article>
	</div>

	<%@ include file="sideBarAdmin.jsp"%>

	<section id="main" class="column"> <article
		class="module width_full"> <header>
	<h3>新建管理</h3>
	</header>
	<form action="Admin_NewAdmin" method="post"
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
