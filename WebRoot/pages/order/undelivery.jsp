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

	Integer salesmanId = (Integer) request.getAttribute("salesmanId");
	Integer companyId = (Integer) request.getAttribute("companyId");
	String company_str = "";
	String salesman_str = "";
	if (salesmanId != null) {
		salesman_str = String.valueOf(salesmanId);
	}
	if (companyId != null) {
		company_str = String.valueOf(companyId);
	}
	if (salesmanId == null) {
		salesmanId = -1;
	}
	if (companyId == null) {
		companyId = -1;
	}
	
	
	HashMap<String, List<Salesman>> companySalesmanMap = SystemCache
			.getCompanySalesmanMap_ID();
	JSONObject jObject = new JSONObject();
	jObject.put("companySalesmanMap", companySalesmanMap);
	String companySalesmanMap_str = jObject.toString();

	//权限相关
	Boolean has_order_detail = SystemCache.hasAuthority(session,
			"order/detail");
	//权限相关
	
	Integer charge_userId = (Integer) request
			.getAttribute("charge_user");
	String charge_user_str = "";
	if (charge_userId != null) {
		charge_user_str = String.valueOf(charge_userId);
	}
%>
<!DOCTYPE html>

<html>
	<head>
		<base href="<%=basePath%>">
		<title>待发货订单管理 -- 桐庐富伟针织厂</title>
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
		<link href="css/order/index.css" rel="stylesheet" type="text/css" />
		<style type="text/css">
.body {
	min-width: 0;
}

#breadcrumbs {
	min-width: 0;
}
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
						<li class="active">
							待发货列表
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
											<label for="charge_user" class="col-sm-3 control-label"
												style="width: 60px;">
												跟单人
											</label>
											<div class="col-sm-8">
												<select id="charge_user" name="charge_user"
													class="form-control">
													<option value="">
														所有
													</option>
													<%
														for (User tempU : SystemCache.userlist) {
															if (charge_userId != null && charge_userId == tempU.getId()) {
													%>
													<option value="<%=tempU.getId()%>" selected="selected"><%=tempU.getName()%></option>
													<%
														} else {
													%>
													<option value="<%=tempU.getId()%>"><%=tempU.getName()%></option>
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
															if (companyId == company.getId()) {
													%>
													<option value="<%=company.getId()%>" selected><%=company.getFullname()%></option>
													<%
														} else {
													%>
													<option value="<%=company.getId()%>"><%=company.getFullname()%></option>
													<%
														}
														}
													%>
												</select>
											</div>
										</div>
										<div class="form-group salesgroup">
											<label for="salesmanId" class="col-sm-4 control-label">
												业务员
											</label>
											<div class="col-sm-8">
												<select class="form-control" name="salesmanId"
													id="salesmanId" placeholder="业务员">
													<option value="">
														所有
													</option>
													<%
														for (Salesman salesman : SystemCache.getSalesmanList(companyId)) {
															if (salesmanId == salesman.getId()) {
													%>
													<option value="<%=salesman.getId()%>" selected><%=salesman.getName()%></option>
													<%
														} else {
													%>
													<option value="<%=salesman.getId()%>"><%=salesman.getName()%></option>
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
												href="order/undelivery?charge_user=<%=charge_user_str %>&companyId=<%=company_str %>&salesmanId=<%=salesman_str %>&start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=1">«</a>
										</li>

										<%
										if (pager.getPageNo() > 1) {
									%>
										<li class="">
											<a
												href="order/undelivery?charge_user=<%=charge_user_str %>&companyId=<%=company_str %>&salesmanId=<%=salesman_str %>&start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=<%=pager.getPageNo() - 1%>">上一页
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
												href="order/undelivery?charge_user=<%=charge_user_str %>&companyId=<%=company_str %>&salesmanId=<%=salesman_str %>&start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=<%=pager.getPageNo() %>"><%=pager.getPageNo()%>/<%=pager.getTotalPage()%>，共<%=pager.getTotalCount()%>条<span
												class="sr-only"></span> </a>
										</li>
										<li>
											<%
											if (pager.getPageNo() < pager.getTotalPage()) {
										%>
										
										<li class="">
											<a
												href="order/undelivery?charge_user=<%=charge_user_str %>&companyId=<%=company_str %>&salesmanId=<%=salesman_str %>&start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=<%=pager.getPageNo() + 1%>">下一页
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
												href="order/undelivery?charge_user=<%=charge_user_str %>&companyId=<%=company_str %>&salesmanId=<%=salesman_str %>&start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=<%=pager.getTotalPage()%>">»</a>
										</li>
									</ul>

								</div>

								<table class="table table-responsive">
									<thead>
										<tr>
											<th width="20px">
												No.
											</th>
											<th width="120px">
												样品
											</th>
											<th width="70px">
												订单号
											</th>
											<th width="110px">
												订单信息
											</th>
											<th width="60px">
												公司
											</th>
											<th width="60px">
												业务员
											</th>
											<th width="60px">
												跟单人
											</th>
											<th width="100px">
												截止时间
											</th>
											
											<th width="50px">
												操作
											</th>
										</tr>
									</thead>
									<tbody>
										<%
											int i = (pager.getPageNo()-1) * pager.getPageSize() + 0;
											for (Order order : orderlist) {
										%>
										<%if(order.isOverEnded()){ %>
										<tr orderId="<%=order.getId()%>" class="alert-danger">
											<%}else if(order.isPre30()){ %>
										
										<tr orderId="<%=order.getId()%>" class="alert-warning">
											<%}else{ %>
										
										<tr orderId="<%=order.getId()%>">
											<%} %>
											<td><%=++i%></td>
											<td
												style="max-width: 120px; height: 120px; max-height: 120px;">
												<a target="_blank" class="cellimg"
													href="/<%=order.getImg()%>"><img
														style="max-width: 120px; height: 120px; max-height: 120px;"
														src="/<%=order.getImg_ss()%>"> </a>
											</td>
											<td><%=order.getOrderNumber()%></td>
											<td><%=order.getInfo()%></td>
											<td><%=SystemCache.getCompanyShortName(order
										.getCompanyId())%></td>
											<td><%=SystemCache.getSalesmanName(order.getSalesmanId())%></td>
											<td><%=SystemCache.getUserName(order
										.getCharge_user())%></td>

											<td><%=DateTool.formatDateYMD(order.getEnd_at())%><br>
												<%if(order.isOverEnded()){ %>
												<span class="label label-danger">已超期</span>
												<%}else if(order.isPre30()){ %>
												<span class="label label-warning">交货时间<=30天</span>
												<%} %>
											</td>
											
											<td>
												<%
													if (has_order_detail) {
												%>
												<a href="order/detail/<%=order.getId()%>">详情</a>
												<%
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
		<script type="text/javascript">
	/* 设置当前选中的页 */
	var $a = $("#left li a[href='order/undelivery']");
	setActiveLeft($a.parent("li"));
	/* 设置当前选中的页 */
</script>
	</body>
</html>