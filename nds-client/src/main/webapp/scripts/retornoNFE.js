var tipoEmissor = "";

var retornoNFEController  = $.extend(true, {

	path : contextPath +"/nfe/retornoNFe/",
	
	init : function() {
		this.initFlexiGrids();
		this.initFiltroDatas();
		this.obterTipoEmissor();
	},

	initFlexiGrids : function() {
		
		$("#retornoNfe-flexigrid-pesquisa", retornoNFEController.workspace).flexigrid({
			colModel : [{
				display : 'Num. Total de Arquivos',
				name : 'numeroTotalArquivos',
				width : 150,
				sortable : false,
				align : 'center',
			}, {
				display : 'Num. NF-e',
				name : 'numeroNotasAprovadas',
				width : 150,
				sortable : false,
				align : 'center',
			}, {
				display : 'Erros Consis.',
				name : 'numeroNotasRejeitadas',
				width : 150,
				sortable : false,
				align : 'center',
			}],
			dataType : 'json',
			sortorder : "asc",
			usepager : false,
			useRp : false,
			rp : 15,
			showTableToggleBtn : false,
			width : 550,
			height : 100
			
		});
		
	},
	
	obterTipoEmissor : function() {
		
		$.postJSON(contextPath + '/nfe/retornoNFe/obterTipoEmissor', null, 
			function(result) {
				tipoEmissor = result.string;
	        }
		);
		
	},
	
	initFiltroDatas : function() {
		$.postJSON(contextPath + '/cadastro/distribuidor/obterDataDistribuidor', null, 
				function(result) {
					$("#retornoNFEDataReferencia", this.workspace).val(result);
		        }
		);
		
		$( "#retornoNFEDataReferencia", retornoNFEController.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$('#retornoNFEDataReferencia', retornoNFEController.workspace).mask("99/99/9999");
	},
	
	pesquisar : function() {
		
		var dataReferencia = $("#retornoNFEDataReferencia", this.workspace).val();
		var tipoRetorno = $('input[name^="tipoRetorno"]:checked').val();
		var tipoEmissorAux = tipoEmissor;
		var params = [];
		
		if(!dataReferencia) {
			exibirMensagem("WARNING", ["O campo [Date de Referência] é obrigatório"], "");
			return false;
		} else {

			params.push({name : "dataReferencia", value : dataReferencia});
			params.push({name : "tipoRetorno", value : tipoRetorno});
			params.push({name : "tipoEmissor", value : tipoEmissorAux});
			
			$("#retornoNfe-flexigrid-pesquisa", retornoNFEController.workspace).flexOptions({
				dataType : 'json',
				url: contextPath + "/nfe/retornoNFe/pesquisarArquivos.json",
				params: params
			});
			
			$("#retornoNfe-flexigrid-pesquisa", retornoNFEController.workspace).flexReload();
			$(".grids").show();
				
		}
	},
	
	confirmar : function() {
		
		var _this = this;
		
		$.postJSON(this.path + 'confirmar.json', null, function(data) {

			retornoNFEController.exibirMensagemSucesso(data);
			
			_this.limparTabela();
			
		});
	},
	
	bindEvents : function() {
		
		var _this = this;
		
		$("#retornoNFEPesquisar", this.workspace).click(function() {
			_this.pesquisarArquivos();
		});
		
		$("#retornoNFEConfirmar", this.workspace).click(function() {
			_this.confirmar();
		});
	},

	limparTabela : function() {
		
		$("#numeroTotalArquivos", this.workspace).html(0);
		$("#numeroNotasAprovadas", this.workspace).html(0);
		$("#numeroNotasRejeitadas", this.workspace).html(0);
		
	},

	exibirMensagemSucesso: function (result){
		
		var tipoMensagem = result.tipoMensagem;
		var listaMensagens = result.listaMensagens;
		if (tipoMensagem && listaMensagens) {
			exibirMensagem(tipoMensagem, listaMensagens);
		}
	},
	
	imprimirBoletoNFE : function() {
		var parametros = []; 
		
		var itens = GerarBoletoAvulsoController.obterCotasSelecionadas();
		
		$.postJSON(contextPath + '/financeiro/boletoAvulso/imprimirBoleto',
			itens,
			 function(resultado) {
				$.fileDownload(contextPath +'/nfe/retornoNFe/imprimirBoleto', {
					httpMethod : "POST",
					data : itens,
					
					successCallback: function (url) {
				    	console.log('success');
				    	alert("caiu aqui");
				    },
				    failCallback: function (responseHtml, url) {
				        preparingFileModal.dialog('close');
				        $("#error-modal").dialog({ modal: true });
				    }
				});
				
			 },
			null,
			true
		);
		
	},
	
	addLoteMix:function(){

		GerarBoletoAvulsoController.initGrids();
		
		$("#modalUploadArquivoMix").dialog({
			resizable: false,
			height:'auto',
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					GerarBoletoAvulsoController.executarSubmitArquivo();
					
				},
				"Cancelar": function() {
					$("#excelFile").val("");
					$(this).dialog("close");
				}
			},
		});
		
	},
	
}, BaseController);
//@ sourceURL=retornoNFE.js