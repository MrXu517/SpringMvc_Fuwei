$(document).ready(function(){
	/* 设置当前选中的页 */
	var $a = $("#left li a[href='gongxu_producing_order/scan']");
	setActiveLeft($a.parent("li"));
	/* 设置当前选中的页 */
	//工序加工单
	var producingGrid = new OrderGrid({
		tipText:"编辑工序加工单单价",
		url:"gongxu_producing_order/editprice",
		$content:$(".body"),
		donecall:function(){
			$(".breadcrumb li.active").prev().children("a").click();
		},
		tbOptions:{
			colnames : [
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