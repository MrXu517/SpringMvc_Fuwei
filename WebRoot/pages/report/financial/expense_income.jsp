<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.financial.Expense_income"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.entity.financial.Bank"%>
<%@page import="com.fuwei.entity.financial.Subject"%>
<%@page import="com.fuwei.commons.Pager"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.NumberUtil"%>
<%@page import="com.fuwei.commons.SystemCache"%>
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
	List<Expense_income> Expense_incomelist = new ArrayList<Expense_income>();
	
	double total_income = 0 ;
	double total_expense = 0 ;
	double total_minus = 0 ;
	if (pager != null & pager.getResult() != null) {
		Expense_incomelist = (List<Expense_income>) pager.getResult();
		total_income = NumberUtil.formateDouble((Double)pager.getTotal().get("income_amount"),2);
		total_expense = NumberUtil.formateDouble((Double)pager.getTotal().get("expense_amount"),2);
		total_minus = NumberUtil.formateDouble(total_income-total_expense,2);
	}
	

	Date start_time = (Date) request.getAttribute("start_time");
	String start_time_str = "";
	if (start_time != null) {
		start_time_str = DateTool.formatDateYMD(start_time);
	}
	Date end_time = (Date) request.getAttribute("end_time");
	String end_time_str = "";
	if (end_time != null) {
		end_time_str = DateTool.formatDateYMD(end_time);
	}

	Integer salesmanId = (Integer) request.getAttribute("salesmanId");
	Integer companyId = (Integer) request.getAttribute("companyId");
	String company_str = "";
	String salesman_str = "";
	if (salesmanId != null) {
		salesman_str = String.valueOf(salesmanId);
	}
	if (companyId != null) {
		company_str = String.valueOf(companyId);
	}
	if (salesmanId == null) {
		salesmanId = -1;
	}
	if (companyId == null) {
		companyId = -1;
	}

	Boolean in_out = (Boolean) request.getAttribute("in_out");
	String in_out_str = "";
	if (in_out != null) {
		in_out_str = in_out.toString();
	}

	Integer bank_id = (Integer) request.getAttribute("bank_id");
	Integer subject_id = (Integer) request.getAttribute("subject_id");
	String bank_str = "";
	String subject_str = "";
	if (bank_id != null) {
		bank_str = String.valueOf(bank_id);
	}
	if (subject_id != null) {
		subject_str = String.valueOf(subject_id);
	}
	if (bank_id == null) {
		bank_id = -1;
	}
	if (subject_id == null) {
		subject_id = -1;
	}


	HashMap<String, List<Salesman>> companySalesmanMap = SystemCache
			.getCompanySalesmanMap_ID();
	JSONObject jObject = new JSONObject();
	jObject.put("companySalesmanMap", companySalesmanMap);
	String companySalesmanMap_str = jObject.toString();

	List<Subject> subjectlist = (List<Subject>) request
			.getAttribute("subjectlist");
	List<Bank> banklist = (List<Bank>) request.getAttribute("banklist");

	//权限相关
	Boolean has_item_delete = SystemCache.hasAuthority(session,
			"expense_income/delete");
	//权限相关
%>
<!DOCTYPE html>

