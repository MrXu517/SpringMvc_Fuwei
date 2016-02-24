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
	Boolean h_has_util = SystemCache.hasAuthority(session,"util");
	
	//2015--4-25添加待发货订单列表
	Boolean h_has_order_undelivery = SystemCache.hasAuthority(session,"order/undelivery");
	
	
	//2015--4-25添加员工列表
	Boolean h_has_systeminfo_employee = SystemCache.hasAuthority(session,"employee");
	
	
	//2015-5-10添加人事系统
	Boolean h_has_renshi = SystemCache.hasAuthority(session,"renshi");
	Boolean h_has_renshi_employees = SystemCache.hasAuthority(session,"renshi/employees");//花名册
	Boolean h_has_renshi_salarys = SystemCache.hasAuthority(session,"renshi/salarys");//工资表
	
	//2015-5-12添加验厂系统
	Boolean h_has_yanchang = SystemCache.hasAuthority(session,"yanchang");
	Boolean h_has_yanchang_fake_salary = SystemCache.hasAuthority(session,"yanchang/fake_salary");
	
	
	//2015-5-30添加财务相关
	Boolean h_has_financial = SystemCache.hasAuthority(session,"financial");
	Boolean h_has_financial_bank = SystemCache.hasAuthority(session,"bank/index");
	Boolean h_has_financial_subject = SystemCache.hasAuthority(session,"subject/index");
	Boolean h_has_financial_expense_income = SystemCache.hasAuthority(session,"expense_income/add");
	Boolean h_has_financial_invoice = SystemCache.hasAuthority(session,"invoice/add");
	//Boolean h_has_financial_sale_invoice = SystemCache.hasAuthority(session,"sale_invoice/add");
	//2015-6-4添加财务工作台
	Boolean h_has_financial_workspace = SystemCache.hasAuthority(session,"financial/workspace");

	//2015-5-30添加财务报表
	Boolean h_has_report_financial_payable = SystemCache.hasAuthority(session,"report/financial/payable");//应付报表
	Boolean h_has_report_financial_purchase_invoice = SystemCache.hasAuthority(session,"report/financial/purchase_invoice");//进项发票报表
	Boolean h_has_report_financial_expense_income = SystemCache.hasAuthority(session,"report/financial/expense_income");//进项发票报表
	Boolean h_has_report_financial_sale_invoice = SystemCache.hasAuthority(session,"report/financial/sale_invoice");//进项发票报表
	Boolean h_has_report_financial_receivable = SystemCache.hasAuthority(session,"report/financial/receivable");//进项发票报表
	
	//2015-6-10添加材料、染色报表
	Boolean h_has_report_material_purchase_detail = SystemCache.hasAuthority(session,"report/material_purchase_detail");
	Boolean h_has_report_coloring_detail = SystemCache.hasAuthority(session,"report/coloring_detail");
	
	
	//2015-6-16添加仓储系统
	Boolean h_has_store = SystemCache.hasAuthority(session,"store");
	Boolean h_has_store_material = SystemCache.hasAuthority(session,"store/material");
	Boolean h_has_store_half_product = SystemCache.hasAuthority(session,"store/half_product"); //半成品仓库
	Boolean h_has_store_product = SystemCache.hasAuthority(session,"store/product"); //成品仓库	
	
	//2015-6-18添加生产系统
	Boolean has_producesystem = SystemCache.hasAuthority(session,"producesystem");
	Boolean has_producing_order_index = SystemCache.hasAuthority(session,"producing_order/index");
	//2015-6-18添加未划价生产单
	Boolean has_order_producing_unprice_list = SystemCache.hasAuthority(session,"order/producing/price_edit") || SystemCache.hasAuthority(session,"order/producing/price_request");
	
	//2015-6-26添加 原材料工作台, 半成品工作台
	Boolean has_material_workspace = SystemCache.hasAuthority(session,"producesystem/material_workspace");
	Boolean has_halfstoreorder_workspace = SystemCache.hasAuthority(session,"producesystem/halfstoreorder_workspace");
	
	
	Boolean has_packing_order_index = SystemCache.hasAuthority(session,"packing_order/index");
	
	//2015-8-24添加
	Boolean h_has_report_fuliao_purchase = SystemCache.hasAuthority(session,"report/fuliao_purchase");//原材料采购报表
	Boolean h_has_report_fuliao_purchase_detail = SystemCache.hasAuthority(session,"report/fuliao_purchase_detail");
	Boolean h_has_report_coloring_summary = SystemCache.hasAuthority(session,"report/coloring_summary");
	
	//2015-10-12
	Boolean h_has_util_pantong_color = SystemCache.hasAuthority(session,"pantongcolor/search");//潘通色号
	Boolean h_has_pantongcolor_import = SystemCache.hasAuthority(session,"pantongcolor/import");//潘通色号
	//2015-10-14
	Boolean h_has_half_current_stock = SystemCache.hasAuthority(session,"report/half_current_stock");//半成品库存报表
	
	Boolean h_has_gongxu_producing_order_index = SystemCache.hasAuthority(session,"gongxu_producing_order/index");//查询工序加工单
	
	Boolean h_has_order_progress = SystemCache.hasAuthority(session,"order/progress");//订单生产进度
	
	Boolean h_has_fuliaotype = SystemCache.hasAuthority(session,"fuliaotype");//辅料类型管理
	
	Boolean h_has_fuliaosystem = SystemCache.hasAuthority(session,"fuliaosystem");//辅料仓库系统
	Boolean h_has_fuliao_workspace = SystemCache.hasAuthority(session,"fuliao_workspace/workspace");//辅料仓库工作台
	Boolean h_has_location = SystemCache.hasAuthority(session,"location/index");//辅料仓库工作台
	//权限相关
	Boolean h_has_systemsettings = SystemCache.hasAuthority(session,"systemsetting");//系统设置
	Boolean h_has_sampledisplay = SystemCache.hasAuthority(session,"sample_display/index");//样品展示
	
	Boolean h_has_datacorrectrecord = SystemCache.hasAuthority(session,"data/correct");//数据纠正
	
	Boolean h_has_producebill_index = SystemCache.hasAuthority(session,"producebill/index");//生产对账单列表
