<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.Sample"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.commons.Pager"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	List<User> userlist = (List<User>) request.getAttribute("userlist");
	Pager pager = (Pager) request.getAttribute("pager");
	if (pager == null) {
		pager = new Pager();
	}
	List<Sample> samplelist = new ArrayList<Sample>();
	if (pager != null & pager.getResult() != null) {
		samplelist = (List<Sample>) pager.getResult();
	}

	Integer charge_userId = (Integer) request
			.getAttribute("charge_user");
	String charge_user_str = "";
	if (charge_userId != null) {
		charge_user_str = String.valueOf(charge_userId);
	}

	String name = (String)request.getAttribute("name");
	if(name == null){
		name = "";
	}
	
%>
<!DOCTYPE html>

<html>
	<head>
		<base href="<%=basePath%>">
		<title>样品管理 -- 桐庐富伟针织厂</title>
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
		<script src="<%=basePath%>js/plugins/WdatePicker.js"></script>
		<script src="js/common/common.js" type="text/javascript"></script>
		<link href="css/sample/sample.css" rel="stylesheet" type="text/css" />
		<script src="js/sample/search.js" type="text/javascript"></script>
	</head>
	<body>
		<div id="Content">
			<div id="main">
				<div class="body">

					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 tablewidget">
								<!-- Table -->
								<div clas="navbar navbar-default">
									<form class="form-horizontal searchform form-inline"
										role="form" action="sample/search">
										<input type="hidden" name="page" id="page"
											value="<%=pager.getPageNo()%>">
										<div class="form-group" style="width: 200px;">
											<label for="charge_user" class="col-sm-3 control-label"
												style="width: 60px;">
												打样人
											</label>
											<div class="col-sm-8">
												<select id="charge_user" name="charge_user"
													class="form-control">
													<option value="">
														所有
													</option>
													<%
														for (User tempU : userlist) {
															if (charge_userId != null && charge_userId == tempU.getId()) {
													%>
													<option value="<%=tempU.getId()%>" selected="selected"><%=tempU.getName()%></option>
													<%
														} else {
													%>
													<option value="<%=tempU.getId()%>"><%=tempU.getName()%></option>
													<%
														}
														}
													%>
												</select>
											</div>
										</div>
										<div class="form-group timegroup">
											<label class="col-sm-2 control-label">
												名称
											</label>

											<div class="input-group col-md-10">
												<input type="text" name="name" id="name"
													class="form-control" value="<%=name%>" />
												<span class="input-group-btn">
													<button class="btn btn-primary" type="submit">
														搜索
													</button> </span>
											</div>
										</div>
									</form>
									<ul class="pagination">
										<li>
											<a
												href="sample/search?charge_user=<%=charge_user_str%>&name=<%=name %>&page=1">«</a>
										</li>

										<%
											if (pager.getPageNo() > 1) {
										%>
										<li class="">
											<a
												href="sample/search?charge_user=<%=charge_user_str%>&name=<%=name %>&page=<%=pager.getPageNo() - 1%>">上一页
												<span class="sr-only"></span> </a>
										</li>
										<%
											} else {
										%>
										<li class="disabled">
											<a disabled>上一页 <span class="sr-only"></span> </a>
										</li>
										<%
											}
										%>

										<li class="active">
											<a
												href="sample/search?charge_user=<%=charge_user_str%>&name=<%=name %>&page=<%=pager.getPageNo()%>"><%=pager.getPageNo()%><span
												class="sr-only"></span> </a>
										</li>
										<li>
											<%
												if (pager.getPageNo() < pager.getTotalPage()) {
											%>
										
										<li class="">
											<a
												href="sample/search?charge_user=<%=charge_user_str%>&name=<%=name %>&page=<%=pager.getPageNo() + 1%>">下一页
												<span class="sr-only"></span> </a>
										</li>
										<%
											} else {
										%>
										<li class="disabled">
											<a disabled>下一页 <span class="sr-only"></span> </a>
										</li>
										<%
											}
										%>

										<li></li>
										<li>
											<a
												href="sample/search?charge_user=<%=charge_user_str%>&name=<%=name %>&page=<%=pager.getTotalPage()%>">»</a>
										</li>
									</ul>
									

								</div>
								<table class="table table-responsive">
									<thead>
										<tr>
											<th>
												操作
											</th>
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
											
											
										</tr>
									</thead>
									<tbody>
										<%
											int i = 0;
											for (Sample sample : samplelist) {
										%>
										<tr sampleId="<%=sample.getId()%>">
											<td>
												<a href="#" class="confirm">选择</a>
											</td>
											<td><%=++i%></td>
											<td
												style="max-width: 120px; height: 120px; max-height: 120px;">
												<a target="_blank" class="cellimg"
													href="/<%=sample.getImg()%>"><img
														style="max-width: 120px; height: 120px; max-height: 120px;"
														src="/<%=sample.getImg_ss()%>"> </a>
											</td>
											<td><%=sample.getName()%></td>
											<td><%=sample.getProductNumber()%></td>
											<td><%=SystemCache.getMaterialName(sample.getMaterialId())%></td>
											<td><%=sample.getWeight()%></td>
											<td><%=sample.getSize()%></td>
											<td><%=SystemCache.getUserName(sample.getCharge_user()) %></td>
											
											
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
	</body>
</html>