<%@page import="cn.com.zdez.po.Grade"%>
<%@page import="cn.com.zdez.po.SchoolSys"%>
<%@page import="cn.com.zdez.po.SchoolAdmin"%>
<%@page import="cn.com.zdez.po.Student"%>
<%@page import="cn.com.zdez.po.Major"%>
<%@page import="cn.com.zdez.po.Department"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="cn.com.zdez.service.*"%>

<%
	if(session.getAttribute("schoolUserLoginSucessFlag") == null) {
%>
<jsp:forward page="index.jsp"></jsp:forward>
<%
	}
%>
<%!String genSelect(List lis, String formFieldName, String id, int auth) {
		StringBuffer buf = new StringBuffer();
		buf.append("<select id='" + id + "' name='" + formFieldName
				+ "' multiple='multiple' style='width: 200px'>");

		if (id == "depart" || id.equals("depart")) {
			for (int i = 0, count = lis.size(); i < count; i++) {
				Department depart = (Department) lis.get(i);
				if (auth == 1) {
					buf.append("<option value='" + depart.getId() + "'");
				} else {
					buf.append("<option selected='selected' value='"
							+ depart.getId() + "'");
				}
				buf.append(">" + depart.getName() + "</option>");
			}
		} else if (id == "major" || id.equals("major")) {
			for (int i = 0, count = lis.size(); i < count; i++) {
				Major major = (Major) lis.get(i);
				if (auth == 3) {
					buf.append("<option selected='selected' value='"
							+ major.getId() + "'");
				} else {
					buf.append("<option value='" + major.getId() + "'");
				}
				buf.append(">" + major.getName() + "</option>");
			}
		} else if (id == "schoolSys" || id.equals("schoolSys")) {
			for (int i = 0, count = lis.size(); i < count; i++) {
				SchoolSys sSys = (SchoolSys) lis.get(i);
				buf.append("<option selected='selected' value='" + sSys.getId()
						+ "'");
				buf.append(">" + sSys.getSysName() + "</option>");
			}
		} else if (id == "grade" || id.equals("grade")) {
			for (int i = 0, count = lis.size(); i < count; i++) {
				Grade grade = (Grade) lis.get(i);
				buf.append("<option value='" + grade.getId() + "'");
				buf.append(">" + grade.getDescription() + "</option>");
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
	List<SchoolSys> schoolSysList = null;
	List<Grade> gradeList = null;
	List<Department> departmentList = null;
	List<Major> majorList = null;
	List<Student> teacherList = null;
	//HashMap<Integer,List<Student>> dptStu = null;
	SchoolAdmin sAdmin = new SchoolAdmin();
	
	schoolSysList = (List<SchoolSys>)session.getAttribute("schoolSysList");
	gradeList = (List<Grade>)session.getAttribute("gradeList");
	departmentList = (List<Department>)session.getAttribute("departmentList");
	majorList = (List<Major>)session.getAttribute("majorList");
	teacherList = (List<Student>)session.getAttribute("teacherList");
	sAdmin = (SchoolAdmin)session.getAttribute("schoolAdmin");
	//dptStu = (HashMap<Integer,List<Student>>)session.getAttribute("dptStu");
	int auth = sAdmin.getAuth();
	String rootPath = pageContext.getServletContext().getRealPath("/") + "attached/";
	session.setAttribute("rootPath", rootPath);
%>

<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8" />
<title>新建通知</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" href="css/layout.css" type="text/css"
	media="screen" />
<!--[if lt IE 9]>
    <link rel="stylesheet" href="css/ie.css" type="text/css" media="screen" />
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
<%--for kindeditor --%>
<link rel="stylesheet" href="kindeditor-4.1.10/themes/default/default.css" />
<link rel="stylesheet" href="kindeditor-4.1.10/plugins/code/prettify.css" />
<script charset="utf-8" src="kindeditor-4.1.10/kindeditor-min.js"></script>
<script charset="utf-8" src="kindeditor-4.1.10/lang/zh_CN.js"></script>
<script charset="utf-8" src="kindeditor-4.1.10/plugins/code/prettify.js"></script>
<%--for kindeditor end --%>
<script src="js/jquery-1.7.2.min.js" type="text/javascript"></script>
<script src="js/hideshow.js" type="text/javascript"></script>
<script type="text/javascript" src="js/jquery.equalHeight.js"></script>
<link rel="stylesheet" type="text/css" href="css/jquery.multiselect.css" />
<link rel="stylesheet" type="text/css" href="css/jquery-ui.css" />

<script src="js/jquery.blockUI.js"></script>
<%--<link rel="stylesheet" type="text/css"
	href="http://ajax.googleapis.com/ajax/libs/jqueryui/1/themes/ui-lightness/jquery-ui.css" />
--%>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery.multiselect.js"></script>
<%
	if (auth == 1 || auth ==2) {
%>
<script type="text/javascript" src="js/ajax.js"></script>
<%
	}else if (auth == 3) {
%>
<script type="text/javascript" src="js/ajaxAuth3.js"></script>
<%
	}
%>
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
		var editor1 = K.create('textarea[name="schoolmessagecontent"]', {
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
			},
			resizeType : 1
		});
		prettyPrint();
		// 为编辑器保留引用方便外部调用
		window.__CURRENT_PAGE_KINDEDITOR__ = editor1;
	});
