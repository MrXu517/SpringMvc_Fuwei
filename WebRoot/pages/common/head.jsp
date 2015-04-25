<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.commons.LoginedUser"%>
<%@page import="com.fuwei.entity.User"%>
<%@page import="com.fuwei.entity.Module"%>
<%@page import="com.fuwei.commons.SystemContextUtils"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%
	LoginedUser loginedUser = SystemContextUtils
			.getCurrentUser(session);
	User user = loginedUser.getLoginedUser();
	
	
	//权限相关
	Boolean h_has_sample = SystemCache.hasAuthority(session,"sample");
	Boolean h_has_sample_add = SystemCache.hasAuthority(session,"sample/add");
	Boolean h_has_sample_index = SystemCache.hasAuthority(session,"sample/index");
	Boolean h_has_sample_undetailedindex = SystemCache.hasAuthority(session,"sample/undetailedindex");
//	Boolean h_has_quote_index = SystemCache.hasAuthority(session,"quote/index");
//	Boolean h_has_quoteorder_index = SystemCache.hasAuthority(session,"quoteorder/index");
//	Boolean h_has_systeminfo= SystemCache.hasAuthority(session,"systeminfo");
	Boolean h_has_authority = SystemCache.hasAuthority(session,"authority");
	
	Boolean h_has_order = SystemCache.hasAuthority(session,"order");
	Boolean h_has_order_add = SystemCache.hasAuthority(session,"order/add");
	Boolean h_has_order_index = SystemCache.hasAuthority(session,"order/index");
	
	Boolean h_has_materialsys = SystemCache.hasAuthority(session,"materialsys");
	//Boolean h_has_materialsys_sample = SystemCache.hasAuthority(session,"materialsys/sample");//打样
	Boolean h_has_material_index = SystemCache.hasAuthority(session,"material/index");
	
	Boolean h_has_materialsys_purchase = SystemCache.hasAuthority(session,"material_purchase_order/index");//原材料采购单
	Boolean h_has_materialsys_purchase_add = SystemCache.hasAuthority(session,"material_purchase_order/add");//原材料采购单
	
	
	//报表
	Boolean h_has_report = SystemCache.hasAuthority(session,"report");
	Boolean h_has_report_material = SystemCache.hasAuthority(session,"report/material");//材料库存报表
	Boolean h_has_report_material_purchase = SystemCache.hasAuthority(session,"report/material_purchase");//原材料采购报表
	Boolean h_has_report_financial = SystemCache.hasAuthority(session,"report/financial");//财务报表
	
	//系统信息管理
	Boolean h_has_systeminfo = SystemCache.hasAuthority(session,"systeminfo");
	Boolean h_has_systeminfo_salesman = SystemCache.hasAuthority(session,"salesman");
	Boolean h_has_systeminfo_material = SystemCache.hasAuthority(session,"material");
	Boolean h_has_systeminfo_factory = SystemCache.hasAuthority(session,"factory");
	Boolean h_has_systeminfo_gongxu = SystemCache.hasAuthority(session,"gongxu");
	Boolean h_has_systeminfo_user = SystemCache.hasAuthority(session,"user");
	//角色管理
	Boolean h_has_systeminfo_role = SystemCache.hasAuthority(session,"role");
	//客户管理
	Boolean h_has_systeminfo_customer = SystemCache.hasAuthority(session,"customer");
	
	//染色单
	Boolean h_has_coloring_order_index = SystemCache.hasAuthority(session,"coloring_order/index");//染色单
	Boolean h_has_coloring_order_add = SystemCache.hasAuthority(session,"coloring_order/add");//原材料采购单
	//辅料采购单
	Boolean h_has_fuliao_purchase_order_index = SystemCache.hasAuthority(session,"fuliao_purchase_order/index");//辅料采购单
	Boolean h_has_fuliao_purchase_order_add = SystemCache.hasAuthority(session,"fuliao_purchase_order/add");//辅料采购单
	
	//2015-4-16添加报价工具
	Boolean h_has_sample_util_price = SystemCache.hasAuthority(session,"sample/util/price");
	
	//2015--4-24添加装箱工具
	Boolean h_has_box_util = SystemCache.hasAuthority(session,"util/box");
	
	//权限相关
	
