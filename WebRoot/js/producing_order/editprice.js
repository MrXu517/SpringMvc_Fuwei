$(document).ready(function(){
	$("#factoryId").select2();
//生产单
	var producingGrid = new OrderGrid({
		tipText:"生产单",
		url:"producing_order/editprice",
		$content:$(".body"),
		donecall:function(){
			$(".breadcrumb li.active").prev().children("a").click();
		},
		tbOptions:{
			colnames : [
					{
						name :'quantity',
						colname :'生产数量',
						width :'15%',
						className:"input int"
					},
					{
						name :'price',
						colname :'价格(/个)',
						width :'15%'
					}],
					$dialog:$("#producingDialog")
		}
	});
	
	//2015-4-3 添加自动focus到第一个可输入input、select
	$("form").find("input[type='text'],select").not("[readonly],[disabled]").first().focus();
	//2015-4-3 添加自动focus到第一个可输入input、select
});