<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	List<Factory> factorylist = (List<Factory>) request
			.getAttribute("factorylist");

	String tabname = (String) request.getParameter("tab");
	String typeStr = request.getParameter("type");
	Integer type = null;
	if(typeStr!=null && !typeStr.equals("")){
		type =Integer.parseInt(typeStr);
	}
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>基础资料 -- 桐庐富伟针织厂</title>
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


		<link href="css/systeminfo.css" rel="stylesheet" type="text/css" />
		<script src="js/systeminfo/factory.js" type="text/javascript"></script>

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
							基础资料
						</li>
						<li class="active">
							工厂管理
						</li>
					</ul>
				</div>
				<div class="body">
					<div id="factory">
						<div class="container-fluid">
							<div class="row">
								<div class="col-md-4 formwidget">
									<div class="panel panel-primary">
										<div class="panel-heading">
											<h3 class="panel-title">
												添加工厂
											</h3>
										</div>
										<div class="panel-body">
											<form class="form-horizontal" role="form">
												<input type="hidden" name="id" id="id" />
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
													<label for="name" class="col-sm-3 control-label">
														类型
													</label>
													<div class="col-sm-8">
														<select class="form-control require" name="type" id="type">	
															<option value="0">
																机织、加工
															</option>
															<option value="1">
																原材料采购
															</option>
															<option value="2">
																染色
															</option>
															<option value="3">
																辅料采购
															</option>
														</select>
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="address" class="col-sm-3 control-label">
														地址
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control require"
															name="address" id="address" placeholder="地址">
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="isyanchang" class="col-sm-3 control-label">
														验厂状态
													</label>
													<div class="col-sm-8">
														<select class="form-control require" name="isyanchang" id="isyanchang">	
															<option value="false">
																不可见
															</option>
															<option value="true">
																可见
															</option>
														</select>
													</div>
													<div class="col-sm-1"></div>
												</div>

												<div class="form-group">
													<div class="col-sm-offset-3 col-sm-5">
														<button type="submit" class="btn btn-primary"
															data-loading-text="正在保存...">
															添加工厂
														</button>
														<a href="#" class="switch_add">添加</a>
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

								<div class="col-md-7 tablewidget">
									<div class="panel panel-primary">
										<!-- Default panel contents -->
										<div class="panel-heading">
											工厂列表
										</div>
										<form class="form-horizontal" role="form" id="filterform" action="factory/index">
											<div class="form-group col-sm-6">
												<label for="name" class="col-sm-3 control-label">
													类型
												</label>
												<div class="col-sm-8">
													<select class="form-control require" name="type" id="type">
														<option value="">
															所有
														</option>
														<%if(type!=null && type == 0){ %>
															<option value="0" selected>
																机织、加工
															</option>
														<%}else{ %>
															<option value="0">
																机织、加工
															</option>
														<%} %>
														<%if(type!=null && type == 1){ %>
															<option value="1" selected>
																原材料采购
															</option>
														<%}else{ %>
															<option value="1">
																原材料采购
															</option>
														<%} %>
														<%if(type!=null && type == 2){ %>
															<option value="2" selected>
																染色
															</option>
														<%}else{ %>
															<option value="2">
																染色
															</option>
														<%} %>
														<%if(type!=null && type == 3){ %>
															<option value="3" selected>
																辅料采购
															</option>
														<%}else{ %>
															<option value="3">
																辅料采购
															</option>
														<%} %>
													</select>
												</div>
												<div class="col-sm-1"></div>
											</div>


											<div class="clear"></div>
										</form>
										<!-- Table -->
										<table class="table table-responsive">
											<thead>
												<tr>
													<th>
														序号
													</th>
													<th>
														名称
													</th>
													<th>
														类型
													</th>
													<th>
														验厂状态
													</th>
													<th>
														操作
													</th>
												</tr>
											</thead>
											<tbody>
												<%
													int f_i = 1;
													for (Factory factory : factorylist) {
												%>
												<tr>
													<td><%=f_i%></td>
													<td><%=factory.getName()%></td>
													<td><%=factory.getTypeName()%></td>
													<td><%if(factory.getIsyanchang()){%>	
													<span class="label label-success">验厂时可见</span>
													<%}%>
													</td>
													<td>
														<a class="editFactory" href="#"
															data-cid="<%=factory.getId()%>">编辑</a>
														<!-- | <a class="deleteFactory" href="#"
															data-cid="<%=factory.getId()%>">删除</a> -->
													</td>
												</tr>
												<%
													f_i++;
													}
												%>
											</tbody>
										</table>
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