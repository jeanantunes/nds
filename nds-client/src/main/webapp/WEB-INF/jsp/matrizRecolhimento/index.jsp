	<head>
		
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
		
		<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/balanceamento.js"></script>
	
		<script type="text/javascript">

			var pathTela = "${pageContext.request.contextPath}";

			var balanceamento = new Balanceamento(pathTela, "balanceamento");
	
			function verificarBalanceamentosAlterados(funcao) {
				
				$.postJSON(
					"${pageContext.request.contextPath}/devolucao/balanceamentoMatriz/verificarBalanceamentosAlterados",
					null,
					function(result) {
						
						if (result == "true") {
							
							$("#dialog-confirm").dialog({
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
								}
							});
							
						} else {
							
							funcao();
						}
					}
				);
			}
		
			function pesquisar() {
	
				fecharGridBalanceamento();
				
				$.postJSON(
					"${pageContext.request.contextPath}/devolucao/balanceamentoMatriz/pesquisar", 
					obterParametrosPesquisa(),
					function(result) {
						
						montarResumoPeriodoBalanceamento(result);
					},
					function() {
	
						$("#resumoPeriodo").hide();
					}
				);
			}
			
			function montarResumoPeriodoBalanceamento(result) {
				
				var rows = '<tr>';
	
				$.each(result.listaResumoPeriodoBalanceamento, function(index, resumo) {
					
					rows += '<td>';
	
					if (resumo.exibeDestaque) {
	
						rows += '<div class="box_resumo alert">';
						
					} else {
	
						rows += '<div class="box_resumo">';
					}
					
					rows += '<label>' + resumo.dataFormatada;
					rows += '<a href="javascript:;" onclick="visualizarMatrizBalanceamentoPorDia(' + "'" + resumo.dataFormatada + "'" + ');" style="float: right;">';
					rows += '<img src="' + contextPath + '/images/ico_detalhes.png" width="15" height="15" border="0" title="Visualizar" />';
					rows += '</a>';
					rows += '</label>';
					rows += '<span class="span_1">Qtde. Títulos:</span>';
					rows += '<span class="span_2">' + resumo.qtdeTitulos + '</span>';
					
					if (resumo.excedeCapacidadeDistribuidor) {
						
						rows += '<span class="span_1">Qtde. Exempl.:</span>';
						rows += '<span name="qtdeExemplares" class="span_2 redLabel"'
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
	
			    $("#tableResumoPeriodoBalanceamento").empty();
			    
			    $("#tableResumoPeriodoBalanceamento").append(rows);
	
			    $("span[name='qtdeExemplares']").tooltip();
			    
			    var matrizFechada = result.matrizFechada;
			    
			    if (matrizFechada) {
			    	
			    	bloquearLinks();
			    	
			    	bloquearCheckAll();
			    	
			    	$(".balanceamentoGrid").flexOptions({
			    		disableSelect : true
			    	});
			    	
			    } else {
			    	
			    	habilitarLinks();
			    	
			    	habilitarCheckAll();
			    	
			    	$(".balanceamentoGrid").flexOptions({
			    		disableSelect : false
			    	});
			    }
			    
			    $("#resumoPeriodo").show();
			}
			
			function bloquearCheckAll() {
				
				$("#checkAllReprogramar").disable();
			}
			
			function habilitarCheckAll() {
				
				$("#checkAllReprogramar").enable();
			}
			
			function bloquearLinks() {
				
				bloquearLink("linkConfirmar");
				bloquearLink("linkEditor");
				bloquearLink("linkValor");
				bloquearLink("linkSalvar");
				bloquearLink("linkConfiguracaoInicial");
				bloquearLink("linkReprogramar");
			}
			
			function habilitarLinks() {
				
				habilitarLink("linkConfirmar", obterConfirmacaoBalanceamento);
				habilitarLink("linkEditor", function() { verificarBalanceamentosAlterados(balancearPorEditor); });
				habilitarLink("linkValor", function() { verificarBalanceamentosAlterados(balancearPorValor); });
				habilitarLink("linkSalvar", salvar);
				habilitarLink("linkConfiguracaoInicial", function() { verificarBalanceamentosAlterados(voltarConfiguracaoInicial); });
				habilitarLink("linkReprogramar", reprogramarSelecionados);
			}
			
			function bloquearLink(idLink) {
				
				var link = $("#" + idLink);
				link.addClass("linkDisabled");
				link.unbind("click");
				link.css("text-decoration", "none");
			}
			
			function habilitarLink(idLink, funcao) {
				
				var link = $("#" + idLink);
				link.removeClass("linkDisabled");
				link.unbind("click");
				link.bind("click", funcao);
				link.css("text-decoration", "");
			}
			
			function visualizarMatrizBalanceamentoPorDia(data) {
				
				$("#dataBalanceamentoHidden").val(data);
				
				$(".balanceamentoGrid").flexOptions({
					url: "${pageContext.request.contextPath}/devolucao/balanceamentoMatriz/exibirMatrizFornecedor",
					onSuccess: executarAposProcessamento,
					params: [
				         {name:'dataFormatada', value: data}
				    ],
				    newp: 1,
				});
				
				$(".balanceamentoGrid").flexReload();
			}
			
			function executarPreProcessamento(resultado) {
				
				deselectCheckAll();
				
				if (resultado.mensagens) {
	
					exibirMensagem(
						resultado.mensagens.tipoMensagem, 
						resultado.mensagens.listaMensagens
					);
					
					fecharGridBalanceamento();
	
					return resultado;
				}
				
				if (resultado.rows == 0) {
					
					fecharGridBalanceamento();
					
					return resultado;
				}
				
				$.each(resultado.rows, function(index, row) {

					var idProdutoEdicao = row.cell.idProdutoEdicao;
					var nomeProduto = row.cell.nomeProduto;
					
					row.cell.nomeProduto = balanceamento.getColunaProduto(idProdutoEdicao, nomeProduto);
					row.cell.sequencia = gerarInputSequencia(row);
					row.cell.novaData = gerarHTMLNovaData(row);
					row.cell.reprogramar = gerarCheckReprogramar(row);
				});
					
				$(".grids").show();
				
				$("#fieldsetGrids").show();
				
				return resultado;
			}

			function gerarInputSequencia(row) {
				
				var retornoHTML;
				
				if (row.cell.bloqueioMatrizFechada) {
					
					retornoHTML = '<input type="text" id="sequencia' + row.id + '"'
							    + 	    ' value="' + row.cell.sequencia + '"'
							    +	    ' style="width: 30px;" disabled="disabled"'
							    +	    ' name="sequencia" maxlength="4" />';
				} else {
					
					retornoHTML = '<input type="text" id="sequencia' + row.id + '"'
							    + 	    ' value="' + row.cell.sequencia + '"'
							    +	    ' style="width: 30px;"'
							    +	    ' name="sequencia" maxlength="4" />';
				}
				
				return retornoHTML;
			}
			
			function gerarHTMLNovaData(row) {
				
				var retornoHTML;
					
				retornoHTML = '<div name="divNovaData" id="divNovaData' + row.id + '" style="width: 100%;">';
				
				retornoHTML += '<input type="hidden" name="hiddenBloqueioMatrizFechada"'
						    + 	     ' value="' + row.cell.bloqueioMatrizFechada + '" />';
						     
		     	retornoHTML += '<input type="hidden" name="hiddenBloqueioDataRecolhimento"'
						     + 	     ' value="' + row.cell.bloqueioDataRecolhimento + '" />';
					
				retornoHTML += '<input type="text" name="novaData"'
							 + 	     ' value="' + row.cell.novaData + '"'
							 + 	     ' style="width:65px; margin-right:5px; float:left;" />';
				
				retornoHTML += '<div class="bt_atualizarIco" title="Reprogramar">'
					  		 + '  <a href="javascript:;">&nbsp;</a>'
					  		 + '</div>';
					  		 
				retornoHTML += '</div>';
				
				return retornoHTML;
			}
			
			function gerarCheckReprogramar(row) {
				
				var retornoHTML;
				
				if (row.cell.bloqueioMatrizFechada) {
					
					retornoHTML = '<input type="checkbox" id="ch' + row.id + '"'
			   		   			+       ' name="checkReprogramar"'
			   		   			+       ' value="' + row.id + '" disabled="disabled" />';
				} else {
					
					retornoHTML = '<input type="checkbox" id="checkReprogramar' + row.id + '"'
			   		   			+       ' name="checkReprogramar"'
			   		   			+       ' value="' + row.id + '"'
			   		   			+       ' onclick="checarBalanceamento(\'' + row.id + '\');" />';
				}
				
				return retornoHTML;
			}
			
			function executarAposProcessamento() {
				
				$("input[name='novaData']").datepicker({
					dateFormat: 'dd/mm/yy'
				});
				
				$("input[name='novaData']").mask("99/99/9999");
				
				$("input[name='sequencia']").numeric(false);
				
				criarDivsNovaData();
			}
					
			function criarDivsNovaData() {
				
				$("div[name='divNovaData']").each(function(index, div) {
					
					verificarBloqueioData(div);
				});
			}
			
			function verificarBloqueioData(divNovaData) {
				
				var idLinha = $(divNovaData).attr("id").replace("divNovaData", "");
				
				var divAtualizar = $(divNovaData).find("div");
				var inputNovaData = $(divNovaData).find("input[name='novaData']");
				var linkAtualizar = $(divNovaData).find("a");
				
				var inputCheck = $("#checkReprogramar" + idLinha);
				
				var hiddenBloqueioMatrizFechada = $(divNovaData).find("input[name='hiddenBloqueioMatrizFechada']").val();
				var hiddenBloqueioDataRecolhimento = $(divNovaData).find("input[name='hiddenBloqueioDataRecolhimento']").val();
				
				if (inputCheck.attr("checked") == "checked"
						|| eval(hiddenBloqueioMatrizFechada) || eval(hiddenBloqueioDataRecolhimento)) {
				
					$(inputNovaData).disable();
					
					$(divAtualizar).addClass("linkDisabled");
					
					$(linkAtualizar).attr("style", "cursor: default;");
					
					$(linkAtualizar).unbind("click");
				
				} else {
					
					$(inputNovaData).enable();
					
					$(divAtualizar).removeClass("linkDisabled");
					
					$(linkAtualizar).attr("style", "");
					
					$(linkAtualizar).unbind("click");
					$(linkAtualizar).bind("click",
							  	 		  function() { reprogramarRecolhimentoUnico(idLinha); });
				}
			}
			
			function checarBalanceamento(idRow) {
				
				$("input[name='checkReprogramar']").each(function() {
				
					var checado = this.checked;
					
					clickLineFlexigrid(this, checado);
					
					if (!checado) {
						
						deselectCheckAll();
					}
				});
				
				var divNovaData = $("#divNovaData" + idRow);
				
				verificarBloqueioData(divNovaData);
			}
			
			function selecionarTodos(input) {
				
				checkAll(input, "checkReprogramar");
				
				$("input[name='checkReprogramar']").each(function() {
				
					var checado = this.checked;
					
					clickLineFlexigrid(this, checado);
				});
				
				criarDivsNovaData();
			}
		
			function obterParametrosPesquisa() {
	
				var parametros = new Array();
	
				parametros.push({name:'numeroSemana', value: $("#numeroSemana").val()});
				
				parametros.push({name:'dataPesquisa', value: $("#dataPesquisa").val()});
				
				$("input[name='checkGroupFornecedores']:checked").each(function(i) {
					
					parametros.push({name:'listaIdsFornecedores', value: $(this).val()});
				});
	
				return parametros;
			}
		
			function carregarDataSemana() {
	
				var numeroSemana = $("#numeroSemana").val();
	
				if (!numeroSemana) {
	
					return;
				}
				
				var data = [
	   				{
	   					name: 'numeroSemana', value: numeroSemana
	   				}
	   			];
				
				$.getJSON(
					"${pageContext.request.contextPath}/cadastro/distribuidor/obterDataDaSemana", 
					data,
					function(result) {
	
						if (result) {
							
							$("#dataPesquisa").val(result);
						}
					}
				);
			}
		
			function carregarDiaSemana() {
	
				var dataPesquisa = $("#dataPesquisa").val();
	
				if (!dataPesquisa) {
	
					return;
				}
				
				var data = [
	   				{
	   					name: 'data', value: $("#dataPesquisa").val()
	   				}
	   			];
				
				$.getJSON(
					"${pageContext.request.contextPath}/cadastro/distribuidor/obterNumeroSemana", 
					data,
					function(result) {
	
						if (result) {
	
							$("#numeroSemana").val(result.int);
						}
					}
				);
			}
		
			function carregarDadosPesquisa() {
	
				var dataPesquisa = $.format.date(new Date(), "dd/MM/yyyy");
	
			  	$("#dataPesquisa").val(dataPesquisa);
				
			  	carregarDiaSemana();
			}
		
			function inicializar() {
				
				iniciarGrid();
				
				inicializarDatas();
				
				$("input[name='numeroSemana']").numeric();
	
				carregarDadosPesquisa();
			}
			
			function inicializarDatas() {
				
				$("#dataPesquisa").datepicker({
					showOn : "button",
					buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
					buttonImageOnly : true,
					dateFormat: 'dd/mm/yy',
					defaultDate: new Date()
				});
	
				$("#novaDataRecolhimento").datepicker({
					showOn : "button",
					buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
					buttonImageOnly : true,
					dateFormat: 'dd/mm/yy',
				});
	
				$("#dataPesquisa").mask("99/99/9999");
				
				$("#novaDataRecolhimento").mask("99/99/9999");
			}
			
			function iniciarGrid() {
				
				$(".balanceamentoGrid").flexigrid({
					preProcess: executarPreProcessamento,
					dataType : 'json',
					colModel : [ {
						display : 'SM',
						name : 'sequencia',
						width : 35,
						sortable : true,
						align : 'left'
					}, {
						display : 'Código',
						name : 'codigoProduto',
						width : 40,
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
						display : 'Preço Venda R$',
						name : 'precoVenda',
						width : 85,
						sortable : true,
						align : 'right'
					}, {
						display : 'Preço Desconto R$',
						name : 'precoDesconto',
						width : 103,
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
						display : 'Lançamento',
						name : 'dataLancamento',
						width : 68,
						sortable : true,
						align : 'center'
					}, {
						display : 'Recolhimento',
						name : 'dataRecolhimento',
						width : 78,
						sortable : true,
						align : 'center'
					}, {
						display : 'Sede',
						name : 'encalheSede',
						width : 60,
						sortable : true,
						align : 'center'
					}, {
						display : 'Atendida',
						name : 'encalheAtendida',
						width : 60,
						sortable : true,
						align : 'center'
					}, {
						display : 'Exemplar',
						name : 'encalhe',
						width : 60,
						sortable : true,
						align : 'center'
					}, {
						display : 'Total R$',
						name : 'valorTotal',
						width : 70,
						sortable : true,
						align : 'right'
					}, {
						display : 'Nova Data',
						name : 'novaData',
						width : 110,
						sortable : false,
						align : 'center'
					},{
						display : 'Reprog.',
						name : 'reprogramar',
						width : 45,
						sortable : false,
						align : 'center'
					}],
					sortname : "sequencia",
					sortorder : "asc",
					usepager : true,
					useRp : true,
					rp : 15,
					showTableToggleBtn : true,
					width : 960,
					height : 180
				});
			}
			
			function confirmarBalanceamento() {
				
				$.postJSON(
					"${pageContext.request.contextPath}/devolucao/balanceamentoMatriz/confirmar",
					balanceamento.obterDatasMarcadasConfirmacao(),
					function(result) {
				
						fecharGridBalanceamento();
						
						$("#resumoPeriodo").hide();
						
						$("#dialog-confirm-balanceamento").dialog("close");

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
			}
			
			function balancearPorEditor() {
				
				fecharGridBalanceamento();
				
				$.postJSON(
					"${pageContext.request.contextPath}/devolucao/balanceamentoMatriz/balancearPorEditor",
					null,
					function(result) {
						
						montarResumoPeriodoBalanceamento(result);
					},
					function(result) {
						
						$("#resumoPeriodo").hide();
					}
				);
			}
			
			function balancearPorValor() {
				
				fecharGridBalanceamento();
				
				$.postJSON(
					"${pageContext.request.contextPath}/devolucao/balanceamentoMatriz/balancearPorValor",
					null,
					function(result) {
						
						montarResumoPeriodoBalanceamento(result);
					},
					function() {
						
						$("#resumoPeriodo").hide();
					}
				);
			}
			
			function salvar() {
				
				fecharGridBalanceamento();
				
				$.postJSON(
					"${pageContext.request.contextPath}/devolucao/balanceamentoMatriz/salvar"
				);
			}
			
			function exibirMatrizFornecedor() {
				
				$("#dataBalanceamentoHidden").val("");
				
				$(".balanceamentoGrid").flexOptions({
					url: "${pageContext.request.contextPath}/devolucao/balanceamentoMatriz/exibirMatrizFornecedor",
					preProcess: executarPreProcessamento,
					onSuccess: executarAposProcessamento,
					params: null,
				    newp: 1,
				});
				
				$(".balanceamentoGrid").flexReload();
			}
			
			function voltarConfiguracaoInicial() {
				
				fecharGridBalanceamento();
				
				$.postJSON(
					"${pageContext.request.contextPath}/devolucao/balanceamentoMatriz/voltarConfiguracaoOriginal",
					null,
					function(result) {
						
						montarResumoPeriodoBalanceamento(result);
					},
					function() {
						
						$("#resumoPeriodo").hide();
					}
				);
			}
			
			function reprogramarSelecionados() {
				
				$("#dialogReprogramarBalanceamento").dialog({
					resizable: false,
					height:'auto',
					width:300,
					modal: true,
					buttons: {
						"Confirmar": function() {
							
							processarReprogramacao();
						},
						"Cancelar": function() {
							
							$(this).dialog("close");
						}
					},
					beforeClose: function() {
						
						$("#novaDataRecolhimento").val("");
						
						clearMessageDialogTimeout();
					}
				});
			}
			
			function processarReprogramacao() {
				
				var linhasDaGrid = $('.balanceamentoGrid tr');
				
				var listaProdutoRecolhimento = "";
				
				var checkAllSelected = verifyCheckAll();
					
				$.each(linhasDaGrid, function(index, value) {
					
					var linha = $(value);
					
					var colunaCheck = linha.find("td")[17];
					
					var inputCheck = $(colunaCheck).find("div").find('input[name="checkReprogramar"]');
					
					var checked = inputCheck.attr("checked") == "checked";
					
					if (checked) {
						
						var idLinha = linha.attr("id");
						
						var idLancamento = idLinha.replace("row", "");
						
						var sequencia = obterValorInputColuna(linha, 0, "sequencia");
						var novaData = obterValorInputColuna(linha, 16, "novaData");
						
						var linhaSelecionada = 'listaProdutoRecolhimento[' + index + '].idLancamento=' + idLancamento + '&';
						linhaSelecionada += 'listaProdutoRecolhimento[' + index + '].sequencia=' + sequencia + '&';
						linhaSelecionada += 'listaProdutoRecolhimento[' + index + '].novaData=' + novaData + '&';
						
						listaProdutoRecolhimento = (listaProdutoRecolhimento + linhaSelecionada);
					}
				});
				
				var novaData = $("#novaDataRecolhimento").val();
				
				var dataAntiga = $("#dataBalanceamentoHidden").val();
				
				$.postJSON("${pageContext.request.contextPath}/devolucao/balanceamentoMatriz/reprogramarSelecionados",
						   listaProdutoRecolhimento
						   		+ "&selecionarTodos=" + checkAllSelected
						   		+ "&novaDataFormatada=" + novaData
						   		+ "&dataAntigaFormatada=" + dataAntiga,
						   function(result) {
						   		
								$("#dialogReprogramarBalanceamento").dialog("close");
					
						   		atualizarResumoBalanceamento();
						   		
						   		deselectCheckAll();
						   },
						   null,
						   true
				);
			}
			
			function reprogramarRecolhimentoUnico(idRow) {
				
				var linhasDaGrid = $('.balanceamentoGrid tr');
				
				var linhaSelecionada;
				
				$.each(linhasDaGrid, function(index, value) {
					
					var linha = $(value);
					
					var idLinha = linha.attr("id");
					
					var idLancamento = idLinha.replace("row", "");
					
					if (idLancamento == idRow) {
						
						var sequencia = obterValorInputColuna(linha, 0, "sequencia");
						var novaData = obterValorInputColuna(linha, 16, "novaData");
						
						linhaSelecionada = 'produtoRecolhimento.idLancamento=' + idLancamento + '&';
						linhaSelecionada += 'produtoRecolhimento.sequencia=' + sequencia + '&';
						linhaSelecionada += 'produtoRecolhimento.novaData=' + novaData + '&';
					}
				});
				
				var dataAntiga = $("#dataBalanceamentoHidden").val();
				
				$.postJSON("${pageContext.request.contextPath}/devolucao/balanceamentoMatriz/reprogramarRecolhimentoUnico",
						   linhaSelecionada
						   		+ "&dataAntigaFormatada=" + dataAntiga,
						   function(result) {
						   
						   		atualizarResumoBalanceamento();
						   }
				);
			}
			
			function fecharGridBalanceamento() {
				
				$(".grids").hide();
				
				deselectCheckAll();
			}
			
			function atualizarResumoBalanceamento() {
				
				$.postJSON("${pageContext.request.contextPath}/devolucao/balanceamentoMatriz/atualizarResumoBalanceamento",
						   null,
						   function(result) {
						   	
						   		montarResumoPeriodoBalanceamento(result);
						   		
						   		$(".balanceamentoGrid").flexReload();
						   },
						   function() {
						   
							   fecharGridBalanceamento();
							   
							   $("#resumoPeriodo").hide();
						   }
				);
			}
						
			function obterValorInputColuna(linha, posicao, inputName) {
				
				var coluna = linha.find("td")[posicao];
				
				var input = $(coluna).find("div").find('input[name="' + inputName + '"]');
				
				return $(input).val();
			}
			
			function verifyCheckAll() {
				return ($("#checkAllReprogramar").attr("checked") == "checked");
			}
			
			function deselectCheckAll() {
				
				$("#checkAllReprogramar").attr("checked", false);
			}

			// TODO: deletar esse metodo
			function mostarDetalhesProduto(idProdutoEdicao) {

				var data = [];
				
				data.push({name:'idProdutoEdicao', value: idProdutoEdicao});
				
				$.postJSON(
					"${pageContext.request.contextPath}/cadastro/edicao/obterDetalheProduto.json", 
					data,
					function(result) {
						balanceamento.popularDetalheProduto(result);
						balanceamento.popup_detalhes_prod("#dialog-detalhe-produto" );
					},
					function() {
						$("#dialog-detalhe-produto").hide();
					}
				);
			}
			
			function obterConfirmacaoBalanceamento() {
				
				$.postJSON(
					"${pageContext.request.contextPath}/devolucao/balanceamentoMatriz/obterAgrupamentoDiarioBalanceamento", 
					null,
					function(result) {
						balanceamento.popularConfirmacaoBalanceamento(result);
						abrirPopupConfirmarBalanceamento();
					},
					function() {
						$("#dialog-confirm-balanceamento").hide();
					}
				);
			}
			
			function abrirPopupConfirmarBalanceamento() {
				
				$( "#dialog-confirm-balanceamento" ).dialog({
					resizable: false,
					height:'auto',
					width:300,
					modal: true,
					buttons: [
					    {
					    	id: "dialogConfirmarBtnConfirmar",
					    	text: "Confirmar",
					    	click: function() {
							
					    		confirmarBalanceamento();
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
				    }
				});
			}
			
			$(function() {
	
				inicializar();
			});
		</script>
	</head>
	
	<body>
		
		<div id="dialog-confirm" title="Balanceamento da Matriz de Recolhimento">
			
			<jsp:include page="../messagesDialog.jsp" />
			
			<p>Ao prosseguir com essa ação você perderá seus dados não salvos ou confirmados. Deseja prosseguir?</p>
			   
		</div>
		
		<div id="dialog-confirm-balanceamento" title="Balanceamento" style="display:none;">
		    
		    <jsp:include page="../messagesDialog.jsp">
				<jsp:param value="dialog-confirmar" name="messageDialog"/>
			</jsp:include>
			
		    <fieldset style="width:250px!important;">
		    	<legend>Confirmar Balanceamento</legend>

		        <table width="240" border="0" cellspacing="1" cellpadding="1" id="tableConfirmaBalanceamento">
		        </table>

		    </fieldset>
		</div>
		
		<div id="dialogReprogramarBalanceamento" title="Reprogramar Recolhimentos">
		    
		    <jsp:include page="../messagesDialog.jsp" />
		    
		    <p>
			    <strong>Nova Data:</strong>
			    <input name="novaDataRecolhimento" type="text"
			    	   style="width:80px;" id="novaDataRecolhimento" />
		    </p>
		</div>
		
		<!-- Filtro de Pesquisa -->
		
		<fieldset class="classFieldset">
		
			<legend>Pesquisar Balanceamento da Matriz de Recolhimento </legend>
			
			<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
				<tr>
					<td width="76">Fornecedor:</td>
					<td colspan="3">
						<a href="#" id="selFornecedor" onclick="return false;">Clique e Selecione o Fornecedor</a>
						<div class="menu_fornecedor" style="display:none;">
		                	<span class="bt_sellAll">
								<input type="checkbox" id="checkBoxSelecionarTodosFornecedores" name="checkBoxSelecionarTodosFornecedores" onclick="checkAll(this, 'checkGroupFornecedores');" style="float:left;"/>
								<label for="checkBoxSelecionarTodosFornecedores">Selecionar Todos</label>
							</span>
		                    <br clear="all" />
		                    <c:forEach items="${fornecedores}" var="fornecedor">
		                    	<input id="fornecedor_${fornecedor.id}" value="${fornecedor.id}" name="checkGroupFornecedores" onclick="verifyCheck($('#checkBoxSelecionarTodosFornecedores'));" type="checkbox"/>
		                      	<label for="fornecedor_${fornecedor.id}">${fornecedor.juridica.razaoSocial}</label>
		                     	<br clear="all" />
		                	</c:forEach> 
		            	</div>
					</td>
					<td width="53">Semana:</td>
					<td width="107">
						<input type="text" 
							   name="numeroSemana" 
							   id="numeroSemana" value="${numeroSemana}" style="width: 50px;"
							   onchange="carregarDataSemana();" />
					</td>
					<td width="33">Data:</td>
					<td width="145">
						<input type="text" 
							   name="dataPesquisa" 
							   id="dataPesquisa" 
							   style="width: 80px; float: left; margin-right: 5px;" maxlength="10"
							   value="${dataAtual}"
							   onchange="carregarDiaSemana();" />
					</td>
					<td width="164">
						<span class="bt_pesquisar" title="Pesquisar">
							<a href="javascript:;" onclick="verificarBalanceamentosAlterados(pesquisar);">Pesquisar</a>
						</span>
					</td>
				</tr>
			</table>
		</fieldset>
		
		<div class="linha_separa_fields">&nbsp;</div>
		
		<!--  Resumo do Período -->
		
		<fieldset class="classFieldset" id="resumoPeriodo" style="display: none;">
		
			<legend>Resumo do Período</legend>
			
			<div style="width: 950px; overflow-x: auto;">
				<table id="tableResumoPeriodoBalanceamento" name="tableResumoPeriodoBalanceamento" width="100%" border="0" cellspacing="2" cellpadding="2">
				</table>
			</div>
			
			<!-- Botões de Ação -->
			
			<table width="950" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="115">
						<span class="bt_confirmar_novo" title="Confirmar balanceamento">
							<a id="linkConfirmar" href="javascript:;">
								<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Confirmar
							</a>
						</span>
					</td>
					<td width="117">
						<strong>Balanceamento por:</strong>
					</td>
					<td width="296">
						<span class="bt_confirmar_novo" title="Balancear Editor">
							<a id="linkEditor" href="javascript:;">
								<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Editor
							</a>
						</span>
						<span class="bt_confirmar_novo" title="Balancear Volume / Valor">
							<a id="linkValor" href="javascript:;">
								<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_check.gif">Valor
							</a>
						</span>
						<span class="bt_novos" title="Salvar">
							<a id="linkSalvar" href="javascript:;">
								<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_salvar.gif">Salvar
							</a>
						</span>
					</td>
					
					<td width="207">
						<span class="bt_novos" title="Matriz Fornecedor" style="float: right;">
							<a id="linkMatrizFornecedor" href="javascript:;" onclick="exibirMatrizFornecedor();">
								<img border="0" hspace="5" src="${pageContext.request.contextPath}/images/ico_detalhes.png">Matriz Fornecedor
							</a>
						</span>
					</td>
					
					<td width="215">
						<span class="bt_configura_inicial" title="Voltar Configuração Inicial">
							<a id="linkConfiguracaoInicial" href="javascript:;">
								<img src="${pageContext.request.contextPath}/images/bt_devolucao.png" border="0" hspace="5" />
								Voltar Configuração Inicial
							</a>
						</span>
					</td>
				</tr>
			</table>
		</fieldset>
		
		<!-- Balanceamento -->
		
		<fieldset id="fieldsetGrids" class="classFieldset">
		
			<legend>Balanceamento da Matriz de Recolhimento </legend>
			
			<div class="grids" style="display: none;">
	
				<span class="bt_novos" id="bt_fechar" title="Fechar" style="float: right;">
					<a id="linkFechar" href="javascript:;" onclick="fecharGridBalanceamento();">
						<img src="${pageContext.request.contextPath}/images/ico_excluir.gif"
							 hspace="5" border="0" />Fechar
					</a>
				</span>
	
				<br clear="all" />
				
				<input type="hidden" id="dataBalanceamentoHidden" />
				
				<!-- GRID -->
				<table class="balanceamentoGrid"></table>
				
				<table width="950" border="0" cellspacing="2" cellpadding="2">
					<tr>
						<td width="152">
							<span class="bt_novos" title="Reprogramar">
								<a id="linkReprogramar" href="javascript:;">
									<img src="${pageContext.request.contextPath}/images/ico_reprogramar.gif" hspace="5" border="0" />Reprogramar
								</a>
							</span>
						</td>
						<td width="46">&nbsp;</td>
						<td width="443">&nbsp;</td>
						<td width="150">
							<span class="bt_sellAll">
								<label for="sel">Selecionar Todos</label>
								<input type="checkbox" name="checkAllReprogramar" id="checkAllReprogramar" onclick="selecionarTodos(this);" style="float: left;" />
							</span>
						</td>
					</tr>
				</table>
			</div>
		</fieldset>
		
		<div id="dialog-detalhe-produto" title="Detalhes do Produto" style="display:none;">
    		<jsp:include page="../produtoEdicao/detalheProduto.jsp" />
		</div>
		
		<div class="linha_separa_fields">&nbsp;</div>
		
	</body>