$(document).ready(function(){
	/*设置当前选中的页*/
	var $a = $("#left li a[href='role/list']");
	setActiveLeft($a.parent("li"));
	/*设置当前选中的页*/
	
	//角色 -- 开始
	setAdd();
	$(".delete").click(function(){
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "role/delete/"+id,
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
            	Common.Error("删除失败：" +result.responseText);
            })
            .always(function() {
            	
            });
		return false;
	});
	
	$(".edit").click(function(){
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "role/get/"+id,
            type: 'GET'
        })
            .done(function(result) {
            	if(result.success || result.success==undefined){
            		//填充到表单中
                	setUpdate(result);
            	}
            })
            .fail(function(result) {
            	Common.Error("获取角色详情失败：" +result.responseText);
            })
            .always(function() {
            });
		//设置
		return false;
	});
	
	//角色 -- 结束

});


function setAdd(){
	var $form = $("#roles form");
	Common.resetForm($form[0]);
	$form.removeClass("edit");
	var $submitBtn = $form.find("[type='submit']");
	$submitBtn.text("添加角色");
	$("#roles .formwidget .panel-title").text("添加角色");
	$form.unbind("submit");
	$form.submit(function(){
		if(!Common.checkform(this)){
			return false;
		}
		$submitBtn.button('loading');
		var formdata = $(this).serializeJson();
		delete formdata.id;
		$.ajax({
            url: "role/add",
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
function setUpdate(result){
	var $form = $("#roles form");
	Common.resetForm($form[0]);
	Common.fillForm($("#roles form")[0],result);
	$form.addClass("edit");
	var $submitBtn = $form.find("[type='submit']");
	$submitBtn.text("修改角色");
	$("#roles .formwidget .panel-title").text("修改角色");
	
	$form.find(".switch_add").unbind("click");
	$form.find(".switch_add").bind("click",function(){
		setAdd();
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
            url: "role/put",
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
            	setAdd();
            });
		return false;
	});
}


function reload(){
	window.location.reload();
}
