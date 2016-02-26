$(document).ajaxError(function(event, jqXHR, ajaxSettings, thrownError) {
    if(jqXHR.responseJSON!=undefined){
    	if(jqXHR.responseJSON.relogin == true){
    		Common.Error("登录失效，请重新登录",function(){
        		location = "login.jsp?message=登录失效，请重新登录";
        	});
    	}else if(jqXHR.responseJSON.locked == true){
    		Common.Error("您的权限已被修改，请重新登录",function(){
        		location = "login.jsp?message=您的权限已被修改，请重新登录";
        	});
    	}
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
//		if (top.Cache != undefined) {
//			var token = top.Cache.getToken();
//			if (token != undefined) {
//				xhr.setRequestHeader("Token", token);
//			}
//		}

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
		var $disabledformeles = this.find("[disabled][formele='true']");
		$disabledformeles.removeAttr("disabled");
		var array = this.serializeArray();
		$disabledformeles.attr("disabled",true);
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
		var $disabledformeles = this.find("[disabled][formele='true']");
		$disabledformeles.removeAttr("disabled");
		var array = this.serializeArray();
		$disabledformeles.attr("disabled",true);
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
		if (data[name] == undefined) {
            continue;
        }
		var value = (data[name] != null) ? data[name] : "";
		if (inputEle.type == "radio") {
			if(value == true || value == "true"){
				value = "1";
			}else if(value == false || value == "false"){
				value = "0";
			}
			
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
	//验证整数
    var inteles = $(formEle).find("input.int");
    for (i = 0; i < inteles.length; ++i) {
        var intele = inteles[i];
        if (!Common.intCheck(intele, intele.value)) {
            $(intele).addClass('checkerror');
            $(intele).focus();
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
    if (value == "") { //若为空，则反悔true
        return true;
    }
    // var myRegExp = /^\d+\.?\d*$/; //数字，且只出现0或1次小数点  问号?表示前面的只出现0或1次
    //var myRegExp = /^(-?\d+)(\.\d+)?$/; //浮点数：^(-?\d+)(\.\d+)?$
    // |表示或者 ()|()
    // var myRegExp = /^(-|\d)\d{0,}\.?\d*$/; //     以-或数字开头的浮点数，只有一个小数点
    var myRegExp2 = /^\d+\.?\d*$/; //正数,不以小数点开头
    var myRegExp3 = /^-\d+\.?\d*$/; //负数,不以小数点开头
    var myRegExp4 = /^(-?\.)\d*$/; //;//以小数点.开头的数字

    if (!myRegExp2.test(value) & !myRegExp3.test(value) & !myRegExp4.test(value)) {
        // alert("数据格式不正确，请重新输入!");
        $(ele).focus();
        return false;
    } else {
        return true;
    }
};

Common.doubleCheck_Rewrite = function(ele, value) {
    if (value == "") { //若为空，则反悔true
        return;
    }
    //var myRegExp = /^\d+\.?\d*$/; //数字，且只出现0或1次小数点  问号?表示前面的只出现0或1次
    //var myRegExp = /^(-?\d+)(\.\d+)?$/; //浮点数：^(-?\d+)(\.\d+)?$
    // var myRegExp = /^(-|\d)\d{0,}\.?\d*$/; //     以-或数字开头的浮点数，只有一个小数点  /(^-{1})|(^(-?\d+)+\.?\d*)$/
    var myRegExp1 = /^-$/; //value="-"时
    var myRegExp2 = /^\d+\.?\d*$/; //正数,不以小数点开头
    var myRegExp3 = /^-\d+\.?\d*$/; //负数,不以小数点开头
    var myRegExp4 = /^(-?\.)\d*$/; //;//以小数点.开头的数字
    if (!myRegExp1.test(value) & !myRegExp2.test(value) & !myRegExp3.test(value) & !myRegExp4.test(value)) {
        value = value.replace(/[^-\d.]/g, ""); //[]中表示其中的字符只出现一次/[^(-\d.)]/g
        //保证只有出现一个.而没有多个.    
        value = value.replace(/\.{2,}/g, ".");
        //保证.只出现一次，而不能出现两次以上    
        value = value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");

        //-先保留第一个-
        value = value.replace("-", "$#$").replace(/\-/g, "").replace("$#$", "-");
        if (!myRegExp3.test(value) & !myRegExp4.test(value)) {
            value = value.replace(/\-/g, "");
        }
        ele.value = value;
        // if (ele.value != "") {
        //     ele.value = Number(value);
        // } else {
        //     ele.value = value;
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

	if($.goup){
        $.goup({
            trigger: 50,
            //bottomOffset: 150,
            //locationOffset: 100,
            title: '返回顶部',
            titleAsText: true,
            $scrollEle:$("#main")
        });
	}

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
	
	//2015-3-4添加数字验证，包括小数点
    $("input.double").keyup(function(event) {
        Common.doubleCheck_Rewrite(this, this.value);
    });
    //2015-3-4添加数字验证，包括小数点
    
  //2015-3-4添加数字验证，包括小数点
    $("input.positive_int").keyup(function(event) {
        Common.positive_intCheck_Rewrite(this, this.value);
    });
    //2015-3-4添加数字验证，包括小数点
		/* 2014-6-11 高度自适应修改 */
		$(window).resize( function(event) {
			_c9_autoheight();
		});
		_c9_autoheight();

		/* 2014-6-11 高度自适应修改 */
		
		/*2015-3-15添加*/
		$(".breadcrumb li a").click(function(){
			location.href = $(this).attr("href");
			return false;
		});
		/*2015-3-15添加*/
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
				
				if(this.tagName.toLocaleUpperCase() == "TABLE"){
					var $caption = $(this).children("caption");
					if($caption.length > 0){
						height = height-$caption.outerHeight();
					}
				}
				
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
	$model.on('shown.bs.modal', function () {
		$model.find(".modal-footer button").first().focus();
	});
	
};
Common.Tip = function(message, callback) {
	var $model = $('#alert_tip>div.modal');

	$model.find(".modal-body").html(message);

	var $dialog = $model.find('.modal-dialog');
	var paddingtop = 0;
	$model.show();
	$model.find(".modal-footer button").first().focus();
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
	$dialog.css("padding-top", 0);
	var paddingtop = 0;
	$modal.show();
	
	margintop = ($modal.height() - $dialog.outerHeight()) / 2;
	if (margintop < 30) {
		margintop = 30;
	}
	//$dialog.css("padding-top", 0);
	$dialog.css("margin-top", margintop);
	$modal.hide();
	$modal.on('shown.bs.modal', function () {
		$modal.find("input[type='text'],select").first().focus();
	});
	$modal.modal({keyboard:true});
	
}
Common.closeModal = function($modal){//关闭对话框
	$modal.modal('hide');
}
// 2014-7-2 之后添加

//2014-11-12添加
/*表格内容 -- 开始*/
var TableTools = {};
TableTools.defaultConfig = {
    fixedPrecision_Number: 2,
    querystring: "",
    locked_query: "",
    tableEle: {}, //表格元素
    fixhead: false,
    tableWidget: null, //绑定头部时，必须有的表格容器
    tableheadWidget: null, //绑定头部时，必须有的头部表格容器
    navWidget: null,
    tableWidget_top_auto: false, //当tableWidget，tableheadWidget都不为null时，tableWidget_top_auto为true时，设置tableheadWidget.top = tableheadWidget.top+tableheadWidget.height
    scrollx: false,
    colnames: [], //列的配置
    showEmptyRow: false, //是否自动添加空行
    fixtotal: false,
    showTotalRow: false, //是否自动添加合计行
    total_row: [],
    showAllTotalRow: false, //是否显示总合计行，分页时显示总合计，而不是该页合计
    totalname: {
        col: 'No',
        name: "合计"
    },
    pagetotalname: {
        col: 'No',
        name: "总合计"
    },
    data: {}, //数据
    ValueClass: "value",
    donecall: function() {}, //获取数据成功的回调函数
    failcall: function() {}, //获取数据失败的回调函数
    haspage: true, //是否分页
    limit: 10,
    start: 0,
    currentpage: 1,
    totalpage: 1,
    totalrecords: 0,
    //绑定事件
    dblclick: function() {}, //双击事件
    _click: function() {}, //单击事件
    showNoOptions: {
        display: true
    },
    focus: true, //表格tr是否可获取焦点
    autoRow1: true, //填充表格后，是否自动选中第一行,
    showrowdata: true, // 是否将行数据显示在DOM TR节点的属性data上
    checked: false, //是否在开头有复选框，true表示有，默认为false，
    rowspan: false, //默认不开启跨行表格模式
    fillNav: function() {
        var tableInstance = this;
        if (!tableInstance.haspage) {
            return;
        }
        var currentpage = tableInstance.currentpage;
        var totalpage = tableInstance.totalpage;
        var totalrecords = tableInstance.totalrecords;
        var nav_info_html = "<span class='currentpage'>" + currentpage +
            "</span>/<span class='totalpage'>" + totalpage +
            "</span>，<span class='totalrecords'>" + totalrecords + "</span>条记录";

        var $navWidget = $(tableInstance.navWidget);
        if ($navWidget.length <= 0) {
            return;
        }
        $navWidget.find(".nav_info").html(nav_info_html);
        $navWidget.find(".pagination>li.currentpage>a").text(currentpage);
        if (currentpage == 1) {
            $navWidget.find(".pagination>li.first").addClass('disabled');
            $navWidget.find(".pagination>li.prev").addClass('disabled');
        } else {
            $navWidget.find(".pagination>li.first").removeClass('disabled');
            $navWidget.find(".pagination>li.prev").removeClass('disabled');
        }
        if (currentpage >= totalpage) {
            $navWidget.find(".pagination>li.last").addClass('disabled');
            $navWidget.find(".pagination>li.next").addClass('disabled');
        } else {
            $navWidget.find(".pagination>li.last").removeClass('disabled');
            $navWidget.find(".pagination>li.next").removeClass('disabled');
        }
    }
};
TableTools.createTableInstance = function(config, isnew) { //isnew为true时，生成一个table dom结构
    var TableInstance = {};
    Common.coverObject(TableInstance, TableTools.defaultConfig, true);
    Common.coverObject(TableInstance, config, true);

    if (isnew) { //若为true，则先生成一个table
        TableInstance._createTable();
    }
    if (TableInstance.showEmptyRow) {
        TableInstance.addEmptyRow();
    }
    if (TableInstance.showTotalRow) {
        TableInstance.addTotalRow();
    }
    //设置total_row的isTotal属性
    var total_row = TableInstance.total_row;
    if (total_row != null & total_row != undefined) {
        for (var i = 0; i < total_row.length; ++i) {
            var colOption = TableInstance.getColOption(total_row[i]);
            if (colOption != null) {
                colOption.isTotal = true;
            }
        }
    }

    TableInstance.init_initCss();
    TableInstance.init_bindEvent();
    TableInstance._fillTableData(); //填充数据

    return TableInstance;
};

TableTools.defaultConfig._createTable = function() {
    var TableInstance = this,
        colnames = TableInstance.colnames;
    var tableEle = document.createElement("table");

    var theadEle = document.createElement("thead");
    var theadTr = TableInstance._createTheadTr();
    theadEle.appendChild(theadTr);

    var tbodyEle = document.createElement("tbody");
    var tfootEle = document.createElement("tfoot");

    tableEle.appendChild(theadEle);
    tableEle.appendChild(tbodyEle);
    tableEle.appendChild(tfootEle);
    TableInstance.tableEle = tableEle;

};
TableTools.defaultConfig.init_initCss = function() {
    var TableInstance = this;
    //2014-3-31添加：如果没有tbody，则添加一个
    var tbody = $(this.tableEle).find("tbody");
    if (tbody.length <= 0) {
        var tbodyEle = document.createElement("tbody");
        TableInstance.tableEle.appendChild(tbodyEle);
    }
    if (TableInstance.focus) {
        $(this.tableEle).find("tbody").attr("tabindex", 1);
    }
    //2014-3-31添加：如果没有tbody，则添加一个
    $(this.tableEle).find('tbody tr:odd').addClass("Odd");
    if (this.fixhead) {
        this.tableWidgetResize();
    }
    if (TableInstance.scrollx & TableInstance.fixhead) {
        // var scrollHeight = TableInstance.tableheadWidget.offsetHeight - TableInstance.tableheadWidget.clientHeight;
        // scrollHeight = -scrollHeight + "px";
        // $(TableInstance.tableWidget).css("margin-top", scrollHeight); 2014-9-24注释掉
    }
    if (this.fixtotal) { //表示表格可以横向滚动，此时要么设置表格可滚动，要么设置foot 合计表格可滚动，取决于TableInstance.fixtotal
        $(TableInstance.foot_widget).css("overflow-x", "auto");
        $(TableInstance.tableWidget).css("overflow-x", "hidden");
    }
    if (!this.fixtotal) { //若没固定合计行，则设置表格可滚动
        $(TableInstance.tableWidget).css("overflow-x", "auto");
        $(TableInstance.foot_widget).css("overflow-x", "hidden");
    }
    if (this.tableWidget_top_auto) {
        var offset = $(TableInstance.tableheadWidget).offset();
        var parent_offset = $(TableInstance.tableheadWidget).offsetParent().offset();
        var top = offset.top - parent_offset.top + TableInstance.tableheadWidget.offsetHeight;
        $(TableInstance.tableWidget).css("top", top);
    }

    if (TableInstance.haspage) {
        if (!TableInstance.navWidget) {
            TableInstance.navWidget = $(TableInstance.tableWidget).siblings(".table_nav")[0];
        }
    }
};

TableTools.defaultConfig.init_bindEvent = function() {
    var TableInstance = this;
    //数字验证，整数
    $(TableInstance.tableEle).find("tbody").on("input propertychange", "td.int input[type='text']", function(event) {
        Common.intCheck_Rewrite(this, this.value);
    });
    //数字验证，整数

    //数字验证，没有小数点
    $(TableInstance.tableEle).find("tbody").on("input propertychange", "td.positive_int input[type='text']", function(event) {
        Common.positive_intCheck_Rewrite(this, this.value);
    });
    //数字验证，没有小数点
    //数字验证，包括小数点
    $(TableInstance.tableEle).find("tbody").on("input propertychange", "td.double input[type='text']", function(event) {
        Common.doubleCheck_Rewrite(this, this.value);
    });
    //数字验证，包括小数点
    $(TableInstance.tableEle).find("tbody").on("input propertychange", "input.oninput_change_property", function() {
        //先验证
        var $td = $(this).closest('td');
        if ($td.hasClass('double')) {
            Common.doubleCheck_Rewrite(this, this.value);
        }
        if ($td.hasClass('int')) {
            Common.intCheck_Rewrite(this, this.value);
        }
        if ($td.hasClass('positive_int')) {
            Common.positive_intCheck_Rewrite(this, this.value);
        }
        //先验证
        TableInstance.changeTotalRow();
    });
    $(TableInstance.tableEle).find('tbody').on("click", "tr", function(event) {
        $(this).siblings().removeClass("selected");
        $(this).addClass("selected");
        // $(this).focus();
        // TableInstance.focusfunc($(this));
        if (TableInstance._click != null) {
            TableInstance._click(this, event);
        }
    });
    $(TableInstance.tableEle).find('tbody').on("dblclick", "tr", function() {
        if (TableInstance.dblclick != null) {
            TableInstance.dblclick(this);
        }
    });
    $(window).resize(function() {
        TableInstance.tableWidgetResize();
    });
    if (TableInstance.fixhead & !TableInstance.fixtotal) {
        $(TableInstance.tableWidget).scroll(function() {
            var scrollleft = $(this).scrollLeft();
            $(TableInstance.tableheadWidget).scrollLeft(scrollleft);
            if (TableInstance.foot_widget != undefined) {
                $(TableInstance.foot_widget).scrollLeft(scrollleft);
            }
        });
    }
    if (TableInstance.fixhead & TableInstance.fixtotal) {
        $(TableInstance.foot_widget).scroll(function() {
            var scrollleft = $(this).scrollLeft();
            $(TableInstance.tableheadWidget).scrollLeft(scrollleft);
            $(TableInstance.tableWidget).scrollLeft(scrollleft);
        });
    }

    /*2014-9-24添加 分页导航条*/
    if (TableInstance.haspage) {
        var $navWidget = $(TableInstance.navWidget);
        if ($navWidget.length > 0) {
            $navWidget.find(".pagination>li.prev").click(function() {
                if ($(this).hasClass("disabled")) {
                    return false;
                }
                TableInstance.prevPage();
                return false;
            });
            $navWidget.find(".pagination>li.next").click(function() {
                if ($(this).hasClass("disabled")) {
                    return false;
                }
                TableInstance.nextPage();
                return false;
            });
            $navWidget.find(".pagination>li.first").click(function() {
                if ($(this).hasClass("disabled")) {
                    return false;
                }
                TableInstance.gotoPage(1);
                return false;
            });
            $navWidget.find(".pagination>li.last").click(function() {
                if ($(this).hasClass("disabled")) {
                    return false;
                }
                var totalpage = TableInstance.totalpage;
                TableInstance.gotoPage(totalpage);
                return false;
            });
        }
    }

    /*2014-9-24添加 分页导航条*/
    TableInstance.setShortcuts();


};

//2014-3-31添加表格的快捷键操作
TableTools.defaultConfig.setShortcuts = function() {
    var TableInstance = this;
    $(TableInstance.tableEle).find('tbody').unbind('keyup');
    $(TableInstance.tableEle).find('tbody').keyup(function(event) {
        event = event || window.event; //IE does not pass the event object
        var keyCode = event.which || event.keyCode; //key property also different
        var selectedTr = $(this).find("tr.selected");
        var targetTr = null;
        if (keyCode == 38) { //38 up
            if (selectedTr.length <= 0) {
                targetTr = $(this).find("tr").first();
            } else {
                targetTr = selectedTr.prev();
            }
            if (targetTr.length <= 0) {
                targetTr = $(this).find("tr").last();
            }

            //targetTr.focus();
            TableInstance.focusfunc(targetTr);
            targetTr.click();
        } else if (keyCode == 40) { //40 down
            if (selectedTr.length <= 0) {
                targetTr = $(this).find("tr").first();
            } else {
                targetTr = selectedTr.next();
            }
            if (targetTr.length <= 0) {
                targetTr = $(this).find("tr").first();
            }
            // targetTr.focus();
            TableInstance.focusfunc(targetTr);
            targetTr.click();
        } else if (keyCode == 13) { //ENTER键操作
            selectedTr.dblclick();
            event.preventDefault();
            event.returnValue = false;

            // return false;
        }
        // return false;
    });
    // $(TableInstance.tableEle).find("tbody").off("keyup", "tr.selected");
    // $(TableInstance.tableEle).find("tbody").on("keyup", "tr.selected", function(event) {
    //     event = event || window.event; //IE does not pass the event object
    //     var keyCode = event.which || event.keyCode; //key property also different
    //     if (keyCode == 13) { //ENTER键操作
    //         $(this).dblclick();
    //     }
    //     event.preventDefault();
    //     event.returnValue = false;
    //     return false;
    // });
};
//2014-3-31添加表格的快捷键操作
/*表现宽度等显示相关*/
TableTools.defaultConfig.tableWidgetResize = function() {
    //当头部固定时，才需要重新计算表格的长宽，因为头部固定时，表头表格的宽与内容表格的宽会因为scroll而导致宽度不一致，因此需要重新设置
    if (!this.fixhead) {
        return false;
    }
    var tableheadWidget = this.tableheadWidget;
    var tableWidget = this.tableWidget;
    var workspacetable = $(tableWidget).children("table");
    var tablehead = $(tableheadWidget).children('table');
    //获取表格高度
    // var TableHeight = workspacetable.outerHeight();
    // var DivHeight = $(tabelWidget).outerHeight();

    var ifscroll = false;

    var TableHeight = workspacetable.outerHeight();
    var DivHeight = tableWidget.clientHeight;


    if (TableHeight > DivHeight) {
        ifscroll = true;
    }

    if (ifscroll) {
        $(tableheadWidget).css("overflow-y", "scroll");
        //定义表头宽度
        var TableWidth = workspacetable.outerWidth();
        tablehead.css("width", TableWidth);
    } else {
        tablehead.removeAttr('style');
        $(tableheadWidget).css("overflow", "hidden");
        $(tableheadWidget).css("overflow-y", "hidden");
    }

    $(tableWidget).scrollLeft(0);

    //如果表格有滚动条，则foot也加上滚动条
    var $foot_widget = $(tableWidget).next(".fixedfoot_widget");
    if ($foot_widget.length <= 0) {
        return;
    }
    var foot_table = $foot_widget.children('table');
    if (ifscroll) {
        $foot_widget.css("overflow-y", "scroll");
        var TableWidth_foot = workspacetable.outerWidth();
        foot_table.css("width", TableWidth_foot);
    } else {
        foot_table.removeAttr('style');
        $foot_widget.css("overflow-y", "hidden");
    }


};
TableTools.emptyTable = function(tableEle) {
    $(tableEle).children('tbody').empty();
};
TableTools.defaultConfig.emptyTable = function() {
    TableTools.emptyTable(this.tableEle);
};
TableTools.defaultConfig._fillTableData = function() {
    var TableInstance = this;
    var tableEle = TableInstance.tableEle;
    var colnames = TableInstance.colnames,
        TableData = TableInstance.data,
        tbodyEle = $(tableEle).children('tbody')[0];

    // var k = 0;
    var start = TableInstance.getNo(); /*序号按页面逐页递增*/

    //数据以tr每行加入到table中
    var length = TableData.length;
    var frag = document.createDocumentFragment();

    for (var i = 0; i < length; ++i) {
        var no = start + i;
        var newtrEL = TableInstance._createRow(no, TableData[i]);

        //设置每行tr的data，以便之后的编辑等

        var row_dataJson = JSON.stringify(TableData[i]);
        if (TableInstance.showrowdata) {
            $(newtrEL).attr("data", row_dataJson);
        }
        //设置每行tr的data，以便之后的编辑等
        frag.appendChild(newtrEL);
    }
    //数据以tr每行加入到table中
    if (tbodyEle == undefined) {
        tbodyEle = document.createElement("tbody");
        tableEle.appendChild(tbodyEle);
    }
    tbodyEle.appendChild(frag);
    //   TableInstance.addTotalRow(); //添加合计行
    TableInstance.changeTotalRow();

    TableInstance.tableWidgetResize();

    //如果自动选中第一行为true,则选中第一行
    var first = $(tbodyEle).find("tr").first();
    if (first.length <= 0) {
        TableInstance.fillNav();
        return;
    }
    if (TableInstance.autoRow1) {
        first.click();
    }
    if (TableInstance.focus & TableInstance.autoRow1) {
        TableInstance.focusfunc(first);
        //first.focus();
    }

    TableInstance.fillNav();
};
TableTools.defaultConfig.focusfunc = function($selectedTr) {
    $selectedTr.focus();
    $selectedTr.find("input[type='text']").first().focus();
};
//colnames 中"No"为序号，若没设置"No"则不需显示序号
TableTools.defaultConfig._createTheadTr = function() {
    var TableInstance = this,
        colnames = TableInstance.colnames;
    var trEle = document.createElement("tr");
    //添加序号列 -- 开始
    var TdNo = document.createElement('td');
    if (typeof(showNoOptions) == "undefined") {
        TdNo.style.display = "none";
    } else {
        //设置td的style、class、nowrap -- 开始
        if (typeof(showNoOptions.style) != "undefined") {
            TdNo.style.cssText = showNoOptions.style;
        }
        if (typeof(showNoOptions.className) != "undefined") {
            TdNo.className = showNoOptions.className;
        }

        if (typeof(showNoOptions.nowrap) != "undefined") {
            TdNo.style.cssText = TdNo.style.cssText + nowrapStyle;
        }
        if (typeof(showNoOptions.width) != "undefined") {
            TdNo.width = showNoOptions.width;
        }
        if (typeof(showNoOptions.name) != "undefined") {
            TdNo.appendChild(document.createTextNode(showNoOptions.name));
        }
        //设置td的style、class、nowrap -- 结束     
    }
    trEle.appendChild(TdNo);
    //添加序号列 -- 开始

    var th_length = colnames.length;

    for (var j = 0; j < th_length; ++j) {
        var colOption = colnames[j];
        var th_name = colOption.colname;

        var TdEle = document.createElement('td');
        //设置td的style、class、nowrap -- 开始
        if (typeof(colOption.style) != "undefined") {
            TdEle.style.cssText = colOption.style;
        }
        if (typeof(colOption.className) != "undefined") {
            TdEle.className = colOption.className;
        }

        if (typeof(colOption.nowrap) != "undefined") {
            TdEle.style.cssText = TdEle.style.cssText + nowrapStyle;
        }
        if (typeof(colOption.width) != "undefined") {
            TdEle.width = colOption.width;
        }
        //设置td的style、class、nowrap -- 结束
        TdEle.appendChild(document.createTextNode(th_name));
        trEle.appendChild(TdEle);
    }
    return trEle;
};
TableTools.defaultConfig._createRow = function(i, rowData, istotalRow) {
    rowData._No = i;

    var TableInstance = this;
    var trEle = document.createElement('tr');
    trEle.className = "tr";
    if (TableInstance.focus) {
        $(trEle).attr("tabindex", 1);
    }
    if (!istotalRow) {
        if (i % 2 == 0) {
            trEle.className = trEle.className + " even";
        } else {
            trEle.className = trEle.className + " odd";
        }
    }

    //添加复选框开始 ，若设置checked=true，表示添加复选框。 
    var checked = TableInstance.checked;
    if (checked) {
        var tdChecked = document.createElement('td');
        tdChecked.className = "_checked";
        var input = document.createElement("input");
        input.type = "checkbox";
        tdChecked.appendChild(input);

        var checkOptions = TableInstance.checkOptions;
        if (typeof(checkOptions.displayValue) != "undefined" & !istotalRow) {
            tdChecked.innerHTML = checkOptions.displayValue(i, rowData, istotalRow);
        }

        //设置td的style、class、nowrap -- 开始
        if (typeof(checkOptions.style) != "undefined") {
            tdChecked.style.cssText = checkOptions.style;
        }
        if (typeof(checkOptions.className) != "undefined") {
            tdChecked.className = tdChecked.className + checkOptions.className;
        }

        if (typeof(checkOptions.nowrap) != "undefined") {
            tdChecked.style.cssText = tdChecked.style.cssText + nowrapStyle;
        }
        if (typeof(checkOptions.width) != "undefined") {
            tdChecked.width = checkOptions.width;
        }
        //设置td的style、class、nowrap -- 结束


        trEle.appendChild(tdChecked);
    }
    //添加复选框结束

    var colnames = TableInstance.colnames;
    var showNoOptions = TableInstance.showNoOptions;
    var ValueClass = TableInstance.ValueClass;;
    var nowrapStyle = ";white-space:nowrap;";
    //添加序号列 -- 开始
    var TdNo = document.createElement('td');
    TdNo.className = "_No ";

    if (typeof(showNoOptions) == "undefined") {
        TdNo.style.display = "none";
    } else {
        var no_value = i;
        if (typeof(showNoOptions.displayValue) != "undefined" & !istotalRow) {
            no_value = showNoOptions.displayValue(no_value, rowData, istotalRow);
        }
        TdNo.innerHTML = no_value;

        //设置td的style、class、nowrap -- 开始
        if (typeof(showNoOptions.style) != "undefined") {
            TdNo.style.cssText = showNoOptions.style;
        }
        if (typeof(showNoOptions.className) != "undefined") {
            TdNo.className = TdNo.className + showNoOptions.className;
        }

        if (typeof(showNoOptions.nowrap) != "undefined") {
            TdNo.style.cssText = TdNo.style.cssText + nowrapStyle;
        }
        if (typeof(showNoOptions.width) != "undefined") {
            TdNo.width = showNoOptions.width;
        }
        //设置td的style、class、nowrap -- 结束
    }
    if (showNoOptions.display) {
        trEle.appendChild(TdNo);
    }
    //添加序号列 -- 结束

    //添加各列 -- 开始
    var th_length = colnames.length;

    for (var j = 0; j < th_length; ++j) {

        /*获取相关的数据 -- 开始*/
        var colOption = colnames[j];
        var property_name = colOption.name;
        var th_name = colOption.colname;
        //获取property_value
        var property_value = rowData[property_name];
        if (typeof(colOption.displayValue) != "undefined") {
            property_value = colOption.displayValue(property_value, rowData, istotalRow); //triggerCallBack("displayValue", display_param, colOption);
        }
        if (colOption.fixedPrecision & !isNaN(Number(property_value))) { //小数位使用默认固定的
            property_value = Common.round(Number(property_value), TableInstance.fixedPrecision_Number);
        }
        //获取property_value


        /*获取相关的数据 -- 结束*/

        var TdEle = document.createElement('td');
        //设置td的style、class、nowrap -- 开始
        if (typeof(colOption.style) != "undefined") {
            TdEle.style.cssText = colOption.style;
        }
        if (typeof(colOption.className) != "undefined") {
            TdEle.className = colOption.className;
        }

        if (typeof(colOption.nowrap) != "undefined") {
            TdEle.style.cssText = TdEle.style.cssText + nowrapStyle;
        }
        if (typeof(colOption.width) != "undefined") {
            TdEle.width = colOption.width;
        }
        //设置td的style、class、nowrap -- 结束

        //若是合计行，则无需input
        if (istotalRow) {
            if (colOption.isTotal || colOption.name == TableInstance.totalname.col) {
                TdEle.innerHTML = property_value;
            }
            TdEle.className = TdEle.className + " " + property_name;
            trEle.appendChild(TdEle);
            continue;
        }

        //若是合计行，则无需input

        //设置td的文本内容 -- 开始
        if ($(TdEle).hasClass("input")) {
            var inputEl = document.createElement('input');
            if (colOption.readonly == true) {
                inputEl.readOnly = true;
            }
            inputEl.style.width = TdEle.style.width;
            inputEl.className = ValueClass;
            inputEl.className = inputEl.className + " " + property_name;

            if (colOption.require == true) {
                inputEl.className = inputEl.className + " require ";;
            }
            inputEl.type = "text";

            //将内容设置到文本框里
            inputEl.value = property_value;
            //将内容设置到文本框里

            if (colOption.isTotal) {
                //若要计算合计，则添加类oninput_change_property
                inputEl.className = inputEl.className + " oninput_change_property";
                //若要计算合计，则添加类oninput_change_property
            }
            TdEle.appendChild(inputEl);
        } else if ($(TdEle).hasClass("input-checkbox")) {
            var inputEl = document.createElement('input');
            var class_value = ValueClass + " " + property_name;
            inputEl.className = class_value;
            inputEl.type = "checkbox";
            //将内容设置到文本框里
            inputEl.checked = property_value;
            //将内容设置到文本框里
            if (colOption.disabled) {
                inputEl.disabled = true;
            }
            TdEle.appendChild(inputEl);
        } else if ($(TdEle).hasClass("input_search")) {
            var inputEl = document.createElement('input');
            var class_value = ValueClass + " " + property_name;
            inputEl.className = class_value;
            if (colOption.require == true) {
                inputEl.className = inputEl.className + " require ";;
            }
            inputEl.type = "text";

            //将内容设置到文本框里
            inputEl.value = property_value;
            //将内容设置到文本框里

            if (colOption.isTotal) {
                //若要计算合计，则添加类oninput_change_property
                inputEl.className = inputEl.className + " oninput_change_property";
                //若要计算合计，则添加类oninput_change_property
            }

            var searchbtnele = document.createElement('button');
            searchbtnele.type = "button";
            var s_i = document.createElement("i");
            s_i.className = "fa fa-search";
            searchbtnele.appendChild(s_i);
            if (typeof(colOption.searchbtn_class) != "undefined") {
                searchbtnele.className = colOption.searchbtn_class;
            }
            searchbtnele.className = searchbtnele.className;
            TdEle.appendChild(inputEl);
            TdEle.appendChild(searchbtnele);
        } else if ($(TdEle).hasClass("select")) {
            var selectEle = document.createElement('select');
            if (colOption.readonly == true) {
                selectEle.readOnly = true;
            }
            selectEle.style.width = TdEle.style.width;
            selectEle.className = ValueClass;
            selectEle.className = selectEle.className + " " + property_name;
            if (colOption.require == true) {
            	selectEle.className = selectEle.className + " require ";;
            }
            //将内容设置到文本框里
            selectEle.innerHTML = property_value;
            //将内容设置到文本框里

            if (colOption.isTotal) {
                //若要计算合计，则添加类oninput_change_property
                selectEle.className = selectEle.className + " oninput_change_property";
                //若要计算合计，则添加类oninput_change_property
            }
            TdEle.appendChild(selectEle);
        } else {
            TdEle.innerHTML = property_value;
            TdEle.className = TdEle.className + " " + property_name;
        }
        //设置td的文本内容 -- 结束
        $(TdEle).attr("attribute_name", colOption.name);
        trEle.appendChild(TdEle);
    }
    //添加各列 -- 结束

    return trEle;
};

TableTools.defaultConfig._setRow = function(rowData, selectedTr) {
    var TableInstance = this;
    /*修改行data属性的值*/
    var row_dataJson = JSON.stringify(rowData);
    if (TableInstance.showrowdata) {
        $(selectedTr).attr("data", row_dataJson);
    }
    /*修改行data属性的值*/

    var colnames = TableInstance.colnames;
    var ValueClass = TableInstance.ValueClass;
    //修改各列 -- 开始
    var th_length = colnames.length;

    for (var j = 0; j < th_length; ++j) {
        /*获取相关的数据 -- 开始*/
        var colOption = colnames[j];
        var property_name = colOption.name;
        var th_name = colOption.colname;

        //获取property_value
        var property_value = rowData[property_name];
        if (typeof(colOption.displayValue) != "undefined") {
            property_value = colOption.displayValue(property_value, rowData);
        }
        if (colOption.fixedPrecision & !isNaN(Number(property_value))) { //小数位使用默认固定的
            property_value = Common.round(Number(property_value), TableInstance.fixedPrecision_Number);
        }
        //获取property_value
        /*获取相关的数据 -- 结束*/

        /*获取填充了 值  的 元素，并修改值 ， 该元素可能为td或input*/
        if (property_name == "") {
            continue;
        }
        var $valueInput = $(selectedTr).find("." + property_name);
        if ($valueInput.hasClass(ValueClass)) {
            var td = $valueInput.closest('td');
            if (td.hasClass('input-checkbox')) {
                $valueInput[0].checked = property_value;
            } else if (td.hasClass('select')) {
                $valueInput[0].innerHTML = property_value;
            } else {
                $valueInput.val(property_value);
            }

        } else {
            $valueInput[0].innerHTML = property_value;
        }
        /*获取填充了 值  的 元素，并修改值 ， 该元素可能为td或input*/
    }
    //修改各列 -- 结束
};
TableTools.defaultConfig.getTrNo = function(trEle) {
    var TableInstance = this;

    var temp = (TableInstance.currentpage - 1) * TableInstance.limit;
    var No = trEle.rowIndex + 1;
    if (temp != undefined & temp != null & !isNaN(Number(temp))) {
        No = No + temp;
    }
    return No;
};
TableTools.defaultConfig.getNo = function() {
    var TableInstance = this;

    var temp = (TableInstance.currentpage - 1) * TableInstance.limit;
    var No = $(TableInstance.tableEle).find("tbody tr").length + 1;
    if (temp != undefined & temp != null & !isNaN(Number(temp))) {
        No = No + temp;
    }
    return No;
};

TableTools.defaultConfig.addRow = function(rowdata) {
    var TableInstance = this,
        tableEle = TableInstance.tableEle,
        tbody = $(tableEle).find("tbody")[0];

    // var No = $(tbody).children("tr").length + 1;
    var No = TableInstance.getNo(); /*序号按页面逐页递增*/


    var addedTr = TableInstance._createRow(No, rowdata);
    if (TableInstance.showrowdata) {
        var row_dataJson = JSON.stringify(rowdata);
        $(addedTr).attr("data", row_dataJson);
    }
    tbody.appendChild(addedTr);

    TableInstance.tableWidgetResize();
    TableInstance.changeTotalRow();
    return addedTr;
};
TableTools.defaultConfig.addRows_batch = function(data) {
    var TableInstance = this,
        tableEle = TableInstance.tableEle,
        tbody = $(tableEle).find("tbody")[0],
        // firstNo = $(tbody).children("tr").length + 1;
        firstNo = TableInstance.getNo(); /*序号按页面逐页递增*/

    var frag = document.createDocumentFragment();
    for (var i = 0; i < data.length; ++i) {
        var No = firstNo + i;

        var row_dataJson = JSON.stringify(data[i]);
        var addedTr = TableInstance._createRow(No, data[i]);
        if (TableInstance.showrowdata) {
            $(addedTr).attr("data", row_dataJson);
        }
        frag.appendChild(addedTr);
    }
    tbody.appendChild(frag);
    TableInstance.tableWidgetResize();
    TableInstance.changeTotalRow();
};
TableTools.defaultConfig.addEmptyRow = function() {
    var TableInstance = this;
    if (!TableInstance.showEmptyRow) {
        return null;
    }
    var colnames = TableInstance.colnames;
    var rowdata = {};
    for (var i = 0; i < colnames.length; ++i) {
        rowdata[colnames[i].name] = "";
    }
    var addedTr = TableInstance.addRow(rowdata);
    $(addedTr).addClass('EmptyTr');
    return addedTr;
};

TableTools.defaultConfig.updateRow = function(rowdata, selectedTr) {
    var TableInstance = this;
    TableInstance._setRow(rowdata, selectedTr);
    if ($(selectedTr).hasClass('EmptyTr')) {
        $(selectedTr).removeClass('EmptyTr');
    }
    TableInstance.changeTotalRow();
};
TableTools.defaultConfig.batch_deleteRow = function(rows) {
    var TableInstance = this;
    if (rows.length <= 0) {
        return;
    }
    var currentpage = TableInstance.currentpage;
    if (currentpage == null || typeof(currentpage) == "undefined") {
        currentpage = 1;
    }
    $(rows).remove();
    var $next_trs = $(TableInstance.tableEle).find("tbody tr");
    /*序号按页面逐页递增*/
    // var selected_index = data._No;    /*序号按页面逐页递增*/ //$(TableInstance.tableEle).find("tbody>tr").index(selectedTr);
    $next_trs.each(function() {
        var _No = TableInstance.getTrNo(this);
        var data = $.parseJSON($(this).attr("data"));
        data._No = _No;

        var NoEle = $(this).children("td._No")[0];
        var no_value = data._No;
        if (typeof(TableInstance.showNoOptions.displayValue) != "undefined") {
            no_value = TableInstance.showNoOptions.displayValue(data._No, data, false);
        }
        NoEle.innerHTML = no_value;

        /*修改行data属性的值*/
        var row_dataJson = JSON.stringify(data);
        if (TableInstance.showrowdata) {
            $(this).attr("data", row_dataJson);
        }
        /*修改行data属性的值*/
    });
    /*序号按页面逐页递增*/


    TableInstance.tableWidgetResize();
    TableInstance.changeTotalRow();
};
TableTools.defaultConfig.deleteRow = function(selectedTr) {
    var TableInstance = this;
    var $next_trs = $(selectedTr).nextAll("tr");
    // var length = $next_trs.length;

    /*序号按页面逐页递增*/
    // var selected_index = data._No;    /*序号按页面逐页递增*/ //$(TableInstance.tableEle).find("tbody>tr").index(selectedTr);


    $next_trs.each(function() {
        var data = $.parseJSON($(this).attr("data"));
        data._No = data._No - 1;

        var NoEle = $(this).children("td._No")[0];
        var no_value = data._No;
        if (typeof(TableInstance.showNoOptions.displayValue) != "undefined") {
            no_value = TableInstance.showNoOptions.displayValue(data._No, data, false);
        }
        NoEle.innerHTML = no_value;

        /*修改行data属性的值*/
        var row_dataJson = JSON.stringify(data);
        if (TableInstance.showrowdata) {
            $(this).attr("data", row_dataJson);
        }
        /*修改行data属性的值*/
    });
    /*序号按页面逐页递增*/

    selectedTr.remove();
    TableInstance.tableWidgetResize();
    TableInstance.changeTotalRow();
};
TableTools.defaultConfig.getQueryString = function() {
    var TableInstance = this;
    var url = TableInstance.url;
    var querystring = decodeURIComponent(TableInstance.querystring);
    var locked_query = decodeURIComponent(TableInstance.locked_query);
    var url_params = Common.urlParams(url);
    if (typeof(querystring) != "undefined" & querystring != "") {
        var querystring_params = Common.urlParams(querystring);
        Common.coverObject(url_params, querystring_params, true);
    }
    if (typeof(locked_query) != "undefined" & locked_query != "") {
        var locked_query_params = Common.urlParams(locked_query);
        Common.coverObject(url_params, locked_query_params, true);
    }
    var currentpage = TableInstance.currentpage;
    if (currentpage == null || typeof(currentpage) == "undefined") {
        currentpage = 1;
    }
    var limit = TableInstance.limit;
    var start = (TableInstance.currentpage - 1) * limit;
    if (TableInstance.haspage) { //表示分页
        url_params["start"] = start;
        TableInstance.start = start;
        url_params["limit"] = limit;
    }
    //如果显示合计总行且分页，则需传递total_row参数，获取总合计数据，上传需要合计的列
    if (TableInstance.haspage & TableInstance.showAllTotalRow) {
        var param_total_row = [];
        //找出需合计的列
        var total_row = TableInstance.total_row;
        //找出需合计的列
        for (var r = 0; r < total_row.length; ++r) {
            var rowname = total_row[r];
            var item = {
                type: "sum",
                field: rowname
            };
            param_total_row.push(item);
        }
        url_params.total_row = JSON.stringify(param_total_row);
    }
    //如果显示合计行，则获取总合计数据，上传需要合计的列

    var head_url = url + "?";
    if (url.indexOf("?") > -1) {
        head_url = url.substring(0, url.indexOf("?") + 1);
    }

    url = head_url + decodeURIComponent($.param(url_params)); //$.param(url_params);//decodeURIComponent($.param(url_params));

    return url;
};
TableTools.defaultConfig.loadData = function(url, settings) {
    var TableInstance = this;
    if (url == undefined || url == null) {
        url = TableInstance.getQueryString();
    }
    //设置table的搜素
    var params = Common.urlParams(url);
    if (params.start != undefined) {
        TableInstance.start = params.start;
        if (TableInstance.limit != undefined & TableInstance.limit != null) {
            TableInstance.currentpage = parseInt(TableInstance.start / TableInstance.limit) + 1;
        }
    }
    TableInstance.querystring = $.param(params); //encodeURIComponent($.param(Common.urlParams(url))); //decodeURIComponent($.param(Common.urlParams(url)));
    // var donecall = TableInstance.donecall;
    // var failcall = TableInstance.failcall;
    var default_settings = {
        url: url,
        type: 'GET',
        dataType: 'json',
        cache: false
    };
    for (var property in settings) {
        default_settings[property] = settings[property];
    }
    $.ajax(default_settings)
        .done(function(data) {
            TableInstance._data = data;
            if (TableInstance.donecall != null & typeof(TableInstance.donecall) == "function") {
                TableInstance.donecall(data);
            }
        })
        .fail(function(data) {
            if (TableInstance.failcall != null & typeof(TableInstance.failcall) == "function") {
                TableInstance.failcall(data);
            }
        })
        .always(function() {});
};
TableTools.defaultConfig.nextPage = function() {
    var TableInstance = this;
    var currentpage = TableInstance.currentpage;
    if (currentpage == null || typeof(currentpage) == "undefined") {
        currentpage = 1;
    }
    var nextpage = currentpage + 1;

    var limit = TableInstance.limit;
    var start = (nextpage - 1) * limit;
    if (TableInstance.haspage & (start > TableInstance.totalrecords || start < 0)) {
        return;
    }

    this.currentpage = this.currentpage + 1;
    this.loadData();
};
TableTools.defaultConfig.prevPage = function() {
    var TableInstance = this;
    var currentpage = TableInstance.currentpage;
    if (currentpage == null || typeof(currentpage) == "undefined") {
        currentpage = 1;
    }
    var prevPage = currentpage - 1;

    var limit = TableInstance.limit;
    var start = (prevPage - 1) * limit;
    if (TableInstance.haspage & (start > TableInstance.totalrecords || start < 0)) {
        return;
    }

    this.currentpage = this.currentpage - 1;
    this.loadData();
};
TableTools.defaultConfig.gotoPage = function(page) {
    var TableInstance = this;
    var limit = TableInstance.limit;
    var start = (page - 1) * limit;
    if (TableInstance.haspage & (start > TableInstance.totalrecords || start < 0)) {
        return;
    }
    this.currentpage = page;
    this.loadData();
};

//获取表格中的内容，不是tr的属性data,因为有些属性是多余的
TableTools.defaultConfig.getTableData = function() {
    var TableInstance = this,
        tableEle = TableInstance.tableEle;
    var result = [];
    var colnames = TableInstance.colnames,
        trlist = $(tableEle).find('tbody tr.tr');
    for (var i = 0; i < trlist.length; ++i) {
        if ($(trlist[i]).hasClass("EmptyTr")) {
            continue;
        }
        var tr_fact_data = TableInstance.getTrData(trlist[i]);
        result.push(tr_fact_data);
    }
    return result;
};
TableTools.defaultConfig.getTrData = function(tr) {
    var TableInstance = this;
    var attr_data = $.parseJSON($(tr).attr("data"));
    // var result_trData = new Object();
    var result_trData = attr_data;
    var colnames = TableInstance.colnames;
    var ValueClass = TableInstance.ValueClass;

    var th_length = colnames.length;

    for (var j = 0; j < th_length; ++j) {
        var colOption = colnames[j];
        var property_name = colOption.name;
        var property_value = null;
        if (property_name == "") {
            continue;
        }
        var $valueEle = $(tr).find("." + property_name);
        if($valueEle.length <= 0){
        	continue;
        }
        if ($valueEle.hasClass(ValueClass)) {
            property_value = $valueEle.val();
        } else {
            property_value = $valueEle.text().trim();
        }
        
        if ($valueEle[0].type == "checkbox") {
            property_value = $valueEle[0].checked;
        }
        var number_property_value = Number(property_value);
        if (!isNaN(number_property_value) & property_value != "") {
            property_value = number_property_value;
        }
        result_trData[property_name] = property_value;
    }
    return result_trData;
};
TableTools.defaultConfig.addTotalRow = function() {

    var TableInstance = this,
        tableEle = TableInstance.tableEle;
    if (!TableInstance.showTotalRow || typeof(TableInstance.showTotalRow) == "undefined") {
        return false;
    }
    //生成合计行
    var totalname_col = TableInstance.totalname.col;
    var totalname_name = TableInstance.totalname.name;

    var colnames = TableInstance.colnames;
    var rowdata = {};
    for (var i = 0; i < colnames.length; ++i) {
        if (colnames[i].name == totalname_col) {
            rowdata[colnames[i].name] = totalname_name;
        } else {
            rowdata[colnames[i].name] = "";
        }
    }

    var No = "";
    if (totalname_col == 'No') {
        No = totalname_name;
    }

    var foot_tr = TableInstance._createRow(No, rowdata, true);
    foot_tr.className = foot_tr.className + " total";
    var tfoot = $(tableEle).find("tfoot")[0];
    if (typeof(tfoot) == "undefined") {
        tfoot = document.createElement("tfoot");
        tableEle.appendChild(tfoot);
    }
    $(tfoot).empty();


    tfoot.appendChild(foot_tr);

    if (TableInstance.fixtotal) { //如果合计需要固定，则需新建一个table_widget，和table
        TableInstance.fixfoot();
        return;
    }

    TableInstance.tableWidgetResize();
    TableInstance.changeTotalRow();
    return tfoot;
};
TableTools.defaultConfig.fixfoot = function() {
    var TableInstance = this;
    if (TableInstance.tableWidget == null) { //如果没有table容器，则不添加foot容器，因为只有fixhead的滚动的表格容器，才会需要设置fix的foot合计行
        return;
    }
    var $tfootTr = $(TableInstance.tableEle).find("tfoot tr");

    var old_foot_widget = $(TableInstance.tableWidget).next(".fixedfoot_widget");
    var new_table_widget = document.createElement("div");
    if (old_foot_widget.length > 0) {
        new_table_widget = old_foot_widget[0];
    }
    $(new_table_widget).empty();
    new_table_widget.className = "fixedfoot_widget";
    var new_tableEle = document.createElement("table");
    new_tableEle.className = "wijmo";
    var new_tbodyEle = document.createElement("tbody");
    $(new_tbodyEle).append($tfootTr);
    new_tableEle.appendChild(new_tbodyEle);
    new_table_widget.appendChild(new_tableEle);

    $(TableInstance.tableWidget).after(new_table_widget);
    TableInstance.foot_widget = new_table_widget;
};
TableTools.defaultConfig.changeTotalRow = function() {
    var TableInstance = this,
        tableEle = TableInstance.tableEle,
        _data = TableInstance._data; //_data与data不一样
    if (!TableInstance.showTotalRow || typeof(TableInstance.showTotalRow) == "undefined") {
        return false;
    }

    //如果显示合计总行且分页，则合计行进行重置
    if (TableInstance.haspage & TableInstance.showAllTotalRow) {
        if (_data == undefined) {
            return;
        }
        if (_data.total_row != undefined) {
            TableInstance._setTotalRow(_data.total_row);
        }
        return;
    }
    //如果显示合计总行且分页，则合计行进行重置

    var tableDataArray = TableInstance.getTableData();
    var tabledCol = [];
    var colnames = TableInstance.colnames;
    for (var i = 0; i < colnames.length; ++i) {
        var colOption = colnames[i];
        if (colOption.isTotal) { //要计算合计
            tabledCol.push(colOption);
        }
    }
    var $tfoot_tr = TableInstance.getTotalTr();
    for (var j = 0; j < tabledCol.length; ++j) {
        var total_result = 0;
        var name = tabledCol[j].name;
        for (var count = 0; count < tableDataArray.length; ++count) {
            var item = tableDataArray[count];
            var value = item[name];
            total_result = total_result + Number(value);
        }
        if (tabledCol[j].fixedPrecision & !isNaN(Number(total_result))) { //小数位使用默认固定的
            total_result = Common.round(Number(total_result), TableInstance.fixedPrecision_Number);
        }
        $tfoot_tr.find("." + name).text(total_result);
    }
};
TableTools.defaultConfig._setTotalRow = function(totalrowdata) {
    var TableInstance = this,
        tableEle = TableInstance.tableEle;
    if (!TableInstance.showTotalRow || typeof(TableInstance.showTotalRow) == "undefined") {
        return false;
    }
    var tabledCol = [];
    var colnames = TableInstance.colnames;
    for (var i = 0; i < colnames.length; ++i) {
        var colOption = colnames[i];
        if (colOption.isTotal) { //要计算合计
            tabledCol.push(colOption);
        }
    }
    var $tfoot_tr = TableInstance.getTotalTr();

    for (var j = 0; j < tabledCol.length; ++j) {
        var name = tabledCol[j].name;
        var total_result = Number(totalrowdata[name]);
        if (tabledCol[j].fixedPrecision & !isNaN((total_result))) { //小数位使用默认固定的
            total_result = Common.round((total_result), TableInstance.fixedPrecision_Number);
        }
        $tfoot_tr.find("." + name).text(total_result);
    }
};
TableTools.defaultConfig.getTotalTr = function() {
    var TableInstance = this;
    if (!TableInstance.showTotalRow || typeof(TableInstance.showTotalRow) == "undefined") {
        return false;
    }
    if (TableInstance.fixtotal) { //如果合计需要固定，则需新建一个table_widget，和table
        return $(TableInstance.foot_widget).find("table tr.total");
    }
    return $(TableInstance.tableEle).find("tfoot tr.total");
};
TableTools.defaultConfig.getColOption = function(name) {
    var TableInstance = this;
    var colnames = TableInstance.colnames;
    for (var i = 0; i < colnames.length; ++i) {
        if (colnames[i].name == name) {
            return colnames[i];
        }
    }
    return null;
};
//根据某属性值获取行列表
TableTools.defaultConfig.getTrListByParam = function(param, value) {
    var TableInstance = this;
    var result = [];
    var trlist = $(TableInstance.tableEle).find('tbody tr.tr');
    for (var i = 0; i < trlist.length; ++i) {
        var trdata = TableInstance.getTrData(trlist[i]);
        if (trdata[param] == value) {
            result.push(trlist[i]);
        }
    }
    return result;
};
//根据某行的某属性值获取该行
TableTools.defaultConfig.getTrsByParam = function(param, value) {
    var TableInstance = this;
    var trlist = $(TableInstance.tableEle).find('tbody tr.tr');
    var result = [];
    for (var i = 0; i < trlist.length; ++i) {
        var trdata = TableInstance.getTrData(trlist[i]);
        if (trdata[param] == value) {
            result.push(trlist[i]);
        }
    }
    return result;
};
TableTools.defaultConfig.getTrByParam = function(param, value) {
    var TableInstance = this;
    var trlist = $(TableInstance.tableEle).find('tbody tr.tr');
    for (var i = 0; i < trlist.length; ++i) {
        var trdata = TableInstance.getTrData(trlist[i]);
        if (trdata[param] == value) {
            return trlist[i];
        }
    }
    return null;
};
TableTools.defaultConfig.getTrByParam2 = function(param, value) {
    var TableInstance = this;
    var trlist = $(TableInstance.tableEle).find('tbody tr.tr');
    for (var i = 0; i < trlist.length; ++i) {
        var tr = trlist[i];
        var trdata = $.parseJSON($(tr).attr("data"));
        if (trdata[param] == value) {
            return tr;
        }
    }
    return null;
};
//获取表格中的内容


/*表格内容 -- 结束*/

//用copiedObj覆盖newObj的所有属性，若copiedObj有newObj没有的属性，则newObj添加新的属性
Common.coverObject = function(newObj, copiedObj, deep) { //用copiedObj覆盖newObj中的属性
    if (deep) {
        for (var propertyname in copiedObj) { //将页面中设置的config与默认config结合
            var propertyvalue = copiedObj[propertyname];
            if (Common.isHTMLElement(propertyvalue)) { //如果是HTML元素，则直接赋值
                newObj[propertyname] = propertyvalue;
            } else if (Common.isObejct(propertyvalue)) {
                if (typeof(newObj[propertyname]) == "undefined") {
                    newObj[propertyname] = {};
                }
                Common.coverObject(newObj[propertyname], propertyvalue, true); //IE8 内存占用过高
            } else {
                newObj[propertyname] = propertyvalue;
            }
        }
    } else {
        for (var propertyname in copiedObj) { //将页面中设置的config与默认config结合
            newObj[propertyname] = copiedObj[propertyname];
        }
    }
};
//用copiedObj覆盖newObj的所有属性，但只覆盖newObj中原来就有的属性，若copiedObj有newObj没有的属性，则newObj不会添加新的属性
Common.CoverObject_exist = function(newObj, copiedObj, deep) {
    if (deep) {
        for (var propertyname in newObj) {
            if (copiedObj.hasOwnProperty(propertyname)) {
                var propertyvalue = copiedObj[propertyname];
                if (Common.isHTMLElement(propertyvalue)) { //如果是HTML元素，则直接赋值
                    newObj[propertyname] = propertyvalue;
                } else if (Common.isObejct(propertyvalue)) {
                    if (typeof(newObj[propertyname]) == "undefined") {
                        newObj[propertyname] = {};
                    }
                    Common.CoverObject_exist(newObj[propertyname], propertyvalue, true);
                } else {
                    newObj[propertyname] = propertyvalue;
                }
                newObj[propertyname] = propertyvalue;
            }
        }
    } else {
        for (var propertyname in newObj) {
            if (copiedObj.hasOwnProperty(propertyname)) {
                var propertyvalue = copiedObj[propertyname];
                newObj[propertyname] = propertyvalue;
            }
        }
    }
};
//newObj添加addedObj中newObj没有的属性，相当于addedObj中的属性值是newObj的默认值
Common.addProperty = function(newObj, addedObj, deep) {
    if (deep) {
        for (var propertyname in addedObj) { //将页面中设置的config与默认config结合
            //newObj是否有propertyname属性
            if (!newObj.hasOwnProperty(propertyname)) { //如果newObj没有该属性，则添加
                var propertyvalue = addedObj[propertyname];
                if (Common.isHTMLElement(propertyvalue)) { //如果是HTML元素，则直接赋值
                    newObj[propertyname] = propertyvalue;
                } else if (Common.isObejct(propertyvalue)) {
                    if (typeof(newObj[propertyname]) == "undefined") {
                        newObj[propertyname] = {};
                    }
                    Common.addProperty(newObj[propertyname], propertyvalue, true);
                } else {
                    newObj[propertyname] = propertyvalue;
                }
            }

        }
    } else {
        for (var propertyname2 in addedObj) { //将页面中设置的config与默认config结合
            if (!newObj.hasOwnProperty(propertyname2)) { //如果newObj没有该属性，则添加
                newObj[propertyname2] = addedObj[propertyname2];
            }
        }
    }
};
//2014-11-12添加

