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
		<title>添加样品 -- 桐庐富伟针织厂</title>
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
							<a href="sample/index">验厂</a>
						</li>
						<li class="active">
							计时工资表
						</li>
					</ul>
				</div>
				<div class="body">

					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 formwidget">
								<fieldset>
									<legend>月工资表</legend>
										<form class="form-horizontal form" role="form"
											enctype="multipart/form-data" action="yanchang/fake_salarys" method="post">
											<div class="col-md-12">
												<div class="form-group">
													<label for="year_month" class="col-sm-3 control-label">
														考勤时间
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control date"
															name="year_month" id="year_month" >
													</div>
													<div class="col-sm-1"></div>
												</div>
												
												<div class="form-group">
													<label for="file" class="col-sm-3 control-label">
														月考勤统计表
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
															获取工资表
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
									<legend>离职工资表</legend>
										<form class="form-horizontal leaveform" role="form"
											enctype="multipart/form-data" action="yanchang/fake_salarys_leave" method="post">
											<div class="col-md-12">
												<div class="form-group">
													<label for="year_month" class="col-sm-3 control-label">
														考勤时间
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control date"
															name="year_month" id="year_month" >
													</div>
													<div class="col-sm-1"></div>
												</div>
												
												<div class="form-group">
													<label for="file" class="col-sm-3 control-label">
														当月考勤统计表
													</label>
													<div class="col-sm-8">
														<input type="file" name="file" id="file"
															class="form-control require" placeholder="请上传xls文件" />

													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="file" class="col-sm-3 control-label">
														上个月考勤统计表
													</label>
													<div class="col-sm-8">
														<input type="file" name="ealier_month" id="ealier_month"
															class="form-control require" placeholder="请上传xls文件" />

													</div>
													<div class="col-sm-1"></div>
												</div>
											
												

												<div class="form-group">
													<div class="col-sm-offset-3 col-sm-5">
														<button type="submit" class="btn btn-primary"
															data-loading-text="正在加载...">
															获取离职工资表
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
	var $a = $("#left li a[href='yanchang/yan_salarys']");
	setActiveLeft($a.parent("li"));
	
	$(document).ready(function(){
	$(".form #year_month,.leaveform #year_month").unbind("focus click");
	$(".form #year_month,.leaveform #year_month").bind("focus click",function(){
		WdatePicker({dateFmt:"yyyy/MM"});
	});
		
	});
	
</script>
	</body>
</html>