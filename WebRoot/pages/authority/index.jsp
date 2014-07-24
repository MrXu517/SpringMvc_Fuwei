<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.Role"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	List<Role> rolelist = (List<Role>) request.getAttribute("rolelist");
%>

<!DOCTYPE html>

<html>
	<head>
		<base href="<%=basePath%>">
		<title>权限管理 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8">
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<!-- 为了让IE浏览器运行最新的渲染模式 -->
		<link href="css/plugins/bootstrap.min.css" rel="stylesheet"
			type="text/css" />
		<link href="css/plugins/font-awesome.min.css" rel="stylesheet"
			type="text/css" />
		<link href="css/plugins/zTreeStyle.css" rel="stylesheet"
			type="text/css" />
		<link href="css/common/common.css" rel="stylesheet" type="text/css" />
		<script src="js/plugins/jquery-1.10.2.min.js"></script>
		<script src="js/plugins/bootstrap.min.js" type="text/javascript"></script>
		<script src="<%=basePath%>js/plugins/WdatePicker.js"></script>
		<script src="js/common/common.js" type="text/javascript"></script>
		<script src="js/authority/index.js" type="text/javascript"></script>
		<script src="js/plugins/jquery.ztree.all-3.5.min.js"
			type="text/javascript"></script>

		<style type="text/css">
.rolewidget {
	width: 300px;
	float: left;
}

.authoritywidget {
	margin-left: 350px;
}

a.list-group-item {
	background-color: #E6E6E6;
	border-color: #ADADAD;
}

a.list-group-item.active {
	background-color: #D9534F;
	border-color: #D43F3A;
}

a.list-group-item.active:hover,a.list-group-item.active:focus {
	background-color: #C9302C;
	border-color: #AC2925;
}

.authoritywidget .panel-title {
	font-size: 14px;
}

.authoritywidget .panel-title #rolename {
	font-weight: bold;
	font-size: 16px;
	font-family: Microsoft Yahei;
}

.authoritywidget .panel-foot {
	padding-left: 20px;
	padding-top: 20px;
}
</style>
	</head>

	<body>
		<%@ include file="../common/head.jsp"%>
		<div id="Content">
			<div id="main">
				<div class="breadcrumbs" id="breadcrumbs">
					<ul class="breadcrumb">
						<li>
							<i class="fa fa-home"></i>
							<a href="user/index">首页</a>
						</li>
						<li class="active">
							权限管理
						</li>
					</ul>
				</div>
				<div class="body">

					<div class="container-fluid">

						<div class="rolewidget">

							<div class="list-group rolelist">
								<%
									for (Role item : rolelist) {
								%>
								<a href="#" class="list-group-item" rid="<%=item.getId()%>">
									<%=item.getDecription()%> </a>
								<%
									}
								%>
							</div>
						</div>

						<div class="authoritywidget">
							<div class="panel panel-primary">
								<div class="panel-heading">
									<h3 class="panel-title">
										设置权限 ，选中的角色：
										<span id="rolename"></span>
									</h3>
								</div>
								<div class="panel-foot">
									<button type="button" class="btn btn-danger" id="submit">
										保存修改
									</button>
									<button type="button" class="btn btn-default" id="reset">
										重置
									</button>
								</div>
								<div class="panel-body">
									<div class="content_wrap">
										<div class="zTreeDemoBackground left">
											<ul id="tree" class="ztree"></ul>
										</div>
									</div>
								</div>
							</div>
						</div>

					</div>

				</div>
			</div>
		</div>
	</body>
</html>