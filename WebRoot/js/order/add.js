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
			var formdata = $(this).serialize();
			var tabledata = [];
			//获取表格数据
			var $trs = $("#detailTable tbody tr");
			for(var i = 0 ; i < $trs.length;++i){
				var $tr = $trs.eq(i);
				var trdata = $.parseJSON($tr.attr("data_detail"));
				var quantity = $tr.find(".quantity_value").val();
				trdata.quantity = quantity;
				tabledata.push(trdata);
			}
			formdata.order_details = tabledata;
			
			//获取表格数据
			$submitBtn.button('loading');
			$.ajax( {
				url :"order/add",
				type :'POST',
				data:JSON.stringify(formdata),
				success : function(result) {
					if (result.success) {
						Common.Tip("添加成功", function() {
							location = "order/detail";
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
	for ( var i = 0; i < SalesNameList.length; ++i) {
		var salesName = SalesNameList[i];
		var option = document.createElement("option");
		option.value = salesName.id;
		option.text = salesName.name;
		frag.appendChild(option);
	}
	$("#salesmanId").append(frag);
}