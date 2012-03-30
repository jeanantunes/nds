<div id="dialog-novo" title="Incluir Novo Tipo de Movimento" style="display: none;">

	<div class="effectDialog ui-state-highlight ui-corner-all" 
		 style="display: none; position: absolute; z-index: 2000; width: 750px;">
		 
		<p>
			<span style="float: left;" class="ui-icon ui-icon-info"></span>
			<b class="effectDialogText"></b>
		</p>
	</div>

	<table width="650" border="0" cellspacing="2" cellpadding="2">
	  <tr>
	    <td width="119">Tipo de Lançamento:</td>
	    <td width="517">
			<select name="debitoCredito.tipoMovimentoFinanceiro.id" id="novoTipoMovimento" style="width:300px;">
		  		<option selected="selected"> </option>
				<c:forEach items="${tiposMovimentoFinanceiro}" var="tipoMovimento">
					<option value="${tipoMovimento.id}">${tipoMovimento.descricao}</option>
				</c:forEach>
		    </select>
	    </td>
	  </tr>
	</table>
	
	<br />

	<table class="debitosCreditosGrid_1" id="debitosCreditosGrid_1"></table>
	
	<script language="javascript" type="text/javascript">

		$(".debitosCreditosGrid_1").flexigrid({
			preProcess: executarPreProcessamentoRateio,
			onSuccess: configurarCampos,
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 90,
				sortable : false,
				align : 'left'
			},{
				display : 'Nome',
				name : 'nome',
				width : 185,
				sortable : false,
				align : 'left'
			}, {
				display : 'Data',
				name : 'data',
				width : 80,
				sortable : false,
				align : 'center'
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 90,
				sortable : false,
				align : 'center'
			}, {
				display : 'Observação',
				name : 'observacao',
				width : 230,
				sortable : false,
				align : 'left'
			}],
			disableSelect : true,
			width : 760,
			height : 230
		});
		
		function configurarCampos() {
			
			$("input[id^='data']").datepicker();
			
			$("input[id^='data']").mask("99/99/9999");
			
			$("input[id^='valor']").maskMoney({
				 thousands:'.', 
				 decimal:',', 
				 precision:2
			});
			
			$("input[id^='numeroCota']").numeric();
		}

// 		function popupNovoDialog() {
		
// 			$( "#dialog-novo" ).dialog({
// 				resizable: false,
// 				height:450,
// 				width:780,
// 				modal: true,
// 				buttons: {
// 					"Confirmar": function() {
// 						$( this ).dialog( "close" );
// 						$("#effect").show("highlight", {}, 1000, callback);
// 						$(".grids").show();
						
// 					},
// 					"Cancelar": function() {
// 						$( this ).dialog( "close" );
// 					}
// 				}
// 			});
// 		};

		function popupNovoDialog() {
			
			$("#novoTipoMovimento").val(0);

			$("#debitosCreditosGrid_1").flexOptions({
				url : '<c:url value="/financeiro/debitoCreditoCota/novoMovimento" />', 
			});
			
			$("#debitosCreditosGrid_1").flexReload();
			
			$(".effectDialog").hide();
			
			$("#dialog-novo").dialog({
				resizable: false,
				height:450,
				width:780,
				modal: true,
				buttons: {
					"Confirmar": function() {
						salvarMovimentoFinanceiro();
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

				var parametroPesquisaCota = '\'#numeroCota' + index + '\', \'#nomeCota' + index + '\', true';

				var parametroAutoCompleteCota = '\'#nomeCota' + index + '\', true';
				
				var inputNumeroCota = '<input id="numeroCota' + index + '" maxlength="9" name="debitoCredito.numeroCota" type="text" style="width:80px; float:left; margin-right:5px;" onchange="cota.pesquisarPorNumeroCota(' + parametroPesquisaCota + ');" />';

				var inputNomeCota = '<input id="nomeCota' + index + '" name="debitoCredito.nomeCota" type="text" style="width:180px;" onkeyup="cota.autoCompletarPorNome(' + parametroAutoCompleteCota + ');" onchange="cota.pesquisarPorNomeCota(' + parametroPesquisaCota + ')" />';
				
				var inputData = '<input id="data' + index + '" name="debitoCredito.dataLancamento" type="text" style="width:70px;" />';
				
				var inputValor = '<input id="valor' + index + '" name="debitoCredito.valor" type="text" style="width:80px;" />';
				
				var inputObservacao = '<input id="observacao' + index + '" name="debitoCredito.observacao" type="text" style="width:220px;" />';

				row.cell[0] = inputNumeroCota;
				row.cell[1] = inputNomeCota;
				row.cell[2] = inputData;
				row.cell[3] = inputValor;
				row.cell[4] = inputObservacao;
			});
			
			return resultado;
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
		
		function obterListaMovimentos() {

			var linhasDaGrid = $(".debitosCreditosGrid_1 tr");

			var listaMovimentos = "";

			$.each(linhasDaGrid, function(index, value) {

				var linha = $(value);
				
				var colunaNumeroCota = linha.find("td")[0];
				var colunaNomeCota = linha.find("td")[1];
				var colunaData = linha.find("td")[2];
				var colunaValor = linha.find("td")[3];
				var colunaObservacao = linha.find("td")[4];

				var numeroCota = 
					$(colunaNumeroCota).find("div").find('input[name="debitoCredito.numeroCota"]').val();
				
				var nomeCota = 
					$(colunaNomeCota).find("div").find('input[name="debitoCredito.nomeCota"]').val();

				var data = 
					$(colunaData).find("div").find('input[name="debitoCredito.dataLancamento"]').val();

				var valor =
					$(colunaValor).find("div").find('input[name="debitoCredito.valor"]').val();
				
				var observacao =
					$(colunaObservacao).find("div").find('input[name="debitoCredito.observacao"]').val();
				
				if (isAtributosMovimentoVazios(numeroCota, nomeCota, data, valor, observacao)) {

					return true;
				}

				var movimento = 'listaNovosDebitoCredito[' + index + '].numeroCota=' + numeroCota + '&';

				movimento += 'listaNovosDebitoCredito[' + index + '].nomeCota=' + nomeCota + '&';
	
				movimento += 'listaNovosDebitoCredito[' + index + '].dataVencimento=' + data + '&';
	
				movimento += 'listaNovosDebitoCredito[' + index + '].valor=' + valor + '&';
				
				movimento += 'listaNovosDebitoCredito[' + index + '].observacao=' + observacao + '&';

				listaMovimentos = (listaMovimentos + movimento);
			});

			return listaMovimentos;
		}

		function isAtributosMovimentoVazios(numeroCota, nomeCota, data, valor, observacao) {

			if (!$.trim(numeroCota) 
					&& !$.trim(nomeCota)
					&& !$.trim(data) 
					&& !$.trim(valor)
					&& !$.trim(observacao)) {

				return true;
			}
		}
	</script>
	
</div>