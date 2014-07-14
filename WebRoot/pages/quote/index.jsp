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
	HashMap<Integer, List<Quote>> quoteMap = (HashMap<Integer, List<Quote>>) request
			.getAttribute("quoteMap");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>报价列表 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8" />
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">

		<script src="js/plugins/jquery-1.10.2.min.js"></script>
		<script src="<%=basePath%>js/plugins/WdatePicker.js"></script>
		<script src="js/common/common.js" type="text/javascript"></script>
		<script src="js/quote/index.js" type="text/javascript"></script>
		<link href="css/quote/index.css" rel="stylesheet" type="text/css" />
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
										<th width="3%" nowrap="nowrap">

										</th>
										<th width="5%" nowrap="nowrap">
											序号
										</th>

										<th width="10%" nowrap="nowrap">
											图片
										</th>
										<th width="5%" nowrap="nowrap">
											公司
										</th>
										<th width="5%" nowrap="nowrap">
											业务员
										</th>
										<th width="5%" nowrap="nowrap">
											工厂款号
										</th>
										<th width="5%" nowrap="nowrap">
											价格
										</th>
										<th width="5%" nowrap="nowrap">
											克重
										</th>
										<th width="10%" nowrap="nowrap">
											创建时间
										</th>
										<th width="15%" nowrap="nowrap">
											操作
										</th>
									</tr>
								</thead>
								<tbody>
									<%
										Set<Integer> salesmans = quoteMap.keySet();
										for (Integer salesmanId : salesmans) {
											List<Quote> quotelist = quoteMap.get(salesmanId);
									%>
<tr class="group-head"><td colspan="10">
															<input type="checkbox" />
															<span class="company"><%=SystemCache.getCompanyName(SystemCache.getSalesman(salesmanId).getCompanyId())  %></span>
<span class="salesman"><%=SystemCache.getSalesmanName(salesmanId) %></span>
														</td></tr>
													<%
														int i = 0;
															for (Quote quote : quotelist) {
													%>
													<tr class="group-body" quoteId="<%=quote.getId()%>">
														<td width="3%" nowrap="nowrap">
															<input type="checkbox" />
														</td>
														<td width="5%" nowrap="nowrap"><%=++i%></td>
														<td width="10%" nowrap="nowrap"
															style="max-width: 120px; height: 120px; max-height: 120px;">
															<a target="_blank" class="cellimg"
																href="<%=quote.getSample().getImg()%>"><img
																	style="max-width: 120px; height: 120px; max-height: 120px;"
																	src="<%=quote.getSample().getImg()%>"> </a>
														</td>
														<td width="5%" nowrap="nowrap"><%=SystemCache.getCompanyName(SystemCache
									.getSalesman(
											quote.getQuotePrice()
													.getSalesmanId())
									.getCompanyId())%></td>
														<td width="5%" nowrap="nowrap"><%=SystemCache.getSalesmanName(quote.getQuotePrice()
									.getSalesmanId())%></td>
														<td width="5%" nowrap="nowrap"><%=quote.getSample().getProductNumber()%></td>
														<td width="5%" nowrap="nowrap">
															<span class="RMB">￥</span><strong><%=quote.getQuotePrice().getPrice()%></strong></td>
														<td width="5%" nowrap="nowrap"><%=quote.getSample().getWeight()%>克
														</td>
														<td width="10%" nowrap="nowrap"><%=quote.getCreated_at()%></td>
														<td width="15%" nowrap="nowrap">
															<a href="sample/detail/<%=quote.getSample().getId()%>">详情</a>
															|
															<a href="#" class="delete" data-cid="<%=quote.getId()%>">删除</a>
														</td>
													</tr>
													<%
														}
													%>
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