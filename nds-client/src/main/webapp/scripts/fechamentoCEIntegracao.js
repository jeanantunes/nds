var fechamentoCEIntegracaoController = $.extend(true, {
	
	init : function(){
		fechamentoCEIntegracaoController.initGrid();
		fechamentoCEIntegracaoController.bindButtons();
		fechamentoCEIntegracaoController.buscarNumeroSemana();		
	},
	
	verificarDataFechamentoCE : function(fechada) {
		
			if (fechada) {					
				$("#btnFechamento", fechamentoCEIntegracaoController.workspace).unbind("click");
				$("#imagemFechamento", fechamentoCEIntegracaoController.workspace).css("opacity", "0.2");
				$("#btnReabertura", fechamentoCEIntegracaoController.workspace).unbind("click");
				$("#imagemReabertura", fechamentoCEIntegracaoController.workspace).css("opacity", "0.2");
			} else {
				$("#imagemFechamento", fechamentoCEIntegracaoController.workspace).css("opacity", "1.0");
				$("#btnFechamento", fechamentoCEIntegracaoController.workspace).click(function() {
					fechamentoCEIntegracaoController.fecharCE();
				});
			}
	},
	
	geraBoleto : function(tipoCobranca) {
		
		var parametros = [{
			name:'tipoCobranca', value: tipoCobranca
		}];
		
		$.postJSON(contextPath + '/devolucao/fechamentoCEIntegracao/geraBoleto', parametros,
				
		function(result) {
			
			var file = contextPath + '/devolucao/fechamentoCEIntegracao/imprimeBoleto';
	
			$('#download-iframe-fechamento', fechamentoCEIntegracaoController.workspace).attr('src', file);
	
		});
		
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
				} else {
					
					if (!resultado.semanaFechada) {	

						$.each(resultado.listaFechamento.rows, function(index, row) {
							var linhaEncalhe = '<input type="text" id="inputEncalhe' + row.cell.sequencial + '" value="' + row.cell.encalhe + '" size="5px" />';
							row.cell.encalhe = linhaEncalhe;
						});
						
					};
					
					fechamentoCEIntegracaoController.popularTotal(resultado);
					fechamentoCEIntegracaoController.verificarDataFechamentoCE(resultado.semanaFechada);
					
					$(".grids", fechamentoCEIntegracaoController.workspace).show();
					
					return resultado.listaFechamento;
					
				};
				
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
				name : 'codigoProduto',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 200,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
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
			sortname : "sequencial",
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
		
	},
	
	popularTotal : function(resultado){
		$("#totalBruto", fechamentoCEIntegracaoController.workspace).html(resultado.totalBruto);
		$("#totalDesconto", fechamentoCEIntegracaoController.workspace).html(resultado.totalDesconto);
		$("#totalLiquido", fechamentoCEIntegracaoController.workspace).html(resultado.totalLiquido);
		$(".tabelaTotal").show();
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
//@ sourceURL=scriptFechamentoCEIntegracao.js
