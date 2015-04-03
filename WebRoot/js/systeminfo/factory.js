$(document).ready(function(){
	/*设置当前选中的页*/
	var $a = $("#left li a[href='factory/index']");
	setActiveLeft($a.parent("li"));
	/*设置当前选中的页*/
	
	//2015-4-3 添加自动focus到第一个可输入input、select
	$("form").find("input[type='text'],select").not("[readonly],[disabled]").first().focus();
	//2015-4-3 添加自动focus到第一个可输入input、select
	
	/*2015-4-2添加*/
	$("#filterform #type").change(function(){
		$("#filterform").submit();
	});
	
	//工厂 -- 开始
	setAddFactory();
	$(".deleteFactory").click(function(){
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "factory/delete/"+id,
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
	
	$(".editFactory").click(function(){
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "factory/get/"+id,
            type: 'GET'
        })
            .done(function(result) {
            	if(result.success || result.success==undefined){
            		//填充到表单中
                	setUpdateFactory(result);
            	}
            })
            .fail(function(result) {
            	Common.Error("获取工厂详情失败：" + result.responseText);
            })
            .always(function() {
            	
            });
		//设置
		return false;
	});
	//工厂 -- 结束
});


function setAddFactory(){
	var $form = $("#factory .formwidget form");
	Common.resetForm($form[0]);
	$form.removeClass("edit");
	var $submitBtn = $form.find("[type='submit']");
	$submitBtn.text("添加加工工厂");
	$("#factory .formwidget .panel-title").text("添加加工工厂");
	$form.unbind("submit");
	$form.submit(function(){
		if(!Common.checkform(this)){
			return false;
		}
		$submitBtn.button('loading');
		var formdata = $(this).serializeJson();
		delete formdata.id;
		$.ajax({
            url: "factory/add",
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
function setUpdateFactory(result){
	var $form = $("#factory .formwidget form");
	Common.resetForm($form[0]);
	Common.fillForm($("#factory form")[0],result);
	$form.addClass("edit");
	var $submitBtn = $form.find("[type='submit']");
	$submitBtn.text("修改工厂");
	$("#factory .formwidget .panel-title").text("修改工厂");
	
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
            url: "factory/put",
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
            	setAddFactory();
            });
		return false;
	});
}

function reload(){
	window.location.reload();
}
