$(document).ready(function(){
	var $form = $(".sampleform");
	var $submitBtn = $form.find("[type='submit']");
	$form.unbind("submit");
	$form.submit(function(){
		if(!Common.checkform(this)){
			return false;
		}
		$submitBtn.button('loading');
		var formdata = $(this).serializeJson();
		delete formdata.id;
//		
//		//新建样品
//		$("#addSampleform").submit( function() {
//			if (!checkform(this)) {
//				return false;
//			}
//			$(this).ajaxSubmit( {
//				type :'post',
//				url :'addUnPricedSample.do',
//				dataType :'json',
//				success : function(result) {
//					if (result.OK) {
//						alert("创建样品成功");
//						//隐藏添加div或者继续添加
//				$("#box_div").hide();
//				location = location.pathname;
//			} else {
//				alert("创建样品失败,错误信息:" + result.message);
//			}
//
//		}
//			});
//			return false;
//		});
//		//新建样品
		
		
		$(this).ajaxSubmit({
            url: "sample/add",
            type: 'POST',
            
            success:function(result) {
            	console.log(result);
//            	if(result.success){
//            		Common.Tip("添加成功",function(){
//            			reload();
//            		});
//            	}else{
//            		Common.Error("添加失败：" + result.message);
//            	}
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
