<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.OrderStep"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.constant.OrderStatus"%>
<%@page import="com.fuwei.constant.OrderStatusUtil"%>
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


	String exeStr = OrderStatusUtil.get(order.getStatus()).getStepName();

	List<OrderDetail> DetailList = order == null || order.getDetaillist() == null ? new ArrayList<OrderDetail>()
			: order.getDetaillist();
			
	Boolean has_order_detail_price = SystemCache.hasAuthority(session,"order/detail/price");
	//Boolean error_notification = order.getStatus() > OrderStatus.BEFOREPRODUCESAMPLE.ordinal() && order.getStart_produce() == null; //若已经进入生产阶段，但却没有生产单，则显示生成生产单按钮
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
									<%if(exeStr!=null){ %>
									<div class="pull-left">
										<button orderId="<%=order.getId()%>" id="exeStep"
											type="button" class="btn btn-danger">
											<%=exeStr%>
										</button>
									</div>
									<%} %>


									<div class="pull-right" style="margin-right: 0;">
										<%
											if (order.isEdit()) {
										%>
										<a href="order/put/<%=order.getId()%>" type="button"
											class="btn btn-info"> 编辑订单 </a>
										<%
											}
										%>
									</div>
									<!-- 
									<div class="pull-right">
										<button id="addStep" type="button" class="btn btn-info">
											添加步骤
										</button>
									</div>
									 -->
									
									
									<div class="pull-right">
										<a href="order/tablelist?orderId=<%=order.getId()%>"
											class="btn btn-default" type="button"> 进入生产系统 </a>
									</div>
									<div class="pull-right">
										<a href="printorder/print?orderId=<%=order.getId()%>"
											target="_blank" type="button" class="btn btn-success"
											data-loading-text="正在打印..."> 打印表格 </a>
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
											int listlist_length = NumberUtil.ceil(size / 10.0);

											for (int k = 0; k < listlist_length; ++k) {
												int begin = k * 10;
												int end = k * 10 + 10;
												if (begin >= size) {
													break;
												}
												if (end >= size) {
													end = size;
												}
												stepList10.add(stepList.subList(begin, end));
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
								<div class="col-md-12 deliveryDiv">
									<div class="pull-left">
										<label class="control-label">
											发货时间：
										</label>
										<span><%=order.getDevelivery()%></span>
										<%if(order.isOverEnded()){ %>
											<span class="label label-danger">已超期</span>
										<%}else if(order.isPre30()){ %>
											<span class="label label-warning">交货时间<=30天</span>
										<%} %>
									</div>
									<div class="pull-left" style="margin-left: 30px;margin-top: -10px;"><a target="_blank" href="packing_order/list/<%=order.getId() %>" type="button" class="btn btn-primary">查看装箱单</a>
										<a href="order/progress/<%=order.getId() %>" target="_blank" type="button" class="btn btn-primary"
						data-loading-text=""> 查看生产进度 </a>
										<a target="_blank" href="order/fuliao_progress/<%=order.getId() %>" type="button" class="btn btn-primary">查看辅料</a>
									</div>

								</div>
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
													客户
												</td>
												<td><%=SystemCache.getCustomerName(order.getCustomerId())%></td>
											</tr>
											<%if(has_order_detail_price){ %>
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
												<td><%=order.getMemo() == null ? "" : order.getMemo()%></td>
											</tr>
											<%}else{ %>
											<tr>
												<td>
													备注
												</td>
												<td><%=order.getMemo() == null ? "" : order.getMemo()%></td>
											</tr>
											<tr>
												<td colspan="2">&nbsp;
												</td>
											</tr>
											<%} %>
											
										</tbody>
									</table>

								</div>
								<div class="col-md-6 detailTb">
									<table class="table table-responsive">
										<tbody>
											<tr>
												<td>
													签订时间
												</td>
												<td><%=DateTool.formatDateYMD(order.getStart_at())%></td>
											</tr>
											<tr>
												<td>
													截止时间
												</td>
												<td><%=DateTool.formatDateYMD(order.getEnd_at())%></td>
											</tr>
											<tr>
												<td>
													创建时间
												</td>
												<td>
													<%=order.getCreated_at()%></td>
											</tr>
											<tr>
												<td>
													最近更新时间
												</td>
												<td><%=order.getUpdated_at()%></td>
											</tr>
											<tr>
												<td>
													创建人
												</td>
												<td><%=SystemCache.getUserName(order.getCreated_user())%></td>
											</tr>

										</tbody>
									</table>

								</div>

								<div class="clear"></div>
								<fieldset>
									<legend>
										样品信息
									</legend>
									<div class="sampleImg">

										<a href="#" class="thumbnail"> <img id="previewImg"
												alt="400 x 100%" src="/<%=order.getImg_s()%>"> </a>

									</div>
									<div class="sampleData">
										<table class="table table-responsive">
											<tbody>
												<tr>
													<td>
														品名
													</td>
													<td><%=order.getName()%></td>
												</tr>
												<tr>
													<td>
														公司货号
													</td>
													<td><%=order.getCompany_productNumber()%></td>
												</tr>
												<tr>
													<td>
														货号
													</td>
													<td><%=order.getProductNumber()%></td>
												</tr>
												<tr>
													<td>
														跟单人
													</td>
													<td><%=SystemCache.getEmployeeName(order.getCharge_employee())%></td>
												</tr>
												<tr>
													<td>
														材料
													</td>
													<td><%= SystemCache.getMaterialName(order
					.getMaterialId())%></td>
												</tr>
												<tr>
													<td>
														克重
													</td>
													<td><%=order.getWeight()%>克
													</td>
												</tr>
												<tr>
													<td>
														尺寸
													</td>
													<td><%=order.getSize()%></td>
												</tr>
												<%if(has_order_detail_price){ %>
												<tr>
													<td>
														成本
													</td>
													<td>
														<span class="RMB">￥</span><%=order.getCost()%></td>
												</tr>
												<%} %>
											


											</tbody>
										</table>

									</div>
								</fieldset>

								<fieldset id="orderDetail">
									<legend>
										颜色及数量
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
													订单数量
												</th>
												<%if(has_order_detail_price){ %>
												<th width="15%">
													单价
												</th>
												<%} %>
											</tr>
										</thead>
										<tbody>
											<%
												for (OrderDetail detail : DetailList) {
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
												<%if(has_order_detail_price){ %>
												<td class="price"><%=detail.getPrice()%>
												</td>
												<%} %>
											</tr>

											<%
												}
											%>

										</tbody>
									</table>
								</fieldset>
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


		<div class="modal fade" id="notificationModal">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
						</button>
						<h4 class="modal-title">
							创建生产单
						</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal notificationform" role="form">
							<input type="hidden" name="id" id="id" />
							<input type="hidden" id="orderId" name="orderId"
								value="<%=order.getId()%>" />
							<div class="row">
								<div class="form-group">
									<label for="processfactory" class="col-sm-3 control-label">
										加工工厂
									</label>
									<div class="col-sm-8">
										<input type="text" name="processfactory" id="processfactory"
											class="form-control require">
									</div>
								</div>
							</div>
							<div class="modal-footer">
								<button type="submit" class="btn btn-primary"
									data-loading-text="正在保存...">
									确定
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

	<%
	int status = order.getStatus();
	if (status == OrderStatus.DELIVERING.ordinal()) { %>
	<!--执行步骤对话框 对话框 -->
		<div class="modal fade" id="exeStepDialog">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
						</button>
						<h4 class="modal-title">
							请填写  <%=exeStr%>  的时间

						</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal exeform" role="form">
							<input type="hidden" id="orderId" name="orderId"
										value="<%=order.getId()%>" class="require" />
							<div class="form-group col-md-12">
								<label for="color" class="col-sm-3 control-label">
									 <%=exeStr%> 的时间
								</label>
								<div class="col-sm-8">
									<input type="text" name="delivery_at" id="delivery_at"
										class="form-control require date" value="<%=DateTool.formatDateYMD(DateTool.now()) %>"/>
								</div>
								<div class="col-sm-1"></div>
							</div>
						
							<div class="modal-footer">
								<button type="submit" class="btn btn-primary"
									data-loading-text="正在执行...">
									确定
								</button>
								<button type="button" class="btn btn-default"
									data-dismiss="modal">
									关闭
								</button>
							</div>
						</form>
					</div>

				</div>
			</div>
		</div>
		<!--
						 执行步骤对话框 -->
		<%} %>
	</body>
</html>
