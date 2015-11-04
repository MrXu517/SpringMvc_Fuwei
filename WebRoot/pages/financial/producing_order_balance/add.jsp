<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.producesystem.StoreInOut"%>
<%@page import="com.fuwei.entity.producesystem.StoreInOutDetail"%>
<%@page import="com.fuwei.commons.SystemCache"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.entity.Factory"%>
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
		<title>创建生产对账单 -- 桐庐富伟针织厂</title>
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
		<link href="css/printorder/print.css" rel="stylesheet" type="text/css" />
		<script src="js/plugins/jquery-barcode.min.js"></script>
		<style type="text/css">
		.table>thead>tr{
			  background: #AEADAD;
		}
		.table>tbody>tr{  background: #ccc;}
		.emptyTr{color:red;}
		#printform button{margin-bottom:5px;}
		#scanTb{table-layout:fixed;}
		#scanTb tr input{width:100%;}
		#scanTb td{  word-wrap: break-word;  word-break: break-word;}
		#completeBtn{margin-left: 20px;}
		</style>
	</head>
	<body>
		<%@ include file="../../common/head.jsp"%>
		<div id="Content">
			<div id="main">
				<div class="breadcrumbs" id="breadcrumbs">
					<ul class="breadcrumb">
						<li>
							<i class="fa fa-home"></i>
							<a href="user/index">首页</a>
						</li>
						<li class="active">
							创建生产对账单
						</li>
					</ul>
				</div>
				<div class="body">
					<div style="page-break-after: always">
						<div class="container-fluid auto_container">
							<div class="row">
								<div class="col-md-12 tablewidget">
									<div class="row">
									<form class="form-horizontal addform" role="form">
										<div class="form-group col-md-6">
											<label for="factoryId" class="col-sm-3 control-label">
												生产单位
											</label>
											<div class="col-sm-8">
												<select name="factoryId" id="factoryId"
													class="form-control require" >
												<option value="">未选择</option>
												<%for(Factory factory : SystemCache.produce_factorylist){ %>
												<option value="<%=factory.getId() %>"><%=factory.getName() %></option>
												<%} %>
												</select>
											</div>
											<div class="col-sm-1"></div>
										</div>
										<div class="form-group col-md-6">
											<label for="balance_employee" class="col-sm-3 control-label">
												本厂对账人
											</label>
											<div class="col-sm-8">
												<input type="text" name="balance_employee" id="balance_employee"
													class="form-control require" />
												
											</div>
											<div class="col-sm-1"></div>
										</div>
										<div class="form-group col-md-6">
											<label for="balance_at" class="col-sm-3 control-label">
												对账时间
											</label>
											<div class="col-sm-8">
												<input type="text" name="balance_at" id="balance_at"
													class="form-control require date" />
												
											</div>
											<div class="col-sm-1"></div>
										</div>
										<div class="form-group col-md-6">
											<label for="balance_factory_employee" class="col-sm-3 control-label">
												机织单位对账人
											</label>
											<div class="col-sm-8">
												<input type="text" name="balance_factory_employee" id="balance_factory_employee"
													class="form-control require" />
												
											</div>
											<div class="col-sm-1"></div>
										</div>
										<div class="form-group col-md-6">
											<label for="memo" class="col-sm-3 control-label">
												备注
											</label>
											<div class="col-sm-8">
												<input type="text" name="memo" id="memo"
													class="form-control require" />
												
											</div>
											<div class="col-sm-1"></div>
										</div>
									</form></div>
								<div class="row">
									<form class="form-horizontal form" role="form">
										<div class="form-group">
											
											<label for="producingOrderId" class="col-sm-3 control-label">
												生产单ID
											</label>
											<div class="input-group col-md-9">
												<input type="text" class="form-control"
													name="producingOrderId" id="producingOrderId"
													placeholder="请使用扫码枪扫描入生产单">
												<span class="input-group-btn">
													<button class="btn btn-primary" type="submit">
														确定
													</button>  </span>
												<button id="completeBtn" type="button" class="btn btn-primary ">对账完成，开始生成对账单</button>
											</div>
										</div>
										
									</form></div>
										<table class="table table-responsive table-bordered" id="scanTb">
										
											<thead>
												<tr><th width="30px">
														操作
													</th>
													<th width="40px">
														公司
													</th>
													<th width="40px">
														跟单
													</th>
													<th width="50px">
														生产单号
													</th>
													<th width="50px">
														订单号
													</th>
													<th width="60px">
														货号
													</th>
													<th width="80px">
														款名
													</th>
													<th width="70px">
														数量
													</th>
													<th width="50px">
														单价
													</th>
													<th width="50px">
														金额
													</th>
													<th width="60px">
														扣款金额
													</th>
													<th width="80px">
														扣款理由
													</th><th width="60px">
														应付金额
													</th>
													<th width="80px">
														备注
													</th>
												</tr>
											</thead>
											<tbody>
												
											</tbody>
										</table>
									
									<p class="pull-right auto_bottom">
										<span id="created_user">制单人：<%=SystemContextUtils.getCurrentUser(session).getLoginedUser().getName()%></span>
										
										<span id="date"> 制单日期：<%=DateTool.formatDateYMD(DateTool.now())%></span>
									</p>



								</div>

							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<script type="text/javascript">
	
	$("#tag").click(function(){
		$("#tag").focus();
		$("#tag").select();
	});
	
	//扫描
	var $form = $(".form");
	$form.submit( function() {
		var factoryId = $("#factoryId").val();
		if(factoryId == ""){
			alert("请先选择机织单位");
			return false;
		}
		var formdata = $(this).serializeJson();
		formdata.factoryId = factoryId;
		//判断是否已有生产单ID的行
		var tabledata = ScanTbInstance.getTableData();
		for(var i = 0 ; i < tabledata.length;++i){
			if(tabledata[i].producingOrderId == formdata.producingOrderId){
				Common.Error("该生产单已扫描");
				return false;
			}
		}
		$.ajax( {
			url :"producing_order_balance/scan_check",
			type :'POST',
			data :$.param(formdata),
			success : function(result) {
				$("#producingOrderId").focus();
				$("#producingOrderId").select();
				var detaillist = result.detaillist;
				var addedTrs = [];
				for(var i = 0 ; i < detaillist.length; ++i){
					var rowdata = {};
					for(var property in result){
						rowdata[property] = result[property];
					}
					delete rowdata.detaillist;
					rowdata.quantity = detaillist[i].quantity;
					rowdata.price = detaillist[i].price;
					rowdata.amount = rowdata.quantity * rowdata.price;
					var addedTr = ScanTbInstance.addRow(rowdata);
					$(addedTr).attr("producingOrderId",rowdata.producingOrderId);
					addedTrs[i] = addedTr;
					
				}
				var added_to_index = addedTrs.length-1;
				var $tds1 = $(addedTrs).find("td:nth-child(1)");
				var $tds2 = $(addedTrs).find("td:nth-child(2)");
				var $tds3 = $(addedTrs).find("td:nth-child(3)");
				var $tds4 = $(addedTrs).find("td:nth-child(4)");
				var $tds5 = $(addedTrs).find("td:nth-child(5)");
				var $tds6 = $(addedTrs).find("td:nth-child(6)");
				var $tds7 = $(addedTrs).find("td:nth-child(7)");
				
				var $tds14 = $(addedTrs).find("td:nth-child(14)");
				var $tds15 = $(addedTrs).find("td:nth-child(15)");
				
				mergeCellByIndex($tds1,"row","0-"+added_to_index);
				mergeCellByIndex($tds2,"row","0-"+added_to_index);
				mergeCellByIndex($tds3,"row","0-"+added_to_index);
				mergeCellByIndex($tds4,"row","0-"+added_to_index);
				mergeCellByIndex($tds5,"row","0-"+added_to_index);
				mergeCellByIndex($tds6,"row","0-"+added_to_index);
				mergeCellByIndex($tds7,"row","0-"+added_to_index);
			
				mergeCellByIndex($tds14,"row","0-"+added_to_index);
				mergeCellByIndex($tds15,"row","0-"+added_to_index);
				
			},
			error : function(result) {
				Common.Error("扫描失败：" + result.responseText);
			}

		});
		return false;
	});
	
	//生成对账单form
	$("#compelteBtn").click(function(){
		if(!confirm("确定生成对账单吗？")){
			return false;
		}
		var tabledata = ScanTbInstance.getTableData();
		var $addform = $(".addform");
		var addform_data = $addform.serializeJson();
		if(!Common.checkform($addform[0])){
			return false;
		}
		addform_data.details = JSON.stringify(tabledata);
		$.ajax( {
			url :"producing_order_balance/add",
			type :'POST',
			data :$.param(addform_data),
			success : function(result) {
				location.href="producing_order_balance/detail/" + result.id;
			},
			error : function(result) {
				Common.Error("不能生成对账单：" + result.responseText);
			}

		});
		return false;
	});
	
	var tbOptions = {
		focus:false,
		tableEle:$("#scanTb")[0],
		showNoOptions : {
					display :false
				},
				colnames : [{
							name :'_handle',
							colname :'操作',
							width :'30px',
							displayValue:function(value,rowdata){
								return "<a href='#' class='delete' producingOrderId='" + rowdata.producingOrderId+ "'>删除</a>";
							}
						},{
							name :'company_name',
							colname :'公司',
							width :'40px'
						},{
							name :'charge_employee_name',
							colname :'跟单',
							width :'40px'
						},{
							name :'producingOrder_number',
							colname :'生产单号',
							width :'50px'
						},
						{
							name :'orderNumber',
							colname :'订单号',
							width :'50px'
						},{
							name :'company_productNumber',
							colname :'货号',
							width :'60px'
						},{
							name :'sample_name',
							colname :'款名',
							width :'80px'
						},{
							name :'quantity',
							colname :'数量',
							width :'70px',
							className:"input int"
						},
						{
							name :'price',
							colname :'单价',
							width :'50px',
							className:"input double"
						},
						{
							name :'amount',
							colname :'金额',
							width :'50px'
						},
						{
							name :'deduct_money',
							colname :'扣款金额',
							width :'50px',
							className:"input double"
						},
						{
							name :'deduct_memo',
							colname :'扣款理由',
							width :'80px',
							className:"input"
						},{
							name :'total_amount',
							colname :'总应付金额',
							width :'60px'
						},
						{
							name :'memo',
							colname :'备注',
							width :'80px',
							className:"input"
						}
						]
			}
	var ScanTbInstance = TableTools.createTableInstance(tbOptions);
	
	$("#scanTb").on("click",".delete",function(){
	 	var producingOrderId = $(this).attr("producingOrderId");
	 	var $trs = $("tr[producingOrderId='" +producingOrderId + "']");
		ScanTbInstance.batch_deleteRow($trs);
		return false;
	});
	function mergeCellByIndex($tds, type, index) {
    
	    var indexArrs = index.split(',');
	    
	    var fromTo;
	    var from;
	    var to;
	    
	    for (var i in indexArrs) {
	        fromTo = indexArrs[i];
	        
	        from = new Number(fromTo.split('-')[0]);
	        to = new Number(fromTo.split('-')[1]);
	        
	        for (var j = 1 + from; j <= to; j++) {
	            $($tds.get(j)).remove();
	        }
	        
	        if (type == 'row') {
	            $($tds.get(from)).attr("rowSpan", to - from + 1);
	        } else if (type == 'col') {
	            $($tds.get(from)).attr("colSpan", to - from + 1);
	        }
	        
	    }
	    
	}
</script>
	</body>
</html>