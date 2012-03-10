<div id="dialogNovasDiferencas" 
	 title="Lançamento < Faltas e Sobras >"
	 style="display: none;">

	<div id="effectDialog" 
		 class="ui-state-highlight ui-corner-all" 
		 style="display: none; position: absolute; z-index: 1000; width: 600px;">
		 
		<p>
			<span style="float: left;" class="ui-icon ui-icon-info"></span>
			<b id="idTextoMensagemDialog"></b>
		</p>
	</div>

	<form id="novoLancamentoDiferencaForm"
		  name="novoLancamentoDiferencaForm" 
		  action="estoque/diferenca/lancamento/cadastrarNovasDiferencas" 
		  method="post">
		
		<table id="gridNovasDiferencas" class="gridNovasDiferencas"></table>
	
	</form>
	
	<table id="" width="505" border="0" cellspacing="2" cellpadding="2">
		<tr style="font-size: 11px;">
			<td id="labelTotalGeralNovo" width="55"><strong>Total Geral:</strong></td>
			<td id="totalPrecoVendaDiferencas" width="5" align="right"></td>
			<td id="totalRecebimentoFisico" width="10" align="center"></td>
		</tr>
	</table>
	
	<script language="javascript" type="text/javascript">

		var ultimaLinhaPreenchida;
	
		$(function() {

			$(".gridNovasDiferencas").flexigrid({
				preProcess: executarPreProcessamentoNovo,
				onSuccess: formatarCamposNumericos,
				dataType : 'json',
				colModel : [{
					display : 'Código',
					name : 'codigoProduto',
					width : 100,
					sortable : false,
					align : 'left'
				},{
					display : 'Produto',
					name : 'descricaoProduto',
					width : 150,
					sortable : false,
					align : 'left'
				},{
					display : 'Edição',
					name : 'numeroEdicao',
					width : 50,
					sortable : false,
					align : 'center'
				}, {
					display : 'Preço Venda R$',
					name : 'precoVenda',
					width : 90,
					sortable : false,
					align : 'right'
				},{
					display : 'Qtde',
					name : 'qtdeRecebimentoFisico',
					width : 55,
					sortable : false,
					align : 'center'
				}, {
					display : 'Exemplares',
					name : 'quantidade',
					width : 70,
					sortable : false,
					align : 'center'
				}],
				singleSelect: true,
				width : 610,
				height : 220,
				disableSelect : true
			});
		});

		function popupNovasDiferencas() {

			var formData = $('#pesquisaLancamentoDiferencaForm').serializeArray();

			$("#gridNovasDiferencas").flexOptions({
				url : '<c:url value="/estoque/diferenca/lancamento/novo" />', 
				params: formData
			});
			
			$("#gridNovasDiferencas").flexReload();

			$("#effectDialog").hide();
			
			$("#dialogNovasDiferencas").dialog({
				resizable: false,
				height:390,
				width:640,
				modal: true,
				buttons: {
					"Confirmar": function() {
						cadastrarNovasDiferencas();
					},
					"Cancelar": function() {
						$("#gridNovasDiferencas").flexAddData({rows:[]});
						$(this).dialog("close");
					}
				}
			});
		}

		function executarPreProcessamentoNovo(resultado) {

			if (resultado.mensagens) {

				exibirMensagemDialog(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);

				return resultado;
			}

			$.each(resultado.rows, function(index, row) {

				var hiddenId = '<input type="hidden" name="id" value="' + index + '" />';

				var parametroLimparCamposPesquisa = '\'#descricaoProduto' + index + '\', \'#edicao' + index + '\',  function() {reprocessarDadosLancamento(\'#precoVenda' + index + '\', \'#precoVendaFormatado' + index + '\', \'#qtdeRecebimentoFisico' + index + '\', \'#qtdeRecebimentoFisicoFormatado' + index + '\')}';
								
				var inputCodigoProduto = 
					'<input type="text" id="codigoProduto' + index + '" name="codigoProduto" style="width:60px; float:left; margin-right:10px;" maxlenght="255" onchange="produto.limparCamposPesquisa(' + parametroLimparCamposPesquisa + ')" />';

				var parametroPesquisaProduto = '\'#codigoProduto' + index + '\', \'#descricaoProduto' + index + '\', \'#edicao' + index + '\', true, null, function() {reprocessarDadosLancamento(\'#precoVenda' + index + '\', \'#precoVendaFormatado' + index + '\', \'#qtdeRecebimentoFisico' + index + '\', \'#qtdeRecebimentoFisicoFormatado' + index + '\')}';

				var parametroAutoCompleteProduto = '\'#descricaoProduto' + index + '\', true';

				var parametroValidacaoEdicao = '\'#codigoProduto' + index + '\', \'#edicao' + index + '\', true, obterDadosProduto';

				var imgLupaPesquisa = '<span class="classPesquisar" title="Pesquisar Produto">'
									+ '<a href="javascript:;" onclick="produto.pesquisarPorCodigoProduto(' + parametroPesquisaProduto + ');">&nbsp;</a>'
									+ '</span>';

				var inputDescricaoProduto = 
					'<input type="text" id="descricaoProduto' + index + '" name="descricaoProduto" style="width:140px;" maxlenght="255" onkeyup="produto.autoCompletarPorNomeProduto(' + parametroAutoCompleteProduto + ');" onchange="produto.pesquisarPorNomeProduto(' + parametroPesquisaProduto + ')" />';

				var inputNumeroEdicao = 
					'<input type="text" id="edicao' + index + '"  name="numeroEdicao" style="width:40px;" maxlenght="20" onchange="produto.validarNumEdicao(' + parametroValidacaoEdicao + '); ultimaLinhaPreenchida=' + index + '" disabled="disabled" />';

				var spanPrecoVenda = '<span id="precoVendaFormatado' + index + '"/>';

				var hiddenPrecoVenda = '<input type="hidden" id="precoVenda' + index + '" />';

				var spanQtdeRecebimentoFisico = '<span id="qtdeRecebimentoFisicoFormatado' + index + '"/>';

				var hiddenQtdeRecebimentoFisico = '<input type="hidden" id="qtdeRecebimentoFisico' + index + '" />';
				
				var inputQuantidade = 
					'<input type="text" name="qtdeDiferenca" style="width:60px;" maxlenght="20" />';

				row.cell.codigoProduto = hiddenId + inputCodigoProduto + imgLupaPesquisa;
				row.cell.descricaoProduto = inputDescricaoProduto;
				row.cell.numeroEdicao = inputNumeroEdicao;
				row.cell.precoVenda = hiddenPrecoVenda + spanPrecoVenda;
				row.cell.qtdeRecebimentoFisico = hiddenQtdeRecebimentoFisico + spanQtdeRecebimentoFisico;
				row.cell.quantidade = inputQuantidade;
			});

			$("#totalPrecoVendaDiferencas").empty();
			$("#totalRecebimentoFisico").empty();
			
			return resultado;
		}

		function cadastrarNovasDiferencas() {
			
			var listaDiferencas = obterListaDiferencas();

			var dataMovimento = "dataMovimento=" + $("#datePickerDataMovimento").val();

			var tipoDiferenca = "&tipoDiferenca=" + $("#selectTiposDiferenca").val();

			$.postJSON(
				"<c:url value='/estoque/diferenca/lancamento/cadastrarNovasDiferencas' />", 
				listaDiferencas + dataMovimento + tipoDiferenca,
				function(result) {

					$("#gridLancamentos").flexOptions({
						url : '<c:url value="/estoque/diferenca/lancamento/pesquisa/novos" />',
						params: dataMovimento + tipoDiferenca
					});
					
					$("#gridLancamentos").flexReload();
					
					$(".grids").show();

					$("#btnConfirmar").show();

					$("#dialogNovasDiferencas").dialog("close");
				},
				tratarErroCadastroNovasDiferencas, 
				true
			);
		}

		function tratarErroCadastroNovasDiferencas(jsonData) {

			if (!jsonData || !jsonData.mensagens) {

				return;
			}

			var dadosValidacao = jsonData.mensagens.dados;
			
			var linhasDaGrid = $(".gridNovasDiferencas tr");

			$.each(linhasDaGrid, function(index, value) {

				var linha = $(value);

				if (dadosValidacao 
						&& ($.inArray(index, dadosValidacao) > -1)) {

					linha.removeClass('erow').addClass('linhaComErro');
					
				} else {

					linha.removeClass('linhaComErro');					
				}
			});
		}

		function obterListaDiferencas() {

			var linhasDaGrid = $(".gridNovasDiferencas tr");

			var listaDiferencas = "";

			$.each(linhasDaGrid, function(index, value) {

				var linha = $(value);
				
				var colunaCodigoProduto = linha.find("td")[0];
				var colunaDescricaoProduto = linha.find("td")[1];
				var colunaNumeroEdicao = linha.find("td")[2];
				var colunaQtdeEstoqueAtual = linha.find("td")[4]
				var colunaQtdeDiferenca = linha.find("td")[5];

				var id = 
					$(colunaCodigoProduto).find("div").find('input[name="id"]').val();
				
				var codigoProduto = 
					$(colunaCodigoProduto).find("div").find('input[name="codigoProduto"]').val();
				
				var descricaoProduto = 
					$(colunaDescricaoProduto).find("div").find('input[name="descricaoProduto"]').val();
				
				var numeroEdicao = 
					$(colunaNumeroEdicao).find("div").find('input[name="numeroEdicao"]').val();

				var qtdeEstoqueAtual =
					$(colunaQtdeEstoqueAtual).find("div").find('input[id^="qtdeRecebimentoFisico"]').val();
				
				var qtdeDiferenca = 
					$(colunaQtdeDiferenca).find("div").find('input[name="qtdeDiferenca"]').val();
				
				if (isAtributosDiferencaVazios(codigoProduto, descricaoProduto, numeroEdicao, qtdeDiferenca)) {

					return true;
				}


				var diferenca = 'listaNovasDiferencas[' + index + '].id=' + id + '&';
				
				diferenca += 'listaNovasDiferencas[' + index + '].codigoProduto=' + codigoProduto + '&';
	
				diferenca += 'listaNovasDiferencas[' + index + '].descricaoProduto=' + descricaoProduto + '&';
	
				diferenca += 'listaNovasDiferencas[' + index + '].numeroEdicao=' + numeroEdicao + '&';
	
				diferenca += 'listaNovasDiferencas[' + index + '].quantidade=' + qtdeDiferenca  + '&';

				diferenca += 'listaNovasDiferencas[' + index + '].qtdeEstoqueAtual=' + qtdeEstoqueAtual  + '&';

				listaDiferencas = (listaDiferencas + diferenca);
			});

			return listaDiferencas;
		}

		function obterDadosProduto(idCodigoProduto, idEdicaoProduto) {

			codigoProduto = $(idCodigoProduto).val();

			edicaoProduto = $(idEdicaoProduto).val();

			var data = "codigoProduto=" + codigoProduto
					 + "&numeroEdicao=" + edicaoProduto;
			
			$.postJSON(
				"<c:url value='/produto/obterProdutoEdicao' />", 
				data,
				function(produtoEdicao) {

					$("#precoVenda" + ultimaLinhaPreenchida).val(produtoEdicao.precoVenda);
					
					$("#precoVendaFormatado" + ultimaLinhaPreenchida).text(produtoEdicao.precoVenda);
					$("#precoVendaFormatado" + ultimaLinhaPreenchida).formatCurrency({region: 'pt-BR', decimalSymbol: ',', symbol: ''});

					$("#totalPrecoVendaDiferencas").text(($("input[id^='precoVenda']").sum()));
					$("#totalPrecoVendaDiferencas").formatCurrency({region: 'pt-BR', decimalSymbol: ',', symbol: ''});

					obterEstoqueProduto(produtoEdicao);
				},
				null, 
				true
			);
		}

		function obterEstoqueProduto(produtoEdicao) {

			if (!produtoEdicao) {

				return;
			}

			var data = "idProdutoEdicao=" + produtoEdicao.id;
			
			$.postJSON(
				"<c:url value='/produto/obterEstoque' />", 
				data,
				function(estoqueProduto) {
					
					if (estoqueProduto) {
						$("#qtdeRecebimentoFisico" + ultimaLinhaPreenchida).val(estoqueProduto.qtde);
						$("#qtdeRecebimentoFisicoFormatado" + ultimaLinhaPreenchida).text(estoqueProduto.qtde);
						$("#totalRecebimentoFisico").text(($("input[id^='qtdeRecebimentoFisico']").sum()));
					}
				},
				null, 
				true
			);
		}

		function isAtributosDiferencaVazios(codigoProduto, descricaoProduto, numeroEdicao, qtdeDiferenca) {

			if (!$.trim(codigoProduto) 
					&& !$.trim(descricaoProduto)
					&& !$.trim(numeroEdicao) 
					&& !$.trim(qtdeDiferenca)) {

				return true;
			}
		}

		function formatarCamposNumericos() {

			$("input[name='codigoProduto']").numeric();
			$("input[name='numeroEdicao']").numeric();
			$("input[name='qtdeDiferenca']").numeric();
		}

		function reprocessarDadosLancamento(idCampoPrecoVenda,
											idCampoPrecoVendaFormatado,
								  			idCampoQtdeRecebimentoFisico,
								  			idCampoQtdeRecebimentoFisicoFormatado) {

			$(idCampoPrecoVenda).val("");
			$(idCampoPrecoVendaFormatado).text("");

			var somaPrecoVenda = $("input[id^='precoVenda']").sum();
			
			$("#totalPrecoVendaDiferencas").text(somaPrecoVenda);
			$("#totalPrecoVendaDiferencas").formatCurrency({region: 'pt-BR', decimalSymbol: ',', symbol: ''});

			if (somaPrecoVenda == 0) {

				$("#totalPrecoVendaDiferencas").text("");
			}
						
			$(idCampoQtdeRecebimentoFisico).val("");
			$(idCampoQtdeRecebimentoFisicoFormatado).text("");

			var somaQtdeRecebimentoFisico = $("input[id^='qtdeRecebimentoFisico']").sum();
			
			$("#totalRecebimentoFisico").text((somaQtdeRecebimentoFisico));
			
			if (somaQtdeRecebimentoFisico == 0) {

				$("#totalRecebimentoFisico").text("");
			}
		}
	</script>
	
</div>