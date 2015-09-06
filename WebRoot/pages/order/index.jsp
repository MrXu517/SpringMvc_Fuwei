<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.commons.Pager"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.constant.OrderStatus"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.fuwei.entity.Employee"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Pager pager = (Pager) request.getAttribute("pager");
	if (pager == null) {
		pager = new Pager();
	}
	List<Order> orderlist = new ArrayList<Order>();
	if (pager != null & pager.getResult() != null) {
		orderlist = (List<Order>) pager.getResult();
	}

	Date start_time = (Date) request.getAttribute("start_time");
	String start_time_str = "";
	if (start_time != null) {
		start_time_str = DateTool.formatDateYMD(start_time);
	}
	Date end_time = (Date) request.getAttribute("end_time");
	String end_time_str = "";
	if (end_time != null) {
		end_time_str = DateTool.formatDateYMD(end_time);
	}

	Integer charge_employeeId = (Integer) request.getAttribute("charge_employeeId");
	Integer companyId = (Integer) request.getAttribute("companyId");
	String company_str = "";
	String charge_employeeId_str = "";
	if (charge_employeeId != null) {
		charge_employeeId_str = String.valueOf(charge_employeeId);
	}
	if (companyId != null) {
		company_str = String.valueOf(companyId);
	}
	if (charge_employeeId == null) {
		charge_employeeId = -1;
	}
	if (companyId == null) {
		companyId = -1;
	}
	//订单状态status
	Integer status = (Integer) request.getAttribute("status");
	String status_str = "";
	if (status != null) {
		status_str = String.valueOf(status);
	}
	//订单状态status
	HashMap<String, List<Salesman>> companySalesmanMap = SystemCache
			.getCompanySalesmanMap_ID();
	JSONObject jObject = new JSONObject();
	jObject.put("companySalesmanMap", companySalesmanMap);
	String companySalesmanMap_str = jObject.toString();
	List<Employee> employeelist = (List<Employee>) request.getAttribute("employeelist");
	
	//权限相关
	Boolean has_order_detail = SystemCache.hasAuthority(session,
			"order/detail");
	Boolean has_order_edit = SystemCache.hasAuthority(session,
			"order/edit");
	Boolean has_order_cancel = SystemCache.hasAuthority(session,
			"order/cancel");
	//权限相关
%>
<!DOCTYPE html>

