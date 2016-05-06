<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.producesystem.Fuliao"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.entity.producesystem.FuliaoOutDetail"%>
<%@page import="com.fuwei.entity.producesystem.FuliaoOutNotice"%>
<%@page import="com.fuwei.entity.producesystem.FuliaoOutNoticeDetail"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.fuwei.entity.Employee"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Order order = (Order) request
			.getAttribute("order");
	FuliaoOutNotice notice = (FuliaoOutNotice) request.getAttribute("notice");
	List<FuliaoOutNoticeDetail> detaillist = notice.getDetaillist();
	if (detaillist == null) {
		detaillist = new ArrayList<FuliaoOutNoticeDetail>();
	}
	List<Employee> employeelist = (List<Employee>)request.getAttribute("employeelist");
	Map<Integer,List<Map<String,Object>>> locationMap = (Map<Integer,List<Map<String,Object>>>) request.getAttribute("locationMap");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>创建辅料出库单 -- 桐庐富伟针织厂</title>
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
		<script src="js/fuliaoinout/add_out.js" type="text/javascript"></script>
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
tr.disable{background:#ddd;}
#created_user{  margin-right: 50px;}
form .selectDiv.form-group{  width: 250px;display: inline-block;}
</style>

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
							<a href="fuliao_workspace/workspace">辅料工作台</a>
						</li>
						<li class="active">
							创建辅料出库单
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid materialorderWidget">
						<div class="row">
							<div class="col-md-12">
								<form class="saveform">
									<input type="hidden" name="id" value="" />
									<input type="hidden" name="fuliaoout_noticeId" value="<%=notice.getId() %>" />
									<input type="hidden" name="orderId"
										value="<%=order.getId()%>" />

									<div class="clear"></div>
									<div class="col-md-12 tablewidget">
										<table class="table">
											<caption id="tablename">
												桐庐富伟针织厂辅料出库单
												<button type="submit"
													class="pull-right btn btn-danger saveTable"
													data-loading-text="正在保存...">
													创建辅料出库单
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
																		<a href="/<%=order.getImg()%>" class="thumbnail"
																			target="_blank"> <img id="previewImg"
																				alt="200 x 100%" src="/<%=order.getImg_s()%>">
																		</a>
																	</td>
																	<td width="200px">
																		<div class="name">订单号：</div><span class="value"><%=order.getOrderNumber()%></span>
																	</td>
																	<td>
																		<div class="name">货号：</div><span class="value"><%=order.getCompany_productNumber()%></span>
																	</td>
																</tr>
																<tr>
																	<td>
																		<div class="name">公司：</div><span class="value"><%=SystemCache.getCompanyShortName(order.getCompanyId())%></span>
																	</td>
																	<td>
																		<div class="name">客户：</div><span class="value"><%=SystemCache.getCustomerName(order.getCustomerId())%></span>
																	</td>
																</tr>
																<tr>
																	<td>
																		<div class="name">款名：</div><span class="value"><%=order.getName()%></span>
																	</td>
																	<td>
																		<div class="name">跟单：</div><span class="value"><%=SystemCache.getEmployeeName(order.getCharge_employee())%></span>
																	</td>
																</tr>
																<tr>
																	<td colspan="2">
																		<div class="name">领取人：</div>
																				<div class="form-group selectDiv"><select class="value require form-control" name="receiver_employee" id="receiver_employee">
																					<option value="">未选择</option>
																					<%for(Employee employee : employeelist){
																						if(notice.getReceiver_employee()!=null && employee.getId() == notice.getReceiver_employee()){
																					%>
																						<option value="<%=employee.getId()%>" selected><%=employee.getName()%></option>
																					<%}else{%>
																						<option value="<%=employee.getId()%>"><%=employee.getName()%></option>
																					<%}}%>
																				</select> </div>
																	</td>
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
												
												<tr>
													<th rowspan="2" width="70px">
														辅料类型
													</th><th rowspan="2" width="120px">
														图片
													</th><th rowspan="2" width="60px">
														订单号
													</th><th rowspan="2" width="60px">
														款号
													</th><th rowspan="2" width="55px">
														国家
													</th><th rowspan="2" width="55px">
														颜色
													</th><th rowspan="2" width="55px">
														尺码
													</th><th rowspan="2" width="55px">
														批次
													</th><th rowspan="2" width="70px">
														出库总数
													</th>
													<th colspan="3" width="180px">出库库位分布</th>
													<th rowspan="2" width="100px">
														备注
													</th>
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
													for (FuliaoOutNoticeDetail detail : detaillist) {
														boolean even = i%2 == 0;
														String classname = even?"even":"odd";
														int fuliaoId = detail.getFuliaoId();
														List<Map<String,Object>> templist = locationMap.get(fuliaoId);
														int detailsize = templist.size();
														int first_locationId = (Integer)templist.get(0).get("locationId");
														int first_quantity = (Integer)templist.get(0).get("quantity");
														int first_stock_quantity = (Integer)templist.get(0).get("stock_quantity");
														JSONObject json = JSONObject.fromObject(detail);
														json.putAll(templist.get(0));
												%>
												<tr data='<%=json.toString()%>' class="tr <%=classname%>">
													<td rowspan="<%=detailsize%>"><%=SystemCache.getFuliaoTypeName((Integer)detail.getFuliaoTypeId())%><br><%=detail.getFnumber()%></td>
													<td rowspan="<%=detailsize%>"><a href="/<%=detail.getImg()%>" class=""
																			target="_blank"> <img id="previewImg"
																				alt="200 x 100%" src="/<%=detail.getImg_ss()%>">
																		</a></td>
													<td rowspan="<%=detailsize%>"><%=detail.getCompany_orderNumber()%></td>
													<td rowspan="<%=detailsize%>"><%=detail.getCompany_productNumber()%></td>
													<td rowspan="<%=detailsize%>"><%=detail.getCountry()%></td>
													<td rowspan="<%=detailsize%>"><%=detail.getColor()%></td>
													<td rowspan="<%=detailsize%>"><%=detail.getSize()%></td>
													<td rowspan="<%=detailsize%>"><%=detail.getBatch()%></td>
													<td rowspan="<%=detailsize%>"><%=detail.getQuantity()%></td>
													<td><%=SystemCache.getLocationNumber(first_locationId)%></td>
													<td><%=first_stock_quantity%></td>
													<td>
														<input disabled class="form-control " type="text" value="<%=first_quantity%>">
													</td>
													<td>
														<input disabled class="form-control " type="text" value="<%=detail.getMemo()==null?"":detail.getMemo()%>">
													</td>
												</tr>
												<%
												templist.remove(0);
												for(Map<String,Object> map : templist){
													JSONObject json2 = JSONObject.fromObject(detail);
													json.putAll(map);
												%>
												<tr data='<%=json.toString()%>' class="tr <%=classname %>">
													<td><%=SystemCache.getLocationNumber((Integer)map.get("locationId"))%></td>
													<td><%=(Integer)map.get("stock_quantity")%></td>
													<td>
														<input disabled class="form-control " type="text" value="<%=map.get("quantity")%>">
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
											<span id="date"> 日期：<%=DateTool.formatDateYMD(DateTool.now())%></span>
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