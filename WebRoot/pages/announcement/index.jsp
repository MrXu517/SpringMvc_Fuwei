<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.commons.Pager"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.fuwei.entity.Announcement"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Pager pager = (Pager) request.getAttribute("pager");
	if (pager == null) {
		pager = new Pager();
	}
	Date start_time = (Date) request.getAttribute("start_time");
	String start_time_str = "";
	if (start_time != null) {
		start_time_str = DateTool.formatDateYMD(start_time);
	}
	Date end_time = (Date) request.getAttribute("end_time");
	String end_time_str = "";
	if (end_time != null) {
		end_time_str = DateTool.formatDateYMD(end_time);
	}
	
	List<Announcement> list = new ArrayList<Announcement>();
	if (pager != null & pager.getResult() != null) {
		list = (List<Announcement>) pager.getResult();
	}
	//权限相关
	Boolean has_add = SystemCache.hasAuthority(session,
			"announcement/add");
	Boolean has_edit = SystemCache.hasAuthority(session,
			"announcement/edit");
	Boolean has_sethomepage = SystemCache.hasAuthority(session,
			"announcement/sethomepage");
%>
<!DOCTYPE html>

<html>
	<head>
		<base href="<%=basePath%>">
		<title>布告栏通知列表 -- 桐庐富伟针织厂</title>
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
		<style type="text/css">
.content>a {
	color: #000;
}

tr,tr strong {
	font-weight: normal;
}

tr.unread,tr.unread strong {
	font-weight: bold;
}
.pagination {
	vertical-align: bottom;margin: 0;
}
.timeDiv{width:360px;display: inline-block;margin-bottom: 0;vertical-align: bottom;}
.timeDiv>label{width:90px;vertical-align: bottom;margin-top: 8px;}
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
					</ul>
				</div>
				<div class="body">

					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 tablewidget">
								<!-- Table -->
								<div class="navbar">
									<%if(has_add){ %>
									<a href="announcement/add" class="btn btn-danger pull-right">发布新通知</a>
									<%} %>
									<form class="form-inline" style="display: inline-block;">
										<input type="hidden" name="page" value="1" />
										<div class="timeDiv">
											<label class="col-sm-3 control-label">
												创建时间
											</label>

											<div class="input-group col-md-9">
												<input type="text" name="start_time" id="start_time"
													class="date form-control" value="<%=start_time_str%>" />
												<span class="input-group-addon">到</span>
												<input type="text" name="end_time" id="end_time"
													class="date form-control" value="<%=end_time_str%>" />
											</div>
										</div>
										<button class="btn btn-primary" type="submit">搜索</button>
									</form>
									<ul class="pagination">
										<li>
											<a href="announcement/index?start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=1">«</a>
										</li>

										<%
										if (pager.getPageNo() > 1) {
									%>
										<li class="">
											<a href="announcement/index?start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=<%=pager.getPageNo() - 1%>">上一页
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
											<a href="announcement/index?start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=<%=pager.getPageNo() %>"><%=pager.getPageNo()%>/<%=pager.getTotalPage()%>，共<%=pager.getTotalCount()%>条<span
												class="sr-only"></span> </a>
										</li>
										<li>
											<%
											if (pager.getPageNo() < pager.getTotalPage()) {
										%>
										
										<li class="">
											<a href="announcement/index?start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=<%=pager.getPageNo() + 1%>">下一页
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

										</li>
										<li>
											<a href="announcement/index?start_time=<%=start_time_str %>&end_time=<%=end_time_str %>&page=<%=pager.getTotalPage()%>">»</a>
										</li>
									</ul>

								</div>

								<table class="table table-responsive">
									<thead>
										<tr>
											<th width="400px">
												主题
											</th>
											<th width="60px">
												发布人
											</th>
											<th width="70px">
												发布时间
											</th>
											<th width="60px">
												操作
											</th>
										</tr>
									</thead>
									<tbody>
										<%
											for (Announcement item : list) {
										%>
										
										<tr>
											
											<td class="topic">
												<a href="announcement/detail/<%=item.getId()%>"><%=item.getTopic()%></a>
											<td class="created_user"><%=SystemCache.getUserName(item.getCreated_user())%></td>
											<td class="created_at"><%=item.getCreated_at()%></td>
											<td>
												<a href="announcement/detail/<%=item.getId()%>">详情</a> 
												<%if(has_edit){ %>
												|
												<a href="announcement/put/<%=item.getId()%>">编辑</a>
												<%} %>
												<%if(has_sethomepage){ 
													if(!item.getHomepage()){
												%>
												|
												<a data-cid="<%=item.getId()%>" class="sethomepage" href="#">设置为首页显示</a>
												<%} else{%>
												<span class="label label-success">首页置顶</span>
												<%} }%>
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
		</div>

		<script type="text/javascript">
		/*设置当前选中的页*/
		var $a = $("#left li a[href='announcement/index']");
		setActiveLeft($a.parent("li"));
		//删除单据 -- 开始
		$(".sethomepage").click( function() {
			var id = $(this).attr("data-cid");
			if (!confirm("确定要将该通知显示在首页吗？")) {
				return false;
			}
			$.ajax( {
				url :"announcement/sethomepage/" + id,
				type :'POST'
			}).done( function(result) {
				if (result.success) {
					Common.Tip("置顶成功", function() {
						location.reload();
					});
				}
			}).fail( function(result) {
				Common.Error("置顶失败：" + result.responseText);
			}).always( function() {
	
			});
			return false;
		});
		//删除单据  -- 结束
		</script>
	</body>
</html>