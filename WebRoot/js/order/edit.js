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
			var sampleId = $(this).find("#sampleId").val();
			if (sampleId == "") {
				Common.Error("添加失败：" + "请必须选择样品");
			}
			if (!Common.checkform(this)) {
				return false;
			}
			var formdata = $(this).serializeJson();
			// 获取表格数据
				$submitBtn.button('loading');
				$.ajax( {
					url :"order/put",
					type :'POST',
					data :$.param(formdata),
					success : function(result) {
						if (result.success) {
							Common.Tip("修改成功", function() {
								location = "order/detail/" + result.id;
							});
						}
						$submitBtn.button('reset');
					},
					error : function(result) {
						Common.Error("修改失败：" + result.responseText);
						$submitBtn.button('reset');
					}

				});
				return false;
			});

		// 公司-业务员级联
		$("#companyId").change( function() {
			changeCompany(this.value);
		});
		// 公司-业务员级联

		// 数量金额的联动
		$("#quantity,#price").bind("input propertychange", function() {
			Common.positive_intCheck_Rewrite(this, this.value);
			// 修改该行金额
				var quantity = Number($("#quantity").val());
				var price = Number($("#price").val());
				var amount = (Number(quantity) * price).toFixed(2);
				$("#amount").val(amount);
			});

		// 2014-11-10添加选择样品
		$("#sampleImgA").click( function() {
			$("#chooseSampleBtn").click();
			return false;
		});
		$("#chooseSampleBtn").click( function() {
			// 出现样品选择弹出框
				var src = $("#sampleIframe").attr("src");
				if (!src || src == "") {
					$("#sampleIframe").attr("src", "sample/search");
				}
				$("#sampleDialog").modal();

				// 设置样品弹出框表格选中之后的操作
				window.searchback = function(sampleId) {
					// 将数据填充到页面上
					$.ajax( {
						url :"sample/get/" + sampleId,
						type :'GET',
						success : function(sample) {
							sample.sampleId = sample.id;
							Common.fillForm($(".orderform")[0], sample);
							$("#sampleImgA").attr("href", "/" + sample.img);
							$("#sampleImg").attr("src", "/" + sample.img_s);
							$("#sampleDialog").modal('hide');
						},
						error : function(result) {
							Common.Error("获取订单详情信息失败：" + result.responseText);
							$("#sampleDialog").modal('hide');
						}
					});
				};
			});

		// 2014-11-10添加选择样品
	});
function totalAmount() {
	var amount = 0;
	var $trs = $("#detailTable tbody tr");
	for ( var i = 0; i < $trs.length; ++i) {
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