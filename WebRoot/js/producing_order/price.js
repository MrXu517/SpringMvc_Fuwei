$(document).ready(function(){
	//生产单
	var producingGrid = new OrderGrid({
		tipText:"划价",
		tipTextFunc:function(){return "划价";},
		url:"producing_order/price",
		$content:$(".body"),
		donecall:function(){
			$(".breadcrumb li.active").prev().children("a").click();
		},
		tbOptions:{
			colnames : [
					{
						name :'color',
						colname :'颜色',
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
						width :'15%',
						className:"input int"
					},
					{
						name :'price',
						colname :'价格(/个)',
						width :'15%'
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
							return "<a class='copyRow' href='#'>复制</a> | <a class='editRow' href='#'>修改</a> | <a class='deleteRow' href='#'>删除</a>";
						}
					} 
					],
			$dialog:$("#producingDetailDialog")
		}
		
	});
	
	$(".price").first().select().focus();
});