</script>
<%--for kindeditor --%>

<script type="text/javascript">
	$(function() {

		$('body').append(
				"<img src='images/busy.gif' style='width:0; height:0; position:fixed;' />")

		$('form')
				.submit(
						function() {

							if (validate_form()) {
							
							     setTimeout(function() {
							     
							         $.blockUI({
                                            message : '<table><tr><td><img src="images/busy.gif" width="80" height="80" /></td> <td><span >信息发送中，请稍等...</span></td></tr></table>'
                                        });
							     }, 10);

								return true;

							} else {
								return false;
							}
						});
	});
</script>


<!-- 一些基于兼容性考虑和效果，一些调整 wu.kui2@gmail.com -->
<script type="text/javascript">
    /* 根据窗口大小自适应左右栏大小，需渲染完成后才能取得正确的高，故用window.onload做为触发时机 */
    $(window).load(function () {
        var $body           =   $('body')
          , $header         =   $('#header')
          , $secondary_bar  =   $('#secondary_bar')
          , $sidebar        =   $('#sidebar')
          , $main           =   $('#main');
        var maxNumFunc = function(a, b, c) {
            var t = a;
            if(t < b) t = b; 
            if(t < c) t = c;
            return t;
        };
        var resizeFunc = function() {
            $sidebar.css('height', 'auto');
            $main.css('min-height', '');
            var contentHeight = function() {
                 var bodyHeight     =   $body.height() - $header.height() - $secondary_bar.height() + 4 // 4px是#contentContainer上浮遮罩阴影的大小
                   , sidebarHeight  =   $sidebar.outerHeight(true)
                   , mainHeight     =   $main.outerHeight(true);
                 return maxNumFunc(bodyHeight, sidebarHeight, mainHeight);
             }();
             $sidebar.height(contentHeight );
             $main.css('min-height', contentHeight + 'px');
        };
        // 窗口大小变动时检查大小
        $(window).resize(resizeFunc).trigger('resize');
        // 滚动时检查大小（无法监听非window的resize事件）
        $(window).scroll(function() {
            $(window).trigger('resize');
        });
    });
    $(function() {
        // 调整 input[type=images] 控件的逻辑，屏蔽其默认行为逻辑，改为直接执行a的跳转。$parent.trigger('click') 在IE下无效？？
        $("input[type='image']").on('click', function(e) {
            var $this = $(this)
              , $parent = $this.parent();
            if($this.is('INPUT') && $parent.is('A') && !!$parent.attr('href')) {
                if(e && e.stopPropagation) {
                    e.stopPropagation();
                } else {
                    window.event.cancelBubble = true;
                }
                window.location.href = $parent.attr('href');
            }
        });
        // 调整 input[type=submit] 的错误使用，同上。如 <a href=''><input type="submit" ... /></a>
        $("input[type='submit']").on('click', function(e) {
            var $this = $(this)
              , $parent = $this.parent();
            if($this.is('input') && $parent.is('A') && !!$parent.attr('href') && !$this.parents('form').length) {
                if(e && e.stopPropagation) {
                    e.stopPropagation();
                } else {
                    window.event.cancelBubble = true;
                }
                window.location.href = $parent.attr('href');
            }
        });
    });
    $(function() {
        var $checkbox = $('#teachers :checkbox')
          , $p        = $checkbox.closest('fieldset').find('>p')
          , onCheckboxValueChanged  = function(v) {
                v ? $p.show() : $p.hide();
          };
        $checkbox.click(function() {
            onCheckboxValueChanged($checkbox.prop('checked'));
        });
        $checkbox.closest('fieldset').find('label').click(function() {
            onCheckboxValueChanged(!$checkbox.prop('checked'));
            $checkbox.prop('checked') ? $checkbox.removeAttr('checked') : $checkbox.attr('checked', 'checked');
        }).css('cursor', 'pointer');
    });
