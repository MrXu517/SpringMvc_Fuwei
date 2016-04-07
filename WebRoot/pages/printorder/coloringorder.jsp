<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.ordergrid.ColoringOrder"%>
<%@page import="com.fuwei.entity.ordergrid.ColoringOrderDetail"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%
	//染色单
	List<ColoringOrder> coloringOrderList = (List<ColoringOrder>) request
			.getAttribute("coloringOrderList");
	coloringOrderList = coloringOrderList == null ? new ArrayList<ColoringOrder>()
			: coloringOrderList;
	String productfactoryStr = (String) request
			.getAttribute("productfactoryStr");
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
		<script src="js/plugins/jquery-barcode.min.js"></script>
	</head>
	<body>
		<%
			for (ColoringOrder coloringOrder : coloringOrderList) {
				List<ColoringOrderDetail> coloringOrderDetailList = coloringOrder == null ? new ArrayList<ColoringOrderDetail>()
						: coloringOrder.getDetaillist();
		%>
		<div style="page-break-after: always">
			<div class="container-fluid gridTb_2 auto_container">
				<div class="row">
					<div class="col-md-12 tablewidget">
						<table class="table noborder">
							<caption id="tablename">
								桐庐富伟针织厂染色单<div table_id="<%=coloringOrder.getNumber() %>" class="id_barcode"></div>
							</caption>

						</table>

						<table id="orderTb" class="tableTb noborder">
							<tbody>
								<tr>
									<td>
										供货单位：
										<span><%=coloringOrder == null ? "" : (SystemCache
						.getFactoryName(coloringOrder.getFactoryId()))%></span>

									</td>
									<td>
										业务员：
										<span><%=coloringOrder == null ? "" : (SystemCache
						.getEmployeeName((coloringOrder.getCharge_employee())))%></span>
									</td>
									<td class="pull-right">

										№：<%=coloringOrder.getNumber()%>

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
												<%if(coloringOrder.getOrderId()==null){ %>
												<td class="center" width="15%">
													货号
												</td>
												<%}else{ %>
												<td class="center" width="15%">
													订单号
												</td>
												<%} %>
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
												<%if(coloringOrder.getOrderId()==null){ %>
												<td class="center">
													<span><%=coloringOrder.getCompany_productNumber()%></span>
												</td>
												<%}else{ %>
												<td class="center">
													<span><%=coloringOrder.getOrderNumber()%></span>
												</td>
												<%} %>
												
												<td class="center">
													<span><%=SystemCache.getCustomerName(coloringOrder
								.getCustomerId())%></span>
												</td>
												<td class="center" width="30%">
													<span><%=coloringOrder.getName()%></span>
												</td>
												<td class="center" width="10%">
													备注
												</td>

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
													<span><%=coloringOrder.getMemo() == null ? ""
						: coloringOrder.getMemo()%></span>
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
												<td class="quantity"><%=(int)detail.getQuantity()%>
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
									<td></td>
								</tr>
								<tr>
									<td style="padding-top: 0;" colspan="3">
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
							<span id="created_user">制单人：<%=SystemCache.getUserName(coloringOrder
								.getCreated_user())%></span>
							<span id="receiver_user">收货人：</span>
							<span id="date"> 日期：<%=DateTool.formatDateYMD(DateTool.getYanDate(coloringOrder
										.getCreated_at()))%></span>
						</p>



					</div>

				</div>
			</div>
		</div>
		<%
			}
		%>
	<script type="text/javascript">
		$(".id_barcode").each(function(){
			var id =$(this).attr("table_id");
			$(this).barcode(id, "code128",{barWidth:2, barHeight:30,showHRI:false});
		});		
	</script>
	</body>
</html>