<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.PantongColor"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	
	PantongColor pantongColor = (PantongColor) request.getAttribute("pantongColor");
	String message = (String)request.getAttribute("message");
	String panTongName = "";
	if(pantongColor != null){
		panTongName = pantongColor.getPanTongName();
	}
%>
<!DOCTYPE html>

<html>
	<head>
		<base href="<%=basePath%>">
		<title>搜索潘通色号 -- 桐庐富伟针织厂</title>
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
		<style type="text/css">
		#searchTb{width:600px;}
		#searchTb,#searchTb td,#searchTb th{border-color:#000;}
		#searchTb td{width:120px;text-align: center;line-height: 15px;}
		#panTongName{border-color:#000;}
		form{width:600px;margin-bottom: 20px;margin-top: 20px;}
		.center{  text-align: center;}
		.red{color:red;}
		.body .container-fluid{width: 800px;margin: auto;}
		td.selected{background: #FAF70A;font-size:16px;font-weight: bold;}
		#searchTb thead,#searchTb tfoot{font-size:16px;font-weight: bold;}
		</style>
	</head>
	<body>
		<%@ include file="../../common/head.jsp"%>
		<div id="Content">
			<div id="main">
				<div class="body">

					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 tablewidget">
								<!-- Table -->
								<div clas="navbar navbar-default">
									<form class="form-horizontal searchform form-inline"
										role="form" action="pantongcolor/search" method="post">
										<div class="form-group timegroup">
											<label class="col-sm-2 control-label">
												潘通色号
											</label>

											<div class="input-group col-md-10">
												<input type="text" name="panTongName" id="panTongName"
													class="form-control" value="<%=panTongName%>" />
												<span class="input-group-btn">
													<button class="btn btn-primary" type="submit">
														搜索
													</button> </span>
											</div>
										</div>
									</form>

								</div>
								<table id="searchTb" class="table table-responsive table-bordered">
									<%if(pantongColor!=null){ %>
										<thead><tr class="center"><td colspan="5">潘通色号<%=pantongColor.getPanTongName() %> 所在位置： 第<%=pantongColor.getSheetNum() %>页， 第<%=pantongColor.getRowNum() %>行，第<%=pantongColor.getColumnNum() %>列   </td></tr></thead>
									<%} %>
									<%if(message!=null){ %>
										<thead><tr class="center red"><td colspan="5"><%=message %></td></tr></thead>
									<%} %>
									
									<tbody>
										<%
											for (int row=1;row<=13;++row) {
										%>
										<tr>
											<%for(int col=1;col<=5;++col){ %>
												<%if(pantongColor!=null && col==pantongColor.getColumnNum() && row==pantongColor.getRowNum()){ %>
												<td class="selected"><%=pantongColor.getPanTongName() %></td>
												<%}else{ %>
												<td>&nbsp;</td>
												<%} %>
											<%} %>
										</tr>
										<%
											}
										%>
									</tbody>
									<tfoot><tr class="center"><td colspan="5">第<%=pantongColor==null?"?":pantongColor.getSheetNum() %>页</td></tr></tfoot>
								</table>
							</div>
						</div>
					</div>

				</div>
			</div>
		</div>
		<script type="text/javascript">
				/* 设置当前选中的页 */
				var $a = $("#left li a[href='pantongcolor/search']");
				setActiveLeft($a.parent("li"));
				/* 设置当前选中的页 */
			</script>
	</body>
</html>