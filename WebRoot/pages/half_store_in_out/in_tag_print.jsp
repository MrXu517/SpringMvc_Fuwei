<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.producesystem.StoreInOut"%>
<%@page import="com.fuwei.entity.producesystem.StoreInOutDetail"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.alibaba.fastjson.JSONObject"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/";
	//半成品入库单
	StoreInOut storeInOut = (StoreInOut) request.getAttribute("storeInOut");
	List<StoreInOutDetail> detaillist = storeInOut == null ? new ArrayList<StoreInOutDetail>() :storeInOut.getDetaillist();
	
	String employee_name = SystemCache.getEmployeeName(storeInOut.getCharge_employee()) ;//跟单人
	String coloring_factory_name = SystemCache.getFactoryName(storeInOut.getFactoryId());

	String date_string = DateTool.formatDateYMD(DateTool.getYanDate(storeInOut.getDate()));
	
	String date_now = DateTool.formatDateYMD(DateTool.getYanDate(DateTool.now()));
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>打印纱线标签 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8">
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<script src="js/plugins/jquery-1.10.2.min.js"></script>
		<script src="js/plugins/jquery-barcode.min.js"></script>
		<!-- 为了让IE浏览器运行最新的渲染模式 -->

		<style type="text/css">
body {
	margin: auto;
	font-family: "Microsoft Yahei", "Verdana", "Tahoma, Arial",
		"Helvetica Neue", Helvetica, Sans-Serif, "SimSun";
	font-size: 14px;
}

.gridTab {
	height: 8cm;
	width: 5cm;
	margin: auto;
}

.pull-right {
	float: right;
}
</style>

	</head>
	<body class="">
		<div class="container-fluid">
			<div class="row">
				<div class="col-md-12 tablewidget">
					<%
						for(StoreInOutDetail detail : detaillist){ 
									int packages = detail.getPackages();
									int count = 1 ;
									double quantity = detail.getQuantity();
									int quantity_int = (int)quantity;
									String quantity_str= "";
									if(quantity_int == quantity){
										quantity_str = quantity_int + "";
									}else{
										quantity_str = quantity + "";
									}
									for(; count <= packages; ++count){
										String tag_string = storeInOut.getId() + "_" + detail.getId();
					%>
					<div style="page-break-after: always">
						<div class="gridTab auto_container">
							<table class="table noborder tagWidget">
								<caption>
									<div tag_string='<%=tag_string %>' class="id_barcode"></div>
									<strong>入库纱线标签</strong>
								</caption>
								<tr>
									<td width="70px">
										业务员：
									</td>
									<td><%=employee_name %></td>
								</tr>
								<tr>
									<td>
										订单号：
									</td>
									<td><%=storeInOut.getOrderNumber() %></td>
								</tr>
								<tr>
									<td>
										品名：
									</td>
									<td><%=storeInOut.getName() %></td>
								</tr>
								<tr>
									<td>
										货号：
									</td>
									<td><%=storeInOut.getCompany_productNumber() %></td>
								</tr>
								<tr>
									<td>
										染厂：
									</td>
									<td><%=coloring_factory_name %></td>
								</tr>
								<tr>
									<td>
										色号：
									</td>
									<td><%=detail.getColor() %></td>
								</tr>
								<tr>
									<td>
										材料：
									</td>
									<td><%=SystemCache.getMaterialName(detail.getMaterial()) %></td>
								</tr>
								<tr>
									<td>
										缸号：
									</td>
									<td><%=detail.getLot_no() %></td>
								</tr>
								<tr>
									<td>
										入库总数：
									</td>
									<td><%=quantity_str  %>
										kg ，
										<%=count %>
										/
										<%=packages %></td>
								</tr><tr>
									<td>
										入库时间：
									</td>
									<td><%=date_string %></td>
								</tr>

							</table>
							<div class="pull-right">打印：<%=date_now %></div>
						</div>
					</div>
				</div>
				<%}
					} %>
			</div>

		</div>
		</div>
		<script type="text/javascript">
	$(".id_barcode").each( function() {
		var id = $(this).attr("tag_string");
		$(this).barcode(id, "code128", {
			barWidth :2,
			barHeight :30,
			showHRI :true
		});
	});
	window.print();
</script>
	</body>
</html>