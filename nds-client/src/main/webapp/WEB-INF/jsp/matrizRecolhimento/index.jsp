<head>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

	<script type="text/javascript">

		function pesquisar() {

			fecharGridBalanceamento();
			
			$.postJSON(
				"<c:url value='/devolucao/balanceamentoMatriz/pesquisar' />", 
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
				rows += '<span class="span_1">Qtde. Exempl.:</span>';	
				rows += '<span class="span_2">' + resumo.qtdeExemplaresFormatada + '</span>';
				rows += '<span class="span_1">Qtde. Parciais:</span>';	
				rows += '<span class="span_2">' + resumo.qtdeTitulosParciais + '</span>';	
				rows += '<span class="span_1">Peso Total:</span>';
				rows += '<span class="span_2">' + resumo.pesoTotalFormatado + '</span>';
				rows += '<span class="span_1">Valor Total:</span>';
				rows += '<span class="span_2">' + resumo.valorTotalFormatado + '</span>'
				rows += '</div>';
				rows += '</td>';
		    });	
		    
		    rows += "</tr>";

		    $("#tableResumoPeriodoBalanceamento").empty();
		    
		    $("#tableResumoPeriodoBalanceamento").append(rows);

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
			bloquearLink("linkMatrizFornecedor");
			bloquearLink("linkConfiguracaoInicial");
			bloquearLink("linkReprogramar");
		}
		
		function habilitarLinks() {
			
			habilitarLink("linkConfirmar", confirmar);
			habilitarLink("linkEditor", balancearPorEditor);
			habilitarLink("linkValor", balancearPorValor);
			habilitarLink("linkSalvar", salvar);
			habilitarLink("linkMatrizFornecedor", exibirMatrizFornecedor);
			habilitarLink("linkConfiguracaoInicial", voltarConfiguracaoInicial);
			habilitarLink("linkReprogramar", reprogramar);
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
			
			$(".balanceamentoGrid").flexOptions({
				url: "<c:url value='/devolucao/balanceamentoMatriz/exibirMatrizBalanceamentoPorDia' />",
				onSuccess: executarAposProcessamento,
				params: [
			         {name:'dataFormatada', value: data}
			    ],
			    newp: 1,
			});
			
			$(".balanceamentoGrid").flexReload();
		}
		
		function executarPreProcessamento(resultado) {
			
			if (resultado.mensagens) {

				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);
				
				$(".grids").hide();

				return resultado;
			}
			
			$.each(resultado.rows, function(index, row) {
				
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
						    +	    ' style="width: 25px;" disabled="disabled"'
						    +	    ' name="sequencia" />';
			} else {
				
				retornoHTML = '<input type="text" id="sequencia' + row.id + '"'
						    + 	    ' value="' + row.cell.sequencia + '"'
						    +	    ' style="width: 25px;"'
						    +	    ' name="sequencia" />';
			}
			
			return retornoHTML;
		}
		
		function gerarHTMLNovaData(row) {
			
			var retornoHTML;
			
			if (row.cell.bloqueioMatrizFechada
					|| row.cell.bloqueioDataRecolhimento) {
				
				retornoHTML = '<input type="text" id="novaData' + row.id + '"'
						    + 	    ' name="novaData"'			  
							+ 	    ' value="' + row.cell.novaData + '"'
							+ 	    ' style="width:65px; margin-right:5px; float:left;"'
							+       ' disabled="disabled" />';
				
				retornoHTML += '<div class="bt_atualizarIco linkDisabled">'
					  		 + '  <a style="cursor: default;" href="javascript:;">&nbsp;</a>'
					  		 + '</div>';
			} else {
				
				retornoHTML = '<input type="text" id="novaData' + row.id + '"'
							+ 	    ' name="novaData"'
							+ 	    ' value="' + row.cell.novaData + '"'
							+ 	    ' style="width:65px; margin-right:5px; float:left;" />';
				
			  	retornoHTML += '<div class="bt_atualizarIco">'
					  		 +   '<a href="javascript:;" onclick="atualizarData();">&nbsp;</a>'
					  		 + '</div>';
			}
			
			return retornoHTML;
		}
		
		function gerarCheckReprogramar(row) {
			
			var retornoHTML;
			
			if (row.cell.bloqueioMatrizFechada) {
				
				retornoHTML = '<input type="checkbox" id="ch' + row.id + '"'
		   		   			+       ' name="checkReprogramar"'
		   		   			+       ' value="' + row.id + '" disabled="disabled" />';
			} else {
				
				retornoHTML = '<input type="checkbox" id="ch' + row.id + '"'
		   		   			+       ' name="checkReprogramar"'
		   		   			+       ' value="' + row.id + '"'
		   		   			+       ' onclick="checarBalanceamento()" />';
			}
			
			return retornoHTML;
		}
		
		function executarAposProcessamento() {
			
			$("input[name='novaData']").datepicker({
				dateFormat: 'dd/mm/yy'
			});
			
			$("input[name='novaData']").mask("99/99/9999");
		}
		
		function checarBalanceamento() {
			
			$("input[name='checkReprogramar']").each(function() {
			
				var checado = this.checked;
				
				clickLineFlexigrid(this, checado);
				
				if (!checado) {
					
					$("#checkAllReprogramar").attr("checked", false);
				}
			});
		}
		
		function selecionarTodos(input) {
			
			checkAll(input, "checkReprogramar");
			
			$("input[name='checkReprogramar']").each(function() {
			
				var checado = this.checked;
				
				clickLineFlexigrid(this, checado);
			});
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
				"<c:url value='/cadastro/distribuidor/obterDataDaSemana' />", 
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
				"<c:url value='/cadastro/distribuidor/obterNumeroSemana' />", 
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
			
			$("#dataPesquisa").datepicker({
				showOn : "button",
				buttonImage: "${pageContext.request.contextPath}/images/calendar.gif",
				buttonImageOnly : true,
				dateFormat: 'dd/mm/yy',
				defaultDate: new Date()
			});

			$("#dataPesquisa").mask("99/99/9999");
			
			$("input[name='numeroSemana']").numeric();

			carregarDadosPesquisa();
		}
		
		function iniciarGrid() {
			
			$(".balanceamentoGrid").flexigrid({
				preProcess: executarPreProcessamento,
				dataType : 'json',
				colModel : [ {
					display : 'SM',
					name : 'sequencia',
					width : 33,
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
					width : 50,
					sortable : true,
					align : 'left'
				}, {
					display : 'Edição',
					name : 'numeroEdicao',
					width : 40,
					sortable : true,
					align : 'left'
				}, {
					display : 'Preço Venda R$',
					name : 'precoVenda',
					width : 45,
					sortable : true,
					align : 'right'
				}, {
					display : 'Fornecedor',
					name : 'nomeFornecedor',
					width : 40,
					sortable : true,
					align : 'left'
				}, {
					display : 'Editor',
					name : 'nomeEditor',
					width : 40,
					sortable : true,
					align : 'left',
				}, {
					display : 'Parcial',
					name : 'parcial',
					width : 40,
					sortable : true,
					align : 'center'
				}, {
					display : 'Brinde',
					name : 'brinde',
					width : 40,
					sortable : true,
					align : 'center'
				}, {
					display : 'Lançamento',
					name : 'dataLancamento',
					width : 60,
					sortable : true,
					align : 'center'
				}, {
					display : 'Recolhimento',
					name : 'dataRecolhimento',
					width : 70,
					sortable : true,
					align : 'center'
				}, {
					display : 'Sede',
					name : 'encalheSede',
					width : 40,
					sortable : true,
					align : 'center'
				}, {
					display : 'Atendida',
					name : 'encalheAtendida',
					width : 50,
					sortable : true,
					align : 'center'
				}, {
					display : 'Exemplar',
					name : 'encalhe',
					width : 45,
					sortable : true,
					align : 'center'
				}, {
					display : 'Total R$',
					name : 'valorTotal',
					width : 50,
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
					width : 40,
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
		
		function confirmar() {
			
			$.postJSON(
				"<c:url value='/devolucao/balanceamentoMatriz/confirmar' />",
				null,
				function() {
					
				}
			);
		}
		
		function balancearPorEditor() {
			
			$.postJSON(
				"<c:url value='/devolucao/balanceamentoMatriz/balancearPorEditor' />",
				null,
				function() {
					
				}
			);
		}
		
		function balancearPorValor() {
			
			$.postJSON(
				"<c:url value='/devolucao/balanceamentoMatriz/balancearPorValor' />",
				null,
				function() {
					
				}
			);
		}
		
		function salvar() {
			
			$.postJSON(
				"<c:url value='/devolucao/balanceamentoMatriz/salvar' />",
				null,
				function() {
					
				}
			);
		}
		
		function exibirMatrizFornecedor() {
			
			$.postJSON(
				"<c:url value='/devolucao/balanceamentoMatriz/exibirMatrizFornecedor' />",
				null,
				function() {
					
				}
			);
		}
		
		function voltarConfiguracaoInicial() {
			
			fecharGridBalanceamento();
			
			$.postJSON(
				"<c:url value='/devolucao/balanceamentoMatriz/voltarConfiguracaoOriginal' />",
				null,
				function() {
					
					montarResumoPeriodoBalanceamento(result);
				},
				function() {
					
					$("#resumoPeriodo").hide();
				}
			);
		}
		
		function reprogramar() {
			
			var linhasDaGrid = $('.balanceamentoGrid tr');
			
			var listaProdutoRecolhimento = "";
			
			var checkAllSelected = verifyCheckAll();
			
			if (!checkAllSelected) {
				
				$.each(linhasDaGrid, function(index, value) {
					
					var linha = $(value);
					
					var colunaCheck = linha.find("td")[16];
					
					var inputCheck = $(colunaCheck).find("div").find('input[name="checkReprogramar"]');
					
					var checked = inputCheck.attr("checked") == "checked";
					
					if (checked) {
						
						var idLinha = linha.attr("id");
						
						var idLancamento = idLinha.replace("row", "");
						
						var sequencia = obterValorInputColuna(linha, 0, "sequencia");
						var codProduto = obterValorColuna(linha, 1);
						var numEdicao = obterValorColuna(linha, 3);
						var novaData = obterValorInputColuna(linha, 15, "novaData");
						
						var linhaSelecionada = 'listaProdutoRecolhimento[' + index + '].idLancamento=' + idLancamento + '&';
						linhaSelecionada += 'listaProdutoRecolhimento[' + index + '].sequencia=' + sequencia + '&';
						linhaSelecionada += 'listaProdutoRecolhimento[' + index + '].codigoProduto=' + codProduto + '&';
						linhaSelecionada += 'listaProdutoRecolhimento[' + index + '].numeroEdicao=' + numEdicao + '&';
						linhaSelecionada += 'listaProdutoRecolhimento[' + index + '].novaData=' + novaData + '&';
						
						listaProdutoRecolhimento = (listaProdutoRecolhimento + linhaSelecionada);
					}
				});
			}
			
			$.postJSON("<c:url value='/devolucao/balanceamentoMatriz/reprogramar' />",
					   listaProdutoRecolhimento + "&selecionarTodos=" + checkAllSelected,
					   function(result) {
							
							//$("#dialog-confirm").dialog("close");
							
							var tipoMensagem = result.tipoMensagem;
							var listaMensagens = result.listaMensagens;
							
							if (tipoMensagem && listaMensagens) {
								
								exibirMensagem(tipoMensagem, listaMensagens);
							}
							
							$(".balanceamentoGrid").flexReload();
							
							$("#checkAllReprogramar").attr("checked", false);
						},
					   null,
					   false
			);
		}
		
		function fecharGridBalanceamento() {
			
			$(".grids").hide();
			
			$("#checkAllReprogramar").attr("checked", false);
		}
		
		function obterValorColuna(linha, posicao) {
			
			var coluna = linha.find("td")[posicao];
			
			return $(coluna).find("div").html();
		}
		
		function obterValorInputColuna(linha, posicao, inputName) {
			
			var coluna = linha.find("td")[posicao];
			
			var input = $(coluna).find("div").find('input[name="' + inputName + '"]');
			
			return $(input).val();
		}
		
		function verifyCheckAll() {
			return ($("#checkAllReprogramar").attr("checked") == "checked");
		}
		
		$(function() {

			inicializar();
		});
	</script>
</head>

<body>
	
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
						<a href="javascript:;" onclick="pesquisar();">Pesquisar</a>
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
							<img border="0" hspace="5" src="<c:url value='/images/ico_check.gif'/>">Confirmar
						</a>
					</span>
				</td>
				<td width="117">
					<strong>Balanceamento por:</strong>
				</td>
				<td width="296">
					<span class="bt_confirmar_novo" title="Balancear Editor">
						<a id="linkEditor" href="javascript:;">
							<img border="0" hspace="5" src="<c:url value='/images/ico_check.gif'/>">Editor
						</a>
					</span>
					<span class="bt_confirmar_novo" title="Balancear Volume / Valor">
						<a id="linkValor" href="javascript:;">
							<img border="0" hspace="5" src="<c:url value='/images/ico_check.gif'/>">Valor
						</a>
					</span>
					<span class="bt_novos" title="Salvar">
						<a id="linkSalvar" href="javascript:;">
							<img border="0" hspace="5" src="<c:url value='/images/ico_salvar.gif'/>">Salvar
						</a>
					</span>
				</td>
				
				<td width="207">
					<span class="bt_novos" title="Matriz Fornecedor" style="float: right;">
						<a id="linkMatrizFornecedor" href="javascript:;">
							<img border="0" hspace="5" src="<c:url value='/images/ico_detalhes.png'/>">Matriz Fornecedor
						</a>
					</span>
				</td>
				
				<td width="215">
					<span class="bt_configura_inicial" title="Voltar Configuração Inicial">
						<a id="linkConfiguracaoInicial" href="javascript:;">
							<img src="<c:url value='/images/bt_devolucao.png'/>" border="0" hspace="5" />
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
			
			<!-- GRID -->
			<table class="balanceamentoGrid"></table>
			
			<table width="950" border="0" cellspacing="2" cellpadding="2">
				<tr>
					<td width="152">
						<span class="bt_novos" title="Reprogramar">
							<a id="linkReprogramar" href="javascript:;">
								<img src="<c:url value='/images/ico_reprogramar.gif'/>" hspace="5" border="0" />Reprogramar
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
		
		<!-- GRID -->
		
		<div id="gridMatriz" style="display: none;">
			<table class="balanceamentoGrid2"></table>
		</div>
		
	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>
	
</body>