$(document).ready( function() {
	/* 设置当前选中的页 */
	var $a = $("#left li a[href='sample/add']");
	setActiveLeft($a.parent("li"));
	/* 设置当前选中的页 */
	
	//重置按钮
	$("button.reset").click(function(){
		$form = $(this).closest("form");
		$form[0].reset();
		$form.find("input[type!=hidden],select,textarea").attr("value","");
		$("#previewWidget img").attr("src","");
	});
	//重置按钮
	
	var $form = $(".sampleform");
	var $submitBtn = $form.find("[type='submit']");
	$form.unbind("submit");
	$form.submit( function() {
		if (!Common.checkform(this)) {
			return false;
		}
		var $file = $(this).find("#file");
		var file_v = $file.val();
		if(file_v!=""){
			if(file_v.lastIndexOf(".")<=0){
				Common.Error("添加失败：" + "请上传有效的图片文件，包括 以.bmp,.png,.jpg,.jpeg,.gif为扩展名的文件");
				return false;
			}
			var ext = file_v.substr(file_v.lastIndexOf(".")+1).toLowerCase();
			if(ext != "bmp" & ext != "png" & ext != "jpg" & ext !="jpeg" & ext !="gif"){
				Common.Error("添加失败：" + "请上传有效的图片文件，包括 以.bmp,.png,.jpg,.jpeg,.gif为扩展名的文件");
				return false;
			}
		}
		
		$submitBtn.button('loading');
		$(this).ajaxSubmit( {
			url :"sample/put",
			type :'POST',
			success : function(result) {
				if (result.success) {
					Common.Tip("修改成功", function() {
						location = "sample/undetailedindex";
					});
				}
				$submitBtn.button('reset');
			},
			error : function(result) {
				Common.Error("修改失败：" + result.responseText);
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

// 兼容IE6,IE7,IE8和Firefox的图片上传预览效果 By Zmor
function PreviewPic(obj) {
	var divPreviewId = "previewWidget";
	var picPath = getFullPath(obj);
	var newPreview1 = document.getElementById("previewImg");
	
	var browserVersion= window.navigator.userAgent.toUpperCase();  
	if (obj.files) {// HTML5实现预览，兼容chrome、火狐7+等
		if (window.FileReader) {
			var reader = new FileReader();
			reader.onload = function(e) {
				newPreview1.setAttribute("src",
						e.target.result);
			}
			reader.readAsDataURL(obj.files[0]);
		} else if (browserVersion.indexOf("SAFARI") > -1) {
			alert("不支持Safari6.0以下浏览器的图片预览!");
		}
	} else if (browserVersion.indexOf("MSIE") > -1) {
		if (browserVersion.indexOf("MSIE 6") > -1) {// ie6
			newPreview.setAttribute("src",
					fileObj.value);
		} else {// ie[7-9]
			obj.select();
			if (browserVersion.indexOf("MSIE 9") > -1)
				obj.blur();// 不加上document.selection.createRange().text在ie9会拒绝访问
			var newPreview = document.getElementById(divPreviewId + "New");
			if (newPreview == null) {
				newPreview = document.createElement("div");
				newPreview.setAttribute("id", divPreviewId + "New");
				newPreview.style.width = newPreview1.width
						+ "px";
				newPreview.style.height = newPreview1.height
						+ "px";
				newPreview.style.border = "solid 1px #d2e2e2";
			}
			newPreview.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='scale',src='"
					+ document.selection.createRange().text + "')";
			var tempDivPreview = document.getElementById(divPreviewId);
			tempDivPreview.parentNode.insertBefore(newPreview, tempDivPreview);
			tempDivPreview.style.display = "none";
		}
	} else if (browserVersion.indexOf("FIREFOX") > -1) {// firefox
		var firefoxVersion = parseFloat(browserVersion.toLowerCase().match(
				/firefox\/([\d.]+)/)[1]);
		if (firefoxVersion < 7) {// firefox7以下版本
			newPreview1.setAttribute("src",
					fileObj.files[0].getAsDataURL());
		} else {// firefox7.0+
			newPreview1.setAttribute("src",
					window.URL.createObjectURL(fileObj.files[0]));
		}
	} else {
		newPreview1
				.setAttribute("src", fileObj.value);
	}
}

function getFullPath(obj) {
	if (obj) {
		// IE
		if (window.navigator.userAgent.indexOf("MSIE") >= 1) {
			obj.select();
			obj.blur();
			// IE下取得图片的本地路径
			return document.selection.createRange().text;
		}
		// firefox
		else if (window.navigator.userAgent.indexOf("Firefox") >= 1) {
			if (obj.files) {
				// Firefox下取得的是图片的数据
				return obj.files.item(0).getAsDataURL();
			}
			return obj.value;
		}
		return obj.value;
	}
}
function checkFiles(str, exStr) {
	if (typeof (exStr) == 'undefined')
		exStr = ".jpg|.png|.gif|.jpeg";
	var strRegex = "(" + exStr + ")$"; // 用于验证图片扩展名的正则表达式
	var re = new RegExp(strRegex);
	if (re.test(str.toLowerCase())) {
		return true;
	} else {
		alert("文件名不合法,文件的扩展名必须为" + exStr + "格式");
		return false;
	}
}
