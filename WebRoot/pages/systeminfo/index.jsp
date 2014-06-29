<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
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
		<title>系统信息管理 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8" />
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">
		<link href="css/systeminfo.css" rel="stylesheet" type="text/css" />
		<script src="js/plugins/jquery-1.10.2.min.js"></script>
		<script src="js/systeminfo/index.js" type="text/javascript"></script>
	</head>
	<body>
		<%@ include file="../common/head.jsp"%>
		<div id="Content">
			<div class="breadcrumbs" id="breadcrumbs">
				<ul class="breadcrumb">
					<li>
						<i class="icon-home home-icon"></i>
						<a href="index.jsp">首页</a>
					</li>
					<li class="active">
						系统信息管理
					</li>
				</ul>
			</div>
			<div class="body">
				<div id="tab">
					<ul class="nav nav-tabs" role="tablist">
						<li class="active">
							<a href="#companys" role="tab" data-toggle="tab">公司</a>
						</li>
						<li>
							<a href="#salesmans" role="tab" data-toggle="tab">业务员</a>
						</li>
						<li>
							<a href="#users" role="tab" data-toggle="tab">系统用户</a>
						</li>
						<li>
							<a href="#gongxus" role="tab" data-toggle="tab">工序</a>
						</li>
					</ul>

					<!-- Tab panes -->
					<div class="tab-content">
						<div class="tab-pane active" id="companys">
							<div class="container-fluid">
								<div class="row">
									<div class="col-md-4">
										<div class="panel panel-primary">
											<div class="panel-heading">
												<h3 class="panel-title">
													添加公司
												</h3>
											</div>
											<div class="panel-body">
												<form class="form-horizontal" role="form">
													<div class="form-group">
														<label for="inputEmail3" class="col-sm-3 control-label">
															中文名称
														</label>
														<div class="col-sm-8">
															<input type="email" class="form-control" id="inputEmail3"
																placeholder="中文名称">
														</div>
														<div class="col-sm-1"></div>
													</div>
													<div class="form-group">
														<label for="inputPassword3" class="col-sm-3 control-label">
															英文名称
														</label>
														<div class="col-sm-8">
															<input type="text" class="form-control"
																id="inputPassword3" placeholder="英文名称">
														</div>
														<div class="col-sm-1"></div>
													</div>
													<div class="form-group">
														<label for="inputPassword3" class="col-sm-3 control-label">
															公司地址
														</label>
														<div class="col-sm-8">
															<input type="text" class="form-control"
																id="inputPassword3" placeholder="公司地址">
														</div>
														<div class="col-sm-1"></div>
													</div>
													<div class="form-group">
														<label for="inputPassword3" class="col-sm-3 control-label">
															公司全称
														</label>
														<div class="col-sm-8">
															<input type="text" class="form-control"
																id="inputPassword3" placeholder="公司全称">
														</div>
														<div class="col-sm-1"></div>
													</div>
													<div class="form-group">
														<label for="inputPassword3" class="col-sm-3 control-label">
															所在城市
														</label>
														<div class="col-sm-8">
															<input type="text" class="form-control"
																id="inputPassword3" placeholder="所在城市">
														</div>
														<div class="col-sm-1"></div>
													</div>

													<div class="form-group">
														<div class="col-sm-offset-3 col-sm-3">
															<button type="submit" class="btn btn-primary" data-loading-text="正在添加...">
																添加公司
															</button>
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

									<div class="col-md-7">
										<div class="panel panel-primary">
											<!-- Default panel contents -->
											<div class="panel-heading">
												公司列表
											</div>

											<!-- Table -->
											<table class="table table-responsive">
												<tr>
													<th>
														序号
													</th>
													<th>
														公司名称
													</th>
													<th>
														简称
													</th>
												</tr>
											</table>
										</div>
									</div>
								</div>
							</div>

						</div>
						<div class="tab-pane" id="salesmans">
							业务员
						</div>
						<div class="tab-pane" id="users">
							系统用户
						</div>
						<div class="tab-pane" id="gongxus">
							工序
						</div>
					</div>
				</div>
				<!-- Nav tabs -->

			</div>
		</div>

	</body>
</html>