<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.financial.Expense_income"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.entity.financial.Bank"%>
<%@page import="com.fuwei.entity.financial.Subject"%>
<%@page import="com.fuwei.commons.Pager"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.constant.OrderStatus"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="net.sf.json.JSONObject"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>

<html>
	<head>
		<base href="<%=basePath%>">
		<title>财务工作台 -- 桐庐富伟针织厂</title>
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
		<script src="<%=basePath%>js/plugins/WdatePicker.js"></script>
		<script src="js/common/common.js" type="text/javascript"></script>
		<script src="js/financial/workspace.js" type="text/javascript"></script>
		<link href="css/financial/workspace.css" rel="stylesheet"
			type="text/css" />
	</head>
	<body>
		<%@ include file="../common/head.jsp"%>
		<div id="Content" class="auto_container">
			<div id="main">
				<div class="breadcrumbs" id="breadcrumbs">
					<ul class="breadcrumb">
						<li>
							<i class="fa fa-home"></i>
							<a href="user/index">首页</a>
						</li>
						<li>
							财务
						</li>
						<li class="active">
							财务工作台
						</li>
					</ul>
				</div>
				<div class="body">
					<div id="tab">

						<ul class="nav nav-tabs" role="tablist">
							<li class="active">
								<a href="#expense_incomes" role="tab" data-toggle="tab">收支明细</a>
							</li>

							<li>
								<a href="#purchase_invoices" role="tab" data-toggle="tab">进项发票</a>
							</li>
							<li>
								<a href="#sale_invoices" role="tab" data-toggle="tab">销项发票</a>
							</li>
						</ul>


						<div class="tab-content auto_height">
							<div class="tab-pane active" id="expense_incomes">
								<iframe id="expense_incomeIframe" name="expense_incomeIframe" border="0" src="financial/workspace/expense_income" > </iframe>

							</div>
							<div class="tab-pane" id="purchase_invoices">
								<iframe id="purchase_invoiceIframe" name="purchase_invoiceIframe" border="0" src="purchase_invoice/index" > </iframe>
							</div>
							<div class="tab-pane" id="sale_invoices">
								<iframe id="sale_invoiceIframe" name="sale_invoiceIframe" border="0" src="sale_invoice/index" > </iframe>
							</div>
						</div>
					</div>

				</div>
			</div>
		</div>
	</body>
</html>