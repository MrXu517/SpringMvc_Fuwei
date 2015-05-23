<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.GongXu"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	List<GongXu> gongxulist = SystemCache.gongxulist;


%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>报价工具 -- 桐庐富伟针织厂</title>
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

		<script src="js/sample/util_price.js" type="text/javascript"></script>
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

#gongxuTb input     , #gongxuTb select {
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

.gongxuTbWidget {
	height: 250px;
	overflow: auto;
}
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
						<li class="active">
							报价工具
						</li>
					</ul>
				</div>
				<div class="body">

					<div class="container-fluid">
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
															<th style="width: 30%;">
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
																<button class="multi12" type="button">x12</button>
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
										
									</div>
								</div>
							</form>
					</div>

				</div>
			</div>
		</div>

	</body>
</html>
