<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.producesystem.Fuliao"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.entity.producesystem.Location"%>
<%@page import="com.fuwei.entity.ordergrid.FuliaoPurchaseOrderDetail"%>
<%@page import="com.fuwei.entity.ordergrid.FuliaoPurchaseOrder"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	FuliaoPurchaseOrder fuliaoPurchaseOrder = (FuliaoPurchaseOrder)request.getAttribute("fuliaoPurchaseOrder");
	FuliaoPurchaseOrderDetail detail = (FuliaoPurchaseOrderDetail)request.getAttribute("fuliaoPurchaseOrderDetail");
	Map<Integer,Integer> locationMap = (Map<Integer,Integer>)request.getAttribute("locationMap");
	List<Location> locationlist = (List<Location>)request.getAttribute("locationlist");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>更改库位 -- 桐庐富伟针织厂</title>
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
		<script src="js/plugins/jquery-barcode.min.js"></script>
		<script src="js/selffuliao/scan_confirm.js" type="text/javascript"></script>
		<link href="css/plugins/ui.jqgrid.css" rel="stylesheet"
			type="text/css" />
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
.arrowSpan{position: absolute;font-weight: bold;font-size: 16px;}
.fa-long-arrow-right.fa-7x{font-size: 7em;}
.widget{  vertical-align: middle;display: table;  width: 100%;overflow: hidden;}
.widget .col-md-5{display:table-cell; float: none;  vertical-align: middle;}
.widget .col-md-2{   display: table-cell;float: none;  vertical-align: middle;}
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
							更改库位
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12">
								<form class="saveform">
									<input type="hidden" name="fuliaoPurchaseOrderDetailId" value="<%=detail.getId() %>" />
									<p class="alert alert-info">信息提示：更改库位将会把选定辅料的所有库存都转移到新库位中，请仔细确认新库位是否足以放下该辅料的所有库存</p>
								<button type="submit"
													class="pull-right btn btn-danger saveTable"
													data-loading-text="正在保存...">
													确认
												</button>
									<div class="clear"></div>
									<div class="col-md-12 tablewidget">
										<table class="table table-responsive noborder">
											<tbody>
												<tr>
													<td colspan="2">
														<table class="table table-responsive table-bordered">
															<tbody>
																<%if(fuliaoPurchaseOrder.getOrderId()!=null && fuliaoPurchaseOrder.getOrderId()!=0){ %>
																<tr>
																	<td colspan="3">
																		<div class="name">工厂订单号：</div><span class="value"><%=fuliaoPurchaseOrder.getOrderNumber()%></span>
																	</td>
																</tr>
																<%} %>
																<tr>
																	<td rowspan="7" width="30%">
																		<a href="/<%=fuliaoPurchaseOrder.getImg()%>" class="thumbnail"
																			target="_blank"> <img id="previewImg"
																				alt="200 x 100%" src="/<%=fuliaoPurchaseOrder.getImg_s()%>">
																		</a>
																	</td>
																	<td width="200px">
																		<div class="name">公司货号：</div><span class="value"><%=fuliaoPurchaseOrder.getCompany_productNumber()%></span>
																	</td>
																</tr>
																<tr>
																	<td>
																		<div class="name">采购单号：</div><span class="value"><%=fuliaoPurchaseOrder.getNumber()%></span>
																	</td>
																</tr>
																<tr>
																	<td>
																		<div class="name">辅料类型：</div><span class="value"><%=SystemCache.getFuliaoTypeName(detail.getStyle())%></span>
																	</td>
																</tr>
																<tr>
																	<td>
																		<div class="name">库位容量：</div><span class="value"><%=detail.getLocationSizeString()%></span>
																	</td>
																</tr>
																<tr>
																	<td>
																		<div class="name">辅料备注：</div><span class="value"><%=detail.getMemo()%></span>
																	</td>
																</tr>
															</tbody>


														</table>
													</td>
												</tr>
											</tbody>
										</table>
								<div class="row widget">
									<div class="col-md-5">
										<table id="mainTb"
											class="table table-responsive table-bordered detailTb">
											<thead>
												<tr>
													<th width="30%">
														库位编号
													</th>
													<th width="30%">
														库位容量
													</th><th width="40%">
														库存数量(个)
													</th>
												</tr>
											</thead>
											<tbody>
												<%
													for (Integer locationId : locationMap.keySet()) {
														Location location = SystemCache.getLocation(locationId);
												%>
												<tr class="tr">
													<td><%=location.getNumber()%></td>
													<td><%=location.getSizeString()%></td>
													<td><%=location.getQuantity()%></td>
												</tr>
												<%
													}
												%>
											</tbody>

										</table></div>
										<div class="col-md-2 arrowDiv"><div class="arrowSpan">更改库位为</div>
										<i class="fa fa-long-arrow-right fa-7x"></i>
										</div>
											<div class="col-md-5">
												<div class="form-group selectDiv">
													<label class="control-label">
														请选择新的库位：
													</label>
													<select class="value require form-control" name="locationId" id="locationId">
													<option value="">未选择</option>
													<%for(Location location : locationlist){%>
														<option value="<%=location.getId()%>"><%=location.getNumber()%></option>
													<%}%>
													</select>
												</div>
											</div>
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