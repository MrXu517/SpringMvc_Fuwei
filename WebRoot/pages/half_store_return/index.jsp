<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.financial.Expense_income"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.entity.producesystem.HalfStoreReturn"%>
<%@page import="com.fuwei.commons.Pager"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="com.fuwei.entity.Employee"%>
<%@page import="com.fuwei.entity.producesystem.HalfStoreReturnDetail"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/";
	Pager pager = (Pager) request.getAttribute("pager");
	if (pager == null) {
		pager = new Pager();
	}
	List<HalfStoreReturn> storeReturnlist = new ArrayList<HalfStoreReturn>();
	if (pager != null & pager.getResult() != null) {
		storeReturnlist = (List<HalfStoreReturn>) pager.getResult();
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
	String salesman_str = "";	
	if (companyId != null) {
		company_str = String.valueOf(companyId);
	}
	
	if (companyId == null) {
		companyId = -1;
	}
	Integer factoryId = (Integer) request.getAttribute("factoryId");
	String factory_str = "";
	if (factoryId != null) {
		factory_str = String.valueOf(factoryId);
	}
	if (factoryId == null) {
		factoryId = -1;
	}
	Integer charge_employee = (Integer) request.getAttribute("charge_employee");
	String charge_employee_str = "";
	if (charge_employee != null) {
		charge_employee_str = String.valueOf(charge_employee);
	}
	if (charge_employee == null) {
		charge_employee = -1;
	}
	String number = (String) request.getAttribute("number");
	String number_str = "";
	if (number != null) {
		number_str = String.valueOf(number);
	}
	List<Employee> employeelist = (List<Employee>) request.getAttribute("employeelist");
	List<Factory> factorylist = (List<Factory>)request.getAttribute("factorylist");
%>
<!DOCTYPE html>

<html>
	<head>
		<base href="<%=basePath%>">
		<title>退货列表 -- 桐庐富伟针织厂</title>
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
										<button class="btn btn-primary pull-right" type="submit" id="searchBtn">
											搜索
										</button>
										<input type="hidden" name="page" value="1">
										<div class="form-group salesgroup">
											<label for="number" class="col-sm-3 control-label" style="width:auto;">
												单号
											</label>
											<div class="col-sm-9">
												<input class="form-control" type="text" name="number" id="number" value="<%=number_str%>" />
											</div>
										</div>
										<div class="form-group salesgroup">
											<label for="factoryId" class="col-sm-3 control-label">
												加工工厂
											</label>
											<div class="col-sm-8">
												<select class="form-control" name="factoryId" id="factoryId">
													<option value="">
														未选择
													</option>
													<%
														for (Factory factory : factorylist) {
																																					if (factoryId != null && factoryId == factory.getId()) {
													%>
													<option value="<%=factory.getId()%>" selected><%=factory.getName()%></option>
													<%
														} else {
													%>
													<option value="<%=factory.getId()%>"><%=factory.getName()%></option>
													<%
														}
																																				}
													%>
												</select>
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
																																					if (companyId != null && companyId == company.getId()) {
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
												退货时间
											</label>

											<div class="input-group col-md-9">
												<input type="text" name="start_time" id="start_time"
													class="date form-control" value="<%=start_time_str%>" />
												<span class="input-group-addon">到</span>
												<input type="text" name="end_time" id="end_time"
													class="date form-control" value="<%=end_time_str%>">
											</div>
										</div>
										
										</form>
										
									<ul class="pagination">
										<li>
											<a
												href="half_store_return/index?number=<%=number_str%>&charge_employee=<%=charge_employee_str%>&factoryId=<%=factory_str%>&companyId=<%=company_str%>&start_time=<%=start_time_str%>&end_time=<%=end_time_str%>&page=1">«</a>
										</li>

										<%
											if (pager.getPageNo() > 1) {
										%>
										<li class="">
											<a
												href="half_store_return/index?number=<%=number_str%>&charge_employee=<%=charge_employee_str%>&factoryId=<%=factory_str%>&companyId=<%=company_str%>&start_time=<%=start_time_str%>&end_time=<%=end_time_str%>&page=<%=pager.getPageNo() - 1%>">上一页
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
												href="half_store_return/index?number=<%=number_str%>&charge_employee=<%=charge_employee_str%>&factoryId=<%=factory_str%>&companyId=<%=company_str%>&start_time=<%=start_time_str%>&end_time=<%=end_time_str%>&page=<%=pager.getPageNo()%>"><%=pager.getPageNo()%>/<%=pager.getTotalPage()%>，共<%=pager.getTotalCount()%>条<span
												class="sr-only"></span> </a>
										</li>
										<li>
											<%
												if (pager.getPageNo() < pager.getTotalPage()) {
											%>
										
										<li class="">
											<a
												href="half_store_return/index?number=<%=number_str%>&charge_employee=<%=charge_employee_str%>&factoryId=<%=factory_str%>&companyId=<%=company_str%>&start_time=<%=start_time_str%>&end_time=<%=end_time_str%>&page=<%=pager.getPageNo() + 1%>">下一页
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
												href="half_store_return/index?number=<%=number_str%>&charge_employee=<%=charge_employee_str%>&factoryId=<%=factory_str%>&companyId=<%=company_str%>&start_time=<%=start_time_str%>&end_time=<%=end_time_str%>&page=<%=pager.getTotalPage()%>">»</a>
										</li>
									</ul>

								</div>

								<table class="table table-responsive table-bordered">
									<thead>
										<tr style="height:0;">
											<th style="width:20px"></th>
    										<th style="width:60px"></th>
    										<th style="width:50px"></th>
											<th style="width:40px"></th>
    										<th style="width:55px"></th>
    										<th style="width:40px"></th>
    										<th style="width:60px"></th>
    										<th style="width:70px"></th>
    										<th style="width:40px"></th>
    										<th style="width:55px"></th>
    										<th style="width:60px"></th>
    										<th style="width:40px"></th>
    										<th style="width:40px"></th>
    										<th style="width:60px"></th>
    										<th style="width:30px"></th>
    										<th style="width:40px"></th>
  										</tr>
										<tr>
											<th rowspan="2"  width="20px" style="padding: 0;">
												No.
											</th>
											<th rowspan="2" width="60px">
												退货单号
											</th><th rowspan="2" width="50px">
												加工工厂
											</th><th rowspan="2" width="40px">
												工序
											</th><th rowspan="2" width="60px">
												订单号
											</th>
											<th rowspan="2" width="40px">
												公司
											</th>
											<th rowspan="2" width="60px">
												公司货号
											</th><th rowspan="2" width="70px">
												款名
											</th>
											<th rowspan="2" width="40px">
												跟单人
											</th><th colspan="3" width="165px">半成品退货列表</th>
											<th rowspan="2" width="40px">
												经办人
											</th><th rowspan="2" width="60px">
												退货时间
											</th><th rowspan="2" width="30px">
												打印
											</th>
											
											<th rowspan="2" width="40px">
												操作
											</th>
										</tr><tr><th width="55px">
												颜色
											</th><th  width="60px">
												尺寸
											</th><th  width="50px">
												数量
											</th></tr>
									</thead>
									<tbody>
										<%
											int i = (pager.getPageNo()-1) * pager.getPageSize() + 0;
																											for (HalfStoreReturn item : storeReturnlist) {
																												boolean even = i%2 == 0;
																												String classname = even?"even":"odd";
																												List<HalfStoreReturnDetail> detailist = item.getDetaillist();
																												int detailsize = item.getDetaillist().size();
										%>
										<tr itemId="<%=item.getId()%>" class="<%=classname%>">
											<td rowspan="<%=detailsize%>"><%=++i%></td>
											
											<td rowspan="<%=detailsize%>"><a target="_top" href="half_store_return/detail/<%=item.getId()%>"><%=item.getNumber()%></a></td>
											<td rowspan="<%=detailsize%>"><%=SystemCache.getFactoryName(item.getFactoryId())%></td>
											<td rowspan="<%=detailsize%>"><%=SystemCache.getGongxuName(item.getGongxuId())%></td>
											<td rowspan="<%=detailsize%>"><a target="_top" href="order/detail/<%=item.getOrderId()%>"><%=item.getOrderNumber()%></a></td>
											<td rowspan="<%=detailsize%>"><%=SystemCache.getCompanyShortName(item.getCompanyId())%></td>
											<td rowspan="<%=detailsize%>"><%=item.getCompany_productNumber()%></td>
											<td rowspan="<%=detailsize%>"><%=item.getName()%></td>
											<td rowspan="<%=detailsize%>"><%=SystemCache.getEmployeeName(item.getCharge_employee())%></td>
											
											
											<td><%=detailist.get(0).getColor()%></td>
											<td><%=detailist.get(0).getSize()%></td>
											<td><%=detailist.get(0).getQuantity()%></td>

											<td rowspan="<%=detailsize%>"><%=SystemCache.getUserName(item.getCreated_user())%></td>				
											<td rowspan="<%=detailsize%>"><%=DateTool.formatDateYMD(item.getDate())%></td>				
											<td rowspan="<%=detailsize%>"><%=item.printStr()%></td>
											
											<td rowspan="<%=detailsize%>">
												<a target="_top" href="half_store_return/detail/<%=item.getId()%>">详情</a>
											</td>
										</tr>
										<%
											detailist.remove(0);
																		for(HalfStoreReturnDetail detail : detailist){
										%>
										<tr class="<%=classname %>">
											<td><%=detail.getColor()%></td>
											<td><%=detail.getSize()%></td>
											<td><%=detail.getQuantity()%></td>

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