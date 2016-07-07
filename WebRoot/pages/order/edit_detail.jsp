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
		<title>修改订单明细 -- 桐庐富伟针织厂</title>
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
		<script src="js/order/edit_detail.js" type="text/javascript"></script>
		<link href="css/order/index.css" rel="stylesheet" type="text/css" />
		<style type="text/css">
		a.thumbnail{border:none;margin-bottom: 0;}
		a.thumbnail img{max-height:120px;max-width:250px;}
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
						<li>
							<i class=""></i>
							<a href="order/index">订单列表</a>
						</li>
						<li class="active">
							修改订单明细
						</li>
					</ul>
				</div>
				<div class="body">

					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 formwidget">
								<p class="alert alert-danger">警告： 修改订单明细时， 系统将自动修改计划单，使其明细恢复到与订单明细一致。若有生产数量放余量，请自行修改计划单。<br>
									计划单将影响到开生产单</p>
								<form class="form-horizontal orderform" role="form">
									
									<div class="form-group col-md-6">
										<div class="col-sm-offset-3 col-sm-5">
											<button type="submit" class="btn btn-primary"
												data-loading-text="正在保存...">
												确认修改
											</button>

										</div>
										<div class="col-sm-3">
											<a href="order/detail/<%=order.getId() %>" type="button" class="reset btn btn-default">
												放弃修改
											</a>
										</div>
										<div class="col-sm-1"></div>
									</div>
									<div class="clear"></div>
									<input type="hidden" id="orderId" name="orderId"
										value="<%=order.getId()%>"/>
									<table
																class="table table-responsive table-bordered tableTb">
																<tbody>
																	<tr>
																		<td rowspan="3" width="30%">
																			<a href="/<%=order.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=order.getImg_s()%>">
																			</a>
																		</td><td>公司 ： <%=SystemCache.getCompanyShortName(order.getCompanyId())%> </td><td>客户：<%=SystemCache.getCustomerName(order.getCustomerId())%> </td>
																	</tr>
																	<tr>
																	<td>货号：<%=order.getCompany_productNumber()%></td><td>跟单：<%=SystemCache.getEmployeeName(order.getCharge_employee())%></td>
																	</tr><tr>
																	<td>款名：<%=order.getName()%></td><td>发货时间：<%=DateTool.formatDateYMD(order.getEnd_at())%></td>
																	</tr>
																</tbody>
															</table>	
									<fieldset id="orderDetail">
										<legend>
											颜色及数量
										</legend>
										<table class="table table-responsive detailTb">
											<caption>
												<button type="button"
													class="btn btn-primary addRow pull-left">
													添加一行
												</button>
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
														订单数量
													</th>
													<th width="10%">
														单价
													</th>
													<th width="15%">
														操作
													</th>
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
													<td class="produce_weight"><%=detail.getProduce_weight()%>
													</td>
													<td class="yarn_name"><%=SystemCache.getMaterialName(detail.getYarn())%>
													</td>
													<td class="size"><%=detail.getSize()%>
													</td>
													<td class="quantity"><%=detail.getQuantity()%>
													</td>
													<td class="price"><%=detail.getPrice()%>
													</td>
													<td class="_handle"><a class='editRow' href='#'>修改</a>    |    <a class='copyRow' href='#'>复制</a>    |    
														<a class='deleteRow' href='#'>删除</a>
													</td>
												</tr>

												<%
													}
												%>

											</tbody>
										</table>
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