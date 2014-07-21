$(document).ready(function(){
	/* 设置当前选中的页 */
	var $a = $("#left li a[href='sample/index']");
	setActiveLeft($a.parent("li"));
	/* 设置当前选中的页 */
	
	//生成样品标签 -- 开始
	$("#printSignBtn").click(function(){
		var sampleId = $(this).attr("sampleId");
		$.ajax( {
			url :"sample/print_sign/"+sampleId,
			type :'GET',
			success : function(result) {
				if (result.success) {
					Common.Tip("生成样品标签成功,开始打印...", function() {
						//reload();
					});
				} else {
					Common.Error("生成样品标签失败：" + result.message);
				}
			},
			error : function(result) {
				Common.Error("请求服务器过程中出错:" + result.responseText);
			}

		});
	});
	//生成样品标签 -- 结束
	
	//打印样品价格详情 -- 开始
	$(".printDetail").click(function(){
		var quotepriceId = $(this).attr("data-cid");
		$.ajax( {
			url :"sample/printDetail/"+quotepriceId,
			type :'GET',
			success : function(result) {
				if (result.success) {
					Common.Tip("开始打印...", function() {
						//reload();
					});
				} else {
					Common.Error("生成样品标签失败：" + result.message);
				}
			},
			error : function(result) {
				Common.Error("请求服务器过程中出错:" + result.responseText);
			}

		});
		return false;
	});
	//打印样品价格详情-- 结束
	
	// 公司-业务员级联
	$("#companyId").change(function(){
		changeCompany(this.value);
	});
	$("#companyId").change();
	// 公司-业务员级联
	
	
	// 创建报价价格 -- 开始
	$quoteModal = $("#quoteModal");
	$quoteModal.on('hidden.bs.modal', function (e) {// 核价对话框被隐藏之后触发
		$(".quoteform")[0].reset();
		$("#companyId").change();
	});
	$("#addQuoteBtn").click(function(){
		setAddQuote();// 设置创建的表单
		var $dialog = $quoteModal.find('.modal-dialog');
		var paddingtop = 0;
		$quoteModal.show();
		paddingtop = ($quoteModal.height() - $dialog.outerHeight()) / 2;
		if (paddingtop < 30) {
			paddingtop = 30;
		}
		$dialog.css("padding-top", paddingtop);
		$quoteModal.hide();
		$quoteModal.modal({keyboard:true});
		
		return false;
	});
	
	// 创建报价 价格-- 结束
	
	// 添加到报价列表 -- 开始
	$(".addQuote").click(function(){
		var quotePriceId = $(this).attr("data-cid");
		$.ajax( {
			url :"quote/add",
			type :'POST',
			data:{quotePriceId:quotePriceId},
			success : function(result) {
				if (result.success) {
					Common.Tip("添加成功", function() {
						//reload();
					});
				} else {
					Common.Error("添加失败：" + result.message);
				}
			},
			error : function(result) {
				Common.Error("请求服务器过程中出错:" + result.responseText);
			}

		});
		return false;
	});
	// 添加到报价列表 -- 结束
	
	// 删除价格 -- 开始
	$(".delete").click(function(){
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "quoteprice/delete/"+id,
            type: 'POST'
        })
            .done(function(result) {
            	if(result.success){
            		Common.Tip("删除成功",function(){
            			location.reload();
            		});
            	}else{
            		Common.Error("删除失败：" + result.message);
            	}
            })
            .fail(function(result) {
            	Common.Error("请求服务器过程中出错:" + result.responseText);
            })
            .always(function() {
            	
            });
		return false;
	});
	// 删除价格 -- 结束
	
	// 编辑价格 -- 开始
	$(".edit").click(function(){
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "quoteprice/get/"+id,
            type: 'GET'
        })
            .done(function(result) {
            	if(result.success!=false){
            		// 将数据填充到编辑表单中
            		setUpdateQuote(result);// 设置创建的表单
            		Common.openModal($quoteModal);
            	}else{
            		Common.Error("获取价格详情失败：" + result.message);
            	}
            })
            .fail(function(result) {
            	Common.Error("请求服务器过程中出错:" + result.responseText);
            })
            .always(function() {
            	
            });
		return false;
	});
	// 编辑价格 -- 结束
});
function changeCompany(companyId){
	var companyName = $("#companyId").val();
	var companySalesmanMap = $("#companyId").attr("data");
	companySalesmanMap = $.parseJSON(companySalesmanMap).companySalesmanMap;
	var SalesNameList = companySalesmanMap[companyName];
	$("#salesmanId").empty();
	var frag = document.createDocumentFragment();
	for(var i = 0 ; i < SalesNameList.length;++i ){
		var salesName = SalesNameList[i];
		var option = document.createElement("option");
		option.value = salesName.id;
		option.text = salesName.name;
		frag.appendChild(option);
	}
	$("#salesmanId").append(frag);
}
function setAddQuote(){
	var $form = $(".quoteform");
	$form[0].reset();
	$form.removeClass("edit");
	var $submitBtn = $form.find("[type='submit']");
	$submitBtn.text("新建价格");
	$("#quoteModal .modal-title").text("新建公司价格");
	$form.unbind("submit");
	$form.submit(function(){
		if (!Common.checkform(this)) {
			return false;
		}
		$submitBtn.button('loading');
		var formdata = $(this).serializeJson();
		delete formdata.id;
		$.ajax( {
			url :"quoteprice/add",
			type :'POST',
			data: $.param(formdata),
			success : function(result) {
				if (result.success) {
					Common.Tip("添加成功", function() {
						location.reload();
					});
				} else {
					Common.Error("添加失败：" + result.message);
				}
				$submitBtn.button('reset');
			},
			error : function(result) {
				Common.Error("请求服务器过程中出错:" + result.responseText);
				$submitBtn.button('reset');
			}
		});
		return false;
	});
}
function setUpdateQuote(result){
	var $form = $(".quoteform");
	$form[0].reset();
	Common.fillForm($form[0],result);
	changeCompany(result.companyId);
	$("#salesmanId").val(result.salesmanId);
	$form.addClass("edit");
	var $submitBtn = $form.find("[type='submit']");
	$submitBtn.text("修改价格");
	$("#quoteModal .modal-title").text("修改公司价格");
	
// $form.find(".switch_add").unbind("click");
// $form.find(".switch_add").bind("click",function(){
// setAddSalesman();
// return false;
// });
	
	$form.unbind("submit");
	$form.submit(function(){
		if(!Common.checkform(this)){
			return false;
		}
		$submitBtn.button('loading');
		var formdata = $(this).serialize();
		$.ajax({
            url: "quoteprice/put",
            type: 'POST',
            data: formdata,
        })
            .done(function(result) {
            	if(result.success){
            		Common.Tip("修改成功",function(){
            			location.reload();
            		});
            		
            	}else{
            		Common.Error("修改失败：" + result.message);
            	}
            })
            .fail(function(result) {
            	Common.Error("请求服务器过程中出错:" + result.responseText);
            })
            .always(function() {
            	$submitBtn.button('reset');
            	
            });
		return false;
	});
}