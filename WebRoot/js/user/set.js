$(document).ready( function() {
//	/* 设置当前选中的页 */
//	var $a = $("#left li a[href='sample/add']");
//	setActiveLeft($a.parent("li"));
//	/* 设置当前选中的页 */
//	
	
	var $form = $(".setform");
	var $submitBtn = $form.find("[type='submit']");
	$form.unbind("submit");
	$form.submit( function() {
		if (!Common.checkform(this)) {
			return false;
		}
		var password = $(this).find("#password").val();
		var password2 = $(this).find("#password_2").val();
		if(password==""){
			$(".error").text("原密码不能为空");
			return false;
		}
		if(password!=password2){
			$(".error").text("原密码不一致");
			return false;
		}
		
		var newPassword = $(this).find("#newPassword").val();
		if(newPassword==""){
			$(".error").text("新密码不能为空");
			return false;
		}
		$submitBtn.button('loading');
		$.ajax( {
			url :"user/set",
			type :'POST',
			data:$(this).serialize(),
			success : function(result) {
				if (result.success) {
					Common.Tip("修改密码成功，点击确定重新登录", function() {
						location = "login.jsp";
					});
				} else {
					Common.Error("修改密码失败：" + result.message);
				}
				$submitBtn.button('reset');
			},
			error : function(result) {
				Common.Error("请求服务器过程中出错:" + result.responseText);
				$submitBtn.button('reset');
			}

		});
		return false;
	});

	// 本地预览图片
		$("#file").change( function() {
			PreviewPic(this);
		});
		// 本地预览图片
	});
