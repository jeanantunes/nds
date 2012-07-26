<head>

<script language="javascript" type="text/javascript" src='<c:url value="/"/>scripts/produto.js'></script>
<script language="javascript" type="text/javascript" src='<c:url value="/"/>/scripts/jquery.numeric.js'></script>

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
			
			$("#datepickerLancto").mask("99/99/9999");
			$("#datepickerRecolhimento").mask("99/99/9999");
			$("#dataEmissao").mask("99/99/9999");
			
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
			document.getElementById('botaoNovoProduto').style.display="";
		}else{
			document.getElementById('botoesOpacos').style.display="";
			document.getElementById('botoesNormais').style.display="none";
			document.getElementById('botaoNovoProdutoOpaco').style.display="";
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
			
			document.getElementById('botoesOpacos').style.display="none";
		}else{
			document.getElementById('botoesOpacos').style.display="";
			document.getElementById('botoesNormais').style.display="none";
			
			document.getElementById('botaoNovoProdutoOpaco').style.display="";
			document.getElementById('botaoNovoProduto').style.display="none";
			
		}	
		
		
		return data;

	}

	
</script>

<style type="text/css">
  fieldset label {width: auto; margin-bottom: 0px!important;
}
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
					<div id="botaoNovoProduto">
							<span class="bt_incluir_novo" id="bt_novo_produto" title="Incluir Nova Linha"> 
								<a href="javascript:;" onclick="popup_novo_item();"> 
									<img src="${pageContext.request.contextPath}/images/ico_add_novo.gif" border="0" hspace="5" />
									Novo Produto 
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

</body>

</html>
