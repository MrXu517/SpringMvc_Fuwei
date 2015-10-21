<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.ordergrid.MaterialPurchaseOrder"%>
<%@page import="com.fuwei.entity.ordergrid.MaterialPurchaseOrderDetail"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.entity.Order"%>
<%
	//原材料采购单
	Order order = (Order) request.getAttribute("order");
	List<MaterialPurchaseOrder> materialPurchaseOrderList = (List<MaterialPurchaseOrder>) request
			.getAttribute("materialPurchaseOrderList");
	materialPurchaseOrderList = materialPurchaseOrderList == null ? new ArrayList<MaterialPurchaseOrder>()
			: materialPurchaseOrderList;
	String productfactoryStr = (String) request
			.getAttribute("productfactoryStr");
	Boolean has_material_purchase_order_save = SystemCache.hasAuthority(session,
			"order/materialpurchase");
	Boolean has_material_purchase_order_delete = SystemCache.hasAuthority(session,
			"material_purchase_order/delete");
%>
<!DOCTYPE html>
<html>
	<head>
		<title>原材料采购单 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8">
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<!-- 为了让IE浏览器运行最新的渲染模式 -->
	</head>
	<body>
		<%if(has_material_purchase_order_save && !order.isDelivered()){ %>
								<div class="emptyrecordwidget">
									<p>
										如果您要创建原材料采购单，请点击下方的按钮
									</p>
									<a href="material_purchase_order/add/<%=order.getId()%>"
										class="btn btn-primary" id="createProducingorderBtn">创建原材料采购单</a>
								</div>
								<%} %>
								<a  href="printorder/print?orderId=<%=order.getId() %>&gridName=materialpurchaseorder" target="_blank" type="button"
												class="printBtn btn btn-success"
												data-loading-text="正在打印..."> 打印原材料采购单 </a>
								<%
									for (MaterialPurchaseOrder materialPurchaseOrder : materialPurchaseOrderList) {
										List<MaterialPurchaseOrderDetail> materialPurchaseOrderDetailList = materialPurchaseOrder == null ? new ArrayList<MaterialPurchaseOrderDetail>()
												: materialPurchaseOrder.getDetaillist();
								%>

								<div class="container-fluid materialorderWidget">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="id"
												value="<%=materialPurchaseOrder == null ? ""
						: materialPurchaseOrder.getId()%>" />
											<input type="hidden" name="orderId"
												value="<%=order.getId()%>" />
											<%if(materialPurchaseOrder.isEdit()){ %>
												<%if(has_material_purchase_order_delete){ %>
													<a target="_blank" type="button"
													class="pull-right btn btn-default deleteTableBtn"
													data-loading-text="正在删除..."> 删除 </a>
												<%} %>
												<a href="material_purchase_order/put/<%=materialPurchaseOrder.getId() %>" target="_blank" type="button"
													class="pull-right btn btn-default"
													data-loading-text="正在跳转页面......"> 编辑 </a>
											<%} %>
											
											
											<a href="material_purchase_order/print/<%=materialPurchaseOrder.getId() %>" target="_blank" type="button"
												class="pull-right btn btn-success"
												data-loading-text="正在打印..."> 打印 </a>
											
											<div class="clear"></div>
											<div class="col-md-12 tablewidget">
												<table class="table noborder">
							<caption id="tablename">
								桐庐富伟针织厂原材料采购单
							</caption>
						</table>

						<table class="table tableTb noborder">
							<tbody>
								<tr>
									<td>

										供货单位：
										<span><%=materialPurchaseOrder == null ? ""
						: (SystemCache.getFactoryName(materialPurchaseOrder
								.getFactoryId()))%></span>

									</td>
									<td>
										业务员：
										<span><%=materialPurchaseOrder == null ? ""
						: (SystemCache.getEmployeeName((materialPurchaseOrder
								.getCharge_employee())))%></span>
									</td>
									<td class="pull-right">

										№：<%=materialPurchaseOrder.getNumber()%>

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
													<span><%=SystemCache.getCompanyShortName(materialPurchaseOrder
								.getCompanyId())%></span>
												</td>
												<td class="center">
													<span><%=materialPurchaseOrder.getCompany_productNumber()%></span>
												</td>
												<td class="center">
													<span><%=SystemCache.getCustomerName(materialPurchaseOrder
								.getCustomerId())%></span>
												</td>
												<td class="center">
													<span><%=materialPurchaseOrder.getName()%></span>
												</td>
											</tr>
										</table>
									</td>
								</tr>

							</tbody>
						</table>

						<table class="noborder table">
							<tr>
								<td>
									<table class="table table-responsive table-bordered">

										<thead>
											<tr>
												<th width="15%">
													材料品种
												</th>
												<th width="15%">
													数量(kg)
												</th>
												<th width="15%">染厂</th>
												<th width="30%">
													备注
												</th>

											</tr>
										</thead>
										<tbody>
											<%
												for (MaterialPurchaseOrderDetail detail : materialPurchaseOrderDetailList) {
											%>
											<tr class="tr">
												<td class="material_name"><%=SystemCache.getMaterialName(detail
											.getMaterial())%>
												</td>
												<td class="quantity"><%=detail.getQuantity()%>
												</td>
												<td class="factory_name"><%=SystemCache.getFactoryName(detail.getFactoryId())%>
																		</td>
												<td class="memo"><%=detail.getMemo() == null ? "" : detail
							.getMemo()%>
												</td>
											</tr>

											<%
												}
													int i = materialPurchaseOrderDetailList.size();
													for (; i < 6; ++i) {
											%>
											<tr class="tr">
												<td class="material_name">
													&nbsp;
												</td>
												<td class="quantity">
												</td>
												<td class="factory_name">
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
							<span id="created_user">制单人：<%=SystemCache.getUserName(materialPurchaseOrder
								.getCreated_user())%></span>
							<span id="receiver_user">收货人：</span>
							<span id="date"> 日期：<%=DateTool.formatDateYMD(materialPurchaseOrder
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