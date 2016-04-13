<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.producesystem.Fuliao"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.entity.producesystem.FuliaoIn"%>
<%@page import="com.fuwei.entity.producesystem.FuliaoInDetail"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	FuliaoIn object = (FuliaoIn)request.getAttribute("object");
	List<FuliaoInDetail> detaillist = object.getDetaillist();
	if (detaillist == null) {
		detaillist = new ArrayList<FuliaoInDetail>();
	}
	Boolean has_print = SystemCache.hasAuthority(session,"fuliaoinout/print");
	Boolean has_datacorrect_delete = SystemCache.hasAuthority(session,"data/correct");//数据纠正

%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>通用辅料入库单详情 -- 桐庐富伟针织厂</title>
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
							<a href="fuliao_workspace/commonfuliao_workspace">通用辅料工作台</a>
						</li>
						<li class="active">
							通用辅料入库单详情
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid materialorderWidget">
						<div class="row">
							<div class="col-md-12">
								<%
									if(has_print){
								%>
								<a target="_blank" href="fuliaoin/print/<%=object.getId()%>" type="button" class="btn btn-success">打印</a>
								<a target="_blank" href="fuliaoin/print/<%=object.getId() %>/tag" type="button" class="btn btn-success">打印辅料标签</a>
								<%
									}
								%>
								
								<%
									if(has_datacorrect_delete && !object.isDeletable()){
								%>
								<button data-cid="<%=object.getId()%>" type="button" class="btn btn-danger pull-right" id="deleteBtn_datacorrect">数据纠正：删除</button>
								<%} %>
								<form class="saveform">
									<div class="clear"></div>
									<div class="col-md-12 tablewidget">
										<table class="table">
											<caption id="tablename">
												桐庐富伟针织厂通用辅料入库单<div table_id="<%=object.getNumber()%>" class="id_barcode"></div>
											</caption>
										</table>
										<p class="auto_bottom" style="padding-top: 15px;">
											<span id="created_user">制单人：<%=SystemCache.getUserName(object.getCreated_user())%></span>
											<span id="date"> 制单日期：<%=DateTool.formatDateYMD(object.getCreated_at())%></span>
										</p>
										<table id="mainTb"
											class="table table-responsive table-bordered detailTb">
											<thead>
												<tr>
													<th width="8%">
														类型
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
													</th><th width="8%">
														来源
													</th>
													<th width="10%">
														入库数量(个)
													</th><th width="10%">
														库位
													</th>
												</tr>
											</thead>
											<tbody>
												<%
													for (FuliaoInDetail detail : detaillist) {
												%>
												<tr class="tr">
													<td><%=SystemCache.getFuliaoTypeName((Integer)detail.getFuliaoTypeId())%><br><%=detail.getFnumber()%></td>
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
													<td><%=SystemCache.getFactoryName(detail.getFuliaoPurchaseFactoryId())%></td>
													<td><%=detail.getQuantity()%></td>
													<td><%=SystemCache.getLocationNumber(detail.getLocationId())%></td>
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
		//数据纠正：删除单据 -- 开始
		$("#deleteBtn_datacorrect").click( function() {
			var id = $(this).attr("data-cid");
			if (!confirm("该辅料入库单已打印入库，请确保辅料实际未入库再进行删除操作， 您是否确定要进行数据纠正：删除？")) {
				return false;
			}
			$.ajax( {
				url :"fuliaoin/delete/" + id,
				type :'POST'
			}).done( function(result) {
				if (result.success) {
					Common.Tip("数据纠正成功", function() {
						$("#breadcrumbs li.active").prev().find("a").click();
					});
				}
			}).fail( function(result) {
				Common.Error("数据纠正：删除辅料入库单失败：" + result.responseText);
			}).always( function() {
	
			});
			return false;
		});
		//数据纠正：删除单据  -- 结束
	</script>
	</body>
</html>