<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.LoginedUser"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.entity.Module"%>
<%@page import="com.fuwei.commons.SystemContextUtils"%>
<%
	LoginedUser loginedUser = SystemContextUtils
			.getCurrentUser(session);
	User user = loginedUser.getLoginedUser();
	List<Module> moduleList = loginedUser.getModulelist();
%>
<html>

	<head>

		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<!-- 为了让IE浏览器运行最新的渲染模式 -->
		<link href="css/plugins/bootstrap.min.css" rel="stylesheet"
			type="text/css" />
		<link href="css/plugins/font-awesome.min.css" rel="stylesheet"
			type="text/css" />

		<script src="js/plugins/bootstrap.min.js" type="text/javascript"></script>

		<link href="css/common/head.css" rel="stylesheet" type="text/css" />
		<link href="css/common/common.css" rel="stylesheet" type="text/css" />

		<script src="js/common/head.js" type="text/javascript"></script>

	</head>

	<div style="display: none;" class="background"></div>
	<div style="display: none;" class="loading">
		数据加载中，请稍等......
	</div>

	<div id="alert">
		<div id="alert_tip">
			<div class="modal fade">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal">
								<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
							</button>
							<h4 class="modal-title">
								桐庐富伟针织厂
							</h4>
						</div>
						<div class="modal-body">
							<p>
								One fine body&hellip;
							</p>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default"
								data-dismiss="modal">
								关闭
							</button>
						</div>
					</div>
					<!-- /.modal-content -->
				</div>
				<!-- /.modal-dialog -->
			</div>
			<!-- /.modal -->
		</div>
		<div id="alert_error">
			<div class="modal fade">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal">
								<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
							</button>
							<h4 class="modal-title">
								桐庐富伟针织厂
							</h4>
						</div>
						<div class="modal-body">
							<p>
								One error body&hellip;
							</p>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default"
								data-dismiss="modal">
								关闭
							</button>
						</div>
					</div>
					<!-- /.modal-content -->
				</div>
				<!-- /.modal-dialog -->
			</div>
			<!-- /.modal -->
		</div>
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
								<a href="sample/add"><i class="fa fa-plus"></i>新增样品</a>
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
						<a href="systeminfo/index"><i class="fa fa-list-alt"></i>系统信息管理</a>
					</li>
					
					<li>
						<a href="authority/index"><i class="fa fa-lock"></i>权限设置</a>
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