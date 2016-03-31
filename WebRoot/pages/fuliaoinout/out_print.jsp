<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.producesystem.Fuliao"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.entity.producesystem.FuliaoOut"%>
<%@page import="com.fuwei.entity.producesystem.FuliaoOutDetail"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Order order = (Order) request
			.getAttribute("order");
	FuliaoOut fuliaoOut = (FuliaoOut)request.getAttribute("fuliaoOut");
	List<FuliaoOutDetail> detaillist = fuliaoOut.getDetaillist();
	if (detaillist == null) {
		detaillist = new ArrayList<FuliaoOutDetail>();
	}
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>打印辅料出库单 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8">
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<link href="css/printorder/print.css" rel="stylesheet" type="text/css" />
		<script src="js/plugins/jquery-1.10.2.min.js"></script>
		<script src="js/common/common.js" type="text/javascript"></script>
		<script src="js/plugins/jquery-barcode.min.js"></script>
		<!-- 为了让IE浏览器运行最新的渲染模式 -->
	</head>
	<body>
		<div style="page-break-after: always">
			<div class="container-fluid gridTb_2 auto_container">
				<div class="row">
					<div class="col-md-12 tablewidget">
						<table class="table noborder">
							<caption id="tablename">
								桐庐富伟针织厂辅料出库单<div table_id="<%=fuliaoOut.getNumber() %>" class="id_barcode"></div>
							</caption>
						</table>

						<table id="orderTb" class="tableTb noborder">
							<tbody>
								<tr><td >
										领取人：<span><%=fuliaoOut == null ? "": SystemCache.getEmployeeName(fuliaoOut.getReceiver_employee())%></span>
									</td>
									<td>
										业务员：
										<span><%=fuliaoOut == null ? ""
						: (SystemCache.getEmployeeName((fuliaoOut.getCharge_employee())))%></span>
									</td>
									<td class="pull-right">

										№：<%=fuliaoOut.getNumber()%>

									</td>
									<td></td>
								</tr>

								<tr>
									<td colspan="3">
										<table>
											<tr>
												<td class="center" width="15%">
													订单号
												</td>
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
													<span><%=fuliaoOut.getOrderNumber()%></span>
												</td>
												<td class="center">
													<span><%=SystemCache.getCompanyShortName(order
								.getCompanyId())%></span>
												</td>
												<td class="center">
													<span><%=fuliaoOut.getCompany_productNumber()%></span>
												</td>
												<td class="center">
													<span><%=SystemCache.getCustomerName(order
								.getCustomerId())%></span>
												</td>
												<td class="center">
													<span><%=fuliaoOut.getName()%></span>
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
													<th width="8%">
														编号
													</th>
													<th width="8%">
														类型
													</th><th width="10%">
														订单号
													</th><th width="8%">
														款号
													</th><th width="8%">
														国家
													</th><th width="8%">
														颜色
													</th><th width="8%">
														尺码
													</th><th width="6%">
														批次
													</th>
													<th width="15%">
														数量(个)
													</th><th width="10%">
														库位
													</th><th width="15%">
														备注
													</th>

												</tr>
											</thead>
											<tbody>
											<%
													for (FuliaoOutDetail detail : detaillist) {
												%>
												<tr class="tr">
													<td><%=detail.getFnumber()%></td>
													<td><%=SystemCache.getFuliaoTypeName((Integer)detail.getFuliaoTypeId())%></td>
													<td><%=detail.getCompany_orderNumber()%></td>
													<td><%=detail.getCompany_productNumber()%></td>
													<td><%=detail.getCountry()%></td>
													<td><%=detail.getColor()%></td>
													<td><%=detail.getSize()%></td>
													<td><%=detail.getBatch()%></td>
													<td><%=detail.getQuantity()%></td>
													<td><%=SystemCache.getLocationNumber(detail.getLocationId())%></td>
													<td><%=detail.getMemo()==null?"":detail.getMemo()%></td>
												</tr>
												<%
													}
														int i = detaillist.size();
														for (; i < 6; ++i) {
												%>
												<tr class="tr">
													<td class="">
														&nbsp;
													</td>
													<td class="">
													</td>
													<td class="">
													</td>
													<td class="">
													</td>
													<td class="">
													</td>
													<td class="">
													</td>
													<td class="">
													</td>
													<td class="">
													</td>
													<td class="">
													</td>
													<td class="">
													</td>
													<td class="">
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
												说明：1.此单说明了本次出库的相关内容，请充分阅读并理解，如有疑问及时联系我方
											</div>
										</div>
									</td>
									<td></td>
								</tr>
							</tbody>
						</table>
						<p class="pull-right auto_bottom">
							<span id="created_user">制单人：<%=SystemCache.getUserName(fuliaoOut
								.getCreated_user())%></span>
							<span id="receiver_user">收货人：</span>
							<span id="date"> 日期：<%=DateTool.formatDateYMD(DateTool.getYanDate(fuliaoOut.getCreated_at()))%></span>
						</p>



					</div>

				</div>
			</div>
		</div>
	<script type="text/javascript">
		$(".id_barcode").each(function(){
			var id =$(this).attr("table_id");
			$(this).barcode(id, "code128",{barWidth:2, barHeight:30,showHRI:true});
		});		
		window.print();
	</script>
	</body>
</html>