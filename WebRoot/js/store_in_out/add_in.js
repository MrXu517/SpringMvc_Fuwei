$(document).ready( function() {
	/* 设置当前选中的页 */
	/*设置当前选中的页*/
	var $a = $("#left li a[href='workspace/material_workspace']");
	setActiveLeft($a.parent("li"));
	/* 设置当前选中的页 */
	
	//2015-4-3 添加自动focus到第一个可输入input、select
	$("form").find("input[type='text'],select").not("[readonly],[disabled]").first().focus();
	//2015-4-3 添加自动focus到第一个可输入input、select
	
	$(".quantity").bind("blur",function(){
		if(this.value == ""){
			this.value = 0 ;
		}
	});
	$(".packages").bind("blur",function(){
		if(this.value == ""){
			this.value = 1 ;
		}
	});
	$(".checkBtn").change(function(){
		var checked = this.checked;
		if(checked){//若选中
			var $tr = $(this).closest("tr");
			$tr.removeClass("disable EmptyTr");
			$tr.find("[disabled]").removeAttr("disabled");
			$tr.find(".quantity").focus();
			$tr.find(".quantity").select();
		}else{
			$(this).closest("tr").addClass("disable EmptyTr");
			$(this).closest("tr").find(".quantity,.lot_no,.packages").attr("disabled",true);
		}
	});
	
		var storInGrid = new OrderGrid({
			tipText:"原材料入库单",
			url:"store_in/add",
			postUrl:"store_in/add",
			$content:$(".body"),
			donecall:function(result){
				Common.Tip("请打印纱线标签，并粘贴在纱线袋上", function() {
					location.href = "store_in/detail/" + result.id;
				});
			},
			tbOptions:{
				beforeAdd:function(){
					var TableInstance = this;
					var length = $(TableInstance.tableEle).find("tbody tr").length;
					if(length >= 6){
						Common.Tip("不能再添加行，一张入库单最多只能填6条材料信息。您可以保存当前入库单后，再创建一张入库单");
						return false;
					}
					return true;
				},
				colnames : [
				            {
				            	name :'color',
				            	colname :'色号',
				            	width :'15%'
				            },
						{
							name :'material_name',
							colname :'材料品种',
							width :'15%'
						},
						{
							name :'quantity',
							colname :'数量',
							width :'15%'
						},
						{
							name :'lot_no',
							colname :'缸号',
							width :'15%'
						},
						{
							name :'packages',
							colname :'包数',
							width :'15%'
						}
						/*{
							name :'_handle',
							colname :'操作',
							width :'15%',
							displayValue : function(value, rowdata) {
								return "<a class='copyRow' href='#'>复制</a> | <a class='editRow' href='#'>修改</a> | <a class='deleteRow' href='#'>删除</a>";
							}
						} */],
						$dialog:$("#storeDialog")
			}
			
		});
		
	});