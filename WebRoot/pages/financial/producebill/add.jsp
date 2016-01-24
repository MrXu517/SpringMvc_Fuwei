<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="com.fuwei.commons.Pager"%>
<%@page import="com.fuwei.entity.financial.ProduceBillDetail"%>
<%@page import="com.fuwei.entity.financial.ProduceBillDetail_Detail"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	Integer factoryId = (Integer) request.getAttribute("factoryId");
	List<ProduceBillDetail> resultlist = new ArrayList<ProduceBillDetail>();
	String factory_str = "";
	if (factoryId != null) {
		factory_str = String.valueOf(factoryId);
		resultlist = (List<ProduceBillDetail>)request.getAttribute("resultlist");
		
	}
	if(resultlist == null){
		resultlist = new ArrayList<ProduceBillDetail>();
	}
	if (factoryId == null) {
		factoryId = -1;
	}
	Integer year = (Integer) request.getAttribute("year");
	String year_str = "";
	if (year != null) {
		year_str = String.valueOf(year);
	}
	
	
	
	
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
		<script src="<%=basePath%>js/plugins/WdatePicker.js"
			type="text/javascript"></script>
		<script src="js/plugins/bootstrap.min.js" type="text/javascript"></script>
		<script src="js/common/common.js" type="text/javascript"></script>
		<script src="js/financial/producebill/add.js" type="text/javascript"></script>
		<script type='text/javascript' src='js/plugins/select2.min.js'></script>
		<link rel="stylesheet" type="text/css" href="css/plugins/select2.min.css" />
		<link href="css/store_in_out/index.css" rel="stylesheet" type="text/css" />
		<style type="text/css">
		.checkBtn{width: 20px;height: 20px;}
		#scanTip.success{background-color: #47a447;color: #fff;}
		#scanTip.fail{background-color: #d2322d;;color: #fff;}
		#contentTb input[disabled]{background: #ccc;}
		.table tbody tr.scanselected{background: #eea236;}
		</style>
	</head>

	<body>
		<%@ include file="../../common/head.jsp"%>
	
		<div id="Content" class="auto_container">
			<div id="main">
				<div class="breadcrumbs" id="breadcrumbs">
					<ul class="breadcrumb">
						<li>
							<i class="fa fa-home"></i>
							<a href="user/index">首页</a>
						</li>
						<li>
							<a href="producebill/index">生产对账单列表</a>
						</li>
						<li class="active">
							创建生产对账单
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 tablewidget">
								<!-- Table -->
									<form class="form-horizontal form-inline" role="form" id="postform">
										<%if(factoryId == null || factoryId <= 0){ %>
										<p class="alert alert-danger">请先选择 【加工方】</p>
										<%}%>
										<button class="btn btn-danger pull-right" type="submit">
											生成对账单
										</button>
										<div class="form-group salesgroup">
											<label for="factoryId" class="col-sm-3 control-label">
												加工方
											</label>
											<div class="col-sm-8">
												<select class="form-control" name="factoryId" id="factoryId">
													<option value="">
														未选择
													</option>
													<%
														for (Factory factory : SystemCache.produce_factorylist) {
														if (factoryId != null && factoryId == factory.getId()) {
													%>
													<option value="<%=factory.getId()%>" selected><%=factory.getName()%></option>
													<%
														} else {
													%>
													<option value="<%=factory.getId()%>"><%=factory.getName()%></option>
													<%
														}
																																				}
													%>
												</select>
											</div>
										</div>
										
										
										<div class="form-group salesgroup">
											<label class="col-sm-3 control-label">
												账单年份
											</label>

											<div class="input-group col-md-9">
												<input type="text" name="year" id="year"
													class="date form-control require" value="<%=year_str%>" />
												<span class="input-group-addon">年</span>
											</div>
										</div>
										
										</form>
							
								<form class="form-inline scanform" role="form" style="text-align: center;background: #ddd;padding-top: 3px;padding-bottom: 3px;">		
										<p id="scanTip"></p>					
										<div class="form-group">
											<label class="col-sm-3 control-label" style="width:200px;">
												生产单/工序加工单单号
											</label>
											 <div class="col-md-9"  style="width:300px;">
												<input type="text" name="number" id="number"
													class="form-control" />
											</div>
												<button class="btn btn-primary" type="submit">
													扫描
												</button>
										</div>
										
										</form>
								<table class="table table-responsive table-bordered" id="contentTb">
									<thead>
										<tr style="height:0;">
											<th style="width:40px"></th>
    										<th style="width:60px"></th>
    										<th style="width:50px"></th>
											<th style="width:60px"></th>
    										<th style="width:60px"></th>
    										<th style="width:60px"></th>
    										<th style="width:70px"></th>
    										<th style="width:55px"></th>
    										<th style="width:60px"></th>
    										<th style="width:55px"></th>
    										<th style="width:55px"></th>
    										<th style="width:70px"></th>
    										<th style="width:50px"></th>
    										<th style="width:60px"></th>
    										<th style="width:80px"></th>
    										<th style="width:80px"></th>
  										</tr>
										<tr>
											<th rowspan="2"  width="40px" style="padding: 0;">
												No.
											</th>
											<th rowspan="2" width="60px">
												加工单号
											</th><th rowspan="2" width="50px">
												工序
											</th><th rowspan="2" width="60px">
												开单日期
											</th><th rowspan="2" width="60px">
												订单号
											</th>
											<th rowspan="2" width="60px">
												货号
											</th><th rowspan="2" width="70px">
												款名
											</th>
											<th colspan="7" width="165px">加工明细</th>
											<th rowspan="2" width="80px">
												扣款
											</th><th rowspan="2" width="80px">
												备注
											</th>
										</tr><tr><th width="55px">
												颜色
											</th><th  width="60px">
												尺寸
											</th><th  width="55px">
												计划数
											</th><th  width="55px">
												入库数
											</th><th  width="70px">
												结账数量
											</th><th  width="50px">
												单价
											</th><th  width="60px">
												金额
											</th></tr>
									</thead>
									<tbody>
										<%
											if(resultlist.size()>0){
												int i = 0;
												for (ProduceBillDetail item : resultlist) {
													boolean even = i%2 == 0;
													String classname = even?"even":"odd";
													List<ProduceBillDetail_Detail> detailist = item.getDetaillist();
													if(detailist == null){
														detailist = new ArrayList<ProduceBillDetail_Detail>();
													}
													int detailsize = detailist.size();
													if(detailsize<=0){
														continue;
													}
										%>
										<tr number="<%=item.getProducingOrderNumber()%>" itemId="<%=item.getId()%>" class="producingTr tr EmptyTr disable <%=classname%>" data='<%=SerializeTool.serialize(detailist.get(0))%>' producebilldetail='<%=SerializeTool.serialize(item)%>' >
											<td rowspan="<%=detailsize%>"><%=++i%> <input type="checkbox" name="checked" class="checkBtn"/></td>
											<%if(item.getGongxuId() == SystemCache.producing_GONGXU.getId()){ %>
											<td rowspan="<%=detailsize%>"><a target="_blank" href="producing_order/detail/<%=item.getProducingOrderId()%>"><%=item.getProducingOrderNumber()%></a></td>
											<%}else{ %>
											<td rowspan="<%=detailsize%>"><a target="_blank" href="gongxu_producing_order/detail/<%=item.getProducingOrderId()%>"><%=item.getProducingOrderNumber()%></a></td>
											<%} %>
											
											<td rowspan="<%=detailsize%>"><%=SystemCache.getGongxuName(item.getGongxuId())%></td>
											<td rowspan="<%=detailsize%>"><%=DateTool.formatDateYMD(item.getProducingOrder_created_at())%></td>
											<td rowspan="<%=detailsize%>"><a target="_top" href="order/detail/<%=item.getOrderId()%>"><%=item.getOrderNumber()%></a></td>
											<td rowspan="<%=detailsize%>"><%=item.getCompany_productNumber()%></td>
											<td rowspan="<%=detailsize%>"><%=item.getName()%></td>
											
											
											<td><%=detailist.get(0).getColor()%></td>
											<td><%=detailist.get(0).getSize()%></td>
											<td><%=detailist.get(0).getPlan_quantity()%></td>
											<td><%=detailist.get(0).getQuantity()%></td>
											<td><input disabled class="quantity form-control require positive_int value"
															type="text" value="<%=Math.min(detailist.get(0).getQuantity(),detailist.get(0).getPlan_quantity())%>"></td>
											<%if(item.getGongxuId() == SystemCache.producing_GONGXU.getId()){ %>
											<td price="<%=detailist.get(0).getPrice()%>" class="price"><%=detailist.get(0).getPrice()%>/个</td>
											<%}else{ %>
											<td price="<%=detailist.get(0).getPrice()/12%>" class="price"><%=detailist.get(0).getPrice()%>/打</td>
											<%} %>
											
											<td class="amount"><%=detailist.get(0).getAmount()%></td>

											<td rowspan="<%=detailsize%>"><input disabled class="deduct form-control require double value"
															type="text" value=""></td>				
											<td rowspan="<%=detailsize%>"><input disabled class="memo form-control value"
															type="text" value=""></td>				
										</tr>
										<%
											if(detailist.size()>0){
												detailist.remove(0);
											}
											for(ProduceBillDetail_Detail detail : detailist){
										%>
										<tr number="<%=item.getProducingOrderNumber()%>" class="tr EmptyTr disable <%=classname %>" data='<%=SerializeTool.serialize(detail)%>'>
											<td><%=detail.getColor()%></td>
											<td><%=detail.getSize()%></td>
											<td><%=detail.getPlan_quantity()%></td>
											<td><%=detail.getQuantity()%></td>
											<td><input disabled class="quantity form-control require positive_int value"
															type="text" value="<%=Math.min(detail.getQuantity(),detail.getPlan_quantity())%>"></td>
											<%if(item.getGongxuId() == SystemCache.producing_GONGXU.getId()){ %>
											<td price="<%=detail.getPrice()%>" class="price"><%=detail.getPrice()%>/个</td>
											<%}else{ %>
											<td price="<%=detail.getPrice()/12%>" class="price"><%=detail.getPrice()%>/打</td>
											<%} %>
											<td class="amount"><%=detail.getAmount()%></td>

										</tr>
										<%} %>
										<%
											}
											}else{
										%>
											<tr><td colspan="16">没有相应的生产记录</td></tr>
										<%} %>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					
					
				</div>

			</div>
		</div>
		</div>
	</body>
</html>
