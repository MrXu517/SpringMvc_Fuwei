<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.ordergrid.ProductionScheduleOrder"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%
	Order order = (Order) request.getAttribute("order");
	//检针记录表
	List<OrderDetail> DetailList = order == null ? new ArrayList<OrderDetail>()
			: order.getDetaillist();
	if(DetailList == null){
		DetailList = new ArrayList<OrderDetail>();
	}
	String productfactoryStr = (String)request.getAttribute("productfactoryStr");
%>
<!DOCTYPE html>
<html>
	<head>

		<title>检针记录表 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8">
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<!-- 为了让IE浏览器运行最新的渲染模式 -->
	</head>
	<body class="">
			<div class="container-fluid">
									<div class="row">
										<form class="saveform">
											<input type="hidden" name="orderId"
												value="<%=order.getId()%>" />
											
											<a target="_blank" type="button"
												class="pull-right btn btn-success printBtn"
												data-loading-text="正在打印..."> 打印 </a>
										</form>

										<div class="clear"></div>
										<div class="col-md-12 tablewidget">
											<table class="table">
												<caption>
													桐庐富伟针织厂检针记录表
												</caption>
												<thead>
													<tr>
														<td colspan="3" class="pull-right orderNumber">
															№：<%=order.getOrderNumber()%></td>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td>
															<table
																class="table table-responsive table-bordered tableTb">
																<tbody>
																	<tr>
																		<td rowspan="<%=7+DetailList.size() %>" width="50%">
																			<a href="/<%=order.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=order.getImg_s()%>">
																			</a>
																		</td>
																		<td>
																			公司
																		</td>
																		<td><%=SystemCache.getCompanyName(order.getCompanyId())%></td>
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
																	<tr>
																		<td colspan="2" class="center">
																			订单数量
																		</td>
																	</tr>
																	<%
																		for (OrderDetail detail : DetailList) {
																	%>
																	<tr>
																		<td><%=detail.getColor()%></td>
																		<td><%=detail.getQuantity()%></td>
																	</tr>
																	<%
																		}
																	%>
																</tbody>
															</table>

														</td>
													</tr>
													<tr>
														
													</tr>
												</tbody>
											</table>

										</div>


									</div>
								</div>
	</body>
</html>