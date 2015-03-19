$(document).ready( function() {
	/* 设置当前选中的页 */
	var $a = $("#left li a[href='order/index']");
	setActiveLeft($a.parent("li"));
	/* 设置当前选中的页 */

		var materialPurchaseGrid = new OrderGrid({
			tipText:"原材料采购单",
			url:"material_purchase_order/addbyorder",
			$content:$(".materialorderWidget"),
			donecall:function(){
				$(".breadcrumb li.active").prev().children("a").click();
			},
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