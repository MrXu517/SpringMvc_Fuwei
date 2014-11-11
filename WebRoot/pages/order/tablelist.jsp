<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.HeadBankOrder"%>
<%@page import="com.fuwei.entity.HeadBankOrderDetail"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Order order = (Order) request.getAttribute("order");
	HeadBankOrder headBankOrder = (HeadBankOrder) request
			.getAttribute("headBankOrder");

	String tabname = (String) request.getParameter("tab");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>系统信息管理 -- 桐庐富伟针织厂</title>
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


		<link href="css/order/tablelist.css" rel="stylesheet" type="text/css" />
		<script src="js/order/tablelist.js" type="text/javascript"></script>

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
						<li class="active">
							系统信息管理
						</li>
					</ul>
				</div>
				<div class="body">
					<div id="tab">
						<ul class="nav nav-tabs" role="tablist">
							<li class="active">
								<a href="#headbank" role="tab" data-toggle="tab">头带质量记录单</a>
							</li>

							<li>
								<a href="#headproduction" role="tab" data-toggle="tab">头带生产单</a>
							</li>

							<li>

								<a href="#purchaseorder" role="tab" data-toggle="tab">原材料采购单</a>
							</li>
							<li>
								<a href="#coloring" role="tab" data-toggle="tab">染色单</a>
							</li>
							<li>
								<a href="#stores" role="tab" data-toggle="tab">原材料仓库</a>
							</li>
						</ul>


						<div class="tab-content">
							<div class="tab-pane active" id="headbank">
								<div class="container-fluid">
									<div class="row">
										<div class="col-md-12 tablewidget">
											<table class="table">
												<caption>
													桐庐富伟针织厂质量记录单
												</caption>
											</table>
											<thead>
												<tr>
													<td>
														<div class="sampleImg">

															<a href="#" class="thumbnail"> <img id="previewImg"
																	alt="400 x 100%" src="/<%=order.getImg_s()%>"> </a>

														</div>
														<div class="sampleData">
															<table class="table table-responsive">
																<tbody>

																	<tr>
																		<td>
																			生产单位
																		</td>
																		<td class="orderproperty"><%=order.getFactoryId() == null ? "" : SystemCache
					.getFactoryName(order.getFactoryId())%></td>
																	</tr>
																	<tr>
																		<td colspan="2">
																			订单信息
																		</td>
																	</tr>
																	<tr>
																		<td>
																			公司
																		</td>
																		<td><%=SystemCache.getCompanyName(order.getCompanyId())%></td>
																	</tr>
																	<tr>
																		<td>
																			客户
																		</td>
																		<td><%=order.getKehu()%></td>
																	</tr>
																	<tr>
																		<td>
																			货号
																		</td>
																		<td><%=order.getProductNumber()%></td>
																	</tr>
																	<tr>
																		<td>
																			款名
																		</td>
																		<td><%=order.getName()%></td>
																	</tr>
																	<tr>
																		<td>
																			跟单
																		</td>
																		<td><%=SystemCache.getUserName(order.getCharge_user())%></td>
																	</tr>
																</tbody>
															</table>

														</div>


													</td>
												</tr>
												<tr>
													<td>
														<table class="table table-responsive detailTb">
															<thead>
																<tr>
																	<th width="15%">
																		颜色
																	</th>
																	<th width="15%">
																		克重(g)
																	</th>
																	<th width="15%">
																		纱线种类
																	</th>
																	<th width="15%">
																		尺寸
																	</th>
																	<th width="15%">
																		生产数量
																	</th>
																	<th width="15%">
																		价格(/个)
																	</th>
																	<th width="15%">
																		操作
																	</th>
																</tr>
															</thead>
															<tbody>

																<tr>
																	<td>
																		<input type="text" name="color" class="color form-control" />
																	</td>
																	<td>
																		<input type="text" name="weight" class="weight double form-control" />
																	</td>
																	<td>
																		<input type="text" name="yarn" class="yarn form-control" />
																	</td>
																	<td>
																		<input type="text" name="size" class="size form-control" />
																	</td>
																	<td>
																		<input type="text" name="quantity"
																			class="quantity int form-control" />
																	</td>
																	<td>
																		<input type="text" name="price" class="price double form-control" />
																	</td>
																	<td>
																		<a class="deletecompany" href="#">删除</a>
																	</td>
																</tr>

															</tbody>
														</table>
													</td>
												</tr>
											</thead>
										</div>


									</div>
								</div>

							</div>


						</div>
					</div>
				</div>
			</div>
	</body>
</html>
<script type="text/javascript">
	var tabname = "<%=tabname%>
	";
	if (tabname == null || tabname == undefined) {
		$('#tab a:first').tab('show') // Select first tab
	}
	$("#tab a[href='#" + tabname + "']").tab('show') // Select tab by name
</script>