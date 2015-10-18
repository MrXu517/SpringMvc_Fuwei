<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="com.fuwei.entity.Material"%>
<%@page import="com.fuwei.entity.ordergrid.HeadBankOrder"%>
<%@page import="com.fuwei.entity.ordergrid.HeadBankOrderDetail"%>
<%@page import="com.fuwei.entity.ordergrid.ProducingOrder"%>
<%@page import="com.fuwei.entity.ordergrid.ProducingOrderDetail"%>
<%@page import="com.fuwei.entity.ordergrid.ProducingOrderMaterialDetail"%>
<%@page import="com.fuwei.entity.ordergrid.PlanOrder"%>
<%@page import="com.fuwei.entity.ordergrid.PlanOrderDetail"%>
<%@page import="com.fuwei.entity.ordergrid.PlanOrderProducingDetail"%>
<%@page import="com.fuwei.entity.ordergrid.StoreOrder"%>
<%@page import="com.fuwei.entity.ordergrid.StoreOrderDetail"%>
<%@page import="com.fuwei.entity.ordergrid.HalfCheckRecordOrder"%>
<%@page import="com.fuwei.entity.ordergrid.HalfCheckRecordOrderDetail"%>
<%@page import="com.fuwei.entity.ordergrid.HalfCheckRecordOrderDetail2"%>
<%@page import="com.fuwei.entity.ordergrid.MaterialPurchaseOrder"%>
<%@page import="com.fuwei.entity.ordergrid.MaterialPurchaseOrderDetail"%>
<%@page import="com.fuwei.entity.ordergrid.ColoringOrder"%>
<%@page import="com.fuwei.entity.ordergrid.ColoringOrderDetail"%>
<%@page import="com.fuwei.entity.ordergrid.CheckRecordOrder"%>
<%@page import="com.fuwei.entity.ordergrid.CheckRecordOrderDetail"%>
<%@page import="com.fuwei.entity.ordergrid.FuliaoPurchaseOrder"%>
<%@page import="com.fuwei.entity.ordergrid.FuliaoPurchaseOrderDetail"%>
<%@page import="com.fuwei.entity.ordergrid.CarFixRecordOrder"%>
<%@page import="com.fuwei.entity.ordergrid.CarFixRecordOrderDetail"%>
<%@page import="com.fuwei.entity.ordergrid.IroningRecordOrder"%>
<%@page import="com.fuwei.entity.ordergrid.IroningRecordOrderDetail"%>

<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.entity.ordergrid.ProductionScheduleOrder"%>
<%@page import="com.fuwei.entity.ordergrid.FinalStoreOrder"%>
<%@page import="com.fuwei.entity.ordergrid.ShopRecordOrder"%>
<%@page import="com.fuwei.entity.ordergrid.ColoringProcessOrder"%>
<%@page import="com.fuwei.entity.ordergrid.ColoringProcessOrderDetail"%>
<%@page import="com.fuwei.entity.ordergrid.GongxuProducingOrder"%>
<%@page import="com.fuwei.entity.ordergrid.GongxuProducingOrderDetail"%>
<%@page import="com.fuwei.entity.ordergrid.GongxuProducingOrderMaterialDetail"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Order order = (Order) request.getAttribute("order");
//	HeadBankOrder headBankOrder = (HeadBankOrder) request
//			.getAttribute("headBankOrder");

	List<ProducingOrder> producingOrderList = (List<ProducingOrder>) request
			.getAttribute("producingOrderList");
	producingOrderList = producingOrderList == null ? new ArrayList<ProducingOrder>()
			: producingOrderList;
	String productfactoryStr = "";
	String seq = "";
	for(ProducingOrder producingOrder : producingOrderList){
		productfactoryStr += seq + SystemCache.getFactoryName(producingOrder.getFactoryId());
		seq = " | ";
	}

	PlanOrder planOrder = (PlanOrder) request.getAttribute("planOrder");
	List<PlanOrderDetail> planOrderDetailList = planOrder == null ? new ArrayList<PlanOrderDetail>()
			: planOrder.getDetaillist();

	StoreOrder storeOrder = (StoreOrder) request
			.getAttribute("storeOrder");
	List<StoreOrderDetail> storeOrderDetailList = storeOrder == null ? new ArrayList<StoreOrderDetail>()
			: storeOrder.getDetaillist();

	//原材料采购单
	List<MaterialPurchaseOrder> materialPurchaseOrderList = (List<MaterialPurchaseOrder>) request
			.getAttribute("materialPurchaseOrderList");
	materialPurchaseOrderList = materialPurchaseOrderList == null ? new ArrayList<MaterialPurchaseOrder>()
			: materialPurchaseOrderList;

	//染色单
	List<ColoringOrder> coloringOrderList = (List<ColoringOrder>) request
			.getAttribute("coloringOrderList");
	coloringOrderList = coloringOrderList == null ? new ArrayList<ColoringOrder>()
			: coloringOrderList;

	//辅料采购单
	List<FuliaoPurchaseOrder> fuliaoPurchaseOrderList = (List<FuliaoPurchaseOrder>) request
			.getAttribute("fuliaoPurchaseOrderList");
	fuliaoPurchaseOrderList = fuliaoPurchaseOrderList == null ? new ArrayList<FuliaoPurchaseOrder>()
			: fuliaoPurchaseOrderList;

	//半检记录单
	HalfCheckRecordOrder halfCheckRecordOrder = (HalfCheckRecordOrder) request
			.getAttribute("halfCheckRecordOrder");
	//	List<HalfCheckRecordOrderDetail> halfCheckRecordOrderDetailList = halfCheckRecordOrder == null ? new ArrayList<HalfCheckRecordOrderDetail>()
	//			: halfCheckRecordOrder.getDetaillist();
	List<HalfCheckRecordOrderDetail2> halfCheckRecordOrderDetailList2 = halfCheckRecordOrder == null ? new ArrayList<HalfCheckRecordOrderDetail2>()
			: halfCheckRecordOrder.getDetail_2_list();

	//抽检记录单
//	CheckRecordOrder checkRecordOrder = (CheckRecordOrder) request
//			.getAttribute("checkRecordOrder");

	//车缝记录单
//	CarFixRecordOrder carFixRecordOrder = (CarFixRecordOrder) request
//			.getAttribute("carFixRecordOrder");

	//整烫记录单
//	IroningRecordOrder ironingRecordOrder = (IroningRecordOrder) request
//			.getAttribute("ironingRecordOrder");

	//2015-3-23添加新表格 生产进度单
