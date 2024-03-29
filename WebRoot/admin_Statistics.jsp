<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%! HashMap<String, HashMap<String, Integer>> statisticsMap = new HashMap<String, HashMap<String,Integer>>();%>
<%
	if (session.getAttribute("adminUserLoginSucessFlag") == null) {
%>
<jsp:forward page="index.jsp"></jsp:forward>
<%
	}else {
		statisticsMap = (HashMap<String, HashMap<String, Integer>>) request.getAttribute("statisticsMap");
	}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html lang="en">

<head>
<meta charset="utf-8" />
<title>找得着</title>

<link rel="stylesheet" href="css/layout.css" type="text/css"
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
<link rel="stylesheet" type="text/css" href="css/layer.css" />
<SCRIPT type="text/javascript" src="js/jquery-1.3.2.js"></SCRIPT>
<script type="text/javascript">
	$(function() {
		$('.column').equalHeight();
	});
</script>
</head>
<body>

	<%@ include file="headerBarAdmin.jsp"%>

	<div class="breadcrumbs_container">
		<article class="breadcrumbs"> <a>系统管理</a>
		<div class="breadcrumb_divider"></div>
		<a class="current">数据统计</a> </article>
	</div>

	<%@ include file="sideBarAdmin.jsp"%>

	<section id="main" class="column"> <article
		class="module width_full"> <header>
	<h3>在线人数统计</h3>
	</header>
	<div class="tab_container">
		<div id="tab1" class="tab_content">
			<table class="tablesorter" cellspacing="0">
				<tr>
					<td></td>
					<td>本日</td>
					<td>7天内</td>
					<td>系统运行以来</td>
				</tr>
				<%
					Iterator<Map.Entry<String, HashMap<String, Integer>>> it = statisticsMap
							.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry<String, HashMap<String, Integer>> entry = it.next();
				%>
				<tr>
					<%
						if (entry.getKey() != null) {
					%>
					<td><%=entry.getKey().toString()%></td>
					<%
						HashMap<String, Integer> tempMap = (HashMap<String, Integer>) entry
										.getValue();
								Iterator<Map.Entry<String, Integer>> tempIt = tempMap
										.entrySet().iterator();
								while (tempIt.hasNext()) {
									Map.Entry<String, Integer> tempEntry = tempIt.next();
					%>
					<td><%=tempEntry.getValue().toString()%></td>
					<%
						}
					%>
					<%
						}
					%>
				</tr>
				<%
					}
				%>
			</table>
		</div>
	</div>
	</article> <!-- end of addproduct article -->

	<div class="clear"></div>

	<div class="spacer"></div>
	</section>
</body>

</html>
