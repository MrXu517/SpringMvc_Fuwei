<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.NumberUtil"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.entity.finishstore.FinishInOut"%>
<%@page import="com.fuwei.entity.finishstore.FinishInOutDetail"%>
<%@page import="com.fuwei.entity.finishstore.PackingOrder"%>
<%@page import="com.fuwei.entity.finishstore.PackingOrderDetail"%>
<%@page import="com.fuwei.entity.finishstore.FinishStoreStockDetail"%>
<%@page import="com.fuwei.entity.finishstore.FinishStoreStock"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Order order = (Order) request.getAttribute("order");
	PackingOrder packingOrder = (PackingOrder) request.getAttribute("packingOrder");
	FinishStoreStock storeStock = (FinishStoreStock) request.getAttribute("storeStock");
	List<PackingOrderDetail> packingOrderDetaillist = packingOrder == null? new ArrayList<PackingOrderDetail>():packingOrder.getDetaillist();
	List<FinishInOut> detailInOutlist = (List<FinishInOut>)request.getAttribute("detailInOutlist");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>成品出入库记录 -- 桐庐富伟针织厂</title>
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
			#rightStoreInfo legend{font-weight: bold;}
			legend{margin-bottom:0;}
			#storeDetail table{border: 1px solid #000;}
			#storeDetail thead th{text-align:center;}
			#packingOrderDetail table thead th{
		   	 	background: #AEADAD;    border: 1px solid #000; text-align: center;padding: 0 3px;
			}
			#stockDetail table thead th{background: #AEADAD;    border: 1px solid #000; text-align: center;}
			#packingOrderDetail table tbody td,#packingOrderDetail table tfoot td {
		    	 border: 1px solid #000; text-align: center;padding: 0;
			}
			#packingOrderDetail table,#stockDetail table{ border: 1px solid #000;    table-layout: fixed;}
			#packingOrderDetail table tfoot td{border: 1px solid #000;text-align: right;padding-top: 0;padding-bottom: 0;}
			#stockDetail table tbody td,#stockDetail table tfoot td{border: 1px solid #000;}
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
							<a href="order/detail/<%=order.getId()%>">订单详情</a>
						</li>
						<li>
							<a target="_blank" href="finishstore_workspace/workspace?tab=current_stock">成品工作台</a>
						</li>
						<li class="active">
							订单 <%=order.getOrderNumber()%> 的成品出入库记录
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
								<fieldset id="packingOrderDetail">
									<legend>
										装箱单颜色及数量
									</legend>
									<table class="table table-responsive detailTb">
										<thead>
										<tr>
											<%
											int col = 0;
											if(packingOrder.getCol1_id()!=null){
											col++;
											 %>
											<th rowspan="2" width="80px">
												<%=SystemCache.getPackPropertyName(packingOrder.getCol1_id()) %>
											</th>
											<%} %>
											
											<%if(packingOrder.getCol2_id()!=null){ 
											col++;%>
											<th rowspan="2" width="80px">
												<%=SystemCache.getPackPropertyName(packingOrder.getCol2_id()) %>
											</th>
											<%} %>
											<%if(packingOrder.getCol3_id()!=null){ 
											col++;%>
											<th rowspan="2" width="80px">
												<%=SystemCache.getPackPropertyName(packingOrder.getCol3_id()) %>
											</th>
											<%} %>
											<%if(packingOrder.getCol4_id()!=null){ 
											col++;%>
											<th rowspan="2" width="80px">
												<%=SystemCache.getPackPropertyName(packingOrder.getCol4_id()) %>
											</th>
											<%} %>

											<th rowspan="2" width="40px">
												颜色
											</th>
											<th rowspan="2" width="40px">
												数量
											</th>
											<th rowspan="2" width="40px">
												每箱数量
											</th><th colspan="3" width="120px">外箱尺寸</th>
												<th colspan="2" width="80px">毛净重</th>
											<th rowspan="2" width="60px">
												箱数
											</th>
											<th colspan="2" width="100px">
												箱号
											</th>
											<th rowspan="2" width="40px">
												每包几件
											</th>
											<th rowspan="2" width="40px">
												立方数
											</th>
										</tr><tr><th width="55px">
												L
											</th><th width="55px">
												W
											</th><th width="55px">
												H
											</th><th width="55px">
												毛重
											</th><th width="55px">
												净重
											</th><th width="60px">
												开始
											</th><th width="60px">
												结束
											</th></tr>
									</thead>
									<tbody>
										<%for(PackingOrderDetail detail : packingOrderDetaillist){ %>
										<tr>
										<%if(packingOrder.getCol1_id()!=null){ %>
										<td>
											<%=detail.getCol1_value()==null?"":detail.getCol1_value() %>
										</td>
										<%} %>
										<%if(packingOrder.getCol2_id()!=null){ %>
										<td>
											<%=detail.getCol2_value()==null?"":detail.getCol2_value() %>
										</td>
										<%} %>	
										<%if(packingOrder.getCol3_id()!=null){ %>
										<td>
											<%=detail.getCol3_value()==null?"":detail.getCol3_value() %>
										</td>
										<%} %>
										<%if(packingOrder.getCol4_id()!=null){ %>
										<td>
											<%=detail.getCol4_value()==null?"":detail.getCol4_value() %>
										</td>
										<%} %>
										<td><%=detail.getColor() %></td>
										<td><%=detail.getQuantity() %></td>
										<td><%=detail.getPer_carton_quantity() %></td>
										<td><%=detail.getBox_L() %></td>
										<td><%=detail.getBox_W() %></td>
										<td><%=detail.getBox_H() %></td>
										<td><%=detail.getGross_weight() %></td>
										<td><%=detail.getNet_weight() %></td>
										<td><%=detail.getCartons() %></td>
										<td><%=detail.getBox_number_start() %></td>
										<td><%=detail.getBox_number_end() %></td>
										<td><%=detail.getPer_pack_quantity() %></td>
										<td><%=detail.getCapacity() %></td>
										</tr>
										<%} %>
									</tbody>
									<tfoot><tr><td>合计</td><td colspan="<%=col+1 %>"><%=packingOrder.getQuantity() %></td>
										<td colspan="7"><%=packingOrder.getCartons() %></td><td colspan="4"><%=packingOrder.getCapacity() %></td>
										</tr></tfoot>
								</table>
								</fieldset>
								<fieldset id="storeDetail">
									<legend>
										出入库记录
									</legend>
									<table class="table table-responsive detailTb table-bordered">
										<thead>
											
											<tr>
												<th rowspan="2" width="50px">
													出/入库
												</th>
												<th rowspan="2" width="60px">
													出/入库时间
												</th>
												<th rowspan="2" width="60px">
													出入库单号
												</th>
												<th colspan="<%=col+3%>" width="200px">
													颜色及数量
												</th><th rowspan="2" width="60px">
													经办人
												</th>
											</tr>
											<tr>
											<%
											if(packingOrder.getCol1_id()!=null){
											 %>
											<th width="80px">
												<%=SystemCache.getPackPropertyName(packingOrder.getCol1_id()) %>
											</th>
											<%} %>
											
											<%if(packingOrder.getCol2_id()!=null){%>
											<th width="80px">
												<%=SystemCache.getPackPropertyName(packingOrder.getCol2_id()) %>
											</th>
											<%} %>
											<%if(packingOrder.getCol3_id()!=null){%>
											<th width="80px">
												<%=SystemCache.getPackPropertyName(packingOrder.getCol3_id()) %>
											</th>
											<%} %>
											<%if(packingOrder.getCol4_id()!=null){%>
											<th width="80px">
												<%=SystemCache.getPackPropertyName(packingOrder.getCol4_id()) %>
											</th>
											<%} %>
											<th width="60px">
												颜色
											</th>
											<th width="60px">
												数量
											</th>
											<th width="60px">
												箱数
											</th>
											</tr>
										</thead>
										<tbody>
										<%
											for (FinishInOut item : detailInOutlist) {
												List<FinishInOutDetail> detailist = item.getDetaillist();
												if(detailist == null){detailist = new ArrayList<FinishInOutDetail>();}
												int detailsize = detailist.size();
												int type = item.getInt();
										%>
										<tr itemId="<%=item.getId()%>">
											<td rowspan="<%=detailsize%>"><%=item.getTypeString()%></td>				
											<td rowspan="<%=detailsize%>"><%=DateTool.formatDateYMD(item.getDate())%></td>
											<td rowspan="<%=detailsize%>">
												<%if(type == 1){ %>
													<a target="_top" href="finishstore_in/detail/<%=item.getId()%>"><%=item.getNumber()%></a>
												<%}else if(type==0){ %>
													<a target="_top" href="finishstore_out/detail/<%=item.getId()%>"><%=item.getNumber()%></a>
												<%}else if(type==-1){ %>
													<a target="_top" href="finishstore_return/detail/<%=item.getId()%>"><%=item.getNumber()%></a>
												<%} else{ %>
													<%=item.getNumber()%>
												<%} %></td>
											
											
											<%if(packingOrder.getCol1_id()!=null){%>
											<td><%=detailist.get(0).getCol1_value() == null?"":detailist.get(0).getCol1_value()%></td>
											<%} %>
											<%if(packingOrder.getCol2_id()!=null){%>
											<td><%=detailist.get(0).getCol2_value() == null?"":detailist.get(0).getCol2_value()%></td>
											<%} %>
											<%if(packingOrder.getCol3_id()!=null){%>
											<td><%=detailist.get(0).getCol3_value() == null?"":detailist.get(0).getCol3_value()%></td>
											<%} %>
											<%if(packingOrder.getCol4_id()!=null){%>
											<td><%=detailist.get(0).getCol4_value() == null?"":detailist.get(0).getCol4_value()%></td>
											<%} %>
											<td><%=detailist.get(0).getColor()%></td>
											<td><%=detailist.get(0).getCartons()%></td>
											<td><%=detailist.get(0).getQuantity()%></td>

										
											<td rowspan="<%=detailsize%>"><%=DateTool.formatDateYMD(item.getDate())%></td>				
										</tr>
										<%
											detailist.remove(0);
											for(FinishInOutDetail detail : detailist){
										%>
										<tr>
											<%if(packingOrder.getCol1_id()!=null){%>
											<td><%=detail.getCol1_value() == null?"":detail.getCol1_value()%></td>
											<%} %>
											<%if(packingOrder.getCol2_id()!=null){%>
											<td><%=detail.getCol2_value() == null?"":detail.getCol2_value()%></td>
											<%} %>
											<%if(packingOrder.getCol3_id()!=null){%>
											<td><%=detail.getCol3_value() == null?"":detail.getCol3_value()%></td>
											<%} %>
											<%if(packingOrder.getCol4_id()!=null){%>
											<td><%=detail.getCol4_value() == null?"":detail.getCol4_value()%></td>
											<%} %>
											<td><%=detail.getColor()%></td>
											<td><%=detail.getCartons()%></td>
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
										出入库统计
									</legend>
									<table class="table table-responsive detailTb">
										<thead>
										<tr>
											<%
											if(packingOrder.getCol1_id()!=null){
											 %>
											<th width="80px">
												<%=SystemCache.getPackPropertyName(packingOrder.getCol1_id()) %>
											</th>
											<%} %>
											
											<%if(packingOrder.getCol2_id()!=null){%>
											<th width="80px">
												<%=SystemCache.getPackPropertyName(packingOrder.getCol2_id()) %>
											</th>
											<%} %>
											<%if(packingOrder.getCol3_id()!=null){%>
											<th width="80px">
												<%=SystemCache.getPackPropertyName(packingOrder.getCol3_id()) %>
											</th>
											<%} %>
											<%if(packingOrder.getCol4_id()!=null){%>
											<th width="80px">
												<%=SystemCache.getPackPropertyName(packingOrder.getCol4_id()) %>
											</th>
											<%} %>

											<th width="40px">
												颜色
											</th>
											<th width="60px">
												计划箱数
											</th><th  width="60px">
												计划数量
											</th><th  width="60px">
												入库数量
											</th><th  width="60px">
												退货数量
											</th><th  width="60px">
												发货数量
											</th>
											<th width="60px">
												库存箱数
											</th><th  width="60px">
												库存数量
											</th>
											
										</tr>
									</thead>
									<tbody>
										<%if(storeStock == null){%>
										<p style="color:red;">库存为0，没有库存记录</p>
										<%}else{%>
											<%for(FinishStoreStockDetail detail : storeStock.getDetaillist()){ 
											%>
											<tr>
											<%if(packingOrder.getCol1_id()!=null){ %>
											<td>
												<%=detail.getCol1_value()==null?"":detail.getCol1_value() %>
											</td>
											<%} %>
											<%if(packingOrder.getCol2_id()!=null){ %>
											<td>
												<%=detail.getCol2_value()==null?"":detail.getCol2_value() %>
											</td>
											<%} %>	
											<%if(packingOrder.getCol3_id()!=null){ %>
											<td>
												<%=detail.getCol3_value()==null?"":detail.getCol3_value() %>
											</td>
											<%} %>
											<%if(packingOrder.getCol4_id()!=null){ %>
											<td>
												<%=detail.getCol4_value()==null?"":detail.getCol4_value() %>
											</td>
											<%} %>
											<td><%=detail.getColor() %></td>
											<td><%=detail.getPlan_cartons() %></td>
											<td><%=detail.getPlan_quantity() %></td>
											<td><%=detail.getIn_quantity() %></td>
											<td><%=detail.getReturn_quantity() %></td>
											<td><%=detail.getOut_quantity() %></td>
											<td><%=detail.getStock_cartons() %></td>
											<td><%=detail.getStock_quantity() %></td>
											</tr>
										<%} }%>
									</tbody>
									<tfoot><tr><td>合计</td><td colspan="<%=col+7 %>">总库存箱数：<%=storeStock.getTotal_stock_cartons() %>
										，总库存数量：<%=storeStock.getTotal_stock_quantity() %></td>
										</tr></tfoot>
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
