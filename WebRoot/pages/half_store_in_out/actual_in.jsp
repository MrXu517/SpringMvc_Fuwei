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
<%@page import="com.fuwei.entity.producesystem.HalfInOut"%>
<%@page import="com.fuwei.entity.producesystem.HalfInOutDetail"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Order order = (Order) request.getAttribute("order");
	Map<Integer,Map<Integer,List<Map<String,Object>>>> resultMap = (Map<Integer,Map<Integer,List<Map<String,Object>>>>) request.getAttribute("resultMap");
	Map<Integer , Map<String,Object>> gongxuQuantityMap = (Map<Integer , Map<String,Object>>)request.getAttribute("gongxuQuantityMap");
	Map<Integer , Map<Integer,Map<String,Object>>> factoryQuantityMap = (Map<Integer , Map<Integer,Map<String,Object>>>)request.getAttribute("factoryQuantityMap");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>半成品生产进度 -- 桐庐富伟针织厂</title>
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
			.table-bordered>tbody>tr>td{border-color: #000;}
			.table-bordered>thead>tr>th,.table-bordered>tbody>tr>td{border-color: #000;font-weight:bold;font-size:16px;text-align: center;}
			#storeDetail legend{font-weight: bold;}
			legend{margin-bottom:0;  border: 2px solid #000;  border-bottom: none;background: #E7CBBE;}
			#storeDetail thead th{text-align:center;}
			span.item{margin-left:60px;}
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
										<span><%=order.getOrderNumber()%></span>
									</div>
									<div class="pull-left">
										<label class="control-label">
											公司货号：
										</label>
										<span><%=order.getCompany_productNumber()%></span>
									</div>
									<div class="pull-left">
										<label class="control-label">
											品名：
										</label>
										<span><%=order.getName()%></span>
									</div>
								

									<div class="clear"></div>

								</div>
								<div class="clear"></div>

							<div class="" id="storeDetail">
								<%for(Integer gongxuId:resultMap.keySet()){
									Map<String,Object> gongxuQuantity = gongxuQuantityMap.get(gongxuId);
									int gongxuprogress = (int)((Integer)gongxuQuantity.get("in_quantity")/(double)(Integer)gongxuQuantity.get("plan_quantity") * 100);
								 %>
								<fieldset>
									<legend>
										工序：<%=SystemCache.getGongxuName(gongxuId) %>  <span class="item">计划数量：<%=gongxuQuantity.get("plan_quantity") %></span>
									 <span class="item"> 实际生产数量：<%=gongxuQuantity.get("in_quantity") %></span>   <span class="item"> 进度：<%=gongxuprogress %>%</span>
									</legend>
								<%
								Map<Integer,List<Map<String,Object>>> map = resultMap.get(gongxuId);
								for(Integer factoryId : map.keySet()){
									List<Map<String,Object>> detaillist = map.get(factoryId);
									Map<String,Object> factoryQuantity = factoryQuantityMap.get(gongxuId).get(factoryId);
								%>
									<table class="table table-responsive detailTb table-bordered">
										<thead>
											<tr style="height:0;">
												<th style="width:70px"></th>
	    										<th style="width:70px"></th>
	    										<th style="width:55px"></th>
	    										<th style="width:60px"></th>
	    										<th style="width:50px"></th>
	    										<th style="width:50px"></th>
	    										<th style="width:30px"></th>
	    										<th style="width:70px"></th>
	    										<th style="width:70px"></th>
	    										<th style="width:60px"></th>
  											</tr>
											<tr>
												<th rowspan="2" width="70px">
													工序
												</th>
												<th rowspan="2" width="70px">
													加工工厂
												</th>
												<th colspan="5" width="200px">
													颜色及数量
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
											<tr><th width="55px">
												颜色
											</th><th  width="60px">
												尺寸
											</th><th  width="50px">
												计划数量
											</th><th  width="50px">
												实际生产
											</th><th  width="30px">
												进度
											</th></tr>
										</thead>
										<tbody>
										<%
											int detailsize = detaillist.size();
											int count = 0 ;
											for (Map<String,Object> item: detaillist) {
												int progress = (int)((Integer)item.get("in_quantity")/(double)(Integer)item.get("total_quantity") * 100);
												int factoryprogress = (int)((Integer)factoryQuantity.get("in_quantity")/(double)(Integer)factoryQuantity.get("plan_quantity") * 100);
													
										%>
										<%if(count == 0){ %>
										<tr>
											<td rowspan="<%=detailsize%>"><%=SystemCache.getGongxuName(gongxuId)%></td>
											<td rowspan="<%=detailsize%>"><%=SystemCache.getFactoryName(factoryId)%></td>
											<td><%=item.get("color")%></td>
											<td><%=item.get("size")%></td>
											<td><%=item.get("total_quantity")%></td>
											<td><%=item.get("in_quantity")%></td>
											<td><%=progress %>%</td>

											<td rowspan="<%=detailsize%>"><%=factoryQuantity.get("plan_quantity")%></td>				
											<td rowspan="<%=detailsize%>"><%=factoryQuantity.get("in_quantity")%></td>				
											<td rowspan="<%=detailsize%>"><%=factoryprogress%>%</td>				
										</tr>
										
										<%}else{
										%>
										<tr>
											<td><%=item.get("color")%></td>
											<td><%=item.get("size")%></td>
											<td><%=item.get("total_quantity")%></td>
											<td><%=item.get("in_quantity")%></td>
											<td><%=progress%>%</td>
										</tr>
										<%} 
										++count;
										%>
										<%	
											}
										%>
										</tbody>
									</table>
									<%} %>
								</fieldset>
								<%} %>
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
