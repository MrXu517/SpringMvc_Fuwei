<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.producesystem.Fuliao"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.entity.producesystem.FuliaoIn"%>
<%@page import="com.fuwei.entity.producesystem.FuliaoInDetail"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Order order = (Order) request
			.getAttribute("order");
	FuliaoIn fuliaoIn = (FuliaoIn)request.getAttribute("fuliaoIn");
	List<FuliaoInDetail> detaillist = fuliaoIn.getDetaillist();
	if (detaillist == null) {
		detaillist = new ArrayList<FuliaoInDetail>();
	}
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>打印辅料入库单 -- 桐庐富伟针织厂</title>
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
								桐庐富伟针织厂辅料入库单<div table_id="<%=fuliaoIn.getNumber() %>" class="id_barcode"></div>
							</caption>
						</table>

						<table id="orderTb" class="tableTb noborder">
							<tbody>
								<tr>
									<td>
										订单号：
										<span><%=fuliaoIn == null ? "": fuliaoIn.getOrderNumber()%></span>
									</td>
									<td>
										业务员：
										<span><%=fuliaoIn == null ? ""
						: (SystemCache.getEmployeeName((fuliaoIn.getCharge_employee())))%></span>
									</td>
									<td class="pull-right">

										№：<%=fuliaoIn.getNumber()%>

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
													<span><%=SystemCache.getCompanyShortName(order
								.getCompanyId())%></span>
												</td>
												<td class="center">
													<span><%=fuliaoIn.getCompany_productNumber()%></span>
												</td>
												<td class="center">
													<span><%=SystemCache.getCustomerName(order
								.getCustomerId())%></span>
												</td>
												<td class="center">
													<span><%=fuliaoIn.getName()%></span>
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
													<th width="5%">
														编号
													</th>
													<th width="5%">
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
													</th><th width="8%">
														来源
													</th>
													<th width="10%">
														入库数量(个)
													</th><th width="10%">
														库位
													</th>

												</tr>
											</thead>
											<tbody>
											<%
													for (FuliaoInDetail detail : detaillist) {
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
													<td><%=SystemCache.getFactoryName((Integer)detail.getFuliaoPurchaseFactoryId())%></td>
													<td><%=detail.getQuantity()%></td>
													<td><%=SystemCache.getLocationNumber(detail.getLocationId())%></td>
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
												说明：1.此单说明了本次入库的相关内容，请充分阅读并理解，如有疑问及时联系我方
											</div>
										</div>
									</td>
									<td></td>
								</tr>
							</tbody>
						</table>
						<p class="pull-right auto_bottom">
							<span id="created_user">制单人：<%=SystemCache.getUserName(fuliaoIn
								.getCreated_user())%></span>
							<span id="receiver_user">送货人：</span>
							<span id="date"> 日期：<%=DateTool.formatDateYMD(DateTool.getYanDate(fuliaoIn.getCreated_at()))%></span>
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