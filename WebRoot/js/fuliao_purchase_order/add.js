$(document).ready( function() {
	/* 设置当前选中的页 */
	var $a = $("#left li a[href='fuliao_purchase_order/add']");
	setActiveLeft($a.parent("li"));
	/* 设置当前选中的页 */
	
	//2015-4-3 添加自动focus到第一个可输入input、select
	$("form").find("input[type='text'],select").not("[readonly],[disabled]").first().focus();
	//2015-4-3 添加自动focus到第一个可输入input、select

	// 重置按钮
		$("button.reset").click( function() {
			$form = $(this).closest("form");
			$form[0].reset();
			$("#previewWidget img").attr("src", "");
		});
		// 重置按钮
		
	
		$("#sampleImgA").click( function() {
			$("#chooseSampleBtn").click();
			return false;
		});
		$("#chooseSampleBtn").click( function() {
			// 出现样品选择弹出框
				var src = $("#sampleIframe").attr("src");
				if (!src || src == "") {
					$("#sampleIframe").attr("src", "sample/search");
				}
				$("#sampleDialog").modal();

				// 设置样品弹出框表格选中之后的操作
				window.searchback = function(sampleId) {
					// 将数据填充到页面上
					$.ajax( {
						url :"sample/get/" + sampleId,
						type :'GET',
						success : function(sample) {
							sample.sampleId = sample.id;
							Common.fillForm($(".saveform")[0], sample);
							$("#sampleImgA").attr("href", "/" + sample.img);
							$("#sampleImg").attr("src", "/" + sample.img_s);
							$("#sampleDialog").modal('hide');
						},
						error : function(result) {
							Common.Error("获取订单详情信息失败：" + result.responseText);
							$("#sampleDialog").modal('hide');
						}
					});
				};
			});
		
		
		var fuliaoPurchaseGrid = new OrderGrid({
			tipText:"辅料采购单",
			url:"fuliao_purchase_order/add",
			postUrl:"fuliao_purchase_order/put",
			$content:$(".fuliaoorderWidget"),
			donecall:function(){
				location.href = "fuliao_purchase_order/index";
			},
			tbOptions:{
				beforeAdd:function(){
					var TableInstance = this;
					var length = $(TableInstance.tableEle).find("tbody tr").length;
					if(length >= 6){
						Common.Tip("不能再添加行，一张采购单最多只能填6条材料信息。您可以保存当前采购单后，再创建一张采购单");
						return false;
					}
					return true;
				},
				colnames : [
							{
								name :'style_name',
								colname :'材料品种',
								width :'15%'
							},
							{
								name :'quantity',
								colname :'数量',
								width :'15%'
							},
							{
								name :'location_size_str',
								colname :'库位容量',
								width :'15%',
								displayValue:function(value,rowdata){
								value = rowdata.location_size;
								if(value == 3){
									return "大";
								}else if(value == 2){
									return "中";
								}else if(value == 1){
									return "小";
								}else{
									return "其他";
								}
							}
							},
							{
								name :'memo',
								colname :'备注',
								width :'30%'
							},
							{
								name :'_handle',
								colname :'操作',
								width :'15%',
								displayValue : function(value, rowdata) {
									return "<a class='copyRow' href='#'>复制</a> | <a class='editRow' href='#'>修改</a> | <a class='deleteRow' href='#'>删除</a>";
								}
							} ],
							$dialog:$("#fuliaopurchaseDialog")
				}
			
		});
		
	});