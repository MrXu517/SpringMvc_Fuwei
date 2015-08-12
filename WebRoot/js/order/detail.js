$(document).ready(function(){
//	$(".basic-steps .step").hover(function(){
//		$(this).siblings(".tooltip").css("opacity",1);
//	},function(){
//		$(this).siblings(".tooltip").css("opacity",0);
//	});
	$(".basic-steps .step").click(function(){
		var open = $(this).siblings(".tooltip").hasClass("open");
		$(this).closest(".basic-steps").find(".tooltip").removeClass("open");
		if(open){
			$(this).siblings(".tooltip").removeClass("open");
		}else{
			$(this).siblings(".tooltip").addClass("open");
		}
		return false;
	});
	/*设置当前选中的页*/
	var $a = $("#left li a[href='order/index']");
	setActiveLeft($a.parent("li"));
	/*设置当前选中的页*/
	
	
	// 添加步骤 -- 开始
	$stepModal = $("#stepModal");
	$stepModal.on('hidden.bs.modal', function (e) {// 核价对话框被隐藏之后触发
		$(".stepform")[0].reset();
	});
	$("#addStep").click(function(){
		setAddStep();// 设置创建的表单
		Common.openModal($stepModal);
		return false;
	});
	
	// 添加步骤-- 结束
	
	// 删除步骤 -- 开始
	$(".deleteStep").click(function(){
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "order/deletestep/"+id,
            type: 'POST'
        })
            .done(function(result) {
            	if(result.success){
            		Common.Tip("删除成功",function(){
            			location.reload();
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
	// 删除步骤 -- 结束
	
	// 编辑步骤 -- 开始
	$(".editStep").click(function(){
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "order/getstep/"+id,
            type: 'GET'
        })
            .done(function(result) {
            	if(result.success!=false){
            		// 将数据填充到编辑表单中
            		setUpdateStep(result);// 设置创建的表单
            		Common.openModal($stepModal);
            	}
            })
            .fail(function(result) {
            	Common.Error("获取步骤详情失败：" + result.responseText);
            })
            .always(function() {
            	
            });
		return false;
	});
	// 编辑步骤 -- 结束
	
	//执行当前步骤 -- 开始
	var $exeform = $("#exeStepDialog .exeform");
	if($exeform.length>0){
		$exeform.submit(function(){
			if(!confirm("是否确定执行？")){
				return false;
			}
			if (!Common.checkform(this)) {
				return false;
			}
			var formdata = $(this).serializeJson();
			$.ajax({
	            url: "order/exestep/"+formdata.orderId,
	            type: 'POST',
	            data :$.param(formdata)
	        })
	            .done(function(result) {
	            	if(result.success!=false){
	            		Common.Tip("执行步骤成功",function(){
	            			location.reload();
	            		});
	            	}
	            })
	            .fail(function(result) {
	            	Common.Error("执行步骤失败：" + result.responseText);
	            })
	            .always(function() {
	            	
	            });
			return false;
		});
	}
	
	$('#exeStepDialog').on('shown.bs.modal', function () {
		$exeform.find("#delivery_at").focus();
		$exeform.find("#delivery_at").select();
	});
	$("#exeStep").click(function(){
		//若是发货状态，则弹出对话框，填写执行步骤的时间
		var $dialog = $("#exeStepDialog");
		if($dialog.length>0){
			$("#exeStepDialog").modal();
		}else{
			var orderId = $(this).attr("orderid");
			$.ajax({
	            url: "order/exestep/"+orderId,
	            type: 'POST'
	        })
	            .done(function(result) {
	            	if(result.success!=false){
	            		Common.Tip("执行步骤成功",function(){
	            			location.reload();
	            		});
	            	}
	            })
	            .fail(function(result) {
	            	Common.Error("执行步骤失败：" + result.responseText);
	            })
	            .always(function() {
	            	
	            });
		}
		
		return false;
	});
	//执行当前步骤 -- 结束
	
	//创建生产单 -- 开始
	var $notificationModal = $("#notificationModal");
	$notificationModal.on('hidden.bs.modal', function (e) {// 核价对话框被隐藏之后触发
		$(".notificationform")[0].reset();
	});
//	$(".addNotificationBtn").click(function(){
//		setAddNotification();// 设置创建的表单
//		Common.openModal($notificationModal);
//		return false;
//	});
	//创建生产单 -- 结束
	
	//打印生产通知单 -- 开始
	$(".printNotificationBtn").click(function(){
		var orderId = $(this).attr("orderId");
		$.ajax( {
			url :"order/printnotification/"+orderId,
			type :'GET',
			success : function(result) {
				if (result.success) {
					Common.Tip("开始打印...", function() {
					});
				}
			},
			error : function(result) {
				Common.Error("打印生产通知单失败：" + result.responseText);
			}

		});
		return false;
	});
	//打印生产通知单 -- 结束
});
function setAddNotification(){
	var $form = $(".notificationform");
	$form[0].reset();
	$form.removeClass("edit");
	var $submitBtn = $form.find("[type='submit']");
	$submitBtn.text("确定");
	$("#notificationModal .modal-title").text("创建生产单");
	$form.unbind("submit");
	$form.submit(function(){
		if (!Common.checkform(this)) {
			return false;
		}
		$submitBtn.button('loading');
		var formdata = $(this).serializeJson();
		delete formdata.id;
		$.ajax({
	        url: "order/addnotification",
	        type: 'POST',
	        data: $.param(formdata)
	    })
	        .done(function(result) {
	        	if(result.success){
	        		Common.Tip("创建生产单成功",function(){
	        			location.reload();
	        		});
	        	}
	        })
	        .fail(function(result) {
	        	Common.Error("创建生产单失败：" + result.responseText);
	        })
	        .always(function() {
	        	$submitBtn.button('reset');
	        });
		return false;
	});
}
function addNotification(){
	setAddNotification();// 设置创建的表单
	Common.openModal($notificationModal);
}
function setAddStep(){
	var $form = $(".stepform");
	$form[0].reset();
	$form.removeClass("edit");
	var $submitBtn = $form.find("[type='submit']");
	$submitBtn.text("新建步骤");
	$("#stepModal .modal-title").text("新建步骤");
	$form.unbind("submit");
	$form.submit(function(){
		if (!Common.checkform(this)) {
			return false;
		}
		$submitBtn.button('loading');
		var formdata = $(this).serializeJson();
		delete formdata.id;
		$.ajax( {
			url :"order/addstep",
			type :'POST',
			data: $.param(formdata),
			success : function(result) {
				if (result.success) {
					Common.Tip("添加成功", function() {
						location.reload();
					});
				}
				$submitBtn.button('reset');
			},
			error : function(result) {
				Common.Error("添加失败：" + result.responseText);
				$submitBtn.button('reset');
			}
		});
		return false;
	});
}
function setUpdateStep(result){
	var $form = $(".stepform");
	$form[0].reset();
	Common.fillForm($form[0],result);
	$("#salesmanId").val(result.salesmanId);
	$form.addClass("edit");
	var $submitBtn = $form.find("[type='submit']");
	$submitBtn.text("修改步骤");
	$("#stepModal .modal-title").text("修改步骤");
	
	$form.unbind("submit");
	$form.submit(function(){
		if(!Common.checkform(this)){
			return false;
		}
		$submitBtn.button('loading');
		var formdata = $(this).serialize();
		$.ajax({
            url: "order/putstep",
            type: 'POST',
            data: formdata,
        })
            .done(function(result) {
            	if(result.success){
            		Common.Tip("修改成功",function(){
            			location.reload();
            		});
            	}
            })
            .fail(function(result) {
            	Common.Error("修改失败：" + result.responseText);
            })
            .always(function() {
            	$submitBtn.button('reset');
            });
		return false;
	});
}	