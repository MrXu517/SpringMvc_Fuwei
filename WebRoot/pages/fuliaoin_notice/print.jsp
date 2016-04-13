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
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Order order = (Order) request
			.getAttribute("order");
	FuliaoInNotice notice = (FuliaoInNotice)request.getAttribute("fuliaoInNotice");
	List<FuliaoInNoticeDetail> detaillist = notice.getDetaillist();
	if (detaillist == null) {
		detaillist = new ArrayList<FuliaoInNoticeDetail>();
	}
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>打印辅料入库通知单 -- 桐庐富伟针织厂</title>
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
												桐庐富伟针织厂辅料入库通知单<div table_id="<%=notice.getNumber()%>" class="id_barcode"></div>
											</caption>
										</table>
										<table class="table table-responsive noborder">
											<tbody>
												<tr>
													<td colspan="2">
														<table class="table table-responsive noborder">
															<tbody>
																<tr>
																	<td align="center" rowspan="7" width="30%">
																		<a href="/<%=order.getImg()%>" class="thumbnail"
																			target="_blank"> <img id="previewImg"
																				alt="200 x 100%" src="/<%=order.getImg_s()%>">
																		</a>
																	</td>
																	<td width="100px">
																		<div class="name">订单号：</div><span class="value"><%=notice.getOrderNumber()%></span>
																	</td>
																</tr>
																<tr>
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
																		<div class="name">货号：</div><span class="value"><%=notice.getCompany_productNumber()%></span>
																	</td>
																</tr>
																<tr>
																	<td>
																		<div class="name">款名：</div><span class="value"><%=notice.getName()%></span>
																	</td>
																</tr>
																<tr>
																	<td>
																		<div class="name">跟单：</div><span class="value"><%=SystemCache.getEmployeeName(notice.getCharge_employee()) %></span>
																	</td>
																	
																</tr>
															</tbody>


														</table>
													</td>
												</tr>
											</tbody>
										</table>
					
										<table id="mainTb"
											class="table table-responsive table-bordered">
											<thead>
												<tr>
													<th width="8%">
														类型
													</th><th width="20%">
														图片
													</th><th width="15%">
														订单号
													</th><th width="8%">
														款号
													</th><th width="8%">
														国家
													</th><th width="8%">
														颜色
													</th><th width="8%">
														尺码
													</th><th width="6%">
														批次
													</th><th width="8%">
														来源
													</th>
													<th width="10%">
														入库数量(个)
													</th>
												</tr>
											</thead>
											<tbody>
												<%
													for (FuliaoInNoticeDetail detail : detaillist) {
												%>
												<tr class="tr">
													<td><%=SystemCache.getFuliaoTypeName((Integer)detail.getFuliaoTypeId())%><br><%=detail.getFnumber()%></td>
													<td><a href="/<%=detail.getImg()%>" class="" target="_blank"> 
														<img class="fuliaoImg" src="/<%=detail.getImg_ss()%>">
																		</a></td>
													<td><%=detail.getCompany_orderNumber()%></td>
													<td><%=detail.getCompany_productNumber()%></td>
													<td><%=detail.getCountry()%></td>
													<td><%=detail.getColor()%></td>
													<td><%=detail.getSize()%></td>
													<td><%=detail.getBatch()%></td>
													<td><%=SystemCache.getFactoryName((Integer)detail.getFuliaoPurchaseFactoryId())%></td>
													<td><%=detail.getQuantity()%></td>
												</tr>
												<%
													}
												%>
											</tbody>

										</table>
											
										<div id="tip" class="auto_bottom">
											
										</div>

										<p class="pull-right auto_bottom" style="padding-top: 15px;">
											<span id="created_user">制单人：<%=SystemCache.getUserName(notice.getCreated_user())%></span>
											<span id="date"> 日期：<%=DateTool.formatDateYMD(DateTool.getYanDate(notice.getCreated_at()))%></span>
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