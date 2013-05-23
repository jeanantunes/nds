
function MatrizDistribuicao(pathTela, descInstancia, workspace) {
	
	var _workspace = workspace;
	
	var T = this;
	
	var opcoesAberto = false;
	
	this.instancia = descInstancia;
	this.lancamentos = [];
	this.isCliquePesquisar;
	this.parametrosDePesquisa = null;
	
	
	this.definirAcaoPesquisaTeclaEnter = function() {
		definirAcaoPesquisaTeclaEnter();
	},
	
	this.exibirMensagemSucesso = function() {
		exibirMensagem("SUCCESS", ["Operação realizada com sucesso!"]);
	},
	
	this.pesquisar = function(filtros) {
		
		var data = [];
		
		if (filtros == null) {
		
			data.push({name:'dataLancamento', value: $("#datepickerDe", _workspace).val()});
			
			$('[id^="fornecedor_"]').each(function(key){
				if (this.checked) {
					data.push({name:'idsFornecedores['+key+']', value: this.value});
				}
			});
			
			$("input[name='checkgroup_menu']:checked", _workspace).each(function(i) {
				data.push({name:'idsFornecedores', value: $(this).val()});
			});
			
			T.parametrosDePesquisa = data;
		
		} 
		else {
			data = filtros;
		}
		
		$.postJSON(
			pathTela + "/matrizDistribuicao/obterMatrizDistribuicao", 
			data,
			function(result) {
			    	if(result.parametrosDistribuidorVO.geracaoAutomaticaEstudo) {
			    	    $('#spanGerarEstudoAutomatico').attr('class', 'bt_novos');
			    	    $('#linkGerarEstudoAutomatico').attr('onclick', 'matrizDistribuicao.gerarEstudoAutomatico();');
			    	} else {
			    	    $('#spanGerarEstudoAutomatico').attr('class', 'linkDisabled');
			    	    $('#linkGerarEstudoAutomatico').attr('onclick', '');
			    	}
			    	
				T.carregarGrid();
			},
			T.escondeGrid()
		);
	},
	
	this.escondeGrid = function() { 
		$("#gridMatrizDistribuicao", _workspace).hide();
	},

	this.carregarGrid = function() {		
		
		T.mostrarGridEBotoesAcao();
		
		lancamentosSelecionados = [];		
		$('#selTodos', _workspace).uncheck();	
		
		T.isCliquePesquisar = true;
		
		$("#lancamentoMatrizDistribuicaoGrid", _workspace).flexOptions({			
			url : pathTela + "/matrizDistribuicao/obterGridMatrizDistribuicao",
			dataType : 'json',
			autoload: false,
			singleSelect: true,
			preProcess: T.processaRetornoPesquisa,
			onSuccess: T.onSuccessPesquisa,
			onSubmit: function(elemento){return T.confirmarPaginacao(this);}
		});
		
		$("#lancamentoMatrizDistribuicaoGrid", _workspace).flexReload();
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
			buttons: [ {
			    	id: "selecaoLancamentosBtnConfirmar",
			    	text: "Confirmar",
			    	click: function() {
						$("#lancamentoMatrizDistribuicaoGrid", _workspace).flexOptions({ onSubmit: null });
						
						$("#lancamentoMatrizDistribuicaoGrid", _workspace).flexReload();
						
					$("#lancamentoMatrizDistribuicaoGrid", _workspace)
					.flexOptions({ 
						onSubmit: function(elemento) { 
							return T.confirmarPaginacao(this); 
						}
					});
						
						$(this).dialog("close");
			    	}
			    },
			    {
			    	id: "selecaoLancamentosBtnCancelar",
			    	text: "Cancelar",
			    	click: function() {
			    		$(this).dialog("close");
			    	}
			} ],
			form: $("#dialog-pagincao-confirmada", this.workspace).parents("form")
		});	
		
		return false;
	},
	
	this.onSuccessPesquisa = function() {
		
		$(T.lancamentos).each(function(i,lancamento){
			 var index = i + 1;
			 var id = '#row' + index;
			 
			 $(id).removeClass("erow");
			 
			 if (T.lancamentos[i].idCopia != null) {
				 
				 for (var j=0; j < 12; j++) {
					 $($(id).children()[j]).html("");
				 }
			 }
			 
			 if (T.lancamentos[i].idRow % 2 == 0) {
				 $(id, _workspace).addClass("gridLinha");
			 }
			 
			 $("#inputRepDistrib" + i).removeAttr('disabled');
				if (T.lancamentos[i].dataFinMatDistrib != undefined) {
					T.finalizaItem(i);
			 }				 
		});
		
//		var linhasOrigemCopia = [];
//		var indexOrgCopia = 0;
//		
//		for (var i=0; i < T.lancamentos.length - 1; i++) {
//			
//			var index = i + 1;
//			
//			if (T.lancamentos[i].idCopia == null && (T.lancamentos[i].idLancamento == T.lancamentos[index].idLancamento)) {
//				 
//				linhasOrigemCopia[indexOrgCopia++] = T.lancamentos[i].idLancamento;
//			 }
//			
//			$("#inputRepDistrib" + i).removeAttr('disabled');
//			if (T.lancamentos[i].dataFinMatDistrib != undefined) {
//				 
//				T.finalizaItem(i);
//			}
//			 
//		}
//		
//		for (var j=0; j < linhasOrigemCopia.length; j++) {
//			
//			for (var i=0; i < T.lancamentos.length; i++) {
//		  
//				if (T.lancamentos[i].idCopia != null && (T.lancamentos[i].idLancamento == linhasOrigemCopia[j])) {
//					
//					var css = $('#row'+(j + 1)).attr('class');
//					
//					if (css == null || css == undefined || css == "") {
//						$('#row'+(i + 1)).removeClass("gridLinha");
//					 }
//					 else {
//						 $('#row'+(i + 1)).addClass("gridLinha");
//					 }
//					
//					for (var c=0; c < 12; c++) {
//						$($('#row'+(i + 1)).children()[c]).html("");
//					 }
//					
//					break;
//				}
//			}
//		}
		
//		$(T.lancamentos).each(function(i,lancamento){
//			 var index = i + 1;
//			 var id = '#row' + index;	
//			 $(id, _workspace).removeClass("erow");
//			 
//			 var codigo = T.lancamentos[i].codigoProduto;
//			 var edicao = T.lancamentos[i].edicao;
//			 var css = $(id, _workspace).attr('class');
//			 var trTochange = "#row" + (index + 1);
//			 
//			 if (index < T.lancamentos.length) {
//				 
//				 if (codigo == T.lancamentos[index].codigoProduto &&  edicao == T.lancamentos[index].edicao) {
//					 
//					 for (var i=0; i < 12; i++) {
//						 $($(trTochange).children()[i]).html("");
//					 }
//					 
//					 if (css == undefined || css == "") {
//					 	 $(trTochange, _workspace).removeClass("gridLinha");
//					 }
//					 else {
//						 $(trTochange, _workspace).addClass("gridLinha");
//					 }
//					 
//					 css = $(trTochange, _workspace).attr('class');
//					 
//					 trTochange = "#row" + (index + 2);
//						 
//					 if (css == undefined || css == "") {
//						 $(trTochange, _workspace).addClass("gridLinha");
//					 }
//					 else {
//						$(trTochange, _workspace).removeClass("gridLinha");
//					 }
//					 
//				 }
//				 
//			 }
//			 
//			 $("#inputRepDistrib" + i, _workspace).removeAttr('disabled');
//			 if (lancamento.dataFinMatDistrib != undefined) {
//				 
//				 T.finalizaItem(i);
//			 }
//			 
//		});
		
	},
		
	this.processaRetornoPesquisa = function(resultadoPesquisa) {
		
		if (resultadoPesquisa[3]) {
			$("#matrizFinalizada").show();
		}
		else {
			$("#matrizFinalizada").hide();
		}
		
		$("#totalGerado", _workspace).clear();
		$("#totalLiberado", _workspace).clear();
		
		T.lancamentos = [];
		
		if (typeof resultadoPesquisa[0] == 'undefined' || resultadoPesquisa[0].rows.length == 0) {
			T.escondeGrid();
		} else {
			$("#totalGerado", _workspace).html(resultadoPesquisa[1]);
			$("#totalLiberado", _workspace).html(resultadoPesquisa[2]);
			
			$.each(resultadoPesquisa[0].rows, function(index,row){ T.processarLinha(index, row);});
		}
		
		return resultadoPesquisa[0];
	},
	
	this.processarLinha = function(i,row) {
		
		var imgLiberado = null;
		
		var liberado = (row.cell.liberado == 'LIBERADO');
		
		if (liberado) {
			imgLiberado = '<img title="Liberado" src="' + contextPath + '/images/ico_check.gif" hspace="5" border="0px" />';
		}
		else {
			imgLiberado = '';
		}	
		
		row.cell.liberado = imgLiberado;
		row.cell.reprogramar = T.gerarCheckDistribuicao(row.cell.idLancamento, i);
		
		T.formataCampos(row);
		
		var repDist = (row.cell.repDistrib != null && row.cell.repDistrib > 0)? row.cell.repDistrib : (row.cell.reparte - row.cell.promo); 
		
		row.cell.sobra = (row.cell.idEstudo == null || row.cell.idEstudo == "")?'<span id="sobra'+i+'">0</span>':
			'<span id="sobra'+i+'">'+(row.cell.reparte - row.cell.promo - row.cell.repDistrib)+'</span>';
		row.cell.repDistrib = T.gerarInputRepDistrib(repDist, i);
		row.cell.reparte = parseInt(row.cell.reparte, 10);
		row.cell.promo = parseInt(row.cell.promo, 10);
		row.cell.juram = parseInt(row.cell.juram, 10);
		row.cell.suplem = parseInt(row.cell.suplem, 10);
		
		T.lancamentos.push({
					idRow 		 : row.cell.idRow, 
					idLancamento : row.cell.idLancamento,
					estudo : row.cell.idEstudo,
					lancto : row.cell.reparte,
					promo : row.cell.promo,
					suplem : row.cell.suplem,
					juram : row.cell.juram,
					repDistrib : repDist,
					sobra : repDist,
					codigoProduto : row.cell.codigoProduto,
					idProdutoEdicao : row.cell.idProdutoEdicao,
					edicao : row.cell.numeroEdicao,
					nomeProduto : row.cell.nomeProduto,
					classificacao : row.cell.classificacao,
					dataLancto : row.cell.dataLancto,
					reparte : row.cell.reparte,
					pctPadrao : row.cell.pctPadrao,
					liberado : liberado,
					dataFinMatDistrib : row.cell.dataFinMatDistrib,
					idCopia : row.cell.idCopia
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
		
		if (row.cell.reparte == null) {
			row.cell.reparte = 0;
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
		return $('input[name=checkgroup]:checked', _workspace).size();
	},
	
	this.alterarReparte = function(input, index) {
		
		if (!$.isNumeric(input.value)) {
			
			exibirMensagem("WARNING", ["Digite um número valido!"]);
			return;
		}
		
		T.lancamentos[index].repDistrib = input.value;
		var vlr = (T.lancamentos[index].reparte - T.lancamentos[index].promo - T.lancamentos[index].repDistrib); 
		$("#sobra" + index, _workspace).text(vlr);
		T.lancamentos[index].sobra = vlr;
	},
	
	this.gerarCheckDistribuicao = function(id, index) { 
		return '<input id="checkDistribuicao' + index + '" type="checkbox" value="'+id+'" name="checkgroup" ' +
			   ' onclick="' + T.instancia + '.selecionarCheck(this,\'' + index + '\');" />';
	},
	
	this.dividirEstudo=function(){
		if($("input[type='checkbox'][name='checkgroup']:checked").length>1){
			exibirMensagemDialog("WARNING",["Escolha somente 1 estudo para ser dividido."],"");
			return;
		}else if($("input[type='checkbox'][name='checkgroup']:checked").length==0){
			exibirMensagemDialog("WARNING",["Não há um estudo selecionado para ser dividido."],"");
			return;
		}
		else{
			var id= parseInt($("input[type='checkbox'][name='checkgroup']:checked").attr("id").replace("checkDistribuicao",""));
			if(T.lancamentos[id].estudo==""){
				exibirMensagemDialog("WARNING",["Estudo não foi gerado."],"");				
				return;
			}
		}
		
		estudoParaDivisao=T.lancamentos[id];
		console.log(estudoParaDivisao);
		showTab(contextPath +"/dividirEstudo/index", "Dividir Estudo");
		T.mostrarOpcoes();
	},
	
	this.selecionarCheck = function(check, index) {
		
		T.lancamentos[index].selecionado = check.checked;
		$("#selTodos", _workspace).uncheck();
	},
	
	this.finalizaItem = function(index) {
		
		var id = "#inputRepDistrib" + index;
		
		$(id, _workspace).closest('tr').css({opacity:0.5});
		$(id, _workspace).attr('disabled','true');
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
		
		var noSelect = $('[name=checkgroup]:checked', _workspace).size() == 0;
		
		var action = (noSelect)?"/matrizDistribuicao/finalizarMatrizDistribuicaoTodosItens":"/matrizDistribuicao/finalizarMatrizDistribuicao";
	
		
		$.each(T.lancamentos, function(index, lancamento){
			
			if (lancamento.selecionado) {
				
				data.push({name: 'produtosDistribuicao[' + index + '].idLancamento',  		 value: lancamento.idLancamento});
				data.push({name: 'produtosDistribuicao[' + index + '].idEstudo',  	  		 value: lancamento.estudo});
				data.push({name: 'produtosDistribuicao[' + index + '].repDistrib',    		 value: lancamento.repDistrib});
				data.push({name: 'produtosDistribuicao[' + index + '].dataFinMatDistrib',    value: lancamento.dataFinMatDistrib});
			}
		});

		$.postJSON(pathTela + action, data,
				function(result){
					T.checkUncheckLancamentos(false);
					T.carregarGrid();
					T.exibirMensagemSucesso();
				}
			);
  },
	
  this.confirmarReaberturaDeMatriz = function() {
		
	  var noSelect = $('[name=checkgroup]:checked', _workspace).size() == 0;
	 
	  if (noSelect) {
		  
		  $.postJSON(pathTela + "/matrizDistribuicao/reabrirMatrizDistribuicaoTodosItens", null, 
					function(){
						T.checkUncheckLancamentos(false);
						T.carregarGrid();
						T.exibirMensagemSucesso();
					}
			);
	  }
	  
	  else {
		  
		  var data = [];
		  
		  $(T.lancamentos).each(function(index, lancamento) {
			  
			   if (lancamento.selecionado && lancamento.dataFinMatDistrib != undefined) {
					
					data.push({name: 'produtosDistribuicao[' + index + '].idLancamento',  	   value: lancamento.idLancamento});
				}
			   
			});
		  
			if (data.length == 0) {
				
				exibirMensagem("WARNING", ["Nenhum item selecionado está finalizado."]);
				return;
			}
			
			$.postJSON(pathTela + "/matrizDistribuicao/reabrirMatrizDistribuicao", data, 
					function(){
						T.checkUncheckLancamentos(false);
						T.carregarGrid();
						T.exibirMensagemSucesso();
					}
			);
	  }
	  
  },
  
  this.confirmarExclusaoEstudos = function() {
		
		var data = [];
		
		$.each(T.lancamentos, function(index, lancamento){
			if(lancamento.selecionado) {
				data.push({name: 'produtosDistribuicao[' + index + '].idEstudo',  		value: lancamento.estudo});
				data.push({name: 'produtosDistribuicao[' + index + '].idLancamento',  	value: lancamento.idLancamento});
				data.push({name: 'produtosDistribuicao[' + index + '].numeroEdicao',  	value: lancamento.edicao});
				data.push({name: 'produtosDistribuicao[' + index + '].codigoProduto',  	value: lancamento.codigoProduto});
				
				if (lancamento.idCopia != null) {
					
					data.push({name: 'produtosDistribuicao[' + index + '].idCopia',  	    value: lancamento.idCopia});
				}
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
	
	this.confirmaDuplicaoLinha = function() {
		
		var selecionado = T.obterUnicoItemMarcado();
		
		var data = [];
		
		data.push({name: 'produtoDistribuicao.idLancamento',  value: selecionado.idLancamento});
		data.push({name: 'produtoDistribuicao.liberado',  	  value: selecionado.liberado});
//		data.push({name: 'produtoDistribuicao.numeroEdicao',  value: selecionado.edicao});
//		data.push({name: 'produtoDistribuicao.codigoProduto', value: selecionado.codigoProduto});
		
		$.postJSON(pathTela + "/matrizDistribuicao/duplicarLinha", data,
				function(result){
					T.checkUncheckLancamentos(false);
					T.carregarGrid();
					T.exibirMensagemSucesso();
		});
		
		
	},
	
	this.mostraTelaMatrizDistribuicao = function() {
		$("#dialog-copiar-estudo", _workspace).hide();
		$("#dialog-somar-estudo", _workspace).hide();
		$("#dialog-informacoes-produto", _workspace).hide();
		$("#telaPesquisaMatriz", _workspace).show();
	},
	
	this.mostraTelaCopiarProporcionalDeEstudo = function() {
		T.mostrarOpcoes();
		$("#telaPesquisaMatriz", _workspace).hide();
		$("#dialog-somar-estudo", _workspace).hide();
		$("#dialog-informacoes-produto", _workspace).hide();
		$("#dialog-copiar-estudo", _workspace).show();
		T.inicializarTelaCopiaProporcional();
	},
	
	this.mostraTelaSomarEstudos = function() {
		T.mostrarOpcoes();
		$("#telaPesquisaMatriz", _workspace ).hide();
		$("#dialog-copiar-estudo", _workspace).hide();
		$("#dialog-informacoes-produto", _workspace).hide();
		$("#dialog-somar-estudo", _workspace ).show();
		T.inicializarTelaSomarEstudos();
	},
	
	this.inicializarTelaCopiaProporcional = function() {
		$('#copiarEstudo-estudo').text('');
		$('#copiarEstudo-reparteDistribuido').text('');
		$('#copiarEstudo-idLancamento').text('');
		$('#copiarEstudo-estudoPesquisa').val('');
		T.cancelarCopiaProporcionalDeEstudo();
	},
	
	this.inicializarTelaSomarEstudos = function() {
		$('#somarEstudo-estudo').text('');
		$('#somarEstudo-operacaoConcluida,#somarEstudo-statusOperacao').empty();
		
		T.cancelarSomarEstudos();
	},
	
	this.pesquisarProdutos = function() {
		$('#workspace')
		.tabs({
		  load: function( event, ui ) {
			  if(informacoesProdutoController){
				  informacoesProdutoController.targetRecuperarEstudo="#somarEstudo-estudoPesquisa";
				  informacoesProdutoController.methodEval='change';
			  }
		  }
		}).tabs('addTab', "Informações do Produto", contextPath + "/distribuicao/informacoesProduto/");
		$( "#tabsNovoEntregador", this.workspace ).tabs();
		$("#dialog-informacoes-produto", _workspace).show();
	},
	
	this.redirectToTelaAnalise = function redirectToTelaAnalise(divToHide, divToShow, estudo){

		var matrizSelecionado_estudo =null;
		
		if(typeof(histogramaPosEstudoController)!="undefined"){
			matrizSelecionado_estudo = histogramaPosEstudoController.matrizSelecionado.estudo;
		}
		
        var idEstudo =  matrizSelecionado_estudo || estudo;
        var urlAnalise = contextPath + '/distribuicao/analise/parcial/?id=' + idEstudo;
        if ($('#parcial').val() === 'true') {
			urlAnalise += '&modoAnalise=PARCIAL';
		}

		$.get(
				urlAnalise,
				null, // parametros
				function(html){ // onSucessCallBack
					$(divToHide).hide();
					$(divToShow).html(html);
					$(divToShow).show();
					$( divToShow + ' #botaoVoltarTelaAnalise').click(function voltarTelaAnalise(){
						$(divToShow).hide();
						$(divToHide).show();
					});
			});
		
	},

	this.somarEstudos = function() {
		
		T.esconderOpcoes();
		
		if (!T.validarMarcacaoUnicoItem()) {
			return;
		}
		
		$.each(T.lancamentos, function(index, lancamento){
			if(lancamento.selecionado) {
				selecionado = lancamento;
			}
		});
		
		if (selecionado.estudo == null || selecionado.estudo == "") {
			exibirMensagem("WARNING",["Selecione um produto que tenha um estudo gerado."]);
			return;
		}
		
		if (selecionado.liberado) {
			exibirMensagem("WARNING",["Não é permitido somar estudos liberados."]);
			return;
		}
		
		T.mostraTelaSomarEstudos();
		
		T.populaEdicaoSelecionadaSomarEstudo(selecionado);
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
		$("#copiarEstudo-reparteDistribuido").text(selecionado.repDistrib).formatNumber({format:"#,###", locale:"br"});
		$("#copiarEstudo-pctPadrao").val(selecionado.pctPadrao);
		$("#copiarEstudo-idLancamento").text(selecionado.idLancamento);
		$('#copiarEstudo-idCopia').text(selecionado.idCopia);
		
	},
	
	this.populaEdicaoSelecionadaSomarEstudo = function(selecionado) {
		
		$("#somarEstudo-estudo").text(selecionado.estudo);
		$("#somarEstudo-codigoProduto").text(selecionado.codigoProduto);
		$("#somarEstudo-edicao").text(selecionado.edicao);
		$("#somarEstudo-nomeProduto").text(selecionado.nomeProduto);
		$("#somarEstudo-classificacao").text(selecionado.classificacao);
		$("#somarEstudo-dataLancto").text(selecionado.dataLancto);
		$("#somarEstudo-reparte").text(selecionado.repDistrib);
		$("#somarEstudo-idLancamento").text(selecionado.idLancamento);
		
	},
	
	this.atualizarGrid = function() {		
		
		T.mostrarGridEBotoesAcao();
		
		lancamentosSelecionados = [];		
		$('#selTodos', _workspace).uncheck();	
		
		T.isCliquePesquisar = true;
		
		$("#lancamentoMatrizDistribuicaoGrid", _workspace).flexOptions({			
			url : pathTela + "/matrizDistribuicao/obterGridMatrizDistribuicao",
			dataType : 'json',
			autoload: false,
			singleSelect: true,
			preProcess: T.processaRetornoPesquisa,
			onSuccess: T.onSuccessPesquisa,
			onSubmit: T.confirmarPaginacao
		});
		
		$("#lancamentoMatrizDistribuicaoGrid", _workspace).flexReload();
	},
	
	this.mostrarGridEBotoesAcao = function () {
		
		$("#gridMatrizDistribuicao", _workspace).show();
		
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
                $("#copiarEstudo-copia-reparte").text(result.qtdeReparteEstudo)
                    .formatNumber({format:"#,###", locale:"br"});
		  }
		);
	},
	
	this.carregarProdutoPorEstudoParaSoma = function() {
			
			var data = [];
			
			var codEstudo = $("#somarEstudo-estudoPesquisa").val();

			if(codEstudo==$("#somarEstudo-estudo").text().trim()){
				T.cancelarSomarEstudos();
				exibirMensagem("WARNING",["Estudo original não pode ser igual ao da soma."]);
				return;
			}
			data.push({name: 'estudo',  value: codEstudo});
			
			$.postJSON(pathTela + "/matrizDistribuicao/carregarProdutoEdicaoPorEstudo", data,
				function(result) {
			
				if(!result.codigoProduto){
					T.cancelarSomarEstudos();
					exibirMensagem("WARNING", ["Estudo não encontrado"]);
					return;
				}
					var msgArray = validarEstudoAserSomado(result);
					if(msgArray && msgArray.length>0){
						T.cancelarSomarEstudos();
						exibirMensagem("WARNING", msgArray);
						return 
					}
					$("#somarEstudo-somado-codigoProduto").text(result.codigoProduto);
					$("#somarEstudo-somado-edicao").text(result.numeroEdicao);
					$("#somarEstudo-somado-nomeProduto").text(result.nomeProduto);
					$("#somarEstudo-somado-classificacao").text(result.classificacao);
					$("#somarEstudo-somado-dataLancto").text(result.dataLancto);
					$("#somarEstudo-somado-reparte").text(result.qtdeReparteEstudo);
			  }
			);
		},
	
		validarEstudoAserSomado=function(result){
			var msgArray =[];
			if(result.estudoLiberado){
				msgArray.push("Estudo a ser somado já liberado!");
			}
			if($("#somarEstudo-codigoProduto").text().trim()!=result.codigoProduto){
				msgArray.push("Estudo a ser somado com código produto diferente.");
			}
			if($("#somarEstudo-nomeProduto").text().trim()!=result.nomeProduto){
				msgArray.push("Estudo a ser somado com produto diferente.");
			}
			if($("#somarEstudo-edicao").text().trim()!=result.numeroEdicao){
				msgArray.push("Estudo a ser somado com edição diferente.");
			}
			
			return msgArray;
		},
	
	this.cancelarCopiaProporcionalDeEstudo = function() {
		
		if ($('#copiarEstudo-estudo').text() == "") {
			
			$("#copiarEstudo-copia-estudoPesquisa").val("");
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
	
	this.cancelarSomarEstudos = function() {
		
		$("#somarEstudo-estudoPesquisa").val("");
		$("#somarEstudo-somado-codigoProduto," +
				"#somarEstudo-somado-edicao," +
				"#somarEstudo-somado-edicao," +
				"#somarEstudo-somado-edicao," +
				"#somarEstudo-somado-nomeProduto," +
				"#somarEstudo-somado-classificacao," +
				"#somarEstudo-somado-dataLancto," +
				"#somarEstudo-somado-reparte").text("");
		
		$("#somarEstudo-somado-estudoPesquisa").removeAttr("disabled");
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
		var reparteDistribuido = $("#copiarEstudo-reparteDistribuido").text().replace(/\D/g, '');
		var idLancamento = $("#copiarEstudo-idLancamento").text();
		var idCopia = $("#copiarEstudo-idCopia").text();
		
		var data = [];
		
		data.push({name: 'copiaProporcionalDeDistribuicaoVO.idLancamento', 	     value: idLancamento});
		data.push({name: 'copiaProporcionalDeDistribuicaoVO.idEstudo', 			 value: estudo});
		data.push({name: 'copiaProporcionalDeDistribuicaoVO.fixacao', 			 value: fixacao});
		data.push({name: 'copiaProporcionalDeDistribuicaoVO.reparteDistribuido', value: reparteDistribuido});
		data.push({name: 'copiaProporcionalDeDistribuicaoVO.idCopia', 			 value: idCopia});
		
		
		if (pctPadrao != null) {
			data.push({name: 'copiaProporcionalDeDistribuicaoVO.pacotePadrao', value: pctPadrao});
		}
		
		$.postJSON(pathTela + "/matrizDistribuicao/confirmarCopiarProporcionalDeEstudo", data,
				function(result){
					T.exibirMensagemSucesso();
					$("#copiarEstudo-estudo").text(result.long);
					$("#copiarEstudo-estudoPesquisa").attr('disabled','true');
//					$("#copiarEstudo-reparteDistribuido").text("");
					T.atualizarGrid();
					//T.mostraTelaMatrizDistribuicao();
				}
			);
	},
	
	this.executarSomaDeEstudos=function(estudoPesquisa,estudo,codigoProduto){
		var data = [];
		
		data.push({name: 'idEstudoBase', 	     								 value: estudoPesquisa});
		data.push({name: 'distribuicaoVO.idEstudo', 			 				 value: estudo});
		data.push({name: 'distribuicaoVO.codigoProduto', 			 			 value: codigoProduto});
		
		$.postJSON(pathTela + "/matrizDistribuicao/somarEstudos", data,
				function(result){
					T.exibirMensagemSucesso();
					$('#somarEstudo-statusOperacao').text('CONCLUIDO');
					T.atualizarGrid();
					T.mostraTelaMatrizDistribuicao();
				}
			);
	},
	this.confirmarSomaDeEstudos = function() {
		
		var estudoPesquisa = $("#somarEstudo-estudoPesquisa").val();
		var operacaoConcluida = $('#somarEstudo-statusOperacao').text();
		
		if (operacaoConcluida == "CONCLUIDO") {
			exibirMensagem("WARNING", ["Operação já foi realizada!"]);
			return;
		}
		
		if (estudoPesquisa == null || estudoPesquisa == "") {
			exibirMensagem("WARNING", ["Pesquise um estudo para a soma!"]);
			return;
		}
		
		var estudo = $('#somarEstudo-estudo').text();
		var codigoProduto = $('#somarEstudo-codigoProduto').text();
		
		var data = [];
		
		data.push({name: 'estudoBase', 	     								 value: estudo});
		data.push({name: 'estudoSomado', 			 				 value: estudoPesquisa});
		
		$.postJSON(pathTela + "/matrizDistribuicao/verificarCoincidenciaEntreCotas", data,
				function(result){

					if(result.boolean){
						$("#dialog-confirm-coincidencia-cotas").dialog({
							resizable: false,
							height:'auto',
							width:400,
							modal: true,
							buttons: [ {
							    	text: "Sim",
							    	click: function() {
							    		$(this).dialog("close");
							    		T.executarSomaDeEstudos(estudoPesquisa,estudo,codigoProduto);
							    	}
							    },
							    {
							    	id: "selecaoLancamentosBtnCancelar",
							    	text: "Não",
							    	click: function() {
							    		$(this).dialog("close");
							    	}
							} ],
						});
					}else{
						T.executarSomaDeEstudos(estudoPesquisa,estudo,codigoProduto);
					}
		
				}
			);
		
		
	
		
		
	},
	
	this.inicializar = function() {
		
		T.mostraTelaMatrizDistribuicao();
		
		T.definirAcaoPesquisaTeclaEnter();
		
		$("#lancamentoMatrizDistribuicaoGrid", _workspace).flexigrid({
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
				name : 'reparte',
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
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 1100,
			height : 220,
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
	};
	
	this.analisarCopiaProporcional = function() {
		//testa se registro selecionado possui estudo gerado
		if ($('#copiarEstudo-estudo').html() == null || $('#copiarEstudo-estudo').html() == "") {
			exibirMensagem("WARNING",["Favor gerar um estudo."]);
			return;
		} else {
			// Deve ir direto para EMS 2031
			T.redirectToTelaAnalise('#dialog-copiar-estudo','#telaAnalise', $('#copiarEstudo-estudo').html());
		}
	},

	this.estudoComplementarShow = function() {

	    var selecionado = null;
	    var maisDeUm = false;
	    $.each(T.lancamentos, function(index, lancamento) {
	        if (lancamento.selecionado) {
	            if (selecionado != null) {
	                selecionado = null;
	                maisDeUm = true;
	                return;
	            }
	            selecionado = lancamento;
	        }
	    });
	    if (selecionado == null) {
	        exibirMensagem("ERROR", ["Selecione "+ (maisDeUm ? "apenas" : "") +" um item para esta opção."]);
	        return;
	    }
	    var postData = [];
	    postData.push({name: "estudoId",        value: selecionado.estudo});
	    postData.push({name: "idProdutoEdicao", value: selecionado.idProdutoEdicao});
	    postData.push({name: "idLancamento",    value: selecionado.idLancamento});

	    var temp = $('#workspace').tabs( "option", "ajaxOptions");
	    $('#workspace').tabs( "option", "ajaxOptions", { data: postData, type: 'POST' } );
	    $('#workspace').tabs('addTab', 'Estudo Complementar', pathTela + '/lancamento/estudoComplementar');
	    $('#workspace').tabs( "option", "ajaxOptions", temp );

	    T.esconderOpcoes();
	};

	this.analise = function(){
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
			} else {
				$('#workspace').tabs({load : function(event, ui) {
					
					params = [];
					
					for(var prop in selecionado){
						params.push({
							name : "selecionado." + prop, value : selecionado[prop]
						});
					}
					
					histogramaPosEstudoController.popularFieldsetHistogramaPreAnalise(params,T);
					
					$('#workspace').tabs({load : function(event, ui) {}});
				}});
				
				$('#workspace').tabs('addTab', 'Histograma Pré Análise', contextPath + '/matrizDistribuicao/histogramaPosEstudo');
			}
			
		}else{
			exibirMensagem("WARNING", ["Selecione um item."]);
			return false;
		}
		
	};
	
	this.gerarEstudoAutomatico = function() {
	    
	    if (T.obeterQuantosItensMarcados() == 0) {
	    	 exibirMensagem("ERROR", ["Selecione um item para esta opção."]);
		     return;
	    }
	    
	    var postData = [];
	    
	    $.each(T.lancamentos, function(index, lancamento) {
	        if (lancamento.selecionado) {
	           
	        	postData.push({name : "produtoDistribuicaoVOs["+index+"].idLancamento", value : lancamento.idLancamento});
	            postData.push({name : "produtoDistribuicaoVOs["+index+"].codigoProduto", value : lancamento.codigoProduto});
	    	    postData.push({name : "produtoDistribuicaoVOs["+index+"].numeroEdicao",  value : lancamento.edicao});
	    	    postData.push({name : "produtoDistribuicaoVOs["+index+"].repDistrib", 	 value : lancamento.repDistrib});
	    	    
	    	    if (lancamento.idCopia != null) {
	    	    	
	    	    	postData.push({name : "produtoDistribuicaoVOs["+index+"].lancamento", value : lancamento.idLancamento});
	    	    	postData.push({name : "produtoDistribuicaoVOs["+index+"].idCopia", 	  value : lancamento.idCopia});
	    	    }
	        }
	    });
	    
	    T.postGeracaoEstudoAutomatico(postData, false);
	};
	
	
	this.postGeracaoEstudoAutomatico = function(postData, confirmaUmaEdicaoBase) {
		
		postData.push({name : "confirmaUmaEdicaoBase", 	 value : confirmaUmaEdicaoBase});
		
		$.postJSON(pathTela + "/matrizDistribuicao/gerarEstudoAutomatico", postData,
	            function(result) {
	        T.estudo = result;
	        T.carregarGrid();
	        
	        if(T.estudo.estudo[1].length > 0) {
	        	
	        	var msgGeral = "";
	        	
	        	 $.each(T.estudo.estudo[1], function(index, mensagem) {
	        		
	        			msgGeral += mensagem + "<br/>";
	        	 });
	        	 
	        	 exibirMensagem("WARNING", [msgGeral]);
	        }
	        else if(T.estudo.estudo[2].length == 0) {
	        	
	        	T.exibirMensagemSucesso();
	        }
	        
	        if (T.estudo.estudo[2].length > 0) {
        		
        		T.confirmarUmaEdicaoBase(T.estudo.estudo[2]);
        		return;
        	}
	        
	        if(T.estudo.estudo[0].length == 0 && T.estudo.estudo[2].length == 0) {
	        	
	        	return;
	        }
	        
	        $('#confirmar_variaveis').dialog({
	            resizable: false,
	            height:'auto',
	            width:600,
	            modal: true,
	            buttons: [{
	                id: "btnConfirmarVariaveis",
	                text: "Confirmar",
	                click: function() {
	                	
	                	if (T.estudo.estudo[0].length > 0) {
	                		
	                		$.each(T.estudo.estudo[0], function(index, htmlEstudo) {
	                			
	                			myWindow = window.open('', '_blank');
	     	                    myWindow.document.write(htmlEstudo);
	     	                    myWindow.focus();
	                		});
	                	}
	                   
	                    $(this).dialog("close");
	                }
	            },
	            {id: "btnCancelarVariaveis",
	                text: "Cancelar",
	                click: function() {
	                    $(this).dialog("close");
	                }
	            }],
	        });
	    }
	    );
	},
	
	this.confirmarUmaEdicaoBase = function(edicoesBase) {
		
		var msg = "O(s) seguinte(s) produto(s) tem apenas 1 edição base, confirma a geração de estudo? </br>";
			
	   $.each(edicoesBase, function(index, edicaoBase) {
   	           
		   msg += "[" + edicaoBase.codigoProduto + "] </br>";
  	           
  	    });	
		
		$('#confirmar_uma_edicao_base').html("<p>"+msg+"</p>");
		
		$('#confirmar_uma_edicao_base').dialog({
            resizable: false,
            height:'auto',
            width:600,
            modal: true,
            buttons: [{
                id: "btnConfirmarUmaEdicaoBase",
                text: "Confirmar",
                click: function() {
                	
                	var postData = [];
             	    
             	    $.each(edicoesBase, function(index, edicaoBase) {
             	           
             	            postData.push({name : "produtoDistribuicaoVOs["+index+"].codigoProduto", value : edicaoBase.codigoProduto});
             	    	    postData.push({name : "produtoDistribuicaoVOs["+index+"].numeroEdicao",  value : edicaoBase.edicao});
             	    	    postData.push({name : "produtoDistribuicaoVOs["+index+"].repDistrib", 	 value : edicaoBase.repDistrib});
             	    	    
             	    	    if (edicaoBase.idCopia != null) {
             	    	    	
             	    	    	postData.push({name : "produtoDistribuicaoVOs["+index+"].lancamento", value : edicaoBase.idLancamento});
             	    	    	postData.push({name : "produtoDistribuicaoVOs["+index+"].idCopia", 	  value : edicaoBase.idCopia});
             	    	    }
             	    });
             	    
             	   T.postGeracaoEstudoAutomatico(postData, true);
             	    
                    $(this).dialog("close");
                }
            },
            {id: "btnCancelarVariaveis",
                text: "Cancelar",
                click: function() {
                    $(this).dialog("close");
                }
            }],
        });
	},

	this.gerarEstudoManual = function() {
		var selecionado = null;
		var maisDeUm = false;
		$.each(T.lancamentos, function(index, lancamento) {
			if (lancamento.selecionado) {
				if (selecionado != null) {
					selecionado = null;
					maisDeUm = true;
					return;
				}
				selecionado = lancamento;
			}
		});
		if (selecionado == null) {
			exibirMensagem("ERROR", ["Selecione um item para esta opção."]);
			return;
		}
		var params = 'produto.codigoProduto='+ selecionado.codigoProduto;
		params += '&produto.nomeProduto='+ selecionado.nomeProduto;
		params += '&produto.numeroEdicao='+ selecionado.edicao;
		params += '&produto.periodo='+ selecionado.periodo;
		params += '&produto.classificacao='+ selecionado.classificacao;
		params += '&produto.dataLancto='+ selecionado.dataLancto;
		params += '&produto.reparte='+ selecionado.repDistrib;
        params += '&produto.idProdutoEdicao='+ selecionado.idProdutoEdicao;
        params += '&produtoDistribuicaoVO.idLancamento='+ selecionado.idLancamento;
        params += '&produtoDistribuicaoVO.idCopia='+ selecionado.idCopia;
        
		$('#workspace').tabs('addTab', 'Distribuição Manual', pathTela +'/distribuicaoManual/?'+ params);
		T.esconderOpcoes();
	};
		
	this.distribuicaoVendaMedia = function() {
		var selecionado = null;
		var maisDeUm = false;
		$.each(T.lancamentos, function(index, lancamento) {
			if (lancamento.selecionado) {
				if (selecionado != null) {
					selecionado = null;
					maisDeUm = true;
					return;
				}
				selecionado = lancamento;
			}
		});
		if (selecionado == null) {
			exibirMensagem("ERROR", ["Selecione "+ (maisDeUm ? "apenas" : "") +" um item para esta opção."]);
			return;
		}
		var postData = [];
		postData.push({name : "edicao", value : selecionado.edicao});
		postData.push({name : "estudoId", value : selecionado.estudo});
		postData.push({name : "lancamentoId", value : selecionado.idLancamento});
		postData.push({name : "codigoProduto", value : selecionado.codigoProduto});
		postData.push({name : "juramentado", value : selecionado.juram});
		postData.push({name : "suplementar", value : selecionado.suplem});
		postData.push({name : "lancado", value : selecionado.lancto});
		postData.push({name : "promocional", value : selecionado.promo});
		postData.push({name : "sobra", value : selecionado.sobra});
		postData.push({name : "repDistrib", value : selecionado.repDistrib});
		
		if(selecionado.idCopia != null) {
			
			postData.push({name : "produtoDistribuicaoVO.idLancamento", value : selecionado.idLancamento});
			postData.push({name : "produtoDistribuicaoVO.idCopia", 	    value : selecionado.idCopia});
		}
		
		var temp = $('#workspace').tabs( "option", "ajaxOptions");
		$('#workspace').tabs( "option", "ajaxOptions", { data: postData, type: 'POST' } );
		$('#workspace').tabs('addTab', 'Distribuição Venda Média', pathTela + '/distribuicaoVendaMedia/index');
		$('#workspace').tabs( "option", "ajaxOptions", temp );
		
		T.esconderOpcoes();
	};
}
//@ sourceURL=matrizDistribuicao.js
