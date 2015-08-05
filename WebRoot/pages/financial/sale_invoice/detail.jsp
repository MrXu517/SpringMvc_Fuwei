<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.financial.Invoice"%>
<%@page import="com.fuwei.entity.financial.Expense_income"%>
<%@page import="com.fuwei.entity.financial.Expense_income_invoice"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.NumberUtil"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Invoice invoice = (Invoice) request.getAttribute("invoice");
	Map<Expense_income, Expense_income_invoice> map = (Map<Expense_income, Expense_income_invoice>) request
			.getAttribute("map");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>发票详情 -- 桐庐富伟针织厂</title>
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
		<link rel="stylesheet" type="text/css"
			href="css/plugins/select2.min.css" />
		<script src="js/common/common.js" type="text/javascript"></script>
		<script src="js/financial/purchase_invoice/detail.js"
			type="text/javascript"></script>
		<link rel="stylesheet" type="text/css"
			href="css/financial/invoice.css" />
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
							<a href="financial/workspace?tab=purchase_invoices">财务</a>
						</li>
						<li class="active">
							发票详情
						</li>
					</ul>
				</div>
				<div class="body">

					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12">
								<fieldset>
									<legend>
										发票属性
										<%
										if (invoice.isMatched()) {
									%>
										<span class="label label-success">已完成匹配</span>
										<%
											} else {
										%>
										<span class="label label-warning">待匹配</span>
										<%
											}
										%>
										<%
											if (invoice.getIn_out()) {
										%>
										<span class="label label-info">进项发票</span>
										<%
											} else {
										%>
										<span class="label label-info">销项发票</span>
										<%
											}
										%>
										
									</legend>
									<table
										class="table table-responsive table-bordered propertyTable">
										<tr><td class="property">公司</td>
											<td>
												<span><%=SystemCache.getCompanyName(invoice.getCompany_id()) %></span>
											</td>
											<td class="property">
												科目
											</td>
											<td>
												<span><%=SystemCache.getSubjectName(invoice.getSubject_id())%></span>
											</td>
											
										</tr>
										<tr>
											<td class="property">
												对方账户
											</td>
											<td>
												<span><%=invoice.getBank_name()%></span>
											</td><td class="property">
												发票号
											</td>
											<td>
												<span><%=invoice.getNumber()%></span>
											</td>
											
										</tr>
										<tr>
											<td class="property">
												发票类型
											</td>
											<td>
												<span><%=invoice.getTypeString()%></span>
											</td>
											<td class="property">
												金额
											</td>
											<td>
												<span><%=invoice.getAmount()%></span>
											</td>
											
										</tr>
										<tr>
											<td class="property">
												开票日期
											</td>
											<td>
												<span><%=invoice.getPrint_date()%></span>
											</td>
											<td class="property">
												已匹配金额
											</td>
											<td>
												<span><%=invoice.getMatch_amount()%></span>
											</td>
											
										</tr>
										<tr>
											<td class="property">
												备注
											</td>
											<td>
												<span><%=invoice.getMemo()%></span>
											</td>
											<td class="property">
												待匹配金额
											</td>
											<td>
												<span class="label label-danger"><%=NumberUtil.formateDouble(
															invoice.getAmount() - invoice.getMatch_amount(), 2)%></span>
												
											</td>
										</tr>
									</table>
								</fieldset>

								<fieldset>
									<legend>
										所收收入款项
									</legend>
									<table class="table table-responsive table-bordered"
										id="detailTb">
										<thead>
											<tr>
												<th width="120px">
													对方账户
												</th>
												<th width="60px">
													科目
												</th>
												<th width="80px">
													公司
												</th>
												<th width="80px">
													业务员
												</th>
												<th width="80px">
													收款时间
												</th>
												<th width="60px">
													开票金额
												</th>
												<th width="60px">
													操作
												</th>
											</tr>
										</thead>
										<tbody>
											<%
												if (map.size() <= 0) {
											%>
											<tr>
												<td colspan="7">
													还没有匹配到财务收入项
												</td>
											</tr>
											<%
												}
											%>
											<%
												double total_match_amount = 0;
												for (Expense_income item : map.keySet()) {
													Expense_income_invoice temp = map.get(item);
													total_match_amount += temp.getAmount();
											%>

											<tr>
												<td><%=item.getBank_name()%></td>
												<td><%=item.getSubject_name()%></td>
												<td><%=SystemCache.getCompanyShortName(item
										.getCompany_id())%></td>
												<td><%=SystemCache.getSalesmanName(item.getSalesman_id())%></td>
												<td><%=DateTool.formatDateYMD(item.getExpense_at())%></td>
												<td><%=temp.getAmount()%></td>

												<td>
													<%
														if (item.getIn_out()) {
													%><a href="income/detail/<%=item.getId()%>">收入详情</a>
													<%
														} else {
													%><a href="expense/detail/<%=item.getId()%>">支出详情</a>
													<%
														}
													%>
													|
													<a data-cid="<%=temp.getId()%>" class="deleteMatch"
														href="#">取消开发票</a>

												</td>
											</tr>
											<%
												}
											%>
											<%
												if (map.size() > 0) {
													total_match_amount = NumberUtil.formateDouble(
															total_match_amount, 2);
											%>
											<tr>
												<td>
													合计
												</td>
												<td></td>
												<td></td>
												<td></td>
												<td></td>
												<td><%=total_match_amount%></td>
												<td></td>
											</tr>
											<%
												}
											%>
										</tbody>
									</table>
								</fieldset>

							</div>


						</div>
					</div>

				</div>
			</div>
		</div>
		<!-- 匹配支出对话框 -->
		<div class="modal fade" id="matchDialog">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
						</button>
						<h4 class="modal-title">
							请选择匹配的收入项
						</h4>
					</div>
					<div class="modal-body">
						<iframe id="matchIframe" name="matchIframe" frameborder=0></iframe>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">
							关闭
						</button>
					</div>
				</div>
			</div>
		</div>
		<!-- 匹配支出对话框 -->
	</body>
</html>