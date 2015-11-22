<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Sample"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.entity.QuotePrice"%>
<%@page import="net.sf.json.JSONObject"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Sample sample = (Sample) request.getAttribute("sample");
%>
<!DOCTYPE html>
<html>
	<head>

		<base href="<%=basePath%>">
		<title>样品展示详情 -- 桐庐富伟针织厂</title>
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
		<link href="css/sample/sample.css" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<%@ include file="../../common/head.jsp"%>
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
							<a href="sample_display/index">样品展示</a>
						</li>
						<li class="active">
							样品展示详情
						</li>
					</ul>
				</div>
				<div class="body">

					<div class="container-fluid">
						<div class="samplehead">
							<div class="pull-left">
								<label class="control-label">
									名称：
								</label>
								<span><%=sample.getName()%></span>
							</div>
							<div class="pull-left">
								<label class="control-label">
									产品工厂款号：
								</label>
								<span><%=sample.getProductNumber()%></span>
							</div>
							

							<div class="clear"></div>

						</div>
						<div class="sampleImg">

							<a href="#" class="thumbnail"> <img id="previewImg"
									alt="400 x 100%" src="/<%=sample.getImg_s()%>"> </a>

						</div>
						<div class="sampleData">
							<table class="table table-responsive">
								<tbody>

									<tr>
										<td>
											打样人
										</td>
										<td><%=SystemCache.getEmployeeName(sample.getCharge_employee())%></td>
									</tr>
									<tr>
										<td>
											材料
										</td>
										<td><%=SystemCache.getMaterialName(sample.getMaterialId()) %></td>
									</tr>
									<tr>
										<td>
											克重
										</td>
										<td><%=sample.getWeight()%>克
										</td>
									</tr>
									<tr>
										<td>
											尺寸
										</td>
										<td><%=sample.getSize()%></td>
									</tr>
									<tr>
										<td>
											创建时间
										</td>
										<td><%=sample.getCreated_at()%></td>
									</tr>
									<tr>
										<td>
											最近更新时间
										</td>
										<td><%=sample.getUpdated_at()%></td>
									</tr>
									<tr>
										<td>
											备注
										</td>
										<td><%=sample.getMemo()%></td>
									</tr>
								</tbody>
							</table>

						</div>
						
						<div class="clear"></div>
					</div>
					
					
					
				</div>


			</div>
		</div>
		<script type="text/javascript">
		/* 设置当前选中的页 */
		var $a = $("#left li a[href='display_sample/index']");
		setActiveLeft($a.parent("li"));
		</script>
	</body>
</html>