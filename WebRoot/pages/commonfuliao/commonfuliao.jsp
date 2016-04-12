<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.Order"%>
<%@page import="com.fuwei.util.DateTool"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>通用辅料列表与出入库情况 -- 桐庐富伟针织厂</title>
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
<style type="text/css">
.thumbnail>img{max-width: 200px;max-height: 150px;}
#leftOrderInfo{  width: 210px;}
#leftOrderInfo .table-bordered>tbody>tr>td{border-color: #000;}
#rightStoreInfo{margin-left:210px;}
#rightStoreInfo .table-bordered>thead>tr>th,#rightStoreInfo .table-bordered>tbody>tr>td{border-color: #000;}
#rightStoreInfo #storeDetail legend{font-weight: bold;}
iframe{border:none;width:100%;}
#tab .tab-content .tab-pane,#tab .tab-content iframe{
	height:100%;
}
#tab .nav-tabs{height:50px;}
div.name{margin-left: 20px;width: 100px; display: inline-block;}
.noborder.table>tbody>tr>td{border:none;padding:3px;}
.noborder.table{font-weight:bold;  margin-bottom: 5px;}
.thumbnail{margin-bottom:0;}
</style>
	</head>

	<body>
		<%@ include file="../common/head.jsp"%>
	
		<div id="Content" class="auto_container">
			<div id="main">
				<div class="breadcrumbs" id="breadcrumbs">
					<ul class="breadcrumb">
						<li>
							<i class="fa fa-home"></i>
							<a href="user/index">首页</a>
						</li>
						<li class="active">
							通用辅料列表与出入库情况
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 btnWidget">
								<div id="tab">
									<ul class="nav nav-tabs" role="tablist">
										<li class="active">
											<a href="#fuliaolist" role="tab" data-toggle="tab">通用辅料列表</a>
										</li>
										<li>
											<a href="#fuliaoinnotice" role="tab" data-toggle="tab">通用辅料入库通知单</a>
										</li>
										<li>
											<a href="#fuliaooutnotice" role="tab" data-toggle="tab">通用辅料出库通知单</a>
										</li>
									</ul>
			
			
									<div class="tab-content">
										<div class="tab-pane active" id="fuliaolist">
											<iframe id="fuliaolistIframe" name="fuliaolistIframe" border="0" furl="fuliao/list_common" > </iframe>
										</div>
										<div class="tab-pane" id="fuliaoinnotice">
											<iframe id="fuliaoinnoticeIframe" name="fuliaoinnoticeIframe" border="0" furl="fuliaoin_notice/list_common" > </iframe>
										</div>
										<div class="tab-pane" id="fuliaooutnotice">
											<iframe id="fuliaooutnoticeIframe" name="fuliaooutnoticeIframe" border="0" furl="fuliaoout_notice/list_common" > </iframe>
										</div>
									</div>
								</div>
							</div>
					
					</div>
					
					
				</div>

			</div>
		</div>
		</div>
	<script type="text/javascript">
			/*设置当前选中的页*/
			var $a = $("#left li a[href='fuliao_workspace/commonfuliao']");
			setActiveLeft($a.parent("li"));
			$("#tab ul.nav>li").click(function(){
				var $a = $(this).find("a");
				var tab_name = $a.attr("href").substring(1);
				var $frame = $("#"+tab_name).find("iframe");
				var src = $frame.attr("src");
				if(src == undefined || src == ""){
					$frame.attr("src",$frame.attr("furl"));
				}
			});
			
			$("iframe").load(function(){
				var mainheight = $(this).contents().find("body")[0].scrollHeight+10;
				$(this).height(mainheight);
			});
			$("#tab ul.nav>li").first().click();
		</script>
	</body>
</html>
