$(document).ready( function() {
			/* 设置当前选中的页 */
			var $a = $("#left li a[href='order/index']");
			setActiveLeft($a.parent("li"));
			/* 设置当前选中的页 */

				// 订单明细
				var orderGrid = new OrderGrid(
						{
							$content :$("#orderDetail"),
							tbOptions : {
								$dialog :$("#orderDetailDialog"),
								colnames : [
										{
											name :'color',
											colname :'颜色',
											width :'10%'
										},
										{
											name :'weight',
											colname :'克重(g)',
											width :'10%'
										},{
											name :'produce_weight',
											colname :'机织克重(g)',
											width :'10%'
										},
										{
											name :'yarn_name',
											colname :'纱线种类',
											width :'10%'
										},
										{
											name :'size',
											colname :'尺寸',
											width :'10%'
										},
										{
											name :'quantity',
											colname :'生产数量',
											width :'10%'
										},
										{
											name :'price',
											colname :'单价',
											width :'10%'
										},
										{
											name :'_handle',
											colname :'操作',
											width :'15%',
											displayValue : function(value,
													rowdata) {
												return "<a class='editRow' href='#'>编辑</a> | <a class='copyRow' href='#'>复制</a> | <a class='deleteRow' href='#'>删除</a>";
											}
										}
										]
							}
						});
				// 2015-2-27添加颜色及数量
				

				var $form = $(".orderform");
				var $submitBtn = $form.find("[type='submit']");
				$form.unbind("submit");
				$form.submit( function() {
					if (!Common.checkform(this)) {
						return false;
					}
					var formdata = $(this).serializeJson();
					// 2015-2-27添加颜色及数量
						var detailTbdata = orderGrid.TableInstance
								.getTableData();
						formdata.details = JSON.stringify(detailTbdata);
						// 2015-2-27添加颜色及数量
						// 获取表格数据
						$submitBtn.button('loading');
						$.ajax( {
							url :"order/put_detail",
							type :'POST',
							data :$.param(formdata),
							success : function(result) {
								if (result.success) {
									Common.Tip("修改订单、计划单明细成功", function() {
										location = "order/detail/" + result.id;
									});
								}
								$submitBtn.button('reset');
							},
							error : function(result) {
								Common.Error("修改订单明细失败：" + result.responseText);
								$submitBtn.button('reset');
							}

						});
						return false;
					});
			});


