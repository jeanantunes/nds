function BalanceamentoLancamento(pathTela, descInstancia, balancemento, workspace) {
	
	var _workspace = workspace;
	
	var T = this;
	
	var opcoesAberto = false;
	
	this.tiposMovimento = []; 
	this.tipoMovimento = null;
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
		
		$("input[name='checkgroup_menu']:checked", _workspace).each(function(i) {
			data.push({name:'idsFornecedores', value: $(this).val()});
		});
				
		$.postJSON(
			pathTela + "/matrizDistribuicao/obterMatrizLancamento", 
			data,
			function(result) {
				
				T.carregarGrid();
			},
			
			T.escondeGrid()
		);
	},
	
	this.escondeGrid = function() { 
		$(".grids", _workspace).hide();
		$(".areaBts").find(".bt_novos", _workspace).hide();		
	} ,


	this.verificarBalanceamentosAlterados = function(funcao) {

		$.postJSON(
			pathTela + "/matrizDistribuicao/verificarBalanceamentosAlterados",
			null,
			function(result){
				
				if (result == "true") 
					T.retornoVerificarBalanceamentosAlterados(funcao);
				else
					funcao();
			}
		);
	},
			
	this.carregarGrid = function() {		
		
		T.mostrarGridEBotoesAcao();
		
		T.linhasDestacadas = [];		
		lancamentosSelecionados = [];		
		$('#selTodos', _workspace).uncheck();	
		
		T.isCliquePesquisar = true;
		
		$(".lancamentosProgramadosGrid", _workspace).flexOptions({			
			url : pathTela + "/matrizDistribuicao/obterGridMatrizLancamento",
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
		
		if(resultadoPesquisa.mensagens) {
			
			exibirMensagem(resultadoPesquisa.mensagens.tipoMensagem, resultadoPesquisa.mensagens.listaMensagens);
			return resultadoPesquisa.rows;
		}
		
		$("#totalGerado", _workspace).clear();
		$("#totalLiberado", _workspace).clear();
		
		T.linhasDestacadas = [];
		
		T.lancamentos = [];
		
		if (resultadoPesquisa[0].rows.lenght == 0) {
			escondeGrid();
			return;
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
		row.cell.reprogramar = T.gerarCheckReprogramar(row.cell.id.toString(), i);
		
		T.formataCampos(row);
		
		var repDist = (row.cell.lancto - row.cell.promo); 
		
		row.cell.sobra = '<span id="sobra'+i+'">'+repDist+'</span>';
		
		row.cell.repDistrib = T.gerarInputRepDistrib(repDist, i);
		
		T.lancamentos.push({
			id:							row.cell.id,
			codigoProduto:              row.cell.codigoProduto,
			estudo:                     row.cell.idEstudo,
			lancto:                     row.cell.lancto,
			promo:                      row.cell.promo,
			repDistrib:                 repDist,
			sobra:                      repDist,
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
			row.cell.lancto = 50;
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
	
	this.reprogramarLancamentosSelecionados = function() {
		
		var selecionados = [];
		
		$.each(T.lancamentos, function(index, lancamento){
			if(lancamento.selecionado == true) {
				selecionados.push(lancamento);
			}
		});
				
		var data = [];
		
		data.push({name: 'novaDataFormatada', value: $("#novaDataLancamento", _workspace).val()});
		
		$.each(T.lancamentos, function(index, lancamento){
			if(lancamento.selecionado == true) {
				data.push({name: 'produtosLancamento[' + index + '].id', 			   		   value: lancamento.id});
				data.push({name: 'produtosLancamento[' + index + '].nomeProduto', 	   		   value: lancamento.nomeProduto});
				data.push({name: 'produtosLancamento[' + index + '].numeroEdicao', 	   		   value: lancamento.numeroEdicao});
				data.push({name: 'produtosLancamento[' + index + '].dataRecolhimentoPrevista', value: lancamento.dataRecolhimentoPrevista});
			}
		});
		
		$.postJSON(
				pathTela + "/matrizDistribuicao/reprogramarLancamentosSelecionados",
				data,
				function(){
					T.atualizarResumoBalanceamento();
					T.checkUncheckLancamentos(false);
				}
			);
				
		$("#dialogReprogramarBalanceamento", _workspace).dialog("close");
	},
	
	this.alterarReparte = function(input, index) {
		$("#sobra" + index, _workspace).html('coisa');
		T.lancamentos[index].repDistrib = input.value;
		var vlr = (T.lancamentos[index].lancto - T.lancamentos[index].promo - T.lancamentos[index].repDistrib); 
		$("#sobra" + index, _workspace).text(vlr);
		T.lancamentos[index].sobra = vlr;
	},
	
	this.gerarCheckReprogramar = function(id, index) { 
		return '<input id="checkReprogramar' + index + '" type="checkbox" value="'+id+'" name="checkgroup" ' +
			   ' onclick="' + T.instancia + '.selecionarCheck(this,\'' + index + '\');" />';
	},
	
	this.selecionarCheck = function(check, index) {
		
		T.lancamentos[index].selecionado = check.checked;
		$("#selTodos", _workspace).uncheck();
	},
	
//	this.onSuccessPesquisa = function() {
//		
//		$(T.linhasDestacadas).each(function(i, item){
//			 id = '#row' + item;			    	
//			 $(id, _workspace).removeClass("erow").addClass("gridLinhaDestacada");
//			 $(id, _workspace).children("td").removeClass("sorted");
//		});
//		 
//
//		$("input[name='dataNova']", _workspace).datepicker({
//			dateFormat: 'dd/mm/yy'
//		});
//		
//		$("input[name='dataNova']", _workspace).mask("99/99/9999");
//		
//	},
	
    /**
     * Obtém tela de confirmação de Balanceamento
     * OBS: Específico para matrizDistribuicao\index.jsp
     * @param codigoProduto
     */
	this.obterConfirmacaoBalanceamento = function (){
		$.postJSON(
			pathTela + "/matrizDistribuicao/obterAgrupamentoDiarioBalanceamento", 
			null,
			function(result) {
				balanceamento.popularConfirmacaoBalanceamento(result,_workspace);
				T.popup_confirmar_balanceamento();
			},
			function() {
				$("#dialog-confirm-balanceamento", _workspace).hide();
			}
		);
	},
	
  this.confirmarExclusaoEstudos = function() {
		
		var data = [];
		
		$.each(T.lancamentos, function(index, lancamento){
			if(lancamento.selecionado) {
				data.push({name: 'produtosDistribuicao[' + index + '].idEstudo',  value: lancamento.estudo});
			}
		});
		
		$.postJSON(pathTela + "/matrizDistribuicao/excluirEstudosSelecionados", data,
				function(){
					T.checkUncheckLancamentos(false);
					T.atualizarGrid();
					exibirMensagemSucesso();
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
					exibirMensagemSucesso();
				}
			);
				
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
	
	this.atualizarGrid = function() {		
		
		T.mostrarGridEBotoesAcao();
		
		T.linhasDestacadas = [];		
		lancamentosSelecionados = [];		
		$('#selTodos', _workspace).uncheck();	
		
		T.isCliquePesquisar = true;
		
		$(".lancamentosProgramadosGrid", _workspace).flexOptions({			
			url : pathTela + "/matrizDistribuicao/obterGridMatrizLancamento",
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
		
		$(".areaBts").find(".bt_novos", _workspace).show();
	},
	
	this.checkUncheckLancamentos = function(checked) {
				
		var todos = $('#selTodos', _workspace);
		
		if(checked) 
			todos.check(checked);
		
		$.each(T.lancamentos, function(index, row){
			row.selecionado = (todos.attr("checked") == 'checked');
		}),
		
		checkAll(document.getElementById('selTodos'),"checkgroup");
		
		//T.verificarBloqueioReprogramacao();
	};
	
//	this.verificarBloqueioReprogramacao = function() {
//		
//		$.each($("input[name='checkgroup']", _workspace), function(index, row) {
//			
//			T.bloquearDesbloquearData(row);
//		});
//	},
	
	this.bloquearDesbloquearData = function(row) {
			
		var checado = row.checked;
		
		var input = $(row.parentElement.parentElement.parentElement).find("input[type='text']");
		var a = $(row.parentElement.parentElement.parentElement).find("a[name='reprogramar']");
		
		if(checado) {				
			input.disable();
			a.attr('onclick',  'return;' + a.attr('onclick') );
			a.parent().addClass("linkDisabled");
		} else {
			input.enable();
			
			if (a.attr('onclick')) {
			
				a.attr('onclick', a.attr('onclick').replace('return;' , '' ) );
			}
			
			a.parent().removeClass("linkDisabled");
		}
	},
	
	this.abrirAlertaVoltarConfiguracaoInicial = function() {
		
		$("#dialogVoltarConfiguracaoInicial", _workspace).dialog({
			resizable: false,
			height:'auto',
			width:600,
			modal: true,
			buttons: [
			    {
			    	id: "voltarConfigOriginalBtnConfirmar",
			    	text: "Confirmar",
			    	click: function() {
					
			    		T.voltarConfiguracaoInicial();
						
						$(this).dialog("close");
			    	}
			    },
			    {
			    	id: "voltarConfigOriginalBtnCancelar",
			    	text: "Cancelar",
			    	click: function() {
			    
			    		$(this).dialog("close");
			    	}
				}
			],
			form: $("#dialogVoltarConfiguracaoInicial", this.workspace).parents("form")
		});
	};
	
	this.voltarConfiguracaoInicial = function() {
		
		T.checkUncheckLancamentos(false);
		
		$.postJSON(
			pathTela + "/matrizDistribuicao/voltarConfiguracaoOriginal",
			null,
			function(result) {
					T.popularResumoPeriodo(result);
					
					T.carregarGrid();
			},
			function() {
				
				$("#resumoPeriodo", _workspace).hide();
			}
		);
	};

	this.inicializar = function() {
		
		balanceamentoLancamento.definirAcaoPesquisaTeclaEnter();
		
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

	this.popularGridProdutosLancamentosCancelados = function(listaProdutosLancamentosCancelados) {
		
		if (listaProdutosLancamentosCancelados.length > 0) {
			$("#flexiGridLancamentosProdutosCancelados", _workspace).flexAddData({
				rows : toFlexiGridObject(listaProdutosLancamentosCancelados),
				page : 1,
				total : 1
			});
		
			this.popupLancamentosCancelados();
		}
	},
	
	this.popupLancamentosCancelados = function() {
		$( "#dialog-alerta-lancamentos-produtos-cancelados", _workspace ).dialog({
			resizable: false,
			width:630,
			modal: true,
			buttons: {
				"Ok": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#dialog-alerta-lancamentos-produtos-cancelados", _workspace).parents("form")
		});
	},
	
	this.mostrarOpcoes = function() {
		opcoesAberto = !opcoesAberto;
		$('.opcoesEstudos').toggle(opcoesAberto);
		$('.setaMuda').attr('src',(opcoesAberto)? contextPath + '/images/p7PM_dark_south_1.gif': contextPath + '/images/p7PM_dark_south.gif');
	},
	
	this.esconderOpcoes = function() {
		setTimeout( function(){
            $( '.opcoesEstudos' ).hide();
             }, 2000);
	},
	
	this.gerarEstudoAutomatico = function() {
		$.each(T.lancamentos, function(index, row){
			if (row.selecionado) {
				var data = [];
				data.push({name:'codigoProduto', value: row.codigoProduto});
				data.push({name:'reparte', value: row.repDistrib});
				
				$.postJSON(
					pathTela + "/matrizDistribuicao/gerarEstudoAutomatico", 
					data,
					function(result) {
						alert('teste');
					}
				);
			}
		});
	};
	
}

//@ sourceURL=matrizDistribuicao.js