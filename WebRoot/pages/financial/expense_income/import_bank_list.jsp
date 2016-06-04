<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Material"%>
<%@page import="com.fuwei.entity.Employee"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="com.fuwei.entity.financial.Expense_income"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.mchange.v2.ser.SerializableUtils"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.entity.financial.SelfAccount"%>
<%@page import="com.fuwei.entity.financial.Subject"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.util.DateTool"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	List<Expense_income> list = (List<Expense_income>)request.getAttribute("list");
	HashMap<String, List<Salesman>> companySalesmanMap = SystemCache
			.getCompanySalesmanMap_ID();
	JSONObject jObject = new JSONObject();
	jObject.put("companySalesmanMap", companySalesmanMap);
	String companySalesmanMap_str = jObject.toString();
	List<Subject> expense_subjectlist = SystemCache.getSubjectList(false);
	List<Subject> income_subjectlist = SystemCache.getSubjectList(true);
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>批量填写银行明细 -- 桐庐富伟针织厂</title>
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
		<script src="js/plugins/jquery.form.js" type="text/javascript"></script>
		<script src="js/financial/expense_income/import_bank_list.js" type="text/javascript"></script>
		<style type="text/css">
		.checkBtn{width:20px ;height:20px;}
		.table tr.checkselected {
    		background: #DCD773 !important;
		}
		#mainTb thead{background: #9E9D9D;}
		#mainTb select{padding-left: 0;padding-right: 0;}
		</style>
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
							<i class=""></i>
							<a href="financial/workspace?tab=expense_incomes">财务工作台</a>
						</li>
						<li>
							<i class=""></i>
							<a href="expense_income/import_bank">批量导入银行明细</a>
						</li>
						<li class="active">
							批量填写银行明细
						</li>
					</ul>
				</div>
				<div class="body">

					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 formwidget">
								<fieldset>
									<legend>
										批量导入
									</legend>

									<form class="form-horizontal form" role="form"
										enctype="multipart/form-data" action="expense_income/import_bank"
										method="post">
											<button type="submit" class="btn btn-primary"
														data-loading-text="正在加载...">
														导入
											</button>
											<p id="tip">您已选择了 <strong id="num">0</strong> 条记录 </p>
											<table id="mainTb"
											class="table table-responsive table-bordered detailTb">
											<thead>
												<tr><th width="5%">
														序号
													</th><th width="8%">
														流水号
													</th><th width="10%">
														交易时间
													</th><th width="8%">
														金额
													</th><th width="12%">
														对方户名\帐号
													</th><th width="8%">
														公司
													</th><th width="8%">
														业务员
													</th><th width="8%">
														科目
													</th><th width="8%">
														本厂收支帐号
													</th><th width="8%">
														备注
													</th>
												</tr>
											</thead>
											<tbody>
												<%
													for (Expense_income item : list) {
												%>
												<tr class="tr EmptyTr disable" data='<%=SerializeTool.serialize(item)%>'>
													<td><input type="checkbox" name="checked" class="checkBtn"/></td>
													<td><%=item.getBank_transaction_no()%></td>
													<td><%=DateTool.formateDate(item.getExpense_at(),"yyyy/MM/dd HH:mm") %></td>
													<%if(item.getIn_out()) {%>
													<td><%=item.getAmount()%><br><span class="label label-success">收入</span></td>
													<%}else{ %>
													<td><%=item.getAmount()%><br><span class="label label-danger">支出</span></td>
													<%} %>
													<td><%=item.getBank_name()%><br><%=item.getOther_bank_no()%></td>
													<td>
														<select data='<%=companySalesmanMap_str%>' class="company_id value" name="company_id"
															 placeholder="公司">
															<option value="">
																未选择
															</option>
															<%
																for (Company company : SystemCache.companylist) {
															%>
															<option value="<%=company.getId()%>"><%=company.getShortname()%></option>
															<%
															}
														%>
														</select>
													</td>
													<td>
														<select class="salesman_id value" name="salesman_id"  placeholder="业务员">
															<option value="">
															未选择
															</option>
														</select>
													</td>
													<td>
														<select class="subject_id require  value">
															<option value="">未选择</option>
															<%if(item.getIn_out()){ 	
																for(Subject temp : income_subjectlist){ %>
															<option value="<%=temp.getId() %>"><%=temp.getName() %></option>
															<%} }else{
																for(Subject temp : expense_subjectlist){ %>
																<option value="<%=temp.getId() %>"><%=temp.getName() %></option>
															<%} }%>
														</select>
													</td>
													<td>
														<select class="account_id require value">
															<option value="">未选择</option>
															<%for(SelfAccount temp : SystemCache.selfAccountlist){ %>
															<option value="<%=temp.getId() %>"><%=temp.getName() %></option>
															<%} %>
														</select>
													</td>
													<td><textarea type="text" class="memo value" value="<%=item.getMemo()%>"><%=item.getMemo()%></textarea> </td>
													
												</tr>
												<%
													}
												%>
											</tbody>

										</table>
									</form>
								</fieldset>
							</div>

						</div>
					</div>

				</div>
			</div>
		</div>
		<script type="text/javascript">
	/*设置当前选中的页*/
	var $a = $("#left li a[href='financial/workspace']");
	setActiveLeft($a.parent("li"));

</script>
	</body>
</html>