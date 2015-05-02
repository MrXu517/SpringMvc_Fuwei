$(document).ready(function(){
	/*设置当前选中的页*/
	var $a = $("#left li a[href='employee/index']");
	setActiveLeft($a.parent("li"));
	/*设置当前选中的页*/
	
	//2015-4-3 添加自动focus到第一个可输入input、select
	$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
		$(".tab-content .tab-pane.active form").find("input[type='text'],select").not("[readonly],[disabled]").first().focus();
	});
	//2015-4-3 添加自动focus到第一个可输入input、select
	
	var tabname = Common.urlParams().tab;
	if (tabname == null || tabname == undefined) {
		$('#tab a:first').tab('show') // Select first tab
		$(".tab-content .tab-pane.active form").find("input[type='text'],select").not("[readonly],[disabled]").first().focus();
	}else{
		$("#tab a[href='#" + tabname + "']").tab('show') // Select tab by name
		$(".tab-content .tab-pane.active form").find("input[type='text'],select").not("[readonly],[disabled]").first().focus();
	}
	
	$("#filterform #departmentId").change(function(){
		$("#filterform").submit();
	});
	
	//公司 -- 开始
	setAddDepartment();
	$("#departments .delete").click(function(){
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "department/delete/"+id,
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
	
	$("#departments .edit").click(function(){
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "department/get/"+id,
            type: 'GET'
        })
            .done(function(result) {
            	if(result.success || result.success==undefined){
            		//填充到表单中
                	setUpdateDepartment(result);
            	}
            })
            .fail(function(result) {
            	Common.Error("获取部门详情失败：" + result.responseText);
            })
            .always(function() {
            	
            });
		//设置
		return false;
	});
	//公司 -- 结束
	
	//业务员 -- 开始
	setAddEmployee();
	$("#employees .delete").click(function(){
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "employee/delete/"+id,
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
	
	$("#employees .edit").click(function(){
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "employee/get/"+id,
            type: 'GET'
        })
            .done(function(result) {
            	if(result.success || result.success==undefined){
            		//填充到表单中
                	setUpdateEmployee(result);
            	}
            })
            .fail(function(result) {
            	Common.Error("获取员工详情失败：" +result.responseText);
            })
            .always(function() {
            	
            });
		//设置
		return false;
	});
	//业务员 -- 结束
	
});

function setAddDepartment(){
	var $form = $("#departments form");
	Common.resetForm($form[0]);
	$form.removeClass("edit");
	var $submitBtn = $form.find("[type='submit']");
	$submitBtn.text("添加部门");
	$("#departments .formwidget .panel-title").text("添加部门");
	$form.unbind("submit");
	$form.submit(function(){
		if(!Common.checkform(this)){
			return false;
		}
		$submitBtn.button('loading');
		var formdata = $(this).serializeJson();
		delete formdata.id;
		$.ajax({
            url: "department/add",
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
function setUpdateDepartment(result){
	var $form = $("#departments form");
	Common.resetForm($form[0]);
	Common.fillForm($("#departments form")[0],result);
	$form.addClass("edit");
	var $submitBtn = $form.find("[type='submit']");
	$submitBtn.text("修改部门");
	$("#departments .formwidget .panel-title").text("修改部门");
	
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
            url: "department/put",
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
            	setAddDepartment();
            });
		return false;
	});
}

function setAddEmployee(){
	var $form = $("#employees form").first();
	Common.resetForm($form[0]);
	$form.removeClass("edit");
	var $submitBtn = $form.find("[type='submit']");
	$submitBtn.text("添加员工");
	$("#employees .formwidget .panel-title").text("添加员工");
	$form.unbind("submit");
	$form.submit(function(){
		if(!Common.checkform(this)){
			return false;
		}
		$submitBtn.button('loading');
		var formdata = $(this).serializeJson();
		delete formdata.id;
		$.ajax({
            url: "employee/add",
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
function setUpdateEmployee(result){
	var $form = $("#employees form");
	Common.resetForm($form[0]);
	Common.fillForm($("#employees form")[0],result);
	$form.addClass("edit");
	var $submitBtn = $form.find("[type='submit']");
	$submitBtn.text("修改员工");
	$("#employees .formwidget .panel-title").text("修改员工");
	
	$form.find(".switch_add").unbind("click");
	$form.find(".switch_add").bind("click",function(){
		setAddEmployee();
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
            url: "employee/put",
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
            	setAddEmployee();
            });
		return false;
	});
}



function reload(){
	var tab = $("#tab>ul>li.active a").attr("href");
	tab = tab.substr(1,tab.length);
	location = location.pathname + "?tab=" + tab;
}
