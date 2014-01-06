<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="java.text.*"%>
<%!String keyword = "";%>
<%
	if (session.getAttribute("schoolUserLoginSucessFlag") == null) {
%>
<jsp:forward page="index.jsp"></jsp:forward>
<%
	} else if (session.getAttribute("keyword") != null) {
		keyword = (String) session.getAttribute("keyword");
	}
%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8" />
<title>查询已发信息</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" href="css/layout.css" type="text/css"
	media="screen" />
<link rel="stylesheet" href="css/bootstrap.css" type="text/css"
	media="screen" />
<!--[if lt IE 9]>
	<link rel="stylesheet" href="css/ie.css" type="text/css" media="screen" />
	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->
<!-- 
<script src="js/jquery-1.5.2.min.js" type="text/javascript">
	-->
<script src="js/jquery-1.7.2.min.js" type="text/javascript"></script>
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
        // 废弃
        // $('.column').equalHeight();
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
		window.location.href = "School_SendedMsgQuery?currentPage="
				+ currentPage + "&keyword=" + keyword;
	}
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
/* 内容框大小 */
section#main > article.module {
    width: auto;
    margin: 12px 10px 12px 16px;
}
/* 分页样式 */
section#main div#pagingLine {
    clear: both;
    overflow: hidden;
    padding: 10px;
    margin-top: 10px;
}
section#main div#pagingLine .left, section#main div#pagingLine .right {
    display: block;
    float: left;
    width: 50%;
    margin: 0;
    padding: 0;
}
section#main div#pagingLine * {
    margin: 0; padding: 0;
    height: 1.5em; line-height: 1.5em;
    font-size: 12px;
}
section#main div#pagingLine .left span {
}
section#main div#pagingLine .right ul {
    text-align: right;
}
section#main div#pagingLine .right ul li {
    list-style: none;
    display: inline;
}
/* 搜索条 */
section#main form.quick_search {
    text-align: left;
    padding-left: 12px;
}
section#main form.quick_search input.text {
    text-align: left;
    text-indent: 0;
    padding-left: 30px;
    width: 85%;
    color: inherit;
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
        <a class="current">已发信息</a> </article>
    </div>
    
    <%@ include file="secondaryBarSchool-2.jsp"%>

<div id="contentContainer" class="clearFixBlock">

    <%@ include file="sideBarSchool.jsp"%>
    
	<section id="main" class="column">

		<article class="module width_full">
			<!-- module width_3_quarter -->
			<form class="quick_search" action="School_SendedMsgQuery"
				method="get">
				<input type="hidden" name="currentPage" value=1 />
				<input size="5" class="text"
					type="text" value="<%=keyword%>" placeholder="查询信息"
					name="keyword">
				<input value="查询" class="login"
					type="submit" />
			</form>

            <!-- 
			<header>
				<h3 class="tabs_involved">信息列表
			</header>
			-->
			<div class="tab_container">
				<div id="tab1" class="tab_content">
					<table class="tablesorter" cellspacing="0">
						<thead>
							<tr>

								<th align="left">信息标题</th>
								<th align="left">发送时间</th>
								<th align="left">已收到/发送数</th>
								<th align="left">操作</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<c:forEach var="msg" items="${schoolMsgList}">
									<tr>
										<td><c:out value="${msg.title}" /></td>
										<td><c:out value="${msg.date}"></c:out></td>
										<td><c:out value="${msg.receivedNum}"></c:out>/<c:out
												value="${msg.receiverNum}"></c:out></td>
										<td><a
											href="School_ViewMsgDetail?schoolMsgId=${msg.schoolMsgId}">
												<input type="image" src="images/icn_alert_info.png"
												title="查看"> </a><a
											href="School_MsgResend?schoolMsgId=${msg.schoolMsgId}"> <input
												type="image" src="images/message_resend.png" title="重新发送">
										</a></td>
									</tr>
								</c:forEach>
							</tr>
						</tbody>
					</table>
					<div id="pagingLine">
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
                           session.setAttribute("type", 2);
                       %>
                       <div class="left">
                            <span>共&nbsp;<font color="red"><%=all%></font>&nbsp;条信息&nbsp;&nbsp;&nbsp;&nbsp;当前第&nbsp;<%=currentPage%>/<%=allpage%>&nbsp;页</span>
                       </div>
                       <div class="right">
                               <ul>
                                    <li><a href="School_SendedMsg?currentPage=1&keyword=<%=keyword%>">首页&nbsp;</a></li>
                                    <li><%
            if (currentPage > 1) {
         %> <a href="School_SendedMsg?currentPage=<%=currentPage - 1%>&keyword=<%=keyword%>">上一页&nbsp;</a>
                                            <%
                                                } else {
                                            %>上一页&nbsp;<%
                                                }
                                            %> </li>
                                    <li><%
            if (currentPage < allpage) {
         %> <a href="School_SendedMsg?currentPage=<%=currentPage + 1%>&keyword=<%=keyword%>">下一页&nbsp;</a>
                                            <%
                                                } else {
                                            %>下一页&nbsp;<%
                                                }
                                            %> </li>
                                    <li><a href="School_SendedMsg?currentPage=<%=allpage%>&keyword=<%=keyword%>">尾页&nbsp;</a></li>
                                    <li><input type="hidden" id="hid1" name="hid1"
                                    value="<%=currentPage%>" /> <!-- 类型id --> 转到 <select
                                    id="select_type" name="select_type" onchange="oK()">
                                        <%
                                            for (int z = 1; z <= allpage; z++) {
                                        %>
                                        <option value=<%=z%>><%=z%></option>
                                        <%
                                            }
                                        %>
                                </select> 页</li>
                               </ul>
                       </div>
                    </div>
				</div>
				<!-- end of #tab1 -->

			</div>
			<!-- end of .tab_container -->
		</article>
		<!-- end of product manager article -->

	</section>
</div>
</body>

</html>
