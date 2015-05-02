<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.Department"%>
<%@page import="com.fuwei.entity.Employee"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.DateTool"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	List<Department> departmentlist = (List<Department>) request
			.getAttribute("departmentlist");
	List<Employee> employeelist = (List<Employee>) request
			.getAttribute("employeelist");
	String tabname = (String) request.getParameter("tab");
	String departmentIdStr = request.getParameter("departmentId");
	Integer departmentId = null;
	if (departmentIdStr != null && !departmentIdStr.equals("")) {
		departmentId = Integer.parseInt(departmentIdStr);
	}
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>部门和员工 -- 桐庐富伟针织厂</title>
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
		<script src="<%=basePath%>js/plugins/WdatePicker.js"></script>
	
		<link href="css/systeminfo.css" rel="stylesheet" type="text/css" />
		<script src="js/systeminfo/employee.js" type="text/javascript"></script>
		
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
							系统信息管理
						</li>
						<li class="active">
							部门、员工管理
						</li>
					</ul>
				</div>
				<div class="body">
					<div id="tab">
						<ul class="nav nav-tabs" role="tablist">
							<li class="active">
								<a href="#departments" role="tab" data-toggle="tab">部门</a>
							</li>

							<li>
								<a href="#employees" role="tab" data-toggle="tab">员工</a>
							</li>
						</ul>


						<div class="tab-content">
							<div class="tab-pane active" id="departments">
								<div class="container-fluid">
									<div class="row">
										<div class="col-md-4 formwidget">
											<div class="panel panel-primary">
												<div class="panel-heading">
													<h3 class="panel-title">
														添加部门
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
															<div class="col-sm-offset-3 col-sm-5">
																<button type="submit" class="btn btn-primary"
																	data-loading-text="正在保存...">
																	添加部门
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
													部门列表
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
																最近更新时间
															</th>
															<th>
																操作
															</th>
														</tr>
													</thead>
													<tbody>
														<%
															int c_i = 1;
															for (Department department : departmentlist) {
														%>
														<tr>
															<td><%=c_i%></td>
															<td><%=department.getName()%></td>
															<td><%=DateTool.formatDateYMD(department.getUpdated_at())%></td>
															<td>
																<a class="edit" href="#"
																	data-cid="<%=department.getId()%>">编辑</a> |
																<a class="delete" href="#"
																	data-cid="<%=department.getId()%>">删除</a>
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
							<div class="tab-pane" id="employees">
								<div class="container-fluid">
									<div class="row">
										<div class="col-md-4 formwidget">
											<div class="panel panel-primary">
												<div class="panel-heading">
													<h3 class="panel-title">
														添加员工
													</h3>
												</div>
												<div class="panel-body">
													<form class="form-horizontal" role="form">
														<input type="hidden" name="id" id="id" />
														
														<div class="form-group">
															<label for="number" class="col-sm-3 control-label">
																员工编号
															</label>
															<div class="col-sm-8">
																<input type="text" class="form-control require"
																	name="number" id="number" placeholder="员工编号">
															</div>
															<div class="col-sm-1"></div>
														</div>
														<div class="form-group" >
															<label for="name" class="col-sm-3 control-label">
																姓名
															</label>
															<div class="col-sm-8">
																<input type="text" class="form-control require"
																	name="name" id="name" placeholder="名称">
															</div>
															<div class="col-sm-1"></div>
														</div>
														<div class="form-group">
															<label for="sex" class="col-sm-3 control-label">
																性别
															</label>
															<div class="col-sm-8">
																<input type="radio" id="sex_0"
																	name="sex" value="男" checked>男
																<input type="radio" id="sex_1"
																	name="sex" value="女">女
															</div>
															<div class="col-sm-1"></div>
														</div>
														<div class="form-group">
															<label for="birthday" class="col-sm-3 control-label">
																出生年月
															</label>
															<div class="col-sm-8">
																<input type="text" class="form-control require date"
																	name="birthday" id="birthday" placeholder="">
															</div>
															<div class="col-sm-1"></div>
														</div>
														<div class="form-group">
															<label for="enter_at" class="col-sm-3 control-label">
																入厂日期
															</label>
															<div class="col-sm-8">
																<input type="text" class="form-control require date"
																	name="enter_at" id="enter_at" placeholder="">
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
															<label for="address_home" class="col-sm-3 control-label">
																家庭地址
															</label>
															<div class="col-sm-8">
																<input type="text" class="form-control" id="address_home"
																	name="address_home" placeholder="">
															</div>
															<div class="col-sm-1"></div>
														</div>
														<div class="form-group">
															<label for="id_card" class="col-sm-3 control-label">
																身份证
															</label>
															<div class="col-sm-8">
																<input type="text" class="form-control require" id="id_card"
																	name="id_card" placeholder="">
															</div>
															<div class="col-sm-1"></div>
														</div>
														<div class="form-group">
															<label for="departmentId" class="col-sm-3 control-label">
																部门
															</label>
															<div class="col-sm-8">
																<select class="form-control require" name="departmentId"
																	id="departmentId">
																	<%
																		for (Department d : departmentlist) {
																	%>
																	<option value="<%=d.getId()%>"><%=d.getName()%></option>
																	<%
																		}
																	%>
																</select>
															</div>
															<div class="col-sm-1"></div>
														</div>
														<div class="form-group">
															<label for="job" class="col-sm-3 control-label">
																岗位
															</label>
															<div class="col-sm-8">
																<input type="text" class="form-control require" id="job"
																	name="job" placeholder="">
															</div>
															<div class="col-sm-1"></div>
														</div>
														
														
														<div class="form-group">
															<label for="address" class="col-sm-3 control-label">
																现居住地
															</label>
															<div class="col-sm-8">
																<input type="text" class="form-control" id="address"
																	name="address" placeholder="">
															</div>
															<div class="col-sm-1"></div>
														</div>
														
														<div class="form-group">
															<label for="agreement_at" class="col-sm-3 control-label">
																合同期限
															</label>
															<div class="col-sm-8">
																<input type="text" class="form-control date"
																	name="agreement_at" id="agreement_at" placeholder="">  到
																<input type="text" class="form-control date"
																	name="agreement_end" id="agreement_end" placeholder="">
															</div>
															<div class="col-sm-1"></div>
														</div>
														<div class="form-group">
															<label for="employee_type" class="col-sm-3 control-label">
																用工形式
															</label>
															<div class="col-sm-8">
																<input type="radio" id="employee_type_0"
																	name="employee_type" value="合同工" checked>合同工
																<input type="radio" id="employee_type_1"
																	name="employee_type" value="其他">其他
															</div>
															<div class="col-sm-1"></div>
														</div>
														<div class="form-group">
															<label for="year_salary" class="col-sm-3 control-label">
																年薪
															</label>
															<div class="col-sm-8">
																<input type="text" class="form-control" id="year_salary"
																	name="year_salary" placeholder="">
															</div>
															<div class="col-sm-1"></div>
														</div>
														<div class="form-group">
															<label for="hour_salary" class="col-sm-3 control-label">
																时薪
															</label>
															<div class="col-sm-8">
																<input type="text" class="form-control" id="hour_salary"
																	name="hour_salary" placeholder="">
															</div>
															<div class="col-sm-1"></div>
														</div>
														<div class="form-group">
															<label for="bank_name" class="col-sm-3 control-label">
																工资卡开户行
															</label>
															<div class="col-sm-8">
																<input type="text" class="form-control" id="bank_name"
																	name="bank_name" placeholder="">
															</div>
															<div class="col-sm-1"></div>
														</div>
														<div class="form-group">
															<label for="bank_no" class="col-sm-3 control-label">
																银行卡号
															</label>
															<div class="col-sm-8">
																<input type="text" class="form-control" id="bank_no"
																	name="bank_no" placeholder="">
															</div>
															<div class="col-sm-1"></div>
														</div>
														<div class="form-group">
															<label for="nation" class="col-sm-3 control-label">
																民族
															</label>
															<div class="col-sm-8">
																<input type="text" class="form-control" id="nation"
																	name="nation" placeholder="">
															</div>
															<div class="col-sm-1"></div>
														</div>
														<div class="form-group">
															<label for="education" class="col-sm-3 control-label">
																学历
															</label>
															<div class="col-sm-8">
																<input type="text" class="form-control" id="education"
																	name="education" placeholder="">
															</div>
															<div class="col-sm-1"></div>
														</div>

														<div class="form-group">
															<label for="email" class="col-sm-3 control-label">
																邮箱
															</label>
															<div class="col-sm-8">
																<input type="text" class="form-control" id="email"
																	name="email" placeholder="">
															</div>
															<div class="col-sm-1"></div>
														</div>
														<div class="form-group">
															<label for="qq" class="col-sm-3 control-label">
																QQ
															</label>
															<div class="col-sm-8">
																<input type="text" class="form-control" id="qq"
																	name="qq" placeholder="">
															</div>
															<div class="col-sm-1"></div>
														</div>
														<div class="form-group">
															<label for="married" class="col-sm-3 control-label">
																是否已婚 
															</label>
															<div class="col-sm-8">
																<input type="radio" id="married_0"
																	name="married" value="1" checked>是
																<input type="radio" id="married_1"
																	name="married" value="0">否
															</div>
															<div class="col-sm-1"></div>
														</div>
														<div class="form-group">
															<div class="col-sm-offset-3 col-sm-5">
																<button type="submit" class="btn btn-primary"
																	data-loading-text="正在保存...">
																	添加员工
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
													员工列表
												</div>
												<form class="form-horizontal" role="form" id="filterform"
													action="employee/index?tab=employees">
													<input type="hidden" name="tab" value="employees" />
													<div class="form-group col-sm-6">
														<label for="name" class="col-sm-3 control-label">
															部门
														</label>
														<div class="col-sm-8">
															<select class="form-control" name="departmentId"
																id="departmentId">
																<option value="">
																	所有
																</option>
																<%
																	for (Department d : departmentlist) {
																		if (departmentId != null && d.getId() == departmentId) {
																%>
																<option selected value="<%=d.getId()%>"><%=d.getName()%></option>
																<%
																	} else {
																%>
																<option value="<%=d.getId()%>"><%=d.getName()%></option>
																<%
																	}
																	}
																%>
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
																姓名
															</th>
															<th>
																拼音
															</th>
															<th>
																公司
															</th>
															<th style="display: none;">
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
															for (Employee employee : employeelist) {
														%>
														<tr>
															<td><%=s_i%></td>
															<td><%=employee.getName()%></td>
															<td><%=employee.getHelp_code()%></td>
															<td><%=SystemCache.getDepartmentName(employee
								.getDepartmentId())%></td>
															<td style="display: none;"><%=SystemCache.getUserName(employee.getCreated_user())%></td>
															<td>
																<a class="edit" href="#"
																	data-cid="<%=employee.getId()%>">编辑</a> |
																<a class="delete" href="#"
																	data-cid="<%=employee.getId()%>">删除</a>
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
						</div>
					</div>
					<!-- Nav tabs -->

				</div>
			</div>
		</div>
	</body>
</html>
