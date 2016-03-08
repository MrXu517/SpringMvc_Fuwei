<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.ordergrid.GongxuProducingOrder"%>
<%@page import="com.fuwei.entity.ordergrid.GongxuProducingOrderDetail"%>
<%@page import="com.fuwei.entity.ordergrid.GongxuProducingOrderMaterialDetail"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%
	Order order = (Order) request.getAttribute("order");
	String productfactoryStr = (String)request.getAttribute("productfactoryStr");

	Boolean has_gongxu_producing_order = SystemCache.hasAuthority(session,"gongxu_producing_order/index");
	Boolean has_gongxu_producing_order_add = SystemCache.hasAuthority(session,"gongxu_producing_order/add");
	Boolean has_gongxu_producing_order_delete = SystemCache.hasAuthority(session,"gongxu_producing_order/delete");
	Boolean has_gongxu_producing_price = SystemCache.hasAuthority(session,"gongxu_producing_order/price");
	List<GongxuProducingOrder> gongxuProducingOrderList = (List<GongxuProducingOrder>) request
			.getAttribute("gongxuProducingOrderList");
	gongxuProducingOrderList = gongxuProducingOrderList == null ? new ArrayList<GongxuProducingOrder>()
			: gongxuProducingOrderList;
%>
<!DOCTYPE html>
<html>
	<head>

		<title>工序加工单 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8">
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<!-- 为了让IE浏览器运行最新的渲染模式 -->
	</head>
	<body>
		<%if(has_gongxu_producing_order_add && !order.isDelivered()){ %>
								<div class="emptyrecordwidget">
									<p>
										如果您要创建工序加工单，请点击下方的按钮
									</p>
									<a href="gongxu_producing_order/<%=order.getId()%>/add"
										class="btn btn-primary" id="createGongxuProducingorderBtn">创建工序加工单</a>
								</div>
								<%} %>
								<a  href="printorder/print?orderId=<%=order.getId() %>&gridName=gongxuproduceorder" target="_blank" type="button"
												class="printBtn btn btn-success"
												data-loading-text="正在打印..."> 打印工序加工单 </a>
								
							
								<%
									for (GongxuProducingOrder gongxuProducingOrder : gongxuProducingOrderList) {
										List<GongxuProducingOrderDetail> gongxuProducingOrderDetailList = gongxuProducingOrder == null ? new ArrayList<GongxuProducingOrderDetail>()
												: gongxuProducingOrder.getDetaillist();
										List<GongxuProducingOrderMaterialDetail> gongxuProducingOrderMaterialDetailList = gongxuProducingOrder == null ? new ArrayList<GongxuProducingOrderMaterialDetail>()
												: gongxuProducingOrder.getDetail_2_list();
								%>
								<div class="container-fluid gongxuproduceorderWidget">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="id"
												value="<%=gongxuProducingOrder == null ? "" : gongxuProducingOrder
						.getId()%>" />
											<input type="hidden" name="orderId"
												value="<%=order.getId()%>" />	
												
											<%if(gongxuProducingOrder.isEdit()){ %>
												<%if(has_gongxu_producing_order_delete){ %>
													<a target="_blank" type="button"
													class="pull-right btn btn-default deleteTableBtn"
													data-loading-text="正在删除..."> 删除 </a>
												<%} %>
												<%if(has_gongxu_producing_order_add){ %>
												<a href="gongxu_producing_order/put/<%=gongxuProducingOrder.getId() %>" target="_blank" type="button"
													class="pull-right btn btn-default"
													data-loading-text="正在跳转页面......"> 编辑 </a><%} %>
											<%} %>
											
											<a href="gongxu_producing_order/print/<%=gongxuProducingOrder.getId()%>" target="_blank" type="button"
												class="pull-right btn btn-success"
												data-loading-text="正在打印..."> 打印 </a>
										</form>

										<div class="clear"></div>
										<div class="col-md-12 tablewidget">
											<table class="table">
												<caption>
													桐庐富伟针织厂工序加工单<%if(gongxuProducingOrder.getInbill()){ %>
														<div id="statusDiv" class="inbill">已结账</div>
													<%} %>
												</caption>
												<thead>
													<tr>
														<td colspan="3" class="pull-right orderNumber">
															№：<%=order.getOrderNumber()%> - <%=gongxuProducingOrder.getNumber() %></td>
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
																			<a href="/<%=gongxuProducingOrder.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=gongxuProducingOrder.getImg_s()%>">
																			</a>
																		</td>
																		<td width="20%">
																			加工单位
																		</td>
																		<td class="orderproperty"><%=SystemCache.getFactoryName(gongxuProducingOrder
								.getFactoryId())%></td>
																	</tr>

																	<tr>
																		<td>
																			加工工序
																		</td>
																		<td><%=SystemCache.getGongxuName(gongxuProducingOrder
										.getGongxuId())%></td>
																	</tr>
																	<tr>
																		<td>
																			公司
																		</td>
																		<td><%=SystemCache.getCompanyName(gongxuProducingOrder
										.getCompanyId())%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=gongxuProducingOrder.getCompany_productNumber()%></td>
																	</tr>
																	<tr>
																		<td>
																			款名
																		</td>
																		<td><%=gongxuProducingOrder.getName()%></td>
																	</tr>
																	<tr>
																		<td>
																			跟单
																		</td>
																		<td><%=SystemCache.getEmployeeName(gongxuProducingOrder.getCharge_employee())%></td>
																	</tr>
																	<tr>
																		<td>
																			备注
																		</td>
																		<td><%=gongxuProducingOrder.getMemo()==null?"":gongxuProducingOrder.getMemo()%></td>
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
																			生产数量(个、双)
																		</th>
																		<%if(has_gongxu_producing_price){ %>
																		<th width="15%">
																			价格(/打)
																		</th>
																		<%} %>
																	</tr>
																</thead>
																<tbody>
																	<%
																		for (GongxuProducingOrderDetail detail : gongxuProducingOrderDetailList) {
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
																		<%if(has_gongxu_producing_price){ %>
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
																	if(gongxuProducingOrderMaterialDetailList!=null){
																		for (GongxuProducingOrderMaterialDetail detail : gongxuProducingOrderMaterialDetailList) {
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
																		}}
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