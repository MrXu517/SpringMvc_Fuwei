<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.entity.GongXu"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.entity.Role"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	List<Company> companylist = (List<Company>) request
			.getAttribute("companylist");
	List<Salesman> salesmanlist = (List<Salesman>) request
			.getAttribute("salesmanlist");
	List<GongXu> gongxulist = (List<GongXu>) request
			.getAttribute("gongxulist");
	List<User> userlist = (List<User>) request.getAttribute("userlist");
	
	List<Role> rolelist = (List<Role>) request.getAttribute("rolelist");
	
	String tabname = (String) request.getParameter("tab");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>系统信息管理 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8" />
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">
		<link href="css/systeminfo.css" rel="stylesheet" type="text/css" />
		<script src="js/plugins/jquery-1.10.2.min.js"></script>
		<script src="js/common/common.js" type="text/javascript"></script>
		<script src="js/systeminfo/index.js" type="text/javascript"></script>

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
						系统信息管理
					</li>
				</ul>
			</div>
			<div class="body">
				<div id="tab">
					<ul class="nav nav-tabs" role="tablist">
						<li class="active">
							<a href="#companys" role="tab" data-toggle="tab">公司</a>
						</li>

						<li>
							<a href="#salesmans" role="tab" data-toggle="tab">业务员</a>
						</li>

						<li>

							<a href="#users" role="tab" data-toggle="tab">系统用户</a>
						</li>
						<li>
							<a href="#gongxus" role="tab" data-toggle="tab">工序</a>
						</li>
					</ul>


					<div class="tab-content">
						<div class="tab-pane active" id="companys">
							<div class="container-fluid">
								<div class="row">
									<div class="col-md-4 formwidget">
										<div class="panel panel-primary">
											<div class="panel-heading">
												<h3 class="panel-title">
													添加公司
												</h3>
											</div>
											<div class="panel-body">
												<form class="form-horizontal" role="form">
													<input type="hidden" name="id" id="id" />
													<div class="form-group">
														<label for="fullname" class="col-sm-3 control-label">
															全称
														</label>
														<div class="col-sm-8">
															<input type="text" class="form-control require" name="fullname"
																id="fullname" placeholder="全称">
														</div>
														<div class="col-sm-1"></div>
													</div>
													<div class="form-group">
														<label for="shortname" class="col-sm-3 control-label">
															简称
														</label>
														<div class="col-sm-8">
															<input type="text" class="form-control require"
																id="shortname" name="shortname" placeholder="简称">
														</div>
														<div class="col-sm-1"></div>
													</div>
													<div class="form-group">
														<label for="address" class="col-sm-3 control-label">
															公司地址
														</label>
														<div class="col-sm-8">
															<input type="text" class="form-control require" name="address"
																id="address" placeholder="公司地址">
														</div>
														<div class="col-sm-1"></div>
													</div>

													<div class="form-group">
														<label for="city" class="col-sm-3 control-label">
															所在城市
														</label>
														<div class="col-sm-8">
															<input type="text" class="form-control require" name="city"
																id="city" placeholder="所在城市">
														</div>
														<div class="col-sm-1"></div>
													</div>

													<div class="form-group">
														<div class="col-sm-offset-3 col-sm-5">
															<button type="submit" class="btn btn-primary"
																data-loading-text="正在保存...">
																添加公司
															</button>
															<a href="#" class="switch_add">添加公司</a>
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
												公司列表
											</div>

											<!-- Table -->
											<table class="table table-responsive">
												<thead>
													<tr>
														<th>
															序号
														</th>
														<th>
															公司名称
														</th>
														<th>
															简称
														</th>
														<th>
														 拼音
														</th>
														<th>
															操作
														</th>
													</tr>
												</thead>
												<tbody>
													<%
														int c_i = 1;
														for (Company company : companylist) {
													%>
													<tr>
														<td><%=c_i%></td>
														<td><%=company.getFullname()%></td>
														<td><%=company.getShortname()%></td>
														<td><%=company.getHelp_code()%></td>
														<td>
															<a class="editcompany" href="#"
																data-cid="<%=company.getId()%>">编辑</a> |
															<a class="deletecompany" href="#"
																data-cid="<%=company.getId()%>">删除</a>
														</td>
													</tr>
													<%
														c_i++;
														}
													%>
												</tbody>
											</table>
										</div>
									</div>
								</div>
							</div>

						</div>
						<div class="tab-pane" id="salesmans">
							<div class="container-fluid">
								<div class="row">
									<div class="col-md-4 formwidget">
										<div class="panel panel-primary">
											<div class="panel-heading">
												<h3 class="panel-title">
													添加业务员
												</h3>
											</div>
											<div class="panel-body">
												<form class="form-horizontal" role="form">
													<input type="hidden" name="id" id="id" />
													<div class="form-group">
														<label for="companyId" class="col-sm-3 control-label">
															公司
														</label>
														<div class="col-sm-8">
															<select class="form-control require" name="companyId"
																id="companyId">
																<%
