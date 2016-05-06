$(document).ready( function() {
	/* 设置当前选中的页 */
	/*设置当前选中的页*/
	var $a = $("#left li a[href='finishstore_workspace/workspace']");
	setActiveLeft($a.parent("li"));
	/* 设置当前选中的页 */
	$(".quantity,.cartons").bind("blur",function(){
		if(this.value == ""){
			this.value = 0 ;
		}
	});
	$(".quantity,.cartons").click(function(){
		$(this).focus();
		$(this).select();
	});
	
	$(".checkBtn").change(function(){
		var checked = this.checked;
		if(checked){//若选中
			var $tr = $(this).closest("tr");
			$tr.removeClass("disable EmptyTr");
			$tr.find("[disabled]").removeAttr("disabled");
			$tr.find(".cartons").focus();
			$tr.find(".cartons").select();
		}else{
			$(this).closest("tr").addClass("disable EmptyTr");
			$(this).closest("tr").find(".quantity,.cartons").attr("disabled",true);
		}
	});
	$(":radio[name='packingOrderId']").click(function(){
		//若选中
		var $tr = $(this).closest("table").find("tr");
		$tr.find(".checkBtn").removeAttr("disabled");
		$(this).closest("table").addClass("selected");
		$(this).closest("table").removeClass("unselected");
		$(this).closest("table").find(".tip_uncheck").hide();
		$(this).closest("table").find(".tip_check").show();
		
		//其他未选中的table设为disabled
			
		var $table_unchecked = $(this).closest("table").siblings(".detailTb");
		$table_unchecked.removeClass("selected");
		$table_unchecked.addClass("unselected");
		$table_unchecked.find(".tip_uncheck").show();
		$table_unchecked.find(".tip_check").hide();
		var $unchecked_tr = $table_unchecked.find("tr");
		$unchecked_tr.find(".checkBtn").prop("checked",false);
		$unchecked_tr.find(".checkBtn").change();
		$unchecked_tr.addClass("disable EmptyTr");
		$unchecked_tr.find(".quantity,.cartons,.checkBtn").attr("disabled",true);
	});
	$(":radio[name='packingOrderId']").first().click();
	
	//2015-4-3 添加自动focus到第一个可输入input、select
	$("form").find(".quantity").not("[readonly],[disabled]").first().click();
	//2015-4-3 添加自动focus到第一个可输入input、select
		//设置数量的自动计算 , 数量 = 箱数*每箱数量
		$(".detailTb").on("input propertychange","input.cartons",function(event) {
			$tr = $(this).closest("tr");
			var $quantity = $tr.find(".quantity");
			var data = $.parseJSON($tr.attr("data"));
			var $cartons = $tr.find(".cartons");
			
			//var quantity = Number($quantity.val());
			var cartons = Number($cartons.val());
			var per_carton_quantity =data.per_carton_quantity;
			
			var quantity = cartons * per_carton_quantity;
			$quantity.text(quantity);
		});
		
		$(".saveform").submit(function(){
			if (!Common.checkform(this)) {
				return false;
			}
			var $saveform = $(this);
			var $submitBtn = $(this).find("[type='submit']");
			$submitBtn.button('loading');
			var formdata = $(this).serializeJson();
			
			var $table_unchecked = $(".detailTb.selected");
			var tbOptions = {
				tableEle : $table_unchecked[0],
				showNoOptions : {
					width :'5%',
					display :false
				},
				colnames : [
						{
							name :'quantity',
							colname :'数量',
							width :'30%'
						},{
							name :'cartons',
							colname :'箱数',
							width :'30%'
						}]
			}
			var TableInstance = TableTools.createTableInstance(tbOptions);
			var detailTbdata = TableInstance.getTableData();
			for(var i = 0 ; i < detailTbdata.length;++i){
				var item = detailTbdata[i];
				if(item.packingOrderId != formdata.packingOrderId){
					alert("页面错误，装箱单ID不一致");
					$submitBtn.button('reset');
					return false;
				}
			}
			formdata.details = JSON.stringify(detailTbdata);
			var url = "finishstore_in/add";
			var tipText = "";
			if(formdata.id == "" || formdata.id == undefined || formdata.id == null){
				tipText = "创建成品入库单";
			}else{
				tipText = "修改成品入库单";
			}
			if(formdata.id == "" || formdata.id == undefined || formdata.id == null){
				delete formdata.id;
			}else{
				url = "finishstore_in/put";
			}
			
			
			$.ajax( {
				url :url,
				type :'POST',
				data :$.param(formdata),
				success : function(result) {
					if (result.success) {
						$saveform.find("[name='id']").val(result.id);
						var message = tipText+"成功";
						if(result.message){
							message = message + "<br>重要提示：" + result.message;
						}
						Common.Tip(message, function() {
							Common.Tip("请打印成品入库单", function() {
								location.href = "finishstore_in/detail/" + result.id;
							});
						});
					}
					$submitBtn.button('reset');
				},
				error : function(result) {
					Common.Error(tipText+"失败：" + result.responseText,function(){
					});
					$submitBtn.button('reset');
				}

			});
			return false;
		});
		
	});
	