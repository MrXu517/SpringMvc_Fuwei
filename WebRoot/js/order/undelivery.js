$(document).ready(function(){
	/* 设置当前选中的页 */
	var $a = $("#left li a[href='order/undelivery']");
	setActiveLeft($a.parent("li"));
	/* 设置当前选中的页 */
	
	// 公司-业务员级联
	$("#companyId").change( function() {
		changeCompany(this.value);
	});
	// 公司-业务员级联
	
	//修改订单备注 -- 开始
	$(".editmemo").click(function(){
		var data= $.parseJSON($(this).closest("tr").attr("data"));
		$("#memoDialog #orderNumber").text(data.orderNumber);
		$("#memoDialog #sampleName").text(data.name);
		$(".memoform input[name='id']").val(data.id);
		$(".memoform #memo").val("");
		$("#memoDialog").modal();	
		return false;
	});
	var $submitBtn = $(".memoform").find("[type='submit']");
	$(".memoform").submit(function(){
		var formdata = $(this).serializeJson();
		var orderId = formdata.id;
		var $selectedTr = $("#Tb>tbody>tr[orderId="+orderId+"]");
			$submitBtn.button('loading');
			$.ajax( {
				url :"order/edit/memo",
				type :'POST',
				data :$.param(formdata),
				success : function(result) {
					if (result.success) {
						Common.Tip("修改备注成功", function() {
							$selectedTr.find("td.memo").html(result.memo);
							$("#memoDialog").modal("hide");	
						});
					}
					$submitBtn.button('reset');
				},
				error : function(result) {
					Common.Error("修改备注失败：" + result.responseText);
					$submitBtn.button('reset');
				}

			});
		return false;
	});
	//修改订单备注 -- 结束
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
	option.text = "所有";
	frag.appendChild(option);
	for ( var i = 0; i < SalesNameList.length; ++i) {
		var salesName = SalesNameList[i];
		var option = document.createElement("option");
		option.value = salesName.id;
		option.text = salesName.name;
		frag.appendChild(option);
	}
	$("#salesmanId").append(frag);
}