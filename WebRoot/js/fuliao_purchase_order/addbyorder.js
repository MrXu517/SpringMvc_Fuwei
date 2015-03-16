$(document).ready( function() {
	/* 设置当前选中的页 */
	var $a = $("#left li a[href='order/index']");
	setActiveLeft($a.parent("li"));
	/* 设置当前选中的页 */

		var fuliaoPurchaseGrid = new OrderGrid({
			tipText:"辅料采购单",
			url:"fuliao_purchase_order/addbyorder",
			$content:$(".fuliaoorderWidget"),
			donecall:function(){
				$(".breadcrumb li.active").prev().children("a").click();
			},
			tbOptions:{
				colnames : [
							{
								name :'style',
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
							$dialog:$("#fuliaopurchaseDialog")
				}
			
		});
		
	});