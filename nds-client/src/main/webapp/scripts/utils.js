var messageTimeout;
var messageDialogTimeout;

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

	var divSuccess = $("#effectSuccess");
	var divWarning = $("#effectWarning");
	var divError = $("#effectError");
	
	var textSuccess = $("#idTextSuccess");
	var textWarning = $("#idTextWarning");
	var textError = $("#idTextError");
	
	montarExibicaoMensagem(false, tipoMensagem, mensagens,
						   divSuccess, textSuccess,
						   divWarning, textWarning,
						   divError, textError);
}

function exibirMensagemDialog(tipoMensagem, mensagens) {
	
	var divSuccess = $("div[name='effectSuccessDialog']");
	var divWarning = $("div[name='effectWarningDialog']");
	var divError = $("div[name='effectErrorDialog']");
	
	var textSuccess = $("b[name='idTextSuccessDialog']");
	var textWarning = $("b[name='idTextWarningDialog']");
	var textError = $("b[name='idTextErrorDialog']");
	
	montarExibicaoMensagem(true, tipoMensagem, mensagens,
						   divSuccess, textSuccess,
						   divWarning, textWarning,
						   divError, textError);
}

function montarExibicaoMensagem(isFromDialog, tipoMensagem, mensagens,
								divSuccess, textSuccess,
							    divWarning, textWarning,
							    divError, textError) {
	
	var campoTexto;

	if (tipoMensagem == "SUCCESS") {
		
		campoTexto = $(textSuccess);
		
		montarTextoMensagem(campoTexto, mensagens);
		
		$(divSuccess).show(1000, esconde(isFromDialog, divSuccess));
		
	} else if (tipoMensagem == "WARNING") {
		
		campoTexto = $(textWarning);
		
		montarTextoMensagem(campoTexto, mensagens);
		
		$(divWarning).show(1000, esconde(isFromDialog, divWarning));
		
	} else if (tipoMensagem == "ERROR") {
		
		campoTexto = $(textError);
		
		montarTextoMensagem(campoTexto, mensagens);
		
		$(divError).show(1000, esconde(isFromDialog, divError));
	}
}

function montarTextoMensagem(campoTexto, mensagens) {
	
	campoTexto.html('');

	$.each(mensagens, function(index, value) {
		campoTexto.append(value + '</br>');
	});
}

function isNumeric(a){
	return !isNaN(parseFloat(a))&&isFinite(a);
}

function esconde(isFromDialog, div) {
	
	if (isFromDialog) {
		
		messageDialogTimeout =
			setTimeout(function() {
				$(div).removeAttr("style").fadeOut();
			}, 5000);
		
	} else {
		
		messageTimeout = 
			setTimeout(function() {
				$(div).removeAttr("style").fadeOut();
			}, 5000);	
	}
}

function clearMessageTimeout() {
	
	clearTimeout(messageTimeout);
	
	$('#effectSuccess').hide();
	$('#effectWarning').hide();
	$('#effectError').hide();
}

function clearMessageDialogTimeout() {
	
	clearTimeout(messageDialogTimeout);
	
	$("div[name='effectSuccessDialog']").hide();
	$("div[name='effectWarningDialog']").hide();
	$("div[name='effectErrorDialog']").hide();
}

function montarComboBox(result, incluirTodos) {
	var options = "";
	
	if (incluirTodos) {
		options += "<option selected='selected'  value=''>Todos</option>";
	}
	
	$.each(result, function(index, row) {
		options += "<option value='" + row.key.$ + "'>" + row.value.$ + "</option>";
	});
	
	return options;
}

function replaceAll(string, token, newtoken) {
	while (string.indexOf(token) != -1) {
 		string = string.replace(token, newtoken);
	}
	return string;
}

function intValue(valor) {

	return parseInt(valor, 10);
}

function removeMascaraPriceFormat(field) {
	
	field = replaceAll(field, ",", "");
	field = replaceAll(field, ".", "");
	
	return field;
}
