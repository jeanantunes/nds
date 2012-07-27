// JavaScript Document
$(document).ready(function(){
	$("#nav-one li").hover(
		function(){ $("ul", this).fadeIn("fast"); }, 
		function() { } 
	);
	if (document.all) {
		$("#nav-one li").hoverClass ("sfHover");
	}
});

$.fn.hoverClass = function(c) {
	return this.each(function(){
		$(this).hover( 
			function() { $(this).addClass(c);  },
			function() { $(this).removeClass(c); }
		);
	});
};

$.fn.disable = function() {
	return this.each(function() {
		if (typeof this.disabled != "undefined") this.disabled = true;
	});
};

$.fn.enable = function() {
	return this.each(function() {
		if (typeof this.disabled != "undefined") this.disabled = false;
	});
};

$.fn.check = function() {
	return this.each(function() {
		if (typeof this.checked != "undefined") this.checked = true;
	});
};

$.fn.uncheck = function() {
	return this.each(function() {
		if (typeof this.checked != "undefined") this.checked = false;
	});
};

var sURL = unescape(window.location.pathname);

function doLoad()
{
	setTimeout( "refresh()", 2*1000 );
}

function refresh()
{
	window.location.href = sURL;
}

//<!--
function refresh()
{
	window.location.replace( sURL );
}
//-->
function refresh()
{

	window.location.reload( false );
}
//valor = true -> mostra do div
function mostra_div(p, valor) {
 for (var x in p) {
 var e = document.getElementById(p[x]);
  e.style.display = valor ? "block" : "none";
 }
}

function checkAll(todos, checkgroupName) {
	if(todos.checked == false) {
		$("input[name=" + checkgroupName + "]").each(function(){
			if (!$(this).is(':disabled')) {
				$(this).uncheck();
			}
		});
	}		
	else {										
		$("input[name=" + checkgroupName + "]").each(function(){
			if (!$(this).is(':disabled')) {
				$(this).check();
			}
		});
	}	
}

function verifyAtLeastOneChecked(checkgroupName) {
	var checked = $("input[name=" + checkgroupName + "]:checked").length;	
	return checked >= 1;
}

function verifyCheck(todos){
	$(todos).uncheck();
}

function removeFromArray(array, item) {
    var newArray = [];
    var i;
    for (i=0; i<array.length; i++) {
	if (array[i] != item) {
	    newArray.push(array[i]);	
	}
    }			
    return newArray;			
}

/**
 * Remove os caracteres especiais de um valor<br><br>
 * <b>Exemplo 01:</b>  <br>
 * <br>
 * removeSpecialCharacteres(<b>32.836.093/0001-8a</b>);<br>
 * return <b>3283609300018a</b>;<br>
 * <br><br>
 * <b>Exemplo 02:</b>  <br>
 * removeSpecialCharacteres(<b>32.836.093/0001-8a</b>, <b>["a", "8"]</b>)<br>
 * return <b>32360930001</b>;<br>
 * <br>
 * @param value valor para remover os caracteres
 * @param extraCharacteres  caracteres adicionais para serem removidos do valor;
 * @returns valor sem caracteres especiais;
 */
function removeSpecialCharacteres(value, extraCharacteres) {
	
	//add default special characteres to remove;
	var specialCharacteres = ["-", ".", "/"];
	
	if (extraCharacteres) {
		specialCharacteres = specialCharacteres.concat(extraCharacteres);
	}
	
	for (var index in specialCharacteres) {
		value = value.split(specialCharacteres[index]).join("");
	}
	
	return value; 
}

/**
 * recebe uma data e retorna uma string no formato dd/MM/AAAA
 * 
 * @param date data
 */
function formatDateToString(date) {
	
	var day = date.getDate();
	var mouth = date.getMonth()+1;
	var year = date.getFullYear();
	
	if (day < 10) 
		day = "0"+day;
	
	if (mouth < 10) 
		mouth = "0"+mouth;
	
	return day+"/"+mouth+"/"+year;
}

function serializeObjectToPost(objectName, object, obj) {
	obj = (obj)?obj:new Object();
	for ( var propriedade in object) {
		if(object[propriedade] || object[propriedade] == 0){
			if(jQuery.isPlainObject(object[propriedade])){
				serializeObjectToPost(objectName + '.' + propriedade, object[propriedade], obj);
			}else{
				obj[objectName + '.' + propriedade] = object[propriedade];
			}
			
		}
	}
	return obj;
};

