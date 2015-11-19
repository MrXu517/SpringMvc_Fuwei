<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.entity.Employee"%>
<%@page import="com.fuwei.commons.Pager"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.constant.OrderStatus"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.entity.producesystem.Fuliao"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Order order = (Order)request.getAttribute("order");
	List<Fuliao> fuliaoList = (List<Fuliao>)request.getAttribute("fuliaoList");

	//权限相关
	Boolean has_edit = SystemCache.hasAuthority(session,
			"fuliao/edit");
	Boolean has_add = SystemCache.hasAuthority(session,
			"fuliao/add");
	Boolean has_delete = SystemCache.hasAuthority(session,
			"fuliao/delete");
	//权限相关
%>
<!DOCTYPE html>

<html>
	<head>
		<base href="<%=basePath%>">
		<title>订单辅料列表 -- 桐庐富伟针织厂</title>
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
		<script src="js/fuliao/listbyorder.js" type="text/javascript"></script>
		<link href="css/order/index.css" rel="stylesheet" type="text/css" />
		<style type="text/css">
.body {
	min-width: 0;
}

#breadcrumbs {
	min-width: 0;
}
#Tb{
	border-color:#000;
}
#Tb>thead>tr{
	  background: #AEADAD;
}
#Tb>thead>tr>th,#Tb>tbody>tr>td{
	border-color:#000;
	border-bottom-width: 1px;
	padding-left: 0;
    padding-right: 0;
    text-align: center;
}
.memoform #memo{
	height:200px;
}
#addBtn,#addNoticeBtn{margin-bottom:10px;}
</style>
	</head>
	<body>
		<div id="Content">
			<div id="main">
				<div class="body">
								<!-- Table -->
								<div clas="navbar navbar-default">
								<%if(has_add){ %>
									<a target="_blank"  id="addBtn" href="fuliao/<%=order.getId() %>/add" class="btn btn-primary" >添加辅料</a>
								<%} %>
								</div>

								<table class="table table-responsive table-bordered" id="Tb">
									<thead>
										<tr>
											<th width="120px">
												图片
											</th>
											<th width="55px">
												辅料类型
											</th>
											<th width="70px">
												公司订单号
											</th>
											<th width="70px">
												公司货号
											</th>
											<th width="50px">
												颜色
											</th><th width="60px">
												尺码
											</th><th width="40px">
												批次
											</th>
											<th width="60px">
												国家/城市
											</th>
											<th width="60px">
												库位容量
											</th>
											<th width="80px">
												备注
											</th>
											<th width="70px">
												创建人
											</th>
											<th width="40px">
												操作
											</th>
										</tr>
									</thead>
									<tbody>
										<%if(fuliaoList.size()<=0){ %>
										<tr><td colspan="12">还未添加辅料，请点击按钮 "添加辅料" 添加</td></tr>
										<%} %>
										<%
											
											for (Fuliao fuliao : fuliaoList) {
										%>
									
										<tr itemId="<%=fuliao.getId()%>">
											<td style="max-width: 120px; height: 120px; max-height: 120px;">
												<a target="_blank" class="cellimg"
													href="/<%=fuliao.getImg()%>"><img
														style="max-width: 120px; height: 120px; max-height: 120px;"
														src="/<%=fuliao.getImg_ss()%>"> </a>
											</td>
											<td><%=SystemCache.getFuliaoTypeName(fuliao.getFuliaoTypeId())%></td>
											<td><%=fuliao.getCompany_orderNumber()%></td>
											<td><%=fuliao.getCompany_productNumber()%></td>
											<td><%=fuliao.getColor()%></td>
											<td><%=fuliao.getSize()%></td>
											<td><%=fuliao.getBatch()%></td>
											<td><%=fuliao.getCountry() %></td>
											<td><%=fuliao.getLocationSizeString() %></td>
											<td><%=fuliao.getMemo()%></td>
											<td><%=SystemCache.getUserName(fuliao.getCreated_user()) %> <br> <%=fuliao.getCreated_at() %></td>
											
											<td>
												<a target="_blank" href="fuliao/detail/<%=fuliao.getId()%>">详情</a>
												<%
													if (has_edit) {
												%><br><br>
												<a target="_blank" href="fuliao/put/<%=fuliao.getId()%>">编辑</a>
												<%
													}
												%>
												<%
													if (has_delete) {
												%><br><br>
												<a href="#" class="delete" data-cid="<%=fuliao.getId() %>">删除</a>
												<%
													}
												}
												%>
											</td>
										</tr>
									</tbody>
								</table>
				</div>
			</div>
		</div>
	</body>
</html>