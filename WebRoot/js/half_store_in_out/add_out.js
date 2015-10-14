$(document).ready( function() {
	/*设置当前选中的页*/
	var $a = $("#left li a[href='workspace/half_workspace']");
	setActiveLeft($a.parent("li"));
	/* 设置当前选中的页 */
	
	
	//2015-4-3 添加自动focus到第一个可输入input、select
	$("form").find("input[type='text'],select").not("[readonly],[disabled]").first().focus();
	//2015-4-3 添加自动focus到第一个可输入input、select

	$(".quantity").bind("blur",function(){
		if(this.value == ""){
			this.value = 0 ;
		}
	});
	$(".quantity").click(function(){
		$(this).focus();
		$(this).select();
	});
		var storInGrid = new OrderGrid({
			tipText:"半成品出库单",
			url:"half_store_out/add",
			postUrl:"half_store_out/add",
			$content:$(".body"),
			donecall:function(result){
				location.href = "half_store_out/detail/" + result.id;
			},
			tbOptions:{
				beforeAdd:function(){
					var TableInstance = this;
					var length = $(TableInstance.tableEle).find("tbody tr").length;
					if(length >= 6){
						Common.Tip("不能再添加行，一张出库单最多只能填6条材料信息。您可以保存当前出库单后，再创建一张出库单");
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
		
		
		$("#factoryId").focus();
	});