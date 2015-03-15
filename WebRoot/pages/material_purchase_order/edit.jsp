<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="com.fuwei.entity.Order"%>

<%@page import="com.fuwei.entity.ordergrid.HeadBankOrder"%>
<%@page import="com.fuwei.entity.ordergrid.HeadBankOrderDetail"%>
<%@page import="com.fuwei.entity.ordergrid.ProducingOrder"%>
<%@page import="com.fuwei.entity.ordergrid.ProducingOrderDetail"%>
<%@page import="com.fuwei.entity.ordergrid.ProducingOrderMaterialDetail"%>
<%@page import="com.fuwei.entity.ordergrid.PlanOrder"%>
<%@page import="com.fuwei.entity.ordergrid.PlanOrderDetail"%>
<%@page import="com.fuwei.entity.ordergrid.PlanOrderProducingDetail"%>
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
		<script src="js/material_purchase_order/add.js" type="text/javascript"></script>

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
							创建原材料采购单
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid materialorderWidget">
						<div class="row">
							<form class="saveform">
								<button type="submit"
									class="pull-right btn btn-danger saveTable"
									data-loading-text="正在保存...">
									创建原材料采购单
								</button>
								<a target="_blank" type="button"
									class="pull-right btn btn-success printBtn"
									data-loading-text="正在打印..."> 打印 </a>
								<div class="clear"></div>
								<div class="col-md-12 tablewidget">
									<table class="table">
										<caption>
											桐庐富伟针织厂原材料采购单
										</caption>
										
										<tbody>
											<tr>
												<td>
													<table
														class="table table-responsive table-bordered tableTb">
														<tbody>
															<tr>
																<td rowspan="7" width="50%">
																	<a href="/" class="thumbnail"
																		target="_blank"> <img id="previewImg"
																			alt="200 x 100%" src="/">
																	</a>
																</td>
																<td width="20%">
																	采购单位
																</td>
																<td class="orderproperty">
																	<input class="form-control require" type="text"
																		name="company"
																		value="" />
																</td>

															</tr>
															<tr>
																<td width="20%">
																	订购日期
																</td>
																<td class="orderproperty">
																	<input class="form-control date require" type="text"
																		name="purchase_at"
																		value="" />
																</td>
															</tr>
														
															<tr>
																<td>
																	公司
																</td>
																<td><input class="form-control require" type="text"
																		name="companyId"
																		value="" /></td>
															</tr>
															<tr>
																<td>
																	客户
																</td>
																<td><input class="form-control require" type="text"
																		name="kehu"
																		value="" /></td>
															</tr>
															<tr>
																<td>
																	货号
																</td>
																<td><input class="form-control require" type="text"
																		name="productNumber"
																		value="" /></td>
															</tr>
															<tr>
																<td>
																	款名
																</td>
																<td><input class="form-control require" type="text"
																		name="name"
																		value="" /></td>
															</tr>
														</tbody>
													</table>

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
												<input type="text" name="material" id="material"
													class="form-control require" />
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
				</div>
			</div>
		</div>
	</body>
</html>