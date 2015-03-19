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
	
	
	//质量记录单
	var headBankGrid = new OrderGrid({
		url:"order/headbank",
		$content:$("#headbankorder")
	});

	//生产单
	var $producingorderTab = $("#producingorder .producingorderWidget");
	$producingorderTab.each(function(){
		var $content = $(this);
		var producingGrid = new OrderGrid({
			url:"order/producingorder",
			$content:$content,
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
							width :'15%',
							className:"int input"
						},
						{
							name :'price',
							colname :'价格(/个)',
							width :'15%',
							className:"double input"
						}],
						$dialog:$("#producingDialog")
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
							name :'quantity',
							colname :'数量',
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
								return "<a class='editRow' href='#'>修改</a> | <a class='deleteRow' href='#'>删除</a>";
							}
						} 
						],
				$dialog:$("#producingDetailDialog")
			}
			
		});
	});

	
	//计划单
	var planGrid = new OrderGrid({
		url:"order/planorder",
		$content:$("#planorder"),
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
	
	//原材料仓库单
	var storeGrid = new OrderGrid({
		url:"order/storeorder",
		$content:$("#storeorder"),
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
							return "<a class='editRow' href='#'>修改</a> | <a class='deleteRow' href='#'>删除</a>";
						}
					} ],
					$dialog:$("#storeDialog")
		}
		
	});
	
	//半检记录单
	var halfcheckrecordGrid = new OrderGrid({
		url:"order/halfcheckrecordorder",
		$content:$("#halfcheckrecordorder"),
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
							return "<a class='editRow' href='#'>修改</a> | <a class='deleteRow' href='#'>删除</a>";
						}
					} 
					],
			$dialog:$("#halfcheckrecordDialog2")
		}
		
	});
	
	//原材料采购单
	var $materialpurchaseorderTab = $("#materialpurchaseorder .materialorderWidget");
	$materialpurchaseorderTab.each(function(){
		var $content = $(this);
		var materialPurchaseGrid = new OrderGrid({
			url:"order/materialpurchaseorder",
			$content:$content,
			tbOptions:{
				colnames : [
						{
							name :'material_name',
							colname :'材料品种',
							width :'15%'
						},
						{
							name :'scale',
							colname :'规格',
							width :'15%'
						},
						{
							name :'quantity',
							colname :'数量',
							width :'15%'
						},
						{
							name :'batch_number',
							colname :'批次号',
							width :'15%'
						},
						{
							name :'price',
							colname :'价格（含税）',
							width :'15%'
						},
						{
							name :'_handle',
							colname :'操作',
							width :'15%',
							displayValue : function(value, rowdata) {
								return "<a class='editRow' href='#'>修改</a> | <a class='deleteRow' href='#'>删除</a>";
							}
						} ],
						$dialog:$("#materialpurchaseDialog")
			}
			
		});
	});
	
	
	//染色单
	var $coloringorderTab = $("#coloringorder .coloringorderWidget");
	$coloringorderTab.each(function(){
		var $content = $(this);
		var coloringGrid = new OrderGrid({
			url:"order/coloringorder",
			$content:$content,
			tbOptions:{
				colnames : [
				        {
				        	name :'color',
				        	colname :'色号',
				        	width :'15%'
				        },
						{
							name :'material_name',
							colname :'材料',
							width :'15%'
						},
						{
							name :'quantity',
							colname :'数量',
							width :'15%'
						},
						
						{
							name :'standardyarn',
							colname :'标准样纱',
							width :'15%'
						},
						{
							name :'_handle',
							colname :'操作',
							width :'15%',
							displayValue : function(value, rowdata) {
								return "<a class='editRow' href='#'>修改</a> | <a class='deleteRow' href='#'>删除</a>";
							}
						} ],
						$dialog:$("#coloringDialog")
			}
			
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
			url:"order/fuliaopurchaseorder",
			$content:$content,
			tbOptions:{
			colnames : [
						{
							name :'style_name',
							colname :'辅料类型',
							width :'15%'
						},
						{
							name :'standardsample',
							colname :'标准样',
							width :'15%'
						},
						{
							name :'quantity',
							colname :'数量',
							width :'15%'
						},
						{
							name :'price',
							colname :'价格(/个)',
							width :'15%'
						},
						{
							name :'end_at',
							colname :'交期',
							width :'15%'
						},
						{
							name :'_handle',
							colname :'操作',
							width :'15%',
							displayValue : function(value, rowdata) {
								return "<a class='editRow' href='#'>修改</a> | <a class='deleteRow' href='#'>删除</a>";
							}
						} ],
						$dialog:$("#fuliaopurchaseDialog"),
			}
			
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
	
	$("#createProducingorderBtn").click(function(){
		
	});
});