<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.entity.Material"%>
<%@page import="com.fuwei.entity.Employee"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="com.fuwei.commons.SystemSettings"%>
<%@page import="com.fuwei.util.DateTool"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String start_at_str = SystemSettings.sample_display_start_at == null ? "" : SystemSettings.sample_display_start_at; 
	String end_at_str = SystemSettings.sample_display_end_at == null ? "" : SystemSettings.sample_display_end_at; 
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>系统设置 -- 桐庐富伟针织厂</title>
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
		<script src="<%=basePath%>js/plugins/WdatePicker.js"></script>
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
							系统设置
						</li>
					</ul>
				</div>
				<div class="body">

					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 formwidget">
								<div class="panel panel-primary">
									<div class="panel-body">

										<form class="form-horizontal form" role="form">
											<div class="col-md-12">
												<div class="form-group">
													<label for="sample_display_start_at" class="col-sm-3 control-label">
														样品展示开始时间
													</label>
													<div class="col-sm-8">
														<input type="text" class="date form-control"
															name="sample_display_start_at" id="sample_display_start_at" 
															value="<%=start_at_str %>" placeholder="请输入起始时间">
													</div>
													<div class="col-sm-1"></div>
												</div>
												<div class="form-group">
													<label for="sample_display_end_at" class="col-sm-3 control-label">
														样品展示结束时间
													</label>
													<div class="col-sm-8">
														<input type="text" class="date form-control"
															name="sample_display_end_at" id="sample_display_end_at" 
															value="<%=end_at_str %>" placeholder="请输入结束时间">
													</div>
													<div class="col-sm-1"></div>
												</div>
												
												<div class="form-group">
													<div class="col-sm-offset-3 col-sm-5">
														<button type="submit" class="btn btn-primary"
															data-loading-text="正在保存...">
															确定
														</button>

													</div>
													<div class="col-sm-3">
													</div>
													<div class="col-sm-1"></div>
												</div>
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
		<script type="text/javascript">
		$(document).ready( function() {
			/* 设置当前选中的页 */
			var $a = $("#left li a[href='systemsetting/set']");
			setActiveLeft($a.parent("li"));
			/* 设置当前选中的页 */
			
			var $form = $(".form"); 
			var $submitBtn = $form.find("[type='submit']");
			$form.unbind("submit");
			$form.submit( function() {
				if (!Common.checkform(this)) {
					return false;
				}
				$submitBtn.button('loading');
				var formdata = $(this).serializeJson();
				var postdata = {data:JSON.stringify(formdata)};
				$.ajax({
					url :"systemsetting/set",
					type :'POST',
					data :$.param(postdata),
					success : function(result) {
						if (result.success) {
							Common.Tip("设置成功", function() {
								location = "systemsetting/set";
							});
						}
						$submitBtn.button('reset');
					},
					error : function(result) {
						Common.Error("设置失败：" + result.responseText);
						$submitBtn.button('reset');
					}
		
				});
				return false;
			});
		});
		</script>
	</body>
</html>