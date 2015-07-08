<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.ordergrid.StoreInOut"%>
<%@page import="com.fuwei.entity.ordergrid.StoreInOutDetail"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	//原材料出库单
	StoreInOut storeInOut = (StoreInOut) request.getAttribute("storeInOut");
	List<StoreInOutDetail> detaillist = storeInOut == null ? new ArrayList<StoreInOutDetail>() :storeInOut.getDetaillist();
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>原材料出库单 -- 桐庐富伟针织厂</title>
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
		<script src="js/common/common.js" type="text/javascript"></script>
		<link href="css/printorder/print.css" rel="stylesheet" type="text/css" />
		<script src="js/plugins/jquery-barcode.min.js"></script>
		<style type="text/css">
		.table>thead>tr{
			  background: #AEADAD;
		}
		.table>tbody>tr{  background: #ccc;}
		.emptyTr{color:red;}
		#printform button{margin-bottom:5px;}
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
							<a href="workspace/material_workspace">原材料工作台</a>
						</li>
						<li>
							<a href="store_out/detail/<%=storeInOut.getId() %>">原材料出库单 -- 详情</a>
						</li>
						<li class="active">
							出库扫描
						</li>
					</ul>
				</div>
				<div class="body">
					<div style="page-break-after: always">
						<div class="container-fluid gridTb_2 auto_container">
							<div class="row">
								<div class="col-md-12 tablewidget">
									
									<form class="form-horizontal form" role="form">
										<input type="hidden" id="storeOutId" name="storeOutId"
											value="<%=storeInOut.getId() %>" />
										<div class="form-group">
											<label for="tag" class="col-sm-3 control-label">
												纱线标签条形码
											</label>
											<div class="input-group col-md-9">
												<input type="text" class="form-control"
													name="storeInId_detailId" id="tag"
													placeholder="请使用扫码枪扫描入库纱线标签">
												<span class="input-group-btn">
													<button class="btn btn-primary" type="submit">
														确定
													</button> </span>
											</div>
										</div>
										
									</form>
									<form class="form-horizontal" role="form" id="printform">
										<input type="hidden" name="storeOutId"
											value="<%=storeInOut.getId() %>" />
										<input type="hidden" name="details" id="details"/>
										<button type="submit" class="btn btn-primary">扫描完毕，开始打印出库单</button>
										<table class="table table-responsive table-bordered" id="scanTb">
											<thead>
												<tr>
													<th>
														入库ID_明细ID
													</th>
													<th>
														订单号
													</th>
													<th>
														色号
													</th>
													<th>
														材料
													</th>
													<th>
														缸号
													</th>
												</tr>
											</thead>
											<tbody>
												<tr class="emptyTr center">
													<td colspan="5">还未扫描过，请扫描</td>
												</tr>
											</tbody>
										</table>
									</form>
									<table class="table noborder">
										<caption id="tablename">
											桐庐富伟针织厂原材料出库单
											<div table_id="<%=storeInOut.getId() %>" class="id_barcode"></div>
										</caption>
									</table>

									<table id="orderTb" class="tableTb noborder">
										<tbody>
											<tr>
												<td>

													领取单位：
													<span><%=storeInOut == null ? ""
						: (SystemCache.getFactoryName(storeInOut
								.getFactoryId()))%></span>

												</td>
												<td>
													业务员：
													<span><%=storeInOut == null ? ""
						: (SystemCache.getEmployeeName((storeInOut
								.getCharge_employee())))%></span>
												</td>
												<td>
													出库时间：
													<span><%=storeInOut == null ? ""
						: (DateTool.formatDateYMD(DateTool.getYanDate(storeInOut.getDate())))%></span>
												</td>
												<td class="pull-right">

													№：<%=storeInOut.getNumber()%>

												</td>
												<td></td>
											</tr>

											<tr>
												<td colspan="4">
													<table>
														<tr>
															<td class="center" width="10%">
																订单号
															</td>
															<td class="center" width="15%">
																公司
															</td>
															<td class="center" width="15%">
																公司货号
															</td>
															<td class="center" width="15%">
																客户
															</td>
															<td class="center" width="15%">
																品名
															</td>
														</tr>
														<tr>
															<td class="center">
																<span><%=storeInOut.getOrderNumber()%></span>
															</td>
															<td class="center">
																<span><%=SystemCache.getCompanyShortName(storeInOut
								.getCompanyId())%></span>
															</td>
															<td class="center">
																<span><%=storeInOut.getCompany_productNumber()%></span>
															</td>
															<td class="center">
																<span><%=SystemCache.getCustomerName(storeInOut
								.getCustomerId())%></span>
															</td>
															<td class="center">
																<span><%=storeInOut.getName()%></span>
															</td>
														</tr>
														<tr>
															<td class="center" width="10%">
																备注
															</td>
															<td colspan="4">
																<span><%=storeInOut.getMemo() == null ? "" : storeInOut.getMemo()%></span>
															</td>
														</tr>
													</table>
												</td>
												<td></td>
											</tr>
											<tr>
												<td colspan="4">
													<table class="detailTb">

														<thead>
															<tr>
																<td width="15%">
																	色号
																</td>
																<td width="15%">
																	材料
																</td>
																<td width="15%">
																	出库数量(kg)
																</td>
																<td width="15%">
																	缸号
																</td>

															</tr>
														</thead>
														<tbody>
															<%
													for (StoreInOutDetail detail : detaillist) {
												%>
															<tr class="tr">
																<td class="color"><%=detail.getColor()%>
																<td class="material_name"><%=SystemCache.getMaterialName(detail
											.getMaterial())%>
																</td>
																<td class="quantity"><%=(int)detail.getQuantity()%>
																</td>
																<td class="lot_no"><%=detail.getLot_no()%>
																</td>
															</tr>

															<%
													}
														int i = detaillist.size();
														for (; i < 6; ++i) {
												%>
															<tr class="tr">
																<td class="color">
																	&nbsp;
																</td>
																<td class="material_name">
																</td>
																<td class="quantity">
																</td>
																<td class="lot_no">
																</td>
															</tr>
															<%
													}
												%>
														</tbody>
													</table>

													<!-- 	<table class="detailTb auto_height stickedTb">
									<tbody>
										<tr>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
										</tr>
									</tbody>
								</table> -->
												</td>
												<td></td>
											</tr>
											<tr>
												<td colspan="4">
													<div id="tip" class="auto_bottom">
														<div>
															说明：1.此单说明了本次出入库的相关内容，请充分阅读并理解，如有疑问及时联系我方
														</div>
														<div class="tip_line">
															2.材料品质及颜色要确保准确，颜色色牢度须达到4级以上。
														</div>
														<div class="tip_line">
															3.不得含有偶氮、PCP、甲醛、APEO。不得有特殊气味，无致敏致癌物质。
														</div>
														<div class="tip_line">
															4.贵单位须妥善保管此单据，结账时须提供此单据
														</div>

													</div>
												</td>
												<td></td>
											</tr>
										</tbody>
									</table>

									<table id="mainTb" class="noborder">

									</table>



									<p class="pull-right auto_bottom">
										<span id="created_user">制单人：<%=SystemCache.getUserName(storeInOut
								.getCreated_user())%></span>
										<span id="receiver_user">收货人：</span>
										<span id="date"> 打印日期：<%=DateTool.formatDateYMD(DateTool.getYanDate(DateTool.now()))%></span>
									</p>



								</div>

							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<script type="text/javascript">
	$(".id_barcode").each( function() {
		var id = $(this).attr("table_id");
		$(this).barcode(id, "code128", {
			barWidth :2,
			barHeight :30,
			showHRI :false
		});
	});
	$("#tag").click(function(){
		$("#tag").focus();
		$("#tag").select();
	});
	$("#tag").focus();
	$("#tag").select();
	var $form = $(".form");
	$form.submit( function() {
		var formdata = $(this).serializeJson();
		$.ajax( {
			url :"store_out/print_scan_check",
			type :'POST',
			data :$.param(formdata),
			success : function(result) {
				if (result.success) {
					$("#tag").focus();
					$("#tag").select();
					//判断是否已有    入库ID_明细ID的行
					var tabledata = ScanTbInstance.getTableData();
					for(var i = 0 ; i < tabledata.length;++i){
						if(tabledata[i].storeInId_detailId == result.data.storeInId_detailId){
							return;
						}
					}
					$("#scanTb .emptyTr").remove();
					ScanTbInstance.addRow(result.data);
				}
			},
			error : function(result) {
				Common.Error("不能出库：" + result.responseText);
			}

		});
		return false;
	});
	
	$printform = $("#printform");
	$printform.submit(function(){
		var tabledata = ScanTbInstance.getTableData();
		var formdata = $(this).serializeJson();
		formdata.details = JSON.stringify(tabledata);
		$.ajax( {
			url :"store_out/print_scan",
			type :'POST',
			data :$.param(formdata),
			success : function(result) {
				location.href="store_out/print/" + result.id;
			},
			error : function(result) {
				Common.Error("不能出库：" + result.responseText);
			}

		});
		return false;
	});
	
	var tbOptions = {
		focus:false,
		tableEle:$("#scanTb")[0],
		showNoOptions : {
					display :false
				},
				colnames : [{
							name :'storeInId_detailId',
							colname :'入库ID_明细ID',
							width :'15%'
						},
						{
							name :'orderNumber',
							colname :'订单号',
							width :'15%'
						},{
							name :'color',
							colname :'色号',
							width :'15%'
						},
						{
							name :'material_name',
							colname :'材料品种',
							width :'15%'
						},
						{
							name :'lot_no',
							colname :'缸号',
							width :'15%'
						}
						]
			}
	var ScanTbInstance = TableTools.createTableInstance(tbOptions);
</script>
	</body>
</html>