function serializeArrayToPost(listaName, lista, obj) {		
	obj = (obj)?obj:new Object();
	var i;
	for(i = 0; i < lista.length;i++){		
		var object = lista[i];
		
		if($.isPlainObject(object)){
			for ( var propriedade in object) {
				if(object[propriedade] || object[propriedade] == 0){
					obj[listaName +'['+i+'].'+propriedade] = object[propriedade];
				}
			}
		}else{
			obj[listaName +'['+i+']'] = object;
		}
		
	}
	
	return obj;
};
/**
 * Transforma um array em um array esperaro pelo flexiggrid
 *
 * @param list
 * @returns {Array} 
 */
function toFlexiGridObject(list){
	
	var rows = new Array();
	for (var index in list) {		
		rows.push({"id" : rows.length,	"cell" : list[index]});		
	}
	return rows;
}



function concatObjects(obj1, obj2){
	var obj = obj1;
	for ( var propriedade in obj2) {
		obj[propriedade] = obj2[propriedade];
	}
	return obj;
}

//callback function to bring a hidden box back
	
$(document).ready(function(){	
	$("#selFornecedor").click(function() {
		$(".menu_fornecedor").show().fadeIn("fast");
	})

	$(".menu_fornecedor").mouseleave(function() {
		$(".menu_fornecedor").hide();
	});
	
})
	
	
	
	
		function callback() {
			setTimeout(function() {
				$( "#effect:visible").removeAttr( "style" ).fadeOut();

			}, 1000 );
		};
		function mostrar(){
			$(".grids").show();
		}
		function mostrar_luminoso(){
			$(".luminoso").show();
		}
		function mostrar_materialPromo(){
			$(".materialPromocional").show();
		}
		
		function mostrar_nfe(){
			$(".nfes").show();
			$("#bt_pesquisar").hide();
			
		}
		function mostrarTabPF(){
			$(".exPF").show();
			$(".exPJ").hide();
			$("#tabs").show();
			
		}
		function mostrarTabPJ(){
			$(".exPF").hide();
			$(".exPJ").show();
			$("#tabs").show();
		}
		
		function mostrarTabFiadorPF(){
			$(".fiadorPF").show();
			$(".fiadorPJ").hide();
			
		}
		function mostrarTabFiadorPJ(){
			$(".fiadorPF").hide();
			$(".fiadorPJ").show();
		}
		
		function confirmar(){
		$(".dados").show();
		}
		function pesqEncalhe(){
			$(".dadosFiltro").show();
			$(".grids").show();
		}
		function pesqTotal(){
			$(".dados").show();
			$(".grids").show();
		}
		function pesqCota(){
			$(".dados").show();
		}
		
		function diaSemana(){
			
			var mydate=new Date()
			var year=mydate.getYear()
			if (year<2000)
			year += (year < 1900) ? 1900 : 0
			var day=mydate.getDay()
			var month=mydate.getMonth()
			var daym=mydate.getDate()
			if (daym<10)
			daym="0"+daym
			var dayarray=new Array("Domingo","Segunda-feira","Terça-feira","Quarta-feira","Quinta-feira","Sexta-feira","Sábado")
			var montharray=new Array(" /01/ "," /02/ "," /03/ ","/04/ ","/05/ ","/06/","/07/","/08/","/09/"," /10/ "," /11/ "," /12/ ")
			document.write("   "+dayarray[day]+", "+daym+" "+montharray[month]+year+" ")	
			
			}
			
			
function MM_jumpMenu(targ,selObj,restore){ //v3.0
  eval(targ+".location='"+selObj.options[selObj.selectedIndex].value+"'");
  if (restore) selObj.selectedIndex=0;
}

/*
 * Função responsável por efetuar uma chamada Ajax via JQuery.
 * 
 * A função delega o retorno à função de callback passada por parâmetro e
 * também efetua o tratamento de erros padrão.
 *
 * O método chamado deve sempre retornar um objeto JSON com nome 'result' e a
 * função de Callback deve sempre receber este objeto.
 *
 * @param type - tipo da chamada (GET/POST...)
 * @param url - url do destino
 * @param sucessCallBackFunction - função de callback para retorno
 * @param errorCallBackFunction - função de callback para retorno em caso de erro
 * @param data - dados a serem enviados no formato 'dado1=x&dado2=y'
 */
