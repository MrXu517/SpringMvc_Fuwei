<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.commons.Pager"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.fuwei.entity.finishstore.PackingOrder"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Pager pager = (Pager) request.getAttribute("pager");
	if (pager == null) {
		pager = new Pager();
	}
	List<PackingOrder> PackingOrderlist = new ArrayList<PackingOrder>();
	if (pager != null & pager.getResult() != null) {
		PackingOrderlist = (List<PackingOrder>) pager.getResult();
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

	
	Integer companyId = (Integer) request.getAttribute("companyId");
	String company_str = "";
	
	if (companyId != null) {
		company_str = String.valueOf(companyId);
	}
	
	if (companyId == null) {
		companyId = -1;
	}
	String orderNumber = (String) request.getAttribute("orderNumber");
	if (orderNumber == null) {
		orderNumber = "";
	}
	
	//权限相关
	Boolean has_order_detail = SystemCache.hasAuthority(session,
			"packing_order/detail");
	
	//权限相关
%>
<!DOCTYPE html>

<html>
	<head>
		<base href="<%=basePath%>">
		<title>查询装箱单 -- 桐庐富伟针织厂</title>
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

		<link href="css/order/index.css" rel="stylesheet" type="text/css" />
		<style type="text/css">
		.tablewidget>table th,.tablewidget>table td,.table>thead>tr>th{
			 text-align: center;
			 border: 1px solid #000;
		}
		.table>thead>tr>th{background: #AEADAD;}
		.tablewidget>table{border: 1px solid #000;}
		</style>
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
							查询装箱单
						</li>
					</ul>
				</div>
				<div class="body">

					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 tablewidget">
								<!-- Table -->
								<div clas="navbar navbar-default">
									<form class="form-horizontal form-inline searchform"
										role="form">
										<input type="hidden" name="page" id="page"
											value="<%=1%>" />
										<div class="form-group salesgroup">
											<label for="number" class="col-sm-3 control-label">
												订单号
											</label>
											<div class="col-sm-9">
												<input class="form-control" type="text" name="orderNumber" id="orderNumber" value="<%=orderNumber %>" />
											</div>
										</div>
										<div class="form-group salesgroup">
											<label for="companyId" class="col-sm-3 control-label">
												公司
											</label>
											<div class="col-sm-9">
												<select class="form-control" name="companyId" id="companyId"
													placeholder="公司">
													<option value="">
														所有
													</option>
													<%
														for (Company company : SystemCache.companylist) {
															if (companyId == company.getId()) {
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
									

										<div class="form-group timegroup">
											<label class="col-sm-3 control-label">
												制单时间
											</label>

											<div class="input-group col-md-9">
												<input type="text" name="start_time" id="start_time"
													class="date form-control" value="<%=start_time_str%>" />
												<span class="input-group-addon">到</span>
												<input type="text" name="end_time" id="end_time"
													class="date form-control" value="<%=end_time_str%>">
											</div>
										</div>
										<button class="btn btn-primary" type="submit">
														搜索
													</button> 
									</form>
									<ul class="pagination">
										<li>
											<a
												href="packing_order/index?orderNumber=<%=orderNumber %>&companyId=<%=company_str %>&start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=1">«</a>
										</li>

										<%
										if (pager.getPageNo() > 1) {
									%>
										<li class="">
											<a
												href="packing_order/index?orderNumber=<%=orderNumber %>&companyId=<%=company_str %>&start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=<%=pager.getPageNo() - 1%>">上一页
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
												href="packing_order/index?orderNumber=<%=orderNumber %>&companyId=<%=company_str %>&start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=<%=pager.getPageNo() %>"><%=pager.getPageNo()%>/<%=pager.getTotalPage()%>，共<%=pager.getTotalCount()%>条<span
												class="sr-only"></span> </a>
										</li>
										<li>
											<%
											if (pager.getPageNo() < pager.getTotalPage()) {
										%>
										
										<li class="">
											<a
												href="packing_order/index?orderNumber=<%=orderNumber %>&companyId=<%=company_str %>&start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=<%=pager.getPageNo() + 1%>">下一页
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
												href="packing_order/index?orderNumber=<%=orderNumber %>&companyId=<%=company_str %>&start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=<%=pager.getTotalPage()%>">»</a>
										</li>
									</ul>

								</div>

								<table class="table table-responsive">
									<thead>
										<tr>
											<th width="30px">
												序号
											</th>
											<th width="100px">
												图片
											</th>
											<th width="80px">
												品名
											</th>
											<th width="60px">
												订单号
											</th>
											<th width="40px">
												公司
											</th>
											<th width="60px">
												公司货号
											</th>
											<th width="60px">
												总数量
											</th>
											<th width="60px">
												总箱数
											</th>
											<th width="60px">
												立方数
											</th>
											<th width="60px">
												跟单人
											</th>
											<th width="60px">
												制单人/日期
											</th>
											<th width="40px">
												操作
											</th>
										</tr>
									</thead>
									<tbody>
										<%
											int i = (pager.getPageNo()-1) * pager.getPageSize() + 0;
											for (PackingOrder item : PackingOrderlist) {
										%>
										<tr orderId="<%=item.getId()%>">
											<td><%=++i%></td>
											<td
												style="max-width: 120px; height: 120px; max-height: 120px;">
												<a target="_blank" class="cellimg"
													href="/<%=item.getImg()%>"><img
														style="max-width: 120px; height: 120px; max-height: 120px;"
														src="/<%=item.getImg_ss()%>"> </a>
											</td>
											<td><%=item.getName() %></td>
											<td><%=item.getOrderNumber() %></td>
											<td><%=SystemCache.getCompanyShortName(item.getCompanyId())%></td>
											<td><%=item.getCompany_productNumber()%></td>
											<td><%=item.getQuantity()%></td>
											<td><%=item.getCartons()%></td>
											<td><%=item.getCapacity()%></td>
											<td><%=SystemCache.getEmployeeName(item.getCharge_employee())%></td>
											
											<td><%=SystemCache.getUserName(item
										.getCreated_user())%><br>
											<%=DateTool.formatDateYMD(item.getCreated_at())%></td>
											<td>
												<%
													if (has_order_detail) {
												%>
												<a target="_blank"
													href="packing_order/detail/<%=item.getId()%>">详情</a>
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

		<script type="text/javascript">
	/*设置当前选中的页*/
	var $a = $("#left li a[href='packing_order/index']");
	setActiveLeft($a.parent("li"));

	//删除单据 -- 开始
	$(".delete").click( function() {
		var id = $(this).attr("data-cid");
		if (!confirm("确定要删除该装箱单吗？")) {
			return false;
		}
		$.ajax( {
			url :"packing_order/delete/" + id,
			type :'POST'
		}).done( function(result) {
			if (result.success) {
				Common.Tip("删除装箱单成功", function() {
					location.reload();
				});
			}
		}).fail( function(result) {
			Common.Error("删除装箱单失败：" + result.responseText);
		}).always( function() {

		});
		return false;
	});
	//删除单据  -- 结束
</script>
	</body>
</html>