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
	Boolean has_print = SystemCache.hasAuthority(session,"fuliaoinout_notice/print");
	Boolean has_edit = SystemCache.hasAuthority(session,"fuliaoinout_notice/edit");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>通用辅料出库通知单详情 -- 桐庐富伟针织厂</title>
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
		<script src="js/plugins/jquery-barcode.min.js"></script>
		<link href="css/plugins/ui.jqgrid.css" rel="stylesheet"
			type="text/css" />
		
		
		<style type="text/css">
#tablename {
  font-weight: bold;
  font-size: 30px;
  margin-bottom: 15px;
}
.table>thead>tr>td {
	padding: 3px 8px;
}
.table-bordered>thead>tr>th, .table-bordered>thead>tr>td{border-bottom-width:1px;}
#mainTb {
	margin-top: 10px;  border-color: #000;
}
.table {
	margin-bottom: 0;
}
caption {
	font-weight: bold;
}
#previewImg {
	max-width: 200px;
	max-height: 150px;
}
#mainTb thead th{ background: #AEADAD;}
#mainTb thead th,#mainTb tbody tr td{border-color:#000;}
body {
  margin: auto;
  font-family: "Microsoft Yahei", "Verdana", "Tahoma, Arial",
 "Helvetica Neue", Helvetica, Sans-Serif,"SimSun";
}
.noborder.table>tbody>tr>td{border:none;}
.noborder.table{font-weight:bold;}
div.name{  width: 100px; display: inline-block;}
.checkBtn{height:25px;width:25px;}
tr.disable{background:#ddd;}
#created_user{  margin-right: 50px;}
#statusDiv{position: absolute;top: 0;right: 100px; width: 38px;font-size: 20px;font-style: oblique;
    transform: rotate(15deg);
    -o-transform: rotate(15deg);
    -webkit-transform: rotate(15deg);
    padding: 0 4px;}
#statusDiv.success{border: 2px solid #47a447;color: #47a447;}
#statusDiv.fail{border: 2px solid red;color: red;}
#statusDiv.wait{border: 2px solid #eea236;color: #eea236;}
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
							<a href="fuliao_workspace/commonfuliao?tab=fuliaoinnotice">通用辅料列表及出入库情况</a>
						</li>
						<li class="active">
							通用辅料出库通知单详情
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid materialorderWidget k4printpage">
						<div class="row">
							<div class="col-md-12">
								<%
									if(has_print){
								%>
								<a target="_blank" href="fuliaoout_notice/print/<%=notice.getId()%>" type="button" class="btn btn-success">打印</a>
								<%
									}
								%>
								<%
									if(has_edit){
								%>
								<a target="_blank" href="fuliaoout_notice/put/<%=notice.getId()%>" type="button" class="btn btn-success">编辑</a>
								<%
									}
								%>
								<form class="saveform">
									<input type="hidden" name="id" value="" />
									<div class="clear"></div>
									<div class="col-md-12 tablewidget">
										<table class="table">
											<caption id="tablename">
												桐庐富伟针织厂通用辅料出库通知单<div table_id="<%=notice.getNumber()%>" class="id_barcode"></div>
											<%
											int status = notice.getStatus();
											String statusDivClass =  "";
											if(status == 6){
												statusDivClass = "success";
											}else if(status == -1){
												statusDivClass = "fail";
											}else{
												statusDivClass = "wait";
											}
											
											 %>
										<div id="statusDiv" class="<%=statusDivClass %>"><%=notice.getStateString() %></div>
											</caption>
										</table>
										<p class="pull-right auto_bottom" style="padding-top: 15px;">
											<span id="created_user">制单人：<%=SystemCache.getUserName(notice.getCreated_user())%></span>
											<span id="date"> 日期：<%=DateTool.formatDateYMD(notice.getCreated_at())%></span>
										</p>
										<table id="mainTb"
											class="table table-responsive table-bordered detailTb">
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
													<td><a href="/<%=detail.getImg()%>" class=""
																			target="_blank"> <img id="previewImg"
																				alt="200 x 100%" src="/<%=detail.getImg_ss()%>">
																		</a></td>
													<td><%=detail.getCompany_orderNumber()%></td>
													<td><%=detail.getCompany_productNumber()%></td>
													<td><%=detail.getCountry()%></td>
													<td><%=detail.getColor()%></td>
													<td><%=detail.getSize()%></td>
													<td><%=detail.getBatch()%></td>
													<td><%=detail.getQuantity()%></td>
													<td  style="word-break: break-all;"><%=detail.getMemo() == null ? "" : detail.getMemo()%></td>
												</tr>
												<%
													}
												%>
											</tbody>

										</table>
											
										<div id="tip" class="auto_bottom">
										
										</div>

										

										</table>

									</div>
								</form>
							</div>
						</div>
					</div>

				</div>
			</div>
		</div>
	<script type="text/javascript">
		$(".id_barcode").each(function(){
			var id =$(this).attr("table_id");
			$(this).barcode(id, "code128",{barWidth:2, barHeight:30,showHRI:true});
		});	
	</script>
	</body>
</html>