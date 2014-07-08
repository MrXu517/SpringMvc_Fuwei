<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.Sample"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	List<User> userlist = (List<User>) request.getAttribute("userlist");

	List<Sample> samplelist = (List<Sample>) request
			.getAttribute("samplelist");
	/*List<Company> companylist = (List<Company>) request
			.getAttribute("companylist");
	List<Salesman> salesmanlist = (List<Salesman>) request
			.getAttribute("salesmanlist");
	List<GongXu> gongxulist = (List<GongXu>) request
			.getAttribute("gongxulist");
	
	
	List<Role> rolelist = (List<Role>) request.getAttribute("rolelist");
	
	String tabname = (String) request.getParameter("tab");*/
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
		<script src="js/common/common.js" type="text/javascript"></script>
		<script src="js/sample/undetailedindex.js" type="text/javascript"></script>

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
						待核价样品
					</li>
				</ul>
			</div>
			<div class="body">

				<div class="container-fluid">
					<div class="row">
						<div class="col-md-12 tablewidget">
							<!-- Table -->
							<table class="table table-responsive">
								<thead>
									<tr>
										<th>
											序号
										</th>

										<th>
											图片
										</th>
										<th>
											名称
										</th>
										<th>
											货号
										</th>
										<th>
											材料
										</th>
										<th>
											克重
										</th>
										<th>
											尺寸
										</th>
										<th>
											打样人
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
										for (Sample sample : samplelist) {
									%>
									<tr sampleId="<%=sample.getId()%>">
										<td><%=++i%></td>
										<td
											style="max-width: 120px; height: 120px; max-height: 120px;">
											<a target="_blank" class="cellimg" href="<%=sample.getImg()%>"><img style="max-width: 120px; height: 120px; max-height: 120px;"
													src="<%=sample.getImg()%>"> </a>
										</td>
										<td><%=sample.getName()%></td>
										<td><%=sample.getProductNumber()%></td>
										<td><%=sample.getMaterial()%></td>
										<td><%=sample.getWeight()%></td>
										<td><%=sample.getSize()%></td>
										<td><%=sample.getCharge_user()%></td>
										<td><%=sample.getCreated_at()%></td>
										<td><a href="#">核价</a> | <a href="#">编辑</a> | <a href="#">删除</a></td>
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