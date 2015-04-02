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
	materialPurchaseOrderList = materialPurchaseOrderList == null? new ArrayList<MaterialPurchaseOrder>()
			: materialPurchaseOrderList;
	String productfactoryStr = (String)request.getAttribute("productfactoryStr");
	
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
		<%for(MaterialPurchaseOrder materialPurchaseOrder : materialPurchaseOrderList){ 
			List<MaterialPurchaseOrderDetail> materialPurchaseOrderDetailList = materialPurchaseOrder == null ? new ArrayList<MaterialPurchaseOrderDetail>()
			: materialPurchaseOrder.getDetaillist();
		%>
		<div style="page-break-after: always">
		<div class="container-fluid gridTab auto_container">
			<div class="row">
				<div class="col-md-12 tablewidget">
					<table class="table noborder">
						<caption id="tablename">
							桐庐富伟针织厂原材料采购单
						</caption>
						<tr><td colspan="3" class="pull-right">№：<%=materialPurchaseOrder.getOrderNumber() %></td></tr>
					</table>

					<table id="orderTb" class="tableTb noborder">
						<tbody>
							<tr>
								<td width="15%">
									供货单位：
								</td>
								<td class="orderproperty">
									<span><%=materialPurchaseOrder == null ? "" : (SystemCache.getFactoryName(materialPurchaseOrder.getFactoryId()))%></span>
								</td>
								<td width="15%">业务员：</td>

								<td width="15%">
									<span><%=materialPurchaseOrder == null ? "" : (SystemCache.getFactoryName((materialPurchaseOrder.getCharge_user())))%></span>
								</td>
								<td>
								</td>

							</tr>
							
							<tr>
								<td colspan="5">
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
												<span><%=SystemCache.getCompanyShortName(materialPurchaseOrder.getCompanyId())%></span>
											</td>
											<td class="center">
												<span><%=materialPurchaseOrder.getProductNumber()%></span>
											</td>
											<td class="center">
												<span><%=SystemCache.getCustomerName(materialPurchaseOrder.getCustomerId())%></span>
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

					<table id="mainTb" class="noborder">
						<tr>
							<td>
								<table class="detailTb">
									
									<thead>
										<tr>
											<th width="15%">
												材料品种
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
											for (MaterialPurchaseOrderDetail detail : materialPurchaseOrderDetailList) {
										%>
										<tr class="tr">
											<td class="material_name"><%=SystemCache.getMaterialName(detail.getMaterial()) %>
											</td>
											<td class="quantity"><%=detail.getQuantity()%>
											</td>
											<td class="memo">
											</td>
										</tr>

										<%
											}
											int i = materialPurchaseOrderList.size();
											for(; i < 6 ; ++i){
										%>
												<tr class="tr">
													<td class="material_name">&nbsp;
													</td>
													<td class="quantity">
													</td>
													<td class="memo">
													</td>
												</tr>
										<%}
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
						</tr>
					</table>

					<p class="auto_bottom">
						质量要求：
					</p>
					<p id="tip" class="auto_bottom font_large">
						纱线(成品)不含机油味，颜色准确，手感柔软，粗细均匀。染色色牢度必须达到欧洲市场要求，染色不含偶氮，不含镍，不可缺斤短两，纱线应烘干，不可有污迹。如有以上问题造成客户索赔，一切后果有贵司承担。
					</p>
					<p id="memo" class="auto_bottom font_small">
						备注：本单须妥善保管。结账以此单信息为准。如有异议须在3日内提出，否则默认为确认。
					</p>
					<p class="pull-right auto_bottom">
						<span id="created_user">制单人：<%=SystemCache.getUserName(materialPurchaseOrder.getCreated_user()) %></span>
						<span id="receiver_user">收货人：</span>
						<span id="date"> 日期：<%=DateTool.formatDateYMD(materialPurchaseOrder.getCreated_at()) %></span>
					</p>



				</div>

			</div>
		</div>
		</div>
	<%} %>
	</body>
</html>