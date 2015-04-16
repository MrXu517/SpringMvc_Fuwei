$(document).ready( function() {
	/* 设置当前选中的页 */
	var $a = $("#left li a[href='order/index']");
	setActiveLeft($a.parent("li"));
	/* 设置当前选中的页 */
	
	//2015-4-3 添加自动focus到第一个可输入input、select
	$("form").find("input[type='text'],select").not("[readonly],[disabled]").first().focus();
	//2015-4-3 添加自动focus到第一个可输入input、select

		var materialPurchaseGrid = new OrderGrid({
			tipText:"染色单",
			url:"coloring_order/addbyorder",
			$content:$(".coloringorderWidget"),
			donecall:function(){
				$(".breadcrumb li.active").prev().children("a").click();
			},
			tbOptions:{
				beforeAdd:function(){
					var TableInstance = this;
					var length = $(TableInstance.tableEle).find("tbody tr").length;
					if(length >= 6){
						Common.Tip("不能再添加行，一张染色单最多只能填6条材料信息。您可以保存当前染色单后，再创建一张染色单");
						return false;
					}
					return true;
				},
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
								return "<a class='copyRow' href='#'>复制</a> | <a class='editRow' href='#'>修改</a> | <a class='deleteRow' href='#'>删除</a>";
							}
						} ],
						$dialog:$("#coloringDialog")
			}
			
		});
		
	});