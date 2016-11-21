<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Material"%>
<%@page import="com.fuwei.entity.Employee"%>
<%@page import="com.fuwei.entity.Factory"%>
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
		<title>KMART生产计划 -- 桐庐富伟针织厂</title>
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
							KMART生产计划
						</li>
					</ul>
				</div>
				<div class="body">

					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 formwidget">
								<fieldset>
									<legend>KMART车缝计划</legend>
										<form class="form-horizontal form" role="form"
											enctype="multipart/form-data" action="kmartyanchang/sewing_plan" method="post">
											<div class="col-md-12">
												<div class="form-group">
													<label for="file" class="col-sm-3 control-label">
														原始计划表
													</label>
													<div class="col-sm-8">
														<input type="file" name="file" id="file"
															class="form-control require" placeholder="请上传xls文件" />

													</div>
													<div class="col-sm-1"></div>
												</div>
												
											
												

												<div class="form-group">
													<div class="col-sm-offset-3 col-sm-5">
														<button type="submit" class="btn btn-primary"
															data-loading-text="正在加载...">
															获取车缝计划表
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
								</fieldset>	
							</div>

							<div class="col-md-12 formwidget">
								<fieldset>
									<legend>KMART机织计划</legend>
										<form class="form-horizontal leaveform" role="form"
											enctype="multipart/form-data" action="kmartyanchang/knitting_plan" method="post">
											<div class="col-md-12">
												<div class="form-group">
													<label for="file" class="col-sm-3 control-label">
														机织机器生产分布表
													</label>
													<div class="col-sm-8">
														<input type="file" name="file" id="knittingfile"
															class="form-control require" placeholder="请上传xls文件" />

													</div>
													<div class="col-sm-1"></div>
												</div>
											
												

												<div class="form-group">
													<div class="col-sm-offset-3 col-sm-5">
														<button type="submit" class="btn btn-primary"
															data-loading-text="正在加载...">
															获取KMART机织计划表
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
								</fieldset>	
							</div>
						</div>
					</div>

				</div>
			</div>
		</div>	
<script type="text/javascript">
	/*设置当前选中的页*/
	var $a = $("#left li a[href='kmartyanchang/plan']");
	setActiveLeft($a.parent("li"));
	
</script>
	</body>
</html>