var balanceamentoRecolhimentoController = $.extend(true, {
	
	balanceamento : null,
	
	verificarBalanceamentosAlterados : function(funcao) {
		
		$.postJSON(
			contextPath + "/devolucao/balanceamentoMatriz/verificarBalanceamentosAlterados",
			null,
			function(result) {
				
				if (result == "true") {
					
					$("#dialog-confirm", balanceamentoRecolhimentoController.workspace).dialog({
						resizable: false,
						height:'auto',
						width:600,
						modal: true,
						buttons: {
							"Confirmar": function() {
								
								funcao();
								
								$(this).dialog("close");
							},
							"Cancelar": function() {
								
								$(this).dialog("close");
							}
						},
						form: $("#dialog-confirm", balanceamentoRecolhimentoController.workspace).parents("form")			
					});
					
				} else {
					
					funcao();
				}
			}
		);
	},

	confirmacaoConfiguracaoInicial : function(funcao) {
		
		$("#dialog-confirm-config-inicial", balanceamentoRecolhimentoController.workspace).dialog({
			resizable: false,
			height:'auto',
			width:600,
			modal: true,
			buttons: {
				"Confirmar": function() {
					
					funcao();
					
					$(this).dialog("close");
				},
				"Cancelar": function() {
					
					$(this).dialog("close");
				}
			},
			form: $("#dialog-confirm-config-inicial", balanceamentoRecolhimentoController.workspace).parents("form")			
		});
	},
	
	pesquisar : function() {

		balanceamentoRecolhimentoController.fecharGridBalanceamento();
		
		$.postJSON(
			contextPath + "/devolucao/balanceamentoMatriz/pesquisar", 
			balanceamentoRecolhimentoController.obterParametrosPesquisa(),
			function(result) {
				
				balanceamentoRecolhimentoController.montarResumoPeriodoBalanceamento(result);
				$('#utilizaSedeAtendida').val(result.utilizaSedeAtendida);
			},
			function() {
				balanceamentoRecolhimentoController.showResumo(false);
			}
		);
	},
	
	montarResumoPeriodoBalanceamento : function(result) {
				
		var rows = '<tr>';

		$.each(result.listaResumoPeriodoBalanceamento, function(index, resumo) {
			
			rows += '<td>';

			if (resumo.exibeDestaque) {

				rows += '<div class="box_resumo alert">';
				
			} else {

				rows += '<div class="box_resumo">';
			}
			
			rows += '<label>' + resumo.dataFormatada;
			rows += '<a href="javascript:;" onclick="balanceamentoRecolhimentoController.visualizarMatrizBalanceamentoPorDia(' + "'" + resumo.dataFormatada + "'" + ');" style="float: right;">';
			rows += '<img src="' + contextPath + '/images/ico_detalhes.png" width="15" height="15" border="0" title="Visualizar" />';
			rows += '</a>';
			rows += '</label>';
			rows += '<span class="span_1">Qtde. Títulos:</span>';
			rows += '<span class="span_2">' + resumo.qtdeTitulos + '</span>';
			
			if (resumo.excedeCapacidadeDistribuidor) {
				
				rows += '<span class="span_1">Qtde. Exempl.:</span>';
				rows += '<span name="qtdeExemplares" class="span_2 redLabel"';
				rows += 'title="A quantidade de exemplares excede a capacidade de manuseio ';
				rows += result.capacidadeRecolhimentoDistribuidor + ' do distribuidor">';
				rows += resumo.qtdeExemplaresFormatada + '</span>';
			
			} else {
			
				rows += '<span class="span_1">Qtde. Exempl.:</span>';	
				rows += '<span class="span_2">' + resumo.qtdeExemplaresFormatada + '</span>';
			}
			
			rows += '<span class="span_1">Qtde. Parciais:</span>';
			rows += '<span class="span_2">' + resumo.qtdeTitulosParciais + '</span>';	
			rows += '<span class="span_1">Peso Total:</span>';
			rows += '<span class="span_2">' + resumo.pesoTotalFormatado + '</span>';
			rows += '<span class="span_1">Valor Total:</span>';
			rows += '<span class="span_2">' + resumo.valorTotalFormatado + '</span>';
			rows += '</div>';
			rows += '</td>';
	    });	
	    
	    rows += "</tr>";

	    $("#tableResumoPeriodoBalanceamento", balanceamentoRecolhimentoController.workspace).empty();
	    
	    $("#tableResumoPeriodoBalanceamento", balanceamentoRecolhimentoController.workspace).append(rows);

	    $("span[name='qtdeExemplares']", balanceamentoRecolhimentoController.workspace).tooltip();
	    
	   
	    	
	    	balanceamentoRecolhimentoController.habilitarLinks();
	    	
	    	balanceamentoRecolhimentoController.habilitarCheckAll();
	    	
	    	$(".balanceamentoGrid", balanceamentoRecolhimentoController.workspace).flexOptions({
	    		disableSelect : false
	    	});
	   
	    
	    	balanceamentoRecolhimentoController.showResumo(true);
	    	 	
	},
	
	defineUtilizacaoSedeAtendida : function() {
		
		var utilizaSedeAtendida = $('#utilizaSedeAtendida').val() == 'true';
		
		$(".balanceamentoGrid", balanceamentoRecolhimentoController.workspace).flexToggleCol(10,utilizaSedeAtendida);
		$(".balanceamentoGrid", balanceamentoRecolhimentoController.workspace).flexToggleCol(11,utilizaSedeAtendida);
		
	},
	
	bloquearCheckAll : function() {
		
		$("#checkAllReprogramar", balanceamentoRecolhimentoController.workspace).disable();
	},
	
	habilitarCheckAll : function() {
		
		$("#checkAllReprogramar", balanceamentoRecolhimentoController.workspace).enable();
	},
	
	bloquearLinks : function() {
		
		balanceamentoRecolhimentoController.bloquearLink("linkConfirmar", balanceamentoRecolhimentoController.workspace);
		balanceamentoRecolhimentoController.bloquearLink("linkEditor", balanceamentoRecolhimentoController.workspace);
		balanceamentoRecolhimentoController.bloquearLink("linkValor", balanceamentoRecolhimentoController.workspace);
		balanceamentoRecolhimentoController.bloquearLink("linkSalvar", balanceamentoRecolhimentoController.workspace);
		balanceamentoRecolhimentoController.bloquearLink("linkConfiguracaoInicial", balanceamentoRecolhimentoController.workspace);
		balanceamentoRecolhimentoController.bloquearLink("linkReprogramar", balanceamentoRecolhimentoController.workspace);
	},
	
	habilitarLinks : function() {
		
		balanceamentoRecolhimentoController.habilitarLink("linkConfirmar", balanceamentoRecolhimentoController.obterConfirmacaoBalanceamento);
		balanceamentoRecolhimentoController.habilitarLink("linkEditor", function() { balanceamentoRecolhimentoController.verificarBalanceamentosAlterados(balanceamentoRecolhimentoController.balancearPorEditor); });
		balanceamentoRecolhimentoController.habilitarLink("linkValor", function() { balanceamentoRecolhimentoController.verificarBalanceamentosAlterados(balanceamentoRecolhimentoController.balancearPorValor); });
		balanceamentoRecolhimentoController.habilitarLink("linkSalvar", balanceamentoRecolhimentoController.salvar);
		balanceamentoRecolhimentoController.habilitarLink("linkConfiguracaoInicial", function() { balanceamentoRecolhimentoController.confirmacaoConfiguracaoInicial(balanceamentoRecolhimentoController.voltarConfiguracaoInicial); });
		balanceamentoRecolhimentoController.habilitarLink("linkReprogramar", balanceamentoRecolhimentoController.reprogramarSelecionados);
	},
	
	bloquearLink : function(idLink) {
		
		var link = $("#" + idLink, balanceamentoRecolhimentoController.workspace);
		link.addClass("linkDisabled");
		link.unbind("click");
		link.css("text-decoration", "none");
	},
	
	habilitarLink : function(idLink, funcao) {
		
		var link = $("#" + idLink, balanceamentoRecolhimentoController.workspace);
		link.removeClass("linkDisabled");
		link.unbind("click");
		link.bind("click", funcao);
		link.css("text-decoration", "");
	},
	
	visualizarMatrizBalanceamentoPorDia : function(data) {
		
		$("#dataBalanceamentoHidden", balanceamentoRecolhimentoController.workspace).val(data);
		
		$(".balanceamentoGrid", balanceamentoRecolhimentoController.workspace).flexOptions({
			url: contextPath + "/devolucao/balanceamentoMatriz/exibirMatrizFornecedor",
			onSuccess: balanceamentoRecolhimentoController.executarAposProcessamento,
			params: [
		         {name:'dataFormatada', value: data}
		    ],
		    newp: 1,
		});
		
		$(".balanceamentoGrid", balanceamentoRecolhimentoController.workspace).flexReload();
	},
	
	executarPreProcessamento : function(resultado) {
		
		balanceamentoRecolhimentoController.deselectCheckAll();
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			balanceamentoRecolhimentoController.fecharGridBalanceamento();

			return resultado;
		}
		
		if (resultado.rows == 0) {
			
			balanceamentoRecolhimentoController.fecharGridBalanceamento();
			
			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {
			
			var idProdutoEdicao = row.cell.idProdutoEdicao;
			var nomeProduto = row.cell.nomeProduto;
			
			row.cell.nomeProduto = balanceamentoRecolhimentoController.balanceamento.getColunaProduto(idProdutoEdicao, nomeProduto);
			row.cell.novaData = balanceamentoRecolhimentoController.gerarHTMLNovaData(row);
			row.cell.reprogramar = balanceamentoRecolhimentoController.gerarCheckReprogramar(row);
			row.cell.acao = balanceamentoRecolhimentoController.gerarBtnAcoes(row);
		});
			
		$(".grids", balanceamentoRecolhimentoController.workspace).show();
		
		$("#fieldsetGrids", balanceamentoRecolhimentoController.workspace).show();
		
		balanceamentoRecolhimentoController.defineUtilizacaoSedeAtendida();	 
		
		return resultado;
	},

	gerarBtnAcoes : function(row) {
		
		var idLancamento = row.cell.idLancamento;
		var btnExcluir;
		
		if (row.cell.bloqueioAlteracaoBalanceamento) {
			btnExcluir = '<img src="' + contextPath + '/images/ico_excluir.gif" border="0" disabled="disabled" class="linkDisabled" />';
		
		} else {
			btnExcluir = '<a href="javascript:;" class="btn_excluir" '+
		    'onclick="balanceamentoRecolhimentoController.excluirBalanceamento(' + idLancamento + ');">'+
		    '<img src="' + contextPath + '/images/ico_excluir.gif" border="0"  /></a>';
		}
		
		return btnExcluir;
	},
	
	gerarHTMLNovaData : function(row) {
		
		var retornoHTML;
			
		retornoHTML = '<div name="divNovaData" id="divNovaData' + row.id + '" style="width: 100%;">';
		
		retornoHTML += '<input type="hidden" name="hiddenBloqueioAlteracaoBalanceamento"'
				    + 	     ' value="' + row.cell.bloqueioAlteracaoBalanceamento + '" />';
		
		retornoHTML += '<input type="hidden" name="hiddenIdFornecedor"'
		    		+ 	     ' value="' + row.cell.idFornecedor + '" />';
		
		retornoHTML += '<input type="text" name="novaData"'
					 + 	     ' value="' + row.cell.novaData + '"'
					 + 	     ' style="width:55px; margin-right:5px; float:left;margin-top: -3px;" />';
		
		retornoHTML += '<div class="bt_atualizarIco" style="margin-top: -9px;" title="Reprogramar">'
			  		 + '  <a href="javascript:;">&nbsp;</a>'
			  		 + '</div>';
			  		 
		retornoHTML += '</div>';
		
		return retornoHTML;
	},
	
	gerarCheckReprogramar : function(row) {
		
		var retornoHTML;
		
		if (row.cell.bloqueioAlteracaoBalanceamento) {
			
			retornoHTML = '<input type="checkbox" id="ch' + row.id + '"'
	   		   			+       ' name="balanceamentoRecolhimentoController.checkReprogramar"'
	   		   			+       ' value="' + row.id + '" disabled="disabled"  />';
		} else {
			
			retornoHTML = '<input type="checkbox" id="checkReprogramar' + row.id + '"'
	   		   			+       ' name="checkReprogramar"'
	   		   			+       ' value="' + row.id + '"'
	   		   			+       ' onclick="balanceamentoRecolhimentoController.checarBalanceamento(\'' + row.id + '\');" />';
		}
		
		return retornoHTML;
	},
	
	executarAposProcessamento : function() {
		
		$("input[name='novaData']", balanceamentoRecolhimentoController.workspace).datepicker({
			dateFormat: 'dd/mm/yy'
		});
		
		$("input[name='novaData']", balanceamentoRecolhimentoController.workspace).mask("99/99/9999");
		
		balanceamentoRecolhimentoController.criarDivsNovaData();
	},
			
	criarDivsNovaData : function() {
		
		$("div[name='divNovaData']", balanceamentoRecolhimentoController.workspace).each(function(index, div) {
			
			balanceamentoRecolhimentoController.verificarBloqueioData(div);
		});
	},
	
	verificarBloqueioData : function(divNovaData) {
		
		var idLinha = $(divNovaData, balanceamentoRecolhimentoController.workspace).attr("id").replace("divNovaData", "");
		
		var divAtualizar = $(divNovaData, balanceamentoRecolhimentoController.workspace).find("div");
		var inputNovaData = $(divNovaData, balanceamentoRecolhimentoController.workspace).find("input[name='novaData']");
		var linkAtualizar = $(divNovaData, balanceamentoRecolhimentoController.workspace).find("a");
		
		var inputCheck = $("#checkReprogramar" + idLinha, balanceamentoRecolhimentoController.workspace);
		
		var bloqueioAlteracaoBalanceamento = $(divNovaData, balanceamentoRecolhimentoController.workspace).find("input[name='hiddenBloqueioAlteracaoBalanceamento']").val();
		
		if (inputCheck.attr("checked") == "checked"
				|| eval(bloqueioAlteracaoBalanceamento)) {	
		
			$(inputNovaData, balanceamentoRecolhimentoController.workspace).disable();
			
			$(divAtualizar, balanceamentoRecolhimentoController.workspace).addClass("linkDisabled");
			
			$(linkAtualizar, balanceamentoRecolhimentoController.workspace).attr("style", "cursor: default;");
			
			$(linkAtualizar, balanceamentoRecolhimentoController.workspace).unbind("click");
		
		} else {
			
			$(inputNovaData, balanceamentoRecolhimentoController.workspace).enable();
			
			$(divAtualizar, balanceamentoRecolhimentoController.workspace).removeClass("linkDisabled");
			
			$(linkAtualizar, balanceamentoRecolhimentoController.workspace).attr("style", "");
			
			$(linkAtualizar, balanceamentoRecolhimentoController.workspace).unbind("click");
			$(linkAtualizar, balanceamentoRecolhimentoController.workspace).bind("click",
					  	 		  function() { balanceamentoRecolhimentoController.reprogramarRecolhimentoUnico(idLinha); });
		}
	},
	
	checarBalanceamento : function(idRow) {
		
		$("input[name='checkReprogramar']", balanceamentoRecolhimentoController.workspace).each(function() {
		
			var checado = this.checked;
			
			clickLineFlexigrid(this, checado);
			
			if (!checado) {
				
				balanceamentoRecolhimentoController.deselectCheckAll();
			}
		});
		
		var divNovaData = $("#divNovaData" + idRow, balanceamentoRecolhimentoController.workspace);
		
		balanceamentoRecolhimentoController.verificarBloqueioData(divNovaData);
	},
	
	selecionarTodos : function(input) {
		
		checkAll(input, "checkReprogramar");
		
		$("input[name='checkReprogramar']", balanceamentoRecolhimentoController.workspace).each(function() {
		
			var checado = this.checked;
			
			clickLineFlexigrid(this, checado);
		});
		
		balanceamentoRecolhimentoController.criarDivsNovaData();
	},

	obterParametrosPesquisa : function() {

		var parametros = new Array();
		
		var anoSemanaNumero = $("#numeroSemana", balanceamentoRecolhimentoController.workspace).val();
		var numeroSemana = '';
		if(anoSemanaNumero && anoSemanaNumero.length>=5){
			numeroSemana = anoSemanaNumero.substr(4);
		}

		parametros.push({name:'numeroSemana', value:numeroSemana });
		
		parametros.push({name:'dataPesquisa', value: $("#dataPesquisa", balanceamentoRecolhimentoController.workspace).val()});
		
		$("input[name='checkGroupFornecedores']:checked", balanceamentoRecolhimentoController.workspace).each(function(i) {
			
			parametros.push({name:'listaIdsFornecedores', value: $(this).val()});
		});

		return parametros;
	},

	carregarDataSemana : function() {

		var numeroSemana = $("#numeroSemana", balanceamentoRecolhimentoController.workspace).val();

		if (!numeroSemana || numeroSemana.length<5) {

			return;
		}
		
//		var dataBase=new Date();
		
		var anoBase = numeroSemana.slice(0,4);
		var nmSemana = numeroSemana.substr(4);
		
		var dataBase = this.w2date(anoBase,nmSemana,0);
		dataBase = dataBase.getDate()+"/"+(dataBase.getMonth()+1)+"/"+dataBase.getFullYear();
		
		var data = [
				{name: 'numeroSemana', value: nmSemana},
				{name: 'dataBase', value: dataBase}
			];
		
		$.getJSON(
			contextPath + "/cadastro/distribuidor/obterDataDaSemana", 
			data,
			function(result) {

				if (result) {
					
					$("#dataPesquisa", balanceamentoRecolhimentoController.workspace).val(result);
				}
			}
		);
	},

	w2date :function(year, wn, dayNb){
	    var j10 = new Date( year,0,10,12,0,0),
	        j4 = new Date( year,0,4,12,0,0),
	        mon1 = j4.getTime() - j10.getDay() * 86400000;
	    return new Date(mon1 + ((wn - 1)  * 7  + dayNb) * 86400000);
	},
	
	carregarDiaSemana : function() {

		var dataPesquisa = $("#dataPesquisa", balanceamentoRecolhimentoController.workspace).val();

		if (!dataPesquisa) {

			return;
		}
		
		
		var dataBase = $("#dataPesquisa", balanceamentoRecolhimentoController.workspace).val();
		var data = [
				{
					name: 'data', value:dataBase 
				}
			];
		
		$.getJSON(
			contextPath + "/cadastro/distribuidor/obterNumeroSemana", 
			data,
			function(result) {

				if (result) {

					$("#numeroSemana", balanceamentoRecolhimentoController.workspace).val((dataBase.substr(6))+(result.int));
				}
			}
		);
	},

	carregarDadosPesquisa : function() {

		var dataPesquisa = $.format.date(new Date(), "dd/MM/yyyy");

	  	$("#dataPesquisa", balanceamentoRecolhimentoController.workspace).val(dataPesquisa);
		
	  	balanceamentoRecolhimentoController.carregarDiaSemana();
	},

	inicializar : function(balanceamento) {
		
		this.balanceamento = balanceamento;
		
		balanceamentoRecolhimentoController.iniciarGrid();
		
		balanceamentoRecolhimentoController.inicializarDatas();
		
		$("input[name='numeroSemana']", balanceamentoRecolhimentoController.workspace).numeric();

		balanceamentoRecolhimentoController.carregarDadosPesquisa();
	},
	
	inicializarDatas : function() {
		
		$("#dataPesquisa", balanceamentoRecolhimentoController.workspace).datepicker({
			showOn : "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly : true,
			dateFormat: 'dd/mm/yy',
			defaultDate: new Date()
		});

		$("#novaDataRecolhimento", balanceamentoRecolhimentoController.workspace).datepicker({
			showOn : "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly : true,
			dateFormat: 'dd/mm/yy',
		});

		$("#dataPesquisa", balanceamentoRecolhimentoController.workspace).mask("99/99/9999");
		
		$("#novaDataRecolhimento", balanceamentoRecolhimentoController.workspace).mask("99/99/9999");
	},
	
	iniciarGrid : function() {
		
		$(".balanceamentoGrid", balanceamentoRecolhimentoController.workspace).flexigrid({
			preProcess: balanceamentoRecolhimentoController.executarPreProcessamento,
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigoProduto',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 35,
				sortable : true,
				align : 'left'
			}, {
				display : 'Capa R$',
				name : 'precoVenda',
				width : 40,
				sortable : true,
				align : 'right'
			}, {
				display : 'Desc R$',
				name : 'precoDesconto',
				width : 40,
				sortable : true,
				align : 'right'
			}, {
				display : 'Fornecedor',
				name : 'nomeFornecedor',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Editor',
				name : 'nomeEditor',
				width : 70,
				sortable : true,
				align : 'left',
			}, {
				display : 'Parcial',
				name : 'parcial',
				width : 36,
				sortable : true,
				align : 'center'
			}, {
				display : 'Brinde',
				name : 'brinde',
				width : 36,
				sortable : true,
				align : 'center'
			}, {
				display : 'Lçto',
				name : 'dataLancamento',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Sede',
				name : 'encalheSede',
				width : 60,
				sortable : true,
				align : 'center',
				hide : true
			}, {
				display : 'Atendida',
				name : 'encalheAtendida',
				width : 60,
				sortable : true,
				align : 'center',
				hide : true
			}, {
				display : 'Exemplar',
				name : 'encalhe',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Total R$',
				name : 'valorTotal',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : 'Rcto',
				name : 'dataRecolhimento',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Nova Data',
				name : 'novaData',
				width : 105,
				sortable : false,
				align : 'center'
			},{
				display : 'Reprog.',
				name : 'reprogramar',
				width : 33,
				sortable : false,
				align : 'center'
			}, {
				display: 'Ação',
				name : 'acao',
				width : 30,
				sortable : false,
				align : 'center'
			}],
			sortname : "nomeProduto",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 1066,
			height : 180
		});
	},
	
	confirmarBalanceamento : function() {
		var param = serializeArrayToPost('datasConfirmadas', this.balanceamento.obterDatasMarcadasConfirmacao());
		$.postJSON(
			contextPath + "/devolucao/balanceamentoMatriz/confirmar",
			param,
			function(result) {
		
				balanceamentoRecolhimentoController.fecharGridBalanceamento();
				
				$("#dialog-confirm-balanceamento", balanceamentoRecolhimentoController.workspace).dialog("close");

				if (result) {
				   
					var tipoMensagem = result.tipoMensagem;
				   	var listaMensagens = result.listaMensagens;
				   
				   	if (tipoMensagem && listaMensagens) {
					   
				    	exibirMensagem(tipoMensagem, listaMensagens);
			       	}
        	   	}
			},
			null,
			true,
			"dialog-confirmar"
		);
	},
	
	balancearPorEditor : function() {
		
		balanceamentoRecolhimentoController.fecharGridBalanceamento();
		
		$.postJSON(
			contextPath + "/devolucao/balanceamentoMatriz/balancearPorEditor",
			null,
			function(result) {
				
				balanceamentoRecolhimentoController.montarResumoPeriodoBalanceamento(result);

				exibirMensagem(
					'SUCCESS', 
					[ 'Balanceamento concluído com sucesso.' ]
				);
			},
			function(result) {
				
				balanceamentoRecolhimentoController.showResumo(false);
			}
		);
	},
	
	balancearPorValor : function() {
		
		balanceamentoRecolhimentoController.fecharGridBalanceamento();
		
		$.postJSON(
			contextPath + "/devolucao/balanceamentoMatriz/balancearPorValor",
			null,
			function(result) {
				
				balanceamentoRecolhimentoController.montarResumoPeriodoBalanceamento(result);
				
				exibirMensagem(
					'SUCCESS', 
					[ 'Balanceamento concluído com sucesso.' ]
				);
			},
			function() {
				
				balanceamentoRecolhimentoController.showResumo(false);
			}
		);
	},
	
	salvar : function() {
		
		$.postJSON(
			contextPath + "/devolucao/balanceamentoMatriz/salvar"
		);
	},
	
	exibirMatrizFornecedor : function() {
		
		$("#dataBalanceamentoHidden", balanceamentoRecolhimentoController.workspace).val("");
		
		$(".balanceamentoGrid", balanceamentoRecolhimentoController.workspace).flexOptions({
			url: contextPath + "/devolucao/balanceamentoMatriz/exibirMatrizFornecedor",
			preProcess: balanceamentoRecolhimentoController.executarPreProcessamento,
			onSuccess: balanceamentoRecolhimentoController.executarAposProcessamento,
			params: null,
		    newp: 1,
		});
		
		$(".balanceamentoGrid", balanceamentoRecolhimentoController.workspace).flexReload();
	},
	
	voltarConfiguracaoInicial : function() {
		
		balanceamentoRecolhimentoController.fecharGridBalanceamento();
		
		$.postJSON(
			contextPath + "/devolucao/balanceamentoMatriz/voltarConfiguracaoOriginal",
			null,
			function(result) {
				
				if (result){
				    
					balanceamentoRecolhimentoController.montarResumoPeriodoBalanceamento(result);	
					
					exibirMensagem('SUCCESS',[ 'Configuração reiniciada com sucesso.' ]);
				}
			},
			function(result) {
				
				if(result && result.mensagem && result.mensagem.tratarValidacao == false){

					balanceamentoRecolhimentoController.showResumo(false);
				}
			}
		);
	},
	
	reprogramarSelecionados : function() {
		
		$("#dialogReprogramarBalanceamento", balanceamentoRecolhimentoController.workspace).dialog({
			resizable: false,
			height:'auto',
			width:300,
			modal: true,
			buttons: {
				"Confirmar": function() {
					
					balanceamentoRecolhimentoController.processarReprogramacao();
				},
				"Cancelar": function() {
					
					$(this).dialog("close");
				}
			},
			beforeClose: function() {
				
				$("#novaDataRecolhimento", balanceamentoRecolhimentoController.workspace).val("");
				
				clearMessageDialogTimeout();
			},
			form: $("#dialogReprogramarBalanceamento", balanceamentoRecolhimentoController.workspace).parents("form")
		});
	},
	
	processarReprogramacao : function() {
		var linhasDaGrid = $('.balanceamentoGrid tr', balanceamentoRecolhimentoController.workspace);
		
		var listaProdutoRecolhimento = new Array();
		
		var checkAllSelected = balanceamentoRecolhimentoController.verifyCheckAll();
			
		$.each(linhasDaGrid, function(index, value) {
			
			var linha = $(value);
			
			var colunaCheck = linha.find("td")[16];
			
			var inputCheck = $(colunaCheck).find("div").find('input[name="checkReprogramar"]');
			
			var checked = inputCheck.attr("checked") == "checked";
			
			if (checked) {
				
				var idLinha = linha.attr("id");
				
				var idLancamento = idLinha.replace("row", "");
				
				var novaData = balanceamentoRecolhimentoController.obterValorInputColuna(linha, 15, "novaData");
				var idFornecedor = balanceamentoRecolhimentoController.obterValorInputColuna(linha, 15, "hiddenIdFornecedor");
				
				listaProdutoRecolhimento.push({idFornecedor:idFornecedor,idLancamento:idLancamento,novaData:novaData});
			}
		});
		
		var novaData = $("#novaDataRecolhimento", balanceamentoRecolhimentoController.workspace).val();
		
		var dataAntiga = $("#dataBalanceamentoHidden", balanceamentoRecolhimentoController.workspace).val();
		
		var param = {selecionarTodos:checkAllSelected,
				novaDataFormatada:novaData,
				dataAntigaFormatada:dataAntiga};
		
		param = serializeArrayToPost("listaProdutoRecolhimento", listaProdutoRecolhimento, param);
		
		$.postJSON(contextPath + "/devolucao/balanceamentoMatriz/reprogramarSelecionados",
				   param,
				   function(result) {
				   		
						$("#dialogReprogramarBalanceamento", balanceamentoRecolhimentoController.workspace).dialog("close");
			
						balanceamentoRecolhimentoController.atualizarResumoBalanceamento();
				   		
						balanceamentoRecolhimentoController.deselectCheckAll();
				   },
				   null,
				   true
		);
	},

	reprogramarRecolhimentoUnico : function(idRow) {
		
		var linhasDaGrid = $('.balanceamentoGrid tr', balanceamentoRecolhimentoController.workspace);
		
		var linhaSelecionada = null;
		
		$.each(linhasDaGrid, function(index, value) {
			
			var linha = $(value);
			
			var idLinha = linha.attr("id");
			
			var idLancamento = idLinha.replace("row", "");
			
			if (idLancamento == idRow) {
				
				var novaData = balanceamentoRecolhimentoController.obterValorInputColuna(linha, 15, "novaData");
				var idFornecedor = balanceamentoRecolhimentoController.obterValorInputColuna(linha, 15, "hiddenIdFornecedor");
				
				linhaSelecionada = {idFornecedor:idFornecedor,idLancamento:idLancamento,novaData:novaData};
			}
		});
		
		var dataAntiga = $("#dataBalanceamentoHidden", balanceamentoRecolhimentoController.workspace).val();
		
		var param = {dataAntigaFormatada:dataAntiga};
		if(linhaSelecionada){
			param =  serializeObjectToPost('produtoRecolhimento', linhaSelecionada,param);
		}
		
		$.postJSON(contextPath + "/devolucao/balanceamentoMatriz/reprogramarRecolhimentoUnico",param,
				   function(result) {
				   
				   		balanceamentoRecolhimentoController.atualizarResumoBalanceamento();
				   },
				   function() {
					   
					   var divNovaData = $("#divNovaData" + idRow, balanceamentoRecolhimentoController.workspace);
					   
					   var inputNovaData = $(divNovaData, balanceamentoRecolhimentoController.workspace).find("input[name='novaData']");
					   
					   $(inputNovaData).val(dataAntiga);
				   }
		);
	},
	
	fecharGridBalanceamento : function() {
		
		$(".grids", balanceamentoRecolhimentoController.workspace).hide();
		
		balanceamentoRecolhimentoController.deselectCheckAll();
	},
	
	atualizarResumoBalanceamento : function() {
		
		$.postJSON(contextPath + "/devolucao/balanceamentoMatriz/atualizarResumoBalanceamento",
				   null,
				   function(result) {
			
						balanceamentoRecolhimentoController.montarResumoPeriodoBalanceamento(result);
				   		
				   		$(".balanceamentoGrid", balanceamentoRecolhimentoController.workspace).flexReload();
				   },
				   function() {
				   
					   balanceamentoRecolhimentoController.fecharGridBalanceamento();
					   
					   balanceamentoRecolhimentoController.showResumo(false);
				   }
		);
	},
				
	obterValorInputColuna : function(linha, posicao, inputName) {
		
		var coluna = linha.find("td")[posicao];
		
		var input = $(coluna).find("div").find('input[name="' + inputName + '"]');
		
		return $(input).val();
	},
	
	verifyCheckAll : function() {
		return ($("#checkAllReprogramar", balanceamentoRecolhimentoController.workspace).attr("checked") == "checked");
	},
	
	deselectCheckAll : function() {
		
		$("#checkAllReprogramar", balanceamentoRecolhimentoController.workspace).attr("checked", false);
	}, 
	
	obterConfirmacaoBalanceamento : function() {
		
		$.postJSON(
			contextPath + "/devolucao/balanceamentoMatriz/obterAgrupamentoDiarioBalanceamento", 
			null,
			function(result) {
				this.balanceamento.popularConfirmacaoBalanceamento(result,balanceamentoRecolhimentoController.workspace);
				balanceamentoRecolhimentoController.abrirPopupConfirmarBalanceamento();
			},
			function() {
				$("#dialog-confirm-balanceamento", balanceamentoRecolhimentoController.workspace).hide();
			}
		);
	},
	
	abrirPopupConfirmarBalanceamento : function() {
		
		$( "#dialog-confirm-balanceamento", balanceamentoRecolhimentoController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:300,
			modal: true,
			buttons: [
			    {
			    	id: "dialogConfirmarBtnConfirmar",
			    	text: "Confirmar",
			    	click: function() {
					
			    		balanceamentoRecolhimentoController.confirmarBalanceamento();
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
				clearMessageDialogTimeout("dialog-confirmar");
		    },
		    form: $("#dialog-confirm-balanceamento", balanceamentoRecolhimentoController.workspace).parents("form")
		});
	},
	
	excluirBalanceamento : function(idLancamento) {
		
		var params = {"idLancamento":idLancamento};
		
		$.postJSON(contextPath + "/devolucao/balanceamentoMatriz/excluirBalanceamento", params, function() {
			balanceamentoRecolhimentoController.atualizarResumoBalanceamento();	   		
			balanceamentoRecolhimentoController.deselectCheckAll();
		});
		
	},
	
	showResumo : function(ativo) {
		
		if(ativo===false) {
			$(".resumoPeriodo", balanceamentoRecolhimentoController.workspace).hide();
			$('.fieldFiltro').css('margin-top','0px');
		} else {
			$(".resumoPeriodo", balanceamentoRecolhimentoController.workspace).show();
			$('.fieldFiltro').css('margin-top','27px');
		}
		
	}

}, BaseController);


//@ sourceURL=balanceamentoRecolhimento.js
