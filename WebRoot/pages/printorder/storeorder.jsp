<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.ordergrid.StoreOrder"%>
<%@page import="com.fuwei.entity.ordergrid.StoreOrderDetail"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%
	Order order = (Order) request.getAttribute("order");
	StoreOrder storeOrder = (StoreOrder) request
			.getAttribute("storeOrder");
	List<StoreOrderDetail> storeOrderDetailList = storeOrder == null ? new ArrayList<StoreOrderDetail>()
			: storeOrder.getDetaillist();
	String productfactoryStr = (String)request.getAttribute("productfactoryStr");
%>
<!DOCTYPE html>
<html>
	<head>

		<title>原材料记录单 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8">
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<script src="js/plugins/jquery-barcode.min.js"></script>
		<!-- 为了让IE浏览器运行最新的渲染模式 -->
	</head>
	<body class="">
		<div class="container-fluid gridTab auto_container">
			<div class="row">
				<div class="col-md-12 tablewidget">
					<table class="table noborder">
						<caption id="tablename">
							桐庐富伟针织厂原材料记录单<div table_id="<%=order.getOrderNumber() %>" class="id_barcode"></div>
						</caption>
						<tr><td colspan="3" class="pull-right">№：<%=order.getOrderNumber() %></td></tr>
					</table>

					<table id="orderTb" class="tableTb">
						<tbody>
							<tr>
								<td align="center" rowspan="8" width="50%">
									<img id="previewImg" alt="200 x 100%"
										src="/<%=order.getImg_s()%>">
								</td>
								<td width="20%">
									生产单位
								</td>
								<td class="orderproperty"><%=productfactoryStr %></td>
							</tr>

							<tr>
								<td colspan="2" class="center">
									订单信息
								</td>
							</tr>
							<tr>
								<td>
									公司
								</td>
								<td><%=SystemCache.getCompanyShortName(order.getCompanyId())%></td>
							</tr>
							<tr>
								<td>
									客户
								</td>
								<td><%=SystemCache.getCustomerName(order.getCustomerId())%></td>
							</tr>
							<tr>
								<td>
									货号
								</td>
								<td><%=order.getCompany_productNumber()%></td>
							</tr>
							<tr>
								<td>
									款名
								</td>
								<td><%=order.getName()%></td>
							</tr>
							<tr>
								<td>
									跟单
								</td>
								<td><%=SystemCache.getEmployeeName(order.getCharge_employee())%></td>
							</tr>
							<tr>
								<td>
									发货时间
								</td>
								<td><%=DateTool.formatDateYMD(order.getEnd_at())%></td>
							</tr>
						</tbody>
					</table>

					<table id="mainTb" class="noborder">
						<tr>
							<td>
								<table class="detailTb">
									<caption>
										材料列表
									</caption>
									<thead>
										<tr>
											<th width="15%">
												色号
											</th>
											<th width="15%">
												材料
											</th>
											<th width="15%">
												总数量(kg)
											</th>
											<th width="15%">
												领取人
											</th>
											
										</tr>
									</thead>
									<tbody>
										<%
											int i = 0 ;
											int length = storeOrderDetailList.size();
											int middle = (int)(Math.ceil(length/2.0));
											for(;i < middle;++i){
												StoreOrderDetail detail = storeOrderDetailList.get(i);
										%>
										<tr class="tr">
											<td class="color"><%=detail.getColor()%>
											</td>
											<td class="material_name"><%=SystemCache.getMaterialName(detail.getMaterial()) %>
											</td>
											<td class="quantity"><%=detail.getQuantity()%>
											</td>
											<td class="factory_name"><%=SystemCache.getFactoryName(detail.getFactoryId())%>
											</td>
										
										</tr>

										<%
											}
										%>

									</tbody>
								</table>
							</td>
							<td>
								<table class="detailTb">
									<caption>
										材料列表
									</caption>
									<thead>
										<tr>
											<th width="15%">
												色号
											</th>
											<th width="15%">
												材料
											</th>
											<th width="15%">
												总数量(kg)
											</th>
											<th width="15%">
												领取人
											</th>
										
										</tr>
									</thead>
									<tbody>
										<%
											for(i = 0;i < middle;++i){
												if(i + middle < length){
												StoreOrderDetail detail = storeOrderDetailList.get(i + middle);
										%>
										<tr class="tr">
											<td class="color"><%=detail.getColor()%>
											</td>
											<td class="material_name"><%=SystemCache.getMaterialName(detail.getMaterial()) %>
											</td>
											<td class="quantity"><%=detail.getQuantity()%>
											</td>
											<td class="factory_name"><%=SystemCache.getFactoryName(detail.getFactoryId())%>
											</td>
										
										</tr>
										<%
											}else{
											
										%>
											<tr class="tr">
											<td class="color">&nbsp;
											</td>
											<td class="material_name">
											</td>
											<td class="quantity">
											</td>
											<td class="factory_name">
											</td>
											
										</tr>
										<%} 
										}%>

									</tbody>
								</table>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<table class="auto_height stickedTb">
									<caption>
										材料出入情况列表
									</caption>
									<thead>
										<tr>
											<th width="15%">
												色号
											</th>
											<th width="15%">
												材料
											</th>
											<th width="15%">
												入库/出库
											</th>
											<th width="15%">
												数量(kg)
											</th>
											<th width="15%">
												日期
											</th>
											<th width="15%">
												相关人员签字
											</th>

										</tr>
									</thead>
									<tbody>
										<tr>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
										</tr>

									</tbody>

								</table>
							</td>
						</tr>
					</table>

					<p class="pull-right auto_bottom">
						<span id="created_user">制单人：<%=SystemCache.getUserName(storeOrder.getCreated_user()) %></span>
						<span id="date"> 日期：<%=DateTool.formatDateYMD(DateTool.getYanDate(storeOrder.getCreated_at())) %></span>
					</p>



				</div>

			</div>
		</div>
	<script type="text/javascript">
		$(".id_barcode").each(function(){
			var id =$(this).attr("table_id");
			$(this).barcode(id, "code128",{barWidth:2, barHeight:30,showHRI:false});
		});		
	</script>
	</body>
</html>