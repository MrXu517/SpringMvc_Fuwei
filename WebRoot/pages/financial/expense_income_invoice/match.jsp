<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.financial.Expense_income"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.entity.financial.Invoice"%>
<%@page import="com.fuwei.entity.financial.Bank"%>
<%@page import="com.fuwei.commons.Pager"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="net.sf.json.JSONObject"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Invoice invoice = (Invoice)request.getAttribute("invoice");
	Map<List<Invoice> , List<Expense_income>> many_to_one_map = (Map<List<Invoice> , List<Expense_income>>)request.getAttribute("many_to_one_map");
	List<List<Expense_income>> one_to_many_Result = (List<List<Expense_income>>)request.getAttribute("one_to_many_Result");
	
%>
<!DOCTYPE html>

<html>
	<head>
		<base href="<%=basePath%>">
		<title>发票匹配 -- 桐庐富伟针织厂</title>
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
		<script src="js/financial/expense_income_invoice/match.js"
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
									<%if((one_to_many_Result==null || one_to_many_Result.size()<=0) && (many_to_one_map==null || many_to_one_map.size()<=0)){ %>
										<!-- 若匹配不到，则手动匹配 -->
									<a class="btn btn-primary" type="button" href="expense_income_invoice/match_manual/<%=invoice.getId() %>">
										手动匹配
									</a>
									<%} %>
								<p>
									<strong>一张发票 对应 一项或多项支出项</strong>
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
											</th>
											<th width="60px">
												总金额
											</th>
											<th width="60px">
												已开金额
											</th>
											<th width="60px">
												未开金额
											</th>
											<th width="60px">
												付款时间
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
											for (List<Expense_income> tempList : one_to_many_Result) {
												String expense_income_ids = "";
												for(Expense_income item :tempList){
													expense_income_ids += item.getId()+",";
												}
												expense_income_ids = expense_income_ids.substring(0,expense_income_ids.length()-1);
										%>
										<tr class="unit">
											<td colspan="8">
												<table class="table table-responsive table-bordered">
													<%int i = 0 ;
											for(Expense_income item :tempList){
											++i;
											 %>
													<tr><%if(i==1){ %><td width="60px" rowspan="<%=tempList.size() %>">
															<a href="#" class="match" invoice_ids="<%=invoice.getId() %>" expense_income_ids="<%=expense_income_ids %>">匹配</a>
														</td><%} %>
														<td width="100px"><%=item.getBank_name() %></td>
														<td width="40px"><%=SystemCache.getCompanyShortName(item.getCompany_id()) %></td>
														<td width="60px"><%=item.getAmount() %></td>
														<td width="60px"><%=item.getInvoice_amount() %></td>
														<td width="60px"><%=item.getAmount() - item.getInvoice_amount() %></td>
														<td width="60px"><%=DateTool.formatDateYMD(item.getExpense_at()) %></td>
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
									<strong>多张发票 对应 一项支出项</strong>
								</p>
								<table class="table table-responsive table-bordered">
									<thead>
										<tr><th width="60px" rowspan="2">
												操作
											</th>
											<th colspan="4">
												发票属性
											</th>
											<th colspan="3">
												支出项属性
											</th>
										</tr>
										<tr>
											<th width="100px">
												对方账户
											</th>
											<th width="55px">
												发票号
											</th>
											<th width="55px">
												未匹配金额
											</th>
											<th width="60px">
												开票时间
											</th>
											<th width="100px">
												账户
											</th>
											<th width="55px">
												未开金额
											</th>
											<th width="60px">
												付款时间
											</th>

										</tr>
									</thead>
									<tbody>
										<%if(many_to_one_map==null || many_to_one_map.size()<=0){ %>
										<tr><td colspan="8">未找到符合条件的记录</td></tr>
										<%}
											for ( List<Invoice> invoicelist: many_to_one_map.keySet()) {
												List<Expense_income> expense_incomelist = many_to_one_map.get(invoicelist);
												
												String expense_income_ids = "";
												for(Expense_income item :expense_incomelist){
													expense_income_ids += item.getId()+",";
												}
												expense_income_ids = expense_income_ids.substring(0,expense_income_ids.length()-1);
												
												String invoice_ids = "";
												for(Invoice item :invoicelist){
													invoice_ids += item.getId()+",";
												}
												invoice_ids = invoice_ids.substring(0,invoice_ids.length()-1);
												
												int invoicelist_length = invoicelist.size();
												int expense_incomelist_length = expense_incomelist.size();
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
														<%if(k<invoicelist_length){ 
													Invoice tempInvoice = invoicelist.get(k);
												%>
														<td><%=tempInvoice.getBank_name() %></td>
														<td><%=tempInvoice.getNumber() %></td>
														<td><%=tempInvoice.getAmount() - tempInvoice.getMatch_amount() %></td>
														<td><%=DateTool.formatDateYMD(tempInvoice.getPrint_date()) %></td>
														<%}else{ %>
														<td></td>
														<td></td>
														<td></td>
														<td></td>
														<%} %>
														<%if(k<expense_incomelist_length){
														Expense_income temp = expense_incomelist.get(k);
														 %>
														<td><%=temp.getBank_name() %></td>
														<td><%=temp.getAmount() - temp.getInvoice_amount() %></td>
														<td><%=DateTool.formatDateYMD(temp.getExpense_at()) %></td>
														<%}else{ %>
														<td></td>
														<td></td>
														<td></td>
														<%} %>

													</tr>
													<%} %></td>
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