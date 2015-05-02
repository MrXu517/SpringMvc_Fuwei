<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.entity.Employee"%>
<%@page import="com.fuwei.entity.Material"%>
<%@page import="com.fuwei.entity.Customer"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	List<Employee> employeelist = new ArrayList<Employee>();
	for (Employee temp : SystemCache.employeelist) {
		if (temp.getIs_charge_employee()) {
			employeelist.add(temp);
		}
	}
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>创建染色单 -- 桐庐富伟针织厂</title>
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
		<script src="<%=basePath%>js/plugins/WdatePicker.js"
			type="text/javascript"></script>
		<script src="js/common/common.js" type="text/javascript"></script>
		<script src="js/plugins/jquery.jqGrid.min.js" type="text/javascript"></script>
		<link href="css/plugins/ui.jqgrid.css" rel="stylesheet"
			type="text/css" />

		<link href="css/order/bill.css" rel="stylesheet" type="text/css" />
		<script src="js/order/ordergrid.js" type="text/javascript"></script>
		<script src="js/coloring_order/add.js" type="text/javascript"></script>

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
							创建染色单
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid coloringorderWidget">
						<div class="row">
							<form class="saveform">
								<input type="hidden" id="sampleId" name="sampleId" />
								<button type="submit"
									class="pull-right btn btn-danger saveTable"
									data-loading-text="正在保存...">
									创建染色单
								</button>

								<div class="clear"></div>
								<div class="col-md-12 tablewidget">
									<table class="table">
										<caption id="tablename">
											桐庐富伟针织厂染色单
										</caption>
									</table>

									<table class="tableTb noborder">
										<tbody>
											<tr>
												<td>
													<div class="form-group">
													染色单位：
													<select class="form-control require" name="factoryId"
																	id="factoryId">
																	<option value="">
																		未选择
																	</option>
																	<%
																		for (Factory factory : SystemCache.coloring_factorylist) {
																	%>
																	<option value="<%=factory.getId()%>"><%=factory.getName()%></option>
																	<%
																		}
																	%>
																</select></div>
													<div class="form-group">
														业务员：
													<select name="charge_employee" id="charge_employee"
														class="form-control">
														<%
															for (Employee item : employeelist) {
														%>
														<option value="<%=item.getId()%>"><%=item.getName()%></option>
														<%
															}
														%>
													</select>
													</div>
													
												</td><td><div class="form-group pull-right">№：<input class="form-control" disabled type="text" value="自动生成" /></div></td>
											</tr>

											<tr>
												<td colspan="2">
													<table class="table table-responsive table-bordered">
														<tr>
															<td class="center" width="15%">
																公司
															</td>
															<td class="center" width="15%">
																公司货号
															</td>
															<td class="center" width="15%">
																客户
															</td>
															<td class="center" width="15%">
																品名
															</td>
														</tr>
														<tr>
															<td class="center">
																<select class="form-control require" name="companyId"
																	id="companyId">
																	<option value="">
																		未选择
																	</option>
																	<%
																		for (Company company : SystemCache.companylist) {
																	%>
																	<option value="<%=company.getId()%>"><%=company.getFullname()%></option>
																	<%
																		}
																	%>
																</select>
															</td>
															<td class="center">
																<input type="text" name="company_productNumber"
																	id="company_productNumber" class="form-control require" />
															</td>
															<td class="center">
																<select class="form-control" name="customerId"
																	id="customerId">
																	<option value="">
																		未选择
																	</option>
																	<%
																		for (Customer customer : SystemCache.customerlist) {
																	%>
																	<option value="<%=customer.getId()%>"><%=customer.getName()%></option>
																	<%
																		}
																	%>
																</select>
															</td>
															<td class="center">
																<input type="text" name="name" id="name"
																	class="form-control require" />
															</td>
														</tr>
														<tr><td class="center vertical-center memo_label">备注</td><td colspan="3">
																
																		<input type="text" class="form-control"
																			name="memo" id="memo" placeholder="备注">
																	</div>
																	
															</td>
														</tr>
													</table>
												</td>
											</tr>
											

										</tbody>
									</table>
									

									<table>
										<tbody>
										
											<tr>
												<td>
													<table class="table table-responsive detailTb">
														<caption>
															<button type="button"
																class="btn btn-primary addRow pull-left">
																添加一行
															</button>
															材料列表
														</caption>
														<thead>
															<tr>
																<th width="15%">
																	色号
																</th>
																<th width="15%">
																	材料
																</th>
																<th width="15%">
																	数量(kg)
																</th>
																<th width="15%">
																	标准样纱
																</th>
																<th width="15%">
																	操作
																</th>
															</tr>
														</thead>
														<tbody>

														</tbody>
													</table>
													<div id="navigator"></div>
												</td>
											</tr>

										</tbody>
									</table>

								</div>
							</form>
						</div>
					</div>

					<!--
						 			添加编辑染色单对话框 -->
					<div class="modal fade tableRowDialog" id="coloringDialog">
						<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal">
										<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
									</button>
									<h4 class="modal-title">
										添加一行
									</h4>
								</div>
								<div class="modal-body">
									<form class="form-horizontal rowform" role="form">
										<div class="form-group col-md-12">
											<label for="color" class="col-sm-3 control-label">
												颜色
											</label>
											<div class="col-sm-8">
												<input type="text" name="color" id="color"
													class="form-control require" />
											</div>
											<div class="col-sm-1"></div>
										</div>
										<div class="form-group col-md-12">
											<label for="material" class="col-sm-3 control-label">
												材料
											</label>
											<div class="col-sm-8">
												<select name="material" id="material"
													class="form-control require">
													<option value="">
														未选择
													</option>
													<%
														for (Material material : SystemCache.materiallist) {
													%>
													<option value="<%=material.getId()%>"><%=material.getName()%></option>
													<%
														}
													%>
												</select>
											</div>
											<div class="col-sm-1"></div>
										</div>
										<div class="form-group col-md-12">
											<label for="quantity" class="col-sm-3 control-label">
												数量(kg)
											</label>
											<div class="col-sm-8">
												<input type="text" name="quantity" id="quantity"
													class="form-control double require" />
											</div>
											<div class="col-sm-1"></div>
										</div>
										<div class="form-group col-md-12">
											<label for="standardyarn" class="col-sm-3 control-label">
												标准样纱
											</label>
											<div class="col-sm-8">
												<input type="text" name="standardyarn" id="standardyarn"
													class="form-control" />
											</div>
											<div class="col-sm-1"></div>
										</div>


										<div class="modal-footer">
											<button type="submit" class="btn btn-primary"
												data-loading-text="正在保存...">
												保存
											</button>
											<button type="button" class="btn btn-default"
												data-dismiss="modal">
												关闭
											</button>
										</div>
									</form>
								</div>

							</div>
						</div>
					</div>
					<!-- 添加编辑染色单对话框 -->


				</div>
			</div>
		</div>
	</body>
</html>