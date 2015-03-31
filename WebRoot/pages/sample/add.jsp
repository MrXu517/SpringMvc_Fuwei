<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Material" %>
<%@page import="com.fuwei.entity.Factory" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	List<User> userlist = SystemCache.userlist;
	List<Material> materiallist = SystemCache.materiallist;
	List<Factory> factorylist = SystemCache.produce_factorylist;
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
		<script src="js/common/common.js" type="text/javascript"></script>
		<script src="js/plugins/jquery.form.js" type="text/javascript"></script>
		<script src="js/sample/add.js" type="text/javascript"></script>
		<style type="text/css">
.sampleform input[type='file'] {
	outline: none !important;
}
</style>
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
							<a href="sample/index">样品管理</a>
						</li>
						<li class="active">
							创建样品
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
											添加样品
										</h3>
									</div>
									<div class="panel-body">

										<form class="form-horizontal sampleform" role="form"
											enctype="multipart/form-data">
											<div class="col-md-7">
												<div class="form-group">
													<label for="productNumber" class="col-sm-3 control-label">
														产品编号
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control"
															name="productNumber" id="productNumber" readonly
															value="自动生成" placeholder="产品编号">
													</div>
													<div class="col-sm-1"></div>
												</div>
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
													<label for="file" class="col-sm-3 control-label">
														图片
													</label>
													<div class="col-sm-8">
														<input type="file" name="file" id="file"
															class="form-control require" placeholder="图片" />

													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="charge_user" class="col-sm-3 control-label">
														打样人
													</label>
													<div class="col-sm-8">
														<select class="form-control require" name="charge_user"
															id="charge_user">
															<%
																for (User item : userlist) {
															%>
															<option value="<%=item.getId()%>"><%=item.getName()%></option>
															<%
																}
															%>
														</select>
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="material" class="col-sm-3 control-label">
														材料
													</label>
													<div class="col-sm-8">
														<select class="form-control require" name="materialId"
															id="materialId">
															<%
																for (Material material : materiallist) {
															%>
															<option value="<%=material.getId()%>"><%=material.getName()%></option>
															<%
																}
															%>
														</select>
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="weight" class="col-sm-3 control-label">
														克重
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control require double"
															name="weight" id="weight" placeholder="克重">
													</div>
													<div class="col-sm-1"></div>
												</div>

												<div class="form-group">
													<label for="size" class="col-sm-3 control-label">
														尺寸
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control require"
															name="size" id="size" placeholder="尺寸">
													</div>
													<div class="col-sm-1"></div>
												</div>

												<div class="form-group">
													<label for="machine" class="col-sm-3 control-label">
														机织
													</label>
													<div class="col-sm-8">
														<select class="form-control" name="factoryId"
															id="factoryId">
															<option value="">未选择</option>
															<%
																for (Factory factory : factorylist) {
															%>
															<option value="<%=factory.getId()%>"><%=factory.getName()%></option>
															<%
																}
															%>
														</select>
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="memo" class="col-sm-3 control-label">
														备注
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control" name="memo"
															id="memo" placeholder="备注">
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<div class="col-sm-offset-3 col-sm-5">
														<button type="submit" class="btn btn-primary"
															data-loading-text="正在保存...">
															添加样品
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
											<div class="col-md-5" style="width: 400px;"
												id="previewWidget">
												<a href="#" class="thumbnail"> <img id="previewImg"
														alt="400 x 100%"> </a>
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