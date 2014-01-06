<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	if (session.getAttribute("schoolUserLoginSucessFlag") == null) {
%>
<jsp:forward page="index.jsp"></jsp:forward>
<%
	}
%>

<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8" />
<title>发送成功</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" href="css/layout.css" type="text/css"
	media="screen" />
<!--[if lt IE 9]>
	<link rel="stylesheet" href="css/ie.css" type="text/css" media="screen" />
	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->
<!-- 
<script src="js/jquery-1.5.2.min.js" type="text/javascript"></script>
-->
<script src="js/jquery-1.7.2.min.js" type="text/javascript"></script>
<script src="js/hideshow.js" type="text/javascript"></script>

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
</script>
<style type="text/css">
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
</style>
<!-- 一些基于兼容性考虑和效果，一些调整  -->
</head>
<body>

    <%@ include file="headerBarSchool.jsp"%>
 
  
    <%@ include file="secondaryBarSchool-1.jsp" %>

    <div class="breadcrumbs_container">
        <article class="breadcrumbs"> <a href="school.jsp">信息管理</a>
        <div class="breadcrumb_divider"></div>
        <a class="current">发送成功</a> </article>
    </div>
    
    <%@ include file="secondaryBarSchool-2.jsp"%>

<div id="contentContainer" class="clearFixBlock">

    <%@ include file="sideBarSchool.jsp"%>

	<section id="main" class="column">

	<h4 class="alert_info"><%=request.getAttribute("errorMsg")==null?"信息已经发送成功":request.getAttribute("errorMsg") %></h4>
	<hr>
	<fieldset
		style="width: 10%; text-align:center; margin-left: 40%;">
		<a href="School_NewMsgHref"><input align="middle"
			type="submit" name="goback" value="返回" onclick="javascript: window.location.href('school.jsp'); return false;" /> </a>
	</fieldset>

	</section>
</div>

</body>

</html>


