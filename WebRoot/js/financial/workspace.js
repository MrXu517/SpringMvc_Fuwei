$(document).ready(function(){
	/*设置当前选中的页*/
	var $a = $("#left li a[href='financial/workspace']");
	setActiveLeft($a.parent("li"));
	/*设置当前选中的页*/
	
	
	$('#tab a').click(function (e) {
		e.preventDefault();
		$(this).tab('show');
	});
//	$('#tab a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
//		$iframe = $($(this).attr("href")).find("iframe");
//		iFrameHeight($iframe[0]);
//	});
//	$("iframe").load(function(){
//		iFrameHeight(this);
//	});
});
//function iFrameHeight(iframeEle) {     
//	var subWeb = $(iframeEle).prop('contentWindow').document;   
//	if(iframeEle != null && subWeb != null) {
//		iframeEle.height = subWeb.body.scrollHeight;
//		iframeEle.width = subWeb.body.scrollWidth;
//	}   
//}  