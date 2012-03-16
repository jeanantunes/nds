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
    for (i=0; i<array.length; i++) {
	if (array[i] != item) {
	    newArray.push(array[i]);	
	}
    }			
    return newArray;			
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
function ajaxRequest(url, data, sucessCallBackFunction, errorCallBackFunction, dataType, method, isFromDialog) {

	$.ajax({
		type: method,
		url: url,
		data: data,
		dataType: dataType, 
		success: function(json) {
			
			var tipoMensagem = null;
			var listaMensagens = null;
			
			if(!json) {
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
						
						exibirMensagemDialog(tipoMensagem, listaMensagens);
						
					} else {
						
						exibirMensagem(tipoMensagem, listaMensagens);
					}
					
					if (sucessCallBackFunction) {
						sucessCallBackFunction(json.result);
					}
				} else {
					
					if (isFromDialog) {
						
						exibirMensagemDialog(tipoMensagem, listaMensagens);
						
					} else {
						
						exibirMensagem(tipoMensagem, listaMensagens);
					}
					
					if (errorCallBackFunction) {
						errorCallBackFunction(json);
					}
				}
			} else {
				if (sucessCallBackFunction) {
					sucessCallBackFunction(json.result);
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
	    	
	        return ajaxRequest(url, data, callback, errorCallback, type, 'PUT', isFromDialog);
	    },
	    delete_: function(url, data, callback, errorCallback, type, isFromDialog) {
	    	
	    	return ajaxRequest(url, data, callback, errorCallback, type, 'DELETE', isFromDialog);
	    },
	    post_: function(url, data, callback, errorCallback, type, isFromDialog) {
	    	
	    	return ajaxRequest(url, data, callback, errorCallback, type, 'POST', isFromDialog);
	    },
	    get_: function(url, data, callback, errorCallback, type, isFromDialog) {
	    	
	    	return ajaxRequest(url, data, callback, errorCallback, type, 'GET', isFromDialog);
	    }
	});
	
	jQuery.extend({
		
	    putJSON: function(url, data, callback, errorCallback, isFromDialog) {
	    	
	        return jQuery.put_(url, data, callback, errorCallback, 'json', isFromDialog);
	    },
	    deleteJSON: function(url, data, callback, errorCallback, isFromDialog) {
	    	
	        return jQuery.delete_(url, data, callback, errorCallback, 'json', isFromDialog);
	    },
	    postJSON: function(url, data, callback, errorCallback, isFromDialog) {
	    	
	        return jQuery.post_(url, data, callback, errorCallback, 'json', isFromDialog);
	    },
	    getJSON: function(url, data, callback, errorCallback, isFromDialog) {
	    	
	        return jQuery.get_(url, data, callback, errorCallback, 'json', isFromDialog);
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