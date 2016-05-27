<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.entity.financial.Bank"%>
<%@page import="com.fuwei.entity.financial.Subject"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.entity.financial.Expense_income"%>
<%@page import="com.fuwei.entity.financial.SelfAccount"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	
	Expense_income expense_income = (Expense_income)request.getAttribute("expense_income");
	List<Subject> subjectlist = (List<Subject>) request
			.getAttribute("subjectlist");
	
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>支出 -- 桐庐富伟针织厂</title>
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
		<script src="js/plugins/jquery.form.js" type="text/javascript"></script>
		<script src="js/financial/expense_income/expense_edit.js" type="text/javascript"></script>
	<script type='text/javascript' src='js/plugins/select2.min.js'></script>
	<link rel="stylesheet" type="text/css" href="css/plugins/select2.min.css" />
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
							支出
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
											编辑支出
										</h3>
									</div>
									<div class="panel-body">

										<form class="form-horizontal form" role="form">
											<input type="hidden" value="<%=expense_income.getId() %>" name="id"/>
											<div class="form-group col-md-6">
												<label for="company_id" class="col-sm-3 control-label">
													公司
												</label>
												<div class="col-sm-8">
													<select disabled 
														class="form-control" name="company_id" id="company_id">
														<option value="<%=expense_income.getCompany_id()==null?"":expense_income.getCompany_id()%>"><%=expense_income.getCompany_name()==null?"":expense_income.getCompany_name()%></option>
													</select>
												</div>
											</div>
											<div class="form-group col-md-6">
												<label for="salesman_id" class="col-sm-3 control-label">
													业务员
												</label>
												<div class="col-sm-8">
													<select disabled class="form-control" name="salesman_id"
														id="salesman_id">
														<option value="<%=expense_income.getSalesman_id()==null?"":expense_income.getSalesman_id()%>"><%=expense_income.getSalesman_name()==null?"":expense_income.getSalesman_name()%></option>
													</select>
												</div>
											</div>

											<div class="form-group col-md-6">
												<label for="bank_id" class="col-sm-3 control-label">
													对方账户
												</label>
												<div class="col-sm-8">
													<select disabled class="form-control require" name="bank_id" id="bank_id">
														<option value="<%=expense_income.getBank_id()%>"><%=expense_income.getBank_name()%></option>
													</select>
												</div>
											</div>

											<div class="form-group col-md-6">
												<label for="subject_id" class="col-sm-3 control-label">
													科目
												</label>
												<div class="col-sm-8">
													<select class="form-control require" name="subject_id"
														id="subject_id">
														<option value="">
															未选择
														</option>
														<%
															for (Subject subject : subjectlist) {
																if((int)subject.getId() == (int)expense_income.getSubject_id()){
														%>
															<option selected value="<%=subject.getId()%>"><%=subject.getName()%></option>
														<%}else{ %>
														<option value="<%=subject.getId()%>"><%=subject.getName()%></option>
														<%}
															}
														%>
													</select>
												</div>
											</div>

											<div class="form-group col-md-6">
												<label for="amount" class="col-sm-3 control-label">
													支出金额
												</label>
												<div class="col-sm-8">
													<input disabled type="text" class="form-control require double"
														name="amount" id="amount" value="<%=expense_income.getAmount() %>">
												</div>
												<div class="col-sm-1"></div>
											</div>

											<div class="form-group col-md-6">
												<label for="expense_at" class="col-sm-3 control-label">
													付款时间
												</label>
												<div class="col-sm-8">
													<input disabled type="text" name="expense_at"
														id="expense_at"  class="date form-control require"
															value="<%=DateTool.formatDateYMD(expense_income.getExpense_at())%>" />

												</div>
												<div class="col-sm-1"></div>
											</div>
											<div class="form-group col-md-6">
												<label for="bank_transaction_no" class="col-sm-3 control-label">
													交易流水号
												</label>
												<div class="col-sm-8">
													<input type="text" class="form-control "
														name="bank_transaction_no" id="bank_transaction_no" value="<%=expense_income.getBank_transaction_no()==null?"":expense_income.getBank_transaction_no()%>" >
												</div>
												<div class="col-sm-1"></div>
											</div>
											<div class="form-group col-md-6">
												<label for="account_id" class="col-sm-3 control-label">
													支付帐号
												</label>
												<div class="col-sm-8">
													<select class="form-control require" name="account_id"
														id="account_id">
														<option value="">
															未选择
														</option>
														<%
															for (SelfAccount account : SystemCache.selfAccountlist) {
															if(expense_income.getAccount_id()!=null && (int)expense_income.getAccount_id() == (int)account.getId()){
														%>
														<option value="<%=account.getId()%>" selected><%=account.getName()%></option>
														<%
															}else{
														%>
														<option value="<%=account.getId()%>"><%=account.getName()%></option>
														<%
															}}
														%>
													</select>
												</div>
												<div class="col-sm-1"></div>
											</div>
											<div class="clear"></div>
											<div class="form-group col-md-6">
												<label for="memo" class="col-sm-3 control-label">
													备注
												</label>
												<div class="col-sm-8">
													<input type="text" class="form-control" name="memo"
														id="memo" value="<%=expense_income.getMemo() %>">
												</div>
												<div class="col-sm-1"></div>
											</div>
											<div class="form-group">
												<div class="col-sm-offset-3 col-sm-5">
													<button type="submit" class="btn btn-primary"
														data-loading-text="正在保存...">
														确定
													</button>

												</div>
												<div class="col-sm-3">
													<button type="reset" class="reset btn btn-default">
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
	</body>
</html>