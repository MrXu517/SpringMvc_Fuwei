<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Material"%>
<%@page import="com.fuwei.entity.Employee"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="com.fuwei.entity.financial.Invoice"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.mchange.v2.ser.SerializableUtils"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.entity.financial.Subject"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.util.DateTool"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	List<Invoice> list = (List<Invoice>)request.getAttribute("list");
	List<Subject> subjectlist = SystemCache.getSubjectList(true);
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>批量填写销项发票明细 -- 桐庐富伟针织厂</title>
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
		<script src="js/plugins/jquery.form.js" type="text/javascript"></script>
		<script src="js/financial/sale_invoice/import_new_list.js" type="text/javascript"></script>
		<style type="text/css">
		.checkBtn{width:20px ;height:20px;}
		.table tr.checkselected {
    		background: #DCD773 !important;
		}
		#mainTb thead{background: #9E9D9D;}
		#mainTb select{padding-left: 0;padding-right: 0;}
		</style>
	</head>
	<body>
		<%@ include file="../../common/head.jsp"%>
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
							<a href="financial/workspace?tab=sale_invoices">财务工作台</a>
						</li>
						<li>
							<i class=""></i>
							<a href="expense_income/import_bank_new">批量导入销项发票</a>
						</li>
						<li class="active">
							批量填写销项发票明细
						</li>
					</ul>
				</div>
				<div class="body">

					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 formwidget">
								<fieldset>
									<legend>
										批量导入销项发票
									</legend>

									<form class="form-horizontal form" role="form"
										enctype="multipart/form-data" action="sale_invoice/import_new_list"
										method="post">
											<button type="submit" class="btn btn-primary"
														data-loading-text="正在加载...">
														导入
											</button>
											<p id="tip">您已选择了 <strong id="num">0</strong> 条记录 </p>
											<table id="mainTb"
											class="table table-responsive table-bordered detailTb">
											<thead>
												<tr><th width="5%">
														序号
													</th><th width="8%">
														发票类型
													</th><th width="10%">
														发票号
													</th><th width="8%">
														金额
													</th><th width="10%">
														开票时间
													</th><th width="12%">
														对方账户
													</th><th width="8%">
														公司
													</th><th width="8%">
														科目
													</th><th width="10%">
														备注
													</th>
												</tr>
											</thead>
											<tbody>
												<%
													for (Invoice item : list) {
												%>
												<tr class="tr EmptyTr disable" data='<%=SerializeTool.serialize(item)%>'>
													<td><input type="checkbox" name="checked" class="checkBtn"/></td>
													<td>增值税专用</td>
													<td><%=item.getNumber()%></td>
													<td><%=item.getAmount()%></td>
													<td><%=DateTool.formateDate(item.getPrint_date(),"yyyy-MM-dd HH:mm") %></td>
													
													<td><%=item.getBank_name()%></td>
													<td>
														<select class="company_id value require" name="company_id">
															<option value="">
																未选择
															</option>
															<%
																for (Company company : SystemCache.companylist) {
															%>
															<option value="<%=company.getId()%>"><%=company.getShortname()%></option>
															<%
															}
														%>
														</select>
													</td>
													<td>
														<select class="subject_id require  value">
															<option value="">未选择</option>
																<%for(Subject temp : subjectlist){ %>
															<option value="<%=temp.getId() %>"><%=temp.getName() %></option>
															<%} %>
														</select>
													</td>
													<td><textarea type="text" class="memo value" value="<%=item.getMemo()%>"><%=item.getMemo()%></textarea> </td>
													
												</tr>
												<%
													}
												%>
											</tbody>

										</table>
									</form>
								</fieldset>
							</div>

						</div>
					</div>

				</div>
			</div>
		</div>
		<script type="text/javascript">
	/*设置当前选中的页*/
	var $a = $("#left li a[href='financial/workspace']");
	setActiveLeft($a.parent("li"));

</script>
	</body>
</html>