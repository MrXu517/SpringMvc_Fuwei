$(document).ready( function() {
	/* 设置当前选中的页 */
	/*设置当前选中的页*/
	var $a = $("#left li a[href='workspace/material_workspace']");
	setActiveLeft($a.parent("li"));
	/* 设置当前选中的页 */
	$("#factoryId").focus();
	$("#factoryId").change(function(){
		var params = Common.urlParams();
		params.factoryId = this.value;
		location.href = location.pathname + "?" + $.param(params);
	});
	$(".quantity").bind("blur",function(){
		if(this.value == ""){
			this.value = 0 ;
		}
	});
	$(".quantity").click(function(){
		$(this).focus();
		$(this).select();
	});
	
	//2015-4-3 添加自动focus到第一个可输入input、select
	$("form").find(".quantity").not("[readonly],[disabled]").first().click();
	//2015-4-3 添加自动focus到第一个可输入input、select
		var storInGrid = new OrderGrid({
			tipText:"原材料退货单",
			url:"store_return/add",
			postUrl:"store_return/add",
			$content:$(".body"),
			donecall:function(result){
				Common.Tip("请打印原材料退货单", function() {
					location.href = "store_return/detail/" + result.id;
				});
			},
			tbOptions:{
				beforeAdd:function(){
					var TableInstance = this;
					var length = $(TableInstance.tableEle).find("tbody tr").length;
					if(length >= 6){
						Common.Tip("不能再添加行，一张退货单最多只能填6条材料信息。您可以保存当前退货单后，再创建一张退货单");
						return false;
					}
					return true;
				},
				colnames : [
						{
							name :'quantity',
							colname :'数量',
							width :'15%'
						}],
						$dialog:$("#storeDialog")
			}
			
		});
		
	});