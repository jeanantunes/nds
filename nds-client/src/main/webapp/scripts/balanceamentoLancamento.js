function BalanceamentoLancamento(pathTela, descInstancia, balancemento, workspace) {
	
	var _workspace = workspace;
	
	var T = this;
	
	this.tiposMovimento = []; 
	this.tipoMovimento = null;
	this.instancia = descInstancia;
	this.linhasDestacadas = [];
	this.lancamentosPaginacao = [];
	this.selecionados = [];
	this.produtosLancamento = [];
	this.dataAtualSelecionada = null;
	
	this.definirAcaoPesquisaTeclaEnter = function() {
		
		definirAcaoPesquisaTeclaEnter();
	},
	
	this.pesquisar = function() {
		
		$("#resumoPeriodo", _workspace).show();
		
		var dataLancamento = $("#matrizLancamento-datepickerDe", _workspace).val();
		
		var data = [];
		
		data.push({name:'dataLancamento', value: dataLancamento});
		
		$("input[name='checkgroup_menu']:checked", _workspace).each(function(i) {
			data.push({name:'idsFornecedores', value: $(this).val()});
		});
		
		T.esconderGrid();
				
		$.postJSON(
			pathTela + "/matrizLancamento/obterMatrizLancamento", 
			data,
			function(result) {
				
				if (result.listaProdutosLancamentosCancelados) {
					T.popularGridProdutosLancamentosCancelados(result.listaProdutosLancamentosCancelados);
				}
				
				T.popularResumoPeriodo(result);
				
				T.mostrarBotoesAcao();
				
				T.habilitarLink("linkVoltarConfiguracaoInicial", T.abrirAlertaVoltarConfiguracaoInicial);
				T.habilitarLink("linkReprogramar", T.reprogramarSelecionados);
				T.habilitarLink("linkBloquearDia", T.salvar);
				T.habilitarLink("linkConfirmar", T.obterConfirmacaoBalanceamento);
				T.habilitarLink("linkReabrirMatriz", T.obterDatasConfirmadasParaReabertura);
				T.habilitarLink("linkSalvar", T.salvarMatriz);
				
				if (eval($("#bloquearBotoes", _workspace).val())) {
					
					var links = $("a[isEdicao='true']", _workspace);
					
					$.each(links, function(index, link) {
						
						if (link.id) {
							
							T.bloquearLink(link.id);
						}
					});
				}

				T.escolherDataAbrirGrid(result, dataLancamento);
			},
			function() {

				$("#resumoPeriodo", _workspace).hide();
				
				T.esconderBotoesAcao();				
			}
		);
	},
	
	this.escolherDataAbrirGrid = function(result, dataLancamento) {
		
		T.dataAtualSelecionada = null;
		
		$.each(result.listaResumoPeriodoBalanceamento, function(index, resumo){
			
			if (resumo.dataFormatada == dataLancamento) {
				
				T.dataAtualSelecionada = resumo.dataFormatada;
			}
		});
		
		T.carregarGrid(T.dataAtualSelecionada, true);
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
	
	this.verificarBalanceamentosAlteradosPesquisar = function(funcao) {
		

		$.postJSON(
			pathTela + "/matrizLancamento/verificarBalanceamentosAlterados",
			null,
			function(result){
				
				if (result == "true") 
					T.retornoVerificarBalanceamentosAlteradosPesquisar(funcao);
				else
					funcao();
			}
		);
	},
	
	this.processaRetornoPesquisa = function(resultadoPesquisa) {
		
		if(resultadoPesquisa.mensagens) {
			
			exibirMensagem(resultadoPesquisa.mensagens.tipoMensagem, resultadoPesquisa.mensagens.listaMensagens);
		
		    return null;
		}
		
		T.linhasDestacadas = [];
		T.lancamentosPaginacao = [];
		T.produtosLancamento = [];
		
		if (resultadoPesquisa[0]) {
		
			T.mostrarGrid();
			
		} else {
			
			T.esconderGrid();
			
			return null;
		}
		
		$("#matrizLancamento-valorTotal", _workspace).clear();
		
		$("#matrizLancamento-valorTotal", _workspace).html(resultadoPesquisa[1]);
		
		if(resultadoPesquisa && resultadoPesquisa[0] && resultadoPesquisa[0].rows) {
			$.each(resultadoPesquisa[0].rows, function(index,row){ T.processarLinha(index, row);});
		}
		
		T.produtosLancamento = resultadoPesquisa[2];
		
		return (resultadoPesquisa[0] && resultadoPesquisa[0].rows) ? resultadoPesquisa[0] : null;
	},
		
	this.popularResumoPeriodo = function(data) {
	
		$("#tableResumoPeriodo", _workspace).clear();
		
		var rows='<tr>';
		$.each(data.listaResumoPeriodoBalanceamento, function(index, resumo){
			
			  if(!resumo.qtdeTitulos){ resumo.qtdeTitulos =0;}
			  if(!resumo.qtdeExemplaresFormatada){ resumo.qtdeExemplaresFormatada =0;}
			  if(!resumo.qtdeTitulosParciais){ resumo.qtdeTitulosParciais =0;}
			  if(!resumo.pesoTotalFormatado){ resumo.pesoTotalFormatado =0;}
			  if(!resumo.valorTotalFormatado){ resumo.valorTotalFormatado =0;}

			  rows+='<td>';
			  rows+='<div class="box_resumo">';
			  rows+= '<label id="labelData'+resumo.dataFormatada.replace(/\//g, "")+'" class="labelDataLancamento">'+ resumo.dataFormatada;
			  
			  if (resumo.statusResumo == "EM_BALANCEAMENTO") {
			 	rows+= '<img src="' + contextPath + '/images/ico_bloqueado.gif" width="15" height="15" border="0" title="" align="left" />';  
			  } else if (resumo.statusResumo == "BALANCEADO") {
			 	rows+= '<img src="' + contextPath + '/images/ico_check.gif" width="15" height="15" border="0" title="" align="left" />';   
			  }else{
			  }
			  
			  rows+= '<a href="javascript:;" onclick="' + T.instancia + '.carregarGrid(' + "'" + resumo.dataFormatada + "'" + ', true);" style="float: right;">';
			  rows+= '<img src="' + contextPath + '/images/ico_detalhes.png" width="15" height="15" border="0" title="Visualizar Produtos" />';
			  rows+= '</a></label>';
			  rows+='<span class="span_1">Qtde. Títulos:</span>';	 
			  rows+='<span class="span_2">'+ resumo.qtdeTitulos  +'</span>';
			  
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
			  rows+='<span class="span_1">Peso Total (kg):</span>';
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
		
		T.lancamentosPaginacao.push({
			id:								row.cell.id, 
			numeroEdicao:					row.cell.numeroEdicao,
			nomeProduto:	    			row.cell.nomeProduto,
			dataRecolhimentoPrevista:		row.cell.dataRecolhimentoPrevista,
			dataRecolhimentoDistribuidor:	row.cell.dataRecolhimentoDistribuidor,
			novaDataLancamento:				row.cell.novaDataLancamento,
			novaDataOriginal:				row.cell.novaDataLancamento,
			statusLancamento:				row.cell.statusLancamento,
			fornecedorId:					row.cell.fornecedorId,
			descricaoLancamento:			row.cell.descricaoLancamento,
			produtoContaFirme:				row.cell.produtoContaFirme
		});
		
		var colunaProduto = balanceamento.getColunaProduto(row.cell.idProdutoEdicao,
				   							   			   row.cell.nomeProduto,
				   							   			   row.cell.possuiFuro);
		
		var stringToReplace ="'"+row.cell.nomeProduto+"'";

		
		row.cell.nomeProduto = colunaProduto;
		
		row.cell.codigoProdutoFormatado = row.cell.codigoProduto;
		
		row.cell.cancelado = T.gerarExcluir(row.cell.cancelado,row.cell.codigoProdutoFormatado,stringToReplace,row.cell.numeroEdicao, i);

		row.cell.novaDataLancamento = T.gerarInputDataDistrib(row.cell.novaDataLancamento, row.cell.bloquearData, i);
		row.cell.reprogramar = T.gerarCheckReprogramar(row.cell.id.toString(), row.cell.bloquearData, i);
		
		if (row.cell.destacarLinha) {
			T.linhasDestacadas.push(i+1);
		}
		
	},
	
   this.gerarExcluir = function(isPodeExcluir,codigoX,nomeX,numeroX, index) {
		

		if(isPodeExcluir){

			// '+codigoX+','+nomeX+','+numeroX+','
			return '<a id="cancelado' + index + '" href="javascript:;" name="cancelado" ' + 
	            ' onclick="' + T.instancia + '.dialogConfirmarExclusaoLancamento('+codigoX+','+nomeX+','+numeroX+',' + index + ');' +
	            '">' + '<img title="Excluir" src="' + contextPath +'/images/ico_excluir.gif" hspace="5" border="0px" />' + '</a>';
		}else{

			return '<a id="cancelado' + index + '" href="javascript:;" name="cancelado" ' + 
            'disabled="disabled' +
             '">' + '<img title="Excluir" src="' + contextPath +'/images/ico_excluir.gif" hspace="5" border="0px" />' + '</a>';
			
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
	
	this.excluirLancamento = function(index) {
		
        var data = [];

        data.push({name : 'produtoLancamento.id', value : T.lancamentosPaginacao[index].id});
		data.push({name : 'produtoLancamento.numeroEdicao', value : T.lancamentosPaginacao[index].numeroEdicao});
		data.push({name : 'produtoLancamento.nomeProduto', value : T.lancamentosPaginacao[index].nomeProduto});

		$.postJSON(pathTela + "/matrizLancamento/excluirLancamento",
				data,
				function(retorno) {
					T.pesquisar();
				}
		);
		
		

	},
	
	this.dialogConfirmarExclusaoLancamento = function(codigoX,nomeX,numeroX,index) {
		
		
		$( "#dialog-excluir-lancamento", _workspace ).text("Deseja realmente Excluir este lançamento : \n\r Nome: "+nomeX+" \n\r Edição: "+numeroX);
		
		$( "#dialog-excluir-lancamento", _workspace ).dialog({
			resizable: false,
			height:'auto',
			width:300,
			modal: true,
			buttons: [
			    {
			    	text: "Confirmar",
			    	click: function() {
			    		
			    		T.excluirLancamento(index);
			    		$(this).dialog("close");
			    	}
			    }, {
			    	text: "Cancelar",
			    	click: function() {
			    
			    		$(this).dialog("close");
			    	}
				}
			],
			beforeClose: function() {
				clearMessageDialogTimeout("#dialog-excluir-lancamento");
		    },
		    form: $("#dialog-excluir-lancamento", _workspace).parents("form")
		});
	},
	
	this.reprogramarLancamentoUnico = function(index) {

		var data = [];
		
		data.push({name : 'produtosLancamento.novaDataLancamento', 				value : T.lancamentosPaginacao[index].novaDataLancamento});
		data.push({name : 'produtosLancamento.fornecedorId', 					value : T.lancamentosPaginacao[index].fornecedorId});
		data.push({name : 'produtosLancamento.descricaoLancamento', 			value : T.lancamentosPaginacao[index].descricaoLancamento});
		data.push({name : 'produtosLancamento.dataRecolhimentoDistribuidor', 	value : T.lancamentosPaginacao[index].dataRecolhimentoDistribuidor});
		data.push({name : 'produtosLancamento.nomeProduto', 					value : T.lancamentosPaginacao[index].nomeProduto});
		data.push({name : 'produtosLancamento.numeroEdicao', 					value : T.lancamentosPaginacao[index].numeroEdicao});
		data.push({name : 'produtosLancamento.produtoContaFirme', 				value : T.lancamentosPaginacao[index].produtoContaFirme});
		data.push({name : 'novaDataLancamento', value: T.lancamentosPaginacao[index].novaDataLancamento});
		
		$.postJSON(
			pathTela + "/matrizLancamento/validarDataReprogramacao",
			data,
			function(retorno) {
				
				var tipo = retorno.tipo;
				var mensagens = retorno.mensagens;
				
				if(tipo == 'DATA_JA_CONFIRMADA') {
					
					$(_workspace).append('<div id="confirm_button"></div>');
					
					$( "#confirm_button", _workspace )
					.text('Essa data é uma data já confirmada. Você deseja continuar?');
					
					T.mostarDialogConfirmacao(index);
					
				} else if (tipo == 'PEB_MENOR_7_DIAS') {
					
					$(_workspace).append('<div id="confirm_button"></div>');
					
					var mensagemDialog = "";
					
					$.each(mensagens, function(index, msg) {
						
						if (mensagemDialog == "") {
							
							mensagemDialog = "<p>" + msg;
							
						} else {
							
							mensagemDialog = mensagemDialog + "<br><br>" + msg;
						}
					});
	
					mensagemDialog = mensagemDialog + "</p>";
					
					$( "#confirm_button", _workspace ).html(mensagemDialog);
					
					T.mostarDialogConfirmacao(index);
					
				} else {
					
					T.enviarDataDeLancamentoUnico(index);
				}
				
			}
		);
	},
	
	this.mostarDialogConfirmacao = function(index) {
		
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
				
				T.voltarDataAnterior(index);
				
				clearMessageDialogTimeout("#confirm_button");
		    },
		    form: $("#confirm_button", _workspace).parents("form")
		});
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

		data.push({name: 'produtoLancamento.id', 					   		value: T.lancamentosPaginacao[index].id});
		data.push({name: 'produtoLancamento.novaDataLancamento', 	   		value: T.lancamentosPaginacao[index].novaDataLancamento});
		data.push({name: 'produtoLancamento.nomeProduto', 			   		value: T.lancamentosPaginacao[index].nomeProduto});
		data.push({name: 'produtoLancamento.numeroEdicao', 	           		value: T.lancamentosPaginacao[index].numeroEdicao});
		data.push({name: 'produtoLancamento.dataRecolhimentoPrevista', 		value: T.lancamentosPaginacao[index].dataRecolhimentoPrevista});
		data.push({name: 'produtoLancamento.dataRecolhimentoDistribuidor', 	value: T.lancamentosPaginacao[index].dataRecolhimentoDistribuidor});
		data.push({name: 'produtoLancamento.fornecedorId', 			   		value: T.lancamentosPaginacao[index].fornecedorId});
		data.push({name: 'produtoLancamento.descricaoLancamento', 			value: T.lancamentosPaginacao[index].descricaoLancamento});
		data.push({name: 'produtoLancamento.produtoContaFirme', 			value: T.lancamentosPaginacao[index].produtoContaFirme});
		$.postJSON(
				pathTela + "/matrizLancamento/reprogramarLancamentoUnico",
				data,
				function(retorno) {
					
					if (retorno.info) {

						  T.popup_confirm_dados_alterados(retorno.info,T.lancamentosPaginacao[index].novaDataLancamento);
						  
					}else{
						T.reprogramarLancamentosSelecionadosSalvar(T.lancamentosPaginacao[index].novaDataLancamento);
					}
					
					
					T.atualizarResumoBalanceamento();
					T.checkUncheckLancamentos();
					T.lancamentosPaginacao[index].novaDataOriginal = T.lancamentosPaginacao[index].novaDataLancamento;
					
				
                  
				},
				function() {
					
					T.voltarDataAnterior(index);
				}
			);
	},
	
	this.voltarDataAnterior = function(index) {
		
		var inputNovaData = $("#inputNovaData" + index, _workspace);
		
		$(inputNovaData).val(T.lancamentosPaginacao[index].novaDataOriginal);
		
		T.lancamentosPaginacao[index].novaDataLancamento = inputNovaData.val();
	},
	
	this.verificaSeDataEstaConfirmada = function(novaDataDeLancamento)
	{	
		var data = [];
		
		data.push({name : 'novaDataLancamento', value : novaDataDeLancamento});
		
		$.each(T.selecionados, function(index, lancamentoSelecionado) {
			
			data.push({name: 'produtosLancamento[' + index + '].id', 			   		   		value: lancamentoSelecionado.id});
			data.push({name: 'produtosLancamento[' + index + '].nomeProduto', 	   		   		value: lancamentoSelecionado.nomeProduto});
			data.push({name: 'produtosLancamento[' + index + '].numeroEdicao', 	   		   		value: lancamentoSelecionado.numeroEdicao});
			data.push({name: 'produtosLancamento[' + index + '].dataRecolhimentoPrevista', 		value: lancamentoSelecionado.dataRecolhimentoPrevista});
			data.push({name: 'produtosLancamento[' + index + '].dataRecolhimentoDistribuidor',	value: lancamentoSelecionado.dataRecolhimentoDistribuidor});
			data.push({name: 'produtosLancamento[' + index + '].fornecedorId'			 , 		value: lancamentoSelecionado.fornecedorId});
			data.push({name: 'produtosLancamento[' + index + '].descricaoLancamento', 			value: lancamentoSelecionado.descricaoLancamento});
			data.push({name: 'produtosLancamento[' + index + '].produtoContaFirme', 			value: lancamentoSelecionado.produtoContaFirme});
		});
		
		$.postJSON(
			pathTela + "/matrizLancamento/validarDataReprogramacao",
			data,
			function(retorno) {
				
				var tipo = retorno.tipo;
				var mensagens = retorno.mensagens;
				
				if(tipo == 'DATA_JA_CONFIRMADA') {
					
					$(_workspace).append('<div id="confirm_button"></div>');
					
					$( "#confirm_button", _workspace )
					.text('Essa data é uma data já confirmada. Você deseja continuar?');
					
					T.mostarDialogConfirmacaoSelecionados(novaDataDeLancamento);
					
				} else if (tipo == 'PEB_MENOR_7_DIAS') {
					
					$(_workspace).append('<div id="confirm_button"></div>');
					
					var mensagemDialog = "";
					
					$.each(mensagens, function(index, msg) {
						
						if (mensagemDialog == "") {
							
							mensagemDialog = "<p>" + msg;
							
						} else {
							
							mensagemDialog = mensagemDialog + "<br><br>" + msg;
						}
					});
	
					mensagemDialog = mensagemDialog + "</p>";
					
					$( "#confirm_button", _workspace ).html(mensagemDialog);
					
					T.mostarDialogConfirmacaoSelecionados(novaDataDeLancamento);
				
				} else {
					
					T.reprogramarLancamentosSelecionados(novaDataDeLancamento, false);
				}
			}
		);	
	},
	
	this.mostarDialogConfirmacaoSelecionados = function(novaDataDeLancamento) {
		
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
			    		
			    		T.reprogramarLancamentosSelecionados(novaDataDeLancamento, true);
			    		$(this).dialog("close");
			    	}
			    },
			    {
			    	id: "dialogConfirmarBtnCancelar",
			    	text: "Cancelar",
			    	click: function() {
			    
			    		T.reprogramarLancamentosSelecionados(novaDataDeLancamento, false);
			    		$(this).dialog("close");
			    	}
				}
			],
			beforeClose: function() {
				clearMessageDialogTimeout("#confirm_button");
		    },
		    form: $("#confirm_button", _workspace).parents("form")
		});
	},
	
	this.reprogramarLancamentosSelecionadosSalvar = function(novaDataFormatada) {
		
       var data = [];
		
		data.push({name: 'novaDataFormatada', value: novaDataFormatada});
			
		$.each(T.selecionados, function(index, lancamentoSelecionado) {
			
			data.push({name: 'produtosLancamento[' + index + '].id', 			   		   		value: lancamentoSelecionado.id});
			data.push({name: 'produtosLancamento[' + index + '].nomeProduto', 	   		   		value: lancamentoSelecionado.nomeProduto});
			data.push({name: 'produtosLancamento[' + index + '].numeroEdicao', 	   		   		value: lancamentoSelecionado.numeroEdicao});
			data.push({name: 'produtosLancamento[' + index + '].dataRecolhimentoPrevista', 		value: lancamentoSelecionado.dataRecolhimentoPrevista});
			data.push({name: 'produtosLancamento[' + index + '].dataRecolhimentoDistribuidor', 	value: lancamentoSelecionado.dataRecolhimentoDistribuidor});
			data.push({name: 'produtosLancamento[' + index + '].fornecedorId'			 , 		value: lancamentoSelecionado.fornecedorId});
			data.push({name: 'produtosLancamento[' + index + '].descricaoLancamento', 			value: lancamentoSelecionado.descricaoLancamento});
			data.push({name: 'produtosLancamento[' + index + '].produtoContaFirme', 			value: lancamentoSelecionado.produtoContaFirme});
		});
		
		$.postJSON(
				pathTela + "/matrizLancamento/reprogramarLancamentosSelecionadosSalvar",
				data,
				function(){
					T.atualizarResumoBalanceamento();
				}
			);
		
		$("#dialogReprogramarBalanceamento", _workspace).dialog("close");
	},

		
	this.reprogramarLancamentosSelecionados = function(novaDataDeLancamento, reprogramarProdutosPEBMenor7Dias) {

		var data = [];
		
		data.push({name: 'novaDataFormatada', value: $("#novaDataLancamento", _workspace).val()});
		
		data.push({name: 'reprogramarProdutosPEBMenor7Dias', value: reprogramarProdutosPEBMenor7Dias});
		
			
		$.each(T.selecionados, function(index, lancamentoSelecionado) {
			
			data.push({name: 'produtosLancamento[' + index + '].id', 			   		   		value: lancamentoSelecionado.id});
			data.push({name: 'produtosLancamento[' + index + '].nomeProduto', 	   		   		value: lancamentoSelecionado.nomeProduto});
			data.push({name: 'produtosLancamento[' + index + '].numeroEdicao', 	   		   		value: lancamentoSelecionado.numeroEdicao});
			data.push({name: 'produtosLancamento[' + index + '].dataRecolhimentoPrevista', 		value: lancamentoSelecionado.dataRecolhimentoPrevista});
			data.push({name: 'produtosLancamento[' + index + '].dataRecolhimentoDistribuidor',	value: lancamentoSelecionado.dataRecolhimentoDistribuidor});
			data.push({name: 'produtosLancamento[' + index + '].fornecedorId'			 , 		value: lancamentoSelecionado.fornecedorId});
			data.push({name: 'produtosLancamento[' + index + '].descricaoLancamento', 			value: lancamentoSelecionado.descricaoLancamento});
			data.push({name: 'produtosLancamento[' + index + '].produtoContaFirme', 			value: lancamentoSelecionado.produtoContaFirme});
		});
		
		$.postJSON(
				pathTela + "/matrizLancamento/reprogramarLancamentosSelecionados",
				data,
				function(retorno){
					
					if (retorno.info) {
					  T.popup_confirm_dados_alterados(retorno.info,novaDataDeLancamento);
					} else if (retorno.msg) {

						exibirMensagem("WARNING", retorno.msg);
						T.reprogramarLancamentosSelecionadosSalvar(novaDataDeLancamento);
						
					} else if (retorno.composto) {

						exibirMensagem("WARNING", retorno.composto[0]);
						T.popup_confirm_dados_alterados(retorno.composto[1],novaDataDeLancamento);
						

					} else if (retorno.info) {
						
						T.popup_confirm_dados_alterados(retorno.composto[1],novaDataDeLancamento);
					
					}else{
					
						T.reprogramarLancamentosSelecionadosSalvar(novaDataDeLancamento);
					}
					
					T.atualizarResumoBalanceamento();
				}
			);
		
		
		$("#dialogReprogramarBalanceamento", _workspace).dialog("close");
	},
	
	this.alterarData = function(input, index) {
		T.lancamentosPaginacao[index].novaDataLancamento = input.value;
	},
	
	this.gerarCheckReprogramar = function(id, bloquearData, index) { 
		
		var input = '<input id="checkReprogramar' + index + '" type="checkbox" value="'+id+'" name="checkgroup" bloqueado="'+bloquearData+'" ' +
		   (bloquearData? ' disabled="disabled" ' : ' onclick="' + T.instancia + '.selecionarCheck(this,\'' + index + '\');" ') + 
		   ' />';
		
		return input;	
	},
	
	this.selecionarCheck = function(check, index) {
		
		var checked = check.checked;
		
		T.lancamentosPaginacao[index].selecionado = checked;
				
		$.each($("input[name='checkgroup'][bloqueado!='true']", _workspace),function(index,row) {
			
			T.bloquearDesbloquearData(row);
		});
		
		if (checked) {
			
			T.selecionados.push(T.lancamentosPaginacao[index]);
			
		} else {
		
			var indexRemover;
			
			$.each(T.selecionados, function(i, lancamentoSelecionado) {
				
				if (lancamentoSelecionado.id == T.lancamentosPaginacao[index].id) {
					
					indexRemover = i;
				}
			});
			
			T.selecionados.splice(indexRemover, 1);
		}
		
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
		
		var selTodos = $('#selTodos', _workspace).attr("checked") == "checked";
		
		if (selTodos) {
		
			checkAll($('#selTodos', _workspace), "checkgroup");
			
		} else {
			
			$.each(T.selecionados, function(i, lancamentoSelecionado) {
				
				$("input[value='" + lancamentoSelecionado.id + "']", _workspace).check();
			});
		}
		
		T.verificarBloqueioReprogramacao();
		
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
			   
			   T.atualizarResumoBalanceamentoSalvar();
            },
			null,
			true,
			"dialog-confirmar"
		);
	},
	
	
	/**
	 * Exibe popup de confirmação de balanceamento
	 */
	this.popup_confirm_dados_alterados = function(info,novaDataFormatada) {
		
		//data.push({name: 'novaDataFormatada', value: $("#novaDataLancamento", _workspace).val()});
		var textoX ="Os lancamentos serão transferidos para um dia que o fornecedor não Opera:"+"\n"+"\n";
		textoX =textoX+"\n";
		
		for(var i = 0;i < info.length; i++) {
			textoX= textoX+info[i]+"\n";
		}
		
		textoX =textoX+"\n";
		textoX =textoX+"\n"+" Deseja Continuar ?";
		
        $( "#dialog-confirm-dados-alterados", _workspace ).text(textoX);
		
		$( "#dialog-confirm-dados-alterados", _workspace ).dialog({
			id: "#dialog-confirm-dados-alterados",
	    	text: info,
			resizable: false,
			height:'auto',
			width:490,
			modal: true,
			buttons: [
			    {
			    	id: "dialogConfirmarBtnConfirmar",
			    	text: "Confirmar",
			    	click: function() {
					
			    		T.reprogramarLancamentosSelecionadosSalvar(novaDataFormatada);
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
			form: $("#dialog-confirm-dados-alterados", this.workspace).parents("form"),
			beforeClose: function() {
				clearMessageDialogTimeout("dialog-confirm-dados-alterados");
		    }
		});
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
			    	text: "Manter",
			    	click: function() {
					
			    		T.salvarMatriz();
			    		
			    		$(this).dialog("close");
			    		
			    		funcao();
			    	}
			    },
			    {
			    	id: "lancamentosNaoConfirmadosBtnCancelar",
			    	text: "Descartar",
			    	click: function() {
			    
			    		$(this).dialog("close");
			    		
			    		funcao();

			    	}
				}
			],
			form: $("#dialog-confirm", this.workspace).parents("form")
		});	
		
	},
	
	this.retornoVerificarBalanceamentosAlteradosPesquisar = function(funcao) {
		
		$("#dialog-confirm", _workspace).dialog({
			resizable: false,
			height:'auto',
			width:600,
			modal: true,
			buttons: [
			    {
			    	id: "lancamentosNaoConfirmadosBtnConfirmar",
			    	text: "Manter",
			    	click: function() {
					
			    		$(this).dialog("close");
			    	}
			    },
			    {
			    	id: "lancamentosNaoConfirmadosBtnCancelar",
			    	text: "Descartar",
			    	click: function() {
			    
			    		$(this).dialog("close");
			    		funcao();
			    	}
				}
			],
			form: $("#dialog-confirm", this.workspace).parents("form")
		});	
	},
	
	this.atualizarResumoBalanceamentoSalvar = function () {
		
		$.postJSON( pathTela + "/matrizLancamento/atualizarResumoBalanceamento",
				   null,
				   function(result) {
						T.popularResumoPeriodo(result);
						
						T.carregarGrid(null, false);
						setTimeout(T.salvarMatriz(),200);
				   }
		);
	},
	
	this.atualizarResumoBalanceamento = function () {
		
		$.postJSON( pathTela + "/matrizLancamento/atualizarResumoBalanceamento",
				   null,
				   function(result) {
						T.popularResumoPeriodo(result);
						
						T.carregarGrid(null, false);
				   }
		);
	},
	
	this.carregarGrid = function(dataLancamento, iniciarGrid) {
		
		if (dataLancamento!=null){
		 $("#matrizLancamento-datepickerDe", _workspace).val(dataLancamento);
		 $.each($('.labelDataLancamento'), function(k, v) {
			 $(v).css('font-size', '11px').css('color', '#01649E');
		 });
		 $("#labelData"+ dataLancamento.replace(/\//g, ""), _workspace).css("font-size", "11px");
		 $("#labelData"+ dataLancamento.replace(/\//g, ""), _workspace).css("color", "red");
		} else {
			if($("#matrizLancamento-datepickerDe", _workspace).val()) {
				$("#labelData"+ $("#matrizLancamento-datepickerDe", _workspace).val().replace(/\//g, ""), _workspace).css("font-size", "11px");
				$("#labelData"+ $("#matrizLancamento-datepickerDe", _workspace).val().replace(/\//g, ""), _workspace).css("color", "red");
			}
		}
		
		if($("#matrizLancamento-datepickerDe", _workspace).val()) {
			
			T.linhasDestacadas = [];		
			lancamentosSelecionados = [];	
			
			if (dataLancamento || iniciarGrid) {
				
				T.dataAtualSelecionada = dataLancamento;
			}
		
			$('#selTodos', _workspace).uncheck();
		
			T.checkUncheckLancamentos();
		
		
			if (iniciarGrid) {
			
				$(".lancamentosProgramadosGrid", _workspace).flexOptions({			
					url : pathTela + "/matrizLancamento/obterGridMatrizLancamento",
					dataType : 'json',
					autoload: false,
					singleSelect: true,
					preProcess: T.processaRetornoPesquisa,
					onSuccess: T.onSuccessPesquisa,
					params: [
				         {name:'dataLancamentoFormatada', value: T.dataAtualSelecionada}
				    ],
				    newp: 1,
				});
				
			} else {
				
				$(".lancamentosProgramadosGrid", _workspace).flexOptions({			
					url : pathTela + "/matrizLancamento/obterGridMatrizLancamento",
					dataType : 'json',
					autoload: false,
					singleSelect: true,
					preProcess: T.processaRetornoPesquisa,
					onSuccess: T.onSuccessPesquisa,
					params: [
				         {name:'dataLancamentoFormatada', value: T.dataAtualSelecionada}
				    ]
				});
			}
			
			$(".lancamentosProgramadosGrid", _workspace).flexReload();
	
			$.getJSON(
		 			contextPath + "/matrizLancamento/obterDatasConfirmadasReabertura", 
		 			null,
		 			function(result) {
		 				
		 				if (result.length == 0) {
		 		
		 					$("#linkReabrirMatriz", _workspace).hide();
		 					
		 				} else {
		 					$("#linkReabrirMatriz", _workspace).show();
	
		 				}
		 			}
		 		);		
			
			$.postJSON(
		 			contextPath + "/matrizLancamento/obterAgrupamentoDiarioBalanceamento", 
		 			null,
		 			function(result) {
		 				
		 				if (result.length == 0) {
		 		
		 					$("#linkConfirmar", _workspace).hide();
		 					
		 				} else {
		 					$("#linkConfirmar", _workspace).show();
	
		 				}
		 			}
		 		);	
		}
	},
	
	this.mostrarGrid = function() {
		
		$(".grids", _workspace).show();
	},
	
	this.esconderGrid = function() {
		
		$(".grids", _workspace).hide();
	},
	
	this.mostrarBotoesAcao = function() {
		
		$(".areaBts",_workspace).find(".bt_novos", _workspace).show();
	},
	
	this.esconderBotoesAcao = function() {
		
		$(".areaBts",_workspace).find(".bt_novos", _workspace).hide();
	},
	
	
	this.checkUncheckLancamentos = function() {
				
		var inputTodos = $('#selTodos', _workspace);
		
		var todos = (inputTodos.attr("checked") == 'checked');
		
		if (todos) {
			
			$.each(T.produtosLancamento, function(key, value){
			
				if(!value.bloquearData) {
					T.selecionados.push(value); 
				}
			});

		} else {
			
			T.selecionados = [];
		}
		
		checkAll(document.getElementById('selTodos'), "checkgroup");
		
		T.verificarBloqueioReprogramacao();
	};
	
	this.verificarBloqueioReprogramacao = function() {
		
		$.each($("input[name='checkgroup']", _workspace), function(index, row) {
			
			T.bloquearDesbloquearData(row);
		});
	},
	
	this.bloquearDesbloquearData = function(row) {
		
		if (row.disabled) {
		    
		    return;
		}
		
		var checado = row.checked;
		
		var bloquearBotoes = eval($("#bloquearBotoes", _workspace).val());
		
		var input = $(row.parentElement.parentElement.parentElement).find("input[type='text']");
		var a = $(row.parentElement.parentElement.parentElement).find("a[name='reprogramar']");
		
		if(checado || bloquearBotoes) {
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
		
		$.postJSON(
			pathTela + "/matrizLancamento/voltarConfiguracaoOriginal",
			null,
			function(result) {
				
					T.esconderGrid();
				
					T.popularResumoPeriodo(result);
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
				name : 'codigoProdutoFormatado',
				width : 45,
				sortable : true,
				align : 'center'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 30,
				sortable : true,
				align : 'center'
			}, {
				display : 'Capa R$',
				name : 'precoVenda',
				width : 40,
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
				width : 62,
				sortable : true,
				align : 'left'
			}, {
				display : 'Recolhto',
				name : 'dataRecolhimentoDistribuidor',
				width : 52,
				sortable : true,
				align : 'center'
			},{
				display : 'PEB',
				name : 'peb',
				width : 18,
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
				width : 38,
				sortable : true,
				align : 'center'
			}, {
				display : 'Distrib.',
				name : 'distribuicao',
				width : 40,
				sortable : true,
				align : 'center'
			}, {
				display : 'Previsto',
				name : 'dataLancamentoPrevista',
				width : 54,
				sortable : true,
				align : 'center'
			}, {
				display : 'Matriz/Distrib.',
				name : 'novaDataLancamento',
				width : 98,
				sortable : true,
				align : 'center'
			},{
				display : 'Repr. ',
				name : 'reprogramar',
				width : 24,
				sortable : false,
				align : 'center'
			},{
				display : 'Excl. ',
				name : 'cancelado',
				width : 24,
				sortable : false,
				align : 'center'
		    },{
		    	display : 'Fornec.',
		    	name : 'nomeFantasia',
		    	width : 38,
		    	sortable : true,
		    	align : 'center'
		    },{
		    	display : 'Status.',
		    	name : 'statusLancamento',
		    	width : 80,
		    	sortable : true,
		    	align : 'center'
		    }],
			sortname : "nomeProduto",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 50,
			showTableToggleBtn : true,
			width : 960,
			height : 180,
			disableSelect : true
			});

		$( "#matrizLancamento-datepickerDe", _workspace ).datepicker({
			showOn: "button",
			dateFormat: 'dd/mm/yy',
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#matrizLancamento-datepickerDe_1", _workspace ).datepicker({
			showOn: "button",
			dateFormat: 'dd/mm/yy',
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$("#matrizLancamento-datepickerDe", _workspace).mask("99/99/9999");
		$("#matrizLancamento-datepickerDe_1", _workspace).mask("99/99/9999");
		
		
		$( "#novaDataLancamento", _workspace ).datepicker({
			showOn: "button",
			dateFormat: 'dd/mm/yy',
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$("#novaDataLancamento", _workspace).mask("99/99/9999");
		
		this.carregarGridLancamentosCancelados();
		
		$(document).ready(function(){
			
			focusSelectRefField($("#matrizLancamento-datepickerDe"));
			
			$('#linkPesquisarMatrizLancamento').keydown(function(e) {
				
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
		
		
        var dataLancamento = $("#matrizLancamento-datepickerDe", _workspace).val();
		
		var data = [];
		
		data.push({name:'dataLancamento', value: dataLancamento});
		
		$("input[name='checkgroup_menu']:checked", _workspace).each(function(i) {
			data.push({name:'idsFornecedores', value: $(this).val()});
		});
		
		$.postJSON(
			pathTela + "/matrizLancamento/salvar",
			data,
			function(mensagens) {
			   
			   if (mensagens){
				   
				   var tipoMensagem = mensagens.tipoMensagem;
				   var listaMensagens = mensagens.listaMensagens;
				   
				   if (tipoMensagem && listaMensagens) {
				       exibirMensagem(tipoMensagem, listaMensagens);
			       }
        	   }
			   
			   T.atualizarResumoBalanceamentoSalvar();
            }
		);
	},
	
	this.salvarMatriz = function() {
		
		
        var dataLancamento = $("#matrizLancamento-datepickerDe", _workspace).val();
		
		var data = [];
		
		data.push({name:'dataLancamento', value: dataLancamento});
		
		$("input[name='checkgroup_menu']:checked", _workspace).each(function(i) {
			data.push({name:'idsFornecedores', value: $(this).val()});
		});
		
		$.postJSON(
			pathTela + "/matrizLancamento/salvarMatriz",
			data,
			function(mensagens) {
			   
			   if (mensagens){
				   
				   var tipoMensagem = mensagens.tipoMensagem;
				   var listaMensagens = mensagens.listaMensagens;
				   
				   if (tipoMensagem && listaMensagens) {
				       exibirMensagem(tipoMensagem, listaMensagens);
			       }
        	   }
			   
			   T.atualizarResumoBalanceamento();
            }
		);
	},
	
	this.obterDatasConfirmadasParaReabertura= function() {
	 	
	 	$.getJSON(
	 			contextPath + "/matrizLancamento/obterDatasConfirmadasReabertura", 
	 			null,
	 			function(result) {
	 				
	 				if (result.length == 0) {
	 		
	 					$("#linkReabrirMatriz", _workspace).hide();
	 					
	 				} else {
	 					this.popularPopupReaberturaMatrizes(result);
	 					$("#linkReabrirMatriz", _workspace).show();

	 				}
	 			}
	 		);		
	},
	

	popularPopupReaberturaMatrizes= function(result) {
			
			var conteudo = '';
			
			$(result).each(function(index, value) {
				conteudo += '<tr class="class_linha_1"><td name=dataReabertura_'+index+' >';
				conteudo += value;
				conteudo += '</td>';
				conteudo += '<td align="center"><input name=checkMatrizReabertura type="checkbox" value="' + value + '" /></td>';
				conteudo += '</tr>';
			});
			
			//console.log(conteudo);
			$("#tableReaberturaMatrizConfirmada", _workspace).html(conteudo);
			abrirPopupReabrirMatriz();
			//$("#dialog-reabrir-matriz", _workspace).show();
		},
		
	abrirPopupReabrirMatriz= function() {
			
		$( "#dialog-reabrir-matriz", _workspace).dialog({
			resizable: false,
			height:'auto',
			width:300,
			modal: true,
			buttons: [
			    {
			    	id: "dialogReaberturaBtnConfirmar",
			    	text: "Reabrir",
			    	click: function() {
					
			    		reabrirMatriz();
			    		//balanceamentoLancamento.verificarBalanceamentosAlterados(balanceamentoLancamento.pesquisar);
			    	}
			    },
			    {
			    	id: "dialogReaberturaBtnCancelar",
			    	text: "Cancelar",
			    	click: function() {
			    		
			    		$(this).dialog("close");
			    		$("#linkReabrirMatriz", _workspace).show();
			    	}
				}
			],
			beforeClose: function() {
				$("input[name='checkMatrizReabertura']:checked", _workspace).attr("checked", false);
		    },
		    form: $("#form-reabrir-matriz", _workspace)
		});
	},
	
	reabrirMatriz= function() {

		var params = new Array();
		
		$("input[name='checkMatrizReabertura']:checked").each(function(index, value) {
			
			params.push({name: 'datasReabertura['+index+']', value: value.value});
		});
		
		var dataLancamento = $("#matrizLancamento-datepickerDe", _workspace).val();
		
		params.push({name:'dataLancamento', value: dataLancamento});
		
		$("input[name='checkgroup_menu']:checked", _workspace).each(function(i) {
			params.push({name:'idsFornecedores', value: $(this).val()});
		});
		
		$.postJSON(
			contextPath + "/matrizLancamento/reabrirMatriz",
			params,
			function(result) {
				$("#dialog-reabrir-matriz").dialog("close");
				
				T.carregarGrid(dataLancamento, true);
				
				exibirMensagem(
					result.tipoMensagem, 
					result.listaMensagens
				);
				T.atualizarResumoBalanceamento();
			}
			
		);
		
	},
	
	this.verificarBloqueioMatrizLancamento = function() {
		
		$("#bloquearBotoes", _workspace).val(false);
		
		$.postJSON(
				contextPath + "/matrizLancamento/verificarBloqueioMatrizLancamentoPost", 
				null, 
				function(result) {
					
					balanceamentoLancamento.mostrarPopUpBloqueio();
				},
				function() {
					
					$("#bloquearBotoes", _workspace).val(true);
					
					balanceamentoLancamento.verificarBalanceamentosAlteradosPesquisar(balanceamentoLancamento.pesquisar);
				}
		);
	},
	
	this.mostrarPopUpBloqueio = function() {
		
		$("#dialog-bloqueio-matriz", _workspace).dialog({
			resizable: false,
			height:'auto',
			width:600,
			modal: true,
			buttons: {
				"Sim": function() {
					
					$.postJSON(
							contextPath + "/matrizLancamento/bloquearMatrizLancamento", 
							null,
							function(result) {
							
								balanceamentoLancamento.verificarBalanceamentosAlteradosPesquisar(balanceamentoLancamento.pesquisar);
								
								$("#dialog-bloqueio-matriz", _workspace).dialog("close");
								
							}, null
					);
					
				},
				"Não": function() {
					
					$("#bloquearBotoes", _workspace).val(true);
					
					$("#dialog-bloqueio-matriz", _workspace).dialog("close");
					
					balanceamentoLancamento.desbloquearMatrizLancamento(
						function() {
							balanceamentoLancamento.verificarBalanceamentosAlteradosPesquisar(balanceamentoLancamento.pesquisar);
						}
					);
				}
			},
			form: $("#dialog-bloqueio-matriz", _workspace).parents("form")			
		});
	},
	
	this.desbloquearMatrizLancamento = function(funcao) {

		$.postJSON(
				contextPath + "/matrizLancamento/desbloquearMatrizLancamentoPost",
				null,
				function(result) {
					
					if (funcao) {
						
						funcao();
					}
				}
		);
	},
	
	this.bloquearLink = function(idLink) {
		
		var link = $("#" + idLink, _workspace);
		link.addClass("linkDisabled");
		link.unbind("click");
		link.css("text-decoration", "none");
	},
	
	this.habilitarLink = function(idLink, funcao) {
		
		var link = $("#" + idLink, _workspace);
		link.removeClass("linkDisabled");
		link.unbind("click");
		link.bind("click", funcao);
		link.css("text-decoration", "");
	};
	
}

//@ sourceURL=balanceamentoLancamento.js  