function ajaxRequest(url, data, sucessCallBackFunction, errorCallBackFunction, dataType, method, isFromDialog,idDialog) {

	$.ajax({
		type: method,
		url: url,
		data: data,
		dataType: dataType, 
		success: function(json) {
			
			var tipoMensagem = null;
			var listaMensagens = null;
			
			if (!json && !sucessCallBackFunction) {
				
				return;
			}
			
			if (json.mensagens) {
				
				tipoMensagem = json.mensagens.tipoMensagem;
				listaMensagens = json.mensagens.listaMensagens;
				
			} else {
				
				tipoMensagem = json.tipoMensagem;
				listaMensagens = json.listaMensagens;
			}
			
			if (tipoMensagem && listaMensagens) {
				
				if (tipoMensagem == "SUCCESS") {
					
					if (isFromDialog) {
						
						if(idDialog){
							
							exibirMensagemDialog(tipoMensagem, listaMensagens,idDialog);
							
						} else {
							
							exibirMensagemDialog(tipoMensagem, listaMensagens,"");
						}
						
					} else {
						
						exibirMensagem(tipoMensagem, listaMensagens);
					}
					
					if (sucessCallBackFunction) {
						
						sucessCallBackFunction(json.result);
					}
				} else {
					
					if (isFromDialog) {
						
						if(idDialog){
							
							exibirMensagemDialog(tipoMensagem, listaMensagens,idDialog);
							
						} else {
							
							exibirMensagemDialog(tipoMensagem, listaMensagens,"");
						}
						
					} else {
						
						exibirMensagem(tipoMensagem, listaMensagens);
					}
					
					if (errorCallBackFunction) {
						errorCallBackFunction(json);
					}
				}
			} else {
				if (sucessCallBackFunction) {
					
					if (json.result) {

						sucessCallBackFunction(json.result);
						
					} else {

						sucessCallBackFunction(json);
					}
				}
			}
		},
		error: function(error, type, msg) {
			alert("Erro: " + msg);
		}
	});		
}

$(document).ready(function() {
	
	jQuery.extend({
		
	    put_: function(url, data, callback, errorCallback, type, isFromDialog) {
	    	
	        return ajaxRequest(url, data, callback, errorCallback, type, 'PUT', isFromDialog,idDialog);
	    },
	    delete_: function(url, data, callback, errorCallback, type, isFromDialog,idDialog) {
	    	
	    	return ajaxRequest(url, data, callback, errorCallback, type, 'DELETE', isFromDialog,idDialog);
	    },
	    post_: function(url, data, callback, errorCallback, type, isFromDialog,idDialog) {
	    	
	    	return ajaxRequest(url, data, callback, errorCallback, type, 'POST', isFromDialog,idDialog);
	    },
	    get_: function(url, data, callback, errorCallback, type, isFromDialog,idDialog) {
	    	
	    	return ajaxRequest(url, data, callback, errorCallback, type, 'GET', isFromDialog,idDialog);
	    }
	});
	
	jQuery.extend({
		
	    putJSON: function(url, data, callback, errorCallback, isFromDialog,idDialog) {
	    	
	        return jQuery.put_(url, data, callback, errorCallback, 'json', isFromDialog,idDialog);
	    },
	    deleteJSON: function(url, data, callback, errorCallback, isFromDialog,idDialog) {
	    	
	        return jQuery.delete_(url, data, callback, errorCallback, 'json', isFromDialog,idDialog);
	    },
	    postJSON: function(url, data, callback, errorCallback, isFromDialog,idDialog) {
	    	
	        return jQuery.post_(url, data, callback, errorCallback, 'json', isFromDialog,idDialog);
	    },
	    getJSON: function(url, data, callback, errorCallback, isFromDialog,idDialog) {
	    	
	        return jQuery.get_(url, data, callback, errorCallback, 'json', isFromDialog,idDialog);
	    }
	});
});

$.fn.clear = function() {
	return this.each(function() {
		$(this).html("");
	});
};

function sub_home(){
	$('#sub_cadastro').hide();
	$('#sub_lancamento').hide();
	$('#sub_estoque').hide();
	$('#sub_expedicao').hide();
	$('#sub_devolucao').hide();
	$('#sub_nfe').hide();
	$('#sub_financeiro').hide();
	$('#sub_administracao').hide();
}
function sub_cadastro(){
	$('#sub_cadastro').show();
	$('#sub_lancamento').hide();
	$('#sub_estoque').hide();
	$('#sub_expedicao').hide();
	$('#sub_devolucao').hide();
	$('#sub_nfe').hide();
	$('#sub_financeiro').hide();
	$('#sub_administracao').hide();
}

function sub_lancamento(){
	$('#sub_cadastro').hide();
	$('#sub_lancamento').show();
	$('#sub_estoque').hide();
	$('#sub_expedicao').hide();
	$('#sub_devolucao').hide();
	$('#sub_nfe').hide();
	$('#sub_financeiro').hide();
	$('#sub_administracao').hide();
}

