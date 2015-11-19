$(document).ready( function() {
	
	/*设置当前选中的页*/
	var $a = $("#left li a[href='fuliao_workspace/workspace']");
	setActiveLeft($a.parent("li"));
	/* 设置当前选中的页 */
	
	var $form = $(".saveform");
	$form.submit(function(){
		if (!Common.checkform(this)) {
			return false;
		}
		if(!confirm("请仔细确认更改的库位，并点击确定，之后请将辅料转移到该库位。          若有问题，则请点击取消")){
			return false;
		}
		var $saveform = $(this);
		var $submitBtn = $(this).find("[type='submit']");
		$submitBtn.button('loading');
		var formdata = $(this).serializeJson();		
		
		$.ajax( {
			url :"fuliao_workspace/changelocation",
			type :'POST',
			data :$.param(formdata),
			success : function(result) {
				var message = "更改库位成功";
				if(result.message){
					message = message + "<br>重要提示：" + result.message;
				}
				top.Common.Tip(message, function() {
					location.href = "fuliao_workspace/workspace?tab=current_stock";
				});
				$submitBtn.button('reset');
			},
			error : function(result) {
				top.Common.Error("更改库位失败：" + result.responseText,function(){
					
				});
				$submitBtn.button('reset');
			}

		});
		return false;
	});	
});
	


