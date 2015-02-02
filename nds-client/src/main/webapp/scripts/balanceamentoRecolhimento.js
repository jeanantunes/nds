var balanceamentoRecolhimentoController = $.extend(true, {
	
	balanceamento : null,

	checkAllGrid : false,
	
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
		
		
		dataHolder.clearAction('matrizRecolhimentoDataHolder', 
			function(){
				balanceamentoRecolhimentoController.fecharGridBalanceamento();
				
				$.postJSON(
					contextPath + "/devolucao/balanceamentoMatriz/pesquisar", 
					balanceamentoRecolhimentoController.obterParametrosPesquisa(),
					function(result) {
						
						if(result.produtosRecolhimentoDeOutraSemana
								&& result.produtosRecolhimentoDeOutraSemana.length > 0 ){
							
							balanceamentoRecolhimentoController.mostrarProdutoDeOutraSemanaDeRecolhimento(result);
						}
						else{
							
							balanceamentoRecolhimentoController.tratarRetornoPesquisa(result);
						}
					},
					function() {
						balanceamentoRecolhimentoController.showResumo(false);
					}
				);
			}
		);
	},
	
	pesquisarLoad : function(resultado) {
		
		
		dataHolder.clearAction('matrizRecolhimentoDataHolder', 
			function(){
				balanceamentoRecolhimentoController.fecharGridBalanceamento();
				
				$.postJSON(
					contextPath + "/devolucao/balanceamentoMatriz/pesquisar", 
					balanceamentoRecolhimentoController.obterParametrosPesquisa(),
					function(result) {
						

					  balanceamentoRecolhimentoController.tratarRetornoPesquisa(result);
					},
					function() {
						balanceamentoRecolhimentoController.showResumo(false);
					}
				);
				if(resultado!=null){
				  exibirMensagem(
					resultado.tipoMensagem, 
					resultado.listaMensagens
				  );
				}
			}
		);
	},
	
	tratarRetornoPesquisa : function(result) {
		
		balanceamentoRecolhimentoController.montarResumoPeriodoBalanceamento(result);
		
		$('#utilizaSedeAtendida').val(result.utilizaSedeAtendida);
		
		var dataPesquisa = 
			$("#dataPesquisa", balanceamentoRecolhimentoController.workspace).val();
		
		balanceamentoRecolhimentoController.escolherDataParaVisualizacaoGrid(dataPesquisa);
		
		balanceamentoRecolhimentoController.visualizarMatrizBalanceamentoPorDia(null);
		
		balanceamentoRecolhimentoController.verificarBloqueioBotoes();
	},
	
	verificarBloqueioBotoes: function() {
		
		$.getJSON(
			contextPath + "/devolucao/balanceamentoMatriz/isTodasDatasConfirmadas", 
			null,
			function(result) {
				
				balanceamentoRecolhimentoController.habilitarLinks();
				
				if (result) {

					balanceamentoRecolhimentoController.bloquearLinks();

					balanceamentoRecolhimentoController.habilitarLink(
						"linkConfirmar", balanceamentoRecolhimentoController.obterConfirmacaoBalanceamento
					);
					
					balanceamentoRecolhimentoController.habilitarLink(
						"linkReabrirMatriz", balanceamentoRecolhimentoController.obterDatasConfirmadasParaReaberturaPost
					);
					
					balanceamentoRecolhimentoController.habilitarLink(
							"linkBloquearDia", balanceamentoRecolhimentoController.obterDatasConfirmadasParaReaberturaCadeadoPost
						);
				}
				
				if (eval($("#bloquearBotoes", balanceamentoRecolhimentoController.workspace).val())) {
					
					var links = $("a[isEdicao='true']", balanceamentoRecolhimentoController.workspace);
					
					$.each(links, function(index, link) {
						
						if (link.id) {
						
							balanceamentoRecolhimentoController.bloquearLink(link.id);
						}
					});
				}
				
				bloquearItensEdicao(balanceamentoRecolhimentoController.workspace);
			}
		);
	},
	
	exibirMatrizFornecedor : function() {
	
		$("#dataBalanceamentoHidden", balanceamentoRecolhimentoController.workspace).val(null);
		
		balanceamentoRecolhimentoController.visualizarMatrizBalanceamentoPorDia(null);
	},
	
	escolherDataParaVisualizacaoGrid : function(dataPesquisa) {
		
		var dataSelecionada = null;
		
		var datasResumo = $("#dataResumo", balanceamentoRecolhimentoController.workspace);
		
		$.each(datasResumo, function(index, dataResumo) {
			
			if ($(dataResumo).html() == dataPesquisa) {
				
				var linkVisualizacao = $(dataResumo).parent().find("a")[0];
				
				if (linkVisualizacao) {
				
					dataSelecionada = $(dataResumo).html();
				}	
			}
		});
		
		$("#dataBalanceamentoHidden", balanceamentoRecolhimentoController.workspace).val(dataSelecionada);
	},
	
	mostrarProdutoDeOutraSemanaDeRecolhimento : function(result) {
		
		var mensagemDialog ="<ul style='margin-left: 20px;'>" ;
		
		$.each(result.produtosRecolhimentoDeOutraSemana, function(index, item) {
			
			mensagemDialog += "<li style='padding-bottom: 10px;'>" + item.codigoProduto +" - "+ item.nomeProduto + " - " + item.numeroEdicao + "</li>";
	    });	
		
		mensagemDialog += "</ul>";
		
		$("#descDialogProdutosDeOutraSemana", balanceamentoRecolhimentoController.workspace).html(mensagemDialog);
		
		$("#dialogProdutosDeOutraSemana", balanceamentoRecolhimentoController.workspace).dialog({
			resizable: false,
			height:'auto',
			width:600,
			modal: true,
			buttons: {
				"Fechar": function() {
					
					balanceamentoRecolhimentoController.tratarRetornoPesquisa(result);
					
					$(this).dialog("close");
				},
			},
			
			form: $("#dialogProdutosDeOutraSemana", balanceamentoRecolhimentoController.workspace).parents("form")
		});
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
			
			rows += '<label id="labelData'+resumo.dataFormatada.replace(/\//g, "")+'" class="labelDataLancamento">' + '<spam id="dataResumo" align=>' + resumo.dataFormatada ;
			
			if (resumo.statusResumo == "EM_BALANCEAMENTO_RECOLHIMENTO") {
			 	rows+= '<img src="' + contextPath + '/images/ico_bloqueado.gif" width="15" height="15" border="0" title="" align="left" />';  
			}else if (resumo.statusResumo == "BALANCEADO_RECOLHIMENTO") {
			 	rows+= '<img src="' + contextPath + '/images/ico_check.gif" width="15" height="15" border="0" title="" align="left" />';   
			//} else if (resumo.statusResumo == "EM_RECOLHIMENTO") {
			 	//rows+= '<img src="' + contextPath + '/images/ico_salvar.gif" width="15" height="15" border="0" title="" align="left" />';   
			} else{
			}
			
			if (!resumo.bloquearVisualizacao) {
			
				rows += '<a href="javascript:;" onclick="balanceamentoRecolhimentoController.visualizarMatrizBalanceamentoPorDia(' + "'" + resumo.dataFormatada + "'" + ');" style="float: right;">';
				rows += '<img src="' + contextPath + '/images/ico_detalhes.png" width="15" height="15" border="0" title="Visualizar" />';
				rows += '</a>';
			}
			
			rows += '</spam></label>';
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
			rows += '<span class="span_1">Peso Total (kg):</span>';
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
		balanceamentoRecolhimentoController.bloquearLink("linkReabrirMatriz", balanceamentoRecolhimentoController.workspace);
		balanceamentoRecolhimentoController.bloquearLink("linkBloquearDia", balanceamentoRecolhimentoController.workspace);
	},
	
	habilitarLinks : function() {
		
		balanceamentoRecolhimentoController.habilitarLink("linkConfirmar", balanceamentoRecolhimentoController.obterConfirmacaoBalanceamento);
		balanceamentoRecolhimentoController.habilitarLink("linkEditor", function() { balanceamentoRecolhimentoController.verificarBalanceamentosAlterados(balanceamentoRecolhimentoController.balancearPorEditor); });
		balanceamentoRecolhimentoController.habilitarLink("linkValor", function() { balanceamentoRecolhimentoController.verificarBalanceamentosAlterados(balanceamentoRecolhimentoController.balancearPorValor); });
		balanceamentoRecolhimentoController.habilitarLink("linkSalvar", balanceamentoRecolhimentoController.salvar);
		balanceamentoRecolhimentoController.habilitarLink("linkConfiguracaoInicial", function() { balanceamentoRecolhimentoController.confirmacaoConfiguracaoInicial(balanceamentoRecolhimentoController.voltarConfiguracaoInicial); });
		balanceamentoRecolhimentoController.habilitarLink("linkReprogramar", balanceamentoRecolhimentoController.reprogramarSelecionados);
		balanceamentoRecolhimentoController.habilitarLink("linkReabrirMatriz", balanceamentoRecolhimentoController.obterDatasConfirmadasParaReaberturaPost);
		balanceamentoRecolhimentoController.habilitarLink("linkBloquearDia", balanceamentoRecolhimentoController.obterDatasConfirmadasParaReaberturaCadeadoPost);
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
		
		//alert(data);
		var dataPesquisa = $("#dataPesquisa", balanceamentoRecolhimentoController.workspace).val();
		//alert(dataPesquisa);
		if (data!=null){
			$("#dataPesquisa", balanceamentoRecolhimentoController.workspace).val(data);
			$.each($('.labelDataLancamento'), function(k, v) {
			 $(v).css('font-size', '11px').css('color', '#01649E');
		    });
		  $("#labelData"+ data.replace(/\//g, ""), balanceamentoRecolhimentoController.workspace).css("font-size", "11px");
		  $("#labelData"+ data.replace(/\//g, ""), balanceamentoRecolhimentoController.workspace).css("color", "red");
		} else {
				if($("#dataPesquisa", balanceamentoRecolhimentoController.workspace).val()) {
					$("#labelData"+ $("#dataResumo", balanceamentoRecolhimentoController.workspace).val().replace(/\//g, ""), balanceamentoRecolhimentoController.workspace).css("font-size", "11px");
					$("#labelData"+ $("#dataResumo", balanceamentoRecolhimentoController.workspace).val().replace(/\//g, ""), balanceamentoRecolhimentoController.workspace).css("color", "red");
				}
			}
		
		dataHolder.clearAction('matrizRecolhimentoDataHolder',function(){
			
			balanceamentoRecolhimentoController.deselectCheckAll();
			
			$(".hidden_buttons", balanceamentoRecolhimentoController.workspace).show();
			
			if (data == null) {
				
				data = $("#dataBalanceamentoHidden", balanceamentoRecolhimentoController.workspace).val();
			}
			
			$("#dataBalanceamentoHidden", balanceamentoRecolhimentoController.workspace).val(data);
			
			$(".balanceamentoGrid", balanceamentoRecolhimentoController.workspace).flexOptions({
				url: contextPath + "/devolucao/balanceamentoMatriz/exibirMatrizFornecedor",
				preProcess: balanceamentoRecolhimentoController.executarPreProcessamento,
				onSuccess: balanceamentoRecolhimentoController.executarAposProcessamento,
				params: [
			         {name:'dataFormatada', value: data}
			    ],
			    newp: 1,
			});
			
			$(".balanceamentoGrid", balanceamentoRecolhimentoController.workspace).flexReload();
			
		});
	},
	
	executarPreProcessamento : function(resultado) {
		
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
		});
			
		$(".grids", balanceamentoRecolhimentoController.workspace).show();
		
		$("#fieldsetGrids", balanceamentoRecolhimentoController.workspace).show();
		
		balanceamentoRecolhimentoController.defineUtilizacaoSedeAtendida();	 
		
		return resultado;
	},
	
	gerarHTMLNovaData : function(row) {
		
		var retornoHTML;
			
		retornoHTML = '<div name="divNovaData" id="divNovaData' + row.id + '" style="width: 100%;">';
		
		retornoHTML += '<input type="hidden" name="hiddenBloqueioAlteracaoBalanceamento"'
				    + 	     ' value="' + row.cell.bloqueioAlteracaoBalanceamento + '" />';
		
		retornoHTML += '<input type="hidden" name="hiddenIdFornecedor"'
		    		+ 	     ' value="' + row.cell.idFornecedor + '" />';
		
		retornoHTML += '<input isEdicao="true" type="text" name="novaData"'
					 + 	     ' value="' + row.cell.novaData + '"'
					 + 	     ' style="width:55px; margin-right:5px; float:left;margin-top: -3px;" />';
		
		retornoHTML += '<div class="bt_atualizarIco" style="margin-top: -9px;" title="Reprogramar">'
			  		 + '  <a isEdicao="true" href="javascript:;">&nbsp;</a>'
			  		 + '</div>';
			  		 
		retornoHTML += '</div>';
		
		return retornoHTML;
	},
	
	gerarCheckReprogramar : function(row) {
		
		var retornoHTML;
		
		if (row.cell.bloqueioAlteracaoBalanceamento) {
			
			retornoHTML = '<input type="checkbox" id="ch' + row.id + '"'
	   		   			+       ' name="balanceamentoRecolhimentoController.checkReprogramar" isEdicao="true" '
	   		   			+       ' value="' + row.id + '" disabled="disabled"  />';
		} else {
			
			if(row.cell.replicar == 'true' || $("#checkAllReprogramar", balanceamentoRecolhimentoController.workspace).is(":checked")){
				retornoHTML = balanceamentoRecolhimentoController.getElementCheckedOrUnchecked(row.id, true);
			}else{
				retornoHTML = balanceamentoRecolhimentoController.getElementCheckedOrUnchecked(row.id, false);
			}
		}
		return retornoHTML;
	},
	
	getElementCheckedOrUnchecked : function (id, checked){
		
		var retornoHTML = '';
		
		if(checked){
			retornoHTML = '<input type="checkbox" id="checkReprogramar' + id + '"'
			+       ' name="checkReprogramar" isEdicao="true" '
			+       ' value="' + id + '"'
			+       ' onclick="balanceamentoRecolhimentoController.checarBalanceamento(\'' + id + '\');" checked />';
		}else{
			retornoHTML = '<input type="checkbox" id="checkReprogramar' + id + '"'
			+       ' name="checkReprogramar" isEdicao="true" '
			+       ' value="' + id + '"'
			+       ' onclick="balanceamentoRecolhimentoController.checarBalanceamento(\'' + id + '\');" />';
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
		
		var bloquearBotoes = $("#bloquearBotoes", balanceamentoRecolhimentoController.workspace).val();
		
		if (inputCheck.attr("checked") == "checked"
				|| eval(bloqueioAlteracaoBalanceamento)
				|| eval(bloquearBotoes)) {	
		
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
					  	 		  function() { balanceamentoRecolhimentoController.reprogramarRecolhimentoUnico(idLinha, false ); });
		}
	},
	
	checarBalanceamento : function(idRow) {
		
		var checkReprogramar = $("#checkReprogramar" + idRow, balanceamentoRecolhimentoController.workspace)[0];
		
		var divNovaData = $("#divNovaData" + idRow, balanceamentoRecolhimentoController.workspace);
		
		var checado = checkReprogramar.checked;

		dataHolder.hold('matrizRecolhimentoDataHolder', idRow , 'checado', checado);

		clickLineFlexigrid(checkReprogramar, checado);
		
		if (!checado) {
			
			balanceamentoRecolhimentoController.deselectCheckAll();
		} 		
		
		balanceamentoRecolhimentoController.verificarBloqueioData(divNovaData);
	},
	
	selecionarTodos : function(input) {
		
		if(input.checked == false){
			dataHolder.clearAction('matrizRecolhimentoDataHolder');
		}
		
		checkAll(input, "checkReprogramar");
		
		$("input[name='checkReprogramar']", balanceamentoRecolhimentoController.workspace).each(function() {
		
			var checado = this.checked;
			
			clickLineFlexigrid(this, checado);
		});
		
		balanceamentoRecolhimentoController.criarDivsNovaData();
		
		if(input.checked == false)
			return;
		
		var parametros = new Array();
		
		parametros.push({name:'fieldValue', value:"true"});
		parametros.push({name:'actionKey', value:'matrizRecolhimentoDataHolder'});
		parametros.push({name:'fieldKey', value:'checado'});
		
		$.postJSON(
				contextPath + "/devolucao/balanceamentoMatriz/atribuirCheckedParaTodosItens",
				parametros
		);
	},

	obterParametrosPesquisa : function() {

		var parametros = new Array();
		
		var anoNumeroSemana = $("#numeroSemana", balanceamentoRecolhimentoController.workspace).val();

		parametros.push({name:'anoNumeroSemana', value:anoNumeroSemana });
		
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
		
		var anoBase = numeroSemana.slice(0,4);
		var nmSemana = numeroSemana.substr(4);
		
		var data = [
				{name: 'numeroSemana', value: nmSemana},
				{name: 'anoBase', value: anoBase}
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

					$("#numeroSemana", balanceamentoRecolhimentoController.workspace).val(result.int);
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
				display : 'PEB',
				name : 'peb',
				width : 20,
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
				sortable : true,
				align : 'center'
			},{
				display : 'Reprog.',
				name : 'reprogramar',
				width : 33,
				sortable : false,
				align : 'center'
			},{
				display : 'Status',
				name : 'statusLancamento',
				width : 50,
				sortable : true,
				align : 'left'
			} ],
			sortname : "nomeProduto",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 1024,
			height : 'auto'
		});
	},
	
	confirmarBalanceamento : function() {
		var param = serializeArrayToPost('datasConfirmadas', this.balanceamento.obterDatasMarcadasConfirmacaoRecolhimento());
		$.postJSON(
			contextPath + "/devolucao/balanceamentoMatriz/confirmar",
			param,
			function(result) {
		
				balanceamentoRecolhimentoController.fecharGridBalanceamento();
				
				$("#dialog-confirm-balanceamento", balanceamentoRecolhimentoController.workspace).dialog("close");

				
				balanceamentoRecolhimentoController.pesquisarLoad(result);
				
				balanceamentoRecolhimentoController.verificarBloqueioBotoes();
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
				
				if(result.produtosRecolhimentoDeOutraSemana
						&& result.produtosRecolhimentoDeOutraSemana.length > 0 ){
					
					balanceamentoRecolhimentoController.mostrarProdutoDeOutraSemanaDeRecolhimento(result);
				}
				else{
					
					balanceamentoRecolhimentoController.montarResumoPeriodoBalanceamento(result);

					exibirMensagem(
						'SUCCESS', 
						[ 'Balanceamento concluído com sucesso.' ]
					);
				}
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
				
				if(result.produtosRecolhimentoDeOutraSemana
						&& result.produtosRecolhimentoDeOutraSemana.length > 0 ){
					
					balanceamentoRecolhimentoController.mostrarProdutoDeOutraSemanaDeRecolhimento(result);
				}
				else{
					
					balanceamentoRecolhimentoController.montarResumoPeriodoBalanceamento(result);
					
					exibirMensagem(
						'SUCCESS', 
						[ 'Balanceamento concluído com sucesso.' ]
					);
				}
			},
			function() {
				
				balanceamentoRecolhimentoController.showResumo(false);
			}
		);
	},
	
	salvar : function() {
		
      dataPesquisa = $("#dataPesquisa", balanceamentoRecolhimentoController.workspace).val();
     
		
       var params = new Array();
		
	   params.push({name:'dataPesquisa', value: dataPesquisa});
		
		
		$.postJSON(
			contextPath + "/devolucao/balanceamentoMatriz/salvar",
			params
		);
		
		//balanceamentoRecolhimentoController.pesquisarLoad(null);
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
				
				dataHolder.clearAction('matrizRecolhimentoDataHolder');
			},
			function(result) {
				
				if(result && result.mensagem && result.mensagem.tratarValidacao == false){

					balanceamentoRecolhimentoController.showResumo(false);
				}
			}
		);
		balanceamentoRecolhimentoController.pesquisarLoad(null);
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
		
		var novaData = $("#novaDataRecolhimento", balanceamentoRecolhimentoController.workspace).val();
		
		var dataAntiga = $("#dataBalanceamentoHidden", balanceamentoRecolhimentoController.workspace).val();
		
		var parametros =
			balanceamentoRecolhimentoController.obterParametrosValidacaoData(novaData, dataAntiga);
		
		$.postJSON(
			contextPath + "/devolucao/balanceamentoMatriz/verificarDataReprogramacao",
			parametros,
			function(result) {
				
				var parametrosReprogramarSelecionados =
					balanceamentoRecolhimentoController.obterParametrosReprogramarSelecionados(novaData, dataAntiga);
				
				if (result == "DATA_VALIDA") {
					
					balanceamentoRecolhimentoController.efetivarReprogramacaoRecolhimentosSelecionados(parametrosReprogramarSelecionados);
					
				} else {
					
					var mensagemValidacaoReprogramacao;
					
					if (result == "DATA_FORA_SEMANA") {
						
						mensagemValidacaoReprogramacao = "A data está fora da semana de recolhimento. Você deseja continuar?";
					
					} else if (result == "DATA_DIA_CONFIRMADO") {
						
						mensagemValidacaoReprogramacao = "A data escolhida é uma data já confirmada. Você deseja continuar?";
					}
					
					$("#mensagemValidacaoReprogramacao", balanceamentoRecolhimentoController.workspace)
						.html(mensagemValidacaoReprogramacao);
					
					$("#alertAceite", balanceamentoRecolhimentoController.workspace).dialog({
						resizable: false,
						height:'auto',
						width:400,
						modal: true,
						buttons: {
							"Confirmar": function() {
								
								balanceamentoRecolhimentoController.efetivarReprogramacaoRecolhimentosSelecionados(parametrosReprogramarSelecionados);
								
								$(this).dialog("close");
							},
							"Cancelar": function() {
								
								$(this).dialog("close");
							}
						},
						form: $("#alertAceite", balanceamentoRecolhimentoController.workspace).parents("form")			
					});
				}
		   }
		);
	},
	
	efetivarReprogramacaoRecolhimentosSelecionados : function(parametros) {
		
		$.postJSON(
			contextPath + "/devolucao/balanceamentoMatriz/reprogramarSelecionados",
			parametros,
			function(result) {
				   		
				$("#dialogReprogramarBalanceamento", balanceamentoRecolhimentoController.workspace).dialog("close");
	
				balanceamentoRecolhimentoController.atualizarResumoBalanceamento();
		   		
				balanceamentoRecolhimentoController.deselectCheckAll();
			},
			null,
			true
		);
	},
	
	obterParametrosReprogramarSelecionados : function(novaData, dataAntiga) {
		
		var checkAllSelected = balanceamentoRecolhimentoController.verifyCheckAll();
		
		var param = {selecionarTodos:checkAllSelected,
				novaDataFormatada:novaData,
				dataAntigaFormatada:dataAntiga};
		
		return param;
	},
	
	reprogramarRecolhimentoUnico : function(idRow, boolean) {
		
		var linhasDaGrid = $('.balanceamentoGrid tr', balanceamentoRecolhimentoController.workspace);
		
		var linhaSelecionada = null;
		
		$.each(linhasDaGrid, function(index, value) {
			
			var linha = $(value);
			
			var idLinha = linha.attr("id");
			
			var idLancamento = idLinha.replace("row", "");
			
			if (idLancamento == idRow) {
				
				var novaData = balanceamentoRecolhimentoController.obterValorInputColuna(linha, 16, "novaData");
				
				var idFornecedor = balanceamentoRecolhimentoController.obterValorInputColuna(linha, 16, "hiddenIdFornecedor");
				
				var novaDataSplit = novaData.split('/');
					
				novaDataFormatoDate = new Date(novaDataSplit[2], novaDataSplit[1] - 1, novaDataSplit[0]);

				var dataAntiga = $("#dataBalanceamentoHidden", balanceamentoRecolhimentoController.workspace).val();
				
				var parametros =
					balanceamentoRecolhimentoController.obterParametrosValidacaoData(novaData, dataAntiga);
				
				$.postJSON(
					contextPath + "/devolucao/balanceamentoMatriz/verificarDataReprogramacao",
					parametros,
					function(result) {
						
						linhaSelecionada = {
							idFornecedor: idFornecedor,
							idLancamento: idLancamento,
							novaData: novaData
						};
						
						var parametros = {dataAntigaFormatada: dataAntiga};
						
						if (linhaSelecionada) {
							
							parametros =  
								serializeObjectToPost(
									'produtoRecolhimento', linhaSelecionada, parametros);
						}
						
						if (result == "DATA_VALIDA") {
							
							balanceamentoRecolhimentoController.efetivarReprogramacaoRecolhimentoUnico(parametros, idRow, dataAntiga);
							
						} else {
							
							var mensagemValidacaoReprogramacao;
							
							if (result == "DATA_FORA_SEMANA") {
								
								mensagemValidacaoReprogramacao = "A data está fora da semana de recolhimento. Você deseja continuar?";
							
							} else if (result == "DATA_DIA_CONFIRMADO") {
								
								mensagemValidacaoReprogramacao = "A data escolhida é uma data já confirmada. Você deseja continuar?";
							}
							
							$("#mensagemValidacaoReprogramacao", balanceamentoRecolhimentoController.workspace)
								.html(mensagemValidacaoReprogramacao);
							
							$("#alertAceite", balanceamentoRecolhimentoController.workspace).dialog({
								resizable: false,
								height:'auto',
								width:400,
								modal: true,
								buttons: {
									"Confirmar": function() {
										
										balanceamentoRecolhimentoController.efetivarReprogramacaoRecolhimentoUnico(parametros, idRow, dataAntiga);
										
										$(this).dialog("close");
									},
									"Cancelar": function() {
										
										balanceamentoRecolhimentoController.recolocacaoDataAntigaReprogramarRecolhimentoUnico(idRow, dataAntiga);
										
										$(this).dialog("close");
									}
								},
								form: $("#alertAceite", balanceamentoRecolhimentoController.workspace).parents("form")			
							});
						}
				   }
				);
			}
		});
	},
	
	efetivarReprogramacaoRecolhimentoUnico : function(parametros, idRow, dataAntiga) {
		
		$.postJSON(
			contextPath + "/devolucao/balanceamentoMatriz/reprogramarRecolhimentoUnico",
			parametros,
			function(result) {
				balanceamentoRecolhimentoController.atualizarResumoBalanceamento();
			},
            function() {
                balanceamentoRecolhimentoController.recolocacaoDataAntigaReprogramarRecolhimentoUnico(idRow, dataAntiga);
            }
		);
	},
	
	obterParametrosValidacaoData : function(novaData, dataAntiga) {
		
		var parametros = new Array();
		
		var anoNumeroSemana = 
			$("#numeroSemana", balanceamentoRecolhimentoController.workspace).val();
		
		parametros.push({name:'anoNumeroSemana', value:anoNumeroSemana });
		parametros.push({name:'novaDataBalanceamentoFormatada', value:novaData });
		
		return parametros;
	},
	
	recolocacaoDataAntigaReprogramarRecolhimentoUnico : function(idRow, dataAntiga)
	{
		var divNovaData = $("#divNovaData" + idRow, balanceamentoRecolhimentoController.workspace);
		   
		var inputNovaData = $(divNovaData, balanceamentoRecolhimentoController.workspace).find("input[name='novaData']");
		   
		$(inputNovaData).val(dataAntiga);
	},
	
	fecharGridBalanceamento : function() {
		$(".hidden_buttons", balanceamentoRecolhimentoController.workspace).hide();
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
	
	obterDatasConfirmadasParaReaberturaPost: function() {
		
		$.postJSON(
			contextPath + "/devolucao/balanceamentoMatriz/obterDatasConfirmadasReaberturaPost", 
			null,
			function(result) {
				
				if (result.length == 0) {
					
					balanceamentoRecolhimentoController.bloquearLink("linkReabrirMatriz", balanceamentoRecolhimentoController.workspace);
					
				} else {
				
					balanceamentoRecolhimentoController.popularPopupReaberturaMatrizes(result);

					balanceamentoRecolhimentoController.abrirPopupReabrirMatriz();
				}
			}
		);		
	},
	
    obterDatasConfirmadasParaReaberturaCadeadoPost: function() {
    	
		
		$.postJSON(
			contextPath + "/devolucao/balanceamentoMatriz/obterDatasConfirmadasReaberturaCadeadoPost", 
			null,
			function(result) {
				
				if (result==null && result.length == 0) {
					
					balanceamentoRecolhimentoController.bloquearLink("linkBloquearDia", balanceamentoRecolhimentoController.workspace);
					
				} else {
				

					balanceamentoRecolhimentoController.popularPopupCadeadoMatrizes(result);
					
					//balanceamentoRecolhimentoController.popularPopupReaberturaMatrizes(result);
					
					balanceamentoRecolhimentoController.abrirPopupCadeadoMatriz();
					
					//balanceamentoRecolhimentoController.abrirPopupReabrirMatriz();
				}
			}
		);		
	},

	validarLancamentoParaReabertura: function() {
	 $.postJSON(contextPath + "/devolucao/balanceamentoMatriz/validarLancamentoParaReabertura", null,function(result) {});
	},
	
	abrirPopupReabrirMatriz : function() {
		
		
		var exec = false;
	
		$( "#dialog-reabrir-matriz", balanceamentoRecolhimentoController.workspace).dialog({
			resizable: false,
			height:'auto',
			width:300,
			modal: true,
			buttons: [
			    {
			    	id: "dialogReaberturaBtnConfirmar",
			    	text: "Reabrir",
			    	click: function() {
					
			    		exec =true;
			    		balanceamentoRecolhimentoController.reabrirMatriz();

			    	}
			    },
			    {
			    	id: "dialogReaberturaBtnCancelar",
			    	text: "Cancelar",
			    	click: function() {
			    
			    		$(this).dialog("close");
			    	}
				}
			],
			beforeClose: function() {
				if(exec){
				  exec =false;
				  balanceamentoRecolhimentoController.validarLancamentoParaReabertura();
				}
				$("input[name='checkMatrizReabertura']:checked", balanceamentoRecolhimentoController.workspace).attr("checked", false);
		    },
		    form: $("#form-reabrir-matriz", balanceamentoRecolhimentoController.workspace)
		});
	},

	abrirPopupCadeadoMatriz : function() {
		
		var exec = false;
		
	
		$( "#dialog-cadeado-matriz", balanceamentoRecolhimentoController.workspace).dialog({
			resizable: false,
			height:'auto',
			width:300,
			modal: true,
			buttons: [
			    {
			    	id: "dialogReaberturaBtnConfirmar",
			    	text: "Bloquear",
			    	click: function() {
					
			    		exec =true;
			    		balanceamentoRecolhimentoController.cadeadoMatriz();

			    	}
			    },
			    {
			    	id: "dialogReaberturaBtnCancelar",
			    	text: "Cancelar",
			    	click: function() {
			    
			    		$(this).dialog("close");
			    	}
				}
			],
			beforeClose: function() {
				if(exec){
				  exec =false;
				  balanceamentoRecolhimentoController.validarLancamentoParaReabertura();
				}
				$("input[name='checkMatrizReabertura']:checked", balanceamentoRecolhimentoController.workspace).attr("checked", false);
		    },
		    form: $("#form-cadeado-matriz", balanceamentoRecolhimentoController.workspace)
		});
	},

	popularPopupCadeadoMatrizes: function(result) {
		
		var conteudo = '';
		
		$(result).each(function(index, value) {
			conteudo += '<tr class="class_linha_1"><td name=dataReabertura_'+index+' >';
			conteudo += value;
			conteudo += '</td>';
			conteudo += '<td align="center"><input name=checkMatrizReabertura type="checkbox" value="' + value + '" /></td>';
			conteudo += '</tr>';
		});
		
		$("#tableCadeadoMatrizConfirmada", balanceamentoRecolhimentoController.workspace).html(conteudo);
	},
	
	popularPopupReaberturaMatrizes: function(result) {
		
		var conteudo = '';
		
		$(result).each(function(index, value) {
			conteudo += '<tr class="class_linha_1"><td name=dataReabertura_'+index+' >';
			conteudo += value;
			conteudo += '</td>';
			conteudo += '<td align="center"><input name=checkMatrizReabertura type="checkbox" value="' + value + '" /></td>';
			conteudo += '</tr>';
		});
		
		$("#tableReaberturaMatrizConfirmada", balanceamentoRecolhimentoController.workspace).html(conteudo);
	},
	
	reabrirMatriz: function() {


		var params = new Array();
		
		$("input[name='checkMatrizReabertura']:checked").each(function(index, value) {
			
			params.push({name: 'datasReabertura['+index+']', value: value.value});
		});
		
		$.postJSON(
			contextPath + "/devolucao/balanceamentoMatriz/reabrirMatriz",
			params,
			function(result) {
				
				$("#dialog-reabrir-matriz").dialog("close");
				
				balanceamentoRecolhimentoController.pesquisarLoad(result);
				
				balanceamentoRecolhimentoController.verificarBloqueioBotoes();
				
			}
		);
	},
	
	cadeadoMatriz: function() {

		var params = new Array();
		
		$("input[name='checkMatrizReabertura']:checked").each(function(index, value) {
			
			params.push({name: 'datasReabertura['+index+']', value: value.value});
		});
		
		$.postJSON(
			contextPath + "/devolucao/balanceamentoMatriz/cadeadoMatriz",
			params,
			function(result) {
				
				$("#dialog-cadeado-matriz").dialog("close");
				
				balanceamentoRecolhimentoController.pesquisarLoad(result);
				
				balanceamentoRecolhimentoController.verificarBloqueioBotoes();
				
			}
		);
	},

	obterConfirmacaoBalanceamento : function() {

		
		$.postJSON(
			contextPath + "/devolucao/balanceamentoMatriz/obterAgrupamentoDiarioBalanceamento", 
			null,
			function(result) {
				this.balanceamento.popularConfirmacaoBalanceamentoRecolhimento(result,balanceamentoRecolhimentoController.workspace);
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
	
	showResumo : function(ativo) {
		
		if(ativo===false) {
			$(".resumoPeriodo", balanceamentoRecolhimentoController.workspace).hide();
			$('.fieldFiltro').css('margin-top','0px');
		} else {
			$(".resumoPeriodo", balanceamentoRecolhimentoController.workspace).show();
			$('.fieldFiltro').css('margin-top','27px');
		}
		
	},
	
	verificarBloqueioMatrizRecolhimento : function() {
		
		$("#bloquearBotoes", balanceamentoRecolhimentoController.workspace).val(false);
		
		dataHolder.clearAction('matrizRecolhimentoDataHolder');
		
		$.postJSON(
				contextPath + "/devolucao/balanceamentoMatriz/verificarBloqueioMatrizRecolhimentoPost", 
				null, 
				function(result) {
					
					balanceamentoRecolhimentoController.mostrarPopUpBloqueio();
				},
				function() {
					
					$("#bloquearBotoes", balanceamentoRecolhimentoController.workspace).val(true);
					
					balanceamentoRecolhimentoController.verificarBalanceamentosAlterados(balanceamentoRecolhimentoController.pesquisar);
				}
		);
	},
	
	mostrarPopUpBloqueio : function() {
		
		$("#dialog-bloqueio-matriz", balanceamentoRecolhimentoController.workspace).dialog({
			resizable: false,
			height:'auto',
			width:600,
			modal: true,
			buttons: {
				"Sim": function() {
					
					$.postJSON(
							contextPath + "/devolucao/balanceamentoMatriz/bloquearMatrizRecolhimento", 
							null,
							function(result) {
							
								balanceamentoRecolhimentoController.verificarBalanceamentosAlterados(balanceamentoRecolhimentoController.pesquisar);
								
								$("#dialog-bloqueio-matriz", balanceamentoRecolhimentoController.workspace).dialog("close");
								
							}, null
					);
					
				},
				"Não": function() {
					
					$.postJSON(
							contextPath + "/devolucao/balanceamentoMatriz/desbloquearMatrizRecolhimentoPost", 
							null,
							function(result) {
							
								$("#bloquearBotoes", balanceamentoRecolhimentoController.workspace).val(true);
								
								$("#dialog-bloqueio-matriz", balanceamentoRecolhimentoController.workspace).dialog("close");
								
								balanceamentoRecolhimentoController.desbloquearMatrizRecolhimento(
									function() {
										balanceamentoRecolhimentoController.verificarBalanceamentosAlterados(balanceamentoRecolhimentoController.pesquisar);
									}
								);
								
							}, null
					);
				}
			},
			form: $("#dialog-bloqueio-matriz", balanceamentoRecolhimentoController.workspace).parents("form")			
		});
	},
	
	desbloquearMatrizRecolhimento : function(funcao) {

		$.postJSON(
				contextPath + "/devolucao/balanceamentoMatriz/desbloquearMatrizRecolhimentoPost",
				null,
				function(result) {
					
					if (funcao) {
						
						funcao();
					}
				}
		);
	},

}, BaseController);


//@ sourceURL=balanceamentoRecolhimento.js
