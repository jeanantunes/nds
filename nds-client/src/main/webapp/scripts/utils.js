var disabledEnterModalConfirmar = [];

//Sobrescrita da funcao toFixed para arredondamento no Chrome
Number.prototype.round = function(digits) {
    digits = Math.floor(digits);
    if (isNaN(digits) || digits === 0) {
        return Math.round(this);
    }
    if (digits < 0 || digits > 16) {
        throw 'RangeError: Number.round() digits argument must be between 0 and 16';
    }
    var multiplicator = Math.pow(10, digits);
    return Math.round(this * multiplicator) / multiplicator;
};

Number.prototype.toFixed = function(digits) {
    digits = Math.floor(digits);
    if (isNaN(digits) || digits === 0) {
        return Math.round(this).toString();
    }
    var parts = this.round(digits).toString().split('.');
    var fraction = parts.length === 1 ? '' : parts[1];
    if (digits > fraction.length) {
        fraction += new Array(digits - fraction.length + 1).join('0');
    }
    return parts[0] + '.' + fraction;
};

var messageTimeout;
var messageDialogTimeout;

$(document).ready(function() {

	jQuery(":input[maxlength]").keyup(function () {
	    var focus = jQuery(this);
	    var valFocus = null;
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
	});
	
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
			
		} else if( keycode == $.ui.keyCode.ENTER ) {
			
			//Confirmação genérica para modais por tec ENTER
			
			var refButtonsDialog = $(".ui-dialog:visible").find("button");
			
			//Considerar apenas bts Confirmação/Desistência pela abrangência sobre sistema.
			if( refButtonsDialog.size() == 2 ) {

				disableConfirmar = false;
				if(disabledEnterModalConfirmar.length > 0) {
					for(index in disabledEnterModalConfirmar) {
						if($($('#'+ disabledEnterModalConfirmar[index]).parent().find('.ui-button')[0]).html().indexOf('Confirmar') > -1) {
							disableConfirmar = true;
							$($('#'+ disabledEnterModalConfirmar[index]).parent().find('.ui-button')[0]).blur();
						}
					}
				}
				
				//Verifica se o segundo botão está selecionado / Caso sim não acionará Confirmação por ser Desistência selecionado
 				if(!disableConfirmar && eventoJs.target != refButtonsDialog[1] && eventoJs.target != $('.pcontrol input:visible')[0]){
					refButtonsDialog.first().click(); /* Assuming the first one is the action button */
				}
				return true;
		    }else{
		    	return false;
		    }
		} else {
			
			//Navegação pelas abas por CTRL+(Seta Esquerda | Direita)
			
			var refTabs = $("li", $('.ui-tabs-nav'));
			var qtdAbasAbertas =  $('.ui-corner-top').size();
			if(qtdAbasAbertas > 1) {
				
				if(keycode == 17){
					pressedCtrl = true; 
				}

				if(keycode == 37 || keycode == 39){
					
					var indexSecionado = $('.ui-tabs-selected').index();
					var indexSelecionar = indexSecionado;
					
					if(keycode == 37 && pressedCtrl == true) {//Esquerda
						if(indexSecionado == 0) {
							indexSelecionar == qtdAbasAbertas-1;
						} else {
							indexSelecionar--;
						}
					}else if(keycode == 39 && pressedCtrl == true) {//Direita
						if(indexSecionado == (qtdAbasAbertas-1)) {
							indexSelecionar = 0;
						} else {
							indexSelecionar++;
						}
					}
					
					if(indexSelecionar != -1) {
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
		
		campoTexto.append(value.replace(/\n/g, '<br />'));
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

function carregarCombo(url, params, element, selected, idDialog, callback){
    $.postJSON(url, params,
        function(result){
            var combo =  montarComboBox(result, false);
            combo = newOption('-1', 'Selecione...') + combo;
            
            if($(element).length>1){
            	$.each(element,function(idx,cpm){
            		$(cpm).html(combo);
                	if (selected) {
                		$(cpm).val(selected);
                	} else {
                		$(cpm).val('-1');
                	}
            		
            	});
            }else{
            	$(element).html(combo);
            	if (selected) {
            		$(element).val(selected);
            	} else {
            		$(element).val('-1');
            	}
            	
            }
            if (callback) {
            	callback();
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
    return "<option value='" + value + "'>" + label + "</option>";
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

	if (!valor) {
		
		return 0;
	}
	
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
	
	return parseFloat(field).toFixed(4);
}

function floatToPrice(field, decimalPlaces) {
	
	var price = String(field);

	if (price.indexOf(".") == -1) {
		price = price + ".0000";
	}
	
	if(price.indexOf(",") > -1) {
		price = price.replace(",", "");
	}
	
    var part = price.split(".");
    return part[0].split("").reverse().reduce(function(acc, price, i, orig) {
        return  price + (i && !(i % 3) ? "." : "") + acc;
    }, "") + "," + (part[1]+"0000").substr(0, (decimalPlaces ? decimalPlaces : 2));
    
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

function formatarMilhar(num) {
    x = 0;

    if (num < 0) {
        num = Math.abs(num);
        x = 1;
    }

    if (isNaN(num))
        num = "0";

    num = Math.floor((num * 100 + 0.5) / 100).toString();

    for (var i = 0; i < Math.floor((num.length - (1 + i)) / 3); i++)
        num = num.substring(0, num.length - (4 * i + 3)) + '.' + num.substring(num.length - (4 * i + 3));

    ret = num;

    if (x == 1) ret = ' - ' + ret;

    return ret;
}

function getCurrentTabContainer(){
	var currentTabId = $(".ui-tabs-selected").find("a").attr("href");
	var currentTab = $(currentTabId);
	
	return currentTab;
}

function focusSelectRefField(objectField){
	setTimeout (function () {objectField.focus();objectField.select();}, 500);
}

function focusFirstContentView(context){
	setTimeout (function () {$(context).find('select:visible, input:text:visible, textarea:visible').first().focus();}, 1);
}

function focusFirstContentModal(){
	setTimeout (function () {$(".ui-dialog:visible").find('select:visible, input:visible, textarea:visible').first().focus();}, 1);
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
	
	$('a[isEdicao="true"][isedicao="true"]',workspace).each(function() {
		this.href="#";
		$(this).removeAttr("onClick");
		$(this).unbind('click');
		$(this).click(function(e){
			exibirAcessoNegado();
		});
	});
	
	$('input[isEdicao="true"][isedicao="true"]',workspace).each(function() {
		$(this).attr("disabled",true);
		$(this).removeAttr("onClick");
	});
}

function exibirAcessoNegado() {
	exibirMensagem('WARNING',['Acesso Negado.']);
}

function verificarPermissaoAcesso(workspace) {
	
	if($('#permissaoAlteracao',workspace).val() == undefined){
		return true;
	}
	
	if($('#permissaoAlteracao',workspace).val()=="true")
		return true;
	
	exibirAcessoNegado();
	
	return false;
}

function removeTabByTitle(title) {
	
	var tabToRemove=-1;
	$("#workspace li.ui-state-default a").each(function(idx,comp){
		if($(comp).text()==title){
			tabToRemove=idx;
		}
	});
	if(tabToRemove>-1){
		$("#workspace").tabs("remove",tabToRemove);
	}
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

/**
 * Objeto para gerenciar quais keys estão pressionadas
 * 
 */
var handler = {
		keys:[],
		//Ativa o Handler
		on:function() {
			$(document).keydown(handler.keydown);
			$(document).keyup(handler.keyup);
		},
		//Desativa o Handler
		off:function() {
			$(document).unbind('keydown',  handler.keydown);
			$(document).unbind('keyup', handler.keyup);
		},
		keydown:function(e){
			handler.keys[e.which] = true;
		},
		keyup:function(e) {
			delete handler.keys[e.which];
		}
};

/**
 * Objeto para gerenciar pageRefresh
 */
var pageRefresh = {
	
	/**
	 * Desabilita o page refresh e informa o usuario sobre os processos que estão rodando
	 * quando ele tenta fechar o browser ou a aba.
	 * 
	 * @param message - mensagem de aviso a ser exibida, quando o usuario tentar fechar o browser ou a aba.
	 */
	disable : function(message) {
		
		handler.on();
		
		$(document).keydown(disableShortcutRefresh);
		
		var defaultMessage = "Um processo está sendo executado";
		
		window.onbeforeunload = function() {
			return message || defaultMessage;
		};
		
	},
	
	/**
	 * ReAbilita o page refresh
	 */
	enable: function() {
		
		handler.off();
		
		//$(document).unbind('keydown', disableShortcutRefresh);
		
		//window.onbeforeunload = null;
		
		window.onbeforeunload = function(e) {
			
			if(!reloadPage) {

				$.ajax({
					type: 'POST',
					async: false,
					url: contextPath + "/j_spring_security_logout",
					data: ''
				});
			}
		};
		
	},
	
	enableLinks: function() {
		
		handler.off();
		
		$(document).unbind('keydown', disableShortcutRefresh);
		
		window.onbeforeunload = null;
	}
};

document.onkeydown = fkey;
document.onkeypress = fkey;
document.onkeyup = fkey;

var reloadPage = false;

function fkey(e) {
	e = e || window.event;
	if( reloadPage ) return; 
	
	if (pressedF5(e) || pressedCtrlR(e)) {
		reloadPage = true;
	}
}

/**
 * Desabilita os atalhos para pageRefresh (F5 e Ctrl+R)
 * 
 * @param e - event
 */
function disableShortcutRefresh(e) {
	if(!e) throw "this function must be bind with a key event";
	
	if (pressedF5(e) || pressedCtrlR(e)) {
		
		reloadPage = true;
		
		e.returnValue = false;
	    e.keyCode = 0;
	    
	    e.preventDefault();
	    e.stopPropagation();
	    
	    return false;
	}
	
}

function pressedF5(e) {
	
	var result = ((e.which || e.keyCode) == 116);
	
	reloadPage = result ? true : false;
	
	return result; 
};

function pressedCtrlR(e) {
	
	var result = false;
	if(navigator.platform == "MacIntel") {
		result = (handler.keys[82] && handler.keys[91]);
		
		reloadPage = result ? true : false;
		
		return result;
	} 
	
	result = ((e.wich || e.keyCode) == 82 && (e.ctrlKey || window.event.ctrlKey));
	
	reloadPage = result ? true : false;
	
	return result;
};

function addTabWithPost(tabs, label, postResponse, blankPath) {
	tabs.tabs({load : function( event, ui ) { $('#'+ ui.panel.id).html(postResponse); }});
	tabs.tabs('addTab', label, blankPath);
}


function insertTelaAnalise(divToHide, divToShow, estudo){

	var matrizSelecionado_estudo =null;
	
	if(typeof(histogramaPosEstudoController)!="undefined"){
		matrizSelecionado_estudo = histogramaPosEstudoController.matrizSelecionado.estudo;
	}
	
    var idEstudo =  matrizSelecionado_estudo || estudo;
    var urlAnalise = contextPath + '/distribuicao/analise/parcial/?id=' + idEstudo;
    if ($('#parcial').val() === 'true') {
		urlAnalise += '&modoAnalise=PARCIAL';
	}

	$.get(
			urlAnalise,
			null, // parametros
			function(html){ // onSucessCallBack
				$(divToHide).hide();
				$(divToShow).html(html);
				$(divToShow).show();

				$( divToShow + ' #botaoVoltarTelaAnalise').attr("onclick","").click(function voltarTelaAnalise(){
					$(divToShow).hide();
					$(divToHide).show();
				});
		});
	
}

/**
 * @returns intervalo de datas validos (true) ou invalidos (false)
 */
function validarDatas(dataInicial, dataFinal) {
	
	var dataSemFormatacao_1 = parseInt(dataInicial.split("/")[2].toString() + dataInicial.split("/")[1].toString() + dataInicial.split("/")[0].toString());
	var dataSemFormatacao_2 = parseInt(dataFinal.split("/")[2].toString() + dataFinal.split("/")[1].toString() + dataFinal.split("/")[0].toString());
	 
	 if (dataSemFormatacao_2 >= dataSemFormatacao_1){
	   return true;
	 }else{
		 return false;		
	 }
    
    return false;
}

/**
* Ordena o Table Model considerando a coluna informada
*/
function obterTableModelOrdenado(tableModel, col){

    var sizeTableModel = tableModel.rows.length;

    var rows_tm = tableModel.rows;
  
    var tm_ordenado = {
    		               page : tableModel.page,
		                   rows : new Array(),
		                   total : tableModel.total
                      };
  
    while (tm_ordenado.rows.length < sizeTableModel){

        var size_tm = rows_tm.length;
    	  
    	var valorMaior = '0';

    	var i = 0;
    	  
    	var i_remove = -1;
    	  
    	while (i < size_tm) {
    		  
            row = rows_tm[i];
    		  
    		cell = row.cell;		

    		if (cell[col] > valorMaior){
    			  
    	        valorMaior = cell[col];

    			i_remove = i; 
    		}
    		  
			i++;
    	}
    	  
    	tm_ordenado.rows.push(rows_tm[i_remove]);
    	  
        rows_tm.splice(i_remove,1);
    }		
    
    return tm_ordenado;
}

//simply visual, let's you know when the correct iframe is selected
/*$(window).on("focus", function(e) {
 $("html, body").css({ background: "#FFF", color: "#000" })
 .find("h2").html("THIS BOX NOW HAS FOCUS<br />F5 should not work.");
})
.on("blur", function(e) {
 $("html, body").css({ background: "", color: "" })
 .find("h2").html("CLICK HERE TO GIVE THIS BOX FOCUS BEFORE PRESSING F5");
});*/

//@ sourceURL=util.js