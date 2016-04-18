<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Order"%>
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
	Boolean has_print = SystemCache.hasAuthority(session,"finishstore/print");
	Boolean has_edit = SystemCache.hasAuthority(session,"finishstore/edit");
	Boolean has_delete = SystemCache.hasAuthority(session,"finishstore/delete");
	Boolean deletable = finishStoreOut.deletable();
	Boolean has_datacorrect_delete = SystemCache.hasAuthority(session,"data/correct");//数据纠正
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>成品发货单详情 -- 桐庐富伟针织厂</title>
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
		<%@ include file="../../common/head.jsp"%>
		<div id="Content">
			<div id="main">
				<div class="breadcrumbs" id="breadcrumbs">
					<ul class="breadcrumb">
						<li>
							<i class="fa fa-home"></i>
							<a href="user/index">首页</a>
						</li>
						<li>
							<a href="order/detail/<%=finishStoreOut.getOrderId()%>">订单详情</a>
						</li>
						<li>
							<a target="_blank" href="finishstore_workspace/workspace">成品工作台</a>
						</li>
						<li class="active">
							成品发货单详情
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid materialorderWidget k4printpage">
						<div class="row">
							<div class="col-md-12">
								<%
									if(has_delete && deletable){
								%>
								<button data-cid="<%=finishStoreOut.getId()%>" type="button" class="btn btn-danger pull-right" id="deleteBtn">删除</button>
								<%
									}else if(has_datacorrect_delete && !deletable){
								%>
								<button data-cid="<%=finishStoreOut.getId()%>" type="button" class="btn btn-danger pull-right" id="deleteBtn_datacorrect">数据纠正：删除</button>
								<%} %>
								
								<%
									if(has_print){
								%>
								<a target="_blank" href="finishstore_out/print/<%=finishStoreOut.getId()%>" type="button" class="btn btn-success">打印</a>
								<%
									}
								%>
								<%
									if(has_edit && finishStoreOut.isEdit()){
								%>
								<a target="_blank" href="finishstore_out/put/<%=finishStoreOut.getId()%>" type="button" class="btn btn-success">编辑</a>
								<%
									}
								%>
								<form class="saveform">
									<input type="hidden" name="id" value="" />
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
																		<a href="/<%=finishStoreOut.getImg()%>" class="thumbnail"
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
																<tr><td colspan="2"><div class="name">发货时间：</div><span class="value"><%=DateTool.formatDateYMD(finishStoreOut.getDate())%></span></td></tr>
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
											if(finishStoreOut.getCol1_id()!=null){
											col++;
											 %>
											<th rowspan="2" width="80px">
												<%=SystemCache.getPackPropertyName(finishStoreOut.getCol1_id()) %>
											</th>
											<%} %>
											
											<%if(finishStoreOut.getCol2_id()!=null){ 
											col++;%>
											<th rowspan="2" width="80px">
												<%=SystemCache.getPackPropertyName(finishStoreOut.getCol2_id()) %>
											</th>
											<%} %>
											<%if(finishStoreOut.getCol3_id()!=null){ 
											col++;%>
											<th rowspan="2" width="80px">
												<%=SystemCache.getPackPropertyName(finishStoreOut.getCol3_id()) %>
											</th>
											<%} %>
											<%if(finishStoreOut.getCol4_id()!=null){ 
											col++;%>
											<th rowspan="2" width="80px">
												<%=SystemCache.getPackPropertyName(finishStoreOut.getCol4_id()) %>
											</th>
											<%} %>
											<th rowspan="2" width="40px">
												颜色
											</th>
											<th rowspan="2" width="40px">
												每箱数量
											</th>
											<th rowspan="2" width="60px">
												通知发货件数
											</th>
											<th rowspan="2" width="60px">
												通知发货箱数
											</th>
											<th rowspan="2" width="60px">
												实际发货件数
											</th>
											<th rowspan="2" width="60px">
												实际发货箱数
											</th>
												</tr>
											</thead>
											<tbody>
												<%
													for (FinishStoreOutDetail detail : detaillist) {
												%>
												<tr class="tr">
													<%if(finishStoreOut.getCol1_id()!=null){ %>
										<td>
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
													<td><%=detail.getPer_carton_quantity()%></td>
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
											<span id="date"> 制单日期：<%=DateTool.formatDateYMD(finishStoreOut.getCreated_at())%></span>
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
	<script type="text/javascript">
		$(".id_barcode").each(function(){
			var id =$(this).attr("table_id");
			$(this).barcode(id, "code128",{barWidth:2, barHeight:30,showHRI:true});
		});	
		//删除单据 -- 开始
		$("#deleteBtn").click( function() {
			var id = $(this).attr("data-cid");
			if (!confirm("确定要删除该成品发货单吗？")) {
				return false;
			}
			$.ajax( {
				url :"finishstore_out/delete/" + id,
				type :'POST'
			}).done( function(result) {
				if (result.success) {
					Common.Tip("删除成品发货单成功", function() {
						$("#breadcrumbs li.active").prev().find("a").click();
					});
				}
			}).fail( function(result) {
				Common.Error("删除成品发货单失败：" + result.responseText);
			}).always( function() {
	
			});
			return false;
		});
		//删除单据  -- 结束
		
		//数据纠正：删除单据 -- 开始
		$("#deleteBtn_datacorrect").click( function() {
			var id = $(this).attr("data-cid");
			if (!confirm("该成品发货单已打印发货， 您是否确定要进行数据纠正：删除？")) {
				return false;
			}
			$.ajax( {
				url :"finishstore_out/delete/" + id,
				type :'POST'
			}).done( function(result) {
				if (result.success) {
					Common.Tip("数据纠正成功：" +  result.message, function() {
						$("#breadcrumbs li.active").prev().find("a").click();
					});
				}
			}).fail( function(result) {
				Common.Error("数据纠正：删除成品发货单失败：" + result.responseText);
			}).always( function() {
	
			});
			return false;
		});
		//数据纠正：删除单据  -- 结束
	</script>
	</body>
</html>