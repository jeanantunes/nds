var indNotaFiscalInterface = false;

var indRecebimentoFisicoConfirmado = false;

var indTodosFornecedor = false;

var jsDadosProduto = {

exibirDetalhesProdutoEdicao : function() {
	
	var data = "codigo=" + $("#codigo").val() + "&edicao=" + $("#edicao").val();
	
	$.postJSON( contextPath + '/estoque/recebimentoFisico/obterProdutoEdicao', data, function(result){
		
		if(typeof result != "undefined") {
			
			$("#precoCapa").val(result.precoVenda);			
			$("#peso").val(result.peso);			
			$("#pacotePadrao").val(result.pacotePadrao);			
			
		}
		
	});
	
},

pesquisarProdutoPorCodigo : function() {

	$("#precoCapa").val("");
	$("#peso").val("");
	$("#pacotePadrao").val("");
	
	produto.pesquisarPorCodigoProduto('#codigo', '#produto', '#edicao', true, function(){});	
},

pesquisarProdutoPorNome : function() {

	$("#precoCapa").val("");
	$("#peso").val("");
	$("#pacotePadrao").val("");
	
	produto.pesquisarPorNomeProduto('#codigo', '#produto', '#edicao', true, function(){});
},

validarNumeroEdicao : function() {

	$("#precoCapa").val("");
	$("#peso").val("");
	$("#pacotePadrao").val("");
	
	produto.validarNumEdicao('#codigo', '#edicao', true, jsDadosProduto.validarEdicaoCallBack);
},

validarEdicaoCallBack : function() {
	
	jsDadosProduto.exibirDetalhesProdutoEdicao();
		
}


};

	/**
	 * SELECIONA UM FORNECEDOR A PARTIR DO CNPJ DIGITADO.
	 */
	function pesquisarPorCnpjFornecedor() {
			
		var cnpj = $("#cnpj").val();	
		
		if(cnpj == "") {
			$("#fornecedor").val("");
			return;
		}
		
		$.postJSON(contextPath + '/estoque/recebimentoFisico/buscaCnpj', "cnpj=" + cnpj, 
		function(result) { 
			$("#fornecedor").val(result.cnpj);
		});	
	
	}
	
	/**
	 * EXIBI O CNPJ DO FORNECEDOR SELECIONADO.
	 */
	function exibirCnpjDoFornecedor() {
			
		var cnpjDoFornecedor = $("#fornecedor").val();	
		
		if(cnpjDoFornecedor == -1){
			document.getElementById('cnpj').value="";
			document.getElementById('cnpj').disabled=true;			
		}else{
			$("#cnpj").val(cnpjDoFornecedor);
			document.getElementById('cnpj').disabled=false;
		}
	
	}
	
	
	/**
	 * VERIFICA A EXISTENCIA DE UMA NOTAFISCAL
	 * COM OS PARÂMETROS DE PESQUISA
	 */
	function verificarNotaFiscalExistente() {
		
		var checkBox = document.getElementById('eNF');

		var cnpj 		= $("#cnpj").val();
		var notaFiscal 	= $("#notaFiscal").val();
		var serie 		= $("#serie").val();		
		var chaveAcesso = $("#chaveAcesso").val();
		var fornecedor  = $("#fornecedor").val();
        var indNFe      = "N";
        
        if(checkBox.checked){
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
		
		$.postJSON(	contextPath +  '/estoque/recebimentoFisico/verificarNotaFiscalExistente', 
					dadosPesquisa,
					confirmaNotaFiscalEncontrada);
	
	}

	/**
	 * SE UMA NOTA FOR ENCONTRADA, SERAO PESQUISADOS OS ITEM RELATIVOS A MESMA
	 * E POPULADA A GRID, CASO CONTRARIO, SERA EXIBIDA POPUP PARA SE CADASTRAR UMA
	 * NOVA NOTA.
	 */
	function confirmaNotaFiscalEncontrada(result) {
		
		var validacao = result.validacao;
		
		indNotaFiscalInterface = result.indNotaInterface;
		
		indRecebimentoFisicoConfirmado = result.indRecebimentoFisicoConfirmado;
		
				
		if (indNotaFiscalInterface){
    		carregarItemNotaGridNotaInterface();
    		
    	}else{
    		carregarItemNotaGridNotaManual();
    		
    	}
		
		if(validacao.tipoMensagem == "SUCCESS") {

			exibirMensagem(validacao.tipoMensagem, validacao.listaMensagens);
			
			pesquisarItemNotaGrid();

		} else {
			
			$(".grids").hide();
			
			popup_nova_nota();	
		
		}
		
	}
		
	/**
	 * APRESENTA O POPUP PARA CADASTRAR NOVO ITEM NOTA/RECEBIMENTO.
	 */
	function popup_novo_item() {
		
		var fornecedor = $("#fornecedor").val();
		
		if(fornecedor == "-1"){
			exibirMensagem('WARNING', ['Não é possível Adicionar Novo Produto para opção TODOS em Fornecedor']);
			return;
		}
		
		limparCamposNovoItem();
		
		$("#dialog-novo-item").dialog({
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
	}
	
	/**
	*INCLUIR NOVO ITEM DA NOTA
	*/
	function incluirNovoItemNota(){
		

		var codigo 				= $("#codigo").val();
		var produto 			= $("#produto").val();
		var precoCapa			= $("#precoCapa").val();
		var edicao 				= $("#edicao").val();
		var dataLancamento 		= $("#datepickerLancto").val();
		var dataRecolhimento 	= $("#datepickerRecolhimento").val();
		var repartePrevisto 	= $("#repartePrevisto").val();
		var tipoLancamento 		= $("#tipoLancamento").val();
		
		var dadosCadastro = 

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
		
		$.postJSON(contextPath + '/estoque/recebimentoFisico/incluirItemNotaFiscal', (dadosCadastro +"&" + listaDeValores), 

		function(result) {
			
		refreshItemNotaGrid();
		
		limparCamposNovoItem();
				
		}, null, true);
		
		
	}
	
	/**
	 * CADASTRA NOVO ITEM DE NOTA.
	 */
	function cadastrarNovoItemNota() {
		
		var codigo 				= $("#codigo").val();
		var produto 			= $("#produto").val();
		var precoCapa			= $("#precoCapa").val();
		var edicao 				= $("#edicao").val();
		var dataLancamento 		= $("#datepickerLancto").val();
		var dataRecolhimento 	= $("#datepickerRecolhimento").val();
		var repartePrevisto 	= $("#repartePrevisto").val();
		var tipoLancamento 		= $("#tipoLancamento").val();
		
		var dadosCadastro = 

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
		
		$.postJSON(contextPath + '/estoque/recebimentoFisico/incluirItemNotaFiscal', (dadosCadastro +"&" + listaDeValores), 

		function(result) {
			
		if(result.tipoMensagem == "SUCCESS") {
				
			$("#dialog-novo-item").dialog( "close" );
				
		} 
		
		refreshItemNotaGrid();
				
		}, null, true);
		
		
		
	}
	
	/**
	 * APRESENTA O POPUP PARA CADASTRAR NOVA NOTA FISCAL
	 */
	function popup_nova_nota() {
		
		$("#dialog-nova-nota").dialog({
			resizable: false,
			height:320,
			width:460,
			modal: true,
			buttons: {
				
				"Confirmar": cadastrarNovaNota,
				
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			beforeClose: function() {
				clearMessageDialogTimeout();
			}
		});	
			      
	}
	
	/**
	 * CADASTRA NOVA NOTA.
	 */
	function cadastrarNovaNota() {
		
		var cnpj 			= $("#cnpj").val();
		var notaFiscal 		= $("#notaFiscal").val();
		var serie 			= $("#serie").val();
		var chaveAcesso 	= $("#chaveAcesso").val();
		var dataEmissao 	= $("#dataEmissao").val();
		var dataEntrada 	= $("#dataEntrada").val();
		var valorBruto		= $("#valorBruto").val();
		var valorLiquido	= $("#valorLiquido").val();
		var valorDesconto 	= $("#valorDesconto").val();
		var cfopId 			= $("#cfop").val();
		var tipoNotaFiscal  = $("#tipoNotaFiscal").val();
		var fornecedor      = $("#fornecedor").val();
		
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
		
		$.postJSON(contextPath + '/estoque/recebimentoFisico/incluirNovaNotaFiscal', dadosCadastro, 
				function(result) {
			
			
					if(result.tipoMensagem == "SUCCESS") {
						
						$("#dialog-nova-nota").dialog( "close" );
						
					} 
					
					refreshItemNotaGrid();
				
		}, null, true);
		
		
	}
	
	/**
	* LIMPA OS CAMPOS DE PESQUISA DE NOTA FISCAL
	*/
	function limparCamposPesquisa() {
		
		$("#cnpj").val("");
		$("#notaFiscal").val("");
		$("#serie").val("");
		$("#chaveAcesso").val("");
		
	}
	
	/**
	* LIMPA OS CAMPOS DO CADASTRO DE NOVA NOTA E NOVO ITEM.
	*/
	function limparCampos() {
		
		$("#dataEmissao").val("");
		$("#valorBruto").val("");
		$("#valorLiquido").val("");
		$("#valorDesconto").val("");
		$("#cfop").val("");
		$("#tipoNotaFiscal").val("");
		
		$("#codigo").val("");
		$("#produto").val("");
		$("#precoCapa").val("");
		$("#edicao").val("");
		$("#datepickerLancto").val("");
		$("#datepickerRecolhimento").val("");
		$("#repartePrevisto").val("");
		$("#tipoLancamento").val("");		
	
	}

	/**
	* LIMPA OS CAMPOS DO CADASTRO DE NOVA NOTA E NOVO ITEM.
	*/
	function limparCamposNovoItem() {
		
		$("#codigo").val("");
		$("#produto").val("");
		$("#precoCapa").val("");
		$("#edicao").val("");
		$("#datepickerLancto").val("");
		$("#datepickerRecolhimento").val("");
		$("#repartePrevisto").val("");
		$("#tipoLancamento").val("");
		$("#peso").val("");
		$("#pacotePadrao").val("");
	
	}

	
	
	
	
	/**
	 * EFEITO PARA ABERTURA POPUP. 
	 */
	function callback() {
		setTimeout(function() {
			$( "#effect:visible").removeAttr("style" ).fadeOut();
	
		}, 1000 );
	}	
	
	/**
	 * CARREGA CAMPOS DE DATA COM CALENDARIO.
	 */
	$(function() {
		
			$("#datepickerLancto").datepicker({
				showOn: "button",
				buttonImage: contextPath + "/images/calendar.gif",
				buttonImageOnly: true,
				dateFormat: "dd/mm/yy"
			});
			$("#datepickerRecolhimento").datepicker({
				showOn: "button",
				buttonImage:  contextPath + "/images/calendar.gif",
				buttonImageOnly: true,
				dateFormat: "dd/mm/yy"
			});
			$("#dataEmissao").datepicker({
				showOn: "button",
				buttonImage:  contextPath + "/images/calendar.gif",
				buttonImageOnly: true,
				dateFormat: "dd/mm/yy"
			});
			
			$("#novoDataEmissao").datepicker({
				showOn: "button",
				buttonImage:  contextPath + "/images/calendar.gif",
				buttonImageOnly: true,
				dateFormat: "dd/mm/yy"
			});
			
			$("#novoDataEntrada").datepicker({
				showOn: "button",
				buttonImage:  contextPath + "/images/calendar.gif",
				buttonImageOnly: true,
				dateFormat: "dd/mm/yy"
			});
			
			$("#datepickerLancto").mask("99/99/9999");
			$("#datepickerRecolhimento").mask("99/99/9999");
			$("#dataEmissao").mask("99/99/9999");
			$("#novoDataEmissao").mask("99/99/9999");
			$("#novoDataEntrada").mask("99/99/9999");
			
			$("#valorBruto").maskMoney({
				 thousands:'.', 
				 decimal:',', 
				 precision:2
			});

			$("#valorLiquido").maskMoney({
				 thousands:'.', 
				 decimal:',', 
				 precision:2
			});
			
			$("#valorDesconto").maskMoney({
				 thousands:'.', 
				 decimal:',', 
				 precision:2
			});
			
			$("#novoValorTotal").maskMoney({
				 thousands:'.', 
				 decimal:',', 
				 precision:2
			});
			
			$("#itensRecebimento.qtdNotaItem").maskMoney({
				 thousands:'.', 
				 decimal:',', 
				 precision:2
			});
			
			$("#itensRecebimento.qtdPacoteItem").maskMoney({
				 thousands:'.', 
				 decimal:',', 
				 precision:2
			});
			
			$("#itensRecebimento.qtdExemplaresItem").maskMoney({
				 thousands:'.', 
				 decimal:',', 
				 precision:2
			});
			
			$("#itensRecebimento.codigoItem").numeric();
			$("#itensRecebimento.edicaoItem").numeric();
			$("#novoNumeroNota").numeric();
			$("#novoSerieNota").numeric();
			
			$("#produto").autocomplete({source: ""});
	});
	
	/**
	 * APRESENTA O CAMPO DE CHAVE DA NFE.
	 */
	function mostrar_nfes(){
		
		var checkBox = document.getElementById('eNF');
		
		if(checkBox.checked){
			$(".nfes").show();
			
		}else{
			$(".nfes").hide();
			$("#chaveAcesso").val("");
		}

	};
	
	/**
	 * ESTRUTURA DE COLUNAS DA GRID DE RESULTADO.
	 */
	function carregarItemNotaGridNotaInterface() {
						
		$(".gridWrapper").empty();
		
		$(".gridWrapper").append($("<table>").attr("class", "itemNotaGrid"));
		
		$(".itemNotaGrid").flexigrid({
			
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
			}],
		
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});
	}
	
	/**
	 * ESTRUTURA DE COLUNAS DA GRID DE RESULTADO.
	 */
	function carregarItemNotaGridNotaManual() {
				
		$(".gridWrapper").empty();
		
		$(".gridWrapper").append($("<table>").attr("class", "itemNotaGrid"));
		
		$(".itemNotaGrid").flexigrid({
			
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
	}
	
    /**
     * FAZ A PESQUISA DOS ITENS REFERENTES A NOTA ENCONTRADA.
     */
	function pesquisarItemNotaGrid() {
    	
		$(".itemNotaGrid").flexOptions({
			url: contextPath + '/estoque/recebimentoFisico/obterListaItemRecebimentoFisico',
			dataType : 'json'
		});
	
		$(".itemNotaGrid").flexReload();
		
		verificarQuantidade();
	
	}
    
    /**
    *FAZ A VERIFICACAO SE EXISTE QUANTIDADE NÃO PREENCHIDA PARA NOTA INTERFACE
    */
    function verificarQuantidade(){
    	$.postJSON( contextPath + '/estoque/recebimentoFisico/validarItensRecebimento', "");
    }

    /**
     * REFRESH DOS ITENS REFERENTES A NOTA ENCONTRADA.
     */
	function refreshItemNotaGrid() {
	
		$(".itemNotaGrid").flexOptions({
			url: contextPath + '/estoque/recebimentoFisico/refreshListaItemRecebimentoFisico',
			dataType : 'json'
		});
			
		$(".itemNotaGrid").flexReload();
	
	}

    /**
     * FAZ O CANCELAMENTO DE UMA NOTA FISCAL E SEU RECEBIMENTO FISICO.
     */
	function cancelarNotaRecebimentoFisico() {
    	
		$.postJSON(contextPath + '/estoque/recebimentoFisico/cancelarNotaRecebimentoFisico', "", 

		function(result) {
					
	    	if(result.tipoMensagem == "SUCCESS") {
				
	    		$(".grids").hide();
	    		
	    		limparCamposPesquisa();
	        	
	        	limparCamposNovoItem();
	        	
	        	limparCampos();
	        	
	        	exibirMensagem(result.tipoMensagem, result.listaMensagens);
	        	
			} 
	    	
		});
    	
    	
	}
	
    /**
     * SALVA OS DADOS DOS ITENS DA NOTA EDITADOS.
     */
	function salvarDadosItensDaNotaFiscal() {
		
		var listaDeValores  = "";
		
		if(indNotaFiscalInterface){
			listaDeValores = obterListaValores();
		}
		
		$.postJSON(contextPath + '/estoque/recebimentoFisico/salvarDadosItensDaNotaFiscal', listaDeValores, 
		function(result) {
			exibirMensagem(result.tipoMensagem, result.listaMensagens);
			refreshItemNotaGrid();
		
		});
		
	}

    /**
     * COFIRMA OS DADOS DOS ITENS DA NOTA EDITADOS.
     */
	function confirmarRecebimentoFisico() {
		
		var listaDeValores  = "";
		
		if(indNotaFiscalInterface){
			listaDeValores = obterListaValores();
		}
		
		$.postJSON(contextPath + '/estoque/recebimentoFisico/confirmarRecebimentoFisico', listaDeValores, 
		function(result) {
			exibirMensagem(result.tipoMensagem, result.listaMensagens);
			refreshItemNotaGrid();
		
		});
		
	}
    
    /**
     * OBTEM OS VALORES DA GRID A SEREM ENVIADOS AO SERVIDOR VIA AJAX
     */
	function obterListaValores() {
		
		var _index_qtd_pacote 	= 5;
		var _index_qtd_exemplar = 6;
		
		var linhasDaGrid = $(".itemNotaGrid tr");
		
		var listaDeValores = "";
		
		$.each(linhasDaGrid, function(index, value) {

			var colunaQtdPacote = $(value).find("td")[_index_qtd_pacote];
			var colunaQtdExemplar = $(value).find("td")[_index_qtd_exemplar];
			
			var qtdPacote 	= $(colunaQtdPacote).find("div").find('input[name="qtdPacote"]').val();
			var qtdExemplar = $(colunaQtdExemplar).find("div").find('input[name="qtdExemplar"]').val();
			var lineId 		= $(colunaQtdPacote).find("div").find('input[name="lineId"]').val();
			
			var itemRecebimento_lineId 		= 'itensRecebimento['+index+'].lineId='+lineId+'&';
			
			var itemRecebimento_qtdPacote 	= 'itensRecebimento['+index+'].qtdPacote='+qtdPacote+'&';
			
			var itemRecebimento_qtdExemplar = 'itensRecebimento['+index+'].qtdExemplar='+qtdExemplar+'&';
			
			listaDeValores = (listaDeValores + itemRecebimento_lineId + itemRecebimento_qtdPacote + itemRecebimento_qtdExemplar);
			
		});
		
		return listaDeValores;
		
	}
	
	/**
	 * EXCLUI UM ITEM DA NOTA
	 */
	function excluirItemNotaFiscal(lineId) {
		
		if(confirm("Deseja realmente excluir o item selecionado?")){
			var dadosExclusao = "lineId=" + lineId;
			
			var listaDeValores  = obterListaValores();
			
			$.postJSON(contextPath + '/estoque/recebimentoFisico/excluirItemNotaFiscal', (dadosExclusao + "&" + listaDeValores), 
			
			function(result) {
				exibirMensagem(result.tipoMensagem, result.listaMensagens);
				refreshItemNotaGrid();
			
			});
		}else{
			return;
		}		
	}
	
	
	

	/**
	 * PREPARA OS DADOS DA NOTA MANUAL A SEREM APRESENTADOS NA GRID.
	 */
	function getDataFromResultNotaManual(data) {	
		
		$.each(data.rows, function(index, value) {
			
			var edicaoItemRecFisicoPermitida 	= value.cell.edicaoItemRecFisicoPermitida;
			
			var lineId = value.cell.lineId;
	
			var imgExclusao = '<img src="'+contextPath+'/images/ico_excluir.gif" width="15" height="15" alt="Salvar" hspace="5" border="0" />'; 
			
			var imgEdicao = '<img src="'+contextPath+'/images/ico_editar.gif" width="15" height="15" alt="Salvar" hspace="5" border="0" />'; 
			
			if(edicaoItemRecFisicoPermitida == "S") {
				
				value.cell.acao = '<a href="javascript:;" onclick="excluirItemNotaFiscal('+[lineId]+');">' + imgExclusao + '</a>';
				
			} else{
				
				value.cell.acao = '<a href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)"  >' + imgExclusao + '</a>' + 
							 '<a href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)"  >' + imgEdicao   + '</a>';
				
			} 
			
			
		});
		
		if(data.rows) {

			$(".grids").show();
			
		}
		
		if(!indRecebimentoFisicoConfirmado){
			
			document.getElementById('botoesNormais').style.display="";			
			document.getElementById('botoesOpacos').style.display="none";
			document.getElementById('botaoNovoProdutoOpaco').style.display="none";
			document.getElementById('botaoAdicionarOpaco').style.display="none";
			document.getElementById('botaoNovoProduto').style.display="";
			
		}else{
			
			document.getElementById('botoesOpacos').style.display="";
			document.getElementById('botoesNormais').style.display="none";
			document.getElementById('botaoNovoProdutoOpaco').style.display="";
			document.getElementById('botaoAdicionarOpaco').style.display="";
			document.getElementById('botaoNovoProduto').style.display="none";
			
		}
		
		return data;

	}
	
	/**
	 * PREPARA OS DADOS DA NOTA INTERFACE A SEREM APRESENTADOS NA GRID.
	 */
	function getDataFromResultNotaInterface(data) {		
				
		$.each(data.rows, function(index, value) {
			
			var edicaoItemRecFisicoPermitida 	= value.cell.edicaoItemRecFisicoPermitida;
			var edicaoItemNotaPermitida 		= value.cell.edicaoItemRecFisicoPermitida;
			var destacarValorNegativo			= value.cell.destacarValorNegativo;
			
			var qtdPacote = value.cell.qtdPacote;
			
			var qtdExemplar = value.cell.qtdExemplar; 
			
			var diferenca = value.cell.diferenca;
			
			var lineId = value.cell.lineId;
			
			var hiddenFields = '<input type="hidden" name="lineId" value="'+lineId+'"/>';
			
			var imgExclusao = '<img src="'+contextPath+'/images/ico_excluir.gif" width="15" height="15" alt="Salvar" hspace="5" border="0" />'; 
			
			var imgEdicao = '<img src="'+contextPath+'/images/ico_editar.gif" width="15" height="15" alt="Salvar" hspace="5" border="0" />'; 
			
			var acoes = '<a href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)"  >' + imgExclusao + '</a>' + 
						'<a href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)"  >' + imgEdicao   + '</a>';
			
			
			if(destacarValorNegativo == "S") {
				value.cell.diferenca = '<span style="color: red">'+diferenca+'</span>';
			} else {
				value.cell.diferenca = '<span style="color: black">'+diferenca+'</span>';
			}
			
			
			if(edicaoItemRecFisicoPermitida == "S") {
				value.cell.qtdPacote 	=  '<input name="qtdPacote" style="width: 45px;" type="text" value="'+qtdPacote+'"/>'+hiddenFields;
				value.cell.qtdExemplar = '<input name="qtdExemplar" style="width: 45px;" type="text" value="'+qtdExemplar+'"/>';
			} else {
				value.cell.qtdPacote 	= '<input name="qtdPacote" disabled="disabled" style="width: 45px;" type="text" value="'+qtdPacote+'"/>'+hiddenFields;
				value.cell.qtdExemplar 	=  '<input name="qtdExemplar" disabled="disabled" style="width: 45px;" type="text" value="'+qtdExemplar+'"/>';
			}
			
			if(edicaoItemNotaPermitida == "S") {
				value.cell.acao = acoes;
			} else {
				value.cell.acao = acoes;
			}
			
			
		});

		
		
		$(".grids").show();
		
		if(!indRecebimentoFisicoConfirmado){
			document.getElementById('botoesNormais').style.display="";
			
			document.getElementById('botaoNovoProdutoOpaco').style.display="none";
			document.getElementById('botaoNovoProduto').style.display="";
			
			document.getElementById('botaoAdicionarOpaco').style.display="none";
			document.getElementById('botaoAdicionar').style.display="";
			
			document.getElementById('botoesOpacos').style.display="none";
		}else{
			document.getElementById('botoesOpacos').style.display="";
			document.getElementById('botoesNormais').style.display="none";
			
			document.getElementById('botaoNovoProdutoOpaco').style.display="";
			document.getElementById('botaoNovoProduto').style.display="none";
			
			document.getElementById('botaoAdicionarOpaco').style.display="";
			document.getElementById('botaoAdicionar').style.display="none";
			
		}	
		
		
		return data;

	}
	
	
	
	// --------------------- conteudo abaixo do popup de nota ----------------------------
	

	/*NOVO POPUP DE LANÇAMENTO DE NOTA*/
	
	$(function() {
		$(".novoItemNotaGrid").flexigrid({
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
	});
	
	function popup_adicionar() {
	
		montaGridItens();
		
		$( "#dialog-adicionar" ).dialog({
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
	};

	function pesquisarFornecedorCnpj() {
		
		var pCnpj = $("#novoCnpj").val();	

		if(pCnpj == "") {
			$("#novoFornecedor").val("");
			return;
		}
		
		$.postJSON(contextPath + '/estoque/recebimentoFisico/buscaCnpj', "cnpj=" + pCnpj, 
		function(result) { 
			$("#novoFornecedor").val(result.cnpj);
		});	
	}

	function pesquisarCnpjFornecedor() {
	
		var idFornecedor = $("#novoFornecedor").val();
		
		if(idFornecedor == -1){
			$("#novoCnpj").val("");
			$("#novoCnpj").disabled(true);			
		}else{
			$.postJSON(contextPath + '/estoque/recebimentoFisico/obterCnpjFornecedor', "idFornecedor=" + idFornecedor, 
			function(result) {
				$("#novoCnpj").val(result);
				document.getElementById('novoCnpj').disabled=false;
			},
			null,
			true);
		}
	}
	
	function obterDadosEdicao(index) {
		
		var codigo = $("#codigoItem"+index).val();
		var edicao = $("#edicaoItem"+index).val();	

		if((codigo == "")||(edicao == "")) {
			$("#precoDescontoItem"+index).val("");
			return;
		}
		
		$.postJSON(contextPath + '/estoque/recebimentoFisico/obterDadosEdicao', "codigo=" + codigo + "&edicao="+edicao, 
			function(result) { 
				$("#precoDescontoItem"+index).val(result.precoDesconto);
			},
			function(result) {
				$("#precoDescontoItem"+index).val("");	   
			},
			true
		);	
	}
	
	function calcularDiferencaEValorItem(index){
		
		var preco = removeMascaraPriceFormat($("#precoDescontoItem"+index).val());
		var quantidade = removeMascaraPriceFormat($("#qtdNotaItem"+index).val());
		var quantidadeExemp = removeMascaraPriceFormat($("#qtdExemplaresItem"+index).val());
		
		if((preco == "")||(quantidade == "")) {
			$("#valorItem"+index).val("");
			return;
		}
		
		if((quantidade == "")||(quantidadeExemp == "")) {
			$("#diferencaItem"+index).val("");
		}
		
        var valor = intValue(preco) * intValue(quantidade);
        
        $("#valorItem"+index).val(valor);
        
        $("#valorItem"+index).priceFormat({
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
        
		$("#diferencaItem"+index).val(diferenca);
		
	    $("#diferencaItem"+index).priceFormat({
			allowNegative: true,
			centsSeparator: '',
		    thousandsSeparator: '.'
		});
	    
	    obterValorTotalItens();
	}
	
	function obterCabecalho() {

		var dadosCabecalho = "";
		
		var fornecedor = $("#novoFornecedor").val();
		var cnpj = $("#novoCnpj").val();
		var numeroNota = $("#novoNumeroNota").val();
        var serieNota = $("#novoSerieNota").val();
        var checkNfe = $("#novoNfe").val();
        var chaveAcesso = $("#novoChaveAcesso").val();
        var dataEmissao = $("#novoDataEmissao").val();
        var dataEntrada = $("#novoDataEntrada").val();
        var valorTotal = $("#novoValorTotal").val();
		
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
	}

    function montaGridItens() {
		$(".novoItemNotaGrid").flexOptions({url: contextPath + '/estoque/recebimentoFisico/montaGridItemNota'});
		$(".novoItemNotaGrid").flexReload();
		$(".grids").show();
	}	
	
	function getDataFromResultItem(resultado) {

		if (resultado.mensagens) {
			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			$(".grids").hide();
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
	}
	
	function isAtributosLancamentoVazios(codigo, produto, edicao, precoDesconto, qtdNota, qtdPacote, qtdExemplares) {

		if (!$.trim(codigo) 
				&& !$.trim(produto)
				&& !$.trim(edicao) 
				&& !$.trim(precoDesconto)
				&& !$.trim(qtdNota)
				&& !$.trim(qtdPacote)
				&& !$.trim(qtdExemplares)) {

			return true;
		}
	}
	
	function obterListaItens() {

		var linhasDaGrid = $(".novoItemNotaGrid tr");

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

	            var dataLancamento = $("#novoDataEntrada").val();
				
			    var dataRecolhimento = $("#novoDataEmissao").val();
	
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
	}

	function obterValorTotalItens() {

		var linhasDaGrid = $(".novoItemNotaGrid tr");

		var listaItens = "";
		
		var valorTotal = 0;

		$.each(linhasDaGrid, function(index, value) {

			var linha = $(value);

			var colunaValor = linha.find("td")[8];

			valorTotal += intValue(removeMascaraPriceFormat($(colunaValor).find("div").find('input[name="itensRecebimento.valorItem"]').val()));
			
		});

        $("#novoValorTotal").val(valorTotal);
        
        $("#novoValorTotal").priceFormat({
			allowNegative: true,
			centsSeparator: ',',
		    thousandsSeparator: '.'
		});
        
        $("#labelValorTotal").html($("#novoValorTotal").val());
	}
    
    function replicarQuantidadeItem(index){
    	if ( (document.getElementById("checkbox"+index).checked) || (document.getElementById("novoReplicarQtde").checked) ){
    	    $("#qtdExemplaresItem"+index).val($("#qtdNotaItem"+(index)).val());
    	}    
    }

    function incluiNovoItem(){
    	
		formData = obterListaItens();
		$(".novoItemNotaGrid").flexOptions({url: contextPath + '/estoque/recebimentoFisico/incluirItemNota?'+formData});
		$(".novoItemNotaGrid").flexReload();
		$(".grids").show(); 
	}
    
	function selecionarTodos(checked){
		
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
	}
    
	function limparCamposNovaNota(){
		
		$("#novoFornecedor").val(-1);
		$("#novoCnpj").val("");
		$("#novoNumeroNota").val("");
        $("#novoSerieNota").val("");
        $("#novoNfe").val("");
        $("#novoChaveAcesso").val("");
        $("#novoDataEmissao").val("");
        $("#novoDataEntrada").val("");
        $("#novoValorTotal").val("");
        
        montaGridItens();
	}
	
	function incluirNota() {

		var url;
		var formData;

		formData = obterCabecalho() + obterListaItens();
		url = contextPath + '/estoque/recebimentoFisico/incluirNota';

		$.postJSON(
			url,
			formData,
			function(result) {
				
				limparCamposNovaNota();
				
				$("#dialog-adicionar").dialog( "close" );
	
				exibirMensagem(
					result.tipoMensagem, 
					result.listaMensagens
				);  
			},
			null,
			true
		);
	}
	
	
	