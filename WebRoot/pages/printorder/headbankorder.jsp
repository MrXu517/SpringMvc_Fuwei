<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.ordergrid.HeadBankOrder"%>
<%@page import="com.fuwei.entity.ordergrid.PlanOrderDetail"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%
	Order order = (Order) request.getAttribute("order");
	HeadBankOrder headBankOrder = (HeadBankOrder) request
			.getAttribute("headBankOrder");
	List<PlanOrderDetail> headBankOrderDetailList = headBankOrder == null ? new ArrayList<PlanOrderDetail>()
			: headBankOrder.getDetaillist();
%>
<!DOCTYPE html>
<html>
	<head>
	
		<title>打印质量记录单 -- 桐庐富伟针织厂</title>
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
							桐庐富伟针织厂质量记录单
						</caption>
						<tr><td colspan="3" class="pull-right">№：<%=order.getOrderNumber() %></td></tr>
					</table>

					<table id="orderTb"
						class="tableTb">
						<tbody>
							<tr>
								<td align="center" rowspan="7" width="50%">
									<img id="previewImg" alt="200 x 100%"
										src="/<%=order.getImg_s()%>">
								</td>
								<td width="20%">
									生产单位
								</td>
								<td class="orderproperty"></td>
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
												生产数量
											</th>

										</tr>
									</thead>
									<tbody>
										<%
											for (PlanOrderDetail detail : headBankOrderDetailList) {
										%>
										<tr class="tr">
											<td class="color"><%=detail.getColor()%>
											</td>
											<td class="weight"><%=detail.getWeight()%>
											</td>
											<td class="yarn_name"><%=SystemCache.getMaterialName(detail.getYarn()) %>
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
								<table class="auto_height">
									<caption>
										质量信息记录
									</caption>
									<tbody>
										<tr>
											<td></td>
										</tr>
									</tbody>
									<tfoot>
										<tr>
											<td>
												<p class="pull-right" id="sign">
													是否同意生产：
												</p>
											</td>
										</tr>
									</tfoot>
								</table>
							</td>
						</tr>
					</table>


					<p id="tip" class="auto_bottom">
						各加工单位：请严格按照此生产单信息进行相关生产。
						<strong>生产前须由我厂质量管理封样，我方确认同意生产并签字后方可生产大货，否则一切后果由贵单位承担。</strong>此生产单一式两份，确认后请签字。此生产单妥善保管，结账以此单为准。
					</p>

					<p class="pull-right auto_bottom">
												<span id="created_user">制单人：<%=SystemCache.getUserName(headBankOrder.getCreated_user()) %></span>
						<span id="date"> 日期：<%=DateTool.formatDateYMD(headBankOrder.getCreated_at()) %></span>
					</p>



				</div>

			</div>
		</div>
	</body>
</html>