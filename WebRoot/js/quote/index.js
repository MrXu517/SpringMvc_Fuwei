$(document).ready(function(){
	/*设置当前选中的页*/
	var $a = $("#left li a[href='quote/index']");
	setActiveLeft($a.parent("li"));
	/*设置当前选中的页*/
	
	//删除报价 -- 开始
	$(".delete").click(function(){
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "quote/delete/"+id,
            type: 'POST'
        })
            .done(function(result) {
            	if(result.success){
            		Common.Tip("删除成功",function(){
            			location.reload();
            		});
            	}else{
            		Common.Error("删除失败：" + result.message);
            	}
            })
            .fail(function(result) {
            	Common.Error("请求服务器过程中出错:" + result.responseText);
            })
            .always(function() {
            	
            });
		return false;
	});
	//删除报价 -- 结束
});
