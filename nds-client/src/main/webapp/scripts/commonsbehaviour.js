	// tooltip for buttons
	$(function() {
		$('.bt_novos a').tipsy({gravity: 'nw'}); 
	});
	$(function() {
		$('.bt_arq a').tipsy({gravity: 'ne'}); 
	});
	
	

	var BaseController = {
			bindData : function(data, form) {
				element = data;
				
				for (property in element) {
					for (item in element[property]) {
//						console.log("[name='" + property + "."+item+"']" + element[property][item])
						$(form + " input[name='" + property + "."+item+"']").val(element[property][item]);
					}
				}
			}
	};