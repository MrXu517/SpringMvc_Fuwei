<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.ordergrid.StoreOrder"%>
<%@page import="com.fuwei.entity.ordergrid.StoreOrderDetail"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.entity.Material"%>
<%@page import="com.fuwei.entity.Factory"%>
<%
	Order order = (Order) request.getAttribute("order");
	StoreOrder storeOrder = (StoreOrder) request
			.getAttribute("storeOrder");
	List<StoreOrderDetail> storeOrderDetailList = storeOrder == null ? new ArrayList<StoreOrderDetail>()
			: storeOrder.getDetaillist();
	String productfactoryStr = (String)request.getAttribute("productfactoryStr");
	Boolean has_store_order_save = SystemCache.hasAuthority(session,
			"order/store");
%>
<!DOCTYPE html>
<html>
	<head>

		<title>原材料记录单 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8">
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<!-- 为了让IE浏览器运行最新的渲染模式 -->
	</head>
	<body class="">
		<div class="container-fluid">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="id"
												value="<%=storeOrder == null ? "" : storeOrder.getId()%>" />
											<input type="hidden" name="orderId"
												value="<%=order.getId()%>" />
											<%if(has_store_order_save && (storeOrder==null || storeOrder.isEdit())){ %>
											<button type="submit"
												class="pull-right btn btn-danger saveTable"
												data-loading-text="正在保存...">
												保存对当前表格的修改
											</button>
											<%} %>
											<a target="_blank" type="button"
												class="pull-right btn btn-success printBtn"
												data-loading-text="正在打印..."> 打印 </a>
										</form>

										<div class="clear"></div>
										<div class="col-md-12 tablewidget">
											<table class="table">
												<caption>
													桐庐富伟针织厂原材料仓库
												</caption>
												<thead>
													<tr>
														<td colspan="3" class="pull-right orderNumber">
															№：<%=order.getOrderNumber()%></td>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td>
															<table
																class="table table-responsive table-bordered tableTb">
																<tbody>
																	<tr>
																		<td rowspan="8" width="50%">
																			<a href="/<%=order.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=order.getImg_s()%>">
																			</a>
																		</td>
																		<td width="20%">
																			生产单位
																		</td>
																		<td class="orderproperty"><%=productfactoryStr %></td>
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
																		<td><%=SystemCache.getCustomerName(order.getCustomerId())%></td>
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
																			发货时间
																		</td>
																		<td><%=DateTool.formatDateYMD(order.getEnd_at())%></td>
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
																		class="btn btn-primary addRow pull-left" save-widget="true">
																		添加一行
																	</button>
																	
																	材料列表
																</caption>
																<thead>
																	<tr>
																		<th width="15%">
																			色号
																		</th>
																		<th width="15%">
																			材料
																		</th>
																		<th width="15%">
																			总数量(kg)
																		</th>
																		<th width="15%">
																			领取人
																		</th>
																		<th width="15%">
																			标准样纱
																		</th>
																		<th width="15%"  save-widget="true">
																			操作
																		</th>
																	</tr>
																</thead>
																<tbody>
																	<%
																		for (StoreOrderDetail detail : storeOrderDetailList) {
																	%>
																	<tr class="tr"
																		data='<%=SerializeTool.serialize(detail)%>'>
																		<td class="color"><%=detail.getColor()%>
																		</td>
																		<td class="material_name"><%=SystemCache.getMaterialName(detail.getMaterial())%>
																		</td>
																		<td class="quantity"><%=detail.getQuantity()%>
																		</td>
																		<td class="factory_name"><%=SystemCache.getFactoryName(detail.getFactoryId())%>
																		</td>
																		<td class="yarn"><%=detail.getYarn()%>
																		</td>
																		<td class="_handle" save-widget="true">
																			<a class='copyRow' href='#'>复制</a> | 
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


									</div>
								</div>

								<!--
						 			添加编辑原材料仓库对话框 -->
								<div class="modal fade tableRowDialog" id="storeDialog">
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
														<label for="material" class="col-sm-3 control-label">
															材料
														</label>
														<div class="col-sm-8">
															<select name="material" id="material"
																class="form-control require">
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
														<label for="quantity" class="col-sm-3 control-label">
															总数量(kg)
														</label>
														<div class="col-sm-8">
															<input type="text" name="quantity" id="quantity"
																class="form-control double require" />
														</div>
														<div class="col-sm-1"></div>
													</div>
													<div class="form-group col-md-12">
														<label for="factoryId" class="col-sm-3 control-label">
															领取人
														</label>
														<div class="col-sm-8">
															<select class="form-control require" name="factoryId"
																id="factoryId">
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
													<div class="form-group col-md-12">
														<label for="yarn" class="col-sm-3 control-label">
															标准样纱
														</label>
														<div class="col-sm-8">
															<input type="text" name="yarn" id="yarn"
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
								<!-- 添加编辑原材料仓库对话框 -->
	
	</body>
</html>