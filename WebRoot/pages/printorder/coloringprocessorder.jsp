<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.ordergrid.ColoringProcessOrder"%>
<%@page import="com.fuwei.entity.ordergrid.ColoringProcessOrderDetail"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.entity.ordergrid.ColoringProcessOrder"%>
<%
	Order order = (Order) request.getAttribute("order");
	//染色进度单
	ColoringProcessOrder coloringProcessOrder = (ColoringProcessOrder) request
			.getAttribute("coloringProcessOrder");
	List<ColoringProcessOrderDetail> coloringProcessOrderDetailList = coloringProcessOrder == null ? new ArrayList<ColoringProcessOrderDetail>()
			: coloringProcessOrder.getDetaillist();
	List<OrderDetail> DetailList = order == null ? new ArrayList<OrderDetail>()
			: order.getDetaillist();
	if (DetailList == null) {
		DetailList = new ArrayList<OrderDetail>();
	}
	String productfactoryStr = (String) request
			.getAttribute("productfactoryStr");
%>
<!DOCTYPE html>
<html>
	<head>

		<title>染色进度单 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8">
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<!-- 为了让IE浏览器运行最新的渲染模式 -->
	</head>
	<body class="">
		<div class="container-fluid gridTab auto_container">
			<div class="row">
				<div class="col-md-12 tablewidget">
					<table class="table noborder">
						<caption id="tablename">
							桐庐富伟针织厂染色进度单
						</caption>
						<tr>
							<td colspan="3" class="pull-right">
								№：<%=order.getOrderNumber()%></td>
						</tr>
					</table>

					<table id="orderTb" class="tableTb">
						<tbody>
							<tr>
								<td align="center" rowspan="6" width="50%">
									<img id="previewImg" alt="200 x 100%"
										src="/<%=order.getImg_s()%>">
								</td>
								<td>
									公司
								</td>
								<td><%=SystemCache.getCompanyShortName(order.getCompanyId())%></td>
							</tr>
							<tr>
								<td>
									客户
								</td>
								<td><%=SystemCache.getCustomerName(order.getCustomerId())%></td>
							</tr>
							<tr>
								<td>
									货号
								</td>
								<td><%=order.getCompany_productNumber()%></td>
							</tr>
							<tr>
								<td>
									款名
								</td>
								<td><%=order.getName()%></td>
							</tr>
							<tr>
								<td>
									跟单
								</td>
								<td><%=SystemCache.getEmployeeName(order.getCharge_employee())%></td>
							</tr>
							<tr>
								<td>
									发货时间
								</td>
								<td><%=DateTool.formatDateYMD(order.getEnd_at())%></td>
							</tr>
						</tbody>
					</table>

					<table id="mainTb" class="noborder">
						<tr>
							<td colspan="2">
								<table class="detailTb">
									<caption>
										纱线信息
									</caption>
									<thead>
										<tr>
											<th width="15%">
												材料
											</th>
											<th width="15%">
												颜色
											</th>
											<th width="15%">
												数量(kg)
											</th>
											<th width="15%">
												染色单位
											</th>
										</tr>
									</thead>
									<tbody>
										<%
											for (ColoringProcessOrderDetail detail : coloringProcessOrderDetailList) {
										%>
										<tr class="tr" data='<%=SerializeTool.serialize(detail)%>'>
											<td class="material_name"><%=SystemCache.getMaterialName(detail.getMaterial())%>
											</td>
											<td class="color"><%=detail.getColor()%>
											</td>
											<td class="quantity"><%=detail.getQuantity()%>
											</td>
											<td class="factory_name"><%=SystemCache.getFactoryName(detail.getFactoryId())%>
											</td>
										</tr>

										<%
											}
										%>

									</tbody>
								</table>
							</td>
						</tr>
						<tr>
							<td style="vertical-align: top;">

								<table class="auto_height stickedTb">
									<thead>
										<tr>
											<th width="25%">
												时间
											</th>
											<th width="25%">
												纱线
											</th>
											<th width="25%">
												颜色
											</th>
											<th width="25%">
												数量(kg)
											</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
										</tr>

									</tbody>

								</table>
							</td>
							<td style="vertical-align: top;">
								<table class="auto_height stickedTb">
									<thead>
										<tr>
											<th width="25%">
												时间
											</th>
											<th width="25%">
												纱线
											</th>
											<th width="25%">
												颜色
											</th>
											<th width="25%">
												数量(kg)
											</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
										</tr>

									</tbody>

								</table>
							</td>

						</tr>
					</table>

					<p class="pull-right auto_bottom">
						<span id="created_user">制单人：<%=SystemCache.getUserName(coloringProcessOrder
							.getCreated_user())%></span>
						<span id="date"> 日期：<%=DateTool.formatDateYMD(DateTool.getYanDate(coloringProcessOrder
							.getCreated_at()))%></span>
					</p>



				</div>

			</div>
		</div>
	</body>
</html>