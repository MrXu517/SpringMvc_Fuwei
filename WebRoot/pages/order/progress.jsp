<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.Order"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Order order = (Order) request.getAttribute("order");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>订单生产进度 -- 桐庐富伟针织厂</title>
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
		<link href="css/order/progress.css" rel="stylesheet" type="text/css" />
	</head>

	<body>
		<%@ include file="../common/head.jsp"%>
	
		<div id="Content" class="auto_container">
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
							生产进度  -- <%=order.getName() %>
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 btnWidget">
								
								
							</div>
						</div>


					</div>
					<div id="tab">

						<ul class="nav nav-tabs" role="tablist">
							<li class="active">
								<a href="#coloringprocessorder" role="tab" data-toggle="tab">染色进度</a>
							</li>

							<li>
								<a href="#halfinout" role="tab" data-toggle="tab">半成品出入库记录</a>
							</li>
							<li>
								<a href="#halfprogress" role="tab" data-toggle="tab">半成品生产进度</a>
							</li>
							<li>
								<a href="#materialprogress" role="tab" data-toggle="tab">原材料生产进度</a>
							</li>
						</ul>


						<div class="tab-content auto_height">
							<div class="tab-pane active" id="coloringprocessorder">
								<iframe id="coloringprocessorderIframe" name="coloringprocessorderIframe" border="0" furl="" > </iframe>

							</div>
							<div class="tab-pane" id="halfinout">
								<iframe id="halfinoutIframe" name="halfinoutIframe" border="0" furl="half_current_stock/in_out2/<%=order.getId() %>" > </iframe>
							</div>
							<div class="tab-pane" id="halfprogress">
								<iframe id="halfprogressIframe" name="halfprogressIframe" border="0" furl="half_store_in/actual_in/<%=order.getId()%>" > </iframe>
							</div>
							<div class="tab-pane" id="materialprogress">
								<iframe id="materialprogressIframe" name="materialprogressIframe" border="0" furl="" > </iframe>
							</div>
						</div>
					</div>
					
				</div>

			</div>
		</div>
		</div>
	<script type="text/javascript">
			/*设置当前选中的页*/
			var $a = $("#left li a[href='order/index']");
			setActiveLeft($a.parent("li"));
			$("#tab ul.nav>li").click(function(){
				var $a = $(this).find("a");
				var tab_name = $a.attr("href").substring(1);
				var $frame = $("#"+tab_name).find("iframe");
				var src = $frame.attr("src");
				if(src == undefined || src == ""){
					$frame.attr("src",$frame.attr("furl"));
				}
			});
			$("#tab ul.nav>li").first().click();
		</script>
	</body>
</html>
