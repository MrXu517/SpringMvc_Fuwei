
$(document).ajaxError(function(event, jqXHR, ajaxSettings, thrownError) {
    if(jqXHR.responseJSON!=undefined & jqXHR.responseJSON.relogin == true){
    	Common.Error("登录失效，请重新登录",function(){
    		location = "login.jsp?message=登录失效，请重新登录";
    	});
    }
    else if(jqXHR.responseJSON!=undefined & jqXHR.responseJSON.locked == true){
    	Common.Error("您的权限已被修改，请重新登录",function(){
    		location = "login.jsp?message=您的权限已被修改，请重新登录";
    	});
    }
});
var loadingNumbers = 0;
$(document).ajaxSend( function(event, xhr, settings) {
	xhr.setRequestHeader("Cache-Control", "no-cache");
	xhr.setRequestHeader("Pragma", "no-cache"); // IE还需要设置
		if (settings.url.indexOf("?") != -1) {
			settings.url = settings.url + "&_timestmp=" + new Date().getTime();
		} else {
			settings.url = settings.url + "?_timestmp=" + new Date().getTime();
		}
		if (top.Cache != undefined) {
			var token = top.Cache.getToken();
			if (token != undefined) {
				xhr.setRequestHeader("Token", token);
			}
		}

		if (settings.loading != false) {
			Common.loading(event.target);
			// IE中document.toString() == "[object Document]"
			if (event.target.toString() == "[object HTMLDocument]"
					|| event.target.toString() == "[object Document]") { // 加载
																			// 公共加载框时，loadingNumbers+1
				loadingNumbers = loadingNumbers + 1;
			}
		}

	});
$(document).ajaxComplete(
		function(event, xhr, settings) {
			if (settings.loading != false) {
				if (event.target.toString() != "[object HTMLDocument]"
						& event.target.toString() != "[object Document]") { // 某些有上下文引用的ajax请求也执行unload函数
					Common.unloading(event.target);
				} else { // 消除公共的加载框
					loadingNumbers = loadingNumbers - 1;
				}
				if (loadingNumbers <= 0) { // 消除公共的加载框
					Common.unloading(event.target);
				}
			}

		});

/* 插件 */
/* 表单序列化成json对象 */
( function($) {
	$.fn.serializeJson = function() {
		var serializeObj = {};
		var array = this.serializeArray();
		$(array).each(
				function() {
					if (serializeObj[this.name]) {
						if ($.isArray(serializeObj[this.name])) {
							serializeObj[this.name].push(this.value);
						} else {
							serializeObj[this.name] = [
									serializeObj[this.name], this.value ];
						}
					} else {
						serializeObj[this.name] = this.value;
					}
				});
		return serializeObj;
	};
})(jQuery);
( function($) {
	$.fn.serializeJson_Number = function() {
		var serializeObj = {};
		var array = this.serializeArray();
		$(array).each(
				function() {
					var number_value = Number(this.value);
					if (!isNaN(number_value) & this.value != "") {
						this.value = number_value;
					}
					if (serializeObj[this.name]) {
						if ($.isArray(serializeObj[this.name])) {
							serializeObj[this.name].push(this.value);
						} else {
							serializeObj[this.name] = [
									serializeObj[this.name], this.value ];
						}
					} else {
						serializeObj[this.name] = this.value;
					}
				});
		return serializeObj;
	};
})(jQuery);
/* 插件 */

/* 公共的数据 */
var Common = new Object();

/* 公共的数据 */

/* 加载框 */
Common.loading = function(target) {
	if (target == null) {
		return;
	}
	var background = $(target).find(".background");
	var loading = $(target).find(".loading");
	if (background.length > 0 || loading.length > 0) {
		$(background).show();
		$(loading).show();
	} else {
		background = $(".background");
		loading = $(".loading");
		if (background.length > 0 || loading.length > 0) {
			$(background).show();
			$(loading).show();
		} else {
			$(".background,.loading", top.document).show();
		}
	}
};
Common.unloading = function(target) {
	if (target == null) {
		return;
	}
	var background = $(target).find(".background");
	var loading = $(target).find(".loading");
	if (background.length > 0 || loading.length > 0) {
		$(background).hide();
		$(loading).hide();
	} else {
		background = $(".background");
		loading = $(".loading");
		if (background.length > 0 || loading.length > 0) {
			$(background).hide();
			$(loading).hide();
		} else {
			$(".background,.loading", top.document).hide();
		}
	}
};
/* 加载框 */

