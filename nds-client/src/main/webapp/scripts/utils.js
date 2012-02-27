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

	$('#effect').show(1000, esconde("effect"));
	
	$("#effect").removeClass("ui-state-error");
	$("#effect").removeClass("ui-state-highlight");
	$("#effect").removeClass("ui-state-default");
	
	if (tipoMensagem == "success") {
		$("#effect").addClass("ui-state-default");
	} else if (tipoMensagem == "warning"){
		$("#effect").addClass("ui-state-highlight");
	} else if (tipoMensagem == "error"){
		$("#effect").addClass("ui-state-error");
	}
}

function isNumeric(a){
	return !isNaN(parseFloat(a))&&isFinite(a);
}

function esconde(idDiv) {
	setTimeout(function() {
		$('#' + idDiv + ':visible').removeAttr("style").fadeOut();
	}, 3000);
}