for(Company company : companylist){
 %>
																<option value="<%=company.getId() %>"><%=company.getFullname() %></option>
																<%
}
 %>
															</select>
														</div>
														<div class="col-sm-1"></div>
													</div>
													<div class="form-group">
														<label for="name" class="col-sm-3 control-label">
															姓名
														</label>
														<div class="col-sm-8">
															<input type="text" class="form-control require" name="name"
																id="name" placeholder="名称">
														</div>
														<div class="col-sm-1"></div>
													</div>
													
													<div class="form-group">
														<label for="tel" class="col-sm-3 control-label">
															电话
														</label>
														<div class="col-sm-8">
															<input type="text" class="form-control require" id="tel"
																name="tel" placeholder="电话，手机">
														</div>
														<div class="col-sm-1"></div>
													</div>

													<div class="form-group">
														<div class="col-sm-offset-3 col-sm-5">
															<button type="submit" class="btn btn-primary"
																data-loading-text="正在保存...">
																添加业务员
															</button>
															<a href="#" class="switch_add">添加业务员</a>
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
												业务员列表
											</div>

											<!-- Table -->
											<table class="table table-responsive">
												<thead>
													<tr>
														<th>
															序号
														</th>
														<th>
															姓名
														</th>
														<th>
															拼音
														</th>
														<th style="display:none;">
															创建人
														</th>
														<th>
															操作
														</th>
													</tr>
												</thead>
												<tbody>
													<%
														int s_i = 1;
														for (Salesman salesman : salesmanlist) {
													%>
													<tr>
														<td><%=s_i%></td>
														<td><%=salesman.getName()%></td>
														<td><%=salesman.getHelp_code()%></td>
														<td style="display:none;"><%=SystemCache.getUserName(salesman.getCreated_user()) %></td>
														<td>
															<a class="editSalesman" href="#"
																data-cid="<%=salesman.getId()%>">编辑</a> |
															<a class="deleteSalesman" href="#"
																data-cid="<%=salesman.getId()%>">删除</a>
														</td>
													</tr>
													<%
														s_i++;
														}
													%>
												</tbody>
											</table>
										</div>
									</div>
								</div>
							</div>

						</div>
						<div class="tab-pane" id="users">
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
															<select class="form-control require" id="roleId" name="roleId">
<%
for(Role role : rolelist){
 %>
<option value="<%=role.getId() %>"><%=role.getDecription() %></option>
<%
} %>
</select>
														</div>
														<div class="col-sm-1"></div>
													</div>
													<div class="form-group">
														<label for="username" class="col-sm-3 control-label">
															用户名
														</label>
														<div class="col-sm-8">
															<input type="text" class="form-control require" name="username"
																id="username" placeholder="用户名">
														</div>
														<div class="col-sm-1"></div>
													</div>
													<div class="form-group">
														<label for="name" class="col-sm-3 control-label">
															姓名
														</label>
														<div class="col-sm-8">
															<input type="text" class="form-control require" name="name"
																id="name" placeholder="姓名">
														</div>
														<div class="col-sm-1"></div>
													</div>
													<div class="form-group">
														<label for="tel" class="col-sm-3 control-label">
															电话
														</label>
														<div class="col-sm-8">
															<input type="text" class="form-control require" id="tel"
																name="tel" placeholder="电话，手机">
														</div>
														<div class="col-sm-1"></div>
													</div>
<div class="form-group">
														<label for="email" class="col-sm-3 control-label">
															邮箱
														</label>
														<div class="col-sm-8">
															<input type="Email" class="form-control require" id="email"
																name="email" placeholder="邮箱">
														</div>
														<div class="col-sm-1"></div>
													</div>
<div class="form-group">
														<label for="qq" class="col-sm-3 control-label">
															QQ
														</label>
														<div class="col-sm-8">
															<input type="text" class="form-control require" id="qq"
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
															<a href="#" class="switch_add">添加用户</a>
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
															姓名
														</th>
														<th>
															拼音
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
														<td><%=i_user.getName()%></td>
														<td><%=i_user.getHelp_code()%></td>
														<td>
															<a class="editUser" href="#"
																data-cid="<%=i_user.getId()%>">编辑</a> |
															<a class="deleteUser" href="#"
																data-cid="<%=i_user.getId()%>">删除</a> |
															<%
															if(i_user.getInUse()){
															 %>
															<a class="cancelUser" href="#"
																data-cid="<%=i_user.getId()%>">注销</a>
															<%} else{
															%>
															<a class="enableUser" href="#"
																data-cid="<%=i_user.getId()%>">启用</a>
															<%}
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
						<div class="tab-pane" id="gongxus">
							<div class="container-fluid">
								<div class="row">
									<div class="col-md-4 formwidget">
										<div class="panel panel-primary">
											<div class="panel-heading">
												<h3 class="panel-title">
													添加工序
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
															<input type="text" class="form-control require" name="name"
																id="name" placeholder="名称">
														</div>
														<div class="col-sm-1"></div>
													</div>
													
													<div class="form-group">
														<div class="col-sm-offset-3 col-sm-5">
															<button type="submit" class="btn btn-primary"
																data-loading-text="正在保存...">
																添加工序
															</button>
															<a href="#" class="switch_add">添加工序</a>
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
												工序列表
											</div>

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
															操作
														</th>
													</tr>
												</thead>
												<tbody>
													<%
														int g_i = 1;
														for (GongXu gongxu : gongxulist) {
													%>
													<tr>
														<td><%=g_i%></td>
														<td><%=gongxu.getName()%></td>
														<td>
															<a class="editGongxu" href="#"
																data-cid="<%=gongxu.getId()%>">编辑</a> |
															<a class="deleteGongxu" href="#"
																data-cid="<%=gongxu.getId()%>">删除</a>
														</td>
													</tr>
													<%
														g_i++;
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
				<!-- Nav tabs -->

			</div>
		</div>

	</body>
</html>
<script type="text/javascript">
	var tabname = "<%=tabname %>";
	if (tabname == null || tabname == undefined) {
		$('#tab a:first').tab('show') // Select first tab
	}
	$("#tab a[href='#" + tabname + "']").tab('show') // Select tab by name
</script>