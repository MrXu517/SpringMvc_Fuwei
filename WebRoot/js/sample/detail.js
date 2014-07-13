$(document).ready(function(){
	/*设置当前选中的页*/
	var $a = $("#left li a[href='sample/index']");
	setActiveLeft($a.parent("li"));
	/*设置当前选中的页*/
	
	//公司-业务员级联
	$("#companyId").change(function(){
		var companyName = $(this).val();
		var companySalesmanMap = $(this).attr("data");
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
	});
	$("#companyId").change();
	//公司-业务员级联
	
	
	//创建报价价格 -- 开始
	$quoteModal = $("#quoteModal");
	$quoteModal.on('hidden.bs.modal', function (e) {//核价对话框被隐藏之后触发
		$(".quoteform")[0].reset();
		$("#companyId").change();
	});
	$("#addQuoteBtn").click(function(){
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
	
	var $form = $(".quoteform");
	var $submitBtn = $form.find("[type='submit']");
	$form.unbind("submit");
	$form.submit( function() {
		if (!Common.checkform(this)) {
			return false;
		}
		$submitBtn.button('loading');
		$.ajax( {
			url :"quoteprice/add",
			type :'POST',
			data:$(this).serialize(),
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
	//创建报价 价格-- 结束
	
	//添加到报价列表 -- 开始
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
				$submitBtn.button('reset');
			},
			error : function(result) {
				Common.Error("请求服务器过程中出错:" + result.responseText);
				$submitBtn.button('reset');
			}

		});
		return false;
	});
	//添加到报价列表 -- 结束
	
	//删除价格 -- 开始
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
	//删除价格 -- 结束
});
