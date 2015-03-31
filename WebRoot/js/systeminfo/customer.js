$(document).ready(function(){
	/*设置当前选中的页*/
	var $a = $("#left li a[href='customer/index']");
	setActiveLeft($a.parent("li"));
	/*设置当前选中的页*/
	
	//客户 -- 开始
	setAddCustomer();
	$(".delete").click(function(){
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "customer/delete/"+id,
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
	
	$(".edit").click(function(){
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "customer/get/"+id,
            type: 'GET'
        })
            .done(function(result) {
            	if(result.success || result.success==undefined){
            		//填充到表单中
                	setUpdateCustomer(result);
            	}
            })
            .fail(function(result) {
            	Common.Error("获取客户详情失败：" + result.responseText);
            })
            .always(function() {
            	
            });
		//设置
		return false;
	});
	//客户 -- 结束
});


function setAddCustomer(){
	var $form = $("#customer form");
	Common.resetForm($form[0]);
	$form.removeClass("edit");
	var $submitBtn = $form.find("[type='submit']");
	$submitBtn.text("添加客户");
	$("#customer .formwidget .panel-title").text("添加客户");
	$form.unbind("submit");
	$form.submit(function(){
		if(!Common.checkform(this)){
			return false;
		}
		$submitBtn.button('loading');
		var formdata = $(this).serializeJson();
		delete formdata.id;
		$.ajax({
            url: "customer/add",
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
function setUpdateCustomer(result){
	var $form = $("#customer form");
	Common.resetForm($form[0]);
	Common.fillForm($("#customer form")[0],result);
	$form.addClass("edit");
	var $submitBtn = $form.find("[type='submit']");
	$submitBtn.text("修改客户");
	$("#customer .formwidget .panel-title").text("修改客户");
	
	$form.find(".switch_add").unbind("click");
	$form.find(".switch_add").bind("click",function(){
		setAddFactory();
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
            url: "customer/put",
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
            	setAddCustomer();
            });
		return false;
	});
}

function reload(){
	window.location.reload();
}
