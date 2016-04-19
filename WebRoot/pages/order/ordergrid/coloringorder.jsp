<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.ordergrid.ColoringOrder"%>
<%@page import="com.fuwei.entity.ordergrid.ColoringOrderDetail"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.entity.Order"%>
<%
	Order order = (Order) request.getAttribute("order");
	//染色单
	List<ColoringOrder> coloringOrderList = (List<ColoringOrder>) request
			.getAttribute("coloringOrderList");
	coloringOrderList = coloringOrderList == null ? new ArrayList<ColoringOrder>()
			: coloringOrderList;
	String productfactoryStr = (String) request
			.getAttribute("productfactoryStr");
	Boolean has_coloring_order_save = SystemCache.hasAuthority(session,
			"order/coloring");
	Boolean has_coloring_order_delete = SystemCache.hasAuthority(session,
			"coloring_order/delete");
	Boolean has_coloring_order_print = SystemCache.hasAuthority(session,
			"coloring_order/print");
%>
<!DOCTYPE html>
<html>
	<head>

		<title>染色单 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8">
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<!-- 为了让IE浏览器运行最新的渲染模式 -->
	</head>
	<body>
		<%if(has_coloring_order_save && !order.isDelivered()){ %>
								<div class="emptyrecordwidget">
									<p>
										如果您要创建染色单，请点击下方的按钮
									</p>
									<a href="coloring_order/add/<%=order.getId()%>"
										class="btn btn-primary" id="createProducingorderBtn">创建染色单</a>
								</div>
								<%} %>
								<%if(has_coloring_order_print){ %>
								<a  href="printorder/print?orderId=<%=order.getId() %>&gridName=coloringorder" target="_blank" type="button"
												class="printAllBtn btn btn-success"
												data-loading-text="正在打印..."> 打印染色单 </a>
								<%} %>
								<%
									for (ColoringOrder coloringOrder : coloringOrderList) {
										List<ColoringOrderDetail> coloringOrderDetailList = coloringOrder == null ? new ArrayList<ColoringOrderDetail>()
												: coloringOrder.getDetaillist();
								%>
								
								<div class="container-fluid coloringorderWidget">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="id"
												value="<%=coloringOrder == null ? "" : coloringOrder
						.getId()%>" />
											<input type="hidden" name="orderId"
												value="<%=order.getId()%>" />
											<%if(coloringOrder.isEdit()){ %>
												<%if(has_coloring_order_delete){ %>
													<a target="_blank" type="button"
													class="pull-right btn btn-default deleteTableBtn"
													data-loading-text="正在删除..."> 删除 </a>
												<%} %>
												<a href="coloring_order/put/<%=coloringOrder.getId() %>" target="_blank" type="button"
													class="pull-right btn btn-default"
													data-loading-text="正在跳转页面......"> 编辑 </a>
											<%} %>
											<%if(has_coloring_order_print){ %>
											<a  href="coloring_order/print/<%=coloringOrder.getId() %>" target="_blank" type="button"
												class="pull-right btn btn-success"
												data-loading-text="正在打印..."> 打印 </a>
											<%} %>
											<div class="clear"></div>
											<div class="col-md-12 tablewidget">
												<table class="table noborder">
							<caption id="tablename">
								桐庐富伟针织厂染色单
							</caption>

						</table>

						<table class="tableTb noborder table">
							<tbody>
								<tr>
									<td>
										供货单位：
										<span><%=coloringOrder == null ? ""
						: (SystemCache.getFactoryName(coloringOrder
								.getFactoryId()))%></span>

									</td>
									<td>
										业务员：
										<span><%=coloringOrder == null ? ""
						: (SystemCache.getEmployeeName((coloringOrder
								.getCharge_employee())))%></span>
									</td>
									<td class="pull-right">

										№：<%=coloringOrder.getNumber()%>

									</td>

								</tr>


								<tr>
									<td colspan="3">
										<table class="table table-responsive table-bordered">
											<tr>
												<td class="center" width="15%">
													公司
												</td>
												<td class="center" width="15%">
													订单号
												</td>
												<td class="center" width="15%">
													客户
												</td>
												<td class="center" width="40%" colspan="2">
													品名
												</td>
											</tr>
											<tr>
												<td class="center">
													<span><%=SystemCache.getCompanyShortName(coloringOrder
								.getCompanyId())%></span>
												</td>
												<td class="center">
													<span><%=coloringOrder.getOrderNumber()%></span>
												</td>
												<td class="center">
													<span><%=SystemCache.getCustomerName(coloringOrder
								.getCustomerId())%></span>
												</td>
												<td class="center" width="30%">
													<span><%=coloringOrder.getName()%></span>
												</td>
												<td class="center" width="10%">备注</td>
												
											</tr>
									
											<tr class="tr">
												<td width="15%">
													色号
												</td>
												<td width="15%">
													材料
												</td>
												<td width="15%">
													数量(kg)
												</td>
												<td width="15%">
													标准样纱
												</td>
												<td class="center" rowspan="7">
													<span><%=coloringOrder.getMemo() == null ?"":coloringOrder.getMemo()%></span>
												</td>
											</tr>
									
											<%
												for (ColoringOrderDetail detail : coloringOrderDetailList) {
											%>
											<tr class="tr">
												<td class="color"><%=detail.getColor()%>
												</td>
												<td class="material_name"><%=SystemCache.getMaterialName(detail
											.getMaterial())%>
												</td>
												<td class="quantity"><%=detail.getQuantity()%>
												</td>
												<td class="standardyarn"><%=detail.getStandardyarn()%>
											</tr>

											<%
												}
													int i = coloringOrderDetailList.size();
													for (; i < 6; ++i) {
											%>
											<tr class="tr">
												<td class="color">
													&nbsp;
												</td>
												<td class="material_name">
												</td>
												<td class="quantity">
												</td>
												<td class="standardyarn">
											</tr>
											<%
												}
											%>

										</table>
									</td>
								</tr>

							</tbody>
						</table>
						<p class="pull-right auto_bottom">
							<span id="created_user">制单人：<%=SystemCache.getUserName(coloringOrder
								.getCreated_user())%></span>
							<span id="receiver_user">收货人：</span>
							<span id="date"> 日期：<%=DateTool.formatDateYMD(coloringOrder
										.getCreated_at())%></span>
						</p>

											</div>
										</form>
									</div>
								</div>
								<%
									}
								%>
	</body>
</html>