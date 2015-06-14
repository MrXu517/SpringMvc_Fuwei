$(document).ready( function() {
	/* 设置当前选中的页 */
	var $a = $("#left li a[href='financial/workspace']");
	setActiveLeft($a.parent("li"));
	/* 设置当前选中的页 */
	
	$(".deleteMatch").click(function(){
		var id= $(this).attr("data-cid");
		if(!confirm("确定要取消此次收发票吗？")){
			return false;
		}
		$.ajax({
            url: "expense_income_invoice/delete/"+id,
            type: 'POST'
        })
            .done(function(result) {
            	if(result.success){
            		Common.Tip("操作成功",function(){
            			location.reload();
            		});
            	}
            })
            .fail(function(result) {
            	Common.Error("操作失败：" + result.responseText);
            })
            .always(function() {
            	
            });
		return false;
	});
		
	$(".match").click(function(){
		var data_cid = $(this).attr("data-cid");
		var src = $("#matchIframe").attr("src");
		$("#matchIframe").attr("src", "expense_income_invoice/match/"+data_cid);	
		$("#matchDialog").modal();
		return false;
	});	
	window.searchback = function(){
		$("#matchDialog").modal("hide");
		location.reload();
	}

});
