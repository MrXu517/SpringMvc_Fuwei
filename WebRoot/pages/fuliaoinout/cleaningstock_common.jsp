<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.commons.Pager"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.fuwei.entity.Employee"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/";
	Pager pager = (Pager) request.getAttribute("pager");
	if (pager == null) {
		pager = new Pager();
	}
	List<Map<String,Object>> currentStocklist = new ArrayList<Map<String,Object>>();
	if (pager != null & pager.getResult() != null) {
		currentStocklist = (List<Map<String,Object>>) pager.getResult();
	}

	String locationNumber = (String) request.getAttribute("locationNumber");
	String locationNumber_str = "";
	if (locationNumber != null) {
		locationNumber_str = String.valueOf(locationNumber);
	}
%>
<!DOCTYPE html>

<html>
	<head>
		<base href="<%=basePath%>">
		<title>手动清通用辅料库存 -- 桐庐富伟针织厂</title>
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
		<script src="js/common/common.js" type="text/javascript"></script>
		<script src="js/fuliaoinout/cleaningstock.js" type="text/javascript"></script>
		<link rel="stylesheet" type="text/css" href="css/plugins/select2.min.css" />
		<link href="css/store_in_out/index.css" rel="stylesheet"
			type="text/css" />
		<style type="text/css">
		#submitBtn{margin-bottom:3px;}
		.checkbtn,#checkAll{height: 20px;width: 20px;}
		.table tr.selected{background: #DCD773 !important;}
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
						<li>
							<a href="fuliao_workspace/commonfuliao_workspace">通用辅料工作台</a>
						</li>
						<li class="active">
							手动清通用辅料库存
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 tablewidget">
								<p class="">提示：请打勾选择需要清空的库存，选择好后，请点击 “清空选中库位的辅料库存” 按钮，则执行清空选中库位的操作</p>
								<!-- Table -->
								<button id="submitBtn" class="btn btn-danger">清空选中库位的辅料库存</button>
								<div clas="navbar navbar-default">
									<form class="form-horizontal searchform form-inline searchform"
										role="form">
										<button class="btn btn-primary pull-right" type="submit" id="searchBtn" style="margin-left: 15px;">
											搜索
										</button>
										<input type="hidden" name="page" value="1">
										<div class="form-group salesgroup">
											<label for="locationNumber" class="col-sm-3 control-label" style="width:auto;">
												库位编号
											</label>
											<div class="col-sm-9">
												<input class="form-control" type="text" name="locationNumber" id="locationNumber" value="<%=locationNumber_str%>" />
											</div>
										</div>
										</form>
										
									<ul class="pagination">
										<li>
											<a
												href="fuliao_workspace/cleaningstock_common?locationNumber=<%=locationNumber_str%>&page=1">«</a>
										</li>

										<%
											if (pager.getPageNo() > 1) {
										%>
										<li class="">
											<a
												href="fuliao_workspace/cleaningstock_common?locationNumber=<%=locationNumber_str%>&page=<%=pager.getPageNo() - 1%>">上一页
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
												href="fuliao_workspace/cleaningstock_common?locationNumber=<%=locationNumber_str%>&page=<%=pager.getPageNo()%>"><%=pager.getPageNo()%>/<%=pager.getTotalPage()%>，共<%=pager.getTotalCount()%>条<span
												class="sr-only"></span> </a>
										</li>
										<li>
											<%
												if (pager.getPageNo() < pager.getTotalPage()) {
											%>
										
										<li class="">
											<a
												href="fuliao_workspace/cleaningstock_common?locationNumber=<%=locationNumber_str%>&page=<%=pager.getPageNo() + 1%>">下一页
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
												href="fuliao_workspace/cleaningstock_common?locationNumber=<%=locationNumber_str%>&page=<%=pager.getTotalPage()%>">»</a>
										</li>
									</ul>

								</div>

								<table class="table table-responsive table-bordered">
									<thead>
										<tr style="height:0;">
											<th style="width:20px"></th>
    										<th style="width:50px"></th>
    										<th style="width:50px"></th>
											<th style="width:60px"></th>
    										<th style="width:50px"></th>
    										<th style="width:50px"></th>
    										<th style="width:50px"></th>
    										<th style="width:120px"></th>
    										<th style="width:55px"></th>
    										<th style="width:70px"></th>
    										<th style="width:60px"></th>
    										<th style="width:50px"></th>
    										<th style="width:50px"></th>
    										<th style="width:50px"></th>
    										<th style="width:40px"></th>
  										</tr>
										<tr>
											<th rowspan="2" width="20px" style="padding: 0;">
												<input type="checkbox" id="checkAll"/><br>No.
											</th>
											<th rowspan="2" width="50px">
												库位编号
											</th>
											<th rowspan="2" width="50px">
												库位容量
											</th>
											<th rowspan="2" width="60px">
												库存数量
											</th>
											<th colspan="11" width="500px">
												库位所放置的辅料相关属性
											</th>
											</tr>
											<tr>
											<th width="50px">
												公司
											</th>
											<th width="50px">
												业务员
											</th>
											<th width="50px">客户</th>
											<th width="120px">
												图片
											</th>
											<th width="55px">
												辅料类型/编号
											</th>
											<th width="70px">
												公司订单号
											</th>
											<th width="60px">
												公司货号
											</th>
											<th width="50px">
												国家
											</th>
											<th width="50px">
												颜色
											</th><th width="50px">
												尺码
											</th><th width="40px">
												批次
											</th></tr>
									</thead>
									<tbody>
										<%
											int i = (pager.getPageNo()-1) * pager.getPageSize() + 0;
											for (Map<String,Object> detail : currentStocklist) {
												boolean even = i%2 == 0;
												String classname = even?"even":"odd";
												int location_size = (Integer)detail.get("l_size");
												String location_size_str = "";
												if(location_size == 3){
													location_size_str = "大";
												}else if(location_size == 2){
													location_size_str = "中";
												}else if(location_size == 1){
													location_size_str = "小";
												}else{
													location_size_str = "其他";
												}
										%>
										<tr class="<%=classname%>">
											<td><%=++i%><br><input locationId="<%=detail.get("locationId")%>" type="checkbox" class="checkbtn"/></td>
											<td><%=detail.get("number")%></td>
											<td><%=location_size_str %></td>
											<td><%=detail.get("quantity")%></td>
											<td><%=SystemCache.getCompanyShortName((Integer)detail.get("companyId"))%></td>
											<td><%=SystemCache.getSalesmanName((Integer)detail.get("salesmanId"))%></td>
											<td><%=SystemCache.getCustomerName((Integer)detail.get("customerId"))%></td>
											<td><a href="/<%=detail.get("img")%>" class="" target="_blank"> <img id="previewImg"
													alt="200 x 100%" src="/<%=detail.get("img_ss")%>">
												</a></td>
											<td><a target="_top" href="fuliao/detail/<%=detail.get("id")%>"><%=SystemCache.getFuliaoTypeName((Integer)detail.get("fuliaoTypeId"))%></a><br><%=detail.get("fnumber")%></td>
											<td><%=detail.get("company_orderNumber")%></td>
											<td><%=detail.get("company_productNumber")%></td>
											<td><%=detail.get("country")%></td>
											<td><%=detail.get("color")%></td>
											<td><%=detail.get("size")%></td>
											<td><%=detail.get("batch")%></td>
											
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