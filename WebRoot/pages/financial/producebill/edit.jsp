<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%@page import="com.fuwei.entity.Factory"%>
<%@page import="com.fuwei.commons.Pager"%>
<%@page import="com.fuwei.entity.financial.ProduceBillDetail"%>
<%@page import="com.fuwei.entity.financial.ProduceBillDetail_Detail"%>
<%@page import="com.fuwei.util.DateTool"%>
<%@page import="com.fuwei.util.SerializeTool"%>
<%@page import="com.fuwei.entity.financial.ProduceBill"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	ProduceBill produceBill  = (ProduceBill) request.getAttribute("bill");
	List<ProduceBillDetail> detaillist = produceBill.getDetaillist()==null? new ArrayList<ProduceBillDetail>():produceBill.getDetaillist();
	//2.获取还未对账的单据
	List<ProduceBillDetail> resultlist = (List<ProduceBillDetail>) request.getAttribute("resultlist");
	if(resultlist == null){
		resultlist = new ArrayList<ProduceBillDetail>();
	}
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>编辑生产对账单 -- 桐庐富伟针织厂</title>
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
		<script src="js/financial/producebill/edit.js" type="text/javascript"></script>
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
							编辑生产对账单
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 tablewidget">
								<!-- Table -->
									<form class="form-horizontal form-inline" role="form" id="postform">	
										<input type="hidden" name="id" value="<%=produceBill.getId() %>" />
										<button class="btn btn-danger pull-right" type="submit">
											编辑对账单
										</button>
										<div class="form-group salesgroup">
											<label for="factoryId" class="col-sm-3 control-label">
												加工方
											</label>
											<div class="col-sm-8">
												<select class="form-control" name="factoryId" id="factoryId">
													<option value="<%=produceBill.getFactoryId()%>" selected><%=SystemCache.getFactoryName(produceBill.getFactoryId())%></option>
												</select>
											</div>
										</div>
										
										<div class="form-group salesgroup">
											<label class="col-sm-3 control-label">
												账单年份
											</label>

											<div class="input-group col-md-9">
												<input type="text" name="year" id="year"
													class="form-control require" value="<%=produceBill.getYear()%>" />
												<span class="input-group-addon">年</span>
											</div>
										</div>
										<br>
										<div class="form-group salesgroup" style="margin-top: 3px;margin-bottom: 3px;">
											<label class="col-sm-3 control-label">
												备&nbsp;&nbsp;&nbsp;&nbsp;注
											</label>

											<div class="col-md-9" style="width:460px;">
												<input type="text" name="memo" id="memo"
													class="form-control" value="<%=produceBill.getMemo()==null?"":produceBill.getMemo()%>" />
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
												计划数量
											</th><th  width="55px">
												入库总数
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
											int i = 0;
											for (ProduceBillDetail item : detaillist) {
												boolean even = i%2 == 0;
												String classname = even?"even":"odd";
												List<ProduceBillDetail_Detail> detailist = item.getDetaillist();
												int detailsize = item.getDetaillist().size();
										%>
										<tr number="<%=item.getProducingOrderNumber()%>" itemId="<%=item.getId()%>" class="producingTr tr  <%=classname%>" data='<%=SerializeTool.serialize(detailist.get(0))%>' producebilldetail='<%=SerializeTool.serialize(item)%>' >
											<td rowspan="<%=detailsize%>"><%=++i%> <input type="checkbox" name="checked" class="checkBtn" checked/></td>
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
											<td><%=detailist.get(0).getActual_in_quantity()%></td>
											<td><input class="quantity form-control require positive_int value"
															type="text" value="<%=detailist.get(0).getQuantity()%>"></td>
											<%if(item.getGongxuId() == SystemCache.producing_GONGXU.getId()){ %>
											<td price="<%=detailist.get(0).getPrice()%>" class="price"><%=detailist.get(0).getPrice()%>/个</td>
											<%}else{ %>
											<td price="<%=detailist.get(0).getPrice()/12%>" class="price"><%=detailist.get(0).getPrice()%>/打</td>
											<%} %>
											<td class="amount"><%=detailist.get(0).getAmount()%></td>

											<td rowspan="<%=detailsize%>"><input class="deduct form-control require double value"
															type="text" value="<%=item.getDeduct() %>"></td>				
											<td rowspan="<%=detailsize%>"><input class="memo form-control value"
															type="text" value="<%=item.getMemo()==null?"":item.getMemo() %>"></td>				
										</tr>
										<%
											detailist.remove(0);
											for(ProduceBillDetail_Detail detail : detailist){
										%>
										<tr number="<%=item.getProducingOrderNumber()%>" class="tr <%=classname %>" data='<%=SerializeTool.serialize(detail)%>'>
											<td><%=detail.getColor()%></td>
											<td><%=detail.getSize()%></td>
											<td><%=detail.getPlan_quantity()%></td>
											<td><%=detail.getActual_in_quantity()%></td>
											<td><input class="quantity form-control require positive_int value"
															type="text" value="<%=detail.getQuantity()%>"></td>
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
										%>
										
										<%
											for (ProduceBillDetail item : resultlist) {
												boolean even = i%2 == 0;
												String classname = even?"even":"odd";
												List<ProduceBillDetail_Detail> detailist = item.getDetaillist();
												int detailsize = item.getDetaillist().size();
										%>
										<tr number="<%=item.getProducingOrderNumber()%>" itemId="<%=item.getId()%>" class="producingTr tr EmptyTr disable  <%=classname%>" data='<%=SerializeTool.serialize(detailist.get(0))%>' producebilldetail='<%=SerializeTool.serialize(item)%>' >
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
											<td><%=detailist.get(0).getActual_in_quantity()%></td>
											<td><input disabled class="quantity form-control require positive_int value"
															type="text" value="<%=detailist.get(0).getQuantity()%>"></td>
											<%if(item.getGongxuId() == SystemCache.producing_GONGXU.getId()){ %>
											<td price="<%=detailist.get(0).getPrice()%>" class="price"><%=detailist.get(0).getPrice()%>/个</td>
											<%}else{ %>
											<td price="<%=detailist.get(0).getPrice()/12%>" class="price"><%=detailist.get(0).getPrice()%>/打</td>
											<%} %>
											<td class="amount"><%=detailist.get(0).getAmount()%></td>

											<td rowspan="<%=detailsize%>"><input disabled class="deduct form-control require double value"
															type="text" value="<%=item.getDeduct() %>"></td>				
											<td rowspan="<%=detailsize%>"><input disabled class="memo form-control value"
															type="text" value="<%=item.getMemo()==null?"":item.getMemo() %>"></td>				
										</tr>
										<%
											detailist.remove(0);
											for(ProduceBillDetail_Detail detail : detailist){
										%>
										<tr number="<%=item.getProducingOrderNumber()%>" class="tr <%=classname %>" data='<%=SerializeTool.serialize(detail)%>'>
											<td><%=detail.getColor()%></td>
											<td><%=detail.getSize()%></td>
											<td><%=detail.getPlan_quantity()%></td>
											<td><%=detail.getActual_in_quantity()%></td>
											<td><input disabled class="quantity form-control require positive_int value"
															type="text" value="<%=detail.getQuantity()%>"></td>
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
										%>
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
