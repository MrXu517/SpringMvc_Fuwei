<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="com.fuwei.entity.producesystem.Location"%>
<%@page import="com.fuwei.entity.producesystem.SelfFuliaoIn"%>
<%@page import="com.fuwei.entity.ordergrid.FuliaoPurchaseOrder"%>
<%@page import="com.fuwei.entity.ordergrid.FuliaoPurchaseOrderDetail"%>
<%@page import="com.alibaba.fastjson.JSONObject"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	FuliaoPurchaseOrder fuliaoPurchaseOrder = (FuliaoPurchaseOrder) request
			.getAttribute("fuliaoPurchaseOrder");
	List<FuliaoPurchaseOrderDetail> detaillist = fuliaoPurchaseOrder.getDetaillist();
	if (detaillist == null) {
		detaillist = new ArrayList<FuliaoPurchaseOrderDetail>();
	}
	Map<Integer,List<Map<String,Object>>> locationMap = (Map<Integer,List<Map<String,Object>>>) request.getAttribute("locationMap");
	Map<Integer,Boolean> locationContains = new HashMap<Integer,Boolean>();
	Map<Integer,Map<String,Object>> stockMap = (Map<Integer,Map<String,Object>>)request.getAttribute("stockMap");
	String message = request.getAttribute("message") == null ? null : (String)request.getAttribute("message");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>创建自购辅料入库单 -- 桐庐富伟针织厂</title>
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
		<script src="js/order/ordergrid.js" type="text/javascript"></script>
		<script src="js/selffuliao/add_in.js" type="text/javascript"></script>
		<style type="text/css">
