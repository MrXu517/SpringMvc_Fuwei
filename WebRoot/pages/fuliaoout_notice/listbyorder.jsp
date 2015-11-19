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
<%@page import="com.fuwei.entity.producesystem.FuliaoOutNotice"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	int orderId = (Integer)request.getAttribute("orderId");
	List<FuliaoOutNotice> resultlist = (List<FuliaoOutNotice>)request.getAttribute("resultlist");

	//权限相关
	Boolean has_edit = SystemCache.hasAuthority(session,
			"fuliaoinout_notice/edit");
	Boolean has_add = SystemCache.hasAuthority(session,
			"fuliaoinout_notice/add");
	Boolean has_delete = SystemCache.hasAuthority(session,
			"fuliaoinout_notice/delete");
	//权限相关
%>
<!DOCTYPE html>

<html>
	<head>
		<base href="<%=basePath%>">
		<title>订单辅料出库通知单列表 -- 桐庐富伟针织厂</title>
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
		<script src="js/fuliaoout_notice/listbyorder.js" type="text/javascript"></script>
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
									<a target="_blank" id="addBtn" href="fuliaoout_notice/add/<%=orderId %>" class="btn btn-primary" >创建出库通知单</a>
								<%} %>
								</div>

								<table class="table table-responsive table-bordered" id="Tb">
									<thead>
										<tr>
											<th width="120px">
												单号
											</th>
											<th width="80px">
												状态
											</th>
											<th width="120px">
												制单时间
											</th>
											<th width="70px">
												制单人
											</th>
											<th width="80px">
												操作
											</th>
										</tr>
									</thead>
									<tbody>
										<%if(resultlist.size()<=0){ %>
										<tr><td colspan="12">还未添加出库通知单，请点击按钮 "创建出库通知单" 添加</td></tr>
										<%} %>
										<%
											
											for (FuliaoOutNotice notice : resultlist) {
										%>
									
										<tr itemId="<%=notice.getId()%>">
											<td><%=notice.getNumber()%></td>
											<td><%=notice.getStateString()%></td>
											<td><%=notice.getCreated_at()%></td>
											<td><%=SystemCache.getUserName(notice.getCreated_user()) %></td>
											
											<td>
												<a target="_blank" href="fuliaoout_notice/detail/<%=notice.getId()%>">详情</a>
												<%
													if (has_edit) {
												%>
												<a target="_blank" href="fuliaoout_notice/put/<%=notice.getId()%>">编辑</a>
												<%
													}
												%>
												<%
													if (has_delete) {
												%>
												<a href="#" class="delete" data-cid="<%=notice.getId() %>">删除</a>
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