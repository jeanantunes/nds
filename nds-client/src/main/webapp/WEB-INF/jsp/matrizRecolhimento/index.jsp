<head>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

	<script type="text/javascript">

		function pesquisar() {

			$.postJSON(
				"<c:url value='/devolucao/balanceamentoMatriz/pesquisar' />", 
				obterParametrosPesquisa(),
				function(result) {
					
					var rows = '<tr>';

					$.each(result, function(index, resumo) {
						
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

				    $("#resumoPeriodo").show();

				    $("#fieldsetGrids").hide();
				},
				function() {

					$("#resumoPeriodo").hide();
				}
			);
		}
		
		function desabilitarLink(idLink) {
			
			var link = $("#" + idLink);
			link.addClass("linkDisabled");
			link.unbind("click");
			link.css("text-decoration", "none");
		}
		
		function habilitarLink(idLink, funcao) {
			
			var link = $("#" + idLink);
			link.removeClass("linkDisabled");
			link.bind("click", funcao);
			link.css("text-decoration", "");
		}		
		
		function visualizarMatrizBalanceamentoPorDia(data) {
			
			$(".balanceamentoGrid").flexOptions({
				url: "<c:url value='/devolucao/balanceamentoMatriz/exibirMatrizBalanceamentoPorDia' />",
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
				
				var inputSequencia = '<input type="text" id="sequencia' + row.id + '"'
								   + ' value="' + row.cell.sequencia + '"'
								   + ' style="width: 25px;" />';
								   
			   	var inputNovaData = '<input type="text" id="novaData' + row.id + '"'
								  + ' value="' + row.cell.novaData + '"'
			   					  + ' style="width:65px; margin-right:5px; float:left;" />';
			   				
			   	var divRecarregarData = '<div class="bt_atualizarIco">'
			   						  + '<a href="javascript:;">&nbsp;</a></div>';
				
   				var novaData = inputNovaData + divRecarregarData;
			   				
				var inputCheck = '<input type="checkbox" id="ch' + row.id + '"'
							   + ' name="checkBalanceamento"'
							   + ' value="' + row.id + '"'
							   + ' onclick="checarBalanceamento()" />';
					
		   		row.cell.sequencia = inputSequencia;
				row.cell.novaData = novaData;
				row.cell.reprogramar = inputCheck;
			});
				
			$(".grids").show();
			
			$("#fieldsetGrids").show();
			
			$("#bt_fechar").show();
			
			return resultado;
		}
		
		function checarBalanceamento() {
			
			$("input[name='checkBalanceamento']").each(function() {
			
				var checado = this.checked;
				
				clickLineFlexigrid(this, checado);
				
				if (!checado) {
					
					$("#checkAllBalanceamento").attr("checked", false);
				}
			});
		}
		
		function selecionarTodos(input) {
			
			checkAll(input, "checkBalanceamento");
			
			$("input[name='checkBalanceamento']").each(function() {
			
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
			
			bindingFuncoesClick();
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
					display : 'Preço Capa R$',
					name : 'precoCapa',
					width : 45,
					sortable : true,
					align : 'right'
				}, {
					display : 'Fornecedor',
					name : 'fornecedor',
					width : 40,
					sortable : true,
					align : 'left'
				}, {
					display : 'Editor',
					name : 'editor',
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
					name : 'sede',
					width : 40,
					sortable : true,
					align : 'center'
				}, {
					display : 'Atendida',
					name : 'atendida',
					width : 50,
					sortable : true,
					align : 'center'
				}, {
					display : 'Exemplar',
					name : 'qtdeExemplares',
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
					sortable : true,
					align : 'center'
				},{
					display : 'Reprog.',
					name : 'reprogramar',
					width : 40,
					sortable : true,
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
		
		function bindingFuncoesClick() {
			
			$("#linkConfirmar").bind("click", confirmar);
			$("#linkEditor").bind("click", balancearPorEditor);
			$("#linkValor").bind("click", balancearPorValor);
			$("#linkSalvar").bind("click", salvar);
			$("#linkMatrizFornecedor").bind("click", balancearMatrizFornecedor);
			$("#linkConfiguracaoInicial").bind("click", voltarConfiguracaoInicial);
			$("#linkFechar").bind("click", fechar);
			$("#linkReprogramar").bind("click", reprogramar);
		}
		
		function confirmar() {
			
		}
		
		function balancearPorEditor() {
			
		}
		
		function balancearPorValor() {
			
			$.postJSON(
				"<c:url value='/devolucao/balanceamentoMatriz/balancearPorValor' />",
				obterParametrosPesquisa(),
				function() {
					
				},
				function() {
					
				}
			);
		}
		
		function salvar() {
			
		}
		
		function balancearMatrizFornecedor() {
			
		}
		
		function voltarConfiguracaoInicial() {
			
		}
		
		function fechar() {
			
		}
		
		function reprogramar() {
			
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
					<strong>Balancear por:</strong>
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

			<span class="bt_novos" id="bt_fechar" title="Fechar" style="float: right; display: none;">
				<a id="linkFechar" href="javascript:;">
					<img src="${pageContext.request.contextPath}/images/ico_excluir.gif"
						 hspace="5" border="0" />Fechar
				</a>
			</span>

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
							<input type="checkbox" name="checkAllBalanceamento" id="checkAllBalanceamento" onclick="selecionarTodos(this);" style="float: left;" />
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