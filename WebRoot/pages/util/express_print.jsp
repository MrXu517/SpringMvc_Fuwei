<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String destination = (String)session.getAttribute("destination");
	String company_name = (String)session.getAttribute("company_name");
	String province = (String)session.getAttribute("province");
	String city = (String)session.getAttribute("city");
	String district = (String)session.getAttribute("district");
	String address = (String)session.getAttribute("address");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>打印快递单 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8">
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<script src="js/plugins/jquery-1.10.2.min.js"></script>
		<script src="js/plugins/jquery-barcode.min.js" type="text/javascript"></script>
		<!-- 为了让IE浏览器运行最新的渲染模式 -->
		<link href="css/printorder/print.css" rel="stylesheet" type="text/css" />
		<style type="text/css">
body {
	font-family: Arial;
}
tr {
	height: 80px;
}
table td {
	border-width: 2px;
	font-size: 25px;
	text-align: center;
	font-weight: bold;
}
.underline {
	text-decoration: underline;
}
.gridTab {
  width: 19cm;
  page-break-after: always;
  margin-bottom: 20px;
  height: auto;
}
</style>
	</head>
	<body>
			<div class="container-fluid gridTab auto_container">
				<div class="row">
					<div class="col-md-12 tablewidget">
						<table class="tableTb">
							<tbody>
								
							</tbody>
						</table>
					</div>

				</div>
			</div>
	

		<script type="text/javascript">
		window.print();
		</script>
	</body>
</html>