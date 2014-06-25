<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.LoginedUser"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.entity.Module"%>
<%@page import="com.fuwei.commons.SystemContextUtils"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%
	LoginedUser loginedUser = SystemContextUtils
			.getCurrentUser(session);
	User user = loginedUser.getLoginedUser();
	List<Module> moduleList = loginedUser.getModulelist();
%>
<html>

	<head>
		<base href="<%=basePath%>">
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<!-- 为了让IE浏览器运行最新的渲染模式 -->
		<link href="css/plugins/bootstrap.min.css" rel="stylesheet"
			type="text/css" />
		<link href="css/plugins/font-awesome.min.css" rel="stylesheet"
			type="text/css" />
		<script src="js/plugins/jquery-1.10.2.min.js"></script>

		<script src="js/plugins/bootstrap.min.js" type="text/javascript"></script>

		<link href="css/common/head.css" rel="stylesheet" type="text/css" />
		<link href="css/common/common.css" rel="stylesheet" type="text/css" />

		<script src="js/common/head.js" type="text/javascript"></script>

	</head>

	<div style="display: none;" class="background"></div>
	<div style="display: none;" id="loading">
		数据加载中，请稍等......
	</div>
	<div id="header">
		<div class="headnav">
			<div class="btn-group">
				<button type="button" class="btn btn-success">
					<%=user.getName()%>
				</button>
				<button type="button" class="btn btn-success dropdown-toggle"
					data-toggle="dropdown">
					<span class="caret"></span>
					<span class="sr-only">Toggle Dropdown</span>
				</button>
				<ul class="dropdown-menu" role="menu">
					<li>
						<a href="#"></a>
					</li>
					<li>
						<a href="#"><i class="fa fa-cog"></i>修改密码</a>
					</li>
					<li>
						<a href="#"><i class="fa fa-power-off"></i>退出登录</a>
					</li>
				</ul>
			</div>
		</div>
		<div class="logo">
			<a href="user/index"><small> <i class="fa fa-leaf"></i>
					桐庐富伟针织厂管理系统 </small> </a>
		</div>
		<div class="clear"></div>
	</div>

	<!--    <div id="mainContainer"> -->
	<div id="left">
		<div class="panel panel-primary">

			<div class="menubar">
				<ul class="menubar-ul">
					<li class="first active">
						<a href="index.jsp"><i class="fa fa-home"></i>首页</a>
					</li>
					<li class="li_dropdown">
						<a href="#"><i class="fa fa-desktop"></i>样品系统<i
							class="fa fa-angle-down"></i> </a>
						<ul class="submenu">
							<%
								//if(user.getAuthority() == FuweiSystemData.AUTHORITY_GENERAL){
							%>
							<li>
								<a href="searchSample.do"><i class="fa fa-dashboard"></i>样品管理</a>
							</li>
							<li>
								<a href="addSample.jsp"><i class="fa fa-plus"></i>新增样品</a>
							</li>
							<li>
								<a href="searchUnPricedSample.do"><i class="fa fa-edit"></i>待核价样品</a>
							</li>
							<li>
								<a href="print.jsp"><i class="fa fa-print"></i>快递单打印</a>
							</li>
							<%
								//}else{
							%>
							<li>
								<a href="searchSample.do"><i class="fa fa-dashboard"></i>样品管理</a>
							</li>
							<li>
								<a href="addSample.jsp"><i class="fa fa-plus"></i>新增样品</a>
							</li>
							<li>
								<a href="searchUnPricedSample.do"><i class="fa fa-edit"></i>待核价样品</a>
							</li>
							<li>
								<a href="searchTransQuotation.do"><i
									class="fa fa-shopping-cart"></i>样品车</a>
							</li>
							<li>
								<a href="searchQuotationList.do"><i class="fa fa-calendar"></i>报价单列表</a>
							</li>
						</ul>
					</li>

					<li class="li_dropdown">
						<a href="#"><i class="fa fa-paperclip"></i>订单系统<i
							class="fa fa-angle-down"></i> </a>
						<ul class="submenu">
							<li>
								<a href="addOrder.jsp"><i class="fa fa-plus"></i>创建订单</a>
							</li>
							<li>
								<a href="orderlist.do"><i class="fa fa-dashboard"></i>订单列表</a>
							</li>
							<li>
								<a href="addproductNotification.jsp"><i class="fa fa-plus"></i>创建生产通知单</a>
							</li>
						</ul>
					</li>
					<li>
						<a href="print.jsp"><i class="fa fa-print"></i>快递单打印</a>
					</li>
					<li>
						<a href="systeminfos.jsp"><i class="fa fa-list-alt"></i>系统信息管理</a>
					</li>
					<%
						//}
					%>
				</ul>
			</div>

			<div class="panel-heading">
				<h3 class="panel-title">
					<div class="sidebar-collapse" id="sidebar-collapse">
						<i class="fa fa-angle-double-left"
							data-icon1="fa-angle-double-left"
							data-icon2="fa-angle-double-right">点击缩放</i>
					</div>
				</h3>
			</div>

		</div>

	</div>