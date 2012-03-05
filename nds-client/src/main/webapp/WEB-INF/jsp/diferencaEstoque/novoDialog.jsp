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
	
	<table id="" width="465" border="0" cellspacing="2" cellpadding="2">
		<tr style="font-size: 11px;">
			<td id="labelTotalGeralNovo" width="329"><strong>Total Geral:</strong></td>
			<td id="totalPrecoVendaDiferencas" width="71" align="right"></td>
			<td id="totalRecebimentoFisico" width="45" align="right"></td>
		</tr>
	</table>
	
	<script language="javascript" type="text/javascript">
	
		$(function() {

			$(".gridNovasDiferencas").flexigrid({
				preProcess: executarPreProcessamentoNovo,
				onSuccess: formatarCamposNumericos,
				dataType : 'json',
				colModel : [{
					display : 'Código',
					name : 'codigoProduto',
					width : 70,
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
				width : 580,
				height : 220
			});

			$("#labelTotalGeralNovo").hide();
			$("#totalPrecoVendaDiferencas").hide();
			$("#totalRecebimentoFisico").hide();
		});

		function popupNovasDiferencas() {

			var formData = $('#pesquisaLancamentoDiferencaForm').serializeArray();

			$("#gridNovasDiferencas").flexOptions({
				url : '<c:url value="/estoque/diferenca/lancamento/novo" />', 
				params: formData
			});
			
			$("#gridNovasDiferencas").flexReload();
			
			$("#dialogNovasDiferencas").dialog({
				resizable: false,
				height:390,
				width:600,
				modal: true,
				buttons: {
					"Confirmar": function() {
						cadastrarNovasDiferencas();
					},
					"Cancelar": function() {
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

				var inputCodigoProduto = 
					'<input type="text" name="codigoProduto" style="width:60px;" maxlenght="255" />';

				var inputDescricaoProduto = 
					'<input type="text" name="descricaoProduto" style="width:140px;" maxlenght="255" />';

				var inputNumeroEdicao = 
					'<input type="text" name="numeroEdicao" style="width:40px;" maxlenght="20" />';

				var inputQuantidade = 
					'<input type="text" name="qtdeDiferenca" style="width:60px;" maxlenght="20" />';
								
				row.cell.codigoProduto = inputCodigoProduto;
				row.cell.descricaoProduto = inputDescricaoProduto;
				row.cell.numeroEdicao = inputNumeroEdicao;
				row.cell.precoVenda = "";
				row.cell.qtdeRecebimentoFisico = "";
				row.cell.quantidade = inputQuantidade;
			});

			return resultado;
		}

		function cadastrarNovasDiferencas() {
			
			var listaDiferencas = obterListaDiferencas();

			$.postJSON(
				"<c:url value='/estoque/diferenca/lancamento/cadastrarNovasDiferencas' />", 
				listaDiferencas,
				function(result) {
					$("#dialogNovasDiferencas").dialog("close");
					$(".grids").show();
				},
				null, 
				true
			);
		}

		function obterListaDiferencas() {

			var linhasDaGrid = $(".gridNovasDiferencas tr");

			var listaDiferencas = "";

			$.each(linhasDaGrid, function(index, value) {

				var linha = $(value);
				
				var colunaCodigoProduto = linha.find("td")[0];
				var colunaDescricaoProduto = linha.find("td")[1];
				var colunaNumeroEdicao = linha.find("td")[2];
				var colunaQtdeDiferenca = linha.find("td")[5];
	
				var codigoProduto = 
					$(colunaCodigoProduto).find("div").find('input[name="codigoProduto"]').val();
				
				var descricaoProduto = 
					$(colunaDescricaoProduto).find("div").find('input[name="descricaoProduto"]').val();
				
				var numeroEdicao = 
					$(colunaNumeroEdicao).find("div").find('input[name="numeroEdicao"]').val();
				
				var qtdeDiferenca = 
					$(colunaQtdeDiferenca).find("div").find('input[name="qtdeDiferenca"]').val();

				if (isAtributosDiferencaVazios(codigoProduto, descricaoProduto, numeroEdicao, qtdeDiferenca)) {

					return true;
				}

				var diferenca = 'listaNovasDiferencas[' + index + '].codigoProduto=' + codigoProduto + '&';
	
				diferenca += 'listaNovasDiferencas[' + index + '].descricaoProduto=' + descricaoProduto + '&';
	
				diferenca += 'listaNovasDiferencas[' + index + '].numeroEdicao=' + numeroEdicao + '&';
	
				diferenca += 'listaNovasDiferencas[' + index + '].quantidade=' + qtdeDiferenca  + '&';

				listaDiferencas = (listaDiferencas + diferenca);
			});

			return listaDiferencas;
		}

		function validarNovaDiferenca(codigoProduto, descricaoProduto, numeroEdicao, qtdeDiferenca) {
			
			return false;
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
	</script>
	
</div>