// 2015-2-27添加颜色及数量
function OrderGrid(settings) {
	var Object = this;
	this.$content = settings.$content || null;
	this.tbOptions = settings.tbOptions || null;
	if (this.tbOptions) {
		this.tbOptions.tableEle = $(this.$content).find(".detailTb")[0];
		if (!this.tbOptions.showNoOptions) {
			this.tbOptions.showNoOptions = {
				width :'5%',
				display :false
			};
		}
	}

	this.$dialog = settings.tbOptions.$dialog || null;
	this.$form = null;
	if (this.$dialog) {
		this.$form = this.$dialog.find(".rowform");
	}

	this.init = function() {
		Object.TableInstance = TableTools.createTableInstance(Object.tbOptions);
		if(Object.$dialog){
			Object.$dialog.on('shown.bs.modal', function () {
				Object.$dialog.find("input[type='text'],select").first().focus();
			});
		}
		this.$content.find(".detailTb .addRow").click( function() {
			Common.resetForm(Object.$form[0]);
			Object.$dialog.find(".modal-title").text("添加一行");
			Object.$dialog.modal();

			Object.$form.unbind("submit");
			Object.$form.bind("submit", function() {
				// 添加一行
					if (!Common.checkform(this)) {
						return false;
					}
					var formdata = $(this).serializeJson();
					var $material = $(this).find("select#material");
					if($material.length > 0){
						formdata.material_name = $material.find("option:selected").text();
					}
					var $yarn = $(this).find("select#yarn");
					if($yarn.length > 0){
						formdata.yarn_name = $yarn.find("option:selected").text();
					}
					var $factoryId = $(this).find("select#factoryId");
					if($factoryId.length > 0){
						formdata.factory_name = $factoryId.find("option:selected").text();
					}
					var $styleId = $(this).find("select#style");
					if($styleId.length > 0){
						formdata.style_name = $styleId.find("option:selected").text();
					}
					
					Object.TableInstance.addRow(formdata);
					Object.$dialog.modal("hide");
					return false;
				});
		});
		this.$content.find(".detailTb").on("click", ".editRow", function() {
			Common.resetForm(Object.$form[0]);
			Object.$dialog.find(".modal-title").text("编辑");
			var $tr = $(this).closest("tr");
			var rowdata = $.parseJSON($tr.attr("data"));
			Common.fillForm(Object.$form[0], rowdata);
			Object.$dialog.modal();
			Object.$form.unbind("submit");
			Object.$form.bind("submit", function() {
				if (!Common.checkform(this)) {
					return false;
				}
				// 修改一行
					var formdata = $(this).serializeJson();
					var $material = $(this).find("select#material");
					if($material.length > 0){
						formdata.material_name = $material.find("option:selected").text();
					}
					var $yarn = $(this).find("select#yarn");
					if($yarn.length > 0){
						formdata.yarn_name = $yarn.find("option:selected").text();
					}
					var $factoryId = $(this).find("select#factoryId");
					if($factoryId.length > 0){
						formdata.factory_name = $factoryId.find("option:selected").text();
					}
					var $styleId = $(this).find("select#style");
					if($styleId.length > 0){
						formdata.style_name = $styleId.find("option:selected").text();
					}
					formdata.id= rowdata.id;
					Object.TableInstance.updateRow(formdata, $tr[0]);
					Object.$dialog.modal("hide");
					return false;
				});
			return false;
		});
		//2015-4-16添加复制一行
		this.$content.find(".detailTb").on("click", ".copyRow", function() {
			if(Object.TableInstance.beforeAdd){
				if (!Object.TableInstance.beforeAdd()) {
					return false;
				}
			}
			Common.resetForm(Object.$form[0]);
			Object.$dialog.find(".modal-title").text("添加一行");

			var $tr = $(this).closest("tr");
			var rowdata = $.parseJSON($tr.attr("data"));
			delete rowdata.id;
			delete rowdata.color;
			delete rowdata.quantity;

			
			Common.fillForm(Object.$form[0],rowdata);
			Object.$dialog.modal();
			Object.$form.unbind("submit");
			Object.$form.bind("submit", function() {
				// 添加一行
				if (!Common.checkform(this)) {
					return false;
				}
				var formdata = $(this).serializeJson();
				var $material = $(this).find("select#material");
				if($material.length > 0){
					formdata.material_name = $material.find("option:selected").text();
				}
				var $yarn = $(this).find("select#yarn");
				if($yarn.length > 0){
					formdata.yarn_name = $yarn.find("option:selected").text();
				}
				var $factoryId = $(this).find("select#factoryId");
				if($factoryId.length > 0){
					formdata.factory_name = $factoryId.find("option:selected").text();
				}
				var $styleId = $(this).find("select#style");
				if($styleId.length > 0){
					formdata.style_name = $styleId.find("option:selected").text();
				}
				
				Object.TableInstance.addRow(formdata);
				Object.$dialog.modal("hide");
				return false;
				});
			return false;
		});

		this.$content.find(".detailTb").on("click", ".deleteRow", function() {
			var rowdata = $.parseJSON($(this).closest("tr").attr("data"));
			if(rowdata.id!=undefined && rowdata.id!=""){
				if(!confirm("原有记录删除时，请确保该明细未开过生产单、工序加工单")){
					return false;
				}
			}
			Object.TableInstance.deleteRow($(this).closest("tr")[0]);
			return false;
		});
	};

	this.init();
}
// 2015-2-27添加颜色及数量
