<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.LoginedUser"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.entity.Module"%>
<%@page import="com.fuwei.commons.SystemContextUtils"%>

<%
	LoginedUser loginedUser = SystemContextUtils.getCurrentUser(session);
	User user = loginedUser.getLoginedUser();
	List<Module> moduleList = loginedUser.getModulelist();
%>
<html>

	<head>
		<meta charset="utf-8" />

		<link href="css/global.css" rel="stylesheet" type="text/css" />
		<link href="css/font-awesome.min.css" rel="stylesheet" type="text/css" />
		<!--web字体-->
		<link href="css/index.css" rel="stylesheet" type="text/css" />
		<script src="js/common.js" type="text/javascript"></script>
		<script src="js/index.js" type="text/javascript"></script>
		
	</head>

	<div style="display: none;" class="background"></div>
	<div style="display: none;" id="loading">
		数据加载中，请稍等......
	</div>
	<div id="header">
		<div class="logo">
			<a href="index.jsp"><small> <i class="fa fa-leaf"></i>
					桐庐富伟针织厂管理系统 </small>
			</a>
		</div>
		<div class="nav">
			<ul class="head-nav">
				<li class="user">
					<a data-toggle="dropdown" href="#" class="dropdown-toggle"> <span
						class="fa fa-user"></span> <!--  <img class="nav-user-photo " src="img/user.jpg" alt="Jason's Photo">
                         --> <span class="user-info"> <span
							id="login_user_name"><%=user.getName() %></span> </span> <i
						class="fa fa-caret-down"></i> </a>
					<ul class="user-nav dropdown">
						<li>
							<a href="editPassword.jsp"> <i class="fa fa-cog"></i> 修改密码 </a>
						</li>
						<li class="divider"></li>
						<li>
							<a href="logout.do"> <i class="fa fa-power-off"></i> 退出登录 </a>
						</li>
					</ul>
				</li>
			</ul>
		</div>
	</div>
	<!--    <div id="mainContainer"> -->
	<div id="left">
		<div class="menubar">
			<ul class="menubar-ul">
				<li class="first active">
					<a href="index.jsp"><i class="fa fa-home"></i>首页</a>
				</li>
				<li class="li_dropdown">
					<a href="#"><i class="fa fa-desktop"></i>样品系统<i class="fa fa-angle-down"></i></a>
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
					<a href="#"><i class="fa fa-paperclip"></i>订单系统<i class="fa fa-angle-down"></i></a>
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

		<div class="sidebar-collapse" id="sidebar-collapse">
			<i class="fa fa-angle-double-left" data-icon1="fa-angle-double-left"
				data-icon2="fa-angle-double-right"></i>
		</div>

	</div>