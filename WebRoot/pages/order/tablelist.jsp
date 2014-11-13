<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.HeadBankOrder"%>
<%@page import="com.fuwei.entity.HeadBankOrderDetail"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Order order = (Order) request.getAttribute("order");
	HeadBankOrder headBankOrder = (HeadBankOrder) request
			.getAttribute("headBankOrder");
	List<HeadBankOrderDetail> headBankOrderDetailList = headBankOrder == null? new ArrayList<HeadBankOrderDetail>():headBankOrder.getDetaillist();

	String tabname = (String) request.getParameter("tab");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>系统信息管理 -- 桐庐富伟针织厂</title>
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
		<script src="js/plugins/jquery.jqGrid.min.js" type="text/javascript"></script>
		<link href="css/plugins/ui.jqgrid.css" rel="stylesheet"
			type="text/css" />

		<link href="css/order/tablelist.css" rel="stylesheet" type="text/css" />
		<script src="js/order/tablelist.js" type="text/javascript"></script>

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
						<li class="active">
							表格
						</li>
					</ul>
				</div>
				<div class="body">
					<div id="tab">
						<ul class="nav nav-tabs" role="tablist">
							<li class="active">
								<a href="#headbank" role="tab" data-toggle="tab">头带质量记录单</a>
							</li>

							<li>
								<a href="#headproduction" role="tab" data-toggle="tab">头带生产单</a>
							</li>

							<li>

								<a href="#purchaseorder" role="tab" data-toggle="tab">原材料采购单</a>
							</li>
							<li>
								<a href="#coloring" role="tab" data-toggle="tab">染色单</a>
							</li>
							<li>
								<a href="#stores" role="tab" data-toggle="tab">原材料仓库</a>
							</li>
						</ul>


						<div class="tab-content">
							<div class="tab-pane active" id="headbank">
								<div class="container-fluid">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="id" value="<%=headBankOrder==null?"":headBankOrder.getId() %>"/>
											<input type="hidden" name="orderId" value="<%=order.getId() %>"/>
											<button type="submit" class="pull-right btn btn-danger saveTable" data-loading-text="正在保存...">保存对当前表格的修改</button>
										</form>
										
										<div class="clear"></div>
										<div class="col-md-12 tablewidget">
											<table class="table">
												<caption>
													桐庐富伟针织厂质量记录单
												</caption>
												<tbody>
													<tr>
														<td>
															<table
																class="table table-responsive table-bordered tableTb">
																<tbody>
																	<tr>
																		<td rowspan="7" width="50%">
																			<a href="/<%=order.getImg()%>" class="thumbnail" target="_blank"> <img
																					id="previewImg" alt="200 x 100%"
																					src="/<%=order.getImg_s()%>"> </a>
																		</td>
																		<td width="20%">
																			生产单位
																		</td>
																		<td class="orderproperty"><%=order.getFactoryId() == null ? "" : SystemCache
					.getFactoryName(order.getFactoryId())%></td>
																	</tr>

																	<tr>
																		<td colspan="2" class="center">
																			订单信息
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
																			客户
																		</td>
																		<td><%=order.getKehu()%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=order.getProductNumber()%></td>
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
																		<td><%=SystemCache.getUserName(order.getCharge_user())%></td>
																	</tr>
																</tbody>
															</table>

														</td>
													</tr>
													<tr>
														<td>
															<button type="button" class="btn btn-primary addRow">
																添加一行
															</button>
														</td>
													</tr>
													<tr>
														<td>
															<table class="table table-responsive detailTb">
																<thead>
																	<tr>
																		<th width="15%">
																			颜色
																		</th>
																		<th width="15%">
																			克重(g)
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
																			价格(/个)
																		</th>
																		<th width="15%">
																			操作
																		</th>
																	</tr>
																</thead>
																<tbody>
																	<%for(HeadBankOrderDetail detail: headBankOrderDetailList){ %>
																	<tr class="tr" data='<%=SerializeTool.serialize(detail) %>'>
																		<td class="color"><%=detail.getColor() %>
																		</td>
																		<td class="weight"><%=detail.getWeight() %>
																		</td>
																		<td class="yarn"><%=detail.getYarn() %>
																		</td>
																		<td class="size"><%=detail.getSize() %>
																		</td>
																		<td class="quantity"><%=detail.getQuantity() %>
																		</td>
																		<td class="price"><%=detail.getPrice() %>
																		</td>
																		<td class="_handle">
																			<a class='editRow' href='#'>修改</a> | <a class='deleteRow' href='#'>删除</a>
																		</td>
																	</tr>

																<%} %>
																	
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

							</div>


						</div>
						
	<div class="tab-content">
							<div class="tab-pane active" id="headproduction">
								<div class="container-fluid">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="id" value="<%=headBankOrder==null?"":headBankOrder.getId() %>"/>
											<input type="hidden" name="orderId" value="<%=order.getId() %>"/>
											<button type="submit" class="pull-right btn btn-danger saveTable" data-loading-text="正在保存...">保存对当前表格的修改</button>
										</form>
										
										<div class="clear"></div>
										<div class="col-md-12 tablewidget">
											<table class="table">
												<caption>
													桐庐富伟针织厂头带生产单
												</caption>
												<tbody>
													<tr>
														<td>
															<table
																class="table table-responsive table-bordered tableTb">
																<tbody>
																	<tr>
																		<td rowspan="7" width="50%">
																			<a href="/<%=order.getImg()%>" class="thumbnail" target="_blank"> <img
																					id="previewImg" alt="200 x 100%"
																					src="/<%=order.getImg_s()%>"> </a>
																		</td>
																		<td width="20%">
																			生产单位
																		</td>
																		<td class="orderproperty"><%=order.getFactoryId() == null ? "" : SystemCache
					.getFactoryName(order.getFactoryId())%></td>
																	</tr>

																	<tr>
																		<td colspan="2" class="center">
																			订单信息
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
																			客户
																		</td>
																		<td><%=order.getKehu()%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=order.getProductNumber()%></td>
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
																		<td><%=SystemCache.getUserName(order.getCharge_user())%></td>
																	</tr>
																</tbody>
															</table>

														</td>
													</tr>
													<tr>
														<td>
															<button type="button" class="btn btn-primary addRow">
																添加一行
															</button>
														</td>
													</tr>
													<tr>
														<td>
															<table class="table table-responsive detailTb">
																<thead>
																	<tr>
																		<th width="15%">
																			颜色
																		</th>
																		<th width="15%">
																			克重(g)
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
																			价格(/个)
																		</th>
																		<th width="15%">
																			操作
																		</th>
																	</tr>
																</thead>
																<tbody>
																	<%for(HeadBankOrderDetail detail: headBankOrderDetailList){ %>
																	<tr class="tr" data='<%=SerializeTool.serialize(detail) %>'>
																		<td class="color"><%=detail.getColor() %>
																		</td>
																		<td class="weight"><%=detail.getWeight() %>
																		</td>
																		<td class="yarn"><%=detail.getYarn() %>
																		</td>
																		<td class="size"><%=detail.getSize() %>
																		</td>
																		<td class="quantity"><%=detail.getQuantity() %>
																		</td>
																		<td class="price"><%=detail.getPrice() %>
																		</td>
																		<td class="_handle">
																			<a class='editRow' href='#'>修改</a> | <a class='deleteRow' href='#'>删除</a>
																		</td>
																	</tr>

																<%} %>
																	
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

							</div>


						</div>
					</div>
				</div>
				<!--
						 添加编辑质量记录单对话框 -->
				<div class="modal fade tableRowDialog" id="headbankDialog">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal">
									<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
								</button>
								<h4 class="modal-title">
									添加一行
									<!--
									添加一行
									<span class="tablename">质量记录单</span>
								-->
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
										<label for="yarn" class="col-sm-3 control-label">
											纱线种类
										</label>
										<div class="col-sm-8">
											<input type="text" name="yarn" id="yarn" class="form-control require" />
										</div>
										<div class="col-sm-1"></div>
									</div>
									<div class="form-group col-md-12">
										<label for="size" class="col-sm-3 control-label">
											尺寸
										</label>
										<div class="col-sm-8">
											<input type="text" name="size" id="size" class="form-control require" />
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
											价格(个)
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
				<!-- 添加编辑质量记录单对话框 -->

			</div>
	</body>
</html>
<!--<script type="text/javascript">
	var tabname =
	";
	if (tabname == null || tabname == undefined) {
		$('#tab a:first').tab('show') // Select first tab
	}
	$("#tab a[href='#" + tabname + "']").tab('show') // Select tab by name
</script>-->