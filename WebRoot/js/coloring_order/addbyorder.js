$(document).ready( function() {
	/* 设置当前选中的页 */
	var $a = $("#left li a[href='order/index']");
	setActiveLeft($a.parent("li"));
	/* 设置当前选中的页 */

		var materialPurchaseGrid = new OrderGrid({
			tipText:"染色单",
			url:"coloring_order/addbyorder",
			$content:$(".coloringorderWidget"),
			donecall:function(){
				$(".breadcrumb li.active").prev().children("a").click();
			},
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