<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.ordergrid.PackingOrder"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	int orderId = (Integer)request.getAttribute("orderId");
	List<PackingOrder> packingOrderlist = (List<PackingOrder>)request.getAttribute("packingOrderList");
	Boolean has_packing_order_add = SystemCache.hasAuthority(session,"packing_order/add");

%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>订单装箱单 -- 桐庐富伟针织厂</title>
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
		<script src="js/plugins/jquery.form.js" type="text/javascript"></script>
		<link href="css/packing_order/index.css" rel="stylesheet"
			type="text/css" />
	<style type="text/css">
		.panel-title span{
			margin-left:40px;
		}
		.panel-title span:first-child{
			margin-left:0;
		}
		a.detail{
			font-weight: bold;
  			text-decoration: underline;
		}
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
							<a href="order/detail/<%=orderId%>">订单详情</a>
						</li>
						<li class="active">
							装箱单
						</li>
					</ul>
				</div>
				<div class="body">

					<div class="container-fluid">
						<%if(has_packing_order_add){ %>
						<a href="packing_order/add/<%=orderId %>" class="btn btn-primary" style="margin-bottom:10px;">上传新的装箱单</a>
						<%} %>
						<%if(packingOrderlist.size()<=0){ %>
							<span>还未创建装箱单</span>
						<%} %>
						<%
						int count = 1;
						int size = packingOrderlist.size();
						for(PackingOrder item : packingOrderlist){ %>
						<div class="row">
							<div class="col-md-12 formwidget">
								<div class="panel panel-info">
									<div class="panel-heading">
										<h3 class="panel-title">
										<span>[<%=count %>/<%=size %>]</span>	<span>上传时间:<%=item.getCreated_at() %> </span> <span>上传用户:<%=SystemCache.getUserName(item.getCreated_user())%></span>   <span class="memoSpan">备注: <%=item.getMemo() %></span>
										<a title="点击查看装箱单详细内容" class="detail pull-right" href="packing_order/detail/<%=item.getId() %>">详情</a>
										</h3>
									</div>
									<div class="panel-body">
										<iframe src="/<%=item.getPdfpath() %>"></iframe>
										
									</div>

								</div>
							</div>


						</div>
						<%++count;} %>
					</div>

				</div>
			</div>
		</div>
	</body>
</html>