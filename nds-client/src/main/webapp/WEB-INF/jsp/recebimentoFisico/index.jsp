<head>

<script language="javascript" type="text/javascript" src='<c:url value="/"/>scripts/produto.js'></script>
<script language="javascript" type="text/javascript" src='<c:url value="/"/>/scripts/jquery.numeric.js'></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<script type="text/javascript">

var indNotaFiscalInterface = false;

var indRecebimentoFisicoConfirmado = false;

var indTodosFornecedor = false;

var jsDadosProduto = {

exibirDetalhesProdutoEdicao : function() {
	
	var data = "codigo=" + $("#codigo").val() + "&edicao=" + $("#edicao").val();
	
	$.postJSON('<c:url value="/"/>estoque/recebimentoFisico/obterProdutoEdicao', data, function(result){
		
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
		
		$.postJSON("<c:url value='/estoque/recebimentoFisico/buscaCnpj'/>", "cnpj=" + cnpj, 
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
		
	checkBox = document.getElementById('eNF');
		
		

		var cnpj 		= $("#cnpj").val();
		var notaFiscal 	= $("#notaFiscal").val();
		var serie 		= $("#serie").val();		
		var chaveAcesso = $("#chaveAcesso").val();
		var fornecedor  = $("#fornecedor").val();
        var indNFe      = "N";
        
        if(checkBox.checked){
			indNFe = "S";
			
		}
        
		var dadosPesquisa = 
			"cnpj=" 			+ cnpj			+ "&" +
			"numeroNotaFiscal=" + notaFiscal 	+ "&" + 
		   	"serie=" 			+ serie			+ "&" +
		 	"indNFe=" 			+ indNFe		+ "&" +
		 	"fornecedor=" 		+ fornecedor	+ "&" +
		    "chaveAcesso=" 		+ chaveAcesso;
		
		limparCampos();
		
		$.postJSON("<c:url value='/estoque/recebimentoFisico/verificarNotaFiscalExistente'/>", 
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
		
		$.postJSON("<c:url value='/estoque/recebimentoFisico/incluirItemNotaFiscal'/>", (dadosCadastro +"&" + listaDeValores), 

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
		
		$.postJSON("<c:url value='/estoque/recebimentoFisico/incluirItemNotaFiscal'/>", (dadosCadastro +"&" + listaDeValores), 

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
		
		$.postJSON("<c:url value='/estoque/recebimentoFisico/incluirNovaNotaFiscal'/>", dadosCadastro, 
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
				buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
				buttonImageOnly: true,
				dateFormat: "dd/mm/yy"
			});
			$("#datepickerRecolhimento").datepicker({
				showOn: "button",
				buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
				buttonImageOnly: true,
				dateFormat: "dd/mm/yy"
			});
			$("#dataEmissao").datepicker({
				showOn: "button",
				buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
				buttonImageOnly: true,
				dateFormat: "dd/mm/yy"
			});
			
			$("#novoDataEmissao").datepicker({
				showOn: "button",
				buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
				buttonImageOnly: true,
				dateFormat: "dd/mm/yy"
			});
			
			$("#novoDataEntrada").datepicker({
				showOn: "button",
				buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
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
		checkBox = document.getElementById('eNF');
		
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
				name : 'produto',
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
				display : 'Qtd. Físico',
				name : 'qtdFisico',
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
				name : 'produto',
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
			url: '<c:url value="/"/>estoque/recebimentoFisico/obterListaItemRecebimentoFisico',
			dataType : 'json'
		});
	
		$(".itemNotaGrid").flexReload();
		
		verificarQuantidade();
	
	}
    
    /**
    *FAZ A VERIFICACAO SE EXISTE QUANTIDADE NÃO PREENCHIDA PARA NOTA INTERFACE
    */
    function verificarQuantidade(){
    	$.postJSON("<c:url value='/estoque/recebimentoFisico/validarItensRecebimento'/>", "");
    }

    /**
     * REFRESH DOS ITENS REFERENTES A NOTA ENCONTRADA.
     */
	function refreshItemNotaGrid() {
	
		$(".itemNotaGrid").flexOptions({
			url: '<c:url value="/"/>estoque/recebimentoFisico/refreshListaItemRecebimentoFisico',
			dataType : 'json'
		});
			
		$(".itemNotaGrid").flexReload();
	
	}

    /**
     * FAZ O CANCELAMENTO DE UMA NOTA FISCAL E SEU RECEBIMENTO FISICO.
     */
	function cancelarNotaRecebimentoFisico() {
    	
		$.postJSON("<c:url value='/estoque/recebimentoFisico/cancelarNotaRecebimentoFisico'/>", "", 

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
		
		$.postJSON("<c:url value='/estoque/recebimentoFisico/salvarDadosItensDaNotaFiscal'/>", listaDeValores, 
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
		
		$.postJSON("<c:url value='/estoque/recebimentoFisico/confirmarRecebimentoFisico'/>", listaDeValores, 
		function(result) {
			exibirMensagem(result.tipoMensagem, result.listaMensagens);
			refreshItemNotaGrid();
		
		});
		
	}
    
    /**
     * OBTEM OS VALORES DA GRID A SEREM ENVIADOS AO SERVIDOR VIA AJAX
     */
	function obterListaValores() {
		
		var linhasDaGrid = $(".itemNotaGrid tr");
		
		var listaDeValores = "";
		
		$.each(linhasDaGrid, function(index, value) {

			var colunaQtdFisico = $(value).find("td")[5];
			
			var qtdFisico = $(colunaQtdFisico).find("div").find('input[name="qtdFisico"]').val();
			
			var lineId = $(colunaQtdFisico).find("div").find('input[name="lineId"]').val();
			
			var itemRecebimento_lineId = 'itensRecebimento['+index+'].lineId='+lineId+'&';
			
			var itemRecebimento_qtdFisico = 'itensRecebimento['+index+'].qtdFisico='+qtdFisico+'&';
			
			listaDeValores = (listaDeValores + itemRecebimento_lineId + itemRecebimento_qtdFisico);
			
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
			
			$.postJSON("<c:url value='/estoque/recebimentoFisico/excluirItemNotaFiscal'/>", (dadosExclusao + "&" + listaDeValores), 
			
			function(result) {
				exibirMensagem(result.tipoMensagem, result.listaMensagens);
				refreshItemNotaGrid();
			
			});
		}else{
			return;
		}		
	}
	
	function destacarValorNegativo(data) {

		$.each(data.rows, function(index, value) {

			var destacarValorNegativo = value.cell[9];
	
			var diferenca = value.cell[6];
	
			if(destacarValorNegativo == "S") {
	
				value.cell[6] = '<span style="color: red">'+diferenca+'</span>';
	
			} else {
	
				value.cell[6] = '<span style="color: black">'+diferenca+'</span>';
	
			}
			
		});

		return data;

	}
	

	/**
	 * PREPARA OS DADOS DA NOTA MANUAL A SEREM APRESENTADOS NA GRID.
	 */
	function getDataFromResultNotaManual(data) {	
		
		$.each(data.rows, function(index, value) {
			
			var alteracaoPermitida = value.cell[6];
			
			var lineId = value.id;
	
			var imgExclusao = '<img src="'+contextPath+'/images/ico_excluir.gif" width="15" height="15" alt="Salvar" hspace="5" border="0" />'; 
			
			if(alteracaoPermitida == "S") {				
				value.cell[6] = '<a href="javascript:;" onclick="excluirItemNotaFiscal('+[lineId]+');">' + imgExclusao + '</a>';
			} else{
				value.cell[6] = '<a href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)"  >'+imgExclusao+'</a>';
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
			
			var qtdFisico = value.cell[5];
			
			var alteracaoPermitida = value.cell[8];
			
			var lineId = value.id;
			
			var hiddeFields = '<input type="hidden" name="lineId" value="'+lineId+'"/>';
			
			var imgExclusao = '<img src="'+contextPath+'/images/ico_excluir.gif" width="15" height="15" alt="Salvar" hspace="5" border="0" />'; 
			
			if(alteracaoPermitida == "S") {
				value.cell[5] = '<input name="qtdFisico" style="width: 45px;" type="text" value="'+qtdFisico+'"/>'+hiddeFields;
				value.cell[8] = '<a href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)"  >'+imgExclusao+'</a>';
			} else {
				value.cell[5] = '<input name="qtdFisico" disabled="disabled" style="width: 45px;" type="text" value="'+qtdFisico+'"/>'+hiddeFields;
				value.cell[8] = '<a href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)"  >'+imgExclusao+'</a>';
			}
			
			
		});

		data = destacarValorNegativo(data);
		
		
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
			    limparCamposNovaNota();
	        }
		});
	};

	function pesquisarFornecedorCnpj() {
		
		var pCnpj = $("#novoCnpj").val();	

		if(pCnpj == "") {
			$("#novoFornecedor").val("");
			return;
		}
		
		$.postJSON("<c:url value='/estoque/recebimentoFisico/buscaCnpj'/>", "cnpj=" + pCnpj, 
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
			$.postJSON("<c:url value='/estoque/recebimentoFisico/obterCnpjFornecedor'/>", "idFornecedor=" + idFornecedor, 
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
		
		$.postJSON("<c:url value='/estoque/recebimentoFisico/obterDadosEdicao'/>", "codigo=" + codigo + "&edicao="+edicao, 
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

		$(".novoItemNotaGrid").flexOptions({url: "<c:url value='/estoque/recebimentoFisico/montaGridItemNota' />"});
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

        	 var codigo =       '<input maxlength="28" value="'+valueCodigo+'" type="number" name="itensRecebimento.codigoItem" id="codigoItem'+ index +'" style="width: 50px;" onchange="produto.pesquisarPorCodigoProduto(\'#codigoItem'+ index +'\', \'#produtoItem'+ index +'\', \'#edicaoItem'+ index +'\', true, null);" ></input>';
	         			 					     
	         var produto =      '<input maxlength="200" value="'+valueProduto+'" type="text" name="itensRecebimento.produtoItem" id="produtoItem'+ index +'" style="width: 140px;" onkeyup="produto.autoCompletarPorNomeProduto(\'#produtoItem'+ index +'\', false);" onblur="produto.pesquisarPorNomeProduto(\'#codigoItem'+ index +'\', \'#produtoItem'+ index +'\', \'#edicaoItem'+ index +'\', true, null);"></input>';
				             
			 var edicao =       '<input maxlength="18" value="'+valueEdicao+'" type="number" name="itensRecebimento.edicaoItem" id="edicaoItem'+ index +'" style="width: 30px;" onkeyup="obterDadosEdicao('+index+');"></input>';         
			
			 var precoDesconto ='<input maxlength="17" value="'+valuePrecoDesconto+'" type="number" readonly="readonly" name="itensRecebimento.precoDescontoItem" id="precoDescontoItem'+ index +'" style="width: 80px; border: 0px; background-color: inherit;"></input>';
			 
			 var qtdNota =      '<input maxlength="17" value="'+valueQtdNota+'" type="number" name="itensRecebimento.qtdNotaItem" id="qtdNotaItem'+ index +'" style="width: 70px;" onchange="replicarQuantidadeItem('+index+'); calcularDiferencaEValorItem('+index+');"></input>';
			     
	         var qtdPacote =    '<input maxlength="17" value="'+valueQtdPacote+'" type="number" name="itensRecebimento.qtdPacoteItem" id="qtdPacoteItem'+ index +'" style="width: 70px;" onchange="calcularDiferencaEValorItem('+index+');"></input>';
				             
			 var qtdExemplares ='<input maxlength="17" value="'+valueQtdExemplares+'" type="number" name="itensRecebimento.qtdExemplaresItem" id="qtdExemplaresItem'+ index +'" style="width: 70px;" onchange="calcularDiferencaEValorItem('+index+');"></input>'; 
				
			 var diferenca =    '<input maxlength="17" value="'+valueDiferenca+'" type="number" readonly="readonly" name="itensRecebimento.diferencaItem" id="diferencaItem'+ index +'" style="width: 70px; border: 0px; background-color: inherit;"></input>';
				 
			 var valor =        '<input maxlength="17" value="'+valueValor+'" type="number" readonly="readonly" name="itensRecebimento.valorItem" id="valorItem'+ index +'" style="width: 70px; border: 0px; background-color: inherit;"></input>';
						 
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

    
    
    
    //PREPARA NOVA LINHA DA GRID
    function adicionarNovaLinha(index){

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
    }
    
    //ADICIONA NOVA LINHA NA GRID
    function incluiNovoItem(){
    	
    	var data = [];

    	var dataValores = [];
    	var rowValores;
    	
    	var idx;
    	
    	//OBTEM VALORES DIGITADOS
    	var nLinhas = 0;
    	var linhasDaGrid = $(".novoItemNotaGrid tr");
    	
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

    	$(".novoItemNotaGrid").flexAddData({
            rows : toFlexiGridObject(data),
            page : 1,
            total : nLinhas+1
        });

    	//RETORNA OS DADOS DIGITADOS PARA AS LINHAS ATUAIS
    	recuperaValoresDigitados(dataValores);
	}
    
    //RECUPERA VALORES DIGITADOS ANTES DA INSERÇÃO DA NOVA LINHA
    function recuperaValoresDigitados(dataValores){
    	
    	var linhasDaGrid = $(".novoItemNotaGrid tr");
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
	}
	
	function incluirNota() {

		var url;
		var formData;

		formData = obterCabecalho() + obterListaItens();
		url = '<c:url value="/estoque/recebimentoFisico/incluirNota" />';

		$.postJSON(
			url,
			formData,
			function(result) {

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
	
	
	
	
</script>

<style type="text/css">
    fieldset label {width: auto; margin-bottom: 0px!important;}
    .nfes{display:none;}
</style>

</head>

<body>

	<div id="dialog-nova-nota" style="display: none;" title="Nova Nota Fiscal">
			
			<jsp:include page="../messagesDialog.jsp" />
			
			<table width="439" cellpadding="2" cellspacing="2"
				style="text-align: left;">
				<tr>
					<td width="127">Emissão:</td>
					<td width="296">
						<input 	type="text"
								style="width: 80px"
								name="dataEmissao"
								id="dataEmissao" />
					</td>
				</tr>
				<tr>
					<td>Entrada:</td>
					<td>
						<input 	disabled="disabled"
								type="text" 
								value="${dataAtual}" 
								style="width: 80px" 
								id="dataEntrada" />
					</td>
				</tr>
				<tr>
					<td>Valor Bruto R$:</td>
					<td>
						<input 	type="text"
								id="valorBruto" 
								style="width: 80px" />
					</td>
				</tr>
				<tr>
					<td>Valor Líquido R$:</td>
					<td>
						<input 	type="text"
								id="valorLiquido" 
								style="width: 80px" />
					</td>
				</tr>
				<tr>
					<td>Valor Desconto R$:</td>
					<td>
						<input 	type="text"
								id="valorDesconto" 
								style="width: 80px" />
					</td>
				</tr>
				<tr>
					<td>CFOP:</td>
					<td>
					
						<select id="cfop" style="width: 250px;">
								<option value=""></option>
								<c:forEach var="cfop" items="${listacfop}">
									<option value="${cfop.id}">${cfop.codigo} - ${cfop.descricao}</option>
								</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td>Tipo Nota Fiscal:</td>
					<td>
					
						<select id="tipoNotaFiscal" style="width: 250px;">
								<option value=""></option>
								<c:forEach var="tipoNotaFiscal" items="${listaTipoNotaFiscal}">
									<option value="${tipoNotaFiscal.id}">${tipoNotaFiscal.descricao}</option>
								</c:forEach>
						</select>
					</td>
				</tr>
			</table>

	</div>


	<div id="dialog-novo-item" style="display: none;" title="Recebimento Físico">

		<jsp:include page="../messagesDialog.jsp" />
	
		<table width="341" border="0" cellspacing="2" cellpadding="2">
			<tr>
				<td>Código:</td>
				<td width="202">
					<input 
					type="text"
					id="codigo"
					maxlength="255"
					style="width: 80px; float: left; margin-right: 5px;"
					onchange="jsDadosProduto.pesquisarProdutoPorCodigo();"/>
					
				</td>
			</tr>
			<tr>
				<td>Produto:</td>
				<td width="202">
					<input 
						maxlength="255"
						type="text" 
						id="produto"
						
					       	   onkeyup="produto.autoCompletarPorNomeProduto('#produto', false);"
					       	   onblur="jsDadosProduto.pesquisarProdutoPorNome();"/>
				</td>
			</tr>
			<tr>
				<td>Edição:</td>
				<td><input 
					type="text" 
					id="edicao" maxlength="20"
					style="width: 80px;" 
					onchange="jsDadosProduto.validarNumeroEdicao();"/>
				</td>
			</tr>
			<tr>
				<td>Data Lançamento:</td>
				<td><input 	type="text" 
							name="datepickerLancto"
							id="datepickerLancto" 
							style="width: 80px;" />
				</td>
			</tr>
			<tr>
				<td>Data Recolhimento:</td>
				<td><input 	type="text" 
							name="datepickerRecolhimento"
							id="datepickerRecolhimento" 
							style="width: 80px;" />
				</td>
			</tr>
			<tr>
				<td>Preço R$:</td>
				<td><input 	
							disabled="disabled"
							type="text" 
							id="precoCapa"
							style="width: 80px;" />
				</td>
			</tr>
			<tr>
				<td>Peso:</td>
				<td><input 	disabled="disabled"
							type="text" 
							id="peso"
							style="width: 80px;" />
				</td>
			</tr>
			<tr>
				<td>Pacote Padrão:</td>
				<td><input 	disabled="disabled"
							type="text" 
							id="pacotePadrao"
							style="width: 200px;" />
				</td>
			</tr>
			<tr>
				<td>Reparte Previsto:</td>
				<td><input 	type="text" 
							id="repartePrevisto"
							style="width: 80px;" />
				</td>
			</tr>
			<tr>
				<td>
					Lançamento: 
				</td>				
				<td>
					<select name="tipoLancamento"
						id="tipoLancamento" style="width: 250px;">
							<option value=""></option>
							<c:forEach var="tipoLancamento" items="${listaTipoLancamento}">
								<option value="${tipoLancamento}">${tipoLancamento}</option>
							</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
			    <td>&nbsp;</td>			
			    <td><span class="bt_incluir_novo" title="Incluir Nova Linha"><a href="javascript:;" onclick="incluirNovoItemNota();"><img src="${pageContext.request.contextPath}/images/ico_add_novo.gif" alt="Incluir Novo" width="16" height="16" border="0" hspace="5" />Incluir Novo</a></span></td>			
			 </tr>			
		</table>

	</div>


	<div class="corpo">

		<br clear="all" /> <br />

		<div class="container">

			<div id="effect" style="padding: 0 .7em;"
				class="ui-state-highlight ui-corner-all">
				<p>
					<span style="float: left; margin-right: .3em;"
						class="ui-icon ui-icon-info"></span> <b>Recebimento Físico <
						evento > com < status >.</b>
				</p>
			</div>

			<fieldset class="classFieldset">
			
				<legend> Pesquisar Recebimento Físico</legend>
				
				<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">

					<tr>
						<td width="86">Fornecedor:</td>
						
						<td width="254"><select id="fornecedor" name="fornecedor"
							onblur="exibirCnpjDoFornecedor()" style="width: 250px;">
								<option value="-1">Todos</option>
								<c:forEach var="fornecedor" items="${listafornecedores}">
									<option value="${fornecedor.juridica.cnpj}">${fornecedor.juridica.razaoSocial}</option>
								</c:forEach>
						</select></td>
						
						<td width="43" align="right">CNPJ:</td>
						<td width="136"><input id="cnpj"
							onblur="pesquisarPorCnpjFornecedor();" disabled="disabled" name="cnpj"
							style="width: 130px;" />
						</td>
												
						<td width="76">Nota Fiscal:</td>
						<td width="123"><input type="text" id=notaFiscal
							style="width: 100px;" />
						</td>
						<td width="33">Série:</td>
						<td width="43"><input id="serie" type="text"
							style="width: 30px;" />
						</td>
						<td width="110"><span class="bt_pesquisar"
							title="Pesquisar Recebimento"> <a href="javascript:;"
								onclick="verificarNotaFiscalExistente();">Pesquisar</a> </span>
						</td>

					</tr>
					<tr>
						<td colspan="4" height="26">
						
							<label for="eNF">É uma NF-e?</label>
							
							<input type="checkbox" name="checkbox8" id="eNF" onchange="mostrar_nfes();" style="float: left; margin-right: 10px;" /> 
							
							<span id="nfes" class="nfes"> 
							
							Chave de Acesso: 
							
							<input type="text" name="chaveAcesso" id="chaveAcesso"
								style="width: 120px; margin-left: 10px;" />
						</span>
						
						</td>
						
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td></span></td>
					</tr>
				</table>
				
			</fieldset>
			
			<div class="linha_separa_fields">&nbsp;</div>

			<fieldset class="classFieldset">
			
				<legend>Recebimentos Físico Cadastrados</legend>
				
				<div class="grids" style="display: none;">
				
					<div class="gridWrapper">
					
						<table class="itemNotaGrid"></table>
					
					</div>
					
					<div id="botaoNovoProdutoOpaco">
							<span class="bt_incluir_novo" id="bt_novo_produtoOpaco" title="Incluir Nova Linha"> 
								<a href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)"> 
									<img src="${pageContext.request.contextPath}/images/ico_add_novo.gif" border="0" hspace="5" />
									Novo Produto 
								</a> 
							</span>
					</div>

					<div id="botaoAdicionarOpaco">
							<span class="bt_incluir_novo" id="bt_adicionarOpaco" title="Adicionar Nota Fiscal"> 
								<a href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)"> 
									<img src="${pageContext.request.contextPath}/images/ico_add_novo.gif" border="0" hspace="5" />
									Adicionar Nota Fiscal 
								</a> 
							</span>
					</div>

					<div id="botaoNovoProduto">
							<span class="bt_incluir_novo" id="bt_novo_produto" title="Incluir Nova Linha"> 
								<a href="javascript:;" onclick="popup_novo_item();"> 
									<img src="${pageContext.request.contextPath}/images/ico_add_novo.gif" border="0" hspace="5" />
									Novo Produto 
								</a> 
							</span>
					</div>	
					
					<div id="botaoAdicionar">
							<span class="bt_incluir_novo" id="bt_adicionar" title="Adicionar Nota Fiscal"> 
								<a href="javascript:;" onclick="popup_adicionar();">  
									<img src="${pageContext.request.contextPath}/images/ico_add_novo.gif" border="0" hspace="5" />
									Adicionar Nota Fiscal 
								</a> 
							</span>
					</div>
					
					<div id="botoesNormais">	
														
						<span class="bt_novos" title="Salvar"> 
							<a href="javascript:;" onclick="salvarDadosItensDaNotaFiscal()">
								<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" width="19" height="17" alt="Salvar" hspace="5" border="0" />
								Salvar 
							</a> 
						</span>
						
						<span class="bt_novos" title="Cancelar"> 
							<a href="javascript:;" onclick="cancelarNotaRecebimentoFisico()">
								<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" width="19" height="17" alt="Salvar" hspace="5" border="0" />
								Cancelar 
							</a> 
						</span>
						
						<span class="bt_confirmar_novo" title="Confirmar Recebimento Físico">
							<a href="javascript:;" onclick="confirmarRecebimentoFisico()">
								<img src="${pageContext.request.contextPath}/images/ico_check.gif" width="16" height="16" alt="Confirmar" border="0" hspace="5"/>
								Confirmar
							</a>
						</span>
					</div>	
					
					<div id="botoesOpacos">
						
						<span class="bt_novos" title="Salvar"> 
							<a href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)"> 
								<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" width="19" height="17" alt="Salvar" hspace="5" border="0" />
								Salvar 
							</a> 
						</span>
						
						<span class="bt_novos" title="Cancelar"> 
							<a href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)"> 
								<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" width="19" height="17" alt="Salvar" hspace="5" border="0" />
								Cancelar 
							</a> 
						</span>
						
						<span class="bt_confirmar_novo" title="Confirmar Recebimento Físico">
							<a href="javascript:;" style="opacity:0.4; filter:alpha(opacity=40)"> 
								<img src="${pageContext.request.contextPath}/images/ico_check.gif" width="16" height="16" alt="Confirmar" border="0" hspace="5"/>
								Confirmar
							</a>
						</span>						
						 
					</div>
					
				</div>

			</fieldset>

			<div class="linha_separa_fields">&nbsp;</div>

		</div>

	</div>
	
	
	

	<!-- NOVO POPUP DE CADASTRO DE NOTA -->
	
	<div id="dialog-adicionar" title="Recebimento Físico" style="display: none;" >
	
	    <jsp:include page="../messagesDialog.jsp" />
		
		<fieldset style="width:910px!important;">
		  <legend>Dados da Nota</legend>
		  <table width="885" cellpadding="2" cellspacing="2" style="text-align:left;">
		   
		    <tr style="width: 25%">

		        <td width="89">Fornecedor:</td>
		        <td width="168">
			        <select name="select" id="novoFornecedor" name="novoFornecedor" onchange="pesquisarCnpjFornecedor();" style="width:160px ">
				        <option selected="selected" value="">Selecione...</option>
						<c:forEach var="fornecedor" items="${listafornecedores}">
						    <option value="${fornecedor.id}">${fornecedor.juridica.razaoSocial}</option>
						</c:forEach>
			        </select>
		        </td>
		        
		        <td width="95">CNPJ:</td>
		        <td width="132">
		            <input maxlength="200" type="text" style="width:100px " id="novoCnpj" name="novoCnpj" onchange="pesquisarFornecedorCnpj();" />
		        </td>
		        
		        <td width="102">Nota Fiscal:</td>
		        <td width="115">
		            <input maxlength="18" type="text" style="width:100px " id="novoNumeroNota" name="novoNumeroNota"  />
		        </td>
		        
		        <td width="34">Série:</td>
		        <td width="97">
		            <input maxlength="200" type="text" style="width:50px " id="novoSerieNota" name="novoSerieNota" />
		        </td>
		      
		    </tr>

		    
		    <tr style="width: 25%">
		      
		      <td>NF-e:</td>
		      <td>
		          <input type="checkbox" id="novoNfe" name="novoNfe" />
		      </td>

		      <td>Chave de Acesso:</td>
		      <td colspan="5">
		          <input maxlength="200" type="text" style="width:470px" id="novoChaveAcesso" name="novoChaveAcesso" />
		      </td>

		    </tr>
		    
		    
		    <tr style="width: 25%">
		      
		      <td>Data Emissão:</td>
		      <td>
		          <input type="text" id="novoDataEmissao" name="novoDataEmissao" style="width:100px " />
		      </td>
		      
		      <td>Data Entrada:</td>
		      <td>
		          <input type="text" id="novoDataEntrada" name="novoDataEntrada" style="width:100px " />
		      </td>
		      
		      <td>Valor Total R$:</td>
		      <td>
		          <input maxlength="17" type="text" style="width:100px; text-align:right; " id="novoValorTotal" name="novoValorTotal"/>
		      </td>
		      
		      <td align="right">
		          <input type="checkbox" id="novoReplicarQtde" name="novoReplicarQtde" />
		      </td>
		      <td>Replicar Qtde:</td>
		      
		    </tr>
		    
		    </table>
		</fieldset>
		    
		
		<fieldset style="width:910px!important; margin-top:10px;">
		    <form name="formularioItensNota" id="formularioItensNota">
			    <legend>Itens da Nota</legend>
			    <table id="tabelaItens" class="novoItemNotaGrid"></table>
			</form>    
		</fieldset> 
		
		<span class="bt_incluir_novo" title="Incluir Novo">
		    <a href="javascript:;" onclick="incluiNovoItem();">
		        <img src= "${pageContext.request.contextPath}/images/ico_add.gif" alt="Incluir Novo" width="16" height="16" border="0" hspace="5" />
		        Incluir Novo
		    </a>
		</span>
		
		<span class="bt_sellAll" style="float:right; margin-right:40px; margin-top:8px">
		    <label for="textoSelTodos" id="textoSelTodos">
                Marcar Todos
            </label>
		    <input type="checkbox"  id="selTodos" name="selTodos" onclick="selecionarTodos(this.checked);" style="float:right; margin-top:-5px"/>
		</span>
		
		<span style="float:right; margin-right:35px; margin-top:8px">
		    <strong style="margin-right:10px;">
		        Valor Total R$
		    </strong> 
		    <label id="labelValorTotal">0,00</label>
		</span>
	 
	</div>


</body>
