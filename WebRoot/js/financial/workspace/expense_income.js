$(document).ready(function(){
	// 公司-业务员级联
	$("#companyId").change( function() {
		changeCompany(this.value);
	});
	// 公司-业务员级联
	
	$("#searchBtn").click(function(){
		$(".searchform").submit();
	});
	
	$(".table tbody tr").click(function(){
		$(this).siblings().removeClass("selected");
		$(this).addClass("selected");
	});
	
	$(".delete").click(function(){
		var id= $(this).attr("data-cid");
		if(!confirm("确定要删除该收入支出项吗，请仔细核对？")){
			return false;
		}
		$.ajax({
            url: "expense_income/delete/"+id,
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
	$("#bank_id").select2();
	
	$("a.match").click(function(){
		var data_cid = $(this).attr("data-cid");
		var src = $("#matchIframe").attr("src");
		$("#matchIframe").attr("src", "expense_income_invoice/income_match_saleinvoice/"+data_cid);	
		$("#matchDialog").modal();
		return false;
	});
	window.searchback = function(){
		$("#matchDialog").modal("hide");
		location.reload();
	}
});
function changeCompany(companyId) {
	var companyName = $("#companyId").val();
	var companySalesmanMap = $("#companyId").attr("data");
	companySalesmanMap = $.parseJSON(companySalesmanMap).companySalesmanMap;
	var SalesNameList = companySalesmanMap[companyName];
	$("#salesmanId").empty();
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

	$("#salesmanId").append(frag);
}