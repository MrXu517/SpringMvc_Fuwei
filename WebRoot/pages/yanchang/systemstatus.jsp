<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Material"%>
<%@page import="com.fuwei.entity.Employee"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="com.fuwei.commons.SystemSettings"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	List<Factory> yanchangFactorylist =  (List<Factory>)request.getAttribute("yanchangFactorylist");
	List<User> yanchangUserlist =  (List<User>)request.getAttribute("yanchangUserlist");
	String users_string = "";
	for(User item : yanchangUserlist){
		users_string += item.getName() + ",";
	}
	if(users_string.length()>0){
		users_string = users_string.substring(0,users_string.length()-1);
	}
	String factorys_string = "";
	for(Factory item : yanchangFactorylist){
		factorys_string += item.getName() + ",";
	}
	if(factorys_string.length()>0){
		factorys_string = factorys_string.substring(0,factorys_string.length()-1);
	}
	
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>当前验厂状态 -- 桐庐富伟针织厂</title>
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
		<script src="<%=basePath%>js/plugins/WdatePicker.js"></script>
		<script src="js/common/common.js" type="text/javascript"></script>
		<script src="js/plugins/jquery.form.js" type="text/javascript"></script>


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
							<i class=""></i>
							验厂
						</li>
						<li class="active">
							当前验厂状态
						</li>
					</ul>
				</div>
				<div class="body">

					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 formwidget">
								<p class="alert alert-info">请注意： 若您已开启了验厂状态，则系统所有帐号登录时看到的时间都是经过处理的时间，请及时提醒</p>	
								<fieldset>
									<legend>状态：
										<strong>
										<%if(SystemSettings.yanchang){ %>
										<span class="label label-danger"><%=SystemSettings.yanchangString() %></span>
										<%}else{ %>
										<%=SystemSettings.yanchangString() %>
										<%} %>
										</strong> </legend>
									<p class="alert alert-warning">若要改变验厂状态，请点击  <a target="_blank" href="systemsetting/set">此链接</a>   </p>	
								</fieldset>	
							
								<fieldset>
									<legend>当前开启验厂用户：
									<strong>
									<%=users_string%>
									</strong>
									</legend>
									<p class="alert alert-warning">若要修改验厂用户，请点击  <a target="_blank" href="user/list">此链接</a>   </p>		
								</fieldset>	

								<fieldset>
									<legend>当前可见的加工工厂：
									<strong>
									<%=factorys_string%>
									</strong>
									</legend>
									<p class="alert alert-warning">若要修改工厂，请点击  <a target="_blank" href="factory/index">此链接</a>   </p>		
								</fieldset>	
							</div>

						</div>
					</div>

				</div>
			</div>
		</div>	
<script type="text/javascript">
	/*设置当前选中的页*/
	var $a = $("#left li a[href='yanchang/systemstatus']");
	setActiveLeft($a.parent("li"));
	
	$(document).ready(function(){
		
	});
	
</script>
	</body>
</html>