<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.ordergrid.FuliaoPurchaseOrder"%>
<%@page import="com.fuwei.entity.ordergrid.FuliaoPurchaseOrderDetail"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%
	Order order = (Order) request.getAttribute("order");
	//辅料采购单
	List<FuliaoPurchaseOrder> fuliaoPurchaseOrderList = (List<FuliaoPurchaseOrder>) request
			.getAttribute("fuliaoPurchaseOrderList");
	fuliaoPurchaseOrderList = fuliaoPurchaseOrderList == null ? new ArrayList<FuliaoPurchaseOrder>()
			: fuliaoPurchaseOrderList;
	String productfactoryStr = (String) request.getAttribute("productfactoryStr");
	Boolean has_fuliaopurchase_order_save = SystemCache.hasAuthority(session,
			"order/fuliaopurchase");
	Boolean has_fuliao_purchase_order_delete = SystemCache.hasAuthority(session,
			"fuliao_purchase_order/delete");
	Boolean has_fuliao_purchase_order_print = SystemCache.hasAuthority(session,
			"fuliao_purchase_order/print");
%>
<!DOCTYPE html>
<html>
	<head>

		<title>辅料采购单 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8">
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<!-- 为了让IE浏览器运行最新的渲染模式 -->
	</head>
	<body class="">
			<%if( has_fuliaopurchase_order_save && !order.isDelivered()){ %>
								<div class="emptyrecordwidget">
									<p>
										如果您要创建辅料采购单，请点击下方的按钮
									</p>
									
									<a href="fuliao_purchase_order/add/<%=order.getId()%>"
										class="btn btn-primary" id="createProducingorderBtn">创建辅料采购单</a>
								</div>
								<%} %>
								<%if(has_fuliao_purchase_order_print){ %>
								<a  href="printorder/print?orderId=<%=order.getId() %>&gridName=fuliaopurchaseorder" target="_blank" type="button"
												class="printAllBtn btn btn-success"
												data-loading-text="正在打印..."> 打印辅料采购单 </a>
								<%} %>
								<%
									for (FuliaoPurchaseOrder fuliaoPurchaseOrder : fuliaoPurchaseOrderList) {
										List<FuliaoPurchaseOrderDetail> fuliaoPurchaseOrderDetailList = fuliaoPurchaseOrder == null ? new ArrayList<FuliaoPurchaseOrderDetail>()
												: fuliaoPurchaseOrder.getDetaillist();
								%>
								<div class="container-fluid fuliaoorderWidget">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="id"
												value="<%=fuliaoPurchaseOrder == null ? ""
						: fuliaoPurchaseOrder.getId()%>" />
											<input type="hidden" name="orderId"
												value="<%=order.getId()%>" />
											<%if(fuliaoPurchaseOrder.isEdit()){ %>
												<%if(has_fuliao_purchase_order_delete){ %>
													<a target="_blank" type="button"
													class="pull-right btn btn-default deleteTableBtn"
													data-loading-text="正在删除..."> 删除 </a>
												<%} %>
												<a href="fuliao_purchase_order/put/<%=fuliaoPurchaseOrder.getId() %>" target="_blank" type="button"
													class="pull-right btn btn-default"
													data-loading-text="正在跳转页面......"> 编辑 </a>
											<%} %>
											<%if(has_fuliao_purchase_order_print){ %>
											<a href="fuliao_purchase_order/print/<%=fuliaoPurchaseOrder.getId() %>" target="_blank" type="button"
												class="pull-right btn btn-success"
												data-loading-text="正在打印..."> 打印 </a>
											<%} %>
											<div class="clear"></div>
											<div class="col-md-12 tablewidget">
												<table class="table noborder">
							<caption id="tablename">
								桐庐富伟针织厂辅料采购单
							</caption>
						</table>

						<table class="table tableTb noborder">
							<tbody>
								<tr>
									<td>

										供货单位：
										<span><%=fuliaoPurchaseOrder == null ? ""
						: (SystemCache.getFactoryName(fuliaoPurchaseOrder
								.getFactoryId()))%></span>

									</td>
									<td>
										业务员：
										<span><%=fuliaoPurchaseOrder == null ? ""
						: (SystemCache.getEmployeeName((fuliaoPurchaseOrder
								.getCharge_employee())))%></span>
									</td>
									<td class="pull-right">

										№：<%=fuliaoPurchaseOrder.getNumber()%>

									</td>

								</tr>


								<tr>
									<td colspan="5">
										<table class="table table-bordered">
											<tr>
												<td class="center" width="15%">
													公司
												</td>
												<td class="center" width="15%">
													货号
												</td>
												<td class="center" width="15%">
													客户
												</td>
												<td class="center" width="15%">
													品名
												</td>
											</tr>
											<tr>
												<td class="center">
													<span><%=SystemCache.getCompanyShortName(fuliaoPurchaseOrder
								.getCompanyId())%></span>
												</td>
												<td class="center">
													<span><%=fuliaoPurchaseOrder.getCompany_productNumber()%></span>
												</td>
												<td class="center">
													<span><%=SystemCache.getCustomerName(fuliaoPurchaseOrder
								.getCustomerId())%></span>
												</td>
												<td class="center">
													<span><%=fuliaoPurchaseOrder.getName()%></span>
												</td>
											</tr>
										</table>
									</td>
								</tr>

							</tbody>
						</table>

						<table id="mainTb" class="noborder table">
							<tr>
								<td>
									<table class="table table-responsive table-bordered">

										<thead>
											<tr>
												<th width="15%">
													材料
												</th>
												<th width="15%">
													数量(kg)
												</th>
												<th width="30%">
													备注
												</th>
											</tr>
										</thead>
										<tbody>
											<%
												for (FuliaoPurchaseOrderDetail detail : fuliaoPurchaseOrderDetailList) {
											%>
											<tr class="tr">
												<td class="style_name"><%=SystemCache.getFuliaoTypeName(detail.getStyle())%>
												</td>
												<td class="quantity"><%=detail.getQuantity()%>
												</td>
												<td class="memo"><%=detail.getMemo() == null ? "" : detail
							.getMemo()%>
												</td>
											</tr>

											<%
												}
													int i = fuliaoPurchaseOrderDetailList.size();
													for (; i < 6; ++i) {
											%>
											<tr>
												<td class="style_name">
													&nbsp;
												</td>
												<td class="quantity">
												</td>
												<td class="memo">
												</td>
											</tr>
											<%
												}
											%>
										</tbody>
									</table>
								</td>
							</tr>
						</table>

						<p class="pull-right auto_bottom">
							<span id="created_user">制单人：<%=SystemCache.getUserName(fuliaoPurchaseOrder
								.getCreated_user())%></span>
							<span id="receiver_user">收货人：</span>
							<span id="date"> 日期：<%=DateTool.formatDateYMD(fuliaoPurchaseOrder
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