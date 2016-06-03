$(document).ready(function(){
	var tbOptions = {
			tableEle : $(".detailTb")[0],
			showNoOptions : {
				width :'5%',
				display :false
			},
			colnames : [
			    {
				name :'company_id',
				colname :'公司',
				width :'30%'
			    }, 
				{
					name :'subject_id',
					colname :'科目',
					width :'30%'
				}, 
				{
					name :'memo',
					colname :'备注',
					width :'30%'
				}]
			};
	var tableInstance = TableTools.createTableInstance(tbOptions);
	$(".detailTb tr").unbind("click");
	$form = $(".form");
	$submitBtn = $form.find("button[type='submit']");
	$form.unbind("submit");
	$form.submit( function() {
		var requireles = $(".detailTb").find(
		"tr.checkselected input.require,tr.checkselected select.require,tr.checkselected textarea.require");
		// 验证必须填的字段
		for ( var i = 0; i < requireles.length; ++i) {
			var requirele = requireles[i];
			if (!Common.requireCheck(requirele, requirele.value)) {
				$(requirele).addClass('checkerror');
				$(requirele).focus();
				return false;
			}
		}
		var $checkedOrders = $(".checkBtn:checked");
		if($checkedOrders.length<=0){
			top.Common.Error("请至少选择一条记录");
			return false;
		}
		var formdata ={};
		var detailTbdata = tableInstance.getTableData();
		formdata.details = JSON.stringify(detailTbdata);
		$.ajax( {
			url :"sale_invoice/import_new_list",
			type :'POST',
			data :$.param(formdata),
			success : function(result) {
				if (result.success) {
					var message = "导入成功";
					if(result.message){
						message = message + "<br>重要提示：" + result.message;
					}
					Common.Tip(message, function() {
						location.href="financial/workspace?tab=sale_invoices";
					});
				}
				$submitBtn.button('reset');
			},
			error : function(result) {
				Common.Error("导入失败：" + result.responseText,function(){
				});
				$submitBtn.button('reset');
			}

		});
		return false;
	});
	
	$(".checkBtn").change(function(){
		var checked = this.checked;
		if(checked){
			$(this).closest("tr").removeClass("EmptyTr");
			$(this).closest("tr").removeClass("disable");
			$(this).closest("tr").addClass("checkselected");
		}else{
			$(this).closest("tr").addClass("EmptyTr");
			$(this).closest("tr").addClass("disable");
			$(this).closest("tr").removeClass("checkselected");
		}
		var $checkedOrders = $(".checkBtn:checked");
		$("#num").text($checkedOrders.length);
	});
});