/* 公共函数 */
// 生成百分数
Common.Percentage = function(value, fixed) {
	if (fixed == undefined) {
		fixed = 2;
	}
	var nvalue = Number(value);
	if (isNaN(nvalue)) {
		return value;
	}
	value = (nvalue * 100).toFixed(fixed) + "%";
	return value;
};
// 字符串转日期
Common.getDate = function(strDate) {
	if (strDate == null || typeof (strDate) == "undefined")
		return strDate;
	var date = eval('new Date(' + strDate.replace(/\d+(?=-[^-]+$)/,
			function(a) {
				return parseInt(a, 10) - 1;
			}).match(/\d+/g) + ')');
	return date;
};
// 四舍五入
Common.round = function(value, precision) {
	var tmp = Math.pow(10, precision);
	return Math.round(value * tmp) / tmp;
};
// 获取文件扩展名
Common.getExt = function(filename) {
	var result = /\.[^\.]+/.exec(filename);
	return result;
};

// 重置表单
Common.resetForm = function(form) {
	form.reset();
	$(form).find("input[type='hidden']").val("");
	$(form).find(".checkerror").removeClass('checkerror');
};
// 打开一个对话框
Common.openiFrameDialog = function(iframe_id, query) {
	var $iframe = $("#" + iframe_id);
	var iframe_url = $iframe.attr("furl") + "?" + query;
	$iframe.attr("src", iframe_url);
	$iframe.closest(".dialog").show();
};
// 加一天，减一天
Common.addDay = function(dateStr, day) {
	var date = new Date(dateStr);
	date = date.valueOf();
	date = date + day * 24 * 60 * 60 * 1000;
	date = new Date(date);
	date = date.getFullYear() + "/" + (date.getMonth() + 1) + "/"
			+ date.getDate();
	return date;
};
// 判断一个字符串数组中是否有重复的
Common.isRepeat = function(array) {
	var hash = {};
	for ( var i in array) {
		if (hash[array[i]]) {
			return true;
		}
		hash[array[i]] = true;
	}
	return false;
};
// 分割，且去掉""
Common.Split = function(str, sep) {
	var splits = str.split(sep);
	var result = [];
	for ( var i = 0; i < splits.length; ++i) {
		if (splits[i] != "") {
			result.push(splits[i]);
		}
	}
	return result;
};

// 获取url的中的参数
Common.urlParams = function(url) {
	if (typeof (url) == "undefined") {
		url = location.href;
	}
	var urlParams = {};
	var para = url;
	if (url.lastIndexOf("?") > -1) {
		para = url.substring(url.lastIndexOf("?") + 1, url.length);
	}
	var arr = para.split("&");
	for ( var i = 0; i < arr.length; i++) {
		if (arr[i].split("=").length < 2) {
			continue;
		}
		urlParams[arr[i].split("=")[0]] = arr[i].split("=")[1];
	}
	return urlParams;
};
// 获取url的中的参数

/* 表单相关 */
Common.fillForm = function(formEle, data) {
	var inputs = $(formEle).find(
			"input[type!='button'][type!='submit'],select,textarea");
	var length = inputs.length;
	for ( var i = 0; i < length; ++i) {
		var inputEle = inputs[i];
		var name = inputEle.name;
		var value = (data[name] != null) ? data[name] : "";
		if (inputEle.type == "radio") {
			value = (value == true) ? "1" : "0";
			if (inputEle.value == value) {
				inputEle.checked = true;
			}
		} else {
			inputEle.value = value;
		}
	}
};
/* 表单相关 */

