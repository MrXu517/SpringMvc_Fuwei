<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.ordergrid.ProducingOrder"%>
<%@page import="com.fuwei.entity.ordergrid.ProducingOrderDetail"%>
<%@page import="com.fuwei.entity.ordergrid.ProducingOrderMaterialDetail"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%
	Order order = (Order) request.getAttribute("order");
	List<ProducingOrder> producingOrderList = (List<ProducingOrder>) request
			.getAttribute("producingOrderList");
	producingOrderList = producingOrderList == null? new ArrayList<ProducingOrder>()
			: producingOrderList;
	String productfactoryStr = (String)request.getAttribute("productfactoryStr");
	Boolean has_order_producing_price = SystemCache.hasAuthority(session,
			"order/producing/price");
	Boolean has_producing_order_edit = SystemCache.hasAuthority(session,
			"order/producing");
	Boolean has_producing_order_print = SystemCache.hasAuthority(session,
			"order/producing/print");
	Boolean has_producing_order_delete = SystemCache.hasAuthority(session,
			"order/producing/delete");
	Boolean has_order_producing_price_edit = SystemCache.hasAuthority(session,"order/producing/price_edit");
	Boolean has_order_producing_price_request = SystemCache.hasAuthority(session,"order/producing/price_request");
%>
<!DOCTYPE html>
<html>
	<head>

		<title>生产单 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8">
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<!-- 为了让IE浏览器运行最新的渲染模式 -->
	</head>
	<body>
	<%if(has_producing_order_edit && !order.isDelivered()){ %>
								<div class="emptyrecordwidget">
									<p>
										如果您要创建生产单，请点击下方的按钮
									</p>
									<a href="producing_order/<%=order.getId()%>/add"
										class="btn btn-primary" id="createProducingorderBtn">创建生产单</a>
								</div>
								<%} %>
								<%if(has_producing_order_print){ %>
								<a  href="printorder/print?orderId=<%=order.getId() %>&gridName=producingorder" target="_blank" type="button"
												class="printAllBtn btn btn-success"
												data-loading-text="正在打印..."> 打印生产单 </a>
								<%} %>
								<%if(has_order_producing_price_request){ %>
								<button orderid="<%=order.getId() %>" ordernumber="<%=order.getOrderNumber() %>" type="button" class="priceRequestBtn btn btn-info" data-loading-text="正在请求划价..."> 请求划价  </button>
								<%} %>
							
								<%
									for (ProducingOrder producingOrder : producingOrderList) {
										List<ProducingOrderDetail> producingOrderDetailList = producingOrder == null ? new ArrayList<ProducingOrderDetail>()
												: producingOrder.getDetaillist();
										List<ProducingOrderMaterialDetail> producingOrderMaterialDetailList = producingOrder == null ? new ArrayList<ProducingOrderMaterialDetail>()
												: producingOrder.getDetail_2_list();
								%>
								<div class="container-fluid producingorderWidget">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="id"
												value="<%=producingOrder == null ? "" : producingOrder
						.getId()%>" />
											<input type="hidden" name="orderId"
												value="<%=order.getId()%>" />	
												
											<%if(producingOrder.isEdit()){ %>
												<%if(has_producing_order_delete){ %>
													<a target="_blank" type="button"
													class="pull-right btn btn-default deleteTableBtn"
													data-loading-text="正在删除..."> 删除 </a>
												<%} %>
												<%if(has_producing_order_edit){ %>
												<a href="producing_order/put/<%=producingOrder.getId() %>" target="_blank" type="button"
													class="pull-right btn btn-default"
													data-loading-text="正在跳转页面......"> 编辑 </a><%} %>
											<%} %>
											<%if(has_order_producing_price_edit){ %>
												<a target="_blank" href="producing_order/price/<%=producingOrder.getId() %>" type="button" class="pull-right btn btn-success"> 开始划价  </a>
											 <%} %>
											<%if(has_producing_order_print){ %>
											<a href="producing_order/print/<%=producingOrder.getId()%>" target="_blank" type="button"
												class="pull-right btn btn-success"
												data-loading-text="正在打印..."> 打印 </a>
											<%} %>
										</form>

										<div class="clear"></div>
										<div class="col-md-12 tablewidget">
											<table class="table">
												<caption>
													桐庐富伟针织厂生产单	
													<%if(producingOrder.getInbill()){ %>
														<div id="statusDiv" class="inbill">已结账</div>
													<%} %>
										
												</caption>
												<thead>
													<tr>
														<td colspan="3" class="pull-right orderNumber">
															№：<%=order.getOrderNumber()%> - <%=producingOrder.getNumber() %></td>
													</tr>
												</thead>
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
																		<td class="orderproperty"><%=SystemCache.getFactoryName(producingOrder
								.getFactoryId())%></td>
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
																		<td><%=SystemCache.getCompanyName(order
										.getCompanyId())%></td>
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
																		
																		<%if(has_order_producing_price){ %>
																		<th width="15%">
																			价格(/个、顶、套)
																		</th>
																		<%} %>

																	</tr>
																</thead>
																<tbody>
																	<%
																		for (ProducingOrderDetail detail : producingOrderDetailList) {
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
																		<td class="quantity"><%=detail.getQuantity()%>
																		</td>
																		<%if(has_order_producing_price){ %>
																		<td class="price"><%=detail.getPrice()%></td>
																		<%} %>
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
																	</tr>
																</thead>
																<tbody>
																	<%
																		for (ProducingOrderMaterialDetail detail : producingOrderMaterialDetailList) {
																	%>
																	<tr class="tr"
																		data='<%=SerializeTool.serialize(detail)%>'>
																		<td class="material_name"><%=SystemCache.getMaterialName(detail
											.getMaterial())%>
																		</td>
																		<td class="color"><%=detail.getColor()%>
																		</td>
																		<td class="quantity"><%=detail.getQuantity()%>
																		</td>
																		<td class="colorsample"><%=detail.getColorsample()%>
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
								<%
									}
								%>
	</body>
	
</html>