%>
<html>
	<link href="css/common/head.css" rel="stylesheet" type="text/css" />
	<script src="js/common/head.js" type="text/javascript"></script>
	<script type="text/javascript">
		version = 2015042501;
	</script>
	<div style="display: none;" class="background"></div>
	<div style="display: none;" class="loading">
		数据加载中，请稍等......
	</div>
	
	<div id="alert">
		<div id="alert_tip">
			<div class="modal fade">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal">
								<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
							</button>
							<h4 class="modal-title">
								提示
							</h4>
						</div>
						<div class="modal-body">
							<p>
								提示
							</p>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default"
								data-dismiss="modal">
								确定
							</button>
						</div>
					</div>
					<!-- /.modal-content -->
				</div>
				<!-- /.modal-dialog -->
			</div>
			<!-- /.modal -->
		</div>
		<div id="alert_error">
			<div class="modal fade">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal">
								<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
							</button>
							<h4 class="modal-title">
								错误信息
							</h4>
						</div>
						<div class="modal-body">
							<p>
								发生错误
							</p>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default"
								data-dismiss="modal">
								关闭
							</button>
						</div>
					</div>
					<!-- /.modal-content -->
				</div>
				<!-- /.modal-dialog -->
			</div>
			<!-- /.modal -->
		</div>
	</div>
	<div id="header">
		<div class="headnav">
			<div class="btn-group">
				<button type="button" class="btn btn-success dropdown-toggle" data-toggle="dropdown">
					<%=user.getName()%>
				</button>
				<button type="button" class="btn btn-success dropdown-toggle"
					data-toggle="dropdown">
					<span class="caret"></span>
					<span class="sr-only">Toggle Dropdown</span>
				</button>
				<ul class="dropdown-menu" role="menu">
					<li>
						<a href="user/set"><i class="fa fa-cog"></i>修改密码</a>
					</li>
					<li>
						<a href="user/logout"><i class="fa fa-power-off"></i>退出登录</a>
					</li>
				</ul>
			</div>
		</div>
		<div class="messagenav">
			<a href="message/unread" type="button" class="btn btn-danger"><i class="fa fa-envelope"></i>
		<%if(loginedUser.getMessage_count() > 0){ %>
<span><%=loginedUser.getMessage_count()%></span>
		<%} %>
		</a> </div>
		<div class="logo">
			<a href="user/index"><small> <i class="fa fa-leaf"></i>
					桐庐富伟针织厂管理系统 </small> </a>
		</div>
		<div class="clear"></div>
	</div>

	<!--    <div id="mainContainer"> -->
	<div id="left">
		<div class="panel panel-primary">

			<div class="menubar">
				<ul class="menubar-ul">
					<li class="first active">
						<a href="user/index"><i class="fa fa-home"></i>首页</a>
					</li>
					<%if(h_has_sample){ %>
					<li class="li_dropdown">
						<a href="#"><i class="fa fa-desktop"></i>样品系统<i
							class="fa fa-angle-down"></i> </a>
						<ul class="submenu">
							<%
								//if(user.getAuthority() == FuweiSystemData.AUTHORITY_GENERAL){
							%>
							<%if(h_has_sample_index){ %>
							<li>
								<a href="sample/index"><i class="fa fa-dashboard"></i>样品管理</a>
							</li>
							<%}if(h_has_sample_add){ %>
							<li>
								<a href="sample/add"><i class="fa fa-plus"></i>新增样品</a>
							</li>
							<%}if(h_has_sample_undetailedindex){ %>
							 <li>
								<a href="sample/undetailedindex"><i class="fa fa-edit"></i>待核价样品</a>
							</li> 
							<%}if(h_has_sample_util_price){ %>
							 <li>
								<a href="sample/util_price"><i class="fa fa-thumb-tack"></i>报价工具</a>
							</li> 
							<%
							}
							%>


							
						</ul>
					</li>
					<%} %>

					<%if(h_has_order){ %>
					<li class="li_dropdown">
						<a href="#"><i class="fa fa-paperclip"></i>订单系统<i
							class="fa fa-angle-down"></i> </a>
						<ul class="submenu">
							<%if(h_has_order_index){ %>
							<li>
								<a href="order/index"><i class="fa fa-dashboard"></i>订单列表</a>
							</li>
							<%} %>
							<%if(h_has_order_add){ %>
							<li>
								<a href="order/add"><i class="fa fa-plus"></i>创建订单</a>
							</li>
							<%} %>
						
						</ul>
					</li>
					<%} %>

					<%if(h_has_materialsys){ %>
					<li class="li_dropdown">
						<a href="#"><i class="fa fa-barcode"></i>材料系统<i
							class="fa fa-angle-down"></i> </a>
						<ul class="submenu">
						
							<%if(h_has_materialsys_purchase){ %>
							<li>
								<a href="material_purchase_order/index"><i class="fa fa-sign-in"></i>原材料采购单</a>
							</li>
							<%} %>
							<%if(h_has_materialsys_purchase_add){ %>
							<li>
								<a href="material_purchase_order/add"><i class="fa fa-plus"></i>创建原材料采购单</a>
							</li>
							<%} %>

							<%if(h_has_fuliao_purchase_order_index){ %>
							<li>
								<a href="fuliao_purchase_order/index"><i class="fa fa-sign-in"></i>辅料采购单</a>
							</li>
							<%} %>
							<%if(h_has_fuliao_purchase_order_add){ %>
							<li>
								<a href="fuliao_purchase_order/add"><i class="fa fa-plus"></i>创建辅料采购单</a>
							</li>
							<%} %>

							<%if(h_has_coloring_order_index){ %>
							<li>
								<a href="coloring_order/index"><i class="fa fa-sign-in"></i>染色单</a>
							</li>
							<%} %>
							<%if(h_has_coloring_order_add){ %>
							<li>
								<a href="coloring_order/add"><i class="fa fa-plus"></i>创建染色单</a>
							</li>
							<%} %>
						
						</ul>
					</li>
					<%} %>

					<%if(h_has_report){ %>
					<li class="li_dropdown">
						<a href="#"><i class="fa fa-table"></i>报表中心<i
							class="fa fa-angle-down"></i> </a>
						<ul class="submenu">
							<%if(h_has_report_material){ %>
							<li>
								<a href="report/material"><i class="fa fa-hdd-o"></i>材料库存报表</a>
							</li>
							<%} %>
							<%if(h_has_report_material_purchase){ %>
							<li>
								<a href="report/material_purchase"><i class="fa fa-sign-in"></i>原材料采购报表</a>
							</li>
							<%} %>
							<%if(h_has_report_financial){ %>
							<li>
								<a href="report/financial"><i class="fa fa-jpy"></i>财务报表</a>
							</li>
							<%} %>
						
						</ul>
					</li>
					<%} %>


					<%if(h_has_systeminfo){ %>
					<li class="li_dropdown">
						<a href="#"><i class="fa fa-list-alt"></i>系统信息管理<i
							class="fa fa-angle-down"></i> </a>
						<ul class="submenu">
							<%if(h_has_systeminfo_customer){ %>
							<li>
								<a href="customer/index">客户管理</a>
							</li>
							<%} %>
							<%if(h_has_systeminfo_salesman){ %>
							<li>
								<a href="salesman/index">公司和业务员</a>
							</li>
							<%} %>
							<%if(h_has_systeminfo_gongxu){ %>
							<li>
								<a href="gongxu/index">工序</a>
							</li>
							<%} %>
							<%if(h_has_systeminfo_factory){ %>
							<li>
								<a href="factory/index">工厂</a>
							</li>
							<%} %>
							<%if(h_has_systeminfo_material){ %>
							<li>
								<a href="material/index">材料</a>
							</li>
							<%} %>
							<%if(h_has_systeminfo_user){ %>
							<li>
								<a href="user/list">用户管理</a>
							</li>
							<%} %>
							<%if(h_has_systeminfo_role){ %>
							<li>
								<a href="role/list">角色管理</a>
							</li>
							<%} %>
							
						
						</ul>
					</li>
					<%} 
					if(h_has_box_util){%>
					<li>
						<a href="util/box"><i class="fa fa-truck"></i>装箱工具</a>
					</li>


					<%} 
					if(h_has_authority){%>
					<li>
						<a href="authority/index"><i class="fa fa-lock"></i>权限设置</a>
					</li>

					<%
					}
					%>

					
				</ul>
			</div>

			<div class="panel-heading">
				<h3 class="panel-title">
					<div class="sidebar-collapse" id="sidebar-collapse">
						<i class="fa fa-angle-double-left"
							data-icon1="fa-angle-double-left"
							data-icon2="fa-angle-double-right"></i>
					</div>
				</h3>
			</div>

		</div>

	</div>