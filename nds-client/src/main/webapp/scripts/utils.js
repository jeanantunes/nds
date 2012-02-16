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
		campoTexto.append(value + '</br>');
	});

	$('#idMensagem').show("highlight", {}, 5000, esconde("idMensagem"));
	
	/*
	if (tipoMensagem == "sucess") {
		$("#idMensagem").removeClass("msg-error");
	} else {
		$("#idMensagem").addClass('msg-error');
	}
	*/
}

function esconde(idDiv) {
	setTimeout(function() {
		$('#' + idDiv + ':visible').removeAttr("style").fadeOut();
	}, 3000);
}