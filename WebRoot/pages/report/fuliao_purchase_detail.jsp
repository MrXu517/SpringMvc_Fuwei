<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.ordergrid.FuliaoPurchaseOrder"%>
<%@page import="com.fuwei.entity.ordergrid.FuliaoPurchaseOrderDetail"%>
<%@page import="com.fuwei.entity.Material"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.commons.Pager"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.constant.OrderStatus"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="net.sf.json.JSONObject"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	
	
	
	List<FuliaoPurchaseOrder> result = (List<FuliaoPurchaseOrder>)request.getAttribute("result");
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

	
	Integer factoryId = (Integer) request.getAttribute("factoryId");
	String factory_str = "";
	if (factoryId != null) {
		factory_str = String.valueOf(factoryId);
	}
	
	if (factoryId == null) {
		factoryId = -1;
	}
	
	

%>
<!DOCTYPE html>

<html>
	<head>
		<base href="<%=basePath%>">
		<title>辅料采购明细报表 -- 桐庐富伟针织厂</title>
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

		<link href="css/report/report.css" rel="stylesheet" type="text/css" />
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
							辅料采购明细报表
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
						
										<div class="form-group salesgroup">
											<label for="factoryId" class="col-sm-3 control-label">
												采购单位
											</label>
											<div class="col-sm-9">
												<select class="form-control require" name="factoryId" id="factoryId">
													<option value="">
														所有
													</option>
													<%
														for (Factory factory : SystemCache.purchase_factorylist) {
															if (factoryId == factory.getId()) {
													%>
													<option value="<%=factory.getId()%>" selected><%=factory.getName()%></option>
													<%
														} else {
													%>
													<option value="<%=factory.getId()%>"><%=factory.getName()%></option>
													<%
														}
														}
													%>
												</select>
											</div>
										</div>

										<div class="form-group timegroup">
											<label class="col-sm-3 control-label">
												订购时间
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
									
									<a target="_blank" href="report/fuliao_purchase_detail/export?factoryId=<%=factory_str %>&start_time=<%=start_time_str %>&end_time=<%=end_time_str %>" class="btn btn-primary">导出</a>
								</div>

								<table class="table table-responsive">
									<thead>
										<tr>
											<th>
												序号
											</th><th>
												日期
											</th><th>
												采购单位
											</th><th>
												采购单号
											</th><th>
												品名
											</th><th>
												公司
											</th><th>
												跟单
											</th><th>
												材料名
											</th><th>
												数量(kg)
											</th><th>
												颜色
											</th>
											
											
											
										</tr>
									</thead>
									<tbody>
										<%
											int i = 0 ;
											for (FuliaoPurchaseOrder fuliaoPurchaseOrder : result) {
												int count = 0 ;
												for (FuliaoPurchaseOrderDetail item : fuliaoPurchaseOrder.getDetaillist()) {
										%>
										<tr>
											<td><%=++i%></td>
											<%if(count==0){ %>
											<td><%=DateTool.formatDateYMD(fuliaoPurchaseOrder.getCreated_at())%></td>
											<td><%=SystemCache.getFactoryName(fuliaoPurchaseOrder.getFactoryId())%></td>
											<td><a href="fuliao_purchase_order/detail/<%=fuliaoPurchaseOrder.getId() %>"><%=fuliaoPurchaseOrder.getNumber()%></a></td>
											<td><%=fuliaoPurchaseOrder.getName()%></td>
											<td><%=SystemCache.getCompanyShortName(fuliaoPurchaseOrder.getCompanyId())%></td>
											<td><%=SystemCache.getEmployeeName(fuliaoPurchaseOrder.getCharge_employee())%></td>
											<%}else{ %>
											<td></td><td></td><td></td><td></td><td></td><td></td>
											<%} %>
											
											<td><%=SystemCache.getMaterialName(item.getStyle())%></td>
											<td><%=item.getQuantity()%></td>
											<td></td>
										</tr>
										<%
										++count;
											}
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
	var $a = $("#left li a[href='report/fuliao_purchase_detail']");
	setActiveLeft($a.parent("li"));

</script>
	</body>
</html>