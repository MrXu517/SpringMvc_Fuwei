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
	
	this.$dialog = settings.$dialog || null;
	this.$form = null;
	if(this.$dialog){
		this.$form = this.$dialog.find(".rowform");
	}
	
	this.init = function(){
		Object.TableInstance = TableTools.createTableInstance(Object.tbOptions);
		this.$content.find(".addRow").click( function() {
			Common.resetForm(Object.$form[0]);
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
		this.$content.on("click", ".editRow", function() {
			Common.resetForm(Object.$form[0]);
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

		this.$content.on("click", ".deleteRow", function() {
			$(this).closest("tr").remove();
			return false;
		});
	};
	
	this.init();
	
	
	this.url = settings.url || "";
	this.$content.find(".saveform").submit(function(){
		var $saveform = $(this);
		var $submitBtn = $(this).find("[type='submit']");
		$submitBtn.button('loading');
		var formdata = $(this).serializeJson();
		var detailTbdata = Object.TableInstance.getTableData();
		if(formdata.id == ""){
			delete formdata.id;
		}
		formdata.details = JSON.stringify(detailTbdata);
		$.ajax( {
			url :Object.url,
			type :'POST',
			data :$.param(formdata),
			success : function(result) {
				if (result.success) {
					$saveform.find("[name='id']").val(result.id);
					Common.Tip("修改当前表格成功", function() {
					});
				}
				$submitBtn.button('reset');
			},
			error : function(result) {
				Common.Error("修改当前表格失败：" + result.responseText);
				$submitBtn.button('reset');
			}

		});
		return false;
	});

}

$(document).ready(function() {
	var headBankGrid = new OrderGrid({
		url:"order/headbank",
		$content:$("#headbank"),
		tbOptions:{
		colnames : [
					{
						name :'color',
						colname :'颜色',
						width :'15%'
					},
					{
						name :'weight',
						colname :'克重(g)',
						width :'15%'
					},
					{
						name :'yarn',
						colname :'纱线种类',
						width :'15%'
					},
					{
						name :'size',
						colname :'尺寸',
						width :'15%'
					},
					{
						name :'quantity',
						colname :'生产数量',
						width :'15%'
					},
					{
						name :'price',
						colname :'价格(/个)',
						width :'15%'
					},
					{
						name :'_handle',
						colname :'操作',
						width :'15%',
						displayValue : function(value, rowdata) {
							return "<a class='editRow' href='#'>修改</a> | <a class='deleteRow' href='#'>删除</a>";
						}
					} ]
		},
		$dialog:$("#headbankDialog"),
	});
// var headBankTbOptions = {
// tableEle :$("#headbank .detailTb")[0],
// showNoOptions : {
// width :'5%',
// display :false
// },
//						
// };
//
// var headBankTableInstance = TableTools
// .createTableInstance(headBankTbOptions);
//					
//					
		
				});