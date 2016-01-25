<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="com.fuwei.commons.Pager"%>
<%@page import="com.fuwei.entity.financial.ProduceBill"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.NumberUtil"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Pager pager = (Pager) request.getAttribute("pager");
	if (pager == null) {
		pager = new Pager();
	}
	List<ProduceBill> producebilllist = new ArrayList<ProduceBill>();
	if (pager != null & pager.getResult() != null) {
		producebilllist = (List<ProduceBill>) pager.getResult();
	}
	Integer factoryId = (Integer) request.getAttribute("factoryId");
	String factory_str = "";
	if (factoryId != null) {
		factory_str = String.valueOf(factoryId);
	}
	if (factoryId == null) {
		factoryId = -1;
	}
	Integer year = (Integer) request.getAttribute("year");
	String year_str = "";
	if (year != null) {
		year_str = String.valueOf(year);
	}
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>生产对账单列表 -- 桐庐富伟针织厂</title>
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
		<script src="<%=basePath%>js/plugins/WdatePicker.js"
			type="text/javascript"></script>
		<script src="js/plugins/bootstrap.min.js" type="text/javascript"></script>
		<script src="js/common/common.js" type="text/javascript"></script>
		<link href="css/store_in_out/index.css" rel="stylesheet" type="text/css" />
	</head>

	<body>
		<%@ include file="../../common/head.jsp"%>
	
		<div id="Content" class="auto_container">
			<div id="main">
				<div class="breadcrumbs" id="breadcrumbs">
					<ul class="breadcrumb">
						<li>
							<i class="fa fa-home"></i>
							<a href="user/index">首页</a>
						</li>
						<li class="active">
							生产对账单列表
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 btnWidget">
								
								<a href="producebill/add" type="button" class="btn btn-danger"><i class="fa fa-sign-in fa-lg"></i>对账</a>
							</div>
						</div>

						<div class="row">
							<div class="col-md-12 tablewidget">
								<!-- Table -->
								<div clas="navbar navbar-default">
									<form class="form-horizontal form-inline"
										role="form">
										<input type="hidden" name="page" value="1">
										<div class="form-group salesgroup">
											<label for="factoryId" class="col-sm-3 control-label">
												加工方
											</label>
											<div class="col-sm-8">
												<select class="form-control" name="factoryId" id="factoryId">
													<option value="">
														未选择
													</option>
													<%
														for (Factory factory : SystemCache.produce_factorylist) {
														if (factoryId != null && factoryId == factory.getId()) {
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
										
										<div class="form-group salesgroup">
											<label class="col-sm-3 control-label">
												账单年份
											</label>

											<div class="input-group col-md-9">
												<input type="text" name="year" id="year"
													class="date form-control require" value="<%=year_str%>" />
												<span class="input-group-addon">年</span>
											</div>
										</div>
										
										<button class="btn btn-primary" type="submit" id="searchBtn">
											搜索
										</button>
										</form>
										
									<ul class="pagination">
										<li>
											<a
												href="producebill/index?year=<%=year_str%>&factoryId=<%=factory_str%>&page=1">«</a>
										</li>

										<%
											if (pager.getPageNo() > 1) {
										%>
										<li class="">
											<a
												href="producebill/index?year=<%=year_str%>&factoryId=<%=factory_str%>&page=<%=pager.getPageNo() - 1%>">上一页
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
												href="producebill/index?year=<%=year_str%>&factoryId=<%=factory_str%>&page=<%=pager.getPageNo()%>"><%=pager.getPageNo()%>/<%=pager.getTotalPage()%>，共<%=pager.getTotalCount()%>条<span
												class="sr-only"></span> </a>
										</li>
										<li>
											<%
												if (pager.getPageNo() < pager.getTotalPage()) {
											%>
										
										<li class="">
											<a
												href="producebill/index?year=<%=year_str%>&factoryId=<%=factory_str%>&page=<%=pager.getPageNo() + 1%>">下一页
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
												href="producebill/index?year=<%=year_str%>&factoryId=<%=factory_str%>&page=<%=pager.getTotalPage()%>">»</a>
										</li>
									</ul>

								</div>

								<table class="table table-responsive table-bordered">
									<thead>
										<tr>
											<th  width="20px" style="padding: 0;">
												No.
											</th><th width="50px">
												加工方
											</th><th width="80px">
												总数量
											</th><th width="60px">
												扣款金额
											</th>
											<th width="100px">
												总金额（已减扣款）
											</th>
											<th width="80px">
												地税金额
											</th>
											<th width="120px">
												应付金额（总金额-地税）
											</th>
											<th width="50px">
												账单年份
											</th>
											<th width="40px">
												对账人
											</th>
											<th width="60px">
												对账时间
											</th>
											<th width="60px">
												操作
											</th>
										</tr>
									</thead>
									<tbody>
										<%
											int i = (pager.getPageNo()-1) * pager.getPageSize() + 0;
											for (ProduceBill item : producebilllist) {
												boolean even = i%2 == 0;
												String classname = even?"even":"odd";
										%>
										<tr itemId="<%=item.getId()%>" class="<%=classname%>">
											<td><a target="_blank" href="producebill/detail/<%=item.getId()%>"><%=++i%></a></td>
											<td><%=SystemCache.getFactoryName(item.getFactoryId())%></td>
											<td><%=item.getQuantity()%></td>
											<td><%=item.getDeduct()%></td>
											<td><%=NumberUtil.formateDouble(item.getAmount() - item.getDeduct(),2)%></td>
											<td><%=item.getRate_deduct()%></td>
											<td><%=item.getPayable_amount()%></td>
											<td><%=item.getYear()%></td>
											<td><%=SystemCache.getUserName(item.getCreated_user())%></td>
											<td><%=DateTool.formatDateYMD(item.getCreated_at())%></td>
											<td><a target="_blank" href="producebill/detail/<%=item.getId()%>">详情</a></td>
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
		</div>
	<script type="text/javascript">
			/*设置当前选中的页*/
			var $a = $("#left li a[href='producebill/index']");
			setActiveLeft($a.parent("li"));
			$("#year").bind("click focus", function() {
				WdatePicker({dateFmt:'yyyy'});
			});
		</script>
	</body>
</html>
