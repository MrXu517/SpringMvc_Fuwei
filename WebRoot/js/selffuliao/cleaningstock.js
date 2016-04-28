$(document).ready(function(){
	/* 设置当前选中的页 */
	var $a = $("#left li a[href='fuliao_workspace/workspace_purchase']");
	setActiveLeft($a.parent("li"));
	/* 设置当前选中的页 */
	
	//批量发货 -- 开始
	//全选
	$("#checkAll").click(function(){
		var checked = this.checked;
		$(".checkbtn").prop("checked",checked);
		$(".checkbtn").change();
	});
	$(".checkbtn").change(function(){
		var checked = this.checked;
		if(checked){
			$(this).closest("tr").addClass("selected");
		}else{
			$(this).closest("tr").removeClass("selected");
		}
	});
		$("#submitBtn").click(function(){
			if(!confirm("是否确定批量执行清空库存操作？")){
				return false;
			}
			var formdata = {};
			var $checkedOrders = $(".checkbtn:checked");
			formdata.ids = "";
			for(var i = 0 ; i < $checkedOrders.length;++i){
				var templocationId = $($checkedOrders[i]).attr("locationId");
				formdata.ids+=templocationId+",";
			}
			formdata.ids = formdata.ids.substring(0,formdata.ids.length-1);
			$.ajax({
	            url: "fuliao_workspace/cleaningstock",
	            type: 'POST',
	            data :$.param(formdata)
	        })
	            .done(function(result) {
	            	if(result.success!=false){
	            		Common.Tip("清空库存成功",function(){
//	            			location.reload();
	            			var ids = result.ids;//新生成的辅料出库单id
	            			location.href="selffuliaoout/detail_batch?ids="+ids;
	            		});
	            	}
	            })
	            .fail(function(result) {
	            	Common.Error("清空库存失败：" + result.responseText);
	            })
	            .always(function() {
	            	
	            });
			return false;
		});
	//批量发货 -- 结束
});