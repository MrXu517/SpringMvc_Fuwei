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
		<title>修改密码 -- 桐庐富伟针织厂</title>
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

	</head>

	<body>
		<%@ include file="../common/head.jsp"%>
		<script src="js/common/common.js"></script>
		<script src="js/user/set.js"></script>

		<div id="Content">
			<div id="main">
				<div class="breadcrumbs" id="breadcrumbs">
					<ul class="breadcrumb">
						<li>
							<i class="fa fa-home"></i>
							<a href="user/index">首页</a>
						</li>
						<li class="active">
							修改密码
						</li>
					</ul>
				</div>
				<div class="body">

					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 formwidget">
								<div class="panel panel-primary">
									<div class="panel-heading">
										<h3 class="panel-title">
											修改密码
										</h3>
									</div>
									<div class="panel-body">
										<form class="form-horizontal setform" role="form">
											<div class="form-group">
												<label for="password" class="col-sm-3 control-label">
												</label>
												<div class="col-sm-8">
													<span class="error"></span>
												</div>
											</div>
											<div class="form-group">
												<label for="password" class="col-sm-3 control-label">
													原密码
												</label>
												<div class="col-sm-8">
													<input type="password" class="form-control" name="password"
														id="password">
												</div>
												<div class="col-sm-1"></div>
											</div>
											<div class="form-group">
												<label for="password_2" class="col-sm-3 control-label">
													密码确认
												</label>
												<div class="col-sm-8">
													<input type="password" class="form-control"
														name="password_2" id="password_2">
												</div>
												<div class="col-sm-1"></div>
											</div>
											<div class="form-group">
												<label for="newPassword" class="col-sm-3 control-label">
													新密码
												</label>
												<div class="col-sm-8">
													<input type="password" class="form-control"
														name="newPassword" id="newPassword">
												</div>
												<div class="col-sm-1"></div>
											</div>

											<div class="form-group">
												<div class="col-sm-offset-3 col-sm-5">
													<button type="submit" class="btn btn-primary"
														data-loading-text="正在保存...">
														确定
													</button>

												</div>
												<div class="col-sm-3">
													<button type="reset" class="reset btn btn-default">
														重置表单
													</button>
												</div>
												<div class="col-sm-1"></div>
											</div>
									</div>
									</form>
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
