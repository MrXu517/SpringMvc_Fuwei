<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.ordergrid.ProducingOrder"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.commons.Pager"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.constant.OrderStatus"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="net.sf.json.JSONObject"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	List<ProducingOrder> list = (List<ProducingOrder>) request.getAttribute("list");
	String orderNumber = (String) request.getAttribute("orderNumber");
	if (orderNumber == null) {
		orderNumber = "";
	}

	//权限相关
	Boolean has_order_detail = SystemCache.hasAuthority(session,
			"producing_order/detail");
	Boolean has_order_delete = SystemCache.hasAuthority(session,
			"order/producing/delete");
	Boolean has_order_producing_price_edit = SystemCache.hasAuthority(session,"order/producing/price_edit");
	//权限相关
%>
<!DOCTYPE html>

<html>
	<head>
		<base href="<%=basePath%>">
		<title>未划价生产单 -- 桐庐富伟针织厂</title>
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

		<link href="css/order/index.css" rel="stylesheet" type="text/css" />
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
						<li class="active">
							未划价生产单
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
										<div class="form-group salesgroup">
											<label for="orderNumber" class="col-sm-3 control-label">
												订单号
											</label>
											<div class="col-sm-9">
												<input class="form-control" type="text" name="orderNumber" id="orderNumber" value="<%=orderNumber %>" />
											</div>
											
										</div>
										<button class="btn btn-primary">搜索</button>
									</form>

								</div>

								<table class="table table-responsive">
									<thead>
										<tr>
											<th width="30px">
												序号
											</th>
											<th width="60px">
												生产单位
											</th>
											<th width="60px">
												订单号
											</th>
											<th width="80px">
												款名
											</th>
											<th width="60px">
												公司
											</th>
											<th width="60px">
												客户
											</th>
											<th width="60px">
												跟单
											</th>
											<th width="60px">
												创建时间
											</th>
											<th width="60px">
												操作
											</th>
										</tr>
									</thead>
									<tbody>
										<%
											int i = 0;
											for (ProducingOrder item : list) {
										%>
										<tr orderId="<%=item.getId()%>">
											<td><%=++i%></td>
											<td><%=SystemCache.getFactoryName(item.getFactoryId())%></td>
											<td><%=item.getOrderNumber() == null ? "":item.getOrderNumber() %></td>
											<td><%=item.getName()%></td>
											<td><%=SystemCache.getCompanyShortName(item.getCompanyId())%></td>
											<td><%=SystemCache.getCustomerName(item.getCustomerId())%></td>
											<td><%=SystemCache.getEmployeeName(item
										.getCharge_employee())%></td>
										
											<td><%=DateTool.formatDateYMD(item.getCreated_at())%></td>
											<td><%
													if (has_order_detail) {
												%>
												<a target="_blank"
													href="producing_order/price/<%=item.getId()%>">划价</a>
												<%
													}
												%>
												<%
													if (has_order_detail) {
												%>
												|
												<a target="_blank"
													href="producing_order/detail/<%=item.getId()%>">详情</a>
												<%
													}
												%>
												<%
												
													if (has_order_delete && item.deletable()) {
												%>
												|
												<a href="#" data-cid="<%=item.getId() %>"
													class="delete">删除</a>
												<%
														}
													%>

											</td>
										</tr>
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

		<script type="text/javascript">
	/*设置当前选中的页*/
	var $a = $("#left li a[href='producing_order/unprice_list']");
	setActiveLeft($a.parent("li"));
	//删除单据 -- 开始
	$(".delete").click( function() {
		var id = $(this).attr("data-cid");
		if (!confirm("确定要删除该生产单吗？")) {
			return false;
		}
		$.ajax( {
			url :"producing_order/delete/" + id,
			type :'POST'
		}).done( function(result) {
			if (result.success) {
				Common.Tip("删除生产单成功", function() {
					location.reload();
				});
			}
		}).fail( function(result) {
			Common.Error("删除生产单失败：" + result.responseText);
		}).always( function() {

		});
		return false;
	});
	//删除单据  -- 结束
</script>
	</body>
</html>