<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.entity.Material"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.entity.ordergrid.MaterialPurchaseOrderDetail"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Order order = (Order) request.getAttribute("order");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>创建原材料采购单 -- 桐庐富伟针织厂</title>
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

		<link href="css/order/tablelist.css" rel="stylesheet" type="text/css" />
		<script src="js/order/ordergrid.js" type="text/javascript"></script>
		<script src="js/material_purchase_order/addbyorder.js" type="text/javascript"></script>

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
							<a href="order/tablelist?orderId=<%=order.getId()%>&tab=materialpurchaseorder">表格</a>
						</li>
						<li class="active">
							创建原材料采购单
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid materialorderWidget">
						<div class="row">
							<form class="saveform">
								<input type="hidden" id="sampleId" name="sampleId"
									value="<%=order.getSampleId()%>" class="require" />
								<input type="hidden" id="orderId" name="orderId"
									value="<%=order.getId()%>" class="require" />
								<button type="submit"
									class="pull-right btn btn-danger saveTable"
									data-loading-text="正在保存...">
									创建原材料采购单
								</button>

								<div class="clear"></div>
								<div class="col-md-12 tablewidget">
									<table class="table">
										<caption>
											桐庐富伟针织厂原材料采购单
										</caption>

										<tbody>
											<tr>
												<td>
													<fieldset>
														<legend>
															基本信息
														</legend>
														<div class="form-group col-md-6">
															<label for="img" class="col-sm-3 control-label">
																样品图片
															</label>
															<div class="col-sm-8">
																<a href="#" class="thumbnail" id="sampleImgA"> <img
																		id="sampleImg" alt="350 x 100%"
																		src="/<%=order.getImg_s()%>"> </a>
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
															<label for="factoryId" class="col-sm-3 control-label">
																采购单位
															</label>
															<div class="col-sm-8">
																<select class="form-control require"
																	name="factoryId" id="factoryId">
																	<option value="">
																		未选择
																	</option>
																	<%
																		for (Factory factory : SystemCache.factorylist) {
																	%>
																	<option value="<%=factory.getId()%>"><%=factory.getName()%></option>
																	<%
																		}
																	%>

																</select>
															</div>
															<div class="col-sm-1"></div>
														</div>

														<div class="form-group col-md-6">
															<label for="purchase_at" class="col-sm-3 control-label">
																订购日期
															</label>
															<div class="col-sm-8">
																<input class="form-control date require" type="text"
																	name="purchase_at" id="purchase_at"
																	value="<%=DateTool.formatDateYMD(DateTool.now())%>" />
															</div>
															<div class="col-sm-1"></div>
														</div>

														<div class="form-group col-md-6">
															<label for="kehu" class="col-sm-3 control-label">
																客户
															</label>
															<div class="col-sm-8">
																<input disabled type="text" class="form-control"
																	name="kehu" id="kehu"
																	value="<%=order.getKehu() == null ? "" : order.getKehu()%>">
															</div>
															<div class="col-sm-1"></div>
														</div>
														<div class="form-group col-md-6">
															<label for="companyId" class="col-sm-3 control-label">
																公司
															</label>
															<div class="col-sm-8">
																<select disabled class="form-control require"
																	name="companyId" id="companyId" placeholder="公司">
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
															<label for="charge_user" class="col-sm-3 control-label">
																跟单人
															</label>
															<div class="col-sm-8">
																<select disabled name="charge_user" id="charge_user"
																	class="form-control">
																	<option value="<%=order.getCharge_user()%>" selected><%=SystemCache.getUserName(order.getCharge_user())%></option>
																</select>

															</div>
															<div class="col-sm-1"></div>
														</div>
														<div class="form-group col-md-6">
															<label for="orderNumber" class="col-sm-3 control-label">
																生产单号
															</label>
															<div class="col-sm-8">
																<input disabled type="text" class="form-control" name="orderNumber"
																	id="orderNumber" value="<%=order.getOrderNumber() == null ? "" : order.getOrderNumber()%>">
															</div>
															<div class="col-sm-1"></div>
														</div>
													</fieldset>


												</td>
											</tr>
											<tr>
												<td>
													<table class="table table-responsive detailTb">
														<caption>
															<button type="button"
																class="btn btn-primary addRow pull-left">
																添加一行
															</button>
															材料列表
														</caption>
														<thead>
															<tr>
																<th width="15%">
																	材料品种
																</th>
																<th width="15%">
																	规格
																</th>
																<th width="15%">
																	数量
																</th>
																<th width="15%">
																	批次号
																</th>
																<th width="15%">
																	价格（含税）
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
						 			添加编辑原材料采购对话框 -->
					<div class="modal fade tableRowDialog" id="materialpurchaseDialog">
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
												材料品种
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
											<label for="scale" class="col-sm-3 control-label">
												规格
											</label>
											<div class="col-sm-8">
												<input type="text" name="scale" id="scale"
													class="form-control require" />
											</div>
											<div class="col-sm-1"></div>
										</div>
										<div class="form-group col-md-12">
											<label for="quantity" class="col-sm-3 control-label">
												数量
											</label>
											<div class="col-sm-8">
												<input type="text" name="quantity" id="quantity"
													class="form-control int require" />
											</div>
											<div class="col-sm-1"></div>
										</div>
										<div class="form-group col-md-12">
											<label for="color" class="col-sm-3 control-label">
												批次号
											</label>
											<div class="col-sm-8">
												<input type="text" name="batch_number" id="batch_number"
													class="form-control" />
											</div>
											<div class="col-sm-1"></div>
										</div>


										<div class="form-group col-md-12">
											<label for="price" class="col-sm-3 control-label">
												价格（含税）
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
					<!-- 添加编辑原材料采购对话框 -->

					<!-- 选择样品对话框 -->
					<div class="modal fade" id="sampleDialog">
						<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal">
										<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
									</button>
									<h4 class="modal-title">
										请选择样品
									</h4>
								</div>
								<div class="modal-body">
									<iframe id="sampleIframe" name="sampleIframe" frameborder=0></iframe>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-default"
										data-dismiss="modal">
										关闭
									</button>
								</div>
							</div>
							<!-- /.modal-content -->
						</div>
						<!-- /.modal-dialog -->
					</div>
					<!-- 选择样品对话框 -->

				</div>
			</div>
		</div>
	</body>
</html>