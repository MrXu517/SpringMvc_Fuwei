<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Sample"%>
<%@page import="com.fuwei.entity.Company"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.fuwei.entity.QuotePrice"%>
<%@page import="net.sf.json.JSONObject"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	//List<User> userlist = (List<User>) request.getAttribute("userlist");
	Sample sample = (Sample) request.getAttribute("sample");
	List<QuotePrice> quotepricelist = (List<QuotePrice>) request
			.getAttribute("quotepricelist");
	/*List<Company> companylist = (List<Company>) request
			.getAttribute("companylist");
	List<Salesman> salesmanlist = (List<Salesman>) request
			.getAttribute("salesmanlist");
	List<GongXu> gongxulist = (List<GongXu>) request
			.getAttribute("gongxulist");
	
	
	List<Role> rolelist = (List<Role>) request.getAttribute("rolelist");
	
	String tabname = (String) request.getParameter("tab");*/
	HashMap<String, List<Salesman>> companySalesmanMap = SystemCache
			.getCompanySalesmanMap_ID();
	JSONObject jObject = new JSONObject();
	jObject.put("companySalesmanMap", companySalesmanMap);
	String companySalesmanMap_str = jObject.toString();
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>样品详情 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8" />
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">

		<script src="js/plugins/jquery-1.10.2.min.js"></script>
		<script src="js/common/common.js" type="text/javascript"></script>
		<script src="js/sample/detail.js" type="text/javascript"></script>
		<link href="css/sample/sample.css" rel="stylesheet" type="text/css" />
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
						样品详情
					</li>
				</ul>
			</div>
			<div class="body">

				<div class="container-fluid">
					<div class="samplehead">
						<div class="pull-left">
							<label class="control-label">
								名称：
							</label>
							<span><%=sample.getName()%></span>
						</div>
						<div class="pull-left">
							<label class="control-label">
								产品编号：
							</label>
							<span><%=sample.getProductNumber()%></span>
						</div>
						<div class="pull-right">
							<button type="button" class="btn btn-info">
								生成样品标签
							</button>
						</div>

						<div class="clear"></div>

					</div>
					<div class="sampleImg">

						<a href="#" class="thumbnail"> <img id="previewImg"
								alt="400 x 100%" src="<%=sample.getImg()%>"> </a>

					</div>
					<div class="sampleData">
						<table class="table table-responsive">
							<tbody>

								<tr>
									<td>
										打样人
									</td>
									<td><%=SystemCache.getUserName(sample.getCharge_user())%></td>
								</tr>
								<tr>
									<td>
										材料
									</td>
									<td><%=sample.getMaterial()%></td>
								</tr>
								<tr>
									<td>
										克重
									</td>
									<td><%=sample.getWeight()%>克
									</td>
								</tr>
								<tr>
									<td>
										尺寸
									</td>
									<td><%=sample.getSize()%></td>
								</tr>
								<tr>
									<td>
										成本
									</td>
									<td>
										<span class="RMB">￥</span><%=sample.getCost()%></td>
								</tr>
								<tr>
									<td>
										机织
									</td>
									<td><%=sample.getMachine()%></td>
								</tr>
								<tr>
									<td>
										创建时间
									</td>
									<td><%=sample.getCreated_at()%></td>
								</tr>
								<tr>
									<td>
										最近更新时间
									</td>
									<td><%=sample.getUpdated_at()%></td>
								</tr>
								<tr>
									<td>
										备注
									</td>
									<td><%=sample.getMemo()%></td>
								</tr>
							</tbody>
						</table>

					</div>
					<div class="sampleDetail">
						<fieldset>
							<legend>
								报价详情
							</legend>
							<textarea readonly class="form-control" id="detail" name="detail"
								placeholder="报价详情"><%=sample.getDetail()%></textarea>
						</fieldset>
					</div>
					<div class="clear"></div>

				</div>

				<div class="container-fluid">
					<div class="panel panel-default">
						<!-- Default panel contents -->
						<div class="panel-heading">
							公司报价
							<button type="button" class="btn btn-primary" id="addQuoteBtn">
								新建公司价格
							</button>
						</div>

						<!-- Table -->
						<table class="table">
							<thead>
								<tr>
									<th>
										序号
									</th>
									<th>
										公司名称
									</th>

									<th>
										业务员
									</th>
									<th>
										报价
									</th>
									<th>
										报价时间
									</th>
									<th>
										备注
									</th>
									<th>
										操作
									</th>
								</tr>
							</thead>
							<tbody>
								<%
									int k = 0;
									for (QuotePrice quotePrice : quotepricelist) {
								%>
								<tr>
									<td><%=++k%></td>
									<td><%=SystemCache.getCompanyName(SystemCache.getSalesman(
								quotePrice.getSalesmanId()).getCompanyId())%></td>
									<td><%=SystemCache.getSalesmanName(quotePrice
								.getSalesmanId())%></td>
									<td><%=quotePrice.getPrice()%></td>
									<td><%=quotePrice.getCreated_at()%></td>
									<td><%=quotePrice.getMemo()%></td>
									<td>
										<a class="addQuote" href="#"
											data-cid="<%=quotePrice.getId()%>">添加到报价列表</a> |
										<a class="edit" href="#"
											data-cid="<%=quotePrice.getId()%>">编辑</a> |
										<a class="printDetail" href="#"
											data-cid="<%=quotePrice.getId()%>">打印样品详情</a>
									</td>
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
		<div class="modal fade" id="quoteModal">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
						</button>
						<h4 class="modal-title">
							新建公司价格
						</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal quoteform" role="form">
							<input type="hidden" name="id" id="id" />
							<input type="hidden" id="sampleId" name="sampleId"
								value="<%=sample.getId()%>" />
							<div class="row">
								<div class="form-group">
									<label for="companyId" class="col-sm-3 control-label">
										公司
									</label>
									<div class="col-sm-8">
										<select data='<%=companySalesmanMap_str%>'
											class="form-control require" name="companyId" id="companyId"
											placeholder="公司">
											<%
												for (Company company : SystemCache.companylist) {
											%>
											<option value="<%=company.getId()%>"><%=company.getFullname()%></option>
											<%
												}
											%>
										</select>
									</div>
								</div>
								<div class="form-group">
									<label for="salesmanId" class="col-sm-3 control-label">
										业务员
									</label>
									<div class="col-sm-8">
										<select class="form-control require " name="salesmanId"
											id="salesmanId" placeholder="业务员"></select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label">
										公司款号
									</label>
									<div class="col-sm-8">
										<input type="text" name="cproductN" id="cproductN"
											class="form-control">
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label">
										价格
									</label>
									<div class="col-sm-8">
										<input type="text" name="price" id="price"
											class="double form-control">
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-3 control-label">
										备注
									</label>
									<div class="col-sm-8">
										<input type="text" name="memo" id="memo" class="form-control">
									</div>
								</div>
							</div>
							<div class="modal-footer">
								<button type="submit" class="btn btn-primary"
									data-loading-text="正在保存...">
									新建价格
								</button>
								<button type="reset" class="btn btn-default"
									data-dismiss="modal">
									取消
								</button>
							</div>
						</form>
					</div>

				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
		</div>
		<!-- /.modal -->
	</body>
</html>