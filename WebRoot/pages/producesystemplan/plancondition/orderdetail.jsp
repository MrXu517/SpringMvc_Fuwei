<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.OrderStep"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.constant.OrderStatus"%>
<%@page import="com.fuwei.constant.OrderStatusUtil"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.NumberUtil"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.entity.GongXu"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	
	Order order = (Order) request.getAttribute("order");		
	List<OrderDetail> DetailList = order == null || order.getDetaillist() == null ? new ArrayList<OrderDetail>()
			: order.getDetaillist();
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>生产计划排程 -- 订单明细 -- 桐庐富伟针织厂</title>
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
		<script src="<%=basePath%>js/plugins/WdatePicker.js"></script>
		<link href="css/order/index.css" rel="stylesheet" type="text/css" />
		<style type="text/css">
		#gongxuset table tr th,#gongxuset table tr td{border: 1px solid #000; text-align:center;vertical-align:center;}    
		</style>
	</head>
	<body>

		<%@ include file="../../common/head.jsp"%>
		<div id="Content">
			<div id="main">
				<div class="breadcrumbs" id="breadcrumbs">
					<ul class="breadcrumb">
						<li>
							<i class="fa fa-home"></i>
							<a href="user/index">首页</a>
						</li>
						<li>
							<i class=""></i>
							<a href="order/detail/<%=order.getId() %>">订单详情</a>
						</li>
						<li class="active">
							生产计划排程 -- 订单明细
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 formwidget">
								<div class="head">
									<div class="pull-left">
										<label class="control-label">
											订单编号：
										</label>
										<span><%=order.getOrderNumber()%></span>
									</div>
									<div class="clear"></div>
								</div>
								<fieldset id="orderDetail">
									<legend>
										1、请填写发货批数 、 每批发货数量、以及交货日期
									</legend>
									<table class="table table-responsive detailTb">
										<caption>
										</caption>
										<thead>
											<tr>
												<th width="15%">
													颜色
												</th>
												<th width="15%">
													克重(g)
												</th>
												<th width="15%">
													机织克重(g)
												</th>
												<th width="15%">
													纱线种类
												</th>
												<th width="15%">
													尺寸
												</th>
												<th width="15%">
													订单数量
												</th>
												<th width="15%">
													分几批发货
												</th>
												<th width="15%">
													发货数量
												</th>
												<th width="15%">
													交货日期
												</th>
											</tr>
										</thead>
										<tbody>
											<%
												for (OrderDetail detail : DetailList) {
											%>
											<tr class="tr" orderdetailId="<%=detail.getId() %>">
												<td class="color"><%=detail.getColor()%>
												</td>
												<td class="weight"><%=detail.getWeight()%>
												</td>
												<td class="produce_weight"><%=detail.getProduce_weight()%>
												</td>
												<td class="yarn_name"><%=SystemCache.getMaterialName(detail.getYarn())%>
												</td>
												<td class="size"><%=detail.getSize()%>
												</td>
												<td class="quantity"><%=detail.getQuantity()%>
												</td>
												<td><input type="text" class="delivery_batch value" value="1" />
												</td>
												<td><input type="text" class="delivery_quantity value" />
												</td>
												<td><input type="text" class="delivery_date value date" />
												</td>
											</tr>

											<%
												}
											%>

										</tbody>
									</table>
								</fieldset>
								<fieldset id="gongxuset">
									<legend>
										2、设置工序
									</legend>
									<table class="table table-responsive detailTb">
										<caption>
										</caption>
										<thead><tr>
												<th rowspan="2" width="20%">颜色/尺寸/订单数量
												</th>
												<th rowspan="2" width="10%">几道工序
												</th>
												<th width="53%" colspan="5">工序流水线生产情况设置</th>
											</tr>
											<tr>
												<th width="5%">
													工序名称
												</th>
												<th width="10%">
													每天生产量
												</th>
												<th width="8%">
													合格率
												</th>
												<th width="15%">
													开始运作时间
												</th>
												<th width="15%">
													开始运作描述
												</th>
											</tr>
										</thead>
										<tbody>
											<%
												for (OrderDetail detail : DetailList) {
											%>
											<tr class="tr" orderdetailId="<%=detail.getId() %>">
												<td><%=detail.getColor()%>/<%=detail.getSize()%>/<%=detail.getQuantity()%>
												</td>
												<td><input type="text" class="gongxus value" style="width:60px;"/>
												</td>
												<td><select type="text" class="gongxuId value"  style="width:120px;">
														<option value="">未选择</option>
													<%for(GongXu gongxu : SystemCache.gongxulist){ %>	
														<option value="<%=gongxu.getId() %>"><%=gongxu.getName() %></option>
													<%} %>
												</select>
												</td>
												<td><input type="text" class="gongxu_quantity_per_day value"  style="width:90px;"/>
												</td>
												<td><input type="text" class="gongxu_rate value"  style="width:30px;"/>%
												</td>
												<td><input type="text" class="gongxu_start_date value date"  style="width:120px;"/>
												</td>
												<td><input type="text" class="gongxu_start_memo value " style="width:160px;" />
												</td>
											</tr>

											<%
												}
											%>

										</tbody>
									</table>
								</fieldset>
							</div>


						</div>
					</div>

				</div>
			</div>
		</div>
	<script type="text/javascript">
	$(".delivery_batch").bind("input propertychange",function(){
		var delivery_batch = Number(this.value);
		if(delivery_batch<1){
			this.value = 1;
			return;
		}
		var $mytr = $(this).closest("tr");
		var orderdetailId = $mytr.attr("orderdetailId");
		var $trs = $(this).closest("tr").siblings("[orderdetailId='" + orderdetailId + "']");
		if(($trs.length+1) != delivery_batch){//如果没有分成相应的批次
			$trs.remove();//删除原来多余的行
			//添加新的行
			for(var count = 0 ; count < delivery_batch -1 ; ++count){
				var html = "<tr orderdetailId='" + orderdetailId + "'><td><input type='text' class='delivery_quantity value' /></td>"+
						"<td><input type='text' class='delivery_date value date' /></td></tr>";
				$mytr.after(html);
				
			}
			$(".date").unbind("click focus");
			$(".date").bind("click focus", function() {
				WdatePicker();
			});
			//将第一行的数值重置
			$mytr.find(".delivery_quantity,.delivery_date").val("");
			$mytr.find("td:lt(7)").attr("rowspan",delivery_batch);
		}
	});
	</script>
	</body>
</html>
