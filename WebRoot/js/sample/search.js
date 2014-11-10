$(document).ready(function(){
	//打样人改变事件
	$("#charge_user").change(function(){
		$(".searchform").submit();
	});
	//打样人改变事件
	
	$("table tbody").on("click","a.confirm",function(){
		//选中某一行，传递回sampleId
		var sampleId = $(this).closest("tr").attr("sampleId");
		parent.searchback(sampleId);
		return false;
	});
	
});