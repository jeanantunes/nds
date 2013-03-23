var fechamentoCEIntegracaoController = $.extend(true, {
	
	itensCEIntegracao : [],
	
	init : function(){
		fechamentoCEIntegracaoController.initGrid();
		fechamentoCEIntegracaoController.bindButtons();
		fechamentoCEIntegracaoController.buscarNumeroSemana();		
	},
	
	verificarDataFechamentoCE : function(fechada) {
		
			if (fechada) {					
				
				$("#btnFechamento", fechamentoCEIntegracaoController.workspace).unbind("click");
				
				$("#imagemFechamento", fechamentoCEIntegracaoController.workspace).css("opacity", "0.2");
				
				$("#imagemReabertura", fechamentoCEIntegracaoController.workspace).css("opacity", "1.0");
				
				$("#btnReabertura", fechamentoCEIntegracaoController.workspace).click(function() {
					fechamentoCEIntegracaoController.reabrirCeIntegracao();
				});
				
			} else {
				$("#btnReabertura", fechamentoCEIntegracaoController.workspace).unbind("click");
				
				$("#imagemReabertura", fechamentoCEIntegracaoController.workspace).css("opacity", "0.2");
				
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
			preProcess : fechamentoCEIntegracaoController.fechamentoCeGridPreProcess,
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
				sortable : false,
				align : 'center'
			},  {
				display : 'Venda',
				name : 'venda',
				width : 80,
				sortable : false,
				align : 'center'
			},  {
				display : 'Preço Capa R$',
				name : 'precoCapaFormatado',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Valor Venda R$',
				name : 'valorVendaFormatado',
				width : 80,
				sortable : false,
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
	
	tratarAlteracaoEncalhe : function(idItemCeIntegracao) {
		
		var reparte = $("#reparte" + idItemCeIntegracao).html();
		var precoCapa = $("#precoCapa" + idItemCeIntegracao).html();
		var encalhe = $("#inputEncalhe" + idItemCeIntegracao).val();
		
		var venda = reparte - encalhe;
		var valorVenda = venda * priceToFloat(precoCapa);
		var valorVendaFormatado = floatToPrice(valorVenda);
		
		if (reparte < encalhe) {
			
			exibirMensagem(
				'WARNING', ["A quantidade de encalhe não pode exceder a quantidade do reparte!"]);
			
			$.each(fechamentoCEIntegracaoController.itensCEIntegracao, function(index, itemCEIntegracao) {
				
				if(itemCEIntegracao.id == idItemCeIntegracao) {
					
					var encalhe = itemCEIntegracao.encalhe;
					
					$("#inputEncalhe" + idItemCeIntegracao).val(encalhe);
					
					return false;
				}
			});
			
			return;
		}
		
		$("#venda" + idItemCeIntegracao).html(venda);
		$("#valorVenda" + idItemCeIntegracao).html(valorVendaFormatado);
		
		$.each(fechamentoCEIntegracaoController.itensCEIntegracao, function(index, itemCEIntegracao) {
		
			if(itemCEIntegracao.id == idItemCeIntegracao) {
				
				itemCEIntegracao.encalhe = encalhe;
				itemCEIntegracao.venda = venda;
				
				return false;
			}
		});
		
		fechamentoCEIntegracaoController.atualizarEncalheCalcularTotais(idItemCeIntegracao, encalhe);
	},
	
	atualizarEncalheCalcularTotais : function(idItemChamadaFornecedor, encalhe) {
		
		var data = {
			'idItemChamadaFornecedor': idItemChamadaFornecedor, 'encalhe': encalhe
		};
		
		$.postJSON(contextPath + '/devolucao/fechamentoCEIntegracao/atualizarEncalheCalcularTotais', 
			data,
			function(result) {
				if (result) {
					
					$("#totalBruto").html(result.totalBruto);
					$("#totalDesconto").html(result.totalDesconto);
					$("#totalLiquido").html(result.totalLiquido);
				}
			}
		);
	},
	
	fechamentoCeGridPreProcess : function(resultado) {
		if (resultado.mensagens) {
			exibirMensagem(resultado.mensagens.tipoMensagem, resultado.mensagens.listaMensagens);
			$(".grids", fechamentoCEIntegracaoController.workspace).hide();
			return resultado;
		} else {
			
			if (!resultado.semanaFechada) {

				fechamentoCEIntegracaoController.itensCEIntegracao = [];
				
				$.each(resultado.listaFechamento.rows, function(index, row) {
					
					fechamentoCEIntegracaoController.itensCEIntegracao.push(
						{id: row.cell.idItemCeIntegracao, encalhe: row.cell.encalhe, venda: row.cell.venda});
					
					var colunaReparte =
						'<span id="reparte' + row.cell.idItemCeIntegracao + '">' +
							row.cell.reparte +
						'</span>';
					
					var colunaEncalhe =
						'<input type="text" name="inputEncalhe"' +
						'id="inputEncalhe' + row.cell.idItemCeIntegracao + '"' +
						'value="' + row.cell.encalhe + '" size="5px"' +
						'onchange="fechamentoCEIntegracaoController.tratarAlteracaoEncalhe(' +
						row.cell.idItemCeIntegracao + ')"/>';
					
					var colunaVenda =
						'<span id="venda' + row.cell.idItemCeIntegracao + '">' +
							row.cell.venda +
						'</span>';
					
					var colunaPrecoCapa =
						'<span id="precoCapa' + row.cell.idItemCeIntegracao + '">' +
							row.cell.precoCapaFormatado +
						'</span>';
					
					var colunaValorVenda =
						'<span id="valorVenda' + row.cell.idItemCeIntegracao + '">' +
							row.cell.valorVendaFormatado +
						'</span>';
					
					row.cell.reparte = colunaReparte;
					row.cell.encalhe = colunaEncalhe;					
					row.cell.venda = colunaVenda;
					row.cell.precoCapaFormatado = colunaPrecoCapa;
					row.cell.valorVendaFormatado = colunaValorVenda;
				});
				
			};
			
			fechamentoCEIntegracaoController.popularTotal(resultado);
			fechamentoCEIntegracaoController.verificarDataFechamentoCE(resultado.semanaFechada);
			
			$(".grids", fechamentoCEIntegracaoController.workspace).show();
			
			return resultado.listaFechamento;
			
		};

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
		for(var id in fechamentoCEIntegracaoController.idsProdEdicao) {
			listaEncalhe[id] = fechamentoCEIntegracaoController.idsProdEdicao[id].encalhe;
			listaIdProdutoEdicao[id] = fechamentoCEIntegracaoController.idsProdEdicao[id].name;
		}
	
		$.postJSON(contextPath + '/devolucao/fechamentoCEIntegracao/fecharCE',
				[
				 {name:'listaEncalhe' , value:listaEncalhe},
				 {name:'listaIdProdutoEdicao' , value:listaIdProdutoEdicao},
				 {name:'idFornecedor', value: $("#idFornecedor", fechamentoCEIntegracaoController.workspace).val()},
				 {name:'semana', value: $("#semana", fechamentoCEIntegracaoController.workspace).val()}
				 ],
				 function(resultado) {
				 	exibirMensagem(resultado.mensagens.tipoMensagem, resultado.mensagens.listaMensagens);
					$(".grids", fechamentoCEIntegracaoController.workspace).hide();
					return resultado;
				 },
				null,
				true
			);
		
	},
	
	reabrirCeIntegracao:function (){
		
		var idFornecedor = $("#idFornecedor", fechamentoCEIntegracaoController.workspace).val();
		var semana = $("#semana", fechamentoCEIntegracaoController.workspace).val();
		
		$.postJSON(contextPath + '/devolucao/fechamentoCEIntegracao/reabrirCeIntegracao',
				[
		         {name:'filtro.idFornecedor' , value:idFornecedor},
		         {name:'filtro.semana' , value:semana}
				]	,
				 function(resultado) {
				 	exibirMensagem(resultado.tipoMensagem, resultado.listaMensagens);
				 },
				null,
				true
			);
	}
	
}, BaseController);
//@ sourceURL=scriptFechamentoCEIntegracao.js
