var recebimentoFisicoController = $.extend(true, {
	
	path: contextPath + '/estoque/recebimentoFisico/',
	
	novoValorTotalTyped: false, 
	
	linhasDestacadas : [],
	
	id: null,
	
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
		
		$("#novoValorTotal", recebimentoFisicoController.workspace).keyup(function(e){
			if((e.keyCode || e.which) != 9) { // 9: tecla tab
				_this.novoValorTotalTyped = true;
			}
		});
		
		$("#novoValorTotal", recebimentoFisicoController.workspace).maskMoney({
			 thousands:'.', 
			 decimal:',', 
			 precision:2,
			 allowZero: true
		});
			
		$("#novoNumeroNota", recebimentoFisicoController.workspace).numeric();
		
		$("#codigo", recebimentoFisicoController.workspace).numeric();
		$("#edicao", recebimentoFisicoController.workspace).numeric();
		$("#recebimento-fisico-precoCapa", recebimentoFisicoController.workspace).numeric();
		$("#peso", recebimentoFisicoController.workspace).numeric();
		$("#recebimento-fisico-pacotePadrao", recebimentoFisicoController.workspace).numeric();
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
			{name: "chaveAcesso" 		, value: chaveAcesso 		}
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
		
		recebimentoFisicoController.id = result.id;
		
		recebimentoFisicoController.indNotaFiscalInterface = result.indNotaInterface;
		
		recebimentoFisicoController.indRecebimentoFisicoConfirmado = result.indRecebimentoFisicoConfirmado;

		if(validacao.tipoMensagem == "SUCCESS") {
		
			recebimentoFisicoController.carregarItemNotaGridNota();
			
			if (recebimentoFisicoController.indNotaFiscalInterface){
				
				$('#chBoxReplicaValorRepartePrevistoAll', recebimentoFisicoController.workspace).disable();
				$(".itemNotaGrid", recebimentoFisicoController.workspace).flexToggleCol(12,false);
				$(".itemNotaGrid", recebimentoFisicoController.workspace).flexToggleCol(13,false);
	    	}else{
	    		
				$('#chBoxReplicaValorRepartePrevistoAll', recebimentoFisicoController.workspace).enable();
				$(".itemNotaGrid", recebimentoFisicoController.workspace).flexToggleCol(12,true);
				
				$(".itemNotaGrid", recebimentoFisicoController.workspace).flexToggleCol(13,
					$("#permissaoGridColRepartePrevisto").val() == "true");
	    	}
			
			if(result.validacao.listaMensagens.length>0){
				exibirMensagem(validacao.tipoMensagem, validacao.listaMensagens);
			}
			
			if (result.cnpj){
				$("#cnpj", recebimentoFisicoController.workspace).val(result.cnpj);
				$("#fornecedor", recebimentoFisicoController.workspace).val(removeSpecialCharacteres(result.cnpj));
			}
			
			if (result.numeroNotaFiscal){
				$("#notaFiscal", recebimentoFisicoController.workspace).val(result.numeroNotaFiscal);
			}
			
			if (result.serieNotaFiscal){
				$("#serie", recebimentoFisicoController.workspace).val(result.serieNotaFiscal);
			}
			
			if (result.chaveAcesso){
				$("#eNF", recebimentoFisicoController.workspace).check();
				recebimentoFisicoController.mostrar_nfes();
				$("#chaveAcesso", recebimentoFisicoController.workspace).val(result.chaveAcesso);
			}
			
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
		$('#chBoxReplicaValorRepartePrevistoAll', recebimentoFisicoController.workspace).uncheck();
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
		var pacotePadrao		= $("#recebimento-fisico-pacotePadrao", recebimentoFisicoController.workspace).val();
		var codigo 				= $("#codigo", recebimentoFisicoController.workspace).val();
		var produto 			= $("#produto", recebimentoFisicoController.workspace).val();
		var precoCapa			= $("#recebimento-fisico-precoCapa", recebimentoFisicoController.workspace).val();
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
		var pacotePadrao		= $("#recebimento-fisico-pacotePadrao", recebimentoFisicoController.workspace).val();
		var codigo 				= $("#codigo", recebimentoFisicoController.workspace).val();
		var produto 			= $("#produto", recebimentoFisicoController.workspace).val();
		var precoCapa			= $("#recebimento-fisico-precoCapa", recebimentoFisicoController.workspace).val();
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
		$("#recebimento-fisico-precoCapa", recebimentoFisicoController.workspace).val("");
		$("#edicao", recebimentoFisicoController.workspace).val("");
		$("#datepickerLancto", recebimentoFisicoController.workspace).val("");
		$("#datepickerRecolhimento", recebimentoFisicoController.workspace).val("");
		$("#repartePrevisto", recebimentoFisicoController.workspace).val("");
		$("#tipoLancamento", recebimentoFisicoController.workspace).val("");		
		$('#labelValorTotal', recebimentoFisicoController.workspace).text('0,00');
		$('#labelValorTotalDesconto', recebimentoFisicoController.workspace).text('0,00');
	
	},

	/**
	* LIMPA OS CAMPOS DO CADASTRO DE NOVA NOTA E NOVO ITEM.
	*/
	limparCamposNovoItem : function() {
		
		$("#codigo", recebimentoFisicoController.workspace).val("");
		$("#produto", recebimentoFisicoController.workspace).val("");
		$("#recebimento-fisico-precoCapa", recebimentoFisicoController.workspace).val("");
		$("#edicao", recebimentoFisicoController.workspace).val("");
		$("#datepickerLancto", recebimentoFisicoController.workspace).val("");
		$("#datepickerRecolhimento", recebimentoFisicoController.workspace).val("");
		$("#repartePrevisto", recebimentoFisicoController.workspace).val("");
		$("#tipoLancamento", recebimentoFisicoController.workspace).val("");
		$("#peso", recebimentoFisicoController.workspace).val("");
		$("#recebimento-fisico-pacotePadrao", recebimentoFisicoController.workspace).val("");
	
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
	carregarItemNotaGridNota : function() {
						
		$(".gridWrapper", recebimentoFisicoController.workspace).empty();
		
		$(".gridWrapper", recebimentoFisicoController.workspace).append($("<table>").attr("class", "itemNotaGrid"));
		
		//ATENTO AO ALTERAR A ORDEM DAS COLUNAS DO GRID POIS HÁ COLUNAS NA POLÍTICA DE SEGURANÇA DO USUÁRIO
		$(".itemNotaGrid", recebimentoFisicoController.workspace).flexigrid({
				onSuccess: function() {bloquearItensEdicao(recebimentoFisicoController.workspace);
									   recebimentoFisicoController.marcarLinhas();},
				preProcess: recebimentoFisicoController.getDataFromResultNota,
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
				display : 'Pcte. Padrão',
				name : 'pacotePadrao',
				width : 70,
				sortable : false,
				align : 'center'
			}, {
				display : 'Preço Capa ',
				name : 'precoCapa',
				width : 70,
				sortable : false,
				align : 'center'
			}, {
				display : 'Preço c/ Desconto R$ ',
				name : 'precoDesconto',
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
				width : 65,
				sortable : false,
				align : 'center'
			}, {
				display : 'Qtd. Quebra',
				name : 'qtdExemplar',
				width : 62,
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
				name : 'valorTotalCapa',
				width : 70,
				sortable : false,
				align : 'center'
			}, {
				display : 'Valor Total c/ Desc. R$',
				name : 'valorTotalDesconto',
				width : 118,
				sortable : false,
				align : 'center'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 40,
				sortable : false,
				align : 'center'
			},{
				display : 'Replicar Qtd.',
				name : 'replicaQtd',
				width : 64,
				sortable : false,
				align : 'center'
			}],
		
			showTableToggleBtn : false,
			width : 1200,
			height : 180
		});
		
		recebimentoFisicoController.apresentarEsconderConteudoPermissao();
	},
	
	marcarLinhas : function(){
		$.each(recebimentoFisicoController.linhasDestacadas,
			function(index, item){
				$('#row' + item, recebimentoFisicoController.workspace).removeClass("erow").addClass("gridLinhaDestacada");
			}
		);
	},
	
	apresentarEsconderConteudoPermissao : function() {
		if($("#permissaoGridColRepartePrevisto").val() != "true"){
			
			$(".itemNotaGrid", recebimentoFisicoController.workspace).flexToggleCol(6,false);
			$(".itemNotaGrid", recebimentoFisicoController.workspace).flexToggleCol(13,false);
		}
		
		if($("#permissaoGridColDiferenca").val() != "true"){
			
			$(".itemNotaGrid", recebimentoFisicoController.workspace).flexToggleCol(9,false);
		}
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
			onSuccess : function() {
							bloquearItensEdicao(recebimentoFisicoController.workspace);
							recebimentoFisicoController.marcarLinhas();
							onSuccessFunction();
						}

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
	
	validarDescontosRecebimentoFisico: function() {
		
		var params = recebimentoFisicoController.getParametrosConfirmacao();
		
		$.postJSON(
			this.path + 'validarDescontosRecebimentoFisico', 
			params, 
			function(result) {

				if (result.mensagens && result.mensagens!="valido") {
					recebimentoFisicoController.popupDescontosDivergentes(result);
				} else {
					if(recebimentoFisicoController.validarPreenchimentoQuantidades()){
					  recebimentoFisicoController.confirmarRecebimentoFisico();
					}
				}
			}
		);		
	},
	
	popupDescontosDivergentes: function(result) {
		
		var message = "";
		
		$.each(result.mensagens, function(index, value) {
			message += value + "<br />";
		});
		
		$("#dialog-descontos-divergente", this.workspace).find("p").html(
			message
		);
		
		$("#dialog-descontos-divergente", this.workspace).dialog({			
			resizable : false,
			height : 'auto',
			width : 450,
			modal : true,
			buttons : {				
				"Confirmar" : function() {
					$(this).dialog("close");
					recebimentoFisicoController.confirmarRecebimentoFisico();
					return true;
				},
				"Cancelar" : function() {
					$(this).dialog("close");
					return false;
				}
			},
			
			form: $("#dialog-descontos-divergente", this.workspace).parents("form")
		});
	},
	

    /**
     * COFIRMA OS DADOS DOS ITENS DA NOTA EDITADOS.
     */
	confirmarRecebimentoFisico: function() {
		
		var params = recebimentoFisicoController.getParametrosConfirmacao();
		
		$.postJSON(
			this.path + 'confirmarRecebimentoFisico', 
			params, 
			function(result) {

				$(".grids", recebimentoFisicoController.workspace).hide();
				
				recebimentoFisicoController.limparCamposPesquisa();
				
				recebimentoFisicoController.ocultarBtns();
			}
		);
	},
	
	getParametrosConfirmacao: function() {

		if (recebimentoFisicoController.indNotaFiscalInterface) {
			
			if (!recebimentoFisicoController.validarPreenchimentoQuantidades()) {

				return;
			}
			
			return serializeArrayToPost('itensRecebimento', recebimentoFisicoController.obterListaValores());
		}
	},
	
	/**
	 * Valida o preenchimento das quantidades do recebimento físico.
	 */
	validarPreenchimentoQuantidades : function() {
		
		var linhasDaGrid = $(".itemNotaGrid tr", recebimentoFisicoController.workspace);
		
		var valido = true;
		
		$.each(linhasDaGrid, function(index, value) {
			
			var qtdPacotes = 
				(!$(value).find('input[name="qtdPacote"]').val()) ? 0 : parseInt($(value).find('input[name="qtdPacote"]').val());
			
			var qtdExemplares = 
				(!$(value).find('input[name="qtdExemplar"]').val()) ? 0 : parseInt($(value).find('input[name="qtdExemplar"]').val());
			
			var qtdTotal = qtdPacotes + qtdExemplares;
			
			if (isNaN(qtdTotal)){
				qtdTotal = 0;
			}
			
			if (qtdTotal == 0) {

				recebimentoFisicoController.exibirConfirmacaoQuantidadeDigitadas();
				
				valido = false;
				
				return;
			}
		});
		
		return valido;
	},
	
	exibirConfirmacaoExclusaoNota : function() {
		
		$("#dialog-verificacao-exclusao", this.workspace).dialog({
			
			resizable : false,
			height : 'auto',
			width : 450,
			modal : true,
			buttons : {
				
				"Confirmar" : function() {
					$(this).dialog("close");
					recebimentoFisicoController.excluirNotaRecebimentoFisico();
					return true;
				},

				"Cancelar" : function() {
					$(this).dialog("close");
					return false;
				}
			},
			
			form: $("#dialog-verificacao-quantidades", this.workspace).parents("form")
		});	
		
	},
	
	exibirConfirmacaoQuantidadeDigitadas : function() {
			
		$("#dialog-verificacao-quantidades", this.workspace).dialog({
			
			resizable : false,
			height : 'auto',
			width : 450,
			modal : true,
			buttons : {
				
				"Confirmar" : function() {
					recebimentoFisicoController.confirmarRecebimentoFisico();
					$(this).dialog("close");
					return true;
				},

				"Cancelar" : function() {
					$(this).dialog("close");
					return false;
				}
			},
			
			form: $("#dialog-verificacao-quantidades", this.workspace).parents("form")
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
			
			$("#qtdPacote_"+ lineId, recebimentoFisicoController.workspace).val(0);
			$("#qtdExemplar_"+ lineId, recebimentoFisicoController.workspace).val(0);
			$("#diferenca_"+ lineId, recebimentoFisicoController.workspace).text(0);
			
			$("#chBoxReplicaValorRepartePrevistoAll", recebimentoFisicoController.workspace).uncheck();
			
			return;
		}
		
		var parametroPesquisa = [{name :"lineId", value : lineId}];
		
		$.postJSON(this.path + 'replicarValorRepartePrevisto', parametroPesquisa, 

		function(resultado) {

			$("#qtdPacote_"+ lineId, recebimentoFisicoController.workspace).val(resultado.qtdPacote);
			$("#qtdExemplar_"+ lineId, recebimentoFisicoController.workspace).val(resultado.qtdExemplar);
			$("#diferenca_"+ lineId, recebimentoFisicoController.workspace).text(0);
			
			resultado.valorTotalCapa = $.formatNumber(resultado.valorTotalCapa, {format:"#,##0.00", locale:"br"});
			
			resultado.valorTotalDesconto = $.formatNumber(resultado.valorTotalDesconto, {format:"#,##0.0000", locale:"br"});
			
			$("#valorTotalCapa_"+ lineId, recebimentoFisicoController.workspace).text(resultado.valorTotalCapa);
			$("#valorTotalDesconto_"+ lineId, recebimentoFisicoController.workspace).text(resultado.valorTotalDesconto);
			
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
				
				$(value).find('input[name="qtdPacote"]').val(0);
				$(value).find('input[name="qtdExemplar"]').val(0);
				$(value).find('input[name="diferenca"]').val(0);
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

			$("#recebimento-fisico-pacotePadrao", recebimentoFisicoController.workspace).val(resultado.pacotePadrao);
			$("#peso", recebimentoFisicoController.workspace).val(resultado.peso);
			$("#codigo", recebimentoFisicoController.workspace).val(resultado.codigo);
			$("#produto", recebimentoFisicoController.workspace).val(resultado.nomeProduto);
			$("#recebimento-fisico-precoCapa", recebimentoFisicoController.workspace).val(
				$.formatNumber(resultado.precoCapa, {format:"#,##0.00", locale:"br"})
			);
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
	
	alterarValor : function(idLinha) {

		var qtdPacote 		= $("#qtdPacote_"+idLinha, recebimentoFisicoController.workspace).val();
		var qtdQuebra 		= $("#qtdExemplar_"+idLinha, recebimentoFisicoController.workspace).val();

		if (qtdPacote == "") {
			qtdPacote = 0;
			$("#qtdPacote_"+idLinha, recebimentoFisicoController.workspace).val(0);
		}

		if (qtdQuebra == "") {
			qtdQuebra = 0;
			$("#qtdExemplar_"+idLinha, recebimentoFisicoController.workspace).val(0);
		}

		qtdPacote 		= parseInt(qtdPacote);
		qtdQuebra 		= parseInt(qtdQuebra);
		var repartePrevisto = parseInt($("#repartePrevisto_"+idLinha, recebimentoFisicoController.workspace).text());
		var pacotePadrao 	= parseInt($("#pacotePadrao_"+idLinha, recebimentoFisicoController.workspace).text());
		var diferenca 		= 0;

		diferenca = ( (qtdPacote * pacotePadrao) + qtdQuebra) - repartePrevisto; 

		if (diferenca < 0) {
			$("#diferenca_"+idLinha, recebimentoFisicoController.workspace)[0].style.color = "red";			
		} else {
			$("#diferenca_"+idLinha, recebimentoFisicoController.workspace)[0].style.color = "black";			
		}
		
		if (isNaN(diferenca)){
			diferenca = 0;
		}

		$("#diferenca_"+idLinha, recebimentoFisicoController.workspace).text(diferenca);
		
	},
	
	atualizarValoresTotais : function(){
		var linhasDaGrid = $(".itemNotaGrid tr", recebimentoFisicoController.workspace);

		var valorTotal = 0, valorTotalDesconto = 0;
		
		var qtdeProdutos = 0;
		
		var totalExemplares = 0;

		$.each(linhasDaGrid, function(index, value) {

			var linha = $(value);

			valorTotal += 
				intValue(removeMascaraPriceFormat(
					linha.find('[name="valorTotalCapa"]').text()));
			
			valorTotalDesconto += 
				intValue(removeMascaraPriceFormat(
					linha.find('[name="valorTotalDesconto"]').text()));
			
			qtdeProdutos++;
			
			totalExemplares+= intValue(linha.find('[name="repartePrevisto"]').text());
		});
		
        $("#totalSemDescontoLbl", recebimentoFisicoController.workspace).html(floatToPrice(valorTotal/100));
        $("#totalComDescontoLbl", recebimentoFisicoController.workspace).html(floatToPrice(valorTotalDesconto/100));
        
        $("#qtdeProdutos", recebimentoFisicoController.workspace).html(qtdeProdutos);
        $("#totalExemplares", recebimentoFisicoController.workspace).html(totalExemplares);
	},
		
	alterarValorItem : function(idLinha) {
		
		var precoDesconto = priceToFloat($("#precoDescontoItem"+idLinha, recebimentoFisicoController.workspace).text());
		var preco = priceToFloat($("#precoCapaItem"+idLinha, recebimentoFisicoController.workspace).text());
		
		var qtdNota			= $("#qtdNotaItem"+idLinha).val();
		var qtdPacote 		= $("#qtdPacoteItem"+idLinha).val();
		var qtdQuebra 		= $("#qtdExemplarItem"+idLinha).val();

		if (qtdNota == "") {
			qtdNota = 0;
			$("#qtdNotaItem"+idLinha).val(0);
		}
		
		if (qtdPacote == "") {
			qtdPacote = 0;
			$("#qtdPacoteItem"+idLinha).val(0);
		}

		if (qtdQuebra == "") {
			qtdQuebra = 0;
			$("#qtdExemplarItem"+idLinha).val(0);
		}
		
		qtdPacote 			= parseInt(qtdPacote);
		qtdQuebra 			= parseInt(qtdQuebra);
		var repartePrevisto = parseInt($("#qtdNotaItem"+ idLinha).val());
		var pacotePadrao 	= !isNaN(parseInt($("#pacotePadraoItem"+ idLinha).text())) ? parseInt($("#pacotePadraoItem"+ idLinha).text()) : 0;
		var diferenca 		= 0;

		var valorDesconto = precoDesconto * qtdNota;
		var valor = preco * qtdNota;

		$("#valorItemDesconto"+idLinha, recebimentoFisicoController.workspace).text(
			$.formatNumber(valorDesconto,{format:"#,##0.0000", locale:'br'}));
		
		$("#valorItem"+idLinha, recebimentoFisicoController.workspace).text($.formatNumber(valor,{locale:'br'}));
		
		diferenca = ((qtdPacote * pacotePadrao) + qtdQuebra) - repartePrevisto; 

		if (diferenca < 0) {
			$("#diferencaItem"+idLinha)[0].style.color = "red";			
		} else {
			$("#diferencaItem"+idLinha)[0].style.color = "black";			
		}
		
		$("#diferencaItem"+idLinha).val(diferenca);
		
		 $("#diferencaItem"+idLinha, recebimentoFisicoController.workspace).numeric({
			decimal:''
		});
		
		recebimentoFisicoController.obterValorTotalItens();
	},
	
	numericOnly : function(event) {
	
			var num = (event.keyCode != 0 ? event.keyCode : event.charCode);
			var numeroId = event.target.id.split("_");
			if(num>=48 & num<=57) {
		  		if($("#replicaValorRepartePrevisto_" + numeroId[1]).is(':checked')) {
		  			$("#replicaValorRepartePrevisto_" + numeroId[1]).attr('checked', false);
		  		} 
		  		return true;
			} else {
				 return false; 
			}
	},
	
	/**
	 * PREPARA OS DADOS DA NOTA INTERFACE A SEREM APRESENTADOS NA GRID.
	 */
	getDataFromResultNota : function(data) {
		
		recebimentoFisicoController.linhasDestacadas = [];
		
		var totalDescontoGeral = 0, totalGeral = 0;
		
		var qtdeProdutos = 0;
		
		var totalExemplares = 0;
		
		$.each(data.rows, function(index, value) {
			
			qtdeProdutos++;
			
			totalExemplares+= intValue(value.cell.repartePrevisto);
			
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
			
			value.cell.pacotePadrao = '<span id="pacotePadrao_'+lineId+'">'+value.cell.pacotePadrao+'</span>'; 
			
			
			if(destacarValorNegativo == "S") {
				value.cell.diferenca = '<span style="color: red" id="diferenca_'+lineId+'">'+diferenca+'</span>';
			} else {
				value.cell.diferenca = '<span style="color: black" id="diferenca_'+lineId+'">'+diferenca+'</span>';
			}

			value.cell.repartePrevisto = '<span name="repartePrevisto" id="repartePrevisto_'+lineId+'">'+repartePrevisto+'</span>'; 
			
			if(edicaoItemRecFisicoPermitida == "S") {
				value.cell.qtdPacote 	=  '<input isEdicao="true" name="qtdPacote" id="qtdPacote_'+ lineId +
				'" onkeypress="return recebimentoFisicoController.numericOnly(event)" style="width: 45px;" type="text" value="'+qtdPacote+
				'" onchange="recebimentoFisicoController.alterarValor('+ lineId +
				');recebimentoFisicoController.atualizarValoresTotais();" onfocus="recebimentoFisicoController.tratarFocoInputQuantidade(this)" />'+
				hiddenFields;
				
				value.cell.qtdExemplar = '<input isEdicao="true" name="qtdExemplar" id="qtdExemplar_'+ lineId +
				'" onkeypress="return recebimentoFisicoController.numericOnly(event)"  style="width: 45px;" type="text" value="'+qtdExemplar+
				'" onchange="recebimentoFisicoController.alterarValor('+ lineId +
				');recebimentoFisicoController.atualizarValoresTotais();" " />';
				
				$('#chBoxReplicaValorRepartePrevistoAll', recebimentoFisicoController.workspace).enable();
			} else {
				value.cell.qtdPacote 	= '<input isEdicao="true" name="qtdPacote" disabled="disabled" style="width: 45px;" type="text" value="'+
					qtdPacote+'"/>'+hiddenFields;
				value.cell.qtdExemplar 	=  '<input isEdicao="true" name="qtdExemplar" disabled="disabled" style="width: 45px;" type="text" value="'+
					qtdExemplar+'"/>';
				
				$('#chBoxReplicaValorRepartePrevistoAll', recebimentoFisicoController.workspace).disable();
			}
			
			totalDescontoGeral += parseFloat(value.cell.valorTotalDesconto);
			totalGeral += parseFloat(value.cell.valorTotalCapa);
			
			value.cell.precoCapa = '<span id="precoCapa_'+ lineId + '">' +
									$.formatNumber(value.cell.precoCapa, {format:"#,##0.00", locale:"br"}) +
									'</span>';
			
			value.cell.precoDesconto = '<span id="precoCapaDesconto_' + lineId + '">' +
										$.formatNumber(value.cell.precoDesconto, {format:"#,##0.0000", locale:"br"})+
										'</span>';
			
			value.cell.valorTotalCapa = '<span name="valorTotalCapa" id="valorTotalCapa_' + lineId + '">' + 
										$.formatNumber(value.cell.valorTotalCapa, {format:"#,##0.00", locale:"br"})+
										'</span>';
			
			value.cell.valorTotalDesconto = '<span name="valorTotalDesconto" id="valorTotalDesconto_' + lineId + '">' + 
											$.formatNumber(value.cell.valorTotalDesconto, {format:"#,##0.0000", locale:"br"})+
											'</span>';

			if(edicaoItemNotaPermitida == "S") {
				
				value.cell.acao =  '<a isEdicao="true" href="javascript:;" onclick="recebimentoFisicoController.editarItemNotaFiscal('+[lineId]+');">' + 
									imgEdicao + '</a>' +
								   '<a isEdicao="true" href="javascript:;" onclick="recebimentoFisicoController.excluirItemNotaFiscal('+[lineId]+');">' + 
								   imgExclusao + '</a>';
				
			} else{
				
				value.cell.acao = 	'<a href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)"  >' + imgEdicao   + '</a>' + 
							 		'<a href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)"  >' + imgExclusao + '</a>';
			}
			
			if(edicaoItemRecFisicoPermitida == "S") {
				value.cell.replicaQtd = '<input isEdicao="true" title="Replicar Item" onclick="recebimentoFisicoController.replicarValorRepartePrevisto('+
										[lineId] + ', this);" type="checkbox" id="replicaValorRepartePrevisto_'+lineId+'" name="replicaQtde" />';
			} else {
				value.cell.replicaQtd = '<input title="Replicar Item" disabled="disabled" type="checkbox"/>';
			}
			
			if (value.cell.produtoSemCadastro){
				recebimentoFisicoController.linhasDestacadas.push(value.id);
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
		
		var subtrairNumeroColunaReplicar = 0;
		if ($("#permissaoColValorTotal", recebimentoFisicoController.workspace).val() == "true"){
			$(".itemNotaGrid", recebimentoFisicoController.workspace).flexToggleCol(11, true);
			$("#totalSemDescontoLbl", recebimentoFisicoController.workspace).text(
					$.formatNumber(totalGeral, {format:"#,##0.00", locale:"br"}));
			$("#spanTotalSemDescontoLbl", recebimentoFisicoController.workspace).show();
		} else {
			$(".itemNotaGrid", recebimentoFisicoController.workspace).flexToggleCol(11, false);
			$("#spanTotalSemDescontoLbl", recebimentoFisicoController.workspace).hide();
			subtrairNumeroColunaReplicar++;
		}
		
		if ($("#permissaoColValorTotalDesconto", recebimentoFisicoController.workspace).val() == "true"){
			$(".itemNotaGrid", recebimentoFisicoController.workspace).flexToggleCol(10, true);
			$("#totalComDescontoLbl", recebimentoFisicoController.workspace).text(
					$.formatNumber(totalDescontoGeral, {format:"#,##0.0000", locale:"br"}));
			$("#spanTotalComDescontoLbl", recebimentoFisicoController.workspace).show();
		} else {
			$(".itemNotaGrid", recebimentoFisicoController.workspace).flexToggleCol(10, false);
			$("#spanTotalComDescontoLbl", recebimentoFisicoController.workspace).hide();
			subtrairNumeroColunaReplicar++;
		}
		
		/*if (subtrairNumeroColunaReplicar != 0 || !recebimentoFisicoController.indNotaFiscalInterface){
			
			$(".itemNotaGrid", recebimentoFisicoController.workspace).flexToggleCol(13,false);
			$(".bt_sellAll", recebimentoFisicoController.workspace).hide();
		/*} else {*/
		
		$(".itemNotaGrid", recebimentoFisicoController.workspace).flexToggleCol(13,
				$("#permissaoGridColRepartePrevisto").val() == "true");
		
			$(".bt_sellAll", recebimentoFisicoController.workspace).show();
		//}
		
		$("#totalExemplares", recebimentoFisicoController.workspace).text(totalExemplares);
		$("#qtdeProdutos", recebimentoFisicoController.workspace).text(qtdeProdutos);
			
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
				display : 'Pcte. Padrão',
				name : 'pacotePadrao',
				width : 70,
				sortable : false,
				align : 'center',
				resizable : false
			},{
				display : 'Preço Capa',
				name : 'precoCapa',
				width : 70,
				sortable : false,
				align : 'right',
				resizable : false
			}, {
				display : 'Preço Desc. R$',
				name : 'precoDesconto',
				width : 80,
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
				display : 'Valor Total R$',
				name : 'valor',
				width : 70,
				sortable : false,
				align : 'center',
				resizable : false
			}, {
				display : 'Valor Total Desconto R$',
				name : 'valorDesconto',
				width : 100,
				sortable : false,
				align : 'center',
				resizable : false
			}, {
				display : 'Replicar Qtd',
				name : 'replicar',
				width : 80,
				sortable : false,
				align : 'center',
				resizable : false
			}, {
				display : 'Excluir',
				name: 'excluir',
				width: 30,
				sortable: false,
				align: 'center',
				resizable: false
			}],
			width : 1250,
			height : 180
		});
	},
	
	popup_adicionar : function() {
		this.limparCamposNovaNota();
		
		this.novoValorTotalTyped = false;
		recebimentoFisicoController.montaGridItens();
		
		
		$( "#dialog-adicionar", recebimentoFisicoController.workspace ).dialog({
			resizable: false,
			height:520,
			width:1280,
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

	        exp = '/\-|\.|\/|\(|\)| /g';
	        campoSoNumeros = campo.replace( exp, "" ); 

	        var posicaoCampo = 0;    
	        var NovoValorCampo="";
	        var TamanhoMascara = campoSoNumeros.length;; 

            for(var i=0; i<= TamanhoMascara; i++) { 
                    boleanoMascara  = ((Mascara.charAt(i) == "-") || (Mascara.charAt(i) == ".")
                                                            || (Mascara.charAt(i) == "/"));
                    boleanoMascara  = boleanoMascara || ((Mascara.charAt(i) == "(") 
                                                            || (Mascara.charAt(i) == ")") || (Mascara.charAt(i) == " "));
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
			
			recebimentoFisicoController.limparCamposItemNota(index);
			
			return;
		}
		
		recebimentoFisicoController.limparCamposItemNota(index);
		
		$.postJSON(this.path + 'obterDadosEdicao', {codigo:codigo,edicao:edicao}, 
			function(result) { 
				$("#precoDescontoItem"+index, recebimentoFisicoController.workspace).text(
					$.formatNumber(result.precoDesconto, {format:"#,##0.0000", locale:"br"})
				);
				$("#precoCapaItem"+index, recebimentoFisicoController.workspace).text(
					$.formatNumber(result.precoCapa, {format:"#,##0.00", locale:"br"})
				);
				$("#pacotePadraoItem"+index, recebimentoFisicoController.workspace).text(result.pacotePadrao);
			},
			null,
			true
		);	
	},
	
	calcularDiferencaEValorItem : function(index){
		
		var preco = priceToFloat($("#precoDescontoItem"+index, recebimentoFisicoController.workspace).text());
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
				'nota.valorTotal': this.preparaValor($("#novoValorTotal",recebimentoFisicoController.workspace).val())};
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
			 
			 var valuePrecoCapa='';
			 if (row.cell.precoCapa!=null){
				 valuePrecoCapa = row.cell.precoCapa;
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
			 
			 var valueValorDesconto='';
			 if (row.cell.valorTotalDesconto!=null){
				 valueValorDesconto = row.cell.valorTotalDesconto;
			 }

        	 var codigo =       '<input class="number" maxlength="28" value="'+valueCodigo+
        	 					'" type="text" name="itensRecebimento.codigoItem" id="codigoItem'+ index +
        	 					'" style="width: 50px;" onchange="pesquisaProdutoRecebimentoFisico.pesquisarPorCodigoProduto(\'#codigoItem'+ index +
        	 					'\', \'#produtoItem'+ index +'\', \'#edicaoItem'+ index +'\', true, null, recebimentoFisicoController.limparCamposItemNota('+
        	 					index+'));" ></input>';
	         			 					     
	         var produto =      '<input maxlength="200" value="'+valueProduto+'" type="text" name="itensRecebimento.produtoItem" id="produtoItem'+ index +
	         					'" style="width: 140px;" onkeyup="pesquisaProdutoRecebimentoFisico.autoCompletarPorNomeProduto(\'#produtoItem'+ index +
	         					'\', false);" onblur="pesquisaProdutoRecebimentoFisico.pesquisarPorNomeProduto(\'#codigoItem'+ index +'\', \'#produtoItem'+ 
	         					index +'\', \'#edicaoItem'+ index +'\', true, null, recebimentoFisicoController.limparCamposItemNota('+index+'));"></input>';
				             
			 var edicao =       '<input class="number" maxlength="18" value="'+valueEdicao+'" type="text" name="itensRecebimento.edicaoItem" id="edicaoItem'+
			 					index +'" style="width: 30px;" onchange="recebimentoFisicoController.obterDadosEdicao('+index+');"></input>';         
			
			 var precoCapa ='<span class="money" maxlength="17" value="'+valuePrecoCapa+
			 				'" type="text" readonly="readonly" name="itensRecebimento.precoCapa" id="precoCapaItem'+ index +
			 				'" style="width: 80px; border: 0px; background-color: inherit;"></span>';
			 
			 var precoDesconto ='<span class="money" maxlength="17" value="'+valuePrecoDesconto+
			 					'" type="text" readonly="readonly" name="itensRecebimento.precoDescontoItem" id="precoDescontoItem'+ index +
			 					'" style="width: 80px; border: 0px; background-color: inherit;"></span>';
			 
			 var qtdNota =      '<input class="number" maxlength="10" value="'+valueQtdNota+
			 					'" type="text" name="itensRecebimento.qtdNotaItem" id="qtdNotaItem'+ index +
			 					'" style="width: 70px;" onchange="recebimentoFisicoController.replicarQuantidadeItem('+index+
			 					'); recebimentoFisicoController.alterarValorItem('+index+'); '+
			 					'$(\'#checkbox'+ index +'\').check(); '+
			 					'recebimentoFisicoController.replicarQtdLancamentoManual('+index+');"></input>';
			     
	         var qtdPacote =    '<input class="number" maxlength="10" value="'+valueQtdPacote+
	         					'" type="text" name="itensRecebimento.qtdPacoteItem" id="qtdPacoteItem'+ index +
	         					'" style="width: 70px;" onchange="recebimentoFisicoController.alterarValorItem('+index+
	         					');" onkeydown="recebimentoFisicoController.retirarChekboxReplicar('+index+');"></input>';
				             
			 var qtdExemplar =  '<input class="number" maxlength="10" value="'+valueQtdExemplar+
			 					'" type="text" name="itensRecebimento.qtdExemplarItem" id="qtdExemplarItem'+ index +
			 					'" style="width: 70px;" onchange="recebimentoFisicoController.alterarValorItem('+index+
			 					');" onkeydown="recebimentoFisicoController.retirarChekboxReplicar('+index+');"></input>'; 
			 
			 var pacotePadrao = '<span class="number" name="itensRecebimento.pacotePadraoItem" id="pacotePadraoItem'+ index +'">'+ valuePacotePadrao +'</span>';
			 
			 var diferenca =    '<input class="number" maxlength="10" value="'+valueDiferenca+
			 					'" type="text" readonly="readonly" name="itensRecebimento.diferencaItem" id="diferencaItem'+ index +
			 					'" style="width: 70px; border: 0px; background-color: inherit;"></input>';
				 
			 var valor =        '<span class="money" maxlength="17" value="'+valueValor+
			 					'" type="text" readonly="readonly" name="itensRecebimento.valorItem" id="valorItem'+ index +
			 					'" style="width: 70px; border: 0px; background-color: inherit;"></span>';
			 
			 var valorDesconto ='<span class="money" maxlength="17" value="'+valueValorDesconto+
			 					'" type="text" readonly="readonly" name="itensRecebimento.valorItemDesconto" id="valorItemDesconto'+ index +
			 					'" style="width: 70px; border: 0px; background-color: inherit;"></span>';
			 
			 var checkBox =     '<input title="Replicar Item" type="checkbox" name="checkboxGrid" id="checkbox'+ index +
			 					'" onclick="recebimentoFisicoController.replicarQtdLancamentoManual('+index+');" />';
			 
			 var btnExcluir =   '<a href="javascript:;"><img src="'+contextPath+'/images/ico_excluir.gif" width="15" height="15" hspace="5" border="0" onclick="recebimentoFisicoController.removerItem(this)"/></a>';
			 
		     row.cell[0] = codigo;
		     row.cell[1] = produto;
		     row.cell[2] = edicao;
		     row.cell[3] = pacotePadrao;
		     row.cell[4] = precoCapa;
		     row.cell[5] = precoDesconto;
		     row.cell[6] = qtdNota;
		     row.cell[7] = qtdPacote;
		     row.cell[8] = qtdExemplar;
		     row.cell[9] = diferenca;
		     row.cell[10] = valor;
		     row.cell[11] = valorDesconto;
		     row.cell[12] = checkBox;
		     row.cell[13] = btnExcluir;
         }
	    
		);

		return resultado;
	},
	
	removerItem : function(element) {
		
		$(element).parent().parent().parent().parent().remove();
		
		recebimentoFisicoController.obterValorTotalItens();
		
	},
	
	replicarQtdLancamentoManual: function(index){
		
		//caso algum dos checkbox da grid tenha sido acionado
		if (index || index == 0){
			
			//caso o checkbox esteja selecionado calcula valores pra a respectiva linha
			if ($("#checkbox" + index, recebimentoFisicoController.workspace).is(":checked")){
			
				var qtdNotaDigitada = parseInt($("#qtdNotaItem" + index, recebimentoFisicoController.workspace).val());
				
				pacotes = qtdNotaDigitada / parseInt($("#pacotePadraoItem" + index, recebimentoFisicoController.workspace).text());
				exemplares = qtdNotaDigitada % parseInt($("#pacotePadraoItem" + index, recebimentoFisicoController.workspace).text());
				
				if (!isNaN(qtdNotaDigitada)){
					if(!isNaN(pacotes - pacotes)) {
						$("#qtdPacoteItem" + index, recebimentoFisicoController.workspace).val(
							parseInt(pacotes)
						);
					} else {
						$("#qtdPacoteItem" + index, recebimentoFisicoController.workspace).val(0);
					}
					
					if(!isNaN(exemplares)) {
						$("#qtdExemplarItem" + index, recebimentoFisicoController.workspace).val(
							parseInt(exemplares)
						);
					} else {
						$("#qtdExemplarItem" + index, recebimentoFisicoController.workspace).val(0);
					}
					
					var pacotePadrao = parseInt($("#pacotePadraoItem" + index, recebimentoFisicoController.workspace).text());
					
					if(pacotePadrao <= 0){
						$("#qtdExemplarItem" + index, recebimentoFisicoController.workspace).val(qtdNotaDigitada);
					}
					
				} else {
					
					$("#qtdPacoteItem" + index, recebimentoFisicoController.workspace).val(0);
					
					$("#qtdExemplarItem" + index, recebimentoFisicoController.workspace).val(0);
				}
				
				$("#diferencaItem"+ index, recebimentoFisicoController.workspace).val(0);
				
				var qtdItens = parseFloat(priceToFloat($("#qtdNotaItem" + index, recebimentoFisicoController.workspace).val()));
				
				$("#valorItem" + index, recebimentoFisicoController.workspace).text(
					$.formatNumber(
						(qtdItens) * parseFloat(priceToFloat($("#precoCapaItem" + index).text())),
						{format:"#,##0.00", locale:"br"}
					)	
				);
				
				$("#valorItemDesconto" + index, recebimentoFisicoController.workspace).text(
					$.formatNumber(
						(qtdItens) * parseFloat(priceToFloat($("#precoDescontoItem" + index).text())),
						{format:"#,##0.0000", locale:"br"}
					)	
				);
			//caso o checkbox esteja deselecionado
			} else {
				
				$("#qtdPacoteItem" + index, recebimentoFisicoController.workspace).val(0);
				$("#qtdExemplarItem" + index, recebimentoFisicoController.workspace).val(0);
				$("#diferencaItem"+ index, recebimentoFisicoController.workspace).val(0);
			}
		//caso o checkbox marcar todos tenha chamado essa função
		} else {
			
			$.each($("[name='checkboxGrid']", recebimentoFisicoController.workspace),
				function (index, item){
					var id = $(item).attr("id");
					id = id.replace('checkbox', '');
					
					//caso o checkbox marcar todos esteja checado
					if ($("#selTodos", recebimentoFisicoController.workspace).is(":checked")){
					
						recebimentoFisicoController.replicarQtdLancamentoManual(id);
						
					//caso o checkbox marcar todos não esteja checado
					} else {
						
						$("#qtdPacoteItem" + id, recebimentoFisicoController.workspace).val(0);
						$("#qtdExemplarItem" + id, recebimentoFisicoController.workspace).val(0);
						$("#diferencaItem"+ id, recebimentoFisicoController.workspace).val(0);
					}
				}
			);
		}
		
		recebimentoFisicoController.obterValorTotalItens();
	},
	
	limparCamposItemNota : function(idLinha) {
		
		$("#precoDescontoItem" + idLinha, recebimentoFisicoController.workspace).val("");
		$("#qtdNotaItem" + idLinha, recebimentoFisicoController.workspace).val("");
		$("#qtdPacoteItem" + idLinha, recebimentoFisicoController.workspace).val("");
		$("#qtdExemplarItem" + idLinha, recebimentoFisicoController.workspace).val("");
		$("#pacotePadraoItem" + idLinha, recebimentoFisicoController.workspace).text("");
		$("#diferencaItem" + idLinha, recebimentoFisicoController.workspace).val("");
		$("#valorItem" + idLinha, recebimentoFisicoController.workspace).val("");
		$("precoCapaItem" + idLinha, recebimentoFisicoController.workspace).val("");
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
		
//		if(vr.substr(vr.length-3,1)==","){
//			vr = this.replaceAll(vr,".","");
//			vr = this.replaceAll(vr,",",".");
//		}
//		if(vr.substr(vr.length-3,1)=="."){
//			vr = this.replaceAll(vr,",","");
//		}
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
			var colunaPacotePadrao = linha.find("td")[3];
			var colunaPrecoCapa = linha.find("td")[4];
			var colunaPrecoDesconto = linha.find("td")[5];
			var colunaQtdNota = linha.find("td")[6];
			var colunaQtdPacote = linha.find("td")[7];
			var colunaQtdExemplar = linha.find("td")[8];
			var colunaDiferenca = linha.find("td")[9];
			var colunaValor = linha.find("td")[10];
			
			var codigo = 
				$(colunaCodigo).find("div").find('input[name="itensRecebimento.codigoItem"]').val();
			
			var produto = 
				$(colunaProduto).find("div").find('input[name="itensRecebimento.produtoItem"]').val();

			var edicao = 
				$(colunaEdicao).find("div").find('input[name="itensRecebimento.edicaoItem"]').val();

			var precoDesconto =
				$(colunaPrecoDesconto).find("div").find('[name="itensRecebimento.precoDescontoItem"]').text();
			
			var precoCapa = 
				$(colunaPrecoCapa).find("div").find('[name="itensRecebimento.precoCapaItem"]').text();
			
			var qtdNota =
				$(colunaQtdNota).find("div").find('input[name="itensRecebimento.qtdNotaItem"]').val();
			
			var qtdPacote =
				$(colunaQtdPacote).find("div").find('input[name="itensRecebimento.qtdPacoteItem"]').val();
			
			var qtdExemplar =
				$(colunaQtdExemplar).find("div").find('input[name="itensRecebimento.qtdExemplarItem"]').val();
			
			var pacotePadrao =
				$(colunaPacotePadrao).find("div").find('[name="itensRecebimento.pacotePadraoItem"]').text();
			
			var diferenca =
				$(colunaDiferenca).find("div").find('input[name="itensRecebimento.diferencaItem"]').val();
			
			var valor =
				$(colunaValor).find("div").find('[name="itensRecebimento.valorItem"]').text();
			

			if (!recebimentoFisicoController.isAtributosLancamentoVazios(codigo, produto, edicao, precoDesconto, qtdNota, qtdPacote, qtdExemplar)) {

	            var dataLancamento = $("#novoDataEntrada", recebimentoFisicoController.workspace).val();
				
			    var dataRecolhimento = $("#novoDataEmissao", recebimentoFisicoController.workspace).val();
	
				var lancamento = {
						codigoProduto:codigo,
						nomeProduto:produto,
						edicao:edicao,
						precoDesconto:recebimentoFisicoController.preparaValor(precoDesconto),
						precoCapa:recebimentoFisicoController.preparaValor(precoCapa),
						repartePrevisto:qtdNota,
						qtdFisico:qtdNota,
						qtdPacote:qtdPacote,
						qtdExemplar:qtdExemplar,
						pacotePadrao:pacotePadrao,
						diferenca:diferenca,
						valorTotal:recebimentoFisicoController.preparaValor(valor),
						valorTotalString:recebimentoFisicoController.preparaValor(valor),
						dataLancamento:dataLancamento,
						dataRecolhimento:dataRecolhimento};
				
				listaItens.push(lancamento);
			
			}

		});

		return listaItens;
	},

	obterValorTotalItens : function() {

		var linhasDaGrid = $(".novoItemNotaGrid tr", recebimentoFisicoController.workspace);

		var valorTotal = 0, valorTotalDesconto = 0;

		$.each(linhasDaGrid, function(index, value) {

			var linha = $(value);

			valorTotal += 
				intValue(removeMascaraPriceFormat(
					linha.find('[name="itensRecebimento.valorItem"]').text()));
			
			valorTotalDesconto += 
				intValue(removeMascaraPriceFormat(
					linha.find('[name="itensRecebimento.valorItemDesconto"]').text()));
		});
		
		if(!this.novoValorTotalTyped){
			$("#novoValorTotal", recebimentoFisicoController.workspace).val(floatToPrice(valorTotal/100));
		}

		$("#labelValorTotal", recebimentoFisicoController.workspace).html(floatToPrice(valorTotal/100));
        //$("#labelValorTotalDesconto", recebimentoFisicoController.workspace).html(floatToPrice(valorTotalDesconto/100));
		$("#labelValorTotalDesconto", recebimentoFisicoController.workspace).html($.formatNumber(
				(valorTotalDesconto/10000),{format:"#,##0.0000", locale:'br'}));
	},
    
    replicarQuantidadeItem : function(index){
    	
    	if ( $("#checkbox"+index, recebimentoFisicoController.workspace).attr('checked') == 'checked') {
    		
    	    $("#qtdExemplarItem"+index, recebimentoFisicoController.workspace).val($("#qtdNotaItem"+index, recebimentoFisicoController.workspace).val());
    	    
    	}    
    },

    //PREPARA NOVA LINHA DA GRID
    adicionarNovaLinha : function(index){

    	var codigo =       '<input class="number" maxlength="28" type="text" name="itensRecebimento.codigoItem" id="codigoItem'+ index +
    						'" style="width: 50px;" onchange="pesquisaProdutoRecebimentoFisico.pesquisarPorCodigoProduto(\'#codigoItem'+ index +
    						'\', \'#produtoItem'+ index +'\', \'#edicaoItem'+ index +'\', true, null);" ></input>';
	     
        var produto =      '<input maxlength="200" type="text" name="itensRecebimento.produtoItem" id="produtoItem'+ index +
        					'" style="width: 140px;" onkeyup="pesquisaProdutoRecebimentoFisico.autoCompletarPorNomeProduto(\'#produtoItem'+ index +
        					'\', false);" onblur="pesquisaProdutoRecebimentoFisico.pesquisarPorNomeProduto(\'#codigoItem'+ index +'\', \'#produtoItem'+ 
        					index +'\', \'#edicaoItem'+ index +'\', true, null);"></input>';
			             
		var edicao =       '<input class="number" maxlength="18" type="text" name="itensRecebimento.edicaoItem" id="edicaoItem'+ index +
							'" style="width: 30px;" onkeyup="recebimentoFisicoController.obterDadosEdicao('+index+');"></input>';         
		
		 var precoCapa ='<span class="money" maxlength="17"'+
						'" type="text" readonly="readonly" name="itensRecebimento.precoCapa" id="precoCapaItem'+ index +
						'" style="width: 80px; border: 0px; background-color: inherit;"></span>';
		
		var precoDesconto ='<span class="money" maxlength="17" type="text" readonly="readonly" name="itensRecebimento.precoDescontoItem" id="precoDescontoItem'+
							index +'" style="width: 80px; border: 0px; background-color: inherit;"></span>';
		 
		var qtdNota =      '<input class="number" maxlength="10" type="text" name="itensRecebimento.qtdNotaItem" id="qtdNotaItem'+ index +
							'" style="width: 70px;" onchange="recebimentoFisicoController.replicarQuantidadeItem('+index+
							'); recebimentoFisicoController.alterarValorItem('+index+');"></input>';
		     
        var qtdPacote =    '<input class="number" maxlength="10" type="text" name="itensRecebimento.qtdPacoteItem" id="qtdPacoteItem'+ index +
        					'" style="width: 70px;" onchange="recebimentoFisicoController.alterarValorItem('+index+');"></input>';
			             
		var qtdExemplar =  '<input class="number" maxlength="10" type="text" name="itensRecebimento.qtdExemplarItem" id="qtdExemplarItem'+ index +
							'" style="width: 70px;" onchange="recebimentoFisicoController.alterarValorItem('+index+');"></input>'; 
		
		var pacotePadrao = '<span class="number" name="itensRecebimento.pacotePadraoItem" id="pacotePadraoItem'+ index +'"></span>';
		
		var diferenca =    '<input class="number" maxlength="10" type="text" readonly="readonly" name="itensRecebimento.diferencaItem" id="diferencaItem'+
							index +'" style="width: 70px; border: 0px; background-color: inherit;"></input>';
			 
		var valor =        '<span class="money" maxlength="17" type="text" readonly="readonly" name="itensRecebimento.valorItem" id="valorItem'+ index +
							'" style="width: 70px; border: 0px; background-color: inherit;"></span>';
		
		var valorDesconto ='<span class="money" maxlength="17" type="text" readonly="readonly" name="itensRecebimento.valorItemDesconto" id="valorItemDesconto'+ index +
							'" style="width: 70px; border: 0px; background-color: inherit;"></span>';
					 
		var checkBox =     '<input title="Replicar Item" type="checkbox" name="checkboxGrid" id="checkbox'+ index +
							'" onclick="recebimentoFisicoController.replicarQtdLancamentoManual('+index+');"/>';
		
		var btnExcluir =   '<a href="javascript:;"><img src="'+contextPath+'/images/ico_excluir.gif" width="15" height="15" hspace="5" border="0" onclick="$(this).parent().parent().parent().parent().remove();"/></a>';

    	var row = [{name: 'codigo', value: codigo},
   	               {name: 'produto', value: produto},
   	               {name: 'edicao', value: edicao},
   	               {name: 'pacotePadrao', value: pacotePadrao},
   	               {name: 'precoCapa', value: precoCapa},
   	               {name: 'precoDesconto', value: precoDesconto},
   	          	   {name: 'qtdeNota', value: qtdNota},
   	               {name: 'qtdePcts', value: qtdPacote},
   	               {name: 'qtdeExemplar', value: qtdExemplar},
   	               {name: 'diferenca', value: diferenca},
   	               {name: 'valor', value: valor},
   	               {name: 'valorDesconto', value: valorDesconto},
   	               {name: 'replicar', value: checkBox},
   	               {name: 'excluir', value: btnExcluir}];
    	
    	return row;
    },
    
    //ADICIONA NOVA LINHA NA GRID
    incluiNovoItem : function(){
    	
    	var data = [];

    	var dataValores = [];
    	var rowValores;
    	
    	//OBTEM VALORES DIGITADOS
    	var nLinhas = 0;
    	var linhasDaGrid = $(".novoItemNotaGrid tr", recebimentoFisicoController.workspace);
    	
    	$.each(linhasDaGrid, function(index, value) {
    		
    		nLinhas++;
    		
    		var linha = $(value);
    		var colunaCodigo = linha.find("td")[0];
			var colunaProduto = linha.find("td")[1];
			var colunaEdicao = linha.find("td")[2];
			var colunaPacotePadrao = linha.find("td")[3];
			var colunaPrecoCapa = linha.find("td")[4];
			var colunaPrecoDesconto = linha.find("td")[5];
			var colunaQtdNota = linha.find("td")[6];
			var colunaQtdPacote = linha.find("td")[7];
			var colunaQtdExemplar = linha.find("td")[8];
			var colunaDiferenca = linha.find("td")[9];
			var colunaValor = linha.find("td")[10];
			var colunaValorDesconto = linha.find("td")[11];
			
			var valueCodigo = 
				$(colunaCodigo).find('input[name="itensRecebimento.codigoItem"]').val();
			
			var valueProduto = 
				$(colunaProduto).find('input[name="itensRecebimento.produtoItem"]').val();

			var valueEdicao = 
				$(colunaEdicao).find('input[name="itensRecebimento.edicaoItem"]').val();

			var valuePrecoDesconto =
				$(colunaPrecoDesconto).find('[name="itensRecebimento.precoDescontoItem"]').text();
			
			var valuePrecoCapa=
				$(colunaPrecoCapa).find('[name="itensRecebimento.precoCapaItem"]').text();
			
			var valueQtdNota =
				$(colunaQtdNota).find('input[name="itensRecebimento.qtdNotaItem"]').val();
			
			var valueQtdPacote =
				$(colunaQtdPacote).find('input[name="itensRecebimento.qtdPacoteItem"]').val();
			
			var valueQtdExemplar =
				$(colunaQtdExemplar).find('input[name="itensRecebimento.qtdExemplarItem"]').val();
			
			var valuePacotePadrao =
				$(colunaPacotePadrao).find('[name="itensRecebimento.pacotePadraoItem"]').text();
			
			var valueDiferenca =
				$(colunaDiferenca).find('input[name="itensRecebimento.diferencaItem"]').val();
			
			var valueValor =
				$(colunaValor).find('[name="itensRecebimento.valorItem"]').text();
			
			var valueValorDesconto =
				$(colunaValorDesconto).find('[name="itensRecebimento.valorItemDesconto"]').text();

			
		    rowValores = [{name: 'codigo', value: valueCodigo},
		   	              {name: 'produto', value: valueProduto},
		   	              {name: 'edicao', value: valueEdicao},
		   	              {name: 'pacotePadrao', value: valuePacotePadrao},
		   	              {name: 'precoCapa', value: valuePrecoCapa},
		   	              {name: 'precoDesconto', value: valuePrecoDesconto},
		   	              {name: 'qtdeNota', value: valueQtdNota},
		   	              {name: 'qtdePcts', value: valueQtdPacote},
						  {name: 'qtdeExemplar', value: valueQtdExemplar},
		   	              {name: 'diferenca', value: valueDiferenca},
		   	              {name: 'valor', value: valueValor},
		   	              {name: 'valorDesconto', value: valueValorDesconto},
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
    	
    	//$("#tabelaItens").parent().animate({scrollTop: $("#tabelaItens").parent().offset().top}, 100);
    	$("#tabelaItens").parent().animate({scrollTop: $("#tabelaItens").height()}, 100);
    	
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
				var colunaPacotePadrao = linha.find("td")[3];
				var colunaPrecoCapa = linha.find("td")[4];
				var colunaPrecoDesconto = linha.find("td")[5];
				var colunaQtdNota = linha.find("td")[6];
				var colunaQtdPacote = linha.find("td")[7];
				var colunaQtdExemplar = linha.find("td")[8];
				var colunaDiferenca = linha.find("td")[9];
				var colunaValor = linha.find("td")[10];
				var colunaValorDesconto = linha.find("td")[11];
				
				$(colunaCodigo).find('input[name="itensRecebimento.codigoItem"]').val(dataValores[index].cell[0].value);
				$(colunaProduto).find('input[name="itensRecebimento.produtoItem"]').val(dataValores[index].cell[1].value);
				$(colunaEdicao).find('input[name="itensRecebimento.edicaoItem"]').val(dataValores[index].cell[2].value);
				$(colunaPacotePadrao).find('[name="itensRecebimento.pacotePadraoItem"]').text(dataValores[index].cell[3].value);
				$(colunaPrecoCapa).find('[name="itensRecebimento.precoCapa"]').text(dataValores[index].cell[4].value);
				$(colunaPrecoDesconto).find('[name="itensRecebimento.precoDescontoItem"]').text(dataValores[index].cell[5].value);
				
				$(colunaQtdNota).find('input[name="itensRecebimento.qtdNotaItem"]').val(dataValores[index].cell[6].value);
				$(colunaQtdPacote).find('input[name="itensRecebimento.qtdPacoteItem"]').val(dataValores[index].cell[7].value);
				$(colunaQtdExemplar).find('input[name="itensRecebimento.qtdExemplarItem"]').val(dataValores[index].cell[8].value);
				$(colunaDiferenca).find('input[name="itensRecebimento.diferencaItem"]').val(dataValores[index].cell[9].value);
				$(colunaValor).find('[name="itensRecebimento.valorItem"]').text(dataValores[index].cell[10].value);
				$(colunaValorDesconto).find('[name="itensRecebimento.valorItemDesconto"]').text(dataValores[index].cell[11].value);
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
		
		recebimentoFisicoController.replicarQtdLancamentoManual();
	},
    
	limparCamposNovaNota : function(){
		
		$("#novoFornecedor", 	  recebimentoFisicoController.workspace).val(-1);
		$("#novoCnpj", 			  recebimentoFisicoController.workspace).val("");
		$("#novoNumeroNota", 	  recebimentoFisicoController.workspace).val("");
        $("#novoSerieNota", 	  recebimentoFisicoController.workspace).val("");
        $("#novoNumeroNotaEnvio", recebimentoFisicoController.workspace).val("");
        $("#novoNfe", 			  recebimentoFisicoController.workspace).removeAttr("checked");
        $("#novoChaveAcesso", 	  recebimentoFisicoController.workspace).val("");
        $("#novoDataEmissao", 	  recebimentoFisicoController.workspace).val("");
        $("#novoDataEntrada", 	  recebimentoFisicoController.workspace).val("");
        $("#novoValorTotal", 	  recebimentoFisicoController.workspace).val("0,00");
        $("#labelValorTotal",     recebimentoFisicoController.workspace).text("0,00");
        $("#labelValorTotalDesconto",     recebimentoFisicoController.workspace).text("0,00");
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
				
				$("#recebimento-fisico-precoCapa", recebimentoFisicoController.workspace).val(
					$.formatNumber(result.precoVenda, {format:"#,##0.00", locale:"br"})
				);			
				$("#peso", recebimentoFisicoController.workspace).val(result.peso);			
				$("#recebimento-fisico-pacotePadrao", recebimentoFisicoController.workspace).val(result.pacotePadrao);			
				
			}
			
		});
		
	},
	
	retirarChekboxReplicar : function(numeroDaLinha) {
		
		if($("#checkbox" + numeroDaLinha).is(":checked")) {
			$("#checkbox" + numeroDaLinha).attr("checked", false);
		}
		if($("#selTodos").is(":checked")) {
			$("#selTodos").attr("checked", false);
		}
	},
	
	/**
     * FAZ A EXCLUSÃO DE UMA NOTA FISCAL
     */
	excluirNotaRecebimentoFisico : function() {
    	
		var dadosPesquisa = [{name: "id", value: recebimentoFisicoController.id}];
		
		
		$.postJSON(this.path + 'excluirNotaRecebimentoFisico', dadosPesquisa, 

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
	}
}, BaseController);

//@ sourceURL=recebimentoFisico.js