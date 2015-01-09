<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.ordergrid.StoreOrder"%>
<%@page import="com.fuwei.entity.ordergrid.StoreOrderDetail"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%
	Order order = (Order) request.getAttribute("order");
	StoreOrder storeOrder = (StoreOrder) request
			.getAttribute("storeOrder");
	List<StoreOrderDetail> storeOrderDetailList = storeOrder == null ? new ArrayList<StoreOrderDetail>()
			: storeOrder.getDetaillist();
%>
<!DOCTYPE html>
<html>
	<head>

		<title>原材料记录单 -- 桐庐富伟针织厂</title>
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
							桐庐富伟针织厂原材料记录单
						</caption>
					</table>

					<table id="orderTb" class="tableTb">
						<tbody>
							<tr>
								<td align="center" rowspan="7" width="50%">
									<img id="previewImg" alt="200 x 100%"
										src="/<%=order.getImg_s()%>">
								</td>
								<td width="20%">
									生产单位
								</td>
								<td class="orderproperty"><%=order.getFactoryId() == null ? "" : SystemCache
					.getFactoryName(order.getFactoryId())%></td>
							</tr>

							<tr>
								<td colspan="2" class="center">
									订单信息
								</td>
							</tr>
							<tr>
								<td>
									公司
								</td>
								<td><%=SystemCache.getCompanyName(order.getCompanyId())%></td>
							</tr>
							<tr>
								<td>
									客户
								</td>
								<td><%=order.getKehu()%></td>
							</tr>
							<tr>
								<td>
									货号
								</td>
								<td><%=order.getProductNumber()%></td>
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
								<td><%=SystemCache.getUserName(order.getCharge_user())%></td>
							</tr>
						</tbody>
					</table>

					<table id="mainTb" class="noborder">
						<tr>
							<td>
								<table class="detailTb">
									<caption>
										材料列表
									</caption>
									<thead>
										<tr>
											<th width="15%">
												色号
											</th>
											<th width="15%">
												材料
											</th>
											<th width="15%">
												总数量
											</th>
											<th width="15%">
												标准样纱
											</th>
										</tr>
									</thead>
									<tbody>
										<%
											for (StoreOrderDetail detail : storeOrderDetailList) {
										%>
										<tr class="tr">
											<td class="color"><%=detail.getColor()%>
											</td>
											<td class="material"><%=detail.getMaterial()%>
											</td>
											<td class="quantity"><%=detail.getQuantity()%>
											</td>
											<td class="yarn"><%=detail.getYarn()%>
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
							<td>
								<table class="auto_height stickedTb">
									<caption>
										材料出入情况列表
									</caption>
									<thead>
										<tr>
											<th width="15%">
												色号
											</th>
											<th width="15%">
												材料
											</th>
											<th width="15%">
												入库/出库
											</th>
											<th width="15%">
												数量
											</th>
											<th width="15%">
												日期
											</th>
											<th width="15%">
												相关人员签字
											</th>

										</tr>
									</thead>
									<tbody>
										<tr>
											<td></td>
											<td></td>
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
						<span id="created_user">制单人：<%=SystemCache.getUserName(storeOrder.getCreated_user()) %></span>
						<span id="date"> 日期：<%=DateTool.formatDateYMD(storeOrder.getCreated_at()) %></span>
					</p>



				</div>

			</div>
		</div>
	</body>
</html>