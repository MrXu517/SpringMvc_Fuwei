<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.NumberUtil"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.entity.producesystem.StoreInOut"%>
<%@page import="com.fuwei.entity.producesystem.StoreInOutDetail"%>
<%@page import="com.fuwei.entity.ordergrid.StoreOrder"%>
<%@page import="com.fuwei.entity.ordergrid.StoreOrderDetail"%>
<%@page import="com.fuwei.entity.producesystem.MaterialInOut"%>
<%@page import="com.fuwei.entity.producesystem.MaterialInOutDetail"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	StoreOrder storeOrder = (StoreOrder) request.getAttribute("storeOrder");
	Map<String, Map<String, Object>> color_materialTotalMap = (Map<String, Map<String, Object>>) request
			.getAttribute("color_materialTotalMap");
	Map<String, Map<Integer, Double>> actualInMap = (Map<String, Map<Integer, Double>>) request
			.getAttribute("actualInMap");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>原材料生产进度 -- 桐庐富伟针织厂</title>
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
.table>thead>tr>th {
	padding: 0 8px;
	vertical-align: middle;
}

.table>thead>tr {
	background: #AEADAD;
}

.table tbody tr {
	background: #ddd;
}

.thumbnail>img {
	max-width: 300px;
	max-height: 200px;
}

.table-bordered>tbody>tr>td {
	border-color: #000;
}

.table-bordered>thead>tr>th,.table-bordered>tbody>tr>td {
	border-color: #000;
	font-weight: bold;
	font-size: 16px;
	text-align: center;
}

#storeDetail legend {
	font-weight: bold;
}

legend {
	margin-bottom: 0;
	border: 2px solid #000;
	border-bottom: none;
	background: #E7CBBE;
}

#storeDetail thead th {
	text-align: center;
}

span.item {
	margin-left: 60px;
}
</style>
	</head>
	<body>
		<div id="Content">
			<div id="main">
				<div class="body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 formwidget">
								<div class="head">
									<div class="pull-left">
										<label class="control-label">
											订单编号：
										</label>
										<span><%=storeOrder.getOrderNumber()%></span>
									</div>
									<div class="pull-left">
										<label class="control-label">
											公司货号：
										</label>
										<span><%=storeOrder.getCompany_productNumber()%></span>
									</div>
									<div class="pull-left">
										<label class="control-label">
											品名：
										</label>
										<span><%=storeOrder.getName()%></span>
									</div>


									<div class="clear"></div>

								</div>
								<div class="clear"></div>

								<div class="" id="storeDetail">
									<table class="table table-responsive detailTb table-bordered">
										<thead>
											<tr style="height: 0;">
												<th style="width: 70px"></th>
												<th style="width: 70px"></th>
												<th style="width: 60px"></th>
												<th style="width: 60px"></th>
												<th style="width: 70px"></th>
												<th style="width: 70px"></th>
												<th style="width: 60px"></th>
											</tr>
											<tr>
												<th rowspan="2" width="70px">
													色号
												</th>
												<th rowspan="2" width="70px">
													材料
												</th>
												<th colspan="2" width="200px">
													各染厂实际生成数量
												</th>
												<th rowspan="2" width="70px">
													总计划数量
												</th>
												<th rowspan="2" width="70px">
													实际总生产数量
												</th>
												<th rowspan="2" width="60px">
													总进度
												</th>
											</tr>
											<tr>
												<th width="60px">
													染厂
												</th>
												<th width="60px">
													实际生产
												</th>
											</tr>
										</thead>
										<tbody>
											<%
												for (String key : color_materialTotalMap.keySet()) {
													int indexOf = key.indexOf(":");
													Integer material = Integer.parseInt(key.substring(0, indexOf));
													String color = key.substring(indexOf + 1);
													Map<String, Object> data = color_materialTotalMap.get(key);
													double total_plan_quantity = (Double) data
															.get("total_plan_quantity");
													double total_actual_in_quantity = (Double) data
															.get("total_actual_in_quantity");
													int progress = (int) (total_actual_in_quantity
															/ total_plan_quantity * 100);
													Map<Integer, Double> factoryMap = actualInMap.get(key);
													if(factoryMap == null || factoryMap.size()<=0){
														factoryMap.put(-1,0.0);
													}
													int detailsize = factoryMap.size();
													int count = 0;
													for (Integer factoryId : factoryMap.keySet()) {
														double actual_in_quantity = (Double) factoryMap
																.get(factoryId);
														if (count == 0) {
											%>
											<tr>
												<td rowspan="<%=detailsize%>"><%=color%></td>
												<td rowspan="<%=detailsize%>"><%=SystemCache.getMaterialName(material)%></td>
												<%if(factoryId == -1){
												%>
												<td colspan="2">没有入库记录</td>
												<%}else{%>
												<td><%=SystemCache.getFactoryName(factoryId)%></td>
												<td><%=actual_in_quantity%></td>
												<%}%>
												<td rowspan="<%=detailsize%>"><%=total_plan_quantity%></td>
												<td rowspan="<%=detailsize%>"><%=total_actual_in_quantity%></td>
												<td rowspan="<%=detailsize%>"><%=progress%>%
												</td>
											</tr>

											<%
												} else {
											%>
											<tr>
												<td><%=SystemCache.getFactoryName(factoryId)%></td>
												<td><%=actual_in_quantity%></td>
											</tr>
											<%
												}
														++count;
													}
												}
											%>
										</tbody>
									</table>
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
