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
	
	
	this.$dialog = settings.tbOptions.$dialog || null;
	this.$form = null;
	if(this.$dialog){
		this.$form = this.$dialog.find(".rowform");
	}
	
	this.init = function(){
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
	this.$content.find(".saveform").submit(function(){
		if (!Common.checkform(this)) {
			return false;
		}
		var $saveform = $(this);
		var $submitBtn = $(this).find("[type='submit']");
		$submitBtn.button('loading');
		var formdata = $(this).serializeJson();
		var detailTbdata = Object.TableInstance.getTableData();
		if(formdata.id == ""){
			delete formdata.id;
		}
		formdata.details = JSON.stringify(detailTbdata);
		
		if(Object.tbOptions2){
			var detailTbdata2 = Object.TableInstance2.getTableData();
			formdata.details_2 = JSON.stringify(detailTbdata2);
		}
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
	$('#tab>ul.nav-tabs a:first').tab('show') // Select first tab
	
	//质量记录单
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
					} ],
					$dialog:$("#headbankDialog"),
		}
		
	});

	//生产单
	var producingGrid = new OrderGrid({
		url:"order/producingorder",
		$content:$("#producing"),
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
					} ],
					$dialog:$("#producingDialog")
		},
		tbOptions2:{
			colnames : [
			        {
			        	name :'material',
			        	colname :'材料',
			        	width :'20%'
			        },
					{
						name :'color',
						colname :'色号',
						width :'20%'
					},
					{
						name :'quantity',
						colname :'数量',
						width :'20%'
					},
					{
						name :'colorsample',
						colname :'标准色样',
						width :'25%'
					},
					{
						name :'_handle',
						colname :'操作',
						width :'15%',
						displayValue : function(value, rowdata) {
							return "<a class='editRow' href='#'>修改</a> | <a class='deleteRow' href='#'>删除</a>";
						}
					} 
					],
			$dialog:$("#producingDetailDialog")
		}
		
	});
	
	//计划单
	var planGrid = new OrderGrid({
		url:"order/planorder",
		$content:$("#planorder"),
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
						name :'_handle',
						colname :'操作',
						width :'15%',
						displayValue : function(value, rowdata) {
							return "<a class='editRow' href='#'>修改</a> | <a class='deleteRow' href='#'>删除</a>";
						}
					} ],
					$dialog:$("#planDialog")
		}
	});
	
	//原材料仓库单
	var storeGrid = new OrderGrid({
		url:"order/storeorder",
		$content:$("#storeorder"),
		tbOptions:{
			colnames : [
					{
						name :'color',
						colname :'颜色',
						width :'15%'
					},
					{
						name :'material',
						colname :'材料',
						width :'15%'
					},
					{
						name :'quantity',
						colname :'总数量',
						width :'15%'
					},
					{
						name :'yarn',
						colname :'标准样纱',
						width :'15%'
					},
					{
						name :'_handle',
						colname :'操作',
						width :'15%',
						displayValue : function(value, rowdata) {
							return "<a class='editRow' href='#'>修改</a> | <a class='deleteRow' href='#'>删除</a>";
						}
					} ],
					$dialog:$("#storeDialog")
		}
		
	});
	
	//半检记录单
	var halfcheckrecordGrid = new OrderGrid({
		url:"order/halfcheckrecordorder",
		$content:$("#halfcheckrecordorder"),
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
						name :'_handle',
						colname :'操作',
						width :'15%',
						displayValue : function(value, rowdata) {
							return "<a class='editRow' href='#'>修改</a> | <a class='deleteRow' href='#'>删除</a>";
						}
					} ],
					$dialog:$("#halfcheckrecordDialog")
		},
		tbOptions2:{
			colnames : [
			        {
			        	name :'material',
			        	colname :'材料',
			        	width :'20%'
			        },
					{
						name :'color',
						colname :'色号',
						width :'20%'
					},
					{
						name :'colorsample',
						colname :'标准色样',
						width :'25%'
					},
					{
						name :'_handle',
						colname :'操作',
						width :'15%',
						displayValue : function(value, rowdata) {
							return "<a class='editRow' href='#'>修改</a> | <a class='deleteRow' href='#'>删除</a>";
						}
					} 
					],
			$dialog:$("#halfcheckrecordDialog2")
		}
		
	});
	
	//原材料采购单
	var materialPurchaseGrid = new OrderGrid({
		url:"order/materialpurchaseorder",
		$content:$("#materialpurchaseorder"),
		tbOptions:{
			colnames : [
					{
						name :'material',
						colname :'材料品种',
						width :'15%'
					},
					{
						name :'scale',
						colname :'规格',
						width :'15%'
					},
					{
						name :'quantity',
						colname :'数量',
						width :'15%'
					},
					{
						name :'color',
						colname :'颜色',
						width :'15%'
					},
					{
						name :'price',
						colname :'价格（含税）',
						width :'15%'
					},
					{
						name :'_handle',
						colname :'操作',
						width :'15%',
						displayValue : function(value, rowdata) {
							return "<a class='editRow' href='#'>修改</a> | <a class='deleteRow' href='#'>删除</a>";
						}
					} ],
					$dialog:$("#materialpurchaseDialog")
		}
		
	});
	
	//染色单
	var coloringGrid = new OrderGrid({
		url:"order/coloringorder",
		$content:$("#coloringorder"),
		tbOptions:{
			colnames : [
			        {
			        	name :'color',
			        	colname :'色号',
			        	width :'15%'
			        },
					{
						name :'material',
						colname :'材料',
						width :'15%'
					},
					{
						name :'quantity',
						colname :'数量',
						width :'15%'
					},
					
					{
						name :'standardyarn',
						colname :'标准样纱',
						width :'15%'
					},
					{
						name :'_handle',
						colname :'操作',
						width :'15%',
						displayValue : function(value, rowdata) {
							return "<a class='editRow' href='#'>修改</a> | <a class='deleteRow' href='#'>删除</a>";
						}
					} ],
					$dialog:$("#coloringDialog")
		}
		
	});
	
	//抽检记录单
	var checkRecordGrid = new OrderGrid({
		url:"order/checkrecordorder",
		$content:$("#checkrecordorder"),
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
						colname :'订单数量',
						width :'15%'
					},
					{
						name :'_handle',
						colname :'操作',
						width :'15%',
						displayValue : function(value, rowdata) {
							return "<a class='editRow' href='#'>修改</a> | <a class='deleteRow' href='#'>删除</a>";
						}
					} ],
					$dialog:$("#checkrecordDialog"),
		}
		
	});
	
	//辅料采购单
	var fuliaoPurchaseGrid = new OrderGrid({
		url:"order/fuliaopurchaseorder",
		$content:$("#fuliaopurchaseorder"),
		tbOptions:{
		colnames : [
					{
						name :'style',
						colname :'辅料类型',
						width :'15%'
					},
					{
						name :'standardsample',
						colname :'标准样',
						width :'15%'
					},
					{
						name :'quantity',
						colname :'数量',
						width :'15%'
					},
					{
						name :'price',
						colname :'价格(/个)',
						width :'15%'
					},
					{
						name :'end_at',
						colname :'交期',
						width :'15%'
					},
					{
						name :'_handle',
						colname :'操作',
						width :'15%',
						displayValue : function(value, rowdata) {
							return "<a class='editRow' href='#'>修改</a> | <a class='deleteRow' href='#'>删除</a>";
						}
					} ],
					$dialog:$("#fuliaopurchaseDialog"),
		}
		
	});
	
	//车缝记录单
	var carfixRecordGrid = new OrderGrid({
		url:"order/carfixrecordorder",
		$content:$("#carfixrecordorder"),
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
						name :'_handle',
						colname :'操作',
						width :'15%',
						displayValue : function(value, rowdata) {
							return "<a class='editRow' href='#'>修改</a> | <a class='deleteRow' href='#'>删除</a>";
						}
					} ],
					$dialog:$("#carfixrecordDialog"),
		}
		
	});
	
	//整烫记录单
	var ironingRecordGrid = new OrderGrid({
		url:"order/ironingrecordorder",
		$content:$("#ironingrecordorder"),
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
						name :'_handle',
						colname :'操作',
						width :'15%',
						displayValue : function(value, rowdata) {
							return "<a class='editRow' href='#'>修改</a> | <a class='deleteRow' href='#'>删除</a>";
						}
					} ],
					$dialog:$("#ironingrecordDialog"),
		}
		
	});
	
		
});