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

	//clearMessageTimeout();
	
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

function exibirMensagemDialog(tipoMensagem, mensagens, idDialog) {
	
	clearMessageDialogTimeout(idDialog);
	
	var divSuccess = $("div[name='" + idDialog + "effectSuccessDialog']");
	var divWarning = $("div[name='" + idDialog + "effectWarningDialog']");
	var divError = $("div[name='" + idDialog + "effectErrorDialog']");
	
	var textSuccess = $("b[name='" + idDialog + "textSuccessDialog']");
	var textWarning = $("b[name='" + idDialog + "textWarningDialog']");
	var textError = $("b[name='" + idDialog + "textErrorDialog']");
	
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
		
		$(divSuccess).show(0);
		//$(divSuccess).show(0, esconde(isFromDialog, divSuccess));
		
	} else if (tipoMensagem == "WARNING") {
		
		campoTexto = $(textWarning);
		
		montarTextoMensagem(campoTexto, mensagens);
		
		$(divWarning).show(0);
		//$(divWarning).show(0, esconde(isFromDialog, divWarning));
		
	} else if (tipoMensagem == "ERROR") {
		
		campoTexto = $(textError);
		
		montarTextoMensagem(campoTexto, mensagens);
		
		$(divError).show(0);
		//$(divError).show(0, esconde(isFromDialog, divError));
	}
}

function montarTextoMensagem(campoTexto, mensagens) {
	
	campoTexto.html('');

	$.each(mensagens, function(index, value) {
		
		if (campoTexto.html() != '') {
			campoTexto.append('</br>');
		}
		
		campoTexto.append(value);
	});
}

function isNumeric(a){
	return !isNaN(parseFloat(a))&&isFinite(a);
}

function esconde(isFromDialog, div) {
	
	if (isFromDialog) {
		
		messageDialogTimeout =
			setTimeout(function() {
				$(div).fadeOut("slow");
			}, 0);
			//}, 5000);
		
	} else {
		
		messageTimeout = 
			setTimeout(function() {
				$(div).fadeOut("slow");
			}, 0);	
			//}, 5000);	
	}
}

function clearMessageTimeout() {
	
	clearTimeout(messageTimeout);
	
	$('#effectSuccess').hide();
	$('#effectWarning').hide();
	$('#effectError').hide();
}

function clearMessageDialogTimeout(idDialog) {
	
	clearTimeout(messageDialogTimeout);
	
	var dialog = "";
	
	if(idDialog){
		dialog = idDialog;
	}
	
	$("div[name='" + dialog + "effectSuccessDialog']").hide();
	$("div[name='" + dialog + "effectWarningDialog']").hide();
	$("div[name='" + dialog + "effectErrorDialog']").hide();
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
function montarComboBoxCustomJson(result, incluirTodos) {
	var options = "";
	
	if (incluirTodos) {
		options += "<option selected='selected'  value=''>Todos</option>";
	}
	
	$.each(result, function(index, row) {
		options += "<option value='" + row.key + "'>" + row.value + "</option>";
	});
	
	return options;
}

function montarComboBoxUnicaOpcao(value, label, element) {
        $(element).html(newOption(value, label));
}

function carregarCombo(url, params, element, selected, idDialog ){
    $.postJSON(url, params,
        function(result){
            var combo =  montarComboBox(result, false);
            combo = newOption('-1', 'Selecione...') + combo;
            $(element).html(combo);
            if (selected) {
                $(element).val(selected);
            } else {
                $(element).val('-1');
            }
        },null,true, idDialog);
}


function newOption(value, label) {
    return "<option value='" + value + "'>" + label + "</option>"
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

function clickLineFlexigrid(inputCheck, select) {
	
	var line = $(inputCheck).parents()[2];
	
	if (select) {
		
		$(line).addClass("trSelected");
		
	} else {
		
		$(line).removeClass("trSelected");
	}
}



/*Função que padroniza telefone (11) 4184-1241*/
function Telefone(v){
	v=v.replace(/\D/g,"");                         
	v=v.replace(/^(\d\d)(\d)/g,"($1) $2");
	v=v.replace(/(\d{4})(\d)/,"$1-$2");   
	return v;
}

/*Função que padroniza telefone (11) 41841241*/
function TelefoneCall(v){
	v=v.replace(/\D/g,"");                 
	v=v.replace(/^(\d\d)(\d)/g,"($1) $2"); 
	return v;
}

/*Função que padroniza CPF*/
function Cpf(v){
	v=v.replace(/\D/g,"");                               
	v=v.replace(/(\d{3})(\d)/,"$1.$2");     
	v=v.replace(/(\d{3})(\d)/,"$1.$2");
	v=v.replace(/(\d{3})(\d{1,2})$/,"$1-$2");
	return v;
}

/*Função que padroniza CNPJ*/
function Cnpj(v){
	v=v.replace(/\D/g,"")    ;                          
	v=v.replace(/^(\d{2})(\d)/,"$1.$2")     ; 
	v=v.replace(/^(\d{2})\.(\d{3})(\d)/,"$1.$2.$3") ;
	v=v.replace(/\.(\d{3})(\d)/,".$1/$2")     ;         
	v=v.replace(/(\d{4})(\d)/,"$1-$2")     ;                   
	return v;
}


