<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.producesystem.StoreInOut"%>
<%@page import="com.fuwei.entity.producesystem.StoreInOutDetail"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	//原材料出库单
	StoreInOut storeInOut = (StoreInOut) request.getAttribute("storeInOut");
	List<StoreInOutDetail> detaillist = storeInOut == null ? new ArrayList<StoreInOutDetail>() :storeInOut.getDetaillist();
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>原材料出库单 -- 桐庐富伟针织厂</title>
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
							<caption id="tablename" style="margin-bottom:0;">
								桐庐富伟针织厂原材料出库单<div table_id="<%=storeInOut.getNumber() %>" class="id_barcode"></div>
								<div style="text-align: center;font-size: 16px;">(样纱)</div>
							</caption>
						</table>

						<table id="orderTb" class="tableTb noborder">
							<tbody>
								<tr>
									<td>
										业务员/领取人：
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
													染色单号
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
													<span><%=storeInOut.getColoring_order_number()%></span>
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
														色号
													</td>
													<td width="15%">
														材料
													</td>
													<td width="15%">
														出库数量(kg)
													</td>
													<td width="15%">
														缸号
													</td>

												</tr>
											</thead>
											<tbody>
												<%
													for (StoreInOutDetail detail : detaillist) {
												%>
												<tr class="tr">
													<td class="color"><%=detail.getColor()%>
													<td class="material_name"><%=SystemCache.getMaterialName(detail
											.getMaterial())%>
													</td>
													<td class="quantity"><%=(int)detail.getQuantity()%>
													</td>
													<td class="lot_no"><%=detail.getLot_no()%>
													</td>
												</tr>

												<%
													}
														int i = detaillist.size();
														for (; i < 6; ++i) {
												%>
												<tr class="tr">
													<td class="color">&nbsp;</td>
													<td class="material_name">
													</td>
													<td class="quantity">
													</td>
													<td class="lot_no">
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
												说明：1.此单说明了本次出入库的相关内容，请充分阅读并理解，如有疑问及时联系我方
											</div>
											<div class="tip_line">
												2.材料品质及颜色要确保准确，颜色色牢度须达到4级以上。
											</div>
											<div class="tip_line">
												3.不得含有偶氮、PCP、甲醛、APEO。不得有特殊气味，无致敏致癌物质。
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