//	ProductionScheduleOrder productionScheduleOrder = (ProductionScheduleOrder) request
//			.getAttribute("productionScheduleOrder");
//	FinalStoreOrder finalStoreOrder = (FinalStoreOrder) request
//			.getAttribute("finalStoreOrder");
//	ShopRecordOrder shopRecordOrder = (ShopRecordOrder) request
//			.getAttribute("shopRecordOrder");
	ColoringProcessOrder coloringProcessOrder = (ColoringProcessOrder) request
			.getAttribute("coloringProcessOrder");
	List<ColoringProcessOrderDetail> coloringProcessOrderDetailList = coloringProcessOrder == null ? new ArrayList<ColoringProcessOrderDetail>()
			: coloringProcessOrder.getDetaillist();
			
	List<OrderDetail> DetailList = order == null ? new ArrayList<OrderDetail>()
			: order.getDetaillist();
	if (DetailList == null) {
		DetailList = new ArrayList<OrderDetail>();
	}
	
	//2015-3-31添加
	Boolean has_store_order_save = SystemCache.hasAuthority(session,
			"order/store");
	Boolean has_plan_order_save = SystemCache.hasAuthority(session,
			"order/plan");
	Boolean has_halfcheckrecord_order_save = SystemCache.hasAuthority(session,
			"order/halfcheckrecord");
	Boolean has_fuliaopurchase_order_save = SystemCache.hasAuthority(session,
			"order/fuliaopurchase");
	Boolean has_material_purchase_order_save = SystemCache.hasAuthority(session,
			"order/materialpurchase");
	Boolean has_coloring_order_save = SystemCache.hasAuthority(session,
			"order/coloring");
	Boolean has_order_producing_price = SystemCache.hasAuthority(session,
			"order/producing/price");
	
	
	Boolean has_material_purchase_order_delete = SystemCache.hasAuthority(session,
			"material_purchase_order/delete");
	Boolean has_coloring_order_delete = SystemCache.hasAuthority(session,
			"coloring_order/delete");
	Boolean has_fuliao_purchase_order_delete = SystemCache.hasAuthority(session,
			"fuliao_purchase_order/delete");
	Boolean has_producing_order_delete = SystemCache.hasAuthority(session,
			"order/producing/delete");
	Boolean has_producing_order_edit = SystemCache.hasAuthority(session,
			"order/producing");
	Boolean has_order_producing_price_edit = SystemCache.hasAuthority(session,"order/producing/price_edit");
	Boolean has_order_producing_price_request = SystemCache.hasAuthority(session,"order/producing/price_request");

	//2015-10-18添加工序加工单
	Boolean has_gongxu_producing_order = SystemCache.hasAuthority(session,"gongxu_producing_order/index");
	Boolean has_gongxu_producing_order_add = SystemCache.hasAuthority(session,"gongxu_producing_order/add");
	Boolean has_gongxu_producing_order_delete = SystemCache.hasAuthority(session,"gongxu_producing_order/delete");
	Boolean has_gongxu_producing_price = SystemCache.hasAuthority(session,"gongxu_producing_order/price");
	List<GongxuProducingOrder> gongxuProducingOrderList = (List<GongxuProducingOrder>) request
			.getAttribute("gongxuProducingOrderList");
	gongxuProducingOrderList = gongxuProducingOrderList == null ? new ArrayList<GongxuProducingOrder>()
			: gongxuProducingOrderList;
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
							<a href="order/detail/<%=order.getId()%>">订单详情</a>
						</li>
						<li class="active">
							表格
						</li>
					</ul>
				</div>
				<div class="body">
					<a href="printorder/print?orderId=<%=order.getId() %>" target="_blank" type="button" class="btn btn-success printAll"
						data-loading-text="正在打印..."> 打印选中的表格 </a>
					<a href="printorder/print?orderId=<%=order.getId() %>" id="printAlla"  target="_blank" ><span></span></a>
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
								<a href="#gongxuproduceorder" role="tab" data-toggle="tab"><input type="checkbox" class="printcheck" checked/>工序加工单</a>
							</li>
							<li>
								<a href="#storeorder" role="tab" data-toggle="tab"><input type="checkbox" class="printcheck" checked/>原材料仓库</a>
							</li>
							<li>
								<a href="#planorder" role="tab" data-toggle="tab"><input type="checkbox" class="printcheck" checked/>计划单</a>
							</li>
							<li>
								<a href="#productionscheduleorder" role="tab" data-toggle="tab"><input type="checkbox" class="printcheck" checked/>生产进度单</a>
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
							<!-- 质量记录单  -->
							<div class="tab-pane" id="headbankorder" role="tabpanel">
								<div class="container-fluid">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="orderId"
												value="<%=order.getId()%>" />

											
											<a target="_blank" type="button"
												class="pull-right btn btn-success printBtn"
												data-loading-text="正在打印..."> 打印 </a>
										</form>

										<div class="clear"></div>
										<div class="col-md-12 tablewidget">
											<table class="table">
												<caption>
													桐庐富伟针织厂质量记录单
												</caption>
												<thead>
													<tr>
														<td colspan="3" class="pull-right orderNumber">
															№：<%=order.getOrderNumber()%></td>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td>
															<table
																class="table table-responsive table-bordered tableTb">
																<tbody>
																	<tr>
																		<td rowspan="8" width="50%">
																			<a href="/<%=order.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=order.getImg_s()%>">
																			</a>
																		</td>
																		<td width="20%">
																			生产单位
																		</td>
																		<td class="orderproperty"><%=productfactoryStr %></td>
																	</tr>

																	<tr>
																		<td colspan="2" class="center">
																			订单信息
																		</td>
																	</tr>
																	<tr>
																		<td>
																			公司
																		</td>
																		<td><%=SystemCache.getCompanyName(order.getCompanyId())%></td>
																	</tr>
																	<tr>
																		<td>
																			客户
																		</td>
																		<td><%=SystemCache.getCustomerName(order.getCustomerId())%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=order.getCompany_productNumber()%></td>
																	</tr>
																	<tr>
																		<td>
																			款名
																		</td>
																		<td><%=order.getName()%></td>
																	</tr>
																	<tr>
																		<td>
																			跟单
																		</td>
																		<td><%=SystemCache.getEmployeeName(order.getCharge_employee())%></td>
																	</tr>
																	<tr>
																		<td>
																			发货时间
																		</td>
																		<td><%=DateTool.formatDateYMD(order.getEnd_at())%></td>
																	</tr>
																</tbody>
															</table>

														</td>
													</tr>
													<tr>
														<td>
															<table class="table table-responsive detailTb">
																<caption>
																	颜色及数量
																</caption>
																<thead>
																	<tr>
																		<th width="15%">
																			颜色
																		</th>
																		<th width="15%">
																			机织克重(g)
																		</th>
																		<th width="15%">
																			纱线种类
																		</th>
																		<th width="15%">
																			尺寸
																		</th>
																		<th width="15%">
																			生产数量
																		</th>
																	</tr>
																</thead>
																<tbody>
																	<%
																		for (PlanOrderDetail detail : planOrderDetailList) {
																	%>
																	<tr class="tr"
																		data='<%=SerializeTool.serialize(detail)%>'>
																		<td class="color"><%=detail.getColor()%>
																		</td>
																		<td class="produce_weight"><%=detail.getProduce_weight()%>
																		</td>
																		<td class="yarn_name"><%=SystemCache.getMaterialName(detail.getYarn())%>
																		</td>
																		<td class="size"><%=detail.getSize()%>
																		</td>
																		<td class="quantity"><%=detail.getQuantity()%>
																		</td>
																	</tr>

																	<%
																		}
																	%>

																</tbody>
															</table>
															<div id="navigator"></div>
														</td>
													</tr>
												</tbody>
											</table>

										</div>


									</div>
								</div>

							</div>

							<!-- 生产单  -->
							<div class="tab-pane" id="producingorder" role="tabpanel">
								<%if(has_producing_order_edit && !order.isDelivered()){ %>
								<div class="emptyrecordwidget">
									<p>
										如果您要创建生产单，请点击下方的按钮
									</p>
									<a href="producing_order/<%=order.getId()%>/add"
										class="btn btn-primary" id="createProducingorderBtn">创建生产单</a>
								</div>
								<%} %>
								<a  href="printorder/print?orderId=<%=order.getId() %>&gridName=producingorder" target="_blank" type="button"
												class="printBtn btn btn-success"
												data-loading-text="正在打印..."> 打印生产单 </a>
								<%if(has_order_producing_price_request){ %>
								<button orderid="<%=order.getId() %>" ordernumber="<%=order.getOrderNumber() %>" type="button" class="priceRequestBtn btn btn-info" data-loading-text="正在请求划价..."> 请求划价  </button>
								<%} %>
							
								<%
									for (ProducingOrder producingOrder : producingOrderList) {
										List<ProducingOrderDetail> producingOrderDetailList = producingOrder == null ? new ArrayList<ProducingOrderDetail>()
												: producingOrder.getDetaillist();
										List<ProducingOrderMaterialDetail> producingOrderMaterialDetailList = producingOrder == null ? new ArrayList<ProducingOrderMaterialDetail>()
												: producingOrder.getDetail_2_list();
								%>
								<div class="container-fluid producingorderWidget">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="id"
												value="<%=producingOrder == null ? "" : producingOrder
						.getId()%>" />
											<input type="hidden" name="orderId"
												value="<%=order.getId()%>" />	
												
											<%if(producingOrder.isEdit()){ %>
												<%if(has_producing_order_delete){ %>
													<a target="_blank" type="button"
													class="pull-right btn btn-default deleteTableBtn"
													data-loading-text="正在删除..."> 删除 </a>
												<%} %>
												<%if(has_producing_order_edit){ %>
												<a href="producing_order/put/<%=producingOrder.getId() %>" target="_blank" type="button"
													class="pull-right btn btn-default"
													data-loading-text="正在跳转页面......"> 编辑 </a><%} %>
											<%} %>
											<%if(has_order_producing_price_edit){ %>
												<a target="_blank" href="producing_order/price/<%=producingOrder.getId() %>" type="button" class="pull-right btn btn-success"> 开始划价  </a>
											 <%} %>
											<a href="producing_order/print/<%=producingOrder.getId()%>" target="_blank" type="button"
												class="pull-right btn btn-success"
												data-loading-text="正在打印..."> 打印 </a>
										</form>

										<div class="clear"></div>
										<div class="col-md-12 tablewidget">
											<table class="table">
												<caption>
													桐庐富伟针织厂生产单
												</caption>
												<thead>
													<tr>
														<td colspan="3" class="pull-right orderNumber">
															№：<%=order.getOrderNumber()%> - <%=producingOrder.getNumber() %></td>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td>
															<table
																class="table table-responsive table-bordered tableTb">
																<tbody>
																	<tr>
																		<td rowspan="7" width="50%">
																			<a href="/<%=order.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=order.getImg_s()%>">
																			</a>
																		</td>
																		<td width="20%">
																			生产单位
																		</td>
																		<td class="orderproperty"><%=SystemCache.getFactoryName(producingOrder
								.getFactoryId())%></td>
																	</tr>

																	<tr>
																		<td colspan="2" class="center">
																			订单信息
																		</td>
																	</tr>
																	<tr>
																		<td>
																			公司
																		</td>
																		<td><%=SystemCache.getCompanyName(order
										.getCompanyId())%></td>
																	</tr>
																	<tr>
																		<td>
																			客户
																		</td>
																		<td><%=SystemCache.getCustomerName(order.getCustomerId())%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=order.getCompany_productNumber()%></td>
																	</tr>
																	<tr>
																		<td>
																			款名
																		</td>
																		<td><%=order.getName()%></td>
																	</tr>
																	<tr>
																		<td>
																			跟单
																		</td>
																		<td><%=SystemCache.getEmployeeName(order.getCharge_employee())%></td>
																	</tr>
																</tbody>
															</table>

														</td>
													</tr>
													<tr>
														<td>
															<table class="table table-responsive detailTb">
																<caption>
																	颜色及数量
																</caption>
																<thead>
																	<tr>
																		<th width="15%">
																			颜色
																		</th>
																		<th width="15%">
																			机织克重(g)
																		</th>
																		<th width="15%">
																			纱线种类
																		</th>
																		<th width="15%">
																			尺寸
																		</th>
																		<th width="15%">
																			生产数量
																		</th>
																		
																		<%if(has_order_producing_price){ %>
																		<th width="15%">
																			价格(/个、顶、套)
																		</th>
																		<%} %>

																	</tr>
																</thead>
																<tbody>
																	<%
																		for (ProducingOrderDetail detail : producingOrderDetailList) {
																	%>
																	<tr class="tr"
																		data='<%=SerializeTool.serialize(detail)%>'>
																		<td class="color"><%=detail.getColor()%>
																		</td>
																		<td class="produce_weight"><%=detail.getProduce_weight()%>
																		</td>
																		<td class="yarn_name"><%=SystemCache.getMaterialName(detail.getYarn())%>
																		</td>
																		<td class="size"><%=detail.getSize()%>
																		</td>
																		<td class="quantity"><%=detail.getQuantity()%>
																		</td>
																		<%if(has_order_producing_price){ %>
																		<td class="price"><%=detail.getPrice()%></td>
																		<%} %>
																	</tr>

																	<%
																		}
																	%>

																</tbody>
															</table>
															<div id="navigator"></div>
														</td>
													</tr>

													<tr>
														<td>
															<table class="table table-responsive detailTb2">
																<caption>
																	
																	生产材料信息
																</caption>
																<thead>
																	<tr>
																		<th width="20%">
																			材料
																		</th>
																		<th width="20%">
																			色号
																		</th>
																		<th width="20%">
																			数量(kg)
																		</th>
																		<th width="25%">
																			标准色样
																		</th>
																	</tr>
																</thead>
																<tbody>
																	<%
																		for (ProducingOrderMaterialDetail detail : producingOrderMaterialDetailList) {
																	%>
																	<tr class="tr"
																		data='<%=SerializeTool.serialize(detail)%>'>
																		<td class="material_name"><%=SystemCache.getMaterialName(detail
											.getMaterial())%>
																		</td>
																		<td class="color"><%=detail.getColor()%>
																		</td>
																		<td class="quantity"><%=detail.getQuantity()%>
																		</td>
																		<td class="colorsample"><%=detail.getColorsample()%>
																		</td>
																	</tr>

																	<%
																		}
																	%>

																</tbody>
															</table>
															<div id="navigator"></div>
														</td>
													</tr>
												</tbody>
											</table>

										</div>


									</div>
								</div>
								<%
									}
								%>
								
							</div>


							<!-- 工序加工单  -->
							<div class="tab-pane" id="gongxuproduceorder" role="tabpanel">
								<%if(has_gongxu_producing_order_add && !order.isDelivered()){ %>
								<div class="emptyrecordwidget">
									<p>
										如果您要创建工序加工单，请点击下方的按钮
									</p>
									<a href="gongxu_producing_order/<%=order.getId()%>/add"
										class="btn btn-primary" id="createGongxuProducingorderBtn">创建工序加工单</a>
								</div>
								<%} %>
								<a  href="printorder/print?orderId=<%=order.getId() %>&gridName=gongxuproduceorder" target="_blank" type="button"
												class="printBtn btn btn-success"
												data-loading-text="正在打印..."> 打印工序加工单 </a>
								
							
								<%
									for (GongxuProducingOrder gongxuProducingOrder : gongxuProducingOrderList) {
										List<GongxuProducingOrderDetail> gongxuProducingOrderDetailList = gongxuProducingOrder == null ? new ArrayList<GongxuProducingOrderDetail>()
												: gongxuProducingOrder.getDetaillist();
										List<GongxuProducingOrderMaterialDetail> gongxuProducingOrderMaterialDetailList = gongxuProducingOrder == null ? new ArrayList<GongxuProducingOrderMaterialDetail>()
												: gongxuProducingOrder.getDetail_2_list();
								%>
								<div class="container-fluid gongxuproduceorderWidget">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="id"
												value="<%=gongxuProducingOrder == null ? "" : gongxuProducingOrder
						.getId()%>" />
											<input type="hidden" name="orderId"
												value="<%=order.getId()%>" />	
												
											<%if(gongxuProducingOrder.isEdit()){ %>
												<%if(has_gongxu_producing_order_delete){ %>
													<a target="_blank" type="button"
													class="pull-right btn btn-default deleteTableBtn"
													data-loading-text="正在删除..."> 删除 </a>
												<%} %>
												<%if(has_gongxu_producing_order_add){ %>
												<a href="gongxu_producing_order/put/<%=gongxuProducingOrder.getId() %>" target="_blank" type="button"
													class="pull-right btn btn-default"
													data-loading-text="正在跳转页面......"> 编辑 </a><%} %>
											<%} %>
											
											<a href="gongxu_producing_order/print/<%=gongxuProducingOrder.getId()%>" target="_blank" type="button"
												class="pull-right btn btn-success"
												data-loading-text="正在打印..."> 打印 </a>
										</form>

										<div class="clear"></div>
										<div class="col-md-12 tablewidget">
											<table class="table">
												<caption>
													桐庐富伟针织厂工序加工单
												</caption>
												<thead>
													<tr>
														<td colspan="3" class="pull-right orderNumber">
															№：<%=order.getOrderNumber()%> - <%=gongxuProducingOrder.getNumber() %></td>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td>
															<table
																class="table table-responsive table-bordered tableTb">
																<tbody>
																	<tr>
																		<td rowspan="7" width="50%">
																			<a href="/<%=gongxuProducingOrder.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=gongxuProducingOrder.getImg_s()%>">
																			</a>
																		</td>
																		<td width="20%">
																			生产单位
																		</td>
																		<td class="orderproperty"><%=SystemCache.getFactoryName(gongxuProducingOrder
								.getFactoryId())%></td>
																	</tr>

																	<tr>
																		<td>
																			生产工序
																		</td>
																		<td><%=SystemCache.getGongxuName(gongxuProducingOrder
										.getGongxuId())%></td>
																	</tr>
																	<tr>
																		<td>
																			公司
																		</td>
																		<td><%=SystemCache.getCompanyName(gongxuProducingOrder
										.getCompanyId())%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=gongxuProducingOrder.getCompany_productNumber()%></td>
																	</tr>
																	<tr>
																		<td>
																			款名
																		</td>
																		<td><%=gongxuProducingOrder.getName()%></td>
																	</tr>
																	<tr>
																		<td>
																			跟单
																		</td>
																		<td><%=SystemCache.getEmployeeName(gongxuProducingOrder.getCharge_employee())%></td>
																	</tr>
																	<tr>
																		<td>
																			备注
																		</td>
																		<td><%=gongxuProducingOrder.getMemo()==null?"":gongxuProducingOrder.getMemo()%></td>
																	</tr>
																</tbody>
															</table>

														</td>
													</tr>
													<tr>
														<td>
															<table class="table table-responsive detailTb">
																<caption>
																	颜色及数量
																</caption>
																<thead>
																	<tr>
																		<th width="15%">
																			颜色
																		</th>
																		<th width="15%">
																			机织克重(g)
																		</th>
																		<th width="15%">
																			纱线种类
																		</th>
																		<th width="15%">
																			尺寸
																		</th>
																		<th width="15%">
																			生产数量
																		</th>
																		<%if(has_gongxu_producing_price){ %>
																		<th width="15%">
																			价格(/个、顶、套)
																		</th>
																		<%} %>
																	</tr>
																</thead>
																<tbody>
																	<%
																		for (GongxuProducingOrderDetail detail : gongxuProducingOrderDetailList) {
																	%>
																	<tr class="tr"
																		data='<%=SerializeTool.serialize(detail)%>'>
																		<td class="color"><%=detail.getColor()%>
																		</td>
																		<td class="produce_weight"><%=detail.getProduce_weight()%>
																		</td>
																		<td class="yarn_name"><%=SystemCache.getMaterialName(detail.getYarn())%>
																		</td>
																		<td class="size"><%=detail.getSize()%>
																		</td>
																		<td class="quantity"><%=detail.getQuantity()%>
																		</td>
																		<%if(has_gongxu_producing_price){ %>
																		<td class="price"><%=detail.getPrice()%></td>
																		<%} %>
																	</tr>

																	<%
																		}
																	%>

																</tbody>
															</table>
															<div id="navigator"></div>
														</td>
													</tr>

													<tr>
														<td>
															<table class="table table-responsive detailTb2">
																<caption>
																	
																	生产材料信息
																</caption>
																<thead>
																	<tr>
																		<th width="20%">
																			材料
																		</th>
																		<th width="20%">
																			色号
																		</th>
																		<th width="20%">
																			数量(kg)
																		</th>
																		<th width="25%">
																			标准色样
																		</th>
																	</tr>
																</thead>
																<tbody>
																	<%
																	if(gongxuProducingOrderMaterialDetailList!=null){
																		for (GongxuProducingOrderMaterialDetail detail : gongxuProducingOrderMaterialDetailList) {
																	%>
																	<tr class="tr"
																		data='<%=SerializeTool.serialize(detail)%>'>
																		<td class="material_name"><%=SystemCache.getMaterialName(detail
											.getMaterial())%>
																		</td>
																		<td class="color"><%=detail.getColor()%>
																		</td>
																		<td class="quantity"><%=detail.getQuantity()%>
																		</td>
																		<td class="colorsample"><%=detail.getColorsample()%>
																		</td>
																	</tr>

																	<%
																		}}
																	%>

																</tbody>
															</table>
															<div id="navigator"></div>
														</td>
													</tr>
												</tbody>
											</table>

										</div>


									</div>
								</div>
								<%
									}
								%>
							</div>


							<!-- 计划单  -->
							<div class="tab-pane" id="planorder" role="tabpanel">
								<div class="container-fluid">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="id"
												value="<%=planOrder == null ? "" : planOrder.getId()%>" />
											<input type="hidden" name="orderId"
												value="<%=order.getId()%>" />
											<%if( has_plan_order_save && planOrder.isEdit()){ %>
											<button type="submit"
												class="pull-right btn btn-danger saveTable"
												data-loading-text="正在保存...">
												保存对当前表格的修改
											</button>
											<%} %>
											<a target="_blank" type="button"
												class="pull-right btn btn-success printBtn"
												data-loading-text="正在打印..."> 打印 </a>
										</form>

										<div class="clear"></div>
										<div class="col-md-12 tablewidget">
											<table class="table">
												<caption>
													桐庐富伟针织厂计划单
												</caption>
												<thead>
													<tr>
														<td colspan="3" class="pull-right orderNumber">
															№：<%=order.getOrderNumber()%></td>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td>
															<table
																class="table table-responsive table-bordered tableTb">
																<tbody>
																	<tr>
																		<td rowspan="8" width="50%">
																			<a href="/<%=order.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=order.getImg_s()%>">
																			</a>
																		</td>
																		<td width="20%">
																			生产单位
																		</td>
																		<td class="orderproperty"><%=productfactoryStr %></td>
																	</tr>

																	<tr>
																		<td colspan="2" class="center">
																			订单信息
																		</td>
																	</tr>
																	<tr>
																		<td>
																			公司
																		</td>
																		<td><%=SystemCache.getCompanyName(order.getCompanyId())%></td>
																	</tr>
																	<tr>
																		<td>
																			客户
																		</td>
																		<td><%=SystemCache.getCustomerName(order.getCustomerId())%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=order.getCompany_productNumber()%></td>
																	</tr>
																	<tr>
																		<td>
																			款名
																		</td>
																		<td><%=order.getName()%></td>
																	</tr>
																	<tr>
																		<td>
																			跟单
																		</td>
																		<td><%=SystemCache.getEmployeeName(order.getCharge_employee())%></td>
																	</tr>
																	<tr>
																		<td>
																			发货时间
																		</td>
																		<td><%=DateTool.formatDateYMD(order.getEnd_at())%></td>
																	</tr>
																</tbody>
															</table>

														</td>
													</tr>
													<tr>
														<td>
															<table class="table table-responsive detailTb">
																<caption>
																	颜色及数量
																</caption>
																<thead>
																	<tr>
																		<th width="15%">
																			颜色
																		</th>
																		<th width="15%">
																			克重(g)
																		</th>
																		<th width="15%">
																			机织克重(g)
																		</th>
																		<th width="15%">
																			纱线种类
																		</th>
																		<th width="15%">
																			尺寸
																		</th>
																		<th width="15%">
																			生产数量
																		</th><th width="15%" save-widget="true">
																			
																		</th>
																	</tr>
																</thead>
																<tbody>
																	<%
																		for (PlanOrderDetail detail : planOrderDetailList) {
																	%>
																	<tr class="tr"
																		data='<%=SerializeTool.serialize(detail)%>'>
																		<td class="color"><%=detail.getColor()%>
																		</td>
																		<td class="weight"><%=detail.getWeight()%>
																		</td>
																		<td><input type="text"
																				class="form-control produce_weight value"
																				value="<%=detail.getProduce_weight()%>" />
																		</td>
																		<td class="yarn_name"><%=SystemCache.getMaterialName(detail.getYarn())%>
																		</td>
																		<td class="size"><%=detail.getSize()%>
																		</td>
																		<td class="int">
																			<input type="text"
																				class="form-control quantity value"
																				value="<%=detail.getQuantity()%>" />
																		</td><td save-widget="true"><a class="btn btn-default plus15Btn">+15</a></td>
																	</tr>

																	<%
																		}
																	%>

																</tbody>
															</table>
															<div id="navigator"></div>
														</td>
													</tr>
												</tbody>
											</table>

										</div>


									</div>
								</div>


							</div>

							<!-- 原材料仓库单  -->
							<div class="tab-pane" id="storeorder" role="tabpanel">
								<div class="container-fluid">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="id"
												value="<%=storeOrder == null ? "" : storeOrder.getId()%>" />
											<input type="hidden" name="orderId"
												value="<%=order.getId()%>" />
											<%if(has_store_order_save && (storeOrder==null || storeOrder.isEdit())){ %>
											<button type="submit"
												class="pull-right btn btn-danger saveTable"
												data-loading-text="正在保存...">
												保存对当前表格的修改
											</button>
											<%} %>
											<a target="_blank" type="button"
												class="pull-right btn btn-success printBtn"
												data-loading-text="正在打印..."> 打印 </a>
										</form>

										<div class="clear"></div>
										<div class="col-md-12 tablewidget">
											<table class="table">
												<caption>
													桐庐富伟针织厂原材料仓库
												</caption>
												<thead>
													<tr>
														<td colspan="3" class="pull-right orderNumber">
															№：<%=order.getOrderNumber()%></td>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td>
															<table
																class="table table-responsive table-bordered tableTb">
																<tbody>
																	<tr>
																		<td rowspan="8" width="50%">
																			<a href="/<%=order.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=order.getImg_s()%>">
																			</a>
																		</td>
																		<td width="20%">
																			生产单位
																		</td>
																		<td class="orderproperty"><%=productfactoryStr %></td>
																	</tr>

																	<tr>
																		<td colspan="2" class="center">
																			订单信息
																		</td>
																	</tr>
																	<tr>
																		<td>
																			公司
																		</td>
																		<td><%=SystemCache.getCompanyName(order.getCompanyId())%></td>
																	</tr>
																	<tr>
																		<td>
																			客户
																		</td>
																		<td><%=SystemCache.getCustomerName(order.getCustomerId())%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=order.getCompany_productNumber()%></td>
																	</tr>
																	<tr>
																		<td>
																			款名
																		</td>
																		<td><%=order.getName()%></td>
																	</tr>
																	<tr>
																		<td>
																			跟单
																		</td>
																		<td><%=SystemCache.getEmployeeName(order.getCharge_employee())%></td>
																	</tr>
																	<tr>
																		<td>
																			发货时间
																		</td>
																		<td><%=DateTool.formatDateYMD(order.getEnd_at())%></td>
																	</tr>
																</tbody>
															</table>

														</td>
													</tr>
													<tr>
														<td>
															<table class="table table-responsive detailTb">
																<caption>
																	
																	<button type="button"
																		class="btn btn-primary addRow pull-left" save-widget="true">
																		添加一行
																	</button>
																	
																	材料列表
																</caption>
																<thead>
																	<tr>
																		<th width="15%">
																			色号
																		</th>
																		<th width="15%">
																			材料
																		</th>
																		<th width="15%">
																			总数量(kg)
																		</th>
																		<th width="15%">
																			领取人
																		</th>
																		<th width="15%">
																			标准样纱
																		</th>
																		<th width="15%"  save-widget="true">
																			操作
																		</th>
																	</tr>
																</thead>
																<tbody>
																	<%
																		for (StoreOrderDetail detail : storeOrderDetailList) {
																	%>
																	<tr class="tr"
																		data='<%=SerializeTool.serialize(detail)%>'>
																		<td class="color"><%=detail.getColor()%>
																		</td>
																		<td class="material_name"><%=SystemCache.getMaterialName(detail.getMaterial())%>
																		</td>
																		<td class="quantity"><%=detail.getQuantity()%>
																		</td>
																		<td class="factory_name"><%=SystemCache.getFactoryName(detail.getFactoryId())%>
																		</td>
																		<td class="yarn"><%=detail.getYarn()%>
																		</td>
																		<td class="_handle" save-widget="true">
																			<a class='copyRow' href='#'>复制</a> | 
																			<a class='editRow' href='#'>修改</a> |
																			<a class='deleteRow' href='#'>删除</a>
																		</td>
																	</tr>

																	<%
																		}
																	%>

																</tbody>
															</table>
															<div id="navigator"></div>
														</td>
													</tr>

												</tbody>
											</table>

										</div>


									</div>
								</div>

								<!--
						 			添加编辑原材料仓库对话框 -->
								<div class="modal fade tableRowDialog" id="storeDialog">
									<div class="modal-dialog">
										<div class="modal-content">
											<div class="modal-header">
												<button type="button" class="close" data-dismiss="modal">
													<span aria-hidden="true">&times;</span><span
														class="sr-only">Close</span>
												</button>
												<h4 class="modal-title">
													添加一行
												</h4>
											</div>
											<div class="modal-body">
												<form class="form-horizontal rowform" role="form">
													<div class="form-group col-md-12">
														<label for="color" class="col-sm-3 control-label">
															色号
														</label>
														<div class="col-sm-8">
															<input type="text" name="color" id="color"
																class="form-control require" />
														</div>
														<div class="col-sm-1"></div>
													</div>
													<div class="form-group col-md-12">
														<label for="material" class="col-sm-3 control-label">
															材料
														</label>
														<div class="col-sm-8">
															<select name="material" id="material"
																class="form-control require">
																<option value="">
																	未选择
																</option>
																<%
																	for (Material material : SystemCache.materiallist) {
																%>
																<option value="<%=material.getId()%>"><%=material.getName()%></option>
																<%
																	}
																%>
															</select>
														</div>
														<div class="col-sm-1"></div>
													</div>
													<div class="form-group col-md-12">
														<label for="quantity" class="col-sm-3 control-label">
															总数量(kg)
														</label>
														<div class="col-sm-8">
															<input type="text" name="quantity" id="quantity"
																class="form-control double require" />
														</div>
														<div class="col-sm-1"></div>
													</div>
													<div class="form-group col-md-12">
														<label for="factoryId" class="col-sm-3 control-label">
															领取人
														</label>
														<div class="col-sm-8">
															<select class="form-control require" name="factoryId"
																id="factoryId">
																<option value="">
																	未选择
																</option>
																<%
																	for (Factory factory : SystemCache.factorylist) {
																%>
																<option value="<%=factory.getId()%>"><%=factory.getName()%></option>
																<%
																	}
																%>
															</select>
														</div>
														<div class="col-sm-1"></div>
													</div>
													<div class="form-group col-md-12">
														<label for="yarn" class="col-sm-3 control-label">
															标准样纱
														</label>
														<div class="col-sm-8">
															<input type="text" name="yarn" id="yarn"
																class="form-control" />
														</div>
														<div class="col-sm-1"></div>
													</div>


													<div class="modal-footer">
														<button type="submit" class="btn btn-primary"
															data-loading-text="正在保存...">
															保存
														</button>
														<button type="button" class="btn btn-default"
															data-dismiss="modal">
															关闭
														</button>
													</div>
												</form>
											</div>

										</div>
									</div>
								</div>
								<!-- 添加编辑原材料仓库对话框 -->
							</div>

							<!--  半检记录单 -->
							<div class="tab-pane" id="halfcheckrecordorder" role="tabpanel">
								<div class="container-fluid">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="id"
												value="<%=halfCheckRecordOrder == null ? ""
					: halfCheckRecordOrder.getId()%>" />
											<input type="hidden" name="orderId"
												value="<%=order.getId()%>" />
											<%if(has_halfcheckrecord_order_save && halfCheckRecordOrder.isEdit()){ %>
											<button type="submit"
												class="pull-right btn btn-danger saveTable"
												data-loading-text="正在保存...">
												保存对当前表格的修改
											</button>
											<%} %>
											<a target="_blank" type="button"
												class="pull-right btn btn-success printBtn"
												data-loading-text="正在打印..."> 打印 </a>
										</form>

										<div class="clear"></div>
										<div class="col-md-12 tablewidget">
											<table class="table">
												<caption>
													桐庐富伟针织厂半检记录单
												</caption>
												<thead>
													<tr>
														<td colspan="3" class="pull-right orderNumber">
															№：<%=order.getOrderNumber()%></td>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td>
															<table
																class="table table-responsive table-bordered tableTb">
																<tbody>
																	<tr>
																		<td rowspan="8" width="50%">
																			<a href="/<%=order.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=order.getImg_s()%>">
																			</a>
																		</td>
																		<td width="20%">
																			生产单位
																		</td>
																		<td class="orderproperty"><%=productfactoryStr %></td>
																	</tr>

																	<tr>
																		<td colspan="2" class="center">
																			订单信息
																		</td>
																	</tr>
																	<tr>
																		<td>
																			公司
																		</td>
																		<td><%=SystemCache.getCompanyName(order.getCompanyId())%></td>
																	</tr>
																	<tr>
																		<td>
																			客户
																		</td>
																		<td><%=SystemCache.getCustomerName(order.getCustomerId())%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=order.getCompany_productNumber()%></td>
																	</tr>
																	<tr>
																		<td>
																			款名
																		</td>
																		<td><%=order.getName()%></td>
																	</tr>
																	<tr>
																		<td>
																			跟单
																		</td>
																		<td><%=SystemCache.getEmployeeName(order.getCharge_employee())%></td>
																	</tr>
																	<tr>
																		<td>
																			发货时间
																		</td>
																		<td><%=DateTool.formatDateYMD(order.getEnd_at())%></td>
																	</tr>
																</tbody>
															</table>

														</td>
													</tr>
													<tr>
														<td>
															<table class="table table-responsive detailTb">
																<caption>
																	颜色及数量
																</caption>
																<thead>
																	<tr>
																		<th width="15%">
																			颜色
																		</th>
																		<th width="15%">
																			克重(g)
																		</th>
																		<th width="15%">
																			纱线种类
																		</th>
																		<th width="15%">
																			尺寸
																		</th>
																		<th width="15%">
																			生产数量
																		</th>
																	</tr>
																</thead>
																<tbody>
																	<%
																		for (PlanOrderDetail detail : planOrderDetailList) {
																	%>
																	<tr class="tr"
																		data='<%=SerializeTool.serialize(detail)%>'>
																		<td class="color"><%=detail.getColor()%>
																		</td>
																		<td class="weight"><%=detail.getWeight()%>
																		</td>
																		<td class="yarn_name"><%=SystemCache.getMaterialName(detail.getYarn())%>
																		</td>
																		<td class="size"><%=detail.getSize()%>
																		</td>
																		<td class="quantity"><%=detail.getQuantity()%>
																		</td>
																	</tr>

																	<%
																		}
																	%>

																</tbody>
															</table>
															<div id="navigator"></div>
														</td>
													</tr>

													<tr>
														<td>
															<table class="table table-responsive detailTb2">
																<caption>
																	<button save-widget="true" type="button"
																		class="btn btn-primary addRow pull-left">
																		添加一行
																	</button>
																	生产材料信息
																</caption>
																<thead>
																	<tr>
																		<th width="20%">
																			材料
																		</th>
																		<th width="20%">
																			色号
																		</th>
																		<th width="25%">
																			标准色样
																		</th>
																		<th width="15%"  save-widget="true">
																			操作
																		</th>
																	</tr>
																</thead>
																<tbody>
																	<%
																		for (HalfCheckRecordOrderDetail2 detail : halfCheckRecordOrderDetailList2) {
																	%>
																	<tr class="tr"
																		data='<%=SerializeTool.serialize(detail)%>'>
																		<td class="material_name"><%=SystemCache.getMaterialName(detail.getMaterial())%>
																		</td>
																		<td class="color"><%=detail.getColor()%>
																		</td>
																		<td class="colorsample"><%=detail.getColorsample()%>
																		</td>
																		<td class="_handle" save-widget="true">
																			<a class='copyRow' href='#'>复制</a> | 
																			<a class='editRow' href='#'>修改</a> |
																			<a class='deleteRow' href='#'>删除</a>
																		</td>
																	</tr>

																	<%
																		}
																	%>

																</tbody>
															</table>
															<div id="navigator"></div>
														</td>
													</tr>
												</tbody>
											</table>

										</div>


									</div>
								</div>

								<div class="modal fade tableRowDialog"
									id="halfcheckrecordDialog2">
									<div class="modal-dialog">
										<div class="modal-content">
											<div class="modal-header">
												<button type="button" class="close" data-dismiss="modal">
													<span aria-hidden="true">&times;</span><span
														class="sr-only">Close</span>
												</button>
												<h4 class="modal-title">
													添加一行
												</h4>
											</div>
											<div class="modal-body">
												<form class="form-horizontal rowform" role="form">
													<div class="form-group col-md-12">
														<label for="material" class="col-sm-3 control-label">
															材料
														</label>
														<div class="col-sm-8">
															<select name="material" id="material"
																class="form-control require">
																<option value="">
																	未选择
																</option>
																<%
																	for (Material material : SystemCache.materiallist) {
																%>
																<option value="<%=material.getId()%>"><%=material.getName()%></option>
																<%
																	}
																%>
															</select>
														</div>
														<div class="col-sm-1"></div>
													</div>
													<div class="form-group col-md-12">
														<label for="color" class="col-sm-3 control-label">
															色号
														</label>
														<div class="col-sm-8">
															<input type="text" name="color" id="color"
																class="form-control require" />
														</div>
														<div class="col-sm-1"></div>
													</div>
													<div class="form-group col-md-12">
														<label for="colorsample" class="col-sm-3 control-label">
															标准色样
														</label>
														<div class="col-sm-8">
															<input type="text" name="colorsample" id="colorsample"
																class="form-control" />
														</div>
														<div class="col-sm-1"></div>
													</div>
													<div class="modal-footer">
														<button type="submit" class="btn btn-primary"
															data-loading-text="正在保存...">
															保存
														</button>
														<button type="button" class="btn btn-default"
															data-dismiss="modal">
															关闭
														</button>
													</div>
												</form>
											</div>

										</div>
									</div>
								</div>
								<!-- 添加编辑生产单对话框 -->
							</div>


							<!-- 原材料采购单 -->
							<div class="tab-pane" id="materialpurchaseorder" role="tabpanel">
								<%if(has_material_purchase_order_save && !order.isDelivered()){ %>
								<div class="emptyrecordwidget">
									<p>
										如果您要创建原材料采购单，请点击下方的按钮
									</p>
									<a href="material_purchase_order/add/<%=order.getId()%>"
										class="btn btn-primary" id="createProducingorderBtn">创建原材料采购单</a>
								</div>
								<%} %>
								<a  href="printorder/print?orderId=<%=order.getId() %>&gridName=materialpurchaseorder" target="_blank" type="button"
												class="printBtn btn btn-success"
												data-loading-text="正在打印..."> 打印原材料采购单 </a>
								<%
									for (MaterialPurchaseOrder materialPurchaseOrder : materialPurchaseOrderList) {
										List<MaterialPurchaseOrderDetail> materialPurchaseOrderDetailList = materialPurchaseOrder == null ? new ArrayList<MaterialPurchaseOrderDetail>()
												: materialPurchaseOrder.getDetaillist();
								%>

								<div class="container-fluid materialorderWidget">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="id"
												value="<%=materialPurchaseOrder == null ? ""
						: materialPurchaseOrder.getId()%>" />
											<input type="hidden" name="orderId"
												value="<%=order.getId()%>" />
											<%if(materialPurchaseOrder.isEdit()){ %>
												<%if(has_material_purchase_order_delete){ %>
													<a target="_blank" type="button"
													class="pull-right btn btn-default deleteTableBtn"
													data-loading-text="正在删除..."> 删除 </a>
												<%} %>
												<a href="material_purchase_order/put/<%=materialPurchaseOrder.getId() %>" target="_blank" type="button"
													class="pull-right btn btn-default"
													data-loading-text="正在跳转页面......"> 编辑 </a>
											<%} %>
											
											
											<a href="material_purchase_order/print/<%=materialPurchaseOrder.getId() %>" target="_blank" type="button"
												class="pull-right btn btn-success"
												data-loading-text="正在打印..."> 打印 </a>
											
											<div class="clear"></div>
											<div class="col-md-12 tablewidget">
												<table class="table noborder">
							<caption id="tablename">
								桐庐富伟针织厂原材料采购单
							</caption>
						</table>

						<table class="table tableTb noborder">
							<tbody>
								<tr>
									<td>

										供货单位：
										<span><%=materialPurchaseOrder == null ? ""
						: (SystemCache.getFactoryName(materialPurchaseOrder
								.getFactoryId()))%></span>

									</td>
									<td>
										业务员：
										<span><%=materialPurchaseOrder == null ? ""
						: (SystemCache.getEmployeeName((materialPurchaseOrder
								.getCharge_employee())))%></span>
									</td>
									<td class="pull-right">

										№：<%=materialPurchaseOrder.getNumber()%>

									</td>

								</tr>

								<tr>
									<td colspan="3">
										<table class="table table-responsive table-bordered">
											<tr>
												<td class="center" width="15%">
													公司
												</td>
												<td class="center" width="15%">
													货号
												</td>
												<td class="center" width="15%">
													客户
												</td>
												<td class="center" width="15%">
													品名
												</td>
											</tr>
											<tr>
												<td class="center">
													<span><%=SystemCache.getCompanyShortName(materialPurchaseOrder
								.getCompanyId())%></span>
												</td>
												<td class="center">
													<span><%=materialPurchaseOrder.getCompany_productNumber()%></span>
												</td>
												<td class="center">
													<span><%=SystemCache.getCustomerName(materialPurchaseOrder
								.getCustomerId())%></span>
												</td>
												<td class="center">
													<span><%=materialPurchaseOrder.getName()%></span>
												</td>
											</tr>
										</table>
									</td>
								</tr>

							</tbody>
						</table>

						<table class="noborder table">
							<tr>
								<td>
									<table class="table table-responsive table-bordered">

										<thead>
											<tr>
												<th width="15%">
													材料品种
												</th>
												<th width="15%">
													数量(kg)
												</th>
												<th width="15%">染厂</th>
												<th width="30%">
													备注
												</th>

											</tr>
										</thead>
										<tbody>
											<%
												for (MaterialPurchaseOrderDetail detail : materialPurchaseOrderDetailList) {
											%>
											<tr class="tr">
												<td class="material_name"><%=SystemCache.getMaterialName(detail
											.getMaterial())%>
												</td>
												<td class="quantity"><%=detail.getQuantity()%>
												</td>
												<td class="factory_name"><%=SystemCache.getFactoryName(detail.getFactoryId())%>
																		</td>
												<td class="memo"><%=detail.getMemo() == null ? "" : detail
							.getMemo()%>
												</td>
											</tr>

											<%
												}
													int i = materialPurchaseOrderDetailList.size();
													for (; i < 6; ++i) {
											%>
											<tr class="tr">
												<td class="material_name">
													&nbsp;
												</td>
												<td class="quantity">
												</td>
												<td class="factory_name">
												</td>
												<td class="memo">
												</td>
											</tr>
											<%
												}
											%>
										</tbody>
									</table>

									
								</td>
							</tr>
						</table>

						<p class="pull-right auto_bottom">
							<span id="created_user">制单人：<%=SystemCache.getUserName(materialPurchaseOrder
								.getCreated_user())%></span>
							<span id="receiver_user">收货人：</span>
							<span id="date"> 日期：<%=DateTool.formatDateYMD(materialPurchaseOrder
								.getCreated_at())%></span>
						</p>


											</div>
										</form>
									</div>
								</div>

								<%
									}
								%>
							
							</div>


							<!-- 染色单 -->

							<div class="tab-pane" id="coloringorder" role="tabpanel">
								<%if(has_coloring_order_save && !order.isDelivered()){ %>
								<div class="emptyrecordwidget">
									<p>
										如果您要创建染色单，请点击下方的按钮
									</p>
									<a href="coloring_order/add/<%=order.getId()%>"
										class="btn btn-primary" id="createProducingorderBtn">创建染色单</a>
								</div>
								<%} %>
								<a  href="printorder/print?orderId=<%=order.getId() %>&gridName=coloringorder" target="_blank" type="button"
												class="printBtn btn btn-success"
												data-loading-text="正在打印..."> 打印染色单 </a>
								<%
									for (ColoringOrder coloringOrder : coloringOrderList) {
										List<ColoringOrderDetail> coloringOrderDetailList = coloringOrder == null ? new ArrayList<ColoringOrderDetail>()
												: coloringOrder.getDetaillist();
								%>
								
								<div class="container-fluid coloringorderWidget">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="id"
												value="<%=coloringOrder == null ? "" : coloringOrder
						.getId()%>" />
											<input type="hidden" name="orderId"
												value="<%=order.getId()%>" />
											<%if(coloringOrder.isEdit()){ %>
												<%if(has_coloring_order_delete){ %>
													<a target="_blank" type="button"
													class="pull-right btn btn-default deleteTableBtn"
													data-loading-text="正在删除..."> 删除 </a>
												<%} %>
												<a href="coloring_order/put/<%=coloringOrder.getId() %>" target="_blank" type="button"
													class="pull-right btn btn-default"
													data-loading-text="正在跳转页面......"> 编辑 </a>
											<%} %>
											<a  href="coloring_order/print/<%=coloringOrder.getId() %>" target="_blank" type="button"
												class="pull-right btn btn-success"
												data-loading-text="正在打印..."> 打印 </a>
											<div class="clear"></div>
											<div class="col-md-12 tablewidget">
												<table class="table noborder">
							<caption id="tablename">
								桐庐富伟针织厂染色单
							</caption>

						</table>

						<table class="tableTb noborder table">
							<tbody>
								<tr>
									<td>
										供货单位：
										<span><%=coloringOrder == null ? ""
						: (SystemCache.getFactoryName(coloringOrder
								.getFactoryId()))%></span>

									</td>
									<td>
										业务员：
										<span><%=coloringOrder == null ? ""
						: (SystemCache.getEmployeeName((coloringOrder
								.getCharge_employee())))%></span>
									</td>
									<td class="pull-right">

										№：<%=coloringOrder.getNumber()%>

									</td>

								</tr>


								<tr>
									<td colspan="3">
										<table class="table table-responsive table-bordered">
											<tr>
												<td class="center" width="15%">
													公司
												</td>
												<td class="center" width="15%">
													货号
												</td>
												<td class="center" width="15%">
													客户
												</td>
												<td class="center" width="40%" colspan="2">
													品名
												</td>
											</tr>
											<tr>
												<td class="center">
													<span><%=SystemCache.getCompanyShortName(coloringOrder
								.getCompanyId())%></span>
												</td>
												<td class="center">
													<span><%=coloringOrder.getCompany_productNumber()%></span>
												</td>
												<td class="center">
													<span><%=SystemCache.getCustomerName(coloringOrder
								.getCustomerId())%></span>
												</td>
												<td class="center" width="30%">
													<span><%=coloringOrder.getName()%></span>
												</td>
												<td class="center" width="10%">备注</td>
												
											</tr>
									
											<tr class="tr">
												<td width="15%">
													色号
												</td>
												<td width="15%">
													材料
												</td>
												<td width="15%">
													数量(kg)
												</td>
												<td width="15%">
													标准样纱
												</td>
												<td class="center" rowspan="7">
													<span><%=coloringOrder.getMemo() == null ?"":coloringOrder.getMemo()%></span>
												</td>
											</tr>
									
											<%
												for (ColoringOrderDetail detail : coloringOrderDetailList) {
											%>
											<tr class="tr">
												<td class="color"><%=detail.getColor()%>
												</td>
												<td class="material_name"><%=SystemCache.getMaterialName(detail
											.getMaterial())%>
												</td>
												<td class="quantity"><%=detail.getQuantity()%>
												</td>
												<td class="standardyarn"><%=detail.getStandardyarn()%>
											</tr>

											<%
												}
													int i = coloringOrderDetailList.size();
													for (; i < 6; ++i) {
											%>
											<tr class="tr">
												<td class="color">
													&nbsp;
												</td>
												<td class="material_name">
												</td>
												<td class="quantity">
												</td>
												<td class="standardyarn">
											</tr>
											<%
												}
											%>

										</table>
									</td>
								</tr>

							</tbody>
						</table>
						<p class="pull-right auto_bottom">
							<span id="created_user">制单人：<%=SystemCache.getUserName(coloringOrder
								.getCreated_user())%></span>
							<span id="receiver_user">收货人：</span>
							<span id="date"> 日期：<%=DateTool.formatDateYMD(coloringOrder
										.getCreated_at())%></span>
						</p>

											</div>
										</form>
									</div>
								</div>
								<%
									}
								%>
							</div>



							<!-- 抽检记录单 -->
							<div class="tab-pane" id="checkrecordorder" role="tabpanel">
								<div class="container-fluid">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="orderId"
												value="<%=order.getId()%>" />
											
											<a target="_blank" type="button"
												class="pull-right btn btn-success printBtn"
												data-loading-text="正在打印..."> 打印 </a>
										</form>

										<div class="clear"></div>
										<div class="col-md-12 tablewidget">
											<table class="table">
												<caption>
													桐庐富伟针织厂抽检记录单
												</caption>
												<thead>
													<tr>
														<td colspan="3" class="pull-right orderNumber">
															№：<%=order.getOrderNumber()%></td>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td>
															<table
																class="table table-responsive table-bordered tableTb">
																<tbody>
																	<tr>
																		<td rowspan="8" width="50%">
																			<a href="/<%=order.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=order.getImg_s()%>">
																			</a>
																		</td>
																		<td width="20%">
																			生产单位
																		</td>
																		<td class="orderproperty"><%=productfactoryStr %></td>
																	</tr>

																	<tr>
																		<td colspan="2" class="center">
																			订单信息
																		</td>
																	</tr>
																	<tr>
																		<td>
																			公司
																		</td>
																		<td><%=SystemCache.getCompanyName(order.getCompanyId())%></td>
																	</tr>
																	<tr>
																		<td>
																			客户
																		</td>
																		<td><%=SystemCache.getCustomerName(order.getCustomerId())%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=order.getCompany_productNumber()%></td>
																	</tr>
																	<tr>
																		<td>
																			款名
																		</td>
																		<td><%=order.getName()%></td>
																	</tr>
																	<tr>
																		<td>
																			跟单
																		</td>
																		<td><%=SystemCache.getEmployeeName(order.getCharge_employee())%></td>
																	</tr>
																	<tr>
																		<td>
																			发货时间
																		</td>
																		<td><%=DateTool.formatDateYMD(order.getEnd_at())%></td>
																	</tr>
																</tbody>
															</table>

														</td>
													</tr>
													<tr>
														<td>
															<table class="table table-responsive detailTb">
																<caption>
																	颜色及数量
																</caption>
																<thead>
																	<tr>
																		<th width="15%">
																			颜色
																		</th>
																		<th width="15%">
																			克重(g)
																		</th>
																		<th width="15%">
																			纱线种类
																		</th>
																		<th width="15%">
																			尺寸
																		</th>
																		<th width="15%">
																			订单数量
																		</th>
																	</tr>
																</thead>
																<tbody>
																	<%
																		for (OrderDetail detail : DetailList) {
																	%>
																	<tr class="tr"
																		data='<%=SerializeTool.serialize(detail)%>'>
																		<td class="color"><%=detail.getColor()%>
																		</td>
																		<td class="weight"><%=detail.getWeight()%>
																		</td>
																		<td class="yarn_name"><%=SystemCache.getMaterialName(detail.getYarn())%>
																		</td>
																		<td class="size"><%=detail.getSize()%>
																		</td>
																		<td class="quantity"><%=detail.getQuantity()%>
																		</td>
																	</tr>

																	<%
																		}
																	%>

																</tbody>
															</table>
															<div id="navigator"></div>
														</td>
													</tr>
												</tbody>
											</table>

										</div>


									</div>
								</div>
								<!--
						 添加编辑抽检记录单对话框 -->

								<!-- 添加编辑抽检记录单对话框 -->

							</div>

							<!-- 辅料采购单 -->

							<div class="tab-pane" id="fuliaopurchaseorder" role="tabpanel">
								<%if( has_fuliaopurchase_order_save && !order.isDelivered()){ %>
								<div class="emptyrecordwidget">
									<p>
										如果您要创建辅料采购单，请点击下方的按钮
									</p>
									
									<a href="fuliao_purchase_order/add/<%=order.getId()%>"
										class="btn btn-primary" id="createProducingorderBtn">创建辅料采购单</a>
								</div>
								<%} %>
								<a  href="printorder/print?orderId=<%=order.getId() %>&gridName=fuliaopurchaseorder" target="_blank" type="button"
												class="printBtn btn btn-success"
												data-loading-text="正在打印..."> 打印辅料采购单 </a>
								<%
									for (FuliaoPurchaseOrder fuliaoPurchaseOrder : fuliaoPurchaseOrderList) {
										List<FuliaoPurchaseOrderDetail> fuliaoPurchaseOrderDetailList = fuliaoPurchaseOrder == null ? new ArrayList<FuliaoPurchaseOrderDetail>()
												: fuliaoPurchaseOrder.getDetaillist();
								%>
								<div class="container-fluid fuliaoorderWidget">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="id"
												value="<%=fuliaoPurchaseOrder == null ? ""
						: fuliaoPurchaseOrder.getId()%>" />
											<input type="hidden" name="orderId"
												value="<%=order.getId()%>" />
											<%if(fuliaoPurchaseOrder.isEdit()){ %>
												<%if(has_fuliao_purchase_order_delete){ %>
													<a target="_blank" type="button"
													class="pull-right btn btn-default deleteTableBtn"
													data-loading-text="正在删除..."> 删除 </a>
												<%} %>
												<a href="fuliao_purchase_order/put/<%=fuliaoPurchaseOrder.getId() %>" target="_blank" type="button"
													class="pull-right btn btn-default"
													data-loading-text="正在跳转页面......"> 编辑 </a>
											<%} %>
											<a href="fuliao_purchase_order/print/<%=fuliaoPurchaseOrder.getId() %>" target="_blank" type="button"
												class="pull-right btn btn-success"
												data-loading-text="正在打印..."> 打印 </a>
											<div class="clear"></div>
											<div class="col-md-12 tablewidget">
												<table class="table noborder">
							<caption id="tablename">
								桐庐富伟针织厂辅料采购单
							</caption>
						</table>

						<table class="table tableTb noborder">
							<tbody>
								<tr>
									<td>

										供货单位：
										<span><%=fuliaoPurchaseOrder == null ? ""
						: (SystemCache.getFactoryName(fuliaoPurchaseOrder
								.getFactoryId()))%></span>

									</td>
									<td>
										业务员：
										<span><%=fuliaoPurchaseOrder == null ? ""
						: (SystemCache.getEmployeeName((fuliaoPurchaseOrder
								.getCharge_employee())))%></span>
									</td>
									<td class="pull-right">

										№：<%=fuliaoPurchaseOrder.getNumber()%>

									</td>

								</tr>


								<tr>
									<td colspan="5">
										<table class="table table-bordered">
											<tr>
												<td class="center" width="15%">
													公司
												</td>
												<td class="center" width="15%">
													货号
												</td>
												<td class="center" width="15%">
													客户
												</td>
												<td class="center" width="15%">
													品名
												</td>
											</tr>
											<tr>
												<td class="center">
													<span><%=SystemCache.getCompanyShortName(fuliaoPurchaseOrder
								.getCompanyId())%></span>
												</td>
												<td class="center">
													<span><%=fuliaoPurchaseOrder.getCompany_productNumber()%></span>
												</td>
												<td class="center">
													<span><%=SystemCache.getCustomerName(fuliaoPurchaseOrder
								.getCustomerId())%></span>
												</td>
												<td class="center">
													<span><%=fuliaoPurchaseOrder.getName()%></span>
												</td>
											</tr>
										</table>
									</td>
								</tr>

							</tbody>
						</table>

						<table id="mainTb" class="noborder table">
							<tr>
								<td>
									<table class="table table-responsive table-bordered">

										<thead>
											<tr>
												<th width="15%">
													材料
												</th>
												<th width="15%">
													数量(kg)
												</th>
												<th width="30%">
													备注
												</th>
											</tr>
										</thead>
										<tbody>
											<%
												for (FuliaoPurchaseOrderDetail detail : fuliaoPurchaseOrderDetailList) {
											%>
											<tr class="tr">
												<td class="style_name"><%=SystemCache.getMaterialName(detail.getStyle())%>
												</td>
												<td class="quantity"><%=detail.getQuantity()%>
												</td>
												<td class="memo"><%=detail.getMemo() == null ? "" : detail
							.getMemo()%>
												</td>
											</tr>

											<%
												}
													int i = fuliaoPurchaseOrderDetailList.size();
													for (; i < 6; ++i) {
											%>
											<tr>
												<td class="style_name">
													&nbsp;
												</td>
												<td class="quantity">
												</td>
												<td class="memo">
												</td>
											</tr>
											<%
												}
											%>
										</tbody>
									</table>
								</td>
							</tr>
						</table>

						<p class="pull-right auto_bottom">
							<span id="created_user">制单人：<%=SystemCache.getUserName(fuliaoPurchaseOrder
								.getCreated_user())%></span>
							<span id="receiver_user">收货人：</span>
							<span id="date"> 日期：<%=DateTool.formatDateYMD(fuliaoPurchaseOrder
								.getCreated_at())%></span>
						</p>
												

											</div>
										</form>




									</div>
								</div>
								<%
									}
								%>

							</div>


							<!-- 车缝记录单 -->
							<div class="tab-pane" id="carfixrecordorder" role="tabpanel">
								<div class="container-fluid">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="orderId"
												value="<%=order.getId()%>" />
											
											<a target="_blank" type="button"
												class="pull-right btn btn-success printBtn"
												data-loading-text="正在打印..."> 打印 </a>
										</form>

										<div class="clear"></div>
										<div class="col-md-12 tablewidget">
											<table class="table">
												<caption>
													桐庐富伟针织厂车缝记录单
												</caption>
												<thead>
													<tr>
														<td colspan="3" class="pull-right orderNumber">
															№：<%=order.getOrderNumber()%></td>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td>
															<table
																class="table table-responsive table-bordered tableTb">
																<tbody>
																	<tr>
																		<td rowspan="8" width="50%">
																			<a href="/<%=order.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=order.getImg_s()%>">
																			</a>
																		</td>
																		<td colspan="2" class="center">
																			订单信息
																		</td>
																	</tr>

																	<tr>
																		<td>
																			公司
																		</td>
																		<td><%=SystemCache.getCompanyName(order.getCompanyId())%></td>
																	</tr>
																	<tr>
																		<td>
																			客户
																		</td>
																		<td><%=SystemCache.getCustomerName(order.getCustomerId())%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=order.getCompany_productNumber()%></td>
																	</tr>
																	<tr>
																		<td>
																			款名
																		</td>
																		<td><%=order.getName()%></td>
																	</tr>
																	<tr>
																		<td>
																			跟单
																		</td>
																		<td><%=SystemCache.getEmployeeName(order.getCharge_employee())%></td>
																	</tr>
																	<tr>
																		<td>
																			发货时间
																		</td>
																		<td><%=DateTool.formatDateYMD(order.getEnd_at())%></td>
																	</tr>
																</tbody>
															</table>

														</td>
													</tr>
													<tr>
														<td>
															<table class="table table-responsive detailTb">
																<caption>
																	颜色及数量
																</caption>
																<thead>
																	<tr>
																		<th width="15%">
																			颜色
																		</th>
																		<th width="15%">
																			克重(g)
																		</th>
																		<th width="15%">
																			纱线种类
																		</th>
																		<th width="15%">
																			尺寸
																		</th>
																		<th width="15%">
																			生产数量
																		</th>
																	</tr>
																</thead>
																<tbody>
																	<%
																		for (PlanOrderDetail detail : planOrderDetailList) {
																	%>
																	<tr class="tr"
																		data='<%=SerializeTool.serialize(detail)%>'>
																		<td class="color"><%=detail.getColor()%>
																		</td>
																		<td class="weight"><%=detail.getWeight()%>
																		</td>
																		<td class="yarn_name"><%=SystemCache.getMaterialName(detail.getYarn())%>
																		</td>
																		<td class="size"><%=detail.getSize()%>
																		</td>
																		<td class="quantity"><%=detail.getQuantity()%>
																		</td>
																	</tr>

																	<%
																		}
																	%>

																</tbody>
															</table>
															<div id="navigator"></div>
														</td>
													</tr>
												</tbody>
											</table>

										</div>


									</div>
								</div>
								<!--
						 添加编辑车缝记录单对话框 -->
								<!-- 添加编辑车缝记录单对话框 -->

							</div>


							<!-- 整烫记录单 -->
							<div class="tab-pane" id="ironingrecordorder" role="tabpanel">
								<div class="container-fluid">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="orderId"
												value="<%=order.getId()%>" />
											
											<a target="_blank" type="button"
												class="pull-right btn btn-success printBtn"
												data-loading-text="正在打印..."> 打印 </a>
										</form>

										<div class="clear"></div>
										<div class="col-md-12 tablewidget">
											<table class="table">
												<caption>
													桐庐富伟针织厂整烫记录单
												</caption>
												<thead>
													<tr>
														<td colspan="3" class="pull-right orderNumber">
															№：<%=order.getOrderNumber()%></td>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td>
															<table
																class="table table-responsive table-bordered tableTb">
																<tbody>
																	<tr>
																		<td rowspan="8" width="50%">
																			<a href="/<%=order.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=order.getImg_s()%>">
																			</a>
																		</td>
																		<td colspan="2" class="center">
																			订单信息
																		</td>
																	</tr>

																	<tr>
																		<td>
																			公司
																		</td>
																		<td><%=SystemCache.getCompanyName(order.getCompanyId())%></td>
																	</tr>
																	<tr>
																		<td>
																			客户
																		</td>
																		<td><%=SystemCache.getCustomerName(order.getCustomerId())%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=order.getCompany_productNumber()%></td>
																	</tr>
																	<tr>
																		<td>
																			款名
																		</td>
																		<td><%=order.getName()%></td>
																	</tr>
																	<tr>
																		<td>
																			跟单
																		</td>
																		<td><%=SystemCache.getEmployeeName(order.getCharge_employee())%></td>
																	</tr>
																	<tr>
																		<td>
																			发货时间
																		</td>
																		<td><%=DateTool.formatDateYMD(order.getEnd_at())%></td>
																	</tr>
																</tbody>
															</table>

														</td>
													</tr>
													<tr>
														<td>
															<table class="table table-responsive detailTb">
																<caption>
																	颜色及数量
																</caption>
																<thead>
																	<tr>
																		<th width="15%">
																			颜色
																		</th>
																		<th width="15%">
																			克重(g)
																		</th>
																		<th width="15%">
																			纱线种类
																		</th>
																		<th width="15%">
																			尺寸
																		</th>
																		<th width="15%">
																			生产数量
																		</th>
																	</tr>
																</thead>
																<tbody>
																	<%
																		for (PlanOrderDetail detail : planOrderDetailList) {
																	%>
																	<tr class="tr"
																		data='<%=SerializeTool.serialize(detail)%>'>
																		<td class="color"><%=detail.getColor()%>
																		</td>
																		<td class="weight"><%=detail.getWeight()%>
																		</td>
																		<td class="yarn_name"><%=SystemCache.getMaterialName(detail.getYarn())%>
																		</td>
																		<td class="size"><%=detail.getSize()%>
																		</td>
																		<td class="quantity"><%=detail.getQuantity()%>
																		</td>
																	</tr>

																	<%
																		}
																	%>

																</tbody>
															</table>
															<div id="navigator"></div>
														</td>
													</tr>
												</tbody>
											</table>

										</div>


									</div>
								</div>
								<!--
						 添加编辑整烫记录单对话框 -->

								<!-- 添加编辑整烫记录单对话框 -->

							</div>

							<!-- 2015-3-23添加生产进度单 -->
							<div class="tab-pane" id="productionscheduleorder"
								role="tabpanel">
								<div class="container-fluid">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="orderId"
												value="<%=order.getId()%>" />
											
											<a target="_blank" type="button"
												class="pull-right btn btn-success printBtn"
												data-loading-text="正在打印..."> 打印 </a>
										</form>

										<div class="clear"></div>
										<div class="col-md-12 tablewidget">
											<table class="table">
												<caption>
													桐庐富伟针织厂生产进度单
												</caption>
												<thead>
													<tr>
														<td colspan="3" class="pull-right orderNumber">
															№：<%=order.getOrderNumber()%></td>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td>
															<table
																class="table table-responsive table-bordered tableTb">
																<tbody>
																	<tr>
																		<td rowspan="<%=7+DetailList.size() %>" width="50%">
																			<a href="/<%=order.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=order.getImg_s()%>">
																			</a>
																		</td>
																		<td>
																			公司
																		</td>
																		<td><%=SystemCache.getCompanyName(order.getCompanyId())%></td>
																	</tr>


																	<tr>
																		<td>
																			客户
																		</td>
																		<td><%=SystemCache.getCustomerName(order.getCustomerId())%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=order.getCompany_productNumber()%></td>
																	</tr>
																	<tr>
																		<td>
																			款名
																		</td>
																		<td><%=order.getName()%></td>
																	</tr>
																	<tr>
																		<td>
																			跟单
																		</td>
																		<td><%=SystemCache.getEmployeeName(order.getCharge_employee())%></td>
																	</tr>
																	<tr>
																		<td>
																			发货时间
																		</td>
																		<td><%=DateTool.formatDateYMD(order.getEnd_at())%></td>
																	</tr>
																	<tr>
																		<td colspan="2" class="center">
																			订单数量
																		</td>
																	</tr>
																	<%
																		for (OrderDetail detail : DetailList) {
																	%>
																	<tr>
																		<td><%=detail.getColor()%></td>
																		<td><%=detail.getQuantity()%></td>
																	</tr>
																	<%
																		}
																	%>
																</tbody>
															</table>

														</td>
													</tr>
													<tr>
														
													</tr>
												</tbody>
											</table>

										</div>


									</div>
								</div>
								<!--
						 添加编辑生产进度单对话框 -->

								<!-- 添加编辑生产进度单对话框 -->

							</div>

							<!-- 成品仓库记录单 -->
							<div class="tab-pane" id="finalstorerecordorder" role="tabpanel">
								<div class="container-fluid">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="orderId"
												value="<%=order.getId()%>" />
											
											<a target="_blank" type="button"
												class="pull-right btn btn-success printBtn"
												data-loading-text="正在打印..."> 打印 </a>
										</form>

										<div class="clear"></div>
										<div class="col-md-12 tablewidget">
											<table class="table">
												<caption>
													桐庐富伟针织厂成品仓库记录单
												</caption>
												<thead>
													<tr>
														<td colspan="3" class="pull-right orderNumber">
															№：<%=order.getOrderNumber()%></td>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td>
															<table
																class="table table-responsive table-bordered tableTb">
																<tbody>
																	<tr>
																		<td rowspan="8" width="50%">
																			<a href="/<%=order.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=order.getImg_s()%>">
																			</a>
																		</td>
																		<td width="20%">
																			生产单位
																		</td>
																		<td class="orderproperty"><%=productfactoryStr %></td>
																	</tr>

																	<tr>
																		<td colspan="2" class="center">
																			订单信息
																		</td>
																	</tr>
																	<tr>
																		<td>
																			公司
																		</td>
																		<td><%=SystemCache.getCompanyName(order.getCompanyId())%></td>
																	</tr>
																	<tr>
																		<td>
																			客户
																		</td>
																		<td><%=SystemCache.getCustomerName(order.getCustomerId())%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=order.getCompany_productNumber()%></td>
																	</tr>
																	<tr>
																		<td>
																			款名
																		</td>
																		<td><%=order.getName()%></td>
																	</tr>
																	<tr>
																		<td>
																			跟单
																		</td>
																		<td><%=SystemCache.getEmployeeName(order.getCharge_employee())%></td>
																	</tr>
																	<tr>
																		<td>
																			发货时间
																		</td>
																		<td><%=DateTool.formatDateYMD(order.getEnd_at())%></td>
																	</tr>
																</tbody>
															</table>

														</td>
													</tr>
													<tr>
														<td>
															<table class="table table-responsive detailTb">
																<caption>
																	颜色及数量
																</caption>
																<thead>
																	<tr>
																		<th width="15%">
																			颜色
																		</th>
																		<th width="15%">
																			克重(g)
																		</th>
																		<th width="15%">
																			纱线种类
																		</th>
																		<th width="15%">
																			尺寸
																		</th>
																		<th width="15%">
																			生产数量
																		</th>
																	</tr>
																</thead>
																<tbody>
																	<%
																		for (PlanOrderDetail detail : planOrderDetailList) {
																	%>
																	<tr class="tr"
																		data='<%=SerializeTool.serialize(detail)%>'>
																		<td class="color"><%=detail.getColor()%>
																		</td>
																		<td class="weight"><%=detail.getWeight()%>
																		</td>
																		<td class="yarn_name"><%=SystemCache.getMaterialName(detail.getYarn())%>
																		</td>
																		<td class="size"><%=detail.getSize()%>
																		</td>
																		<td class="quantity"><%=detail.getQuantity()%>
																		</td>
																	</tr>

																	<%
																		}
																	%>

																</tbody>
															</table>
															<div id="navigator"></div>
														</td>
													</tr>
												</tbody>
											</table>

										</div>


									</div>
								</div>
								<!--
						 添加编辑抽检记录单对话框 -->

								<!-- 添加编辑抽检记录单对话框 -->

							</div>

						<!-- 成品检验记录单 -->
							<div class="tab-pane" id="finalcheckrecordorder" role="tabpanel">
								<div class="container-fluid">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="orderId"
												value="<%=order.getId()%>" />
											
											<a target="_blank" type="button"
												class="pull-right btn btn-success printBtn"
												data-loading-text="正在打印..."> 打印 </a>
										</form>

										<div class="clear"></div>
										<div class="col-md-12 tablewidget">
											<table class="table">
												<caption>
													桐庐富伟针织厂成品检验记录单
												</caption>
												<thead>
													<tr>
														<td colspan="3" class="pull-right orderNumber">
															№：<%=order.getOrderNumber()%></td>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td>
															<table
																class="table table-responsive table-bordered tableTb">
																<tbody>
																	<tr>
																		<td rowspan="8" width="50%">
																			<a href="/<%=order.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=order.getImg_s()%>">
																			</a>
																		</td>
																		<td width="20%">
																			生产单位
																		</td>
																		<td class="orderproperty"><%=productfactoryStr %></td>
																	</tr>

																	<tr>
																		<td colspan="2" class="center">
																			订单信息
																		</td>
																	</tr>
																	<tr>
																		<td>
																			公司
																		</td>
																		<td><%=SystemCache.getCompanyName(order.getCompanyId())%></td>
																	</tr>
																	<tr>
																		<td>
																			客户
																		</td>
																		<td><%=SystemCache.getCustomerName(order.getCustomerId())%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=order.getCompany_productNumber()%></td>
																	</tr>
																	<tr>
																		<td>
																			款名
																		</td>
																		<td><%=order.getName()%></td>
																	</tr>
																	<tr>
																		<td>
																			跟单
																		</td>
																		<td><%=SystemCache.getEmployeeName(order.getCharge_employee())%></td>
																	</tr>
																	<tr>
																		<td>
																			发货时间
																		</td>
																		<td><%=DateTool.formatDateYMD(order.getEnd_at())%></td>
																	</tr>
																</tbody>
															</table>

														</td>
													</tr>
													<tr>
														<td>
															<table class="table table-responsive detailTb">
																<caption>
																	颜色及数量
																</caption>
																<thead>
																	<tr>
																		<th width="15%">
																			颜色
																		</th>
																		<th width="15%">
																			克重(g)
																		</th>
																		<th width="15%">
																			纱线种类
																		</th>
																		<th width="15%">
																			尺寸
																		</th>
																		<th width="15%">
																			生产数量
																		</th>
																	</tr>
																</thead>
																<tbody>
																	<%
																		for (PlanOrderDetail detail : planOrderDetailList) {
																	%>
																	<tr class="tr"
																		data='<%=SerializeTool.serialize(detail)%>'>
																		<td class="color"><%=detail.getColor()%>
																		</td>
																		<td class="weight"><%=detail.getWeight()%>
																		</td>
																		<td class="yarn_name"><%=SystemCache.getMaterialName(detail.getYarn())%>
																		</td>
																		<td class="size"><%=detail.getSize()%>
																		</td>
																		<td class="quantity"><%=detail.getQuantity()%>
																		</td>
																	</tr>

																	<%
																		}
																	%>

																</tbody>
															</table>
															<div id="navigator"></div>
														</td>
													</tr>
												</tbody>
											</table>

										</div>


									</div>
								</div>
							</div>

							<!-- 检针记录表 -->
							<div class="tab-pane" id="needlecheckrecordorder" role="tabpanel">
									<div class="container-fluid">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="orderId"
												value="<%=order.getId()%>" />
											
											<a target="_blank" type="button"
												class="pull-right btn btn-success printBtn"
												data-loading-text="正在打印..."> 打印 </a>
										</form>

										<div class="clear"></div>
										<div class="col-md-12 tablewidget">
											<table class="table">
												<caption>
													桐庐富伟针织厂检针记录表
												</caption>
												<thead>
													<tr>
														<td colspan="3" class="pull-right orderNumber">
															№：<%=order.getOrderNumber()%></td>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td>
															<table
																class="table table-responsive table-bordered tableTb">
																<tbody>
																	<tr>
																		<td rowspan="<%=7+DetailList.size() %>" width="50%">
																			<a href="/<%=order.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=order.getImg_s()%>">
																			</a>
																		</td>
																		<td>
																			公司
																		</td>
																		<td><%=SystemCache.getCompanyName(order.getCompanyId())%></td>
																	</tr>


																	<tr>
																		<td>
																			客户
																		</td>
																		<td><%=SystemCache.getCustomerName(order.getCustomerId())%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=order.getCompany_productNumber()%></td>
																	</tr>
																	<tr>
																		<td>
																			款名
																		</td>
																		<td><%=order.getName()%></td>
																	</tr>
																	<tr>
																		<td>
																			跟单
																		</td>
																		<td><%=SystemCache.getEmployeeName(order.getCharge_employee())%></td>
																	</tr>
																	<tr>
																		<td>
																			发货时间
																		</td>
																		<td><%=DateTool.formatDateYMD(order.getEnd_at())%></td>
																	</tr>
																	<tr>
																		<td colspan="2" class="center">
																			订单数量
																		</td>
																	</tr>
																	<%
																		for (OrderDetail detail : DetailList) {
																	%>
																	<tr>
																		<td><%=detail.getColor()%></td>
																		<td><%=detail.getQuantity()%></td>
																	</tr>
																	<%
																		}
																	%>
																</tbody>
															</table>

														</td>
													</tr>
													<tr>
														
													</tr>
												</tbody>
											</table>

										</div>


									</div>
								</div>
							</div>
							<!-- 车间记录单 -->
							<div class="tab-pane" id="shoprecordorder" role="tabpanel">
								<div class="container-fluid">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="orderId"
												value="<%=order.getId()%>" />
											
											<a target="_blank" type="button"
												class="pull-right btn btn-success printBtn"
												data-loading-text="正在打印..."> 打印 </a>
										</form>

										<div class="clear"></div>
										<div class="col-md-12 tablewidget">
											<table class="table">
												<caption>
													桐庐富伟针织厂车间记录单
												</caption>
												<thead>
													<tr>
														<td colspan="3" class="pull-right orderNumber">
															№：<%=order.getOrderNumber()%></td>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td>
															<table
																class="table table-responsive table-bordered tableTb">
																<tbody>
																	<tr>
																		<td rowspan="8" width="50%">
																			<a href="/<%=order.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=order.getImg_s()%>">
																			</a>
																		</td>
																		<td width="20%">
																			生产单位
																		</td>
																		<td class="orderproperty"><%=productfactoryStr %></td>
																	</tr>

																	<tr>
																		<td colspan="2" class="center">
																			订单信息
																		</td>
																	</tr>
																	<tr>
																		<td>
																			公司
																		</td>
																		<td><%=SystemCache.getCompanyName(order.getCompanyId())%></td>
																	</tr>
																	<tr>
																		<td>
																			客户
																		</td>
																		<td><%=SystemCache.getCustomerName(order.getCustomerId())%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=order.getCompany_productNumber()%></td>
																	</tr>
																	<tr>
																		<td>
																			款名
																		</td>
																		<td><%=order.getName()%></td>
																	</tr>
																	<tr>
																		<td>
																			跟单
																		</td>
																		<td><%=SystemCache.getEmployeeName(order.getCharge_employee())%></td>
																	</tr>
																	<tr>
																		<td>
																			发货时间
																		</td>
																		<td><%=DateTool.formatDateYMD(order.getEnd_at())%></td>
																	</tr>
																</tbody>
															</table>

														</td>
													</tr>
													<tr>
														<td>
															<table class="table table-responsive detailTb">
																<caption>
																	颜色及数量
																</caption>
																<thead>
																	<tr>
																		<th width="15%">
																			颜色
																		</th>
																		<th width="15%">
																			克重(g)
																		</th>
																		<th width="15%">
																			纱线种类
																		</th>
																		<th width="15%">
																			尺寸
																		</th>
																		<th width="15%">
																			生产数量
																		</th>
																	</tr>
																</thead>
																<tbody>
																	<%
																		for (PlanOrderDetail detail : planOrderDetailList) {
																	%>
																	<tr class="tr"
																		data='<%=SerializeTool.serialize(detail)%>'>
																		<td class="color"><%=detail.getColor()%>
																		</td>
																		<td class="weight"><%=detail.getWeight()%>
																		</td>
																		<td class="yarn_name"><%=SystemCache.getMaterialName(detail.getYarn())%>
																		</td>
																		<td class="size"><%=detail.getSize()%>
																		</td>
																		<td class="quantity"><%=detail.getQuantity()%>
																		</td>
																	</tr>

																	<%
																		}
																	%>

																</tbody>
															</table>
															<div id="navigator"></div>
														</td>
													</tr>
												</tbody>
											</table>

										</div>


									</div>
								</div>
								<!--
						 添加编辑抽检记录单对话框 -->

								<!-- 添加编辑抽检记录单对话框 -->

							</div>

							<!-- 染色进度单 -->
							<div class="tab-pane" id="coloringprocessorder"
								role="tabpanel">
								<div class="container-fluid">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="orderId"
												value="<%=order.getId()%>" />
											
											<a target="_blank" type="button"
												class="pull-right btn btn-success printBtn"
												data-loading-text="正在打印..."> 打印 </a>
										</form>

										<div class="clear"></div>
										<div class="col-md-12 tablewidget">
											<table class="table">
												<caption>
													桐庐富伟针织厂染色进度单
												</caption>
												<thead>
													<tr>
														<td colspan="3" class="pull-right orderNumber">
															№：<%=order.getOrderNumber()%></td>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td>
															<table
																class="table table-responsive table-bordered tableTb">
																<tbody>
																	<tr>
																		<td rowspan="6" width="50%">
																			<a href="/<%=order.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=order.getImg_s()%>">
																			</a>
																		</td>
																		<td>
																			公司
																		</td>
																		<td><%=SystemCache.getCompanyName(order.getCompanyId())%></td>
																	</tr>


																	<tr>
																		<td>
																			客户
																		</td>
																		<td><%=SystemCache.getCustomerName(order.getCustomerId())%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=order.getCompany_productNumber()%></td>
																	</tr>
																	<tr>
																		<td>
																			款名
																		</td>
																		<td><%=order.getName()%></td>
																	</tr>
																	<tr>
																		<td>
																			跟单
																		</td>
																		<td><%=SystemCache.getEmployeeName(order.getCharge_employee())%></td>
																	</tr>
																	<tr>
																		<td>
																			发货时间
																		</td>
																		<td><%=DateTool.formatDateYMD(order.getEnd_at())%></td>
																	</tr>
																	
																</tbody>
															</table>

														</td>
													</tr>
													<tr>
														<td>
															<table class="table table-responsive detailTb">
																<caption>
																	纱线信息
																</caption>
																<thead>
																	<tr>
																		<th width="15%">
																			材料
																		</th>
																		<th width="15%">
																			颜色
																		</th>
																		<th width="15%">
																			数量(kg)
																		</th>
																		<th width="15%">
																			染色单位
																		</th>
																	</tr>
																</thead>
																<tbody>
																	<%
																		for (ColoringProcessOrderDetail detail : coloringProcessOrderDetailList) {
																	%>
																	<tr class="tr"
																		data='<%=SerializeTool.serialize(detail)%>'>
																		<td class="material_name"><%=SystemCache.getMaterialName(detail.getMaterial())%>
																		</td>
																		<td class="color"><%=detail.getColor()%>
																		</td>
																		<td class="quantity"><%=detail.getQuantity()%>
																		</td>
																		<td class="factory_name"><%=SystemCache.getFactoryName(detail.getFactoryId())%>
																		</td>
																	</tr>

																	<%
																		}
																	%>

																</tbody>
															</table>
															<div id="navigator"></div>
														</td>
													</tr>
												</tbody>
											</table>

										</div>


									</div>
								</div>
								<!--
						 添加编辑染色进度单对话框 -->

								<!-- 添加编辑染色进度单对话框 -->

							</div>				

						</div>


					</div>
				</div>


			</div>
		</div>
	</body>
</html>