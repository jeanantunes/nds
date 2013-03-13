function MatrizDistribuicao(pathTela, descInstancia, workspace) {
	
	var _workspace = workspace;
	
	var T = this;
	
	var opcoesAberto = false;
	
	this.instancia = descInstancia;
	this.linhasDestacadas = [];
	this.lancamentos = [];
	this.isCliquePesquisar;
	
	this.definirAcaoPesquisaTeclaEnter = function() {
		
		definirAcaoPesquisaTeclaEnter();
	},
	
	this.exibirMensagemSucesso = function() {
		
		exibirMensagem("SUCCESS", ["Operação realizada com sucesso!"]);
	},
	
	this.pesquisar = function() {
		
		$("#resumoPeriodo", _workspace).show();				
		
		var data = [];
		
		data.push({name:'dataLancamento', value: $("#datepickerDe", _workspace).val()});
		
//		$("input[name='checkgroup_menu']:checked", _workspace).each(function(i) {
//			data.push({name:'idsFornecedores', value: $(this).val()});
//		});
				
		$.postJSON(
			pathTela + "/matrizDistribuicao/obterMatrizDistribuicao", 
			data,
			function(result) {
				
				T.carregarGrid();
			},
			
			T.escondeGrid()
		);
	},
	
	this.escondeGrid = function() { 
		$(".grids", _workspace).hide();
	} ,

	this.carregarGrid = function() {		
		
		T.mostrarGridEBotoesAcao();
		
		T.linhasDestacadas = [];		
		lancamentosSelecionados = [];		
		$('#selTodos', _workspace).uncheck();	
		
		T.isCliquePesquisar = true;
		
		$(".lancamentosProgramadosGrid", _workspace).flexOptions({			
			url : pathTela + "/matrizDistribuicao/obterGridMatrizDistribuicao",
			dataType : 'json',
			autoload: false,
			singleSelect: true,
			preProcess: T.processaRetornoPesquisa,
			onSuccess: T.onSuccessPesquisa,
			onSubmit: function(elemento){return T.confirmarPaginacao(this);}
		});
		
		$(".lancamentosProgramadosGrid", _workspace).flexReload();
	},

	this.confirmarPaginacao = function(elemento) {
		
		var noSelect = $('[name=checkgroup]:checked', _workspace).size() == 0;
		
		if(T.isCliquePesquisar || noSelect ) {
			T.isCliquePesquisar = false;
			return true;
		}
		
		$("#dialog-pagincao-confirmada", _workspace).dialog({
			resizable: false,
			height:'auto',
			width:600,
			modal: true,
			buttons: [
			    {
			    	id: "selecaoLancamentosBtnConfirmar",
			    	text: "Confirmar",
			    	click: function() {
					
						$(".lancamentosProgramadosGrid", _workspace).flexOptions({ onSubmit: null });
						
						$(".lancamentosProgramadosGrid", _workspace).flexReload();
						
						$(".lancamentosProgramadosGrid", _workspace).flexOptions({ onSubmit: function(elemento){return T.confirmarPaginacao(this);} });
						
						$(this).dialog("close");
			    	}
			    },
			    {
			    	id: "selecaoLancamentosBtnCancelar",
			    	text: "Cancelar",
			    	click: function() {
			    
			    		$(this).dialog("close");
			    	}
				}
			],
			form: $("#dialog-pagincao-confirmada", this.workspace).parents("form")
		});	
		
		return false;
	},
	
	this.onSuccessPesquisa = function() {
		
		$(T.linhasDestacadas).each(function(i, item){
			 id = '#row' + item;			    	
			 $(id, _workspace).removeClass("erow").addClass("gridLinhaDestacada");
			 $(id, _workspace).children("td").removeClass("sorted");
		});
		
		
	},
		
	this.processaRetornoPesquisa = function(resultadoPesquisa) {
		
		if (resultadoPesquisa[3]) {
			$(".matrizFinalizada").show();
		}
		else {
			$(".matrizFinalizada").hide();
		}
		
		$("#totalGerado", _workspace).clear();
		$("#totalLiberado", _workspace).clear();
		
		T.lancamentos = [];
		
		if (resultadoPesquisa[0].rows.length == 0) {
			T.escondeGrid();
		}
		
		$("#totalGerado", _workspace).html(resultadoPesquisa[1]);
		$("#totalLiberado", _workspace).html(resultadoPesquisa[2]);
		
		$.each(resultadoPesquisa[0].rows, function(index,row){ T.processarLinha(index, row);});
		
		return resultadoPesquisa[0];
	},
	
	this.processarLinha = function(i,row) {
		
		var imgLiberado = null;
			
		if (row.cell.liberado == 'LIBERADO') {
			imgLiberado = '<img title="Liberado" src="' + contextPath + '/images/ico_check.gif" hspace="5" border="0px" />';
		}
		else {
			imgLiberado = '';
		}	
		
		row.cell.liberado = imgLiberado;
		row.cell.reprogramar = T.gerarCheckDistribuicao(row.cell.idLancamento, i);
		
		T.formataCampos(row);
		
		var repDist = (row.cell.lancto - row.cell.promo); 
		
		row.cell.sobra = '<span id="sobra'+i+'">'+repDist+'</span>';
		
		row.cell.repDistrib = T.gerarInputRepDistrib(repDist, i);
		
		T.lancamentos.push({
			idLancamento:				row.cell.idLancamento, 
			estudo:                     row.cell.idEstudo,
			lancto:                     row.cell.lancto,
			promo:                      row.cell.promo,
			repDistrib:                 repDist,
			sobra:                      repDist,
			codigoProduto:              row.cell.codigoProduto,
			edicao:                     row.cell.numeroEdicao,
			nomeProduto:                row.cell.nomeProduto,
			classificacao:              row.cell.classificacao,
			dataLancto:                 row.cell.dataLancto,
			reparte:					row.cell.reparte,
			pctPadrao:					row.cell.pctPadrao
		});
		
	},
	
	this.formataCampos = function(row) {
		
		if (row.cell.codigoProduto == null) {
			row.cell.codigoProduto = '';
		}
		
		if (row.cell.nomeProduto == null) {
			row.cell.nomeProduto = '';
		}
		
		if (row.cell.numeroEdicao == null) {
			row.cell.numeroEdicao = '';
		}
		
		if (row.cell.periodo == null) {
			row.cell.periodo = '';
		}
		
		if (row.cell.juram == null) {
			row.cell.juram = 0;
		}
		
		if (row.cell.suplem == null) {
			row.cell.suplem = 0;
		}
		
		if (row.cell.lancto == null) {
			row.cell.lancto = 0;
		}
		
		if (row.cell.promo == null) {
			row.cell.promo = 0;
		}
		
		if (row.cell.precoVenda == null) {
			row.cell.precoVenda = '';
		}
		
		if (row.cell.classificacao == null) {
			row.cell.classificacao = '';
		}
		
		if (row.cell.pctPadrao == null) {
			row.cell.pctPadrao = '';
		}
		
		if (row.cell.nomeFornecedor == null) {
			row.cell.nomeFornecedor = '';
		}
		
		if (row.cell.liberado == null) {
			row.cell.liberado = '';
		}
		
		if (row.cell.idEstudo == null) {
			row.cell.idEstudo = '';
		}
		
		row.cell.sobra = 0;
		
	},
	
	this.gerarInputRepDistrib = function(valor, index) {
		
		return '<input id="inputRepDistrib' + index + '" onchange="' + T.instancia + '.alterarReparte(this,\'' + index + '\');" type="text" name="repDistrib" style="width:60px; float:left;" value="' + valor + '" />'; 
		
	},
	

	this.obeterQuantosItensMarcados = function() {
		return $('[name=checkgroup]:checked', _workspace).size();
	},
	
	this.alterarReparte = function(input, index) {
		
		if (!$.isNumeric(input.value)) {
			
			exibirMensagem("WARNING", ["Digite um número valido!"]);
			return;
		}
		
		T.lancamentos[index].repDistrib = input.value;
		var vlr = (T.lancamentos[index].lancto - T.lancamentos[index].promo - T.lancamentos[index].repDistrib); 
		$("#sobra" + index, _workspace).text(vlr);
		T.lancamentos[index].sobra = vlr;
	},
	
	this.gerarCheckDistribuicao = function(id, index) { 
		return '<input id="checkDistribuicao' + index + '" type="checkbox" value="'+id+'" name="checkgroup" ' +
			   ' onclick="' + T.instancia + '.selecionarCheck(this,\'' + index + '\');" />';
	},
	
	this.selecionarCheck = function(check, index) {
		
		T.lancamentos[index].selecionado = check.checked;
		$("#selTodos", _workspace).uncheck();
	},
	
	
  this.obterUnicoItemMarcado = function() {
		
		var selecionado = null;
		
		if (T.validarMarcacaoUnicoItem()) {
			$.each(T.lancamentos, function(index, lancamento){
				if(lancamento.selecionado) {
					selecionado = lancamento;
				}
			});
		}
		
	 return selecionado;	
  },
  
  this.confirmarFinalizacaoDeMatriz = function() {
		
		var data = [];
		
		$.each(T.lancamentos, function(index, lancamento){
			data.push({name: 'produtosDistribuicao[' + index + '].idEstudo',  	  value: lancamento.estudo});
			data.push({name: 'produtosDistribuicao[' + index + '].repDistrib',    value: lancamento.repDistrib});
		});

		$.postJSON(pathTela + "/matrizDistribuicao/finalizarMatrizDistribuicao", data,
				function(result){
					T.checkUncheckLancamentos(false);
					T.carregarGrid();
					T.exibirMensagemSucesso();
				}
			);
  },
	
  this.confirmarReaberturaDeMatriz = function() {
		
		$.postJSON(pathTela + "/matrizDistribuicao/reabrirMatrizDistribuicao", null, 
				function(){
					T.checkUncheckLancamentos(false);
					T.carregarGrid();
					T.exibirMensagemSucesso();
				}
			);
  },
  
  this.confirmarExclusaoEstudos = function() {
		
		var data = [];
		
		$.each(T.lancamentos, function(index, lancamento){
			if(lancamento.selecionado) {
				data.push({name: 'produtosDistribuicao[' + index + '].idEstudo',  value: lancamento.estudo});
				data.push({name: 'produtosDistribuicao[' + index + '].idLancamento',  	  value: lancamento.idLancamento});
				data.push({name: 'produtosDistribuicao[' + index + '].numeroEdicao',  	  value: lancamento.edicao});
				data.push({name: 'produtosDistribuicao[' + index + '].codigoProduto',  	  value: lancamento.codigoProduto});
			}
		});
		
		$.postJSON(pathTela + "/matrizDistribuicao/excluirEstudosSelecionados", data,
				function(){
					T.checkUncheckLancamentos(false);
					T.atualizarGrid();
					T.exibirMensagemSucesso();
				}
			);
	},
	
	this.confirmarReabrirEstudo = function() {
		
		var data = [];
		
		$.each(T.lancamentos, function(index, lancamento){
			if(lancamento.selecionado && lancamento.estudo != null) {
				data.push({name: 'produtosDistribuicao[' + index + '].idEstudo',  value: lancamento.estudo});
			}
		});
		
		$.postJSON(pathTela + "/matrizDistribuicao/reabrirEstudosSelecionados", data,
				function(){
					T.checkUncheckLancamentos(false);
					T.atualizarGrid();
					T.exibirMensagemSucesso();
				}
			);
				
	},
	
	this.popup_confirmar_duplicarLinha = function() {
		
		$( "#dialog-confirm-duplicar", _workspace ).dialog({
			resizable: false,
			height:'auto',
			width:300,
			modal: true,
			buttons: [
			    {
			    	id: "dialogConfirmarBtnConfirmar",
			    	text: "Confirmar",
			    	click: function() {
			    		T.confirmaDuplicaoLinha(); 
			    		$(this).dialog("close");
			    	}
			    },
			    {
			    	id: "dialogConfirmarBtnCancelar",
			    	text: "Cancelar",
			    	click: function() {
			    
			    		$(this).dialog("close");
			    	}
				}
			],
			form: $("#dialog-confirm-duplicar", this.workspace).parents("form"),
			beforeClose: function() {
				clearMessageDialogTimeout("dialog-confirmar");
		    }
		});
	},
	
	this.popup_confirmar_finalizacao_matriz = function() {
		
			$( "#dialog-confirm-finalizacao", _workspace ).dialog({
				resizable: false,
				height:'auto',
				width:300,
				modal: true,
				buttons: [
				    {
				    	id: "dialogConfirmarBtnConfirmar",
				    	text: "Confirmar",
				    	click: function() {
				    		T.confirmarFinalizacaoDeMatriz();
				    		$(this).dialog("close");
				    	}
				    },
				    {
				    	id: "dialogConfirmarBtnCancelar",
				    	text: "Cancelar",
				    	click: function() {
				    
				    		$(this).dialog("close");
				    	}
					}
				],
				form: $("#dialog-confirm-finalizacao", this.workspace).parents("form"),
				beforeClose: function() {
					clearMessageDialogTimeout("dialog-confirmar");
			    }
			});
		},
		
		this.popup_confirmar_reabertura_matriz = function() {
					
					$( "#dialog-confirm-reabrir-matriz", _workspace ).dialog({
						resizable: false,
						height:'auto',
						width:300,
						modal: true,
						buttons: [
						    {
						    	id: "dialogConfirmarBtnConfirmar",
						    	text: "Confirmar",
						    	click: function() {
						    		T.confirmarReaberturaDeMatriz();
						    		$(this).dialog("close");
						    	}
						    },
						    {
						    	id: "dialogConfirmarBtnCancelar",
						    	text: "Cancelar",
						    	click: function() {
						    
						    		$(this).dialog("close");
						    	}
							}
						],
						form: $("#dialog-confirm-reabrir-matriz", this.workspace).parents("form"),
						beforeClose: function() {
							clearMessageDialogTimeout("dialog-confirmar");
					    }
					});
				},
	
	/**
	 * Exibe popup de confirmação de exclusão de estudo
	 */
	this.popup_confirmar_exclusao_estudo = function() {
		
		$( "#dialog-confirm-exclusao", _workspace ).dialog({
			resizable: false,
			height:'auto',
			width:300,
			modal: true,
			buttons: [
			    {
			    	id: "dialogConfirmarBtnConfirmar",
			    	text: "Confirmar",
			    	click: function() {
			    		T.confirmarExclusaoEstudos();
			    		$(this).dialog("close");
			    	}
			    },
			    {
			    	id: "dialogConfirmarBtnCancelar",
			    	text: "Cancelar",
			    	click: function() {
			    
			    		$(this).dialog("close");
			    	}
				}
			],
			form: $("#dialog-confirm-exclusao", this.workspace).parents("form"),
			beforeClose: function() {
				clearMessageDialogTimeout("dialog-confirmar");
		    }
		});
	},
	
	/**
	 * Exibe popup de confirmação de exclusão de estudo
	 */
	this.popup_confirmar_reabertura_estudo = function() {
		
		$( "#dialog-confirm-reabert", _workspace ).dialog({
			resizable: false,
			height:'auto',
			width:300,
			modal: true,
			buttons: [
			    {
			    	id: "dialogConfirmarBtnConfirmar",
			    	text: "Confirmar",
			    	click: function() {
			    		T.confirmarReabrirEstudo();
			    		$(this).dialog("close");
			    	}
			    },
			    {
			    	id: "dialogConfirmarBtnCancelar",
			    	text: "Cancelar",
			    	click: function() {
			    
			    		$(this).dialog("close");
			    	}
				}
			],
			form: $("#dialog-confirm-reabert", this.workspace).parents("form"),
			beforeClose: function() {
				clearMessageDialogTimeout("dialog-confirmar");
		    }
		});
	},
	
	this.atualizarResumoBalanceamento = function () {
		
		$.postJSON( pathTela + "/matrizDistribuicao/atualizarResumoBalanceamento",
				   null,
				   function(result) {
						T.popularResumoPeriodo(result);
						
						T.atualizarGrid();
				   }
		);
	},
	
	this.validarMarcacaoUnicoItem = function() {
		
		if (T.obeterQuantosItensMarcados() == 0) {
			exibirMensagem("WARNING", ["Selecione um item."]);
			return false;
		}
		
		if (T.obeterQuantosItensMarcados() > 1) {
			exibirMensagem("WARNING",["Apenas um item pode ser marcado."]);
			return false;
		}
		
		return true;
	},
	
	this.obterUnicoIndiceSelecionado = function() {
		
		var i = null;
		
		if (T.validarMarcacaoUnicoItem()) {
			$.each(T.lancamentos, function(index, lancamento){
				if(lancamento.selecionado) {
					i = index;
				}
			});
		}
		
		return i;
	},
	
//	this.duplicarLinha = function() {
//		
//		if(!T.validarMarcacaoUnicoItem()) {
//			return;
//		}
//		
//		var index = T.obterUnicoIndiceSelecionado() + 1;
//		var idTR = 'row'+index;
//		var idCloneTR = 'row'+index+'clone';
//		var cloneCheckBox =  'checkDistribuicao' + (index - 1);
//		var checkBox =  'checkDistribuicao' + (index - 1);
//		
//		
//		if ($('#'+idCloneTR + '2') > 0) {
//			return;
//		}
//		
//		if ($('#'+idCloneTR + '1') > 0) {
//			idCloneTR = idCloneTR + '1';
//		}
//		
//		$('#'+idTR).clone().insertAfter('#'+idTR).attr('id',idCloneTR);
//		$($('#'+idCloneTR).find('td')[14]).find('div').text('');
//		$($('#'+idCloneTR).find('td')[15]).find('div').text('');
//		$($('#'+idCloneTR).find('td')[16]).find('input').attr('id',cloneCheckBox);
//		$('#'+checkBox).uncheck();
//		
//	},
	
	this.confirmaDuplicaoLinha = function() {
		
		if(!T.validarMarcacaoUnicoItem()) {
			return;
		}
		
		var selecionado = T.obterUnicoItemMarcado();
		
		var data = [];
		
		data.push({name: 'produtoDistribuicao.idLancamento',  	  value: selecionado.idLancamento});
		data.push({name: 'produtoDistribuicao.numeroEdicao',  	  value: selecionado.edicao});
		data.push({name: 'produtoDistribuicao.codigoProduto',  	  value: selecionado.codigoProduto});
		
		
		$.postJSON(pathTela + "/matrizDistribuicao/duplicarLinha", data,
				function(result){
					T.checkUncheckLancamentos(false);
					T.carregarGrid();
					T.exibirMensagemSucesso();
		}
			);

		
	},
	
	this.mostraTelaMatrizDistribuicao = function() {
		$("#telaPesquisaMatriz", _workspace ).show();
		$("#dialog-copiar-estudo", _workspace ).hide();
	},
	
	this.mostraTelaCopiarProporcionalDeEstudo = function() {
		T.mostrarOpcoes();
		$("#telaPesquisaMatriz", _workspace ).hide();
		$("#dialog-copiar-estudo", _workspace ).show();
		T.inicializarTelaCopiaProporcional();
	},
	
	this.inicializarTelaCopiaProporcional = function() {
		$('#copiarEstudo-estudo').text('');
		$('#copiarEstudo-reparteDistribuido').text('');
		$('#copiarEstudo-idLancamento').text('');
		$('#copiarEstudo-idLancamento').text('copiarEstudo-reparte');
		T.cancelarCopiaProporcionalDeEstudo();
	},
	
	this.analise= function(){
		//testa se registro foi selecionado
		if (T.validarMarcacaoUnicoItem()) {
			$.each(T.lancamentos, function(index, lancamento){
				if(lancamento.selecionado) {
					selecionado = lancamento;
				}
			});
			
			//testa se registro selecionado possui estudo gerado
			if (selecionado.estudo == null || selecionado.estudo == "") {
				exibirMensagem("WARNING",["Selecione um produto que tenha um estudo gerado."]);
				return;
			}else{
				$.get(
					pathTela + '/matrizDistribuicao/histogramaPosEstudo', //url
					null, // parametros
					function(html){ // onSucessCallBack
						$('#matrizDistribuicaoContent').hide();
						$('#histogramaPosEstudoContent').html(html);
						$('#histogramaPosEstudoContent').show();
				
						histogramaPosEstudoController.carregarGridHistogramaPosEstudo(selecionado);
					
				});
			}
			
		}else{
			exibirMensagem("WARNING", ["Selecione um item."]);
			return false;
		}
		
	},

	this.copiarProporcionalDeEstudo = function() {
		
		T.esconderOpcoes();
		
		if (!T.validarMarcacaoUnicoItem()) {
			return;
		}
		
		$.each(T.lancamentos, function(index, lancamento){
			if(lancamento.selecionado) {
				selecionado = lancamento;
			}
		});
		
		if (selecionado.estudo != null && selecionado.estudo != "") {
			exibirMensagem("WARNING",["Selecione um produto que não tenha um estudo gerado."]);
			return;
		}
		
		T.mostraTelaCopiarProporcionalDeEstudo();
		
		T.populaEdicaoSelecionada(selecionado);
	},
	
	this.populaEdicaoSelecionada = function(selecionado) {
		
		$("#copiarEstudo-codigoProduto").text(selecionado.codigoProduto);
		$("#copiarEstudo-edicao").text(selecionado.edicao);
		$("#copiarEstudo-nomeProduto").text(selecionado.nomeProduto);
		$("#copiarEstudo-classificacao").text(selecionado.classificacao);
		$("#copiarEstudo-dataLancto").text(selecionado.dataLancto);
		$("#copiarEstudo-reparte").text(selecionado.reparte);
		$("#copiarEstudo-reparteDistribuido").text(selecionado.repDistrib);
		$("#copiarEstudo-pctPadrao").val(selecionado.pctPadrao);
		$("#copiarEstudo-idLancamento").text(selecionado.idLancamento);
		
	},
	
	this.atualizarGrid = function() {		
		
		T.mostrarGridEBotoesAcao();
		
		T.linhasDestacadas = [];		
		lancamentosSelecionados = [];		
		$('#selTodos', _workspace).uncheck();	
		
		T.isCliquePesquisar = true;
		
		$(".lancamentosProgramadosGrid", _workspace).flexOptions({			

		
		},
	
	this.mostraTelaMatrizDistribuicao = function() {
		$("#telaPesquisaMatriz", _workspace ).show();
		$("#dialog-copiar-estudo", _workspace ).hide();
	},
	
	this.mostraTelaCopiarProporcionalDeEstudo = function() {
		T.mostrarOpcoes();
		$("#telaPesquisaMatriz", _workspace ).hide();
		$("#dialog-copiar-estudo", _workspace ).show();
		T.inicializarTelaCopiaProporcional();
	},
	
	this.inicializarTelaCopiaProporcional = function() {
		$('#copiarEstudo-estudo').text('');
		$('#copiarEstudo-reparteDistribuido').text('');
		$('#copiarEstudo-idLancamento').text('');
		$('#copiarEstudo-idLancamento').text('copiarEstudo-reparte');
		T.cancelarCopiaProporcionalDeEstudo();
	},

	this.copiarProporcionalDeEstudo = function() {
		
		T.esconderOpcoes();
		
		if (!T.validarMarcacaoUnicoItem()) {
			return;
		}
		
		$.each(T.lancamentos, function(index, lancamento){
			if(lancamento.selecionado) {
				selecionado = lancamento;
			}
		});
		
		if (selecionado.estudo != null && selecionado.estudo != "") {
			exibirMensagem("WARNING",["Selecione um produto que não tenha um estudo gerado."]);
			return;
		}
		
		T.mostraTelaCopiarProporcionalDeEstudo();
		
		T.populaEdicaoSelecionada(selecionado);
	},
	
	this.populaEdicaoSelecionada = function(selecionado) {
		
		$("#copiarEstudo-codigoProduto").text(selecionado.codigoProduto);
		$("#copiarEstudo-edicao").text(selecionado.edicao);
		$("#copiarEstudo-nomeProduto").text(selecionado.nomeProduto);
		$("#copiarEstudo-classificacao").text(selecionado.classificacao);
		$("#copiarEstudo-dataLancto").text(selecionado.dataLancto);
		$("#copiarEstudo-reparte").text(selecionado.reparte);
		$("#copiarEstudo-reparteDistribuido").text(selecionado.repDistrib);
		$("#copiarEstudo-pctPadrao").val(selecionado.pctPadrao);
		$("#copiarEstudo-idLancamento").text(selecionado.idLancamento);
		
	},
	
	this.atualizarGrid = function() {		
		
		T.mostrarGridEBotoesAcao();
		
		T.linhasDestacadas = [];		
		lancamentosSelecionados = [];		
		$('#selTodos', _workspace).uncheck();	
		
		T.isCliquePesquisar = true;
		
		$(".lancamentosProgramadosGrid", _workspace).flexOptions({			

			url : pathTela + "/matrizDistribuicao/obterGridMatrizDistribuicao",
			dataType : 'json',
			autoload: false,
			singleSelect: true,
			preProcess: T.processaRetornoPesquisa,
			onSuccess: T.onSuccessPesquisa,
			onSubmit: T.confirmarPaginacao
		});
		
		$(".lancamentosProgramadosGrid", _workspace).flexReload();
	},
	
	this.mostrarGridEBotoesAcao = function () {
		
		$(".grids", _workspace).show();
		
	},
	
	this.checkUncheckLancamentos = function(checked) {
				
		var todos = $('#selTodos', _workspace);
		
		if(checked) 
			todos.check(checked);
		
		$.each(T.lancamentos, function(index, row){
			row.selecionado = (todos.attr("checked") == 'checked');
		}),
		
		checkAll(document.getElementById('selTodos'),"checkgroup");
		
	},
	
	this.carregarProdutoPorEstudo = function() {
		
		var data = [];
		
		var codEstudo = $("#copiarEstudo-estudoPesquisa").val();
		
		data.push({name: 'estudo',  value: codEstudo});
		
		$.postJSON(pathTela + "/matrizDistribuicao/carregarProdutoEdicaoPorEstudo", data,
			function(result) {
			   
				$("#copiarEstudo-copia-codigoProduto").text(result.codigoProduto);
				$("#copiarEstudo-copia-edicao").text(result.numeroEdicao);
				$("#copiarEstudo-copia-nomeProduto").text(result.nomeProduto);
				$("#copiarEstudo-copia-classificacao").text(result.classificacao);
				$("#copiarEstudo-copia-dataLancto").text(result.dataLancto);
				$("#copiarEstudo-copia-reparte").text(result.reparte);
		  }
		);
	},
	
	this.cancelarCopiaProporcionalDeEstudo = function() {
		
		if ($('#copiarEstudo-estudo').text() == "") {
			
			$("#copiarEstudo-estudoPesquisa").val("");
			$("#copiarEstudo-copia-codigoProduto").text("");
			$("#copiarEstudo-copia-edicao").text("");
			$("#copiarEstudo-copia-nomeProduto").text("");
			$("#copiarEstudo-copia-classificacao").text("");
			$("#copiarEstudo-copia-dataLancto").text("");
			$("#copiarEstudo-copia-reparte").text("");
			$('#copiarEstudo-multiplos-check').uncheck();
			$('.porMultiplos').toggle(false);
			$("#copiarEstudo-estudoPesquisa").removeAttr("disabled");
		}
		else {
			exibirMensagem("WARNING", ["O estudo já foi gerado."]);
		}
	},
	
	this.confirmarCopiarProporcionalDeEstudo = function() {
		
		if ($('#copiarEstudo-estudo').text() != '') {
			exibirMensagem("WARNING", ["O estudo já foi gerado para este produto."]);
			return;
		}
		
		var estudo = $("#copiarEstudo-estudoPesquisa").val();
		
		if (estudo == null || estudo == "") {
			exibirMensagem("WARNING", ["Pesquise um estudo para a copia!"]);
			return;
		}
		
		var fixacao = $('#copiarEstudo-fixacao').attr("checked") == 'checked';
		var pctPadrao = ($('#copiarEstudo-multiplos-check').attr("checked") == 'checked')?$('#copiarEstudo-pctPadrao').val():null;
		var reparteDistribuido = $("#copiarEstudo-reparteDistribuido").text();
		var idLancamento = $("#copiarEstudo-idLancamento").text();
		
		var data = [];
		
		data.push({name: 'copiaProporcionalDeDistribuicaoVO.idLancamento', 	     value: idLancamento});
		data.push({name: 'copiaProporcionalDeDistribuicaoVO.idEstudo', 			 value: estudo});
		data.push({name: 'copiaProporcionalDeDistribuicaoVO.fixacao', 			 value: fixacao});
		data.push({name: 'copiaProporcionalDeDistribuicaoVO.reparteDistribuido', value: reparteDistribuido});
		
		if (pctPadrao != null) {
			data.push({name: 'copiaProporcionalDeDistribuicaoVO.pacotePadrao', value: pctPadrao});
		}
		
		$.postJSON(pathTela + "/matrizDistribuicao/confirmarCopiarProporcionalDeEstudo", data,
				function(result){
					T.exibirMensagemSucesso();
					$("#copiarEstudo-estudo").text(result.long);
					$("#copiarEstudo-estudoPesquisa").attr('disabled','true');
					$("#copiarEstudo-reparteDistribuido").text("");
					T.atualizarGrid();
				}
			);
	},
	
	this.inicializar = function() {
		
		T.mostraTelaMatrizDistribuicao();
		
		T.definirAcaoPesquisaTeclaEnter();
		
		$("#lancamentosProgramadosGrid", _workspace).flexigrid({
			colModel : [  {
				display : 'Código',
				name : 'codigoProduto',
				width : 45,
				sortable : true,
				align : 'center'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 130,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 30,
				sortable : true,
				align : 'left'
			}, {
				display : 'Periodo',
				name : 'periodo',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Clas.',
				name : 'classificacao',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Preço RS',
				name : 'precoVenda',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Pct. Padrão',
				name : 'pctPadrao',
				width : 55,
				sortable : true,
				align : 'center'
			},{
				display : 'Fornecedor',
				name : 'nomeFornecedor',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Juram.',
				name : 'juram',
				width : 40,
				sortable : true,
				align : 'center'
			}, {
				display : 'Suplem.',
				name : 'suplem',
				width : 40,
				sortable : true,
				align : 'center'
			}, {
				display : 'Lancto.',
				name : 'lancto',
				width : 40,
				sortable : true,
				align : 'center'
			}, {
				display : 'Promo.',
				name : 'promo',
				width : 40,
				sortable : true,
				align : 'center'
			}, {
				display : 'Sobra',
				name : 'sobra',
				width : 30,
				sortable : false,
				align : 'center'
			}, {
				display : 'Rep. Distrib.',
				name : 'repDistrib',
				width : 80,
				sortable : false,
				align : 'center'
			}, {
				display : 'Lib.',
				name : 'liberado',
				width : 30,
				sortable : false,
				align : 'center'
			}, {
				display : 'Estudo.',
				name : 'idEstudo',
				width : 40,
				sortable : false,
				align : 'center'
			}, {
				display : '',
				name : 'reprogramar',
				width : 30,
				sortable : false,
				align : 'center'
			 }
			],
			sortname : "codigoProduto",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180,
			disableSelect : true
			});

		$( "#datepickerDe", _workspace ).datepicker({
			showOn: "button",
			dateFormat: 'dd/mm/yy',
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#datepickerDe_1", _workspace ).datepicker({
			showOn: "button",
			dateFormat: 'dd/mm/yy',
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$("#datepickerDe", _workspace).mask("99/99/9999");
		$("#datepickerDe_1", _workspace).mask("99/99/9999");
		
		
		$( "#novaDataLancamento", _workspace ).datepicker({
			showOn: "button",
			dateFormat: 'dd/mm/yy',
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$("#novaDataLancamento", _workspace).mask("99/99/9999");
		
	},

	this.mostrarOpcoes = function() {
		opcoesAberto = !opcoesAberto;
		$( '.opcoesEstudos' ).toggle(opcoesAberto);
		$('.setaMuda').attr('src',(opcoesAberto)? contextPath + '/images/p7PM_dark_south_1.gif': contextPath + '/images/p7PM_dark_south.gif');
	},
	
	this.esconderOpcoes = function() {
		setTimeout( function(){
            $( '.opcoesEstudos' ).hide();
            $('.setaMuda').attr('src', contextPath + '/images/p7PM_dark_south.gif');
         }, 2000);
	});
	

	};
};

//@ sourceURL=matrizDistribuicao.js
