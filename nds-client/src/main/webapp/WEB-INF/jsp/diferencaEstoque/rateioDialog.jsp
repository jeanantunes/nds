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
	
	<table width="420" border="0" cellspacing="2" cellpadding="2">
		<tr style="font-size:11px;">
			<td id="labelTotalRateio" width="100"><strong>Total:</strong></td>
			<td id="totalRateio" width="10" align="center"></td>
		</tr>
    </table>
	
	<script language="javascript" type="text/javascript">

		var ultimaLinhaPreenchida;

		var idDiferencaAtual;
	
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
  					name: 'idDiferenca', value: idDiferencaSelecionada
  				}
  			];

			$("#gridRateioDiferencas").flexOptions({
				url : '<c:url value="/estoque/diferenca/lancamento/rateio" />', 
				params: rateioData
			});
			
			$("#gridRateioDiferencas").flexReload();
			
			$(".effectDialog").hide();

			idDiferencaAtual = idDiferencaSelecionada;
			
			$("#dialogRateioDiferencas").dialog({
				resizable: false,
				height: 370,
				width: 557,
				modal: true,
				buttons: {
					"Confirmar": function() {
						cadastrasRateioCotas();
					},
					"Cancelar": function() {
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

				var numeroCota = row.cell.numeroCota ? row.cell.numeroCota : '';

				var nomeCota = row.cell.nomeCota ? row.cell.nomeCota : '';

				var reparteCota = row.cell.reparteCota ? row.cell.reparteCota : '';

				var quantidade = row.cell.quantidade ? row.cell.quantidade : ''; 

				var parametroLimparCamposPesquisa = '\'#nomeCota' + index + '\',  function() {reprocessarDadosRateio(\'#quantidadeRateio' + index + '\')}';

				var chamadaMetodoObterQuantidadeReparteCota = 'obterQuantidadeReparteCota(' + row.cell.idDiferenca + ', \'#numeroCota' + index  + '\', \'#quantidadeRateio' + index + '\'); ultimaLinhaPreenchida=' + index + ';';

				var parametroPesquisaCota = '\'#numeroCota' + index + '\', \'#nomeCota' + index + '\', true, function() {' + chamadaMetodoObterQuantidadeReparteCota + '}, null';

				var parametroAutoCompleteCota = '\'#nomeCota' + index + '\', true';

				var inputId = '<input name="id" type="hidden" value="' + row.cell.id + '" />';
				
				var inputIdDiferenca = '<input name="idDiferenca" type="hidden" value="' + row.cell.idDiferenca + '" />';
				
				var inputNumeroCota = '<input id="numeroCota' + index + '" name="numeroCota" type="text" style="width:80px; float:left; margin-right:5px;" onchange="cota.limparCamposPesquisa(' + parametroLimparCamposPesquisa + ')" value="' + numeroCota + '" />';

				var imgLupaPesquisa = '<span class="classPesquisar" title="Pesquisar Cota"><a href="javascript:;" onclick="cota.pesquisarPorNumeroCota(' + parametroPesquisaCota + ');">&nbsp;</a></span>';

				var inputNomeCota = '<input id="nomeCota' + index + '" name="nomeCota" type="text" style="width:220px;" onkeyup="cota.autoCompletarPorNome(' + parametroAutoCompleteCota + ');" onchange="cota.pesquisarPorNomeCota(' + parametroPesquisaCota + ')" value="' + nomeCota + '" />';

				var inputReparteCota = '<input id="qtdeReparteCota' + index + '" name="qtdeReparteCota" type="hidden" value="' + reparteCota + '" />';
				
				var spanReparteCota = '<span id="spanQtdeReparteCota' + index + '">' + reparteCota + '</span>';
				
				var inputQtdeRateio = '<input id="quantidadeRateio' + index + '" name="quantidadeRateio" type="text" style="width:60px; text-align:center;" value="' + quantidade + '" />';

				row.cell.numeroCota = inputId + inputIdDiferenca + inputNumeroCota + imgLupaPesquisa;
				row.cell.nomeCota = inputNomeCota;
				row.cell.reparteCota = inputReparteCota + spanReparteCota;
				row.cell.quantidade = inputQtdeRateio;
			});
			
			reprocessarDadosRateio();
			
			return resultado;
		}

		function obterQuantidadeReparteCota(idDiferenca, idCampoNumeroCota, idCampoQtdeRateio) {

			var data = [
   				{
   					name: 'idDiferenca', value: idDiferenca
   				},
   				{
   					name: 'numeroCota', value: $(idCampoNumeroCota).val()
   				}
   			];
			
			$.postJSON(
				"<c:url value='/estoque/diferenca/lancamento/rateio/obterQuantidadeReparte' />", 
				data,
				function(qtdeReparteCota) {

					$("#qtdeReparteCota" + ultimaLinhaPreenchida).val(qtdeReparteCota);
					$("#spanQtdeReparteCota" + ultimaLinhaPreenchida).text(qtdeReparteCota);

					reprocessarDadosRateio(idCampoQtdeRateio);
				},
				null, 
				true
			);
		}

		function reprocessarDadosRateio(idCampoQtdeRateio) {

			if (idCampoQtdeRateio) {

				$(idCampoQtdeRateio).val("");
			}
			
			var somaQtdeRateio = $("input[id^='qtdeReparteCota']").sum();
			
			$("#totalRateio").text(somaQtdeRateio);
			
			if (somaQtdeRateio == 0) {
			
				$("#totalRateio").text("");
			}
		}

		function cadastrasRateioCotas() {

			var listaRateioCotas = obterListaRateioCotas();

			$.postJSON(
				"<c:url value='/estoque/diferenca/lancamento/cadastrarRateioCotas' />", 
				listaRateioCotas + 'idDiferenca=' + idDiferencaAtual,
				function(result) {
					$("#dialogRateioDiferencas").dialog("close");
				},
				tratarErroCadastroNovosRateios, 
				true
			);
		}

		function tratarErroCadastroNovosRateios(jsonData) {

			if (!jsonData || !jsonData.mensagens) {

				return;
			}

			var dadosValidacao = jsonData.mensagens.dados;
			
			var linhasDaGrid = $(".gridRateioDiferencas tr");

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
		
		function obterListaRateioCotas() {

			var linhasDaGrid = $(".gridRateioDiferencas tr");

			var listaRateioCotas = "";

			$.each(linhasDaGrid, function(index, value) {

				var linha = $(value);
				
				var colunaNumeroCota = linha.find("td")[0];
				var colunaNomeCota = linha.find("td")[1];
				var colunaReparteCota = linha.find("td")[2];
				var colunaQtdeRateio = linha.find("td")[3]

				var id = $(colunaNumeroCota).find("div").find('input[name="id"]').val();
				
				var idDiferenca = 
					$(colunaNumeroCota).find("div").find('input[name="idDiferenca"]').val();
					
				var numeroCota = 
					$(colunaNumeroCota).find("div").find('input[name="numeroCota"]').val();
				
				var nomeCota = 
					$(colunaNomeCota).find("div").find('input[name="nomeCota"]').val();

				var reparteCota = 
					$(colunaReparteCota).find("div").find('input[name="qtdeReparteCota"]').val();

				var qtdeRateio =
					$(colunaQtdeRateio).find("div").find('input[name="quantidadeRateio"]').val();
				
				if (isAtributosRateioVazios(numeroCota, nomeCota, reparteCota, qtdeRateio)) {

					return true;
				}

				var rateio = 'listaNovosRateios[' + index + '].id=' + id + '&';
				
				rateio += 'listaNovosRateios[' + index + '].idDiferenca=' + idDiferenca + '&';

				rateio += 'listaNovosRateios[' + index + '].numeroCota=' + numeroCota + '&';

				rateio += 'listaNovosRateios[' + index + '].nomeCota=' + nomeCota + '&';
	
				rateio += 'listaNovosRateios[' + index + '].reparteCota=' + reparteCota + '&';
	
				rateio += 'listaNovosRateios[' + index + '].quantidade=' + qtdeRateio + '&';

				listaRateioCotas = (listaRateioCotas + rateio);
			});

			return listaRateioCotas;
		}

		function isAtributosRateioVazios(numeroCota, nomeCota, reparteCota, qtdeRateio) {

			if (!$.trim(numeroCota) 
					&& !$.trim(nomeCota)
					&& !$.trim(reparteCota) 
					&& !$.trim(qtdeRateio)) {

				return true;
			}
		}

		function formatarCamposNumericosRateio() {

			$("input[name='numeroCota']").numeric();
			$("input[name='quantidadeRateio']").numeric();
		}
	</script>
	
</div>