<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%! HashMap<String, List<HashMap<String, Integer>>> statisticsDetailsMap = new HashMap<String, List<HashMap<String, Integer>>>();%>
<%
	if (session.getAttribute("adminUserLoginSucessFlag") == null) {
%>
<jsp:forward page="index.jsp"></jsp:forward>
<%
	}else {
		statisticsDetailsMap = (HashMap<String, List<HashMap<String, Integer>>>) request.getAttribute("statisticsDetailsMap");
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
		class="module width_full"> <%
 	Iterator<Map.Entry<String, List<HashMap<String, Integer>>>> it = statisticsDetailsMap
 			.entrySet().iterator();
 	while (it.hasNext()) {
 		Map.Entry<String, List<HashMap<String, Integer>>> entry = it
 				.next();
 %> <header>
	<h3><%=entry.getKey()%></h3>
	</header>
	<div class="tab_container">


		<div id="tab1" class="tab_content">
			<table class="tablesorter" cellspacing="0">
				<tr>
					<td>学院</td>
					<td>在线人数</td>
				</tr>
				<%
					List<HashMap<String, Integer>> dptList = entry.getValue();
						int size = dptList.size();
						for (int i = 0; i < size; i++) {
							Iterator<Map.Entry<String, Integer>> dptIt = dptList.get(i)
									.entrySet().iterator();
							while (dptIt.hasNext()) {
								Map.Entry<String, Integer> dptEntry = dptIt.next();
				%>
				<tr>
					<td><%=dptEntry.getKey()%></td>
					<td><%=dptEntry.getValue()%></td>
				</tr>
				<%
					}
						}
				%>
			</table>
		</div>
	</div>

	<%
		statisticsDetailsMap = null;
		}
	%> </article> <!-- end of addproduct article -->

	<div class="clear"></div>

	<div class="spacer"></div>
	</section>
</body>

</html>