<html>
	<head>
		<base href="<%=basePath%>">
		<title>订单管理 -- 桐庐富伟针织厂</title>
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
		<script src="<%=basePath%>js/plugins/WdatePicker.js"></script>
		<script src="js/common/common.js" type="text/javascript"></script>
		<script src="js/order/index.js" type="text/javascript"></script>
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
						<li class="active">
							订单列表
						</li>
					</ul>
				</div>
				<div class="body">

					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 tablewidget">
								<!-- Table -->
								<div clas="navbar navbar-default">
									<form class="form-horizontal searchform form-inline searchform"
										role="form">
										
										<div class="form-group">
											<label for="status" class="col-sm-3 control-label">
												状态
											</label>
											<div class="col-sm-9">
												<select class="form-control" name="status" id="status">
													<option value="">
														所有
													</option>
													<%
														for (OrderStatus orderStatus : OrderStatus.values()) {
															if (status!=null && orderStatus.ordinal() == status) {
													%>
													<option value="<%=orderStatus.ordinal()%>" selected><%=orderStatus.getName()%></option>
													<%
														} else {
													%>
													<option value="<%=orderStatus.ordinal()%>"><%=orderStatus.getName()%></option>
													<%
														}
														}
													%>
												</select>
											</div>
										</div>
										<div class="form-group salesgroup">
											<label for="companyId" class="col-sm-3 control-label">
												公司
											</label>
											<div class="col-sm-9">
												<select data='<%=companySalesmanMap_str%>'
													class="form-control" name="companyId" id="companyId"
													placeholder="公司">
													<option value="">
														所有
													</option>
													<%
														for (Company company : SystemCache.companylist) {
															if (companyId!=null&&companyId == company.getId()) {
													%>
													<option value="<%=company.getId()%>" selected><%=company.getShortname()%></option>
													<%
														} else {
													%>
													<option value="<%=company.getId()%>"><%=company.getShortname()%></option>
													<%
														}
														}
													%>
												</select>
											</div>
										</div>
										<div class="form-group salesgroup">
											<label for="charge_employee" class="col-sm-4 control-label">
												跟单人
											</label>
											<div class="col-sm-8">
												<select class="form-control" name="charge_employeeId"
													id="charge_employeeId">
													<option value="">
														所有
													</option>
													<%
														for (Employee temp : employeelist) {
															if (charge_employeeId != null && charge_employeeId == temp.getId()) {
													%>
													<option value="<%=temp.getId()%>" selected="selected"><%=temp.getName()%></option>
													<%
														} else {
													%>
													<option value="<%=temp.getId()%>"><%=temp.getName()%></option>
													<%
														}
														}
													%>
												</select>
											</div>
										</div>
										<div class="form-group timegroup">
											<label class="col-sm-3 control-label">
												创建时间
											</label>

											<div class="input-group col-md-9">
												<input type="text" name="start_time" id="start_time"
													class="date form-control" value="<%=start_time_str%>" />
												<span class="input-group-addon">到</span>
												<input type="text" name="end_time" id="end_time"
													class="date form-control" value="<%=end_time_str%>">

												<span class="input-group-btn">
													<button class="btn btn-primary" type="submit">
														搜索
													</button> </span>
											</div>
										</div>
									</form>
									<ul class="pagination">
										<li>
											<a
												href="order/index?status=<%=status_str %>&companyId=<%=company_str %>&charge_employeeId=<%=charge_employeeId_str %>&start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=1">«</a>
										</li>

										<%
										if (pager.getPageNo() > 1) {
									%>
										<li class="">
											<a
												href="order/index?status=<%=status_str %>&companyId=<%=company_str %>&charge_employeeId=<%=charge_employeeId_str %>&start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=<%=pager.getPageNo() - 1%>">上一页
												<span class="sr-only"></span> </a>
										</li>
										<%
										} else {
									%>
										<li class="disabled">
											<a disabled>上一页 <span class="sr-only"></span> </a>
										</li>
										<%
										}
									%>

										<li class="active">
											<a
												href="order/index?status=<%=status_str %>&companyId=<%=company_str %>&charge_employeeId=<%=charge_employeeId_str %>&start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=<%=pager.getPageNo() %>"><%=pager.getPageNo()%>/<%=pager.getTotalPage()%>，共<%=pager.getTotalCount()%>条<span
												class="sr-only"></span> </a>
										</li>
										<li>
											<%
											if (pager.getPageNo() < pager.getTotalPage()) {
										%>
										
										<li class="">
											<a
												href="order/index?status=<%=status_str %>&companyId=<%=company_str %>&charge_employeeId=<%=charge_employeeId_str %>&start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=<%=pager.getPageNo() + 1%>">下一页
												<span class="sr-only"></span> </a>
										</li>
										<%
										} else {
									%>
										<li class="disabled">
											<a disabled>下一页 <span class="sr-only"></span> </a>
										</li>
										<%
										}
									%>

										</li>
										<li>
											<a
												href="order/index?status=<%=status_str %>&companyId=<%=company_str %>&charge_employeeId=<%=charge_employeeId_str %>&start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=<%=pager.getTotalPage()%>">»</a>
										</li>
									</ul>
									
								</div>
								
								<table class="table table-responsive">
									<thead>
										<tr>
											<th width="20px">
												No.
											</th>
											<th width="110px">样品</th>
											<th width="70px">
												订单号
											</th>
											<th width="55px">
												状态
											</th>
											<th width="70px">
												总金额
											</th>
											<th width="120px">
												订单信息
											</th>
											<th width="55px">
												公司
											</th>
											<th width="55px">
												业务员
											</th>
											<th width="60px">
												创建用户
											</th>
											<th width="60px">
												生效时间
											</th>
											<th width="60px">
												截止时间
											</th>
											<th width="60px">
												创建时间
											</th>
											<th  width="60px">
												操作
											</th>
										</tr>
									</thead>
									<tbody>
										<%
											int i = (pager.getPageNo()-1) * pager.getPageSize() + 0;
											for (Order order : orderlist) {
										%>
										<tr orderId="<%=order.getId()%>">
											<td><%=++i%></td>
											<td
												style="max-width: 120px; height: 120px; max-height: 120px;">
												<a target="_blank" class="cellimg"
													href="/<%=order.getImg()%>"><img
														style="max-width: 120px; height: 120px; max-height: 120px;"
														src="/<%=order.getImg_ss()%>"> </a>
											</td>
											<td><%=order.getOrderNumber()%></td>
											<td><%if(order.getStatus() == OrderStatus.CANCEL.ordinal()){ %>
												<span class="label label-default">已取消</span>
											<%}else if(order.getStatus() == OrderStatus.DELIVERED.ordinal()){ %>
												<span class="label label-primary">已发货</span>
											<%}else if(order.isCompleted()){ %>
												<span class="label label-success">交易已完成</span>
											<%}else{ %>
												<%=order.getState()%>
											<%} %>
											</td>
											<td><%=order.getAmount()%></td>
											<td><%=order.getInfo()%></td>
											<td><%=SystemCache.getCompanyShortName(order
										.getCompanyId())%></td>
											<td><%=SystemCache.getSalesmanName(order.getSalesmanId())%></td>
											<td><%=SystemCache.getUserName(order
										.getCreated_user())%></td>
											<td><%=DateTool.formatDateYMD(order.getStart_at())%></td>
											<td><%=DateTool.formatDateYMD(order.getEnd_at())%></td>
											<td><%=DateTool.formatDateYMD(order.getCreated_at())%></td>
											<td>
												<%
													if (has_order_detail) {
												%>
												<a href="order/detail/<%=order.getId()%>">详情</a>
												<%
													}
												%>
												<%
												if(order.getIn_use()){
													if (has_order_edit && order.isEdit()) {
												%>
													<br><br>
													<a href="order/put/<%=order.getId()%>">编辑</a>
													<%
														}
													%>
													<%
														if (has_order_cancel && order.isCancelable()) {
													%>
													<br><br>
													<a data-cid="<%=order.getId()%>" class="delete" href="#">取消订单</a>
												<%
														}
													}
												%>

											</td>
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
	</body>
</html>