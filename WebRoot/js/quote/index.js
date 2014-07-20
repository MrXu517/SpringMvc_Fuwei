$(document).ready(function(){
	/*设置当前选中的页*/
	var $a = $("#left li a[href='quote/index']");
	setActiveLeft($a.parent("li"));
	/*设置当前选中的页*/
	
	//删除报价 -- 开始
	$(".delete").click(function(){
		var id= $(this).attr("data-cid");
		$.ajax({
            url: "quote/delete/"+id,
            type: 'POST'
        })
            .done(function(result) {
            	if(result.success){
            		Common.Tip("删除成功",function(){
            			location.reload();
            		});
            	}else{
            		Common.Error("删除失败：" + result.message);
            	}
            })
            .fail(function(result) {
            	Common.Error("请求服务器过程中出错:" + result.responseText);
            })
            .always(function() {
            	
            });
		return false;
	});
	//删除报价 -- 结束
	
	//级联选中   
	//点击父节点：父节点选中，子节点自动全部选中；父节点点击不选中，子节点自动全部不选中
	$("tr.group-head td>input[type='checkbox']").click(function(){
		var checked = this.checked;
		var $tr = $(this).closest("tr");
		var salesmanId = $tr.attr("sid");
		changeGroup(salesmanId);//同时只能选中一个group
		var $group_bodys = $tr.siblings("tr.group-body[sid='"+salesmanId+"']");
		$group_bodys.find("td>input[type='checkbox']").prop("checked",checked);
	});
	//点击子节点：子节点选中，若此时所有节点都选中，则父节点自动选中；子节点不选中，若此时所有节点都不选中，则父节点自动不选中
	$("tr.group-body td>input[type='checkbox']").click(function(){
		var $tr = $(this).closest("tr");
		var salesmanId = $tr.attr("sid");
		changeGroup(salesmanId);//同时只能选中一个group
		var $group_bodyTrs = $("tr.group-body[sid='"+salesmanId+"']");//所有子元素
		var $group_bodyCheckeds = $group_bodyTrs.find("td>input[type='checkbox']:checked");//所有选中的子元素
		var $group_head = $tr.siblings("tr.group-head[sid='"+salesmanId+"']");
		if($group_bodyCheckeds.length < $group_bodyTrs.length){//如果有一个没选中，则父节点自动不选中
			$group_head.find("td>input[type='checkbox']").prop("checked",false);
		}else{//全部选中，则父节点自动选中
			$group_head.find("td>input[type='checkbox']").prop("checked",true);
		}
	});
	
	//同时只能选中一个group
	function changeGroup(salesmanId){
		$("tbody tr[sid!='"+salesmanId + "']").find("td>input[type='checkbox']").prop("checked",false);
	}
	//级联选中
	
	//生成报价单
	$("#QuoteBtn").click(function(){
		var $checkeds = $("table tr.group-body td>input[type='checkbox']:checked");
		if($checkeds.length<0){
			
		}
		var quoteIds = [];
		var $checkedTrs = $checkeds.closest("tr");
		for(var i = 0 ; i < $checkedTrs.length;++i){
			var $checkedTr = $checkedTrs.eq(i);
			var quoteId = $checkedTr.attr("quoteId");
			quoteIds.push(quoteId);
		}
			$.ajax({
	            url: "quoteorder/add",
	            type: 'POST',
	            data:{ids:quoteIds.toString()}
	        })
	            .done(function(result) {
	            	if(result.success){
	            		Common.Tip("创建报价单成功",function(){
	            			location = "quoteorder/detail/" +result.id;
	            		});
	            	}else{
	            		Common.Error("创建报价单失败：" + result.message);
	            	}
	            })
	            .fail(function(result) {
	            	Common.Error("请求服务器过程中出错:" + result.responseText);
	            })
	            .always(function() {
	            	
	            });
	});
	
	//生成报价单
});
