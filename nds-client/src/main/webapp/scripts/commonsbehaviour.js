	// tooltip for buttons
	$(function() {
		$('.bt_novos a').tipsy({live: true, gravity: 'nw'}); 
		//$('a').tipsy({live: true, gravity: 'nw'}); 
	});
	$(function() {
		$('.bt_arq a').tipsy({live: true, gravity: 'ne'}); 
	});
	
	//workspace : "#workspace div.ui-tabs-panel:not(.ui-tabs-hide)";
	var BaseController = {
			workspace : "#workspace div[id*='ui-tabs-']:not(.ui-tabs-hide):not('#ui-tabs-1')",
			bindData : function(data, form) {
				element = data;
				
				for (property in element) {
					for (item in element[property]) {
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
	