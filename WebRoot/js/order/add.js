$(document).ready( function() {
	/* 设置当前选中的页 */
	var $a = $("#left li a[href='order/add']");
	setActiveLeft($a.parent("li"));
	/* 设置当前选中的页 */

	// 重置按钮
		$("button.reset").click( function() {
			$form = $(this).closest("form");
			$form[0].reset();
			$("#previewWidget img").attr("src", "");
		});
		// 重置按钮

		var $form = $(".orderform");
		var $submitBtn = $form.find("[type='submit']");
		$form.unbind("submit");
		$form.submit( function() {
			if (!Common.checkform(this)) {
				return false;
			}
			var formdata = $(this).serializeJson();
			var tabledata = [];
			//获取表格数据
			var $trs = $("#detailTable tbody tr");
			for(var i = 0 ; i < $trs.length;++i){
				var $tr = $trs.eq(i);
				var trdata = $.parseJSON($tr.attr("data_detail"));
				var quantity = $tr.find(".quantity_value").val();
				var memo = $tr.find(".memo_value").val();
				trdata.memo = memo;
				trdata.quantity = quantity;
				tabledata.push(trdata);
			}
			formdata.order_details = JSON.stringify(tabledata);
			
			//获取表格数据
			$submitBtn.button('loading');
			$.ajax( {
				url :"order/add",
				type :'POST',
				data:$.param(formdata),
				success : function(result) {
					if (result.success) {
						Common.Tip("添加成功", function() {
							location = "order/detail/"+result.id;
						});
					}
					$submitBtn.button('reset');
				},
				error : function(result) {
					Common.Error("添加失败：" + result.responseText);
					$submitBtn.button('reset');
				}

			});
			return false;
		});
		
		// 公司-业务员级联
		$("#companyId").change(function(){
			changeCompany(this.value);
		});
		// 公司-业务员级联
		
		//数量金额的联动
		$("#detailTable").on("input propertychange",".quantity_value",function(){
			Common.positive_intCheck_Rewrite(this, this.value);
			//修改该行金额
			var quantity = this.value;
			var price = Number($(this).parent().siblings(".price").text());
			var amount = (Number(quantity) * price).toFixed(2);
			$(this).parent().siblings(".amount").text(amount);
			//修改订单总金额
			$("#amount").val(totalAmount());
		});

	});
function totalAmount(){
	var amount = 0 ; 
	var $trs = $("#detailTable tbody tr");
	for(var i = 0 ; i < $trs.length ; ++i){
		var $tr = $trs.eq(i);
		amount += Number($tr.find(".amount").text());
	}
	return amount.toFixed(2);
}
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
	for ( var i = 0; i < SalesNameList.length; ++i) {
		var salesName = SalesNameList[i];
		var option = document.createElement("option");
		option.value = salesName.id;
		option.text = salesName.name;
		frag.appendChild(option);
	}
	$("#salesmanId").append(frag);
}