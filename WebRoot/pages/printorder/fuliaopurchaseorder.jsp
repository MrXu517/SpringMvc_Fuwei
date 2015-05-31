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
	String productfactoryStr = (String) request
			.getAttribute("productfactoryStr");
%>
<!DOCTYPE html>
<html>
	<head>

		<title>辅料采购单 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8">
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<link href="css/printorder/print.css" rel="stylesheet" type="text/css" />
		<!-- 为了让IE浏览器运行最新的渲染模式 -->
	</head>
	<body class="">
		<%
			for (FuliaoPurchaseOrder fuliaoPurchaseOrder : fuliaoPurchaseOrderList) {
				List<FuliaoPurchaseOrderDetail> fuliaoPurchaseOrderDetailList = fuliaoPurchaseOrder == null ? new ArrayList<FuliaoPurchaseOrderDetail>()
						: fuliaoPurchaseOrder.getDetaillist();
		%>
		<div style="page-break-after: always">
			<div class="container-fluid gridTb_2 auto_container">
				<div class="row">
					<div class="col-md-12 tablewidget">
						<table class="table noborder">
							<caption id="tablename">
								桐庐富伟针织厂辅料采购单
							</caption>
						</table>

						<table id="orderTb" class="tableTb noborder">
							<tbody>
								<tr>
									<td>

										供货单位：
										<span><%=fuliaoPurchaseOrder == null ? "" : (SystemCache
						.getFactoryName(fuliaoPurchaseOrder.getFactoryId()))%></span>

									</td>
									<td>
										业务员：
										<span><%=fuliaoPurchaseOrder == null ? "" : (SystemCache
						.getEmployeeName((fuliaoPurchaseOrder.getCharge_employee())))%></span>
									</td>
									<td class="pull-right">

										№：<%=fuliaoPurchaseOrder.getNumber()%>

									</td>
									<td></td>

								</tr>


								<tr>
									<td colspan="3">
										<table>
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
									<td></td>
								</tr>
								<tr>
									<td colspan="3">
										<table class="detailTb">

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
													<td class="style_name"><%=SystemCache.getMaterialName(detail.getStyle())%>
													</td>
													<td class="quantity"><%=(int)detail.getQuantity()%>
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
									<td></td>
								</tr>
								<tr>
									<td colspan="3">
										<div id="tip" class="auto_bottom">
											<div>
												说明：1.此单说明了本次采购的相关内容，请充分阅读并理解，如有疑问及时联系我方
											</div>
											<div class="tip_line">
												2.材料品质及颜色要确保准确，颜色色牢度须达到4级以上。
											</div>
											<div class="tip_line">
												3.不得含有偶氮、PCP、甲醛、APEO。不得有特殊气味，无致敏致癌物质。
											</div>
											<div class="tip_line">
												4.贵单位须妥善保管此单据，结账时须提供此单据
											</div>

										</div>
									</td>
									<td></td>
								</tr>
							</tbody>
						</table>


						<p class="pull-right auto_bottom">
							<span id="created_user">制单人：<%=SystemCache.getUserName(fuliaoPurchaseOrder
								.getCreated_user())%></span>
							<span id="receiver_user">收货人：</span>
							<span id="date"> 日期：<%=DateTool.formatDateYMD(DateTool.getYanDate(fuliaoPurchaseOrder
								.getCreated_at()))%></span>
						</p>



					</div>

				</div>
			</div>
		</div>
		<%
			}
		%>
	</body>
</html>