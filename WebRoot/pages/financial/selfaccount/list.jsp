<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.financial.SelfAccount"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	List<SelfAccount> selfaccountlist = (List<SelfAccount>) request
			.getAttribute("selfaccountlist");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>本厂收支帐号 -- 桐庐富伟针织厂</title>
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


		<link href="css/financial/bank.css" rel="stylesheet" type="text/css" />
		<script src="js/financial/selfaccount.js" type="text/javascript"></script>

	</head>
	<body>
		<%@ include file="../../common/head.jsp"%>
		<div id="Content">
			<div id="main">
				<div class="breadcrumbs" id="breadcrumbs">
					<ul class="breadcrumb">
						<li>
							<i class="fa fa-home"></i>
							<a href="user/index">首页</a>
						</li>
						<li>
							财务
						</li>
						<li class="active">
							本厂收支帐号
						</li>
					</ul>
				</div>
				<div class="body">
					<div id="bank">
						<div class="container-fluid">
							<div class="row">
								<div class="col-md-4 formwidget">
									<div class="panel panel-primary">
										<div class="panel-heading">
											<h3 class="panel-title">
												添加本厂收支帐号
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
													<label for="address" class="col-sm-3 control-label">
														开户行
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control require"
															name="bank_name" id="bank_name" placeholder="">
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="address" class="col-sm-3 control-label">
														帐号
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control require"
															name="bank_no" id="bank_no" placeholder="">
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="ispublic" class="col-sm-3 control-label">
														是否公帐
													</label>
													<div class="col-sm-8">
														<select class="form-control require" name="ispublic" id="ispublic">	
															<option value="false">
																否
															</option>
															<option value="true">
																是
															</option>
														</select>
													</div>
													<div class="col-sm-1"></div>
												</div>

												<div class="form-group">
													<div class="col-sm-offset-3 col-sm-5">
														<button type="submit" class="btn btn-primary"
															data-loading-text="正在保存...">
															添加账号
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
											本厂收支帐号列表
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
														银行
													</th>
													<th>
														帐号
													</th>
													
													<th>
														操作
													</th>
												</tr>
											</thead>
											<tbody>
												<%
													int f_i = 1;
													for (SelfAccount item : selfaccountlist) {
												%>
												<tr>
													<td><%=f_i%></td>
													<td><%=item.getName()%><%if(item.getIspublic()){%><span class="label label-success">公帐账号<%} %></td>
													<td><%=item.getBank_name()%></td>
													<td><%=item.getBank_no()%></td>
													<td>
														<a class="edit" href="#"
															data-cid="<%=item.getId()%>">编辑</a>
														| <a class="delete" href="#"
															data-cid="<%=item.getId()%>">删除</a>
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