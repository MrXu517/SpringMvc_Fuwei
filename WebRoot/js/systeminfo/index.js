$(document).ready(function(){
	
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
            		alert("删除成功");
            		reload();
            	}else{
            		alert(result.message);
            	}
            })
            .fail(function(result) {
                alert("请求服务器过程中出错:" + result.responseText);
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
            	}else{
            		alert(result.message);
            	}
            	
            	
            })
            .fail(function(result) {
                alert("请求服务器过程中出错:" + result.responseText);
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
            		alert("删除成功");
            		reload();
            	}else{
            		alert(result.message);
            	}
            })
            .fail(function(result) {
                alert("请求服务器过程中出错:" + result.responseText);
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
            	}else{
            		alert(result.message);
            	}
            	
            	
            })
            .fail(function(result) {
                alert("请求服务器过程中出错:" + result.responseText);
            })
            .always(function() {
            	
            });
		//设置
		return false;
	});
	//业务员 -- 结束
	
	//用户 -- 开始
	setAddUser();
	$(".deleteUser").click(function(){
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "user/delete/"+id,
            type: 'POST'
        })
            .done(function(result) {
            	if(result.success){
            		alert("删除成功");
            		reload();
            	}else{
            		alert(result.message);
            	}
            })
            .fail(function(result) {
                alert("请求服务器过程中出错:" + result.responseText);
            })
            .always(function() {
            	
            });
		return false;
	});
	
	$(".editUser").click(function(){
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "user/get/"+id,
            type: 'GET'
        })
            .done(function(result) {
            	if(result.success || result.success==undefined){
            		//填充到表单中
                	setUpdateUser(result);
            	}else{
            		alert(result.message);
            	}
            	
            	
            })
            .fail(function(result) {
                alert("请求服务器过程中出错:" + result.responseText);
            })
            .always(function() {
            	
            });
		//设置
		return false;
	});
	//用户 -- 结束
	
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
            		alert("删除成功");
            		reload();
            	}else{
            		alert(result.message);
            	}
            })
            .fail(function(result) {
                alert("请求服务器过程中出错:" + result.responseText);
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
            	}else{
            		alert(result.message);
            	}
            	
            	
            })
            .fail(function(result) {
                alert("请求服务器过程中出错:" + result.responseText);
            })
            .always(function() {
            	
            });
		//设置
		return false;
	});
	//工序 -- 结束
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
            		alert("添加成功");
            		reload();
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
		$submitBtn.button('loading');
		var formdata = $(this).serialize();
		$.ajax({
            url: "company/put",
            type: 'POST',
            data: formdata,
        })
            .done(function(result) {
            	if(result.success){
            		alert("修改成功");
            		reload();
            	}else{
            		alert(result.message);
            	}
            })
            .fail(function(result) {
                alert("请求服务器过程中出错:" + result.responseText);
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
            		alert("添加成功");
            		reload();
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
		$submitBtn.button('loading');
		var formdata = $(this).serialize();
		$.ajax({
            url: "salesman/put",
            type: 'POST',
            data: formdata,
        })
            .done(function(result) {
            	if(result.success){
            		alert("修改成功");
            		reload();
            	}else{
            		alert(result.message);
            	}
            })
            .fail(function(result) {
                alert("请求服务器过程中出错:" + result.responseText);
            })
            .always(function() {
            	$submitBtn.button('reset');
            	setAddSalesman();
            });
		return false;
	});
}


function setAddUser(){
	var $form = $("#users form");
	Common.resetForm($form[0]);
	$form.removeClass("edit");
	var $submitBtn = $form.find("[type='submit']");
	$submitBtn.text("添加用户");
	$("#users .formwidget .panel-title").text("添加用户");
	$form.unbind("submit");
	$form.submit(function(){
		$submitBtn.button('loading');
		var formdata = $(this).serializeJson();
		delete formdata.id;
		$.ajax({
            url: "user/add",
            type: 'POST',
            data: $.param(formdata),
        })
            .done(function(result) {
            	if(result.success){
            		alert("添加成功");
            		reload();
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
}
function setUpdateUser(result){
	var $form = $("#users form");
	Common.resetForm($form[0]);
	Common.fillForm($("#users form")[0],result);
	$form.addClass("edit");
	var $submitBtn = $form.find("[type='submit']");
	$submitBtn.text("修改用户");
	$("#users .formwidget .panel-title").text("修改用户");
	
	$form.find(".switch_add").unbind("click");
	$form.find(".switch_add").bind("click",function(){
		setAddUser();
		return false;
	});
	
	$form.unbind("submit");
	$form.submit(function(){
		$submitBtn.button('loading');
		var formdata = $(this).serialize();
		$.ajax({
            url: "user/put",
            type: 'POST',
            data: formdata,
        })
            .done(function(result) {
            	if(result.success){
            		alert("修改成功");
            		reload();
            	}else{
            		alert(result.message);
            	}
            })
            .fail(function(result) {
                alert("请求服务器过程中出错:" + result.responseText);
            })
            .always(function() {
            	$submitBtn.button('reset');
            	setAddUser();
            });
		return false;
	});
}

function setAddGongxu(){
	var $form = $("#gongxus form");
	Common.resetForm($form[0]);
	$form.removeClass("edit");
	var $submitBtn = $form.find("[type='submit']");
	$submitBtn.text("添加工序");
	$("#gongxus .formwidget .panel-title").text("添加工序");
	$form.unbind("submit");
	$form.submit(function(){
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
            		alert("添加成功");
            		reload();
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
		$submitBtn.button('loading');
		var formdata = $(this).serialize();
		$.ajax({
            url: "gongxu/put",
            type: 'POST',
            data: formdata,
        })
            .done(function(result) {
            	if(result.success){
            		alert("修改成功");
            		reload();
            	}else{
            		alert(result.message);
            	}
            })
            .fail(function(result) {
                alert("请求服务器过程中出错:" + result.responseText);
            })
            .always(function() {
            	$submitBtn.button('reset');
            	setAddGongxu();
            });
		return false;
	});
}

function reload(){
	var tab = $("#tab>ul>li.active a").attr("href");
	tab = tab.substr(1,tab.length);
	location = location.pathname + "?tab=" + tab;
}