#tablename {
  font-weight: bold;
  font-size: 30px;
  margin-bottom: 15px;
}
.table>thead>tr>td {
	padding: 3px 8px;
}
.table-bordered>thead>tr>th, .table-bordered>thead>tr>td{border-bottom-width:1px;}
#mainTb {
	margin-top: 10px;  border-color: #000;
}
.table {
	margin-bottom: 0;
}
caption {
	font-weight: bold;
}
#previewImg {
	max-width: 200px;
	max-height: 150px;
}
#mainTb thead th{ background: #AEADAD;}
#mainTb thead th,#mainTb tbody tr td{border-color:#000;}
body {
  margin: auto;
  font-family: "Microsoft Yahei", "Verdana", "Tahoma, Arial",
 "Helvetica Neue", Helvetica, Sans-Serif,"SimSun";
}
.noborder.table>tbody>tr>td{border:none;}
.noborder.table{font-weight:bold;}
div.name{  width: 100px; display: inline-block;}
.checkBtn{height:25px;width:25px;}
tr.disable{background:#ddd;}
#created_user{  margin-right: 50px;}
</style>

	</head>
	<body>
		<%@ include file="../../common/head.jsp"%>
		<div id="Content">
			<div id="main">
				<div class="breadcrumbs" id="breadcrumbs">
					<ul class="breadcrumb">
						<li>
							<i class="fa fa-home"></i>
							<a href="user/index">首页</a>
						</li>
						<li>
							<a href="fuliao_workspace/workspace_purchase">自购辅料工作台</a>
						</li>
						<li class="active">
							创建自购辅料入库单
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid materialorderWidget">
						<div class="row">
							<div class="col-md-12">
								<form class="saveform">
									<input type="hidden" name="id" value="" />
									<input type="hidden" name="fuliaoPurchaseOrderId" value="<%=fuliaoPurchaseOrder.getId() %>" />
									<input type="hidden" name="orderId"
										value="<%=fuliaoPurchaseOrder.getOrderId()%>" />
									<%if(message != null){ %>
								<p class="alert alert-danger">信息提示：<%=message %></p>
								<%}%>
									<div class="clear"></div>
									<div class="col-md-12 tablewidget">
										<table class="table">
											<caption id="tablename">
												桐庐富伟针织厂自购辅料入库单
												<button type="submit"
													class="pull-right btn btn-danger saveTable"
													data-loading-text="正在保存...">
													确认入库
												</button>
											</caption>
										</table>
										<table class="table table-responsive noborder">
											<tbody>
												<tr>
													<td colspan="2">
														<table class="table table-responsive table-bordered">
															<tbody>
																<tr>
																	<td rowspan="7" width="30%">
																		<a href="/<%=fuliaoPurchaseOrder.getImg()%>" class="thumbnail"
																			target="_blank"> <img id="previewImg"
																				alt="200 x 100%" src="/<%=fuliaoPurchaseOrder.getImg_s()%>">
																		</a>
																	</td>
																	<td width="250px">
																		<div class="name">订单号：</div><span class="value"><%=fuliaoPurchaseOrder.getOrderNumber()%></span>
																	</td>
																	<td>
																		<div class="name">货号：</div><span class="value"><%=fuliaoPurchaseOrder.getCompany_productNumber()%></span>
																	</td>
																</tr>
																<tr>
																	<td>
																		<div class="name">公司：</div><span class="value"><%=SystemCache.getCompanyShortName(fuliaoPurchaseOrder.getCompanyId())%></span>
																	</td>
																	<td>
																		<div class="name">款名：</div><span class="value"><%=fuliaoPurchaseOrder.getName()%></span>
																	</td>
																</tr>
																
																<tr>
																	<td>
																		<div class="name">客户：</div><span class="value"><%=SystemCache.getCustomerName(fuliaoPurchaseOrder.getCustomerId())%></span>
																	</td>
																	<td>
																		<div class="name">跟单：</div><span class="value"><%=SystemCache.getEmployeeName(fuliaoPurchaseOrder.getCharge_employee())%></span>
																	</td>
																</tr>
																<tr>
																	<td>
																		<div class="name">辅料采购单：</div><span class="value"><%=fuliaoPurchaseOrder.getNumber()%></span>
																	</td>
																	<td>
																		<div class="name">采购单位：</div><span class="value"><%=SystemCache.getFactoryName(fuliaoPurchaseOrder.getFactoryId())%></span>
																	</td>
																</tr>
																<tr>
																	<td colspan="2"><div class="form-group ">
																入库时间：
																<input type="text" class="form-control require date"
																	name="date" id="date" style="width: 200px;display: inline-block;"
																	value="<%=DateTool.formatDateYMD(DateTool.now())%>">
															</div></td>
																	
																</tr>
														
															</tbody>


														</table>
													</td>
												</tr>
											</tbody>
										</table>
					
										<table id="mainTb"
											class="table table-responsive table-bordered detailTb">
											<thead>
												<tr><th width="5%">
														序号
													</th>
													<th width="8%">
														辅料类型
													</th><th width="8%">
														总数量
													</th><th width="8%">
														已入库
													</th><th width="8%">
														未入库
													</th><th width="15%">
														备注
													</th>
													<th width="10%">
														入库数量(个)
													</th>
													<th width="15%">
														库位
													</th>
												</tr>
											</thead>
											<tbody>
												<%
													for (FuliaoPurchaseOrderDetail detail : detaillist) {
														Map<String,Object> stockItem = stockMap.get(detail.getId());
														int in_quantity = 0;
														if(stockItem!=null && stockItem.containsKey("in_quantity")){
															in_quantity = (Integer)stockItem.get("in_quantity");
														}
														int not_in_quantity = detail.getQuantity();
														if(stockItem!=null && stockItem.containsKey("not_in_quantity")){
															not_in_quantity = (Integer)stockItem.get("not_in_quantity");
														}
														boolean flag = false;
														List<Map<String,Object>> templist = locationMap.get(detail.getId());
														JSONObject jsonObject = new JSONObject();
														jsonObject.put("fuliaoPurchaseOrderDetailId",detail.getId());
														jsonObject.put("style",detail.getStyle());
														jsonObject.put("memo",detail.getMemo());
												%>
												<tr class="tr EmptyTr disable" data='<%=jsonObject.toString()%>'>
													<td><input type="checkbox" class="checkBtn"/></td>
													<td><%=SystemCache.getFuliaoTypeName(detail.getStyle())%></td>
													
													<td><%=detail.getQuantity()%></td>
													<td><%=in_quantity%></td>
													<td><%=not_in_quantity%></td>
													<td><%=detail.getMemo()%></td>
													<td>
														<input disabled class="quantity form-control require positive_int value"
															type="text" value="0"
															placeholder="请输入预入库数量">
													</td>
													<td><select disabled class="locationId form-control value require">
														<%for(Map<String,Object> item: templist) {
															Integer locationId = (Integer)item.get("locationId");
															if(!flag && !locationContains.containsKey(locationId)){
																locationContains.put(locationId,true);
																flag=true;
																%>
															<option value="<%=locationId %>" selected><%=SystemCache.getLocationNumber(locationId) %></option>
															<%}else{ %>
																<option value="<%=locationId %>"><%=SystemCache.getLocationNumber(locationId) %></option>
															<%} %>
														<%} %>
													</td>
												</tr>
												<%
													}
												%>
											</tbody>

										</table>
											
										<div id="tip" class="auto_bottom">
											
										</div>

										<p class="pull-right auto_bottom">
											<span id="created_user">制单人：<%=SystemCache.getUserName(SystemContextUtils
							.getCurrentUser(session).getLoginedUser().getId())%></span>
											<span id="date"> 制单日期：<%=DateTool.formatDateYMD(DateTool.now())%></span>
										</p>

										</table>

									</div>
								</form>
							</div>
						</div>
					</div>

				</div>
			</div>
		</div>
	</body>
</html>