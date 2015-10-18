$(document).ready(function() {
	/*设置当前选中的页*/
	var $a = $("#left li a[href='order/index']");
	setActiveLeft($a.parent("li"));
	/*设置当前选中的页*/
	var tabname = Common.urlParams().tab;
	if (tabname == null || tabname == undefined) {
		$('#tab>ul.nav a:first').tab('show') // Select first tab
	}
	$("#tab a[href='#" + tabname + "']").tab('show') // Select tab by name
	
	
	//2015-6-30添加 生产数量+15功能
		$(".plus15Btn").click(function(){
			var $quantityEle = $(this).closest("tr").find(".quantity");
			var quantity = $quantityEle.val();
			if(quantity == ""){
				return false;
			}
			quantity = Number(quantity);
			$quantityEle.val(quantity+15);
			return false;
		});
		//2015-6-30添加 生产数量+15功能
	
		 
	//2015-4-3添加打印表格时要勾选
	$(".printcheck").click(function(e){
		e.stopPropagation();
		//return false;
	});
	$(".printAll").click(function(){
		var href = $(this).attr("href");
		var $checkeds = $(".printcheck:checked").not("[disabled]");
		var gridName = "";
		$checkeds.each(function(){
			var tempgridname = $(this).parent("a[role='tab']").attr("href");
			if(tempgridname!=undefined){
				tempgridname = tempgridname.substring(1);
				if(tempgridname!=""){
					gridName += "," + tempgridname;
				}
			}
		});
		if(gridName != ""){
			gridName = gridName.substring(1);
			$("#printAlla").attr("href",href+"&gridName="+gridName);
			$("#printAlla span").click();
			return false;
		}else{
			alert("请至少选中一种单据");
			return false;
		}
	});
	//$("#printAlla").click(function(){return true;});
	//2015-4-3添加打印表格时要勾选
	
	//2015-4-4添加 生产单 请求划价 、完成划价功能
	$(".priceRequestBtn").click(function(){
		$button = $(this);
		var orderId = $(this).attr("orderid");
		if(orderId == undefined || orderId == null || orderId == ""){
			Common.Error("缺少订单ID");
			return false;
		}
		var orderNumber = $(this).attr("ordernumber");
		if(orderNumber == undefined || orderNumber == null || orderNumber == ""){
			Common.Error("缺少订单号");
			return false;
		}
		$button.button('loading');
		$.ajax( {
			url :"producing_order/price_request/"+orderId,
			type :'POST',
			data :{orderNumber:orderNumber},
			success : function(result) {
				if (result.success) {
					Common.Tip("请求划价成功", function() {
						
					});
				}
				$button.button('reset');
			},
			error : function(result) {
				Common.Error("请求划价失败：" + result.responseText);
				$button.button('reset');
			}
		});
	});
	
	
	//2015-4-4添加 生产单 请求划价、完成划价功能
	
	
	//质量记录单
	var headBankGrid = new OrderGrid({
		url:"order/headbank",
		$content:$("#headbankorder")
	});

	//生产单
	var $producingorderTab = $("#producingorder .producingorderWidget");
	$producingorderTab.each(function(){
		var $content = $(this);
		var coloringGrid = new OrderGrid({
			url:"producing_order/put",
			deleteUrl:"producing_order/delete",
			$content:$content,
			donecall:function(){
				var params = Common.urlParams();
				params.tab = "producingorder";
				location.href = location.pathname + "?" + $.param(params);
			},
			
		});
	});
	
	//2015-10-18添加工序加工单
	var $gongxuproducingorderTab = $("#gongxuproduceorder .gongxuproduceorderWidget");
	$gongxuproducingorderTab.each(function(){
		var $content = $(this);
		var coloringGrid = new OrderGrid({
			url:"gongxu_producing_order/put",
			deleteUrl:"gongxu_producing_order/delete",
			$content:$content,
			donecall:function(){
				var params = Common.urlParams();
				params.tab = "gongxuproduceorder";
				location.href = location.pathname + "?" + $.param(params);
			},
			
		});
	});
	
	//计划单
	var planGrid = new OrderGrid({
		url:"order/planorder",
		$content:$("#planorder"),
		donecall:function(){
			var params = Common.urlParams();
			params.tab = "planorder";
			location.href = location.pathname + "?" + $.param(params);
		},
		tbOptions:{
			colnames : [
					{
						name :'color',
						colname :'颜色',
						width :'15%'
					},
					{
						name :'weight',
						colname :'克重(g)',
						width :'15%'
					},
					{
						name :'produce_weight',
						colname :'机织克重(g)',
						width :'15%'
					},
					{
						name :'yarn_name',
						colname :'纱线种类',
						width :'15%'
					},
					{
						name :'size',
						colname :'尺寸',
						width :'15%'
					},
					{
						name :'quantity',
						colname :'生产数量',
						width :'15%'
					}],
					$dialog:$("#planDialog")
		}
	});
	//2015-6-30添加  查看状态下，去掉【保存、修改单据的元素】
	var $SubmitButton = $("#planorder button.saveTable[type='submit']");
	if($SubmitButton.length<=0){
		 $("#planorder [save-widget='true']").remove();
	}
	//2015-6-30添加  查看状态下，去掉【保存、修改单据的元素】
	
	
	//原材料仓库单
	var storeGrid = new OrderGrid({
		url:"order/storeorder",
		$content:$("#storeorder"),
		donecall:function(){
			var params = Common.urlParams();
			params.tab = "storeorder";
			location.href = location.pathname + "?" + $.param(params);
		},
		tbOptions:{
			colnames : [
					{
						name :'color',
						colname :'颜色',
						width :'15%'
					},
					{
						name :'material_name',
						colname :'材料',
						width :'15%'
					},
					{
						name :'quantity',
						colname :'总数量',
						width :'15%'
					},
					{
						name :'factory_name',
						colname :'领取人',
						width :'15%'
					},
					{
						name :'yarn',
						colname :'标准样纱',
						width :'15%'
					},
					{
						name :'_handle',
						colname :'操作',
						width :'15%',
						displayValue : function(value, rowdata) {
							return "<a class='copyRow' href='#'>复制</a> | <a class='editRow' href='#'>修改</a> | <a class='deleteRow' href='#'>删除</a>";
						}
					} ],
					$dialog:$("#storeDialog")
		}
		
	});
	
	//2015-6-30添加  查看状态下，去掉【保存、修改单据的元素】
	var $SubmitButton = $("#storeorder button.saveTable[type='submit']");
	if($SubmitButton.length<=0){
		 $("#storeorder [save-widget='true']").remove();
	}
	//2015-6-30添加  查看状态下，去掉【保存、修改单据的元素】
	
	
	
	//半检记录单
	var halfcheckrecordGrid = new OrderGrid({
		url:"order/halfcheckrecordorder",
		$content:$("#halfcheckrecordorder"),
		donecall:function(){
			var params = Common.urlParams();
			params.tab = "halfcheckrecordorder";
			location.href = location.pathname + "?" + $.param(params);
		},
		tbOptions2:{
			colnames : [
			        {
			        	name :'material_name',
			        	colname :'材料',
			        	width :'20%'
			        },
					{
						name :'color',
						colname :'色号',
						width :'20%'
					},
					{
						name :'colorsample',
						colname :'标准色样',
						width :'25%'
					},
					{
						name :'_handle',
						colname :'操作',
						width :'15%',
						displayValue : function(value, rowdata) {
							return "<a class='copyRow' href='#'>复制</a> | <a class='editRow' href='#'>修改</a> | <a class='deleteRow' href='#'>删除</a>";
						}
					} 
					],
			$dialog:$("#halfcheckrecordDialog2")
		}
		
	});
	//2015-6-30添加  查看状态下，去掉【保存、修改单据的元素】
	var $SubmitButton = $("#halfcheckrecordorder button.saveTable[type='submit']");
	if($SubmitButton.length<=0){
		 $("#halfcheckrecordorder [save-widget='true']").remove();
	}
	//2015-6-30添加  查看状态下，去掉【保存、修改单据的元素】
	
	
	//原材料采购单
	var $materialpurchaseorderTab = $("#materialpurchaseorder .materialorderWidget");
	$materialpurchaseorderTab.each(function(){
		var $content = $(this);
		var materialPurchaseGrid = new OrderGrid({
			url:"material_purchase_order/put",
			deleteUrl:"material_purchase_order/delete",
			$content:$content,
			donecall:function(){
				var params = Common.urlParams();
				params.tab = "materialpurchaseorder";
				location.href = location.pathname + "?" + $.param(params);
			},
			
		});
	});
	
	
	//染色单
	var $coloringorderTab = $("#coloringorder .coloringorderWidget");
	$coloringorderTab.each(function(){
		var $content = $(this);
		var coloringGrid = new OrderGrid({
			url:"coloring_order/put",
			deleteUrl:"coloring_order/delete",
			$content:$content,
			donecall:function(){
				var params = Common.urlParams();
				params.tab = "coloringorder";
				location.href = location.pathname + "?" + $.param(params);
			},
			
		});
	});
	
	
	//抽检记录单
	var checkRecordGrid = new OrderGrid({
		url:"order/checkrecordorder",
		$content:$("#checkrecordorder"),		
	});
	
	//辅料采购单
	var $fuliaoorderTab = $("#fuliaopurchaseorder .fuliaoorderWidget");
	$fuliaoorderTab.each(function(){
		var $content = $(this);
		var fuliaoPurchaseGrid = new OrderGrid({
			url:"fuliao_purchase_order/put",
			deleteUrl:"fuliao_purchase_order/delete",
			$content:$content,
			donecall:function(){
				var params = Common.urlParams();
				params.tab = "fuliaopurchaseorder";
				location.href = location.pathname + "?" + $.param(params);
			},
		});
	});
	
	
	//车缝记录单
	var carfixRecordGrid = new OrderGrid({
		url:"order/carfixrecordorder",
		$content:$("#carfixrecordorder"),
	});
	
	//整烫记录单
	var ironingRecordGrid = new OrderGrid({
		url:"order/ironingrecordorder",
		$content:$("#ironingrecordorder"),
	});
	
	//2015-3-23添加新表格
	//生产进度单
	var productionScheduleGrid = new OrderGrid({
		$content:$("#productionscheduleorder")
	});
	
	//成品仓库记录单
	var finalStoreGrid = new OrderGrid({
		$content:$("#finalstorerecordorder")
	});
	//2015-5-12添加新表格 成品检验记录单
	var finalCheckRecordGrid = new OrderGrid({
		$content:$("#finalcheckrecordorder")
	});
	//2015-5-23添加新表格 检针记录表
	var needleCheckRecordGrid = new OrderGrid({
		$content:$("#needlecheckrecordorder")
	});
	
	//车间记录单
	var shopRecordGrid = new OrderGrid({
		$content:$("#shoprecordorder")
	});
	
	//染色进度单
	var coloringProcessGrid = new OrderGrid({
		$content:$("#coloringprocessorder")
	});
	
	$("#createProducingorderBtn").click(function(){
		
	});
});