<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.entity.Material"%>
<%@page import="com.fuwei.entity.Customer"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.entity.producesystem.HalfStoreReturn"%>
<%@page import="com.fuwei.entity.GongXu"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/";
	HalfStoreReturn object = (HalfStoreReturn) request
	.getAttribute("halfStoreReturn");
	List<Map<String, Object>> detaillist = (List<Map<String, Object>>) request
	.getAttribute("detaillist");
	if (detaillist == null) {
		detaillist = new ArrayList<Map<String, Object>>();
	}
	Map<Integer,String> factoryMap = (Map<Integer,String>)request.getAttribute("factoryMap");
	Integer factoryId = (Integer)request.getAttribute("factoryId");
	Map<Integer,String> gongxuMap = (Map<Integer,String>)request.getAttribute("gongxuMap");
	Integer gongxuId = (Integer)request.getAttribute("gongxuId");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>编辑半成品退货单 -- 桐庐富伟针织厂</title>
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

		<link href="css/order/bill.css" rel="stylesheet" type="text/css" />
		<script src="js/order/ordergrid.js" type="text/javascript"></script>
		<script src="js/half_store_return/add.js" type="text/javascript"></script>
		<style type="text/css">
.saveform .form-group {
	width: 250px;
}

.saveform .form-group input,.saveform .form-group select {
	width: 150px;
	padding: 0 12px;
	height: 30px;
}

#out_in_date {
	
}

.table>thead>tr>td {
	padding: 3px 8px;
}

#mainTb {
	margin-top: 0;
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
#storeOrderWidget caption{
	font-size:20px;
	margin-top: 15px;
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
							<a href="order/detail/<%=object.getOrderId()%>">订单详情</a>
						</li>
						<li>
							<a href="workspace/half_workspace">半成品工作台</a>
						</li>
						<li class="active">
							编辑半成品退货单
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid materialorderWidget">
						<div class="row">
							<div class="col-md-12">
								<form class="saveform">
									<input type="hidden" name="id" value="<%=object.getId() %>" />
									<input type="hidden" name="orderId"
										value="<%=object.getOrderId()%>" />
									<%if(factoryId == null || factoryId <= 0){ %>
								<p class="alert alert-danger">请先选择 【加工单位】</p>
								<%}else if(gongxuId == null || gongxuId <= 0){ %>
									<p class="alert alert-danger">请先选择 【工序】</p>		
								<%}%>
									<div class="clear"></div>
									<div class="col-md-12 tablewidget">
										<table class="table">
											<caption id="tablename">
												桐庐富伟针织厂半成品退货单
												<button type="submit"
													class="pull-right btn btn-danger saveTable"
													data-loading-text="正在保存...">
													编辑半成品退货单
												</button>
											</caption>
										</table>
										<table class="tableTb noborder">
											<tbody>
												<tr>
													<td>
														<div class="form-group">
															加工单位：
															<select class="form-control require" name="factoryId"
																id="factoryId">
																<option value="">
																	未选择
																</option>
																<%
																	for (int tempfactoryId : factoryMap.keySet()) {
																%>
																	<%if(factoryId!=null && factoryId == tempfactoryId){ %>
																		<option selected value="<%=factoryId%>"><%=factoryMap.get(tempfactoryId)%></option>
																	<%} else{ %>
																		<option value="<%=tempfactoryId%>"><%=factoryMap.get(tempfactoryId)%></option>
																<%
																	}
																}
																%>
															</select>
														</div>
														<div class="form-group">
															工序：
															<select class="form-control require" name="gongxuId"
																id="gongxuId">
																<option value="">
																	未选择
																</option>
																<%
																	for (int tempgongxuId : gongxuMap.keySet()) {
																%>
																	<%if(gongxuId!=null && gongxuId == tempgongxuId){ %>
																		<option selected value="<%=tempgongxuId%>"><%=gongxuMap.get(tempgongxuId)%></option>
																	<%} else{ %>
																		<option value="<%=tempgongxuId%>"><%=gongxuMap.get(tempgongxuId)%></option>
																<%
																	}
																}
																%>
															</select>
														</div>
														<div class="form-group ">
															退货时间：
															<input type="text" class="form-control require date"
																name="date" id="out_in_date"
																value="<%=DateTool.formatDateYMD(object.getDate())%>">
														</div>

													</td>
													<td>
														<div class="form-group pull-right">
															№：
															<input class="form-control" disabled type="text"
																value="<%=object.getNumber()%>" />
														</div>
													</td>
												</tr>
												<tr>
													<td colspan="2">
														<table class="table table-responsive table-bordered">
															<tbody>
																<tr>
																	<td rowspan="4" width="30%">
																		<a href="/<%=object.getImg()%>" class="thumbnail"
																			target="_blank"> <img id="previewImg"
																				alt="200 x 100%" src="/<%=object.getImg_s()%>">
																		</a>
																	</td>
																</tr>
																<tr>
																	<td>
																		<table class="table table-responsive table-bordered">
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
																				<td class="center"><%=object.getOrderNumber()%>

																				</td>
																				<td class="center"><%=SystemCache
							.getCompanyShortName(object.getCompanyId())%>

																				</td>
																				<td class="center"><%=object.getCompany_productNumber() == null ? ""
					: object.getCompany_productNumber()%>
																				</td>
																				<td class="center"><%=SystemCache.getEmployeeName(object.getCharge_employee())%>
																				</td>
																				<td class="center"><%=object.getName() == null ? "" : object
					.getName()%>
																				</td>
																			</tr>
																			<tr>
																				<td class="center">
																					备注
																				</td>
																				<td colspan="4">
																					<input type="text" name="memo" id="memo"
																						class="form-control" value="<%=object.getMemo()==null?"":object.getMemo() %>">
																				</td>
																			</tr>
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
											<caption>
												<!-- <button type="button"
												class="btn btn-primary addRow pull-left">
												添加一行
											</button> -->
												半成品退货列表
											</caption>
											<thead>
												<tr>
													<th width="15%">
														颜色
													</th>
													<th width="15%">
														机织克重(g)
													</th>
													<th width="15%">
														尺寸
													</th>
													<th width="15%">
														入库总数量
													</th>
													<th width="10%">
														已退货
													</th>
													<th width="10%">
														实际入库
													</th>
													<th width="10%">
														本次退货
													</th>

												</tr>
											</thead>
											<tbody>
												<%
													for (Map<String, Object> item : detaillist) {
												%>
												<tr class="tr" data='<%=SerializeTool.serialize(item)%>'>
													<td><%=item.get("color")%></td>
													<td><%=item.get("weight")%></td>
													<td><%=item.get("size")%></td>
													<td><%=item.get("in_quantity")%></td>
													<td><%=item.get("return_quantity")%></td>
													<td><%=item.get("actual_in_quantity")%></td>
													<td>
														<input class="quantity form-control require double value"
															type="text" value="<%=item.get("quantity")%>"
															placeholder="小于等于<%=(Integer)item.get("actual_in_quantity") + (Integer)item.get("quantity")%>的数量">
													</td>
												</tr>
												<%
													}
												%>
											</tbody>

										</table>
										
										<div id="tip" class="auto_bottom">
											<div>
												说明：1.此单说明了本次退货的相关内容，请充分阅读并理解，如有疑问及时联系我方
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

										<p class="pull-right auto_bottom">
											<span id="created_user">制单人：<%=SystemCache.getUserName(object.getCreated_user())%></span>
											<span id="receiver_user">收货人：</span>
											<span id="date"> 日期：<%=DateTool.formatDateYMD(DateTool.now())%></span>
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