/* 验证相关 */
// 2014-3-27添加表单输入时的验证
Common.check_writeform = function(formEle) {
	// 验证正整数或0
	$(formEle).find("input.positive_int").bind("keyup", function(event) {
		Common.positive_intCheck_Rewrite(this, this.value);
	});
	// 验证正整数或0
	// 验证浮点数
	$(formEle).find("input.double").bind("keyup", function(event) {
		Common.doubleCheck_Rewrite(this, this.value);
	});
	// 验证浮点数
};
// 2014-3-27添加表单输入时的验证
Common.checkform = function(formEle) {
	var requireles = $(formEle).find(
			"input.require,select.require,textarea.require");
	// 验证必须填的字段
	for ( var i = 0; i < requireles.length; ++i) {
		var requirele = requireles[i];
		if (!Common.requireCheck(requirele, requirele.value)) {
			var requirele_siblingstext = $(requirele).siblings(
					"input[type='text']");
			if (requirele.type == "hidden" & requirele_siblingstext.length > 0) {
				requirele_siblingstext.addClass('checkerror');
				$(requirele_siblingstext).focus();
			} else {
				$(requirele).addClass('checkerror');
				$(requirele).focus();
			}
			return false;
		}
	}
	// 验证浮点数
	var doubleles = $(formEle).find("input.double");
	for (i = 0; i < doubleles.length; ++i) {
		var doublele = doubleles[i];
		if (!Common.doubleCheck(doublele, doublele.value)) {
			$(doublele).addClass('checkerror');
			$(doublele).focus();
			return false;
		}
	}
	// 验证正整数
	var positive_inteles = $(formEle).find("input.positive_int");
	for (i = 0; i < positive_inteles.length; ++i) {
		var positive_intele = positive_inteles[i];
		if (!Common.positive_intCheck(positive_intele, positive_intele.value)) {
			$(positive_intele).addClass('checkerror');
			$(positive_intele).focus();
			return false;
		}
	}

	// //验证整数
	// var inteles = $(formEle).find("input.int");
	// for (i = 0; i < positive_inteles.length; ++i) {
	// var intele = inteles[i];
	// if (!Common.intCheck(intele, intele.value)) {
	// $(intele).addClass('checkerror');
	// $(intele).focus();
	// return false;
	// }
	// }
	return true;
};
Common.doubleCheck = function(ele, value) {
	if (value == "") { // 若为空，则反悔true
		return true;
	}
	// var myRegExp = /^\d+\.?\d*$/; //数字，且只出现0或1次小数点 问号?表示前面的只出现0或1次
	// var myRegExp = /^(-?\d+)(\.\d+)?$/; //浮点数：^(-?\d+)(\.\d+)?$
	// |表示或者 ()|()
	// var myRegExp = /^(-|\d)\d{0,}\.?\d*$/; // 以-或数字开头的浮点数，只有一个小数点
	var myRegExp2 = /^\d+\.?\d*$/; // 正数,不以小数点开头
	var myRegExp3 = /^-\d+\.?\d*$/; // 负数,不以小数点开头
	var myRegExp4 = /^(-?\.)\d*$/; // ;//以小数点.开头的数字

	if (!myRegExp2.test(value) & !myRegExp3.test(value)
			& !myRegExp4.test(value)) {
		// alert("数据格式不正确，请重新输入!");
		$(ele).focus();
		return false;
	} else {
		return true;
	}
};

