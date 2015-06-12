<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.financial.Invoice"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.entity.financial.Bank"%>
<%@page import="com.fuwei.commons.Pager"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Pager pager = (Pager) request.getAttribute("pager");
	if (pager == null) {
		pager = new Pager();
	}
	List<Invoice> invoicelist = new ArrayList<Invoice>();
	if (pager != null & pager.getResult() != null) {
		invoicelist = (List<Invoice>) pager.getResult();
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


	Integer bank_id = (Integer) request.getAttribute("bank_id");
	String bank_str = "";
	if (bank_id != null) {
		bank_str = String.valueOf(bank_id);
	}
	if (bank_id == null) {
		bank_id = -1;
	}
	
	Double amount_from = (Double) request.getAttribute("amount_from");
	String amount_from_str = "";
	if (amount_from != null) {
		amount_from_str = amount_from.toString();
	}
	Double amount_to = (Double) request.getAttribute("amount_to");
	String amount_to_str = "";
	if (amount_to != null) {
		amount_to_str = amount_to.toString();
	}

	String number = (String)request.getAttribute("number");
	String number_str = "";
	if (number != null) {
		number_str = number.toString();
	}

	
	List<Bank> banklist = (List<Bank>) request.getAttribute("banklist");

	//权限相关
	Boolean has_item_delete = SystemCache.hasAuthority(session,
			"invoice/detail");
	//权限相关
%>
<!DOCTYPE html>

<html>
	<head>
		<base href="<%=basePath%>">
		<title>收入支出 -- 桐庐富伟针织厂</title>
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
		<script src="js/financial/workspace/purchase_invoice.js" type="text/javascript"></script>
		<script type='text/javascript' src='js/plugins/select2.min.js'></script>
	<link rel="stylesheet" type="text/css" href="css/plugins/select2.min.css" />
		<link href="css/financial/workspace.css" rel="stylesheet"
			type="text/css" />
        <style type="text/css">
		.timegroup .control-label+div {
  width: 270px;
}.amountgroup .control-label+div {
  width: 260px;
}
#number{
	width:120px;
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
											<div clas="navbar navbar-default">
												<form
													class="form-horizontal searchform form-inline searchform"
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

													<div class="form-group timegroup">
														<label class="col-sm-3 control-label">
															开票日期
														</label>

														<div class="input-group col-md-9">
															<input type="text" name="start_time" id="start_time"
																class="date form-control" value="<%=start_time_str%>" />
															<span class="input-group-addon">到</span>
															<input type="text" name="end_time" id="end_time"
																class="date form-control" value="<%=end_time_str%>">
														</div>
													</div>
													
													<div class="form-group amountgroup">
														<label class="col-sm-3 control-label">
															金额
														</label>

														<div class="input-group col-md-9">
															<input type="text" name="amount_from" id="amount_from"
																class="double form-control" value="<%=amount_from_str%>" />
															<span class="input-group-addon">到</span>
															<input type="text" name="amount_to" id="amount_to"
																class="double form-control" value="<%=amount_to_str%>">


														</div>
													</div>
													<div class="form-group">
														<label class="col-sm-3 control-label">
															发票号
														</label>

														<div class="input-group col-md-9">
															<input type="text" name="number" id="number"
																class="form-control" value="<%=number_str%>" />
														</div>
													</div>
													
												</form>
												<br>
												<button class="btn btn-primary" type="button" id="searchBtn">
														搜索
													</button>
												<a href="purchase_invoice/import" class="btn btn-primary" target="_top">批量导入</a>
												<button class="btn btn-primary" type="button" id="matchBtn">
														批量匹配
													</button>
												<ul class="pagination">
													<li>
														<a
															href="purchase_invoice/index?bank_id=<%=bank_str%>&number=<%=number_str%>&amount_from=<%=amount_from_str%>&amount_to=<%=amount_to_str%>&start_time=<%=start_time_str%>&end_time=<%=end_time_str%>&page=1">«</a>
													</li>

													<%
														if (pager.getPageNo() > 1) {
													%>
													<li class="">
														<a
															href="purchase_invoice/index?bank_id=<%=bank_str%>&number=<%=number_str%>&amount_from=<%=amount_from_str%>&amount_to=<%=amount_to_str%>&start_time=<%=start_time_str%>&end_time=<%=end_time_str%>&page=<%=pager.getPageNo() - 1%>">上一页
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
															href="purchase_invoice/index?bank_id=<%=bank_str%>&number=<%=number_str%>&amount_from=<%=amount_from_str%>&amount_to=<%=amount_to_str%>&start_time=<%=start_time_str%>&end_time=<%=end_time_str%>&page=<%=pager.getPageNo()%>"><%=pager.getPageNo()%>/<%=pager.getTotalPage()%>，共<%=pager.getTotalCount()%>条<span
															class="sr-only"></span> </a>
													</li>
													<li>
														<%
															if (pager.getPageNo() < pager.getTotalPage()) {
														%>
													
													<li class="">
														<a
															href="purchase_invoice/index?bank_id=<%=bank_str%>&number=<%=number_str%>&amount_from=<%=amount_from_str%>&amount_to=<%=amount_to_str%>&start_time=<%=start_time_str%>&end_time=<%=end_time_str%>&page=<%=pager.getPageNo() + 1%>">下一页
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
															href="purchase_invoice/index?bank_id=<%=bank_str%>&number=<%=number_str%>&amount_from=<%=amount_from_str%>&amount_to=<%=amount_to_str%>&start_time=<%=start_time_str%>&end_time=<%=end_time_str%>&page=<%=pager.getTotalPage()%>">»</a>
													</li>
												</ul>

											</div>

											<table class="table table-responsive">
												<thead>
													<tr>
														<th width="20px">
															No.
														</th>
														<th width="120px">
															对方账户
														</th>
														<th width="60px">
															金额
														</th>
														
														<th width="60px">
															发票号
														</th>
														<th width="70px">
															类型
														</th>
														<th width="55px">
															开票日期
														</th>
														<th width="60px">
															更新时间
														</th>
														<th width="60px">
															备注
														</th><th width="60px">
															匹配金额
														</th>
														<th width="100px">
															操作
														</th>
													</tr>
												</thead>
												<tbody>
													<%
														int i = (pager.getPageNo() - 1) * pager.getPageSize() + 0;
														for (Invoice item : invoicelist) {
													%>
													<tr itemId="<%=item.getId()%>">
														<td><%=++i%></td>
														
														
														<td><%=item.getBank_name()%></td>
														<td><%=item.getAmount()%></td>
														<td><%=item.getNumber()%></td>
														<td><%=item.getTypeString()%></td>
														<td><%=DateTool.formatDateYMD(item.getPrint_date())%></td>
														<td><%=DateTool.formatDateYMD(item.getCreated_at())%></td>
														<td><%=item.getMemo()%></td><td><%=item.getMatch_amount()%></td>
														<td>
													<%if(item.isMatched()){ %>
														<span class="label label-success">已匹配完成</span>
													<%}else{ %><a data-cid="<%=item.getId()%>" class="match" href="#">匹配支出</a>
															|
													<%} %>
											
															<a target="_blank"
																href="purchase_invoice/detail/<%=item.getId()%>">详情</a>

															<%
																if (has_item_delete) {
															%>
															|
															<a  data-number="<%=item.getNumber() %>" data-cid="<%=item.getId()%>" class="delete" href="#">删除</a>
															<%
																}
															%>

														</td>
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

<!-- 匹配支出对话框 -->
		<div class="modal fade" id="matchDialog">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
						</button>
						<h4 class="modal-title">
							请选择匹配的支出项
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


