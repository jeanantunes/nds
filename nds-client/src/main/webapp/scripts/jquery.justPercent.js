/*
 * JustPercent-beta:0.0.1 plugin
 * Plugin para tratar porcentagem com decimais no padrão brasileiro.
 * 
 * @author: direx 
 * @date: 17/10/2012
 * 
 * Exemple:
 * $(". input-just-percent").justPercent();
 * 
 * 
 * Para obter o float de um valor = 10,5 utilize:
 * $(". input-just-percent").justPercent("floatValue") 
 * 
 */
(function($) {
	
	var input;
	
	var methods = {
			
			floatValue : function () {
				
				var val;
			    				        	
				val =  input.val();
			        	
		        if (!val) return; 
		        
		        if (val.indexOf(",") >= 0) {
		        	val = val.replace(",", ".");
		        }
		        		
		        val = parseFloat(val);
		                
		        return val;
		    }
			
	};
	
	$.fn.justPercent = function(method) {
		
		input = $(this);
		
		if (methods[method]) {
			return methods[method].call();
		}
		
		return this.blur($.fn.justPercent.blur).keypress($.fn.justPercent.keypress);
	}
	
		
	$.fn.justPercent.blur = function(e){
		
		var value = $(this).val() + "";
		
		if (value == "") return;
		
		if (value.indexOf(",") >= 0) {
			value = value.replace(",", ".");
		} 
		
		value = parseFloat(value).toFixed(2);
		
		if (value > 100) {
			
			exibirMensagem("WARNING", ["Porcentagem máxima permitida é 100%"]); 
			
			$(this).val(""); 
			
			return;
		}
		
		value = value.toString().replace(".", ",");
		
		$(this).val(value);
	}
	
	$.fn.justPercent.keypress = function(e) {
		
		var key = e.charCode ? e.charCode : e.keyCode ? e.keyCode : 0;
		
		if(key == 13 && this.nodeName.toLowerCase() == "input")
		{
			return true;
		}
		else if(key == 13)
		{
			return false;
		}

		//Ctrl+A
		if((e.ctrlKey && key == 97 /* firefox */) || (e.ctrlKey && key == 65) /* opera */) return true;
		//Ctrl+X (cut)
		if((e.ctrlKey && key == 120 /* firefox */) || (e.ctrlKey && key == 88) /* opera */) return true;
		//Ctrl+C (copy)
		if((e.ctrlKey && key == 99 /* firefox */) || (e.ctrlKey && key == 67) /* opera */) return true;
		//Ctrl+Z (undo)
		if((e.ctrlKey && key == 122 /* firefox */) || (e.ctrlKey && key == 90) /* opera */) return true;
		// Ctrl+V (paste), Shift+Ins
		if((e.ctrlKey && key == 118 /* firefox */) || (e.ctrlKey && key == 86) /* opera */ || (e.shiftKey && key == 45)) return true;
		
		var inputValue = $(this).val();
		
		var keyChar = String.fromCharCode(key);
		var pattern = /[0-9]/;
			
		if (inputValue.indexOf(",") < 0) {
		
			pattern = /[0-9,]/;
					
		} else {
		
			if (inputValue.split(",")[1].length >= 2) return false;
		}
		
		return (pattern.test(keyChar)); 
		
	}

		
})(jQuery);

//@ sourceURL=jquery.justPercent.js