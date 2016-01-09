$(document).ready( function() {	
	/*设置当前选中的页*/
	var $a = $("#left li a[href='producebill/index']");
	setActiveLeft($a.parent("li"));
	$("#number").focus();
	$("#number").select();
	$("#year").bind("click focus", function() {
		WdatePicker({dateFmt:'yyyy'});
	});
	$(".quantity").bind("blur",function(){
		if(this.value == ""){
			this.value = 0 ;
		}
	});
	$(".quantity").click(function(){
		$(this).focus();
		$(this).select();
	});
	$(".deduct").click(function(){
		$(this).focus();
		$(this).select();
	});
	$(".checkBtn").change(function(){
		var checked = this.checked;
		if(checked){//若选中
			var $tr = $(this).closest("tr.producingTr");
			var $childtrs = $("#contentTb tr[number='" + $tr.attr("number") +"']"); 
			$childtrs.removeClass("disable EmptyTr");
			$childtrs.find("[disabled]").removeAttr("disabled");
			$childtrs.find(".quantity").first().focus();
			$childtrs.find(".quantity").first().select();
		}else{
			var $tr = $(this).closest("tr.producingTr");
			var $childtrs = $("#contentTb tr[number='" + $tr.attr("number") +"']"); 
			$childtrs.addClass("disable EmptyTr");
			$childtrs.find(".quantity").attr("disabled",true);
			$childtrs.find(".deduct").attr("disabled",true);
			$childtrs.find(".memo").attr("disabled",true);
		}
	});
	$(".quantity").bind("input propertychange", function(event) {
        Common.positive_intCheck_Rewrite(this, this.value);
        //修改金额的值
        var quantity = Number(this.value);
        var price = Number($(this).closest("tr").find(".price").attr("price"));
        var $amount = $(this).closest("tr").find(".amount");
        var amount = (quantity * price).toFixed(2);
        $amount.text(amount);
    });
	 $(".scanform").submit(function(){
	    	var formdata = $(this).serializeJson();
	    	var number =formdata.number;
	    	var $tr = $("#contentTb tr.producingTr[number='" + number +"']");
	    	$("tr.scanselected").removeClass("scanselected");
	    	if($tr.length<=0){
	    		$("#scanTip").text("找不到相应的生产单或工序加工单：" + number);
	    		$("#scanTip").removeClass("success");
	    		$("#scanTip").addClass("fail");
	    	}else if($tr.length>1){
	    		$("#scanTip").text("找不到超过一条的相符记录，无法锁定行：" + number);
	    		$("#scanTip").removeClass("success");
	    		$("#scanTip").addClass("fail");
	    	}else{
	    		var $trs = $("#contentTb tr[number='" + number +"']");
	    		$trs.addClass("scanselected");
	    		var $checkbtn = $tr.find(".checkBtn");
	    		if($checkbtn[0].checked){//若当前行已被选中，则无需移动节点到第一行
	    			$("#scanTip").text("已扫描");
	        		$("#scanTip").removeClass("fail");
	        		$("#scanTip").addClass("success");
	    		}else{
	        		$trs.insertBefore($("#contentTb tbody tr").first());    //移动节点到第一行
	        		$("#scanTip").text("扫描成功");
	        		$("#scanTip").removeClass("fail");
	        		$("#scanTip").addClass("success");
	        		$checkbtn.prop("checked","on");
	        		$checkbtn.change();
	    		}
	    		
	    	}
	    	$("#number").focus();
	    	$("#number").select();
	    	return false;
	    });
    var tbOptions = {
    	tableEle : $("#contentTb")[0],
		showNoOptions : {
			width :'5%',
			display :false
		},
		colnames : [{
					name :'quantity',
					colname :'实际入库数量'
				},{
					name :'deduct',
					colname :'扣款金额'
				},{
					name :'memo',
					colname :'备注'
				}]
    };
    var TableInstance = TableTools.createTableInstance(tbOptions);
    
    $("#postform").submit(function(){
    	var $submitBtn = $(this).find("[type='submit']");
		$submitBtn.button('loading');
    	var formdata = $(this).serializeJson();
    	var tabledata=[];
    	var $producingTrs = $("#contentTb .producingTr").not(".EmptyTr");//获取选中的明细行
    	for (var i = 0; i < $producingTrs.length; ++i) {
    		var $producingTr = $producingTrs.eq(i);
	        var detaildata = $.parseJSON($producingTr.attr("producebilldetail"));  
	        var detail_tabledata=[];      
	        var number = $producingTr.attr("number");
	        var $detail_detailtrs = $("#contentTb tr[number='" + number +"']");
	        for(var k = 0 ; k <$detail_detailtrs.length;++k ){
	        	var detail_detail_data = TableInstance.getTrData($detail_detailtrs[k]);
	        	if(k==0){
	        		detaildata.deduct = detail_detail_data.deduct;
	        		if(detaildata.deduct == "" || detaildata.deduct == undefined){
	        			detaildata.deduct = 0;
	        		}
        			detaildata.deduct = Number(detaildata.deduct);
	        		detaildata.memo = detail_detail_data.memo;
	        	}
	        	delete detail_detail_data.deduct;
	        	delete detail_detail_data.memo;
	        	detail_tabledata.push(detail_detail_data);
	        }
	        detaildata.details = JSON.stringify(detail_tabledata);
	        
	        tabledata.push(detaildata);
    	}
		formdata.details = JSON.stringify(tabledata);
    	$.ajax( {
			url :"producebill/put",
			type :'POST',
			data :$.param(formdata),
			success : function(result) {
				if (result.success) {
					var message ="修改生产对账单成功，请导出";
					if(result.message){
						message = message + "<br>重要提示：" + result.message;
					}
					Common.Tip(message, function() {
						location.href="producebill/detail/"+result.id;
					});
				}
				$submitBtn.button('reset');
			},
			error : function(result) {
				Common.Error("修改生产对账单失败：" + result.responseText,function(){
				});
				$submitBtn.button('reset');
			}

		});
    	return false;
    });
});