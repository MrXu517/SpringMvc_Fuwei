function OrderGrid(settings){
	var Object = this;
	Object.donecall = settings.donecall;//2015-3-18添加
	Object.tipText = settings.tipText;//2015-3-18添加
	
	this.$content = settings.$content || null;
	this.tbOptions = settings.tbOptions || null;
	if(this.tbOptions){
		this.tbOptions.tableEle = $(this.$content).find(".detailTb")[0];
		if(!this.tbOptions.showNoOptions){
			this.tbOptions.showNoOptions = {
					width :'5%',
					display :false
				};
		}
	}
	
	this.tbOptions2 = settings.tbOptions2 || null;
	if(this.tbOptions2){
		this.tbOptions2.tableEle = $(this.$content).find(".detailTb2")[0];
		if(!this.tbOptions2.showNoOptions){
			this.tbOptions2.showNoOptions = {
					width :'5%',
					display :false
				};
		}
		this.$dialog2 = settings.tbOptions2.$dialog || null;
		this.$form2 = null;
		if(this.$dialog2){
			this.$form2 = this.$dialog2.find(".rowform");
		}
		
	}
	
	if(this.tbOptions){
		this.$dialog = settings.tbOptions.$dialog || null;
		this.$form = null;
		if(this.$dialog){
			this.$form = this.$dialog.find(".rowform");
		}
	}
	
	
	this.init = function(){
		//2015-1-8添加设置打印按钮的href
		this.orderId = this.$content.find("input[name='orderId']").val();
		this.gridName = this.$content.closest(".tab-pane").attr("id");
		this.$content.find("a.printBtn").attr("href","printorder/print?orderId="+this.orderId+"&gridName=" + this.gridName );
		if(this.tbOptions){
			Object.TableInstance = TableTools.createTableInstance(Object.tbOptions);
			if(Object.$dialog){
				Object.$dialog.on('shown.bs.modal', function () {
					Object.$dialog.find("input[type='text'],select").first().focus();
				});
			}
			this.$content.find(".detailTb .addRow").click( function() {
				if(Object.TableInstance.beforeAdd){
					if (!Object.TableInstance.beforeAdd()) {
						return false;
					}
				}
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
				if(Object.TableInstance.beforeEdit){
					if (!Object.TableInstance.beforeEdit()) {
						return false;
					}
				}
				Common.resetForm(Object.$form[0]);
				Object.$dialog.find(".modal-title").text("编辑");
				var $tr = $(this).closest("tr");
				var rowdata = $.parseJSON($tr.attr("data"));
				Common.fillForm(Object.$form[0],rowdata);
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
					
					Object.TableInstance.updateRow(formdata,$tr[0]);
					Object.$dialog.modal("hide");
					return false;
				});
				return false;
			});

			this.$content.find(".detailTb").on("click", ".deleteRow", function() {
				$(this).closest("tr").remove();
				return false;
			});
		}
		
		
		if(Object.tbOptions2){
			Object.TableInstance2 = TableTools.createTableInstance(Object.tbOptions2);
			if(Object.$dialog2){
				Object.$dialog2.on('shown.bs.modal', function () {
					Object.$dialog2.find("input[type='text'],select").first().focus();
				});
			}
			
			this.$content.find(".detailTb2 .addRow").click( function() {
				if(Object.TableInstance2.beforeAdd){
					if (!Object.TableInstance2.beforeAdd()) {
						return false;
					}
				}
				Common.resetForm(Object.$form2[0]);
				Object.$dialog2.find(".modal-title").text("添加一行");
				Object.$dialog2.modal();

				Object.$form2.unbind("submit");
				Object.$form2.bind("submit", function() {
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
						
						Object.TableInstance2.addRow(formdata);
						Object.$dialog2.modal("hide");
						return false;
					});
			});
			this.$content.find(".detailTb2").on("click", ".editRow", function() {
				if(Object.TableInstance2.beforeEdit){
					if (!Object.TableInstance2.beforeEdit()) {
						return false;
					}
				}
				Common.resetForm(Object.$form2[0]);
				Object.$dialog2.find(".modal-title").text("编辑");
				var $tr = $(this).closest("tr");
				var rowdata = $.parseJSON($tr.attr("data"));
				Common.fillForm(Object.$form2[0],rowdata);
				Object.$dialog2.modal();
				Object.$form2.unbind("submit");
				Object.$form2.bind("submit", function() {
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
					
					Object.TableInstance2.updateRow(formdata,$tr[0]);
					Object.$dialog2.modal("hide");
					return false;
				});
				return false;
			});

			this.$content.find(".detailTb2").on("click", ".deleteRow", function() {
				$(this).closest("tr").remove();
				return false;
			});
		}
		
	};
	
	this.init();
	
	
	this.url = settings.url || "";
	this.postUrl = settings.postUrl;
	this.$content.find(".saveform").submit(function(){
		if (!Common.checkform(this)) {
			return false;
		}
		var $saveform = $(this);
		var $submitBtn = $(this).find("[type='submit']");
		$submitBtn.button('loading');
		var formdata = $(this).serializeJson();
		if(Object.tbOptions){
			var detailTbdata = Object.TableInstance.getTableData();
			formdata.details = JSON.stringify(detailTbdata);
		}		
		
		var url = Object.url;
		var tipText = Object.tipText || "当前表格";
		if(formdata.id == "" || formdata.id == undefined || formdata.id == null){
			delete formdata.id;
			tipText = "创建" + tipText;
		}else{
			tipText = "修改" + tipText;
			url = Object.postUrl || url;
		}
		
		
		if(Object.tbOptions2){
			var detailTbdata2 = Object.TableInstance2.getTableData();
			formdata.details_2 = JSON.stringify(detailTbdata2);
		}
		
		
		$.ajax( {
			url :url,
			type :'POST',
			data :$.param(formdata),
			success : function(result) {
				if (result.success) {
					$saveform.find("[name='id']").val(result.id);
					Common.Tip(tipText+"成功", function() {
						if(Object.donecall){
							Object.donecall(result);
						}
					});
				}
				$submitBtn.button('reset');
			},
			error : function(result) {
				Common.Error(tipText+"失败：" + result.responseText);
				$submitBtn.button('reset');
			}

		});
		return false;
	});
	
	Object.deleteUrl = settings.deleteUrl;//2015-3-31添加
	this.$content.find(".deleteTableBtn").click(function(){
		var $thisBtn = $(this);
		if(Object.deleteUrl!=undefined && Object.deleteUrl!=null && Object.deleteUrl!=""){
			var tableOrderId = Object.$content.find(".saveform [name='id']").val();
			if(tableOrderId == undefined || tableOrderId==""){
				return false;
			}
			if (!confirm("确定要删除该单据吗？")) {
				return false;
			}
			$.ajax( {
				url :Object.deleteUrl + "/" + tableOrderId,
				type :'POST',
				success : function(result) {
					if (result.success) {
						Common.Tip("删除单据成功", function() {
							if(Object.donecall){
								Object.donecall(result);
							}
						});
					}
					$thisBtn.button('reset');
				},
				error : function(result) {
					Common.Error("删除单据失败：" + result.responseText);
					$thisBtn.button('reset');
				}

			});
		}	
		return false;
	});

}