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
});

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