<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.QuoteOrder"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.commons.Pager"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="net.sf.json.JSONObject"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	//List<User> userlist = (List<User>) request.getAttribute("userlist");
	Pager pager = (Pager) request.getAttribute("pager");
	if (pager == null) {
		pager = new Pager();
	}
	List<QuoteOrder> quoteOrderlist = new ArrayList<QuoteOrder>();
	if (pager != null & pager.getResult() != null) {
		quoteOrderlist = (List<QuoteOrder>) pager.getResult();
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
	Integer salesmanId = (Integer)request.getAttribute("salesmanId");
	Integer companyId = (Integer)request.getAttribute("companyId");
	String company_str = "";
	String salesman_str = "";
	if(salesmanId!=null){
		salesman_str = String.valueOf(salesmanId);
	}
	if(companyId!=null){
		company_str = String.valueOf(companyId);
	}
	if(salesmanId == null){
		salesmanId = -1;
	}
	if(companyId == null){
		companyId = -1;
	}
	HashMap<String, List<Salesman>> companySalesmanMap = SystemCache
			.getCompanySalesmanMap_ID();
	JSONObject jObject = new JSONObject();
	jObject.put("companySalesmanMap", companySalesmanMap);
	String companySalesmanMap_str = jObject.toString();
%>
<!DOCTYPE html>

<html>
	<head>
		<base href="<%=basePath%>">
		<title>报价单列表 -- 桐庐富伟针织厂</title>
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
		<link href="css/sample/sample.css" rel="stylesheet" type="text/css" />
		<script src="js/quoteorder/index.js" type="text/javascript"></script>
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
							报价单列表
						</li>
					</ul>
				</div>
				<div class="body">

					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 tablewidget">
								<!-- Table -->
								<div clas="navbar navbar-default">
									<form
										class="form-horizontal searchform form-inline quoteorderform"
										role="form">
										<input type="hidden" name="page" id="page"
											value="<%=pager.getPageNo()%>" />
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
													if(companyId == company.getId()){
											%>
													<option value="<%=company.getId()%>" selected><%=company.getFullname()%></option>
													<%}
		else{ %>
													<option value="<%=company.getId()%>"><%=company.getFullname()%></option>
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
													if(salesmanId == salesman.getId()){
											%>
													<option value="<%=salesman.getId()%>" selected><%=salesman.getName()%></option>
													<%}
		else{ %>
													<option value="<%=salesman.getId()%>"><%=salesman.getName()%></option>
													<%
												}
												}
											%>
												</select>
											</div>
										</div>
										<div class="form-group timegroup">
											<label class="col-sm-3 control-label">
												创建时间
											</label>

											<div class="input-group col-md-9">
												<input type="text" name="start_time" id="start_time"
													class="date form-control" value="<%=start_time_str%>" />
												<span class="input-group-addon">到</span>
												<input type="text" name="end_time" id="end_time"
													class="date form-control" value="<%=end_time_str%>">

												<span class="input-group-btn">
													<button class="btn btn-primary" type="submit">
														搜索
													</button> </span>
											</div>
										</div>
									</form>
									<ul class="pagination">
										<li>
											<a
												href="quoteorder/index?companyId=<%=company_str %>&salesmanId=<%=salesman_str %>&start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=1">«</a>
										</li>

										<%
										if (pager.getPageNo() > 1) {
									%>
										<li class="">
											<a
												href="quoteorder/index?companyId=<%=company_str %>&salesmanId=<%=salesman_str %>&start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=<%=pager.getPageNo() - 1%>">上一页
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
												href="quoteorder/index?companyId=<%=company_str %>&salesmanId=<%=salesman_str %>&start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=<%=pager.getPageNo() %>"><%=pager.getPageNo()%><span
												class="sr-only"></span> </a>
										</li>
										<li>
											<%
											if (pager.getPageNo() < pager.getTotalPage()) {
										%>
										
										<li class="">
											<a
												href="quoteorder/index?companyId=<%=company_str %>&salesmanId=<%=salesman_str %>&start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=<%=pager.getPageNo() + 1%>">下一页
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
												href="quoteorder/index?companyId=<%=company_str %>&salesmanId=<%=salesman_str %>&start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=<%=pager.getTotalPage()%>">»</a>
										</li>
									</ul>
									<form class="form-inline pageform form-horizontal" role="form"
										action="quoteorder/index">

										<input type="hidden" name="salesmanId" id="salesmanId"
											value="<%=salesman_str %>" />
										<input type="hidden" name="companyId" id="companyId"
											value="<%=company_str %>" />
										<input type="hidden" name="start_time" id="start_time"
											value="<%=start_time_str %>">
										<input type="hidden" name="end_time" id="end_time"
											value="<%=end_time_str %>">
										<div class="form-group">
											<div class="input-group">
												<span class="input-group-addon">去第</span>
												<input type="text" name="page" id="page"
													class="int form-control" placeholder="1,2,..."
													value="<%=pager.getPageNo() %>">

												<span class="input-group-addon">页</span>
												<span class="input-group-btn">
													<button class="btn btn-primary" type="submit">
														Go!
													</button> </span>
											</div>
										</div>
									</form>

								</div>
								<table class="table table-responsive">
									<thead>
										<tr>
											<th>
												序号
											</th>
											<th>
												报价单号
											</th>
											<th>
												公司
											</th>
											<th>
												业务员
											</th>
											<th>
												创建时间
											</th>
											<th>
												操作
											</th>
										</tr>
									</thead>
									<tbody>
										<%
										int i = 0;
										for (QuoteOrder quoteorder : quoteOrderlist) {
									%>
										<tr quoteorderId="<%=quoteorder.getId()%>">
											<td><%=++i%></td>
											<td><%=quoteorder.getQuotationNumber()%></td>
											<td><%=SystemCache.getCompanyName(quoteorder.getCompanyId()) %></td>
											<td><%=SystemCache.getSalesmanName(quoteorder.getSalesmanId())%></td>
											<td><%=quoteorder.getCreated_at()%></td>
											<td>
												<a href="quoteorder/detail/<%=quoteorder.getId()%>">详情</a>
												 | <a href="order/add/<%=quoteorder.getId()%>">创建订单</a>
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
	</body>
</html>
<script type="text/javascript">
	$(document).ready( function() {
		/*设置当前选中的页*/
		var $a = $("#left li a[href='quoteorder/index']");
		setActiveLeft($a.parent("li"));
		/*设置当前选中的页*/
	});
</script>