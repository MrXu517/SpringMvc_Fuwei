<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
	contentType="text/html; charset=utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
	<head>
		<base href="<%=basePath%>">
		<title>发布新通知 -- 桐庐富伟针织厂</title>
		<meta charset="utf-8">
		<meta http-equiv="keywords" content="针织厂,针织,富伟,桐庐">
		<meta http-equiv="description" content="富伟桐庐针织厂">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<!-- 为了让IE浏览器运行最新的渲染模式 -->
		<link href="css/plugins/bootstrap.min.css" rel="stylesheet"
			type="text/css" />
		<link href="css/plugins/font-awesome.min.css" rel="stylesheet"
			type="text/css" />
		<link href="css/common/common.css" rel="stylesheet" type="text/css" />
		<script src="js/plugins/jquery-1.10.2.min.js"></script>
		<script src="js/plugins/bootstrap.min.js" type="text/javascript"></script>
		<script src="js/common/common.js" type="text/javascript"></script>
		<script charset="utf-8" src="js/plugins/kindeditor/kindeditor-all-min.js"></script>
		<script charset="utf-8" src="js/plugins/kindeditor/lang/zh-CN.js"></script>
		<script charset="utf-8" src="js/plugins/jquery.form.js"></script>
		
		<style type="text/css">
			#mainImgDiv .thumbnail{width:200px;height:200px;text-align: center;line-height: 180px;text-decoration: none;display: inline-block;}
			#mainImgDiv .uploadDiv,#mainImgDiv #mainfile{display: inline-block;vertical-align: top;margin-left: 10px;}
			p.tip{color:red;}
			span.require{color: red;font-size: 20px;vertical-align: top;margin-left: 5px;}
			#detailImgDiv #detailfile{display: inline-block;}
			#detailImgDiv .thumbnail{vertical-align: top;width:150px;height:150px;text-align: center;line-height: 150px;text-decoration: none;display: inline-block;margin-right: 10px;}
			#detailImgDiv .thumbnail img{max-width: 140px;max-height: 140px;}
			#mainImgDiv .thumbnail img{max-width: 190px;max-height: 190px;}
		</style>
	</head>
	<body>
		<%@ include file="../common/head.jsp"%>
		<div id="Content">
			<div id="main">
				<div class="breadcrumbs" id="breadcrumbs">
					<ul class="breadcrumb">
						<li>
							<i class="fa fa-home"></i>
							<a href="user/index">首页 </a>
						</li>
						<li>
							<a href="announcement/index">布告栏通知列表</a>
						</li>
						<li class="active">
							发布新通知
						</li>
					</ul>
				</div>
				<div class="body">
					<div class="container-fluid"><div class="row"><div class="col-xs-12 col-md-12">
					<fieldset>
						<legend>
							发布新通知： 请填写主题和正文
						</legend>
						<form class="form-horizontal" role="form" id="subForm">
							<div class="form-group">
								<div class="col-sm-offset-3 col-sm-5">
									<button type="submit" class="btn btn-primary"
										data-loading-text="正在保存...">
										确认发布
									</button>
								</div>
								<div class="col-sm-3">
									<button type="reset" class="btn btn-default">
										重置表单
									</button>
								</div>
								<div class="col-sm-1"></div>
							</div>
							<input type="hidden" name="id" id="id" />
							<div class="form-group">
								<label for="topic" class="col-sm-3 control-label">
									主题：<span class="require">*</span>
								</label>
								<div class="col-sm-8">
									<input type="text" class="form-control require" name="topic"
										id="topic" placeholder="请填写主题"  style="width:700px;">
								</div>
								<div class="col-sm-1"></div>
							</div>
							<div class="form-group">
								<label for="content" class="col-sm-3 control-label">
									正文：<span class="require">*</span>
								</label>
								<div class="col-sm-8">
									<textarea id="editor_id" name="content" style="width:700px;height:500px;">
									此处填写正文
								</textarea>
								</div>
								<div class="col-sm-1"></div>
							</div>
							
						</form>
							
					</fieldset></div></div></div>
				</div>
			</div>
		</div>
		<script type="text/javascript">
			/*设置当前选中的页*/
			var $a = $("#left li a[href='announcement/index']");
			setActiveLeft($a.parent("li"));
			/*设置当前选中的页*/
			$("form").find("input[type='text'],select").not("[readonly],[disabled]").first().focus();
			$form = $("form");
			$submitBtn = $("button[type='submit']")
			$form.unbind("submit");
			$form.submit(function(){
				// 同步数据后可以直接取得textarea的value
				editor.sync();
				if(!Common.checkform(this)){
					return false;
				}
				$submitBtn.button('loading');
				var formdata = $(this).serializeJson();
				delete formdata.id;
				$.ajax({
		            url: "announcement/add",
		            type: 'POST',
		            data: $.param(formdata),
		        })
		            .done(function(result) {
		            	if(result.success){
		            		Common.Tip("发布成功",function(){
		            			location.href="announcement/detail/"+result.id;
		            		});
		            	}
		            })
		            .fail(function(result) {
		            	Common.Error("发布失败：" + result.responseText);
		            })
		            .always(function() {
		            	$submitBtn.button('reset');
		            });
				return false;
			});
		</script>
		<script>
        	KindEditor.ready(function(K) {
                window.editor = K.create('#editor_id',{
                	items:[
        			'source', '|', 'undo', 'redo', '|', 'preview', 'template','|', 'justifyleft', 'justifycenter', 'justifyright',
        			'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
        			'superscript', '|','formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
        			'italic', 'underline', 'strikethrough', 'lineheight', '|','table', 'hr', 'emoticons',
        			 'link', 'unlink'
					],
                	uploadJson : 'image/upload_product',
                	allowFileUpload:false,
                	allowMediaUpload:false,
                	allowImageRemote:false
                });
       	 	});
		</script>
	</body>
</html>