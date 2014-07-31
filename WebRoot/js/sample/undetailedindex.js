$(document).ready(function(){
	/*设置当前选中的页*/
	var $a = $("#left li a[href='sample/undetailedindex']");
	setActiveLeft($a.parent("li"));
	/*设置当前选中的页*/
	
	//删除 -- 开始
	$(".delete").click(function(){
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "sample/delete/"+id,
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
	//删除 -- 结束
	
	//打样人改变事件
	$("#charge_user").change(function(){
		var value = this.value;
		location = location.pathname+"?charge_user="+this.value;
	});
	//打样人改变事件
	
	//核价按钮 -- 开始
//	var $priceModal = $('#priceDialog>div.modal');
//	$priceModal.on('hidden.bs.modal', function (e) {//核价对话框被隐藏之后触发
//		Common.resetForm($(".priceform")[0]);
//	});
	$(".calcuteDetail").click(function(){
		var sampleId = $(this).closest("tr").attr("sampleid");
		$(".priceform #id").val(sampleId);
		Common.openModal($('#priceDialog .modal'));
		return false;
	});
	//核价按钮 -- 开始
	
	//报价计算器按钮 -- 开始
	var $calculateModal = $('#calculateDialog>div.modal');
	$calculateModal.on('hidden.bs.modal', function (e) {//核价对话框被隐藏之后触发
		Common.resetForm($(".calculateform")[0]);
		//恢复到报价计算器初始阶段
		//1.材料费1的倒纱系数
		$("#bj_dsxishu").attr("disabled",true);
		//2.测试工序恢复到一行，且默认选中第一个
		$("#gongxuTb tbody tr:not(:first)").remove();
		$("#gongxuTb tbody tr").first().find("#bj_gongxu option").first().attr("checked",true);
		//3.材料费2
		$("#bj_2_enable").change();
		//恢复到报价计算器初始阶段
		
	});
	$("#calculateBtn").click(function(){
		Common.openModal($('#calculateDialog .modal'));
		return false;
	});
	//报价计算器按钮 -- 结束
	
	//报价计算器 -- 开始
	
	/*材料费2*/
	//材料费2的倒纱按钮
	$("#bj_ifds_2").unbind("change");
    $("#bj_ifds_2").change(function(){
    	if(this.checked){
    		$("#bj_dsxishu_2").removeAttr("disabled");//倒纱系数2
    	}else{
    		$("#bj_dsxishu_2").attr("disabled","disabled");//倒纱系数2
    	}
    	bj_result_fill();
    });
    //材料费2的倒纱按钮
    
    //材料费2是否加入核价事件：材料费2的所有input,
    $("#bj_2_enable").change(function(){
    	if(this.checked){
    		$("#bj2_fieldsets .form-group input").removeAttr("disabled");
    		$("#bj_ifds_2").change();//需要倒纱2的checkbox
    	}else{
    		$("#bj2_fieldsets .form-group input").attr("disabled","disabled");
    		$("#bj_ifds_2").change();//需要倒纱2的checkbox
    	}
    	bj_result_fill();
    });
    $("#bj_2_enable").change();
   //材料费2是否加入核价事件：材料费2的所有input,
	/*材料费2*/
    
    //材料费1
    $("#bj_ifds").unbind("change");//需要倒纱按钮
    $("#bj_ifds").change(function(){//需要倒纱按钮
    	if(this.checked){
    		$("#bj_dsxishu").removeAttr("disabled");
    	}else{
    		$("#bj_dsxishu").attr("disabled","disabled");
    	}
    	bj_result_fill();
    });
    //材料费1
    /*报价详情计算器*/
    $(".bj_gongxu_add").click(function(){//增加工序
    	var $copiedTr = $("#gongxuTb tbody tr").first();
    	var $new_tr = $copiedTr.clone(true);
    	//$new_tr.find(".bj_gongxu_delete").css("display","inline-block");
    	$new_tr.find("#bj_gongxu").val("");
    	$new_tr.find("#bj_price").val("");
    	$("#gongxuTb tbody").append($new_tr);
    	bj_result_fill();
    	return false;
    });
    $(".bj_gongxu_delete").click(function(){
    	$(this).closest("tr").remove();
    	bj_result_fill();
    });
    
    //设置报价计算器表单中的input select变化时，实时改变报价结果
    $(".calculateform").on("input propertychange change","input,select",function(){
    	bj_result_fill();
    });
    
	function getDoubleValById(id){
		var value = $("#"+id).val();
		if(value == ""){
			return "0";
		}else{
			return value;
		}
	}
	function getDoubleValByVal(value){
		if(value == ""){
			return "0";
		}else{
			return value;
		}
	}
	var priceform_cost = 0 ;//成本
    function bj_result_fill(){
    	$(".calculateform .checkerror").removeClass("checkerror");
    	//验证表格
    	if (!Common.checkform($(".calculateform")[0])) {
			return false;
		}
    	
    	/*材料费1*/
    	var bj_weigth_val = parseFloat(getDoubleValById("bj_weight"));//克重
    	var bj_sunhao_val = parseFloat(getDoubleValById("bj_sunhao"));//损耗
    	var bj_mprice_val = parseFloat(getDoubleValById("bj_mprice"));//
    	var bj_dsxishu_val = parseFloat(getDoubleValById("bj_dsxishu"));//倒纱系数
    	var bj_1 =  bj_weigth_val * bj_sunhao_val *12 * bj_mprice_val /1000;//材料1价格
    	bj_1 = parseFloat(bj_1.toFixed(3));
    	var bj_1_ds = bj_weigth_val * bj_sunhao_val *12 * bj_dsxishu_val/1000;//材料1倒纱
    	bj_1_ds = parseFloat(bj_1_ds.toFixed(3));
    	/*材料费1*/
    	
    	/*材料费2*/
    	var bj_weigth_2_val = parseFloat(getDoubleValById("bj_weight_2"));//克重
    	var bj_sunhao_2_val = parseFloat(getDoubleValById("bj_sunhao_2"));//损耗
    	var bj_mprice_2_val = parseFloat(getDoubleValById("bj_mprice_2"));//
    	var bj_dsxishu_2_val = parseFloat(getDoubleValById("bj_dsxishu_2"));//倒纱系数
    	var bj_2 =  bj_weigth_2_val * bj_sunhao_2_val *12 * bj_mprice_2_val /1000;//材料2价格
    	bj_2 = parseFloat(bj_2.toFixed(3));
    	var bj_2_ds = bj_weigth_2_val * bj_sunhao_2_val *12 * bj_dsxishu_2_val/1000;//材料2倒纱
    	bj_2_ds = parseFloat(bj_2_ds.toFixed(3));
    	/*材料费2*/
   
    
    	var bj_2_enable_checked = $("#bj_2_enable")[0].checked;//是否需要计算材料费2
    	
    	var bj_ps_val = parseFloat(getDoubleValById("bj_ps"));//附加值
    	
    	var result1 = bj_1 ;//结果1 = 材料1价格 + 材料2价格（若选中）
    	if(bj_2_enable_checked){
    		result1 = result1 + bj_2;
    	}
    	result1 = parseFloat(result1.toFixed(3));
    	var result2 = result1 + bj_ps_val;//结果2 = 结果1  + 附加值
    	result2 = parseFloat(result2.toFixed(3));
    	var html1 = "";
    	if(bj_2_enable_checked){
    		html1 = html1 + "材料1：" + bj_weigth_val+"*" + bj_sunhao_val + "*12*"+bj_mprice_val +"÷ 1000"
    				+ "="+bj_1;
    		html1 = html1 + "\n" + 
    				"材料2：" + bj_weigth_2_val+"*" + bj_sunhao_2_val + "*12*"+bj_mprice_2_val +"÷ 1000"
    				+ "="+bj_2;
    		html1 = html1 + "\n" + bj_1 + "+" + bj_2 + "=" + result1 + "+" + bj_ps_val + "=" + result2;
    						
    	}else{
    		html1 = bj_weigth_val+"*" + bj_sunhao_val + "*12*"+bj_mprice_val +"÷ 1000"
    				+ "="+result1 +"+"+bj_ps_val + "="+result2;
    	}
    	
    	var html2 = "\n";
    	var result_bjds = 0;//倒纱价格
    	if($("#bj_ifds")[0].checked){//倒纱价格1
    		result_bjds = bj_1_ds;
    		result_bjds = parseFloat(result_bjds.toFixed(3));
    		html2 =  html2 + result_bjds;
    	}
    	if($("#bj_ifds_2")[0].checked){//倒纱价格2
    		result_bjds = result_bjds + bj_2_ds;
    		result_bjds = parseFloat(result_bjds.toFixed(3));
    		html2 =  html2+ "+" + bj_2_ds + "=" + result_bjds;
    	}
    	
    	var html3 = "\n";
    	var bj_gongxu_eles = $("select[name='bj_gongxu']");//工序select
    	var result3 = 0;
    	for(var i = 0 ; i < bj_gongxu_eles.length ; ++i){
    		var bj_gongxu_ele = bj_gongxu_eles[i]; $("#bj_gongxu option:selected")
    		var $selectedOption = $(bj_gongxu_ele).find("option:selected");
    		var gongxuname = $selectedOption.text();
    		var gongxuid = $selectedOption.val();
    		var gongxuprice = $(bj_gongxu_ele).closest("tr").find("input[name='bj_price']").val();
    		gongxuprice = getDoubleValByVal(gongxuprice);
    		gongxuprice = parseFloat(gongxuprice).toFixed(3);
    		html3 = html3 + gongxuname + ":" + gongxuprice + "\n";
    		result3 = result3 + parseFloat(gongxuprice);
    	}
    	result3 = parseFloat(result3.toFixed(3));
    	html3 = html3 + "____________________________\n";
    	var bj_zzrate_val = parseFloat(getDoubleValById("bj_zzrate"));
    	var bj_lrate_val = parseFloat(getDoubleValById("bj_lrate"));
    	var result4 = result3 + result_bjds;
    	result4 = parseFloat(result4.toFixed(3));
    	var result5 = result4 * bj_zzrate_val;
    	result5 = parseFloat(result5.toFixed(3));
    	var result6 = result5 + result2;
    	result6 = parseFloat(result6.toFixed(3));
    	var result7 = result6/12;
    	result7 = parseFloat(result7.toFixed(3));
    	var result8 = result7 * bj_lrate_val;
    	result8 = parseFloat(result8.toFixed(3));
    	var html4 = result3 + "+" + result_bjds + "\n="
    				+ result4 + "*" + bj_zzrate_val + "\n="
    				+ result5 + "+" + result2 + "\n="
    				+ result6 + "÷12" + "\n="
    				+ result7 + "*" + bj_lrate_val  + "\n="
    				+ result8 + "\n";
    	$("#bj_result").val( html1 + html2 + html3 + html4);
    	
    	priceform_cost =  result7;//设置倒数第二个结果：成本
    }
    
    //报价计算器 -- 保存按钮
    $(".calculateform").submit(function(){
    	var detail = $("#bj_result").val();//计算结果
    	$(".priceform #detail").val(detail);
    	//获取成本
    	$(".priceform #cost").val(priceform_cost);
    	var $model = $('#calculateDialog>div.modal');
    	$model.modal('hide');//关闭报价计算器对话框
    	return false;
    });
    //报价计算器 -- 保存按钮
    
    //核价表单提交 -- 开始
    var $form = $(".priceform");
	var $submitBtn = $form.find("[type='submit']");
	$form.unbind("submit");
	$form.submit( function() {
		if (!Common.checkform(this)) {
			return false;
		}
		var formdata = $(this).serialize();
		var sampleId = $(this).find("#id").val();
		$submitBtn.button('loading');
		$.ajax( {
			url :"sample/setDetail",
			type :'POST',
			data:formdata,
			success : function(result) {
				if (result.success) {
					Common.Tip("核价成功", function() {
						location = "sample/detail/" + sampleId;
					});
				}
				$submitBtn.button('reset');
			},
			error : function(result) {
				Common.Error("核价失败：" + result.responseText);
				$submitBtn.button('reset');
			}

		});
		return false;
	});
    //核价表单提交 -- 结束
    /*报价详情计算器*/
    
	//报价计算器 -- 结束
});
