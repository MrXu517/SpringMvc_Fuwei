<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.producesystem.Location"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	List<Location> locationlist = (List<Location>) request
			.getAttribute("locationList");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>库位管理 -- 桐庐富伟针织厂</title>
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


		<link href="css/systeminfo.css" rel="stylesheet" type="text/css" />
		<script src="js/location/index.js" type="text/javascript"></script>
		<style type="text/css">
		span.size{margin-right:20px;font-size:20px;}
		input[name='size']{height:20px;width:20px;}
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
							辅料仓库系统
						</li>
						<li class="active">
							库位管理
						</li>
					</ul>
				</div>
				<div class="body">
					<div id="materialTab">
						<div class="container-fluid">
							<div class="row">
								<div class="col-md-4 formwidget">
									<div class="panel panel-primary">
										<div class="panel-heading">
											<h3 class="panel-title">
												添加库位
											</h3>
										</div>
										<div class="panel-body">
											<form class="form-horizontal" role="form">
												<input type="hidden" name="id" id="id" />
												<div class="form-group">
													<label for="number" class="col-sm-3 control-label">
														编号
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control require"
															name="number" id="number" placeholder="编号">
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="location" class="col-sm-3 control-label">
														位置
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control"
															name="location" id="location" placeholder="位置">
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="size" class="col-sm-3 control-label">
														容量大小
													</label>
													<div class="col-sm-8">
														<input type="radio" name="size" id="size_3" value="3"/><span class="size">大</span>
														<input type="radio" name="size" id="size_2" value="2"/><span class="size">中</span>
														<input type="radio" name="size" id="size_1" value="1" checked/><span class="size">小</span>
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<div class="col-sm-offset-3 col-sm-5">
														<button type="submit" class="btn btn-primary"
															data-loading-text="正在保存...">
															添加库位
														</button>
														<a href="#" class="switch_add">添加</a>
													</div>
													<div class="col-sm-3">
														<button type="reset" class="btn btn-default">
															重置表单
														</button>
													</div>
													<div class="col-sm-1"></div>
												</div>
											</form>
										</div>
									</div>
								</div>

								<div class="col-md-7 tablewidget">
									<div class="panel panel-primary">
										<!-- Default panel contents -->
										<div class="panel-heading">
											库位列表
										</div>

										<!-- Table -->
										<table class="table table-responsive">
											<thead>
												<tr>
													<th>
														序号
													</th>
													<th>
														编号
													</th>
													<th>
														位置
													</th>
													<th>
														容量
													</th>
													<th>
														操作
													</th>
												</tr>
											</thead>
											<tbody>
												<%
													int g_i = 1;
													for (Location temp : locationlist) {
												%>
												<tr>
													<td><%=g_i%></td>
													<td><%=temp.getNumber()%></td>
													<td><%=temp.getLocation()%></td>
													<td><%=temp.getSizeString()%></td>
													<td>
														<a class="edit" href="#"
															data-cid="<%=temp.getId()%>">编辑</a>
													</td>
												</tr>
												<%
													g_i++;
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
			</div>
		</div>
	</body>
</html>