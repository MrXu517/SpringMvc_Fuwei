<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.producesystem.Fuliao"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.fuwei.entity.Customer"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.entity.Company"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	List<Fuliao> fuliaolist = (List<Fuliao>) request.getAttribute("fuliaolist");
	if (fuliaolist == null) {
		fuliaolist = new ArrayList<Fuliao>();
	}
	HashMap<String, List<Salesman>> companySalesmanMap = SystemCache
			.getCompanySalesmanMap_ID();
	JSONObject jObject = new JSONObject();
	jObject.put("companySalesmanMap", companySalesmanMap);
	String companySalesmanMap_str = jObject.toString();
	
	Integer salesmanId = (Integer) request.getAttribute("salesmanId");
	Integer companyId = (Integer) request.getAttribute("companyId");
	Integer customerId = (Integer) request.getAttribute("customerId");
	String memo = (String) request.getAttribute("memo");
	String company_str = "";
	String salesman_str = "";
	String customer_str = "";
	String memo_str = "";
	if(memo!=null){
		memo_str = memo;
	}
	if (salesmanId != null) {
		salesman_str = String.valueOf(salesmanId);
	}
	if (companyId != null) {
		company_str = String.valueOf(companyId);
	}
	if (customerId != null) {
		customer_str = String.valueOf(customerId);
	}
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>创建通用辅料入库通知单 -- 桐庐富伟针织厂</title>
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
		<script src="<%=basePath%>js/plugins/WdatePicker.js"
			type="text/javascript"></script>
		<script src="js/common/common.js" type="text/javascript"></script>
		<script src="js/plugins/jquery.jqGrid.min.js" type="text/javascript"></script>
		<link href="css/plugins/ui.jqgrid.css" rel="stylesheet"
			type="text/css" />
		<script src="js/order/ordergrid.js" type="text/javascript"></script>
		<script src="js/fuliaoin_notice/add_common.js" type="text/javascript"></script>
		<style type="text/css">