</script>
<style type="text/css">
html, body {
    margin: 0;
    padding: 0;
}
/* 设置最小宽度，修正页宽过小时的显示异常 */
body {
    min-width: 960px;
}
/* 修正"查看网站"溢出导致页面超过body限宽 */
header#header {
    overflow: hidden;
}
er#header h2.section_title {
    width: 67%;
}
header#header div.btn_view_site {
    width: 10%;
}
/* 修正 article.breadcrumbs 对齐 */
article.breadcrumbs {
    margin-left: 16px;
}
/* 修正浮动对布局高度的影响 */
header#header, section#secondary_bar, .clearFixBlock {
    clear: both;
    overflow: hidden;
}
/* 统一secondaryBar行的下阴影对后续内容带来的影响 */
div#contentContainer {
    margin-top: -4px;
}
aside#sidebar, section#main {
    margin-top: 0px;
}
/* 内容框大小、样式 */
section#main > article.module {
    width: auto;
    margin: 12px 10px 12px 16px;
}
section#main div.module_content {
    margin: 0px;
}
section#main div.module_content fieldset {
    padding: 6px 14px; margin: 0;
    vertical-align: middle;
    display: block;
    clear: both;
    overflow: hidden;
}
section#main div.module_content fieldset label { 
    width: auto;
}
section#main div.module_content fieldset p {
    margin: 0;
}
section#main div.module_content td fieldset label {
    padding: 0; margin: 0;
    display: block;
    float: none;
}
section#main div.module_content td fieldset > p {
    display: block;
    float: none;
    width: 100%;
}

</style>
<!-- 一些基于兼容性考虑和效果，一些调整  -->

</head>
<body link="#FF0000" vlink="#0000FF" alink="#ff0000"
	onload="initSelArray('depart','major')">

    <%@ include file="headerBarSchool.jsp"%>
 
  
    <%@ include file="secondaryBarSchool-1.jsp" %>

    <div class="breadcrumbs_container">
        <article class="breadcrumbs"> <a href="school.jsp">信息管理</a>
        <div class="breadcrumb_divider"></div>
        <a class="current">新建信息</a> </article>
    </div>
    
    <%@ include file="secondaryBarSchool-2.jsp"%>

