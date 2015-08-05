<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.financial.Expense_income"%>
<%@page import="com.fuwei.entity.financial.Invoice"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.entity.financial.Expense_income"%>
<%@page import="com.fuwei.entity.financial.Bank"%>
<%@page import="com.fuwei.commons.Pager"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.fuwei.util.NumberUtil"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Expense_income income = (Expense_income)request.getAttribute("income");
	Map<List<Expense_income> , List<Invoice>> many_to_one_map = (Map<List<Expense_income> , List<Invoice>>)request.getAttribute("many_to_one_map");
	List<List<Invoice>> one_to_many_Result = (List<List<Invoice>>)request.getAttribute("one_to_many_Result");
	
%>
<!DOCTYPE html>

<html>
	<head>
		<base href="<%=basePath%>">
		<title>收入匹配 -- 桐庐富伟针织厂</title>
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
		<script src="js/financial/expense_income_invoice/income_match_saleinvoice.js"
			type="text/javascript"></script>
		<style type="text/css">
			body,.body{
				min-width:0px;
			}
			.table>tbody>tr>td,.table>thead>tr>th{
				text-align:center;
				padding-top:3px;
				padding-bottom:3px;
				font-size: 12px;
				vertical-align: middle;
			}  
			.table>tbody>tr>td{border-color: #B8B5B5;}
			
			.table>thead>tr>th{background: #B8B5B5;}
			.container-fluid{
				padding-right: 0;
			}
			.table>tbody>tr.unit>td{
				padding:0;
				border:none;
			}
			.table>tbody>tr.unit>td table{
				margin-bottom:0;
				border:none;
			}
			.table>tbody>tr.unit>td table>tr>td:first-child{
				border-left:none;
			}
			.table>tbody>tr.unit>td table tr>td:last-child{
				border-right:none;
			}
			table.table-bordered{
				  table-layout: fixed;border-color: #B8B5B5;
			}
		</style>
	</head>
	<body>
		<div id="Content">
			<div id="main">
				<div class="body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 tablewidget">
								<!-- Table -->
									
								<p>
									<strong>一项【收入】 对应 一张或多张【销项发票】</strong>
								</p>
								<table class="table table-responsive table-bordered">
									<thead>
										<tr><th width="60px">
												操作
											</th>
											<th width="100px">
												对方账户
											</th><th width="40px">
												公司
											</th><th width="40px">
												科目
											</th>
											<th width="60px">
												总金额
											</th>
											<th width="60px">
												已匹配
											</th>
											<th width="60px">
												未匹配
											</th>
											<th width="60px">
												开票时间
											</th>
											<th width="60px">
												备注
											</th>

										</tr>
									</thead>
									<tbody>
										<%if(one_to_many_Result==null || one_to_many_Result.size()<=0){ %>
										<tr><td colspan="8">未找到符合条件的记录</td></tr>
										<%}else{
											for (List<Invoice> tempList : one_to_many_Result) {
												String invoice_ids = "";
												for(Invoice item :tempList){
													invoice_ids += item.getId()+",";
												}
												invoice_ids = invoice_ids.substring(0,invoice_ids.length()-1);
										%>
										<tr class="unit">
											<td colspan="9">
												<table class="table table-responsive table-bordered">
													<%int i = 0 ;
											for(Invoice item :tempList){
											++i;
											 %>
													<tr><%if(i==1){ %><td width="60px" rowspan="<%=tempList.size() %>">
															<a href="#" class="match" invoice_ids="<%=invoice_ids %>" expense_income_ids="<%=income.getId() %>">匹配</a>
														</td><%} %>
														<td width="100px"><a target="_blank" href="sale_invoice/detail/<%=item.getId() %>"><%=item.getBank_name() %></td>
														<td width="40px"><%=SystemCache.getSubjectName(item.getSubject_id()) %></td>
														<td width="40px"><%=SystemCache.getCompanyShortName(item.getCompany_id()) %></td>
														<td width="60px"><%=item.getAmount() %></td>
														<td width="60px"><%=item.getMatch_amount() %></td>
														<td width="60px"><%=NumberUtil.formateDouble(item.getAmount() - item.getMatch_amount(),2)  %></td>
														<td width="60px"><%=DateTool.formatDateYMD(item.getPrint_date()) %></td>
														<td width="60px"><%=item.getMemo() %></td>
													</tr>
													<%} %></td>
												</table>
										</tr>
										<%
											}}
										%>
									</tbody>
								</table>
								<p>
									<strong>多项【收入】 对应 一张【销项发票】</strong>
								</p>
								<table class="table table-responsive table-bordered">
									<thead>
										<tr><th width="60px" rowspan="2">
												操作
											</th>
											<th colspan="3">
												收入项属性
											</th>
											<th colspan="4">
												发票属性
											</th>
										</tr>
										<tr>
											<th width="100px">
												对方账户
											</th>
											<th width="55px">
												未匹配金额
											</th>
											<th width="60px">
												收款时间
											</th>
											<th width="55px">
												发票号
											</th>
											<th width="100px">
												账户
											</th>
											<th width="55px">
												未匹配金额
											</th>
											<th width="60px">
												开票时间
											</th>

										</tr>
									</thead>
									<tbody>
										<%if(many_to_one_map==null || many_to_one_map.size()<=0){ %>
										<tr><td colspan="8">未找到符合条件的记录</td></tr>
										<%}
											for ( List<Expense_income> expense_incomeList: many_to_one_map.keySet()) {
												List<Invoice> invoiceList = many_to_one_map.get(expense_incomeList);
												
												String invoice_ids = "";
												for(Invoice item :invoiceList){
													invoice_ids += item.getId()+",";
												}
												invoice_ids = invoice_ids.substring(0,invoice_ids.length()-1);
												
												String expense_income_ids = "";
												for(Expense_income item :expense_incomeList){
													expense_income_ids += item.getId()+",";
												}
												expense_income_ids = expense_income_ids.substring(0,expense_income_ids.length()-1);
												
												int invoicelist_length = invoiceList.size();
												int expense_incomelist_length = expense_incomeList.size();
												int max = Math.max(invoicelist_length,expense_incomelist_length);
										%>
										<tr class="unit">
											<td colspan="8">
												<table class="table table-responsive table-bordered">
												
													<%
											for(int k = 0 ; k < max;++k){
											
											 %>
													<tr><%if(k==0){ %><td width="60px" rowspan="<%=max %>">
															<a href="#" class="match" invoice_ids="<%=invoice_ids %>" expense_income_ids="<%=expense_income_ids %>" >匹配</a>
														</td><%} %>
														<%if(k<expense_incomelist_length){ 
													Expense_income tempExpenseIncome = expense_incomeList.get(k);
												%>
														<td><%=tempExpenseIncome.getBank_name() %></td>
														<td><%=NumberUtil.formateDouble(tempExpenseIncome.getAmount() - tempExpenseIncome.getInvoice_amount(),2) %></td>
														<td><%=DateTool.formatDateYMD(tempExpenseIncome.getExpense_at()) %></td>
														<%}else{ %>
														<td></td>
														<td></td>
														<td></td>
														<%} %>
														<%if(k<invoicelist_length){
														Invoice temp = invoiceList.get(k);
														 %>
														<td><%=temp.getNumber() %></td>
														<td><%=temp.getBank_name() %></td>
														<td><%=NumberUtil.formateDouble(temp.getAmount() - temp.getMatch_amount(),2) %></td>
														<td><%=DateTool.formatDateYMD(temp.getPrint_date()) %></td>
														<%}else{ %>
														<td></td>
														<td></td>
														<td></td>
														<td></td>
														<%} %>

													</tr>
													<%} %><td></td>
												</table>
										</tr>
										<%
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
	</body>
</html>