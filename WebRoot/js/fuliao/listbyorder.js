$(document).ready(function(){
//	/*设置当前选中的页*/
//	var $a = $("#left li a[href='order/index']");
//	setActiveLeft($a.parent("li"));
//	/*设置当前选中的页*/
	
	//删除 -- 开始
	$(".delete").click(function(){
		if(!confirm("确定要删除该辅料吗？")){
			return false;
		}
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "fuliao/delete/"+id,
            type: 'POST'
        })
            .done(function(result) {
            	if(result.success){
            		top.Common.Tip("删除成功",function(){
            			location.reload();
            		});
            	}
            })
            .fail(function(result) {
            	top.Common.Error("删除失败：" + result.responseText);
            })
            .always(function() {
            });
		return false;
	});
	//删除 -- 结束
	
	//全选
	$("#checkAll").click(function(){
		var checked = this.checked;
		$(".checkbtn").prop("checked",checked);
		$(".checkbtn").change();
	});
	$(".checkbtn").change(function(){
		var checked = this.checked;
		if(checked){
			$(this).closest("tr").addClass("selected");
		}else{
			$(this).closest("tr").removeClass("selected");
		}
	});
//	$("#cardBtn").click(function(){
//		var orderId = $(this).attr("orderId");
//		var $checkedOrders = $(".checkbtn:checked");
//		if($checkedOrders.length<=0){
//			Common.Error("打印辅料卡时，请至少选择一种辅料");
//			return false;
//		}
//		var ids = "";
//		for(var i = 0 ; i < $checkedOrders.length;++i){
//			var tempfuliaoId = $($checkedOrders[i]).attr("fuliaoId");
//			ids+=tempfuliaoId+",";
//		}
//		ids = ids.substring(0,ids.length-1);
//		location.href="fuliao/card/"+orderId+"?ids="+ids;
//	});
	
});

function card(){
	var $card=$("#cardBtn");
	var $checkedOrders = $(".checkbtn:checked");
	if($checkedOrders.length<=0){
		var old_href= $card.attr("href");
		if(old_href.indexOf("?")>0){
			old_href = old_href.substr(0,old_href.indexOf("?"));
		}
		$card.attr("href",old_href);
		top.Common.Error("打印辅料卡时，请至少选择一种辅料");
		return false;
	}
	var ids = "";
	for(var i = 0 ; i < $checkedOrders.length;++i){
		var tempfuliaoId = $($checkedOrders[i]).attr("fuliaoId");
		ids+=tempfuliaoId+",";
	}
	ids = ids.substring(0,ids.length-1);
	
	var old_href= $card.attr("href");
	if(old_href.indexOf("?")>0){
		old_href = old_href.substr(0,old_href.indexOf("?"));
	}
	old_href = old_href + "?ids=" + ids;
	$card.attr("href",old_href);
	return true;
}