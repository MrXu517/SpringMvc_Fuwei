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
<%@page import="com.fuwei.entity.ordergrid.StoreOrderDetail2"%>
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
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Order order = (Order) request.getAttribute("order");
	HeadBankOrder headBankOrder = (HeadBankOrder) request
			.getAttribute("headBankOrder");

	List<ProducingOrder> producingOrderList = (List<ProducingOrder>) request
			.getAttribute("producingOrderList");
	producingOrderList = producingOrderList == null ? new ArrayList<ProducingOrder>()
			: producingOrderList;

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
	CheckRecordOrder checkRecordOrder = (CheckRecordOrder) request
			.getAttribute("checkRecordOrder");

	//车缝记录单
	CarFixRecordOrder carFixRecordOrder = (CarFixRecordOrder) request
			.getAttribute("carFixRecordOrder");

	//整烫记录单
	IroningRecordOrder ironingRecordOrder = (IroningRecordOrder) request
			.getAttribute("ironingRecordOrder");

	//2015-3-23添加新表格 生产进度单
	ProductionScheduleOrder productionScheduleOrder = (ProductionScheduleOrder) request
			.getAttribute("productionScheduleOrder");
	FinalStoreOrder finalStoreOrder = (FinalStoreOrder) request
			.getAttribute("finalStoreOrder");
	ShopRecordOrder shopRecordOrder = (ShopRecordOrder) request
			.getAttribute("shopRecordOrder");
	ColoringProcessOrder coloringProcessOrder = (ColoringProcessOrder) request
			.getAttribute("coloringProcessOrder");
	List<ColoringProcessOrderDetail> coloringProcessOrderDetailList = coloringProcessOrder == null ? new ArrayList<ColoringProcessOrderDetail>()
			: coloringProcessOrder.getDetaillist();
			
	List<OrderDetail> DetailList = order == null ? new ArrayList<OrderDetail>()
			: order.getDetaillist();
	if (DetailList == null) {
		DetailList = new ArrayList<OrderDetail>();
	}
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
					<a href="printorder/print?orderId=<%=order.getId()%>"
						target="_blank" type="button" class="btn btn-success printAll"
						data-loading-text="正在打印..."> 打印所有的表格 </a>
					<div class="clear"></div>

					<div id="tab">
						<ul class="nav nav-pills nav-stacked" role="tablist">
							<li>
								<a href="#planorder" role="tab" data-toggle="tab">计划单</a>
							</li>
							<li>
								<a href="#producingorder" role="tab" data-toggle="tab">生产单</a>
							</li>
							<li>
								<a href="#coloringprocessorder" role="tab" data-toggle="tab">染色进度单</a>
							</li>
							<li>
								<a href="#productionscheduleorder" role="tab" data-toggle="tab">生产进度单</a>
							</li>
							<li>
								<a href="#finalstorerecordorder" role="tab" data-toggle="tab">成品仓库记录单</a>
							</li>
							<li>
								<a href="#materialpurchaseorder" role="tab" data-toggle="tab">原材料采购单</a>
							</li>
							<li>
								<a href="#fuliaopurchaseorder" role="tab" data-toggle="tab">辅料采购单</a>
							</li>
							<li>
								<a href="#coloringorder" role="tab" data-toggle="tab">染色单</a>
							</li>
							<li>
								<a href="#storeorder" role="tab" data-toggle="tab">原材料仓库</a>
							</li>
							<li>
								<a href="#shoprecordorder" role="tab" data-toggle="tab">车间记录单</a>
							</li>
							<li>
								<a href="#halfcheckrecordorder" role="tab" data-toggle="tab">半检记录单</a>
							</li>
							<li>
								<a href="#checkrecordorder" role="tab" data-toggle="tab">抽检记录单</a>
							</li>
							<li>
								<a href="#headbankorder" role="tab" data-toggle="tab">质量记录单</a>
							</li>
							<li>
								<a href="#carfixrecordorder" role="tab" data-toggle="tab">车缝记录单</a>
							</li>
							<li>
								<a href="#ironingrecordorder" role="tab" data-toggle="tab">整烫记录单</a>
							</li>
						</ul>


						<div class="tab-content">
							<!-- 质量记录单  -->
							<div class="tab-pane" id="headbankorder" role="tabpanel">
								<div class="container-fluid">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="id"
												value="<%=headBankOrder == null ? "" : headBankOrder.getId()%>" />
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
																		<td rowspan="7" width="50%">
																			<a href="/<%=order.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=order.getImg_s()%>">
																			</a>
																		</td>
																		<td width="20%">
																			生产单位
																		</td>
																		<td class="orderproperty"></td>
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
																		<td><%=order.getKehu()%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=order.getProductNumber()%></td>
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
																		<td><%=SystemCache.getUserName(order.getCharge_user())%></td>
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

							<!-- 生产单  -->
							<div class="tab-pane" id="producingorder" role="tabpanel">

								<div class="emptyrecordwidget">
									<p>
										如果您要创建生产单，请点击下方的按钮
									</p>
									<a href="order/<%=order.getId()%>/addproducingorder"
										class="btn btn-primary" id="createProducingorderBtn">创建生产单</a>
								</div>

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
											<button type="submit"
												class="pull-right btn btn-danger saveTable"
												data-loading-text="正在保存...">
												保存对当前表格的修改
											</button>
											<a target="_blank" type="button"
												class="pull-right btn btn-success printBtn"
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
																		<td><%=order.getKehu()%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=order.getProductNumber()%></td>
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
																		<td><%=SystemCache.getUserName(order.getCharge_user())%></td>
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
																		<th width="15%">
																			价格(/个)
																		</th>

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
																		<td class="weight"><%=detail.getWeight()%>
																		</td>
																		<td class="yarn_name"><%=SystemCache.getMaterialName(detail.getYarn())%>
																		</td>
																		<td class="size"><%=detail.getSize()%>
																		</td>
																		<td class="int">
																			<input class="form-control require quantity value"
																				value="<%=detail.getQuantity()%>" />
																		</td>
																		<td class="double">
																			<input class="form-control require price value"
																				value="<%=detail.getPrice()%>" />
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
																	<button type="button"
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
																		<th width="20%">
																			数量
																		</th>
																		<th width="25%">
																			标准色样
																		</th>
																		<th width="15%">
																			操作
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
																		<td class="_handle">
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
								<%
									}
								%>
								<div class="modal fade tableRowDialog"
									id="producingDetailDialog">
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
														<label for="quantity" class="col-sm-3 control-label">
															数量
														</label>
														<div class="col-sm-8">
															<input type="text" name="quantity" id="quantity"
																class="form-control int require" />
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



							<!-- 计划单  -->
							<div class="tab-pane" id="planorder" role="tabpanel">
								<div class="container-fluid">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="id"
												value="<%=planOrder == null ? "" : planOrder.getId()%>" />
											<input type="hidden" name="orderId"
												value="<%=order.getId()%>" />
											<button type="submit"
												class="pull-right btn btn-danger saveTable"
												data-loading-text="正在保存...">
												保存对当前表格的修改
											</button>
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
																		<td rowspan="7" width="50%">
																			<a href="/<%=order.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=order.getImg_s()%>">
																			</a>
																		</td>
																		<td width="20%">
																			生产单位
																		</td>
																		<td class="orderproperty"></td>
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
																		<td><%=order.getKehu()%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=order.getProductNumber()%></td>
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
																		<td><%=SystemCache.getUserName(order.getCharge_user())%></td>
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
																		<td class="int">
																			<input type="text"
																				class="form-control quantity value"
																				value="<%=detail.getQuantity()%>" />
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

							<!-- 原材料仓库单  -->
							<div class="tab-pane" id="storeorder" role="tabpanel">
								<div class="container-fluid">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="id"
												value="<%=storeOrder == null ? "" : storeOrder.getId()%>" />
											<input type="hidden" name="orderId"
												value="<%=order.getId()%>" />
											<button type="submit"
												class="pull-right btn btn-danger saveTable"
												data-loading-text="正在保存...">
												保存对当前表格的修改
											</button>
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
																		<td rowspan="7" width="50%">
																			<a href="/<%=order.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=order.getImg_s()%>">
																			</a>
																		</td>
																		<td width="20%">
																			生产单位
																		</td>
																		<td class="orderproperty"></td>
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
																		<td><%=order.getKehu()%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=order.getProductNumber()%></td>
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
																		<td><%=SystemCache.getUserName(order.getCharge_user())%></td>
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
																		class="btn btn-primary addRow pull-left">
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
																			总数量
																		</th>
																		<th width="15%">
																			领取人
																		</th>
																		<th width="15%">
																			标准样纱
																		</th>
																		<th width="15%">
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
																		<td class="_handle">
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
															总数量
														</label>
														<div class="col-sm-8">
															<input type="text" name="quantity" id="quantity"
																class="form-control int require" />
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
											<button type="submit"
												class="pull-right btn btn-danger saveTable"
												data-loading-text="正在保存...">
												保存对当前表格的修改
											</button>
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
																		<td rowspan="7" width="50%">
																			<a href="/<%=order.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=order.getImg_s()%>">
																			</a>
																		</td>
																		<td width="20%">
																			生产单位
																		</td>
																		<td class="orderproperty"></td>
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
																		<td><%=order.getKehu()%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=order.getProductNumber()%></td>
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
																		<td><%=SystemCache.getUserName(order.getCharge_user())%></td>
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

													<tr>
														<td>
															<table class="table table-responsive detailTb2">
																<caption>
																	<button type="button"
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
																		<th width="15%">
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
																		<td class="_handle">
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
								<div class="emptyrecordwidget">
									<p>
										如果您要创建原材料采购单，请点击下方的按钮
									</p>
									<a href="material_purchase_order/add/<%=order.getId()%>"
										class="btn btn-primary" id="createProducingorderBtn">创建原材料采购单</a>
								</div>

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
											<button type="submit"
												class="pull-right btn btn-danger saveTable"
												data-loading-text="正在保存...">
												保存对当前表格的修改
											</button>
											<a target="_blank" type="button"
												class="pull-right btn btn-success printBtn"
												data-loading-text="正在打印..."> 打印 </a>
											<div class="clear"></div>
											<div class="col-md-12 tablewidget">
												<table class="table">
													<caption>
														桐庐富伟针织厂原材料采购单
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
																			<td rowspan="7" width="50%">
																				<a href="/<%=order.getImg()%>" class="thumbnail"
																					target="_blank"> <img id="previewImg"
																						alt="200 x 100%" src="/<%=order.getImg_s()%>">
																				</a>
																			</td>
																			<td width="20%">
																				采购单位
																			</td>
																			<td class="orderproperty">
																				<select class="form-control require"
																					name="factoryId" id="factoryId">
																					<option value="">
																						未选择
																					</option>
																					<%
																						for (Factory factory : SystemCache.factorylist) {
																								if (factory.getId() == materialPurchaseOrder.getFactoryId()) {
																					%>
																					<option value="<%=factory.getId()%>"
																						selected='selected'><%=factory.getName()%></option>
																					<%
																						} else {
																					%>
																					<option value="<%=factory.getId()%>"><%=factory.getName()%></option>
																					<%
																						}
																							}
																					%>

																				</select>
																			</td>

																		</tr>
																		<tr>
																			<td width="20%">
																				订购日期
																			</td>
																			<td class="orderproperty"><%=materialPurchaseOrder == null ? ""
						: (materialPurchaseOrder.getPurchase_at() == null ? ""
								: materialPurchaseOrder.getPurchase_at())%>
																			</td>
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
																			<td><%=order.getKehu()%></td>
																		</tr>
																		<tr>
																			<td>
																				货号
																			</td>
																			<td><%=order.getProductNumber()%></td>
																		</tr>
																		<tr>
																			<td>
																				款名
																			</td>
																			<td><%=order.getName()%></td>
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
																			class="btn btn-primary addRow pull-left">
																			添加一行
																		</button>
																		材料列表
																	</caption>
																	<thead>
																		<tr>
																			<th width="15%">
																				材料品种
																			</th>
																			<th width="15%">
																				规格
																			</th>
																			<th width="15%">
																				数量
																			</th>
																			<th width="15%">
																				批次号
																			</th>
																			<th width="15%">
																				价格（含税）
																			</th>
																			<th width="15%">
																				操作
																			</th>
																		</tr>
																	</thead>
																	<tbody>
																		<%
																			for (MaterialPurchaseOrderDetail detail : materialPurchaseOrderDetailList) {
																		%>
																		<tr class="tr"
																			data='<%=SerializeTool.serialize(detail)%>'>
																			<td class="material_name"><%=SystemCache.getMaterialName(detail
											.getMaterial())%>
																			</td>
																			<td class="scale"><%=detail.getScale()%>
																			</td>
																			<td class="quantity"><%=detail.getQuantity()%>
																			</td>
																			<td class="batch_number"><%=detail.getBatch_number()%>
																			</td>
																			<td class="price"><%=detail.getPrice()%>
																			</td>
																			<td class="_handle">
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
										</form>
									</div>
								</div>

								<%
									}
								%>
								<!--
						 			添加编辑原材料采购对话框 -->
								<div class="modal fade tableRowDialog"
									id="materialpurchaseDialog">
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
															材料品种
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
														<label for="scale" class="col-sm-3 control-label">
															规格
														</label>
														<div class="col-sm-8">
															<input type="text" name="scale" id="scale"
																class="form-control require" />
														</div>
														<div class="col-sm-1"></div>
													</div>
													<div class="form-group col-md-12">
														<label for="quantity" class="col-sm-3 control-label">
															数量
														</label>
														<div class="col-sm-8">
															<input type="text" name="quantity" id="quantity"
																class="form-control int require" />
														</div>
														<div class="col-sm-1"></div>
													</div>
													<div class="form-group col-md-12">
														<label for="color" class="col-sm-3 control-label">
															批次号
														</label>
														<div class="col-sm-8">
															<input type="text" name="batch_number" id="batch_number"
																class="form-control" />
														</div>
														<div class="col-sm-1"></div>
													</div>


													<div class="form-group col-md-12">
														<label for="price" class="col-sm-3 control-label">
															价格（含税）
														</label>
														<div class="col-sm-8">
															<input type="text" name="price" id="price"
																class="form-control double require" />
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
								<!-- 添加编辑原材料采购对话框 -->
							</div>


							<!-- 染色单 -->

							<div class="tab-pane" id="coloringorder" role="tabpanel">
								<div class="emptyrecordwidget">
									<p>
										如果您要创建染色单，请点击下方的按钮
									</p>
									<a href="coloring_order/add/<%=order.getId()%>"
										class="btn btn-primary" id="createProducingorderBtn">创建原材料染色单</a>
								</div>

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
											<button type="submit"
												class="pull-right btn btn-danger saveTable"
												data-loading-text="正在保存...">
												保存对当前表格的修改
											</button>
											<a target="_blank" type="button"
												class="pull-right btn btn-success printBtn"
												data-loading-text="正在打印..."> 打印 </a>
											<div class="clear"></div>
											<div class="col-md-12 tablewidget">
												<table class="table">
													<caption>
														桐庐富伟针织厂染色单
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
																			<td rowspan="7" width="50%">
																				<a href="/<%=order.getImg()%>" class="thumbnail"
																					target="_blank"> <img id="previewImg"
																						alt="200 x 100%" src="/<%=order.getImg_s()%>">
																				</a>
																			</td>
																			<td width="20%">
																				染色单位
																			</td>
																			<td class="orderproperty">
																				<select class="form-control require"
																					name="factoryId" id="factoryId">
																					<option value="">
																						未选择
																					</option>
																					<%
																						for (Factory factory : SystemCache.factorylist) {
																								if (factory.getId() == coloringOrder.getFactoryId()) {
																					%>
																					<option value="<%=factory.getId()%>"
																						selected='selected'><%=factory.getName()%></option>
																					<%
																						} else {
																					%>
																					<option value="<%=factory.getId()%>"><%=factory.getName()%></option>
																					<%
																						}
																							}
																					%>

																				</select>
																			</td>

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
																			<td><%=order.getKehu()%></td>
																		</tr>
																		<tr>
																			<td>
																				货号
																			</td>
																			<td><%=order.getProductNumber()%></td>
																		</tr>
																		<tr>
																			<td>
																				款名
																			</td>
																			<td><%=order.getName()%></td>
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
																			class="btn btn-primary addRow pull-left">
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
																				数量
																			</th>
																			<th width="15%">
																				标准样纱
																			</th>
																			<th width="15%">
																				操作
																			</th>
																		</tr>
																	</thead>
																	<tbody>
																		<%
																			for (ColoringOrderDetail detail : coloringOrderDetailList) {
																		%>
																		<tr class="tr"
																			data='<%=SerializeTool.serialize(detail)%>'>
																			<td class="color"><%=detail.getColor()%>
																			</td>
																			<td class="material_name"><%=SystemCache.getMaterialName(detail
											.getMaterial())%>
																			</td>
																			<td class="quantity"><%=detail.getQuantity()%>
																			</td>
																			<td class="standardyarn"><%=detail.getStandardyarn()%>
																			<td class="_handle">
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
										</form>
									</div>
								</div>
								<%
									}
								%>



								<!--
						 			添加编辑染色单对话框 -->
								<div class="modal fade tableRowDialog" id="coloringDialog">
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
															颜色
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
															数量
														</label>
														<div class="col-sm-8">
															<input type="text" name="quantity" id="quantity"
																class="form-control int require" />
														</div>
														<div class="col-sm-1"></div>
													</div>
													<div class="form-group col-md-12">
														<label for="standardyarn" class="col-sm-3 control-label">
															标准样纱
														</label>
														<div class="col-sm-8">
															<input type="text" name="standardyarn" id="standardyarn"
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
								<!-- 添加编辑染色单对话框 -->
							</div>



							<!-- 抽检记录单 -->
							<div class="tab-pane" id="checkrecordorder" role="tabpanel">
								<div class="container-fluid">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="id"
												value="<%=checkRecordOrder == null ? "" : checkRecordOrder
					.getId()%>" />
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
																		<td rowspan="7" width="50%">
																			<a href="/<%=order.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=order.getImg_s()%>">
																			</a>
																		</td>
																		<td width="20%">
																			生产单位
																		</td>
																		<td class="orderproperty"></td>
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
																		<td><%=order.getKehu()%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=order.getProductNumber()%></td>
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
																		<td><%=SystemCache.getUserName(order.getCharge_user())%></td>
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
								<div class="emptyrecordwidget">
									<p>
										如果您要创建辅料采购单，请点击下方的按钮
									</p>
									<a href="fuliao_purchase_order/add/<%=order.getId()%>"
										class="btn btn-primary" id="createProducingorderBtn">创建辅料采购单</a>
								</div>

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
											<button type="submit"
												class="pull-right btn btn-danger saveTable"
												data-loading-text="正在保存...">
												保存对当前表格的修改
											</button>
											<a target="_blank" type="button"
												class="pull-right btn btn-success printBtn"
												data-loading-text="正在打印..."> 打印 </a>
											<div class="clear"></div>
											<div class="col-md-12 tablewidget">
												<table class="table">
													<caption>
														桐庐富伟针织厂辅料采购单
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
																			<td rowspan="7" width="50%">
																				<a href="/<%=order.getImg()%>" class="thumbnail"
																					target="_blank"> <img id="previewImg"
																						alt="200 x 100%" src="/<%=order.getImg_s()%>">
																				</a>
																			</td>
																			<td width="20%">
																				供货方
																			</td>
																			<td class="orderproperty">
																				<select class="form-control require"
																					name="factoryId" id="factoryId">
																					<option value="">
																						未选择
																					</option>
																					<%
																						for (Factory factory : SystemCache.factorylist) {
																								if (factory.getId() == fuliaoPurchaseOrder.getFactoryId()) {
																					%>
																					<option value="<%=factory.getId()%>"
																						selected='selected'><%=factory.getName()%></option>
																					<%
																						} else {
																					%>
																					<option value="<%=factory.getId()%>"><%=factory.getName()%></option>
																					<%
																						}
																							}
																					%>

																				</select>
																			</td>
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
																			<td><%=SystemCache.getCompanyName(fuliaoPurchaseOrder
								.getCompanyId())%></td>
																		</tr>
																		<tr>
																			<td>
																				客户
																			</td>
																			<td><%=fuliaoPurchaseOrder.getKehu()%></td>
																		</tr>
																		<tr>
																			<td>
																				货号
																			</td>
																			<td><%=fuliaoPurchaseOrder.getProductNumber()%></td>
																		</tr>
																		<tr>
																			<td>
																				款名
																			</td>
																			<td><%=fuliaoPurchaseOrder.getName()%></td>
																		</tr>
																		<tr>
																			<td>
																				跟单
																			</td>
																			<td><%=SystemCache.getUserName(order.getCharge_user())%></td>
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
																			class="btn btn-primary addRow pull-left">
																			添加一行
																		</button>
																		采购列表
																	</caption>
																	<thead>
																		<tr>
																			<th width="15%">
																				辅料类型
																			</th>
																			<th width="15%">
																				标准样
																			</th>
																			<th width="15%">
																				数量
																			</th>
																			<th width="15%">
																				价格
																			</th>
																			<th width="15%">
																				交期
																			</th>
																			<th width="15%">
																				操作
																			</th>
																		</tr>
																	</thead>
																	<tbody>
																		<%
																			for (FuliaoPurchaseOrderDetail detail : fuliaoPurchaseOrderDetailList) {
																		%>
																		<tr class="tr"
																			data='<%=SerializeTool.serialize(detail)%>'>
																			<td class="style_name"><%=SystemCache.getMaterialName(detail.getStyle())%>
																			</td>
																			<td class="standardsample"><%=detail.getStandardsample()%>
																			</td>
																			<td class="quantity"><%=detail.getQuantity()%>
																			</td>
																			<td class="price"><%=detail.getPrice()%>
																			</td>
																			<td class="end_at"><%=detail.getEnd_at()%>
																			</td>
																			<td class="_handle">
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
										</form>




									</div>
								</div>
								<%
									}
								%>
								<!--
						 添加编辑辅料采购单对话框 -->
								<div class="modal fade tableRowDialog" id="fuliaopurchaseDialog">
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
														<label for="style" class="col-sm-3 control-label">
															辅料类型
														</label>
														<div class="col-sm-8">
															<select name="style" id="style"
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
														<label for="standardsample" class="col-sm-3 control-label">
															标准样
														</label>
														<div class="col-sm-8">
															<input type="text" name="standardsample"
																id="standardsample" class="form-control" />
														</div>
														<div class="col-sm-1"></div>
													</div>
													<div class="form-group col-md-12">
														<label for="quantity" class="col-sm-3 control-label">
															数量
														</label>
														<div class="col-sm-8">
															<input type="text" name="quantity" id="quantity"
																class="form-control int require" />
														</div>
														<div class="col-sm-1"></div>
													</div>
													<div class="form-group col-md-12">
														<label for="price" class="col-sm-3 control-label">
															价格
														</label>
														<div class="col-sm-8">
															<input type="text" name="price" id="price"
																class="form-control double require" />
														</div>
														<div class="col-sm-1"></div>
													</div>
													<div class="form-group col-md-12">
														<label for="end_at" class="col-sm-3 control-label">
															交期
														</label>
														<div class="col-sm-8">
															<input type="text" name="end_at" id="end_at"
																class="form-control date require" />
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
								<!-- 添加编辑辅料采购单对话框 -->

							</div>


							<!-- 车缝记录单 -->
							<div class="tab-pane" id="carfixrecordorder" role="tabpanel">
								<div class="container-fluid">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="id"
												value="<%=carFixRecordOrder == null ? "" : carFixRecordOrder
					.getId()%>" />
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
																		<td rowspan="7" width="50%">
																			<a href="/<%=order.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=order.getImg_s()%>">
																			</a>
																		</td>
																		<td width="20%">
																			生产单位
																		</td>
																		<td class="orderproperty"></td>
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
																		<td><%=order.getKehu()%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=order.getProductNumber()%></td>
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
																		<td><%=SystemCache.getUserName(order.getCharge_user())%></td>
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
											<input type="hidden" name="id"
												value="<%=ironingRecordOrder == null ? "" : ironingRecordOrder
					.getId()%>" />
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
																		<td rowspan="7" width="50%">
																			<a href="/<%=order.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=order.getImg_s()%>">
																			</a>
																		</td>
																		<td width="20%">
																			生产单位
																		</td>
																		<td class="orderproperty"></td>
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
																		<td><%=order.getKehu()%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=order.getProductNumber()%></td>
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
																		<td><%=SystemCache.getUserName(order.getCharge_user())%></td>
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
											<input type="hidden" name="id"
												value="<%=productionScheduleOrder == null ? ""
					: productionScheduleOrder.getId()%>" />
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
																		<td rowspan="7" width="50%">
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
																		<td><%=order.getKehu()%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=order.getProductNumber()%></td>
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
																		<td><%=SystemCache.getUserName(order.getCharge_user())%></td>
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
											<input type="hidden" name="id"
												value="<%=finalStoreOrder == null ? "" : finalStoreOrder
					.getId()%>" />
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
																		<td rowspan="7" width="50%">
																			<a href="/<%=order.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=order.getImg_s()%>">
																			</a>
																		</td>
																		<td width="20%">
																			生产单位
																		</td>
																		<td class="orderproperty"></td>
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
																		<td><%=order.getKehu()%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=order.getProductNumber()%></td>
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
																		<td><%=SystemCache.getUserName(order.getCharge_user())%></td>
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

							<!-- 车间记录单 -->
							<div class="tab-pane" id="shoprecordorder" role="tabpanel">
								<div class="container-fluid">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="id"
												value="<%=shopRecordOrder == null ? "" : shopRecordOrder
					.getId()%>" />
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
																		<td rowspan="7" width="50%">
																			<a href="/<%=order.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=order.getImg_s()%>">
																			</a>
																		</td>
																		<td width="20%">
																			生产单位
																		</td>
																		<td class="orderproperty"></td>
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
																		<td><%=order.getKehu()%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=order.getProductNumber()%></td>
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
																		<td><%=SystemCache.getUserName(order.getCharge_user())%></td>
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
											<input type="hidden" name="id"
												value="<%=productionScheduleOrder == null ? ""
					: productionScheduleOrder.getId()%>" />
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
																		<td rowspan="7" width="50%">
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
																		<td><%=order.getKehu()%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=order.getProductNumber()%></td>
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
																		<td><%=SystemCache.getUserName(order.getCharge_user())%></td>
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
																			数量
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