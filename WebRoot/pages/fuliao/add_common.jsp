<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.FuliaoType"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="com.fuwei.entity.Salesman"%>
<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@page import="com.fuwei.entity.Customer"%>
<%@page import="com.fuwei.entity.Company"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	List<FuliaoType> fuliaoTypelist = SystemCache.fuliaotypelist;
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
		<title>添加通用辅料 -- 桐庐富伟针织厂</title>
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
		<script src="js/plugins/jquery.form.js" type="text/javascript"></script>
		<script src="js/fuliao/add_common.js" type="text/javascript"></script>
		<style type="text/css">
.addform input[type='file'] {
	outline: none !important;
}
span.location_size{margin-right:20px;font-size:20px;}
input[name='location_size']{height:20px;width:20px;}
</style>
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
							<a href="fuliao_workspace/commonfuliao?tab=fuliaolist">通用辅料列表及出入库情况</a>
						</li>
						<li class="active">
							添加通用辅料
						</li>
					</ul>
				</div>
				<div class="body">

					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 formwidget">
								<div class="panel panel-primary">
									<div class="panel-heading">
										<h3 class="panel-title">
											添加通用辅料
										</h3>
									</div>
									<div class="panel-body">
	
										<form class="form-horizontal addform" role="form"
											enctype="multipart/form-data">
											<div class="col-md-7">
												<fieldset>
													<legend>第一步、填写公司客户信息</legend>
											<div class="form-group">
											<label for="kehu" class="col-sm-3 control-label">
												客户
											</label>
											<div class="col-sm-8">
												<select class="form-control" name="customerId"
													id="customerId">
													<option value="">
														未选择
													</option>
													<%
														for (Customer customer : SystemCache.customerlist) {
														%>	
													<option value="<%=customer.getId()%>"><%=customer.getName()%></option>
													<%
														}
													%>
												</select>
											</div>
											<div class="col-sm-1"></div>
										</div>
										<div class="form-group">
											<label for="companyId" class="col-sm-3 control-label">
												公司
											</label>
											<div class="col-sm-8">
												<select data='<%=companySalesmanMap_str%>'
													class="form-control" name="companyId"
													id="companyId" placeholder="公司">
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
											</div>
										</div>
										<div class="form-group">
											<label for="salesmanId" class="col-sm-3 control-label">
												业务员
											</label>
											<div class="col-sm-8">
												<select class="form-control" name="salesmanId"
													id="salesmanId" placeholder="业务员">
													<option value="">
														未选择
													</option>
												</select>
											</div>
										</div>
											</fieldset>
												<fieldset>
													<legend>第二步、填写辅料基本信息</legend>
												<div class="form-group">
													<label for="file" class="col-sm-3 control-label">
														图片
													</label>
													<div class="col-sm-8">
														<input type="file" name="file" id="file"
															class="form-control require" placeholder="图片" />

													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="fuliaoTypeId" class="col-sm-3 control-label">
														辅料类型
													</label>
													<div class="col-sm-8">
														<select class="form-control require" name="fuliaoTypeId"
															id="fuliaoTypeId">
															<option value="">
																未选择
															</option>
															<%
																for (FuliaoType fuliaoType : fuliaoTypelist) {
															%>
															<option value="<%=fuliaoType.getId()%>"><%=fuliaoType.getName()%></option>
															<%
																}
															%>
														</select>
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="company_orderNumber" class="col-sm-3 control-label">
														公司订单号
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control"
															name="company_orderNumber" id="company_orderNumber" placeholder="公司订单号">
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="company_productNumber" class="col-sm-3 control-label">
														公司款号
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control"
															name="company_productNumber" id="company_productNumber" placeholder="公司款号">
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="country" class="col-sm-3 control-label">
														国家/城市
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control"
															name="country" id="country" placeholder="国家/城市">
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="color" class="col-sm-3 control-label">
														颜色
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control"
															name="color" id="color" placeholder="颜色">
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="size" class="col-sm-3 control-label">
														尺码
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control"
															name="size" id="size" placeholder="尺码">
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="batch" class="col-sm-3 control-label">
														批次
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control"
															name="batch" id="batch" placeholder="批次">
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="plan_quantity" class="col-sm-3 control-label">
														订单所需数量
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control positive_int require"
															name="plan_quantity" id="plan_quantity" placeholder="数量" value="0">
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="memo" class="col-sm-3 control-label">
														备注
													</label>
													<div class="col-sm-8">
														<input type="text" class="form-control" name="memo"
															id="memo" placeholder="备注">
													</div>
													<div class="col-sm-1"></div>
												</div>
												
												</fieldset>
												<fieldset>
													<legend>第三步、选择合适的库位</legend>
													<div class="form-group">
													<label for="location_size" class="col-sm-3 control-label">
														请选择库位容量，将为您自动匹配相应容量的库位
													</label>
													<div class="col-sm-8">
														<input type="radio" name="location_size" id="location_size_3" value="3"/><span class="location_size">大</span>
														<input type="radio" name="location_size" id="location_size_2" value="2"/><span class="location_size">中</span>
														<input type="radio" name="location_size" id="location_size_1" value="1" checked/><span class="location_size">小</span>
													</div>
													<div class="col-sm-1"></div>
												</div>
													<div class="form-group">
													<div class="col-sm-offset-3 col-sm-5">
														<button type="submit" class="btn btn-primary"
															data-loading-text="正在保存...">
															添加
														</button>

													</div>
													<div class="col-sm-3">
														<button type="reset" class="reset btn btn-default">
															重置表单
														</button>
													</div>
													<div class="col-sm-1"></div>
												</div>
												</fieldset>
											</div>
											<div class="col-md-5" style="width: 400px;"
												id="previewWidget">
												<a href="#" class="thumbnail"> <img id="previewImg"
														alt="400 x 100%"> </a>
											</div>
										</form>
									</div>

								</div>
							</div>


						</div>
					</div>

				</div>
			</div>
		</div>
	</body>
</html>