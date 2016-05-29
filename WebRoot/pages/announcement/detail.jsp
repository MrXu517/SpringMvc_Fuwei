<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.Announcement"%>
<%@page import="com.fuwei.util.DateTool"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Announcement announcement = (Announcement)request.getAttribute("announcement");
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>布告栏通知详情 -- 桐庐富伟针织厂</title>
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
		<script charset="utf-8" src="js/plugins/kindeditor/kindeditor-all-min.js"></script>
		<script charset="utf-8" src="js/plugins/kindeditor/lang/zh-CN.js"></script>
		<script charset="utf-8" src="js/plugins/jquery.form.js"></script>
		
		<style type="text/css">
			#mainImgDiv .thumbnail{width:200px;height:200px;text-align: center;line-height: 180px;text-decoration: none;display: inline-block;}
			#mainImgDiv .uploadDiv,#mainImgDiv #mainfile{display: inline-block;vertical-align: top;margin-left: 10px;}
			p.tip{color:red;}
			span.require{color: red;font-size: 20px;vertical-align: top;margin-left: 5px;}
			#detailImgDiv #detailfile{display: inline-block;}
			#detailImgDiv .thumbnail{vertical-align: top;width:150px;height:150px;text-align: center;line-height: 150px;text-decoration: none;display: inline-block;margin-right: 10px;}
			#detailImgDiv .thumbnail img{max-width: 140px;max-height: 140px;}
			#mainImgDiv .thumbnail img{max-width: 190px;max-height: 190px;}
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
							<a href="user/index">首页 </a>
						</li>
						<li>
							<a href="announcement/index">布告栏通知列表</a>
						</li>
						<li class="active">
							布告栏通知详情
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid"><div class="row"><div class="col-xs-12 col-md-12">
					<fieldset>
						<legend>
							布告栏通知详情
						</legend>
						<div id="detailDiv" style="width:700px;margin: 0 auto;">
							<h1 style="text-align: center;"> <%=announcement.getTopic() %></h1>
							<div><%=announcement.getContent() %></div>
							<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />
						<p><span style="padding-right: 40px;">发布人：<%=SystemCache.getUserName(announcement.getCreated_user()) %></span>
						   <span style="padding-right: 30px;">发布时间：<%=DateTool.formateDate(announcement.getCreated_at(),"yyyy/MM/dd HH:mm:ss") %></span>
						   <span style="padding-right: 30px;">最近修改时间：<%=DateTool.formateDate(announcement.getUpdated_at(),"yyyy/MM/dd HH:mm:ss") %></span></p>
						</div>
						
					</fieldset></div></div></div>
				</div>
			</div>
		</div>
		<script type="text/javascript">
			/*设置当前选中的页*/
			var $a = $("#left li a[href='announcement/index']");
			setActiveLeft($a.parent("li"));
			/*设置当前选中的页*/
		</script>
	</body>
</html>