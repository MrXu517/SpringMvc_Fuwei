$(document).ready( function() {
	/* 设置当前选中的页 */
	var $a = $("#left li a[href='notification/index']");
	setActiveLeft($a.parent("li"));
	/* 设置当前选中的页 */
	
	$(".notificationform").submit(function(){
		var $submitBtn = $(this).find("[type='submit']");
		var formdata = $(this).serializeJson();
		var tabledata = [];
		//获取表格数据
		var $trs = $("#detailTable tbody tr");
		for(var i = 0 ; i < $trs.length;++i){
			var $tr = $trs.eq(i);
			var trdata = $.parseJSON($tr.attr("data_detail"));
			tabledata.push(trdata);
		}
		formdata.details = JSON.stringify(tabledata);
		$.ajax({
	        url: "notification/add",
	        type: 'POST',
	        data: $.param(formdata)
	    })
	        .done(function(result) {
	        	if(result.success){
	        		Common.Tip("创建生产单成功",function(){
	        			location.reload();
	        		});
	        	}
	        })
	        .fail(function(result) {
	        	Common.Error("创建生产单失败：" + result.responseText);
	        })
	        .always(function() {
	        	$submitBtn.button('reset');
	        });
		return false;
	});
	
	$addModal = $("#addModal");
	$addModal.on('hidden.bs.modal', function (e) {// 核价对话框被隐藏之后触发
		$(".detailform")[0].reset();
		$(".detailform .checkerror").removeClass("checkerror");
	});
	$(".addRow").click(function(){
		setAddRow();// 设置创建的表单
		Common.openModal($addModal);
		return false;
	});
});

function setAddRow(){
	var $form = $(".detailform");
	$form[0].reset();
	$form.removeClass("edit");
	var $submitBtn = $form.find("[type='submit']");
	$submitBtn.text("确定");
	$("#addModal .modal-title").text("添加一行");
	$form.unbind("submit");
	$form.submit(function(){
		if (!Common.checkform(this)) {
			return false;
		}
		var formdata = $(this).serializeJson();
		addRow(formdata);
		Common.closeModal($addModal);
		return false;
	});
}

function addRow(rowdata){
	$("#detailTable tbody").append("<tr data_detail='" + JSON.stringify(rowdata) + "'" + ">"+
									"<td>" + rowdata.colorCode +"</td>"+
									"<td>" + rowdata.colorStyle +"</td>"+
									"<td>" + rowdata.size +"</td>"+
									"<td class='quantity'>" + rowdata.quantity +"</td>"+
									"<td>" + rowdata.material +"</td>"+
									"<td>" + rowdata.material_quantity +"</td>"+
									"<td>" + rowdata.waste +"</td>"+
									"<td>" + rowdata.total_material +"</td>"+
									"<td>" + rowdata.memo +"</td>"+
									"</tr>");
	total();
}
function total(){
	var quantity = 0 ; 
	var $trs = $("#detailTable tbody tr");
	for(var i = 0 ; i < $trs.length ; ++i){
		var $tr = $trs.eq(i);
		quantity += Number($tr.find(".quantity").text());
	}
	$(".notificationform #quantity").val(quantity);
}