$(document).ready(function(){
	var tbOptions = {
			tableEle : $(".detailTb")[0],
			showNoOptions : {
				width :'5%',
				display :false
			},
			colnames : [
			    {
				name :'companyId',
				colname :'公司',
				width :'30%'
			    }, 
			    {
				name :'salesmanId',
				colname :'业务员',
				width :'30%'
				}, 
				{
					name :'subject_id',
					colname :'科目',
					width :'30%'
				}, {
					name :'account_id',
					colname :'本厂收支帐号',
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
		var formdata = $(this).serializeJson();
		var detailTbdata = tableInstance.getTableData();
		formdata.details = JSON.stringify(detailTbdata);
		$.ajax( {
			url :"expense_income/import_bank_list",
			type :'POST',
			data :$.param(formdata),
			success : function(result) {
				if (result.success) {
					var message = "导入成功";
					if(result.message){
						message = message + "<br>重要提示：" + result.message;
					}
					Common.Tip(message, function() {
						location.href="financial/workspace";
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
	
	// 公司-业务员级联
	$(".companyId").change( function() {
		changeCompany(this,this.value);
	});
	// 公司-业务员级联
});

function changeCompany(companyIdEle,companyId) {
	var $companyId = $(companyIdEle);
	var $salesmanId = $companyId.closest("tr").find(".salesmanId");
	var companyName = $companyId.val();
	var companySalesmanMap = $companyId.attr("data");
	companySalesmanMap = $.parseJSON(companySalesmanMap).companySalesmanMap;
	var SalesNameList = companySalesmanMap[companyName];
	$salesmanId.empty();
	var frag = document.createDocumentFragment();
	var option = document.createElement("option");
	option.value = "";
	option.text = "未选择";
	frag.appendChild(option);
	if (SalesNameList) {
		for ( var i = 0; i < SalesNameList.length; ++i) {
			var salesName = SalesNameList[i];
			var option = document.createElement("option");
			option.value = salesName.id;
			option.text = salesName.name;
			frag.appendChild(option);
		}
	}

	$salesmanId.append(frag);
}