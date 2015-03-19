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
<%@page import="com.fuwei.entity.ordergrid.FuliaoPurchaseOrderDetail"%>
<%@page import="com.fuwei.entity.ordergrid.FuliaoPurchaseOrder"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	FuliaoPurchaseOrder fuliaoPurchaseOrder = (FuliaoPurchaseOrder)request.getAttribute("fuliaoPurchaseOrder");
	List<FuliaoPurchaseOrderDetail> detaillist = fuliaoPurchaseOrder.getDetaillist();
	if(detaillist == null){
		detaillist = new ArrayList<FuliaoPurchaseOrderDetail>();
	}
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>编辑辅料采购单 -- 桐庐富伟针织厂</title>
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
		<script src="js/fuliao_purchase_order/addbyorder.js" type="text/javascript"></script>

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
							<a href="order/tablelist?orderId=<%=fuliaoPurchaseOrder.getOrderId()%>&tab=fuliaopurchaseorder">订单表格</a>
						</li>
						<li class="active">
							编辑辅料采购单
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid fuliaoorderWidget">
						<div class="row">
							<form class="saveform">
								<input type="hidden" id="id" name="id"
									value="<%=fuliaoPurchaseOrder.getId()%>" class="require" />
								<input type="hidden" id="sampleId" name="sampleId"
									value="<%=fuliaoPurchaseOrder.getSampleId()%>" class="require" />
								<input type="hidden" id="orderId" name="orderId"
									value="<%=fuliaoPurchaseOrder.getOrderId()%>" class="require" />
								<button type="submit"
									class="pull-right btn btn-danger saveTable"
									data-loading-text="正在保存...">
									保存修改
								</button>

								<div class="clear"></div>
								<div class="col-md-12 tablewidget">
									<table class="table">
										<caption>
											桐庐富伟针织厂辅料采购单
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
																<a class="thumbnail" id="sampleImgA"> <img
																		id="sampleImg" alt="350 x 100%"
																		src="/<%=fuliaoPurchaseOrder.getImg_s()%>"> </a>
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
																	value="<%=fuliaoPurchaseOrder.getName() == null ? "" : fuliaoPurchaseOrder.getName()%>" />

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
																	value="<%=fuliaoPurchaseOrder.getProductNumber() == null ? "" : fuliaoPurchaseOrder
					.getProductNumber()%>" />

															</div>
															<div class="col-sm-1"></div>
														</div>
														<div class="form-group col-md-6">
															<label for="factoryId" class="col-sm-3 control-label">
																供货方
															</label>
															<div class="col-sm-8">
																<select class="form-control require"
																	name="factoryId" id="factoryId">
																	<option value="">
																		未选择
																	</option>
																	<%
																		for (Factory factory : SystemCache.factorylist) {
																			if(factory.getId() == fuliaoPurchaseOrder.getFactoryId()){
																	%>
																	<option value="<%=factory.getId()%>" selected='selected'><%=factory.getName()%></option>
																	<%}else{ %>
																	<option value="<%=factory.getId()%>"><%=factory.getName()%></option>
																	<%
																		}
																		}
																	%>

																</select>
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
																	value="<%=fuliaoPurchaseOrder.getKehu() == null ? "" : fuliaoPurchaseOrder.getKehu()%>">
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
																			if (fuliaoPurchaseOrder.getCompanyId() != null
																					&& company.getId() == fuliaoPurchaseOrder.getCompanyId()) {
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
																	<option value="<%=fuliaoPurchaseOrder.getCharge_user()%>" selected><%=SystemCache.getUserName(fuliaoPurchaseOrder.getCharge_user())%></option>
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
																	id="orderNumber" value="<%=fuliaoPurchaseOrder.getOrderNumber() == null ? "" : fuliaoPurchaseOrder.getOrderNumber()%>">
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
															采购列表
														</caption>
														<thead>
															<tr>
																<th width="15%">
																				辅料类型
																			</th>
																			<th width="15%">
																				标准样
																			</th>
																			<th width="15%">
																				数量
																			</th>
																			<th width="15%">
																				价格
																			</th>
																			<th width="15%">
																				交期
																			</th>
																			<th width="15%">
																				操作
																			</th>
															</tr>
														</thead>
														<tbody>
															<%
																			for (FuliaoPurchaseOrderDetail detail : detaillist) {
																		%>
																		<tr class="tr"
																			data='<%=SerializeTool.serialize(detail)%>'>
																			<td class="style_name"><%=SystemCache.getMaterialName(detail.getStyle())%>
																			</td>
																			<td class="standardsample"><%=detail.getStandardsample()%>
																			</td>
																			<td class="quantity"><%=detail.getQuantity()%>
																			</td>
																			<td class="price"><%=detail.getPrice()%>
																			</td>
																			<td class="end_at"><%=detail.getEnd_at()%>
																			</td>
																			<td class="_handle">
																				<a class='editRow' href='#'>修改</a> |
																				<a class='deleteRow' href='#'>删除</a>
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

										</tbody>
									</table>

								</div>
							</form>
						</div>
					</div>
					
<!--
						 添加编辑辅料采购单对话框 -->
								<div class="modal fade tableRowDialog" id="fuliaopurchaseDialog">
									<div class="modal-dialog">
										<div class="modal-content">
											<div class="modal-header">
												<button type="button" class="close" data-dismiss="modal">
													<span aria-hidden="true">&times;</span><span
														class="sr-only">Close</span>
												</button>
												<h4 class="modal-title">
													添加一行
												</h4>
											</div>
											<div class="modal-body">
												<form class="form-horizontal rowform" role="form">
													<div class="form-group col-md-12">
														<label for="style" class="col-sm-3 control-label">
															辅料类型
														</label>
														<div class="col-sm-8">
															<select name="style" id="style" class="form-control require">
																<option value="">未选择</option>
																<%for(Material material : SystemCache.materiallist){ %>
																	<option value="<%=material.getId() %>" ><%=material.getName() %></option>
																<%} %>
															</select>
														</div>
														<div class="col-sm-1"></div>
													</div>
													<div class="form-group col-md-12">
														<label for="standardsample" class="col-sm-3 control-label">
															标准样
														</label>
														<div class="col-sm-8">
															<input type="text" name="standardsample"
																id="standardsample" class="form-control" />
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
														<label for="price" class="col-sm-3 control-label">
															价格
														</label>
														<div class="col-sm-8">
															<input type="text" name="price" id="price"
																class="form-control double require" />
														</div>
														<div class="col-sm-1"></div>
													</div>
													<div class="form-group col-md-12">
														<label for="end_at" class="col-sm-3 control-label">
															交期
														</label>
														<div class="col-sm-8">
															<input type="text" name="end_at" id="end_at"
																class="form-control date require" />
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
								<!-- 添加编辑辅料采购单对话框 -->

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