<div id="contentContainer" class="clearFixBlock">

    <%@ include file="sideBarSchool.jsp"%>

	<section id="main" class="column"> <article
		class="module width_full">
		<header>
	<h3>新建信息</h3>
	</header>
	<form action="School_NewMsg" method="post">
		<div class="module_content">
			<div class="tab_container">
				<table style="width:100%;"  border="0" cellpadding="0" cellspacing="18">
					<tbody>
						<tr valign="top">
							<td>
								<fieldset>
									<label style="font-size:12px">学业层次</label>
									<p>
										<%=genSelect(schoolSysList, "xuezhi[]", "schoolSys", auth)%>
									</p>
								</fieldset>
							</td>
							<td>
								<fieldset>
									<label style="font-size:12px">毕业年度</label>
									<p>
										<%=genSelect(gradeList, "nianji[]", "grade", auth)%>
									</p>
								</fieldset>
							</td>
						</tr>
						<tr valign="top">
							<td>
								<fieldset>
									<label style="font-size:12px">学院</label>
									<p>
										<%=genSelect(departmentList, "xueyuan[]", "depart", auth)%>
									</p>
								</fieldset>
							</td>
							<td>
								<fieldset>
									<label style="font-size:12px">专业</label>
									<p>
										<%=genSelect(majorList, "zhuanye[]", "major", auth)%>
									</p>
								</fieldset>
							</td>
						</tr>
						<tr valign="top">
							<td>
							     
								<div id="teachers">
									<fieldset style="background:none; border: none; padding: 0;">
									    <div style="font-size: 12px; height: 20px; line-height: 20px;">
									       <input style="float: none; margin: 0; display: inline;" id="checkbox1" name="checkbox" type="checkbox" value="1" />
									       <label style="float: none; display: inline;">是否同时发送给指定的教职人员</label>
									    </div>
										<p align="left" style="margin:0; padding: 0; padding-left: 16px; display: none;">
											<%=genSelect(teacherList, "teacher[]", "teacher", auth)%>
										</p>
									</fieldset>
								</div></td>
						      <td></td>
						</tr>
						<tr valign="top">
							<td colspan="2">
								<fieldset>
									<label style="font-size:12px">信息标题</label>
									<input type="text"
										name="schoolmessagetitle"
										id="schoolmessagetitle"
										style="margin: 0; width: 98%; display: block; float: none;"
										 />
								</fieldset>
							</td>
						</tr>
						<tr valign="top">
							<td colspan="2">
								<fieldset>
									<label style="font-size:12px"> 信息内容 </label>
									<p></p>
									<textarea rows="20" name="schoolmessagecontent"
										id="schoolmessagecontent" style="font-size:12px; width: 100%;">
							</textarea>
								</fieldset>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="clear"></div>
		</div>
		<footer>
		<div class="submit_link">
			<input id="t1" type="submit" value="提交发送">
		</div>
		</footer>
	</form>

	</article> <!-- end of addproduct article -->

	</section>
	
</div>

<script type="text/javascript">
	function show() {
		if (document.getElementById("checkbox1").checked) {
			document.getElementById("teachers").style.display = "";//显示
		} else {
			document.getElementById("teachers").style.display = "none";//隐藏 
		}
	}

	function validate_form() {
        var $schoolmessagetitle = $('#schoolmessagetitle')
          , $schoolSys = $('#schoolSys')
          , $grade = $('#grade')
          , $depart = $('#depart')
          , $major = $('#major')
          , $teacherSwith = $('#teachers input[type=checkbox]')
          , $teacher = $('#teacher');
       if(!$schoolmessagetitle.val()) {
            alert('请输入信息标题');
            $schoolmessagetitle.focus();
            return false;
       }
       if(!window.__CURRENT_PAGE_KINDEDITOR__.html()) {
            alert('请输入信息内容');
            window.__CURRENT_PAGE_KINDEDITOR__.focus();
            return false;
       }
       if($schoolSys.get(0).selectedIndex === -1) {
            alert('请选择学业层次');
            $schoolSys.focus();
            return false;
       }
       if($grade.get(0).selectedIndex == -1) {
            alert('请选择毕业年度');
            $grade.focus();
            return false;
       }
       if($depart.get(0).selectedIndex == -1) {
            alert('请选择学院');
            $depart.focus();
            return false;
       }
       if($major.get(0).selectedIndex == -1) {
            alert('请选择专业');
            $major.focus();
            return false;
       }
       if($teacherSwith.prop('checked') && $teacher.get(0).selectedIndex == -1) {
            alert('要选择发送特定教职人员，请确保里面有可选的人员并至少选择一个，否则请取消该选择！');
            $teacher.focus();
            return false;
       }
       return true;
	}
</script>
</body>
</html>