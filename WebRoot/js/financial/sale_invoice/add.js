$(document).ready( function() {
	/* 设置当前选中的页 */
	var $a = $("#left li a[href='sale_invoice/add']");
	setActiveLeft($a.parent("li"));
	/* 设置当前选中的页 */

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
			
		
			// 获取表格数据
				$submitBtn.button('loading');
				$.ajax( {
					url :"sale_invoice/add",
					type :'POST',
					data :$.param(formdata),
					success : function(result) {
						if (result.success) {
							Common.Tip("添加成功", function() {
								location = "sale_invoice/detail/" + result.id;
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
