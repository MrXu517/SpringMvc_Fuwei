$(document).ready(function(){
//	/*设置当前选中的页*/
//	var $a = $("#left li a[href='order/index']");
//	setActiveLeft($a.parent("li"));
//	/*设置当前选中的页*/
	
	//删除 -- 开始
	$(".delete").click(function(){
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
	
});
