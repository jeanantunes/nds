<div id="dialogRateioDiferencas" title="Lançamento Faltas e Sobras" style="display: none;">

	<div class="effectDialog ui-state-highlight ui-corner-all" 
		 style="display: none; position: absolute; z-index: 2000; width: 600px;">
		 
		<p>
			<span style="float: left;" class="ui-icon ui-icon-info"></span>
			<b class="effectDialogText"></b>
		</p>
	</div>

	<table id="gridRateioDiferencas" class="gridRateioDiferencas"></table>
	
	<br />
	
	<table width="415" border="0" cellspacing="2" cellpadding="2">
		<tr style="font-size:11px;">
			<td id="labelTotalRateio" width="314"><strong>Total:</strong></td>
			<td id="totalRateio" width="87" align="right"></td>
		</tr>
    </table>
	
	<script language="javascript" type="text/javascript">

		$(function() {
			
			$(".gridRateioDiferencas").flexigrid({
				preProcess: executarPreProcessamentoRateio,
				onSuccess: formatarCamposNumericosRateio,
				dataType : 'json',
				colModel : [{
					display : 'Cota',
					name : 'numeroCota',
					width : 110,
					sortable : false,
					align : 'left'
				},{
					display : 'Nome',
					name : 'nomeCota',
					width : 227,
					sortable : false,
					align : 'left'
				}, {
					display : 'Reparte',
					name : 'reparteCota',
					width : 50,
					sortable : false,
					align : 'center'
				}, {
					display : 'Quantidade',
					name : 'quantidade',
					width : 70,
					sortable : false,
					align : 'center'
				}],
				width : 527,
				height : 180,
				disableSelect : true
			});
		});

		function popupRateioDiferenca(idDiferencaSelecionada) {

			var rateioData = [
  				{
  					name: 'dataMovimento', value: $("#datePickerDataMovimento").val()
  				},
  				{
  					name: 'tipoDiferenca', value: $("#selectTiposDiferenca").val()
  				},
  				{
  					name: 'idDiferenca', value: idDiferencaSelecionada
  				}
  			];

			$("#gridRateioDiferencas").flexOptions({
				url : '<c:url value="/estoque/diferenca/lancamento/rateio" />', 
				params: rateioData
			});
			
			$("#gridRateioDiferencas").flexReload();
			
			$(".effectDialog").hide();
			
			$("#dialogRateioDiferencas").dialog({
				resizable: false,
				height: 370,
				width: 557,
				modal: true,
				buttons: {
					"Confirmar": function() {
						$(this).dialog("close");
					},
					"Cancelar": function() {
						$("#gridRateioDiferencas").flexAddData({rows:[]});
						$(this).dialog("close");
					}
				}
			});     
		}

		function executarPreProcessamentoRateio(resultado) {

			if (resultado.mensagens) {

				exibirMensagemDialog(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
				);

				return resultado;
			}

			$.each(resultado.rows, function(index, row) {

				var parametroLimparCamposPesquisa = '\'#nomeCota' + index + '\',  function() {reprocessarDadosRateio(\'#quantidadeRateio' + index + '\')}';

				var parametroPesquisaCota = '\'#numeroCota' + index + '\', \'#nomeCota' + index + '\', true, null, function() {reprocessarDadosRateio(\'#quantidadeRateio' + index + '\')}';

				var parametroAutoCompleteCota = '\'#nomeCota' + index + '\', true';
				
				var inputNumeroCota = '<input id="numeroCota' + index + '" name="numeroCota" type="text" style="width:80px; float:left; margin-right:5px;" onchange="cota.limparCamposPesquisa(' + parametroLimparCamposPesquisa + ')" />';

				var imgLupaPesquisa = '<span class="classPesquisar" title="Pesquisar Cota"><a href="javascript:;" onclick="cota.pesquisarPorNumeroCota(' + parametroPesquisaCota + ');">&nbsp;</a></span>';

				var inputNomeCota = '<input id="nomeCota' + index + '" name="nomeCota" type="text" style="width:220px;" onkeyup="cota.autoCompletarPorNome(' + parametroAutoCompleteCota + ');" onchange="cota.pesquisarPorNomeCota(' + parametroPesquisaCota + ')"  />';

				var spanReparteCota = '<span id="qtdeReparteCota' + index + '"/>';
				
				var inputQtdeRateio = '<input id="quantidadeRateio' + index + '" name="quantidadeRateio" type="text" style="width:60px; text-align:center;" />';

				row.cell.numeroCota = inputNumeroCota + imgLupaPesquisa;
				row.cell.nomeCota = inputNomeCota;
				row.cell.reparteCota = spanReparteCota;
				row.cell.quantidade = inputQtdeRateio;
			});

			$("#totalRateio").empty();
			
			return resultado;
		}

		function reprocessarDadosRateio(idCampoQtdeRateio) {
			
			$(idCampoQtdeRateio).val("");
			
			var somaQtdeRateio = $("input[id^='quantidadeRateio']").sum();
			
			$("#totalRateio").text(somaQtdeRateio);
			
			if (somaQtdeRateio == 0) {
			
				$("#totalRateio").text("");
			}
		}

		function formatarCamposNumericosRateio() {

			$("input[name='numeroCota']").numeric();
			$("input[name='quantidadeRateio']").numeric();
		}
	</script>
	
</div>