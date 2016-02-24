<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.entity.Material"%>
<%@page import="com.fuwei.entity.Customer"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.entity.ordergrid.ProducingOrderMaterialDetail"%>
<%@page import="com.fuwei.entity.ordergrid.ProducingOrderDetail"%>
<%@page import="com.fuwei.entity.ordergrid.ProducingOrder"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>扫描入库 -- 桐庐富伟针织厂</title>
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

		<link href="css/order/bill.css" rel="stylesheet" type="text/css" />
		<script src="js/order/ordergrid.js" type="text/javascript"></script>
		<script src="js/producing_order/price.js" type="text/javascript"></script>

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
							<a target="_blank" href="workspace/material_workspace">原材料工作台 -- 入库</a>
						</li>
						<li class="active">
							扫描入库
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid producingWidget">
						<div class="row">
							<p style="color:red;font-size:16px;margin-top: 10px;"><strong>大货纱线入库请扫描 【原材料采购单号】 进下面的空格中</strong></p>	
							<form action="store_in/add">
								<div class="form-group col-md-12">
									<label for="orderNumber" class="col-sm-3 control-label">
										原材料仓库单上的订单号
									</label>
									<div class="input-group col-sm-9">
										<input type="text" class="form-control" name="orderNumber" id="orderNumber" />

										<span class="input-group-btn">
											<button type="submit" class="pull-right btn btn-primary">
												搜索
											</button> </span>
									</div>

								</div>
						</form>
							<p style="color:red;font-size:16px;margin-top: 100px;"><strong>样纱入库请将 【染色单号】 扫描进入下面的空格中</strong></p>	
							<form action="store_in/add_coloring">
								<div class="form-group col-md-12">
									<label for="coloringOrderNumber" class="col-sm-3 control-label">
										请扫描样纱染色单
									</label>
									<div class="input-group col-sm-9">
										<input type="text" class="form-control" name="coloringOrderNumber" id="coloringOrderNumber" />

										<span class="input-group-btn">
											<button type="submit" class="pull-right btn btn-primary">
												搜索
											</button> </span>
									</div>

								</div>
							</form>
					</div>
				</div>
			</div>
		</div>
		</div>
		<script type="text/javascript">
			/*设置当前选中的页*/
			var $a = $("#left li a[href='workspace/material_workspace']");
			setActiveLeft($a.parent("li"));
			$("#orderNumber").focus();
			
			$("form").submit(function(){
				$("#orderNumber").focus();
				$("#orderNumber").select();
				return true;
			});
			
			$("#orderNumber").click(function(){
				$("#orderNumber").focus();
				$("#orderNumber").select();
				return true;
			});
			$("#coloringOrderNumber").click(function(){
				$("#coloringOrderNumber").focus();
				$("#coloringOrderNumber").select();
				return true;
			});
		</script>
	</body>
</html>