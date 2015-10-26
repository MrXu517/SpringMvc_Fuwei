<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.producesystem.HalfStoreInOut"%>
<%@page import="com.fuwei.entity.producesystem.HalfStoreInOutDetail"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/";
	//半成品出库单
	HalfStoreInOut storeInOut = (HalfStoreInOut) request.getAttribute("halfStoreInOut");
	List<HalfStoreInOutDetail> detaillist = storeInOut == null ? new ArrayList<HalfStoreInOutDetail>() :storeInOut.getDetaillist();
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>半成品出库单 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8">
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<!-- 为了让IE浏览器运行最新的渲染模式 -->
		<script src="js/plugins/jquery-1.10.2.min.js"></script>
		<link href="css/printorder/print.css" rel="stylesheet" type="text/css" />
		<script src="js/plugins/jquery-barcode.min.js"></script>
	</head>
	<body>

		<div style="page-break-after: always">
			<div class="container-fluid gridTb_2 auto_container">
				<div class="row">
					<div class="col-md-12 tablewidget">
						<table class="table noborder">
							<caption id="tablename">
								桐庐富伟针织厂半成品出库单<div table_id="<%=storeInOut.getNumber()%>" class="id_barcode"></div>
							</caption>
						</table>

						<table id="orderTb" class="tableTb noborder">
							<tbody>
								<tr>
									<td>

										领取单位：
										<span><%=storeInOut == null ? ""
						: (SystemCache.getFactoryName(storeInOut
								.getFactoryId()))%></span>

									</td>
									<td>
										业务员：
										<span><%=storeInOut == null ? ""
						: (SystemCache.getEmployeeName((storeInOut
								.getCharge_employee())))%></span>
									</td>
									<td>
										出库时间：
										<span><%=storeInOut == null ? ""
						: (DateTool.formatDateYMD(DateTool.getYanDate(storeInOut.getDate())))%></span>
									</td>
									<td class="pull-right">

										№：<%=storeInOut.getNumber()%>

									</td>
									<td></td>
								</tr>

								<tr>
									<td colspan="4">
										<table>
											<tr><td class="center" width="10%">
													订单号
												</td>
												<td class="center" width="15%">
													公司
												</td>
												<td class="center" width="15%">
													公司货号
												</td>
												<td class="center" width="15%">
													客户
												</td>
												<td class="center" width="15%">
													品名
												</td>
											</tr>
											<tr><td class="center">
													<span><%=storeInOut.getOrderNumber()%></span>
												</td>
												<td class="center">
													<span><%=SystemCache.getCompanyShortName(storeInOut
								.getCompanyId())%></span>
												</td>
												<td class="center">
													<span><%=storeInOut.getCompany_productNumber()%></span>
												</td>
												<td class="center">
													<span><%=SystemCache.getCustomerName(storeInOut
								.getCustomerId())%></span>
												</td>
												<td class="center">
													<span><%=storeInOut.getName()%></span>
												</td>
											</tr>
											<tr><td class="center" width="10%">
													备注
												</td><td colspan="4">
													<span><%=storeInOut.getMemo() == null ? "" : storeInOut.getMemo()%></span>
												</td></tr>
										</table>
									</td>
									<td></td>
								</tr>
								<tr>
									<td colspan="4">
										<table class="detailTb">

											<thead>
												<tr>
													<td width="15%">
														颜色
													</td>
													<td width="15%">
														尺寸
													</td>
													<td width="15%">
														出库数量
													</td>

												</tr>
											</thead>
											<tbody>
												<%
													for (HalfStoreInOutDetail detail : detaillist) {
												%>
												<tr class="tr">
													<td class="color"><%=detail.getColor()%>
													<td class="size"><%=detail.getSize()%>
													</td>
													<td class="quantity"><%=(int)detail.getQuantity()%>
													</td>
												</tr>

												<%
													}
														int i = detaillist.size();
														for (; i < 6; ++i) {
												%>
												<tr class="tr">
													<td class="color">&nbsp;</td>
													<td class="size">
													</td>
													<td class="quantity">
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
									<td colspan="4">
										<div id="tip" class="auto_bottom">
											<div>
												说明：1.出库前请确认产品及数量是否正确，一旦出库若入库时存在数量不足，一切后果自负
											</div>
											<div class="tip_line">
												2.请在规定时间内严格按照计划生产单进行生产
											</div>
											<div class="tip_line">
												3.保证产品符合要求品质
											</div>
											<div class="tip_line">
												4.<span style="font-weight: bold;text-decoration: underline;font-size: 18px;">产品入库后须到我厂收发处开具入库单，否则无法完成结账</span>
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
							<span id="created_user">制单人：<%=SystemCache.getUserName(storeInOut
								.getCreated_user())%></span>
							<span id="receiver_user">收货人：</span>
							<span id="date"> 打印日期：<%=DateTool.formatDateYMD(DateTool.getYanDate(DateTool.now()))%></span>
						</p>



					</div>

				
</div></div>
		
	<script type="text/javascript">
		$(".id_barcode").each(function(){
			var id =$(this).attr("table_id");
			$(this).barcode(id, "code128",{barWidth:2, barHeight:30,showHRI:false});
		});		
		window.print();
	</script>
	</body>
</html>