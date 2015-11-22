<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.entity.Role"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	List<User> userlist = (List<User>) request.getAttribute("userlist");

	List<Role> rolelist = (List<Role>) request.getAttribute("rolelist");
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
		<script src="js/systeminfo/user.js" type="text/javascript"></script>

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
							用户管理
						</li>
					</ul>
				</div>
				<div class="body">
					<div id="users">
						<div class="container-fluid">
							<div class="row">
								<div class="col-md-4 formwidget">
									<div class="panel panel-primary">
										<div class="panel-heading">
											<h3 class="panel-title">
												添加用户
											</h3>
										</div>
										<div class="panel-body">
											<form class="form-horizontal" role="form">
												<input type="hidden" name="id" id="id" />

												<div class="form-group">
													<label for="roleId" class="col-sm-3 control-label">
														角色
													</label>
													<div class="col-sm-8">
														<select class="form-control require" id="roleId"
															name="roleId">
															<%
																for (Role role : rolelist) {
															%>
															<option value="<%=role.getId()%>"><%=role.getDecription()%></option>
															<%
																}
															%>
														</select>
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="username" class="col-sm-3 control-label">
														用户名
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control require"
															name="username" id="username" placeholder="用户名必须唯一">
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="name" class="col-sm-3 control-label">
														姓名
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control require"
															name="name" id="name" placeholder="姓名">
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="tel" class="col-sm-3 control-label">
														电话
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control" id="tel"
															name="tel" placeholder="电话，手机">
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="email" class="col-sm-3 control-label">
														邮箱
													</label>
													<div class="col-sm-8">
														<input type="Email" class="form-control"
															id="email" name="email" placeholder="邮箱">
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="qq" class="col-sm-3 control-label">
														QQ
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control" id="qq"
															name="qq" placeholder="QQ">
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<div class="col-sm-offset-3 col-sm-5">
														<button type="submit" class="btn btn-primary"
															data-loading-text="正在保存...">
															添加用户
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
											系统用户列表
										</div>

										<!-- Table -->
										<table class="table table-responsive">
											<thead>
												<tr>
													<th>
														序号
													</th>
													<th>
														用户名
													</th>
													<th>
														姓名
													</th>
													<th>
														拼音
													</th>
													<th>
														角色
													</th>
													<th>
														操作
													</th>
												</tr>
											</thead>
											<tbody>
												<%
													int u_i = 1;
													for (User i_user : userlist) {
												%>
												<tr>
													<td><%=u_i%></td>
													<td><%=i_user.getUsername() %></td>
													<td><%=i_user.getName()%></td>
													<td><%=i_user.getHelp_code()%></td>
													<td><%=i_user.getRoleId() == null ? "" : SystemCache
						.getRole(i_user.getRoleId()).getDecription()%></td>
													<td>
														<a class="editUser" href="#"
															data-cid="<%=i_user.getId()%>">编辑</a> |
														<a class="deleteUser" href="#"
															data-cid="<%=i_user.getId()%>">删除</a> |
														<%
															if (i_user.getInUse()) {
														%>
														<a class="cancelUser" href="#"
															data-cid="<%=i_user.getId()%>">注销</a>
														<%
															} else {
														%>
														<a class="enableUser" href="#"
															data-cid="<%=i_user.getId()%>">启用</a>
														<%
															}
														%>
													</td>
												</tr>
												<%
													u_i++;
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