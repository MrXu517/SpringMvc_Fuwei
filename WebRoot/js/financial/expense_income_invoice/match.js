$(document).ready( function() {
	
	$(".match").click(function(){
		var invoice_ids = $(this).attr("invoice_ids");
		if(invoice_ids == ""){
			return;
		}
		var expense_income_ids = $(this).attr("expense_income_ids");
		if(expense_income_ids == ""){
			return;
		}
		$.ajax( {
			url :"expense_income_invoice/match",
			type :'POST',
			data :{expense_income_ids:expense_income_ids,
				   invoice_ids:invoice_ids
			},
			success : function(result) {
				if (result.success) {
					top.Common.Tip("匹配成功", function() {
						if(window.parent.searchback){
							window.parent.searchback();
						}
					});
				}
			},
			error : function(result) {
				top.Common.Error("匹配失败：" + result.responseText);
			}

		});
		return false;
	});
});
