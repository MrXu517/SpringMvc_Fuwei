<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.ProductionNotification"%>
<%@page import="com.fuwei.entity.ProductionNotificationDetail"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	ProductionNotification productionNotification = (ProductionNotification) request
			.getAttribute("productionNotification");
	List<ProductionNotificationDetail> detaillist = productionNotification
			.getDetaillist();
	if (detaillist == null) {
		detaillist = new ArrayList<ProductionNotificationDetail>();
	}
	Map<String, Object> orderDetail = (Map<String, Object>) request
			.getAttribute("orderDetail");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>创建生产单 -- 桐庐富伟针织厂</title>
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
		<script src="js/plugins/jquery.form.js" type="text/javascript"></script>
		<script src="<%=basePath%>js/plugins/WdatePicker.js"></script>
		<script src="js/notification/add.js" type="text/javascript"></script>
		<link href="css/notification/index.css" rel="stylesheet" type="text/css" />
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
							<i class=""></i>
							<a href="order/index">生产单列表</a>
						</li>
						<li class="active">
							创建生产单
						</li>
					</ul>
				</div>
				<div class="body">

					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 formwidget">
								<form class="form-horizontal notificationform" role="form">
									<input type="hidden" name="orderDetailId" id="orderDetailId" value="<%=productionNotification.getOrderDetailId() %>"/>
									<div class="form-group col-md-6">
										<div class="col-sm-offset-2 col-sm-5">
											<button type="submit" class="btn btn-primary"
												data-loading-text="正在保存...">
												创建生产单
											</button>

										</div>
										<div class="col-sm-3">
											<button type="reset" class="reset btn btn-default">
												重置表单
											</button>
										</div>
										<div class="col-sm-1"></div>
									</div>
									<div class="clear"></div>
									<div class="samplehead">
										<div class="pull-right">
											<label class="control-label">
												所属订单：
											</label>
											<span><%=orderDetail.get("orderNumber")%></span>
										</div>
										<div class="pull-right">
											<label class="control-label">
												样品名称：
											</label>
											<span><%=orderDetail.get("name")%></span>
										</div>
										<div class="clear"></div>
									</div>
																		<div class="formbody">
										<div class="form-group">
											<label for="notificationNumber"
												class="col-sm-3 control-label">
												生产单号
											</label>
											<div class="col-sm-8">
												<input type="text" class="form-control"
													name="notificationNumber" id="notificationNumber" readonly
													value="自动生成">
											</div>
											<div class="col-sm-1"></div>
										</div>
										<div class="form-group">
											<label for="factoryId" class="col-sm-3 control-label">
												加工工厂
											</label>
											<div class="col-sm-8">
												<select name="factoryId" id="factoryId"
													class="form-control require">
													<%
														for (Factory factory : SystemCache.factorylist) {
													%>
													<option value="<%=factory.getId()%>"><%=factory.getName()%></option>
													<%
														}
													%>
												</select>
											</div>
											<div class="col-sm-1"></div>
										</div>
										<div class="form-group">
											<label for="quantity" class="col-sm-3 control-label">
												生产数量
											</label>
											<div class="col-sm-8">
												<input readonly type="text" name="quantity" id="quantity"
													class="form-control require" />
											</div>
											<div class="col-sm-1"></div>
										</div>


										<div class="form-group">
											<label for="end_at" class="col-sm-3 control-label">
												交货日期
											</label>
											<div class="col-sm-8">
												<input type="text" name="end_at" id="end_at"
													class="date form-control require"
													value="<%=DateTool.formatDateYMD((Date) orderDetail.get("end_at"))%>" />
											</div>
											<div class="col-sm-1"></div>
										</div>
										<div class="clear"></div>
									</div>
									

									<div class="sampleData">
										<table class="table table-responsive">
											<tbody>

												<tr>
													<td>
														打样人
													</td>
													<td><%=SystemCache.getUserName((Integer) orderDetail
							.get("charge_user"))%></td>
												</tr>
												<tr>
													<td>
														材料
													</td>
													<td><%=orderDetail.get("material")%></td>
												</tr>
												<tr>
													<td>
														克重
													</td>
													<td><%=orderDetail.get("weight")%>克
													</td>
												</tr>
												<tr>
													<td>
														尺寸
													</td>
													<td><%=orderDetail.get("size")%></td>
												</tr>
												<tr>
													<td>
														机织
													</td>
													<td><%=orderDetail.get("machine")%></td>
												</tr>
												<tr>
													<td>
														订单截止日期
													</td>
													<td><%=DateTool.formatDateYMD((Date) orderDetail.get("end_at"))%></td>
												</tr>
											</tbody>
										</table>

									</div>
									<div class="sampleImg">

										<a href="#" class="thumbnail"> <img id="previewImg"
												alt="400 x 100%" src="/<%=orderDetail.get("img_s")%>">
										</a>

									</div>
									<div class="clear"></div>
									<div>
										<button type="button" class="btn btn-primary addRow"
											data-dismiss="modal">
											添加行
										</button>
									</div>
									<table class="table table-responsive" id="detailTable">
										<thead>
											<tr>
												<th>
													色号
												</th>
												<th>
													色别
												</th>
												<th>
													尺寸
												</th>
												<th>
													生产数量
												</th>
												<th>
													材料
												</th>
												<th>
													材料数量
												</th>
												<th>
													损耗
												</th>
												<th>
													总材料
												</th>
												<th>
													备注
												</th>
											</tr>
										</thead>
										<tbody>
											<%
												for (ProductionNotificationDetail detail : detaillist) {
											%>
											<tr data_detail='<%=SerializeTool.serialize(detail)%>'>
												<td><%=detail.getColorCode()%></td>
												<td><%=detail.getColorStyle()%></td>
												<td><%=detail.getSize()%></td>
												<td class="quantity"><%=detail.getQuantity()%></td>
												<td><%=detail.getMaterial()%></td>
												<td><%=detail.getMaterial_quantity()%></td>
												<td><%=detail.getWaste()%></td>
												<td><%=detail.getTotal_material()%></td>
												<td><%=detail.getMemo() == null ? "" : detail.getMemo()%></td>
											</tr>
											<%
												}
											%>
										</tbody>
									</table>
								</form>

							</div>


						</div>
					</div>

				</div>
			</div>
		</div>

		<div class="modal fade" id="addModal">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
						</button>
						<h4 class="modal-title">
							添加一行生产详情
						</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal detailform" role="form">
							<div class="row">
								<div class="form-group">
									<label for="colorCode" class="col-sm-3 control-label">色号
									</label>
									<div class="col-sm-8">
										<input type="text" name="colorCode" id="colorCode" class="form-control require" />
									</div>
								</div>
								<div class="form-group">
									<label for="colorStyle" class="col-sm-3 control-label">色别
									</label>
									<div class="col-sm-8">
										<input type="text" name="colorStyle" id="colorStyle" class="form-control require"/>
									</div>
								</div>
								<div class="form-group">
									<label for="size" class="col-sm-3 control-label">尺寸
									</label>
									<div class="col-sm-8">
										<input type="text" name="size" id="size" class="form-control require"/>
									</div>
								</div>
								<div class="form-group">
									<label for="quantity" class="col-sm-3 control-label">生产数量
									</label>
									<div class="col-sm-8">
										<input type="text" name="quantity" id="quantity" class="form-control require positive_int" />
									</div>
								</div>
								<div class="form-group">
									<label for="material" class="col-sm-3 control-label">材料
									</label>
									<div class="col-sm-8">
										<input type="text" name="material" id="material" class="form-control require"/>
									</div>
								</div>
								<div class="form-group">
									<label for="material_quantity" class="col-sm-3 control-label">材料数量
									</label>
									<div class="col-sm-8">
										<input type="text" name="material_quantity" id="material_quantity" class="form-control require positive_int"/>
									</div>
								</div>
								<div class="form-group">
									<label for="waste" class="col-sm-3 control-label">损耗
									</label>
									<div class="col-sm-8">
										<input type="text" name="waste" id="waste" class="form-control require double"/>
									</div>
								</div>
								<div class="form-group">
									<label for="total_material" class="col-sm-3 control-label">总材料
									</label>
									<div class="col-sm-8">
										<input type="text" name="total_material" id="total_material" class="form-control require"/>
									</div>
								</div>
								<div class="form-group">
									<label for="memo" class="col-sm-3 control-label">备注
									</label>
									<div class="col-sm-8">
										<input type="text" name="memo" id="memo" class="form-control"/>
									</div>
								</div>
							</div>
							<div class="modal-footer">
								<button type="submit" class="btn btn-primary"
									data-loading-text="正在保存...">
									确定
								</button>
								<button type="reset" class="btn btn-default"
									data-dismiss="modal">
									取消
								</button>
							</div>
						</form>
					</div>

				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
		</div>
		<!-- /.modal -->
	</body>
</html>