Common.doubleCheck_Rewrite = function(ele, value) {
	if (value == "") { // 若为空，则反悔true
		return;
	}
	// var myRegExp = /^\d+\.?\d*$/; //数字，且只出现0或1次小数点 问号?表示前面的只出现0或1次
	// var myRegExp = /^(-?\d+)(\.\d+)?$/; //浮点数：^(-?\d+)(\.\d+)?$
	// var myRegExp = /^(-|\d)\d{0,}\.?\d*$/; // 以-或数字开头的浮点数，只有一个小数点
	// /(^-{1})|(^(-?\d+)+\.?\d*)$/
	var myRegExp1 = /^-$/; // value="-"时
	var myRegExp2 = /^\d+\.?\d*$/; // 正数,不以小数点开头
	var myRegExp3 = /^-\d+\.?\d*$/; // 负数,不以小数点开头
	var myRegExp4 = /^(-?\.)\d*$/; // ;//以小数点.开头的数字
	if (!myRegExp1.test(value) & !myRegExp2.test(value)
			& !myRegExp3.test(value) & !myRegExp4.test(value)) {
		value = value.replace(/[^-\d.]/g, ""); // []中表示其中的字符只出现一次/[^(-\d.)]/g
		// 保证只有出现一个.而没有多个.
		value = value.replace(/\.{2,}/g, ".");
		// 保证.只出现一次，而不能出现两次以上
		value = value.replace(".", "$#$").replace(/\./g, "")
				.replace("$#$", ".");

		// -先保留第一个-
		value = value.replace("-", "$#$").replace(/\-/g, "")
				.replace("$#$", "-");
		if (!myRegExp3.test(value) & !myRegExp4.test(value)) {
			value = value.replace(/\-/g, "");
		}
		ele.value = value;
		// if (ele.value != "") {
		// ele.value = Number(value);
		// } else {
		// ele.value = value;
		// }
	}

};
Common.positive_intCheck = function(ele, value) {
	if (value == "") { // 若为空，则反悔true
		return true;
	}
	var myRegExp = /^([0-9][0-9]*)$/; // 数字，且只出现0或1次小数点 问号?表示前面的只出现0或1次
	if (!myRegExp.test(value)) {
		// alert("数据格式不正确，请重新输入!");
		$(ele).focus();
		return false;
	} else {
		return true;
	}
};

Common.positive_intCheck_Rewrite = function(ele, value) {
	if (value == "") { // 若为空，则反悔true
		return;
	}
	var myRegExp = /^([0-9][0-9]*)$/; // 数字，且只出现0或1次小数点 问号?表示前面的只出现0或1次
	if (!myRegExp.test(value)) {
		value = value.replace(/[^\d]/g, ""); // 保证只有数字
		value = value.replace(/^0/g, ""); // 保证第一个数字不是0
		ele.value = value;
	}
};

Common.requireCheck = function(ele, value) {
	if (value == "") {
		// alert("数据不能为空!");
		// $(ele).focus();
		return false;
	}
	return true;
};

Common.intCheck = function(ele, value) {
	if (value == "") { // 若为空，则反悔true
		return true;
	}
	var myRegExp2 = /^\d+\d*$/; // 正整数（包括0）
	var myRegExp3 = /^-\d+\d*$/; // 负整数（包括0）

	if (!myRegExp2.test(value) & !myRegExp3.test(value)) {
		// alert("数据格式不正确，请重新输入!");
		$(ele).focus();
		return false;
	} else {
		return true;
	}
};

Common.intCheck_Rewrite = function(ele, value) {
	if (value == "") { // 若为空，则反悔true
		return;
	}
	var myRegExp1 = /^-$/; // value="-"时
	var myRegExp2 = /^\d+\d*$/; // 正整数（包括0）
	var myRegExp3 = /^-\d+\d*$/; // 负整数（包括0）

	if (!myRegExp1.test(value) & !myRegExp2.test(value)
			& !myRegExp3.test(value)) {
		value = value.replace(/[^-\d]/g, ""); // 保证只有数字或-
		// -先保留第一个-
		value = value.replace("-", "$#$").replace(/\-/g, "")
				.replace("$#$", "-");
		if (!myRegExp2.test(value) & !myRegExp3.test(value)) {
			value = value.replace(/\-/g, "");
		}
		ele.value = value;
	}
};
/* 验证相关 */

/* 判断数组对象相关 */
// 判断是否htmlElement HTML元素
Common.isHTMLElement = function(obj) {
	var d = document.createElement("div");
	try {
		d.appendChild(obj.cloneNode(true));
		return obj.nodeType == 1 ? true : false;
	} catch (e) {
		return false;
	}
};
Common.isObejct = function(obj) {
	return Object.prototype.toString.call(obj) === '[object Object]';
};
Common.isArray = function(obj) {
	return Object.prototype.toString.call(obj) === '[object Array]';
};

Common.fillSelect = function(selectEle, data) {
	$(selectEle).empty();
	var frag = document.createDocumentFragment();
	for ( var prop in data) {
		var option = document.createElement("option");
		option.value = prop;
		option.text = data[prop];
		option.innerText = option.text;
		frag.appendChild(option);
	}
	selectEle.appendChild(frag);
};

/* 公共函数 */

/* 常量数据相关 */
var Constants = {};

/* 常量数据相关 */

