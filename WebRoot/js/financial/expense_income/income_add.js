$(document).ready( function() {
	/* 设置当前选中的页 */
	var $a = $("#left li a[href='income/add']");
	setActiveLeft($a.parent("li"));
	/* 设置当前选中的页 */

	// 公司-业务员级联
		$("#company_id").change( function() {
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
			var bank_name = $("#bank_id option:selected").text().trim();
			formdata.bank_name = bank_name;
			var company_name = $("#company_id option:selected").text().trim();
			if(company_name!=""){
				formdata.company_name = company_name;
			}
			var salesman_name = $("#salesman_id option:selected").text().trim();
			if(salesman_name!=""){
				formdata.salesman_name = salesman_name;
			}
			var subject_name = $("#subject_id option:selected").text().trim();
			if(subject_name!=""){
				formdata.subject_name = subject_name;
			}
			// 获取表格数据
				$submitBtn.button('loading');
				$.ajax( {
					url :"income/add",
					type :'POST',
					data :$.param(formdata),
					success : function(result) {
						if (result.success) {
							Common.Tip("添加成功", function() {
								location = "income/detail/" + result.id;
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

		// 2015-6-4添加账户选择combobox	
		$("#bank_id").select2();
		

	});
function changeCompany(companyId) {
	var companyName = $("#company_id").val();
	var companySalesmanMap = $("#company_id").attr("data");
	companySalesmanMap = $.parseJSON(companySalesmanMap).companySalesmanMap;
	var SalesNameList = companySalesmanMap[companyName];
	$("#salesman_id").empty();
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

	$("#salesman_id").append(frag);
}