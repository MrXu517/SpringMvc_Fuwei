<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.Sample"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	List<User> userlist = (List<User>) request.getAttribute("userlist");
	/*List<Company> companylist = (List<Company>) request
			.getAttribute("companylist");
	List<Salesman> salesmanlist = (List<Salesman>) request
			.getAttribute("salesmanlist");
	List<GongXu> gongxulist = (List<GongXu>) request
			.getAttribute("gongxulist");
	
	
	List<Role> rolelist = (List<Role>) request.getAttribute("rolelist");
	
	String tabname = (String) request.getParameter("tab");*/
	
	Sample sample = (Sample) request.getAttribute("sample");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>样品管理 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8" />
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">

		<script src="js/plugins/jquery-1.10.2.min.js"></script>
		<script src="js/common/common.js" type="text/javascript"></script>
		<script src="js/plugins/jquery.form.js" type="text/javascript"></script>
		<script src="js/sample/edit.js" type="text/javascript"></script>
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
						编辑样品属性
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
										修改样品
									</h3>
								</div>
								<div class="panel-body">

									<form class="form-horizontal sampleform" role="form"
										enctype="multipart/form-data">
										<div class="col-md-7">
											<input type="hidden" id="id" name="id"
												value="<%=sample.getId() %>" />
											<div class="form-group">
												<label for="productNumber" class="col-sm-3 control-label">
													产品编号
												</label>
												<div class="col-sm-8">
													<input type="text" class="form-control require"
														name="productNumber" id="productNumber" readonly placeholder="产品编号"
														value="<%=sample.getProductNumber() %>">
												</div>
												<div class="col-sm-1"></div>
											</div>
											<div class="form-group">
												<label for="name" class="col-sm-3 control-label">
													名称
												</label>
												<div class="col-sm-8">
													<input type="text" class="form-control require" name="name"
														id="name" placeholder="名称" value="<%=sample.getName() %>">
												</div>
												<div class="col-sm-1"></div>
											</div>
											<div class="form-group">
												<label for="file" class="col-sm-3 control-label">
													图片
												</label>
												<div class="col-sm-8">
													<input type="file" name="file" id="file" class=""
														placeholder="图片" />

												</div>
												<div class="col-sm-1"></div>
											</div>
											<div class="form-group">
												<label for="charge_user" class="col-sm-3 control-label">
													打样人
												</label>
												<div class="col-sm-8">
													<select value="<%=sample.getCharge_user() %>"
														class="form-control require" name="charge_user"
														id="charge_user">
														<%
															for (User item : userlist) {
															if(item.getId() == sample.getCharge_user()){
														%>
														<option value="<%=item.getId()%>" selected="selected"><%=item.getName()%></option>
														<%
														}else{
														%>
														<option value="<%=item.getId()%>"><%=item.getName()%></option>
														<%
															}
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
													<input type="text" class="form-control require"
														id="material" name="material" placeholder="材料"
														value="<%=sample.getMaterial() %>">
												</div>
												<div class="col-sm-1"></div>
											</div>
											<div class="form-group">
												<label for="weight" class="col-sm-3 control-label">
													克重
												</label>
												<div class="col-sm-8">
													<input type="text" class="form-control require"
														name="weight" id="weight" placeholder="克重"
														value="<%=sample.getWeight() %>">
												</div>
												<div class="col-sm-1"></div>
											</div>

											<div class="form-group">
												<label for="size" class="col-sm-3 control-label">
													尺寸
												</label>
												<div class="col-sm-8">
													<input type="text" class="form-control require" name="size"
														id="size" placeholder="尺寸" value="<%=sample.getSize() %>">
												</div>
												<div class="col-sm-1"></div>
											</div>

											<div class="form-group">
												<label for="machine" class="col-sm-3 control-label">
													机织
												</label>
												<div class="col-sm-8">
													<input type="text" class="form-control require"
														name="machine" id="machine" placeholder="机织"
														value="<%=sample.getMachine() %>">
												</div>
												<div class="col-sm-1"></div>
											</div>
											<div class="form-group">
												<label for="memo" class="col-sm-3 control-label">
													备注
												</label>
												<div class="col-sm-8">
													<input type="text" class="form-control require" name="memo"
														id="memo" placeholder="备注" value="<%=sample.getMemo() %>">
												</div>
												<div class="col-sm-1"></div>
											</div>
											<div class="form-group">
												<div class="col-sm-offset-3 col-sm-5">
													<button type="submit" class="btn btn-primary"
														data-loading-text="正在保存...">
														修改样品
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
										<div class="col-md-5" style="width: 400px;" id="previewWidget">
											<a href="#" class="thumbnail"> <img id="previewImg"
													alt="400 x 100%" src="<%=sample.getImg() %>"> </a>
										</div>
									</form>
								</div>

							</div>
						</div>


					</div>
				</div>

			</div>
		</div>

	</body>
</html>