#tablename {
  font-weight: bold;
  font-size: 30px;
  margin-bottom: 15px;
}
.table>thead>tr>td {
	padding: 3px 8px;
}
.table-bordered>thead>tr>th, .table-bordered>thead>tr>td{border-bottom-width:1px;}
#mainTb {
	margin-top: 10px;  border-color: #000;
}
.table {
	margin-bottom: 0;
}
caption {
	font-weight: bold;
}
#previewImg {
	max-width: 200px;
	max-height: 150px;
}
#mainTb thead th{ background: #AEADAD;}
#mainTb thead th,#mainTb tbody tr td{border-color:#000;}
body {
  margin: auto;
  font-family: "Microsoft Yahei", "Verdana", "Tahoma, Arial",
 "Helvetica Neue", Helvetica, Sans-Serif,"SimSun";
}
.noborder.table>tbody>tr>td{border:none;}
.noborder.table{font-weight:bold;}
div.name{  width: 100px; display: inline-block;}
.checkBtn{height:25px;width:25px;}
tr.disable{background:#ddd;}
#created_user{  margin-right: 50px;}
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
							<a href="fuliao_workspace/commonfuliao?tab=fuliaoinnotice">通用辅料列表及出入库情况</a>
						</li>
						<li class="active">
							创建通用辅料入库通知单
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid materialorderWidget">
						<div class="row">
							<div class="col-md-12">
								<form class="saveform">
									<input type="hidden" name="id" value="" />
									<div class="clear"></div>
									<div class="col-md-12 tablewidget">
										<table class="table">
											<caption id="tablename">
												桐庐富伟针织厂通用辅料入库通知单
												<button type="submit"
													class="pull-right btn btn-danger saveTable"
													data-loading-text="正在保存...">
													创建通用辅料入库通知单
												</button>
											</caption>
										</table>
										<form class="form-horizontal searchform form-inline searchform"
										role="form">
										<button class="btn btn-primary pull-right" type="submit" id="searchBtn">
											搜索
										</button>
										<input type="hidden" name="page" value="1">
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
															if (companyId!=null&&companyId == company.getId()) {
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
										<div class="form-group salesgroup">
											<label for="salesmanId" class="col-sm-3 control-label">
												业务员
											</label>
											<div class="col-sm-9">
												<select class="form-control" name="salesmanId" id="salesmanId"
													placeholder="业务员">
													<option value="">
														所有
													</option>
													<%
														for (Salesman salesman : SystemCache.salesmanlist) {
															if (salesmanId!=null&&salesmanId == salesman.getId()) {
													%>
													<option value="<%=salesman.getId()%>" selected><%=salesman.getName()%></option>
													<%
														} else {
													%>
													<option value="<%=salesman.getId()%>"><%=salesman.getName()%></option>
													<%
														}
														}
													%>
												</select>
											</div>
										</div>
										<div class="form-group salesgroup">
											<label for="customerId" class="col-sm-3 control-label">
												客户
											</label>
											<div class="col-sm-9">
												<select class="form-control" name="customerId" id="customerId"
													placeholder="客户">
													<option value="">
														所有
													</option>
													<%
														for (Customer customer : SystemCache.customerlist) {
															if (customerId!=null&&customerId == customer.getId()) {
													%>
													<option value="<%=customer.getId()%>" selected><%=customer.getName()%></option>
													<%
														} else {
													%>
													<option value="<%=customer.getId()%>"><%=customer.getName()%></option>
													<%
														}
														}
													%>
												</select>
											</div>
										</div>
										<div class="form-group salesgroup">
											<label for="memo" class="col-sm-3 control-label">
												备注
											</label>
											<div class="col-sm-9">
												<input type='text' class="form-control" name="memo" id="memo" value="<%=memo_str %>" />
											</div>
										</div>
									
										</form>
										<table id="mainTb"
											class="table table-responsive table-bordered detailTb">
											<thead>
												<tr><th width="5%">
														序号
													</th>
													<th width="5%">
														编号
													</th>
													<th width="5%">
														类型
													</th><th width="15%">
														图片
													</th><th width="10%">
														订单号
													</th><th width="8%">
														款号
													</th><th width="6%">
														国家
													</th><th width="6%">
														颜色
													</th><th width="6%">
														尺码
													</th><th width="6%">
														批次
													</th>
													<th width="6%">
														备注
													</th>
													<th width="10%">
														来源
													</th>
													<th width="20%">
														入库数量(个)
													</th>
												</tr>
											</thead>
											<tbody>
												<%
													for (Fuliao fuliao : fuliaolist) {
														JSONObject json = JSONObject.fromObject(fuliao);
														json.put("fuliaoId",fuliao.getId());
														json.remove("id");
												%>
												<tr class="tr EmptyTr disable" data='<%=json.toString()%>'>
													<td><input type="checkbox" name="checked" class="checkBtn"/></td>
													<td><%=fuliao.getFnumber()%></td>
													<td><%=SystemCache.getFuliaoTypeName(fuliao.getFuliaoTypeId())%></td>
													<td><a href="/<%=fuliao.getImg()%>" class=""
																			target="_blank"> <img id="previewImg"
																				alt="200 x 100%" src="/<%=fuliao.getImg_ss()%>">
																		</a></td>
													<td><%=fuliao.getCompany_orderNumber()%></td>
													<td><%=fuliao.getCompany_productNumber()%></td>
													<td><%=fuliao.getCountry()%></td>
													<td><%=fuliao.getColor()%></td>
													<td><%=fuliao.getSize()%></td>
													<td><%=fuliao.getBatch()%></td>
													<td><%=fuliao.getMemo()%></td>
													<td>
														<select disabled class="fuliaoPurchaseFactoryId form-control  value">
															<option value="">未选择</option>
															<%for(Factory factory : SystemCache.fuliao_factorylist){ %>
															<option value="<%=factory.getId() %>"><%=factory.getName() %></option>
															<%} %>
														</select>
													</td>
													<td>
														<input disabled class="quantity form-control require positive_int value"
															type="text" value="0"
															placeholder="请输入预入库数量">
													</td>
												</tr>
												<%
													}
												%>
											</tbody>

										</table>
											
										<div id="tip" class="auto_bottom">
											
										</div>

										<p class="pull-right auto_bottom">
											<span id="created_user">制单人：<%=SystemCache.getUserName(SystemContextUtils
							.getCurrentUser(session).getLoginedUser().getId())%></span>
											<span id="date"> 日期：<%=DateTool.formatDateYMD(DateTool.now())%></span>
										</p>

										</table>

									</div>
								</form>
							</div>
						</div>
					</div>

				</div>
			</div>
		</div>
	</body>
</html>