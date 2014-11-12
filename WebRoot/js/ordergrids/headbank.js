$(document)
		.ready(
				function() {
					var TbOptions = {
						tableEle :$("#headbank .detailTb")[0],
						showNoOptions : {
							width :'5%',
							display :false
						},
						colnames : [
								{
									name :'color',
									colname :'颜色',
									width :'15%'
								},
								{
									name :'weight',
									colname :'克重(g)',
									width :'15%'
								},
								{
									name :'yarn',
									colname :'纱线种类',
									width :'15%'
								},
								{
									name :'size',
									colname :'尺寸',
									width :'15%'
								},
								{
									name :'quantity',
									colname :'生产数量',
									width :'15%'
								},
								{
									name :'price',
									colname :'价格(/个)',
									width :'15%'
								},
								{
									name :'_handle',
									colname :'操作',
									width :'15%',
									displayValue : function(value, rowdata) {
										return "<a class='editRow' href='#'>修改</a> | <a class='deleteRow' href='#'>删除</a>";
									}
								} ]
					};

					var TableInstance = TableTools
							.createTableInstance(TbOptions);
					$("#headbank .addRow").click( function() {
						Common.resetForm($("#headbankDialog form")[0]);
						$("#headbankDialog").modal();

						$("#headbankDialog form").unbind("submit");
						$("#headbankDialog form").bind("submit", function() {
							// 添加一行
								if (!Common.checkform(this)) {
									return false;
								}
								var formdata = $(this).serializeJson();
								TableInstance.addRow(formdata);
								$("#headbankDialog").modal("hide");
								return false;
							});
					});
					$("#headbank").on("click", ".editRow", function() {
						Common.resetForm($("#headbankDialog form")[0]);
						$("#headbankDialog").modal();
						$("#headbankDialog form").unbind("submit");
						$("#headbankDialog form").bind("submit", function() {
							if (!Common.checkform(this)) {
								return false;
							}
							// 修改一行
								var formdata = $(this).serializeJson();
								TableInstance.updateRow(formdata);
								$("#headbankDialog").modal("hide");
								return false;
							});
					});

					$("table").on("click", ".deleteRow", function() {
						$(this).closest("tr").remove();
						return false;
					});
				});