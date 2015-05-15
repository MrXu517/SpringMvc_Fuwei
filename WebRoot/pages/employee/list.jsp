<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.Department"%>
<%@page import="com.fuwei.entity.Employee"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.DateTool"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	List<Employee> employeelist = (List<Employee>) request
			.getAttribute("employeelist");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>花名册 -- 桐庐富伟针织厂</title>
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
		<link href="css/employee/list.css" rel="stylesheet"
			type="text/css"/>
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
							考勤系统
						</li>
						<li class="active">
							花名册
						</li>
					</ul>
				</div>
				<div class="body">

					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 tablewidget">
								<a class="btn btn-primary export" href="employee/export" target="_blank">导出</a>
								<!-- Table -->
								<table class="table table-responsive table-bordered">
									<thead>
										<tr>
											<th width="20px">
												序号
											</th>
											<th width="40px">
												编号
											</th>
											<th width="40px">
												姓名
											</th>
											<th width="20px">
												性别
											</th><th width="50px">
												入厂日期
											</th><th width="100px">
												身份证号码
											</th><th width="50px">
												生日
											</th><th width="50px">
												联系方式
											</th><th width="50px">
												岗位
											</th><th width="50px">
												部门
											</th><th width="100px">
												家庭住址
											</th><th width="100px">
												现居住地
											</th><th colspan="2" width="100px">
												合同期限
											</th><th width="50px">
												用工形式
											</th><th width="50px">
												年薪
											</th><th width="50px">
												时薪
											</th><th width="50px">
												银行
											</th><th width="100px">
												银行卡号
											</th>
											
										</tr>
									</thead>
									<tbody>
										<%
											int s_i = 1;
											for (Employee employee : employeelist) {
										%>
										<tr>
											<td><%=s_i%></td>
											<td><%=employee.getNumber()%></td>
											<td><%=employee.getName()%>
												<%
													if (!employee.getInUse()) {
												%><span class="label label-default">已离职</span><br>
													<span class="label label-default"><%=DateTool.formatDateYMD(employee.getLeave_at()) %></span>	
												<%
													}
												%>
											</td>
											<td><%=employee.getSex()%></td>
											<td><%=DateTool.formatDateYMD(employee.getEnter_at())%></td>
											<td><%=employee.getId_card()%></td>
											<td><%=DateTool.formatDateYMD(employee.getBirthday())%></td>
											<td><%=employee.getTel()%></td>
											<td><%=employee.getJob()%></td>
											<td><%=SystemCache.getDepartmentName(employee
								.getDepartmentId())%></td>
											<td><%=employee.getAddress_home()%></td>
											<td><%=employee.getAddress()%></td>
											<td><%=DateTool.formatDateYMD(employee.getAgreement_at())%></td>
											<td><%=DateTool.formatDateYMD(employee.getAgreement_end())%></td>
											<td><%=employee.getEmployee_type()%></td>
											<td><%=employee.getYear_salary()%></td>
											<td><%=employee.getHour_salary()%></td>
											<td><%=employee.getBank_name() == null ? "行内":employee.getBank_name()%></td>
											<td><%=employee.getBank_no()%></td>
											
											
											
										</tr>
										<%
											s_i++;
											}
										%>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- Nav tabs -->
		</div>
	<script type="text/javascript">
	/*设置当前选中的页*/
	var $a = $("#left li a[href='employee/list']");
	setActiveLeft($a.parent("li"));

</script>
	</body>
</html>
