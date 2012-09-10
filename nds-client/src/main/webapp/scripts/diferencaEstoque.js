var diferencaEstoqueController = $.extend(true, {
	
	init : function () {
		$('input[id^="data"]', diferencaEstoqueController.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		
		$('input[id^="data"]', diferencaEstoqueController.workspace).mask("99/99/9999");
		
		$("#edicao", diferencaEstoqueController.workspace).numeric();
		
		$("#produto", diferencaEstoqueController.workspace).autocomplete({source: ""});

		$(".consultaFaltasSobrasGrid", diferencaEstoqueController.workspace).flexigrid({
			preProcess: diferencaEstoqueController.executarPreProcessamento,
			dataType : 'json',
			colModel : [ {
				display : 'Data',
				name : 'dataLancamento',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Código',
				name : 'codigoProduto',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Produto',
				name : 'descricaoProduto',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço Venda R$',
				name : 'precoVenda',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Preço Desconto R$',
				name : 'precoDesconto',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Tipo de Diferença',
				name : 'tipoDiferenca',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nota',
				name : 'numeroNotaFiscal',
				width : 104,
				sortable : true,
				align : 'left'
			}, {
				display : 'Exemplar',
				name : 'quantidade',
				width : 50,
				sortable : true,
				align : 'right'
			}, {
				display : 'Status',
				name : 'statusAprovacao',
				width : 45,
				sortable : true,
				align : 'center'
			}, {
				display : 'Total R$',
				name : 'valorTotalDiferenca',
				width : 50,
				sortable : true,
				align : 'right'
			}, {
				display : 'Detalhes',
				name : 'detalhes',
				width : 50,
				sortable : true,
				align : 'center'
			}],
			sortname : "dataLancamentoNumeroEdicao",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});

		$("#codigo", diferencaEstoqueController.workspace).focus();
		
		diferencaEstoqueController.initDetalhesCotaGrid();
	},
		
	
	pesquisarProdutosSuccessCallBack : function() {
		diferencaEstoqueController.pesquisarFornecedores();
	},
	
	pesquisarProdutosErrorCallBack : function() {
		diferencaEstoqueController.pesquisarFornecedores();
	},
	
	pesquisarFornecedores : function() {
		var data = "codigoProduto=" + $("#codigo", diferencaEstoqueController.workspace).val();
		
		$.postJSON(contextPath + "/estoque/diferenca/pesquisarFonecedores",
				   data, diferencaEstoqueController.montarComboFornecedores);
	},
	
	montarComboFornecedores : function(result) {
		var comboFornecedores =  montarComboBox(result, true);
		
		$("#fornecedor", diferencaEstoqueController.workspace).html(comboFornecedores);
	},
	
	pesquisar : function() {
		var codigoProduto = $("#codigo", diferencaEstoqueController.workspace).val();
		var numeroEdicao = $("#edicao", diferencaEstoqueController.workspace).val();
		var idFornecedor = $("#fornecedor", diferencaEstoqueController.workspace).val();
		var dataInicial = $("#dataInicial", diferencaEstoqueController.workspace).val();
		var dataFinal = $("#dataFinal", diferencaEstoqueController.workspace).val();
		var tipoDiferenca = $("#tipoDiferenca", diferencaEstoqueController.workspace).val();
		
		$(".consultaFaltasSobrasGrid", diferencaEstoqueController.workspace).flexOptions({
			url: contextPath + "/estoque/diferenca/pesquisarDiferencas",
			onSuccess: diferencaEstoqueController.executarAposProcessamento,
			params: [
			         {name:'codigoProduto', value:codigoProduto},
			         {name:'numeroEdicao', value:numeroEdicao},
			         {name:'idFornecedor', value:idFornecedor},
			         {name:'dataInicial', value:dataInicial},
			         {name:'dataFinal', value:dataFinal},
			         {name:'tipoDiferenca', value:tipoDiferenca}
			        ] ,
	        newp: 1
		});
		
		$(".consultaFaltasSobrasGrid", diferencaEstoqueController.workspace).flexReload();
	},
	
	executarAposProcessamento : function() {
		$("span[name='statusAprovacao']", diferencaEstoqueController.workspace).tooltip();
	},
	
	executarPreProcessamento : function(resultado) {
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".grids", diferencaEstoqueController.workspace).hide();

			return resultado.tableModel;
		}
		
		$("#qtdeTotalDiferencas", diferencaEstoqueController.workspace).html(resultado.qtdeTotalDiferencas);
		
		$("#valorTotalDiferencas", diferencaEstoqueController.workspace).html(resultado.valorTotalDiferencas);
		
		$.each(resultado.tableModel.rows, function(index, row) {
			
			if (row.cell.motivoAprovacao) {
			
				var spanAprovacao = "<span name='statusAprovacao' title='" + row.cell.motivoAprovacao + "'>"
									+ row.cell.statusAprovacao + "</span>";
				
				row.cell.statusAprovacao = spanAprovacao;
			}

			row.cell.detalhes = '<a href="javascript:;" onclick="diferencaEstoqueController.obterDetalhesDiferencaCota('
							  + row.cell.id
							  + ')" '
							  + ' style="cursor:pointer;border:0px" title="Visualizar Detalhes">'
							  + '<img src="' + contextPath + '/images/ico_detalhes.png" border="0px"/>'
							  + '</a>';
		});

		$(".grids", diferencaEstoqueController.workspace).show();

		return resultado.tableModel;
	},
	
	initDetalhesCotaGrid: function() {

		$(".detalhesCotaGrid", diferencaEstoqueController.workspace).flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Data',
				name : 'data',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Cota',
				name : 'numeroCota',
				width : 45,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeCota',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Box',
				name : 'codigoBox',
				width : 40,
				sortable : true,
				align : 'center'
			}, {
				display : 'Exemplares',
				name : 'exemplares',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço Desc. R$',
				name : 'precoDesconto',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Total Aprovadas R$',
				name : 'totalAprovadas',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Total Rejeitadas R$',
				name : 'totalRejeitadas',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Total R$',
				name : 'valorTotal',
				width : 45,
				sortable : true,
				align : 'right'
			}],
			sortname : "data",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 730,
			height : 180
		});
	},

	obterDetalhesDiferencaCota: function(idDiferenca) {

		diferencaEstoqueController.showPopupDetalhesCota();

		$.postJSON(
			contextPath + "/estoque/diferenca/obterDetalhesDiferencaCota",
			[{name:'idDiferenca', value:idDiferenca}],
			diferencaEstoqueController.setupDialogDetalhesEstoqueCota
		);
	},

	setupDialogDetalhesEstoqueCota: function(result) {

		$("#codigoDetalheEstoqueCota").html(result.codigoProduto);
		$("#nomeProdutoDetalheEstoqueCota").html(result.nomeProduto);
		$("#numeroEdicaoDetalheEstoqueCota").html(result.numeroEdicao);
		$("#nomeFornecedorDetalheEstoqueCota").html(result.nomeFornecedor);
		$("#tipoDiferencaDetalheEstoqueCota").html(result.tipoDiferenca);
		$("#quantidadeDiferencaDetalheEstoqueCota").html(result.quantidadeDiferenca);
		$("#totalExemplaresDetalheEstoqueCota").html(result.totalExemplares);
		$("#valorTotalDetalheEstoqueCota").html(result.valorTotal);

		$(".detalhesCotaGrid", diferencaEstoqueController.workspace).flexAddData({
			rows: result.detalhesDiferenca.rows, 
			page: result.detalhesDiferenca.page, 
			total: result.detalhesDiferenca.total
		});
	},
	
	showPopupDetalhesCota: function() {

		$( "#dialogDetalheEncalheCota", diferencaEstoqueController.workspace ).dialog({
			resizable: false,
			height:470,
			width:800,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this, diferencaEstoqueController.workspace ).dialog( "close" );
				}
			}
		});
	}

}, BaseController);

//@ sourceURL=diferenca.js
