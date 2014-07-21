<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.Sample"%>
<%@page import="com.fuwei.entity.GongXu"%>
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
	Integer charge_userId = null;
	try {
		charge_userId = Integer.valueOf(request
				.getParameter("charge_user"));
	} catch (Exception e) {
		charge_userId = -1;
	}
	List<GongXu> gongxulist = (List<GongXu>) request
			.getAttribute("gongxulist");
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
		<style type="text/css">
#calculateBtn {
	text-decoration: underline;
	padding-left: 10px;
}

#detail {
	height: 200px;
}

#priceDialog .modal-dialog {
	width: 700px;
}

#calculateDialog .modal-dialog {
	width: 1000px;
}

#calculateDialog .control-label {
	padding-left: 0;
	padding-right: 0;
}

#gongxuTb input   , #gongxuTb select {
	height: 30px;
}

.bj_gongxu_delete {
	font-size: 1.5em;
	padding: 6px 0;
}

#bj_result {
	height: 300px;
}

.bj_ifds_span {
	font-size: 12px !important;
	padding: 6px !important;
}

.bj_ifds_span>* {
	vertical-align: bottom;
}

.bj_gongxu_add {
	padding: 3px 6px !important;
	margin-bottom: 5px !important;
}

.bj_gongxu_delete {
	cursor: pointer;
}
.gongxuTbWidget{
	height:250px;
	overflow:auto;
}
</style>
	</head>
	<body>
		<%@ include file="../common/head.jsp"%>
		<div id="Content">
			<div class="breadcrumbs" id="breadcrumbs">
				<ul class="breadcrumb">
					<li>
						<i class="fa fa-home"></i>
						<a href="user/index">首页</a>
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
							<form class="form-horizontal sampleform" role="form"
								enctype="multipart/form-data">
								<div class="form-group col-md-4">
									<label for="charge_user" class="col-sm-3 control-label">
													打样人
												</label>
									<div class="col-sm-6">
									<select id="charge_user" class="form-control">
										<option value="">
											所有
										</option>
										<%
											for (User tempU : userlist) {
												if (charge_userId == tempU.getId()) {
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
									</select></div>
								</div>
							</form>
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
											<a target="_blank" class="cellimg"
												href="<%=sample.getImg()%>"><img
													style="max-width: 120px; height: 120px; max-height: 120px;"
													src="<%=sample.getImg_ss()%>"> </a>
										</td>
										<td><%=sample.getName()%></td>
										<td><%=sample.getProductNumber()%></td>
										<td><%=sample.getMaterial()%></td>
										<td><%=sample.getWeight()%></td>
										<td><%=sample.getSize()%></td>
										<td><%=sample.getCharge_user()%></td>
										<td><%=sample.getCreated_at()%></td>
										<td>
											<a class="calcuteDetail" href="#">核价</a> |
											<a href="sample/put/<%=sample.getId()%>">编辑</a> |
											<a data-cid="<%=sample.getId()%>" class="delete" href="#">删除</a>
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
		</div>

		<div id="priceDialog">
			<div class="modal fade">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal">
								<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
							</button>
							<h4 class="modal-title">
								核价
							</h4>
						</div>
						<div class="modal-body">
							<form class="form-horizontal priceform" role="form">
								<input type="hidden" id="id" name="id" />
								<div class="row">
									<div class="form-group col-md-5">
										<label for="cost" class="col-sm-12">
											成本
										</label>
										<div class="col-sm-12">
											<input type="text" class="form-control require double"
												name="cost" id="cost" placeholder="成本">
										</div>
									</div>
									<div class="form-group col-md-7">
										<label for="detail" class="col-sm-12">
											报价详情
											<a href="#" id="calculateBtn">报价计算器</a>
										</label>

										<div class="col-sm-12">
											<textarea class="form-control require" name="detail"
												id="detail" placeholder="报价详情"></textarea>
										</div>
									</div>
								</div>
								<div class="modal-footer">
									<button type="submit" class="btn btn-primary"
										data-loading-text="正在保存...">
										修改价格
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
		</div>

		<div id="calculateDialog">
			<div class="modal">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal">
								<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
							</button>
							<h4 class="modal-title">
								报价计算器
							</h4>
						</div>
						<div class="modal-body">
							<form class="form-horizontal calculateform" role="form">
								<div class="row">
									<div class="col-md-4">
										<fieldset id="bj1_fieldsets" class="bj_fieldset">
											<legend>
												材料费
											</legend>
											<div class="form-group">
												<label class="col-sm-3 control-label">
													克重
												</label>
												<div class="input-group col-md-8">

													<input type="text" name="bj_weight" id="bj_weight"
														class="double form-control">
													<span class="input-group-addon">/克</span>
												</div>
											</div>

											<div class="form-group">
												<label class="col-sm-3 control-label">
													损耗
												</label>
												<div class="col-sm-8">
													<input type="text" name="bj_sunhao" id="bj_sunhao"
														class="double form-control">
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-3 control-label">
													材料价格
												</label>
												<div class="col-sm-8">
													<input type="text" name="bj_mprice" id="bj_mprice"
														class="double form-control">
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-3 control-label">
													倒纱系数
												</label>
												<div class="input-group col-md-8">

													<input type="text" name="bj_dsxishu" id="bj_dsxishu"
														class="double form-control" disabled="">

													<span class="bj_ifds_span input-group-addon"><input
															type="checkbox" name="bj_ifds" id="bj_ifds" style=""><span
														style="">需要倒纱</span> </span>
												</div>
											</div>

										</fieldset>
									</div>
									<div class="gongxu col-md-4">
										<fieldset>
											<legend>
												工序费
											</legend>
											<div>
												<button type="button" class="btn btn-primary bj_gongxu_add"
													data-dismiss="modal">
													添加工序
												</button>
											</div>
											<div class="panel panel-default gongxuTbWidget">
												<table class="table-condensed table  table-hover"
													id="gongxuTb">
													<thead>
														<tr>
															<th style="width: 45%;">
																工序
															</th>
															<th style="width: 40%;">
																价格
															</th>
															<th>
																操作
															</th>
														</tr>
													</thead>
													<tbody>
														<tr>
															<td>
																<select name="bj_gongxu" id="bj_gongxu"
																	class="form-control" style="">
																	<%
																		for (GongXu gongxu : gongxulist) {
																	%>
																	<option value="<%=gongxu.getId()%>"><%=gongxu.getName()%></option>
																	<%
																		}
																	%>
																</select>
															</td>
															<td>
																<input type="text" name="bj_price" id="bj_price"
																	class="col-sm-8 double form-control" style="">
															</td>
															<td>
																<a class="bj_gongxu_delete fa fa-trash-o"></a>
															</td>
														</tr>
													</tbody>
												</table>
											</div>
										</fieldset>

									</div>
									<div class="cal_result col-md-4">
										<fieldset>
											<legend>
												报价计算结果
											</legend>

											<textarea id="bj_result" class="form-control"></textarea>

										</fieldset>
									</div>


								</div>
								<div class="row">
									<div class="col-md-4">
										<!-- 材料费2 -->

										<fieldset id="bj2_fieldsets" class="bj_fieldset">
											<legend>
												<input type="checkbox" name="bj_2_enable" id="bj_2_enable">
												材料费2
											</legend>
											<div class="form-group">
												<label class="col-sm-3 control-label">
													克重
												</label>
												<div class="input-group col-md-8">

													<input type="text" name="bj_weight_2" id="bj_weight_2"
														class="double form-control">
													<span class="input-group-addon">/克</span>
												</div>
											</div>

											<div class="form-group">
												<label class="col-sm-3 control-label">
													损耗
												</label>
												<div class="col-sm-8">
													<input type="text" name="bj_sunhao_2" id="bj_sunhao_2"
														class="double form-control">
												</div>



											</div>
											<div class="form-group">
												<label class="col-sm-3 control-label">
													材料价格
												</label>
												<div class="col-sm-8">
													<input type="text" name="bj_mprice_2" id="bj_mprice_2"
														class="double form-control">
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-3 control-label">
													倒纱系数
												</label>
												<div class="input-group col-md-8">

													<input type="text" name="bj_dsxishu_2" id="bj_dsxishu_2"
														class="double form-control" disabled="">

													<span class="bj_ifds_span input-group-addon"><input
															type="checkbox" name="bj_ifds_2" id="bj_ifds_2" style=""><span
														style="">需要倒纱</span> </span>
												</div>
											</div>
										</fieldset>
										<!-- 材料费2 -->

									</div>
									<div class="col-md-4">
										<fieldset>
											<legend>
												增值税率和利润率
											</legend>
											<div class="form-group">
												<label class="col-sm-3 control-label">
													附加值
												</label>
												<div class="col-sm-8">
													<input type="text" name="bj_ps" id="bj_ps"
														class="double form-control">
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-3 control-label">
													增值税率
												</label>
												<div class="col-sm-8">
													<input type="text" name="bj_zzrate" id="bj_zzrate"
														class="double form-control">
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-3 control-label">
													利润率
												</label>
												<div class="col-sm-8">
													<input type="text" name="bj_lrate" id="bj_lrate"
														class="double form-control">
												</div>
											</div>
										</fieldset>
									</div>
								</div>
								<div class="row">
									<div class="modal-footer">
										<button type="submit" class="btn btn-primary">
											确定
										</button>
										<button type="button" class="btn btn-default"
											data-dismiss="modal">
											关闭
										</button>
									</div>
								</div>
							</form>
						</div>

					</div>
					<!-- /.modal-content -->
				</div>
				<!-- /.modal-dialog -->
			</div>
			<!-- /.modal -->
		</div>

	</body>
</html>
