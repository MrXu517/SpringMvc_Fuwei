<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.entity.finishstore.FinishStoreOut"%>
<%@page import="com.fuwei.entity.finishstore.FinishStoreOutDetail"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	FinishStoreOut finishStoreOut = (FinishStoreOut)request.getAttribute("finishStoreOut");
	List<FinishStoreOutDetail> detaillist = finishStoreOut.getDetaillist();
	if (detaillist == null) {
		detaillist = new ArrayList<FinishStoreOutDetail>();
	}
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>打印成品发货单 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8">
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<!-- 为了让IE浏览器运行最新的渲染模式 -->
		<link href="css/printorder/print.css" rel="stylesheet" type="text/css" />
		<script src="js/plugins/jquery-1.10.2.min.js"></script>
		<script src="js/common/common.js" type="text/javascript"></script>
		<script src="js/plugins/jquery-barcode.min.js"></script>
		<link href="css/plugins/ui.jqgrid.css" rel="stylesheet"
			type="text/css" />
		
		
		<style type="text/css">
#mainTb{  margin-top: 10px;}
#previewImg {max-width: 200px;max-height: 150px;}
div.name{   margin-left: 15px; width: 100px; display: inline-block;}
#mainTb td,.noborder .table>tbody>tr>td[rowspan],#mainTb th{text-align:center;}
body {
  margin: auto;
  font-family: "Microsoft Yahei", "Verdana", "Tahoma, Arial",
 "Helvetica Neue", Helvetica, Sans-Serif,"SimSun";
}
#tablename{border: none;}
</style>

	</head>
	<body>
		<div class="container-fluid gridTab auto_container ">
						<div class="row">
							<div class="col-md-12">
								<form class="saveform">
									<div class="clear"></div>
									<div class="col-md-12 tablewidget">
										<table class="table">
											<caption id="tablename">
												桐庐富伟针织厂成品发货单<div table_id="<%=finishStoreOut.getNumber()%>" class="id_barcode"></div>
											</caption>
										</table>
										<table class="table table-responsive noborder">
											<tbody>
												<tr>
													<td colspan="2">
													<table class="table table-responsive table-bordered">
															<tbody>
																<tr>
																	<td rowspan="7" width="30%">
																		<a class="thumbnail"
																			target="_blank"> <img id="previewImg"
																				alt="200 x 100%" src="/<%=finishStoreOut.getImg_s()%>">
																		</a>
																	</td>
																	<td width="300px">
																		<div class="name">订单号：</div><span class="value"><%=finishStoreOut.getOrderNumber()%></span>
																	</td>
																	<td>
																		<div class="name">公司：</div><span class="value"><%=SystemCache.getCompanyShortName(finishStoreOut.getCompanyId())%></span>
																	</td>
																</tr>
																<tr>
																	<td>
																		<div class="name">货号：</div><span class="value"><%=finishStoreOut.getCompany_productNumber()%></span>
																	</td>
																	<td>
																		<div class="name">客户：</div><span class="value"><%=SystemCache.getCustomerName(finishStoreOut.getCustomerId())%></span>
																	</td>
																</tr>
																<tr>
																	<td>
																		<div class="name">款名：</div><span class="value"><%=finishStoreOut.getName()%></span>
																	</td>
																	<td>
																		<div class="name">跟单：</div><span class="value"><%=SystemCache.getEmployeeName(finishStoreOut.getCharge_employee())%></span>
																	</td>
																</tr>
																<tr><td colspan="2" style="font-weight: bold;"><div class="name">发货时间：</div><span class="value"><%=DateTool.formatDateYMD(finishStoreOut.getDate())%></span></td></tr>
															</tbody>


														</table>
													</td>
												</tr>
											</tbody>
										</table>
					
										<table id="mainTb"
											class="table table-responsive table-bordered detailTb">
											<thead>
												<tr><th width="30px">
														序号
													</th>
											<%
											int col = 0;
											if(finishStoreOut.getCol1_id()!=null){
											col++;
											 %>
											<th rowspan="2" width="70px">
												<%=SystemCache.getPackPropertyName(finishStoreOut.getCol1_id()) %>
											</th>
											<%} %>
											
											<%if(finishStoreOut.getCol2_id()!=null){ 
											col++;%>
											<th rowspan="2" width="70px">
												<%=SystemCache.getPackPropertyName(finishStoreOut.getCol2_id()) %>
											</th>
											<%} %>
											<%if(finishStoreOut.getCol3_id()!=null){ 
											col++;%>
											<th rowspan="2" width="70px">
												<%=SystemCache.getPackPropertyName(finishStoreOut.getCol3_id()) %>
											</th>
											<%} %>
											<%if(finishStoreOut.getCol4_id()!=null){ 
											col++;%>
											<th rowspan="2" width="70px">
												<%=SystemCache.getPackPropertyName(finishStoreOut.getCol4_id()) %>
											</th>
											<%} %>
											<th rowspan="2" width="60px">
												颜色
											</th>
											<th rowspan="2" width="60px">
												通知发货数量
											</th>
											<th rowspan="2" width="60px">
												通知发货箱数
											</th>
											<th rowspan="2" width="60px">
												本次发货数量
											</th>
											<th rowspan="2" width="60px">
												本次发货箱数
											</th>
												</tr>
											</thead>
											<tbody>
												<%
													int i = 0 ;
													for (FinishStoreOutDetail detail : detaillist) {
												%>
												<tr class="tr">
													<td><%=++i%></td>
													<td>
										<%if(finishStoreOut.getCol1_id()!=null){ %>
											<%=detail.getCol1_value()==null?"":detail.getCol1_value() %>
										</td>
										<%} %>
										<%if(finishStoreOut.getCol2_id()!=null){ %>
										<td>
											<%=detail.getCol2_value()==null?"":detail.getCol2_value() %>
										</td>
										<%} %>	
										<%if(finishStoreOut.getCol3_id()!=null){ %>
										<td>
											<%=detail.getCol3_value()==null?"":detail.getCol3_value() %>
										</td>
										<%} %>
										<%if(finishStoreOut.getCol4_id()!=null){ %>
										<td>
											<%=detail.getCol4_value()==null?"":detail.getCol4_value() %>
										</td>
										<%} %>


													<td><%=detail.getColor()%></td>
													<td><%=detail.getNotice_quantity()%></td>
													<td><%=detail.getNotice_cartons()%></td>
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
											<span id="created_user">制单人：<%=SystemCache.getUserName(finishStoreOut.getCreated_user())%></span>
											<span id="date"> 制单日期：<%=DateTool.formatDateYMD(DateTool.getYanDate(finishStoreOut.getCreated_at()))%></span>
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