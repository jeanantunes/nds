var fechamentoCEIntegracaoController = $.extend(true, {
	
	itensCEIntegracao : [],
	
	init : function(){
		fechamentoCEIntegracaoController.initGrid();
		fechamentoCEIntegracaoController.bindButtons();
		fechamentoCEIntegracaoController.buscarNumeroSemana();
		fechamentoCEIntegracaoController.esconderBotoes();

	},
	
	verificarDataFechamentoCE : function(fechada) {
			
			$("#btnReabertura", fechamentoCEIntegracaoController.workspace).unbind("click");
			
			$("#btnImpBoleto", fechamentoCEIntegracaoController.workspace).unbind("click");
			
			$("#btnImpBoletoEmBranco", fechamentoCEIntegracaoController.workspace).unbind("click");
			
			$("#btnFechamento", fechamentoCEIntegracaoController.workspace).unbind("click");
			
			$("#btnSalvarCE", fechamentoCEIntegracaoController.workspace).unbind("click");
			
			$("#btnImpressaoCE", fechamentoCEIntegracaoController.workspace).unbind("click");
			
			if (fechada) {					
				
				$("#imagemFechamento", fechamentoCEIntegracaoController.workspace).css("opacity", "0.2");
				
				$("#imagemSalvarCE", fechamentoCEIntegracaoController.workspace).css("opacity", "0.2");
				
				$("#imagemReabertura", fechamentoCEIntegracaoController.workspace).css("opacity", "1.0");
				
				$("#imagemImpressaoBoleto", fechamentoCEIntegracaoController.workspace).css("opacity", "1.0");
				
				$("#imagemBoletoEmBranco", fechamentoCEIntegracaoController.workspace).css("opacity", "1.0");
				
				$("#imagemImprimirCE", fechamentoCEIntegracaoController.workspace).css("opacity", "1.0");
				
				$("#btnReabertura", fechamentoCEIntegracaoController.workspace).click(function() {
					fechamentoCEIntegracaoController.reabrirCeIntegracao();
				});
				
				$("#btnImpBoleto", fechamentoCEIntegracaoController.workspace).click(function() {
					fechamentoCEIntegracaoController.geraBoleto('BOLETO');
				});
				
				$("#btnImpBoletoEmBranco", fechamentoCEIntegracaoController.workspace).click(function() {
					fechamentoCEIntegracaoController.geraBoleto('BOLETO_EM_BRANCO');
				});
				
				$("#btnImpressaoCE", fechamentoCEIntegracaoController.workspace).click(function() {
					fechamentoCEIntegracaoController.imprimirCE();
				});
				
			} else {
				
				$("#imagemReabertura", fechamentoCEIntegracaoController.workspace).css("opacity", "0.2");
				
				$("#imagemFechamento", fechamentoCEIntegracaoController.workspace).css("opacity", "1.0");
				
				$("#imagemSalvarCE", fechamentoCEIntegracaoController.workspace).css("opacity", "1.0");
				
				$("#imagemImpressaoBoleto", fechamentoCEIntegracaoController.workspace).css("opacity", "0.2");
				
				$("#imagemBoletoEmBranco", fechamentoCEIntegracaoController.workspace).css("opacity", "0.2");
				
				$("#imagemImprimirCE", fechamentoCEIntegracaoController.workspace).css("opacity", "0.2");
				
				$("#btnFechamento", fechamentoCEIntegracaoController.workspace).click(function() {
					fechamentoCEIntegracaoController.fecharCE();
				});
				
				$("#btnSalvarCE", fechamentoCEIntegracaoController.workspace).click(function() {
					fechamentoCEIntegracaoController.popupConfirmacao();
				});
			}
	},
	
	salvarCE:function(){
		
		$.postJSON(contextPath + '/devolucao/fechamentoCEIntegracao/salvarCE',
				 fechamentoCEIntegracaoController.getItensAlteradosCE(),
				 function(resultado) {
				 	exibirMensagem(resultado.tipoMensagem, resultado.listaMensagens);
					$(".grids", fechamentoCEIntegracaoController.workspace).hide();
					fechamentoCEIntegracaoController.esconderBotoes();
					fechamentoCEIntegracaoController.itensCEIntegracao = [];
					return resultado;
				 },
				null,
				true
			);
	},
	
	imprimirCE:function(){
		
		$.postJSON(contextPath + '/devolucao/fechamentoCEIntegracao/geraChamadaCE', null,
				
		function(result) {
			
			var file = contextPath + '/devolucao/fechamentoCEIntegracao/imprimirCE';
	
			$('#download-iframe-fechamento', fechamentoCEIntegracaoController.workspace).attr('src', file);
	
		});
		
	},
	
	getItensAlteradosCE:function(){
		
		var itens = [];
		
		$.each(fechamentoCEIntegracaoController.itensCEIntegracao, function(index, itemCEIntegracao) {
			
			if(itemCEIntegracao.alteracao) {

				itens.push({name:"itens[" + index + "].idItemCeIntegracao",value:itemCEIntegracao.id});
				itens.push({name:"itens[" + index + "].encalhe",value:itemCEIntegracao.encalhe});
				itens.push({name:"itens[" + index + "].venda",value:itemCEIntegracao.venda});				
			}
		});
		
		return itens;
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
	
	validarPreenchimentoCampo:function(campo,nomeCampo){
		
		if(campo.value.length < 1){
			exibirMensagem('WARNING', ["Deve ser informado um valor para campo "+nomeCampo+"!"]);
			campo.value = "0";
			campo.focus();
			return false;
		}
		return true;
	},
	
	popupConfirmacao : function() {
	
		$( "#dialog-ConfirmacaoSalvar", fechamentoCEIntegracaoController.workspace).dialog({
			resizable: false,
			height:150,
			width:300,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					
					fechamentoCEIntegracaoController.salvarCE();
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-ConfirmacaoSalvar", fechamentoCEIntegracaoController.workspace).parents("form")
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
			},{
				display : 'Integração NFE',
				name : 'integracaoNFEAprovado',
				width : 80,
				sortable : false,
				align : 'center'
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
	
	tratarAlteracaoEncalhe : function(idItemCeIntegracao, campo) {
		
		if(!fechamentoCEIntegracaoController.validarPreenchimentoCampo(campo,"Encalhe")){
			return;
		}
		
		var encalhe = campo.value;
		
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
	
	tratarAlteracaoVenda : function(idItemCeIntegracao, campo) {
		
		if(!fechamentoCEIntegracaoController.validarPreenchimentoCampo(campo,"Venda")){
			return;
		}
		
		var venda = campo.value;
		
		var reparte = $("#reparte" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
		var precoCapa = $("#precoCapa" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
		var encalhe = $("#encalhe" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
		
		var valorVenda = venda * priceToFloat(precoCapa);
		var valorVendaFormatado = floatToPrice(valorVenda);
		
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
				itemCEIntegracao.alteracao = true;
				
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
						{id: row.cell.idItemCeIntegracao, encalhe: row.cell.encalhe, venda: row.cell.venda,alteracao:false});
					
					var isParcial = row.cell.tipoFormatado == 'PARCIAL';
					
					var colunaReparte;
					
					var colunaEncalhe;
					
					var colunaVenda;
					
					colunaReparte =
						'<span id="reparte' + row.cell.idItemCeIntegracao + '">' +
							((row.cell.reparte) ? row.cell.reparte : "") +
						'</span>';
					
					if (isParcial) {
						
						colunaEncalhe =
							'<span id="encalhe' + row.cell.idItemCeIntegracao + '">' +
								((row.cell.encalhe) ? row.cell.encalhe : "") +
							'</span>';
						
						colunaVenda =
							'<input isEdicao="true" type="text" name="inputVenda"' +
							'id="inputVenda' + row.cell.idItemCeIntegracao + '"' +
							'value="' + row.cell.venda + '" size="5px"' +
							'onchange="fechamentoCEIntegracaoController.tratarAlteracaoVenda(' +
							row.cell.idItemCeIntegracao + ', this)"/>';
						
					} else {
						
						colunaEncalhe =
							'<input isEdicao="true" type="text" name="inputEncalhe" ' +
							'id="inputEncalhe' + row.cell.idItemCeIntegracao + '" ' +
							'value="' + ((row.cell.encalhe)?row.cell.encalhe:'') + '" size="5px" ' +
							'onchange="fechamentoCEIntegracaoController.tratarAlteracaoEncalhe(' +
							row.cell.idItemCeIntegracao + ', this)"/>';
						
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
					
					var nomeImagemStatusIntegracaoNFE = (!row.cell.integracaoNFEAprovado) 
														? "ico_reopen.gif" : "ico_check.gif";
					
					var colunaStatusIntegracaoNFE = 
						'<span>'+
							'<a  href="javascript:;" title="'+row.cell.statusIntegracaoNFE+'" rel="tipsy">'+
								'<img src="'+contextPath+'/images/'+nomeImagemStatusIntegracaoNFE+'" hspace="5" border="0"/>'+
							'</a>'+
						'</span>';
				
					row.cell.reparte = colunaReparte;
					row.cell.encalhe = colunaEncalhe;					
					row.cell.venda = colunaVenda;
					row.cell.precoCapaFormatado = colunaPrecoCapa;
					row.cell.valorVendaFormatado = colunaValorVenda;
					row.cell.integracaoNFEAprovado = colunaStatusIntegracaoNFE;
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
			         {name:'idFornecedor' , value:idFornecedor},
			         {name:'semana' , value:semana}
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
				 fechamentoCEIntegracaoController.getItensAlteradosCE(),
				 function(resultado) {
				 	exibirMensagem(resultado.tipoMensagem, resultado.listaMensagens);
					$(".grids", fechamentoCEIntegracaoController.workspace).hide();
					fechamentoCEIntegracaoController.esconderBotoes();
					fechamentoCEIntegracaoController.itensCEIntegracao = [];
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
