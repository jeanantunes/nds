$(document).ready(function(){
	jQuery(":input[maxlength]").keyup(function () {
	    var focus = jQuery(this);
	    var valFocus;
	    try {
	        if (typeof jQuery(focus).mask() == 'string') {
	            valFocus = jQuery(focus).val().replace("_", "");
	        }          
	    } catch (e) {
	        valFocus = jQuery(focus).val();      
	    }
	
	    if (focus.attr('maxLength') == valFocus.length) {
	        var next = false;
	        jQuery(":input[maxlength]").each(function(element) {  
	            if (next) {
	                jQuery(this).focus();
	            }
	            next = false;
	            if (jQuery(this).attr('id') == focus.attr('id')) {
	                next = true;
	            }
	        });
	    }
	});
});

function exibirMensagem(tipoMensagem, mensagens) {

	var campoTexto = $("#idTextoMensagem");

	campoTexto.html('');

	$.each(mensagens, function(index, value) {
		if (index != 0){
			campoTexto.append(value + '</br>');
		}
	});

	$('#idMensagem').show(1000, esconde("idMensagem"));
	
	$("#idMensagem").removeClass("ui-state-error");
	$("#idMensagem").removeClass("ui-state-highlight");
	$("#idMensagem").removeClass("ui-state-default");
	
	if (tipoMensagem == "success") {
		$("#idMensagem").addClass("ui-state-default");
	} else if (tipoMensagem == "warning"){
		$("#idMensagem").addClass("ui-state-highlight");
	} else if (tipoMensagem == "error"){
		$("#idMensagem").addClass("ui-state-error");
	}
}

function esconde(idDiv) {
	setTimeout(function() {
		$('#' + idDiv + ':visible').removeAttr("style").fadeOut();
	}, 3000);
}