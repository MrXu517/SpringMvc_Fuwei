<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.producesystem.Fuliao"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="com.fuwei.entity.producesystem.FuliaoInNotice"%>
<%@page import="com.fuwei.entity.producesystem.FuliaoInNoticeDetail"%>
<%@page import="com.fuwei.entity.producesystem.Location"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	
	FuliaoInNotice notice = (FuliaoInNotice) request.getAttribute("notice");
	List<FuliaoInNoticeDetail> detaillist = (List<FuliaoInNoticeDetail>)request.getAttribute("detaillist");
	if (detaillist == null) {
		detaillist = new ArrayList<FuliaoInNoticeDetail>();
	}
	Map<Integer,List<Map<String,Object>>> locationMap = (Map<Integer,List<Map<String,Object>>>) request.getAttribute("locationMap");
	Map<Integer,Boolean> locationContains = new HashMap<Integer,Boolean>();
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>创建通用辅料入库单 -- 桐庐富伟针织厂</title>
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
		<link href="css/plugins/ui.jqgrid.css" rel="stylesheet"
			type="text/css" />
		<script src="js/order/ordergrid.js" type="text/javascript"></script>
		<script src="js/fuliaoinout/add_in_common.js" type="text/javascript"></script>
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
							创建通用辅料入库单
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid materialorderWidget">
						<div class="row">
							<div class="col-md-12">
								<form class="saveform">
									<input type="hidden" name="id" value="" />
									<input type="hidden" name="fuliaoin_noticeId" value="<%=notice.getId() %>" />
									<div class="clear"></div>
									<div class="col-md-12 tablewidget">
										<table class="table">
											<caption id="tablename">
												桐庐富伟针织厂通用辅料入库单
												<button type="submit"
													class="pull-right btn btn-danger saveTable"
													data-loading-text="正在保存...">
													确认入库
												</button>
											</caption>
										</table>
										
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
													</th>
													<th width="8%">
														来源
													</th>
													<th width="10%">
														入库数量(个)
													</th>
													<th width="15%">
														库位
													</th>
												</tr>
											</thead>
											<tbody>
												<%
													for (FuliaoInNoticeDetail detail : detaillist) {
														boolean flag = false;
														List<Map<String,Object>> templist = locationMap.get(detail.getFuliaoId());
												%>
												<tr class="tr" data='<%=SerializeTool.serialize(detail)%>'>
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
													<td><%=SystemCache.getFactoryName(detail.getFuliaoPurchaseFactoryId())%>
													</td>
													<td><%=detail.getQuantity()%></td>
													<td><select class="locationId form-control value require" name="locationId">
														<%for(Map<String,Object> item: templist) {
															Integer locationId = (Integer)item.get("locationId");
															if(!flag && !locationContains.containsKey(locationId)){
																locationContains.put(locationId,true);
																flag=true;
																%>
															<option value="<%=locationId %>" selected><%=SystemCache.getLocationNumber(locationId) %></option>
															<%}else{ %>
																<option value="<%=locationId %>"><%=SystemCache.getLocationNumber(locationId) %></option>
															<%} %>
														<%} %>
													</td>
												</tr>
												<%
													}
												%>
											</tbody>

										</table>
											
										<div id="tip" class="auto_bottom">
											
										</div>

										<p class="pull-right auto_bottom">
											<span id="created_user">制单人：<%=SystemCache.getUserName(SystemContextUtils
							.getCurrentUser(session).getLoginedUser().getId())%></span>
											<span id="date"> 制单日期：<%=DateTool.formatDateYMD(DateTool.now())%></span>
										</p>

										</table>

									</div>
								</form>
							</div>
						</div>
					</div>

				</div>
			</div>
		</div>
	</body>
</html>