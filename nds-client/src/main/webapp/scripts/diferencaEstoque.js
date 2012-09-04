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
			} ],
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
		});
			
		$(".grids", diferencaEstoqueController.workspace).show();
		
		return resultado.tableModel;
	}

}, BaseController);
