<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.entity.Employee"%>
<%@page import="com.fuwei.commons.Pager"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.constant.OrderStatus"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.entity.producesystem.Fuliao"%>
<%@page import="com.fuwei.entity.Customer"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Order order = (Order)request.getAttribute("order");
	List<Fuliao> fuliaoList = (List<Fuliao>)request.getAttribute("fuliaoList");
	//各通用辅料当前库存
	Map<Integer,Integer> stockMap = (Map<Integer,Integer>)request.getAttribute("stockMap");
	if (stockMap == null) {
		stockMap = new HashMap<Integer,Integer>();
	}
	
	//权限相关
	
	Boolean has_add = SystemCache.hasAuthority(session,
			"fuliao/add");
	Boolean has_card= SystemCache.hasAuthority(session,
			"fuliao/card");
	//权限相关
	
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
		<title>通用辅料列表 -- 桐庐富伟针织厂</title>
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
.body {
	min-width: 0;
}

#breadcrumbs {
	min-width: 0;
}
#Tb{
	border-color:#000;
}
#Tb>thead>tr{
	  background: #AEADAD;
}
#Tb>thead>tr>th,#Tb>tbody>tr>td{
	border-color:#000;
	border-bottom-width: 1px;
	padding-left: 0;
    padding-right: 0;
    text-align: center;
}
.memoform #memo{
	height:200px;
}
#addBtn,#addNoticeBtn,#cardBtn{margin-bottom:10px;}
.checkbtn,#checkAll{height: 20px;width: 20px;}
.table tr.selected{background: #DCD773 !important;}
#checkAll{margin-right: 3px;vertical-align: middle;}
</style>
	</head>
	<body>
		<div id="Content">
			<div id="main">
				<div class="body">
								<!-- Table -->
								<div clas="navbar navbar-default">
								<%if(has_add){ %>
									<a target="_blank"  id="addBtn" href="fuliao/add_common" class="btn btn-primary" >添加辅料</a>
								<%} %>
								</div>
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
														<select class="form-control" name="salesmanId"
															id="salesmanId" placeholder="业务员">
															<option value="">
																未选择
															</option>
															<%if(companyId!=null){//若公司已选，则自动显示 %>
																<%
																for (Salesman salesman : companySalesmanMap.get(companyId.toString())) {
																	if(salesmanId!=null && salesmanId==salesman.getId()){
															%>
															<option value="<%=salesman.getId()%>" selected><%=salesman.getName()%></option>
															<%}else{ %>
															<option value="<%=salesman.getId()%>"><%=salesman.getName()%></option>
															<%
																}}
															%>
															<%}%>
															
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
								<table class="table table-responsive table-bordered" id="Tb">
									<thead>
										<tr>
											<th width="50px">
												编号
											</th>
											<th width="120px">
												图片
											</th>
											<th width="55px">
												辅料类型
											</th>
											<th width="40px">
												公司等
											</th>
											<th width="70px">
												公司订单号
											</th>
											<th width="70px">
												公司货号
											</th>
											<th width="50px">
												颜色
											</th><th width="60px">
												尺码
											</th><th width="40px">
												批次
											</th>
											<th width="60px">
												国家/城市
											</th>
											<th width="50px">
												库位容量
											</th>
											<th width="80px">
												备注
											</th>
											<th width="70px">
												创建人
											</th>
											<th width="70px">
												当前库存(个)
											</th>
										</tr>
									</thead>
									<tbody>
										<%if(fuliaoList.size()<=0){ %>
										<tr><td colspan="14">还未添加辅料，请点击按钮 "添加辅料" 添加</td></tr>
										<%} %>
										<%
											
											for (Fuliao fuliao : fuliaoList) {
										%>
									
										<tr itemId="<%=fuliao.getId()%>">
											<td><%=fuliao.getFnumber()%><br><a target="_blank" href="fuliao/detail/<%=fuliao.getId()%>">详情</a></td>
											<td style="max-width: 120px; height: 120px; max-height: 120px;">
												<a target="_blank" class="cellimg"
													href="/<%=fuliao.getImg()%>"><img
														style="max-width: 120px; height: 120px; max-height: 120px;"
														src="/<%=fuliao.getImg_ss()%>"> </a>
											</td>
											<td><%=SystemCache.getFuliaoTypeName(fuliao.getFuliaoTypeId())%></td>
											<td><%=SystemCache.getCompanyShortName(fuliao.getCompanyId())%><br><%=SystemCache.getSalesmanName(fuliao.getSalesmanId())%>
											<br><%=SystemCache.getCustomerName(fuliao.getCustomerId())%></td>
											<td><%=fuliao.getCompany_orderNumber()%></td>
											<td><%=fuliao.getCompany_productNumber()%></td>
											<td><%=fuliao.getColor()%></td>
											<td><%=fuliao.getSize()%></td>
											<td><%=fuliao.getBatch()%></td>
											<td><%=fuliao.getCountry() %></td>
											<td><%=fuliao.getLocationSizeString() %></td>
											<td><%=fuliao.getMemo()%></td>
											<td><%=SystemCache.getUserName(fuliao.getCreated_user()) %> <br> <%=fuliao.getCreated_at() %></td>
											<td><%=stockMap.containsKey(fuliao.getId())?stockMap.get(fuliao.getId()) : 0 %></td>
										</tr>
										<%} %>
									</tbody>
								</table>
				</div>
			</div>
		</div>
		<script type="text/javascript">
		// 公司-业务员级联
		$("#companyId").change( function() {
			changeCompany(this.value);
		});
		// 公司-业务员级联
		function changeCompany(companyId) {
			var companyName = $("#companyId").val();
			var companySalesmanMap = $("#companyId").attr("data");
			companySalesmanMap = $.parseJSON(companySalesmanMap).companySalesmanMap;
			var SalesNameList = companySalesmanMap[companyName];
			$("#salesmanId").empty();
			var frag = document.createDocumentFragment();
			var option = document.createElement("option");
			option.value = "";
			option.text = "未选择";
			frag.appendChild(option);
			if (SalesNameList) {
				for ( var i = 0; i < SalesNameList.length; ++i) {
					var salesName = SalesNameList[i];
					var option = document.createElement("option");
					option.value = salesName.id;
					option.text = salesName.name;
					frag.appendChild(option);
				}
			}
		
			$("#salesmanId").append(frag);
		}
		</script>
	</body>
</html>