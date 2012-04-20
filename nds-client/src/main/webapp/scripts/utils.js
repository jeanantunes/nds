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

	var campoTexto = $("#idTextoMensagem");

	campoTexto.html('');

	$.each(mensagens, function(index, value) {
		campoTexto.append(value + '</br>');
	});

	$("#effect").removeClass("ui-state-error");
	$("#effect").removeClass("ui-state-highlight");
	$("#effect").removeClass("ui-state-default");
	
	if (tipoMensagem == "SUCCESS") {
		$("#effect").addClass("ui-state-default");
	} else if (tipoMensagem == "WARNING"){
		$("#effect").addClass("ui-state-highlight");
	} else if (tipoMensagem == "ERROR"){
		$("#effect").addClass("ui-state-error");
	}
	
	$('#effect').show(1000, esconde("effect"));
}

function exibirMensagemDialog(tipoMensagem, mensagens) {
	
	var campoTexto = $(".effectDialogText");

	campoTexto.html('');

	$.each(mensagens, function(index, value) {
		campoTexto.append(value + '</br>');
	});

	$(".effectDialog").removeClass("ui-state-error");
	$(".effectDialog").removeClass("ui-state-highlight");
	$(".effectDialog").removeClass("ui-state-default");
	
	if (tipoMensagem == "SUCCESS") {
		$(".effectDialog").addClass("ui-state-default");
	} else if (tipoMensagem == "WARNING"){
		$(".effectDialog").addClass("ui-state-highlight");
	} else if (tipoMensagem == "ERROR"){
		$(".effectDialog").addClass("ui-state-error");
	}
	
	$('.effectDialog').show(1000, escondeDialog("effectDialog"));
}

function isNumeric(a){
	return !isNaN(parseFloat(a))&&isFinite(a);
}

function esconde(idDiv) {
	messageTimeout = 
		setTimeout(function() {
			$('#' + idDiv).removeAttr("style").fadeOut();
		}, 5000);
}

function escondeDialog(idDiv) {
	messageDialogTimeout =
		setTimeout(function() {
			$('.' + idDiv).removeAttr("style").fadeOut();
		}, 5000);
}

function clearMessageTimeout() {
	
	clearTimeout(messageTimeout);
	
	$('#effect').hide();
}

function clearMessageDialogTimeout() {
	
	clearTimeout(messageDialogTimeout);
	
	$('.effectDialog').hide();
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
