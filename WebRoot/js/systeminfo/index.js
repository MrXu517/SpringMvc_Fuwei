$(document).ready(function(){
	$("#companys form").submit(function(){
		var $submitBtn = $(this).find("[type='submit']");
		
		$($submitBtn).button('loading');
		return false;
	});
	
});
