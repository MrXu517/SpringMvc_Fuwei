<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String ASOS_ORDER_NUMBER = (String) session
			.getAttribute("ASOS_ORDER_NUMBER");
	String ASOS_MDA_NUMBER = (String) session
			.getAttribute("ASOS_MDA_NUMBER");
	ASOS_MDA_NUMBER = ASOS_MDA_NUMBER.trim();
	String ASOS_Style_Number = (String) session
			.getAttribute("ASOS_Style_Number");
	String UNITS_PER_CARTON = (String) session
			.getAttribute("UNITS_PER_CARTON");
	String CARTON_NUMBER = (String) session
			.getAttribute("CARTON_NUMBER");
	String ASOS_SKU_Number = (String) session
			.getAttribute("ASOS_SKU_Number");
	int total_number = (Integer) session.getAttribute("total_number");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>打印生产单 -- 桐庐富伟针织厂</title>
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

.ASOS_MDA_NUMBER_BARCODE {
	width: 350px !important;
	  margin: auto;
}
.ASOS_MDA_NUMBER_BARCODE object{
	width:350px;
}

.to {
	margin-left: -100px;
	vertical-align: top;
	font-size: 30px;
}
.br {
	margin-left: 100px;
	text-align: left;
	font-style: italic;
	display: inline-block;
	font-size: 30px;
}
.asos_mad_number_td{
	font-size:15px;
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

		<%
			for (int i = 1; i <= total_number; ++i) {
		%>
	
			<div class="container-fluid gridTab auto_container">
				<div class="row">
					<div class="col-md-12 tablewidget">
						<table id="orderTb" class="tableTb">
							<tbody>
								<tr>
									<td colspan="2">
										<span class="to">To</span>
										<span class="br">ASOS.com Ltd <br> Park spring
											Road <br> Grimethorpe <br> Barnsley <br> S72
											7GX <br>
										</span>

									</td>
								</tr>
								<tr>
									<td width="50%">
										FROM
									</td>
									<td>
										SINOSKY
									</td>
								</tr>
								<tr>
									<td>
										ASOS ORDER NUMBER
										<br>
										(PO)
									</td>
									<td class="ASOS_ORDER_NUMBER"><%=ASOS_ORDER_NUMBER%></td>
								</tr>
								<tr>
									<td>
										ASOS MDA NUMBER
									</td>
									<td class="ASOS_MDA_NUMBER"><%=ASOS_MDA_NUMBER%></td>
								</tr>
								<tr>
									<td>
										ASOS MDA NUMBER
										<br>
										<span class="underline">BARCODE</span>
									</td>
									<td class="asos_mad_number_td">
										<div class="ASOS_MDA_NUMBER_BARCODE"></div>
										<%=ASOS_MDA_NUMBER %>
									</td>
								</tr>
								<tr>
									<td>
										ASOS Style Number
										<br>
										(ASOS Parent SKU)
									</td>
									<td class="ASOS_Style_Number"><%=ASOS_Style_Number%></td>
								</tr>
								<tr>
									<td>
										UNITS PER CARTON
									</td>
									<td class="UNITS_PER_CARTON "><%=UNITS_PER_CARTON%></td>
								</tr>
								<tr>
									<td>
										CARTON NUMBER
									</td>
									<td class="CARTON_NUMBER "><%=i%>
										of
										<%=total_number%></td>
								</tr>


								<tr>
									<td><br>
										ASOS SKU Number
										<br>
										(ASOS Child SKU)<br><br>
									</td>
									<td class="ASOS_SKU_Number"><%=ASOS_SKU_Number%></td>
								</tr>
								<tr>
									<td><br>
										ASOS SKU Number
										<br>
										<span class="underline">BARCODE</span>
										<br><br>
									</td>
									<td class="ASOS_SKU_Number_BARCODE"></td>
								</tr>
							</tbody>
						</table>
					</div>

				</div>
			</div>
	
		<%
			}
		%>

		<script type="text/javascript">
			$(".ASOS_MDA_NUMBER_BARCODE").empty().barcode("<%=ASOS_MDA_NUMBER%>", "code39",{barWidth:2, barHeight:70,fontSize:10,output:"bmp",showHRI:true});
		//	$(".ASOS_SKU_Number_BARCODE").empty().barcode("<%=ASOS_SKU_Number%>
	//", "ean8",{barWidth:2, barHeight:35,showHRI:true});
		window.print();
</script>
	</body>
</html>