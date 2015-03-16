function OrderGrid(settings){
	var Object = this;
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
		this.gridName = this.$content.attr("id");
		this.$content.find("a.printBtn").attr("href","printorder/print?orderId="+this.orderId+"&gridName=" + this.gridName );
		if(this.tbOptions){
			Object.TableInstance = TableTools.createTableInstance(Object.tbOptions);
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
				Common.fillForm(Object.$form[0],rowdata);
				Object.$dialog.modal();
				Object.$form.unbind("submit");
				Object.$form.bind("submit", function() {
					if (!Common.checkform(this)) {
						return false;
					}
					// 修改一行
					var formdata = $(this).serializeJson();
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
			this.$content.find(".detailTb2 .addRow").click( function() {
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
						Object.TableInstance2.addRow(formdata);
						Object.$dialog2.modal("hide");
						return false;
					});
			});
			this.$content.find(".detailTb2").on("click", ".editRow", function() {
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

}