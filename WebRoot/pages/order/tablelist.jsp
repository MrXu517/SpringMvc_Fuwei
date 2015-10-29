<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.Order"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Order order2 = (Order) request.getAttribute("order");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>订单表格 -- 桐庐富伟针织厂</title>
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
		<script src="<%=basePath%>js/plugins/WdatePicker.js"
			type="text/javascript"></script>
		<script src="js/common/common.js" type="text/javascript"></script>
		<script src="js/plugins/jquery.jqGrid.min.js" type="text/javascript"></script>
		<link href="css/plugins/ui.jqgrid.css" rel="stylesheet"
			type="text/css" />

		<link href="css/order/tablelist.css" rel="stylesheet" type="text/css" />
		<script src="js/order/ordergrid.js" type="text/javascript"></script>
		<script src="js/order/tablelist.js" type="text/javascript"></script>

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
						<li>
							<a href="order/detail/<%=order2.getId()%>">订单详情</a>
						</li>
						<li class="active">
							表格
						</li>
					</ul>
				</div>
				<div class="body">
					<a href="printorder/print?orderId=<%=order2.getId() %>" target="_blank" type="button" class="btn btn-success printAll"
						data-loading-text="正在打印..."> 打印选中的表格 </a>
					<a href="printorder/print?orderId=<%=order2.getId() %>" id="printAlla"  target="_blank" ><span></span></a>
					<div class="clear"></div>

					<div id="tab">
						<ul class="nav nav-pills nav-stacked" role="tablist">
							<li>
								<a href="#coloringorder" role="tab" data-toggle="tab"><input type="checkbox" class="printcheck" disabled/>染色单</a>
							</li>
							<li>
								<a href="#materialpurchaseorder" role="tab" data-toggle="tab"><input type="checkbox" class="printcheck" disabled/>原材料采购单</a>
							</li>
							<li>
								<a href="#producingorder" role="tab" data-toggle="tab"><input type="checkbox" class="printcheck" checked/>生产单</a>
							</li>
							<li>
								<a href="#gongxuproduceorder" role="tab" data-toggle="tab"><input type="checkbox" class="printcheck"/>工序加工单</a>
							</li>
							<li>
								<a href="#storeorder" role="tab" data-toggle="tab"><input type="checkbox" class="printcheck" checked/>原材料仓库</a>
							</li>
							<li>
								<a href="#planorder" role="tab" data-toggle="tab"><input type="checkbox" class="printcheck" checked/>计划单</a>
							</li>
							<li>
								<a href="#coloringprocessorder" role="tab" data-toggle="tab"><input type="checkbox" class="printcheck" checked/>染色进度单</a>
							</li>
							<li>
								<a href="#finalstorerecordorder" role="tab" data-toggle="tab"><input type="checkbox" class="printcheck" checked/>成品仓库记录单</a>
							</li>
							<li>
								<a href="#finalcheckrecordorder" role="tab" data-toggle="tab"><input type="checkbox" class="printcheck" checked/>成品检验记录单</a>
							</li>	
							<li>
								<a href="#needlecheckrecordorder" role="tab" data-toggle="tab"><input type="checkbox" class="printcheck" checked/>检针记录表</a>
							</li>			
							<li>
								<a href="#shoprecordorder" role="tab" data-toggle="tab"><input type="checkbox" class="printcheck" checked />车间记录单</a>
							</li>
							<li>
								<a href="#checkrecordorder" role="tab" data-toggle="tab"><input type="checkbox" class="printcheck" checked/>抽检记录单</a>
							</li>
							<li>
								<a href="#halfcheckrecordorder" role="tab" data-toggle="tab"><input type="checkbox" class="printcheck" checked/>半检记录单</a>
							</li>
							<li>
								<a href="#headbankorder" role="tab" data-toggle="tab"><input type="checkbox" class="printcheck" checked/>质量记录单</a>
							</li>
							<li>
								<a href="#carfixrecordorder" role="tab" data-toggle="tab"><input type="checkbox" class="printcheck" checked/>车缝记录单</a>
							</li>
							<li>
								<a href="#ironingrecordorder" role="tab" data-toggle="tab"><input type="checkbox" class="printcheck" checked/>整烫记录单</a>
							</li>

							<li>
								<a href="#fuliaopurchaseorder" role="tab" data-toggle="tab"><input type="checkbox" class="printcheck" disabled/>辅料采购单</a>
							</li>
						</ul>


						<div class="tab-content">
							<!-- 染色单 -->
							<div class="tab-pane" id="coloringorder" role="tabpanel">
								
								<jsp:include flush="true" page="ordergrid/coloringorder.jsp" />
							</div>
							<!-- 原材料采购单 -->
							<div class="tab-pane" id="materialpurchaseorder" role="tabpanel">
								
								<jsp:include flush="true" page="ordergrid/materialpurchaseorder.jsp" />
							</div>
							<!-- 生产单 -->
							<div class="tab-pane" id="producingorder" role="tabpanel">
								
								<jsp:include flush="true" page="ordergrid/producingorder.jsp" />
							</div>
							<!-- 工序加工单 -->
							<div class="tab-pane" id="gongxuproduceorder" role="tabpanel">
								
								<jsp:include flush="true" page="ordergrid/gongxuproducingorder.jsp" />
							</div>
							<!-- 原材料仓库 -->
							<div class="tab-pane" id="storeorder" role="tabpanel">
								
								<jsp:include flush="true" page="ordergrid/storeorder.jsp" />
							</div>
							<!-- 计划单 -->
							<div class="tab-pane" id="planorder" role="tabpanel">
								
								<jsp:include flush="true" page="ordergrid/planorder.jsp" />
							</div>
							<!-- 染色进度单 -->
							<div class="tab-pane" id="coloringprocessorder" role="tabpanel">
								
								<jsp:include flush="true" page="ordergrid/coloringprocessorder.jsp" />
							</div>
							<!-- 成品仓库记录单 -->
							<div class="tab-pane" id="finalstorerecordorder" role="tabpanel">
								
								<jsp:include flush="true" page="ordergrid/finalstorerecordorder.jsp" />
							</div>
							<!-- 成品检验记录单 -->
							<div class="tab-pane" id="finalcheckrecordorder" role="tabpanel">
								
								<jsp:include flush="true" page="ordergrid/finalcheckrecordorder.jsp" />
							</div>
							<!-- 检针记录单 -->
							<div class="tab-pane" id="needlecheckrecordorder" role="tabpanel">
								
								<jsp:include flush="true" page="ordergrid/needlecheckrecordorder.jsp" />
							</div>
							<!-- 车间记录单 -->
							<div class="tab-pane" id="shoprecordorder" role="tabpanel">
								
								<jsp:include flush="true" page="ordergrid/shoprecordorder.jsp" />
							</div>
							<!-- 抽检记录单 -->
							<div class="tab-pane" id="checkrecordorder" role="tabpanel">
								
								<jsp:include flush="true" page="ordergrid/checkrecordorder.jsp" />
							</div>
							<!-- 半检记录单 -->
							<div class="tab-pane" id="halfcheckrecordorder" role="tabpanel">
								
								<jsp:include flush="true" page="ordergrid/halfcheckrecordorder.jsp" />
							</div>
							<!-- 质量记录单 -->
							<div class="tab-pane" id="headbankorder" role="tabpanel">
								
								<jsp:include flush="true" page="ordergrid/headbankorder.jsp" />
							</div>
							<!-- 车缝记录单 -->
							<div class="tab-pane" id="carfixrecordorder" role="tabpanel">
								
								<jsp:include flush="true" page="ordergrid/carfixrecordorder.jsp" />
							</div>
							<!-- 整烫记录单 -->
							<div class="tab-pane" id="ironingrecordorder" role="tabpanel">
								
								<jsp:include flush="true" page="ordergrid/ironingrecordorder.jsp" />
							</div>
							<!-- 辅料采购单 -->
							<div class="tab-pane" id="fuliaopurchaseorder" role="tabpanel">
								
								<jsp:include flush="true" page="ordergrid/fuliaopurchaseorder.jsp" />
							</div>
						</div>


					</div>
				</div>


			</div>
		</div>
	</body>
</html>