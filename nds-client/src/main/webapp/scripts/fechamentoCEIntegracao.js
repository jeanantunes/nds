var fechamentoCEIntegracaoController = $.extend(true, {
	
	itensCEIntegracao : [],
	
	init : function() {
		fechamentoCEIntegracaoController.initGrid();
		fechamentoCEIntegracaoController.bindButtons();
		fechamentoCEIntegracaoController.buscarNumeroSemana();
		fechamentoCEIntegracaoController.esconderBotoes();
		
		this.replicar = {
			all:false,
			list:[]
		};

	},
	
	
	buscarCESemana : function(){
		
		var semana = $("#semana", fechamentoCEIntegracaoController.workspace).val();
		
		if (  semana.length > 0 ) {
			
		var data = [
	   				{
	   					name: 'semana', value:semana
	   				}
	   			];
				
				$.getJSON(
						contextPath + '/devolucao/fechamentoCEIntegracao/obterCESemana', 
					data,
					function(result) {	
							
						if (result) {	
							
							var op = "";
							
							$.each(result, function(index,row){
								op+="<option value="+row.key.$+">"+row.value.$+"</option>";
							});
							$("#comboCE-fechamentoCe-integracao").empty().append(op);
							if(op.indexOf("ABERTO")> 0  && op.indexOf("FECHADO")> 0 )
								exibirMensagem('WARNING', ["Existem CE em aberto. É necessário efetuar o fechamento."]);
							
							
							
						}
					}
				);
		}
	},
	verificarDataFechamentoCE : function(fechada) {
			
			$("#btnReabertura", fechamentoCEIntegracaoController.workspace).unbind("click");
			
			$("#btnImpBoleto", fechamentoCEIntegracaoController.workspace).unbind("click");
			
			$("#btnImpBoletoEmBranco", fechamentoCEIntegracaoController.workspace).unbind("click");
			
			$("#btnFechamento", fechamentoCEIntegracaoController.workspace).unbind("mousedown");
			
			$("#btnSalvarCE", fechamentoCEIntegracaoController.workspace).unbind("mousedown");
			
			$("#btnImpressaoCE", fechamentoCEIntegracaoController.workspace).unbind("click");
			
			if (fechada) {					
				
				$("#imagemFechamento", fechamentoCEIntegracaoController.workspace).css("opacity", "0.2");
				
				$("#imagemSalvarCE", fechamentoCEIntegracaoController.workspace).css("opacity", "0.2");
				
				$("#imagemReabertura", fechamentoCEIntegracaoController.workspace).css("opacity", "1.0");
				
				$("#imagemImpressaoBoleto", fechamentoCEIntegracaoController.workspace).css("opacity", "1.0");
				
				$("#imagemImprimirCE", fechamentoCEIntegracaoController.workspace).css("opacity", "1.0");
				
				$("#imagemGerarNotaCE", fechamentoCEIntegracaoController.workspace).css("opacity", "1.0");
				
				$("#btnReabertura", fechamentoCEIntegracaoController.workspace).click(function() {
					fechamentoCEIntegracaoController.reabrirCeIntegracao();
				});
				
				$("#btnImpBoleto", fechamentoCEIntegracaoController.workspace).click(function() {
					fechamentoCEIntegracaoController.geraBoleto('BOLETO');
				});
				
				$("#btnImpressaoCE", fechamentoCEIntegracaoController.workspace).click(function() {
					fechamentoCEIntegracaoController.imprimirCE();
				});
				
			} else {
				
				$("#imagemReabertura", fechamentoCEIntegracaoController.workspace).css("opacity", "0.2");
				
				$("#imagemFechamento", fechamentoCEIntegracaoController.workspace).css("opacity", "1.0");
				
				$("#imagemSalvarCE", fechamentoCEIntegracaoController.workspace).css("opacity", "1.0");
				
				$("#imagemImpressaoBoleto", fechamentoCEIntegracaoController.workspace).css("opacity", "0.2");
				
				$("#imagemImprimirCE", fechamentoCEIntegracaoController.workspace).css("opacity", "0.2");
				
				$("#imagemGerarNotaCE", fechamentoCEIntegracaoController.workspace).css("opacity", "0.2");
				
				$("#btnFechamento", fechamentoCEIntegracaoController.workspace).mousedown(function() {
					fechamentoCEIntegracaoController.validarPerdaGanho();
				});
				
				$("#btnSalvarCE", fechamentoCEIntegracaoController.workspace).mousedown(function() {
					fechamentoCEIntegracaoController.popupConfirmacao();
				});
			}
			
			$("#btnImpBoletoEmBranco", fechamentoCEIntegracaoController.workspace).click(function() {
				fechamentoCEIntegracaoController.geraBoleto('BOLETO_EM_BRANCO');
			});
	},
	
	salvarCE : function() {
		
		$.postJSON(contextPath + '/devolucao/fechamentoCEIntegracao/salvarCE',
				 fechamentoCEIntegracaoController.getItensAlteradosCE(),
				 function(resultado) {
				 	exibirMensagem(resultado.tipoMensagem, resultado.listaMensagens);
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
	
	nextInputExemplares : function(curIndex, evt) {
	
		if (evt.keyCode == 13 || evt.keyCode == 40) {
			var nextElement = $('[tabindex=' + (curIndex + 1) + ']');
			nextElement.focus();
			nextElement.select();
		}else if (event.keyCode == 38) {
			var nextElement = $('[tabindex=' + (curIndex - 1) + ']');
			nextElement.focus();
			nextElement.select();  
		} 
	},
	
	getItensAlteradosCE : function() {
		
		var itens = [];
		
		$.each(fechamentoCEIntegracaoController.itensCEIntegracao, function(index, itemCEIntegracao) {
		
			if(itemCEIntegracao.alteracao) 
			{

				itens.push({name:"itens[" + index + "].idItemCeIntegracao",value:itemCEIntegracao.id});
				itens.push({name:"itens[" + index + "].encalhe",value:itemCEIntegracao.encalhe});
				itens.push({name:"itens[" + index + "].venda",value:itemCEIntegracao.venda});
				itens.push({name:"itens[" + index + "].diferenca",value:itemCEIntegracao.diferenca});
				itens.push({name:"itens[" + index + "].estoque",value:itemCEIntegracao.estoque});
				
				if($("#combo-fechamentoCe-integracao", fechamentoCEIntegracaoController.workspace).val() == "SEM") {
					
					itens.push({name:"itens[" + index + "].codigoProduto", value:itemCEIntegracao.codigoProduto});
					itens.push({name:"itens[" + index + "].nomeProduto", value:itemCEIntegracao.nomeProduto});
					itens.push({name:"itens[" + index + "].numeroEdicao", value:itemCEIntegracao.numeroEdicao});
					itens.push({name:"itens[" + index + "].precoCapa", value:itemCEIntegracao.precoCapa});
					itens.push({name:"itens[" + index + "].isProdutoSemCe", value:true});
				} else {
					itens.push({name:"itens[" + index + "].qtdeDevSemCE", value:itemCEIntegracao.qtdeDevSemCE});
				}
				
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
							fechamentoCEIntegracaoController.buscarCESemana();
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
	
	aplicarNumericCampo : function() {
		
		$('input[id^="inputEncalhe"]', fechamentoCEIntegracaoController.workspace).numeric();
		$('input[id^="inputVenda"]', fechamentoCEIntegracaoController.workspace).numeric();
	},
	
	initGrid : function(){	
		$(".fechamentoCeGrid", fechamentoCEIntegracaoController.workspace).flexigrid({
			onSuccess: function() {
				bloquearItensEdicao(fechamentoCEIntegracaoController.workspace);
				fechamentoCEIntegracaoController.aplicarNumericCampo();
			},
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
			}, {
				display : 'Qtde Dev Inf',
				name : 'qtdeDevSemCE',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Estoque',
				name : 'estoque',
				width : 100,
				sortable : false,
				align : 'center'
			}, {
				display : 'Encalhe',
				name : 'encalhe',
				width : 100,
				sortable : false,
				align : 'center'
			},  {
				display : 'Diferenca',
				name : 'diferenca',
				width : 100,
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
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Valor Venda R$',
				name : 'valorVendaFormatado',
				width : 100,
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
		
		$(".fechamentoSemCeGrid", fechamentoCEIntegracaoController.workspace).flexigrid({
			onSuccess: function() {
				bloquearItensEdicao(fechamentoCEIntegracaoController.workspace);
				fechamentoCEIntegracaoController.aplicarNumericCampo();
			},
			preProcess : fechamentoCEIntegracaoController.fechamentoSemCeGridPreProcess,
			dataType : 'json',
			colModel : [{
				display : 'Código',
				name : 'codigoProduto',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço Capa R$',
				name : 'precoCapaFormatado',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Exemplar Devolução',
				name : 'qtdDevolucao',
				width : 110,
				sortable : true,
				align : 'center'
			}, {
				display : 'Total c/ Desc.R$',
				name : 'valorTotalComDescontoFormatado',
				width : 100,
				sortable : false,
				align : 'right'
			}, {
				display : 'Total R$',
				name : 'valorVendaFormatado',
				width : 80,
				sortable : false,
				align : 'right'
			}, {
				display : 'Exemplar Nota',
				name : 'venda',
				width : 100,
				sortable : false,
				align : 'center'
			}, {
				display : 'Diferença',
				name : 'diferenca',
				width : 100,
				sortable : false,
				align : 'center'
			},{
				display : 'Replicar Qtde',
				name : 'replicarQtde',
				width : 70,
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
	
	tratarAlteracaoEncalhe : function(idItemCeIntegracao, campo,isFinal) {
		
		if(!fechamentoCEIntegracaoController.validarPreenchimentoCampo(campo, "Encalhe")){
			return;
		}
		
	
		
		if ( isFinal) {
			return;
		}
		
		var encalhe = campo.value;
		
		var qtdeDevSemCE = $("#qtdeDevSemCE" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
		
		var reparte = $("#reparte" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
		var precoCapa = $("#precoCapa" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
		
		var diferenca = $("#diferenca" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
		var estoque = $("#estoque" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
		
		var venda = eval(reparte) - ( eval(encalhe) + eval(qtdeDevSemCE));
		
		var valorVenda = venda * priceToFloat(precoCapa);
		var valorVendaFormatado = floatToPrice(valorVenda);
		
		if (eval(reparte) < eval(encalhe)) {
			
			exibirMensagem('WARNING', ["A quantidade de encalhe não pode exceder a quantidade do reparte!"]);
			
			$.each(fechamentoCEIntegracaoController.itensCEIntegracao, function(index, itemCEIntegracao) {
				
				if(itemCEIntegracao.id == idItemCeIntegracao) {
					
					var encalhe = itemCEIntegracao.encalhe;
					
					$("#inputEncalhe" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).val(encalhe);
					
					return false;
				}
			});
			
			return;
		}
		
		$("#inputVenda" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).val(venda);
		$("#valorVenda" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html(valorVendaFormatado);
		
		fechamentoCEIntegracaoController.atualizarDiferenca(encalhe, idItemCeIntegracao);
		
		diferenca = $("#diferenca" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
		
		fechamentoCEIntegracaoController.atualizarItensCEIntegracao(idItemCeIntegracao, encalhe, venda, diferenca, estoque, qtdeDevSemCE);
		
		fechamentoCEIntegracaoController.atualizarEncalheCalcularTotais(idItemCeIntegracao, encalhe, venda);
	},
	
	
	tratarAlteracaoEncalheSemCE : function(idItemCeIntegracao, campo) {
		
		if(!fechamentoCEIntegracaoController.validarPreenchimentoCampo(campo,"Encalhe")){
			return;
		}
		
		var encalhe = campo.value;
		
		var reparte = $("#qtdeDevolucao" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
		
		var precoCapa = $("#precoCapa" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
		
		var diferenca = $("#diferenca" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
		
		var estoque = $("#estoque" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
		
		var codigoProduto = $("#codigoProduto" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
		
		var nomeProduto = $("#nomeProduto" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
		
		var numeroEdicao = $("#numeroEdicao" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();

		var venda = encalhe;
		
		var valorVenda = venda * priceToFloat(precoCapa);
		
		var valorVendaFormatado = floatToPrice(valorVenda);
		
		if (eval(reparte) < eval(encalhe)) {
			
			exibirMensagem('WARNING', ["A quantidade de encalhe não pode exceder a quantidade do reparte!"]);
			
			$.each(fechamentoCEIntegracaoController.itensCEIntegracao, function(index, itemCEIntegracao) {
				
				if(itemCEIntegracao.id == idItemCeIntegracao) {
					
					var encalhe = itemCEIntegracao.encalhe;
					
					$("#inputEncalhe" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).val(encalhe);
					
					return false;
				}
			});
			
			return;
		}
		
		$("#inputVenda" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).val(venda);
		
		$("#valorVenda" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html(valorVendaFormatado);
		
		fechamentoCEIntegracaoController.atualizarDiferenca(encalhe, idItemCeIntegracao);
		
		diferenca = $("#diferenca" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
		
		if($("#combo-fechamentoCe-integracao", fechamentoCEIntegracaoController.workspace).val() == "COM") {
			
			fechamentoCEIntegracaoController.atualizarItensCEIntegracao(idItemCeIntegracao, encalhe, venda, diferenca, estoque);
			fechamentoCEIntegracaoController.atualizarEncalheCalcularTotais(idItemCeIntegracao, encalhe, venda);
		
		} else {
			
			fechamentoCEIntegracaoController.atualizarItensCEIntegracaoSemCE(idItemCeIntegracao, encalhe, venda, diferenca, estoque, codigoProduto, nomeProduto, numeroEdicao, precoCapa);
			
		}
		
	},
	
	atualizarDiferenca : function(encalhe,idItemCeIntegracao) {
		
		var estoque = 0;
		
		var valorDiferenca = 0;
		
		var venda = 0;
		
		if($("#combo-fechamentoCe-integracao", fechamentoCEIntegracaoController.workspace).val() == "COM") {
			
			estoque = $("#estoque" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
			
			var reparte = $("#reparte" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
			
			valorDiferenca = eval(encalhe) - eval(estoque);
			
			$("#diferenca" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html(valorDiferenca);
			
			var qtdeDevSemCE = $("#qtdeDevSemCE" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
			
			$("#venda" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html(eval(reparte) - ( eval(encalhe) + eval(qtdeDevSemCE)));
			
		} else {
			
			estoque = $("#qtdeDevolucao" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
			
			valorDiferenca = eval(encalhe) - eval(estoque);
			
			$("#diferenca" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html(valorDiferenca);
		}
		
		
	},
	
	tratarAlteracaoVenda : function(idItemCeIntegracao, isFinal, campo) {
		
		if(!fechamentoCEIntegracaoController.validarPreenchimentoCampo(campo, "Venda")){
			return true;
		}
		
		var venda = campo.value;
	
		var precoCapa = $("#precoCapa" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
		
		var encalhe = 0; 
		
		if(isFinal) {
			
			encalhe = $("#inputEncalhe" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).val();
			
		} else {
			
			encalhe = $("#encalhe" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
			
		}
		
		var diferenca = $("#diferenca" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
		var estoque = $("#estoque" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
		var reparte = $("#reparte" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html();
		
		var valorVenda = venda * priceToFloat(precoCapa);
		var valorVendaFormatado = floatToPrice(valorVenda);
		
		$("#valorVenda" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).html(valorVendaFormatado);
		
		if (!isNaN(reparte) && eval(reparte) < eval(venda)) {
			
			exibirMensagem('WARNING', ["A quantidade de venda não pode exceder a quantidade do reparte!"]);
			
			$.each(fechamentoCEIntegracaoController.itensCEIntegracao, function(index, itemCEIntegracao) {
				
				if(itemCEIntegracao.id == idItemCeIntegracao) {
					
					var venda = itemCEIntegracao.venda;
					
					$("#inputVenda" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).val(venda);
					
					return false;
				}
			});
			
			return true;
		}
		
		if(!isFinal){
			
			$("#inputEncalhe" + idItemCeIntegracao, fechamentoCEIntegracaoController.workspace).val(reparte - venda);
		} 
				
		fechamentoCEIntegracaoController.atualizarItensCEIntegracao(idItemCeIntegracao, encalhe, venda, diferenca, estoque);
		
		fechamentoCEIntegracaoController.atualizarEncalheCalcularTotais(idItemCeIntegracao, encalhe, venda);
		
		return true;
		
	},
	
	atualizarItensCEIntegracao : function(idItemCeIntegracao, encalhe, venda, diferenca, estoque, qtdeDevSemCE) {
		
		$.each(fechamentoCEIntegracaoController.itensCEIntegracao, function(index, itemCEIntegracao) {
			
			if(itemCEIntegracao.id == idItemCeIntegracao) {
				itemCEIntegracao.qtdeDevSemCE = qtdeDevSemCE;
				itemCEIntegracao.encalhe = encalhe;
				itemCEIntegracao.venda = venda;
				itemCEIntegracao.diferenca = diferenca;
				itemCEIntegracao.estoque = estoque;
				itemCEIntegracao.alteracao = true;
				
				return false;
			}
		});
	},
	
	atualizarItensCEIntegracaoSemCE : function(idItemCeIntegracao, encalhe, venda, diferenca, estoque, codigoProduto, nomeProduto, numeroEdicao, precoCapa) {
		
		$.each(fechamentoCEIntegracaoController.itensCEIntegracao, function(index, itemCEIntegracao) {
			
			if(itemCEIntegracao.id == idItemCeIntegracao) {
				
				itemCEIntegracao.codigoProduto = codigoProduto;
				itemCEIntegracao.nomeProduto = nomeProduto;
				itemCEIntegracao.numeroEdicao = numeroEdicao;
				itemCEIntegracao.encalhe = encalhe;
				itemCEIntegracao.venda = venda;
				itemCEIntegracao.diferenca = diferenca;
				itemCEIntegracao.estoque = estoque;
				itemCEIntegracao.alteracao = true;
				itemCEIntegracao.precoCapa = precoCapa;
				
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
					
					fechamentoCEIntegracaoController.itensCEIntegracao.push({id: row.cell.idItemCeIntegracao, encalhe: row.cell.encalhe, venda: row.cell.venda,alteracao:false});
					
					var isParcial = row.cell.tipoFormatado == 'PARCIAL';
					var isFinal = row.cell.tipoFormatado == 'FINAL';
					
					var colunaQtdeDevForn;
					
					var colunaReparte;
					
					var colunaEncalhe;
					
					var colunaVenda;
					
					var valorDiferenca = 0;
					
					if (isParcial) {
						
						colunaQtdeDevForn =
							'<span id="qtdeDevSemCE' + row.cell.idItemCeIntegracao + '">' +
								((row.cell.qtdeDevSemCE) ? row.cell.qtdeDevSemCE : "") +
							'</span>';
						
						colunaReparte =
							'<span id="reparte' + row.cell.idItemCeIntegracao + '">' +
								((row.cell.reparte) ? (row.cell.reparte = "0") ? "***" : row.cell.reparte : "***") +
							'</span>';
						
						colunaEncalhe =
							'<span id="encalhe' + row.cell.idItemCeIntegracao + '">' +
								((row.cell.encalhe) ? (row.cell.encalhe = "0" ) ? "***" : row.cell.encalhe: "***") +
							'</span>';
						
						colunaVenda =
							' <input style="width:45px;" isEdicao="true" type="text" name="inputVenda"' +
							' id="inputVenda' + row.cell.idItemCeIntegracao + '"' +
							' value="' + row.cell.venda + '" size="5px"' +
							' tabindex="' + (++index) +'"' +
							' onkeydown="fechamentoCEIntegracaoController.nextInputExemplares('+index+', window.event);"' +
							' onblur="fechamentoCEIntegracaoController.tratarAlteracaoVenda(' +row.cell.idItemCeIntegracao + ', '+isFinal +', this)"/>';
						
					} else if (isFinal) {
						
						colunaQtdeDevForn =
							'<span id="qtdeDevSemCE' + row.cell.idItemCeIntegracao + '">' +
								((row.cell.qtdeDevSemCE) ? row.cell.qtdeDevSemCE : "") +
							'</span>';
						
						colunaReparte =
							'<span id="reparte' + row.cell.idItemCeIntegracao + '">' +
								((row.cell.reparte) ? row.cell.reparte : "") +
							'</span>';
						
						colunaEncalhe =
							' <input style="width:45px;" isEdicao="true" type="text" name="inputEncalhe" ' +
							' id="inputEncalhe' + row.cell.idItemCeIntegracao + '" ' +
							' value="' + ((row.cell.encalhe)?row.cell.encalhe:'') + '" size="5px" ' +
							' tabindex="' + (++index) +'"' +
							' onkeydown="fechamentoCEIntegracaoController.nextInputExemplares('+index+', window.event);"' +
							' onblur="fechamentoCEIntegracaoController.tratarAlteracaoEncalhe(' +
							row.cell.idItemCeIntegracao + ', this,'+isFinal+')"/>';
							
							colunaVenda =
								' <input style="width:45px;" isEdicao="true" type="text" name="inputVenda"' +
								' id="inputVenda' + row.cell.idItemCeIntegracao + '"' +
								' value="' + row.cell.venda + '" size="5px"' +
								' tabindex="' + (++index) +'"' +
								' onkeydown="fechamentoCEIntegracaoController.nextInputExemplares('+index+', window.event);"' +
								' onblur="fechamentoCEIntegracaoController.tratarAlteracaoVenda(' + row.cell.idItemCeIntegracao + ', '+isFinal +' , this)"/>';
							
					} else {
						
						colunaQtdeDevForn =
							'<span id="qtdeDevSemCE' + row.cell.idItemCeIntegracao + '">' +
								((row.cell.qtdeDevSemCE) ? row.cell.qtdeDevSemCE : "") +
							'</span>';
						
						colunaReparte =
							'<span id="reparte' + row.cell.idItemCeIntegracao + '">' +
								((row.cell.reparte) ? row.cell.reparte : "") +
							'</span>';
						
						colunaEncalhe =
							' <input style="width:45px;" isEdicao="true" type="text" name="inputEncalhe" ' +
							' id="inputEncalhe' + row.cell.idItemCeIntegracao + '" ' +
							' value="' + ((row.cell.encalhe)?row.cell.encalhe:'') + '" size="5px" ' +
							' tabindex="' + (++index) +'"' +
							' onkeydown="fechamentoCEIntegracaoController.nextInputExemplares('+index+', window.event);"' +
							' onblur="fechamentoCEIntegracaoController.tratarAlteracaoEncalhe(' +
							row.cell.idItemCeIntegracao + ', this,'+isFinal+')"/>';
						
						colunaVenda =	
							'<span id="venda' + row.cell.idItemCeIntegracao + '">' +
								row.cell.venda +
							'</span>';
					
						colunaQtdeDevolucao =	
							'<span id="qtdeDevolucao' + row.cell.idItemCeIntegracao + '">' +
								row.cell.qtdeDevolucao +
							'</span>';

						if(row.cell.diferenca == undefined){
							
							valorDiferenca = row.cell.encalhe - row.cell.estoque ;
						}

					}
					
					var colunaEstoque =
						'<span id="estoque' + row.cell.idItemCeIntegracao + '">' +
							row.cell.estoque +
						'</span>';
					
					var colunaDiferenca = '';
						
				
					if (isFinal) {
						
						var valorDiferencaAux = row.cell.encalhe - row.cell.estoque;
						
						colunaDiferenca =
							'<span id="diferenca' + row.cell.idItemCeIntegracao + '">' 
							+ valorDiferencaAux +
							'</span>';
						
					} else {
						
						colunaDiferenca =
						'<span id="diferenca' + row.cell.idItemCeIntegracao + '">' +
						valorDiferenca +
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
					
					row.cell.estoque = colunaEstoque;
					row.cell.reparte = colunaReparte;
					row.cell.qtdeDevSemCE = colunaQtdeDevForn;
					row.cell.encalhe = colunaEncalhe;
					row.cell.diferenca = colunaDiferenca;
					row.cell.venda = colunaVenda;
					row.cell.precoCapaFormatado = colunaPrecoCapa;
					row.cell.valorVendaFormatado = colunaValorVenda;
					
				});
				
			} else {
				
				$.each(resultado.listaFechamento.rows, function(index, row) {
					if(row.cell.diferenca == undefined) {
						row.cell.diferenca = 0;
					}
				});
			}
			
			fechamentoCEIntegracaoController.popularTotal(resultado);
			fechamentoCEIntegracaoController.verificarDataFechamentoCE(resultado.semanaFechada);
			
			$(".grids", fechamentoCEIntegracaoController.workspace).show();
			
			fechamentoCEIntegracaoController.mostrarBotoes();
			
			return resultado.listaFechamento;
			
		};

	},
	
	
	fechamentoSemCeGridPreProcess : function(resultado) {
		
		if (resultado.mensagens) {
			
			exibirMensagem(resultado.mensagens.tipoMensagem, resultado.mensagens.listaMensagens);
			
			$(".grids", fechamentoCEIntegracaoController.workspace).hide();
			
			fechamentoCEIntegracaoController.esconderBotoes();
			
			return resultado;
			
		} else {
			
			if (!resultado.semanaFechada) {

				fechamentoCEIntegracaoController.itensCEIntegracao = [];
				
				var checked = '';
				
				$.each(resultado.listaFechamento.rows, function(index, row) {
					
					var id = index;
					
					fechamentoCEIntegracaoController.itensCEIntegracao.push(
						{id: row.cell.idItemCeIntegracao, encalhe: row.cell.encalhe, venda: row.cell.venda,alteracao:false});
					
					var isParcial = row.cell.tipoFormatado == 'PARCIAL';
					var isFinal = row.cell.tipoFormatado == 'FINAL';
					
					var colunaReparte;
					
					var colunaEncalhe;
					
					var colunaVenda;
					
					var valorDiferenca = 0;
					
					var colunaCodigoProduto = 0;
					
					var colunaNomeProduto = null;
					
					var colunaNumeroEdicao = 0;
					
					if (isParcial) {
						
						colunaReparte =
							'<span id="reparte' + row.cell.idItemCeIntegracao + '">' +
								((row.cell.reparte) ? (row.cell.reparte = "0") ? "***" : row.cell.reparte : "***") +
							'</span>';
						
						colunaEncalhe =
							'<span id="encalhe' + row.cell.idItemCeIntegracao + '">' +
								((row.cell.encalhe) ? (row.cell.encalhe = "0" ) ? "***" : row.cell.encalhe: "***") +
							'</span>';
						
						colunaVenda =
							' <input style="width:45px;" isEdicao="true" type="text" name="inputVenda"' +
							' id="inputVenda' + row.cell.idItemCeIntegracao + '"' +
							' value="' + row.cell.venda + '" size="5px"' +
							' tabindex="' + (++index) +'"' +
							' onkeydown="fechamentoCEIntegracaoController.nextInputExemplares('+index+', window.event);"' +
							' onblur="fechamentoCEIntegracaoController.tratarAlteracaoVenda(' +row.cell.idItemCeIntegracao + ', '+isFinal +' , this)"/>';
						
					} else if (isFinal) {
												
						colunaReparte =
							'<span id="reparte' + row.cell.idItemCeIntegracao + '">' +
								((row.cell.reparte) ? row.cell.reparte : "") +
							'</span>';
						
						colunaEncalhe =
							' <input style="width:45px;" isEdicao="true" type="text" name="inputEncalhe" ' +
							' id="inputEncalhe' + row.cell.idItemCeIntegracao + '" ' +
							' value="' + ((row.cell.encalhe)?row.cell.encalhe:'') + '" size="5px" ' +
							' tabindex="' + (++index) +'"' +
							' onkeydown="fechamentoCEIntegracaoController.nextInputExemplares('+index+', window.event);"' +
							' onblur="fechamentoCEIntegracaoController.tratarAlteracaoEncalhe(' +
							row.cell.idItemCeIntegracao + ', this,'+isFinal+')"/>';
							
							colunaVenda =
								' <input style="width:45px;" isEdicao="true" type="text" name="inputVenda"' +
								' id="inputVenda' + row.cell.idItemCeIntegracao + '"' +
								' value="' + row.cell.venda + '" size="5px"' +
								' tabindex="' + (++index) +'"' +
								' onkeydown="fechamentoCEIntegracaoController.nextInputExemplares('+index+', window.event);"' +
								' onblur="fechamentoCEIntegracaoController.tratarAlteracaoVenda(' + row.cell.idItemCeIntegracao + ', '+isFinal +' , this)"/>';
							
						} else {
						
							colunaCodigoProduto =
								'<span id="codigoProduto' + row.cell.idItemCeIntegracao + '">' +
									((row.cell.codigoProduto) ? row.cell.codigoProduto : "") +
								'</span>';

							colunaNomeProduto =
								'<span id="nomeProduto' + row.cell.idItemCeIntegracao + '">' +
									((row.cell.nomeProduto) ? row.cell.nomeProduto : "") +
								'</span>';
							
							colunaNumeroEdicao =
								'<span id="numeroEdicao' + row.cell.idItemCeIntegracao + '">' +
									((row.cell.numeroEdicao) ? row.cell.numeroEdicao : "") +
								'</span>';
							
							colunaReparte =
								'<span id="reparte' + row.cell.idItemCeIntegracao + '">' +
									((row.cell.reparte) ? row.cell.reparte : "") +
								'</span>';
						
							colunaEncalhe =
								' <input style="width:45px;"  isEdicao="true" type="text" name="inputEncalhe" ' +
								' id="inputEncalhe' + row.cell.idItemCeIntegracao + '" ' +
								' value="' + ((row.cell.encalhe)?row.cell.encalhe:'') + '" size="5px" ' +
								' tabindex="' + (++index) +'"' +
								' onkeydown="fechamentoCEIntegracaoController.nextInputExemplares('+index+', window.event);"' +
								' onchange="fechamentoCEIntegracaoController.tratarAlteracaoEncalheSemCE(' +
								row.cell.idItemCeIntegracao + ', this,'+isFinal+')"/>';
							
							colunaVenda =
								' <input style="width:45px;" isEdicao="true" type="text" name="inputVenda"' +
								' id="inputVenda' + row.cell.idItemCeIntegracao + '"' +
								' value="' + row.cell.encalhe + '" size="5px"' +
								' tabindex="' + (++index) +'"' +
								' onkeydown="fechamentoCEIntegracaoController.nextInputExemplares('+index+', window.event);"' +
								' onblur="fechamentoCEIntegracaoController.tratarAlteracaoEncalheSemCE(' +
								row.cell.idItemCeIntegracao + ', this)"/>';

						if(row.cell.diferenca == undefined){
							valorDiferenca = row.cell.encalhe - row.cell.estoque ;
						}

					}
					
					var colunaEstoque =
						'<span id="estoque' + row.cell.idItemCeIntegracao + '">' +
							row.cell.estoque +
						'</span>';
					
					var colunaDiferenca =
							'<span id="diferenca' + row.cell.idItemCeIntegracao + '">' +
							valorDiferenca +
							'</span>';
					
					var colunaPrecoCapa =
						'<span id="precoCapa' + row.cell.idItemCeIntegracao + '">' +
							row.cell.precoCapaFormatado +
						'</span>';
					
					var colunaValorVenda =
						'<span id="valorVenda' + row.cell.idItemCeIntegracao + '">' +
							row.cell.valorVendaFormatado +
						'</span>';
					
					colunaQtdeDevolucao =	
						'<span id="qtdeDevolucao' + row.cell.idItemCeIntegracao + '">' +
							row.cell.qtdDevolucao +
						'</span>';
					
					var inputCheckReplicarValor = '<input isEdicao="true" type="checkbox" id="ch'+row.cell.idItemCeIntegracao+
					'" class="chBoxReplicar" name="checkgroup" ' + checked + ' data-id="'+row.cell.idItemCeIntegracao
					+'" onclick="fechamentoCEIntegracaoController.replicarValor(\''+row.cell.idItemCeIntegracao+'\')"/>';
					
					row.cell.replicarQtde = inputCheckReplicarValor;
					
					//Altera cor do valor da quantidade, caso seja um valo negativo
					if (row.cell.diferenca < 0){
						corDif = 'color:red';
					}
					
					row.cell.estoque = colunaEstoque;
					row.cell.diferenca = colunaDiferenca;
					row.cell.reparte = colunaReparte;
					row.cell.encalhe = colunaEncalhe;					
					row.cell.venda = colunaVenda;
					row.cell.precoCapaFormatado = colunaPrecoCapa;
					row.cell.valorVendaFormatado = colunaValorVenda;
					row.cell.qtdDevolucao = colunaQtdeDevolucao;
					row.cell.codigoProduto = colunaCodigoProduto;
					row.cell.nomeProduto = colunaNomeProduto;
					row.cell.numeroEdicao = colunaNumeroEdicao;
					
				});
				
				
			} else {
				
				$.each(resultado.listaFechamento.rows, function(index, row) {
					
					if(row.cell.diferenca == undefined){
						row.cell.diferenca = 0;
					}
				});
			}
			
			fechamentoCEIntegracaoController.popularTotal(resultado);
			
			fechamentoCEIntegracaoController.verificarDataFechamentoCE(resultado.semanaFechada);
			
			$(".grids", fechamentoCEIntegracaoController.workspace).show();
			
			fechamentoCEIntegracaoController.mostrarBotoes();
			
			$("#btnSalvarCE", fechamentoCEIntegracaoController.workspace).hide();
			
			return resultado.listaFechamento;
			
		};

	},
	
	verificarDataFechamentoCE : function(fechada) {
		
		$("#btnReabertura", fechamentoCEIntegracaoController.workspace).unbind("click");
		
		$("#btnImpBoleto", fechamentoCEIntegracaoController.workspace).unbind("click");
		
		$("#btnImpBoletoEmBranco", fechamentoCEIntegracaoController.workspace).unbind("click");
		
		$("#btnFechamento", fechamentoCEIntegracaoController.workspace).unbind("mousedown");
		
		$("#btnSalvarCE", fechamentoCEIntegracaoController.workspace).unbind("mousedown");
		
		$("#btnImpressaoCE", fechamentoCEIntegracaoController.workspace).unbind("click");
		
		if (fechada) {					
			
			$("#imagemFechamento", fechamentoCEIntegracaoController.workspace).css("opacity", "0.2");
			
			$("#imagemSalvarCE", fechamentoCEIntegracaoController.workspace).css("opacity", "0.2");
			
			$("#imagemReabertura", fechamentoCEIntegracaoController.workspace).css("opacity", "1.0");
			
			$("#imagemImpressaoBoleto", fechamentoCEIntegracaoController.workspace).css("opacity", "1.0");
			
			$("#imagemImprimirCE", fechamentoCEIntegracaoController.workspace).css("opacity", "1.0");
			
			$("#imagemGerarNotaCE", fechamentoCEIntegracaoController.workspace).css("opacity", "1.0");
			
			$("#btnReabertura", fechamentoCEIntegracaoController.workspace).click(function() {
				fechamentoCEIntegracaoController.reabrirCeIntegracao();
			});
			
			$("#btnImpBoleto", fechamentoCEIntegracaoController.workspace).click(function() {
				fechamentoCEIntegracaoController.geraBoleto('BOLETO');
			});
			
			$("#btnImpressaoCE", fechamentoCEIntegracaoController.workspace).click(function() {
				fechamentoCEIntegracaoController.imprimirCE();
			});
			
		} else {
			
			$("#imagemReabertura", fechamentoCEIntegracaoController.workspace).css("opacity", "0.2");
			
			$("#imagemFechamento", fechamentoCEIntegracaoController.workspace).css("opacity", "1.0");
			
			$("#imagemSalvarCE", fechamentoCEIntegracaoController.workspace).css("opacity", "1.0");
			
			$("#imagemImpressaoBoleto", fechamentoCEIntegracaoController.workspace).css("opacity", "0.2");
			
			$("#imagemImprimirCE", fechamentoCEIntegracaoController.workspace).css("opacity", "0.2");
			
			$("#imagemGerarNotaCE", fechamentoCEIntegracaoController.workspace).css("opacity", "0.2");
			
			if($("#combo-fechamentoCe-integracao", fechamentoCEIntegracaoController.workspace).val() == "SEM") {
				
				$("#btnFechamento", fechamentoCEIntegracaoController.workspace).mousedown(function() {
					fechamentoCEIntegracaoController.popupConfirmacaoFechamrentoSemCE();
				});
			} else {
				
				$("#btnFechamento", fechamentoCEIntegracaoController.workspace).mousedown(function() {
					fechamentoCEIntegracaoController.validarPerdaGanho();
				});
				
			}
			
			$("#btnSalvarCE", fechamentoCEIntegracaoController.workspace).mousedown(function() {
				
				fechamentoCEIntegracaoController.popupConfirmacao();
				
			});
		}
		
		$("#btnImpBoletoEmBranco", fechamentoCEIntegracaoController.workspace).click(function() {
			fechamentoCEIntegracaoController.geraBoleto('BOLETO_EM_BRANCO');
		});
	},
	
	
	replicarValor : function(id) {
		
		if ($("#ch" + id, fechamentoCEIntegracaoController.workspace).is(":checked")){
			
			$("#inputVenda" + id, fechamentoCEIntegracaoController.workspace).val(
				$("#qtdeDevolucao" + id, fechamentoCEIntegracaoController.workspace).text()
			);


			var encalhe = $("#inputVenda" + id, fechamentoCEIntegracaoController.workspace).val();
			
			var estoque = $("#estoque" + id, fechamentoCEIntegracaoController.workspace).html();
			
			var diferenca = $("#inputVenda" + id, fechamentoCEIntegracaoController.workspace).val();
			
			var precoCapa = $("#precoCapa" + id, fechamentoCEIntegracaoController.workspace).val();		
			
			var codigoProduto = $("#codigoProduto" + id, fechamentoCEIntegracaoController.workspace).html();
			
			var nomeProduto = $("#nomeProduto" + id, fechamentoCEIntegracaoController.workspace).html();
			
			var numeroEdicao = $("#numeroEdicao" + id, fechamentoCEIntegracaoController.workspace).html();

			var venda = encalhe;
			
			fechamentoCEIntegracaoController.atualizarItensCEIntegracaoSemCE(id, encalhe, venda, diferenca, estoque, codigoProduto, nomeProduto, numeroEdicao, precoCapa);
			
			$("#diferenca" + id, fechamentoCEIntegracaoController.workspace).text("0");
			
			
		} else {
			
			$("#diferenca" + id, fechamentoCEIntegracaoController.workspace).text("");
			
			$("#sel", fechamentoCEIntegracaoController.workspace).attr("checked",false);
			
			var valorAux = $("#inputVenda" + id, fechamentoCEIntegracaoController.workspace).val();
			
			if (valorAux || valorAux == "0"){
				$("#inputVenda" + id, fechamentoCEIntegracaoController.workspace).val(valorAux);
				
				$("#diferenca" + id, fechamentoCEIntegracaoController.workspace).text(
					parseInt($("#qtdeDevolucao" + id, fechamentoCEIntegracaoController.workspace).text()) - valorAux
				);
				
			} else {
				$("#inputVenda" + id, fechamentoCEIntegracaoController.workspace).val("");
				$("#diferenca" + id, fechamentoCEIntegracaoController.workspace).text("");
			}
		}
		
		if(this.replicar.list.indexOf(id) >= 0) {
			
			this.replicar.list.splice(this.replicar.list.indexOf(id), 1);
		}
		
		if(this.replicar.all ^ $("#ch" + id, this.workspace).is(":checked")) {
			
			this.replicar.list.push(id);
			
		}
	},
	
	
	pesquisaPrincipal : function(){
		
		var idFornecedor = $("#idFornecedor", fechamentoCEIntegracaoController.workspace).val();
		
		var semana = $("#semana", fechamentoCEIntegracaoController.workspace).val();
		
		var idChamadaEncalhe =  $("#comboCE-fechamentoCe-integracao", fechamentoCEIntegracaoController.workspace).val();
		
		if($("#combo-fechamentoCe-integracao", fechamentoCEIntegracaoController.workspace).val() == "-1") {
			exibirMensagem('WARNING', ['Favor selecionar um item na combo!']);
			return;
		} 
		
		var comboCEIntegracao = $("#combo-fechamentoCe-integracao", fechamentoCEIntegracaoController.workspace).val();
		
		if($("#combo-fechamentoCe-integracao", fechamentoCEIntegracaoController.workspace).val() == "COM") {
			$(".fechamentoCeGrid", fechamentoCEIntegracaoController.workspace).flexOptions({
				url: contextPath + '/devolucao/fechamentoCEIntegracao/pesquisaPrincipal',
				dataType : 'json',
				params: [
				         {name:'idFornecedor' , value:idFornecedor},
				         {name:'semana' , value:semana},
				         {name:'comboCEIntegracao' , value:comboCEIntegracao},
				         {name:'idChamadaEncalhe' , value:idChamadaEncalhe}
				         
				        ]		         
			});
			$($(".fechamentoSemCeGrid", fechamentoCEIntegracaoController.workspace).parent().parent()).hide();
			$($(".fechamentoCeGrid", fechamentoCEIntegracaoController.workspace).parent().parent()).show();
			$(".fechamentoCeGrid", fechamentoCEIntegracaoController.workspace).flexReload();
		} else {
			
			$(".fechamentoSemCeGrid", fechamentoCEIntegracaoController.workspace).flexOptions({
				url: contextPath + '/devolucao/fechamentoCEIntegracao/pesquisaPrincipal',
				dataType : 'json',
				params: [
				         {name:'idFornecedor' , value:idFornecedor},
				         {name:'semana' , value:semana},
				         {name:'comboCEIntegracao' , value:comboCEIntegracao},
				         {name:'idChamadaEncalhe' , value:idChamadaEncalhe}
				        ]		         
			});
			$($(".fechamentoCeGrid", fechamentoCEIntegracaoController.workspace).parent().parent()).hide();
			$($(".fechamentoSemCeGrid", fechamentoCEIntegracaoController.workspace).parent().parent()).show();
			$(".fechamentoSemCeGrid", fechamentoCEIntegracaoController.workspace).flexReload();
			
		}
		
		
	},
	
	popularTotal : function(resultado){
		$("#totalBruto", fechamentoCEIntegracaoController.workspace).html(resultado.totalBruto);
		$("#totalDesconto", fechamentoCEIntegracaoController.workspace).html(resultado.totalDesconto);
		$("#totalLiquido", fechamentoCEIntegracaoController.workspace).html(resultado.totalLiquido);
		$(".tabelaTotal", fechamentoCEIntegracaoController.workspace).show();
	},
	
	validarPerdaGanho : function(){
		
		fechamentoCEIntegracaoController.initGridPerdasGanho();
		
		var idFornecedor = $("#idFornecedor", fechamentoCEIntegracaoController.workspace).val();
		
		var semana = $("#semana", fechamentoCEIntegracaoController.workspace).val(); 
		
		var comboCEIntegracao = $("#combo-fechamentoCe-integracao", fechamentoCEIntegracaoController.workspace).val();
		
		var idChamadaEncalhe =  $("#comboCE-fechamentoCe-integracao", fechamentoCEIntegracaoController.workspace).val();
		
		var parametros = fechamentoCEIntegracaoController.getItensAlteradosCE();
		
		parametros.push({name:'semana' , value:semana});
		
		parametros.push({name:'idFornecedor' , value:idFornecedor});
		
		parametros.push({name:'comboCEIntegracao' , value:comboCEIntegracao});
		
		parametros.push({name:'idChamadaEncalhe' , value:idChamadaEncalhe});
		
		$(".perdaGanhoGrid", fechamentoCEIntegracaoController.workspace).flexOptions({
			url: contextPath + '/devolucao/fechamentoCEIntegracao/pesquisarPerdaGanho',
			dataType : 'json',
			params: parametros		         
		});
		
		$(".perdaGanhoGrid", fechamentoCEIntegracaoController.workspace).flexReload();
		
	},
	
	perdaGanhoGridPreProcess : function(resultado) {
		
		if (resultado.mensagens) {
			
			exibirMensagem(resultado.mensagens.tipoMensagem, resultado.mensagens.listaMensagens);
			
			$(".perdaGanhoGrid", fechamentoCEIntegracaoController.workspace).hide();
			$("#dialog-perdas-ganhos-Fechamento", fechamentoCEIntegracaoController.workspace).hide();
			
			return resultado;
		} else {
			
			if(resultado.rows.length > 0) {
				
				fechamentoCEIntegracaoController.popupPerdaGanho();
				
				$(".perdaGanhoGrid", fechamentoCEIntegracaoController.workspace).show();
				$("#dialog-perdas-ganhos-Fechamento", fechamentoCEIntegracaoController.workspace).show();
				
				return resultado;
			} else {
				
				$(".perdaGanhoGrid", fechamentoCEIntegracaoController.workspace).hide();
				$("#dialog-perdas-ganhos-Fechamento", fechamentoCEIntegracaoController.workspace).hide();
				fechamentoCEIntegracaoController.popupConfirmacaoFechamrento();
			}
		}
	},
	
	popupPerdaGanho: function() {
		
		$( "#dialog-perdas-ganhos-Fechamento", fechamentoCEIntegracaoController.workspace).dialog({
			resizable: false,
			height:450,
			width:820,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					
					fechamentoCEIntegracaoController.popupConfirmacaoFechamrento();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-perdas-ganhos-Fechamento", fechamentoCEIntegracaoController.workspace).parents("form")
		});	
	},
	
	popupConfirmacaoFechamrento : function() {
		
		$( "#dialog-Confirmacao-Fechamento", fechamentoCEIntegracaoController.workspace).dialog({
			resizable: false,
			height:150,
			width:300,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					
					fechamentoCEIntegracaoController.fecharCE();
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-Confirmacao-Fechamento", fechamentoCEIntegracaoController.workspace).parents("form")
		});	
	},
	
	
	popupConfirmacaoFechamrentoSemCE : function() {
		
		$( "#dialog-Confirmacao-Fechamento-SemCe", fechamentoCEIntegracaoController.workspace).dialog({
			resizable: false,
			height:150,
			width:300,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					
					fechamentoCEIntegracaoController.fecharCE();
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-Confirmacao-Fechamento-SemCe", fechamentoCEIntegracaoController.workspace).parents("form")
		});	
	},
	
	fecharCE : function(){
		
		$.postJSON(contextPath + '/devolucao/fechamentoCEIntegracao/fecharCE',
				 fechamentoCEIntegracaoController.getItensAlteradosCE(),
				 function(resultado) {
				 	exibirMensagem(resultado.tipoMensagem, resultado.listaMensagens);
					$(".grids", fechamentoCEIntegracaoController.workspace).hide();
					fechamentoCEIntegracaoController.esconderBotoes();
					fechamentoCEIntegracaoController.itensCEIntegracao = [];
					fechamentoCEIntegracaoController.buscarNumeroSemana();
					return resultado;
				 },
				null,
				true
			);
	},
	
	initGridPerdasGanho : function() {
		
		$(".perdaGanhoGrid", fechamentoCEIntegracaoController.workspace).flexigrid({			
			preProcess : fechamentoCEIntegracaoController.perdaGanhoGridPreProcess,
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
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Estoque',
				name : 'estoque',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Encalhe',
				name : 'encalhe',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Diferenca',
				name : 'diferenca',
				width : 100,
				sortable : true,
				align : 'center'
			}],
			sortname : "sequencial",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 800,
			height : 280
		});
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