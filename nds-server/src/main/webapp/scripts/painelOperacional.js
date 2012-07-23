$(function() {
	$(".accordion").accordion({
		autoHeight : false,
		navigation : true
	});
});

function mostrarIndicadores(estado){
	
	$(".grupoIndicadorEstado").hide();
	$("#div_" + estado).show();
}

function openDistrib(idDistrib) {
	
	$("#detalhe_" + idDistrib).fadeIn('slow');
};

function closeDistrib(idDistrib) {
	
	$("#detalhe_" + idDistrib).fadeOut('slow');
};