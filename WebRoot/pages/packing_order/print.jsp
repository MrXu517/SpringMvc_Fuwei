<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.entity.finishstore.PackingOrder"%>
<%@page import="com.fuwei.entity.finishstore.PackingOrderDetail"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/";
	//装箱单
	PackingOrder packingOrder = (PackingOrder) request.getAttribute("packingOrder");
	List<PackingOrderDetail> detaillist = packingOrder == null ? new ArrayList<PackingOrderDetail>() :packingOrder.getDetaillist();
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>打印装箱单 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8">
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<!-- 为了让IE浏览器运行最新的渲染模式 -->
		<script src="js/plugins/jquery-1.10.2.min.js"></script>
		<link href="css/printorder/print.css" rel="stylesheet" type="text/css" />
		<script src="js/plugins/jquery-barcode.min.js"></script>
		<style type="text/css">
		table tr>th{text-align:center;}
		.gridTb_2{width:29.7cm;}
		.detailTb tr td{text-align:center;}
		tfoot>tr{height:25px;}
		</style>
	</head>
	<body>
		<%
		//固定行数分页
		int MAX_SIZE = 23;
		int size = detaillist.size();
		int total_page = (int)(Math.ceil((double)size/MAX_SIZE)) ;
		if(total_page == 0){total_page = 1;}
		int current_page = 1;
		for(;current_page <= total_page;++current_page){
		 %>
		<div style="page-break-after: always">
			<div class="container-fluid gridTb_2 auto_container">
				<div class="row">
					<div class="col-md-12 tablewidget">
						<table class="table noborder">
							<caption id="tablename">
								桐庐富伟针织厂装箱单<div table_id="<%=packingOrder.getNumber()%>" class="id_barcode"></div>
							</caption>
						</table>

						<table id="orderTb" class="tableTb noborder">
							<tbody>
								<tr>
									<td>
										工厂订单号：
										<span><%=packingOrder.getOrderNumber()%></span>
									</td>
									<td>
										款名：
										<span><%=packingOrder.getName()%></span>
									</td>
									<td>
										公司：
										<span><%=SystemCache.getCompanyShortName(packingOrder.getCompanyId()) %></span>
									</td>
									<%if(packingOrder.getCustomerId()!=null && packingOrder.getCustomerId()!=0) {%>
									<td>
										客户：
										<span><%=SystemCache.getCustomerName(packingOrder.getCustomerId())%></span>
									</td>
									<%} %>
									<td class="pull-right">

										№：<%=packingOrder.getNumber()%>

									</td>
									<td></td>
								</tr>
								<tr>
									<td colspan="5">
										<table class="detailTb">
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
										<%
										for (int i = 0 ; i < MAX_SIZE ; ++i) {
											int temp_i = (current_page-1)*MAX_SIZE + i;
											if(temp_i>=size){break;}
											PackingOrderDetail detail = detaillist.get(temp_i);
										%>
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
									<%if(current_page == total_page){ %>
									<tfoot><tr><td>合计</td>	
										<%if(col>0){ %>
									<td colspan="<%=col+1 %>">总数量：<%=packingOrder.getQuantity() %></td>
										<%}else{ %>
										<td colspan="<%=col+1 %>"><%=packingOrder.getQuantity() %></td>
										<%} %>
										<td colspan="7">总箱数：<%=packingOrder.getCartons() %></td><td colspan="4">总体积：<%=packingOrder.getCapacity() %></td>
										</tr>
										<tr><td colspan="<%=col+13 %>" style="text-align:left;padding-left: 20px;">备注：<%=packingOrder.getMemo()==null?"":packingOrder.getMemo() %></td></tr>
										</tfoot>
									<%} %>
								</table>
									</td>
									<td></td>
								</tr>
							</tbody>
						</table>

						<table id="mainTb" class="noborder">

						</table>


						<p class="pull-right auto_bottom" style="margin-top:0;margin-bottom:0;">
							<span id="created_user">制单人：<%=SystemCache.getUserName(packingOrder
								.getCreated_user())%></span>
							<span id="date"> 制单日期：<%=DateTool.formatDateYMD(DateTool.getYanDate(packingOrder.getCreated_at()))%></span>
							<span id="page"> 页码：<%=current_page+"/"+total_page%></span>
						</p>



					</div>
				</div>				
			</div>
		</div>
<%} %>		
	<script type="text/javascript">
		$(".id_barcode").each(function(){
			var id =$(this).attr("table_id");
			$(this).barcode(id, "code128",{barWidth:2, barHeight:30,showHRI:false});
		});		
		window.print();
	</script>
	</body>
</html>