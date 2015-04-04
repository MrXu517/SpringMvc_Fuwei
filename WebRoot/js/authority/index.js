$(document).ready( function() {
	/*设置当前选中的页*/
	var $a = $("#left li a[href='authority/index']");
	setActiveLeft($a.parent("li"));
	/*设置当前选中的页*/
	
	var checkedRoleId = null;
	$(".rolelist>a").click( function() {
		$(this).siblings().removeClass("active");
		$(this).addClass("active");
		$("#rolename").text($(this).text());
		// 获取该角色的权限
			var roleId = $(this).attr("rid");
			checkedRoleId = roleId;
			if (roleId == undefined || roleId == "" || roleId == null) {
				return;
			}
			$.ajax( {
				url :"authority/get/" + roleId,
				type :'GET'
			}).done( function(result) {
				fillTree(result);
			}).fail( function(result) {
				Common.Error("获取角色的所有权限失败：" + result.responseText);
			}).always( function() {
			});

			return false;
		});
	$(".rolelist>a").first().click();

	// 保存权限修改 -- 开始
		$("#submit").click( function() {
			if (checkedRoleId == null) {
				return;
			}
			var tree = $.fn.zTree.getZTreeObj("tree");
			var checkednodes = tree.getCheckedNodes(true);
			$.ajax( {
				url :'authority/put',
				type :'POST',
				dataType :'json',
				data : {
					roleId :checkedRoleId,
					authoritys :JSON.stringify(checkednodes)
				}
			}).done( function(result) {
				Common.Tip("修改权限成功",function(){
					$(".rolelist>a.active").click();
				});
				
			}).fail( function(result) {
				Common.Error("修改权限失败：" + result.responseText);
			}).always( function() {
			});
		});
		$("#reset").click( function() {
			$(".rolelist>a.active").click();
		});
		// 保存权限修改 -- 结束
	});

fillTree = function(nodes) {
	var setting = {
		view : {
			showIcon :true,
			dblClickExpand :false
		},
		data : {
			key : {
				checked :"checked",
				name :"cname"
			},
			simpleData : {
				enable :true,
				idKey :"id",
				pIdKey :"pid",
				rootPId :null
			}
		},
		check : {
			enable :true,
			nocheckInherit :true,
			chkboxType : {
				"Y" :"ps",
				"N" :"s"
			}
		}

	};

	var tree = $.fn.zTree.init($("#tree"), setting, nodes);
	var nodes = tree.getNodes();
	for ( var i = 0; i < nodes.length; ++i) {
		tree.expandNode(nodes[i], true, false, false, false);
	}
};