/* 表单相关 */
var FormTools = {};
FormTools.checkFrom = function(formEle) {
	var requiredEles = $(formEle).find(".require");
	for ( var i = 0; i < requiredEles.length; ++i) {

	}
	var doubleEles = $(formEle).find(".double");
};
/* 表单相关 */

$(document).ready( function() {
	$(".dialog_icons .close").click( function(event) {
		$(this).closest('.dialog').hide();
		return false;
	});
	$(".date").bind("click focus", function() {
		WdatePicker();
	});
	$("input[tip],textarea[tip]").bind("focus click", function() {
		var tip = $(this).attr("tip");
		if (this.value == tip) {
			this.value = "";
			$(this).removeClass('tip');
		}
	});
	$("input[tip],textarea[tip]").bind("blur", function() {
		var tip = $(this).attr("tip");
		if (this.value == "") {
			this.value = tip;
			$(this).addClass('tip');
		}
	});
		/* 2014-6-11 高度自适应修改 */
		$(window).resize( function(event) {
			_c9_autoheight();
		});
		_c9_autoheight();

		/* 2014-6-11 高度自适应修改 */
	});

function _c9_autoheight() {
	// 设置表格的自适应高度
	$(".auto_height").each(
			function(index, el) {
				$container = $(this).closest('.auto_container');
				if ($container.length <= 0) {
					return;
				}
				var $bottom = $container.find(".auto_bottom");
				var bottom_height = 0;
				$bottom.each( function(index, el) {
					bottom_height = bottom_height + $(this).outerHeight(true); // true表示边距也计算在内
					});

				var height = $container.height() + $container.offset().top
						- $(this).offset().top - bottom_height;
				// height为自适应DOM的outerHeight(true)的高度，而非内容高度，因此还需要减去margin.border等
				var maring_border_height = $(this).outerHeight(true)
						- $(this).height();
				height = height - maring_border_height; // 这才是真实的高度

				$(this).height(height);
			});
	// 设置表格的自适应高度
}

// 2014-7-2 之后添加
Common.Error = function(message, callback) {
	var $model = $('#alert_error>div.modal');
	$model.modal();
	$model.find(".modal-body").html(message);

	var $dialog = $model.find('.modal-dialog');
	var paddingtop = 0;
	$model.show();
	paddingtop = ($model.height() - $dialog.outerHeight()) / 2;
	if (paddingtop < 30) {
		paddingtop = 30;
	}
	$dialog.css("padding-top", paddingtop);
	$model.hide();
	$model.modal({keyboard:true});

	$model.on('hidden.bs.modal', function(e) {
		if(callback!=undefined){
			callback(e);
		}
	});
};
Common.Tip = function(message, callback) {
	var $model = $('#alert_tip>div.modal');

	$model.find(".modal-body").html(message);

	var $dialog = $model.find('.modal-dialog');
	var paddingtop = 0;
	$model.show();
	paddingtop = ($model.height() - $dialog.outerHeight()) / 2;
	if (paddingtop < 30) {
		paddingtop = 30;
	}
	$dialog.css("padding-top", paddingtop);
	$model.hide();
	$model.one('show.bs.modal', function(e) {//show 方法调用之后立即触发该事件
		//添加一个
		$model.after("<div class='modal-backdrop fade in'></div>");
		
	});
	$model.one('hide.bs.modal', function(e) {//hide 方法调用之后立即触发该事件
		$model.siblings(".modal-backdrop").first().remove();
		
	});
	$model.modal({keyboard:true,backdrop:false});

	$model.one('hidden.bs.modal', function(e) {
		if(callback!=undefined){
			callback(e);
		}
		
	});
	
};
Common.openModal = function($modal){//打开对话框并高度居中
	var $dialog = $modal.find('.modal-dialog');
	var paddingtop = 0;
	$modal.show();
	paddingtop = ($modal.height() - $dialog.outerHeight()) / 2;
	if (paddingtop < 30) {
		paddingtop = 30;
	}
	$dialog.css("padding-top", paddingtop);
	$modal.hide();
	$modal.modal({keyboard:true});
}
Common.closeModal = function($modal){//关闭对话框
	$modal.modal('hide');
}
// 2014-7-2 之后添加

