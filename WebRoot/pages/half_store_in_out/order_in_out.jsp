<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.NumberUtil"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.entity.producesystem.HalfStoreInOut"%>
<%@page import="com.fuwei.entity.producesystem.HalfStoreInOutDetail"%>
<%@page import="com.fuwei.entity.ordergrid.PlanOrder"%>
<%@page import="com.fuwei.entity.ordergrid.PlanOrderDetail"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Order order = (Order) request.getAttribute("order");
	PlanOrder planOrder = (PlanOrder) request.getAttribute("planOrder");
	List<PlanOrderDetail> DetailList = planOrder == null || planOrder.getDetaillist() == null ? new ArrayList<PlanOrderDetail>()
			: planOrder.getDetaillist();
	List<HalfStoreInOut> storeInOutList = (List<HalfStoreInOut>)request.getAttribute("storeInOutList");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>半成品出入库记录 -- 桐庐富伟针织厂</title>
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
		<link href="css/order/index.css" rel="stylesheet" type="text/css" />
		<style type="text/css">
			.table>thead>tr>th {padding: 0 8px;vertical-align: middle;}
			.table>thead>tr {background: #AEADAD;}
			.table tbody tr {background: #ddd;}
			.thumbnail>img{max-width: 300px;max-height: 200px;}
			#leftOrderInfo{  width: 250px;}
			#leftOrderInfo .table-bordered>tbody>tr>td{border-color: #000;}
			#rightStoreInfo{margin-left:250px;}
			#rightStoreInfo .table-bordered>thead>tr>th,#rightStoreInfo .table-bordered>tbody>tr>td{border-color: #000;}
			#rightStoreInfo #storeDetail legend{font-weight: bold;}
			legend{margin-bottom:0;}
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
						<li>
							<a target="_blank" href="workspace/half_workspace">半成品工作台 </a>
						</li>
						<li>
							<a href="order/detail/<%=order.getId()%>">订单详情</a>
						</li>
						<li class="active">
							半成品出入库记录
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 formwidget">
								<div class="head">
									<div class="pull-left">
										<label class="control-label">
											订单编号：
										</label>
										<span><%=order.getOrderNumber()%></span>
									</div>
									<div class="pull-left">
										<label class="control-label">
											订单状态：
										</label>
										<span><%=order.getCNState()%></span>
									</div>
								

									<div class="clear"></div>

								</div>
								<div class="clear"></div>

								<div class="col-md-6" id="leftOrderInfo">
									<table class="table table-responsive table-bordered">
																<tbody>
																	<tr>
																		<td colspan="2">
																			<a href="/<%=order.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=order.getImg_s()%>">
																			</a>
																		</td></tr>
																		<tr>
																			<td>
																				品名
																			</td>
																			<td><%=order.getName()%></td>
																		</tr>
																	
																	<tr>
																		<td>
																			公司
																		</td>
																		<td><%=SystemCache.getCompanyShortName(order.getCompanyId())%></td>
																	</tr>
																	<tr>
																		<td>
																			业务员
																		</td>
																		<td><%=SystemCache.getSalesmanName(order.getSalesmanId())%></td>
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
								</div>
							<div class="" id="rightStoreInfo">
								<fieldset id="orderDetail">
									<legend>
										计划生产颜色及数量
									</legend>
									<table class="table table-responsive detailTb">
										<caption>
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
												</th>
											</tr>
										</thead>
										<tbody>
											<%
												for (PlanOrderDetail detail : DetailList) {
											%>
											<tr class="tr">
												<td class="color"><%=detail.getColor()%>
												</td>
												<td class="weight"><%=detail.getWeight()%>
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
								</fieldset>
								<fieldset id="storeDetail">
									<legend>
										出入库记录
									</legend>
									<table class="table table-responsive detailTb table-bordered">
										<thead>
											<tr style="height:0;">
												<th style="width:50px"></th>
	    										<th style="width:70px"></th>
	    										<th style="width:100px"></th>
	    										<th style="width:55px"></th>
	    										<th style="width:60px"></th>
	    										<th style="width:50px"></th>
	    										<th style="width:60px"></th>
	    										<th style="width:60px"></th>
  											</tr>
											<tr>
												<th rowspan="2" width="50px">
													出/入库
												</th>
												<th rowspan="2" width="70px">
													单号
												</th>
												<th rowspan="2" width="100px">
													领取/送货单位
												</th>
												<th colspan="3" width="200px">
													颜色及数量
												</th><th rowspan="2" width="60px">
													经办人
												</th>
												<th rowspan="2" width="60px">
													出/入库时间
												</th>
											</tr>
											<tr><th width="55px">
												颜色
											</th><th  width="60px">
												尺寸
											</th><th  width="50px">
												数量
											</th></tr>
										</thead>
										<tbody>
												<%
												for (HalfStoreInOut storeInout : storeInOutList) {
													String classname = storeInout.getIn_out()?"ruku":"chuku";
													List<HalfStoreInOutDetail> detailist = storeInout.getDetaillist();
													int detailsize = storeInout.getDetaillist().size();
										%>
										<tr itemId="<%=storeInout.getId()%>" class="<%=classname%>">
											<td rowspan="<%=detailsize%>"><%=storeInout.getType()%></td>
											<td rowspan="<%=detailsize%>"><a target="_top" href="half_store_in/detail/<%=storeInout.getId()%>"><%=storeInout.getNumber()%></a></td>
											<td rowspan="<%=detailsize%>"><%=SystemCache.getFactoryName(storeInout.getFactoryId())%></td>
											<td><%=detailist.get(0).getColor()%></td>
											<td><%=detailist.get(0).getSize()%></td>
											<td><%=detailist.get(0).getQuantity()%></td>

											<td rowspan="<%=detailsize%>"><%=SystemCache.getUserName(storeInout.getCreated_user())%></td>				
											<td rowspan="<%=detailsize%>"><%=DateTool.formatDateYMD(storeInout.getDate())%></td>				
										</tr>
										<%
											detailist.remove(0);
																		for(HalfStoreInOutDetail detail : detailist){
										%>
										<tr class="<%=classname %>">
											<td><%=detail.getColor()%></td>
											<td><%=detail.getSize()%></td>
											<td><%=detail.getQuantity()%></td>

										</tr>
										<%} %>
										<%
											}
										%>
										</tbody>
									</table>
								</fieldset>
								</div>
								<div class="clear"></div>
							</div>


						</div>
					</div>

				</div>
			</div>
		</div>
	</body>
</html>
