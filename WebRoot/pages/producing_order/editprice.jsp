<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.entity.Material"%>
<%@page import="com.fuwei.entity.Customer"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.entity.ordergrid.ProducingOrderMaterialDetail"%>
<%@page import="com.fuwei.entity.ordergrid.ProducingOrderDetail"%>
<%@page import="com.fuwei.entity.ordergrid.ProducingOrder"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Order order = (Order)request.getAttribute("order");
	ProducingOrder producingOrder = (ProducingOrder)request.getAttribute("producingOrder");
	List<ProducingOrderDetail> detaillist = producingOrder.getDetaillist();
	if(detaillist == null){
		detaillist = new ArrayList<ProducingOrderDetail>();
	}
	List<ProducingOrderMaterialDetail> producingOrderMaterialDetailList = producingOrder == null ? new ArrayList<ProducingOrderMaterialDetail>()
												: producingOrder.getDetail_2_list();
	
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>编辑生产单 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8">
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<!-- 为了让IE浏览器运行最新的渲染模式 -->
		<link href="css/plugins/bootstrap.min.css" rel="stylesheet"
			type="text/css" />
		<link href="css/plugins/font-awesome.min.css" rel="stylesheet"
			type="text/css" />
		<link href="css/common/common.css" rel="stylesheet" type="text/css" />
		<script src="js/plugins/jquery-1.10.2.min.js"></script>
		<script src="js/plugins/bootstrap.min.js" type="text/javascript"></script>
		<script src="<%=basePath%>js/plugins/WdatePicker.js"
			type="text/javascript"></script>
		<script src="js/common/common.js" type="text/javascript"></script>
		<script src="js/plugins/jquery.jqGrid.min.js" type="text/javascript"></script>
		<link href="css/plugins/ui.jqgrid.css" rel="stylesheet"
			type="text/css" />

		<link href="css/order/bill.css" rel="stylesheet" type="text/css" />
		<script src="js/order/ordergrid.js" type="text/javascript"></script>
		<script type='text/javascript' src='js/plugins/select2.min.js'></script>
		<link rel="stylesheet" type="text/css" href="css/plugins/select2.min.css" />
		<script src="js/producing_order/editprice.js" type="text/javascript"></script>

	</head>
	<body>
		<%@ include file="../common/head.jsp"%>
		<div id="Content">
			<div id="main">
				<div class="breadcrumbs" id="breadcrumbs">
					<ul class="breadcrumb">
						<li>
							<i class="fa fa-home"></i>
							<a href="user/index">首页</a>
						</li>
						<li>
							<a href="order/tablelist?orderId=<%=producingOrder.getOrderId()%>&tab=producingorder">订单表格</a>
						</li>
						<li class="active">
							编辑生产单
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid producingWidget">
						<div class="row">
							<form class="saveform">
								<input type="hidden" id="id" name="id"
									value="<%=producingOrder.getId()%>" class="require" />
								<input type="hidden" id="orderId" name="orderId"
									value="<%=producingOrder.getOrderId()%>" class="require" />
								

								<div class="clear"></div>
								<div class="col-md-12 tablewidget">
									<table class="table">
										<caption id="tablename">
											桐庐富伟针织厂生产单<button type="submit"
									class="pull-right btn btn-danger saveTable" data-loading-text="正在保存...">
									保存修改
								</button>
										</caption>
									</table><table><tbody>
										<tr>
														<td>
											<table class="table table-responsive table-bordered tableTb">
																<tbody>
																	<tr>
																		<td rowspan="7" width="50%">
																			<a href="/<%=order.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=order.getImg_s()%>">
																			</a>
																		</td>
																		<td width="20%">
																			生产单位
																		</td>
																		<td class="orderproperty">
																		<select id="factoryId" name="factoryId" class="require" style="width:150px;">
																		<%for(Factory fac: SystemCache.produce_factorylist){ 
																			if(producingOrder.getFactoryId()!=null && producingOrder.getFactoryId() == fac.getId()){
																			%>
																		<option selected value="<%=fac.getId() %>"><%=fac.getName() %></option>
																		<%}else{ %><option value="<%=fac.getId() %>"><%=fac.getName() %></option>
																		<%}} %>
																		</select></td>
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
																		<td><%=SystemCache.getCompanyName(order
										.getCompanyId())%></td>
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
																</tbody>
															</table>

														</td>
													</tr>
													<tr>
														<td>
															<table class="table table-responsive detailTb">
																<caption>
																	颜色及数量
																</caption>
																<thead>
																	<tr>
																		<th width="15%">
																			颜色
																		</th>
																		<th width="15%">
																			机织克重(g)
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
																		
																		<th width="15%">
																			价格(/个、顶、套)
																		</th>
																		

																	</tr>
																</thead>
																<tbody>
																	<%
																		for (ProducingOrderDetail detail : detaillist) {
																	%>
																	<tr class="tr" data='<%=SerializeTool.serialize(detail)%>'>
																		<td class="color"><%=detail.getColor()%>
																		</td>
																		<td class="produce_weight"><%=detail.getProduce_weight()%>
																		</td>
																		<td class="yarn_name"><%=SystemCache.getMaterialName(detail.getYarn())%>
																		</td>
																		<td class="size"><%=detail.getSize()%>
																		</td>
																		<td class="int">
																			<input type="text" class="form-control require quantity value"
																				value="<%=detail.getQuantity()%>" />
																		</td>
																		<td class="double"><input type="text" class="form-control require price value"
																				value="<%=detail.getPrice()%>" /></td>
																		
																	</tr>

																	<%
																		}
																	%>

																</tbody>
															</table>
															<div id="navigator"></div>
														</td>
													</tr>
												</tbody>
											</table>

								</div>


							
									
								</div>
							</form>
						</div>
					</div>
					

				</div>
			</div>
		</div>
	</body>
</html>