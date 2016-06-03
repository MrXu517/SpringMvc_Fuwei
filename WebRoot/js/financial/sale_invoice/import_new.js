$(document).ready(function(){
	$form = $(".form");
	$submitBtn = $form.find("button[type='submit']");
	$form.unbind("submit");
	$form.submit( function() {
		if (!Common.checkform(this)) {
			return false;
		}
		var $file = $(this).find("#file");
		var file_v = $file.val();
		if(file_v.lastIndexOf(".")<=0){
			Common.Error("添加失败：" + "请上传有效的扩展名为.xls的Excel文件");
			return false;
		}
		var ext = file_v.substr(file_v.lastIndexOf(".")+1).toLowerCase();
		if(ext != "xls" & ext != "xlsx"){
			Common.Error("添加失败：" + "请上传有效的扩展名为.xls的Excel文件");
			return false;
		}
		$submitBtn.button('loading');
		return true;
	});
});