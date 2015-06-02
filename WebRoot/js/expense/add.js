$(document).ready( function() {
	/* 设置当前选中的页 */
	var $a = $("#left li a[href='expense/add']");
	setActiveLeft($a.parent("li"));
	/* 设置当前选中的页 */
	
	// 公司-业务员级联
	$("#companyId").change( function() {
		changeCompany(this.value);
	});
	// 公司-业务员级联
	
	var $form = $(".form");
	var $submitBtn = $form.find("[type='submit']");
	$form.unbind("submit");
	$form.submit( function() {
		if (!Common.checkform(this)) {
			return false;
		}
		var formdata = $(this).serializeJson();
		// 获取表格数据
			$submitBtn.button('loading');
			$.ajax( {
				url :"expense/add",
				type :'POST',
				data :$.param(formdata),
				success : function(result) {
					if (result.success) {
						Common.Tip("添加成功", function() {
							location = "expense/detail/" + result.id;
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