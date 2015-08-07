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
	Expense_income income = (Expense_income)request.getAttribute("income");
	List<Invoice> invoicelist = (List<Invoice>)request.getAttribute("invoicelist");
	
%>
<!DOCTYPE html>

<html>
	<head>
		<base href="<%=basePath%>">
		<title>手动 -- 收入匹配 -- 桐庐富伟针织厂</title>
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
		<script src="js/financial/expense_income_invoice/income_match_manual.js"
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
									<strong>手动匹配：  请选择一项销项发票匹配</strong>
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
												开票金额
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
										<%if(invoicelist==null || invoicelist.size()<=0){ %>
										<tr><td colspan="9">未找到符合条件的记录</td></tr>
										<%}else{
											int i = 0 ;
											for (Invoice temp : invoicelist) {
											
										%>
													<tr><td width="60px">
															<a href="#" class="match" invoice_id="<%=temp.getId() %>" expense_income_id="<%=income.getId() %>">匹配</a>
														</td>
														<td width="100px"><%=temp.getBank_name() %></td>
														<td width="40px"><%=SystemCache.getCompanyShortName(temp.getCompany_id()) %></td>
														<td width="40px"><%=SystemCache.getSubjectName(temp.getSubject_id()) %></td>
														<td width="60px"><%=temp.getAmount() %></td>
														<td width="60px"><%=temp.getMatch_amount() %></td>
														<td width="60px"><%=temp.getAmount() - temp.getMatch_amount() %></td>
														<td width="60px"><%=DateTool.formatDateYMD(temp.getPrint_date()) %></td>
														<td width="60px"><%=temp.getMemo() %></td>
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