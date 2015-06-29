$(document).ready( function() {
	/* 设置当前选中的页 */
	var $a = $("#left li a[href='order/undelivery']");
	setActiveLeft($a.parent("li"));
	/* 设置当前选中的页 */
	
	$("#previewBtn").click(function(){
		var file = $(".form #file").val();
		if(file == ""){
			alert("请先填写 装箱EXCEL文件！");
			return false;
		}
		var $form = $(".form");
		$form.attr("target","previewContent");
		$form.attr("action","packing_order/preview");
		$form.submit();
	});
	
	$("#uploadBtn").click(function(){
		var file = $(".form #file").val();
		if(file == ""){
			alert("请先填写 装箱EXCEL文件！");
			return false;
		}
		var $form = $(".form");	
		$submitBtn = $("#uploadBtn");
		$form.ajaxSubmit( {
			url :"packing_order/add",
			type :'POST',
			success : function(result) {
				if (result.success) {
					Common.Tip("上传成功", function() {
						location = "packing_order/detail/" + result.id;
					});
				}
				$submitBtn.button('reset');
			},
			error : function(result) {
				Common.Error("上传失败：" + result.responseText);
				$submitBtn.button('reset');
			}

		});
	});
});