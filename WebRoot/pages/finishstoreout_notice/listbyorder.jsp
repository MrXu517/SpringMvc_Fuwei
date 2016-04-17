<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.commons.Pager"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.fuwei.entity.finishstore.FinishStoreOutNotice"%>
<%@page import="com.fuwei.entity.finishstore.FinishStoreOutNoticeDetail"%>
<%@page import="com.fuwei.entity.Order"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/";
	List<FinishStoreOutNotice> resultlist = (List<FinishStoreOutNotice>)request.getAttribute("resultlist");
	if (resultlist== null) {
		resultlist = new ArrayList<FinishStoreOutNotice>();
	}
	Order order = (Order) request.getAttribute("order");
%>
<!DOCTYPE html>

<html>
	<head>
		<base href="<%=basePath%>">
		<title>成品发货通知单列表 -- 桐庐富伟针织厂</title>
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
		<script src="<%=basePath%>js/plugins/WdatePicker.js"></script>
		<script src="js/common/common.js" type="text/javascript"></script>
		<script type='text/javascript' src='js/plugins/select2.min.js'></script>
		<link rel="stylesheet" type="text/css" href="css/plugins/select2.min.css" />
		<link href="css/store_in_out/index.css" rel="stylesheet"
			type="text/css" />
		<style type="text/css">
.thumbnail>img{max-width: 200px;max-height: 100px;}
div.name{margin-left: 20px;width: 100px; display: inline-block;}
.noborder.table>tbody>tr>td{border:none;padding:3px;text-align: left;}
.noborder.table{font-weight:bold;  margin-bottom: 5px;}
.thumbnail{margin-bottom:0;}
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
							<a href="order/detail/<%=order.getId()%>">订单详情</a>
						</li>
						<li class="active">
							成品发货通知单列表
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 tablewidget">
								<!-- Table -->
								<a class="btn btn-primary" href="finishstoreout_notice/add/<%=order.getId() %>" >通知发货</a>
								<table class="table table-responsive noborder">
																<tbody>
																<tr>
																		<td rowspan="3">
																			<a href="/<%=order.getImg()%>" class="thumbnail"
																				target="_blank"> <img id="previewImg"
																					alt="200 x 100%" src="/<%=order.getImg_s()%>">
																			</a>
																		</td><td>
																		<div class="name">订单号：</div><span class="value"><%=order.getOrderNumber()%></span>
																	</td>
																	<td>
																		<div class="name">公司：</div><span class="value"><%=SystemCache.getCompanyShortName(order.getCompanyId())%></span>
																	</td>
																	</tr>
																<tr>
																	<td>
																		<div class="name">款名：</div><span class="value"><%=order.getName()%></span>
																	</td>
																	<td>
																		<div class="name">客户：</div><span class="value"><%=SystemCache.getCustomerName(order.getCustomerId())%></span>
																	</td>
																</tr>
																<tr>
																	
																	<td>
																		<div class="name">货号：</div><span class="value"><%=order.getCompany_productNumber()%></span>
																	</td>
																	<td>
																		<div class="name">跟单：</div><span class="value"><%=SystemCache.getEmployeeName(order.getCharge_employee())%></span>
																	</td>
																</tr>
																</tbody>
															</table>
								<table class="table table-responsive table-bordered">
									<thead>
										<tr style="height:0;">
											<th style="width:20px"></th>
    										<th style="width:60px"></th>
    										<th style="width:60px"></th>
    										<th style="width:55px"></th>
    										<th style="width:55px"></th>
    										<th style="width:55px"></th>
    										<th style="width:55px"></th>
    										<th style="width:55px"></th>
    										<th style="width:60px"></th>
    										<th style="width:60px"></th>
    										<th style="width:60px"></th>
    										<th style="width:40px"></th>
  										</tr>
										<tr>
											<th rowspan="2"  width="20px" style="padding: 0;">
												No.
											</th>
											<th rowspan="2" width="60px">
												通知单号
											</th>
											<th rowspan="2" width="60px">
												状态
											</th><th colspan="7" width="165px">通知发货列表</th>
											<th rowspan="2" width="60px">
												发货时间
											</th>
											<th rowspan="2" width="40px">
												操作
											</th>
										</tr><tr><th width="55px">
												列1
											</th><th width="55px">
												列2
											</th><th width="55px">
												列3
											</th><th width="55px">
												列4
											</th><th width="55px">
												颜色
											</th><th  width="60px">
												箱数
											</th><th  width="60px">
												数量
											</th></tr>
									</thead>
									<tbody>
										<%if(resultlist==null || resultlist.size()==0){ %>
										<td colspan="12">还未创建“发货通知单”，请点击“通知发货”按钮创建</td>
										<%} %>
										<%
											int i = 0;
											for (FinishStoreOutNotice item : resultlist) {
												int status = item.getStatus();
												boolean even = i%2 == 0;
												String classname = even?"even":"odd";
												List<FinishStoreOutNoticeDetail> detailist = item.getDetaillist();
												if(detailist == null){detailist = new ArrayList<FinishStoreOutNoticeDetail>();}
												int detailsize = detailist.size();
										%>
										<tr itemId="<%=item.getId()%>" class="<%=classname%>">
											<td rowspan="<%=detailsize%>"><%=++i%></td>
											
											<td rowspan="<%=detailsize%>"><a target="_blank" href="finishstoreout_notice/detail/<%=item.getId()%>"><%=item.getNumber()%></a></td>
											<td rowspan="<%=detailsize %>"><%if(status == 6){ %><span class="label label-success">已入库</span>
												<%} else if(status == -1){ %><span class="label label-danger">入库失败</span>
												<%} else{ %><span class="label label-info">等待入库</span>
												<%} %></td>
											<td><%=detailist.get(0).getCol1_value() == null?"":detailist.get(0).getCol1_value()%></td>
											<td><%=detailist.get(0).getCol2_value() == null?"":detailist.get(0).getCol2_value()%></td>
											<td><%=detailist.get(0).getCol3_value() == null?"":detailist.get(0).getCol3_value()%></td>
											<td><%=detailist.get(0).getCol4_value() == null?"":detailist.get(0).getCol4_value()%></td>
											<td><%=detailist.get(0).getColor()%></td>
											<td><%=detailist.get(0).getCartons()%></td>
											<td><%=detailist.get(0).getQuantity()%></td>

										
											<td rowspan="<%=detailsize%>"><%=DateTool.formatDateYMD(item.getDate())%></td>				
											
											
											<td rowspan="<%=detailsize%>">
												<a target="_blank" href="finishstoreout_notice/detail/<%=item.getId()%>">详情</a>
												
											</td>
										</tr>
										<%
											detailist.remove(0);
											for(FinishStoreOutNoticeDetail detail : detailist){
										%>
										<tr class="<%=classname %>">
											<td><%=detail.getCol1_value() == null?"":detail.getCol1_value()%></td>
											<td><%=detail.getCol2_value() == null?"":detail.getCol2_value()%></td>
											<td><%=detail.getCol3_value() == null?"":detail.getCol3_value()%></td>
											<td><%=detail.getCol4_value() == null?"":detail.getCol4_value()%></td>
											<td><%=detail.getColor()%></td>
											<td><%=detail.getCartons()%></td>
											<td><%=detail.getQuantity()%></td>

										</tr>
										<%} %>
										<%
											}
										%>
										
									</tbody>
								</table>
							</div>
						</div>

					</div>
				</div>
			</div>
		</div>
	</body>
</html>