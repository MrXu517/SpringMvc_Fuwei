<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.entity.Material"%>
<%@page import="com.fuwei.entity.Customer"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.entity.ordergrid.MaterialPurchaseOrderDetail"%>
<%@page import="com.fuwei.entity.finishstore.PackProperty"%>
<%@page import="com.fuwei.entity.finishstore.PackingOrder"%>
<%@page import="com.fuwei.entity.finishstore.PackingOrderDetail"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	PackingOrder packingOrder = (PackingOrder) request.getAttribute("packingOrder");
	List<PackingOrderDetail> detaillist = packingOrder==null?new ArrayList<PackingOrderDetail>():packingOrder.getDetaillist();
	if(detaillist == null){
		detaillist = new ArrayList<PackingOrderDetail>();
	}
	Boolean has_delete = SystemCache.hasAuthority(session,"packing_order/delete");
	Boolean has_edit = SystemCache.hasAuthority(session,"packing_order/edit");
	Boolean has_export = SystemCache.hasAuthority(session,"packing_order/export");
	
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>装箱单详情 -- 桐庐富伟针织厂</title>
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

		<link href="css/order/bill.css" rel="stylesheet" type="text/css" />
		<style type="text/css">
		#saveTb thead th, #mainTb tbody tr td {
		    border-color: #000;
		}
		#saveTb thead th {
		    background: #AEADAD;    border: 1px solid #000; text-align: center;padding: 0 3px;
		}
		#saveTb tbody td,#saveTb tfoot td {
		     border: 1px solid #000; text-align: center;padding: 0;
		}
		select.colselect{padding:0;}
		#saveTb tbody td [disabled],select[disabled]{cursor: not-allowed;background: #ccc;}
		#saveTb{ border: 1px solid #000;    table-layout: fixed;}
		.colable{width:20px;height:20px;}
		#saveTb tbody td input{width:100%;}
		#saveTb tfoot td{text-align: right;}
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
							<a href="order/detail/<%=packingOrder.getOrderId()%>">订单详情</a>
						</li>
						<li>
							<a href="packing_order/list/<%=packingOrder.getOrderId()%>">订单 -- 装箱单</a>
						</li>
						<li class="active">
							装箱单详情
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid orderWidget">
						<div class="row">
								<div class="clear"></div>
								<%
									if(has_edit){
								%>
								<a href="packing_order/put/<%=packingOrder.getId()%>" type="button" class="btn btn-success">编辑</a>
								<%
									}
								%>
								<%
									if(has_export){
								%>
								<a href="packing_order/export/<%=packingOrder.getId()%>" type="button" class="btn btn-primary">导出</a>
								<%
									}
								%>
								<%
									if(has_delete){
								%>
								<button data-cid="<%=packingOrder.getId()%>" type="button" class="btn btn-danger pull-right" id="deleteBtn">删除</button>
								<%
									}
								%>
								<div class="col-md-12 tablewidget">
									<table class="table">
										<caption id="tablename">
											桐庐富伟针织厂装箱单(<%=DateTool.getYear(packingOrder.getCreated_at()) %>年,<%=packingOrder.getCompany_productNumber() %><%=packingOrder.getName() %>)
										</caption>
									</table>
									<table class="tableTb noborder">
										<tbody>
											<tr>
												<td width="20%">
														跟单人：<%=SystemCache.getEmployeeName(packingOrder.getCharge_employee())%>
												</td>
												<td width="20%">
														订单号：<%=packingOrder.getOrderNumber()%>
												</td>
												<td width="60%">
														备注：<%=packingOrder.getMemo()==null?"":packingOrder.getMemo() %>
												</td>
											</tr></tbody>
									</table>
									<table class="table table-responsive detailTb" id="saveTb">
										<thead>
										<tr>
											<%
											int col = 0;
											if(packingOrder.getCol1_id()!=null){
											col++;
											 %>
											<th rowspan="2" width="80px">
												<%=SystemCache.getPackPropertyName(packingOrder.getCol1_id()) %>
											</th>
											<%} %>
											
											<%if(packingOrder.getCol2_id()!=null){ 
											col++;%>
											<th rowspan="2" width="80px">
												<%=SystemCache.getPackPropertyName(packingOrder.getCol2_id()) %>
											</th>
											<%} %>
											<%if(packingOrder.getCol3_id()!=null){ 
											col++;%>
											<th rowspan="2" width="80px">
												<%=SystemCache.getPackPropertyName(packingOrder.getCol3_id()) %>
											</th>
											<%} %>
											<%if(packingOrder.getCol4_id()!=null){ 
											col++;%>
											<th rowspan="2" width="80px">
												<%=SystemCache.getPackPropertyName(packingOrder.getCol4_id()) %>
											</th>
											<%} %>

											<th rowspan="2" width="40px">
												颜色
											</th>
											<th rowspan="2" width="40px">
												数量
											</th>
											<th rowspan="2" width="40px">
												每箱数量
											</th><th colspan="3" width="120px">外箱尺寸</th>
												<th colspan="2" width="80px">毛净重</th>
											<th rowspan="2" width="60px">
												箱数
											</th>
											<th colspan="2" width="100px">
												箱号
											</th>
											<th rowspan="2" width="40px">
												每包几件
											</th>
											<th rowspan="2" width="40px">
												立方数
											</th>
										</tr><tr><th width="55px">
												L
											</th><th width="55px">
												W
											</th><th width="55px">
												H
											</th><th width="55px">
												毛重
											</th><th width="55px">
												净重
											</th><th width="60px">
												开始
											</th><th width="60px">
												结束
											</th></tr>
									</thead>
									<tbody>
										<%for(PackingOrderDetail detail : detaillist){ %>
										<tr>
										<%if(packingOrder.getCol1_id()!=null){ %>
										<td>
											<%=detail.getCol1_value()==null?"":detail.getCol1_value() %>
										</td>
										<%} %>
										<%if(packingOrder.getCol2_id()!=null){ %>
										<td>
											<%=detail.getCol2_value()==null?"":detail.getCol2_value() %>
										</td>
										<%} %>	
										<%if(packingOrder.getCol3_id()!=null){ %>
										<td>
											<%=detail.getCol3_value()==null?"":detail.getCol3_value() %>
										</td>
										<%} %>
										<%if(packingOrder.getCol4_id()!=null){ %>
										<td>
											<%=detail.getCol4_value()==null?"":detail.getCol4_value() %>
										</td>
										<%} %>
										<td><%=detail.getColor() %></td>
										<td><%=detail.getQuantity() %></td>
										<td><%=detail.getPer_carton_quantity() %></td>
										<td><%=detail.getBox_L() %></td>
										<td><%=detail.getBox_W() %></td>
										<td><%=detail.getBox_H() %></td>
										<td><%=detail.getGross_weight() %></td>
										<td><%=detail.getNet_weight() %></td>
										<td><%=detail.getCartons() %></td>
										<td><%=detail.getBox_number_start() %></td>
										<td><%=detail.getBox_number_end() %></td>
										<td><%=detail.getPer_pack_quantity() %></td>
										<td><%=detail.getCapacity() %></td>
										</tr>
										<%} %>
									</tbody>
									<tfoot><tr><td>合计</td><td colspan="<%=col+1 %>"><%=packingOrder.getQuantity() %></td>
										<td colspan="7"><%=packingOrder.getCartons() %></td><td colspan="4"><%=packingOrder.getCapacity() %></td>
										</tr></tfoot>
								</table>
								<p class="pull-right auto_bottom">
									<span id="created_user">制单人：<%=SystemCache.getUserName(packingOrder.getCreated_user()) %></span>
									<span id="date"> 日期：<%=DateTool.formatDateYMD(packingOrder.getCreated_at()) %></span>
								</p>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
		<script type="text/javascript">
		//删除单据 -- 开始
		$("#deleteBtn").click( function() {
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
						$("#breadcrumbs li.active").prev().find("a").click();
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