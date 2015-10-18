<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.Material"%>
<%@page import="com.fuwei.entity.ordergrid.PlanOrder"%>
<%@page import="com.fuwei.entity.ordergrid.PlanOrderDetail"%>
<%@page import="com.fuwei.entity.ordergrid.PlanOrderProducingDetail"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.entity.GongXu"%>
<%@page import="com.fuwei.entity.ordergrid.GongxuProducingOrderDetail"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Order order = (Order) request.getAttribute("order");

	List<GongxuProducingOrderDetail> detaillist = (List<GongxuProducingOrderDetail>) request
			.getAttribute("detaillist");

	if (detaillist == null) {
		detaillist = new ArrayList<GongxuProducingOrderDetail>();
	}
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>创建工序加工单 -- 桐庐富伟针织厂</title>
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
		<script src="<%=basePath%>js/plugins/WdatePicker.js"
			type="text/javascript"></script>
		<script src="js/common/common.js" type="text/javascript"></script>
		<script src="js/plugins/jquery.jqGrid.min.js" type="text/javascript"></script>
		<link href="css/plugins/ui.jqgrid.css" rel="stylesheet"
			type="text/css" />

		<link href="css/order/bill.css" rel="stylesheet" type="text/css" />
		<script src="js/order/ordergrid.js" type="text/javascript"></script>
		<script src="js/gongxu_producing_order/addbyorder.js" type="text/javascript"></script>

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
							<a href="order/detail/<%=order.getId()%>">订单详情</a>
						</li>
						<li>
							<a href="order/tablelist?orderId=<%=order.getId()%>&tab=gongxuproduceorder">表格</a>
						</li>
						<li class="active">
							创建工序加工单
						</li>
					</ul>
				</div>
				<div class="body">

					<!-- 生产单  -->


					<div class="container-fluid">
						<div class="row">
							<form class="saveform">
								<input type="hidden" name="id" value="" />
								<input type="hidden" name="orderId" value="<%=order.getId()%>" />
							


								<div class="clear"></div>
								<div class="col-md-12 tablewidget">
									<table class="table">
										<caption id="tablename">
											桐庐富伟针织厂工序加工单	<button type="submit"
									class="pull-right btn btn-danger saveTable"
									data-loading-text="正在保存...">
									创建工序加工单
								</button>

										</caption></table>
									<table>
										
										<tbody>
											<tr>
												<td>
													<table
														class="table table-responsive table-bordered tableTb">
														<tbody>
															<tr>
																<td rowspan="7" width="50%">
																	<a href="/<%=order.getImg()%>" class="thumbnail"
																		target="_blank"> <img id="previewImg"
																			alt="200 x 100%" src="/<%=order.getImg_s()%>">
																	</a>
																</td>
																<td width="20%">
																	生产单位
																</td>
																<td class="orderproperty">
																	<select class="form-control require" name="factoryId"
																		id="factoryId">
																		<option value="">
																			未选择
																		</option>
																		<%
																			for (Factory factory : SystemCache.produce_factorylist) {
																		%>
																		<option value="<%=factory.getId()%>"><%=factory.getName()%></option>
																		<%
																			}
																		%>
																	</select>
																</td>
															</tr>

															<tr>
																<td>
																	生产工序
																</td>
																<td class="orderproperty">
																	<select class="form-control require" name="gongxuId"
																		id="gongxuId">
																		<option value="">
																			未选择
																		</option>
																		<%
																			for (GongXu gongxu : SystemCache.gongxulist) {
																		%>
																		<option value="<%=gongxu.getId()%>"><%=gongxu.getName()%></option>
																		<%
																			}
																		%>
																	</select>
																</td>
															</tr>
															<tr>
																<td>
																	公司
																</td>
																<td><%=SystemCache.getCompanyName(order.getCompanyId())%></td>
															</tr>
															<tr>
																<td>
																	货号
																</td>
																<td><%=order.getCompany_productNumber()%></td>
															</tr>
															<tr>
																<td>
																	款名
																</td>
																<td><%=order.getName()%></td>
															</tr>
															<tr>
																<td>
																	跟单
																</td>
																<td><%=SystemCache.getEmployeeName(order.getCharge_employee())%></td>
															</tr>
															<tr>
																<td>
																	备注
																</td>
																<td class="orderproperty">
																	<textarea class="form-control" name="memo"
																		id="memo"></textarea></td>
															</tr>
														</tbody>
													</table>

												</td>
											</tr>
											<tr>
												<td>
													<table class="table table-responsive detailTb">
														<caption>

															颜色及数量
														</caption>
														<thead>
															<tr>
																<th width="15%">
																	颜色
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
																	生产数量
																</th>
																<th width="15%">
																	价格(/打)
																</th>
																
															</tr>
														</thead>
														<tbody>
															<%
																for (GongxuProducingOrderDetail detail : detaillist) {
															%>
															<tr class="tr"
																data='<%=SerializeTool.serialize(detail)%>'>
																<td class="color"><%=detail.getColor()%>
																</td>
																<td class="produce_weight"><%=detail.getProduce_weight()%>
																</td>
																<td class="yarn_name"><%=SystemCache.getMaterialName(detail.getYarn())%>
																</td>
																<td class="size"><%=detail.getSize()%>
																</td>
																<td class="int">
																	<input type="text" class="form-control quantity value"
																		value="<%=detail.getQuantity()%>" />
																</td>
																
																<td class="double">
																	<input type="text" class="form-control price value"
																		value="<%=detail.getPrice()%>" />
																</td>
																
																
															</tr>

															<%
																}
															%>

														</tbody>
													</table>
													<div id="navigator"></div>
												</td>
											</tr>
										
									<tr>
										<td>
											<table class="table table-responsive detailTb2">
												<caption>
													<button type="button"
														class="btn btn-primary addRow pull-left">
														添加一行
													</button>
													生产材料信息
												</caption>
												<thead>
													<tr>
														<th width="20%">
															材料
														</th>
														<th width="20%">
															色号
														</th>
														<th width="20%">
															数量(kg)
														</th>
														<th width="25%">
															标准色样
														</th>
														<th width="15%">
															操作
														</th>
													</tr>
												</thead>
												<tbody>
												</tbody>
											</table>
											<div id="navigator"></div>
										</td>
									</tr>
									</tbody>
									</table>

								</div>

							</form>
						</div>
					</div>

					<!--
						 添加编辑明细对话框 -->
					<div class="modal fade tableRowDialog" id="producingDialog">
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
												<select name="yarn" id="yarn" class="form-control require">
													<option value="">
														未选择
													</option>
													<%
														for (Material material : SystemCache.materiallist) {
													%>
													<option value="<%=material.getId()%>"><%=material.getName()%></option>
													<%
														}
													%>
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
					<div class="modal fade tableRowDialog" id="producingDetailDialog">
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
											<label for="material" class="col-sm-3 control-label">
												材料
											</label>
											<div class="col-sm-8">
												<select name="material" id="material" class="form-control require">
																<option value="">未选择</option>
																<%for(Material material : SystemCache.materiallist){ %>
																	<option value="<%=material.getId() %>" ><%=material.getName() %></option>
																<%} %>
															</select>
											</div>
											<div class="col-sm-1"></div>
										</div>
										<div class="form-group col-md-12">
											<label for="color" class="col-sm-3 control-label">
												色号
											</label>
											<div class="col-sm-8">
												<input type="text" name="color" id="color"
													class="form-control require" />
											</div>
											<div class="col-sm-1"></div>
										</div>
										<div class="form-group col-md-12">
											<label for="quantity" class="col-sm-3 control-label">
												数量(kg)
											</label>
											<div class="col-sm-8">
												<input type="text" name="quantity" id="quantity"
													class="form-control double require" />
											</div>
											<div class="col-sm-1"></div>
										</div>
										<div class="form-group col-md-12">
											<label for="colorsample" class="col-sm-3 control-label">
												标准色样
											</label>
											<div class="col-sm-8">
												<input type="text" name="colorsample" id="colorsample"
													class="form-control" />
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
					<!-- 添加编辑生产单对话框 -->
				</div>


			</div>


		</div>
	</body>
</html>