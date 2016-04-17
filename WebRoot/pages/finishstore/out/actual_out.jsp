<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.entity.finishstore.FinishStoreStockDetail"%>
<%@page import="com.fuwei.entity.finishstore.FinishStoreStock"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	FinishStoreStock storeStock = (FinishStoreStock) request.getAttribute("storeStock");
	List<FinishStoreStockDetail> detaillist = storeStock==null? null : storeStock.getDetaillist();
	if(detaillist == null){
		detaillist = new ArrayList<FinishStoreStockDetail>();
	}
	int total_plan_quantity = 0 ;
	int total_actual_in_quantity = 0;
	for(FinishStoreStockDetail detail : detaillist){
		total_plan_quantity += detail.getPlan_quantity();
		total_actual_in_quantity += detail.getIn_quantity() - detail.getReturn_quantity();
	}
	int total_progress = 0;
	if(total_plan_quantity!=0){
		total_progress = (int)(total_actual_in_quantity/(double)total_plan_quantity * 100);
	}
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>成品生产进度 -- 桐庐富伟针织厂</title>
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
			legend{margin-bottom:0;}
			#stockDetail table thead th{background: #AEADAD;    border: 1px solid #000; text-align: center;}
			#stockDetail table{ border: 1px solid #000;    table-layout: fixed;}
			#stockDetail table tbody td,#stockDetail table tfoot td{border: 1px solid #000;}
			.item{margin-right: 60px;}
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
									<a target="_blank" href="finishstore_workspace/in_out/<%=storeStock.getOrderId() %>" type="button" class="pull-right btn btn-primary">查看成品出入库详情与当前库存</a>
								</div>
								<div class="clear"></div>
								
								
								<fieldset id="stockDetail">
									<legend>
										<span class="item">计划总数量：<%=total_plan_quantity %></span>
									 <span class="item"> 实际总生产数量：<%=total_actual_in_quantity %></span>   <span class="item"> 总进度：<%=total_progress %>%</span>
									</legend>
									<table class="table table-responsive detailTb">
										<thead>
										<tr>
											<th width="80px">
												列1
											</th>
											<th width="80px">
												列2
											</th>
											<th width="80px">
												列3
											</th>
											<th width="80px">
												列4
											</th>
											<th width="40px">
												颜色
											</th>
											<th width="60px">
												计划箱数
											</th><th  width="60px">
												计划数量
											</th>
											<th width="60px">
												实际入库箱数
											</th><th  width="60px">
												实际入库数量
											</th><th  width="80px">
												完成进度
											</th>
											
										</tr>
									</thead>
									<tbody>
										<%if(storeStock == null){%>
										<p style="color:red;">库存为0，没有库存记录</p>
										<%}else{%>
											<%for(FinishStoreStockDetail detail : detaillist){ 
												int actual_in_quantity = detail.getIn_quantity() - detail.getReturn_quantity();
												int plan_quantity = detail.getPlan_quantity();
												int progress = (int)(actual_in_quantity/(double)plan_quantity * 100);
											%>
											<tr>
											<td>
												<%=detail.getCol1_value()==null?"":detail.getCol1_value() %>
											</td>
											<td>
												<%=detail.getCol2_value()==null?"":detail.getCol2_value() %>
											</td>
											<td>
												<%=detail.getCol3_value()==null?"":detail.getCol3_value() %>
											</td>
											<td>
												<%=detail.getCol4_value()==null?"":detail.getCol4_value() %>
											</td>
											<td><%=detail.getColor() %></td>
											<td><%=detail.getPlan_cartons() %></td>
											<td><%=plan_quantity %></td>
											<td><%=detail.getIn_cartons() - detail.getReturn_cartons() %></td>
											<td><%=actual_in_quantity %></td>
											<td><%=progress %>%</td>
											</tr>
										<%} }%>
									</tbody>
								</table>
								</fieldset>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
