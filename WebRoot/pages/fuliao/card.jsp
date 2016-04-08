<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.producesystem.Fuliao"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.entity.producesystem.FuliaoInNotice"%>
<%@page import="com.fuwei.entity.producesystem.FuliaoInNoticeDetail"%>
<%@page import="com.fuwei.commons.SystemContextUtils"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Order order = (Order)request.getAttribute("order");
	List<Fuliao> fuliaoList = (List<Fuliao>)request.getAttribute("fuliaoList");
	List<OrderDetail> DetailList = order == null ? new ArrayList<OrderDetail>()
			: order.getDetaillist();
	if(DetailList == null){
		DetailList = new ArrayList<OrderDetail>();
	}
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>打印辅料卡 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8">
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<link href="css/printorder/print.css" rel="stylesheet" type="text/css" />
		<script src="js/plugins/jquery-1.10.2.min.js"></script>
		<script src="js/common/common.js" type="text/javascript"></script>
		
<link href="css/plugins/ui.jqgrid.css" rel="stylesheet"
			type="text/css" />
		
		
		<style type="text/css">
#previewImg,.fuliaoImg {
	max-width: 120px;
    max-height: 120px;
}
div.name{   margin-left: 15px; width: 100px; display: inline-block;}
#mainTb td{text-align:center;word-break: break-word;}
td.th{font-weight:bold;}
th{font-size: 15px;}
#mainTb{table-layout: fixed;}
</style>

	</head>
	<body>
				<%
				int MAX_SIZE = 6;
				int size = fuliaoList.size();
				int total_page = (int)(Math.ceil((double)size/MAX_SIZE)) ;
				if(total_page == 0){total_page = 1;}
				int current_page = 1;
				for(;current_page <= total_page;++current_page){
				%>
					<div style="page-break-after: always">
					<div class="container-fluid gridTab auto_container ">
						<div class="row">
							<div class="col-md-12">
								<form class="saveform">
									<div class="clear"></div>
									<div class="col-md-12 tablewidget">
										<table class="table noborder">
											<caption id="tablename">
												桐庐富伟针织厂  --- 辅料卡     <%=order.getOrderNumber()%>
											</caption>
										</table>
										<table class="table table-responsive noborder">
											<tbody>
												<tr>
													<td colspan="2">
														<table class="table table-responsive noborder" style="width:65%;display: inline-table;">
															<tbody>
																<tr>
																	<td align="center" rowspan="7" width="30%">
																		<img id="previewImg"
																				alt="200 x 100%" src="/<%=order.getImg_s()%>">
																	</td>
																	<td>
																		<div class="name">公司：</div><span class="value"><%=SystemCache.getCompanyShortName(order.getCompanyId())%></span>
																	</td>
																</tr>
																<tr>
																	<td>
																		<div class="name">客户：</div><span class="value"><%=SystemCache.getCustomerName(order.getCustomerId())%></span>
																	</td>
																</tr>
																<tr>
																	<td>
																		<div class="name">货号：</div><span class="value"><%=order.getCompany_productNumber()%></span>
																	</td>
																</tr>
																<tr>
																	<td>
																		<div class="name">款名：</div><span class="value"><%=order.getName()%></span>
																	</td>
																</tr>
																<tr>
																	<td>
																		<div class="name">跟单：</div><span class="value"><%=SystemCache.getEmployeeName(order.getCharge_employee())%></span>
																	</td>
																	
																</tr>
															</tbody>
														</table>
														<table style="width:30%;display: inline-table;vertical-align: top;">
															<caption>订单数量</caption>
															<tbody>
																<%
																for (OrderDetail detail : DetailList) {
																%>
																<tr>
																<td><%=detail.getColor() %></td>
																<td><%=detail.getQuantity()%></td>
																</tr>
																<%
																}
																%>
															</tbody>


														</table>
													</td>
												</tr>
											</tbody>
										</table>
					
										<table id="mainTb"
											class="table table-responsive table-bordered">
											<thead>
												<tr style="height:0;">
													<th style="border: none;" width="8%">
													</th><th style="border: none;" width="50%">
													</th>
													<th style="border: none;" width="8%">
													</th><th style="border: none;" width="17%">
													</th>
													<th style="border: none;" width="8%">
													</th>
													<th style="border: none;" width="8%">
													</th>
												</tr>
												<tr>
													<th width="8%">
														辅料类型
													</th><th width="50%">
														图片
													</th>
													<th colspan="2" width="25%">
														区别属性
													</th>
													<th width="8%">
														订单所需数量(个)
													</th>
													<th width="8%">
														备注
													</th>
												</tr>
											</thead>
											<tbody>
												<%if(fuliaoList.size()<=0){ %>
										<tr><td colspan="13">还未添加辅料，请点击按钮 "添加辅料" 添加</td></tr>
										<%} %>
										<%
											for (int i = 0 ; i < MAX_SIZE ; ++i) {
												int temp_i = (current_page-1)*MAX_SIZE + i;
												if(temp_i>=size){break;}
												Fuliao fuliao = fuliaoList.get(temp_i);
										%>
									
										<tr>
											<td rowspan="6"><%=SystemCache.getFuliaoTypeName(fuliao.getFuliaoTypeId())%>
											<br>
											<%=fuliao.getFnumber()%></td>
											<td rowspan="6" style="text-align:left; max-width: 120px; height: 120px; max-height: 120px;">
												<img style="max-width: 120px; height: 120px; max-height: 120px;"
														src="/<%=fuliao.getImg_ss()%>">
											</td>
											<td class="th">订单号</td><td><%=fuliao.getCompany_orderNumber()%></td>
											<td rowspan="6"><%=fuliao.getPlan_quantity() %></td>
											<td rowspan="6"><%=fuliao.getMemo()%></td>
											
											
										</tr>
											<tr><td class="th">款号</td><td><%=fuliao.getCompany_productNumber()%></td></tr>
											<tr><td class="th">颜色</td><td><%=fuliao.getColor()%></td></tr>
											<tr><td class="th">尺码</td><td><%=fuliao.getSize()%></td></tr>
											<tr><td class="th">批次</td><td><%=fuliao.getBatch()%></td></tr>
											<tr><td class="th">国家</td><td><%=fuliao.getCountry() %></td></tr>
											<%}%>
											</tbody>

										</table>
											
										<div id="tip" class="auto_bottom">
											
										</div>

										<p class="pull-right auto_bottom" style="margin-top: 5px;">
											<span id="created_user">打印人：<%=SystemContextUtils.getCurrentUser(session).getLoginedUser().getName()%></span>
											<span id="date"> 日期：<%=DateTool.formatDateYMD(DateTool.getYanDate(DateTool.now()))%></span>
											<span id="page"> 页码：<%=current_page+"/"+total_page%></span>
										</p>

										</table>

									</div>
								</form>
							</div>
						</div>
					</div></div>
				<%} %>
		<script type="text/javascript">
		window.print();
	</script>
	</body>
</html>