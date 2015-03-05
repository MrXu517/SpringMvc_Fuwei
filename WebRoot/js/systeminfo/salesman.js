$(document).ready(function(){
	/*设置当前选中的页*/
	var $a = $("#left li a[href='salesman/index']");
	setActiveLeft($a.parent("li"));
	/*设置当前选中的页*/
	
	//公司 -- 开始
	setAddCompany();
	$(".deletecompany").click(function(){
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "company/delete/"+id,
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
	
	$(".editcompany").click(function(){
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "company/get/"+id,
            type: 'GET'
        })
            .done(function(result) {
            	if(result.success || result.success==undefined){
            		//填充到表单中
                	setUpdateCompany(result);
            	}
            })
            .fail(function(result) {
            	Common.Error("获取公司详情失败：" + result.responseText);
            })
            .always(function() {
            	
            });
		//设置
		return false;
	});
	//公司 -- 结束
	
	//业务员 -- 开始
	setAddSalesman();
	$(".deleteSalesman").click(function(){
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "salesman/delete/"+id,
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
	
	$(".editSalesman").click(function(){
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "salesman/get/"+id,
            type: 'GET'
        })
            .done(function(result) {
            	if(result.success || result.success==undefined){
            		//填充到表单中
                	setUpdateSalesman(result);
            	}
            })
            .fail(function(result) {
            	Common.Error("获取业务员详情失败：" +result.responseText);
            })
            .always(function() {
            	
            });
		//设置
		return false;
	});
	//业务员 -- 结束
	
});

function setAddCompany(){
	var $form = $("#companys form");
	Common.resetForm($form[0]);
	$form.removeClass("edit");
	var $submitBtn = $form.find("[type='submit']");
	$submitBtn.text("添加公司");
	$("#companys .formwidget .panel-title").text("添加公司");
	$form.unbind("submit");
	$form.submit(function(){
		if(!Common.checkform(this)){
			return false;
		}
		$submitBtn.button('loading');
		var formdata = $(this).serializeJson();
		delete formdata.id;
		$.ajax({
            url: "company/add",
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
function setUpdateCompany(result){
	var $form = $("#companys form");
	Common.resetForm($form[0]);
	Common.fillForm($("#companys form")[0],result);
	$form.addClass("edit");
	var $submitBtn = $form.find("[type='submit']");
	$submitBtn.text("修改公司");
	$("#companys .formwidget .panel-title").text("修改公司");
	
	$form.find(".switch_add").unbind("click");
	$form.find(".switch_add").bind("click",function(){
		setAddCompany();
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
            url: "company/put",
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
            	setAddCompany();
            });
		return false;
	});
}

function setAddSalesman(){
	var $form = $("#salesmans form");
	Common.resetForm($form[0]);
	$form.removeClass("edit");
	var $submitBtn = $form.find("[type='submit']");
	$submitBtn.text("添加业务员");
	$("#salesmans .formwidget .panel-title").text("添加业务员");
	$form.unbind("submit");
	$form.submit(function(){
		if(!Common.checkform(this)){
			return false;
		}
		$submitBtn.button('loading');
		var formdata = $(this).serializeJson();
		delete formdata.id;
		$.ajax({
            url: "salesman/add",
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
function setUpdateSalesman(result){
	var $form = $("#salesmans form");
	Common.resetForm($form[0]);
	Common.fillForm($("#salesmans form")[0],result);
	$form.addClass("edit");
	var $submitBtn = $form.find("[type='submit']");
	$submitBtn.text("修改业务员");
	$("#salesmans .formwidget .panel-title").text("修改业务员");
	
	$form.find(".switch_add").unbind("click");
	$form.find(".switch_add").bind("click",function(){
		setAddSalesman();
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
            url: "salesman/put",
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
            	setAddSalesman();
            });
		return false;
	});
}



function reload(){
	var tab = $("#tab>ul>li.active a").attr("href");
	tab = tab.substr(1,tab.length);
	location = location.pathname + "?tab=" + tab;
}
