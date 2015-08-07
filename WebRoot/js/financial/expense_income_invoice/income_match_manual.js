$(document).ready( function() {
	
	$(".match").click(function(){
		var invoice_id = $(this).attr("invoice_id");
		if(invoice_id == ""){
			return;
		}
		var expense_income_id = $(this).attr("expense_income_id");
		if(expense_income_id == ""){
			return;
		}
		if(confirm("手动匹配发票与收入项，请谨慎，是否确定要匹配？"))
		$.ajax( {
			url :"expense_income_invoice/income_match_manual",
			type :'POST',
			data :{expense_income_id:expense_income_id,
				   invoice_id:invoice_id
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
