	// tooltip for buttons
	$(function() {
		$('.bt_novos a').tipsy({gravity: 'nw'}); 
	});
	$(function() {
		$('.bt_arq a').tipsy({gravity: 'ne'}); 
	});
	
	//workspace : "#workspace div.ui-tabs-panel:not(.ui-tabs-hide)";
	var BaseController = {
			workspace : "",
			bindData : function(data, form) {
				element = data;
				
				for (property in element) {
					for (item in element[property]) {
//						console.log("[name='" + property + "."+item+"']" + element[property][item])
						$("#" + form.attr("id") + " [name='" + property + "."+item+"']").val(element[property][item]);
					}
				}
			},
			clearForm : function(form) {
				$(form).find(':input').each(function() {
				        switch(this.type) {
			            	case 'hidden':
				            case 'password':
				            case 'select-multiple':
				            case 'select-one':
				            case 'text':
				            case 'textarea':
				                $(this).val('');
				                break;
				            case 'checkbox':
				            case 'radio':
				                this.checked = false;
				        }
				    });
				}
	};
	