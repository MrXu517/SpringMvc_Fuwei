<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.QuoteOrder"%>
<%@page import="com.fuwei.entity.QuoteOrderDetail"%>
<%@page import="com.fuwei.util.DateTool"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	QuoteOrder quoteOrder = (QuoteOrder) request.getAttribute("quoteorder");
	
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>报价单详情 -- 桐庐富伟针织厂</title>
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
							<a href="quoteorder/index">报价单列表</a>
						</li>
						<li class="active">
							报价详情
						</li>
					</ul>
				</div>
				<div class="body">

					<div class="container-fluid">
						<p>
							<br>
							<br>
							<span class="sss">
								<h2 align="center">
									桐庐富伟针织厂报价单
									</h3>
									<div align="right">
										<a href="/<%=quoteOrder.getExcelUrl()%>"
											style="margin-right: 200px;">下载对应excel表格</a>
									</div>
							</span>
						</p>

						<table width="797" height="80" cellpadding="1" cellspacing="1"
							align="center">
							<tr>
								<td width="522" valign="top" class="biaodan">
									<font size="4"> №:<%=quoteOrder.getQuotationNumber()%><br>
										TO:<%=SystemCache.getCompanyName(quoteOrder.getCompanyId())%>
										&nbsp;&nbsp;<%=SystemCache.getSalesmanName(quoteOrder.getSalesmanId())%><br>
										时间:<%=DateTool.formateDate(quoteOrder.getCreated_at(),"yyyy年MM月dd日  HH:mm:ss")%></font>
								</td>
							</tr>
						</table>

						<table width="797" cellpadding="1" cellspacing="1" align="center"
							border="1" bordercolor="#000000"
							style="border-collapse: collapse;">
							<thead>
								<tr>
									<th style="width: 80px;">
										序号
									</th>
									<th style="width: 120px;">
										图片
									</th>
									<th style="width: 125px;">
										款号
									</th>
									<th style="width: 125px;">
										材料
									</th>
									<th style="width: 125px;">
										尺寸
									</th>
									<th style="width: 90px;">
										重量
									</th>
									<th style="width: 90px;">
										价格
									</th>
								</tr>
							</thead>


							<%
				List<QuoteOrderDetail> detaillist = quoteOrder.getDetaillist();
				for (int i = 0; i < detaillist.size(); i++) {
					QuoteOrderDetail detail = detaillist.get(i);
			%>
							<tr>
								<td align="center"><%=i + 1%></td>
								<td align="center">
									<img src="/<%=detail.getImg_ss()%>">
								</td>
								<td align="center">
									<%=detail.getName()%>
								</td>
								<td align="center">
									<%=detail.getMaterial()%>
								</td>
								<td align="center">
									<%=detail.getSize()%>
								</td>
								<td align="center">
									<%=detail.getWeight()%>
								</td>
								<td align="center">
									<%=detail.getPrice()%>
								</td>
							</tr>
							<%
				}
			%>

						</table>
					</div>

				</div>


			</div>
		</div>
	</body>
</html>
<script type="text/javascript">
	$(document).ready( function() {
		/*设置当前选中的页*/
		var $a = $("#left li a[href='quoteorder/index']");
		setActiveLeft($a.parent("li"));
		/*设置当前选中的页*/
	});
</script>