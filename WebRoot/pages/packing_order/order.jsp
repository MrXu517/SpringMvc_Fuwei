<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.finishstore.PackingOrder"%>
<%@page import="com.fuwei.entity.finishstore.PackingOrderDetail"%>
<%@page import="com.fuwei.util.DateTool"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	int orderId = (Integer)request.getAttribute("orderId");
	List<PackingOrder> packingOrderlist = (List<PackingOrder>)request.getAttribute("packingOrderList");
	Boolean has_packing_order_add = SystemCache.hasAuthority(session,"packing_order/add");

%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>订单装箱单 -- 桐庐富伟针织厂</title>
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
		<script src="js/plugins/jquery.form.js" type="text/javascript"></script>
		<link href="css/packing_order/index.css" rel="stylesheet"
			type="text/css" />
	<style type="text/css">
		.panel-title span{
			margin-left:40px;
		}
		.panel-title span:first-child{
			margin-left:0;
		}
		a.detail{
			font-weight: bold;
  			text-decoration: underline;
		}
		#saveTb tbody td input{width:100%;}
		#saveTb tfoot td{text-align: right;}
		#saveTb tbody td [disabled],select[disabled]{cursor: not-allowed;background: #ccc;}
		#saveTb{ border: 1px solid #000;    table-layout: fixed;}
		#saveTb thead th, #mainTb tbody tr td {
		    border-color: #000;
		}
		#saveTb thead th {
		    background: #AEADAD;    border: 1px solid #000; text-align: center;padding: 0 3px;
		}
		#saveTb tbody td,#saveTb tfoot td {
		     border: 1px solid #000; text-align: center;padding: 0;
		}
		#saveTb tfoot td{text-align: right;}
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
							<a href="order/detail/<%=orderId%>">订单详情</a>
						</li>
						<li class="active">
							装箱单
						</li>
					</ul>
				</div>
				<div class="body">

					<div class="container-fluid">
						<%if(has_packing_order_add){ %>
						<a href="packing_order/add/<%=orderId %>" class="btn btn-primary" style="margin-bottom:10px;">上传新的装箱单</a>
						<%} %>
						<%if(packingOrderlist.size()<=0){ %>
							<span>还未创建装箱单</span>
						<%} %>
						<%
						int count = 1;
						int size = packingOrderlist.size();
						for(PackingOrder packingOrder : packingOrderlist){ 
							List<PackingOrderDetail> detaillist = packingOrder.getDetaillist();
							if(detaillist == null){
								detaillist = new ArrayList<PackingOrderDetail>();
							}
						%>
						<div class="row">
							<div class="col-md-12 formwidget">
								<div class="panel panel-info">
									<div class="panel-heading">
										<h3 class="panel-title">
										<span>[<%=count %>/<%=size %>]</span>	<span>创建时间:<%=packingOrder.getCreated_at() %> </span> <span>创建用户:<%=SystemCache.getUserName(packingOrder.getCreated_user())%></span>   <span class="memoSpan">备注: <%=packingOrder.getMemo() %></span>
										<a target="_blank" title="点击查看装箱单详细内容" class="detail pull-right" href="packing_order/detail/<%=packingOrder.getId() %>">详情</a>
										</h3>
									</div>
									<div class="panel-body">
										<table class="table">
										<caption id="tablename">
											桐庐富伟针织厂装箱单(<%=DateTool.getYear(packingOrder.getCreated_at()) %>年,<%=packingOrder.getCompany_productNumber() %><%=packingOrder.getName() %>)
										</caption>
									</table>
									<table class="tableTb noborder">
										<tbody>
											<tr>
												<td width="20%">
														跟单人：<%=SystemCache.getEmployeeName(packingOrder.getCharge_employee())%>
												</td>
												<td width="20%">
														订单号：<%=packingOrder.getOrderNumber()%>
												</td>
												<td width="60%">
														备注：<%=packingOrder.getMemo() %>
												</td>
											</tr></tbody>
									</table>
									<table class="table table-responsive detailTb" id="saveTb">
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
										<%for(PackingOrderDetail detail : detaillist){ %>
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
									</div>

								</div>
							</div>


						</div>
						<%++count;} %>
					</div>

				</div>
			</div>
		</div>
	</body>
</html>