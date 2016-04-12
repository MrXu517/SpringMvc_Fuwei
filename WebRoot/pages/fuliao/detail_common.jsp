<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.NumberUtil"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.entity.producesystem.HalfStoreInOut"%>
<%@page import="com.fuwei.entity.producesystem.HalfStoreInOutDetail"%>
<%@page import="com.fuwei.entity.ordergrid.PlanOrder"%>
<%@page import="com.fuwei.entity.ordergrid.PlanOrderDetail"%>
<%@page import="com.fuwei.entity.producesystem.HalfInOut"%>
<%@page import="com.fuwei.entity.producesystem.HalfInOutDetail"%>
<%@page import="com.fuwei.entity.producesystem.Fuliao"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Fuliao fuliao = (Fuliao) request.getAttribute("fuliao");
	Map<String,Object> stockMap = (Map<String,Object>)request.getAttribute("stockMap");
	List<Map<String,Object>> inNoticeMap = (List<Map<String,Object>>)request.getAttribute("inNoticeMap");
	List<Map<String,Object>> outNoticeMap = (List<Map<String,Object>>)request.getAttribute("outNoticeMap");
	List<Map<String,Object>> storeInOutMap = (List<Map<String,Object>>)request.getAttribute("storeInOutMap");
	Map<Integer,Integer> locationMap = (Map<Integer,Integer>)request.getAttribute("locationMap");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>辅料详情 -- 桐庐富伟针织厂</title>
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
		<script src="js/common/common.js" type="text/javascript"></script>
		<link href="css/order/index.css" rel="stylesheet" type="text/css" />
		<style type="text/css">
			.table>thead>tr>th {padding: 0 8px;vertical-align: middle;}
			.table>thead>tr {background: #AEADAD;}
			.table tbody tr {background: #ddd;}
			.thumbnail>img{max-width: 200px;max-height: 200px;}
			#leftOrderInfo{  width: 250px;}
			#leftOrderInfo .table-bordered>tbody>tr>td{border-color: #000;}
			#rightStoreInfo{margin-left:250px;}
			#rightStoreInfo .table-bordered>thead>tr>th,#rightStoreInfo .table-bordered>tbody>tr>td{border-color: #000;}
			#rightStoreInfo #storeDetail legend{font-weight: bold;}
			legend{margin-bottom:0;}
			#storeDetail thead th{text-align:center;}
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
							<i class=""></i>
							<a href="fuliao_workspace/commonfuliao?tab=fuliaolist">辅料列表及出入库情况</a>
						<li class="active">
							通用辅料详情
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 formwidget">
								<div class="head">
									<div class="pull-left">
										<label class="control-label">
											辅料编号：
										</label>
										<span><%=fuliao.getFnumber()%></span>
									</div>
								

									<div class="clear"></div>

								</div>
								<div class="clear"></div>

								<div class="col-md-6" id="leftOrderInfo">
									<table class="table table-responsive table-bordered">
																<tbody>
																	<tr>
																		<td colspan="2">
																			<a href="/<%=fuliao.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=fuliao.getImg_s()%>">
																			</a>
																		</td></tr>
																		<tr>
																			<td width="90px">
																				辅料类型
																			</td>
																			<td><%=SystemCache.getFuliaoTypeName(fuliao.getFuliaoTypeId())%></td>
																		</tr>
																	<tr>
																		<td>
																			公司
																		</td>
																		<td><%=SystemCache.getCompanyShortName(fuliao.getCompanyId()) %></td>
																	</tr><tr>
																		<td>
																			业务员
																		</td>
																		<td><%=SystemCache.getSalesmanName(fuliao.getSalesmanId())%></td>
																	</tr><tr>
																		<td>
																			客户
																		</td>
																		<td><%=SystemCache.getCustomerName(fuliao.getCustomerId())%></td>
																	</tr>
																	<tr>
																		<td>
																			公司订单号
																		</td>
																		<td><%=fuliao.getCompany_orderNumber()%></td>
																	</tr>
																	<tr>
																		<td>
																			公司货号
																		</td>
																		<td><%=fuliao.getCompany_productNumber()%></td>
																	</tr>
																	<tr>
																		<td>
																			颜色
																		</td>
																		<td><%=fuliao.getColor()%></td>
																	</tr>
																	<tr>
																		<td>
																			尺码
																		</td>
																		<td><%=fuliao.getSize()%></td>
																	</tr>
																	<tr>
																		<td>
																			批次
																		</td>
																		<td><%=fuliao.getBatch()%></td>
																	</tr>
																	<tr>
																		<td>
																			国家/城市
																		</td>
																		<td><%=fuliao.getCountry()%></td>
																	</tr>
																	<tr>
																		<td>
																			计划数量
																		</td>
																		<td><%=fuliao.getPlan_quantity()%></td>
																	</tr>
																	<tr>
																		<td>
																			库位容量
																		</td>
																		<td><%=fuliao.getLocationSizeString()%></td>
																	</tr>
																	<tr>
																		<td>
																			备注
																		</td>
																		<td><%=fuliao.getMemo()%></td>
																	</tr>
																	
																	<tr>
																		<td>
																			创建人
																		</td>
																		<td><%=SystemCache.getUserName(fuliao.getCreated_user())%></td>
																	</tr>
																	<tr>
																		<td>
																			创建时间
																		</td>
																		<td><%=DateTool.formatDateYMD(fuliao.getCreated_at())%></td>
																	</tr>
																</tbody>
															</table>
								</div>
							<div class="" id="rightStoreInfo">
								<fieldset id="orderDetail">
									<legend>
										预入库记录
									</legend>
									<table class="table table-responsive detailTb">
										<caption>
										</caption>
										<thead>
											<tr>
												<th width="15%">
													通知单单号
												</th>
												<th width="15%">
													状态
												</th>
												<th width="15%">
													辅料来源
												</th>
												<th width="15%">
													预入库数量
												</th>
												<th width="15%">
													创建时间
												</th>
											</tr>
										</thead>
										<tbody>
											<%for(Map<String,Object> item : inNoticeMap){ 
												int status = (Integer)item.get("status");
											%>
											<tr class="tr">
												<td><a href="fuliaoin_notice/detail/<%=item.get("fuliaoInOutNoticeId") %>"><%=item.get("number")%></a></td>
												<td><%if(status == 6){ %><span class="label label-success">已入库</span>
												<%} else if(status == -1){ %><span class="label label-danger">入库失败</span>
												<%} else{ %><span class="label label-info">等待入库</span>
												<%} %>
												</td>
												<td><%=SystemCache.getFactoryName((Integer)item.get("fuliaoPurchaseFactoryId"))%></td>
												<td><%=item.get("quantity")%></td>
												<td><%=item.get("created_at")%></td>
											</tr>
											<%} %>
										</tbody>
									</table>
								</fieldset>
								<fieldset id="orderDetail">
									<legend>
										预出库记录
									</legend>
									<table class="table table-responsive detailTb">
										<caption>
										</caption>
										<thead>
											<tr>
												<th width="15%">
													通知单单号
												</th>
												<th width="15%">
													状态
												</th>
												<th width="15%">
													预出库数量
												</th>
												<th width="15%">
													创建时间
												</th>
											</tr>
										</thead>
										<tbody>
											<%for(Map<String,Object> item : outNoticeMap){ 
												int status = (Integer)item.get("status");
											%>
											<tr class="tr">
												<td><a href="fuliaoout_notice/detail/<%=item.get("fuliaoInOutNoticeId") %>"><%=item.get("number")%></a></td>
												<td><%if(status == 6){ %><span class="label label-success">已出库</span>
												<%} else if(status == -1){ %><span class="label label-danger">出库失败</span>
												<%} else{ %><span class="label label-info">等待出库</span>
												<%} %>
												</td>
												<td><%=item.get("quantity")%></td>
												<td><%=item.get("created_at")%></td>
											</tr>
											<%} %>
										</tbody>
									</table>
								</fieldset>
								<fieldset id="storeDetail">
									<legend>
										出入库统计
									</legend>
									<table class="table table-responsive detailTb table-bordered">
										<thead>
											<tr>
												<th width="70px">
													出入库
												</th>
												<th width="70px">
													单号
												</th>
												<th width="100px">
													辅料来源
												</th>
												<th width="70px">
													数量
												</th>
												<th width="70px">
													库位
												</th>
												<th width="60px">
													经办人
												</th>
												<th width="60px">
													时间
												</th>
											</tr>
										</thead>
										<tbody>
											<%for(Map<String,Object> item : storeInOutMap){ 
												String type = item.get("type").toString();
												Object is_cleaning = item.get("is_cleaning");
											%>
											<tr class="tr">
												<td><%if(type.equals("in")){ %>入库
												<%} else if(type.equals("out") && is_cleaning!=null && Boolean.parseBoolean(is_cleaning.toString())==true){ %>
												清空库存
												<%} else{%>
												出库
												<%}%>
												</td>
												<td>
												<%if(type.equals("in")){ %><a href="fuliaoin/detail/<%=item.get("fuliaoInOutId") %>"><%=item.get("number")%></a>
												<%} else if(type.equals("out")){ %><a href="fuliaoout/detail/<%=item.get("fuliaoInOutId") %>"><%=item.get("number")%></a>
												<%}%>
												</td>
												<td><%=SystemCache.getFactoryName((Integer)item.get("fuliaoPurchaseFactoryId"))%></td>
												<td><%=item.get("quantity")%></td>
												<td><%=SystemCache.getLocationNumber((Integer)item.get("locationId"))%></td>
												<td><%=SystemCache.getUserName((Integer)item.get("created_user"))%></td>
												<td><%=item.get("created_at")%></td>
											</tr>
											<%} %>
										</tbody>
										<tfoot><td colspan="2">当前库存：<%=stockMap.get("stock_quantity")%></td>
												<td colspan="2">入库总数：<%=stockMap.get("in_quantity")%></td>
												<td colspan="3">出库总数：<%=stockMap.get("out_quantity")%> </td></tfoot>
									</table>
								</fieldset>

								<fieldset id="storeDetail">
									<legend>
										当前库位分布
									</legend>
									<table class="table table-responsive detailTb table-bordered">
										<thead>
											<tr>
												<th width="70px">
													库位编号
												</th>
												<th width="70px">
													库存数量
												</th>
											</tr>
										</thead>
										<tbody>
											<%for(Integer locationId : locationMap.keySet()){ 
											%>
											<tr class="tr">
												<td><%=SystemCache.getLocationNumber(locationId)%></td>
												<td><%=locationMap.get(locationId)%></td>
											</tr>
											<%} %>
										</tbody>
									</table>
								</fieldset>
								</div>
								<div class="clear"></div>
							</div>


						</div>
					</div>

				</div>
			</div>
		</div>
	</body>
</html>
