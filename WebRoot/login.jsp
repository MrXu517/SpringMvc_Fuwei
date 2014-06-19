<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<!-- 为了让IE浏览器运行最新的渲染模式 -->
		<link href="css/plugins/bootstrap.min.css" rel="stylesheet"
			type="text/css" />
		<link href="css/login.css" rel="stylesheet" type="text/css" />
		<link href="css/plugins/font-awesome.min.css" rel="stylesheet"
			type="text/css" />
		<script src="js/plugins/jquery-1.10.2.min.js"></script>

		<script src="js/plugins/bootstrap.min.js" type="text/javascript"></script>
		<title>登录</title>
	</head>

	<body>
		<div class="wraper">
			<header role="banner" id="top"
				class="navbar navbar-static-top bs-docs-nav">
			<div class="container">
				<h2>
					<i class="fa fa-leaf"></i>桐庐富伟针织厂管理系统
					<small></small>
				</h2>
			</div>
			</header>
		</div>
		<div class="container">

			<div class="row body">
				<div class="col-md-6 png"></div>
				<div class="col-md-6">

					<div class="panel panel-default">

						<div class="panel-body">
							<form class="" role="form">
								<div class="form-group">
									<span class="error"></span>
								</div>
								<div class="form-group">
									<label for="username">
										用户名
									</label>

									<input type="text" class="form-control" id="username"
										placeholder="请输入用户名">

								</div>
								<div class="form-group">
									<label for="password">
										密码
									</label>
									<input type="password" class="form-control" id="password"
										placeholder="请输入密码">

								</div>
								<div class="form-group">
									<div class="checkbox">
										<label>
											<input type="checkbox">
											Remember me
										</label>
									</div>

								</div>
								<div class="form-group">
									<button id="loginbtn" type="submit" class="btn btn-lg btn-block btn-success">
										登录
									</button>
								</div>
							</form>

						</div>
						<div class="panel-footer">


							<abbr title="忘记密码？请联系管理员">忘记密码？</abbr>

						</div>
					</div>
				</div>


			</div>
		</div>

		</div>
		<div class="container">
			<div class="row">
				<p class="text-right">
					copy 2014 桐庐富伟针织厂
				</p>
			</div>
		</div>
	</body>

</html>