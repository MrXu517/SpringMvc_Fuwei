<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.entity.Quote"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	List<Quote> quotelist = (List<Quote>) request
			.getAttribute("quotelist");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>样品管理 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8" />
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">

		<script src="js/plugins/jquery-1.10.2.min.js"></script>
		<script src="<%=basePath%>js/plugins/WdatePicker.js"></script>
		<script src="js/common/common.js" type="text/javascript"></script>
		<script src="js/quote/index.js" type="text/javascript"></script>
	</head>
	<body>
		<%@ include file="../common/head.jsp"%>
		<div id="Content">
			<div class="breadcrumbs" id="breadcrumbs">
				<ul class="breadcrumb">
					<li>
						<i class="icon-home home-icon"></i>
						<a href="index.jsp">首页</a>
					</li>
					<li class="active">
						报价列表
					</li>
				</ul>
			</div>
			<div class="body">

				<div class="container-fluid">
					<div class="row">
						<div class="col-md-12 tablewidget">
							<div>
								<button type="button" class="btn btn-danger">
									确定报价
								</button>
							</div>
							<table class="table table-responsive">
								<thead>
									<tr>
										<th>

										</th>
										<th>
											序号
										</th>

										<th>
											图片
										</th>
										<th>
											公司
										</th>
										<th>
											业务员
										</th>
										<th>
											工厂款号
										</th>
										<th>
											价格
										</th>
										<th>
											克重
										</th>
										<th>
											创建时间
										</th>
										<th>
											操作
										</th>
									</tr>
								</thead>
								<tbody>
									<%
										int i = 0;
										for (Quote quote : quotelist) {
									%>
									<tr quoteId="<%=quote.getId()%>">
										<td>
											<input type="checkbox" />
										</td>
										<td><%=++i%></td>
										<td
											style="max-width: 120px; height: 120px; max-height: 120px;">
											<a target="_blank" class="cellimg"
												href="<%=quote.getSample().getImg()%>"><img
													style="max-width: 120px; height: 120px; max-height: 120px;"
													src="<%=quote.getSample().getImg()%>"> </a>
										</td>
										<td><%=SystemCache.getCompanyName(SystemCache.getSalesman(
								quote.getQuotePrice().getSalesmanId())
								.getCompanyId())%></td>
										<td><%=SystemCache.getSalesmanName(quote.getQuotePrice()
								.getSalesmanId())%></td>
										<td><%=quote.getSample().getProductNumber()%></td>
										<td>
											<span class="RMB">￥</span><%=quote.getQuotePrice().getPrice()%></td>
										<td><%=quote.getSample().getWeight()%>克
										</td>
										<td><%=quote.getCreated_at()%></td>
										<td>
											<a href="sample/detail/<%=quote.getSample().getId()%>">详情</a>
											|
											<a href="#" class="delete" data-cid="<%=quote.getId()%>">删除</a>
										</td>
									</tr>
									<%
										}
									%>
								</tbody>
							</table>
						</div>
					</div>
				</div>

			</div>
		</div>

	</body>
</html>