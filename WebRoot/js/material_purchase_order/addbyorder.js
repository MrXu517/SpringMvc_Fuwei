$(document)
		.ready(
				function() {
					/* 设置当前选中的页 */
					var $a = $("#left li a[href='order/index']");
					setActiveLeft($a.parent("li"));
					/* 设置当前选中的页 */
					
					//2015-4-3 添加自动focus到第一个可输入input、select
					$("form").find("input[type='text'],select").not("[readonly],[disabled]").first().focus();
					//2015-4-3 添加自动focus到第一个可输入input、select

					var materialPurchaseGrid = new OrderGrid(
							{
								tipText :"原材料采购单",
								url :"material_purchase_order/addbyorder",
								$content :$(".materialorderWidget"),
								donecall : function() {
									$(".breadcrumb li.active").prev().children(
											"a").click();
								},
								tbOptions : {
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
												name :'factory_name',
												colname :'领取人',
												width :'15%'
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
												displayValue : function(value,
														rowdata) {
													return "<a class='copyRow' href='#'>复制</a> | <a class='editRow' href='#'>修改</a> | <a class='deleteRow' href='#'>删除</a>";
												}
											} ],
									$dialog :$("#materialpurchaseDialog")
								}

							});

				});