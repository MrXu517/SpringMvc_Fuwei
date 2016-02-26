$(document).ready( function() {
	/* 设置当前选中的页 */
	var $a = $("#left li a[href='order/undelivery']");
	setActiveLeft($a.parent("li"));
	/* 设置当前选中的页 */
	
	
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
			        	className:"input",
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
			        	className:"input positive_int",
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
							return "<a class='deleteRow' href='#'>删除</a>";
						}
					} ],
					$dialog:$("#coloringDialog")
		}
		
	});
	
	//添加原有的行数据
	$("#saveTb tbody tr").remove();
	var temptabledata = $.parseJSON($("#saveTb tbody").attr("data"));
	grid.TableInstance.data = temptabledata;
	grid.TableInstance._fillTableData();
	
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
	
	
});