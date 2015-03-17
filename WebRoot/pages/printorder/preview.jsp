<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String gridName = (String)request.getAttribute("gridName");
%>

<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">

		<title>打印订单表格 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8">
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<!-- 为了让IE浏览器运行最新的渲染模式 -->
		<link href="css/printorder/print.css" rel="stylesheet" type="text/css" />
		<script src="js/plugins/jquery-1.10.2.min.js"></script>
		<script src="js/common/common.js" type="text/javascript"></script>

	</head>

	<body>
		<%
		//获取头带质量记录单
			if(gridName.indexOf("headbankorder") > -1){
		 %>
			<div style="page-break-after: always">
			<%@ include file="../printorder/headbankorder.jsp"%>
			</div>
		<%} %>
	
		<%
		//获取生产单
			if(gridName.indexOf("producingorder") > -1){
		 %>
			<div style="page-break-after: always">
			<%@ include file="../printorder/producingorder.jsp"%>
			</div>
		<%} %>
		
		<%
		//获取计划单
			if(gridName.indexOf("planorder") > -1){
		 %>
			<div style="page-break-after: always">
			<%@ include file="../printorder/planorder.jsp"%>
			</div>
		<%} %>

		<%
		//获取原材料仓库单
			if(gridName.indexOf("storeorder") > -1){
		 %>
			<div style="page-break-after: always">
			<%@ include file="../printorder/storeorder.jsp"%>
			</div>
		<%} %>

		<%
		//获取半检记录单
			if(gridName.indexOf("halfcheckrecordorder") > -1){
		 %>
			<div style="page-break-after: always">
			<%@ include file="../printorder/halfcheckrecordorder.jsp"%>
			</div>
		<%} %>

		<%
		//获取原材料采购单
			if(gridName.indexOf("materialpurchaseorder") > -1){
		 %>
			<div style="page-break-after: always">
			<%@ include file="../printorder/materialpurchaseorder.jsp"%>
			</div>
		<%} %>

		<%
		//获取染色单
			if(gridName.indexOf("coloringorder") > -1){
		 %>
			<div style="page-break-after: always">
			<%@ include file="../printorder/coloringorder.jsp"%>
			</div>
		<%} %>

		<%
		//获取抽检记录单
			if(gridName.indexOf("checkrecordorder") > -1){
		 %>
			<div style="page-break-after: always">
			<%@ include file="../printorder/checkrecordorder.jsp"%>
			</div>
		<%} %>

		<%
		//获取辅料采购单
			if(gridName.indexOf("fuliaopurchaseorder") > -1){
		 %>
			<div style="page-break-after: always">
			<%@ include file="../printorder/fuliaopurchaseorder.jsp"%>
			</div>
		<%} %>

		<%
		//获取车缝记录单
			if(gridName.indexOf("carfixrecordorder") > -1){
		 %>
			<div style="page-break-after: always">
			<%@ include file="../printorder/carfixrecordorder.jsp"%>
			</div>
		<%} %>

		<%
		//获取整烫记录单
			if(gridName.indexOf("ironingrecordorder") > -1){
		 %>
			<div style="page-break-after: always">
			<%@ include file="../printorder/ironingrecordorder.jsp"%>
			</div>
		<%} %>


	</body>

	<script type="text/javascript">
		//固定高度的表格添加空行
		$(document).ready(function(){
			$(".stickedTb").each(function(){
				var html = "<tr>";
				var $thead = $(this).find("thead");
				var $ths = $thead.find("th");
				if($thead.length <= 0){
					$thead = $(this).find("tbody tr").first();
					$ths = $thead.find("td");
				}
				var length = $ths.length;
				for(var i = 0 ; i < length ; ++i){
					html += "<td></td>";
				}
				html += "</tr>";
				
				addTr(html,this);
			});
			
			function addTr(html,table){
				var $tr = $(table).find("tbody tr").first();
				var height = $tr.outerHeight();
				if(height!=null && height <= 30){
					return true;
				}else{
					$(table).find("tbody").append(html);
					addTr(html,table);
				}
		
			}
			
		});
	</script>
</html>
