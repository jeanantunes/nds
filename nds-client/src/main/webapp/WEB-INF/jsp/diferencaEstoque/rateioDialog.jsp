<div id="dialogRateioDiferencas" title="Incluir Novo Tipo de Movimento" style="display: none;">

	<jsp:include page="../messagesDialog.jsp" />

	<table id="gridRateioDiferencas" class="gridRateioDiferencas"></table>
	
	<br />
	
	<table width="420" border="0" cellspacing="2" cellpadding="2">
		<tr style="font-size:11px;">
			<td id="labelTotalRateio" width="100"><strong>Total:</strong></td>
			<td id="totalRateio" width="10" align="center"></td>
		</tr>
    </table>
	
	<script language="javascript" type="text/javascript">

		var idDiferencaAtual;
	
		$(function() {
			
			$(".gridRateioDiferencas").flexigrid({
				preProcess: executarPreProcessamentoRateio,
				onSuccess: formatarCamposRateio,
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

			idDiferencaAtual = idDiferencaSelecionada;
			
			$("#dialogRateioDiferencas").dialog({
				resizable: false,
				height: 370,
				width: 557,
				modal: true,
				buttons: [
					{
						id: "btConfirmarRateio",
						text: "Confirmar",
						click: function() {
							cadastrasRateioCotas();
						}
					},
					{
						id: "btCancelarRateio",
						text: "Cancelar",
						click: function() {
							$(this).dialog("close")
						}
					}
				],
				beforeClose: function() {
					clearMessageDialogTimeout();
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

				var chamadaMetodoObterQuantidadeReparteCota = 'obterQuantidadeReparteCota(' + row.cell.idDiferenca + ', \'#numeroCota' + index  + '\', ' + index + ');';

				var parametroPesquisaCota = '\'#numeroCota' + index + '\', \'#nomeCota' + index + '\', true, function() {' + chamadaMetodoObterQuantidadeReparteCota + '}, null';

				var parametroAutoCompleteCota = '\'#nomeCota' + index + '\', true';

				var inputId = '<input name="id" type="hidden" value="' + row.cell.id + '" />';
				
				var inputIdDiferenca = '<input name="idDiferenca" type="hidden" value="' + row.cell.idDiferenca + '" />';
				
				var inputNumeroCota = '<input id="numeroCota' + index + '" name="numeroCota" type="text" style="width:80px; float:left; margin-right:5px;" onchange="pesquisarPorNumeroCota(' + parametroPesquisaCota + ');" value="' + numeroCota + '" />';

				var inputNomeCota = '<input id="nomeCota' + index + '" name="nomeCota" type="text" style="width:220px;" onkeyup="autoCompletarPorNome(' + parametroAutoCompleteCota + ');" onblur="pesquisarPorNomeCota(' + parametroPesquisaCota + ')" value="' + nomeCota + '" />';

				var inputReparteCota = '<input id="qtdeReparteCota' + index + '" name="qtdeReparteCota" type="hidden" value="' + reparteCota + '" />';
				
				var spanReparteCota = '<span id="spanQtdeReparteCota' + index + '">' + reparteCota + '</span>';
				
				var inputQtdeRateio = '<input id="quantidadeRateio' + index + '" name="quantidadeRateio" type="text" style="width:60px; text-align:center;" value="' + quantidade + '" />';

				row.cell.numeroCota = inputId + inputIdDiferenca + inputNumeroCota;
				row.cell.nomeCota = inputNomeCota;
				row.cell.reparteCota = inputReparteCota + spanReparteCota;
				row.cell.quantidade = inputQtdeRateio;
			});
			
			reprocessarDadosRateio(null, true);
			
			return resultado;
		}

		function obterQuantidadeReparteCota(idDiferenca, idCampoNumeroCota, index) {

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

					$("#qtdeReparteCota" + index).val(qtdeReparteCota);
					$("#spanQtdeReparteCota" + index).html(qtdeReparteCota);
					
					$('#quantidadeRateio' + index).focus();
					
					reprocessarDadosRateio(index);
				},
				null, 
				true
			);
		}

		function reprocessarDadosRateio(index, limpar) {

			var campoQtdeRateio = $('#quantidadeRateio' + index);
			
			if (campoQtdeRateio && limpar) {

				campoQtdeRateio.val("");

				campoQtdeRateio.focus();
			}

			var campoQtdeReparteCota = $('#qtdeReparteCota' + index);

			if (campoQtdeReparteCota && limpar) {

				campoQtdeReparteCota.val("");
			}

			var campoSpanQtdeReparteCota = $('#spanQtdeReparteCota' + index);

			if (campoSpanQtdeReparteCota && limpar) {

				campoSpanQtdeReparteCota.text("");
			}

			var linhaDaGrid = $(".gridRateioDiferencas tr").eq(index);

			if (linhaDaGrid) {

				linhaDaGrid.removeClass('linhaComErro');
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

		function formatarCamposRateio() {

			$("input[name='numeroCota']").numeric();
			$("input[name='quantidadeRateio']").numeric();
			
			$("input[name='nomeCota']").autocomplete({source: ""});
		}
	</script>
	
</div>