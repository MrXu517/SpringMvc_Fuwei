<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.ordergrid.ColoringOrder"%>
<%@page import="com.fuwei.entity.ordergrid.ColoringOrderDetail"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%
	Order order = (Order) request.getAttribute("order");
	//染色单
	ColoringOrder coloringOrder = (ColoringOrder) request
			.getAttribute("coloringOrder");
	List<ColoringOrderDetail> coloringOrderDetailList = coloringOrder == null ? new ArrayList<ColoringOrderDetail>()
			: coloringOrder.getDetaillist();
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
	</head>
	<body>
		<div class="container-fluid gridTab auto_container">
			<div class="row">
				<div class="col-md-12 tablewidget">
					<table class="table noborder">
						<caption id="tablename">
							桐庐富伟针织厂染色单
						</caption>
						<tr><td colspan="3" class="pull-right">№：<%=order.getOrderNumber() %></td></tr>
					</table>

					<table id="orderTb" class="tableTb noborder">
						<tbody>
							<tr>
								<td width="15%">
									染色单位：
								</td>
								<td class="orderproperty">
									<span><%=coloringOrder == null ? "" : (coloringOrder
					.getCompany() == null ? "" : coloringOrder.getCompany())%></span>
								</td>
								<td width="15%"></td>

								<td width="15%">

								</td>
								<td>
								</td>

							</tr>
							<tr height="20px">
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
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
												<span><%=SystemCache.getCompanyName(order.getCompanyId())%></span>
											</td>
											<td class="center">
												<span><%=order.getProductNumber()%></span>
											</td>
											<td class="center">
												<span><%=order.getKehu()%></span>
											</td>
											<td class="center">
												<span><%=order.getName()%></span>
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
												色号
											</th>
											<th width="15%">
												材料
											</th>
											<th width="15%">
												数量
											</th>
											<th width="15%">
												标准样纱
											</th>

										</tr>
									</thead>
									<tbody>
										<%
											for (ColoringOrderDetail detail : coloringOrderDetailList) {
										%>
										<tr class="tr">
											<td class="color"><%=detail.getColor()%>
											</td>
											<td class="material"><%=detail.getMaterial()%>
											</td>
											<td class="quantity"><%=detail.getQuantity()%>
											</td>
											<td class="standardyarn"><%=detail.getStandardyarn()%>
										</tr>

										<%
											}
										%>

									</tbody>
								</table>

								<table class="detailTb auto_height stickedTb">
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

					<p class="auto_bottom">
						质量要求：
					</p>
					<p id="tip" class="auto_bottom font_large">
						线颜色确保准确，手感柔软。颜色色牢度必须达到4级以上，不得含有偶氮，PCP，甲醛，APEO。不得含有特殊异味，无致敏致癌物质。纱线应烘干，且不可有污迹。如有以上问题造成客户索赔，贵厂须承担全部责任。
					</p>
					<p id="memo" class="auto_bottom font_small">
						<strong>备注：请务必在贵厂的纱卡上标注相应的货号，谢谢！</strong>
					</p>
					<p class="pull-right auto_bottom">
						<span id="created_user">制单人：<%=SystemCache.getUserName(coloringOrder.getCreated_user()) %></span>
						<span id="date"> 日期：<%=DateTool.formatDateYMD(coloringOrder.getCreated_at()) %></span>
					</p>



				</div>

			</div>
		</div>
	</body>
</html>