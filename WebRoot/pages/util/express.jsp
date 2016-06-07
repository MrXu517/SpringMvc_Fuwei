<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.Customer"%>
<%@page import="com.fuwei.commons.SystemCache"%>
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
		<title>打印快递单-- 桐庐富伟针织厂</title>
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
		
		<script src="js/plugins/jquery-barcode.min.js"></script>
	
		
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
						<li class="active">
							打印快递单
						</li>
					</ul>
				</div>
				<div class="body">
					<div id="customer">
						<div class="container-fluid">
							<div class="row">
								<div id="formwidget" class="col-sm-12">
									<div class="panel panel-primary">
										<div class="panel-heading">
											<h3 class="panel-title">
												设置装箱资料
											</h3>
										</div>
										<div class="panel-body">
											<form class="form-horizontal" role="form" method="post">
												<input type="hidden" name="id" id="id" />
												<div class="form-group">
													<label for="destination"
														class="col-sm-3 control-label">
														目的地
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control require"
															name="destination" id="destination"
															placeholder="目的地">
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="company_name"
														class="col-sm-3 control-label">
														单位名称
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control require"
															name="company_name" id="company_name"
															placeholder="单位名称">
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="province"
														class="col-sm-3 control-label">
														收件地址-省
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control require"
															name="province" id="province"
															placeholder="收件地址-省">
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="city"
														class="col-sm-3 control-label">
														市
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control require"
															name="city" id="city"
															placeholder="市">
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="district"
														class="col-sm-3 control-label">
														区
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control require"
															name="district" id="district"
															placeholder="区">
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="address"
														class="col-sm-3 control-label">
														详细地址
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control require"
															name="address" id="address"
															placeholder="详细地址">
													</div>
													<div class="col-sm-1"></div>
												</div>

												<div class="form-group">
													<div class="col-sm-offset-3 col-sm-5">
														<button type="submit" class="btn btn-primary"
															data-loading-text="正在保存...">
															生成
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

							
							</div>
						</div>

					</div>
				</div>
			</div>

			<div class="id_barcode"></div>
			<script type="text/javascript">
				/* 设置当前选中的页 */
				var $a = $("#left li a[href='util/express']");
				setActiveLeft($a.parent("li"));
				/* 设置当前选中的页 */
				
				
			</script>
	</body>
</html>