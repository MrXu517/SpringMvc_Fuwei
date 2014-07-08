$(document).ready(function() {
    /*左侧栏 样式相关js*/
	//设置左边栏的选中项
	var pathname = location.pathname;
	var pathname_lst = pathname.substring(pathname.lastIndexOf("/")+1);
	$(".menubar-ul li>a").each(function(){
		var href = $(this).attr("href");
		if(href==pathname_lst){
			$(this).parent().siblings().removeClass('active');
			$(this).parent().addClass("active");
			$(this).closest('.menubar-ul>li').siblings().removeClass('active');
			$(this).closest('.menubar-ul>li').addClass("active");
			$(this).closest('.menubar-ul>li').toggleClass("show");
		}
	});
	//设置左边栏的选中项
    $("#sidebar-collapse>i").click(function() {
    	var data_icon2 = $(this).attr("data-icon2");
    	$(this).toggleClass(data_icon2);
        $("#left").toggleClass('left_mini');
    });
    /*左侧栏 样式相关js*/
    /*左侧栏点击后功能相关js，比如打开工作台，打开新建单据等等*/
    $('.menubar-ul>li>a').click(function() {
        $(this).parent().toggleClass("show");
        return true;
    });
    $('.menubar-ul li>a').click(function() {
        $(this).closest('.menubar-ul>li').siblings().removeClass('active');
        $(this).closest('.menubar-ul>li').addClass('active');
        if($(this).attr("href") == "#"){
        	return false;
        }
        return true;
    });
    /*左侧栏点击后功能相关js，比如打开工作台，打开新建单据等等*/
    
    //下拉框
    $("a.dropdown-toggle").click(function(){
    	$(this).siblings("ul.dropdown").toggle();
    	return false;
    });
  //下拉框
});

function setActiveLeft($li){
	$("#left .menubar li").removeClass("active");
	$li.addClass("active");
	$li.closest(".li_dropdown").addClass("show");
}