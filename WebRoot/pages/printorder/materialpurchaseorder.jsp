<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.ordergrid.MaterialPurchaseOrder"%>
<%@page import="com.fuwei.entity.ordergrid.MaterialPurchaseOrderDetail"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%
	//原材料采购单
	List<MaterialPurchaseOrder> materialPurchaseOrderList = (List<MaterialPurchaseOrder>) request
			.getAttribute("materialPurchaseOrderList");
	materialPurchaseOrderList = materialPurchaseOrderList == null ? new ArrayList<MaterialPurchaseOrder>()
			: materialPurchaseOrderList;
	String productfactoryStr = (String) request
			.getAttribute("productfactoryStr");
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
		<%
			for (MaterialPurchaseOrder materialPurchaseOrder : materialPurchaseOrderList) {
				List<MaterialPurchaseOrderDetail> materialPurchaseOrderDetailList = materialPurchaseOrder == null ? new ArrayList<MaterialPurchaseOrderDetail>()
						: materialPurchaseOrder.getDetaillist();
		%>
		<div style="page-break-after: always">
			<div class="container-fluid gridTb_2 auto_container">
				<div class="row">
					<div class="col-md-12 tablewidget">
						<table class="table noborder">
							<caption id="tablename">
								桐庐富伟针织厂原材料采购单
							</caption>
						</table>

						<table id="orderTb" class="tableTb noborder">
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
						: (SystemCache.getUserName((materialPurchaseOrder
								.getCharge_user())))%></span>
									</td>
									<td class="pull-right">

										№：<%=materialPurchaseOrder.getNumber()%>

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
									<td></td>
								</tr>
								<tr>
									<td colspan="3">
										<table class="detailTb">

											<thead>
												<tr>
													<th width="15%" class="pull-left">
														材料品种
													</th>
													<th width="15%" class="pull-left">
														数量(kg)
													</th>
													<th width="15%" class="pull-left">
														染厂
													</th>
													<th width="30%" class="pull-left">
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
													<td class="quantity"><%=(int)detail.getQuantity()%>
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

										<!-- 	<table class="detailTb auto_height stickedTb">
									<tbody>
										<tr>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
										</tr>
									</tbody>
								</table> -->
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

						<table id="mainTb" class="noborder">

						</table>



						<p class="pull-right auto_bottom">
							<span id="created_user">制单人：<%=SystemCache.getUserName(materialPurchaseOrder
								.getCreated_user())%></span>
							<span id="receiver_user">收货人：</span>
							<span id="date"> 日期：<%=DateTool.formatDateYMD(materialPurchaseOrder
								.getCreated_at())%></span>
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