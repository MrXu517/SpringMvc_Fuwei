<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.entity.finishstore.PackingOrder"%>
<%@page import="com.fuwei.entity.finishstore.PackingOrderDetail"%>
<%@page import="com.fuwei.entity.finishstore.FinishStoreStockDetail"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.fuwei.entity.finishstore.FinishStoreOutNotice"%>
<%@page import="com.fuwei.entity.finishstore.FinishStoreOutNoticeDetail"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	FinishStoreOutNotice notice = (FinishStoreOutNotice)request.getAttribute("finishStoreOutNotice");
	List<FinishStoreOutNoticeDetail> detaillist = notice.getDetaillist();
	if (detaillist == null) {
		detaillist = new ArrayList<FinishStoreOutNoticeDetail>();
	}
	Map<Integer,FinishStoreStockDetail> stockMap = (Map<Integer,FinishStoreStockDetail>)request.getAttribute("stockMap");
	if (stockMap == null) {
		stockMap = new HashMap<Integer,FinishStoreStockDetail>();
	}
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>编辑成品发货通知单 -- 桐庐富伟针织厂</title>
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
		<script src="js/finishstoreout_notice/edit.js" type="text/javascript"></script>
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
							<a href="order/detail/<%=notice.getOrderId()%>">订单详情</a>
						</li>
						<li>
							<a href="finishstoreout_notice/list/<%=notice.getOrderId()%>">成品发货通知单列表</a>
						</li>
						<li class="active">
							编辑成品发货通知单
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid materialorderWidget">
						<div class="row">
							<div class="col-md-12">
								<form class="saveform">
									<input type="hidden" name="id" value="<%=notice.getId() %>" />
									<input type="hidden" name="orderId"
										value="<%=notice.getOrderId()%>" />
									<input type="hidden" name="packingOrderId"
										value="<%=notice.getPackingOrderId()%>" />

									<div class="clear"></div>
									<div class="col-md-12 tablewidget">
										<table class="table">
											<caption id="tablename">
												桐庐富伟针织厂成品发货通知单
												<button type="submit"
													class="pull-right btn btn-danger saveTable"
													data-loading-text="正在保存...">
													编辑成品发货通知单
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
																		<a href="/<%=notice.getImg()%>" class="thumbnail"
																			target="_blank"> <img id="previewImg"
																				alt="200 x 100%" src="/<%=notice.getImg_s()%>">
																		</a>
																	</td>
																	<td width="300px">
																		<div class="name">订单号：</div><span class="value"><%=notice.getOrderNumber()%></span>
																	</td>
																	<td>
																		<div class="name">公司：</div><span class="value"><%=SystemCache.getCompanyShortName(notice.getCompanyId())%></span>
																	</td>
																</tr>
																<tr>
																	<td>
																		<div class="name">货号：</div><span class="value"><%=notice.getCompany_productNumber()%></span>
																	</td>
																	<td>
																		<div class="name">客户：</div><span class="value"><%=SystemCache.getCustomerName(notice.getCustomerId())%></span>
																	</td>
																</tr>
																<tr>
																	<td>
																		<div class="name">款名：</div><span class="value"><%=notice.getName()%></span>
																	</td>
																	<td>
																		<div class="name">跟单：</div><span class="value"><%=SystemCache.getEmployeeName(notice.getCharge_employee())%></span>
																	</td>
																</tr>
																<tr><td colspan="2"><div class="form-group ">
															<div class="name">发货时间：</div>
															<input type="text" class="form-control require date" style="width: 200px;display: inline-block"
																name="date" id="date"
																value="<%=DateTool.formatDateYMD(notice.getDate())%>">
														</div></td></tr>
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
											<%
											int col = 0;
											if(notice.getCol1_id()!=null){
											col++;
											 %>
											<th rowspan="2" width="70px">
												<%=SystemCache.getPackPropertyName(notice.getCol1_id()) %>
											</th>
											<%} %>
											
											<%if(notice.getCol2_id()!=null){ 
											col++;%>
											<th rowspan="2" width="70px">
												<%=SystemCache.getPackPropertyName(notice.getCol2_id()) %>
											</th>
											<%} %>
											<%if(notice.getCol3_id()!=null){ 
											col++;%>
											<th rowspan="2" width="70px">
												<%=SystemCache.getPackPropertyName(notice.getCol3_id()) %>
											</th>
											<%} %>
											<%if(notice.getCol4_id()!=null){ 
											col++;%>
											<th rowspan="2" width="70px">
												<%=SystemCache.getPackPropertyName(notice.getCol4_id()) %>
											</th>
											<%} %>
											<th rowspan="2" width="40px">
												颜色
											</th>
											<th rowspan="2" width="40px">
												总数量
											</th>
											<th rowspan="2" width="40px">
												总箱数
											</th>
											<th rowspan="2" width="50px">
												已发数量
											</th>
											<th rowspan="2" width="50px">
												已发箱数
											</th>
											<th rowspan="2" width="50px">
												库存箱数
											</th>
											<th rowspan="2" width="50px">
												待发箱数
											</th>
											<th rowspan="2" width="50px">
												本次发货数量
											</th>
											<th rowspan="2" width="60px">
												本次发货箱数
											</th>
												</tr>
											</thead>
											<tbody>
												<%
													for (FinishStoreOutNoticeDetail detail : detaillist) {
														FinishStoreStockDetail temp = stockMap.get(detail.getPackingOrderDetailId());
												%>
												<tr class="tr" data='<%=SerializeTool.serialize(detail)%>'>
													<td><input type="checkbox" name="checked" class="checkBtn" checked/></td>
													<%if(notice.getCol1_id()!=null){ %>
										<td>
											<%=detail.getCol1_value()==null?"":detail.getCol1_value() %>
										</td>
										<%} %>
										<%if(notice.getCol2_id()!=null){ %>
										<td>
											<%=detail.getCol2_value()==null?"":detail.getCol2_value() %>
										</td>
										<%} %>	
										<%if(notice.getCol3_id()!=null){ %>
										<td>
											<%=detail.getCol3_value()==null?"":detail.getCol3_value() %>
										</td>
										<%} %>
										<%if(notice.getCol4_id()!=null){ %>
										<td>
											<%=detail.getCol4_value()==null?"":detail.getCol4_value() %>
										</td>
										<%} %>


													<td><%=detail.getColor()%></td>
													<td><%=temp.getPlan_quantity()%></td>
													<td><%=temp.getPlan_cartons()%></td>
													<td><%=temp.getOut_quantity()%></td>
													<td><%=temp.getOut_cartons()%></td>
													<td><%=temp.getStock_cartons()%></td>
													<td><%=temp.getPlan_cartons() - temp.getOut_cartons()%></td>
													<td class="quantity">
														<%=detail.getQuantity()%>
													</td>
													<td>
														<input class="cartons form-control require positive_int value"
															type="text" value="<%=detail.getCartons() %>"
															placeholder="请输入发货箱数">
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