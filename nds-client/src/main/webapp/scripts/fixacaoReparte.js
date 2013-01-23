var fixacaoReparteController = $.extend(true, {
	
	init : function() {
		
		$(".historicoGrid").flexigrid({
			url : '../xml/historicoFixacao-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Edição',
				name : 'edicao',
				width : 75,
				sortable : true,
				align : 'left'
			},{
				display : 'Reparte',
				name : 'reparte',
				width : 80,
				sortable : true,
				align : 'center'
			},{
				display : 'Venda',
				name : 'venda',
				width : 80,
				sortable : true,
				align : 'center'
			},{
				display : 'Lançamento',
				name : 'lancamento',
				width : 80,
				sortable : true,
				align : 'center'
			},{
				display : 'Recolhimento',
				name : 'recolhimento',
				width : 80,
				sortable : true,
				align : 'center'
			},  {
				display : 'Status',
				name : 'status',
				width : 130,
				sortable : true,
				align : 'left'
			}],
			width : 600,
			height : 150
		});

	$(".excessaoBGrid").flexigrid({
			url : '../xml/excessaoB-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 40,
				sortable : true,
				align : 'left'
			},{
				display : 'Produto',
				name : 'produto',
				width : 60,
				sortable : true,
				align : 'left'
			},{
				display : 'Segmento',
				name : 'segmento',
				width : 50,
				sortable : true,
				align : 'left'
			},{
				display : 'Fornecedor',
				name : 'fornecedor',
				width : 50,
				sortable : true,
				align : 'left'
			},  {
				display : '',
				name : 'sel',
				width : 20,
				sortable : true,
				align : 'center'
			}],
			width : 300,
			height : 235
		});
	$(".fixacaoProdutoGrid").flexigrid({
			url : '../xml/fixacaoProduto-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 170,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição Inicial',
				name : 'edicaoInicial',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Edição Final',
				name : 'edicaoFinal',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Ed. Atendidas',
				name : 'edicoesAtendidas',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Qtde Edições',
				name : 'qtdeEdicoes',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Exemplares',
				name : 'exemplares',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Usuário',
				name : 'usuario',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Data',
				name : 'data',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Hora',
				name : 'hora',
				width : 45,
				sortable : true,
				align : 'center'
			},  {
				display : 'Ação',
				name : 'acao',
				width : 50,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 250
		});
		
	
	$(".excessaoCotaGrid").flexigrid({
			url : '../xml/fixacaoCotaGrid-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Classificação',
				name : 'classificacao',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição Inicial',
				name : 'edicaoInicial',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição Final',
				name : 'edicaoFinal',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ed. Atendidas',
				name : 'edicoesAtendidas',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Qtde Ed.',
				name : 'qtdeEdicoes',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Exemplares',
				name : 'exemplares',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Usuário',
				name : 'usuario',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Data',
				name : 'data',
				width : 75,
				sortable : true,
				align : 'center'
			}, {
				display : 'Hora',
				name : 'hora',
				width : 60,
				sortable : true,
				align : 'center'
			},  {
				display : 'Ação',
				name : 'acao',
				width : 50,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 250
		});
	},
	
	porCota:function(){
		$('.porCota').show();
		$('.porExcessao').hide();
	},
	
	pesquisar:function(){
		
		$(".fixacaoProdutoGrid", fixacaoReparteController.workspace).flexOptions({
			url: contextPath + "/fixacaoReparte/pesquisar",
			dataType : 'json',
			params: vendaProdutoController.getDados()
		});
		
		$(".fixacaoProdutoGrid", fixacaoReparteController.workspace).flexReload();
				
	},
		
	getDados : function() {
			
			var data = [];
			
			data.push({name:'filtroPorProduto.codigoProduto',	value: vendaProdutoController.get("codigoProduto")});
			data.push({name:'filtroPorProduto.nomeProduto',		value: vendaProdutoController.get("produto")});
		//	data.push({name:'filtroPorProduto.classificacaoProduto',		value: vendaProdutoController.get("edicoes")});
		//	data.push({name:'filtro.nomeFornecedor',	value: $('#idFornecedor option:selected', vendaProdutoController.workspace).text()});
			
			return data;
		},
	
	}, BaseController);
