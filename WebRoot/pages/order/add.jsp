<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.fuwei.util.DateTool"%>
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

	HashMap<String, List<Salesman>> companySalesmanMap = SystemCache
			.getCompanySalesmanMap_ID();
	JSONObject jObject = new JSONObject();
	jObject.put("companySalesmanMap", companySalesmanMap);
	String companySalesmanMap_str = jObject.toString();
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>创建订单 -- 桐庐富伟针织厂</title>
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
		<script src="js/order/add.js" type="text/javascript"></script>
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
							创建订单
						</li>
					</ul>
				</div>
				<div class="body">

					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 formwidget">
								<form class="form-horizontal orderform" role="form">
									<div class="form-group col-md-6">
										<div class="col-sm-offset-3 col-sm-5">
											<button type="submit" class="btn btn-primary"
												data-loading-text="正在保存...">
												创建订单
											</button>

										</div>
										<div class="col-sm-3">
											<button type="reset" class="reset btn btn-default">
												重置表单
											</button>
										</div>
										<div class="col-sm-1"></div>
									</div>
									<div class="clear"></div>
									<div class="form-group col-md-6">
										<label for="orderNumber" class="col-sm-3 control-label">
											订单号
										</label>
										<div class="col-sm-8">
											<input type="text" class="form-control" name="orderNumber"
												id="orderNumber" readonly value="自动生成" placeholder="订单号">
										</div>
										<div class="col-sm-1"></div>
									</div>
									<div class="form-group col-md-6">
										<label for="companyId" class="col-sm-3 control-label">
											公司
										</label>
										<div class="col-sm-8">
											<select data='<%=companySalesmanMap_str%>'
												class="form-control" name="companyId" id="companyId"
												placeholder="公司">
												<option value="">
													未选择
												</option>
												<%
													for (Company company : SystemCache.companylist) {
														if (order.getCompanyId()!=null && order.getCompanyId() == company.getId()) {
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
									<div class="form-group col-md-6">
										<label for="salesmanId" class="col-sm-3 control-label">
											业务员
										</label>
										<div class="col-sm-8">
											<select class="form-control" name="salesmanId"
												id="salesmanId" placeholder="业务员">
												<option value="">
													未选择
												</option>
												<%
													if (order.getCompanyId() != null) {
														for (Salesman salesman : SystemCache.getSalesmanList(order
																.getCompanyId())) {
															if (order.getSalesmanId() == salesman.getId()) {
												%>
												<option value="<%=salesman.getId()%>" selected><%=salesman.getName()%></option>
												<%
													} else {
												%>
												<option value="<%=salesman.getId()%>"><%=salesman.getName()%></option>
												<%
													}
														}
													}
												%>
											</select>
										</div>
									</div>
									<div class="form-group col-md-6">
										<label for="amount" class="col-sm-3 control-label">
											金额
										</label>
										<div class="col-sm-8">
											<input type="text" class="form-control require double"
												name="amount" id="amount" readonly placeholder="金额"
												value="<%=order.getAmount()%>">
										</div>
										<div class="col-sm-1"></div>
									</div>
									<div class="form-group col-md-6">
										<label for="start_at" class="col-sm-3 control-label">
											订单签订时间
										</label>
										<div class="col-sm-8">
											<input type="text" name="start_at" id="start_at"
												class="date form-control require" placeholder="" />

										</div>
										<div class="col-sm-1"></div>
									</div>
									<div class="form-group col-md-6">
										<label for="created_at" class="col-sm-3 control-label">
											订单创建时间
										</label>
										<div class="col-sm-8">
											<input disabled type="text" name="created_at" id="created_at"
												class="date form-control require" placeholder=""
												value="<%=DateTool.formatDateYMD(order.getCreated_at())%>" />

										</div>
										<div class="col-sm-1"></div>
									</div>
									<div class="form-group col-md-6">
										<label for="end_at" class="col-sm-3 control-label">
											订单截止日期
										</label>
										<div class="col-sm-8">
											<input type="text" name="end_at" id="end_at"
												class="date form-control require" placeholder="" />

										</div>
										<div class="col-sm-1"></div>
									</div>

									<div class="form-group col-md-6">
										<label for="created_user_name" class="col-sm-3 control-label">
											订单创建人
										</label>
										<div class="col-sm-8">
											<input type="text" name="created_user_name"
												id="created_user_name" class="form-control require" readonly
												value="<%=SystemCache.getUserName(order.getCreated_user())%>" />

										</div>
										<div class="col-sm-1"></div>
									</div>
									<div class="form-group col-md-6">
										<label for="memo" class="col-sm-3 control-label">
											备注
										</label>
										<div class="col-sm-8">
											<input type="text" name="memo"
												id="memo" class="form-control"
												value="<%=order.getMemo()==null?"":order.getMemo()==null%>" />

										</div>
										<div class="col-sm-1"></div>
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
											<th>
												备注
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
											<td><%=detail.getCharge_user()%></td>
											<td class="price"><%=detail.getPrice()%></td>
											<td>
												<input class="form-control quantity_value positive_int require"
													value="<%=detail.getQuantity()%>" />
											</td>
											<td class="amount"><%=detail.getAmount()%></td>
											<td class="memo"><input class="form-control memo_value" value="<%=detail.getMemo()==null?"":detail.getMemo() %>" /></td>
										</tr>
										<%
											}
										%>
									</tbody>
								</table>
								</form>
								
							</div>


						</div>
					</div>

				</div>
			</div>
		</div>
	</body>
</html>