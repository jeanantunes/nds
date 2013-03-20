var recebimentoFisicoController = $.extend(true, {
	
	path: contextPath + '/estoque/recebimentoFisico/',
	
	novoValorTotalTyped: false, 
	
	init: function() {
		var _this = this;

		$("#datepickerLancto", recebimentoFisicoController.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		$("#datepickerRecolhimento", recebimentoFisicoController.workspace).datepicker({
			showOn: "button",
			buttonImage:  contextPath + "/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		$("#dataEmissao", recebimentoFisicoController.workspace).datepicker({
			showOn: "button",
			buttonImage:  contextPath + "/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		
		$("#novoDataEmissao", recebimentoFisicoController.workspace).datepicker({
			showOn: "button",
			buttonImage:  contextPath + "/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		
		$("#novoDataEntrada", recebimentoFisicoController.workspace).datepicker({
			showOn: "button",
			buttonImage:  contextPath + "/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		
		$("#datepickerLancto", recebimentoFisicoController.workspace).mask("99/99/9999");
		$("#datepickerRecolhimento", recebimentoFisicoController.workspace).mask("99/99/9999");
		$("#dataEmissao", recebimentoFisicoController.workspace).mask("99/99/9999");
		$("#novoDataEmissao", recebimentoFisicoController.workspace).mask("99/99/9999");
		$("#novoDataEntrada", recebimentoFisicoController.workspace).mask("99/99/9999");
		
		$("#notaFiscal", recebimentoFisicoController.workspace).numeric();
		
		$("#numeroNotaEnvio", recebimentoFisicoController.workspace).numeric();
		
		$("#novoNumeroNotaEnvio", recebimentoFisicoController.workspace).numeric();
		
		$("#chaveAcesso", recebimentoFisicoController.workspace).numeric();
		
		$("#valorBruto", recebimentoFisicoController.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});

		$("#valorLiquido", recebimentoFisicoController.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});
		
		$("#valorDesconto", recebimentoFisicoController.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});
		
		$("#novoValorTotal", recebimentoFisicoController.workspace).keyup(function(){
			_this.novoValorTotalTyped = true;
		});
		
		$("#novoValorTotal", recebimentoFisicoController.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});
			
		$("#novoNumeroNota", recebimentoFisicoController.workspace).numeric();
		
		$("#codigo", recebimentoFisicoController.workspace).numeric();
		$("#edicao", recebimentoFisicoController.workspace).numeric();
		$("#precoCapa", recebimentoFisicoController.workspace).numeric();
		$("#peso", recebimentoFisicoController.workspace).numeric();
		$("#pacotePadrao", recebimentoFisicoController.workspace).numeric();
		$("#repartePrevisto", recebimentoFisicoController.workspace).numeric();

		$("#produto", recebimentoFisicoController.workspace).autocomplete({source: ""});
		
		$("#cnpj", recebimentoFisicoController.workspace).mask("99.999.999/9999-99");
		
		$("#fornecedor", recebimentoFisicoController.workspace).focus();
		
		recebimentoFisicoController.ocultarBtns();
		
		this.formatItemNota();

		this.inicializarGridPopUpNota();
		
	},
	
	ocultarBtns : function() {

		$('#botoesNormais', recebimentoFisicoController.workspace).hide();
		$('#botaoNovoProdutoOpaco', recebimentoFisicoController.workspace).hide();
		$('#botaoNovoProduto', recebimentoFisicoController.workspace).hide();
		$('#botoesOpacos', recebimentoFisicoController.workspace).hide();
		
	},
	
	
	indNotaFiscalInterface : false,

	indRecebimentoFisicoConfirmado : false,

	indTodosFornecedor : false,

	/**
	 * LINE ID DO ITEM NOTA QUE ESTA SENDO EDITADO 
	 */
	lineIdItemNotaEmEdicao : null,

	/**
	 * SELECIONA UM FORNECEDOR A PARTIR DO CNPJ DIGITADO.
	 */
	pesquisarPorCnpjFornecedor : function() {
			
		var cnpj = $("#cnpj", recebimentoFisicoController.workspace).val();	
		
		var cnpjUnmask = removeSpecialCharacteres(cnpj, "_").toString();
		
		if(!cnpjUnmask || cnpjUnmask.length <= 14)
			return;
		
		if(cnpj == "") {
			$("#fornecedor", recebimentoFisicoController.workspace).val("");
			return;
		}
		
		$.postJSON(this.path + 'buscaCnpj', {cnpj:removeSpecialCharacteres(cnpj)}, 
		function(result) { 
			$("#fornecedor", recebimentoFisicoController.workspace).val(result.cnpj);
		});	
	
	},
	
	/**
	 * EXIBI O CNPJ DO FORNECEDOR SELECIONADO.
	 */
	exibirCnpjDoFornecedor : function() {
		
		var cnpjDoFornecedor = $("#fornecedor", recebimentoFisicoController.workspace).val();	
		
		if (cnpjDoFornecedor == -2) {
		
			$("#cnpj", recebimentoFisicoController.workspace).val("");
			
			$("#cnpj", recebimentoFisicoController.workspace).attr("disabled", false);
			
			$("#fornecedor", recebimentoFisicoController.workspace).focus();
			
		} else {
			
			$("#cnpj", recebimentoFisicoController.workspace).val(cnpjDoFornecedor);
			
			$("#cnpj", recebimentoFisicoController.workspace).mask("99.999.999/9999-99");
			
			$("#cnpj", recebimentoFisicoController.workspace).attr("disabled", true);
		}
	},
	
	
	/**
	 * VERIFICA A EXISTENCIA DE UMA NOTAFISCAL
	 * COM OS PARÂMETROS DE PESQUISA
	 */
	verificarNotaFiscalExistente : function() {

		var cnpj 		= $("#cnpj", recebimentoFisicoController.workspace).val();
		var notaFiscal 	= $("#notaFiscal", recebimentoFisicoController.workspace).val();
		var serie 		= $("#serie", recebimentoFisicoController.workspace).val();		
		var chaveAcesso = $("#chaveAcesso", recebimentoFisicoController.workspace).val();
		var fornecedor  = $("#fornecedor", recebimentoFisicoController.workspace).val();
		var numeroNotaEnvio  = $("#numeroNotaEnvio", recebimentoFisicoController.workspace).val();

		
		
        var indNFe      = "N";
        
        if( $('#eNF', recebimentoFisicoController.workspace).attr('checked') == 'checked' ) {
			indNFe = "S";
		}
        
		var dadosPesquisa = [
			{name: "cnpj" 				, value: cnpj				},
			{name: "numeroNotaFiscal" 	, value: notaFiscal 		},
			{name: "serie" 				, value: serie				},
			{name: "indNFe" 			, value: indNFe				},
			{name: "fornecedor" 		, value: fornecedor			},
			{name: "chaveAcesso" 		, value: chaveAcesso 		},
			{name: "numeroNotaEnvio" 	, value: numeroNotaEnvio 	}
			
		];
		
		this.limparCampos();
		
		$.postJSON(	this.path +  'verificarNotaFiscalExistente', 
					dadosPesquisa,
					this.confirmaNotaFiscalEncontrada);
	
	},

	/**
	 * SE UMA NOTA FOR ENCONTRADA, SERAO PESQUISADOS OS ITEM RELATIVOS A MESMA
	 * E POPULADA A GRID, CASO CONTRARIO, SERA EXIBIDA POPUP PARA SE CADASTRAR UMA
	 * NOVA NOTA.
	 */
	confirmaNotaFiscalEncontrada : function(result) {
		
		var validacao = result.validacao;
		
		recebimentoFisicoController.indNotaFiscalInterface = result.indNotaInterface;
		
		recebimentoFisicoController.indRecebimentoFisicoConfirmado = result.indRecebimentoFisicoConfirmado;

		if(validacao.tipoMensagem == "SUCCESS") {
		
			if (recebimentoFisicoController.indNotaFiscalInterface){
				$('#chBoxReplicaValorRepartePrevistoAll', recebimentoFisicoController.workspace).attr('disabled', false);
				recebimentoFisicoController.carregarItemNotaGridNotaInterface();
				
	    	}else{
				$('#chBoxReplicaValorRepartePrevistoAll', recebimentoFisicoController.workspace).attr('disabled', true);
				recebimentoFisicoController.carregarItemNotaGridNotaManual();
	    	}
			
			if(result.validacao.listaMensagens.length>0)
				exibirMensagem(validacao.tipoMensagem, validacao.listaMensagens);
			
			recebimentoFisicoController.pesquisarItemNotaGrid();

		} else {

			exibirMensagem(validacao.tipoMensagem, validacao.listaMensagens);
			
			$(".grids", recebimentoFisicoController.workspace).hide();
			
			recebimentoFisicoController.ocultarBtns();
			
			$('#selTodos',recebimentoFisicoController.workspace).uncheck();
			
			recebimentoFisicoController.popup_adicionar();
			
			$("#novoNumeroNota", recebimentoFisicoController.workspace).val($("#notaFiscal", recebimentoFisicoController.workspace).val());
			$("#novoSerieNota", recebimentoFisicoController.workspace).val($("#serie", recebimentoFisicoController.workspace).val());		
			$("#novoChaveAcesso", recebimentoFisicoController.workspace).val($("#chaveAcesso", recebimentoFisicoController.workspace).val());		
		}
		
	},
		
	/**
	 * APRESENTA O POPUP PARA CADASTRAR NOVO ITEM NOTA/RECEBIMENTO.
	 */
	popup_novo_item : function() {
		
		recebimentoFisicoController.lineIdItemNotaEmEdicao = null;
		
		var fornecedor = $("#fornecedor", recebimentoFisicoController.workspace).val();
		
		if(fornecedor == "-1"){
			exibirMensagem('WARNING', ['Não é possível Adicionar Novo Produto para opção TODOS em Fornecedor']);
			return;
		}
		
		this.novoValorTotalTyped=false;
		recebimentoFisicoController.limparCamposNovoItem();
		
		$("#dialog-novo-item", recebimentoFisicoController.workspace).dialog({
			resizable: false,
			height:480,
			width:500,
			modal: true,
			buttons: {
				"Confirmar": recebimentoFisicoController.cadastrarNovoItemNota,
				
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			
			beforeClose: function() {
				clearMessageDialogTimeout();
			},
			
			form: $("#dialog-novo-item", recebimentoFisicoController.workspace).parents("form")
			
		});
	},
	
	/**
	*INCLUIR NOVO ITEM DA NOTA
	*/
	incluirNovoItemNota : function(){
		
		var lineId = -1;
		
		if(	typeof recebimentoFisicoController.lineIdItemNotaEmEdicao!='undefined' && 
			
				recebimentoFisicoController.lineIdItemNotaEmEdicao!=null ) {
			
			lineId = recebimentoFisicoController.lineIdItemNotaEmEdicao;
			
		} 
		
		var peso				= $("#peso", recebimentoFisicoController.workspace).val();
		var pacotePadrao		= $("#pacotePadrao", recebimentoFisicoController.workspace).val();
		var codigo 				= $("#codigo", recebimentoFisicoController.workspace).val();
		var produto 			= $("#produto", recebimentoFisicoController.workspace).val();
		var precoCapa			= $("#precoCapa", recebimentoFisicoController.workspace).val();
		var edicao 				= $("#edicao", recebimentoFisicoController.workspace).val();
		var dataLancamento 		= $("#datepickerLancto", recebimentoFisicoController.workspace).val();
		var dataRecolhimento 	= $("#datepickerRecolhimento", recebimentoFisicoController.workspace).val();
		var repartePrevisto 	= $("#repartePrevisto", recebimentoFisicoController.workspace).val();
		var tipoLancamento 		= $("#tipoLancamento", recebimentoFisicoController.workspace).val();
		
		var dadosCadastro = {
				"itemRecebimento.lineId":lineId,
				"itemRecebimento.peso":peso,
				"itemRecebimento.pacotePadrao":pacotePadrao,
				"itemRecebimento.codigoProduto":codigo,
				"itemRecebimento.nomeProduto":produto,
				"itemRecebimento.precoCapa":precoCapa,
				"numeroEdicao":edicao,
				"dataLancamento":dataLancamento,
				"dataRecolhimento":dataRecolhimento,
				"itemRecebimento.repartePrevisto":repartePrevisto,
				"itemRecebimento.tipoLancamento":tipoLancamento};
		
	
		
		if (recebimentoFisicoController.indNotaFiscalInterface) {			
			dadosCadastro = serializeArrayToPost('itensRecebimento', recebimentoFisicoController.obterListaValores(), dadosCadastro);
			
		}
		
		$.postJSON(recebimentoFisicoController.path + 'incluirItemNotaFiscal', dadosCadastro, 

		function(result) {
			
			recebimentoFisicoController.refreshItemNotaGrid();
		
			recebimentoFisicoController.limparCamposNovoItem();
				
		}, null, true);
		
		
	},
	
	/**
	 * CADASTRA NOVO ITEM DE NOTA.
	 */
	cadastrarNovoItemNota : function() {
		
		var lineId = -1;
		
		if(	typeof recebimentoFisicoController.lineIdItemNotaEmEdicao!='undefined' && 
				recebimentoFisicoController.lineIdItemNotaEmEdicao!=null ) {
			
			lineId = recebimentoFisicoController.lineIdItemNotaEmEdicao;
			
		} 
		
		var peso				= $("#peso", recebimentoFisicoController.workspace).val();
		var pacotePadrao		= $("#pacotePadrao", recebimentoFisicoController.workspace).val();
		var codigo 				= $("#codigo", recebimentoFisicoController.workspace).val();
		var produto 			= $("#produto", recebimentoFisicoController.workspace).val();
		var precoCapa			= $("#precoCapa", recebimentoFisicoController.workspace).val();
		var edicao 				= $("#edicao", recebimentoFisicoController.workspace).val();
		var dataLancamento 		= $("#datepickerLancto", recebimentoFisicoController.workspace).val();
		var dataRecolhimento 	= $("#datepickerRecolhimento", recebimentoFisicoController.workspace).val();
		var repartePrevisto 	= $("#repartePrevisto", recebimentoFisicoController.workspace).val();
		var tipoLancamento 		= $("#tipoLancamento", recebimentoFisicoController.workspace).val();
		
		var dadosCadastro = {"itemRecebimento.lineId":lineId,
				"itemRecebimento.peso":peso,
				"itemRecebimento.pacotePadrao":pacotePadrao,
				"itemRecebimento.codigoProduto":codigo,
				"itemRecebimento.nomeProduto":produto,
				"itemRecebimento.precoCapa":precoCapa,
				"numeroEdicao":edicao,
				"dataLancamento":dataLancamento,
				"dataRecolhimento":dataRecolhimento,
				"itemRecebimento.repartePrevisto":repartePrevisto,
				"itemRecebimento.tipoLancamento":tipoLancamento};
		
	
		
		if (recebimentoFisicoController.indNotaFiscalInterface) {
			dadosCadastro = serializeArrayToPost('itensRecebimento', recebimentoFisicoController.obterListaValores(), dadosCadastro);
		}
		
		$.postJSON(recebimentoFisicoController.path + 'incluirItemNotaFiscal', dadosCadastro, 

		function(result) {
			
		if(result.tipoMensagem == "SUCCESS") {
				
			$("#dialog-novo-item", recebimentoFisicoController.workspace).dialog( "close" );
				
		} 
		
			recebimentoFisicoController.refreshItemNotaGrid();
				
		}, null, true);
		
		
		
	},
	
	/**
	* LIMPA OS CAMPOS DE PESQUISA DE NOTA FISCAL
	*/
	limparCamposPesquisa : function() {
		
		$("#cnpj", recebimentoFisicoController.workspace).val("");
		$("#notaFiscal", recebimentoFisicoController.workspace).val("");
		$("#serie", recebimentoFisicoController.workspace).val("");
		$("#chaveAcesso", recebimentoFisicoController.workspace).val("");
		$("#fornecedor", recebimentoFisicoController.workspace).val("-1");
		
	},
	
	/**
	* LIMPA OS CAMPOS DO CADASTRO DE NOVA NOTA E NOVO ITEM.
	*/
	limparCampos : function() {
		
		$("#dataEmissao", recebimentoFisicoController.workspace).val("");
		$("#valorBruto", recebimentoFisicoController.workspace).val("");
		$("#valorLiquido", recebimentoFisicoController.workspace).val("");
		$("#valorDesconto", recebimentoFisicoController.workspace).val("");
		$("#cfop", recebimentoFisicoController.workspace).val("");
		$("#tipoNotaFiscal", recebimentoFisicoController.workspace).val("");
		
		$("#codigo", recebimentoFisicoController.workspace).val("");
		$("#produto", recebimentoFisicoController.workspace).val("");
		$("#precoCapa", recebimentoFisicoController.workspace).val("");
		$("#edicao", recebimentoFisicoController.workspace).val("");
		$("#datepickerLancto", recebimentoFisicoController.workspace).val("");
		$("#datepickerRecolhimento", recebimentoFisicoController.workspace).val("");
		$("#repartePrevisto", recebimentoFisicoController.workspace).val("");
		$("#tipoLancamento", recebimentoFisicoController.workspace).val("");		
		$("#novoValorTotal", recebimentoFisicoController.workspace).val("");
		$('#labelValorTotal', recebimentoFisicoController.workspace).text('0,00');
	
	},

	/**
	* LIMPA OS CAMPOS DO CADASTRO DE NOVA NOTA E NOVO ITEM.
	*/
	limparCamposNovoItem : function() {
		
		$("#codigo", recebimentoFisicoController.workspace).val("");
		$("#produto", recebimentoFisicoController.workspace).val("");
		$("#precoCapa", recebimentoFisicoController.workspace).val("");
		$("#edicao", recebimentoFisicoController.workspace).val("");
		$("#datepickerLancto", recebimentoFisicoController.workspace).val("");
		$("#datepickerRecolhimento", recebimentoFisicoController.workspace).val("");
		$("#repartePrevisto", recebimentoFisicoController.workspace).val("");
		$("#tipoLancamento", recebimentoFisicoController.workspace).val("");
		$("#peso", recebimentoFisicoController.workspace).val("");
		$("#pacotePadrao", recebimentoFisicoController.workspace).val("");
		$("#novoValorTotal", recebimentoFisicoController.workspace).val("");
		
	
	},
	
	/**
	 * EFEITO PARA ABERTURA POPUP. 
	 */
	callback : function() {
		setTimeout(function() {
			$( "#effect:visible", recebimentoFisicoController.workspace).removeAttr("style" ).fadeOut();
	
		}, 1000 );
	},	
	
	
	/**
	 * APRESENTA O CAMPO DE CHAVE DA NFE.
	 */
	mostrar_nfes : function(){
		
		if( $("#eNF", recebimentoFisicoController.workspace).attr('checked') == 'checked' ){
			
			$(".nfes", recebimentoFisicoController.workspace).show();
			
		}else{
			
			$(".nfes", recebimentoFisicoController.workspace).hide();
			
			$("#chaveAcesso", recebimentoFisicoController.workspace).val("");
			
		}

	},
	
	/**
	 * APRESENTA O CAMPO DA NOVA CHAVE DA NFE.
	 */
	mostrar_chave_acesso_nova : function(){
		
		if( $("#novoNfe", recebimentoFisicoController.workspace).attr('checked') == 'checked' ){
			
			$(".nfesNovo", recebimentoFisicoController.workspace).show();
			
		}else{
			
			$(".nfesNovo", recebimentoFisicoController.workspace).hide();
			
			$("#novoChaveAcesso", recebimentoFisicoController.workspace).val("");
			
		}

	},
	
	
	/**
	 * ESTRUTURA DE COLUNAS DA GRID DE RESULTADO.
	 */
	carregarItemNotaGridNotaInterface : function() {
						
		$(".gridWrapper", recebimentoFisicoController.workspace).empty();
		
		$(".gridWrapper", recebimentoFisicoController.workspace).append($("<table>").attr("class", "itemNotaGrid"));
		
		$(".itemNotaGrid", recebimentoFisicoController.workspace).flexigrid({
			
				preProcess: recebimentoFisicoController.getDataFromResultNotaInterface,
				dataType : 'json',
				colModel : [
			{
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : false,
				align : 'center'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 100,
				sortable : false,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 40,
				sortable : false,
				align : 'center'
			}, {
				display : 'Preço c/ Desconto R$ ',
				name : 'precoCapa',
				width : 110,
				sortable : false,
				align : 'center'
			}, {
				display : 'Reparte previsto',
				name : 'repartePrevisto',
				width : 100,
				sortable : false,
				align : 'center'
			}, {
				display : 'Qtd. Pacotes',
				name : 'qtdPacote',
				width : 60,
				sortable : false,
				align : 'center'
			}, {
				display : 'Qtd. Quebra',
				name : 'qtdExemplar',
				width : 60,
				sortable : false,
				align : 'center'
			}, {
				display : 'Pcte. Padrão',
				name : 'pacotePadrao',
				width : 70,
				sortable : false,
				align : 'center'
			}, {				
				display : 'Diferença',
				name : 'diferenca',
				width : 60,
				sortable : false,
				align : 'center'
				
			}, {
				display : 'Valor Total R$',
				name : 'valorTotal',
				width : 70,
				sortable : false,
				align : 'center'
				
			},{
				display : 'Ação',
				name : 'acao',
				width : 40,
				sortable : true,
				align : 'center'
			},{
				
				display : 'Replicar Qtd.',
				name : 'replicaQtd',
				width : 60,
				sortable : true,
				align : 'center'
					
			}],
		
			showTableToggleBtn : true,
			width : 980,
			height : 180
		});
	},
	
	/**
	 * ESTRUTURA DE COLUNAS DA GRID DE RESULTADO.
	 */
	carregarItemNotaGridNotaManual : function() {
				
		$(".gridWrapper", recebimentoFisicoController.workspace).empty();
		
		$(".gridWrapper", recebimentoFisicoController.workspace).append($("<table>").attr("class", "itemNotaGrid"));
		
		$(".itemNotaGrid", recebimentoFisicoController.workspace).flexigrid({
			
				preProcess: recebimentoFisicoController.getDataFromResultNotaManual,
				dataType : 'json',
				colModel : [
			{
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : false,
				align : 'center'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 200,
				sortable : false,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 60,
				sortable : false,
				align : 'center'
			}, {
				display : 'Preço Capa R$',
				name : 'precoCapa',
				width : 120,
				sortable : false,
				align : 'center'
			}, {
				display : 'Reparte previsto',
				name : 'repartePrevisto',
				width : 100,
				sortable : false,
				align : 'center'
			}, {
				display : 'Valor Total R$',
				name : 'valorTotal',
				width : 70,
				sortable : false,
				align : 'center'
				
			},{
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : true,
				align : 'center'
			}],
			
		
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});
	},
	
    /**
     * FAZ A PESQUISA DOS ITENS REFERENTES A NOTA ENCONTRADA.
     */
	pesquisarItemNotaGrid : function() {
    	
		$(".itemNotaGrid", recebimentoFisicoController.workspace).flexOptions({
			url: contextPath + '/estoque/recebimentoFisico/obterListaItemRecebimentoFisico',
			dataType : 'json'
		});
	
		$(".itemNotaGrid", recebimentoFisicoController.workspace).flexReload();
	
	},

    /**
     * REFRESH DOS ITENS REFERENTES A NOTA ENCONTRADA.
     */
	refreshItemNotaGrid : function(onSuccessFunction) {
	
		$(".itemNotaGrid", recebimentoFisicoController.workspace).flexOptions({
			url: contextPath + '/estoque/recebimentoFisico/refreshListaItemRecebimentoFisico',
			dataType : 'json',
			onSuccess : onSuccessFunction	
		});
			
		$(".itemNotaGrid", recebimentoFisicoController.workspace).flexReload();
	
	},

    /**
     * FAZ O CANCELAMENTO DE UMA NOTA FISCAL E SEU RECEBIMENTO FISICO.
     */
	cancelarNotaRecebimentoFisico : function() {
    	
		$.postJSON(this.path + 'cancelarNotaRecebimentoFisico', null, 

		function(result) {
					
	    	if(result.tipoMensagem == "SUCCESS") {
				
	    		$(".grids", recebimentoFisicoController.workspace).hide();
	    		
	    		recebimentoFisicoController.ocultarBtns();
	    		
	    		recebimentoFisicoController.limparCamposPesquisa();
	        	
	    		recebimentoFisicoController.limparCamposNovoItem();
	        	
	    		recebimentoFisicoController.limparCampos();
	        	
	        	exibirMensagem(result.tipoMensagem, result.listaMensagens);
	        	
	        	
	        	
			} 
	    	
		});
    	
    	
	},
	
    /**
     * SALVA OS DADOS DOS ITENS DA NOTA EDITADOS.
     */
	salvarDadosItensDaNotaFiscal : function() {
		
		var listaDeValores  = null;
		
		if(recebimentoFisicoController.indNotaFiscalInterface){
			listaDeValores = serializeArrayToPost('itensRecebimento', recebimentoFisicoController.obterListaValores());
		}
		
		$.postJSON(this.path + 'salvarDadosItensDaNotaFiscal', listaDeValores, 
		function(result) {
			
			exibirMensagem(result.tipoMensagem, result.listaMensagens);
			
			recebimentoFisicoController.pesquisarItemNotaGrid();
		
		});
		
	},

    /**
     * COFIRMA OS DADOS DOS ITENS DA NOTA EDITADOS.
     */
	confirmarRecebimentoFisico : function() {
		
		var listaDeValores  = null;
		
		if(recebimentoFisicoController.indNotaFiscalInterface){
			listaDeValores = serializeArrayToPost('itensRecebimento', recebimentoFisicoController.obterListaValores());
		}
		
		$.postJSON(this.path + 'confirmarRecebimentoFisico', listaDeValores, 
		function(result) {

			$(".grids", recebimentoFisicoController.workspace).hide();
			recebimentoFisicoController.limparCamposPesquisa();
			recebimentoFisicoController.ocultarBtns();
		});
		
	},
    
    /**
     * OBTEM OS VALORES DA GRID A SEREM ENVIADOS AO SERVIDOR VIA AJAX
     */
	obterListaValores : function() {
		
		var linhasDaGrid = $(".itemNotaGrid tr", recebimentoFisicoController.workspace);
		
		var listaDeValores = new Array();
		
		$.each(linhasDaGrid, function(index, value) {
			
			var qtdPacote 	= $(value).find('input[name="qtdPacote"]').val();
			
			var qtdExemplar = $(value).find('input[name="qtdExemplar"]').val();
			
			var lineId 		= $(value).find('input[name="lineId"]').val();
			
			listaDeValores.push({lineId:lineId,qtdPacote:qtdPacote,qtdExemplar:qtdExemplar});
			
		});
		
		return listaDeValores;
		
	},
	
	replicarValorRepartePrevisto : function(lineId, elementoCheckBox) {
		
		if( $(elementoCheckBox).attr('checked') != 'checked') {
			return;
		}
		
		var parametroPesquisa = [{name :"lineId", value : lineId}];
		
		$.postJSON(this.path + 'replicarValorRepartePrevisto', parametroPesquisa, 

		function(resultado) {

			$("#qtdPacote_"+lineId, recebimentoFisicoController.workspace).val(resultado.qtdPacote);
			
			$("#qtdExemplar_"+lineId, recebimentoFisicoController.workspace).val(resultado.qtdExemplar);
		
		});
		
	},
	
	replicarTodosValoresRepartePrevisto : function(elementoCheckBox) {
	
		var selecionado = false;
		
		if( $(elementoCheckBox).attr('checked') == 'checked' ) {
			selecionado = true;
		}
		
		if(selecionado == false) {
			
			var linhasDaGrid = $(".itemNotaGrid tr", recebimentoFisicoController.workspace);
			
			$.each(linhasDaGrid, function(index, value) {
				$(value).find('input[name="replicaQtde"]').attr('checked', false);
			});
			
			return;
			
		}
		
		$.postJSON(this.path + 'replicarTodosValoresRepartePrevisto', null, 
				
		function(result) {

			recebimentoFisicoController.refreshItemNotaGrid(function(){

				var linhasDaGrid = $(".itemNotaGrid tr", recebimentoFisicoController.workspace);
				
				$.each(linhasDaGrid, function(index, value) {
					
					$(value).find('input[name="replicaQtde"]').attr('checked', true);
					
				});
				
			});
				
		});
		
	},
	
	/**
	 * EXCLUI UM ITEM DA NOTA
	 */
	excluirItemNotaFiscal : function(lineId) {
		
		if(confirm("Deseja realmente excluir o item selecionado?")){
			
			var dadosExclusao = {lineId:lineId};
			
			dadosExclusao  = serializeArrayToPost('itensRecebimento', recebimentoFisicoController.obterListaValores(), dadosExclusao);
			
			$.postJSON(this.path + 'excluirItemNotaFiscal', dadosExclusao, 
			
			function(result) {
				
				exibirMensagem(result.tipoMensagem, result.listaMensagens);
				
				recebimentoFisicoController.refreshItemNotaGrid();
			
			});
		}else{
			return;
		}		
	},
	

	/**
	 * EDITA UM ITEM DA NOTA
	 */
	editarItemNotaFiscal : function(lineId) {
		
		recebimentoFisicoController.lineIdItemNotaEmEdicao = lineId;
		this.novoValorTotalTyped=true;
		
		recebimentoFisicoController.limparCamposNovoItem();
		
		var parametroPesquisa = [{name :"lineId", value : lineId}];
		
		$.postJSON(this.path + 'obterRecebimentoFisicoVO', parametroPesquisa, 

		function(resultado) {

			$("#pacotePadrao", recebimentoFisicoController.workspace).val(resultado.pacotePadrao);
			$("#peso", recebimentoFisicoController.workspace).val(resultado.peso);
			$("#codigo", recebimentoFisicoController.workspace).val(resultado.codigo);
			$("#produto", recebimentoFisicoController.workspace).val(resultado.nomeProduto);
			$("#precoCapa", recebimentoFisicoController.workspace).val(resultado.precoCapa);
			$("#edicao", recebimentoFisicoController.workspace).val(resultado.edicao);
			$("#datepickerLancto", recebimentoFisicoController.workspace).val(resultado.dataLancamento);
			$("#datepickerRecolhimento", recebimentoFisicoController.workspace).val(resultado.dataRecolhimento);
			$("#repartePrevisto", recebimentoFisicoController.workspace).val(resultado.repartePrevisto);
			$("#tipoLancamento", recebimentoFisicoController.workspace).val(resultado.tipoLancamento);


			$("#dialog-novo-item", recebimentoFisicoController.workspace).dialog({
				resizable: false,
				height:480,
				width:500,
				modal: true,
				buttons: {
					"Confirmar": recebimentoFisicoController.cadastrarNovoItemNota,
					
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				
				beforeClose: function() {
					clearMessageDialogTimeout();
				},
				
				form: $("#dialog-novo-item", recebimentoFisicoController.workspace).parents("form")
				
			});
			
		
		});
			
	},
	

	/**
	 * PREPARA OS DADOS DA NOTA MANUAL A SEREM APRESENTADOS NA GRID.
	 */
	getDataFromResultNotaManual : function(data) {	
		
		$.each(data.rows, function(index, value) {
			
			var edicaoItemNotaPermitida 	= value.cell.edicaoItemNotaPermitida;
			
			var lineId = value.cell.lineId;
	
			var imgExclusao = '<img src="'+contextPath+'/images/ico_excluir.gif" width="15" height="15" alt="Salvar" hspace="5" border="0" />'; 
			
			var imgEdicao = '<img src="'+contextPath+'/images/ico_editar.gif" width="15" height="15" alt="Salvar" hspace="5" border="0" />'; 
			
			value.cell.precoCapa = $.formatNumber(value.cell.precoCapa, {format:"#,##0.00", locale:"br"}); 
			value.cell.valorTotal = $.formatNumber(value.cell.valorTotal, {format:"#,##0.00", locale:"br"}); 
			
			if(edicaoItemNotaPermitida == "S") {
				
				value.cell.acao = '<a href="javascript:;" onclick="recebimentoFisicoController.editarItemNotaFiscal('+[lineId]+');">' + imgEdicao + '</a>'  +
								  '<a href="javascript:;" onclick="recebimentoFisicoController.excluirItemNotaFiscal('+[lineId]+');">' + imgExclusao + '</a>';
				
			} else{
				
				value.cell.acao = 	'<a href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)"  >' + imgEdicao   + '</a>' + 
							 		'<a href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)"  >' + imgExclusao + '</a>';
				
			} 
			
			
		});
		
		if(data.rows) {

			$(".grids", recebimentoFisicoController.workspace).show();
			
		}
		
		if(!recebimentoFisicoController.indRecebimentoFisicoConfirmado){
			
			$("#botoesNormais", recebimentoFisicoController.workspace).show();
			$("#botoesOpacos", recebimentoFisicoController.workspace).hide();
			$("#botaoNovoProdutoOpaco", recebimentoFisicoController.workspace).hide();
			
			if(recebimentoFisicoController.indNotaFiscalInterface){
				$("#botaoNovoProduto", recebimentoFisicoController.workspace).hide();
			}else{
				$("#botaoNovoProduto", recebimentoFisicoController.workspace).show();
			}
				
			
		}else{

			$("#botoesOpacos", recebimentoFisicoController.workspace).show();
			$("#botoesNormais", recebimentoFisicoController.workspace).hide();			
			
			if(recebimentoFisicoController.indNotaFiscalInterface){
				$("#botaoNovoProdutoOpaco", recebimentoFisicoController.workspace).hide();
			}else{
				$("#botaoNovoProdutoOpaco", recebimentoFisicoController.workspace).show();
			}
			$("#botaoNovoProduto", recebimentoFisicoController.workspace).hide();

		}
		
		return data;

	},
	
	alterarValor : function(idLinha) {

		var qtdPacote 		= $("#qtdPacote_"+idLinha).val();
		var qtdQuebra 		= $("#qtdExemplar_"+idLinha).val();

		if (qtdPacote == "") {
			qtdPacote = 0;
			$("#qtdPacote_"+idLinha).val(0);
		}

		if (qtdQuebra == "") {
			qtdQuebra = 0;
			$("#qtdExemplar_"+idLinha).val(0);
		}

		var qtdPacote 		= parseInt(qtdPacote);
		var qtdQuebra 		= parseInt(qtdQuebra);
		var repartePrevisto = parseInt($("#repartePrevisto_"+idLinha).text());
		var pacotePadrao 	= parseInt($("#pacotePadrao_"+idLinha).val());
		var diferenca 		= 0;

		diferenca = ( (qtdPacote * pacotePadrao) + qtdQuebra) - repartePrevisto; 

		if (diferenca < 0) {
			$("#diferenca_"+idLinha)[0].style.color = "red";			
		} else {
			$("#diferenca_"+idLinha)[0].style.color = "black";			
		}

		$("#diferenca_"+idLinha).text(diferenca);
	},
	
	alterarValorItem : function(idLinha) {
		
		var preco = priceToFloat($("#precoDescontoItem"+idLinha, recebimentoFisicoController.workspace).val());
		
		var qtdPacote 		= $("#qtdPacoteItem"+idLinha).val();
		var qtdQuebra 		= $("#qtdExemplarItem"+idLinha).val();

		if (qtdPacote == "") {
			qtdPacote = 0;
			$("#qtdPacoteItem"+idLinha).val(0);
		}

		if (qtdQuebra == "") {
			qtdQuebra = 0;
			$("#qtdExemplarItem"+idLinha).val(0);
		}
		
		var qtdPacote 		= parseInt(qtdPacote);
		var qtdQuebra 		= parseInt(qtdQuebra);
		var repartePrevisto = parseInt($("#qtdNotaItem"+idLinha).text());
		var pacotePadrao 	= parseInt($("#pacotePadraoItem"+idLinha).val());
		var diferenca 		= 0;

		var valor = preco * ((qtdPacote * pacotePadrao) + qtdQuebra)

        $("#valorItem"+idLinha, recebimentoFisicoController.workspace).val($.formatNumber(valor,{locale:'br'}));
		
		diferenca = ((qtdPacote * pacotePadrao) + qtdQuebra) - repartePrevisto; 

		if (diferenca < 0) {
			$("#diferencaItem"+idLinha)[0].style.color = "red";			
		} else {
			$("#diferencaItem"+idLinha)[0].style.color = "black";			
		}

		if (isNaN(diferenca)){
        	diferenca = 0;
        }
		
		$("#diferencaItem"+idLinha).val(diferenca);
		
		 $("#diferencaItem"+idLinha, recebimentoFisicoController.workspace).numeric({
			decimal:''
		});
		
		recebimentoFisicoController.obterValorTotalItens();
	},
	
	numericOnly : function(event) {
          var num=event.keyCode;
          if(num>=48 & num<=57)
               return true;
          return false; 
	},
	
	/**
	 * PREPARA OS DADOS DA NOTA INTERFACE A SEREM APRESENTADOS NA GRID.
	 */
	getDataFromResultNotaInterface : function(data) {		
		
		$.each(data.rows, function(index, value) {
			
			var edicaoItemRecFisicoPermitida 	= value.cell.edicaoItemRecFisicoPermitida;
			
			var edicaoItemNotaPermitida 		= value.cell.edicaoItemNotaPermitida;
			
			var destacarValorNegativo			= value.cell.destacarValorNegativo;
			
			var qtdPacote = value.cell.qtdPacote;
			
			var qtdExemplar = value.cell.qtdExemplar; 
			
			var diferenca = value.cell.diferenca;

			var repartePrevisto = value.cell.repartePrevisto;

			var lineId = value.cell.lineId;

			var hiddenFields = '<input type="hidden" name="lineId" value="'+lineId+'"/>';
			
			var imgExclusao = '<img src="'+contextPath+'/images/ico_excluir.gif" width="15" height="15" alt="Salvar" hspace="5" border="0" />'; 
			
			var imgEdicao = '<img src="'+contextPath+'/images/ico_editar.gif" width="15" height="15" alt="Salvar" hspace="5" border="0" />'; 
			
			var pacotePadrao = value.cell.pacotePadrao;
			
			
			if(destacarValorNegativo == "S") {
				value.cell.diferenca = '<span style="color: red" id="diferenca_'+lineId+'">'+diferenca+'</span>';
			} else {
				value.cell.diferenca = '<span style="color: black" id="diferenca_'+lineId+'">'+diferenca+'</span>';
			}

			value.cell.repartePrevisto = '<span id="repartePrevisto_'+lineId+'">'+repartePrevisto+'</span>'; 
			
			if(edicaoItemRecFisicoPermitida == "S") {
				value.cell.qtdPacote 	=  '<input name="qtdPacote" id="qtdPacote_'+ lineId +'" onkeypress="return recebimentoFisicoController.numericOnly(event)" style="width: 45px;" type="text" value="'+qtdPacote+'" onblur="recebimentoFisicoController.alterarValor('+ lineId +')" onfocus="recebimentoFisicoController.tratarFocoInputQuantidade(this)" />'+hiddenFields;
				value.cell.qtdExemplar = '<input name="qtdExemplar" id="qtdExemplar_'+ lineId +'" onkeypress="return recebimentoFisicoController.numericOnly(event)"  style="width: 45px;" type="text" value="'+qtdExemplar+'" onblur="recebimentoFisicoController.alterarValor('+ lineId +')" onfocus="recebimentoFisicoController.tratarFocoInputQuantidade(this)" />';
				$('#chBoxReplicaValorRepartePrevistoAll', recebimentoFisicoController.workspace).enable();
			} else {
				value.cell.qtdPacote 	= '<input name="qtdPacote" disabled="disabled" style="width: 45px;" type="text" value="'+qtdPacote+'"/>'+hiddenFields;
				value.cell.qtdExemplar 	=  '<input name="qtdExemplar" disabled="disabled" style="width: 45px;" type="text" value="'+qtdExemplar+'"/>';
				$('#chBoxReplicaValorRepartePrevistoAll', recebimentoFisicoController.workspace).disable();
			}
			
			$('#chBoxReplicaValorRepartePrevistoAll', recebimentoFisicoController.workspace).uncheck();
			
			value.cell.precoCapa = $.formatNumber(value.cell.precoCapa, {format:"#,##0.00", locale:"br"}); 
			value.cell.valorTotal = $.formatNumber(value.cell.valorTotal, {format:"#,##0.00", locale:"br"}); 

			if(edicaoItemNotaPermitida == "S") {
				
				value.cell.acao =  '<a href="javascript:;" onclick="recebimentoFisicoController.editarItemNotaFiscal('+[lineId]+');">' + imgEdicao + '</a>' +
								   '<a href="javascript:;" onclick="recebimentoFisicoController.excluirItemNotaFiscal('+[lineId]+');">' + imgExclusao + '</a>' +
								   '<input type="hidden" id="pacotePadrao_'+[lineId]+'" value='+pacotePadrao+' />';
				
			} else{
				
				value.cell.acao = 	'<a href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)"  >' + imgEdicao   + '</a>' + 
							 		'<a href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)"  >' + imgExclusao + '</a>'  +
				   					'<input type="hidden" id="pacotePadrao_'+[lineId]+'" value='+pacotePadrao+' />';
			}
			
			
			
			
			if(edicaoItemRecFisicoPermitida == "S") {
				value.cell.replicaQtd = '<input title="Replicar Item" onclick="recebimentoFisicoController.replicarValorRepartePrevisto('+
										[lineId] + ', this);" type="checkbox" id="replicaValorRepartePrevisto_'+lineId+'" name="replicaQtde" />';
			} else {
				value.cell.replicaQtd = '<input title="Replicar Item" disabled="disabled" type="checkbox"/>';
			}
			
		});
		
		$(".grids", recebimentoFisicoController.workspace).show();

		if(!recebimentoFisicoController.indRecebimentoFisicoConfirmado){
			
			$('#botoesNormais', recebimentoFisicoController.workspace).show();
			
			$('#botaoNovoProdutoOpaco', recebimentoFisicoController.workspace).hide();
			
			if(recebimentoFisicoController.indNotaFiscalInterface){
				$("#botaoNovoProduto", recebimentoFisicoController.workspace).hide();
			}else{
				$("#botaoNovoProduto", recebimentoFisicoController.workspace).show();
			}
			
			$('#botoesOpacos', recebimentoFisicoController.workspace).hide();
			
			
		} else {
			
			$('#botoesOpacos', recebimentoFisicoController.workspace).show();
			
			$('#botoesNormais', recebimentoFisicoController.workspace).hide();
			
			if(recebimentoFisicoController.indNotaFiscalInterface){
				$("#botaoNovoProdutoOpaco", recebimentoFisicoController.workspace).hide();
			}else{
				$("#botaoNovoProdutoOpaco", recebimentoFisicoController.workspace).show();
			}
			
			$('#botaoNovoProduto', recebimentoFisicoController.workspace).hide();
			
		}	
		
		return data;

	},
	
	tratarFocoInputQuantidade : function(input) {

		if (0 == input.value) {
		
			input.value = "";
		}
	},
	
	//------------------------- NOVO POPUP DE NOTA ---------------------------
	
    formatItemNota : function(){
		
		$(".number").numeric({
			decimal:''
		});

    	$(".money").maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});
	},
	
	inicializarGridPopUpNota : function() {
	
		$(".novoItemNotaGrid", recebimentoFisicoController.workspace).flexigrid({
			preProcess: recebimentoFisicoController.getDataFromResultItem,
			onSuccess: recebimentoFisicoController.formatItemNota,
			dataType : 'json',
			colModel : [
			{
				display : 'Código',
				name : 'codigo	',
				width : 60,
				sortable : false,
				align : 'left',
				resizable : false
			}, {
				display : 'Produto',
				name : 'produto	',
				width : 150,
				sortable : false,
				align : 'left',
				resizable : false
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 40,
				sortable : false,
				align : 'left',
				resizable : false
			}, {
				display : 'Preço Desc. R$',
				name : 'precoDesconto',
				width : 90,
				sortable : false,
				align : 'right',
				resizable : false
			}, {
				display : 'Qtde Nota',
				name : 'qtdeNota',
				width : 80,
				sortable : false,
				align : 'center',
				resizable : false
			}, {
				display : 'Qtde. Pcts',
				name : 'qtdePcts',
				width : 80,
				sortable : false,
				align : 'center',
				resizable : false
			}, {
				display : 'Qtde. Exems',
				name : 'qtdeExemplar',
				width : 80,
				sortable : false,
				align : 'center',
				resizable : false
			}, {
				display : 'Pcte. Padrão',
				name : 'pacotePadrao',
				width : 70,
				sortable : false,
				align : 'center',
				resizable : false
			}, {
				display : 'Diferença',
				name : 'diferenca',
				width : 80,
				sortable : false,
				align : 'center',
				resizable : false
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 70,
				sortable : false,
				align : 'center',
				resizable : false
			}, {
				display : 'Replicar',
				name : 'replicar',
				width : 40,
				sortable : false,
				align : 'center',
				resizable : false
			}],
			width : 1000,
			height : 180
		});

		
	},
	
	popup_adicionar : function() {
		this.limparCamposNovaNota();
		
		this.novoValorTotalTyped = false;
		recebimentoFisicoController.montaGridItens();
		
		
		$( "#dialog-adicionar", recebimentoFisicoController.workspace ).dialog({
			resizable: false,
			height:530,
			width:1028,
			modal: true,
			buttons:[ 
			          {
				           id:"bt_confirmar",
				           text:"Confirmar", 
				           click: function() {
				        	   recebimentoFisicoController.validarValorTotalNotaFiscal();
				        	   recebimentoFisicoController.limparCamposPesquisa();
				        	   $(".grids", recebimentoFisicoController.workspace).hide();
				           }
			           },
			           {
				           id:"bt_cancelar",
				           text:"Cancelar", 
				           click: function() {
				        	   $( this ).dialog( "close" );
				           }
			           }
	        ],
		    
	        beforeClose: function() {
			    clearMessageDialogTimeout();
	        },
	        
	        form: $("#div-wrapper-dialog-adicionar", recebimentoFisicoController.workspace)
	        
		});
	},

	pesquisarFornecedorCnpj : function() {
		
		var pCnpj = $("#novoCnpj", recebimentoFisicoController.workspace).val();	

		if(pCnpj == "") {
			$("#novoFornecedor", recebimentoFisicoController.workspace).val("");
			return;
		}
		
		$.postJSON(recebimentoFisicoController.path + 'buscaCnpj', {cnpj: pCnpj}, 
		function(result) { 
			$("#novoFornecedor", recebimentoFisicoController.workspace).val(result.cnpj);
		});	
	},

	pesquisarCnpjFornecedor : function() {
	
		var idFornecedor = $("#novoFornecedor", recebimentoFisicoController.workspace).val();
		
		if(idFornecedor == -1){

			$("#novoCnpj", recebimentoFisicoController.workspace).val("");
			
			$("#novoCnpj", recebimentoFisicoController.workspace).disabled(true);
			
		}else{
			$.postJSON(recebimentoFisicoController.path + 'obterCnpjFornecedor', {idFornecedor:idFornecedor}, 
			
			function(result) {
				
				var cnpj = result;

				if (cnpj.result != undefined) {
					$("#novoCnpj", recebimentoFisicoController.workspace).val("");
				} else if (cnpj != "") {
					$("#novoCnpj", recebimentoFisicoController.workspace).val(recebimentoFisicoController.mascaraCNPJ(cnpj));
				}
					
				$("#novoCnpj", recebimentoFisicoController.workspace).attr('disabled', true);
				
			},
			null,
			true);
		}
	},
	
	mascaraCNPJ : function(cnpj) {
        return recebimentoFisicoController.formataCampo(cnpj, '00.000.000/0000-00');
	},
	
	//formata de forma generica os campos
	formataCampo : function(campo, Mascara) { 
	        var boleanoMascara; 

	        exp = /\-|\.|\/|\(|\)| /g
	        campoSoNumeros = campo.replace( exp, "" ); 

	        var posicaoCampo = 0;    
	        var NovoValorCampo="";
	        var TamanhoMascara = campoSoNumeros.length;; 

            for(i=0; i<= TamanhoMascara; i++) { 
                    boleanoMascara  = ((Mascara.charAt(i) == "-") || (Mascara.charAt(i) == ".")
                                                            || (Mascara.charAt(i) == "/")) 
                    boleanoMascara  = boleanoMascara || ((Mascara.charAt(i) == "(") 
                                                            || (Mascara.charAt(i) == ")") || (Mascara.charAt(i) == " ")) 
                    if (boleanoMascara) { 
                            NovoValorCampo += Mascara.charAt(i); 
                              TamanhoMascara++;
                    } else {
                            NovoValorCampo += campoSoNumeros.charAt(posicaoCampo); 
                            posicaoCampo++; 
                    }              
            }
            return NovoValorCampo;
	},
	
	obterDadosEdicao : function(index) {
		
		var codigo = $("#codigoItem"+index, recebimentoFisicoController.workspace).val();
		
		var edicao = $("#edicaoItem"+index, recebimentoFisicoController.workspace).val();	

		if((codigo == "")||(edicao == "")) {
			$("#precoDescontoItem"+index, recebimentoFisicoController.workspace).val("");
			$("#pacotePadraoItem"+index, recebimentoFisicoController.workspace).val("");
			return;
		}
		
		$.postJSON(this.path + 'obterDadosEdicao', {codigo:codigo,edicao:edicao}, 
			function(result) { 
				
				$("#precoDescontoItem"+index, recebimentoFisicoController.workspace).val(result.precoDesconto);
				$("#pacotePadraoItem"+index, recebimentoFisicoController.workspace).val(result.pacotePadrao);
			},
			function(result) {
				$("#precoDescontoItem"+index, recebimentoFisicoController.workspace).val("");
				$("#pacotePadraoItem"+index, recebimentoFisicoController.workspace).val("");
			},
			true
		);	
	},
	
	calcularDiferencaEValorItem : function(index){
		
		var preco = priceToFloat($("#precoDescontoItem"+index, recebimentoFisicoController.workspace).val());
		var quantidade = removeMascaraPriceFormat($("#qtdNotaItem"+index, recebimentoFisicoController.workspace).val());
		var quantidadeExemp = removeMascaraPriceFormat($("#qtdExemplarItem"+index, recebimentoFisicoController.workspace).val());
		
		if((preco == "")||(quantidade == "")) {
			$("#valorItem"+index, recebimentoFisicoController.workspace).val("");
			return;
		}
		
		if((quantidade == "")||(quantidadeExemp == "")) {
			$("#diferencaItem"+index, recebimentoFisicoController.workspace).val("");
		}
		
        var valor = preco * intValue(quantidade);

        $("#valorItem"+index, recebimentoFisicoController.workspace).val($.formatNumber(valor,{locale:'br'}));

        var diferenca = 0;
        if (quantidade > quantidadeExemp){
        	diferenca = intValue(quantidade) - intValue(quantidadeExemp);
        }
        else{
        	diferenca = intValue(quantidadeExemp) - intValue(quantidade);
        }

        if (isNaN(diferenca)){
        	diferenca = 0;
        }
        
        $("#diferencaItem"+index, recebimentoFisicoController.workspace).val(diferenca);
        
	    $("#diferencaItem"+index, recebimentoFisicoController.workspace).numeric({
			decimal:''
		});
	    
	    recebimentoFisicoController.obterValorTotalItens();
	},
	
	obterCabecalho : function() {

		var dadosCabecalho = {
				'nota.fornecedor':$("#novoFornecedor",recebimentoFisicoController.workspace).val(),
				'nota.cnpj':$("#novoCnpj",recebimentoFisicoController.workspace).val(),
				'nota.numero':$("#novoNumeroNota",recebimentoFisicoController.workspace).val(),
				'nota.numeroNotaEnvio':$("#novoNumeroNotaEnvio",recebimentoFisicoController.workspace).val(),
				'nota.serie':$("#novoSerieNota",recebimentoFisicoController.workspace).val(),
				'nota.chaveAcesso':$("#novoChaveAcesso",recebimentoFisicoController.workspace).val(),
				'nota.dataEmissao':$("#novoDataEmissao",recebimentoFisicoController.workspace).val(),
				'nota.dataEntrada':$("#novoDataEntrada",recebimentoFisicoController.workspace).val(),
				'nota.valorTotal':$("#novoValorTotal",recebimentoFisicoController.workspace).val()};
		return dadosCabecalho;
	},

    montaGridItens : function() {
		$(".novoItemNotaGrid", recebimentoFisicoController.workspace).flexOptions({url: contextPath + '/estoque/recebimentoFisico/montaGridItemNota'});
		$(".novoItemNotaGrid", recebimentoFisicoController.workspace).flexReload();	
	},	
	
	getDataFromResultItem : function(resultado) {

		if (resultado.mensagens) {
			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".grids", recebimentoFisicoController.workspace).hide();
			
			recebimentoFisicoController.ocultarBtns();
			
			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {

			 var valueCodigo='';
			 if (row.cell.codigoProduto!=null){
				 valueCodigo = row.cell.codigoProduto;
			 }
			 
			 var valueProduto='';
			 if (row.cell.nomeProduto!=null){
				 valueProduto = row.cell.nomeProduto;
			 }
			 
			 var valueEdicao='';
			 if (row.cell.edicao!=null){
				 valueEdicao = row.cell.edicao;
			 }
			 
			 var valuePrecoDesconto='';
			 if (row.cell.precoDesconto!=null){
				 valuePrecoDesconto = row.cell.precoDesconto;
			 }
			 
			 var valueQtdNota='';
			 if (row.cell.qtdFisico!=null){
				 valueQtdNota = row.cell.qtdFisico;
			 }
			 
			 var valueQtdPacote='';
			 if (row.cell.qtdPacote!=null){
				 valueQtdPacote = row.cell.qtdPacote;
			 }
			 
			 var valueQtdExemplar='';
			 if (row.cell.qtdExemplar!=null){
				 valueQtdExemplar = row.cell.qtdExemplar;
			 }
			 
			 var valuePacotePadrao='';
			 if (row.cell.pacotePadrao!=null && row.cell.pacotePadrao != 0){
				 valuePacotePadrao = row.cell.pacotePadrao;
			 }
			 
			 var valueDiferenca='';
			 if (row.cell.diferenca!=null){
				 valueDiferenca = row.cell.diferenca;
			 }
			 
			 var valueValor='';
			 if (row.cell.valorTotal!=null){
				 valueValor = row.cell.valorTotal;
			 }

        	 var codigo =       '<input class="number" maxlength="28" value="'+valueCodigo+'" type="text" name="itensRecebimento.codigoItem" id="codigoItem'+ index +'" style="width: 50px;" onchange="pesquisaProdutoRecebimentoFisico.pesquisarPorCodigoProduto(\'#codigoItem'+ index +'\', \'#produtoItem'+ index +'\', \'#edicaoItem'+ index +'\', true, null);" ></input>';
	         			 					     
	         var produto =      '<input maxlength="200" value="'+valueProduto+'" type="text" name="itensRecebimento.produtoItem" id="produtoItem'+ index +'" style="width: 140px;" onkeyup="pesquisaProdutoRecebimentoFisico.autoCompletarPorNomeProduto(\'#produtoItem'+ index +'\', false);" onblur="pesquisaProdutoRecebimentoFisico.pesquisarPorNomeProduto(\'#codigoItem'+ index +'\', \'#produtoItem'+ index +'\', \'#edicaoItem'+ index +'\', true, null);"></input>';
				             
			 var edicao =       '<input class="number" maxlength="18" value="'+valueEdicao+'" type="text" name="itensRecebimento.edicaoItem" id="edicaoItem'+ index +'" style="width: 30px;" onchange="recebimentoFisicoController.obterDadosEdicao('+index+');"></input>';         
			
			 var precoDesconto ='<input class="money" maxlength="17" value="'+valuePrecoDesconto+'" type="text" readonly="readonly" name="itensRecebimento.precoDescontoItem" id="precoDescontoItem'+ index +'" style="width: 80px; border: 0px; background-color: inherit;"></input>';
			 
			 var qtdNota =      '<input class="number" maxlength="10" value="'+valueQtdNota+'" type="text" name="itensRecebimento.qtdNotaItem" id="qtdNotaItem'+ index +'" style="width: 70px;" onchange="recebimentoFisicoController.replicarQuantidadeItem('+index+'); recebimentoFisicoController.alterarValorItem('+index+');"></input>';
			     
	         var qtdPacote =    '<input class="number" maxlength="10" value="'+valueQtdPacote+'" type="text" name="itensRecebimento.qtdPacoteItem" id="qtdPacoteItem'+ index +'" style="width: 70px;" onchange="recebimentoFisicoController.alterarValorItem('+index+');"></input>';
				             
			 var qtdExemplar =  '<input class="number" maxlength="10" value="'+valueQtdExemplar+'" type="text" name="itensRecebimento.qtdExemplarItem" id="qtdExemplarItem'+ index +'" style="width: 70px;" onchange="recebimentoFisicoController.alterarValorItem('+index+');"></input>'; 
			 
			 var pacotePadrao =  '<input class="number" maxlength="10" value="'+valuePacotePadrao+'" type="text" readonly="readonly" name="itensRecebimento.pacotePadraoItem" id="pacotePadraoItem'+ index +'" style="width: 70px; border: 0px; background-color: inherit;"></input>';
			 
			 var diferenca =    '<input class="number" maxlength="10" value="'+valueDiferenca+'" type="text" readonly="readonly" name="itensRecebimento.diferencaItem" id="diferencaItem'+ index +'" style="width: 70px; border: 0px; background-color: inherit;"></input>';
				 
			 var valor =        '<input class="money" maxlength="17" value="'+valueValor+'" type="text" readonly="readonly" name="itensRecebimento.valorItem" id="valorItem'+ index +'" style="width: 70px; border: 0px; background-color: inherit;"></input>';
						 
			 var checkBox =     '<input title="Replicar Item" type="checkbox" name="checkboxGrid" id="checkbox'+ index +'"/>';

		     row.cell[0] = codigo;
		     row.cell[1] = produto;
		     row.cell[2] = edicao;
		     row.cell[3] = precoDesconto;
		     row.cell[4] = qtdNota;
		     row.cell[5] = qtdPacote;
		     row.cell[6] = qtdExemplar;
		     row.cell[7] = pacotePadrao;
		     row.cell[8] = diferenca;
		     row.cell[9] = valor;
		     row.cell[10] = checkBox;
         }
	    
		);

		return resultado;
	},
	
	isAtributosLancamentoVazios : function(codigo, produto, edicao, precoDesconto, qtdNota, qtdPacote, qtdExemplar) {

		if (!$.trim(codigo) 
				&& !$.trim(produto)
				&& !$.trim(edicao) 
				&& !$.trim(precoDesconto)
				&& !$.trim(qtdNota)
				&& !$.trim(qtdPacote)
				&& !$.trim(qtdExemplar)) {

			return true;
		}
	},
	
	replaceAll : function(string, token, newtoken) {
		while (string.indexOf(token) != -1) {
	 		string = string.replace(token, newtoken);
		}
		return string;
	},
	
	preparaValor : function(vr){
		
		if(vr.substr(vr.length-3,1)==","){
			vr = this.replaceAll(vr,".","");
			vr = this.replaceAll(vr,",",".");
		}
		if(vr.substr(vr.length-3,1)=="."){
			vr = this.replaceAll(vr,",","");
		}
		return vr;
	},
	
	obterListaItens : function() {

		var linhasDaGrid = $(".novoItemNotaGrid tr", recebimentoFisicoController.workspace);

		var listaItens = new Array();

		$.each(linhasDaGrid, function(index, value) {

			var linha = $(value);
			
			var colunaCodigo = linha.find("td")[0];
			var colunaProduto = linha.find("td")[1];
			var colunaEdicao = linha.find("td")[2];
			var colunaPrecoDesconto = linha.find("td")[3];
			var colunaQtdNota = linha.find("td")[4];
			var colunaQtdPacote = linha.find("td")[5];
			var colunaQtdExemplar = linha.find("td")[6];
			var colunaPacotePadrao = linha.find("td")[7];
			var colunaDiferenca = linha.find("td")[8];
			var colunaValor = linha.find("td")[9];
			var colunaCheck = linha.find("td")[10];
			
			var codigo = 
				$(colunaCodigo).find("div").find('input[name="itensRecebimento.codigoItem"]').val();
			
			var produto = 
				$(colunaProduto).find("div").find('input[name="itensRecebimento.produtoItem"]').val();

			var edicao = 
				$(colunaEdicao).find("div").find('input[name="itensRecebimento.edicaoItem"]').val();

			var precoDesconto =
				$(colunaPrecoDesconto).find("div").find('input[name="itensRecebimento.precoDescontoItem"]').val();
			
			var qtdNota =
				$(colunaQtdNota).find("div").find('input[name="itensRecebimento.qtdNotaItem"]').val();
			
			var qtdPacote =
				$(colunaQtdPacote).find("div").find('input[name="itensRecebimento.qtdPacoteItem"]').val();
			
			var qtdExemplar =
				$(colunaQtdExemplar).find("div").find('input[name="itensRecebimento.qtdExemplarItem"]').val();
			
			var pacotePadrao =
				$(colunaPacotePadrao).find("div").find('input[name="itensRecebimento.pacotePadraoItem"]').val();
			
			var diferenca =
				$(colunaDiferenca).find("div").find('input[name="itensRecebimento.diferencaItem"]').val();
			
			var valor =
				$(colunaValor).find("div").find('input[name="itensRecebimento.valorItem"]').val();
			

			if (!recebimentoFisicoController.isAtributosLancamentoVazios(codigo, produto, edicao, precoDesconto, qtdNota, qtdPacote, qtdExemplar)) {

	            var dataLancamento = $("#novoDataEntrada", recebimentoFisicoController.workspace).val();
				
			    var dataRecolhimento = $("#novoDataEmissao", recebimentoFisicoController.workspace).val();
	
				var lancamento = {
						codigoProduto:codigo,
						nomeProduto:produto,
						edicao:edicao,
						precoDesconto:recebimentoFisicoController.preparaValor(precoDesconto),
						repartePrevisto:qtdNota,
						qtdFisico:qtdNota,
						qtdPacote:qtdPacote,
						qtdExemplar:qtdExemplar,
						pacotePadrao:pacotePadrao,
						diferenca:diferenca,
						valorTotal:recebimentoFisicoController.preparaValor(valor),
						dataLancamento:dataLancamento,
						dataRecolhimento:dataRecolhimento};
				
				listaItens.push(lancamento);
			
			}

		});

		return listaItens;
	},

	obterValorTotalItens : function() {

		var linhasDaGrid = $(".novoItemNotaGrid tr", recebimentoFisicoController.workspace);

		var listaItens = "";
		
		var valorTotal = 0;

		$.each(linhasDaGrid, function(index, value) {

			var linha = $(value);

			var colunaValor = linha.find("td")[9];

			valorTotal += intValue(removeMascaraPriceFormat($(colunaValor).find("div").find('input[name="itensRecebimento.valorItem"]').val()));
			
		});
		
		if(!this.novoValorTotalTyped){
			$("#novoValorTotal", recebimentoFisicoController.workspace).val(floatToPrice(valorTotal/100));
		}
       
        
        $("#labelValorTotal", recebimentoFisicoController.workspace).html(floatToPrice(valorTotal/100));
	},
    
    replicarQuantidadeItem : function(index){
    	
    	if ( $("#checkbox"+index, recebimentoFisicoController.workspace).attr('checked') == 'checked' || 
    		 $("#novoReplicarQtde", recebimentoFisicoController.workspace).attr('checked') == 'checked' ) {
    		
    	    $("#qtdExemplarItem"+index, recebimentoFisicoController.workspace).val($("#qtdNotaItem"+index, recebimentoFisicoController.workspace).val());
    	    
    	}    
    },

    //PREPARA NOVA LINHA DA GRID
    adicionarNovaLinha : function(index){

    	var row;

    	var codigo =       '<input class="number" maxlength="28" type="text" name="itensRecebimento.codigoItem" id="codigoItem'+ index +'" style="width: 50px;" onchange="pesquisaProdutoRecebimentoFisico.pesquisarPorCodigoProduto(\'#codigoItem'+ index +'\', \'#produtoItem'+ index +'\', \'#edicaoItem'+ index +'\', true, null);" ></input>';
	     
        var produto =      '<input maxlength="200" type="text" name="itensRecebimento.produtoItem" id="produtoItem'+ index +'" style="width: 140px;" onkeyup="pesquisaProdutoRecebimentoFisico.autoCompletarPorNomeProduto(\'#produtoItem'+ index +'\', false);" onblur="pesquisaProdutoRecebimentoFisico.pesquisarPorNomeProduto(\'#codigoItem'+ index +'\', \'#produtoItem'+ index +'\', \'#edicaoItem'+ index +'\', true, null);"></input>';
			             
		var edicao =       '<input class="number" maxlength="18" type="text" name="itensRecebimento.edicaoItem" id="edicaoItem'+ index +'" style="width: 30px;" onkeyup="recebimentoFisicoController.obterDadosEdicao('+index+');"></input>';         
		
		var precoDesconto ='<input class="money" maxlength="17" type="text" readonly="readonly" name="itensRecebimento.precoDescontoItem" id="precoDescontoItem'+ index +'" style="width: 80px; border: 0px; background-color: inherit;"></input>';
		 
		var qtdNota =      '<input class="number" maxlength="10" type="text" name="itensRecebimento.qtdNotaItem" id="qtdNotaItem'+ index +'" style="width: 70px;" onchange="recebimentoFisicoController.replicarQuantidadeItem('+index+'); recebimentoFisicoController.alterarValorItem('+index+');"></input>';
		     
        var qtdPacote =    '<input class="number" maxlength="10" type="text" name="itensRecebimento.qtdPacoteItem" id="qtdPacoteItem'+ index +'" style="width: 70px;" onchange="recebimentoFisicoController.alterarValorItem('+index+');"></input>';
			             
		var qtdExemplar =  '<input class="number" maxlength="10" type="text" name="itensRecebimento.qtdExemplarItem" id="qtdExemplarItem'+ index +'" style="width: 70px;" onchange="recebimentoFisicoController.alterarValorItem('+index+');"></input>'; 
		
		var pacotePadrao =    '<input class="number" maxlength="10" type="text" readonly="readonly" name="itensRecebimento.pacotePadraoItem" id="pacotePadraoItem'+ index +'" style="width: 70px; border: 0px; background-color: inherit;"></input>';
		
		var diferenca =    '<input class="number" maxlength="10" type="text" readonly="readonly" name="itensRecebimento.diferencaItem" id="diferencaItem'+ index +'" style="width: 70px; border: 0px; background-color: inherit;"></input>';
			 
		var valor =        '<input class="money" maxlength="17" type="text" readonly="readonly" name="itensRecebimento.valorItem" id="valorItem'+ index +'" style="width: 70px; border: 0px; background-color: inherit;"></input>';
					 
		var checkBox =     '<input title="Replicar Item" type="checkbox" name="checkboxGrid" id="checkbox'+ index +'"/>';

    	var row = [{name: 'codigo', value: codigo},
   	               {name: 'produto', value: produto},
   	               {name: 'edicao', value: edicao},
   	               {name: 'precoDesconto', value: precoDesconto},
   	          	   {name: 'qtdeNota', value: qtdNota},
   	               {name: 'qtdePcts', value: qtdPacote},
   	               {name: 'qtdeExemplar', value: qtdExemplar},
				   {name: 'pacotePadrao', value: pacotePadrao},
   	               {name: 'diferenca', value: diferenca},
   	               {name: 'valor', value: valor},
   	               {name: 'replicar', value: checkBox}];
    	
    	return row;
    },
    
    //ADICIONA NOVA LINHA NA GRID
    incluiNovoItem : function(){
    	
    	var data = [];

    	var dataValores = [];
    	var rowValores;
    	
    	var idx;
    	
    	//OBTEM VALORES DIGITADOS
    	var nLinhas = 0;
    	var linhasDaGrid = $(".novoItemNotaGrid tr", recebimentoFisicoController.workspace);
    	
    	$.each(linhasDaGrid, function(index, value) {
    		
    		nLinhas++;
    		
    		var linha = $(value);
    		var colunaCodigo = linha.find("td")[0];
			var colunaProduto = linha.find("td")[1];
			var colunaEdicao = linha.find("td")[2];
			var colunaPrecoDesconto = linha.find("td")[3];
			var colunaQtdNota = linha.find("td")[4];
			var colunaQtdPacote = linha.find("td")[5];
			var colunaQtdExemplar = linha.find("td")[6];
			var colunaPacotePadrao = linha.find("td")[7];
			var colunaDiferenca = linha.find("td")[8];
			var colunaValor = linha.find("td")[9];
			var colunaCheck = linha.find("td")[10];
			
			var valueCodigo = 
				$(colunaCodigo).find("div").find('input[name="itensRecebimento.codigoItem"]').val();
			
			var valueProduto = 
				$(colunaProduto).find("div").find('input[name="itensRecebimento.produtoItem"]').val();

			var valueEdicao = 
				$(colunaEdicao).find("div").find('input[name="itensRecebimento.edicaoItem"]').val();

			var valuePrecoDesconto =
				$(colunaPrecoDesconto).find("div").find('input[name="itensRecebimento.precoDescontoItem"]').val();
			
			var valueQtdNota =
				$(colunaQtdNota).find("div").find('input[name="itensRecebimento.qtdNotaItem"]').val();
			
			var valueQtdPacote =
				$(colunaQtdPacote).find("div").find('input[name="itensRecebimento.qtdPacoteItem"]').val();
			
			var valueQtdExemplar =
				$(colunaQtdExemplar).find("div").find('input[name="itensRecebimento.qtdExemplarItem"]').val();
			
			var valuePacotePadrao =
				$(colunaPacotePadrao).find("div").find('input[name="itensRecebimento.pacotePadraoItem"]').val();
			
			var valueDiferenca =
				$(colunaDiferenca).find("div").find('input[name="itensRecebimento.diferencaItem"]').val();
			
			var valueValor =
				$(colunaValor).find("div").find('input[name="itensRecebimento.valorItem"]').val();

			
		    rowValores = [{name: 'codigo', value: valueCodigo},
		   	              {name: 'produto', value: valueProduto},
		   	              {name: 'edicao', value: valueEdicao},
		   	              {name: 'precoDesconto', value: valuePrecoDesconto},
		   	              {name: 'qtdeNota', value: valueQtdNota},
		   	              {name: 'qtdePcts', value: valueQtdPacote},
						  {name: 'qtdeExemplar', value: valueQtdExemplar},
		   	              {name: 'pacotePadrao', value: valuePacotePadrao},
		   	              {name: 'diferenca', value: valueDiferenca},
		   	              {name: 'valor', value: valueValor},
		   	              {name: 'replicar', value: 0}];
			
		    dataValores.push({id:index, cell: rowValores});
			
		    //MANTEM LINHAS ATUAIS
			if (!recebimentoFisicoController.isAtributosLancamentoVazios(valueCodigo, valueProduto, valueEdicao, valuePrecoDesconto, valueQtdNota, valueQtdPacote, valueQtdExemplar)) {
		        data.push({id:nLinhas, cell: recebimentoFisicoController.adicionarNovaLinha(index)});
		    }
			 
		});
    	
    	//CRIA NOVA LINHA
    	data.push({id:nLinhas+1, cell: recebimentoFisicoController.adicionarNovaLinha(nLinhas)});

    	$(".novoItemNotaGrid", recebimentoFisicoController.workspace).flexAddData({
            rows : toFlexiGridObject(data),
            page : 1,
            total : nLinhas+1
        });

    	//RETORNA OS DADOS DIGITADOS PARA AS LINHAS ATUAIS
    	recebimentoFisicoController.recuperaValoresDigitados(dataValores);
    	
    	this.formatItemNota();
	},
    
    //RECUPERA VALORES DIGITADOS ANTES DA INSERÇÃO DA NOVA LINHA
    recuperaValoresDigitados : function(dataValores) {
    	
    	var linhasDaGrid = $(".novoItemNotaGrid tr", recebimentoFisicoController.workspace);
        
    	$.each(linhasDaGrid, function(index, value) {
        	
        	if (index < dataValores.length){

	    		var linha = $(value);
	    		var colunaCodigo = linha.find("td")[0];
				var colunaProduto = linha.find("td")[1];
				var colunaEdicao = linha.find("td")[2];
				var colunaPrecoDesconto = linha.find("td")[3];
				var colunaQtdNota = linha.find("td")[4];
				var colunaQtdPacote = linha.find("td")[5];
				var colunaQtdExemplar = linha.find("td")[6];
				var colunaPacotePadrao = linha.find("td")[7];
				var colunaDiferenca = linha.find("td")[8];
				var colunaValor = linha.find("td")[9];
				var colunaCheck = linha.find("td")[10];
				
				$(colunaCodigo).find("div").find('input[name="itensRecebimento.codigoItem"]').val(dataValores[index].cell[0].value);
				$(colunaProduto).find("div").find('input[name="itensRecebimento.produtoItem"]').val(dataValores[index].cell[1].value);
				$(colunaEdicao).find("div").find('input[name="itensRecebimento.edicaoItem"]').val(dataValores[index].cell[2].value);
				$(colunaPrecoDesconto).find("div").find('input[name="itensRecebimento.precoDescontoItem"]').val(dataValores[index].cell[3].value);
				$(colunaQtdNota).find("div").find('input[name="itensRecebimento.qtdNotaItem"]').val(dataValores[index].cell[4].value);
				$(colunaQtdPacote).find("div").find('input[name="itensRecebimento.qtdPacoteItem"]').val(dataValores[index].cell[5].value);
				$(colunaQtdExemplar).find("div").find('input[name="itensRecebimento.qtdExemplarItem"]').val(dataValores[index].cell[6].value);
				$(colunaPacotePadrao).find("div").find('input[name="itensRecebimento.pacotePadraoItem"]').val(dataValores[index].cell[7].value);
				$(colunaDiferenca).find("div").find('input[name="itensRecebimento.diferencaItem"]').val(dataValores[index].cell[8].value);
				$(colunaValor).find("div").find('input[name="itensRecebimento.valorItem"]').val(dataValores[index].cell[9].value);
	        	
        	}
        });
    	
    },    
    
	selecionarTodos : function(checked){
		
		for (var i=0;i<document.formularioItensNota.elements.length;i++) {
		     var x = document.formularioItensNota.elements[i];
		     if (x.name == 'checkboxGrid') {
		    	 x.checked = checked;
		     }    
		}

		if (checked){
			var elem = document.getElementById("textoSelTodos");
			elem.innerHTML = "Desmarcar todos";
        }
		
		else{
			var elem = document.getElementById("textoSelTodos");
			elem.innerHTML = "Marcar todos";
		}
	},
    
	limparCamposNovaNota : function(){
		
		$("#novoFornecedor", 	recebimentoFisicoController.workspace).val(-1);
		$("#novoCnpj", 			recebimentoFisicoController.workspace).val("");
		$("#novoNumeroNota", 	recebimentoFisicoController.workspace).val("");
        $("#novoSerieNota", 	recebimentoFisicoController.workspace).val("");
        $("#novoNfe", 			recebimentoFisicoController.workspace).val("");
        $("#novoChaveAcesso", 	recebimentoFisicoController.workspace).val("");
        $("#novoDataEmissao", 	recebimentoFisicoController.workspace).val("");
        $("#novoDataEntrada", 	recebimentoFisicoController.workspace).val("");
        $("#novoValorTotal", 	recebimentoFisicoController.workspace).val("");
        
        recebimentoFisicoController.montaGridItens();
	},
	
	
	showConfirmacaoValorTotalNotaFiscalDivergente : function() {
		
		$("#dialog-valor-nota-divergente", this.workspace).dialog({
			
			resizable : false,
			height : 'auto',
			width : 450,
			modal : true,
			buttons : {
				
				"Confirmar" : function() {
					$(this).dialog("close");
					recebimentoFisicoController.incluirNota();
				},

				"Cancelar" : function() {
					$(this).dialog("close");
				}
			},
			
			form: $("#dialog-valor-nota-divergente", this.workspace).parents("form")
		
		});	
		
	},
	
	validarValorTotalNotaFiscal : function() {
		
		var url;
		
		var formData = recebimentoFisicoController.obterCabecalho();				
		
		formData = serializeArrayToPost('itens', recebimentoFisicoController.obterListaItens(), formData);
		
		url = this.path + 'validarValorTotalNotaFiscal';
		
		$.postJSON(url, formData, 
				function(result) {
				if(result.validacao) {
					recebimentoFisicoController.showConfirmacaoValorTotalNotaFiscalDivergente();
				}else{
					recebimentoFisicoController.incluirNota();
				}
		});		
		
	},
	
	incluirNota : function() {

		var url;
		var formData = recebimentoFisicoController.obterCabecalho();				
		formData = serializeArrayToPost('itens', recebimentoFisicoController.obterListaItens(), formData);
		url = this.path + 'incluirNota';

		$.postJSON(
			url,
			formData,
			function(result) {
				
				recebimentoFisicoController.limparCamposNovaNota();
				
				$("#dialog-adicionar", recebimentoFisicoController.workspace).dialog( "close" );
	
				exibirMensagem(
					result.tipoMensagem, 
					result.listaMensagens
				);  
			},
			null,
			true
		);
	},
		
	exibirDetalhesProdutoEdicao : function() {
		
		var data = {codigo:$("#codigo", recebimentoFisicoController.workspace).val(),edicao:$("#edicao", recebimentoFisicoController.workspace).val()};
		
		$.postJSON( recebimentoFisicoController.path + 'obterProdutoEdicao', data, function(result){
			
			if(typeof result != "undefined") {
				
				$("#precoCapa", recebimentoFisicoController.workspace).val(result.precoVenda);			
				$("#peso", recebimentoFisicoController.workspace).val(result.peso);			
				$("#pacotePadrao", recebimentoFisicoController.workspace).val(result.pacotePadrao);			
				
			}
			
		});
		
	}
		
	
}, BaseController);

//@ sourceURL=meuScriptRecebimentoFisico.js