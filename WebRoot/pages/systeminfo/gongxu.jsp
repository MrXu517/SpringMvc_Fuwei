<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.GongXu"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	List<GongXu> gongxulist = (List<GongXu>) request
			.getAttribute("gongxulist");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>系统信息管理 -- 桐庐富伟针织厂</title>
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
		<script src="js/systeminfo/gongxu.js" type="text/javascript"></script>

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
							系统信息管理
						</li>
						<li class="active">
							工序管理
						</li>
					</ul>
				</div>
				<div class="body">
					<div id="gongxus">
						<div class="container-fluid">
							<div class="row">
								<div class="col-md-4 formwidget">
									<div class="panel panel-primary">
										<div class="panel-heading">
											<h3 class="panel-title">
												添加工序
											</h3>
										</div>
										<div class="panel-body">
											<form class="form-horizontal" role="form">
												<input type="hidden" name="id" id="id" />
												<div class="form-group">
													<label for="name" class="col-sm-3 control-label">
														名称
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control require"
															name="name" id="name" placeholder="名称">
													</div>
													<div class="col-sm-1"></div>
												</div>

												<div class="form-group">
													<div class="col-sm-offset-3 col-sm-5">
														<button type="submit" class="btn btn-primary"
															data-loading-text="正在保存...">
															添加工序
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
											工序列表
										</div>

										<!-- Table -->
										<table class="table table-responsive">
											<thead>
												<tr>
													<th>
														序号
													</th>
													<th>
														名称
													</th>
													<th>
														操作
													</th>
												</tr>
											</thead>
											<tbody>
												<%
													int g_i = 1;
													for (GongXu gongxu : gongxulist) {
												%>
												<tr>
													<td><%=g_i%></td>
													<td><%=gongxu.getName()%></td>
													<td>
														<a class="editGongxu" href="#"
															data-cid="<%=gongxu.getId()%>">编辑</a> |
														<a class="deleteGongxu" href="#"
															data-cid="<%=gongxu.getId()%>">删除</a>
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