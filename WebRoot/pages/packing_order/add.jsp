<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.entity.Material"%>
<%@page import="com.fuwei.entity.Customer"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.entity.ordergrid.MaterialPurchaseOrderDetail"%>
<%@page import="com.fuwei.entity.finishstore.PackProperty"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Order order = (Order) request.getAttribute("order");
	List<PackProperty> propertylist = SystemCache.packpropertylist;
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>创建装箱单 -- 桐庐富伟针织厂</title>
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

		<link href="css/order/bill.css" rel="stylesheet" type="text/css" />
		<script src="js/order/ordergrid.js" type="text/javascript"></script>
		<script src="js/packing_order/add.js" type="text/javascript"></script>
		<style type="text/css">
		#saveTb thead th, #mainTb tbody tr td {
		    border-color: #000;
		}
		#saveTb thead th {
		    background: #AEADAD;    border: 1px solid #000; text-align: center;padding: 0 3px;
		}
		#saveTb tbody td,#saveTb tfoot td {
		     border: 1px solid #000; text-align: center;padding: 0;
		}
		select.colselect{padding:0;}
		#saveTb tbody td [disabled],select[disabled]{cursor: not-allowed;background: #ccc;}
		#saveTb{ border: 1px solid #000;    table-layout: fixed;}
		.colable{width:20px;height:20px;}
		#saveTb tbody td input{width:100%;}
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
							<a href="packing_order/list/<%=order.getId()%>">订单 -- 装箱单</a>
						</li>
						<li class="active">
							创建装箱单
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid orderWidget">
						<div class="row">
							<form class="saveform">
								<input type="hidden" id="orderId" name="orderId"
									value="<%=order.getId()%>" class="require" />
								<button type="submit"
									class="pull-right btn btn-danger saveTable"
									data-loading-text="正在保存...">
									创建装箱单
								</button>

								<div class="clear"></div>
								<div class="col-md-12 tablewidget">
									<table class="table">
										<caption id="tablename">
											桐庐富伟针织厂装箱单(<%=DateTool.nowYear() %>年,<%=order.getCompany_productNumber() %><%=order.getName() %>)
										</caption>
									</table>
									<table class="tableTb noborder">
										<tbody>
											<tr>
												<td width="20%">
														跟单人：<%=SystemCache.getEmployeeName(order.getCharge_employee())%>
												</td>
												<td width="20%">
														订单号：<%=order.getOrderNumber()%>
												</td>
												<td width="60%">
													<div class="form-group" style="width: auto;">
														备注：<input style="width: 350px;" type="text" class="form-control"
																			name="memo" id="memo" placeholder="备注">
													</div>
												</td>
											</tr>
										</tbody>
									</table>
									<table>
										<tbody>
											<tr>
												<td>
													<table class="table table-responsive detailTb" id="saveTb">
														<caption>
															<button type="button"
																class="btn btn-primary addRow pull-left">
																添加一行
															</button>
														</caption>
														<thead>
										
										<tr>
											<th rowspan="2" width="80px">
												<input type="checkbox" class="colable">
												<select name="col1_id" class="colselect form-control" id="col1id">
												<%for(int i = 0 ; i < propertylist.size();++i){ 
													PackProperty item = propertylist.get(i);
													if(i == 0){
												%>
												<option selected value="<%=item.getId() %>"><%=item.getName() %></option>
												<%}else{ %>
													<option value="<%=item.getId() %>"><%=item.getName() %></option>
												<%} }%></select>
											</th>
											<th rowspan="2" width="80px">
												<input type="checkbox" class="colable">
												<select name="col2_id" class="colselect form-control"  id="col2id">
												<%for(int i = 0 ; i < propertylist.size();++i){ 
													PackProperty item = propertylist.get(i);
													if(i == 1){
												%>
												<option selected value="<%=item.getId() %>"><%=item.getName() %></option>
												<%}else{ %>
													<option value="<%=item.getId() %>"><%=item.getName() %></option>
												<%}}%></select>
											</th><th rowspan="2" width="80px">
												<input type="checkbox" class="colable">
												<select name="col3_id" class="colselect form-control" id="col3id">
												<%for(int i = 0 ; i < propertylist.size();++i){ 
													PackProperty item = propertylist.get(i);
													if(i == 2){
												%>
												<option selected value="<%=item.getId() %>"><%=item.getName() %></option>
												<%}else{ %>
													<option value="<%=item.getId() %>"><%=item.getName() %></option>
												<%} }%></select>
											</th>
											<th rowspan="2" width="80px">
												<input type="checkbox" class="colable">
												<select name="col4_id" class="colselect form-control" id="col4id">
												<%for(int i = 0 ; i < propertylist.size();++i){ 
													PackProperty item = propertylist.get(i);
													if(i == 3){
												%>
												<option selected value="<%=item.getId() %>"><%=item.getName() %></option>
												<%}else{ %>
													<option value="<%=item.getId() %>"><%=item.getName() %></option>
												<%} }%></select>
											</th>
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
											<th rowspan="2" width="40px">
												操作
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
				</div>
			</div>
		</div>
	</body>
</html>