%>
<html>
	<link href="css/common/head.css" rel="stylesheet" type="text/css" />
	<script src="js/common/head.js" type="text/javascript"></script>
	<script src="js/plugins/jquery.goup.min.js" type="text/javascript"></script> 
	
	<script type="text/javascript">
	version = 2015042502;
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
				<button type="button" class="btn btn-success dropdown-toggle"
					data-toggle="dropdown">
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
			<a href="message/unread" type="button" class="btn btn-danger"><i
				class="fa fa-envelope"></i> <%if(loginedUser.getMessage_count() > 0){ %>
				<span><%=loginedUser.getMessage_count()%></span> <%} %> </a>
		</div>
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
							}if(h_has_sampledisplay){ %>
							<li>
								<a href="sample_display/index"><i class="fa fa-dashboard"></i>样品展示</a>
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
							<%if(h_has_order_undelivery){ %>
							<li>
								<a href="order/undelivery"><i class="fa fa-truck"></i>待发货</a>
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
					<%if(has_producesystem){ %>
					<li class="li_dropdown">
						<a href="#"><i class="fa fa-barcode"></i>生产系统<i
							class="fa fa-angle-down"></i> </a>
						<ul class="submenu">
							<%if(has_packing_order_index){ %>
							<li>
								<a href="packing_order/index">查询装箱单</a>
							</li>
							<%} %>
							<%if(has_material_workspace){ %>
							<li>
								<a href="workspace/material_workspace">原材料工作台</a>
							</li>
							<%} %><%if(h_has_order_progress){ %>
							<li>
								<a href="store_in/order_progress">原材料生产进度</a>
							</li>
							<%} %>
							<%if(has_halfstoreorder_workspace){ %>
							<li>
								<a href="workspace/half_workspace">半成品工作台</a>
							</li>
							<%} %><%if(h_has_order_progress){ %>
							<li>
								<a href="half_store_in/order_progress">半成品生产进度</a>
							</li>
							<%} %>
							<%if(has_order_producing_unprice_list){ %>
							<li>
								<a href="producing_order/unprice_list"><i class="fa fa-warning"></i>未划价生产单</a>
							</li>
							<%} %>

							<%if(has_producing_order_index){ %>
							<li>
								<a href="producing_order/index">查询生产单</a>
							</li>
							<%} %>
							<%if(has_producing_order_index){ %>
							<li>
								<a href="producing_order/scan">扫描生产单</a>
							</li>
							<%} %>
							<%if(h_has_gongxu_producing_order_index){ %>
							<li>
								<a href="gongxu_producing_order/index">查询工序加工单</a>
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
								<a href="material_purchase_order/index"><i
									class="fa fa-sign-in"></i>原材料采购查询</a>
							</li>
							<%} %>
							<%if(h_has_materialsys_purchase_add){ %>
							<li>
								<a href="material_purchase_order/add"><i class="fa fa-plus"></i>采购原材料</a>
							</li>
							<%} %>

							<%if(h_has_fuliao_purchase_order_index){ %>
							<li>
								<a href="fuliao_purchase_order/index"><i
									class="fa fa-sign-in"></i>辅料采购查询</a>
							</li>
							<%} %>
							<%if(h_has_fuliao_purchase_order_add){ %>
							<li>
								<a href="fuliao_purchase_order/add"><i class="fa fa-plus"></i>采购辅料</a>
							</li>
							<%} %>

							<%if(h_has_coloring_order_index){ %>
							<li>
								<a href="coloring_order/index"><i class="fa fa-sign-in"></i>染色单查询</a>
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
					<%if(h_has_financial){ %>
					<li class="li_dropdown">
						<a href="#"><i class="fa fa-list-alt"></i>财务<i
							class="fa fa-angle-down"></i> </a>
						<ul class="submenu">
							<%if(h_has_financial_workspace){ %>
							<li>
								<a href="financial/workspace">财务工作台</a>
							</li>
							<%} %>
							<%if(h_has_financial_invoice){ %>
							<li>
								<a href="purchase_invoice/add">进项发票</a>
							</li>
							<%} %>
							<%if(h_has_financial_invoice){ %>
							<li>
								<a href="sale_invoice/add">销项发票</a>
							</li>
							<%} %>
							<%if(h_has_financial_expense_income){ %>
							<li>
								<a href="expense/add">支出</a>
							</li>
							<%} %>
							<%if(h_has_financial_expense_income){ %>
							<li>
								<a href="income/add">收入</a>
							</li>
							<%} %>
							<%if(h_has_financial_bank){ %>
							<li>
								<a href="bank/index">对方银行账户</a>
							</li>
							<%} %>
							<%if(h_has_financial_subject){ %>
							<li>
								<a href="subject/index">科目</a>
							</li>
							<%} %>
							<%if(h_has_producebill_index){ %>
							<li>
								<a href="producebill/index">生产对账单</a>
							</li>
							<%} %>
							
						</ul>
					</li>
					<%} %>
					<%if(h_has_fuliaosystem){ %>
					<li class="li_dropdown">
						<a href="#"><i class="fa fa-list-alt"></i>辅料仓库系统<i
							class="fa fa-angle-down"></i> </a>
						<ul class="submenu">
							<%if(h_has_fuliao_workspace){ %>
							<li>
								<a href="fuliao_workspace/workspace">辅料工作台</a>
							</li>
							<%} %>
							<%if(h_has_location){ %>
							<li>
								<a href="location/index">库位</a>
							</li>
							<%} %>
						</ul>
					</li>
					<%}%>
					<%if(h_has_report){ %>
					<li class="li_dropdown">
						<a href="#"><i class="fa fa-table"></i>报表中心<i
							class="fa fa-angle-down"></i> </a>
						<ul class="submenu">
							<%if(h_has_report_material){ %>
							<li>
								<a href="report/material">材料库存报表</a>
							</li>
							<%} %>
							<%if(h_has_report_material_purchase){ %>
							<li>
								<a href="report/material_purchase">原材料采购汇总报表</a>
							</li>
							<%} %>
							<%if(h_has_report_material_purchase_detail){ %>
							<li>
								<a href="report/material_purchase_detail">原材料采购明细报表</a>
							</li>
							<%} %>
							<%if(h_has_report_fuliao_purchase){ %>
							<li>
								<a href="report/fuliao_purchase">辅料采购汇总报表</a>
							</li>
							<%} %>
							<%if(h_has_report_fuliao_purchase_detail){ %>
							<li>
								<a href="report/fuliao_purchase_detail">辅料采购明细报表</a>
							</li>
							<%} %>
							<%if(h_has_report_coloring_summary){ %>
							<li>
								<a href="report/coloring_summary">染色汇总报表</a>
							</li>
							<%} %>
							<%if(h_has_report_coloring_detail){ %>
							<li>
								<a href="report/coloring_detail">染色明细报表</a>
							</li>
							<%} %>
							<%if(h_has_report_financial_payable){ %>
							<li>
								<a href="report/financial/payable">应付报表</a>
							</li>
							<%} %>
							<%if(h_has_report_financial_receivable){ %>
							<li>
								<a href="report/financial/receivable">应收报表</a>
							</li>
							<%} %>
							<%if(h_has_report_financial_expense_income){ %>
							<li>
								<a href="report/financial/expense_income">收支报表</a>
							</li>
							<%} %>
							<%if(h_has_report_financial_purchase_invoice){ %>
							<li>
								<a href="report/financial/purchase_invoice">进项发票报表</a>
							</li>
							<%} %>
							<%if(h_has_report_financial_sale_invoice){ %>
							<li>
								<a href="report/financial/sale_invoice">销项发票报表</a>
							</li>
							<%} %>
							<%if(h_has_half_current_stock){ %>
							<li>
								<a href="half_current_stock/report">半成品库存报表</a>
							</li>
							<%} %>
						</ul>
					</li>
					<%} %>
					

					<%if(h_has_renshi){ %>
					<li class="li_dropdown">
						<a href="#"><i class="fa fa-list-alt"></i>人事系统<i
							class="fa fa-angle-down"></i> </a>
						<ul class="submenu">
							<%if(h_has_renshi_employees){ %>
							<li>
								<a href="employee/list">花名册</a>
							</li>
							<%} %>
							<%if(h_has_renshi_salarys){ %>
							<li>
								<a href="employee/salarys">工资表</a>
							</li>
							<%} %>
						</ul>
					</li>
					<%}%>

					<%if(h_has_yanchang){ %>
					<li class="li_dropdown">
						<a href="#"><i class="fa fa-list-alt"></i>验厂<i
							class="fa fa-angle-down"></i> </a>
						<ul class="submenu">
							<%if(h_has_yanchang_fake_salary){ %>
							<li>
								<a href="yanchang/yan_salarys">计时工资表</a>
							</li>
							<%} %>
						</ul>
					</li>
					<%}%>
					
					<%if(h_has_systeminfo){ %>
					<li class="li_dropdown">
						<a href="#"><i class="fa fa-list-alt"></i>基础资料<i
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
							<%if(h_has_fuliaotype){ %>
							<li>
								<a href="fuliaotype/index">辅料类型</a>
							</li>
							<%} %>
							<%if(h_has_systeminfo_employee){ %>
							<li>
								<a href="employee/index">部门和员工</a>
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
							<%if(h_has_pantongcolor_import){ %>
							<li>
								<a href="pantongcolor/import">重置潘通色卡</a>
							</li>
							<%} %>


						</ul>
					</li>


					<%} %>

						

					<%if(h_has_util){ %>
					<li class="li_dropdown">
						<a href="#"><i class="fa fa-wrench"></i>工具<i
							class="fa fa-angle-down"></i> </a>
						<ul class="submenu">
							<%if(h_has_box_util){%>
							<li>
								<a href="util/box">ASOS箱贴生成器</a>
							</li>
							<%} %>
							<%if(h_has_util_pantong_color){%>
							<li>
								<a href="pantongcolor/search">潘通色号</a>
							</li>
							<%} %>
						</ul>
					</li>
					<%} %>

					<%if(h_has_datacorrectrecord){%>
					<li>
						<a href="datacorrectrecord/index"><i class="fa fa-lock"></i>数据纠正记录</a>
					</li>
					<%
					}
					%>

					<%if(h_has_authority){%>
					<li>
						<a href="authority/index"><i class="fa fa-lock"></i>权限设置</a>
					</li>
					<%
					}
					%>

					<%if(h_has_systemsettings){%>
					<li>
						<a href="systemsetting/set"><i class="fa fa-lock"></i>系统设置</a>
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
	