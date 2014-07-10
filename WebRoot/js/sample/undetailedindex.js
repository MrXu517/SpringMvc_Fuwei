$(document).ready(function(){
	/*设置当前选中的页*/
	var $a = $("#left li a[href='sample/undetailedindex']");
	setActiveLeft($a.parent("li"));
	/*设置当前选中的页*/
	
	//删除 -- 开始
	$(".delete").click(function(){
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "sample/delete/"+id,
            type: 'POST'
        })
            .done(function(result) {
            	if(result.success){
            		Common.Tip("删除成功",function(){
            			location.reload();
            		});
            	}else{
            		Common.Error("删除失败：" + result.message);
            	}
            })
            .fail(function(result) {
            	Common.Error("请求服务器过程中出错:" + result.responseText);
            })
            .always(function() {
            	
            });
		return false;
	});
	//删除 -- 结束
	
	//打样人改变事件
	$("#charge_user").change(function(){
		var value = this.value;
		location = location.pathname+"?charge_user="+this.value;
	});
	//打样人改变事件
	
	//核价按钮 -- 开始
	$(".calcuteDetail").click(function(){
		var $model = $('#priceDialog>div.modal');
		var $dialog = $model.find('.modal-dialog');
		var paddingtop = 0;
		$model.show();
		paddingtop = ($model.height() - $dialog.outerHeight()) / 2;
		if (paddingtop < 30) {
			paddingtop = 30;
		}
		$dialog.css("padding-top", paddingtop);
		$model.hide();
		$model.modal({keyboard:true});
		return false;
	});
	//核价按钮 -- 开始
	
	//报价计算器按钮 -- 开始
	$("#calculateBtn").click(function(){
		var $model = $('#calculateDialog>div.modal');
		var $dialog = $model.find('.modal-dialog');
		var paddingtop = 0;
		$model.show();
		paddingtop = ($model.height() - $dialog.outerHeight()) / 2;
		if (paddingtop < 30) {
			paddingtop = 30;
		}
		$dialog.css("padding-top", paddingtop);
		$model.hide();
		$model.modal({keyboard:true});
		return false;
	});
	//报价计算器按钮 -- 结束
	
	//报价计算器 -- 开始
	//报价计算器 -- 结束
});
