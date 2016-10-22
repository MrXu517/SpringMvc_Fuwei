<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.entity.Employee"%>
<%@page import="com.fuwei.commons.Pager"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.constant.OrderStatus"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.entity.ordergrid.HalfCheckRecordOrder"%>
<%@page import="com.fuwei.entity.ordergrid.PlanOrderDetail"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Pager pager = (Pager) request.getAttribute("pager");
	if (pager == null) {
		pager = new Pager();
	}
	List<HalfCheckRecordOrder> orderlist = new ArrayList<HalfCheckRecordOrder>();
	if (pager != null & pager.getResult() != null) {
		orderlist = (List<HalfCheckRecordOrder>) pager.getResult();
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


	Integer companyId = (Integer) request.getAttribute("companyId");
	String company_str = "";
	if (companyId != null) {
		company_str = String.valueOf(companyId);
	}
	if (companyId == null) {
		companyId = -1;
	}
	String orderNumber = (String) request.getAttribute("orderNumber");
	String orderNumber_str = "";
	if (orderNumber != null) {
		orderNumber_str = String.valueOf(orderNumber);
	}
	List<Employee> employeelist = new ArrayList<Employee>();
	for (Employee temp : SystemCache.employeelist) {
		if (temp.getIs_charge_employee()) {
			employeelist.add(temp);
		}
	}
	Integer charge_employeeId = (Integer) request
			.getAttribute("charge_employee");
	String charge_employee_str = "";
	if (charge_employeeId != null) {
		charge_employee_str = String.valueOf(charge_employeeId);
	}

	//权限相关
	Boolean has_export = SystemCache.hasAuthority(session,
			"halfcheckbill/export");
	//权限相关
	
	
%>
<!DOCTYPE html>

<html>
	<head>
		<base href="<%=basePath%>">
		<title>半检记录单列表 -- 桐庐富伟针织厂</title>
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
#Tb{
	border-color:#000;
}
#Tb>thead>tr{
	  background: #AEADAD;
}
#Tb>thead>tr>th,#Tb>tbody>tr>td{
	border-color:#000;
	border-bottom-width: 1px;
}
.memoform #memo{
	height:200px;
}
</style>
	</head>
	<body>
		<%@ include file="../../common/head.jsp"%>
		<div id="Content">
			<div id="main">
				<div class="breadcrumbs" id="breadcrumbs">
					<ul class="breadcrumb">
						<li>
							<i class="fa fa-home"></i>
							<a href="user/index">首页</a>
						</li>
						<li class="active">
							半检记录单列表
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
										<input type="hidden" name="page" value="1">
										<div class="form-group salesgroup">
											<label for="orderNumber" class="col-sm-3 control-label" style="width:auto;">
												订单号
											</label>
											<div class="col-sm-9">
												<input class="form-control" type="text" name="orderNumber" id="orderNumber" value="<%=orderNumber_str%>" />
											</div>
										</div>
										<div class="form-group">
											<label for="charge_employee" class="col-sm-3 control-label"
												style="width: 60px;">
												跟单人
											</label>
											<div class="col-sm-8">
												<select id="charge_employee" name="charge_employee"
													class="form-control">
													<option value="">
														所有
													</option>
													<%
														for (Employee tempU : employeelist) {
															if (charge_employeeId != null && charge_employeeId == tempU.getId()) {
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
												<select class="form-control" name="companyId" id="companyId"
													placeholder="公司">
													<option value="">
														所有
													</option>
													<%
														for (Company company : SystemCache.companylist) {
															if (companyId!=null && companyId == company.getId()) {
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
											</div>
										</div>
										<button class="btn btn-primary" type="submit">
														搜索
													</button>
									</form>
									<ul class="pagination">
										<li>
											<a
												href="halfcheckbill/index?charge_employee=<%=charge_employee_str %>&companyId=<%=company_str %>&orderNumber=<%=orderNumber_str %>&start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=1">«</a>
										</li>

										<%
										if (pager.getPageNo() > 1) {
									%>
										<li class="">
											<a
												href="halfcheckbill/index?charge_employee=<%=charge_employee_str %>&companyId=<%=company_str %>&orderNumber=<%=orderNumber_str %>&start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=<%=pager.getPageNo() - 1%>">上一页
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
												href="halfcheckbill/index?charge_employee=<%=charge_employee_str %>&companyId=<%=company_str %>&orderNumber=<%=orderNumber_str %>&start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=<%=pager.getPageNo() %>"><%=pager.getPageNo()%>/<%=pager.getTotalPage()%>，共<%=pager.getTotalCount()%>条<span
												class="sr-only"></span> </a>
										</li>
										<li>
											<%
											if (pager.getPageNo() < pager.getTotalPage()) {
										%>
										
										<li class="">
											<a
												href="halfcheckbill/index?charge_employee=<%=charge_employee_str %>&companyId=<%=company_str %>&orderNumber=<%=orderNumber_str %>&start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=<%=pager.getPageNo() + 1%>">下一页
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
												href="halfcheckbill/index?charge_employee=<%=charge_employee_str %>&companyId=<%=company_str %>&orderNumber=<%=orderNumber_str %>&start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=<%=pager.getTotalPage()%>">»</a>
										</li>
									</ul>

								</div>
								<%if(has_export){ %>
								<a target="_blank" href="halfcheckbill/export?companyId=<%=company_str %>&charge_employee=<%=charge_employee_str %>&orderNumber=<%=orderNumber_str %>&start_time=<%=start_time_str %>&end_time=<%=end_time_str %>" class="btn btn-primary">导出</a>
								<%} %>
								<table class="table table-responsive table-bordered" id="Tb">
									<thead>
										<tr>
											<th width="20px">
												No.</th>
											<th width="70px">
												订单号
											</th>
											<th width="50px">
												公司
											</th>
											<th width="50px">
												跟单人
											</th>
											<th width="50px">
												日期
											</th>
											<th width="50px">
												交货日期
											</th>
											<th width="90px">
												名称
											</th><th width="70px">
												颜色
											</th><th width="70px">
												尺寸
											</th><th width="70px">
												数量
											</th>
										</tr>
									</thead>
									<tbody>
										<%
											int i = (pager.getPageNo()-1) * pager.getPageSize() + 0;
											for (HalfCheckRecordOrder order : orderlist) {
												String order_json = SerializeTool.serialize(order);
												List<PlanOrderDetail> detailist = order.getDetaillist();
												int detailsize = detailist.size();
										%>
									
										<tr>
											<td rowspan="<%=detailsize %>"><%=++i%>
											</td>
											<td rowspan="<%=detailsize %>"><%=order.getOrderNumber()%></td>
											<td rowspan="<%=detailsize %>"><%=SystemCache.getCompanyShortName(order
										.getCompanyId())%></td>
											<td rowspan="<%=detailsize %>"><%=SystemCache.getEmployeeName(order
										.getCharge_employee())%></td>
											<td rowspan="<%=detailsize %>"><%=DateTool.formatDateYMD(order.getCreated_at())%></td>
											<td rowspan="<%=detailsize %>"><%=DateTool.formatDateYMD(order.getEnd_at())%></td>
											<td rowspan="<%=detailsize %>"><%=order.getName()%></td>
											<td><%=detailist.get(0).getColor() %></td>
											<td><%=detailist.get(0).getSize() %></td>
											<td><%=detailist.get(0).getQuantity() %></td>
										</tr><%
										detailist.remove(0);
										for(PlanOrderDetail detail : detailist){ %>
										<tr><td><%=detail.getColor() %></td><td><%=detail.getSize() %></td><td><%=detail.getQuantity() %></td></tr>
										<%} %>
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
	</body>
</html>