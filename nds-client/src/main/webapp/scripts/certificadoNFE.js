var params = new Array();

var certificadoNFEController  = $.extend(true, {

	path : contextPath +"/nfe/certificadoNFE/",
	
	init : function() {
		this.initDatas();
		this.initFlexiGrids();
		
		$('#certificado-upload').fileupload(
			{
				url : this.path + "uploadCertificado",
				sequentialUploads: false,
				dataType : 'json',
				paramName : 'uploadedFile',
				replaceFileInput: false,
				submit : function(e, data) {
					data = $("#pesquisarForm", this.workspace).serialize();
	
				},
				success : function(e, data) {$("#nomeArquivoCertificado").html(e.result);
			}
					 
		});
		
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
	
	
	exibirMensagemSucesso : function (result){
		
		var tipoMensagem = result.tipoMensagem;
		var listaMensagens = result.listaMensagens;
		if (tipoMensagem && listaMensagens) {
			exibirMensagem(tipoMensagem, listaMensagens);
		}
	},
	
	initFlexiGrids : function() {
		$(".certificadosGrid", certificadoNFEController.workspace).flexigrid({
			preProcess : certificadoNFEController.preProcessGridPesquisa,
			// url : contextPath + "/nfe/certificadoNFE/obterCertificado",
			dataType : 'json',
			colModel : [ {
				display : 'Nome Arquivo',
				name : 'nomeArquivo',
				width : 80,
				sortable : true,
				align : 'left',
			},{
				display : 'Data Inicio',
				name : 'dataInicio',
				width : 340,
				sortable : true,
				align : 'left',
			},{
				display : 'Data Fim',
				name : 'dataFim',
				width : 340,
				sortable : true,
				align : 'left',	
			},{
				display : '',
				name : 'sel',
				width : 20,
				sortable : false,
				align : 'center',
			}],
			showToggleBtn : false,
			sortname : "nomeArquivo",
			sortorder : "asc",
			usepager : false,
			useRp : true,
			rp : 15,
			width : 500,
			height : 130
		});
	},
	
	/**
	 * Metodo de pre-processamento dos dados inseridos na grid Pesquisa
	 * 
	 * @param data - dados inseridos na grid
	 * @returns dados normalizados para a grid
	 */
	preProcessGridPesquisa : function(data) {
		
		if (typeof data.mensagens == "object") {
		
			exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
			$("#certificadosGrid", certificadoNFEController.workspace).empty();
		
		} else {
			
			for(var index in data.rows) {
						
			}
			return data;
		}
	},
	
}, BaseController);
//@ sourceURL=certificadoNFE.js