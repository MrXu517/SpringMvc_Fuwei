$(document).ready(function(){
	/*设置当前选中的页*/
	var $a = $("#left li a[href='gongxu/index']");
	setActiveLeft($a.parent("li"));
	/*设置当前选中的页*/
	
	//工序 -- 开始
	setAddGongxu();
	$(".deleteGongxu").click(function(){
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "gongxu/delete/"+id,
            type: 'POST'
        })
            .done(function(result) {
            	if(result.success){
            		Common.Tip("删除成功",function(){
            			reload();
            		});
            	}
            })
            .fail(function(result) {
            	Common.Error("删除失败：" + result.responseText);
            })
            .always(function() {
            	
            });
		return false;
	});
	
	$(".editGongxu").click(function(){
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "gongxu/get/"+id,
            type: 'GET'
        })
            .done(function(result) {
            	if(result.success || result.success==undefined){
            		//填充到表单中
                	setUpdateGongxu(result);
            	}
            })
            .fail(function(result) {
            	Common.Error("获取工序详情失败：" + result.responseText);
            })
            .always(function() {
            	
            });
		//设置
		return false;
	});
	//工序 -- 结束

});


function setAddGongxu(){
	var $form = $("#gongxus form");
	Common.resetForm($form[0]);
	$form.removeClass("edit");
	var $submitBtn = $form.find("[type='submit']");
	$submitBtn.text("添加工序");
	$("#gongxus .formwidget .panel-title").text("添加工序");
	$form.unbind("submit");
	$form.submit(function(){
		if(!Common.checkform(this)){
			return false;
		}
		$submitBtn.button('loading');
		var formdata = $(this).serializeJson();
		delete formdata.id;
		$.ajax({
            url: "gongxu/add",
            type: 'POST',
            data: $.param(formdata),
        })
            .done(function(result) {
            	if(result.success){
            		Common.Tip("添加成功",function(){
            			reload();
            		});
            	}
            })
            .fail(function(result) {
            	Common.Error("添加失败：" + result.responseText);
            })
            .always(function() {
            	$submitBtn.button('reset');
            });
		return false;
	});
}
function setUpdateGongxu(result){
	var $form = $("#gongxus form");
	Common.resetForm($form[0]);
	Common.fillForm($("#gongxus form")[0],result);
	$form.addClass("edit");
	var $submitBtn = $form.find("[type='submit']");
	$submitBtn.text("修改工序");
	$("#gongxus .formwidget .panel-title").text("修改工序");
	
	$form.find(".switch_add").unbind("click");
	$form.find(".switch_add").bind("click",function(){
		setAddGongxu();
		return false;
	});
	
	$form.unbind("submit");
	$form.submit(function(){
		if(!Common.checkform(this)){
			return false;
		}
		$submitBtn.button('loading');
		var formdata = $(this).serialize();
		$.ajax({
            url: "gongxu/put",
            type: 'POST',
            data: formdata,
        })
            .done(function(result) {
            	if(result.success){
            		Common.Tip("修改成功",function(){
            			reload();
            		});
            	}
            })
            .fail(function(result) {
            	Common.Error("修改失败：" + result.responseText);
            })
            .always(function() {
            	$submitBtn.button('reset');
            	setAddGongxu();
            });
		return false;
	});
}

function reload(){
	window.location.reload();
}
