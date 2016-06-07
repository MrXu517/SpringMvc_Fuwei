<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.entity.financial.Bank"%>
<%@page import="com.fuwei.entity.financial.Subject"%>
<%@page import="com.fuwei.commons.Pager"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.NumberUtil"%>
<%@page import="com.fuwei.entity.report.Payable"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="net.sf.json.JSONObject"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Pager pager = (Pager) request.getAttribute("pager");
	if (pager == null) {
		pager = new Pager();
	}
	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
	if (pager != null & pager.getResult() != null) {
		list = (List<Map<String,Object>>) pager.getResult();
	}

	Integer bank_id = (Integer) request.getAttribute("bank_id");
	String bank_str = "";
	if (bank_id != null) {
		bank_str = String.valueOf(bank_id);
	}
	if (bank_id == null) {
		bank_id = -1;
	}
	List<Bank> banklist = (List<Bank>) request.getAttribute("banklist");
%>
<!DOCTYPE html>

<html>
	<head>
		<base href="<%=basePath%>">
		<title>未收发票报表 -- 财务报表 -- 桐庐富伟针织厂</title>
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
		<script type='text/javascript' src='js/plugins/select2.min.js'></script>
		<link rel="stylesheet" type="text/css"
			href="css/plugins/select2.min.css" />
		<link href="css/report/financial.css" rel="stylesheet" type="text/css" />
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
							财务报表
						</li>
						<li class="active">
							未收发票报表
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 tablewidget">
								<!-- Table -->
								<div clas="navbar navbar-default">
									<p class="alert alert-info">提示：1、【实际未收发票金额】是正数时，则显示为红色，表示该账户有进项发票未收回<br>
																	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2、这份报表只统计对公账户，选择对方账户时也只能选择企业账户</p>
									<form class="form-horizontal searchform form-inline searchform"
										role="form">
										<input type="hidden" name="page" value="1">
										<div class="form-group salesgroup">
											<label for="bank_id" class="col-sm-3 control-label">
												对方账户
											</label>
											<div class="col-sm-8">
												<select class="form-control" name="bank_id" id="bank_id">
													<option value="">
														未选择
													</option>
													<%
																	for (Bank bank : banklist) {
																		if (bank_id != null && bank_id == bank.getId()) {
																%>
													<option value="<%=bank.getId()%>" selected><%=bank.getName()%></option>
													<%
																	} else {
																%>
													<option value="<%=bank.getId()%>"><%=bank.getName()%></option>
													<%
																	}
																	}
																%>
												</select>
											</div>
										</div>
										<br>
										<button class="btn btn-primary" type="submit" id="searchBtn">
											搜索
										</button><a class="exportBtn btn btn-primary" type="button" href="report/financial/unreceivedinvoice/export?bank_id=<%=bank_str%>">
											导出
										</a>
										<ul class="pagination">
											<li>
												<a
													href="report/financial/unreceivedinvoice?bank_id=<%=bank_str%>&page=1">«</a>
											</li>

											<%
														if (pager.getPageNo() > 1) {
													%>
											<li class="">
												<a
													href="report/financial/unreceivedinvoice?bank_id=<%=bank_str%>&page=<%=pager.getPageNo() - 1%>">上一页
													<span class="sr-only"></span> </a>
											</li>
											<%
														} else {
													%>
											<li class="disabled">
												<a disabled>上一页 <span class="sr-only"></span> </a>
											</li>
											<%
														}
													%>

											<li class="active">
												<a
													href="report/financial/unreceivedinvoice?bank_id=<%=bank_str%>&page=<%=pager.getPageNo()%>"><%=pager.getPageNo()%>/<%=pager.getTotalPage()%>，共<%=pager.getTotalCount()%>条<span
													class="sr-only"></span> </a>
											</li>
											<li>
												<%
															if (pager.getPageNo() < pager.getTotalPage()) {
														%>
											
											<li class="">
												<a
													href="report/financial/unreceivedinvoice?bank_id=<%=bank_str%>&page=<%=pager.getPageNo() + 1%>">下一页
													<span class="sr-only"></span> </a>
											</li>
											<%
														} else {
													%>
											<li class="disabled">
												<a disabled>下一页 <span class="sr-only"></span> </a>
											</li>
											<%
														}
													%>

											</li>
											<li>
												<a
													href="report/financial/unreceivedinvoice?bank_id=<%=bank_str%>&page=<%=pager.getTotalPage()%>">»</a>
											</li>
										</ul>
									</form>


								</div>

								<table class="table table-responsive table-bordered">
									<thead>
										<tr>
											<th width="20px">
												No.
											</th>
											<th width="120px">
												对方账户
											</th>
											<th width="100px">
												已收发票金额
											</th>
											<th width="100px">
												已付金额
											</th>
											<th width="100px">
												实际未收发票金额
											</th>
										</tr>
									</thead>
									<tbody>

										<%
													int i = (pager.getPageNo() - 1) * pager.getPageSize() + 0;
													for (Map<String,Object> item : list) {
														int temp_bank_id = (Integer)item.get("bank_id");
														String temp_bank_name = (String)item.get("bank_name");
														Double temp_total_payable = (Double)item.get("total_payable");
														Double temp_total_pay = (Double)item.get("total_pay");
														Double temp_total_unreceivedinvoice_amount = (Double)item.get("total_unreceivedinvoice_amount");
													%>
										<tr>
											<td><%=++i%></td>
											<td><%=temp_bank_name%></td>
											<td><%=NumberUtil.formateDouble(temp_total_payable,2) %></td>
											<td><%=NumberUtil.formateDouble(temp_total_pay,2)%></td>
											<td><%if(temp_total_unreceivedinvoice_amount>0){%>
												<span class="error"><%=NumberUtil.formateDouble(temp_total_unreceivedinvoice_amount,2)%></span>
												<%}else{ %>
												<%=NumberUtil.formateDouble(temp_total_unreceivedinvoice_amount,2)%>
												<%} %>
											</td>
										</tr>
										<%}%>
										<%if(list == null || list.size() <= 0){ %>
										<tr>
											<td colspan="8">
												找不到符合条件的记录
											</td>
										</tr>
										<%}%>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<script type="text/javascript">
	/*设置当前选中的页*/
	var $a = $("#left li a[href='report/financial/unreceivedinvoice']");
	setActiveLeft($a.parent("li"));
	$("#bank_id").select2();
</script>
	</body>
</html>


