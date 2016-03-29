<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.producesystem.StoreReturn"%>
<%@page import="com.fuwei.entity.producesystem.StoreReturnDetail"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/";
	//原材料退货单
	StoreReturn storeReturn = (StoreReturn) request.getAttribute("storeReturn");
	List<StoreReturnDetail> detaillist = storeReturn == null ? new ArrayList<StoreReturnDetail>() :storeReturn.getDetaillist();
	//权限
	Boolean has_delete = SystemCache.hasAuthority(session,"store_return/delete");
	Boolean has_print = SystemCache.hasAuthority(session,"store_return/print");
	Boolean deletable = storeReturn.deletable();
	Boolean has_datacorrect_delete = SystemCache.hasAuthority(session,"data/correct");//数据纠正
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>原材料退货单 -- 桐庐富伟针织厂</title>
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
			table td, table th{
				padding:8px;
			}
			a.btn{
				margin-right:30px;
			}
			#previewImg {
				max-width: 200px;
				max-height: 150px;
			}
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
						<li class="active">
							原材料退货单 --- 详情
						</li>
					</ul>
				</div>
				<div class="body">
		<div style="page-break-after: always">
			<div class="container-fluid gridTb_2 auto_container">
				<div class="row">
					<div class="col-md-12 tablewidget">
						<%
							if(has_print){
						%>
						<a target="_blank" href="store_return/print/<%=storeReturn.getId()%>" type="button" class="btn btn-success">打印</a>
						<%
							}
						%>
						
						<%
							if(has_delete && deletable){
						%>
						<button data-cid="<%=storeReturn.getId()%>" type="button" class="btn btn-danger" id="deleteBtn">删除</button>
						<%
							}else if(has_datacorrect_delete && !deletable){
						%>
						<button data-cid="<%=storeReturn.getId()%>" type="button" class="btn btn-danger" id="deleteBtn_datacorrect">数据纠正：删除</button>
						<%} %>
						<table class="table noborder">
							<caption id="tablename">
								桐庐富伟针织厂原材料退货单<div table_id="<%=storeReturn.getNumber()%>" class="id_barcode"></div>
							</caption>
						</table>

						<table id="orderTb" class="tableTb noborder">
							<tbody>
								<tr>
									<td>
										染色单位：
										<span><%=storeReturn == null ? ""
						: (SystemCache.getFactoryName(storeReturn
								.getFactoryId()))%></span>

									</td>
									<td>
										退货时间：
										<span><%=storeReturn == null ? ""
						: (DateTool.formatDateYMD(storeReturn.getDate()))%></span>
									</td>
									<td class="pull-right">

										№：<%=storeReturn.getNumber()%>

									</td>
									<td></td>
								</tr>

								<tr>
									<td colspan="4">
										<table>
											<tbody>
																<tr>
																	<td rowspan="4" width="30%">
																		<a href="/<%=storeReturn.getImg()%>" class="thumbnail"
																			target="_blank"> <img id="previewImg"
																				alt="200 x 100%" src="/<%=storeReturn.getImg_s()%>">
																		</a>
																	</td>
																</tr>
																<tr>
																	<td style="vertical-align: top;">
																		<table>
																			<tr>
																				<th class="center" width="10%">
																					订单号
																				</th>
																				<th class="center" width="10%">
																					公司
																				</th>
																				<th class="center" width="15%">
																					公司货号
																				</th>
																				<th class="center" width="15%">
																					业务员
																				</th>
																				<th class="center" width="20%">
																					品名
																				</th>
																			</tr>
																			<tr>
																				<td class="center"><%=storeReturn.getOrderNumber()%>

																				</td>
																				<td class="center"><%=SystemCache
							.getCompanyShortName(storeReturn.getCompanyId())%>

																				</td>
																				<td class="center"><%=storeReturn.getCompany_productNumber() == null ? ""
					: storeReturn.getCompany_productNumber()%>
																				</td><td>
																					<span><%=storeReturn == null ? ""
																	: (SystemCache.getEmployeeName((storeReturn
																			.getCharge_employee())))%></span>
																				</td>
																				<td class="center"><%=storeReturn.getName() == null ? "" : storeReturn
					.getName()%>
																				</td>
																			</tr>
																			<tr>
																				<td class="center">
																					备注
																				</td>
																				<td colspan="4">
																					<%=storeReturn.getMemo() == null ? "" : storeReturn.getMemo()%>
																				</td>
																			</tr>
																		</table>
																	</td>
																</tr>
															</tbody>
										
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
														材料
													</td>
													<td width="15%">
														颜色
													</td>
													<td width="15%">
														缸号
													</td>
													<td width="15%">
														退货数量
													</td>

												</tr>
											</thead>
											<tbody>
												<%
													for (StoreReturnDetail detail : detaillist) {
												%>
												<tr class="tr">
													<td class="material_name"><%=SystemCache.getMaterialName(detail.getMaterial())%>
													<td class="color"><%=detail.getColor()%>
													</td>
													<td class="lot_no"><%=detail.getLot_no()%>
													</td>
													<td class="quantity"><%=detail.getQuantity()%>
													</td>
												</tr>

												<%
													}
														int i = detaillist.size();
														for (; i < 6; ++i) {
												%>
												<tr class="tr">
													<td class="material_name">&nbsp;</td>
													<td class="color">
													</td>
													<td class="lot_no">
													</td>
													<td class="quantity">
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
								
							</tbody>
						</table>

						<table id="mainTb" class="noborder">

						</table>



						<p class="pull-right auto_bottom">
							<span id="created_user">制单人：<%=SystemCache.getUserName(storeReturn
								.getCreated_user())%></span>
							<span id="receiver_user">收货人：</span>
							
						</p>



					</div>

				</div>
			</div>
		</div>
</div>
</div></div>
		
	<script type="text/javascript">
		/*设置当前选中的页*/
			var $a = $("#left li a[href='workspace/material_workspace']");
			setActiveLeft($a.parent("li"));
		$(".id_barcode").each(function(){
			var id =$(this).attr("table_id");
			$(this).barcode(id, "code128",{barWidth:2, barHeight:30,showHRI:false});
		});	
		//删除单据 -- 开始
		$("#deleteBtn").click( function() {
			var id = $(this).attr("data-cid");
			if (!confirm("确定要删除该原材料退货单吗？")) {
				return false;
			}
			$.ajax( {
				url :"store_return/delete/" + id,
				type :'POST'
			}).done( function(result) {
				if (result.success) {
					Common.Tip("删除原材料退货单成功", function() {
						$("#breadcrumbs li.active").prev().find("a").click();
					});
				}
			}).fail( function(result) {
				Common.Error("删除原材料退货单失败：" + result.responseText);
			}).always( function() {
	
			});
			return false;
		});
		//删除单据  -- 结束
		
		//数据纠正：删除单据 -- 开始
		$("#deleteBtn_datacorrect").click( function() {
			var id = $(this).attr("data-cid");
			if (!confirm("该原材料退货单已打印退货， 您是否确定要进行数据纠正：删除？")) {
				return false;
			}
			$.ajax( {
				url :"store_return/delete/" + id,
				type :'POST'
			}).done( function(result) {
				if (result.success) {
					Common.Tip("数据纠正成功：" +  result.message, function() {
						$("#breadcrumbs li.active").prev().find("a").click();
					});
				}
			}).fail( function(result) {
				Common.Error("数据纠正：删除原材料退货单失败：" + result.responseText);
			}).always( function() {
	
			});
			return false;
		});
		//数据纠正：删除单据 -- 结束
	</script>
	</body>
</html>