<html>
	<head>
		<base href="<%=basePath%>">
		<title>收支报表 -- 财务报表 -- 桐庐富伟针织厂</title>
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
		<script src="js/financial/workspace/expense_income.js" type="text/javascript"></script>
		<script type='text/javascript' src='js/plugins/select2.min.js'></script>
	<link rel="stylesheet" type="text/css" href="css/plugins/select2.min.css" />
		<link href="css/report/financial.css" rel="stylesheet"
			type="text/css" />
		
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
						</li><li>
							财务报表
						</li>
						<li class="active">
							收支报表
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 tablewidget">
								<!-- Table -->
								<div clas="navbar navbar-default">
									<form class="form-horizontal searchform form-inline searchform"
										role="form">
										<input type="hidden" name="page" value="1">
										<div class="form-group">
											<label for="in_out" class="col-sm-3 control-label">
												收支
											</label>
											<div class="col-sm-9">
												<select class="form-control" name="in_out" id="in_out">
													<option value="">
														所有
													</option>
													<%
														if (in_out != null && in_out == true) {
													%>
													<option value="<%=true%>" selected>
														收入
													</option>
													<%
														} else {
													%>
													<option value="<%=true%>">
														收入
													</option>
													<%
														}
													%>
													<%
														if (in_out != null && in_out == false) {
													%>
													<option value="<%=false%>" selected>
														支出
													</option>
													<%
														} else {
													%>
													<option value="<%=false%>">
														支出
													</option>
													<%
														}
													%>

												</select>
											</div>
										</div>
										<div class="form-group salesgroup">
											<label for="companyId" class="col-sm-3 control-label">
												公司
											</label>
											<div class="col-sm-9">
												<select data='<%=companySalesmanMap_str%>'
													class="form-control" name="companyId" id="companyId"
													placeholder="公司">
													<option value="">
														所有
													</option>
													<%
														for (Company company : SystemCache.companylist) {
															if (companyId != null && companyId == company.getId()) {
													%>
													<option value="<%=company.getId()%>" selected><%=company.getShortname()%></option>
													<%
														} else {
													%>
													<option value="<%=company.getId()%>"><%=company.getShortname()%></option>
													<%
														}
														}
													%>
												</select>
											</div>
										</div>
										<div class="form-group salesgroup">
											<label for="salesmanId" class="col-sm-4 control-label">
												业务员
											</label>
											<div class="col-sm-8">
												<select class="form-control" name="salesmanId"
													id="salesmanId" placeholder="业务员">
													<option value="">
														所有
													</option>
													<%
														for (Salesman salesman : SystemCache.getSalesmanList(companyId)) {
															if (salesmanId != null && salesmanId == salesman.getId()) {
													%>
													<option value="<%=salesman.getId()%>" selected><%=salesman.getName()%></option>
													<%
														} else {
													%>
													<option value="<%=salesman.getId()%>"><%=salesman.getName()%></option>
													<%
														}
														}
													%>
												</select>
											</div>
										</div>

										<div class="form-group salesgroup">
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
															if (subject_id != null && subject_id == subject.getId()) {
													%>
													<option value="<%=subject.getId()%>" selected><%=subject.getName()%></option>
													<%
														} else {
													%>
													<option value="<%=subject.getId()%>"><%=subject.getName()%></option>
													<%
														}
														}
													%>


												</select>
											</div>
										</div>
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

										<div class="form-group timegroup">
											<label class="col-sm-3 control-label">
												时间
											</label>

											<div class="input-group col-md-9">
												<input type="text" name="start_time" id="start_time"
													class="date form-control" value="<%=start_time_str%>" />
												<span class="input-group-addon">到</span>
												<input type="text" name="end_time" id="end_time"
													class="date form-control" value="<%=end_time_str%>">
											</div>
										</div>

										
									<br><button class="btn btn-primary" type="button" id="searchBtn">
											搜索
										</button><a class="exportBtn btn btn-primary" type="button" href="report/financial/expense_income/export?bank_id=<%=bank_str%>&subject_id=<%=subject_str%>&in_out=<%=in_out_str%>&companyId=<%=company_str%>&salesmanId=<%=salesman_str%>&start_time=<%=start_time_str%>&end_time=<%=end_time_str%>">
											导出
										</a>

									<ul class="pagination">
										<li>
											<a
												href="report/financial/expense_income?bank_id=<%=bank_str%>&subject_id=<%=subject_str%>&in_out=<%=in_out_str%>&companyId=<%=company_str%>&salesmanId=<%=salesman_str%>&start_time=<%=start_time_str%>&end_time=<%=end_time_str%>&page=1">«</a>
										</li>

										<%
											if (pager.getPageNo() > 1) {
										%>
										<li class="">
											<a
												href="report/financial/expense_income?bank_id=<%=bank_str%>&subject_id=<%=subject_str%>&in_out=<%=in_out_str%>&companyId=<%=company_str%>&salesmanId=<%=salesman_str%>&start_time=<%=start_time_str%>&end_time=<%=end_time_str%>&page=<%=pager.getPageNo() - 1%>">上一页
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
												href="report/financial/expense_income?bank_id=<%=bank_str%>&subject_id=<%=subject_str%>&in_out=<%=in_out_str%>&companyId=<%=company_str%>&salesmanId=<%=salesman_str%>&start_time=<%=start_time_str%>&end_time=<%=end_time_str%>&page=<%=pager.getPageNo()%>"><%=pager.getPageNo()%>/<%=pager.getTotalPage()%>，共<%=pager.getTotalCount()%>条<span
												class="sr-only"></span> </a>
										</li>
										<li>
											<%
												if (pager.getPageNo() < pager.getTotalPage()) {
											%>
										
										<li class="">
											<a
												href="report/financial/expense_income?bank_id=<%=bank_str%>&subject_id=<%=subject_str%>&in_out=<%=in_out_str%>&companyId=<%=company_str%>&salesmanId=<%=salesman_str%>&start_time=<%=start_time_str%>&end_time=<%=end_time_str%>&page=<%=pager.getPageNo() + 1%>">下一页
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
												href="report/financial/expense_income?bank_id=<%=bank_str%>&subject_id=<%=subject_str%>&in_out=<%=in_out_str%>&companyId=<%=company_str%>&salesmanId=<%=salesman_str%>&start_time=<%=start_time_str%>&end_time=<%=end_time_str%>&page=<%=pager.getTotalPage()%>">»</a>
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

											<th width="50px">
												收入
											</th>
											<th width="50px">
												支出
											</th>
											<th width="120px">
												对方账户
											</th>
											<th width="40px">
												科目
											</th>
											<th width="40px">
												公司
											</th>
											<th width="40px">
												业务员
											</th>
											<th width="60px">
												时间
											</th>
											<th width="80px">
												备注
											</th>
										</tr>
									</thead>
									<tbody>
										<%
											int i = (pager.getPageNo() - 1) * pager.getPageSize() + 0;
											for (Expense_income item : Expense_incomelist) {
										%>
										<tr itemId="<%=item.getId()%>">
											<td><%=++i%></td>
											<%
												if (item.getIn_out() == true) {
											%>
											<td><%=item.getAmount()%></td>
											<td></td>
											<%
												} else {
											%>
											<td></td>
											<td><%=item.getAmount()%></td>
											<%
												}
											%>

											<td><%=item.getBank_name()%></td>
											<td><%=item.getSubject_name()%></td>
											<td><%=SystemCache.getCompanyShortName(item
										.getCompany_id())%></td>
											<td><%=SystemCache.getSalesmanName(item.getSalesman_id())%></td>
											<td><%=DateTool.formatDateYMD(item.getExpense_at())%></td>
											<td><%=item.getMemo()%></td>
										
										</tr>
										<%
											}
										%>
											<%if(Expense_incomelist == null || Expense_incomelist.size() <= 0){ %>
														<tr><td colspan="8">找不到符合条件的记录</td></tr>
													<%}else{ %>
												</tbody><tfoot><tr><td>合计</td><td><%=total_income %></td><td><%=total_expense%></td><td style="text-align:left;padding-left:10px;" colspan="6"> 收入-支出 = <%=total_minus%></td></tr>
</tfoot>
											<%} %>
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
	var $a = $("#left li a[href='report/financial/expense_income']");
	setActiveLeft($a.parent("li"));

</script>
	</body>
</html>