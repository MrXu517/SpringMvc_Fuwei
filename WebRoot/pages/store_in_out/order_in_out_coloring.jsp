<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.NumberUtil"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.entity.producesystem.StoreInOut"%>
<%@page import="com.fuwei.entity.producesystem.StoreInOutDetail"%>
<%@page import="com.fuwei.entity.producesystem.MaterialInOut"%>
<%@page import="com.fuwei.entity.producesystem.MaterialInOutDetail"%>
<%@page import="com.fuwei.entity.ordergrid.ColoringOrder"%>
<%@page import="com.fuwei.entity.ordergrid.ColoringOrderDetail"%>
<%@page import="com.fuwei.entity.producesystem.MaterialCurrentStock"%>
<%@page import="com.fuwei.entity.producesystem.MaterialCurrentStockDetail"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	ColoringOrder coloringOrder = (ColoringOrder) request.getAttribute("coloringOrder");
	List<ColoringOrderDetail> DetailList = coloringOrder == null || coloringOrder.getDetaillist() == null ? new ArrayList<ColoringOrderDetail>()
			: coloringOrder.getDetaillist();
	List<MaterialInOut> detailInOutlist = (List<MaterialInOut>)request.getAttribute("detailInOutlist");
	MaterialCurrentStock materialCurrentStock = (MaterialCurrentStock)request.getAttribute("materialCurrentStock");
	
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>原材料样纱出入库记录 -- 桐庐富伟针织厂</title>
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
			.thumbnail>img{max-width: 200px;max-height: 200px;}
			#leftOrderInfo{  width: 250px;}
			#leftOrderInfo .table-bordered>tbody>tr>td{border-color: #000;}
			#rightStoreInfo{margin-left:250px;}
			#rightStoreInfo .table-bordered>thead>tr>th,#rightStoreInfo .table-bordered>tbody>tr>td{border-color: #000;}
			#rightStoreInfo #storeDetail legend{font-weight: bold;}
			legend{margin-bottom:0;}
			#storeDetail thead th{text-align:center;}
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
							<a target="_blank" href="workspace/material_workspace">原材料工作台 </a>
						</li>
						<li>
							<a href="coloring_order/detail/<%=coloringOrder.getId()%>">染色单详情</a>
						</li>
						<li class="active">原材料出入库记录 --样纱</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 formwidget">
								<div class="head">
									<div class="pull-left">
										<label class="control-label">
											染色单号：
										</label>
										<span><%=coloringOrder.getNumber()%></span>
									</div>
									<div class="clear"></div>

								</div>
								<div class="clear"></div>

								<div class="col-md-6" id="leftOrderInfo">
									<table class="table table-responsive table-bordered">
																<tbody>
																		<tr>
																			<td>
																				品名
																			</td>
																			<td><%=coloringOrder.getName()%></td>
																		</tr>
																	
																	<tr>
																		<td>
																			公司
																		</td>
																		<td><%=SystemCache.getCompanyShortName(coloringOrder.getCompanyId())%></td>
																	</tr>
																	<tr>
																		<td>
																			客户
																		</td>
																		<td><%=SystemCache.getCustomerName(coloringOrder.getCustomerId())%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=coloringOrder.getCompany_productNumber()%></td>
																	</tr>
																	<tr>
																		<td>
																			跟单
																		</td>
																		<td><%=SystemCache.getEmployeeName(coloringOrder.getCharge_employee())%></td>
																	</tr>
																</tbody>
															</table>
								</div>
							<div class="" id="rightStoreInfo">
								<fieldset id="orderDetail">
									<legend>
										计划染色列表
									</legend>
									<table class="table table-responsive detailTb">
										<caption>
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
																			标准样纱
																		</th>
											</tr>
										</thead>
										<tbody>
											<%
												for (ColoringOrderDetail detail : DetailList) {
											%>
											<tr class="tr">
												<td class="color"><%=detail.getColor()%>
																		</td>
																		<td class="material_name"><%=SystemCache.getMaterialName(detail.getMaterial())%>
																		</td>
																		<td class="quantity"><%=detail.getQuantity()%>
																		</td>
																		<td class="yarn"><%=detail.getStandardyarn()%>
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
													染色/出库单位
												</th>
												<th colspan="4" width="200px">
													材料列表
												</th><th rowspan="2" width="60px">
													经办人
												</th>
												<th rowspan="2" width="60px">
													出/入库时间
												</th>
											</tr>
											<tr><th width="55px">
												色号
											</th><th  width="60px">
												材料
											</th><th  width="60px">
												缸号
											</th><th  width="50px">
												数量
											</th></tr>
										</thead>
										<tbody>
												<%
												for (MaterialInOut item : detailInOutlist) {
													List<MaterialInOutDetail> detailist = item.getDetaillist();
													int detailsize = detailist.size();
													int type = item.getInt();
										%>
										<tr itemId="<%=item.getId()%>">
											<td rowspan="<%=detailsize%>"><%=item.getTypeString()%></td>
											<td rowspan="<%=detailsize%>">
												<%if(type == 1){ %>
													<a target="_top" href="store_in/detail/<%=item.getId()%>"><%=item.getNumber()%></a>
												<%}else if(type==0){ %>
													<a target="_top" href="store_out/detail/<%=item.getId()%>"><%=item.getNumber()%></a>
												<%}else if(type==-1){ %>
													<a target="_top" href="store_return/detail/<%=item.getId()%>"><%=item.getNumber()%></a>
												<%} else{ %>
													<%=item.getNumber()%>
												<%} %></td>
											<td rowspan="<%=detailsize%>"><%=SystemCache.getFactoryName(item.getFactoryId())%></td>
											<td><%=detailist.get(0).getColor()%></td>
											<td><%=SystemCache.getMaterialName(detailist.get(0).getMaterial())%></td>
											<td><%=detailist.get(0).getLot_no()%></td>
											<td><%=detailist.get(0).getQuantity()%></td>

											<td rowspan="<%=detailsize%>"><%=SystemCache.getUserName(item.getCreated_user())%></td>				
											<td rowspan="<%=detailsize%>"><%=DateTool.formatDateYMD(item.getDate())%></td>				
										</tr>
										<%
											detailist.remove(0);
																		for(MaterialInOutDetail detail : detailist){
										%>
										<tr>
											<td><%=detail.getColor()%></td>
											<td><%=SystemCache.getMaterialName(detail.getMaterial())%></td>
											<td><%=detail.getLot_no()%></td>
											<td><%=detail.getQuantity()%></td>

										</tr>
										<%} %>
										<%
											}
										%>
										</tbody>
									</table>
								</fieldset>

								<fieldset id="stockDetail">
									<legend>
										当前库存
									</legend>
									<table class="table table-responsive detailTb table-bordered">
										<thead>
											<tr><th width="55px">
												色号
											</th><th  width="60px">
												材料
											</th><th  width="50px">
												计划数量(kg)
											</th><th  width="50px">
												总入库(kg)
											</th><th  width="50px">
												总退货(kg)
											</th><th  width="50px">
												当前库存(kg)
											</th></tr>
										</thead>
										<tbody>
												<%
												List<MaterialCurrentStockDetail> detaillist = materialCurrentStock.getDetaillist();
												if(detaillist == null){
													detaillist = new ArrayList<MaterialCurrentStockDetail>();
												}
												for (MaterialCurrentStockDetail item : detaillist) {
										%>
										<tr itemId="<%=item.getId()%>">
											<td><%=item.getColor()%></td>
											<td><%=SystemCache.getMaterialName(item.getMaterial())%></td>
											<td><%=item.getPlan_quantity()%></td>
											<td><%=item.getIn_quantity()%></td>
											<td><%=item.getReturn_quantity()%></td>
											<td><%=item.getStock_quantity()%></td>
										</tr>
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
