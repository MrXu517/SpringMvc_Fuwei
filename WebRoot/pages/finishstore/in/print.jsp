<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.entity.finishstore.FinishStoreInDetail"%>
<%@page import="com.fuwei.entity.finishstore.FinishStoreIn"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	FinishStoreIn finishStoreIn = (FinishStoreIn)request.getAttribute("finishStoreIn");
	List<FinishStoreInDetail> detaillist = finishStoreIn.getDetaillist();
	if (detaillist == null) {
		detaillist = new ArrayList<FinishStoreInDetail>();
	}
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>打印成品入库单 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8">
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<link href="css/printorder/print.css" rel="stylesheet" type="text/css" />
		<script src="js/plugins/jquery-1.10.2.min.js"></script>
		<script src="js/common/common.js" type="text/javascript"></script>
		<script src="js/plugins/jquery-barcode.min.js"></script>
		
<link href="css/plugins/ui.jqgrid.css" rel="stylesheet"
			type="text/css" />
		
		
		<style type="text/css">
#mainTb{  margin-top: 10px;}
#previewImg,.fuliaoImg {
	max-width: 200px;
	max-height: 150px;
}
div.name{   margin-left: 15px; width: 100px; display: inline-block;}
#mainTb td{text-align:center;}
</style>

	</head>
	<body>
					<div class="container-fluid gridTab auto_container ">
						<div class="row">
							<div class="col-md-12">
								<form class="saveform">
									<div class="clear"></div>
									<div class="col-md-12 tablewidget">
										<table class="table noborder">
											<caption id="tablename">
												桐庐富伟针织厂成品入库单<div table_id="<%=finishStoreIn.getNumber()%>" class="id_barcode"></div>
											</caption>
										</table>
										<table class="table table-responsive noborder">
											<tbody>
												<tr>
													<td colspan="2">
														<table class="table table-responsive noborder">
															<tbody>
												<tr>
													<td colspan="2">
														<table class="table table-responsive table-bordered">
															<tbody>
																<tr>
																	<td rowspan="7" width="30%">
																		<a href="/<%=finishStoreIn.getImg()%>" class="thumbnail"
																			target="_blank"> <img id="previewImg"
																				alt="200 x 100%" src="/<%=finishStoreIn.getImg_s()%>">
																		</a>
																	</td>
																	<td width="100px">
																		<div class="name">订单号：</div><span class="value"><%=finishStoreIn.getOrderNumber()%></span>
																	</td>
																</tr>
																<tr>
																	<td>
																		<div class="name">公司：</div><span class="value"><%=SystemCache.getCompanyShortName(finishStoreIn.getCompanyId())%></span>
																	</td>
																</tr>
																
																<tr>
																	<td>
																		<div class="name">客户：</div><span class="value"><%=SystemCache.getCustomerName(finishStoreIn.getCustomerId())%></span>
																	</td>
																</tr>
																<tr>
																	<td>
																		<div class="name">货号：</div><span class="value"><%=finishStoreIn.getCompany_productNumber()%></span>
																	</td>
																</tr>
																<tr>
																	<td>
																		<div class="name">款名：</div><span class="value"><%=finishStoreIn.getName()%></span>
																	</td>
																</tr>
																<tr>
																	<td>
																		<div class="name">跟单：</div><span class="value"><%=SystemCache.getEmployeeName(finishStoreIn.getCharge_employee())%></span>
																	</td>
																	
																</tr>
															</tbody>


														</table>
													</td>
												</tr>
											</tbody>
														</table>
													</td>
												</tr>
											</tbody>
										</table>
					
										<table id="mainTb"
											class="table table-responsive table-bordered detailTb">
											<thead>
												<tr>
													<%
											int col = 0;
											if(finishStoreIn.getCol1_id()!=null){
											col++;
											 %>
											<th rowspan="2" width="80px">
												<%=SystemCache.getPackPropertyName(finishStoreIn.getCol1_id()) %>
											</th>
											<%} %>
											
											<%if(finishStoreIn.getCol2_id()!=null){ 
											col++;%>
											<th rowspan="2" width="80px">
												<%=SystemCache.getPackPropertyName(finishStoreIn.getCol2_id()) %>
											</th>
											<%} %>
											<%if(finishStoreIn.getCol3_id()!=null){ 
											col++;%>
											<th rowspan="2" width="80px">
												<%=SystemCache.getPackPropertyName(finishStoreIn.getCol3_id()) %>
											</th>
											<%} %>
											<%if(finishStoreIn.getCol4_id()!=null){ 
											col++;%>
											<th rowspan="2" width="80px">
												<%=SystemCache.getPackPropertyName(finishStoreIn.getCol4_id()) %>
											</th>
											<%} %>
											<th rowspan="2" width="40px">
												颜色
											</th>
											<th rowspan="2" width="40px">
												每箱数量
											</th>
											<th rowspan="2" width="80px">
												本次入库数量
											</th>
											<th rowspan="2" width="60px">
												本次入库箱数
											</th>
												</tr>
											</thead>
											<tbody>
												<%
													for (FinishStoreInDetail detail : detaillist) {
												%>
												<tr class="tr">
													<%if(finishStoreIn.getCol1_id()!=null){ %>
										<td>
											<%=detail.getCol1_value()==null?"":detail.getCol1_value() %>
										</td>
										<%} %>
										<%if(finishStoreIn.getCol2_id()!=null){ %>
										<td>
											<%=detail.getCol2_value()==null?"":detail.getCol2_value() %>
										</td>
										<%} %>	
										<%if(finishStoreIn.getCol3_id()!=null){ %>
										<td>
											<%=detail.getCol3_value()==null?"":detail.getCol3_value() %>
										</td>
										<%} %>
										<%if(finishStoreIn.getCol4_id()!=null){ %>
										<td>
											<%=detail.getCol4_value()==null?"":detail.getCol4_value() %>
										</td>
										<%} %>


													<td><%=detail.getColor()%></td>
													<td><%=detail.getPer_carton_quantity()%></td>
													<td><%=detail.getQuantity()%></td>
													<td><%=detail.getCartons()%></td>
												</tr>
												<%
													}
												%>
											</tbody>

										</table>
											
										<div id="tip" class="auto_bottom">
										
										</div>

										<p class="pull-right auto_bottom" style="padding-top: 15px;">
											<span id="created_user">制单人：<%=SystemCache.getUserName(finishStoreIn.getCreated_user())%></span>
											<span id="date"> 制单日期：<%=DateTool.formatDateYMD(DateTool.getYanDate(finishStoreIn.getCreated_at()))%></span>
											<span id="date"> 入库日期：<%=DateTool.formatDateYMD(DateTool.getYanDate(finishStoreIn.getDate()))%></span>
										</p>

										</table>
									</div>
								</form>
							</div>
						</div>
					</div>

		<script type="text/javascript">
		$(".id_barcode").each(function(){
			var id =$(this).attr("table_id");
			$(this).barcode(id, "code128",{barWidth:2, barHeight:30,showHRI:true});
		});		
		window.print();
	</script>
	</body>
</html>