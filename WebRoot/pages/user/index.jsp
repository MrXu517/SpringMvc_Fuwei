<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.Announcement"%>
<%@page import="com.fuwei.util.DateTool"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Announcement announcement = (Announcement)request.getAttribute("announcement");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>个人中心 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8">
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<!-- 为了让IE浏览器运行最新的渲染模式 -->
		<link href="css/plugins/bootstrap.min.css" rel="stylesheet"
			type="text/css" />
		<link href="css/plugins/font-awesome.min.css" rel="stylesheet"
			type="text/css" />
		<link href="css/common/common.css" rel="stylesheet" type="text/css" />
		<script src="js/plugins/jquery-1.10.2.min.js"></script>
		<script src="js/plugins/bootstrap.min.js" type="text/javascript"></script>
		<script src="js/common/common.js" type="text/javascript"></script>

	</head>

	<body>
		<%@ include file="../common/head.jsp"%>
		<div id="Content">
			<div id="main">
				
				<center>
					<%if(announcement==null){ %>
					欢迎来到桐庐富伟针织厂<a href="announcement/index" style="margin-left:50px;">历史布告栏通知</a>
					<%}else{ %>
					<div class="panel panel-primary" style="width: 750px;margin-top: 20px;">
						<div class="panel-heading">
							<h3 class="panel-title" style="text-align: left;">
								<span style="font-weight: bold;font-size: 20px;">布告栏</span>
								<a href="announcement/index" style="margin-left:50px;text-decoration: underline;">点击查看历史布告栏通知</a>
							</h3>
						</div>
						<div class="panel-body">
							<div style="min-height:350px;">
							<h1><%=announcement.getTopic() %></h1>
							<hr style="height:1px;border:none;border-top:1px solid #0066CC;" />
							<div><%=announcement.getContent() %></div></div>
						</div>
						<hr style="height:1px;border:none;border-top:1px solid #0066CC;" />
						<p><span style="padding-right: 40px;">发布人：<%=SystemCache.getUserName(announcement.getCreated_user()) %></span>
						   <span style="padding-right: 30px;">发布时间：<%=DateTool.formateDate(announcement.getCreated_at(),"yyyy/MM/dd HH:mm:ss") %></span>
						   <span style="padding-right: 30px;">最近修改时间：<%=DateTool.formateDate(announcement.getUpdated_at(),"yyyy/MM/dd HH:mm:ss") %></span></p>	
					</div>
					<%} %>
				</center>
			</div>
		</div>

	</body>
</html>
