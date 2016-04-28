$(document).ready( function() {
	/* 设置当前选中的页 */
	/*设置当前选中的页*/
	var $a = $("#left li a[href='fuliao_workspace/workspace_purchase']");
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
			$(this).closest("tr").find(".quantity,.locationId").attr("disabled",true);
		}
	});
	
	//2015-4-3 添加自动focus到第一个可输入input、select
	$("form").find(".quantity").not("[readonly],[disabled]").first().click();
	//2015-4-3 添加自动focus到第一个可输入input、select
		var storInGrid = new OrderGrid({
			tipText:"自购辅料入库通知单",
			url:"selffuliaoin/add",
			postUrl:"selffuliaoin/add",
			$content:$(".body"),
			donecall:function(result){
				Common.Tip("请打印自购辅料入库通知单", function() {
					location.href = "selffuliaoin/detail/" + result.id;
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
							name :'locationId',
							colname :'库位',
							width :'30%'
						}]
			}
			
		});
		
	});
	