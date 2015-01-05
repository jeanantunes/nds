var diferencaEstoqueController = $.extend(true, {
	
	detalhes : [],
	idDiferenca: "",
	
	init : function () {
		$('input[id^="data"]', diferencaEstoqueController.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		
		$('input[id^="data"]', diferencaEstoqueController.workspace).mask("99/99/9999");
		
		$("#diferenca-estoque-produto", diferencaEstoqueController.workspace).autocomplete({source: []});

		$(".consultaFaltasSobrasGrid", diferencaEstoqueController.workspace).flexigrid({
			preProcess: diferencaEstoqueController.executarPreProcessamento,
			dataType : 'json',
			colModel : [ {
				display : 'Data',
				name : 'dataLancamento',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Código',
				name : 'codigoProduto',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'descricaoProduto',
				width : 200,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Tipo de Diferença',
				name : 'descricaoTipoDiferenca',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Exemplar',
				name : 'quantidade',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Status',
				name : 'statusAprovacao',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Status Integração',
				name : 'statusIntegracao',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Total R$',
				name : 'valorTotalDiferenca',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : 'Detalhes',
				name : 'detalhes',
				width : 50,
				sortable : true,
				align : 'right'
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
		var data = {codigoProduto: $("#codigo", diferencaEstoqueController.workspace).val()};
		
		$.postJSON(contextPath + "/estoque/diferenca/pesquisarFonecedores",
				   data, diferencaEstoqueController.montarComboFornecedores);
	},
	
	montarComboFornecedores : function(result) {
		var comboFornecedores =  montarComboBox(result, true);
		
		$("#diferenca-estoque-fornecedor", diferencaEstoqueController.workspace).html(comboFornecedores);
	},
	
	pesquisar : function() {
		
		var codigoProduto = $("#codigo", diferencaEstoqueController.workspace).val();
		var idFornecedor = $("#diferenca-estoque-fornecedor", diferencaEstoqueController.workspace).val();
		var dataInicial = $("#dataInicial", diferencaEstoqueController.workspace).val();
		var dataFinal = $("#dataFinal", diferencaEstoqueController.workspace).val();
		var tipoDiferenca = $("#tipoDiferenca", diferencaEstoqueController.workspace).val();
		var numeroCota = $("#diferenca-estoque-numeroCota", diferencaEstoqueController.workspace).val();
		var nomeCota = $("#descricaoCota", diferencaEstoqueController.workspace).val();
		
		$(".consultaFaltasSobrasGrid", diferencaEstoqueController.workspace).flexOptions({
			url: contextPath + "/estoque/diferenca/pesquisarDiferencas",
			onSuccess: diferencaEstoqueController.executarAposProcessamento,
			params: [
			         {name:'codigoProduto', value:codigoProduto},
			         {name:'idFornecedor', value:idFornecedor},
			         {name:'dataInicial', value:dataInicial},
			         {name:'dataFinal', value:dataFinal},
			         {name:'tipoDiferenca', value:tipoDiferenca},
			         {name:'numeroCota', value:numeroCota},
			         {name:'nomeCota', value:nomeCota}
			        ] ,
	        newp: 1
		});
		
		$(".consultaFaltasSobrasGrid", diferencaEstoqueController.workspace).flexReload();
	},
	
	executarAposProcessamento : function() {
		//$("span[name='statusAprovacao']", diferencaEstoqueController.workspace).tooltip();
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
		
		diferencaEstoqueController.detalhes = [];
		
		$.each(resultado.tableModel.rows, function(index, row) {
			
			diferencaEstoqueController.detalhes.push(row.cell);
			
			row.cell.statusIntegracao = (row.cell.statusIntegracao)
												?row.cell.statusIntegracao
														:"";
			
			if (row.cell.motivoAprovacao) {
			
				var spanAprovacao = "<span name='statusAprovacao' title='" + row.cell.motivoAprovacao + "'>"
									+ row.cell.statusAprovacao + "</span>";
				
				row.cell.statusAprovacao = spanAprovacao;
			
			}
			
			if (row.cell.existemRateios) {

				row.cell.detalhes = diferencaEstoqueController.gerarBotaoDetalheCota(index, row.cell.id);
			
			} else {
				
				row.cell.detalhes = diferencaEstoqueController.gerarBotaoDetalhe(index);
			}
		});

		$(".grids", diferencaEstoqueController.workspace).show();

		return resultado.tableModel;
	},
	
	gerarBotaoDetalhe : function(index) {
		return '<a href="javascript:;" onclick="diferencaEstoqueController.popupDetalhe(diferencaEstoqueController.detalhes[' + index + ']);">' +
			   '<img src="'+ contextPath +'/images/ico_detalhes.png" border="0"></a>';
	},
	
	gerarBotaoDetalheCota: function(index, idDiferenca) {

		return '<a href="javascript:;" onclick="diferencaEstoqueController.obterDetalhesDiferencaCota(diferencaEstoqueController.detalhes[' + index + '], ' + idDiferenca + ');">' +
		   '<img src="'+ contextPath +'/images/ico_detalhes.png" border="0"></a>';
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
				name : 'precoDescontoFormatado',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Total Aprovadas R$',
				name : 'totalAprovadasFormatado',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Total Rejeitadas R$',
				name : 'totalRejeitadasFormatado',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Total R$',
				name : 'valorTotalFormatado',
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

	obterDetalhesDiferencaCota: function(resultado, idDiferenca) {

		diferencaEstoqueController.showPopupDetalhesCota();

		$("#codigoDetalheEstoqueCota",              diferencaEstoqueController.workspace).html(resultado.codigoProduto);
		$("#nomeProdutoDetalheEstoqueCota",         diferencaEstoqueController.workspace).html(resultado.descricaoProduto);
		$("#numeroEdicaoDetalheEstoqueCota",        diferencaEstoqueController.workspace).html(resultado.numeroEdicao);
		$("#nomeFornecedorDetalheEstoqueCota",      diferencaEstoqueController.workspace).html(resultado.fornecedor);
		$("#tipoDiferencaDetalheEstoqueCota",       diferencaEstoqueController.workspace).html(resultado.descricaoTipoDiferenca);
		$("#quantidadeDiferencaDetalheEstoqueCota", diferencaEstoqueController.workspace).html(resultado.quantidade);

		$(".detalhesCotaGrid", diferencaEstoqueController.workspace).flexOptions({
			url: contextPath + "/estoque/diferenca/obterDetalhesDiferencaCota",
			preProcess: diferencaEstoqueController.setupDetalhesEstoqueCota,
			params: diferencaEstoqueController.getParametrosDetalhesDiferencaCota(resultado, idDiferenca)			 
		});
		
		$(".detalhesCotaGrid", diferencaEstoqueController.workspace).flexReload();
	},

	getParametrosDetalhesDiferencaCota: function(resultado, idDiferenca) {
		
		var data = new Array();
		
		data.push({name:'filtro.idDiferenca',      value: idDiferenca});
		data.push({name:'filtro.codigoProduto',    value: resultado.codigoProduto});
		data.push({name:'filtro.descricaoProduto', value: resultado.descricaoProduto});
		data.push({name:'filtro.numeroEdicao', 	   value: resultado.numeroEdicao});
		data.push({name:'filtro.nomeFornecedor',   value: resultado.fornecedor});
		data.push({name:'filtro.tipoDiferenca',	   value: resultado.tipoDiferenca});
		data.push({name:'filtro.quantidade', 	   value: resultado.quantidade});
		
		var numeroCota = $("#diferenca-estoque-numeroCota", diferencaEstoqueController.workspace).val();
		
		data.push({name:'filtro.numeroCota', 	   value: numeroCota});
		
		return data;
	},
	
	setupDetalhesEstoqueCota: function(resultado) {

		if (resultado.mensagens) {
			
			exibirMensagemDialog(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens,
				"dialogDetalheEncalheCota"
			);

			return resultado.detalhesDiferenca;
		}

		$.each(resultado.tableModel.rows, function(index, row) {
			
			row.cell.data = row.cell.data.$;
		});

		$("#valorTotalDetalheEstoqueCota", diferencaEstoqueController.workspace).html(resultado.valorTotalFormatado);
		$("#totalExemplaresDetalheEstoqueCota", diferencaEstoqueController.workspace).html(resultado.totalExemplares);

		return resultado.tableModel;
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
			},
			form: $("#dialogDetalheEncalheCota", this.workspace).parents("form")
		});
	},

	popupDetalhe : function(result) {
		
		$('#detalheCodigo', diferencaEstoqueController.workspace)		.html(result.codigoProduto);
		$('#detalheNome', diferencaEstoqueController.workspace)			.html(result.descricaoProduto);
		$('#detalheEdicao', diferencaEstoqueController.workspace)		.html(result.numeroEdicao);
		$('#detalheFornecedor', diferencaEstoqueController.workspace)	.html(result.fornecedor);
		$('#detalheTipo', diferencaEstoqueController.workspace)			.html(result.descricaoTipoDiferenca);
		$('#detalheQtde', diferencaEstoqueController.workspace)			.html(result.quantidade);
		$('#detalheEstoque', diferencaEstoqueController.workspace)		.html(result.tipoEstoque);
		
		$( "#dialog-detalhe-1", diferencaEstoqueController.workspace ).dialog({
			resizable: false,
			height:370,
			width:350,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
				}
			},
		    form: $("#dialog-detalhe-1", this.workspace).parents("form")
		});
	}

}, BaseController);

//@ sourceURL=diferencaEstoque.js
