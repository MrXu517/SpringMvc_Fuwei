<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.producesystem.Fuliao"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Order order = (Order) request
			.getAttribute("order");
	List<Map<String,Object>> detaillist = (List<Map<String,Object>>)request.getAttribute("detaillist");
	if (detaillist == null) {
		detaillist = new ArrayList<Map<String,Object>>();
	}
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>创建辅料出库通知单 -- 桐庐富伟针织厂</title>
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
		<script src="js/fuliaoout_notice/add.js" type="text/javascript"></script>
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
							<a href="order/detail/<%=order.getId()%>">订单详情</a>
						</li>
						<li>
							<a href="order/fuliao_progress/<%=order.getId()%>?tab=fuliaooutnotice">辅料列表及出入库情况</a>
						</li>
						<li class="active">
							创建辅料出库通知单
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid materialorderWidget">
						<div class="row">
							<div class="col-md-12">
								<form class="saveform">
									<input type="hidden" name="id" value="" />
									<input type="hidden" name="orderId"
										value="<%=order.getId()%>" />

									<div class="clear"></div>
									<div class="col-md-12 tablewidget">
										<table class="table">
											<caption id="tablename">
												桐庐富伟针织厂辅料出库通知单
												<button type="submit"
													class="pull-right btn btn-danger saveTable"
													data-loading-text="正在保存...">
													创建辅料出库通知单
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
																	<td width="100px">
																		<div class="name">订单号：</div><span class="value"><%=order.getOrderNumber()%></span>
																	</td>
																</tr>
																<tr>
																	<td>
																		<div class="name">公司：</div><span class="value"><%=SystemCache.getCompanyShortName(order.getCompanyId())%></span>
																	</td>
																</tr>
																
																<tr>
																	<td>
																		<div class="name">客户：</div><span class="value"><%=SystemCache.getCustomerName(order.getCustomerId())%></span>
																	</td>
																</tr>
																<tr>
																	<td>
																		<div class="name">货号：</div><span class="value"><%=order.getCompany_productNumber()%></span>
																	</td>
																</tr>
																<tr>
																	<td>
																		<div class="name">款名：</div><span class="value"><%=order.getName()%></span>
																	</td>
																</tr>
																<tr>
																	<td>
																		<div class="name">跟单：</div><span class="value"><%=SystemCache.getEmployeeName(order.getCharge_employee())%></span>
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
												<tr><th width="5%">
														序号
													</th>
													<th width="8%">
														辅料类型
													</th><th width="20%">
														图片
													</th><th width="10%">
														订单号
													</th><th width="10%">
														款号
													</th><th width="10%">
														颜色
													</th><th width="10%">
														尺码
													</th><th width="10%">
														批次
													</th>
													<th width="10%">
														当前库存(个)
													</th>
													<th width="20%">
														预出库数量(个)
													</th>
												</tr>
											</thead>
											<tbody>
												<%
													for (Map<String,Object> detail : detaillist) {
														detail.put("fuliaoId",detail.get("id"));
														detail.remove("id");
												%>
												<tr class="tr EmptyTr disable" data='<%=SerializeTool.serialize(detail)%>'>
													<td><input type="checkbox" name="checked" class="checkBtn"/></td>
													<td><%=SystemCache.getFuliaoTypeName((Integer)detail.get("fuliaoTypeId"))%></td>
													<td><a href="/<%=detail.get("img")%>" class=""
																			target="_blank"> <img id="previewImg"
																				alt="200 x 100%" src="/<%=detail.get("img_ss")%>">
																		</a></td>
													<td><%=detail.get("company_orderNumber")%></td>
													<td><%=detail.get("company_productNumber")%></td>
													<td><%=detail.get("color")%></td>
													<td><%=detail.get("size")%></td>
													<td><%=detail.get("batch")%></td>
													<td><%=detail.get("stock_quantity")%></td>
													<td>
														<input disabled class="quantity form-control require positive_int value"
															type="text" value="0"
															placeholder="请输入预出库数量">
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