$(document).ready(function(){
	/*设置当前选中的页*/
	var $a = $("#left li a[href='order/index']");
	setActiveLeft($a.parent("li"));
	/*设置当前选中的页*/
	
	// 公司-业务员级联
	$("#companyId").change( function() {
		changeCompany(this.value);
	});
	// 公司-业务员级联
	
	//取消订单 -- 开始
	$(".delete").click(function(){
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "order/cancel/"+id,
            type: 'POST'
        })
            .done(function(result) {
            	if(result.success){
            		Common.Tip("取消订单成功",function(){
            			location.reload();
            		});
            	}
            })
            .fail(function(result) {
            	Common.Error("取消订单失败：" + result.responseText);
            })
            .always(function() {
            	
            });
		return false;
	});
	//取消订单 -- 结束
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