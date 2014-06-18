<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<!-- 为了让IE浏览器运行最新的渲染模式 -->
		<link href="css/plugins/bootstrap.min.css" rel="stylesheet"
			type="text/css" />
		<link href="css/plugins/login-cleaned.css" rel="stylesheet"
			type="text/css" />
		<link href="css/plugins/font-awesome.min.css" rel="stylesheet"
			type="text/css" />
		<script src="js/plugins/jquery-1.10.2.min.js"></script>

		<script src="js/plugins/bootstrap.min.js" type="text/javascript"></script>
		<title>登录</title>
	</head>

	<body>
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="col-md-12">
					<div class="page-header">
						<h1>
							页标题范例
							<small>此处编写页标题</small>
						</h1>
					</div>
					<div class="row-fluid">
						<div class="col-md-6">
						</div>
						<div class="col-md-6">


							<form class="form-horizontal" role="form">
								<div class="form-group">
									<label for="inputEmail3" class="col-sm-2 control-label">
										Email
									</label>
									<div class="col-sm-10">
										<input type="email" class="form-control" id="inputEmail3"
											placeholder="Email">
									</div>
								</div>
								<div class="form-group">
									<label for="inputPassword3" class="col-sm-2 control-label">
										Password
									</label>
									<div class="col-sm-10">
										<input type="password" class="form-control"
											id="inputPassword3" placeholder="Password">
									</div>
								</div>
								<div class="form-group">
									<div class="col-sm-offset-2 col-sm-10">
										<div class="checkbox">
											<label>
												<input type="checkbox">
												Remember me
											</label>
										</div>
									</div>
								</div>
								<div class="form-group">
									<div class="col-sm-offset-2 col-sm-10">
										<button type="submit" class="btn btn-default">
											Sign in
										</button>
									</div>
								</div>
							</form>


						</div>
					</div>
				</div>
			</div>
		</div>

	</body>

</html>