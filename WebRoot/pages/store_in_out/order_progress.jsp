<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.commons.Pager"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.constant.OrderStatus"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.fuwei.entity.Order"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Pager pager = (Pager) request.getAttribute("pager");
	if (pager == null) {
		pager = new Pager();
	}
	HashMap<Integer, List<Map<String, Object>>> resultMap = (HashMap<Integer,  List<Map<String, Object>>> )request.getAttribute("resultMap");
	String orderNumber = (String) request.getAttribute("orderNumber");
	if (orderNumber == null) {
		orderNumber = "";
	}
	List<Order> orderlist = (List<Order>)request.getAttribute("orderlist");

%>
<!DOCTYPE html>

<html>
	<head>
		<base href="<%=basePath%>">
		<title>未发货订单原材料生产进度列表 -- 桐庐富伟针织厂</title>
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

		<link href="css/store_in_out/index.css" rel="stylesheet"
			type="text/css" />
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
							<a href="workspace/material_workspace">原材料工作台</a>
						</li>
						<li class="active">
							未发货订单原材料生产进度列表
						</li>
					</ul>
				</div>
				<div class="body">

					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 tablewidget">
								<!-- Table -->
								<div clas="navbar navbar-default">
									<form class="form-horizontal form-inline searchform"
										role="form">
										<input type="hidden" name="page" id="page"
											value="<%=1%>" />
										<div class="form-group salesgroup">
											<label for="orderNumber" class="col-sm-3 control-label">
												订单号
											</label>
											<div class="col-sm-9">
												<input class="form-control" type="text" name="orderNumber" id="orderNumber" value="<%=orderNumber %>" />
											</div>
										</div>
										<button class="btn btn-primary" type="submit">
														搜索
													</button>
									</form>
									<ul class="pagination">
										<li>
											<a
												href="store_in/order_progress?orderNumber=<%=orderNumber %>&page=1">«</a>
										</li>

										<%
										if (pager.getPageNo() > 1) {
									%>
										<li class="">
											<a
												href="store_in/order_progress?orderNumber=<%=orderNumber %>&page=<%=pager.getPageNo() - 1%>">上一页
												<span class="sr-only"></span> </a>
										</li>
										<%
										} else {
									%>
										<li class="disabled">
											<a disabled>上一页 <span class="sr-only"></span> </a>
										</li>
										<%
										}
									%>

										<li class="active">
											<a
												href="store_in/order_progress?orderNumber=<%=orderNumber %>&page=<%=pager.getPageNo() %>"><%=pager.getPageNo()%>/<%=pager.getTotalPage()%>，共<%=pager.getTotalCount()%>条<span
												class="sr-only"></span> </a>
										</li>
										<li>
											<%
											if (pager.getPageNo() < pager.getTotalPage()) {
										%>
										
										<li class="">
											<a
												href="store_in/order_progress?orderNumber=<%=orderNumber %>&page=<%=pager.getPageNo() + 1%>">下一页
												<span class="sr-only"></span> </a>
										</li>
										<%
										} else {
									%>
										<li class="disabled">
											<a disabled>下一页 <span class="sr-only"></span> </a>
										</li>
										<%
										}
									%>

										</li>
										<li>
											<a
												href="store_in/order_progress?orderNumber=<%=orderNumber %>&page=<%=pager.getTotalPage()%>">»</a>
										</li>
									</ul>

								</div>

								<table class="table table-responsive table-bordered">
									<thead>
										<tr style="height:0;">
											<th style="width:120px"></th>
    										<th style="width:55px"></th>
    										<th style="width:60px"></th>
											<th style="width:70px"></th>
    										<th style="width:55px"></th>
    										<th style="width:55px"></th>
    										<th style="width:60px"></th>
    										<th style="width:60px"></th>
    										<th style="width:50px"></th>
    										<th style="width:40px"></th>
    										<th style="width:40px"></th>
    										<th style="width:60px"></th>
  										</tr>
										<tr>
											<th rowspan="2" width="120px">
												样品图片
											</th>
											<th rowspan="2" width="55px">
												订单号
											</th>
											<th rowspan="2" width="60px">
												公司货号
											</th>
											<th rowspan="2" width="70px">
												款名
											</th><th colspan="5" width="165px">各色号及材料生产进度</th>
											<th rowspan="2" width="40px">
												公司
											</th><th rowspan="2" width="40px">
												跟单
											</th><th rowspan="2" width="60px">
												发货日期
											</th>
										</tr><tr><th width="55px">
												色号
											</th><th width="55px">
												材料
											</th><th  width="60px">
												计划数量
											</th><th  width="60px">
												实际生产
											</th><th  width="50px">
												进度
											</th></tr>
									</thead>
									<tbody>
										<%
											for(int i = 0 ; i < orderlist.size();++i){
												Order order = orderlist.get(i);
												int orderId = order.getId();
												int count = 0 ;
												boolean even = i%2 == 0;
												String classname = even?"even":"odd";
												List<Map<String,Object>> color_materiallist = resultMap.get(orderId);			
												int size = color_materiallist.size();
												int detailsize = Math.max(size,1);//若color_materiallist为空，则detailsize=1 ，否则=color_materiallist.size()
												for (int k=0;k<detailsize;++k) {
													Map<String,Object> detail = null;
													if(color_materiallist.size() > k){
														detail = color_materiallist.get(k);
													}
													String color = detail == null ? "" :(String)detail.get("color");
													int materialId = detail == null ? 0 :(Integer)detail.get("material");
													double plan_quantity = detail == null ? 0.0 :(Double)detail.get("total_quantity");
													double actual_in_quantity = detail == null ? 0.0:(Double)detail.get("actual_in_quantity");
													int progress = detail == null ? 0 :(int)(actual_in_quantity/(double)plan_quantity * 100);
													if(count == 0){ %>
													<tr class="<%=classname%>">
														<td rowspan="<%=detailsize %>"
															style="max-width: 120px; height: 120px; max-height: 120px;">
															<a target="_blank" class="cellimg"
																href="/<%=order.getImg()%>"><img
																	style="max-width: 120px; height: 120px; max-height: 120px;"
																	src="/<%=order.getImg_ss()%>"> </a>
														</td>
														<td rowspan="<%=detailsize%>"><a target="_top" href="order/detail/<%=order.getId()%>"><%=order.getOrderNumber()%></a>
															<br><br><a target="_blank" href="store_in/actual_in/<%=order.getId()%>">进度详情</a>
														</td>
														<td rowspan="<%=detailsize%>"><%=order.getCompany_productNumber()%></td>
														<td rowspan="<%=detailsize%>"><%=order.getName()%></td>
														
														<%if(size<=0){
															%>
															<td colspan="5">没有计划与入库数据</td>
														<%}else{%>
															<td><%=color%></td>
														<td><%=SystemCache.getMaterialName(materialId)%></td>
														<td><%=plan_quantity%></td>
														<td><%=actual_in_quantity%></td>
														<td><%=progress%>%</td>
														<%}%>
														<td rowspan="<%=detailsize%>"><%=SystemCache.getCompanyShortName(order.getCompanyId())%></td>
														<td rowspan="<%=detailsize%>"><%=SystemCache.getEmployeeName(order.getCharge_employee())%></td>
														<td rowspan="<%=detailsize%>"><%=DateTool.formatDateYMD(order.getEnd_at())%></td>
													</tr>
													<%} else{
													%>
													<tr class="<%=classname%>">
														<td><%=color%></td>
														<td><%=SystemCache.getMaterialName(materialId)%></td>
														<td><%=plan_quantity%></td>
														<td><%=actual_in_quantity%></td>
														<td><%=progress%>%</td>
													</tr>
													<%} 
													++count;
													}
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

		<script type="text/javascript">
	/*设置当前选中的页*/
	var $a = $("#left li a[href='store_in/order_progress']");
	setActiveLeft($a.parent("li"));
</script>
	</body>
</html>