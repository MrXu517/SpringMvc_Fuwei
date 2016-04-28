<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.fuwei.entity.Employee"%>
<%@page import="com.fuwei.entity.ordergrid.FuliaoPurchaseOrder"%>
<%@page import="com.fuwei.entity.ordergrid.FuliaoPurchaseOrderDetail"%>
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
	List<Employee> employeelist = (List<Employee>)request.getAttribute("employeelist");
	Map<Integer,List<Map<String,Object>>> locationMap = (Map<Integer,List<Map<String,Object>>>) request.getAttribute("locationMap");
	Map<Integer,Map<String,Object>> stockMap = (Map<Integer,Map<String,Object>>)request.getAttribute("stockMap");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>创建自购辅料出库单 -- 桐庐富伟针织厂</title>
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
		<script src="js/selffuliao/add_out.js" type="text/javascript"></script>
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
#mainTb thead th{ background: #AEADAD;line-height: 10px;}
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
tr.disable,.quantity[readonly]{background:#ddd;}
#created_user{  margin-right: 50px;}
form .selectDiv.form-group{  width: 250px;display: inline-block;}
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
							创建自购辅料出库单
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

									<div class="clear"></div>
									<div class="col-md-12 tablewidget">
										<table class="table">
											<caption id="tablename">
												桐庐富伟针织厂自购辅料出库单
												<button type="submit"
													class="pull-right btn btn-danger saveTable"
													data-loading-text="正在保存...">
													创建自购辅料出库单
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
																	<td width="300px">
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
																	<td><div class="form-group ">
																出库时间：
																<input type="text" class="form-control require date"
																	name="date" id="date" style="width: 200px;display: inline-block;"
																	value="<%=DateTool.formatDateYMD(DateTool.now())%>">
															</div></td>
																<td>
																		<div class="name">领取人：</div>
																				<div class="form-group selectDiv"><select class="value require form-control" name="receiver_employee" id="receiver_employee">
																					<option value="">未选择</option>
																					<%for(Employee employee : employeelist){%>
																						<option value="<%=employee.getId()%>"><%=employee.getName()%></option>
																					<%}%>
																				</select> </div>
																	</td>
																	
																</tr>
														
															</tbody>


														</table>
													</td>
												</tr>
											</tbody>
										</table>
										<p id="errorTip" style="color:red;"></p>
										<table id="mainTb"
											class="table table-responsive table-bordered detailTb">
											<thead>
												
												<tr><th rowspan="2" width="5%">
														序号
													</th>
													<th rowspan="2" width="70px">
														辅料类型
													</th>
													<th rowspan="2" width="50px">
														计划数
													</th>
													<th rowspan="2" width="50px">
														已出库
													</th>
													<th rowspan="2" width="50px">
														未出库
													</th>
													<th rowspan="2" width="100px">
														备注
													</th><th rowspan="2" width="70px">
														出库总数
													</th>
													<th colspan="3" width="180px">出库库位分布</th>
													</tr>
												<tr>
													<th width="50px">
														库位
													</th>
													<th width="65px">
														库存(个)
													</th>
													<th width="70px">
														出库数量(个)
													</th>
												</tr>
											</thead>
											<tbody>
												<%
													int i=0;
													for (FuliaoPurchaseOrderDetail detail : detaillist) {
														Map<String,Object> stockItem = stockMap.get(detail.getId());
														int out_quantity = 0;
														if(stockItem!=null && stockItem.containsKey("out_quantity")){
															out_quantity = (Integer)stockItem.get("out_quantity");
														}
														int not_out_quantity = 0;
														if(stockItem!=null && stockItem.containsKey("not_out_quantity")){
															not_out_quantity = (Integer)stockItem.get("not_out_quantity");
														}
														boolean even = i%2 == 0;
														String classname = even?"even":"odd";
														int fuliaoPurchaseOrderDetailId = detail.getId();
														List<Map<String,Object>> templist = locationMap.get(fuliaoPurchaseOrderDetailId);
														int detailsize = templist.size();
														Integer first_locationId = null;
														if(templist.size()>0){
															first_locationId = (Integer)templist.get(0).get("locationId");
														}
														int first_stock_quantity = 0;
														if(templist.size()>0){
															first_stock_quantity = (Integer)templist.get(0).get("stock_quantity");
														}
														JSONObject json = new JSONObject();
														json.put("fuliaoPurchaseOrderDetailId",fuliaoPurchaseOrderDetailId);
														json.put("style",detail.getStyle());
														json.put("memo",detail.getMemo());
														json.put("locationId",first_locationId);
														json.put("stock_quantity",first_stock_quantity);
														json.put("quantity",0);
												%>
												<tr fuliaoPurchaseOrderDetailId="<%=fuliaoPurchaseOrderDetailId %>"  data='<%=json.toString()%>' class="tr EmptyTr disable <%=classname%>">
													<td rowspan="<%=detailsize%>"><input type="checkbox" class="checkBtn"/></td>
													<td rowspan="<%=detailsize%>"><%=SystemCache.getFuliaoTypeName((Integer)detail.getStyle())%></td>
													<td rowspan="<%=detailsize%>"><%=detail.getQuantity()%></td>
													<td rowspan="<%=detailsize%>"><%=out_quantity%></td>
													<td rowspan="<%=detailsize%>"><%=not_out_quantity%></td>
													<td rowspan="<%=detailsize%>"><%=detail.getMemo()==null?"":detail.getMemo()%></td>
													<td rowspan="<%=detailsize%>">
														<input disabled class="form-control total_quantity positive_int" type="text" value="<%=0%>">
													</td>
													<td><%=SystemCache.getLocationNumber(first_locationId)%></td>
													<td><%=first_stock_quantity%></td>
													<td>
														<input readonly class="form-control quantity value" type="text" value="<%=0%>">
													</td>
												</tr>
												<%if(templist.size()>0){
													templist.remove(0);
												}
												for(Map<String,Object> map : templist){
													JSONObject json2 = JSONObject.fromObject(detail);
													json2.put("fuliaoPurchaseOrderDetailId",fuliaoPurchaseOrderDetailId);
													json2.put("style",detail.getStyle());
													json2.put("memo",detail.getMemo());
													json2.put("locationId",(Integer)map.get("locationId"));
													json2.put("stock_quantity",(Integer)map.get("stock_quantity"));
													json2.put("quantity",0);
												%>
												<tr fuliaoPurchaseOrderDetailId="<%=fuliaoPurchaseOrderDetailId %>" data='<%=json.toString()%>' class="tr <%=classname %>">
													<td><%=SystemCache.getLocationNumber((Integer)map.get("locationId"))%></td>
													<td><%=(Integer)map.get("stock_quantity")%></td>
													<td>
														<input readonly class="form-control quantity value" type="text" value="<%=0%>">
													</td>
												</tr>
												<%} %>
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