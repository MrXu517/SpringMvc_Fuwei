$(document).ready( function() {
	
	/*设置当前选中的页*/
	var $a = $("#left li a[href='fuliao_workspace/workspace']");
	setActiveLeft($a.parent("li"));
	/* 设置当前选中的页 */
	
		var storInGrid = new OrderGrid({
			tipText:"辅料入库单",
			url:"fuliaoin/add",
			postUrl:"fuliaoin/add",
			$content:$(".body"),
			_beforeSubmit:function(){
				if(!confirm("请仔细确认入库的辅料与数量是否正确，如是请点击 ‘确定’ 进行入库操作。           若否，则请点击取消，并提醒业务员修改‘辅料入库通知单’")){
					return false;
				}
				return true;
			},
			donecall:function(result){
				Common.Tip("请打印辅料入库单 以及 辅料标签，并将标签粘贴在辅料袋上", function() {
					location.href = "fuliaoin/detail/" + result.id;
				});
			},
			tbOptions:{
				tableEle : $(".detailTb")[0],
				showNoOptions : {
					width :'5%',
					display :false
				},
				colnames : [{
							name :'locationId',
							colname :'库位',
							width :'30%'
						}]
			}
			
		});
		
	});
	