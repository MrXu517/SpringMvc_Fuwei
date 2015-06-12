$(document).ready(function(){
	
	$("#searchBtn").click(function(){
		$(".searchform").submit();
	});
	
	$("a.match").click(function(){
		var data_cid = $(this).attr("data-cid");
		var src = $("#matchIframe").attr("src");
		$("#matchIframe").attr("src", "expense_income_invoice/match/"+data_cid);	
		$("#matchDialog").modal();
		return false;
	});
	
	$(".delete").click(function(){
		var id= $(this).attr("data-cid");
		var number= $(this).attr("data-number");
		if(!confirm("确定要删除发票【"+ number + "】， 并删除与之相关的发票与支出项的匹配项吗？")){
			return false;
		}
		$.ajax({
            url: "purchase_invoice/delete/"+id,
            type: 'POST'
        })
            .done(function(result) {
            	if(result.success){
            		top.Common.Tip("删除发票【"+ number + "】成功 ， 且成功修改与之相关的财务支出项",function(){
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
	
	window.searchback = function(){
		$("#matchDialog").modal("hide");
		location.reload();
	}
	
	$("#bank_id").select2();
	
});