var params = new Array();

var certificadoNFEController  = $.extend(true, {

	path : contextPath +"/nfe/certificadoNFE/",
	
	init : function() {
		this.initDatas();
		this.initFlexiGrids();
		
	},

	initDatas : function() {
		$(".input-date").datepicker({
			showOn : "button",
			buttonImage : contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly : true
		});
		
		$(".input-date").mask("99/99/9999");
		
		$("#certificado-data-inicio").val(formatDateToString(new Date()));
		
		$("#certificado-data-fim").val(formatDateToString(new Date()));
	},
	
	initButtons : function() {
		
		var _this = this;

		$("#certificadoNFEConfirmar", this.workspace).click(function() {
			_this.btnConfirmar();
		});
	},	
	
	initFlexiGrids : function() {
		
		
	},
	
	upload : function() {
		
	},
	
	btnConfirmar : function() {
		
		this.isEmptyOrNull();
		
		var parametros = this.param();
		
		$.postJSON(this.path + 'confirmar', parametros, function(data) {
			
			var tipoMensagem = data.tipoMensagem;
			var listaMensagens = data.listaMensagens;

			if (tipoMensagem && listaMensagens) {
				exibirMensagemDialog(tipoMensagem, listaMensagens, "");
			}
			exibirMensagem("SUCCESS", ["Operação realizada com sucesso!"]);
			
		});
		
		
	},
	
	limparTabela : function() {
		
	},

	isEmptyOrNull : function () {
		
		var mensagens = []; 
		
		if($("#certificado-senha").val() == '') {
			mensagens.push("A ['Senha'] não pode ser nula");
		} 
		
		if($("#certificado-alias").val() == '') {
			mensagens.push("O ['Alias do Certificado'] não pode ser nulo");
		}
		
		if($("#certificado-dataIncio").val() == '') {
			mensagens.push("A ['Data Inicio'] não pode ser nula");
		}
		
		if($("#certificado-dataFim").val() == '') {
			mensagens.push("A ['Data Fim'] não pode ser nula");
		}
		
		if(mensagens.length > 0) {
			exibirMensagem('WARNING', mensagens);
			return false;
		}
	},
	
	param : function () {
    	
    	params.push({name:"filtro.senha" , value: $("#certificado-senha").val()});
    	params.push({name:"filtro.alias" , value: $("#certificado-alias").val()});
    	params.push({name:"filtro.dataInicio" , value: $("#certificado-data-inicio").val()});
    	params.push({name:"filtro.dataFim" , value: $("#certificado-data-fim").val()});
    	
    	return params;
	},
	
	
	exibirMensagemSucesso: function (result){
		
		var tipoMensagem = result.tipoMensagem;
		var listaMensagens = result.listaMensagens;
		if (tipoMensagem && listaMensagens) {
			exibirMensagem(tipoMensagem, listaMensagens);
		}
	},
	
}, BaseController);
//@ sourceURL=certificadoNFE.js