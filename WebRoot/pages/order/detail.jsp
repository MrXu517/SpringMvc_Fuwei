<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.OrderStep"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.constant.OrderStatus"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.NumberUtil"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	List<User> userlist = (List<User>) request.getAttribute("userlist");
	Order order = (Order) request.getAttribute("order");
	List<OrderDetail> orderdetaillist = order.getDetaillist();
	if (orderdetaillist == null) {
		orderdetaillist = new ArrayList<OrderDetail>();
	}
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>订单详情 -- 桐庐富伟针织厂</title>
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
		<script src="<%=basePath%>js/plugins/WdatePicker.js"></script>
		<script src="js/order/detail.js" type="text/javascript"></script>
		<link href="css/order/index.css" rel="stylesheet" type="text/css" />
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
							<i class=""></i>
							<a href="order/index">订单列表</a>
						</li>
						<li class="active">
							订单详情
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


									<div class="pull-right">
										<%
											if (order.isEdit()) {
										%>
										<a href="order/put/<%=order.getId()%>" type="button"
											class="btn btn-info"> 编辑订单 </a>
										<%
											}
										%>
									</div>
									<div class="pull-right">
										<button id="addStep" type="button" class="btn btn-info">
											添加步骤
										</button>
									</div>
									<div class="pull-right">
										<button orderId="<%=order.getId()%>" id="exeStep"
											type="button" class="btn btn-danger">
											执行当前步骤
										</button>
									</div>

									<div class="clear"></div>

								</div>
								<%
									if (order.getStatus() != OrderStatus.CANCEL.ordinal()) {
								%>
								<div class="step_widget">
									<%
										List<OrderStep> stepList = order.getStepList();
											if (stepList == null) {
												stepList = new ArrayList<OrderStep>();
											}
											List<List<OrderStep>> stepList10 = new ArrayList<List<OrderStep>>();
											int size = stepList.size();
											int listlist_length = NumberUtil.ceil( size/ 10.0);
											
											for (int k = 0; k < listlist_length; ++k) {
												int begin = k * 10;
												int end = k * 10 + 10;
												if(begin >= size ){
													break;
												}
												if(end >= size){
													end = size;
												}
												stepList10.add(stepList.subList(begin,end ));
											}
											for (List<OrderStep> list : stepList10) {
									%>
									<div class="basic-steps">
										<dl class="tb-desc-segments-list tb-clearfix">
											<dt>
												详情直达
											</dt>
											<%
												for (OrderStep step : list) {
															if (step.getChecked()) {
											%>
											<dd class="tb-desc-segment selected">
												<%
													} else {
												%>
											
											<dd class="tb-desc-segment">
												<%
													}
												%>
												<%
													if (step.getStepId() != null) {//如果是动态生产步骤
												%>
												<a class="tb-desc-segment-link step stepId" href="#"
													data-toggle="tooltip" title=""><%=step.getState()%></a>
												<div class="tooltip top" role="tooltip">
													<div class="tooltip-arrow"></div>
													<div class="tooltip-inner">
														<button type="button" class="editStep btn btn-success"
															data-cid="<%=step.getStepId()%>">
															编辑
														</button>
														<button type="button" class="deleteStep btn btn-danger"
															data-cid="<%=step.getStepId()%>">
															删除
														</button>
													</div>
												</div>
												<%
													} else {
												%>
												<a class="tb-desc-segment-link"><%=step.getState()%></a>
												<%
													}
												%>

											</dd>

											<%
												}
											%>
											<div class="clear"></div>
										</dl>
									</div>
									<%
										}
									%>
								</div>
								<%
									}
								%>
								<div class="clear"></div>
								<div class="col-md-6 detailTb">
									<table class="table table-responsive">
										<tbody>

											<tr>
												<td>
													公司
												</td>
												<td><%=SystemCache.getCompanyName(order.getCompanyId())%></td>
											</tr>
											<tr>
												<td>
													业务员
												</td>
												<td><%=SystemCache.getSalesmanName(order.getSalesmanId())%></td>
											</tr>
											<tr>
												<td>
													金额
												</td>
												<td><%=order.getAmount()%>
												</td>
											</tr>
											<tr>
												<td>
													备注
												</td>
												<td><%=order.getMemo()%></td>
											</tr>
										</tbody>
									</table>

								</div>
								<div class="col-md-6 detailTb">
									<table class="table table-responsive">
										<tbody>
											<tr>
												<td>
													订单签订时间
												</td>
												<td><%=DateTool.formatDateYMD(order.getStart_at())%></td>
											</tr>
											<tr>
												<td>
													订单截止时间
												</td>
												<td><%=DateTool.formatDateYMD(order.getEnd_at())%></td>
											</tr>
											<tr>
												<td>
													订单创建时间
												</td>
												<td>
													<%=order.getCreated_at()%></td>
											</tr>
											<tr>
												<td>
													订单创建人
												</td>
												<td><%=SystemCache.getUserName(order.getCreated_user())%></td>
											</tr>
										</tbody>
									</table>

								</div>
								<div class="clear"></div>
								<table class="table table-responsive" id="detailTable">
									<thead>
										<tr>
											<th>
												图片
											</th>
											<th>
												名称
											</th>
											<th>
												货号
											</th>
											<th>
												材料
											</th>
											<th>
												克重
											</th>
											<th>
												尺寸
											</th>
											<th>
												打样人
											</th>
											<th>
												单价
											</th>
											<th>
												数量
											</th>
											<th>
												金额
											</th>
										</tr>
									</thead>
									<tbody>
										<%
											for (OrderDetail detail : orderdetaillist) {
										%>
										<tr data_detail='<%=SerializeTool.serialize(detail)%>'>

											<td
												style="max-width: 120px; height: 120px; max-height: 120px;">
												<a target="_blank" class="cellimg"
													href="/<%=detail.getImg()%>"><img
														style="max-width: 120px; height: 120px; max-height: 120px;"
														src="/<%=detail.getImg_ss()%>"> </a>
											</td>
											<td><%=detail.getName()%></td>
											<td><%=detail.getProductNumber()%></td>
											<td><%=detail.getMaterial()%></td>
											<td><%=detail.getWeight()%></td>
											<td><%=detail.getSize()%></td>
											<td><%=SystemCache.getUserName(detail
										.getCharge_user())%></td>
											<td class="price"><%=detail.getPrice()%></td>
											<td><%=detail.getQuantity()%></td>
											<td class="amount"><%=detail.getAmount()%></td>
										</tr>
										<%
											}
										%>
									</tbody>
								</table>
							</div>


						</div>
					</div>

				</div>
			</div>
		</div>

		<div class="modal fade" id="stepModal">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
						</button>
						<h4 class="modal-title">
							新建步骤
						</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal stepform" role="form">
							<input type="hidden" name="id" id="id" />
							<input type="hidden" id="orderId" name="orderId"
								value="<%=order.getId()%>" />
							<div class="row">
								<div class="form-group">
									<label for="name" class="col-sm-3 control-label">
										步骤名称
									</label>
									<div class="col-sm-8">
										<input type="text" name="name" id="name"
											class="form-control require">
									</div>
								</div>
							</div>
							<div class="modal-footer">
								<button type="submit" class="btn btn-primary"
									data-loading-text="正在保存...">
									新建步骤
								</button>
								<button type="reset" class="btn btn-default"
									data-dismiss="modal">
									取消
								</button>
							</div>
						</form>
					</div>

				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
		</div>
		<!-- /.modal -->
	</body>
</html>
