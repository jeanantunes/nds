// exibirColuna('#flexigrid', 'Coluna', visible);

// you can put this in a separate javascript library
function exibirColuna(tbl, columnName, visible) {

	var grd = $(tbl).closest('.flexigrid');
	if(typeof(columnName) == 'object' && columnName.length > 1) {
		
		$.each(columnName, function(k, v) {
			
			var colHeader = $('th[abbr=' + v + ']', grd);
			var colIndex = $(colHeader).attr('axis').replace(/col/, "");
			
			$(colHeader).toggle(visible);
			
			$('tbody tr', grd).each(
					function () {
						$('td:eq(' + colIndex + ')', this).toggle(visible);
					}
			);
		});
		
	} else {
		
		var colHeader = $('th[abbr=' + columnName + ']', grd);
		var colIndex = $(colHeader).attr('axis').replace(/col/, "");
		
		$(colHeader).toggle(visible);
		
		$('tbody tr', grd).each(
				function () {
					$('td:eq(' + colIndex + ')', this).toggle(visible);
				}
		);
	}

}
//@sourceURL=flexigridUtils.js