<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URLDecoder"%>
<%
String message = request.getParameter("message");
String referer =  request.getParameter("referer");
if(message == null){
	message = "";
}
String login_redirect_url = "message/unread";
if(referer != null && !referer.equals("")){
	login_redirect_url = referer;
}
 %>
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
			<div class="container">

				<div class="row">
					<div class="col-sm-offset-1 col-sm-4 png"></div>
					<div class="col-sm-offset-1 col-sm-5">

						<div class="panel panel-default">

							<div class="panel-body">
								<form id="loginform" role="form" >
									<div class="form-group">
										<span class="error"><%=message %></span>
									</div>
									<div class="form-group">
										<label for="username">
											用户名
										</label>

										<input type="text" class="form-control" id="username" name="username"
											placeholder="请输入用户名">

									</div>
									<div class="form-group">
										<label for="password">
											密码
										</label>
										<input type="password" class="form-control" id="password" name="password"
											placeholder="请输入密码">

									</div>
									<div class="form-group">
										<div class="checkbox">
											<label>
												<input type="checkbox">
												记住密码
											</label>
										</div>

									</div>
									<div class="form-group">
										<button id="loginbtn" type="submit"
											class="btn btn-lg btn-block btn-primary">
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
			<footer class="authenticated-footer">
			<div class="container">
				<hr>
				<div class="row">
					<ul class="list-inline footer text-center">
						<li>
							<a href="#">帮助</a>
						</li>
						<li>
							|
						</li>
						<li>
							<a href="#">联系我们</a>
						</li>
						<li>
							|
						</li>
						<li>
							<a href="#">诚聘英才</a>
						</li>
						<li>
							|
						</li>
						<li>
							<a href="#">联系合作</a>
						</li>
						<li>
							|
						</li>
						<li class="corp">
							<a href="#">© 2014 桐庐富伟针织厂. <span
								class="icn"></span> </a>
						</li>
					</ul>
				</div>
			</div>
			</footer>
		</div>

<script type="text/javascript">
	$("#loginform").submit(function(){
		var formdata = $(this).serialize();
			$.ajax({
                url: "user/login",
                type: 'POST',
             
                data: formdata,
            })
                .done(function(result) {
                	if(result.success){
                		window.location = "<%=login_redirect_url%>";
                	}else{
                		showError(result.message);
                	}
                })
                .fail(function(result) {
                    showError("请求服务器过程中出错:" + result.responseText);
                })
                .always(function() {});
		return false;
	});
	
	function showError(message){
//		$("#loginform .alert-danger").show();
		$("#loginform .error").text(message);
	}
	function hideError(){
//		$("#loginform .alert-danger").hide();
		$("#loginform .error").text("");
	}
</script>
	</body>

</html>