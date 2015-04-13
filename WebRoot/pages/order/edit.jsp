<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.constant.OrderStatus"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="com.fuwei.entity.Material"%>
<%@page import="com.fuwei.entity.Customer"%>
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

	HashMap<String, List<Salesman>> companySalesmanMap = SystemCache
			.getCompanySalesmanMap_ID();
	JSONObject jObject = new JSONObject();
	jObject.put("companySalesmanMap", companySalesmanMap);
	String companySalesmanMap_str = jObject.toString();

	List<OrderDetail> DetailList = order == null ? new ArrayList<OrderDetail>()
			: order.getDetaillist();
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>编辑订单 -- 桐庐富伟针织厂</title>
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
		<script src="js/order/edit.js" type="text/javascript"></script>
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
							编辑订单
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
												编辑订单
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
									<fieldset id="sampleInfoWidget">
										<legend>
											样品信息
										</legend>
										<input type="hidden" id="sampleId" name="sampleId"
										value="<%=order.getSampleId()%>" class="require" />
										<div class="form-group col-md-6">
											<label for="img" class="col-sm-3 control-label">
												样品图片
											</label>
											<div class="col-sm-8">
												<a href="#" class="thumbnail" id="sampleImgA"> <img
														id="sampleImg" alt="350 x 100%"
														src="/<%=order.getImg_s()%>"> </a>
												<!--  <button type="button" id="chooseSampleBtn">
													选择样品
												</button> -->
											</div>
											<div class="col-sm-1"></div>
										</div>

										<div class="form-group col-md-6">
											<label for="name" class="col-sm-3 control-label">
												样品名称
											</label>
											<div class="col-sm-8">
												<input readonly type="text" name="name" id="name"
													class="form-control"
													value="<%=order.getName() == null ? "" : order.getName()%>" />

											</div>
											<div class="col-sm-1"></div>
										</div>

										<div class="form-group col-md-6">
											<label for="productNumber" class="col-sm-3 control-label">
												货号
											</label>
											<div class="col-sm-8">
												<input readonly type="text" name="productNumber"
													id="productNumber" class="form-control"
													value="<%=order.getProductNumber() == null ? "" : order
					.getProductNumber()%>" />

											</div>
											<div class="col-sm-1"></div>
										</div>

										<div class="form-group col-md-6">
											<label for="company_productNumber" class="col-sm-3 control-label">
												公司货号
											</label>
											<div class="col-sm-8">
												<input type="text" name="company_productNumber"
													id="company_productNumber" class="form-control require"
													value="<%=order.getCompany_productNumber() == null ? "" : order
					.getCompany_productNumber()%>" />

											</div>
											<div class="col-sm-1"></div>
										</div>


										<div class="form-group col-md-6">
											<label for="material" class="col-sm-3 control-label">
												材料
											</label>
											<div class="col-sm-8">
												<select class="form-control require" name="materialId"
															id="materialId" disabled formele=true>
															<option value="">未选择</option>
															<%
																for (Material material : SystemCache.materiallist) {
																	if (order.getMaterialId()!=null && material.getId() == order.getMaterialId()) {
															%>
															<option value="<%=material.getId()%>" selected="selected"><%=material.getName()%></option>
															<%
																} else {
															%>
															<option value="<%=material.getId()%>"><%=material.getName()%></option>
															<%
																}
																}
															%>
														</select>

											</div>
											<div class="col-sm-1"></div>
										</div>

										<div class="form-group col-md-6">
											<label for="weight" class="col-sm-3 control-label">
												克重
											</label>
											<div class="col-sm-8">
												<input readonly type="text" name="weight" id="weight"
													class="form-control" value="<%=order.getWeight()%>"
													class="require double" />

											</div>
											<div class="col-sm-1"></div>
										</div>

										<div class="form-group col-md-6">
											<label for="size" class="col-sm-3 control-label">
												尺寸
											</label>
											<div class="col-sm-8">
												<input readonly type="text" name="size" id="size"
													class="form-control"
													value="<%=order.getSize() == null ? "" : order.getSize()%>" />

											</div>
											<div class="col-sm-1"></div>
										</div>

										<div class="form-group col-md-6">
											<label for="charge_user" class="col-sm-3 control-label">
												跟单人
											</label>
											<div class="col-sm-8">
												<select disabled name="charge_user" id="charge_user"
													class="form-control">
													<option value="<%=order.getCharge_user()%>"
														selected><%=SystemCache.getUserName(order
							.getCharge_user())%></option>
												</select>

											</div>
											<div class="col-sm-1"></div>
										</div>
										<div class="form-group col-md-6">
											<label for="amount" class="col-sm-3 control-label">
												金额
											</label>
											<div class="col-sm-8">
												<input type="text" name="amount" id="amount"
													class="form-control" value="<%=order.getAmount()%>"
													class="require double"
													defaultvalue="<%=order.getAmount()%>" />

											</div>
											<div class="col-sm-1"></div>
										</div>
										<div class="form-group col-md-6">
											<label for="quantity" class="col-sm-3 control-label">
												数量
											</label>
											<div class="col-sm-8">
												<input type="text" name="quantity" id="quantity"
													class="form-control" value="<%=order.getQuantity()%>"
													readonly />

											</div>
											<div class="col-sm-1"></div>
										</div>

									</fieldset>

									<fieldset id="orderDetail">
										<legend>
											颜色及数量
										</legend>
										<table class="table table-responsive detailTb">
											<caption>
												<!-- <button type="button"
													class="btn btn-primary addRow pull-left">
													添加一行
												</button> -->
											</caption>
											<thead>
												<tr>
													<th width="10%">
														颜色
													</th>
													<th width="10%">
														克重(g)
													</th>
													<th width="10%">
														机织克重(g)
													</th>
													<th width="10%">
														纱线种类
													</th>
													<th width="10%">
														尺寸
													</th>
													<th width="10%">
														生产数量
													</th>
													<th width="10%">
														单价
													</th>
													<!-- 	<th width="15%">
														操作
													</th> -->
												</tr>
											</thead>
											<tbody>
												<%
													for (OrderDetail detail : DetailList) {
												%>
												<tr class="tr" data='<%=SerializeTool.serialize(detail)%>'>
													<td class="color"><%=detail.getColor()%>
													</td>
													<td class="weight"><%=detail.getWeight()%>
													</td>
													<td><input type="text" class="form-control produce_weight value"
															value="<%=detail.getProduce_weight()%>" />
													</td>
													<td class="yarn_name"><%=SystemCache.getMaterialName(detail.getYarn())%>
													</td>
													<td class="size"><%=detail.getSize()%>
													</td>
													<td>
														<input type="text" class="form-control quantity value"
															value="<%=detail.getQuantity()%>" />
													</td>
													<td class="price"><%=detail.getPrice()%>
													</td>
													<!--  <td class="_handle">
														<a class='editRow' href='#'>修改</a>
													</td> -->
												</tr>

												<%
													}
												%>

											</tbody>
										</table>
									</fieldset>
									<fieldset>
										<legend>
											订单基本属性
										</legend>
										<div class="form-group col-md-6">
											<input type="hidden" id="id" name="id"
												value="<%=order.getId()%>" />
											<label for="orderNumber" class="col-sm-3 control-label">
												订单号
											</label>
											<div class="col-sm-8">
												<input type="text" class="form-control" name="orderNumber"
													id="orderNumber" disabled
													value="<%=order.getOrderNumber()%>" placeholder="订单号">
											</div>
											<div class="col-sm-1"></div>
										</div>

										<div class="form-group col-md-6">
											<label for="kehu" class="col-sm-3 control-label">
												客户
											</label>
											<div class="col-sm-8">
												<select class="form-control" name="customerId"
													id="customerId" disabled formele=true>
													<option value="">
														未选择
													</option>
													<%
														for (Customer customer : SystemCache.customerlist) {
															if (order.getCustomerId() != null
																	&& order.getCustomerId() == customer.getId()) {
													%>
													<option value="<%=customer.getId()%>" selected><%=customer.getName()%></option>
													<%
														} else {
													%>
													<option value="<%=customer.getId()%>"><%=customer.getName()%></option>
													<%
														}
														}
													%>
												</select>
											</div>
											<div class="col-sm-1"></div>
										</div>
										<div class="form-group col-md-6">
											<label for="companyId" class="col-sm-3 control-label">
												公司
											</label>
											<div class="col-sm-8">
												<select disabled data='<%=companySalesmanMap_str%>'
													class="form-control require" name="companyId"
													id="companyId" placeholder="公司">
													<option value="">
														未选择
													</option>
													<%
														for (Company company : SystemCache.companylist) {
															if (order.getCompanyId() != null
																	&& company.getId() == order.getCompanyId()) {
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
												<select class="form-control require" name="salesmanId"
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
													name="amount" id="amount" disabled placeholder="金额"
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
													class="date form-control require"
													value="<%=DateTool.formatDateYMD(order.getStart_at())%>"
													placeholder="" />

											</div>
											<div class="col-sm-1"></div>
										</div>
										<div class="form-group col-md-6">
											<label for="created_at" class="col-sm-3 control-label">
												订单创建时间
											</label>
											<div class="col-sm-8">
												<input disabled type="text" name="created_at"
													id="created_at" class="date form-control require"
													placeholder=""
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
													class="date form-control require" placeholder=""
													value="<%=DateTool.formatDateYMD(order.getEnd_at())%>" />

											</div>
											<div class="col-sm-1"></div>
										</div>

										<div class="form-group col-md-6">
											<label for="created_user_name" class="col-sm-3 control-label">
												订单创建人
											</label>
											<div class="col-sm-8">
												<input type="text" name="created_user_name"
													id="created_user_name" class="form-control require"
													readonly
													value="<%=SystemCache.getUserName(order.getCreated_user())%>" />

											</div>
											<div class="col-sm-1"></div>
										</div>
										<div class="form-group col-md-6">
											<label for="memo" class="col-sm-3 control-label">
												备注
											</label>
											<div class="col-sm-8">
												<input type="text" name="memo" id="memo"
													class="form-control"
													value="<%=order.getMemo() == null ? "" : order.getMemo()%>" />

											</div>
											<div class="col-sm-1"></div>
										</div>

										<div class="clear"></div>
									</fieldset>

								</form>

							</div>


						</div>
					</div>

				</div>
			</div>
		</div>

		<!--
						 添加编辑订单明细对话框 -->
		<div class="modal fade tableRowDialog" id="orderDetailDialog">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
						</button>
						<h4 class="modal-title">
							添加一行

						</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal rowform" role="form">
							<div class="form-group col-md-12">
								<label for="color" class="col-sm-3 control-label">
									颜色
								</label>
								<div class="col-sm-8">
									<input type="text" name="color" id="color"
										class="form-control require" />
								</div>
								<div class="col-sm-1"></div>
							</div>
							<div class="form-group col-md-12">
								<label for="weight" class="col-sm-3 control-label">
									克重(g)
								</label>
								<div class="col-sm-8">
									<input type="text" name="weight" id="weight"
										class="form-control double require" />
								</div>
								<div class="col-sm-1"></div>
							</div>
							<div class="form-group col-md-12">
								<label for="weight" class="col-sm-3 control-label">
									机织克重(g)
								</label>
								<div class="col-sm-8">
									<input type="text" name="produce_weight" id="produce_weight"
										class="form-control double require" />
								</div>
								<div class="col-sm-1"></div>
							</div>
							<div class="form-group col-md-12">
								<label for="yarn" class="col-sm-3 control-label">
									纱线种类
								</label>
								<div class="col-sm-8">
									<select name="yarn" id="yarn"
										class="form-control require">
										<option value="">未选择</option>
										<%for(Material material : SystemCache.materiallist){ %>
											<option value="<%=material.getId() %>" ><%=material.getName() %></option>
										<%} %>
									</select>
								</div>
								<div class="col-sm-1"></div>
							</div>
							<div class="form-group col-md-12">
								<label for="size" class="col-sm-3 control-label">
									尺寸
								</label>
								<div class="col-sm-8">
									<input type="text" name="size" id="size"
										class="form-control require" />
								</div>
								<div class="col-sm-1"></div>
							</div>
							<div class="form-group col-md-12">
								<label for="quantity" class="col-sm-3 control-label">
									生产数量
								</label>
								<div class="col-sm-8">
									<input type="text" name="quantity" id="quantity"
										class="form-control int require" />
								</div>
								<div class="col-sm-1"></div>
							</div>
							<div class="form-group col-md-12">
								<label for="price" class="col-sm-3 control-label">
									单价
								</label>
								<div class="col-sm-8">
									<input type="text" name="price" id="price"
										class="form-control double require" />
								</div>
								<div class="col-sm-1"></div>
							</div>
							<div class="modal-footer">
								<button type="submit" class="btn btn-primary"
									data-loading-text="正在保存...">
									保存
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
						 添加编辑订单明细对话框 -->
	</body>
</html>