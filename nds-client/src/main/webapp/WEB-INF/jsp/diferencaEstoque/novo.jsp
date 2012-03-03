<div id="dialogNovasDiferencas" title="Lançamento < Faltas e Sobras >" style="display: none;">

	<table id="gridNovasDiferencas" class="gridNovasDiferencas"></table>
	
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
					width : 80,
					sortable : false,
					align : 'center'
				}],
				width : 600,
				height : 220
			});

			$("#labelTotalGeralNovo").hide();
			$("#totalPrecoVendaDiferencas").hide();
			$("#totalRecebimentoFisico").hide();
		});

		function popupNovasDiferencas() {

			var formData = $('#pesquisaLancamentoDiferencaForm').serializeArray();

			$("#gridNovasDiferencas").flexOptions({url : '<c:url value="/estoque/diferenca/lancamento/novo" />', params: formData});
			
			$("#gridNovasDiferencas").flexReload();
			
			$("#dialogNovasDiferencas" ).dialog({
				resizable: false,
				height:390,
				width:600,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$( this ).dialog( "close" );
						$("#effect").hide("highlight", {}, 1000, callback);
						mostrar_1();
						
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				}
			});
		}

		function executarPreProcessamentoNovo(resultado) {

			if (resultado.mensagens) {

				exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);

				return resultado;
			}

			$.each(resultado.rows, function(index, row) {

				var inputCodigoProduto = '<input type="text" style="width:60px;" />';

				var inputDescricaoProduto = '<input type="text" style="width:140px;" />';

				var inputNumeroEdicao = '<input type="text" style="width:40px;" />';

				var inputQuantidade = '<input type="text" style="width:70px;" />';
								
				row.cell.codigoProduto = inputCodigoProduto;
				row.cell.descricaoProduto = inputDescricaoProduto;
				row.cell.numeroEdicao = inputNumeroEdicao;
				row.cell.quantidade = inputQuantidade;
			});

			return resultado;
		}
	</script>
	
</div>