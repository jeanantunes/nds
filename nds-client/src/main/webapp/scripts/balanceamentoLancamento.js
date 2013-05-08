function BalanceamentoLancamento(pathTela, descInstancia, balancemento, workspace) {
	
	var _workspace = workspace;
	
	var T = this;
	
	this.tiposMovimento = []; 
	this.tipoMovimento = null;
	this.instancia = descInstancia;
	this.linhasDestacadas = [];
	this.lancamentos = [];
	this.isCliquePesquisar;
	
	this.definirAcaoPesquisaTeclaEnter = function() {
		
		definirAcaoPesquisaTeclaEnter();
	},
	
	this.pesquisar = function() {
		
		$("#resumoPeriodo", _workspace).show();				
		
		var data = [];
		
		data.push({name:'dataLancamento', value: $("#datepickerDe", _workspace).val()});
		
		$("input[name='checkgroup_menu']:checked", _workspace).each(function(i) {
			data.push({name:'idsFornecedores', value: $(this).val()});
		});
				
		$.postJSON(
			pathTela + "/matrizLancamento/obterMatrizLancamento", 
			data,
			function(result) {
				
				if (result.listaProdutosLancamentosCancelados) {
					T.popularGridProdutosLancamentosCancelados(result.listaProdutosLancamentosCancelados);
				}
				
				T.popularResumoPeriodo(result);
				
				T.carregarGrid();
			},
			function() {

				$("#resumoPeriodo", _workspace).hide();
				
				$(".grids", _workspace).hide();
				
				$(".areaBts").find(".bt_novos", _workspace).hide();				
			}
		);
	},
	


	this.verificarBalanceamentosAlterados = function(funcao) {

		$.postJSON(
			pathTela + "/matrizLancamento/verificarBalanceamentosAlterados",
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
			url : pathTela + "/matrizLancamento/obterGridMatrizLancamento",
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
		
	this.processaRetornoPesquisa = function(resultadoPesquisa) {
		
		if(resultadoPesquisa.mensagens) {
			
			exibirMensagem(resultadoPesquisa.mensagens.tipoMensagem, resultadoPesquisa.mensagens.listaMensagens);
			return resultadoPesquisa.rows;
		}
		
		$("#valorTotal", _workspace).clear();
		
		T.linhasDestacadas = [];
		
		T.lancamentos = [];
		
		$("#valorTotal", _workspace).html(resultadoPesquisa[1]);
		
		$.each(resultadoPesquisa[0].rows, function(index,row){ T.processarLinha(index, row);});
		
		return resultadoPesquisa[0];
	},
		
	this.popularResumoPeriodo = function(data) {
	
		$("#tableResumoPeriodo", _workspace).clear();
		
		var rows='<tr>';
		$.each(data.listaResumoPeriodoBalanceamento, function(index, resumo){
			  rows+='<td>';
			  rows+='<div class="box_resumo">';
			  rows+='<label>'+ resumo.dataFormatada +'</label>';
			  rows+='<span class="span_1">Qtde. Títulos:</span>';	 
			  rows+='<span class="span_2">'+ resumo.qtdeTitulos +'</span>';
			  
			  if (resumo.excedeCapacidadeDistribuidor) {
				  
				  rows+='<span class="span_1">Qtde. Exempl.:</span>';
				  rows+='<span name="qtdeExemplares" class="span_2 redLabel"';
				  rows+='title="A quantidade de exemplares excede a capacidade de manuseio ';
				  rows+=data.capacidadeRecolhimentoDistribuidor + ' do distribuidor">';
				  rows+=resumo.qtdeExemplaresFormatada + '</span>';
			  
			  } else {
				  
				  rows+='<span class="span_1">Qtde. Exempl.:</span>';	
				  rows+='<span class="span_2">' + resumo.qtdeExemplaresFormatada + '</span>';
			  }
			  
			  rows+='<span class="span_1">Qtde. Parciais:</span>';
			  rows+='<span class="span_2">' + resumo.qtdeTitulosParciais + '</span>';
			  rows+='<span class="span_1">Peso Total:</span>';
			  rows+='<span class="span_2">'+ resumo.pesoTotalFormatado +'</span>';
			  rows+='<span class="span_1">Valor Total:</span>';
			  rows+='<span class="span_2">'+ resumo.valorTotalFormatado +'</span>';
			  rows+='</div>';
			  rows+='</td>';					  
	    });	
	    
		rows+="</tr>";
	    
	    $("#tableResumoPeriodo", _workspace).append(rows);
	    
	    $("span[name='qtdeExemplares']", _workspace).tooltip();
	},
	
	
	this.processarLinha = function(i,row) {
		
		T.lancamentos.push({
			id:							row.cell.id, 
			numeroEdicao:				row.cell.numeroEdicao,
			nomeProduto:	    		row.cell.nomeProduto,
			dataRecolhimentoPrevista:	row.cell.dataRecolhimentoPrevista,
			novaDataLancamento:			row.cell.novaDataLancamento,
			novaDataOriginal:			row.cell.novaDataLancamento
		});
		
		var colunaProduto = balanceamento.getColunaProduto(row.cell.idProdutoEdicao,
				   							   			   row.cell.nomeProduto,
				   							   			   row.cell.possuiFuro);
		
		row.cell.nomeProduto = colunaProduto;
		
		row.cell.novaDataLancamento = T.gerarInputDataDistrib(row.cell.novaDataLancamento, row.cell.bloquearData, i);
		row.cell.reprogramar = T.gerarCheckReprogramar(row.cell.id.toString(), row.cell.bloquearData, i);
		
		if (row.cell.destacarLinha) {
			T.linhasDestacadas.push(i+1);
		}
		
	},
	
	this.gerarInputDataDistrib = function(dataMatrizDistrib, bloquearData, index) {
		
		return '<input id="inputNovaData' + index + '" onchange="' + T.instancia + '.alterarData(this,\'' + index + '\');" type="text" name="dataNova" style="width:60px; float:left;" value="' + dataMatrizDistrib + '" ' + 
			   (bloquearData? ' disabled="disabled" ' : '') +  
			   '/>' +
		       '<span class="bt_atualizarIco" title="Reprogramar" ' +
		       (bloquearData? ' style="opacity:0.5;" ' : '') + 
		       '>' +
		       '<a id="linkReprogramarUnico' + index + '" href="javascript:;" name="reprogramar" ' + 
		       (bloquearData? '' : ' onclick="' + T.instancia + '.reprogramarLancamentoUnico(' + index + ');') +
		       '">&nbsp;</a></span>';
		
	},
	
	this.reprogramarLancamentoUnico = function(index) {
		
		var data = [];
		
		data.push({name : 'produtoLancamento.novaDataLancamento', value : T.lancamentos[index].novaDataLancamento});
		$.postJSON(
			pathTela + "/matrizLancamento/perguntarDataConfirmadaOuNao",
			data,
			function(retorno) {
				
				if(retorno.boolean == true)
				{
					$(_workspace).append('<div id="confirm_button"></div>');
					
					$( "#confirm_button", _workspace )
					.text('Essa data é uma data já confirmada. Você deseja continuar?');
					
					$( "#confirm_button", _workspace ).dialog({
						resizable: false,
						height:'auto',
						width:300,
						modal: true,
						buttons: [
						    {
						    	id: "dialogConfirmarBtnConfirmar",
						    	text: "Confirmar",
						    	click: function() {
						    		
						    		T.enviarDataDeLancamentoUnico(index);
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
						beforeClose: function() {
							clearMessageDialogTimeout("#confirm_button");
					    },
					    form: $("#confirm_button", _workspace).parents("form")
					});
				}
				else
				{
					T.enviarDataDeLancamentoUnico(index);
				}
				
			}
		);
	},
	
	this.perguntarSeDataEhConfirmadaOuNao = function(novaDataDeLancamento)
	{
		var data = [];
		
		data.push({name : 'produtoLancamento.novaDataLancamento', value : novaDataDeLancamento});
		var retornoDadosDoMetodo = null;
		$.postJSON(
			pathTela + "/matrizLancamento/perguntarDataConfirmadaOuNao",
			data,
			function(retorno) {
				retornoDadosDoMetodo = retorno.boolean;
			}
		);
	
		var retornoDaPergunta = retornoDadosDoMetodo;
		return retornoDaPergunta;
	},
	
	this.enviarDataDeLancamentoUnico = function(index) {
		
		
		var data = [];
		
		data.push({name: 'produtoLancamento.id', 					   value: T.lancamentos[index].id});
		data.push({name: 'produtoLancamento.novaDataLancamento', 	   value: T.lancamentos[index].novaDataLancamento});
		data.push({name: 'produtoLancamento.nomeProduto', 			   value: T.lancamentos[index].nomeProduto});
		data.push({name: 'produtoLancamento.numeroEdicao', 	           value: T.lancamentos[index].numeroEdicao});
		data.push({name: 'produtoLancamento.dataRecolhimentoPrevista', value: T.lancamentos[index].dataRecolhimentoPrevista});
		
		$.postJSON(
				pathTela + "/matrizLancamento/reprogramarLancamentoUnico",
				data,
				function() {
					
					T.atualizarResumoBalanceamento();
					T.checkUncheckLancamentos(false);
					
					T.lancamentos[index].novaDataOriginal = T.lancamentos[index].novaDataLancamento;
				},
				function() {
					
					var inputNovaData = $("#inputNovaData" + index, _workspace);
					
					$(inputNovaData).val(T.lancamentos[index].novaDataOriginal);
					
					T.lancamentos[index].novaDataLancamento = inputNovaData.val();
				}
			);
	},
	
	this.verificaSeDataEstaConfirmada = function(novaDataDeLancamento)
	{	
		var data = [];
		
		data.push({name : 'produtoLancamento.novaDataLancamento', value : novaDataDeLancamento});
		$.postJSON(
			pathTela + "/matrizLancamento/perguntarDataConfirmadaOuNao",
			data,
			function(retorno) {
				
				if(retorno.boolean == true)
				{
					$(_workspace).append('<div id="confirm_button"></div>');
					
					$( "#confirm_button", _workspace )
					.text('Essa data é uma data já confirmada. Você deseja continuar?');
					
					$( "#confirm_button", _workspace ).dialog({
						resizable: false,
						height:'auto',
						width:300,
						modal: true,
						buttons: [
						    {
						    	id: "dialogConfirmarBtnConfirmar",
						    	text: "Confirmar",
						    	click: function() {
						    		
						    		T.reprogramarLancamentosSelecionados();
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
						beforeClose: function() {
							clearMessageDialogTimeout("#confirm_button");
					    },
					    form: $("#confirm_button", _workspace).parents("form")
					});
				}
				else
				{
					T.reprogramarLancamentosSelecionados();
				}
				
			}
		);	
	},
	
	this.reprogramarLancamentosSelecionados = function() {

		var selecionados = [];
		
		$.each(T.lancamentos, function(index, lancamento){
		
			if(lancamento.selecionado == true) {
				selecionados.push(lancamento);
			}
		});
		
				
		var data = [];
		var todos = $('#selTodos', _workspace);
		
		data.push({name: 'novaDataFormatada', value: $("#novaDataLancamento", _workspace).val()});
		if(todos.attr("checked") == 'checked'){
			data.push({name: 'selTodos', value: true});
		} else {
			data.push({name: 'selTodos', value: false});
		}
		
		$.each(T.lancamentos, function(index, lancamento){
			if(lancamento.selecionado == true) {
				data.push({name: 'produtosLancamento[' + index + '].id', 			   		   value: lancamento.id});
				data.push({name: 'produtosLancamento[' + index + '].nomeProduto', 	   		   value: lancamento.nomeProduto});
				data.push({name: 'produtosLancamento[' + index + '].numeroEdicao', 	   		   value: lancamento.numeroEdicao});
				data.push({name: 'produtosLancamento[' + index + '].dataRecolhimentoPrevista', value: lancamento.dataRecolhimentoPrevista});
			}
		});
		
		$.postJSON(
				pathTela + "/matrizLancamento/reprogramarLancamentosSelecionados",
				data,
				function(){
					T.atualizarResumoBalanceamento();
					T.checkUncheckLancamentos(false);
				}
			);
				
		$("#dialogReprogramarBalanceamento", _workspace).dialog("close");
	},
	
	this.alterarData = function(input, index) {
		T.lancamentos[index].novaDataLancamento = input.value;
	},
	
	this.gerarCheckReprogramar = function(id, bloquearData, index) { 
		return '<input id="checkReprogramar' + index + '" type="checkbox" value="'+id+'" name="checkgroup" bloqueado="'+bloquearData+'" ' +
			   (bloquearData? ' disabled="disabled" ' : ' onclick="' + T.instancia + '.selecionarCheck(this,\'' + index + '\');" ') + 
			   ' />';	
	},
	
	this.selecionarCheck = function(check, index) {
		
		T.lancamentos[index].selecionado = check.checked;
				
		$.each($("input[name='checkgroup'][bloqueado!='true']", _workspace),function(index,row) {
			
			T.bloquearDesbloquearData(row);
		});
		
		$("#selTodos", _workspace).uncheck();
	},
	
	this.onSuccessPesquisa = function() {
		
		$(T.linhasDestacadas).each(function(i, item){
			 id = '#row' + item;			    	
			 $(id, _workspace).removeClass("erow").addClass("gridLinhaDestacada");
			 $(id, _workspace).children("td").removeClass("sorted");
		});
		 

		$("input[name='dataNova']", _workspace).datepicker({
			dateFormat: 'dd/mm/yy'
		});
		
		$("input[name='dataNova']", _workspace).mask("99/99/9999");
		
	},
	
    /**
     * Obtém tela de confirmação de Balanceamento
     * OBS: Específico para matrizLancamento\index.jsp
     * @param codigoProduto
     */
	this.obterConfirmacaoBalanceamento = function (){
		$.postJSON(
			pathTela + "/matrizLancamento/obterAgrupamentoDiarioBalanceamento", 
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
	
	/**
     * Confirma a matriz de lançamento
     * OBS: Específico para matrizLancamento\index.jsp
     */
	this.confirmarMatrizLancamento = function (){
		
		var param = serializeArrayToPost('datasConfirmadas', balanceamento.obterDatasMarcadasConfirmacao());
		
		$.postJSON(
			pathTela + "/matrizLancamento/confirmarMatrizLancamento", 
			param,
			function(mensagens) {
				
	           $("#dialog-confirm-balanceamento", _workspace).dialog("close");

			   if (mensagens){
				   var tipoMensagem = mensagens.tipoMensagem;
				   var listaMensagens = mensagens.listaMensagens;
				   if (tipoMensagem && listaMensagens) {
				       exibirMensagem(tipoMensagem, listaMensagens);
			       }
        	   }
			   
			   T.atualizarResumoBalanceamento();
			   T.checkUncheckLancamentos(false);
            },
			null,
			true,
			"dialog-confirmar"
		);
	},
	
	/**
	 * Exibe popup de confirmação de balanceamento
	 */
	this.popup_confirmar_balanceamento = function() {
		$( "#dialog-confirm-balanceamento", _workspace ).dialog({
			resizable: false,
			height:'auto',
			width:300,
			modal: true,
			buttons: [
			    {
			    	id: "dialogConfirmarBtnConfirmar",
			    	text: "Confirmar",
			    	click: function() {
					
			    		T.confirmarMatrizLancamento();
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
			form: $("#dialog-confirm-balanceamento", this.workspace).parents("form"),
			beforeClose: function() {
				clearMessageDialogTimeout("dialog-confirmar");
		    }
		});
	},
	
	this.retornoVerificarBalanceamentosAlterados = function(funcao) {
			
		$("#dialog-confirm", _workspace).dialog({
			resizable: false,
			height:'auto',
			width:600,
			modal: true,
			buttons: [
			    {
			    	id: "lancamentosNaoConfirmadosBtnConfirmar",
			    	text: "Confirmar",
			    	click: function() {
					
			    		funcao();
						
						$(this).dialog("close");
			    	}
			    },
			    {
			    	id: "lancamentosNaoConfirmadosBtnCancelar",
			    	text: "Cancelar",
			    	click: function() {
			    
			    		$(this).dialog("close");
			    	}
				}
			],
			form: $("#dialog-confirm", this.workspace).parents("form")
		});	
	},
	
	this.atualizarResumoBalanceamento = function () {
		
		$.postJSON( pathTela + "/matrizLancamento/atualizarResumoBalanceamento",
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
			url : pathTela + "/matrizLancamento/obterGridMatrizLancamento",
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
		
		T.verificarBloqueioReprogramacao();
	};
	
	this.verificarBloqueioReprogramacao = function() {
		
		$.each($("input[name='checkgroup']", _workspace), function(index, row) {
			
			T.bloquearDesbloquearData(row);
		});
	},
	
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
			pathTela + "/matrizLancamento/voltarConfiguracaoOriginal",
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
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 30,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço Capa R$',
				name : 'precoVenda',
				width : 78,
				sortable : true,
				align : 'right'
			}, {
				display : 'Reparte',
				name : 'repartePrevisto',
				width : 38,
				sortable : true,
				align : 'center'
			}, {
				display : 'Lançamento',
				name : 'descricaoLancamento',
				width : 63,
				sortable : true,
				align : 'left'
			}, {
				display : 'Recolhimento',
				name : 'dataRecolhimentoPrevista',
				width : 70,
				sortable : true,
				align : 'center'
			},{
				display : 'Total R$',
				name : 'valorTotal',
				width : 42,
				sortable : true,
				align : 'right'
			}, {
				display : 'Físico',
				name : 'reparteFisico',
				width : 40,
				sortable : true,
				align : 'center'
			}, {
				display : 'Distribuição',
				name : 'distribuicao',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Previsto',
				name : 'dataLancamentoPrevista',
				width : 55,
				sortable : true,
				align : 'center'
			}, {
				display : 'Matriz/Distrib.',
				name : 'novaDataLancamento',
				width : 97,
				sortable : true,
				align : 'center'
			},{
				display : 'Reprogramar',
				name : 'reprogramar',
				width : 65,
				sortable : false,
				align : 'center'
			}],
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
		
		this.carregarGridLancamentosCancelados();
		
		$(document).ready(function(){
			
			focusSelectRefField($("#datepickerDe"));
			
			$(document.body).keydown(function(e) {
				
				if(keyEventEnterAux(e) && $(".ui-tabs-selected .classROLE_LANCAMENTO").length > 0){
					balanceamentoLancamento.verificarBalanceamentosAlterados(balanceamentoLancamento.pesquisar);
				}
				
				return true;
			});
		});
	},

	this.carregarGridLancamentosCancelados = function(){
		
		$("#flexiGridLancamentosProdutosCancelados", _workspace).flexigrid({
			dataType : 'json',
			colModel : [  {
				display : 'Código',
				name : 'codigo',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 110,
				sortable : true,
				align : 'center'
			}, {
				display : 'Reparte',
				name : 'repartePrevisto',
				width : 100,
				sortable : true,
				align : 'center'
			},{
				display : 'Data Lançamento',
				name : 'dataLancamento',
				width : 120,
				sortable : true,
				align : 'center'
			}],
			width : 600
		});
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
	
	this.reprogramarSelecionados = function() {
		
		$("#dialogReprogramarBalanceamento", _workspace).dialog({
			resizable: false,
			height:'auto',
			width:"300px",
			modal: true,
			buttons: [
			    {
			    	id: "dialogReprogramarBtnConfirmar",
			    	text: "Confirmar",
			    	click: function() {
					
			    		balanceamentoLancamento.verificaSeDataEstaConfirmada($('#novaDataLancamento').val());
			    	}
			    },
			    {
			    	id: "dialogReprogramarBtnCancelar",
			    	text: "Cancelar",
			    	click: function() {
			    
			    		$(this).dialog("close");
			    	}
				}
			],
			beforeClose: function() {
				
				$("#novaDataLancamento", _workspace).val("");
				
				clearMessageDialogTimeout();
			},
			form: $("#dialogReprogramarBalanceamento", _workspace).parents("form")			
		});
	},
	
	this.salvar = function() {
		$.postJSON(
			contextPath + "/matrizLancamento/salvar"
		);
	};
}

//@ sourceURL=balanceamentoLancamento.js
