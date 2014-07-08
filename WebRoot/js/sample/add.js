$(document).ready(function(){
	/*设置当前选中的页*/
	var $a = $("#left li a[href='sample/add']");
	setActiveLeft($a.parent("li"));
	/*设置当前选中的页*/
	
	var $form = $(".sampleform");
	var $submitBtn = $form.find("[type='submit']");
	$form.unbind("submit");
	$form.submit(function(){
		if(!Common.checkform(this)){
			return false;
		}
		$submitBtn.button('loading');
		$(this).ajaxSubmit({
            url: "sample/add",
            type: 'POST',
            success:function(result) {
            	if(result.success){
            		Common.Tip("添加成功",function(){
            			reload();
            		});
            	}else{
            		Common.Error("添加失败：" + result.message);
            	}
            	$submitBtn.button('reset');
            },
            error:function(result) {
            	Common.Error("请求服务器过程中出错:" + result.responseText);
            	$submitBtn.button('reset');
            }
            
        });
		return false;
	});
});
