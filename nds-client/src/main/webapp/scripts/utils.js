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
	
	var pressedCtrl = false;
	$(document).keyup(function (e) {
		var eventoJs= e;
		var keycode = e.which;
		if (window.event) {
			eventoJs = window.event;
			keycode = eventoJs.keyCode;
		}
		
		if(keycode == 17){
			pressedCtrl=false; 
		}
	})
	
	$(document.body).keydown(function(e) {
		
		var eventoJs= e;
		var keycode = e.which;
		if (window.event) {
			eventoJs = window.event;
			keycode = eventoJs.keyCode;
		}
		
		if (keycode == 32 && ($("#effectWarning").css("display") == "block" || $("#effectError").css("display") == "block"
				|| $("#effectSuccess").css("display") == "block")) {
			esconde(false, $('#effectWarning'));
			esconde(false, $('#effectError'));
			esconde(false, $('#effectSuccess'));

			focusFirstContentView(this);
			
		}else if( keycode == $.ui.keyCode.ENTER ) {
			
			//Confirmação genérica para modais por tec ENTER
			
			var refButtonsDialog = $(".ui-dialog:visible").find("button");
			
			//Considerar apenas bts Confirmação/Desistência pela abrangência sobre sistema.
			if( refButtonsDialog.size()==2 ) {

				//Verifica se o segundo botão está selecionado / Caso sim não acionará Confirmação por ser Desistência selecionado
				if(eventoJs.target!=refButtonsDialog[1]){
					refButtonsDialog.first().click(); /* Assuming the first one is the action button */
				}
				return true;
		    }
		}else{
			
			//Navegação pelas abas por CTRL+(Seta Esquerda | Direita)
			
			var refTabs = $("li", $('.ui-tabs-nav'));
			var qtdAbasAbertas =  $('.ui-corner-top').size()
			if(qtdAbasAbertas > 1){
				
				if(keycode == 17){
					pressedCtrl = true; 
				}

				if(keycode == 37 || keycode == 39){
					
					var indexSecionado = $('.ui-tabs-selected').index();
					var indexSelecionar = indexSecionado;
					
					if(keycode == 37 && pressedCtrl == true) {//Esquerda
						if(indexSecionado == 0){
							indexSelecionar == qtdAbasAbertas-1;
						}else{
							indexSelecionar--;
						}
					}else if(keycode == 39 && pressedCtrl == true) {//Direita
						if(indexSecionado == (qtdAbasAbertas-1)){
							indexSelecionar = 0;
						}else{
							indexSelecionar++;
						}
					}
					
					if(indexSelecionar != -1){
						refTabs.children('a')[indexSelecionar].click();
					}
				}
			}
		}
	});
	
	//Move foco para primeiro campo do modal ao abrir modal
	$(document).bind("dialogopen", function() {
		focusFirstContentModal();
	});
	
	//Move foco para primeiro campo ao fechar modal
	$(document).bind("dialogclose", function() {
		focusFirstContentView(this);
	});	
	
	
	//Foco primeiro campo ao carregar aba 
//	$("#workspace").bind('focus',function(){
//		focusFirstContentView(document);
//	});
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
						   divError, textError, false);
	
	
	$(document.body).bind('keydown.hideMessages', jwerty.event('ESC',
	function(){
		esconde(false,divSuccess);
		esconde(false,divWarning);
		esconde(false,divError);
	}));

}

function exibirMensagemDialog(tipoMensagem, mensagens, idDialog) {
	
	exibirMensagem(tipoMensagem, mensagens);
	
	// Not using this for now
	/*clearMessageDialogTimeout(idDialog);
	
	var divSuccess = $("div[name='" + idDialog + "effectSuccessDialog']");
	var divWarning = $("div[name='" + idDialog + "effectWarningDialog']");
	var divError = $("div[name='" + idDialog + "effectErrorDialog']");
	
	var textSuccess = $("b[name='" + idDialog + "textSuccessDialog']");
	var textWarning = $("b[name='" + idDialog + "textWarningDialog']");
	var textError = $("b[name='" + idDialog + "textErrorDialog']");
	
	montarExibicaoMensagem(true, tipoMensagem, mensagens,
						   divSuccess, textSuccess,
						   divWarning, textWarning,
						   divError, textError, true);*/
	
}

