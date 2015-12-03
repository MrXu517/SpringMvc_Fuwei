<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.DataCorrectRecord"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	List<DataCorrectRecord> list = (List<DataCorrectRecord>) request
			.getAttribute("list");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>数据纠正记录 -- 桐庐富伟针织厂</title>
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


		<link href="css/systeminfo.css" rel="stylesheet" type="text/css" />
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
							数据纠正记录
						</li>
					</ul>
				</div>
				<div class="body">
					<div id="materialTab">
						<div class="container-fluid">
							<div class="row">
								<div class="col-md-12 tablewidget">
									<div class="panel panel-primary">
										<!-- Default panel contents -->
										<div class="panel-heading">
											数据纠正记录
										</div>

										<!-- Table -->
										<table class="table table-responsive">
											<thead>
												<tr>
													<th>
														动作
													</th>
													<th>
														单据
													</th>
													<th>
														描述
													</th>
													<th>
														操作人
													</th>
													<th>
														时间
													</th>
												</tr>
											</thead>
											<tbody>
												<%
													for (DataCorrectRecord temp : list) {
												%>
												<tr>
													<td><%=temp.getOperation()%></td>
													<td><%=temp.getTb_table()%></td>
													<td><%=temp.getDescription()%></td>
													<td><%=SystemCache.getUserName(temp.getCreated_user())%></td>
													<td><%=temp.getCreated_at()%></td>
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
				</div>
			</div>
		</div>
	<script type="text/javascript">
	/*设置当前选中的页*/
	var $a = $("#left li a[href='datacorrectrecord/index']");
	setActiveLeft($a.parent("li"));

	</script>
	</body>
</html>