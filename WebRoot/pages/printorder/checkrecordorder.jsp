<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.ordergrid.CheckRecordOrder"%>
<%@page import="com.fuwei.entity.ordergrid.CheckRecordOrderDetail"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%
	Order order = (Order) request.getAttribute("order");
	//抽检记录单
	CheckRecordOrder checkRecordOrder = (CheckRecordOrder) request
			.getAttribute("checkRecordOrder");
	List<CheckRecordOrderDetail> checkRecordOrderDetailList = checkRecordOrder == null ? new ArrayList<CheckRecordOrderDetail>()
			: checkRecordOrder.getDetaillist();
%>
<!DOCTYPE html>
<html>
	<head>

		<title>抽检记录单 -- 桐庐富伟针织厂</title>
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
							桐庐富伟针织厂抽检记录单
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
										颜色及数量
									</caption>
									<thead>
										<tr>
											<th width="15%">
												颜色
											</th>
											<th width="15%">
												克重(g)
											</th>
											<th width="15%">
												纱线种类
											</th>
											<th width="15%">
												尺寸
											</th>
											<th width="15%">
												订单数量
											</th>
										</tr>
									</thead>
									<tbody>
										<%
											for (CheckRecordOrderDetail detail : checkRecordOrderDetailList) {
										%>
										<tr class="tr">
											<td class="color"><%=detail.getColor()%>
											</td>
											<td class="weight"><%=detail.getWeight()%>
											</td>
											<td class="yarn"><%=detail.getYarn()%>
											</td>
											<td class="size"><%=detail.getSize()%>
											</td>
											<td class="quantity"><%=detail.getQuantity()%>
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
										抽检记录
									</caption>
									<thead>
										<tr>
											<th width="7%">
												日期
											</th>
											<th width="7%">
												抽检数
											</th>
											<th width="10%">
												初/中/尾期
											</th>
											<th width="20%">
												问题描述
											</th>
											<th width="10%">
												严重
											</th>
											<th width="10%">
												轻微
											</th>
											<th width="15%">
												抽检结果
											</th>
											<th width="10%">
												质管签字
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
											<td></td>
											<td></td>
										</tr>

									</tbody>

								</table>
							</td>
						</tr>
					</table>

					<p class="pull-right auto_bottom">
						<span id="created_user">制单人：<%=SystemCache.getUserName(checkRecordOrder.getCreated_user()) %></span>
						<span id="date"> 日期：<%=DateTool.formatDateYMD(checkRecordOrder.getCreated_at()) %></span>
					</p>



				</div>

			</div>
		</div>
	</body>
</html>