$(document).ready( function() {
	/* 设置当前选中的页 */
	var $a = $("#left li a[href='order/undelivery']");
	setActiveLeft($a.parent("li"));
	/* 设置当前选中的页 */
	
	var colors = $.parseJSON($(".detailTb").attr("colors"));
	var grid = new OrderGrid({
		tipText:"装箱单",
		url:"packing_order/add",
		postUrl:"packing_order/put",
		$content:$(".orderWidget"),
		donecall:function(result){
			location.href = "packing_order/detail/"+result.id;
		},
		_beforeSubmit:function(formEle){
//			//去掉有值的行的emptyTr 类
			$(grid.TableInstance.tableEle).find("tbody tr").removeClass("EmptyTr");
			var TableInstance = grid.TableInstance;
			var $trs = $(TableInstance.tableEle).find("tbody tr");
			for(var i=0;i<$trs.length; ++i){
				//若数量/每箱数量 不能除尽，则返回错误。
				var trEle = $trs[i];
				var tempdata = TableInstance.getTrData(trEle);
				var quantity = Number(tempdata.quantity);
				var per_carton_quantity = Number(tempdata.per_carton_quantity);
				if(per_carton_quantity===0){
					$(trEle).find(".per_carton_quantity").addClass("checkerror");
					$("#tip").text("每箱数量不能为0");
					return false;
				}else{
					$(trEle).find(".per_carton_quantity").removeClass("checkerror");
				}
				if( quantity % per_carton_quantity===0){
					$(trEle).find(".quantity").removeClass("checkerror");
				}else{
					$(trEle).find(".quantity").addClass("checkerror");
					$("#tip").text("数量 除以 每箱数量 无法除尽，请确保可以除尽");
					return false;
				}
			}
			return true;
		},
		focusfunc : function($selectedTr) {
		    $selectedTr.focus();
		    $selectedTr.find("input[type='text']").not("[disabled]").first().focus();
		},
		tbOptions:{
			fixedPrecision_Number: 2,
			showTotalRow: true, //是否自动添加合计行
			total_row: ["quantity","cartons","capacity"],
			totalname: {
		        col: 'col1_value',
		        name: "合计"
		    },
			beforeAdd:function(){
				return true;
			},
			showEmptyRow:true,
			colnames : [
			{
				name :'col1_value',
				colname :'列1值',
				width :'15%',
				className:"input"
			},
			{
				name :'col2_value',
				colname :'列2值',
				width :'15%',
				className:"input"
			},
			{
				name :'col3_value',
				colname :'列3值',
				width :'15%',
				className:"input"
			},
			{
				name :'col4_value',
				colname :'列4值',
				width :'15%',
				className:"input"
			},
			        {
			        	name :'color',
			        	colname :'颜色',
			        	width :'15%',
			        	className:"select",
			        	displayValue:function(value, rowData, istotalRow){
							var html = "";
							for(var i = 0; i < colors.length;++i){
								if(value == colors[i]){
									html = html + "<option selected value='" + colors[i] + "'>" + colors[i] + "</option>";
								}else{
									html = html + "<option value='" + colors[i] + "'>" + colors[i] + "</option>";
								}
								
							}
							return html;
						},
			        	require:true
			        },
					{
						name :'quantity',
						colname :'数量',
						width :'15%',
			        	className:"input positive_int",
			        	require:true
					},
					{
						name :'per_carton_quantity',
						colname :'每箱数量',
						width :'15%',
			        	className:"input positive_int",
			        	require:true
					},
					{
						name :'box_L',
						colname :'外箱尺寸L',
						width :'15%',
			        	className:"input double",
			        	require:true
					},
					{
						name :'box_W',
						colname :'外箱尺寸W',
						width :'15%',
			        	className:"input double",
			        	require:true
					},
					{
						name :'box_H',
						colname :'外箱尺寸H',
						width :'15%',
			        	className:"input double",
			        	require:true
					},
					{
						name :'gross_weight',
						colname :'毛重',
						width :'15%',
			        	className:"input double",
			        	require:true
					},
					{
						name :'net_weight',
						colname :'净重',
						width :'15%',
			        	className:"input double",
			        	require:true
					},
					{
						name :'cartons',
						colname :'箱数',
						width :'15%',
			        	require:true,
			        	fixedPrecision:true
					},
					{
						name :'box_number_start',
						colname :'箱数开始',
						width :'15%',
			        	className:"input positive_int",
			        	require:true
					},
					{
						name :'box_number_end',
						colname :'箱数结束',
						width :'15%',
			        	className:"",
			        	require:true
					},
					{
						name :'per_pack_quantity',
						colname :'每包几件',
						width :'15%',
			        	className:"input positive_int",
			        	require:true
					},
					{
						name :'capacity',
						colname :'立方',
						width :'15%',
			        	require:true,
			        	fixedPrecision:true
					},
					{
						name :'_handle',
						colname :'操作',
						width :'15%',
						displayValue : function(value, rowdata) {
							return "<a class='copyRow' href='#'>复制</a> | <a class='deleteRow' href='#'>删除</a>";
						}
					} ],
					$dialog:$("#coloringDialog")
		}
		
	});
	//复制行
	$(grid.TableInstance.tableEle).off("click",".copyRow");
	$(grid.TableInstance.tableEle).on("click",".copyRow",function(event){
		var $tr = $(this).closest("tr");
		var rowdata = grid.TableInstance.getTrData($tr[0]);
		delete rowdata.id;
		grid.TableInstance.addRow(rowdata);
		$(".colable").change();
		return false;
	});
	$(grid.TableInstance.tableEle).off("click",".deleteRow");
	$(grid.TableInstance.tableEle).on("click", ".deleteRow", function() {
		grid.TableInstance.deleteRow($(this).closest("tr")[0]);
		return false;
	});
	
	//设置箱号结束号的自动计算，箱号结束号 = 开始号+箱数-1
	$(grid.TableInstance.tableEle).on("input propertychange","input.box_number_start",function(event) {
		$tr = $(this).closest("tr");
		var $cartons = $tr.find(".cartons");
		var cartons = Number($cartons.text());
		//箱数改变时，自动修改箱号结束号
		var box_number_start = Number($(this).val());
		var box_number_end = box_number_start+cartons-1;
		$tr.find(".box_number_end").text(box_number_end);
	});
	
	//设置箱数的自动计算 , 箱数 = 数量/每箱数量
	$(grid.TableInstance.tableEle).on("input propertychange","input.quantity,input.per_carton_quantity",function(event) {
		$tr = $(this).closest("tr");
		var $quantity = $tr.find("input.quantity");
		var $per_carton_quantity = $tr.find("input.per_carton_quantity");
		var $cartons = $tr.find(".cartons");
		var quantity = Number($quantity.val());
		var per_carton_quantity = Number($per_carton_quantity.val());
		
		var cartons = 0;
		if(per_carton_quantity != 0){
			cartons = Math.ceil(quantity/per_carton_quantity);
		}
		$cartons.text(cartons);
		//箱数改变时，自动修改箱号结束号
		var box_number_start = Number($tr.find(".box_number_start").val());
		if(box_number_start !=0){
			var box_number_end = box_number_start+cartons-1;
			$tr.find(".box_number_end").text(box_number_end);
		}
		grid.TableInstance.changeTotalRow();
	});
	
	//设置立方数的自动计算 , 立方数 = L/100*W/100*H/100*箱数
	$(grid.TableInstance.tableEle).on("input propertychange","input.box_L,input.box_W,input.box_H,input.quantity,input.per_carton_quantity",function(event) {
		$tr = $(this).closest("tr");
		var $boxL = $tr.find("input.box_L");
		var $boxW = $tr.find("input.box_W");
		var $boxH = $tr.find("input.box_H");
		var $cartons = $tr.find(".cartons");
		var $capacity = $tr.find(".capacity");
		var L = Number($boxL.val());
		var W = Number($boxW.val());
		var H = Number($boxH.val());
		var cartons = Number($cartons.text());
		var capacity = L/100 * W/100 * H/100 * cartons;
		capacity = Common.round(Number(capacity),2);
		$capacity.text(capacity);
		grid.TableInstance.changeTotalRow();
	});
	
	//设置快捷键
	$(grid.TableInstance.tableEle).on("keyup","input[type='text']",function(event) {
        event = event || window.event; //IE does not pass the event object
        var keyCode = event.which || event.keyCode; //key property also different
        var $selectedTr = $(this).closest("tr");
        var $targetTr = null;
        var $selectedTd = $(this).closest("td");
        var attribute_name = $selectedTd.attr("attribute_name");
        if (keyCode == 38) { //38 up
        	$targetTr = $selectedTr.prev();
            if ($targetTr.length <= 0) {
            	//$targetTr = $selectedTr;
            	return;
            }
            $targetTr.find("."+attribute_name).focus();
            $targetTr.find("."+attribute_name).select();
        } else if (keyCode == 40) { //40 down
        	$targetTr = $selectedTr.next();
            if ($targetTr.length <= 0) {
            	//$targetTr = $selectedTr;
            	return;
            }
            $targetTr.find("."+attribute_name).focus();
            $targetTr.find("."+attribute_name).select();
        } else if (keyCode == 13) { //ENTER键操作
            selectedTr.dblclick();
            event.preventDefault();
            event.returnValue = false;

            // return false;
        }else if (keyCode == 37) { //37 left
        	$targetTd = $selectedTd.prevAll(".input").first();
            if ($targetTd.length <= 0) {
            	return;
            }

            var $input = $targetTd.find("input[type='text']").not("[disabled]");
            if ($input.length <= 0) {
            	return;
            }
            $input.focus();
            $input.select();
        }else if (keyCode == 39) { //39 right
        	$targetTd = $selectedTd.nextAll(".input").first();
            if ($targetTd.length <= 0) {
            	return;
            }
            var $input = $targetTd.find("input[type='text']").not("[disabled]");
            if ($input.length <= 0) {
            	return;
            }
            $input.focus();
            $input.select();
        }
        // return false;
    });
	$(".EmptyTr").removeClass("EmptyTr");
	$(".addRow").unbind("click");
	$(".addRow").click(function(){
		var addedTr = grid.TableInstance.addEmptyRow();
		$(".colable").change();
		$(addedTr).removeClass("EmptyTr");
	});
	
	$(".colable").change(function(){
		$th = $(this).closest("th");
		var $tr = $(this).closest("tr");
		var thIndex = $tr.find("th").index($th)+1;
		var checked = this.checked;
		var $tds = $("#saveTb tbody tr td:nth-child("+thIndex+")");
		var $inputs = $tds.find("input");
		if(checked){//若选中
			$inputs.removeClass("disable");
			$inputs.removeAttr("disabled");
			$th.find("select.colselect").removeAttr("disabled");
		}else{
			$inputs.addClass("disable EmptyTr");
			$inputs.attr("disabled",true);
			$th.find("select.colselect").prop("disabled",true);
		}
	});
	$(".colable").change();
	
//	$("#previewBtn").click(function(){
//		var file = $(".form #file").val();
//		if(file == ""){
//			alert("请先填写 装箱EXCEL文件！");
//			return false;
//		}
//		var $form = $(".form");
//		$form.attr("target","previewContent");
//		$form.attr("action","packing_order/preview");
//		$form.submit();
//	});
	
//	$("#uploadBtn").click(function(){
//		var file = $(".form #file").val();
//		if(file == ""){
//			alert("请先填写 装箱EXCEL文件！");
//			return false;
//		}
//		var $form = $(".form");	
//		$submitBtn = $("#uploadBtn");
//		$form.ajaxSubmit( {
//			url :"packing_order/add",
//			type :'POST',
//			success : function(result) {
//				if (result.success) {
//					Common.Tip("上传成功", function() {
//						location = "packing_order/detail/" + result.id;
//					});
//				}
//				$submitBtn.button('reset');
//			},
//			error : function(result) {
//				Common.Error("上传失败：" + result.responseText);
//				$submitBtn.button('reset');
//			}
//
//		});
//	});
});