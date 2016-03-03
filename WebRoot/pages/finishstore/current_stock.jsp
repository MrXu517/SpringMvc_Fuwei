<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.commons.Pager"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.fuwei.entity.Employee"%>
<%@page import="com.fuwei.entity.finishstore.FinishStoreStock"%>
<%@page import="com.fuwei.entity.finishstore.FinishStoreStockDetail"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/";
	Pager pager = (Pager) request.getAttribute("pager");
	if (pager == null) {
		pager = new Pager();
	}
	List<FinishStoreStock> currentStocklist = new ArrayList<FinishStoreStock>();
	if (pager != null & pager.getResult() != null) {
		currentStocklist = (List<FinishStoreStock>) pager.getResult();
	}

	Integer charge_employee = (Integer) request.getAttribute("charge_employee");
	String charge_employee_str = "";
	if (charge_employee != null) {
		charge_employee_str = String.valueOf(charge_employee);
	}
	if (charge_employee == null) {
		charge_employee = -1;
	}
	String orderNumber = (String) request.getAttribute("orderNumber");
	String orderNumber_str = "";
	if (orderNumber != null) {
		orderNumber_str = String.valueOf(orderNumber);
	}
	Boolean not_zero = (Boolean) request.getAttribute("not_zero");
	String not_zero_str = "";
	if(not_zero!=null){
		not_zero_str = not_zero?"1":"0";
	}
	
	List<Employee> employeelist = (List<Employee>) request.getAttribute("employeelist");
%>
<!DOCTYPE html>