function sub_estoque(){
	$('#sub_cadastro').hide();
	$('#sub_lancamento').hide();
	$('#sub_estoque').show();
	$('#sub_expedicao').hide();
	$('#sub_devolucao').hide();
	$('#sub_nfe').hide();
	$('#sub_financeiro').hide();
	$('#sub_administracao').hide();
}

function sub_expedicao(){
	$('#sub_cadastro').hide();
	$('#sub_lancamento').hide();
	$('#sub_estoque').hide();
	$('#sub_expedicao').show();
	$('#sub_devolucao').hide();
	$('#sub_nfe').hide();
	$('#sub_financeiro').hide();
	$('#sub_administracao').hide();
}

function sub_devolucao(){
	$('#sub_cadastro').hide();
	$('#sub_lancamento').hide();
	$('#sub_estoque').hide();
	$('#sub_expedicao').hide();
	$('#sub_devolucao').show();
	$('#sub_nfe').hide();
	$('#sub_financeiro').hide();
	$('#sub_administracao').hide();
}

function sub_nfe(){
	$('#sub_cadastro').hide();
	$('#sub_lancamento').hide();
	$('#sub_estoque').hide();
	$('#sub_expedicao').hide();
	$('#sub_devolucao').hide();
	$('#sub_nfe').show();
	$('#sub_financeiro').hide();
	$('#sub_administracao').hide();

}
function sub_financeiro(){
	$('#sub_cadastro').hide();
	$('#sub_lancamento').hide();
	$('#sub_estoque').hide();
	$('#sub_expedicao').hide();
	$('#sub_devolucao').hide();
	$('#sub_nfe').hide();
	$('#sub_financeiro').show();
	$('#sub_administracao').hide();

}
function sub_administracao(){
	$('#sub_cadastro').hide();
	$('#sub_lancamento').hide();
	$('#sub_estoque').hide();
	$('#sub_expedicao').hide();
	$('#sub_devolucao').hide();
	$('#sub_nfe').hide();
	$('#sub_financeiro').hide();
	$('#sub_administracao').show();
}

function sub_help(){
	$('#sub_cadastro').hide();
	$('#sub_lancamento').hide();
	$('#sub_estoque').hide();
	$('#sub_expedicao').hide();
	$('#sub_devolucao').hide();
	$('#sub_nfe').hide();
	$('#sub_financeiro').hide();
	$('#sub_administracao').hide();
}

function mostra_status(opcao) {
	if(opcao == 'RECEBIDA'){
		opcao = '1';
	}else{
		opcao = '2';
	}
	
	var grid_1 = document.getElementById("notaRecebida"); 
	var grid_2 = document.getElementById("pendenteRecEmissao");  
	   
	switch (opcao) {   
		case '1':   
			grid_1.style.display = "";   
			grid_2.style.display = "none";     
		break;   
		case '2':   
			grid_1.style.display = "none";   
			grid_2.style.display = "";      
		break; 
		default:   
			grid_1.style.display = "none";   
			grid_2.style.display = "none";   
		break;   
	}   

}

function focarPrimeiroElemento() {
	$('input:visible:enabled:first').focus();
}

function escondeHeader(){
	$('.sub-header').fadeOut('fast');
	$('#sobeHeader').fadeOut('fast');
	$('.headerEsconde').fadeIn('fast');
	$('#desceHeader').fadeIn('fast');
	// Elemento existe
	if ($('.gridOff').length != 0) {
		$('.gridOff').removeClass('gridOff').addClass('gridOn');
	}
}
function mostraHeader(){
	$('.sub-header').fadeIn('fast');
	$('#sobeHeader').fadeIn('fast');
	$('.headerEsconde').fadeOut('fast');
	$('#desceHeader').fadeOut('fast');
	// Elemento existe
	if ($('.gridOn').length != 0) {
		$('.gridOn').removeClass('gridOn').addClass('gridOff');
	}
}

/**
 * Define botão de pesquisa como comando padrão ao clicar na tecla 'Enter' 
 * durante preenchimento do filtro de pesquisa.
 *
 * 
 * Necessário para o funcionamento:
 * Definir a classe css 'botaoPesquisar' no botão de Pesquisa 
 * Definir a classe css 'campoDePesquisa' nos campos de filtro utilizados.
 * 
 * Ex: tipoMovimento/tipoMovimento.jsp
 */
function definirAcaoPesquisaTeclaEnter() {
	
	$(".campoDePesquisa").bind("keydown", function(event) {
		  var keycode = (event.keyCode ? event.keyCode : (event.which ? event.which : event.charCode));
	      if (keycode == 13) { 	    	  
	    	  $('.botaoPesquisar').click();
	    	  return false;
	      } else  {
	    	  return true;
	      }
		
	});
}

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
})
