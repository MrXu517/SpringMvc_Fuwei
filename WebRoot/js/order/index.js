$(document).ready(function(){
	/*设置当前选中的页*/
	var $a = $("#left li a[href='order/index']");
	setActiveLeft($a.parent("li"));
	/*设置当前选中的页*/
	
	
	//取消订单 -- 开始
	$(".delete").click(function(){
		var id= $(this).attr("data-cid");
		if(!confirm("确定要取消该订单吗？")){
			return false;
		}
		$.ajax({
            url: "order/cancel/"+id,
            type: 'POST'
        })
            .done(function(result) {
            	if(result.success){
            		Common.Tip("取消订单成功",function(){
            			location.reload();
            		});
            	}
            })
            .fail(function(result) {
            	Common.Error("取消订单失败：" + result.responseText);
            })
            .always(function() {
            	
            });
		return false;
	});
	//取消订单 -- 结束
});
