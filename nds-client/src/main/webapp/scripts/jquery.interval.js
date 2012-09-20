(function($) {
	
	var idsInterval = new Array;
	
	$.fn.interval = function(func, interval) {
		
		var idInterval = window.setInterval(func, interval);
		
		idsInterval[idsInterval.length] = idInterval;
		
		return idInterval;
	};
	
	$.fn.clearAllInterval = function() {
		
		for (var index in idsInterval) {
		
			clearInterval(idsInterval[index]);
		}
		
		idsInterval = new Array;
	};
})(jQuery);