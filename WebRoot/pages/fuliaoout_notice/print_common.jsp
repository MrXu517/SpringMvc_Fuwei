<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.producesystem.Fuliao"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.entity.producesystem.FuliaoOutNotice"%>
<%@page import="com.fuwei.entity.producesystem.FuliaoOutNoticeDetail"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	FuliaoOutNotice notice = (FuliaoOutNotice)request.getAttribute("fuliaoOutNotice");
	List<FuliaoOutNoticeDetail> detaillist = notice.getDetaillist();
	if (detaillist == null) {
		detaillist = new ArrayList<FuliaoOutNoticeDetail>();
	}
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>打印通用辅料出库通知单 -- 桐庐富伟针织厂</title>
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
												桐庐富伟针织厂通用辅料出库通知单<div table_id="<%=notice.getNumber()%>" class="id_barcode"></div>
											</caption>
										</table>
										<p class="auto_bottom" style="padding-top: 15px;">
											<span id="created_user">制单人：<%=SystemCache.getUserName(notice.getCreated_user())%></span>
											<span id="date"> 制单日期：<%=DateTool.formatDateYMD(DateTool.getYanDate(notice.getCreated_at()))%></span>
										</p>
										<table id="mainTb"
											class="table table-responsive table-bordered">
											<thead>
												<tr>
													<th width="10%">
														辅料类型
													</th><th width="15%">
														图片
													</th><th width="10%">
														订单号
													</th><th width="8%">
														款号
													</th><th width="8%">
														国家
													</th><th width="8%">
														颜色
													</th><th width="8%">
														尺码
													</th><th width="8%">
														批次
													</th>
													<th width="10%">
														预出库数量(个)
													</th>
													<th width="20%">
														备注
													</th>
												</tr>
											</thead>
											<tbody>
												<%
													for (FuliaoOutNoticeDetail detail : detaillist) {
												%>
												<tr class="tr">
													<td><%=SystemCache.getFuliaoTypeName(detail.getFuliaoTypeId())%><br><%=detail.getFnumber()%></td>
													<td><a href="/<%=detail.getImg()%>" class="" target="_blank"> 
														<img class="fuliaoImg" src="/<%=detail.getImg_ss()%>">
																		</a></td>
													<td><%=detail.getCompany_orderNumber()%></td>
													<td><%=detail.getCompany_productNumber()%></td>
													<td><%=detail.getCountry()%></td>
													<td><%=detail.getColor()%></td>
													<td><%=detail.getSize()%></td>
													<td><%=detail.getBatch()%></td>
													<td><%=detail.getQuantity()%></td>
													<td><%=detail.getMemo() == null ? "" : detail.getMemo()%></td>
												</tr>
												<%
													}
												%>
											</tbody>

										</table>
											
										<div id="tip" class="auto_bottom">
											
										</div>

										<p class="pull-right auto_bottom" style="padding-top: 15px;">
											<span id="date"> 打印日期：<%=DateTool.formatDateYMD(DateTool.getYanDate(DateTool.now()))%></span>
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