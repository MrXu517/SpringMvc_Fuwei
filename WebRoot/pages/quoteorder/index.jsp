<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.QuoteOrder"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.commons.Pager"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.commons.SystemCache"%>
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
%>
<!DOCTYPE html>

<html>
	<head>
		<base href="<%=basePath%>">
		<title>样品管理 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8" />
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">

		<script src="js/plugins/jquery-1.10.2.min.js"></script>
		<script src="<%=basePath%>js/plugins/WdatePicker.js"></script>
		<script src="js/common/common.js" type="text/javascript"></script>
		<link href="css/sample/sample.css" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<%@ include file="../common/head.jsp"%>
		<div id="Content">
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
								<ul class="pagination">
									<li>
										<a
											href="quoteorder/index?start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=1">«</a>
									</li>

									<%
										if (pager.getPageNo() > 1) {
									%>
									<li class="">
										<a
											href="quoteorder/index?start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=<%=pager.getPageNo() - 1%>">上一页
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
											href="quoteorder/index?start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=<%=pager.getPageNo() %>"><%=pager.getPageNo()%><span
											class="sr-only"></span> </a>
									</li>
									<li>
										<%
											if (pager.getPageNo() < pager.getTotalPage()) {
										%>
									
									<li class="">
										<a
											href="quoteorder/index?start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=<%=pager.getPageNo() + 1%>">下一页
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
											href="quoteorder/index?start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=<%=pager.getTotalPage()%>">»</a>
									</li>
								</ul>
								<form class="form-inline pageform form-horizontal" role="form"
									action="quoteorder/index?start_time=<%=start_time_str %>&end_time=<%=end_time_str %>">
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
								<form class="form-horizontal searchform form-inline" role="form">
									<input type="hidden" name="page" id="page"
										value="<%=pager.getPageNo()%>" />
									<div class="form-group">
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
							</div>
							<table class="table table-responsive">
								<thead>
									<tr>
										<th>
											序号
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
										<td><%=SystemCache.getCompanyName(quoteorder.getCompanyId()) %></td>
										<td><%=SystemCache.getSalesmanName(quoteorder.getSalesmanId())%></td>
										<td><%=quoteorder.getCreated_at()%></td>
										<td>
											<a href="quoteorder/detail/<%=quoteorder.getId()%>">详情</a>
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