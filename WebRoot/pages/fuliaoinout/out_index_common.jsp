<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.financial.Expense_income"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.commons.Pager"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="com.fuwei.entity.Employee"%>
<%@page import="com.fuwei.entity.producesystem.FuliaoOut"%>
<%@page import="com.fuwei.entity.producesystem.FuliaoOutDetail"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/";
	Pager pager = (Pager) request.getAttribute("pager");
	if (pager == null) {
		pager = new Pager();
	}
	List<FuliaoOut> inOutlist = new ArrayList<FuliaoOut>();
	if (pager != null & pager.getResult() != null) {
		inOutlist = (List<FuliaoOut>) pager.getResult();
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

	
	String number = (String) request.getAttribute("number");
	String number_str = "";
	if (number != null) {
		number_str = String.valueOf(number);
	}
	
%>
<!DOCTYPE html>

<html>
	<head>
		<base href="<%=basePath%>">
		<title>通用辅料出库列表 -- 桐庐富伟针织厂</title>
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
		<script type='text/javascript' src='js/plugins/select2.min.js'></script>
		<link rel="stylesheet" type="text/css" href="css/plugins/select2.min.css" />
		<link href="css/store_in_out/index.css" rel="stylesheet"
			type="text/css" />
		
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
									<form class="form-horizontal searchform form-inline searchform"
										role="form">
										<button class="btn btn-primary pull-right" type="submit" id="searchBtn">
											搜索
										</button>
										<input type="hidden" name="page" value="1">
										<div class="form-group salesgroup">
											<label for="number" class="col-sm-3 control-label" style="width:auto;">
												出库单号
											</label>
											<div class="col-sm-9">
												<input class="form-control" type="text" name="number" id="number" value="<%=number_str%>" />
											</div>
										</div>
										<div class="form-group timegroup">
											<label class="col-sm-3 control-label">
												出库时间
											</label>

											<div class="input-group col-md-9">
												<input type="text" name="start_time" id="start_time"
													class="date form-control" value="<%=start_time_str%>" />
												<span class="input-group-addon">到</span>
												<input type="text" name="end_time" id="end_time"
													class="date form-control" value="<%=end_time_str%>">
											</div>
										</div>
										
										</form>
										
									<ul class="pagination">
										<li>
											<a
												href="fuliaoout/index_common?number=<%=number_str%>&start_time=<%=start_time_str%>&end_time=<%=end_time_str%>&page=1">«</a>
										</li>

										<%
											if (pager.getPageNo() > 1) {
										%>
										<li class="">
											<a
												href="fuliaoout/index_common?number=<%=number_str%>&start_time=<%=start_time_str%>&end_time=<%=end_time_str%>&page=<%=pager.getPageNo() - 1%>">上一页
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
												href="fuliaoout/index_common?number=<%=number_str%>&start_time=<%=start_time_str%>&end_time=<%=end_time_str%>&page=<%=pager.getPageNo()%>"><%=pager.getPageNo()%>/<%=pager.getTotalPage()%>，共<%=pager.getTotalCount()%>条<span
												class="sr-only"></span> </a>
										</li>
										<li>
											<%
												if (pager.getPageNo() < pager.getTotalPage()) {
											%>
										
										<li class="">
											<a
												href="fuliaoout/index_common?number=<%=number_str%>&start_time=<%=start_time_str%>&end_time=<%=end_time_str%>&page=<%=pager.getPageNo() + 1%>">下一页
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

										<li></li>
										<li>
											<a
												href="fuliaoout/index_common?number=<%=number_str%>&start_time=<%=start_time_str%>&end_time=<%=end_time_str%>&page=<%=pager.getTotalPage()%>">»</a>
										</li>
									</ul>

								</div>

								<table class="table table-responsive table-bordered">
									<thead>
										<tr style="height:0;">
											<th style="width:20px"></th>
    										<th style="width:60px"></th>
											<th style="width:40px"></th>
    										<th style="width:55px"></th>
    										<th style="width:55px"></th>
    										<th style="width:55px"></th>
    										<th style="width:55px"></th>
    										<th style="width:55px"></th>
    										<th style="width:60px"></th>
    										<th style="width:40px"></th>
    										<th style="width:60px"></th>
    										<th style="width:50px"></th>
    										<th style="width:60px"></th>
    										<th style="width:30px"></th>
    										<th style="width:40px"></th>
  										</tr>
										<tr>
											<th rowspan="2"  width="20px" style="padding: 0;">
												No.
											</th>
											<th rowspan="2" width="60px">
												出库单号
											</th>
											<th rowspan="2" width="40px">
												领取人
											</th><th colspan="9" width="165px">通用辅料出库列表</th>
											<th rowspan="2" width="60px">
												出库时间
											</th>
											<th rowspan="2" width="30px">
												打印
											</th>
											<th rowspan="2" width="40px">
												操作
											</th>
										</tr><tr><th width="55px">
												类型
											</th><th width="55px">
												订单号
											</th><th width="55px">
												款号
											</th><th width="55px">
												国家
											</th><th width="55px">
												颜色
											</th><th  width="60px">
												尺码
											</th><th  width="40px">
												批次
											</th><th  width="60px">
												数量(个)
											</th><th  width="50px">
												库位
											</th></tr>
									</thead>
									<tbody>
										<%
											int i = (pager.getPageNo()-1) * pager.getPageSize() + 0;
																											for (FuliaoOut item : inOutlist) {
																												boolean even = i%2 == 0;
																												String classname = even?"even":"odd";
																												List<FuliaoOutDetail> detailist = item.getDetaillist();
																												if(detailist == null){detailist = new ArrayList<FuliaoOutDetail>();}
																												int detailsize = detailist.size();
										%>
										<tr itemId="<%=item.getId()%>" class="<%=classname%>">
											<td rowspan="<%=detailsize%>"><%=++i%></td>
											
											<td rowspan="<%=detailsize%>"><a target="_blank" href="fuliaoout/detail/<%=item.getId()%>"><%=item.getNumber()%></a></td>
											<td rowspan="<%=detailsize%>"><%=SystemCache.getEmployeeName(item.getReceiver_employee())%></td>
											
											
											<td><%=SystemCache.getFuliaoTypeName(detailist.get(0).getFuliaoTypeId())%></td>
											<td><%=detailist.get(0).getCompany_orderNumber()%></td>
											<td><%=detailist.get(0).getCompany_productNumber()%></td>
											<td><%=detailist.get(0).getCountry()%></td>
											<td><%=detailist.get(0).getColor()%></td>
											<td><%=detailist.get(0).getSize()%></td>
											<td><%=detailist.get(0).getBatch()%></td>
											<td><%=detailist.get(0).getQuantity()%></td>
											<td><%=SystemCache.getLocationNumber(detailist.get(0).getLocationId())%></td>

										
											<td rowspan="<%=detailsize%>"><%=DateTool.formatDateYMD(item.getCreated_at())%></td>				
											<td rowspan="<%=detailsize%>"><%=item.printStr()%></td>	
											
											<td rowspan="<%=detailsize%>">
												<a target="_blank" href="fuliaoout/detail/<%=item.getId()%>">详情</a>
												
											</td>
										</tr>
										<%
											detailist.remove(0);
											for(FuliaoOutDetail detail : detailist){
										%>
										<tr class="<%=classname %>">
											<td><%=SystemCache.getFuliaoTypeName(detail.getFuliaoTypeId())%></td>
											<td><%=detail.getCompany_orderNumber()%></td>
											<td><%=detail.getCompany_productNumber()%></td>
											<td><%=detail.getCountry()%></td>
											<td><%=detail.getColor()%></td>
											<td><%=detail.getSize()%></td>
											<td><%=detail.getBatch()%></td>
											<td><%=detail.getQuantity()%></td>
											<td><%=SystemCache.getLocationNumber(detail.getLocationId())%></td>

										</tr>
										<%} %>
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