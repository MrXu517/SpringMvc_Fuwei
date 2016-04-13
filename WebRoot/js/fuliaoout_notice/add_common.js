$(document).ready( function() {
	/* 设置当前选中的页 */
	/*设置当前选中的页*/
	var $a = $("#left li a[href='fuliao_workspace/commonfuliao']");
	setActiveLeft($a.parent("li"));
	/* 设置当前选中的页 */
	$(".quantity").bind("blur",function(){
		if(this.value == ""){
			this.value = 0 ;
		}
	});
	$(".quantity").click(function(){
		$(this).focus();
		$(this).select();
	});
	$(".checkBtn").change(function(){
		var checked = this.checked;
		if(checked){//若选中
			var $tr = $(this).closest("tr");
			$tr.removeClass("disable EmptyTr");
			$tr.find("[disabled]").removeAttr("disabled");
			$tr.find(".quantity").focus();
			$tr.find(".quantity").select();
		}else{
			$(this).closest("tr").addClass("disable EmptyTr");
			$(this).closest("tr").find(".quantity").attr("disabled",true);
			$(this).closest("tr").find(".memo").attr("disabled",true);
		}
	});
	
	//2015-4-3 添加自动focus到第一个可输入input、select
	$("form").find(".quantity").not("[readonly],[disabled]").first().click();
	//2015-4-3 添加自动focus到第一个可输入input、select
		var storInGrid = new OrderGrid({
			tipText:"辅料出库通知单",
			url:"fuliaoout_notice/add_common",
			postUrl:"fuliaoout_notice/add_common",
			$content:$(".body"),
			donecall:function(result){
				Common.Tip("请打印辅料出库通知单", function() {
					location.href = "fuliaoout_notice/detail/" + result.id;
				});
			},
			tbOptions:{
				tableEle : $(".detailTb")[0],
				showNoOptions : {
					width :'5%',
					display :false
				},
				colnames : [
						{
							name :'quantity',
							colname :'数量',
							width :'30%'
						},{
							name :'memo',
							colname :'备注',
							width :'30%'
						}]
			}
			
		});
		// 公司-业务员级联
		$("#companyId").change( function() {
			changeCompany(this.value);
		});
		// 公司-业务员级联
	});
function changeCompany(companyId) {
	var companyName = $("#companyId").val();
	var companySalesmanMap = $("#companyId").attr("data");
	companySalesmanMap = $.parseJSON(companySalesmanMap).companySalesmanMap;
	var SalesNameList = companySalesmanMap[companyName];
	$("#salesmanId").empty();
	var frag = document.createDocumentFragment();
	var option = document.createElement("option");
	option.value = "";
	option.text = "未选择";
	frag.appendChild(option);
	if (SalesNameList) {
		for ( var i = 0; i < SalesNameList.length; ++i) {
			var salesName = SalesNameList[i];
			var option = document.createElement("option");
			option.value = salesName.id;
			option.text = salesName.name;
			frag.appendChild(option);
		}
	}

	$("#salesmanId").append(frag);
}
	