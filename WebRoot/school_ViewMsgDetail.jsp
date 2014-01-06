<%@page import="cn.com.zdez.vo.SchoolMsgVo"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%!SchoolMsgVo sMsgVo = new SchoolMsgVo();
	int currentPage = 0;
	String keyword = "";
	String temp = "";
	String filePath = "";
	int type = 0;%>
<%
	if (session.getAttribute("schoolUserLoginSucessFlag") == null) {
%>
<jsp:forward page="index.jsp"></jsp:forward>
<%
	} else {
		sMsgVo = (SchoolMsgVo) request.getAttribute("schoolMsg");
		currentPage = (Integer) session.getAttribute("currentPage");
		type = (Integer) session.getAttribute("type");
		filePath = "/zdezServer/attached/html/schoolMsg"
				+ Integer.toString(sMsgVo.getSchoolMsgId()) + ".html";
	}
%>

<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8" />
<title>查看已发送信息详情</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" href="css/layout.css" type="text/css"
	media="screen" />
<!--[if lt IE 9]>
	<link rel="stylesheet" href="css/ie.css" type="text/css" media="screen" />
	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->
<!--
<script src="js/jquery-1.5.2.min.js" type="text/javascript">
</script>
-->
<script src="js/jquery-1.7.2.min.js" type="text/javascript"></script>
<script src="js/hideshow.js" type="text/javascript">
	
</script>
<script src="js/jquery.tablesorter.min.js" type="text/javascript">
	
</script>
<script type="text/javascript" src="js/jquery.equalHeight.js">
	
</script>
<link rel="stylesheet" type="text/css" href="css/layer.css" />
<!--
<SCRIPT type="text/javascript" src="js/jquery-1.3.2.js"></SCRIPT>
-->
<script type="text/javascript">
    $(function() {
        // 废弃
        // $('.column').equalHeight();
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
        // iframe自适应高度
        var iframeAutoHeight = function() {
            var $iframe = $('#iframe_content');
            $iframe.height('auto');
            $iframe.height($iframe.contents().outerHeight());
        };
        $('#iframe_content').load(function() {
            iframeAutoHeight();
        });
        $(window).resize(function() {
            iframeAutoHeight();
        });
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
header#header h2.section_title {
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
<body>

    <%@ include file="headerBarSchool.jsp"%>
 
  
    <%@ include file="secondaryBarSchool-1.jsp" %>

    <div class="breadcrumbs_container">
        <article class="breadcrumbs"> <a>信息管理</a>
        <div class="breadcrumb_divider"></div>
        <a class="current">查看信息</a> </article>
    </div>
    
    <%@ include file="secondaryBarSchool-2.jsp"%>

<div id="contentContainer" class="clearFixBlock">

    <%@ include file="sideBarSchool.jsp"%>

	<section id="main" class="column"> <article
		class="module width_full"> <header>
	<h3>信息详情</h3>
	</header>
	<div class="tab_container">
		<div id="tab1" class="tab_content">
			<table class="tablesorter" cellspacing="0">
				<tr>
					<td style="width:40pt; min-width: 40pt;">标题</td>
					<td><%=sMsgVo.getTitle()%></td>
				</tr>
				<tr>
					<td>内容</td>
					<td><iframe src="<%=filePath%>" width="100%" height="100%" frameborder="0" scrolling="auto" id="iframe_content"></iframe>
					</td>
				</tr>
				<tr>
					<td>日期</td>
					<td><%=sMsgVo.getDate()%></td>
				</tr>
				<%
					temp = "";
					for (int i = 0, count = sMsgVo.getDestGrade().size(); i < count; i++) {
						temp += sMsgVo.getDestGrade().get(i) + ", ";
					}
				%>

				<tr>
					<td>目的年级</td>
					<td><%=temp%></td>
				</tr>
				<%
					temp = "";
					for (int i = 0, count = sMsgVo.getDestDepartment().size(); i < count; i++) {
						temp += sMsgVo.getDestDepartment().get(i) + ", ";
					}
				%>
				<tr>
					<td>目的学院</td>
					<td><%=temp%></td>
				</tr>
				<%
					temp = "";
					for (int i = 0, count = sMsgVo.getDestMajor().size(); i < count; i++) {
						temp += sMsgVo.getDestMajor().get(i) + ", ";
					}
				%>
				<tr>
					<td>目的专业</td>
					<td><%=temp%></td>
				</tr>
			</table>
		</div>
	</div>
	<footer>
	<div class="submit_link">
		<%
			if (type == 1) {
		%>
		<a href="School_SendedMsg?currentPage=<%=currentPage%>"> <input
			type="submit" value="返回"> </a>
		<%
			} else {
				String keyword = (String) session.getAttribute("keyword");
		%>

		<a
			href="School_SendedMsgQuery?currentPage=<%=currentPage%>&keyword=<%=keyword%>">
			<input type="submit" value="返回"> </a>
		<%
			}
		%>
	</div>
	</footer> </article> <!-- end of addproduct article -->

	</section>
	
</div>

</body>

</html>
