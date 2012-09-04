var fechamentoCEIntegracaoController = $.extend(true, {
	
	init : function(){
		fechamentoCEIntegracaoController.initGrid();
		fechamentoCEIntegracaoController.bindButtons();
		fechamentoCEIntegracaoController.buscarNumeroSemana();
		fechamentoCEIntegracaoController.verificarDataFechamentoCE();
	},
	
	verificarDataFechamentoCE : function(){
		
		$.postJSON(contextPath + '/devolucao/fechamentoCEIntegracao/verificarStatusSemana', 
			null,
			function(result) {
				
				if (result) {					
					$("#btnFechamento", fechamentoCEIntegracaoController.workspace).click(function() {
						alert('JÁ FECHOU A BAGAÇA');						
					});
				}else{
					$("#btnFechamento", fechamentoCEIntegracaoController.workspace).click(function() {
						fechamentoCEIntegracaoController.fecharCE();			
					});
				}
			}
		);
		
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
				}else{
					$.each(resultado.rows, function(index, value) {
						var encalhe = value.cell.encalhe;
						var idProdutoEdicao = value.cell.idProdutoEdicao;
						var inputEncalhe = '<input type="text" id="inputEncalhe" name="inputEncalhe" value='+encalhe+' /> <input type="hidden" id="codigoProdutoEdicao" name="codigoProdutoEdicao" value='+idProdutoEdicao+' /> '
						value.cell.encalhe = inputEncalhe;				
									
					});
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
	},
	
	fecharCE : function(){
		var listaEncalhe = new Array();
		var listaIdProdutoEdicao = new Array();
		var cont = 0;
		var cont2 = 0;
		$(".fechamentoCeGrid tr", fechamentoCEIntegracaoController.workspace).each(function(){			
			$("td", this).each(function() {				
				$("input[name='inputEncalhe']", this).each(function() {
					listaEncalhe[cont] = $(this).val();
					cont++;
				})
				
				$("input[name='codigoProdutoEdicao']", this).each(function() {
					listaIdProdutoEdicao[cont2] = $(this).val();
					cont2++;
				})
			})
		});
		
		$.postJSON(contextPath + '/devolucao/fechamentoCEIntegracao/fecharCE',
				[
				 {name:'listaEncalhe' , value:listaEncalhe},
				 {name:'listaIdProdutoEdicao' , value:listaIdProdutoEdicao},
				 ],
				null,
				null,
				true
			);

		
		
	}
	
	
	
}, BaseController);