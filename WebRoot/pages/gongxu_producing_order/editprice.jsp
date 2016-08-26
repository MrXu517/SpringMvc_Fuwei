<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.entity.Material"%>
<%@page import="com.fuwei.entity.Customer"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.entity.ordergrid.GongxuProducingOrderMaterialDetail"%>
<%@page import="com.fuwei.entity.ordergrid.GongxuProducingOrderDetail"%>
<%@page import="com.fuwei.entity.ordergrid.GongxuProducingOrder"%>
<%@page import="com.fuwei.entity.GongXu"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	GongxuProducingOrder gongxuProducingOrder = (GongxuProducingOrder)request.getAttribute("gongxuProducingOrder");
	List<GongxuProducingOrderDetail> detaillist = gongxuProducingOrder.getDetaillist();
	if(detaillist == null){
		detaillist = new ArrayList<GongxuProducingOrderDetail>();
	}
	List<GongxuProducingOrderMaterialDetail> producingOrderMaterialDetailList = gongxuProducingOrder == null ? new ArrayList<GongxuProducingOrderMaterialDetail>()
												: gongxuProducingOrder.getDetail_2_list();
	producingOrderMaterialDetailList = producingOrderMaterialDetailList==null?new ArrayList<GongxuProducingOrderMaterialDetail>():producingOrderMaterialDetailList;
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>编辑工序加工单单价 -- 桐庐富伟针织厂</title>
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
		<script src="js/gongxu_producing_order/editprice.js" type="text/javascript"></script>
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
							<a href="order/tablelist?orderId=<%=gongxuProducingOrder.getOrderId()%>&tab=gongxuproduceorder">订单工序加工单</a>
						</li>
						<li class="active">
							编辑工序加工单单价
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid producingWidget">
						<div class="row">
							<form class="saveform">
								<input type="hidden" id="id" name="id"
									value="<%=gongxuProducingOrder.getId()%>" class="require" />
								<input type="hidden" id="orderId" name="orderId"
									value="<%=gongxuProducingOrder.getOrderId()%>" class="require" />
								

								<div class="clear"></div>
								<div class="col-md-12 tablewidget">
									<table class="table">
										<caption id="tablename">
											桐庐富伟针织厂工序加工单<button type="submit"
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
																			<a href="/<%=gongxuProducingOrder.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=gongxuProducingOrder.getImg_s()%>">
																			</a>
																		</td>
																		<td width="20%">
																			加工单位
																		</td>
																		<td class="orderproperty"><%=SystemCache.getFactoryName(gongxuProducingOrder.getFactoryId()) %>
																</td>
																	</tr>

																	<tr>
																		<td>
																	加工工序
																</td>
																<td class="orderproperty"><%=SystemCache.getGongxuName(gongxuProducingOrder.getGongxuId()) %>
																</td>
																	</tr>
																	<tr>
																		<td>
																			公司
																		</td>
																		<td><%=SystemCache.getCompanyName(gongxuProducingOrder
										.getCompanyId())%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=gongxuProducingOrder.getCompany_productNumber()%></td>
																	</tr>
																	<tr>
																		<td>
																			款名
																		</td>
																		<td><%=gongxuProducingOrder.getName()%></td>
																	</tr>
																	<tr>
																		<td>
																			跟单
																		</td>
																		<td><%=SystemCache.getEmployeeName(gongxuProducingOrder.getCharge_employee())%></td>
																	</tr>
															<tr>
																<td>
																	备注
																</td>
																<td class="orderproperty"><%=gongxuProducingOrder.getMemo()==null?"":gongxuProducingOrder.getMemo()%>
																</td>
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
																			生产数量(个、双)
																		</th>
																		
																		<th width="15%">
																			价格(/打)
																		</th>
																		

																	</tr>
																</thead>
																<tbody>
																	<%
																		for (GongxuProducingOrderDetail detail : detaillist) {
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
																		<td class="quantity"><%=detail.getQuantity()%>
																		</td>
																		<td class="double">
																			<input type="text" class="form-control price value"
																				value="<%=detail.getPrice()%>" />
																		</td>
																		
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