$(document).ready(function(){
	$("#companys form").submit(function(){
		var $submitBtn = $(this).find("[type='submit']");
		$submitBtn.button('loading');
		var formdata = $(this).serialize();
		$.ajax({
            url: "company/add",
            type: 'POST',
            data: formdata,
        })
            .done(function(result) {
            	if(result.success){
            		alert("添加成功");
            		location.reload();
            	}else{
            		alert(result.message);
            	}
            })
            .fail(function(result) {
                alert("请求服务器过程中出错:" + result.responseText);
            })
            .always(function() {
            	$submitBtn.button('reset');
            });
		return false;
	});
	
});
