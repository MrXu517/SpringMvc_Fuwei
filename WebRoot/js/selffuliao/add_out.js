$(document).ready( function() {
	
	/*设置当前选中的页*/
	var $a = $("#left li a[href='fuliao_workspace/workspace_purchase']");
	setActiveLeft($a.parent("li"));
	/* 设置当前选中的页 */
	/* 设置当前选中的页 */
	$(".total_quantity").bind("blur",function(){
		if(this.value == ""){
			this.value = 0 ;
		}
	});
	$(".total_quantity").click(function(){
		$(this).focus();
		$(this).select();
	});
	$(".checkBtn").change(function(){
		var checked = this.checked;
		if(checked){//若选中
			var $tr = $(this).closest("tr");
			$tr.removeClass("disable EmptyTr");
			$tr.find(".total_quantity").removeAttr("disabled");
			$tr.find(".total_quantity").focus();
			$tr.find(".total_quantity").select();
		}else{
			$(this).closest("tr").addClass("disable EmptyTr");
			$(this).closest("tr").find(".total_quantity").attr("disabled",true);
		}
	});
	
	//2015-4-3 添加自动focus到第一个可输入input、select
	$("form").find(".total_quantity").not("[readonly],[disabled]").first().click();
	//2015-4-3 添加自动focus到第一个可输入input、select
		var storInGrid = new OrderGrid({
			tipText:"自购辅料出库单",
			url:"selffuliaoout/add",
			postUrl:"selffuliaoout/add",
			$content:$(".body"),
			_beforeSubmit:function(){
				if(!confirm("请仔细确认出库的辅料与数量是否正确，如是请点击 ‘确定’ 进行出库操作。           若否，则请点击取消")){
					return false;
				}
				return true;
			},
			donecall:function(result){
				Common.Tip("请打印自购辅料出库单 以及 自购辅料标签，并将标签粘贴在辅料袋上", function() {
					location.href = "selffuliaoout/detail/" + result.id;
				});
			},
			tbOptions:{
				tableEle : $(".detailTb")[0],
				showNoOptions : {
					width :'5%',
					display :false
				},
				colnames : [{
					name :'quantity',
					colname :'数量',
					width :'30%'
				}]
			}
			
		});
		//出库总数变化时，自动匹配各库位需要多少
		$("input.total_quantity").bind("input propertychange",function(event) {
			var $this = $(this);
			var $tr = $(this).closest("tr");
			var fuliaoPurchaseOrderDetailId = $tr.attr("fuliaoPurchaseOrderDetailId");
			var $locationTrs = $(".detailTb tbody tr[fuliaoPurchaseOrderDetailId='" + fuliaoPurchaseOrderDetailId + "']");
			$locationTrs.find(".quantity").val(0);
			//计算分配当前应在各库位出库的数量
			var total_quantity = this.value;
			
			//1.计算是否可以在一个库位出掉
			var flag_one = false;
			for(var i=0;i< $locationTrs.length;++i){
				var currentTr = $locationTrs[i];
				var trdata = storInGrid.TableInstance.getTrData(currentTr);
				var locationId = trdata.locationId;
				var stock_quantity = trdata.stock_quantity;
				if(stock_quantity >= total_quantity){
					flag_one = true;
					$(currentTr).find(".quantity").val(total_quantity);
					$("#errorTip").hide();
					$this.removeClass("checkerror");
					break;
				}
			}
			

			if(!flag_one){//若不可以在一个库位出掉
				var resultLocations = [];
				var flag_many = false;
				var temp_need_out = total_quantity;
				for(var i=0;i< $locationTrs.length;++i){
					var currentTr = $locationTrs[i];
					var trdata = storInGrid.TableInstance.getTrData(currentTr);
					var locationId = trdata.locationId;
					var stock_quantity = trdata.stock_quantity;//库存量此时stock_quantity 肯定 < quantity					
					var temp_quantity = Math.min(temp_need_out,stock_quantity);								
					var tempObj = {};
					tempObj.quantity = temp_quantity;
					tempObj.tr = currentTr;
					resultLocations.push(tempObj);				
					var temp_need_out = temp_need_out - temp_quantity ;//还需出库的数量
					if(temp_need_out == 0){
						flag_many = true;
						break;
					}
				}
				if(!flag_many){//若库存不足，则报错
					$("#errorTip").text("出库数量：" + total_quantity  +"，库存不足");
					$("#errorTip").hide();
					$("#errorTip").show(500);
					$this.addClass("checkerror");
				}else{//库存足够
					for(var obj in resultLocations){
						$(obj.tr).find(".quantity").val(obj.quantity);
					}
					$("#errorTip").hide();
					$this.removeClass("checkerror");
				}
			}
		});
		
	});
	