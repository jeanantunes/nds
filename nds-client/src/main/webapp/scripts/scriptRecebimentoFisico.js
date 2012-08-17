var recebimentoFisicoController = $.extend(true, {
	
	path: contextPath + '/estoque/recebimentoFisico/',
	
	init: function() {
		
		$("#datepickerLancto", this.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		$("#datepickerRecolhimento", this.workspace).datepicker({
			showOn: "button",
			buttonImage:  contextPath + "/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		$("#dataEmissao", this.workspace).datepicker({
			showOn: "button",
			buttonImage:  contextPath + "/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		
		$("#novoDataEmissao", this.workspace).datepicker({
			showOn: "button",
			buttonImage:  contextPath + "/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		
		$("#novoDataEntrada", this.workspace).datepicker({
			showOn: "button",
			buttonImage:  contextPath + "/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		
		$("#datepickerLancto", this.workspace).mask("99/99/9999");
		$("#datepickerRecolhimento", this.workspace).mask("99/99/9999");
		$("#dataEmissao", this.workspace).mask("99/99/9999");
		$("#novoDataEmissao", this.workspace).mask("99/99/9999");
		$("#novoDataEntrada", this.workspace).mask("99/99/9999");
		
		$("#valorBruto", this.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});

		$("#valorLiquido", this.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});
		
		$("#valorDesconto", this.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});
		
		$("#novoValorTotal", this.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});
		
		$("#itensRecebimento.qtdNotaItem", this.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});
		
		$("#itensRecebimento.qtdPacoteItem", this.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});
		
		$("#itensRecebimento.qtdExemplaresItem", this.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2
		});
		
		$("#itensRecebimento.codigoItem", this.workspace).numeric();
		$("#itensRecebimento.edicaoItem", this.workspace).numeric();
		$("#novoNumeroNota", this.workspace).numeric();
		$("#novoSerieNota", this.workspace).numeric();
		
		$("#produto", this.workspace).autocomplete({source: ""});
		
		this.inicializarGridPopUpNota();
		
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
			
		var cnpj = $("#cnpj", this.workspace).val();	
		
		if(cnpj == "") {
			$("#fornecedor", this.workspace).val("");
			return;
		}
		
		$.postJSON(this.path + 'buscaCnpj', "cnpj=" + cnpj, 
		function(result) { 
			$("#fornecedor", this.workspace).val(result.cnpj);
		});	
	
	},
	
	/**
	 * EXIBI O CNPJ DO FORNECEDOR SELECIONADO.
	 */
	exibirCnpjDoFornecedor : function() {
		
		var cnpjDoFornecedor = $("#fornecedor", this.workspace).val();	
		
		if(cnpjDoFornecedor == -1){
			
			$("#cnpj", this.workspace).val("");
			
			$("#cnpj", this.workspace).attr("disabled", true);
			
		}else{
			
			$("#cnpj", this.workspace).val(cnpjDoFornecedor);
			
			$("#cnpj", this.workspace).attr("disabled", false);
			
		}
	
	},
	
	
	/**
	 * VERIFICA A EXISTENCIA DE UMA NOTAFISCAL
	 * COM OS PARÂMETROS DE PESQUISA
	 */
	verificarNotaFiscalExistente : function() {

		var cnpj 		= $("#cnpj", this.workspace).val();
		var notaFiscal 	= $("#notaFiscal", this.workspace).val();
		var serie 		= $("#serie", this.workspace).val();		
		var chaveAcesso = $("#chaveAcesso", this.workspace).val();
		var fornecedor  = $("#fornecedor", this.workspace).val();
        var indNFe      = "N";
        
        if( $('#eNF', this.workspace).attr('checked') == 'checked' ) {
			indNFe = "S";
		}
        
		var dadosPesquisa = [
			{name: "cnpj" 				, value: cnpj			},
			{name: "numeroNotaFiscal" 	, value: notaFiscal 	},
			{name: "serie" 				, value: serie			},
			{name: "indNFe" 			, value: indNFe			},
			{name: "fornecedor" 		, value: fornecedor		},
			{name: "chaveAcesso" 		, value: chaveAcesso 	}
		];
		
		limparCampos();
		
		$.postJSON(	this.path +  'verificarNotaFiscalExistente', 
					dadosPesquisa,
					confirmaNotaFiscalEncontrada);
	
	},

	/**
	 * SE UMA NOTA FOR ENCONTRADA, SERAO PESQUISADOS OS ITEM RELATIVOS A MESMA
	 * E POPULADA A GRID, CASO CONTRARIO, SERA EXIBIDA POPUP PARA SE CADASTRAR UMA
	 * NOVA NOTA.
	 */
	confirmaNotaFiscalEncontrada : function(result) {
		
		var validacao = result.validacao;
		
		indNotaFiscalInterface = result.indNotaInterface;
		
		indRecebimentoFisicoConfirmado = result.indRecebimentoFisicoConfirmado;

		if(validacao.tipoMensagem == "SUCCESS") {
		
			if (indNotaFiscalInterface){
				$('#chBoxReplicaValorRepartePrevistoAll', this.workspace).attr('disabled', false);
	    		carregarItemNotaGridNotaInterface();
	    	}else{
				$('#chBoxReplicaValorRepartePrevistoAll', this.workspace).attr('disabled', true);
	    		carregarItemNotaGridNotaManual();
	    	}

			exibirMensagem(validacao.tipoMensagem, validacao.listaMensagens);
			
			pesquisarItemNotaGrid();

		} else {
			
			$(".grids").hide();
			
			popup_adicionar();
		
		}
		
	},
		
	/**
	 * APRESENTA O POPUP PARA CADASTRAR NOVO ITEM NOTA/RECEBIMENTO.
	 */
	popup_novo_item : function() {
		
		lineIdItemNotaEmEdicao = null;
		
		var fornecedor = $("#fornecedor", this.workspace).val();
		
		if(fornecedor == "-1"){
			exibirMensagem('WARNING', ['Não é possível Adicionar Novo Produto para opção TODOS em Fornecedor']);
			return;
		}
		
		limparCamposNovoItem();
		
		$("#dialog-novo-item", this.workspace).dialog({
			resizable: false,
			height:480,
			width:500,
			modal: true,
			buttons: {
				"Confirmar": cadastrarNovoItemNota,
				
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			beforeClose: function() {
				clearMessageDialogTimeout();
			}
		});
	},
	
	/**
	*INCLUIR NOVO ITEM DA NOTA
	*/
	incluirNovoItemNota : function(){
		
		var lineId = -1;
		
		if(	typeof lineIdItemNotaEmEdicao!='undefined' && 
			lineIdItemNotaEmEdicao!=null ) {
			
			lineId = lineIdItemNotaEmEdicao;
			
		} 
		
		var peso				= $("#peso", this.workspace).val();
		var pacotePadrao		= $("#pacotePadrao", this.workspace).val();
		var codigo 				= $("#codigo", this.workspace).val();
		var produto 			= $("#produto", this.workspace).val();
		var precoCapa			= $("#precoCapa", this.workspace).val();
		var edicao 				= $("#edicao", this.workspace).val();
		var dataLancamento 		= $("#datepickerLancto", this.workspace).val();
		var dataRecolhimento 	= $("#datepickerRecolhimento", this.workspace).val();
		var repartePrevisto 	= $("#repartePrevisto", this.workspace).val();
		var tipoLancamento 		= $("#tipoLancamento", this.workspace).val();
		
		var dadosCadastro = 
			
			"itemRecebimento.lineId="				+ lineId			+ "&" +
			"itemRecebimento.peso=" 				+ peso 				+ "&" +
			"itemRecebimento.pacotePadrao=" 		+ pacotePadrao 		+ "&" +
			"itemRecebimento.codigoProduto=" 		+ codigo			+ "&" +
			"itemRecebimento.nomeProduto=" 			+ produto			+ "&" +
			"itemRecebimento.precoCapa=" 			+ precoCapa			+ "&" +
			"numeroEdicao=" 						+ edicao			+ "&" +
			"dataLancamento=" 						+ dataLancamento 	+ "&" + 
		   	"dataRecolhimento=" 					+ dataRecolhimento	+ "&" +
		    "itemRecebimento.repartePrevisto=" 		+ repartePrevisto	+ "&" +
		    "itemRecebimento.tipoLancamento=" 		+ tipoLancamento;
		
		var listaDeValores = "";
		
		if (indNotaFiscalInterface) {
			
			listaDeValores  = obterListaValores();
		}
		
		$.postJSON(this.path + 'incluirItemNotaFiscal', (dadosCadastro +"&" + listaDeValores), 

		function(result) {
			
		refreshItemNotaGrid();
		
		limparCamposNovoItem();
				
		}, null, true);
		
		
	},
	
	/**
	 * CADASTRA NOVO ITEM DE NOTA.
	 */
	cadastrarNovoItemNota : function() {
		
		var lineId = -1;
		
		if(	typeof lineIdItemNotaEmEdicao!='undefined' && 
			lineIdItemNotaEmEdicao!=null ) {
			
			lineId = lineIdItemNotaEmEdicao;
			
		} 
		
		var peso				= $("#peso", this.workspace).val();
		var pacotePadrao		= $("#pacotePadrao", this.workspace).val();
		var codigo 				= $("#codigo", this.workspace).val();
		var produto 			= $("#produto", this.workspace).val();
		var precoCapa			= $("#precoCapa", this.workspace).val();
		var edicao 				= $("#edicao", this.workspace).val();
		var dataLancamento 		= $("#datepickerLancto", this.workspace).val();
		var dataRecolhimento 	= $("#datepickerRecolhimento", this.workspace).val();
		var repartePrevisto 	= $("#repartePrevisto", this.workspace).val();
		var tipoLancamento 		= $("#tipoLancamento", this.workspace).val();
		
		var dadosCadastro = 
			
			"itemRecebimento.lineId=" 				+ lineId 			+ "&" +
			"itemRecebimento.peso=" 				+ peso 				+ "&" +
			"itemRecebimento.pacotePadrao=" 		+ pacotePadrao 		+ "&" +
			"itemRecebimento.codigoProduto=" 		+ codigo			+ "&" +
			"itemRecebimento.nomeProduto=" 			+ produto			+ "&" +
			"itemRecebimento.precoCapa=" 			+ precoCapa			+ "&" +
			"numeroEdicao=" 						+ edicao			+ "&" +
			"dataLancamento=" 						+ dataLancamento 	+ "&" + 
		   	"dataRecolhimento=" 					+ dataRecolhimento	+ "&" +
		    "itemRecebimento.repartePrevisto=" 		+ repartePrevisto	+ "&" +
		    "itemRecebimento.tipoLancamento=" 		+ tipoLancamento;
		
		var listaDeValores = "";
		
		if (indNotaFiscalInterface) {
			
			listaDeValores  = obterListaValores();
		}
		
		$.postJSON(this.path + 'incluirItemNotaFiscal', (dadosCadastro +"&" + listaDeValores), 

		function(result) {
			
		if(result.tipoMensagem == "SUCCESS") {
				
			$("#dialog-novo-item", this.workspace).dialog( "close" );
				
		} 
		
		refreshItemNotaGrid();
				
		}, null, true);
		
		
		
	},
	
	/**
	 * CADASTRA NOVA NOTA.
	 */
	cadastrarNovaNota : function() {
		
		var cnpj 			= $("#cnpj", this.workspace).val();
		var notaFiscal 		= $("#notaFiscal", this.workspace).val();
		var serie 			= $("#serie", this.workspace).val();
		var chaveAcesso 	= $("#chaveAcesso", this.workspace).val();
		var dataEmissao 	= $("#dataEmissao", this.workspace).val();
		var dataEntrada 	= $("#dataEntrada", this.workspace).val();
		var valorBruto		= $("#valorBruto", this.workspace).val();
		var valorLiquido	= $("#valorLiquido", this.workspace).val();
		var valorDesconto 	= $("#valorDesconto", this.workspace).val();
		var cfopId 			= $("#cfop", this.workspace).val();
		var tipoNotaFiscal  = $("#tipoNotaFiscal", this.workspace).val();
		var fornecedor      = $("#fornecedor", this.workspace).val();
		
		var dadosCadastro = 

			"notaFiscalFornecedor.emitente.cnpj=" 		+ cnpj			+ "&" +
			"notaFiscalFornecedor.numero=" 				+ notaFiscal	+ "&" +
			"notaFiscalFornecedor.serie=" 				+ serie			+ "&" +
			"notaFiscalFornecedor.chaveAcesso=" 		+ chaveAcesso	+ "&" +
			"dataEmissao=" 								+ dataEmissao	+ "&" +
			"dataEntrada=" 								+ dataEntrada	+ "&" +
			"valorBruto=" 								+ valorBruto	+ "&" +
			"valorLiquido=" 							+ valorLiquido	+ "&" +
			"valorDesconto=" 							+ valorDesconto	+ "&" + 
			"fornecedor=" 							    + fornecedor	+ "&" + 
			"notaFiscalFornecedor.cfop.id=" 			+ cfopId		+ "&" +
			"notaFiscalFornecedor.tipoNotaFiscal.id="   + tipoNotaFiscal;
		
		$.postJSON(this.path + 'incluirNovaNotaFiscal', dadosCadastro, 
				function(result) {
			
			
					if(result.tipoMensagem == "SUCCESS") {
						
						$("#dialog-nova-nota", this.workspace).dialog( "close" );
						
					} 
					
					refreshItemNotaGrid();
				
		}, null, true);
		
		
	},
	
	/**
	* LIMPA OS CAMPOS DE PESQUISA DE NOTA FISCAL
	*/
	limparCamposPesquisa : function() {
		
		$("#cnpj", this.workspace).val("");
		$("#notaFiscal", this.workspace).val("");
		$("#serie", this.workspace).val("");
		$("#chaveAcesso", this.workspace).val("");
		
	},
	
	/**
	* LIMPA OS CAMPOS DO CADASTRO DE NOVA NOTA E NOVO ITEM.
	*/
	limparCampos : function() {
		
		$("#dataEmissao", this.workspace).val("");
		$("#valorBruto", this.workspace).val("");
		$("#valorLiquido", this.workspace).val("");
		$("#valorDesconto", this.workspace).val("");
		$("#cfop", this.workspace).val("");
		$("#tipoNotaFiscal", this.workspace).val("");
		
		$("#codigo", this.workspace).val("");
		$("#produto", this.workspace).val("");
		$("#precoCapa", this.workspace).val("");
		$("#edicao", this.workspace).val("");
		$("#datepickerLancto", this.workspace).val("");
		$("#datepickerRecolhimento", this.workspace).val("");
		$("#repartePrevisto", this.workspace).val("");
		$("#tipoLancamento", this.workspace).val("");		
	
	},

	/**
	* LIMPA OS CAMPOS DO CADASTRO DE NOVA NOTA E NOVO ITEM.
	*/
	limparCamposNovoItem : function() {
		
		$("#codigo", this.workspace).val("");
		$("#produto", this.workspace).val("");
		$("#precoCapa", this.workspace).val("");
		$("#edicao", this.workspace).val("");
		$("#datepickerLancto", this.workspace).val("");
		$("#datepickerRecolhimento", this.workspace).val("");
		$("#repartePrevisto", this.workspace).val("");
		$("#tipoLancamento", this.workspace).val("");
		$("#peso", this.workspace).val("");
		$("#pacotePadrao", this.workspace).val("");
	
	},
	
	/**
	 * EFEITO PARA ABERTURA POPUP. 
	 */
	callback : function() {
		setTimeout(function() {
			$( "#effect:visible", this.workspace).removeAttr("style" ).fadeOut();
	
		}, 1000 );
	},	
	
	
	/**
	 * APRESENTA O CAMPO DE CHAVE DA NFE.
	 */
	mostrar_nfes : function(){
		
		if( $("eNF", this.workspace).attr('checked') == 'checked' ){
			
			$(".nfes", this.workspace).show();
			
		}else{
			
			$(".nfes", this.workspace).hide();
			
			$("#chaveAcesso", this.workspace).val("");
			
		}

	},
	
	/**
	 * ESTRUTURA DE COLUNAS DA GRID DE RESULTADO.
	 */
	carregarItemNotaGridNotaInterface : function() {
						
		$(".gridWrapper", this.workspace).empty();
		
		$(".gridWrapper", this.workspace).append($("<table>").attr("class", "itemNotaGrid"));
		
		$(".itemNotaGrid", this.workspace).flexigrid({
			
				preProcess: getDataFromResultNotaInterface,
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
				width : 60,
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
			width : 960,
			height : 180
		});
	},
	
	/**
	 * ESTRUTURA DE COLUNAS DA GRID DE RESULTADO.
	 */
	carregarItemNotaGridNotaManual : function() {
				
		$(".gridWrapper", this.workspace).empty();
		
		$(".gridWrapper", this.workspace).append($("<table>").attr("class", "itemNotaGrid"));
		
		$(".itemNotaGrid", this.workspace).flexigrid({
			
				preProcess: getDataFromResultNotaManual,
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
    	
		$(".itemNotaGrid", this.workspace).flexOptions({
			url: contextPath + '/estoque/recebimentoFisico/obterListaItemRecebimentoFisico',
			dataType : 'json'
		});
	
		$(".itemNotaGrid", this.workspace).flexReload();
	
	},

    /**
     * REFRESH DOS ITENS REFERENTES A NOTA ENCONTRADA.
     */
	refreshItemNotaGrid : function(onSuccessFunction) {
	
		$(".itemNotaGrid", this.workspace).flexOptions({
			url: contextPath + '/estoque/recebimentoFisico/refreshListaItemRecebimentoFisico',
			dataType : 'json',
			onSuccess : onSuccessFunction	
		});
			
		$(".itemNotaGrid", this.workspace).flexReload();
	
	},

    /**
     * FAZ O CANCELAMENTO DE UMA NOTA FISCAL E SEU RECEBIMENTO FISICO.
     */
	cancelarNotaRecebimentoFisico : function() {
    	
		$.postJSON(this.path + 'cancelarNotaRecebimentoFisico', null, 

		function(result) {
					
	    	if(result.tipoMensagem == "SUCCESS") {
				
	    		$(".grids", this.workspace).hide();
	    		
	    		limparCamposPesquisa();
	        	
	        	limparCamposNovoItem();
	        	
	        	limparCampos();
	        	
	        	exibirMensagem(result.tipoMensagem, result.listaMensagens);
	        	
			} 
	    	
		});
    	
    	
	},
	
    /**
     * SALVA OS DADOS DOS ITENS DA NOTA EDITADOS.
     */
	salvarDadosItensDaNotaFiscal : function() {
		
		var listaDeValores  = "";
		
		if(indNotaFiscalInterface){
			listaDeValores = obterListaValores();
		}
		
		$.postJSON(this.path + 'salvarDadosItensDaNotaFiscal', listaDeValores, 
		function(result) {
			
			exibirMensagem(result.tipoMensagem, result.listaMensagens);
			
			pesquisarItemNotaGrid();
		
		});
		
	},

    /**
     * COFIRMA OS DADOS DOS ITENS DA NOTA EDITADOS.
     */
	confirmarRecebimentoFisico : function() {
		
		var listaDeValores  = "";
		
		if(indNotaFiscalInterface){
			listaDeValores = obterListaValores();
		}
		
		$.postJSON(this.path + 'confirmarRecebimentoFisico', listaDeValores, 
		function(result) {
			exibirMensagem(result.tipoMensagem, result.listaMensagens);
			
			pesquisarItemNotaGrid();
		
		});
		
	},
    
    /**
     * OBTEM OS VALORES DA GRID A SEREM ENVIADOS AO SERVIDOR VIA AJAX
     */
	obterListaValores : function() {
		
		var linhasDaGrid = $(".itemNotaGrid tr", this.workspace);
		
		var listaDeValores = "";
		
		$.each(linhasDaGrid, function(index, value) {
			
			var qtdPacote 	= $(value).find('input[name="qtdPacote"]').val();
			
			var qtdExemplar = $(value).find('input[name="qtdExemplar"]').val();
			
			var lineId 		= $(value).find('input[name="lineId"]').val();
			
			var itemRecebimento_lineId 		= 'itensRecebimento['+index+'].lineId='+lineId+'&';
			
			var itemRecebimento_qtdPacote 	= 'itensRecebimento['+index+'].qtdPacote='+qtdPacote+'&';
			
			var itemRecebimento_qtdExemplar = 'itensRecebimento['+index+'].qtdExemplar='+qtdExemplar+'&';
			
			listaDeValores = (listaDeValores + itemRecebimento_lineId + itemRecebimento_qtdPacote + itemRecebimento_qtdExemplar);
			
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

			$("#qtdPacote_"+lineId, this.workspace).val(resultado.qtdPacote);
			
			$("#qtdExemplar_"+lineId, this.workspace).val(resultado.qtdExemplar);
		
		});
		
	},
	
	replicarTodosValoresRepartePrevisto : function(elementoCheckBox) {
	
		var selecionado = false;
		
		if( $(elementoCheckBox).attr('checked') == 'checked' ) {
			selecionado = true;
		}
		
		if(selecionado == false) {
			
			var linhasDaGrid = $(".itemNotaGrid tr", this.workspace);
			
			$.each(linhasDaGrid, function(index, value) {
				$(value).find('input[name="replicaQtde"]').attr('checked', false);
			});
			
			return;
			
		}
		
		$.postJSON(this.path + 'replicarTodosValoresRepartePrevisto', null, 
				
		function(result) {

			refreshItemNotaGrid(function(){

				var linhasDaGrid = $(".itemNotaGrid tr", this.workspace);
				
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
			var dadosExclusao = "lineId=" + lineId;
			
			var listaDeValores  = obterListaValores();
			
			$.postJSON(this.path + 'excluirItemNotaFiscal', (dadosExclusao + "&" + listaDeValores), 
			
			function(result) {
				
				exibirMensagem(result.tipoMensagem, result.listaMensagens);
				
				refreshItemNotaGrid();
			
			});
		}else{
			return;
		}		
	},
	

	/**
	 * EDITA UM ITEM DA NOTA
	 */
	editarItemNotaFiscal : function(lineId) {
		
		lineIdItemNotaEmEdicao = lineId;
		
		limparCamposNovoItem();
		
		var parametroPesquisa = [{name :"lineId", value : lineId}];
		
		$.postJSON(this.path + 'obterRecebimentoFisicoVO', parametroPesquisa, 

		function(resultado) {

			$("#pacotePadrao", this.workspace).val(resultado.pacotePadrao);
			$("#peso", this.workspace).val(resultado.peso);
			$("#codigo", this.workspace).val(resultado.codigo);
			$("#produto", this.workspace).val(resultado.nomeProduto);
			$("#precoCapa", this.workspace).val(resultado.precoCapa);
			$("#edicao", this.workspace).val(resultado.edicao);
			$("#datepickerLancto", this.workspace).val(resultado.dataLancamento);
			$("#datepickerRecolhimento", this.workspace).val(resultado.dataRecolhimento);
			$("#repartePrevisto", this.workspace).val(resultado.repartePrevisto);
			$("#tipoLancamento", this.workspace).val(resultado.tipoLancamento);


			$("#dialog-novo-item", this.workspace).dialog({
				resizable: false,
				height:480,
				width:500,
				modal: true,
				buttons: {
					"Confirmar": cadastrarNovoItemNota,
					
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				beforeClose: function() {
					clearMessageDialogTimeout();
				}
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
			
			if(edicaoItemNotaPermitida == "S") {
				
				value.cell.acao = '<a href="javascript:;" onclick="excluirItemNotaFiscal('+[lineId]+');">' + imgExclusao + '</a>' + 
				'<a href="javascript:;" onclick="editarItemNotaFiscal('+[lineId]+');">' + imgEdicao + '</a>';
				
			} else{
				
				value.cell.acao = '<a href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)"  >' + imgExclusao + '</a>' + 
							 '<a href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)"  >' + imgEdicao   + '</a>';
				
			} 
			
			
		});
		
		if(data.rows) {

			$(".grids", this.workspace).show();
			
		}
		
		if(!indRecebimentoFisicoConfirmado){
			
			$("#botoesNormais", this.workspace).show();
			$("#botoesOpacos", this.workspace).hide();
			$("#botaoNovoProdutoOpaco", this.workspace).hide();
			$("#botaoAdicionarOpaco", this.workspace).hide();
			$("#botaoNovoProduto", this.workspace).show();
			
		}else{

			$("#botoesOpacos", this.workspace).show();
			$("#botoesNormais", this.workspace).hide();
			$("#botaoNovoProdutoOpaco", this.workspace).show();
			$("#botaoAdicionarOpaco", this.workspace).show();
			$("#botaoNovoProduto", this.workspace).hide();

		}
		
		return data;

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
			
			var lineId = value.cell.lineId;
			
			var hiddenFields = '<input type="hidden" name="lineId" value="'+lineId+'"/>';
			
			var imgExclusao = '<img src="'+contextPath+'/images/ico_excluir.gif" width="15" height="15" alt="Salvar" hspace="5" border="0" />'; 
			
			var imgEdicao = '<img src="'+contextPath+'/images/ico_editar.gif" width="15" height="15" alt="Salvar" hspace="5" border="0" />'; 
			
			
			if(destacarValorNegativo == "S") {
				value.cell.diferenca = '<span style="color: red">'+diferenca+'</span>';
			} else {
				value.cell.diferenca = '<span style="color: black">'+diferenca+'</span>';
			}
			
			
			if(edicaoItemRecFisicoPermitida == "S") {
				value.cell.qtdPacote 	=  '<input name="qtdPacote" id="qtdPacote_'+ lineId +'" style="width: 45px;" type="text" value="'+qtdPacote+'"/>'+hiddenFields;
				value.cell.qtdExemplar = '<input name="qtdExemplar" id="qtdExemplar_'+ lineId +'" style="width: 45px;" type="text" value="'+qtdExemplar+'"/>';
			} else {
				value.cell.qtdPacote 	= '<input name="qtdPacote" disabled="disabled" style="width: 45px;" type="text" value="'+qtdPacote+'"/>'+hiddenFields;
				value.cell.qtdExemplar 	=  '<input name="qtdExemplar" disabled="disabled" style="width: 45px;" type="text" value="'+qtdExemplar+'"/>';
			}
			
			if(edicaoItemNotaPermitida == "S") {
				
				value.cell.acao = '<a href="javascript:;" onclick="excluirItemNotaFiscal('+[lineId]+');">' + imgExclusao + '</a>' + 
				'<a href="javascript:;" onclick="editarItemNotaFiscal('+[lineId]+');">' + imgEdicao + '</a>';
				
			} else{
				
				value.cell.acao = '<a href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)"  >' + imgExclusao + '</a>' + 
							 '<a href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)"  >' + imgEdicao   + '</a>';
			}
			
			if(edicaoItemRecFisicoPermitida == "S") {
				value.cell.replicaQtd = '<input title="Replicar Item" onclick="replicarValorRepartePrevisto('+
										[lineId] + ', this);" type="checkbox" id="replicaValorRepartePrevisto_'+lineId+'" name="replicaQtde" />';
			} else {
				value.cell.replicaQtd = '<input title="Replicar Item" disabled="disabled" type="checkbox"/>';
			}
			
		});
		
		$(".grids", this.workspace).show();
		
		if(!indRecebimentoFisicoConfirmado){
			
			$('#botoesNormais', this.workspace).show();
			
			$('#botaoNovoProdutoOpaco', this.workspace).hide();
			
			$('#botaoNovoProduto', this.workspace).show();
			
			$('#botaoAdicionarOpaco', this.workspace).hide();
			
			$('#botaoAdicionar', this.workspace).show();
			
			$('#botoesOpacos', this.workspace).hide();
			
			
		} else {
			
			$('#botoesOpacos', this.workspace).show();
			
			$('#botoesNormais', this.workspace).hide();
			
			$('#botaoNovoProdutoOpaco', this.workspace).show();
			
			$('#botaoNovoProduto', this.workspace).hide();
			
			$('#botaoAdicionarOpaco', this.workspace).show();
			
			$('#botaoAdicionar', this.workspace).hide();
			
		}	
		
		
		return data;

	},
	
	//------------------------- NOVO POPUP DE NOTA ---------------------------
	
	
	/*NOVO POPUP DE LANÇAMENTO DE NOTA*/
	inicializarGridPopUpNota : function() {
	
		$(".novoItemNotaGrid", this.workspace).flexigrid({
			preProcess: getDataFromResultItem,
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
			width : 910,
			height : 180
		});

		
	},
	
	popup_adicionar : function() {
		
		montaGridItens();
		
		$( "#dialog-adicionar", this.workspace ).dialog({
			resizable: false,
			height:530,
			width:958,
			modal: true,
			buttons:[ 
			          {
				           id:"bt_confirmar",
				           text:"Confirmar", 
				           click: function() {
				        	   incluirNota();
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
	        }
		});
	},

	pesquisarFornecedorCnpj : function() {
		
		var pCnpj = $("#novoCnpj", this.workspace).val();	

		if(pCnpj == "") {
			$("#novoFornecedor", this.workspace).val("");
			return;
		}
		
		$.postJSON(this.path + 'buscaCnpj', "cnpj=" + pCnpj, 
		function(result) { 
			$("#novoFornecedor", this.workspace).val(result.cnpj);
		});	
	},

	pesquisarCnpjFornecedor : function() {
	
		var idFornecedor = $("#novoFornecedor", this.workspace).val();
		
		if(idFornecedor == -1){

			$("#novoCnpj", this.workspace).val("");
			
			$("#novoCnpj", this.workspace).disabled(true);
			
		}else{
			$.postJSON(this.path + 'obterCnpjFornecedor', "idFornecedor=" + idFornecedor, 
			
			function(result) {
				
				$("#novoCnpj", this.workspace).val(result);
				
				$("#novoCnpj", this.workspace).attr('disabled', true);
				
			},
			null,
			true);
		}
	},
	
	obterDadosEdicao : function(index) {
		
		var codigo = $("#codigoItem"+index, this.workspace).val();
		
		var edicao = $("#edicaoItem"+index, this.workspace).val();	

		if((codigo == "")||(edicao == "")) {
			$("#precoDescontoItem"+index, this.workspace).val("");
			return;
		}
		
		$.postJSON(this.path + 'obterDadosEdicao', "codigo=" + codigo + "&edicao="+edicao, 
			function(result) { 
				$("#precoDescontoItem"+index, this.workspace).val(result.precoDesconto);
			},
			function(result) {
				$("#precoDescontoItem"+index, this.workspace).val("");	   
			},
			true
		);	
	},
	
	calcularDiferencaEValorItem : function(index){
		
		var preco = removeMascaraPriceFormat($("#precoDescontoItem"+index, this.workspace).val());
		var quantidade = removeMascaraPriceFormat($("#qtdNotaItem"+index, this.workspace).val());
		var quantidadeExemp = removeMascaraPriceFormat($("#qtdExemplaresItem"+index, this.workspace).val());
		
		if((preco == "")||(quantidade == "")) {
			$("#valorItem"+index, this.workspace).val("");
			return;
		}
		
		if((quantidade == "")||(quantidadeExemp == "")) {
			$("#diferencaItem"+index, this.workspace).val("");
		}
		
        var valor = intValue(preco) * intValue(quantidade);
        
        $("#valorItem"+index, this.workspace).val(valor);
        
        $("#valorItem"+index, this.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: '.',
		    thousandsSeparator: ''
		});

        var diferenca = 0;
        if (quantidade > quantidadeExemp){
        	diferenca = intValue(quantidade) - intValue(quantidadeExemp);
        }
        else{
        	diferenca = intValue(quantidadeExemp) - intValue(quantidade);
        }
        
		$("#diferencaItem"+index, this.workspace).val(diferenca);
		
	    $("#diferencaItem"+index, this.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: '',
		    thousandsSeparator: '.'
		});
	    
	    obterValorTotalItens();
	},
	
	obterCabecalho : function() {

		var dadosCabecalho = "";
		
		var fornecedor 	= $("#novoFornecedor"	, this.workspace).val();
		var cnpj 		= $("#novoCnpj"			, this.workspace).val();
		var numeroNota 	= $("#novoNumeroNota"	, this.workspace).val();
        var serieNota 	= $("#novoSerieNota"	, this.workspace).val();
        var checkNfe 	= $("#novoNfe"			, this.workspace).val();
        var chaveAcesso = $("#novoChaveAcesso"	, this.workspace).val();
        var dataEmissao = $("#novoDataEmissao"	, this.workspace).val();
        var dataEntrada = $("#novoDataEntrada"	, this.workspace).val();
        var valorTotal 	= $("#novoValorTotal"	, this.workspace).val();
		
		var cabecalho = 'nota.fornecedor=' + fornecedor + '&';

		cabecalho += 'nota.cnpj=' + cnpj + '&';

		cabecalho += 'nota.numero=' + numeroNota + '&';
		
		cabecalho += 'nota.serie=' + serieNota + '&';

		cabecalho += 'nota.chaveAcesso=' + chaveAcesso + '&';

		cabecalho += 'nota.dataEmissao=' + dataEmissao + '&';
		
		cabecalho += 'nota.dataEntrada=' + dataEntrada + '&';

		cabecalho += 'nota.valorTotal=' + valorTotal + '&';

		dadosCabecalho = (dadosCabecalho + cabecalho);

		return dadosCabecalho;
	},

    montaGridItens : function() {
		$(".novoItemNotaGrid", this.workspace).flexOptions({url: contextPath + '/estoque/recebimentoFisico/montaGridItemNota'});
		$(".novoItemNotaGrid", this.workspace).flexReload();
	},	
	
	getDataFromResultItem : function(resultado) {

		if (resultado.mensagens) {
			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			$(".grids", this.workspace).hide();
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
			 
			 var valueQtdExemplares='';
			 if (row.cell.qtdExemplares!=null){
				 valueQtdExemplares = row.cell.qtdExemplares;
			 }
			 
			 var valueDiferenca='';
			 if (row.cell.diferenca!=null){
				 valueDiferenca = row.cell.diferenca;
			 }
			 
			 var valueValor='';
			 if (row.cell.valorTotal!=null){
				 valueValor = row.cell.valorTotal;
			 }

        	 var codigo =       '<input maxlength="28" value="'+valueCodigo+'" type="text" name="itensRecebimento.codigoItem" id="codigoItem'+ index +'" style="width: 50px;" onchange="produto.pesquisarPorCodigoProduto(\'#codigoItem'+ index +'\', \'#produtoItem'+ index +'\', \'#edicaoItem'+ index +'\', true, null);" ></input>';
	         			 					     
	         var produto =      '<input maxlength="200" value="'+valueProduto+'" type="text" name="itensRecebimento.produtoItem" id="produtoItem'+ index +'" style="width: 140px;" onkeyup="produto.autoCompletarPorNomeProduto(\'#produtoItem'+ index +'\', false);" onblur="produto.pesquisarPorNomeProduto(\'#codigoItem'+ index +'\', \'#produtoItem'+ index +'\', \'#edicaoItem'+ index +'\', true, null);"></input>';
				             
			 var edicao =       '<input maxlength="18" value="'+valueEdicao+'" type="text" name="itensRecebimento.edicaoItem" id="edicaoItem'+ index +'" style="width: 30px;" onkeyup="obterDadosEdicao('+index+');"></input>';         
			
			 var precoDesconto ='<input maxlength="17" value="'+valuePrecoDesconto+'" type="text" readonly="readonly" name="itensRecebimento.precoDescontoItem" id="precoDescontoItem'+ index +'" style="width: 80px; border: 0px; background-color: inherit;"></input>';
			 
			 var qtdNota =      '<input maxlength="17" value="'+valueQtdNota+'" type="text" name="itensRecebimento.qtdNotaItem" id="qtdNotaItem'+ index +'" style="width: 70px;" onchange="replicarQuantidadeItem('+index+'); calcularDiferencaEValorItem('+index+');"></input>';
			     
	         var qtdPacote =    '<input maxlength="17" value="'+valueQtdPacote+'" type="text" name="itensRecebimento.qtdPacoteItem" id="qtdPacoteItem'+ index +'" style="width: 70px;" onchange="calcularDiferencaEValorItem('+index+');"></input>';
				             
			 var qtdExemplares ='<input maxlength="17" value="'+valueQtdExemplares+'" type="text" name="itensRecebimento.qtdExemplaresItem" id="qtdExemplaresItem'+ index +'" style="width: 70px;" onchange="calcularDiferencaEValorItem('+index+');"></input>'; 
				
			 var diferenca =    '<input maxlength="17" value="'+valueDiferenca+'" type="text" readonly="readonly" name="itensRecebimento.diferencaItem" id="diferencaItem'+ index +'" style="width: 70px; border: 0px; background-color: inherit;"></input>';
				 
			 var valor =        '<input maxlength="17" value="'+valueValor+'" type="text" readonly="readonly" name="itensRecebimento.valorItem" id="valorItem'+ index +'" style="width: 70px; border: 0px; background-color: inherit;"></input>';
						 
			 var checkBox =     '<input title="Replicar Item" type="checkbox" name="checkboxGrid" id="checkbox'+ index +'"/>';

		     row.cell[0] = codigo;
		     row.cell[1] = produto;
		     row.cell[2] = edicao;
		     row.cell[3] = precoDesconto;
		     row.cell[4] = qtdNota;
		     row.cell[5] = qtdPacote;
		     row.cell[6] = qtdExemplares;
		     row.cell[7] = diferenca;
		     row.cell[8] = valor;
		     row.cell[9] = checkBox;
         }
	    
		);

		return resultado;
	},
	
	isAtributosLancamentoVazios : function(codigo, produto, edicao, precoDesconto, qtdNota, qtdPacote, qtdExemplares) {

		if (!$.trim(codigo) 
				&& !$.trim(produto)
				&& !$.trim(edicao) 
				&& !$.trim(precoDesconto)
				&& !$.trim(qtdNota)
				&& !$.trim(qtdPacote)
				&& !$.trim(qtdExemplares)) {

			return true;
		}
	},
	
	obterListaItens : function() {

		var linhasDaGrid = $(".novoItemNotaGrid tr", this.workspace);

		var listaItens = "";

		$.each(linhasDaGrid, function(index, value) {

			var linha = $(value);
			
			var colunaCodigo = linha.find("td")[0];
			var colunaProduto = linha.find("td")[1];
			var colunaEdicao = linha.find("td")[2];
			var colunaPrecoDesconto = linha.find("td")[3];
			var colunaQtdNota = linha.find("td")[4];
			var colunaQtdPacote = linha.find("td")[5];
			var colunaQtdExemplares = linha.find("td")[6];
			var colunaDiferenca = linha.find("td")[7];
			var colunaValor = linha.find("td")[8];
			var colunaCheck = linha.find("td")[9];
			
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
			
			var qtdExemplares =
				$(colunaQtdExemplares).find("div").find('input[name="itensRecebimento.qtdExemplaresItem"]').val();
			
			var diferenca =
				$(colunaDiferenca).find("div").find('input[name="itensRecebimento.diferencaItem"]').val();
			
			var valor =
				$(colunaValor).find("div").find('input[name="itensRecebimento.valorItem"]').val();

			if (!isAtributosLancamentoVazios(codigo, produto, edicao, precoDesconto, qtdNota, qtdPacote, qtdExemplares)) {

	            var dataLancamento = $("#novoDataEntrada", this.workspace).val();
				
			    var dataRecolhimento = $("#novoDataEmissao", this.workspace).val();
	
				var lancamento = 'itens[' + index + '].codigoProduto=' + codigo + '&';
	
				lancamento += 'itens[' + index + '].nomeProduto=' + produto + '&';
	
				lancamento += 'itens[' + index + '].edicao=' + edicao + '&';
				
				lancamento += 'itens[' + index + '].precoDesconto=' + precoDesconto + '&';
				
				lancamento += 'itens[' + index + '].repartePrevisto=' + qtdNota + '&';
	
				lancamento += 'itens[' + index + '].qtdFisico=' + qtdNota + '&';
				
				lancamento += 'itens[' + index + '].qtdPacote=' + qtdPacote + '&';
				
				lancamento += 'itens[' + index + '].qtdExemplares=' + qtdExemplares + '&';
				
				lancamento += 'itens[' + index + '].diferenca=' + diferenca + '&';
				
				lancamento += 'itens[' + index + '].valorTotal=' + valor + '&';
	
				lancamento += 'itens[' + index + '].dataLancamento=' + dataLancamento + '&';
				
				lancamento += 'itens[' + index + '].dataRecolhimento=' + dataRecolhimento + '&';
				
				listaItens = (listaItens + lancamento);
			
			}

		});

		return listaItens;
	},

	obterValorTotalItens : function() {

		var linhasDaGrid = $(".novoItemNotaGrid tr", this.workspace);

		var listaItens = "";
		
		var valorTotal = 0;

		$.each(linhasDaGrid, function(index, value) {

			var linha = $(value);

			var colunaValor = linha.find("td")[8];

			valorTotal += intValue(removeMascaraPriceFormat($(colunaValor).find("div").find('input[name="itensRecebimento.valorItem"]').val()));
			
		});

        $("#novoValorTotal", this.workspace).val(valorTotal);
        
        $("#novoValorTotal", this.workspace).priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.'
		});
        
        $("#labelValorTotal", this.workspace).html($("#novoValorTotal").val());
	},
    
    replicarQuantidadeItem : function(index){
    	
    		
    	
    	if ( $("#checkbox"+index, this.workspace).attr('checked') == 'checked' || 
    		 $("#novoReplicarQtde", this.workspace).attr('checked') == 'checked' ) {
    		
    	    $("#qtdExemplaresItem"+index, this.workspace).val($("#qtdNotaItem"+index, this.workspace).val());
    	    
    	}    
    },
    
    //PREPARA NOVA LINHA DA GRID
    adicionarNovaLinha : function(index){

    	var row;

    	var codigo =       '<input maxlength="28" type="number" name="itensRecebimento.codigoItem" id="codigoItem'+ index +'" style="width: 50px;" onchange="produto.pesquisarPorCodigoProduto(\'#codigoItem'+ index +'\', \'#produtoItem'+ index +'\', \'#edicaoItem'+ index +'\', true, null);" ></input>';
	     
        var produto =      '<input maxlength="200" type="text" name="itensRecebimento.produtoItem" id="produtoItem'+ index +'" style="width: 140px;" onkeyup="produto.autoCompletarPorNomeProduto(\'#produtoItem'+ index +'\', false);" onblur="produto.pesquisarPorNomeProduto(\'#codigoItem'+ index +'\', \'#produtoItem'+ index +'\', \'#edicaoItem'+ index +'\', true, null);"></input>';
			             
		var edicao =       '<input maxlength="18" type="number" name="itensRecebimento.edicaoItem" id="edicaoItem'+ index +'" style="width: 30px;" onkeyup="obterDadosEdicao('+index+');"></input>';         
		
		var precoDesconto ='<input maxlength="17" type="number" readonly="readonly" name="itensRecebimento.precoDescontoItem" id="precoDescontoItem'+ index +'" style="width: 80px; border: 0px; background-color: inherit;"></input>';
		 
		var qtdNota =      '<input maxlength="17" type="number" name="itensRecebimento.qtdNotaItem" id="qtdNotaItem'+ index +'" style="width: 70px;" onchange="replicarQuantidadeItem('+index+'); calcularDiferencaEValorItem('+index+');"></input>';
		     
        var qtdPacote =    '<input maxlength="17" type="number" name="itensRecebimento.qtdPacoteItem" id="qtdPacoteItem'+ index +'" style="width: 70px;" onchange="calcularDiferencaEValorItem('+index+');"></input>';
			             
		var qtdExemplares ='<input maxlength="17" type="number" name="itensRecebimento.qtdExemplaresItem" id="qtdExemplaresItem'+ index +'" style="width: 70px;" onchange="calcularDiferencaEValorItem('+index+');"></input>'; 
			
		var diferenca =    '<input maxlength="17" type="number" readonly="readonly" name="itensRecebimento.diferencaItem" id="diferencaItem'+ index +'" style="width: 70px; border: 0px; background-color: inherit;"></input>';
			 
		var valor =        '<input maxlength="17" type="number" readonly="readonly" name="itensRecebimento.valorItem" id="valorItem'+ index +'" style="width: 70px; border: 0px; background-color: inherit;"></input>';
					 
		var checkBox =     '<input title="Replicar Item" type="checkbox" name="checkboxGrid" id="checkbox'+ index +'"/>';

    	var row = [{name: 'codigo', value: codigo},
   	               {name: 'produto', value: produto},
   	               {name: 'edicao', value: edicao},
   	               {name: 'precoDesconto', value: precoDesconto},
   	          	   {name: 'qtdeNota', value: qtdNota},
   	               {name: 'qtdePcts', value: qtdPacote},
   	               {name: 'qtdeExemplar', value: qtdExemplares},
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
    	var linhasDaGrid = $(".novoItemNotaGrid tr", this.workspace);
    	
    	$.each(linhasDaGrid, function(index, value) {
    		
    		nLinhas++;
    		
    		var linha = $(value);
    		var colunaCodigo = linha.find("td")[0];
			var colunaProduto = linha.find("td")[1];
			var colunaEdicao = linha.find("td")[2];
			var colunaPrecoDesconto = linha.find("td")[3];
			var colunaQtdNota = linha.find("td")[4];
			var colunaQtdPacote = linha.find("td")[5];
			var colunaQtdExemplares = linha.find("td")[6];
			var colunaDiferenca = linha.find("td")[7];
			var colunaValor = linha.find("td")[8];
			var colunaCheck = linha.find("td")[9];
			
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
			
			var valueQtdExemplares =
				$(colunaQtdExemplares).find("div").find('input[name="itensRecebimento.qtdExemplaresItem"]').val();
			
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
		   	              {name: 'qtdeExemplar', value: valueQtdExemplares},
		   	              {name: 'diferenca', value: valueDiferenca},
		   	              {name: 'valor', value: valueValor},
		   	              {name: 'replicar', value: 0}];
			
		    dataValores.push({id:index, cell: rowValores});
			
		    //MANTEM LINHAS ATUAIS
			if (!isAtributosLancamentoVazios(valueCodigo, valueProduto, valueEdicao, valuePrecoDesconto, valueQtdNota, valueQtdPacote, valueQtdExemplares)) {
		        data.push({id:nLinhas, cell: adicionarNovaLinha(index)});
		    }
			 
		});
    	
    	//CRIA NOVA LINHA
    	data.push({id:nLinhas+1, cell: adicionarNovaLinha(nLinhas)});

    	$(".novoItemNotaGrid", this.workspace).flexAddData({
            rows : toFlexiGridObject(data),
            page : 1,
            total : nLinhas+1
        });

    	//RETORNA OS DADOS DIGITADOS PARA AS LINHAS ATUAIS
    	recuperaValoresDigitados(dataValores);
	},
    
    //RECUPERA VALORES DIGITADOS ANTES DA INSERÇÃO DA NOVA LINHA
    recuperaValoresDigitados : function(dataValores) {
    	
    	var linhasDaGrid = $(".novoItemNotaGrid tr", this.workspace);
        
    	$.each(linhasDaGrid, function(index, value) {
        	
        	if (index < dataValores.length){

	    		var linha = $(value);
	    		var colunaCodigo = linha.find("td")[0];
				var colunaProduto = linha.find("td")[1];
				var colunaEdicao = linha.find("td")[2];
				var colunaPrecoDesconto = linha.find("td")[3];
				var colunaQtdNota = linha.find("td")[4];
				var colunaQtdPacote = linha.find("td")[5];
				var colunaQtdExemplares = linha.find("td")[6];
				var colunaDiferenca = linha.find("td")[7];
				var colunaValor = linha.find("td")[8];
				var colunaCheck = linha.find("td")[9];
				
				$(colunaCodigo).find("div").find('input[name="itensRecebimento.codigoItem"]').val(dataValores[index].cell[0].value);
				$(colunaProduto).find("div").find('input[name="itensRecebimento.produtoItem"]').val(dataValores[index].cell[1].value);
				$(colunaEdicao).find("div").find('input[name="itensRecebimento.edicaoItem"]').val(dataValores[index].cell[2].value);
				$(colunaPrecoDesconto).find("div").find('input[name="itensRecebimento.precoDescontoItem"]').val(dataValores[index].cell[3].value);
				$(colunaQtdNota).find("div").find('input[name="itensRecebimento.qtdNotaItem"]').val(dataValores[index].cell[4].value);
				$(colunaQtdPacote).find("div").find('input[name="itensRecebimento.qtdPacoteItem"]').val(dataValores[index].cell[5].value);
				$(colunaQtdExemplares).find("div").find('input[name="itensRecebimento.qtdExemplaresItem"]').val(dataValores[index].cell[6].value);
				$(colunaDiferenca).find("div").find('input[name="itensRecebimento.diferencaItem"]').val(dataValores[index].cell[7].value);
				$(colunaValor).find("div").find('input[name="itensRecebimento.valorItem"]').val(dataValores[index].cell[8].value);
	        	
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
		
		$("#novoFornecedor", 	this.workspace).val(-1);
		$("#novoCnpj", 			this.workspace).val("");
		$("#novoNumeroNota", 	this.workspace).val("");
        $("#novoSerieNota", 	this.workspace).val("");
        $("#novoNfe", 			this.workspace).val("");
        $("#novoChaveAcesso", 	this.workspace).val("");
        $("#novoDataEmissao", 	this.workspace).val("");
        $("#novoDataEntrada", 	this.workspace).val("");
        $("#novoValorTotal", 	this.workspace).val("");
        
        montaGridItens();
	},
	
	incluirNota : function() {

		var url;
		var formData;

		formData = obterCabecalho() + obterListaItens();
		url = this.path + 'incluirNota';

		$.postJSON(
			url,
			formData,
			function(result) {
				
				limparCamposNovaNota();
				
				$("#dialog-adicionar", this.workspace).dialog( "close" );
	
				exibirMensagem(
					result.tipoMensagem, 
					result.listaMensagens
				);  
			},
			null,
			true
		);
	},
	
	jsDadosProduto : {
		
		exibirDetalhesProdutoEdicao : function() {
			
			var data = "codigo=" + $("#codigo", this.workspace).val() + "&edicao=" + $("#edicao", this.workspace).val();
			
			$.postJSON( this.path + 'obterProdutoEdicao', data, function(result){
				
				if(typeof result != "undefined") {
					
					$("#precoCapa", this.workspace).val(result.precoVenda);			
					$("#peso", this.workspace).val(result.peso);			
					$("#pacotePadrao", this.workspace).val(result.pacotePadrao);			
					
				}
				
			});
			
		},

		pesquisarProdutoPorCodigo : function() {

			$("#precoCapa", this.workspace).val("");
			$("#peso", this.workspace).val("");
			$("#pacotePadrao", this.workspace).val("");
			
			produto.pesquisarPorCodigoProduto('#codigo', '#produto', '#edicao', true, function(){});	
		},

		pesquisarProdutoPorNome : function() {

			$("#precoCapa", 	this.workspace).val("");
			$("#peso", 			this.workspace).val("");
			$("#pacotePadrao", 	this.workspace).val("");
			
			produto.pesquisarPorNomeProduto('#codigo', '#produto', '#edicao', true, function(){});
		},

		validarNumeroEdicao : function() {

			$("#precoCapa", 	this.workspace).val("");
			$("#peso", 			this.workspace).val("");
			$("#pacotePadrao", 	this.workspace).val("");
			
			produto.validarNumEdicao('#codigo', '#edicao', true, jsDadosProduto.validarEdicaoCallBack);
		},

		validarEdicaoCallBack : function() {
			
			jsDadosProduto.exibirDetalhesProdutoEdicao();
				
		}
		
	}
	
}, BaseController);
	