<html>
	<head>
		<base href="<%=basePath%>">
		<title>成品当前库存列表 -- 桐庐富伟针织厂</title>
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
		<script type='text/javascript' src='js/plugins/select2.min.js'></script>
		<link rel="stylesheet" type="text/css" href="css/plugins/select2.min.css" />
		<link href="css/store_in_out/index.css" rel="stylesheet"
			type="text/css" />
		
	</head>
	<body>
		<div id="Content">
			<div id="main">
				<div class="body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 tablewidget">
								<!-- Table -->
								<div clas="navbar navbar-default">
									<form class="form-horizontal searchform form-inline searchform"
										role="form">
										<button class="btn btn-primary pull-right" type="submit" id="searchBtn" style="margin-left: 15px;">
											搜索
										</button>
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
											<label class="col-sm-3 control-label">
												跟单人
											</label>

											<div class="col-sm-8">
												<select id="charge_employee" name="charge_employee"
													class="form-control">
													<option value="">
														所有
													</option>
													<%
														for (Employee temp : employeelist) {
																																					if (charge_employee != null && charge_employee == temp.getId()) {
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
										<%if(not_zero!=null && not_zero){ %><input checked name="not_zero" type="checkbox" value="1" style="margin-left:10px;">  不显示0库存
											<%}else{ %>
											<input name="not_zero" type="checkbox" value="1" style="margin-left:10px;">  不显示0库存<%} %>
										
									
										</form>
										
									<ul class="pagination">
										<li>
											<a
												href="finishstore_workspace/current_stock?not_zero=<%=not_zero_str %>&orderNumber=<%=orderNumber_str%>&charge_employee=<%=charge_employee_str%>&page=1">«</a>
										</li>

										<%
											if (pager.getPageNo() > 1) {
										%>
										<li class="">
											<a
												href="finishstore_workspace/current_stock?not_zero=<%=not_zero_str %>&orderNumber=<%=orderNumber_str%>&charge_employee=<%=charge_employee_str%>&page=<%=pager.getPageNo() - 1%>">上一页
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
												href="finishstore_workspace/current_stock?not_zero=<%=not_zero_str %>&orderNumber=<%=orderNumber_str%>&charge_employee=<%=charge_employee_str%>&page=<%=pager.getPageNo()%>"><%=pager.getPageNo()%>/<%=pager.getTotalPage()%>，共<%=pager.getTotalCount()%>条<span
												class="sr-only"></span> </a>
										</li>
										<li>
											<%
												if (pager.getPageNo() < pager.getTotalPage()) {
											%>
										
										<li class="">
											<a
												href="finishstore_workspace/current_stock?not_zero=<%=not_zero_str %>&orderNumber=<%=orderNumber_str%>&charge_employee=<%=charge_employee_str%>&page=<%=pager.getPageNo() + 1%>">下一页
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

										<li></li>
										<li>
											<a
												href="finishstore_workspace/current_stock?not_zero=<%=not_zero_str %>&orderNumber=<%=orderNumber_str%>&charge_employee=<%=charge_employee_str%>&page=<%=pager.getTotalPage()%>">»</a>
										</li>
									</ul>

								</div>

								<table class="table table-responsive table-bordered">
									<thead>
										<tr style="height:0;">
											<th style="width:20px"></th>
    										<th style="width:60px"></th>
    										<th style="width:100px"></th>
											<th style="width:40px"></th>
    										<th style="width:55px"></th>
    										<th style="width:55px"></th>
    										<th style="width:55px"></th>
    										<th style="width:55px"></th>
    										<th style="width:55px"></th>
    										<th style="width:60px"></th>
    										<th style="width:60px"></th>
    										<th style="width:40px"></th>
  										</tr>
										<tr>
											<th rowspan="2"  width="20px" style="padding: 0;">
												No.
											</th>
											<th rowspan="2" width="60px">
												订单号
											</th><th rowspan="2" width="100px">
												款名
											</th>
											<th rowspan="2" width="40px">
												跟单人
											</th><th colspan="7" width="165px">入库列表</th>
											<th rowspan="2" width="40px">
												操作
											</th>
										</tr><tr><th width="55px">
												列1
											</th><th width="55px">
												列2
											</th><th width="55px">
												列3
											</th><th width="55px">
												列4
											</th><th width="55px">
												颜色
											</th><th  width="60px">
												库存箱数
											</th><th  width="60px">
												库存数量
											</th></tr>
									</thead>
									<tbody>
										<%
											int i = (pager.getPageNo()-1) * pager.getPageSize() + 0;
												for (FinishStoreStock item : currentStocklist) {
												boolean even = i%2 == 0;
												String classname = even?"even":"odd";
												List<FinishStoreStockDetail> detailist = item.getDetaillist();
												if(detailist == null){detailist = new ArrayList<FinishStoreStockDetail>();}
												int detailsize = detailist.size();
										%>
										<tr itemId="<%=item.getId()%>" class="<%=classname%>">
											<td rowspan="<%=detailsize%>"><%=++i%></td>
											<td rowspan="<%=detailsize%>"><a target="_blank" href="order/detail/<%=item.getOrderId()%>"><%=item.getOrderNumber()%></a></td>
											<td rowspan="<%=detailsize%>"><%=item.getName()%></td>
											<td rowspan="<%=detailsize%>"><%=SystemCache.getEmployeeName(item.getCharge_employee())%></td>
											<td><%=detailist.get(0).getCol1_value() == null?"":detailist.get(0).getCol1_value()%></td>
											<td><%=detailist.get(0).getCol2_value() == null?"":detailist.get(0).getCol2_value()%></td>
											<td><%=detailist.get(0).getCol3_value() == null?"":detailist.get(0).getCol3_value()%></td>
											<td><%=detailist.get(0).getCol4_value() == null?"":detailist.get(0).getCol4_value()%></td>
											<td><%=detailist.get(0).getColor()%></td>
											<td><%=detailist.get(0).getStock_cartons()%></td>
											<td><%=detailist.get(0).getStock_quantity()%></td>
											<td rowspan="<%=detailsize%>">
												<a target="_top" href="finishstore_workspace/in_out/<%=item.getOrderId()%>">出入库记录</a>
											</td>
										</tr>
										<%
											detailist.remove(0);
											for(FinishStoreStockDetail detail : detailist){
										%>
										<tr class="<%=classname %>">
											<td><%=detail.getCol1_value() == null?"":detail.getCol1_value()%></td>
											<td><%=detail.getCol2_value() == null?"":detail.getCol2_value()%></td>
											<td><%=detail.getCol3_value() == null?"":detail.getCol3_value()%></td>
											<td><%=detail.getCol4_value() == null?"":detail.getCol4_value()%></td>
											<td><%=detail.getColor()%></td>
											<td><%=detail.getStock_cartons()%></td>
											<td><%=detail.getStock_quantity()%></td>

										</tr>
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
		</div>
	</body>
</html>