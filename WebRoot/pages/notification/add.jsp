<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.ProductionNotification"%>
<%@page import="com.fuwei.entity.ProductionNotificationDetail"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	ProductionNotification productionNotification = (ProductionNotification) request.getAttribute("productionNotification");
	List<ProductionNotificationDetail> detaillist = productionNotification.getDetaillist();
	if (detaillist == null) {
		detaillist = new ArrayList<ProductionNotificationDetail>();
	}
	Map<String,Object> orderDetail = (Map<String,Object>)request.getAttribute("orderDetail");
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
		<script src="js/order/add.js" type="text/javascript"></script>
		<link href="css/order/index.css" rel="stylesheet" type="text/css" />
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
								<form class="form-horizontal orderform" role="form">
									<div class="form-group col-md-6">
										<div class="col-sm-offset-3 col-sm-5">
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
									<div class="form-group col-md-6">
										<label for="orderNumber" class="col-sm-3 control-label">
											生产单号
										</label>
										<div class="col-sm-8">
											<input type="text" class="form-control" name="orderNumber"
												id="orderNumber" readonly value="自动生成">
										</div>
										<div class="col-sm-1"></div>
									</div>
									<div class="form-group col-md-6">
										<label for="companyId" class="col-sm-3 control-label">
											公司
										</label>
										<div class="col-sm-8">
											
										</div>
									</div>
									<div class="form-group col-md-6">
										<label for="salesmanId" class="col-sm-3 control-label">
											业务员
										</label>
										<div class="col-sm-8">
											
										</div>
									</div>
									
									<div class="form-group col-md-6">
										<label for="start_at" class="col-sm-3 control-label">
											订单签订时间
										</label>
										<div class="col-sm-8">
											<input type="text" name="start_at" id="start_at"
												class="date form-control require" placeholder="" />

										</div>
										<div class="col-sm-1"></div>
									</div>
									
									<div class="form-group col-md-6">
										<label for="end_at" class="col-sm-3 control-label">
											订单截止日期
										</label>
										<div class="col-sm-8">
											<input disabled type="text" name="end_at" id="end_at"
												class="date form-control require" value="<%=DateTool.formatDateYMD((Date)orderDetail.get("end_at")) %>"/>

										</div>
										<div class="col-sm-1"></div>
									</div>

									<div class="form-group col-md-6">
										<label for="created_user_name" class="col-sm-3 control-label">
											创建人
										</label>
										<div class="col-sm-8">
											<input type="text" name="created_user_name"
												id="created_user_name" class="form-control require" readonly
												value="<%=SystemCache.getUserName(productionNotification.getCreated_user())%>" />

										</div>
										<div class="col-sm-1"></div>
									</div>
									<div class="form-group col-md-6">
										<label for="memo" class="col-sm-3 control-label">
											备注
										</label>
										<div class="col-sm-8">
											<input type="text" name="memo"
												id="memo" class="form-control"
												value="" />

										</div>
										<div class="col-sm-1"></div>
									</div>
									<div class="clear"></div>
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
												材料名称
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
											<td><%=detail.getQuantity()%></td>
											<td><%=detail.getMaterial()%></td>
											<td><%=detail.getMaterial_quantity()%></td>
											<td><%=detail.getWaste()%></td>
											<td><%=detail.getTotal_material()%></td>
											<td class="memo"><input class="form-control memo_value" value="<%=detail.getMemo()==null?"":detail.getMemo() %>" /></td>
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
	</body>
</html>