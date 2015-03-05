$(document).ready(function(){
	/*设置当前选中的页*/
	var $a = $("#left li a[href='material/index']");
	setActiveLeft($a.parent("li"));
	/*设置当前选中的页*/
	
	//材料 -- 开始
	setAddMaterial();
	$(".deleteMaterial").click(function(){
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "material/delete/"+id,
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
	
	$(".editMaterial").click(function(){
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "material/get/"+id,
            type: 'GET'
        })
            .done(function(result) {
            	if(result.success || result.success==undefined){
            		//填充到表单中
                	setUpdateMaterial(result);
            	}
            })
            .fail(function(result) {
            	Common.Error("获取材料详情失败：" + result.responseText);
            })
            .always(function() {
            	
            });
		//设置
		return false;
	});
	//材料 -- 结束
});


function setAddMaterial(){
	var $form = $("#materialTab form");
	Common.resetForm($form[0]);
	$form.removeClass("edit");
	var $submitBtn = $form.find("[type='submit']");
	$submitBtn.text("添加材料");
	$("#materialTab .formwidget .panel-title").text("添加材料");
	$form.unbind("submit");
	$form.submit(function(){
		if(!Common.checkform(this)){
			return false;
		}
		$submitBtn.button('loading');
		var formdata = $(this).serializeJson();
		delete formdata.id;
		$.ajax({
            url: "material/add",
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
function setUpdateMaterial(result){
	var $form = $("#materialTab form");
	Common.resetForm($form[0]);
	Common.fillForm($("#materialTab form")[0],result);
	$form.addClass("edit");
	var $submitBtn = $form.find("[type='submit']");
	$submitBtn.text("修改材料");
	$("#materialTab .formwidget .panel-title").text("修改材料");
	
	$form.find(".switch_add").unbind("click");
	$form.find(".switch_add").bind("click",function(){
		setAddMaterial();
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
            url: "material/put",
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
            	setAddMaterial();
            });
		return false;
	});
}

function reload(){
	window.location.reload();
}
