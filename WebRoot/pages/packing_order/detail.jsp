<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.OrderDetail"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.entity.Material"%>
<%@page import="com.fuwei.entity.ordergrid.PackingOrder"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	PackingOrder packingOrder = (PackingOrder) request
			.getAttribute("packingOrder");
	Order order = (Order) request.getAttribute("order");
	List<OrderDetail> DetailList = order == null
			|| order.getDetaillist() == null ? new ArrayList<OrderDetail>()
			: order.getDetaillist();
	Boolean has_packing_order_delete = SystemCache.hasAuthority(session,"packing_order/delete");
	
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>装箱单详情 -- 桐庐富伟针织厂</title>
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
		<script src="js/plugins/jquery.form.js" type="text/javascript"></script>
		<link href="css/packing_order/index.css" rel="stylesheet"
			type="text/css" />
		<script src="js/packing_order/add.js" type="text/javascript"></script>
		
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
							<a href="order/detail/<%=order.getId()%>">订单详情</a>
						</li>
						<li>
							<a href="packing_order/list/<%=order.getId()%>">装箱单</a>
						</li>
						<li class="active">
							装箱单详情
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid">

						<div class="row">
							<%if(has_packing_order_delete){ %>
							<button data-cid="<%=packingOrder.getId() %>" class="btn btn-danger delete">删除装箱单</button>
							<%} %>
							<div class="col-md-4 left">
							
									<h4>上传时间:<%=packingOrder.getCreated_at() %> </h4> 
									<h4>上传用户:<%=SystemCache.getUserName(packingOrder.getCreated_user())%></h4>   
									<h4 class="memoSpan">备注: <%=packingOrder.getMemo() %></h4>
								
							</div>
							<div class="col-md-7 right">
								<table class="table">
									<tbody>
										<tr>
											<td style="border: none;">
												<table class="table table-responsive table-bordered tableTb">
													<tbody>
														<tr>
															<td rowspan="<%=7+DetailList.size() %>" width="50%">
																<a href="/<%=order.getImg()%>" class="thumbnail"
																	target="_blank"> <img id="previewImg"
																		alt="200 x 100%" src="/<%=order.getImg_s()%>"> </a>
															</td>

														</tr>
														<tr>
															<td>
																订单号
															</td>
															<td><%=order.getOrderNumber()%></td>
														</tr>
														<tr>
															<td>
																公司
															</td>
															<td><%=SystemCache.getCompanyName(order.getCompanyId())%></td>
														</tr>
														<tr>
															<td>
																公司货号
															</td>
															<td><%=order.getCompany_productNumber()%></td>
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
															<td><%=SystemCache.getEmployeeName(order.getCharge_employee())%></td>
														</tr>
														<tr class="important">
															<td colspan="2" class="center">
																颜色及订单数量
															</td>
														</tr>
														<%
															for (OrderDetail detail : DetailList) {
														%>
														<tr class="important">
															<td><%=detail.getColor()%></td>
															<td><%=detail.getQuantity()%></td>
														</tr>
														<%
															}
														%>
													</tbody>
												</table>

											</td>
										</tr>
									</tbody>
								</table>

							</div>
							</div><div class="row">
							<div class="col-md-12">
								<p>
									装箱文件：
								</p>
								<iframe name="pdfContent" id="pdfContent" 
									src="/<%=packingOrder.getPdfpath()%>"></iframe>
							</div>

						</div>

					</div>


				</div>
			</div>


		</div>


		</div>
	<script type="text/javascript">
	//删除单据 -- 开始
	$(".delete").click( function() {
		var id = $(this).attr("data-cid");
		if (!confirm("确定要删除该装箱单吗？")) {
			return false;
		}
		$.ajax( {
			url :"packing_order/delete/" + id,
			type :'POST'
		}).done( function(result) {
			if (result.success) {
				Common.Tip("删除装箱单成功", function() {
					$("#breadcrumbs li.active").prev().find("a").click();
				});
			}
		}).fail( function(result) {
			Common.Error("删除装箱单失败：" + result.responseText);
		}).always( function() {

		});
		return false;
	});
	//删除单据  -- 结束
	</script>
	</body>
</html>