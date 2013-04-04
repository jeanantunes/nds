var fechamentoCEIntegracaoController = $.extend(true, {
	
	itensCEIntegracao : [],
	
	init : function(){
		fechamentoCEIntegracaoController.initGrid();
		fechamentoCEIntegracaoController.bindButtons();
		fechamentoCEIntegracaoController.buscarNumeroSemana();
		fechamentoCEIntegracaoController.esconderBotoes();

	},
	
	verificarDataFechamentoCE : function(fechada) {
		
			if (fechada) {					
				
				$("#btnFechamento", fechamentoCEIntegracaoController.workspace).unbind("click");
				
				$("#imagemFechamento", fechamentoCEIntegracaoController.workspace).css("opacity", "0.2");
				
				$("#imagemReabertura", fechamentoCEIntegracaoController.workspace).css("opacity", "1.0");
				
				$("#imagemImpressaoBoleto", fechamentoCEIntegracaoController.workspace).css("opacity", "1.0");
				
				$("#imagemBoletoEmBranco", fechamentoCEIntegracaoController.workspace).css("opacity", "1.0");
				
				$("#btnReabertura", fechamentoCEIntegracaoController.workspace).click(function() {
					fechamentoCEIntegracaoController.reabrirCeIntegracao();
				});
				
				$("#btnImpBoleto", fechamentoCEIntegracaoController.workspace).click(function() {
					fechamentoCEIntegracaoController.geraBoleto('BOLETO')
				});
				
				$("#btnImpBoletoEmBranco", fechamentoCEIntegracaoController.workspace).click(function() {
					fechamentoCEIntegracaoController.geraBoleto('BOLETO_EM_BRANCO')
				});
				
			} else {
				$("#btnReabertura", fechamentoCEIntegracaoController.workspace).unbind("click");
				
				$("#btnImpBoleto", fechamentoCEIntegracaoController.workspace).unbind("click");
				
				$("#btnImpBoletoEmBranco", fechamentoCEIntegracaoController.workspace).unbind("click");
				
				$("#imagemReabertura", fechamentoCEIntegracaoController.workspace).css("opacity", "0.2");
				
				$("#imagemFechamento", fechamentoCEIntegracaoController.workspace).css("opacity", "1.0");
				
				$("#imagemImpressaoBoleto", fechamentoCEIntegracaoController.workspace).css("opacity", "0.2");
				
				$("#imagemBoletoEmBranco", fechamentoCEIntegracaoController.workspace).css("opacity", "0.2");
				
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
			onSuccess: function() {bloquearItensEdicao(fechamentoCEIntegracaoController.workspace);},
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
	
	tratarAlteracaoEncalhe : function(idItemCeIntegracao, encalhe) {
		
		var reparte = $("#reparte" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
		var precoCapa = $("#precoCapa" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
		
		var venda = reparte - encalhe;
		var valorVenda = venda * priceToFloat(precoCapa);
		var valorVendaFormatado = floatToPrice(valorVenda);
		
		if (eval(reparte) < eval(encalhe)) {
			
			exibirMensagem(
				'WARNING', ["A quantidade de encalhe não pode exceder a quantidade do reparte!"]);
			
			$.each(fechamentoCEIntegracaoController.itensCEIntegracao, function(index, itemCEIntegracao) {
				
				if(itemCEIntegracao.id == idItemCeIntegracao) {
					
					var encalhe = itemCEIntegracao.encalhe;
					
					$("#inputEncalhe" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).val(encalhe);
					
					return false;
				}
			});
			
			return;
		}
		
		$("#venda" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html(venda);
		$("#valorVenda" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html(valorVendaFormatado);
		
		fechamentoCEIntegracaoController.atualizarItensCEIntegracao(
			idItemCeIntegracao, encalhe, venda);
		
		fechamentoCEIntegracaoController.atualizarEncalheCalcularTotais(
			idItemCeIntegracao, encalhe, venda);
	},
	
	tratarAlteracaoVenda : function(idItemCeIntegracao, venda) {
		
		var reparte = $("#reparte" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
		var precoCapa = $("#precoCapa" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
		var encalhe = $("#encalhe" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
		
		var valorVenda = venda * priceToFloat(precoCapa);
		var valorVendaFormatado = floatToPrice(valorVenda);
		
		if (eval(reparte) < eval(venda)) {
			
			exibirMensagem(
				'WARNING', ["A quantidade de venda não pode exceder a quantidade do reparte!"]);
			
			$.each(fechamentoCEIntegracaoController.itensCEIntegracao, function(index, itemCEIntegracao) {
				
				if(itemCEIntegracao.id == idItemCeIntegracao) {
					
					var venda = itemCEIntegracao.venda;
					
					$("#inputVenda" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).val(venda);
					
					return false;
				}
			});
			
			return;
		}
		
		$("#valorVenda" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html(valorVendaFormatado);
		
		fechamentoCEIntegracaoController.atualizarItensCEIntegracao(
			idItemCeIntegracao, encalhe, venda);
		
		fechamentoCEIntegracaoController.atualizarEncalheCalcularTotais(
			idItemCeIntegracao, encalhe, venda);
	},
	
	atualizarItensCEIntegracao : function(idItemCeIntegracao, encalhe, venda) {
		
		$.each(fechamentoCEIntegracaoController.itensCEIntegracao, function(index, itemCEIntegracao) {
			
			if(itemCEIntegracao.id == idItemCeIntegracao) {
				
				itemCEIntegracao.encalhe = encalhe;
				itemCEIntegracao.venda = venda;
				
				return false;
			}
		});
	},
	
	atualizarEncalheCalcularTotais : function(idItemChamadaFornecedor, encalhe, venda) {
		
		var data = {
			'idItemChamadaFornecedor' : idItemChamadaFornecedor, 'encalhe': encalhe, 'venda' : venda
		};
		
		$.postJSON(contextPath + '/devolucao/fechamentoCEIntegracao/atualizarEncalheCalcularTotais', 
			data,
			function(result) {
				if (result) {
					
					$("#totalBruto", fechamentoCEIntegracaoController.workspace).html(result.totalBruto);
					$("#totalDesconto", fechamentoCEIntegracaoController.workspace).html(result.totalDesconto);
					$("#totalLiquido", fechamentoCEIntegracaoController.workspace).html(result.totalLiquido);
				}
			}
		);
	},
	
	fechamentoCeGridPreProcess : function(resultado) {
		if (resultado.mensagens) {
			
			exibirMensagem(resultado.mensagens.tipoMensagem, resultado.mensagens.listaMensagens);
			
			$(".grids", fechamentoCEIntegracaoController.workspace).hide();
			
			fechamentoCEIntegracaoController.esconderBotoes();
			
			return resultado;
			
		} else {
			
			if (!resultado.semanaFechada) {

				fechamentoCEIntegracaoController.itensCEIntegracao = [];
				
				$.each(resultado.listaFechamento.rows, function(index, row) {
					
					fechamentoCEIntegracaoController.itensCEIntegracao.push(
						{id: row.cell.idItemCeIntegracao, encalhe: row.cell.encalhe, venda: row.cell.venda});
					
					var isParcial = row.cell.tipoFormatado == 'PARCIAL';
					
					var colunaReparte =
						'<span id="reparte' + row.cell.idItemCeIntegracao + '">' +
							(row.cell.reparte)?row.cell.reparte:"" +
						'</span>';
					
					var colunaEncalhe;
					
					var colunaVenda;
					
					if (isParcial) {
						
						colunaEncalhe =
							'<span id="encalhe' + row.cell.idItemCeIntegracao + '">' +
								(row.cell.encalhe)?row.cell.encalhe:"" +
							'</span>';
						
						colunaVenda =
							'<input isEdicao="true" type="text" name="inputVenda"' +
							'id="inputVenda' + row.cell.idItemCeIntegracao + '"' +
							'value="' + row.cell.venda + '" size="5px"' +
							'onchange="fechamentoCEIntegracaoController.tratarAlteracaoVenda(' +
							row.cell.idItemCeIntegracao + ', this.value)"/>';
						
					} else {
						
						colunaEncalhe =
							'<input isEdicao="true" type="text" name="inputEncalhe"' +
							'id="inputEncalhe' + row.cell.idItemCeIntegracao + '"' +
							'value="' + (row.cell.encalhe)?row.cell.encalhe:'' + '" size="5px"' +
							'onchange="fechamentoCEIntegracaoController.tratarAlteracaoEncalhe(' +
							row.cell.idItemCeIntegracao + ', this.value)"/>';
						
						colunaVenda =
							'<span id="venda' + row.cell.idItemCeIntegracao + '">' +
								row.cell.venda +
							'</span>';
					}
					
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
			
			fechamentoCEIntegracaoController.mostrarBotoes();
			
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
		
		$(".fechamentoCeGrid", fechamentoCEIntegracaoController.workspace).flexReload();
		
	},
	
	popularTotal : function(resultado){
		$("#totalBruto", fechamentoCEIntegracaoController.workspace).html(resultado.totalBruto);
		$("#totalDesconto", fechamentoCEIntegracaoController.workspace).html(resultado.totalDesconto);
		$("#totalLiquido", fechamentoCEIntegracaoController.workspace).html(resultado.totalLiquido);
		$(".tabelaTotal", fechamentoCEIntegracaoController.workspace).show();
	},
	
	fecharCE : function(){
	
		$.postJSON(contextPath + '/devolucao/fechamentoCEIntegracao/fecharCE',
				 null,
				 function(resultado) {
				 	exibirMensagem(resultado.tipoMensagem, resultado.listaMensagens);
					$(".grids", fechamentoCEIntegracaoController.workspace).hide();
					fechamentoCEIntegracaoController.esconderBotoes();
					return resultado;
				 },
				null,
				true
			);
		
	},
	
	esconderBotoes : function() {
		
		$(".bt_acoes_grid_ce", fechamentoCEIntegracaoController.workspace).hide();
	},
	
	mostrarBotoes : function() {
	
		$(".bt_acoes_grid_ce", fechamentoCEIntegracaoController.workspace).show();
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
			
					fechamentoCEIntegracaoController.pesquisaPrincipal();
				 },
				null,
				true
			);
	}
	
}, BaseController);
//@ sourceURL=scriptFechamentoCEIntegracao.js
