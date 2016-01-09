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
	
	
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>生产对账单详情 -- 桐庐富伟针织厂</title>
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
		<link href="css/store_in_out/index.css" rel="stylesheet" type="text/css" />
		<style type="text/css">
		.table>tfoot>tr {background: #AEADAD;}
		.table>tfoot>tr>td{border-color: #000;border-bottom-width: 1px;word-break: break-all;padding: 8px 0;font-weight: bold;}
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
							生产对账单详情
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid">
						<div class="row">
							<div class="col-md-12 tablewidget">
								<!-- Table -->
									<a data-cid="<%=produceBill.getId() %>" href="producebill/export/<%=produceBill.getId() %>" class="exportBtn btn btn-primary" type="button">
											导出
									</a>
									<a href="producebill/put/<%=produceBill.getId() %>" class="btn btn-warning" type="button" style="margin-left:30px;">
											编辑
									</a>
									<a data-cid="<%=produceBill.getId() %>" class="deleteBtn btn btn-danger pull-right" type="button">
											删除
									</a>
									<form class="form-horizontal form-inline" role="form" id="postform">
										
										<div class="form-group salesgroup" style="width: 200px;">
											<label for="factoryId" class="col-sm-3 control-label">
												加工方 : <%=SystemCache.getFactoryName(produceBill.getFactoryId()) %>
											</label>
											
										</div>
										
										
										<div class="form-group salesgroup" style="width: 200px;">
											<label class="col-sm-3 control-label">
												账单年份: <%=produceBill.getYear() %>
											</label>
										</div>
										
										</form>
								<table class="table table-responsive table-bordered" id="contentTb">
									<thead>
										<tr style="height:0;">
											<th style="width:20px"></th>
    										<th style="width:60px"></th>
    										<th style="width:40px"></th>
											<th style="width:60px"></th>
    										<th style="width:60px"></th>
    										<th style="width:40px"></th>
    										<th style="width:55px"></th>
    										<th style="width:60px"></th>
    										<th style="width:70px"></th>
    										<th style="width:55px"></th>
    										<th style="width:60px"></th>
    										<th style="width:65px"></th>
    										<th style="width:70px"></th>
    										<th style="width:45px"></th>
    										<th style="width:60px"></th>
    										<th style="width:60px"></th>
    										<th style="width:80px"></th>
  										</tr>
										<tr>
											<th rowspan="2"  width="20px" style="padding: 0;">
												No.
											</th>
											<th rowspan="2" width="60px">
												加工单号
											</th><th rowspan="2" width="40px">
												工序
											</th><th rowspan="2" width="60px">
												开单日期
											</th><th rowspan="2" width="60px">
												订单号
											</th><th rowspan="2" width="40px">
												公司
											</th><th rowspan="2" width="55px">
												跟单人
											</th>
											
											<th rowspan="2" width="60px">
												货号
											</th><th rowspan="2" width="70px">
												款名
											</th>
											<th colspan="6" width="165px">加工明细</th>
											<th rowspan="2" width="60px">
												扣款
											</th><th rowspan="2" width="80px">
												备注
											</th>
										</tr><tr><th width="55px">
												颜色
											</th><th  width="60px">
												尺寸
											</th><th  width="65px">
												计划数量
											</th><th  width="70px">
												结账数量
											</th><th  width="45px">
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
										<tr number="<%=item.getProducingOrderNumber()%>" itemId="<%=item.getId()%>" class="producingTr tr EmptyTr disable <%=classname%>" data='<%=SerializeTool.serialize(detailist.get(0))%>' producebilldetail='<%=SerializeTool.serialize(item)%>' >
											<td rowspan="<%=detailsize%>"><%=++i%></td>
											<%if(item.getGongxuId() == SystemCache.producing_GONGXU.getId()){ %>
											<td rowspan="<%=detailsize%>"><a target="_blank" href="producing_order/detail/<%=item.getProducingOrderId()%>"><%=item.getProducingOrderNumber()%></a></td>
											<%}else{ %>
											<td rowspan="<%=detailsize%>"><a target="_blank" href="gongxu_producing_order/detail/<%=item.getProducingOrderId()%>"><%=item.getProducingOrderNumber()%></a></td>
											<%} %>
											
											<td rowspan="<%=detailsize%>"><%=SystemCache.getGongxuName(item.getGongxuId())%></td>
											<td rowspan="<%=detailsize%>"><%=DateTool.formatDateYMD(item.getProducingOrder_created_at())%></td>
											<td rowspan="<%=detailsize%>"><a target="_blank" href="order/detail/<%=item.getOrderId()%>"><%=item.getOrderNumber()%></a></td>
											<td rowspan="<%=detailsize%>"><%=SystemCache.getCompanyShortName(item.getCompanyId())%></td>
											<td rowspan="<%=detailsize%>"><%=SystemCache.getEmployeeName(item.getCharge_employee())%></td>
											<td rowspan="<%=detailsize%>"><%=item.getCompany_productNumber()%></td>
											<td rowspan="<%=detailsize%>"><%=item.getName()%></td>
											
											
											<td><%=detailist.get(0).getColor()%></td>
											<td><%=detailist.get(0).getSize()%></td>
											<td><%=detailist.get(0).getPlan_quantity()%></td>
											<td><%=detailist.get(0).getQuantity()%></td>
											<%if(item.getGongxuId() == SystemCache.producing_GONGXU.getId()){ %>
											<td price="<%=detailist.get(0).getPrice()%>" class="price"><%=detailist.get(0).getPrice()%>/个</td>
											<%}else{ %>
											<td price="<%=detailist.get(0).getPrice()/12%>" class="price"><%=detailist.get(0).getPrice()%>/打</td>
											<%} %>
											<td class="amount"><%=detailist.get(0).getAmount()%></td>

											<td rowspan="<%=detailsize%>"><%=item.getDeduct()%></td>				
											<td rowspan="<%=detailsize%>"><%=item.getMemo() == null ? "" :item.getMemo()%></td>				
										</tr>
										<%
											detailist.remove(0);
											for(ProduceBillDetail_Detail detail : detailist){
										%>
										<tr number="<%=item.getProducingOrderNumber()%>" class="tr EmptyTr disable <%=classname %>" data='<%=SerializeTool.serialize(detail)%>'>
											<td><%=detail.getColor()%></td>
											<td><%=detail.getSize()%></td>
											<td><%=detail.getPlan_quantity()%></td>
											<td><%=detail.getQuantity()%></td>
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
									<tfoot><tr><td colspan="2">合计</td>
									<td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td>
									<td><%=produceBill.getQuantity() %></td><td></td><td><%=produceBill.getAmount() %></td>
									<td><%=produceBill.getDeduct() %></td><td></td></tr>
									<tr><td colspan="17" style="text-align: left;padding-left: 30px;">总金额（减去扣款）： <%=produceBill.getAmount() - produceBill.getDeduct() %></td></tr>
									<tr><td colspan="17" style="text-align: left;padding-left: 30px;">地税扣款： <%=produceBill.getRate_deduct() %></td></tr>
									<tr><td colspan="17" style="text-align: left;padding-left: 30px;">合计应付金额： <%=produceBill.getPayable_amount() %></td></tr>
									</tfoot>
								</table>
							</div>
						</div>
					</div>
					
					
				</div>

			</div>
		</div>
		</div>
	<script type="text/javascript">
			/*设置当前选中的页*/
	var $a = $("#left li a[href='producebill/index']");
	setActiveLeft($a.parent("li"));
		//删除 -- 开始
	$(".deleteBtn").click(function(){
		var id= $(this).attr("data-cid");
		if(!confirm("确定要删除该生产对账单吗？")){
			return false;
		}
		$.ajax({
            url: "producebill/delete/"+id,
            type: 'POST'
        })
            .done(function(result) {
            	if(result.success){
            		top.Common.Tip("删除成功",function(){
            			location.href="producebill/index";
            		});
            	}
            })
            .fail(function(result) {
            	top.Common.Error("删除失败：" + result.responseText);
            })
            .always(function() {
            });
		return false;
	});
	//删除 -- 结束
	
		</script>
	</body>
</html>
