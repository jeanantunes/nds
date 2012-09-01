var fechamentoCEIntegracaoController = $.extend(true, {
	
	init : function(){
		fechamentoCEIntegracaoController.initGrid();
		fechamentoCEIntegracaoController.bindButtons();
		fechamentoCEIntegracaoController.buscarNumeroSemana();
	},
	
	buscarNumeroSemana : function(){
		var dataAtual = $.format.date(new Date(), "dd/MM/yyyy");
		var data = [
	   				{
	   					name: 'data', value:dataAtual
	   				}
	   			];
				
				$.getJSON(
						contextPath + '/cadastro/distribuidor/obterNumeroSemana', 
					data,
					function(result) {
	
						if (result) {
	
							$("#semana", fechamentoCEIntegracaoController.workspace).val(result.int);
						}
					}
				);
	},
	
	bindButtons : function(){
		$("#btnPesquisar", fechamentoCEIntegracaoController.workspace).click(function() {
			fechamentoCEIntegracaoController.pesquisaPrincipal();
			$(".grids", fechamentoCEIntegracaoController.worspace).show();
		});
	},
	initGrid : function(){	
		$(".fechamentoCeGrid", fechamentoCEIntegracaoController.workspace).flexigrid({
			preProcess : function(resultado) {
				
				if (resultado.mensagens) {
					exibirMensagem(resultado.mensagens.tipoMensagem, resultado.mensagens.listaMensagens);
					$(".grids", fechamentoCEIntegracaoController.workspace).hide();
					return resultado;
				}
				
				$(".grids", fechamentoCEIntegracaoController.workspace).show();
				return resultado;
			  },
			dataType : 'json',
			colModel : [ {
				display : 'Seq',
				name : 'sequencial',
				width : 30,
				sortable : true,
				align : 'left'
			},  {
				display : 'Código',
				name : 'codigo',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 200,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Tipo',
				name : 'tipoFormatado',
				width : 50,
				sortable : true,
				align : 'center'
			},  {
				display : 'Reparte',
				name : 'reparte',
				width : 80,
				sortable : true,
				align : 'center'
			},  {
				display : 'Encalhe',
				name : 'encalhe',
				width : 80,
				sortable : true,
				align : 'center'
			},  {
				display : 'Venda',
				name : 'venda',
				width : 80,
				sortable : true,
				align : 'center'
			},  {
				display : 'Preço Capa R$',
				name : 'precoCapa',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Valor Venda R$',
				name : 'valorVendaFormatado',
				width : 80,
				sortable : true,
				align : 'right'
			}],
			sortname : "qtde",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});
		
	},
	
	pesquisaPrincipal : function(){
		var idFornecedor = $("#idFornecedor", fechamentoCEIntegracaoController.workspace).val();
		var semana = $("#semana", fechamentoCEIntegracaoController.workspace).val();
		$(".fechamentoCeGrid", fechamentoCEIntegracaoController.workspace).flexOptions({
			url: contextPath + '/devolucao/fechamentoCEIntegracao/pesquisaPrincipal',
			dataType : 'json',
			params: [
			         {name:'filtro.idFornecedor' , value:idFornecedor},
			         {name:'filtro.semana' , value:semana}
			         ]
		});
		
		$(".fechamentoCeGrid").flexReload();
		fechamentoCEIntegracaoController.popularTotal(idFornecedor, semana);
	},
	
	popularTotal : function(idFornecedor, semana){
 		$.postJSON(
			contextPath + '/devolucao/fechamentoCEIntegracao/buscarTotalDaPesquisa',
			[
	         {name:'filtro.idFornecedor' , value:idFornecedor},
	         {name:'filtro.semana' , value:semana}
	         ],
			function(result) {
				$('.tabelaTotal', fechamentoCEIntegracaoController.workspace).show();
				$("#total", fechamentoCEIntegracaoController.workspace).html(result);
			},
			null,
			true
		);
	}
	
	
	
}, BaseController);