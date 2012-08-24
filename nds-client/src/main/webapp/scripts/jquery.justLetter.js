/*
 * JustLetter-beta:0.0.1 plugin
 * Plugin to reject numerics values on input.
 * 
 * @author: direx 
 * @date: 24/08/2012
 * 
 * Exemple:
 * $(". input-just-letter").justLetter();
 */
(function($) {

	$.fn.justLetter = function() {
		
		$(this).keypress(function(e) {
	
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
			
			return !(/[0-9 ]/.test(String.fromCharCode(key))); 
		
		});
	}
	
})(jQuery);