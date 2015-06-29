<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.Material"%>

<%@page import="com.fuwei.entity.ordergrid.HeadBankOrder"%>
<%@page import="com.fuwei.entity.ordergrid.HeadBankOrderDetail"%>
<%@page import="com.fuwei.entity.ordergrid.ProducingOrder"%>
<%@page import="com.fuwei.entity.ordergrid.ProducingOrderDetail"%>
<%@page import="com.fuwei.entity.ordergrid.ProducingOrderMaterialDetail"%>
<%@page import="com.fuwei.entity.ordergrid.PlanOrder"%>
<%@page import="com.fuwei.entity.ordergrid.PlanOrderDetail"%>
<%@page import="com.fuwei.entity.ordergrid.PlanOrderProducingDetail"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Order order = (Order) request.getAttribute("order");
	List<OrderDetail> DetailList = order == null || order.getDetaillist() == null ? new ArrayList<OrderDetail>()
			: order.getDetaillist();
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
		<script src="js/plugins/jquery.form.js" type="text/javascript"></script>
		<link href="css/packing_order/index.css" rel="stylesheet" type="text/css" />
		<script src="js/packing_order/add.js" type="text/javascript"></script>

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
							创建装箱单
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid">
						
						<div class="row">
							<div class="col-md-6 formwidget">
								<div class="panel panel-primary">
									<div class="panel-heading">
										<h3 class="panel-title">
											上传装箱文件
										</h3>
									</div>
									<div class="panel-body">

										<form class="form-horizontal form" role="form" method="post"
											enctype="multipart/form-data">
											<!-- <input type="hidden" name="id" value="" /> -->
											<input type="hidden" name="orderId"
												value="<%=order.getId()%>" />
											<div class="col-md-12">
												<div class="form-group">
													<label for="file" class="col-sm-3 control-label">
														装箱excel
													</label>
													<div class="col-sm-8">
														<input type="file" name="file" id="file"
															class="form-control require" placeholder="请上传装箱文件" />

													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="memo" class="col-sm-3 control-label">
														备注
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control" name="memo"
															id="memo" placeholder="备注">
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<div class="col-sm-offset-3 col-sm-5">
														<button id="previewBtn" type="button" class="btn btn-primary">
															预览
														</button>
														<button id="uploadBtn" type="button" class="btn btn-primary"
															data-loading-text="正在上传...">
															上传
														</button>

													</div>
													<div class="col-sm-3">
														<button type="reset" class="reset btn btn-default">
															重置表单
														</button>
													</div>
													<div class="col-sm-1"></div>
												</div>
											</div>
											
										</form>
									</div>

								</div>
								<table class="table">
								<thead>
									<tr>
										<td colspan="3" class="pull-right orderNumber">
											№：<%=order.getOrderNumber()%></td>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td>
											<table class="table table-responsive table-bordered tableTb">
												<tbody>
													<tr>
														<td rowspan="7" width="50%">
															<a href="/<%=order.getImg()%>" class="thumbnail"
																target="_blank"> <img id="previewImg"
																	alt="200 x 100%" src="/<%=order.getImg_s()%>"> </a>
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
															公司货号
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
								</tbody>
							</table>
								<table class="table table-responsive detailTb table-bordered">
										<caption>
										</caption>
										<thead>
											<tr>
												<th width="15%">
													颜色
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
											<tr class="tr">
												<td class="color"><%=detail.getColor()%>
												</td>
												<td class="quantity"><%=detail.getQuantity()%>
												</td>
											</tr>

											<%
												}
											%>

										</tbody>
									</table>
							</div>
							<div class="col-md-6">
								<p>预览装箱文件：</p>
								<iframe name="previewContent" id="previewContent"></iframe>						</div>
						
							</div>

						</div>

						
				</div>
			</div>


		</div>


		</div>
	</body>
</html>