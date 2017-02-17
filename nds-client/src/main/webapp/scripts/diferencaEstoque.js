var diferencaEstoqueController = $.extend(true, {
	
	detalhes : [],
	idDiferenca: "",
	
	init : function () {
		
		$('input[id^="diferenca-estoque-dataInicial"]', diferencaEstoqueController.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		
		$('input[id^="diferenca-estoque-dataFinal"]', diferencaEstoqueController.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		
		$('input[id^="data"]', diferencaEstoqueController.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true,
			dateFormat: "dd/mm/yy"
		});
		
		$('input[id^="data"]', diferencaEstoqueController.workspace).mask("99/99/9999");
		
		$("#diferenca-estoque-produto", diferencaEstoqueController.workspace).autocomplete({source: []});
		
		$("#sugerirSemana", diferencaEstoqueController.workspace).click(function(e){
			
			if($(this).is(":checked")){
				diferencaEstoqueController.carregarDiaSemana();
			}else{
				$("#semanaRecolhimentoBox", diferencaEstoqueController.workspace).val("");
			 }
		});
		
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

		$("#diferenca-estoque-codigo", diferencaEstoqueController.workspace).focus();

		diferencaEstoqueController.initDetalhesCotaGrid();
	},
		
	
	pesquisarProdutosSuccessCallBack : function() {
		diferencaEstoqueController.pesquisarFornecedores();
	},
	
	pesquisarProdutosErrorCallBack : function() {
		diferencaEstoqueController.pesquisarFornecedores();
	},
	
	pesquisarFornecedores : function() {
		var data = {codigoProduto: $("#diferenca-estoque-codigo", diferencaEstoqueController.workspace).val()};
		
		$.postJSON(contextPath + "/estoque/diferenca/pesquisarFonecedores",
				   data, diferencaEstoqueController.montarComboFornecedores);
	},
	
	montarComboFornecedores : function(result) {
		var comboFornecedores =  montarComboBox(result, true);
		
		$("#diferenca-estoque-fornecedor", diferencaEstoqueController.workspace).html(comboFornecedores);
	},
	
	pesquisar : function() {
		
		var codigoProduto = $("#diferenca-estoque-codigo", diferencaEstoqueController.workspace).val();
		var idFornecedor = $("#diferenca-estoque-fornecedor", diferencaEstoqueController.workspace).val();
		var dataInicial = $("#diferenca-estoque-dataInicial", diferencaEstoqueController.workspace).val();
		var dataFinal = $("#diferenca-estoque-dataFinal", diferencaEstoqueController.workspace).val();
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
	
	carregarDiaSemana : function() {
		
		if($("#sugerirSemana:checked").size() < 1)
			return;
		
		var dataPesquisa = $("#diferenca-estoque-dataInicial", diferencaEstoqueController.workspace).val();

		if (!dataPesquisa) {

			return;
		}

		var data = [{ name: 'data', value: $("#diferenca-estoque-dataInicial", diferencaEstoqueController.workspace).val() }];
		
		$.getJSON(
			contextPath + "/cadastro/distribuidor/obterNumeroSemana", 
			data,
			function(result) {
				if (result) {
					$("#semanaRecolhimentoBox", diferencaEstoqueController.workspace).val(result.int);
				}
			});
	},
	
	carregarDataSemana : function() {
		
		var numeroSemana = $("#semanaRecolhimentoBox", diferencaEstoqueController.workspace).val();

		if (!numeroSemana) {

			return;
		}
		
		var data = [{ name: 'numeroSemana', value: numeroSemana }];
		
		$.getJSON(
			contextPath + "/cadastro/distribuidor/obterDataDaSemana", 
			data,
			function(result) {

				if (result) {
					
					$("#diferenca-estoque-dataInicial", diferencaEstoqueController.workspace).val(result);
				}
			});
	},
	
	extracaoExcelPDF : function(fileType) {
		
		var mensagens = []; 
		
		var data = new Array();
		
		var numeroSemana = $("#semanaRecolhimentoBox", diferencaEstoqueController.workspace).val();
		
		var path = contextPath + "/estoque/diferenca/extracaoExcelPDF";
		
		if(numeroSemana == '') {
			mensagens.push("A ['Semana de Recolhimento'] não pode ser nula");
		}
		
		if(mensagens.length > 0) {
			exibirMensagem('WARNING', mensagens);
			return;
		}
		
		
		data.push({name:'filtro.fileType', value: fileType});
		data.push({name:'filtro.numeroSemana', value: numeroSemana});
		
		data.push({name:'filtro.ano', value: numeroSemana.substr(0, 4)});
		data.push({name:'filtro.semana', value: numeroSemana.substr(4, 6)});
		
		
		$.fileDownload(path, {
			httpMethod : "GET",
			data : data,
			successCallback: function(result) {
				if (result.mensagens) {
	
					exibirMensagem(
							result.mensagens.tipoMensagem, 
							result.mensagens.listaMensagens
					);
				}			
			},
		});
		
//		$.getJSON(contextPath + "/estoque/diferenca/extracaoExcelPDF", data,
//		function(result) {
//		
//			if (result) {
//				
//				$("#diferenca-estoque-dataInicial", diferencaEstoqueController.workspace).val(result);
//			}
//		});
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