function montarExibicaoMensagem(isFromDialog, tipoMensagem, mensagens,
								divSuccess, textSuccess,
							    divWarning, textWarning,
							    divError, textError, isPopUp) {
	
	if(!$('#disabledBackground') || $('#disabledBackground').size() < 1) {
		$("<div id='disabledBackground' class='ui-widget-overlay disabledBackground' style='width: 100%; height: 100%; z-index: 10001;'/>").appendTo($("#divCorpo"));
	}
	
	var campoTexto;

	if (tipoMensagem == "SUCCESS") {
		
		campoTexto = $(textSuccess);
		
		montarTextoMensagem(campoTexto, mensagens);

		$(divSuccess).show(0, esconteAutomatico(divSuccess));
		
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

	$(div).fadeOut("slow");
	
	// Remove a div que deixa o fundo desabilitado e escuro 
	while($("#disabledBackground").length != 0) {
		$("#disabledBackground").remove();
	}

}

function esconteAutomatico(div) {
	messageTimeout = 
		setTimeout(function() {
			$(div).fadeOut("slow");
		}, 3000);
	
	// Remove a div que deixa o fundo desabilitado e escuro 
	while($("#disabledBackground").length != 0) {
		$("#disabledBackground").remove();
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
function montarComboBoxCustomJson(result, incluirTodos, labelTodos) {
	var options = "";
	
	if (incluirTodos) {
		if(!labelTodos){
			labelTodos = 'Todos';
		}
		options += "<option selected='selected'  value=''>"+labelTodos+"</option>";
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

function doGet(url, params, target) {
	
	var element;
	
	var href = url;
	
	if (params && params.length > 0) href = href.concat("?");
	
	for(var index in params) {
		
		href = href.concat(params[index].name+"="+params[index].value);
		
		if(index+1 < params.length)
			href = href.concat("&&");
	}
	
	element = document.createElement("a");
	
	element.href   = href;
	element.target = target;
	
	element.click();
}

function newOption(value, label) {
    return "<option value='" + value + "'>" + label + "</option>"
}

function replaceAll(string, token, newtoken) {
	if (string){
		while (string.indexOf(token) != -1) {
	 		string = string.replace(token, newtoken);
		}
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

function priceToFloat(field) {
	
	field = replaceAll(field, ".", "");
	field = replaceAll(field, ",", ".");
	
	return parseFloat(field).toFixed(2);
}

function floatToPrice(field) {
	
	var price = String(field);

	if (price.indexOf(".") == -1) {
		price = price + ".00";
	}
	
	if(price.indexOf(",") > -1) {
		price = price.replace(",", "");
	}
	
    var part = price.split(".");
    return part[0].split("").reverse().reduce(function(acc, price, i, orig) {
        return  price + (i && !(i % 3) ? "." : "") + acc;
    }, "") + "," + (part[1]+"0").substr(0, 2);
    
}

function sumPrice(price1, price2){
	
	return floatToPrice((parseFloat(priceToFloat(price1)) + parseFloat(priceToFloat(price2))).toFixed(2));
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


/*Permite apenas número no campo input[type=text] 
 * Adicionar no onkeydown do input()
 * */
function onlyNumeric(event){
        // Allow: backspace, delete, tab, escape, and enter
        if ( event.keyCode == 46 || event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 27 || event.keyCode == 13 || 
             // Allow: Ctrl+A
            (event.keyCode == 65 && event.ctrlKey === true) || 
             // Allow: home, end, left, right
            (event.keyCode >= 35 && event.keyCode <= 39)) {
                 // let it happen, don't do anything
                 return;
        }
        else {
            // Ensure that it is a number and stop the keypress
            if (event.shiftKey || (event.keyCode < 48 || event.keyCode > 57) && (event.keyCode < 96 || event.keyCode > 105 )) {
                event.preventDefault(); 
            }   
        }
}

function focusSelectRefField(objectField){
	setTimeout (function () {objectField.focus();objectField.select()}, 500);
}

function focusFirstContentView(context){
	setTimeout (function () {$(context).find('select:visible, input:text:visible, textarea:visible').first().focus()}, 1);
}

function focusFirstContentModal(){
	setTimeout (function () {$(".ui-dialog:visible").find('select:visible, input:visible, textarea:visible').first().focus()}, 1);
}

function keyEventEnterAux(e){
	var eventoJs= e;
	var keycode = e.which;
	if (window.event) {
		eventoJs = window.event;
		keycode = eventoJs.keyCode;
	}
	
	if( keycode == $.ui.keyCode.ENTER ) {
		if(eventoJs.target!=":input"){
			return true;
		}
	}
	
	return false;
}

function visibleOverlay(){
	return $("div.ui-widget-overlay").val()=="";
}

function bloquearItensEdicao(workspace) {

	if($('#permissaoAlteracao',workspace).val()=="true")
		return;
	
	$('a[isEdicao="true"]',workspace).each(function() {
		this.href="#";
		$(this).removeAttr("onClick");
		$(this).unbind('click');
		$(this).click(function(e){
			exibirAcessoNegado();
		});
	});
	
	$('input[isEdicao="true"]',workspace).each(function() {
		$(this).attr("disabled",true);
		$(this).removeAttr("onClick");
	});
}

function exibirAcessoNegado() {
	exibirMensagem('WARNING',['Acesso Negado.']);
}

function verificarPermissaoAcesso(workspace) {
	
	if($('#permissaoAlteracao',workspace).val()=="true")
		return true;
	
	exibirAcessoNegado();
	
	return false;
}

function direcionar(novaTab, path){
	    
	$('#workspace').tabs('remove', $('#workspace').tabs('option','selected'));
		
	$(".tipsy").hide();
	
	$('#workspace').tabs('addTab', novaTab, contextPath + path + "?random=" + Math.random());
}

function adicionarTab(novaTab, path){
	
	$(".tipsy").hide();
	
	$('#workspace').tabs('addTab', novaTab, contextPath + path + "?random=" + Math.random());
}

//@ sourceURL=util.js