var lancamentoRateioController = $.extend(true, {

	idDiferencaAtual : "",

	init : function () {
		$(".gridRateioDiferencas", lancamentoRateioController.workspace).flexigrid({
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
		
	},
	
	popupRateioDiferenca : function(idDiferencaSelecionada) {

		var rateioData = [
				{
					name: 'idDiferenca', value: idDiferencaSelecionada
				}
			];

		$("#gridRateioDiferencas", lancamentoRateioController.workspace).flexOptions({
			url : contextPath + '/estoque/diferenca/lancamento/rateio', 
			params: rateioData
		});
		
		$("#gridRateioDiferencas", lancamentoRateioController.workspace).flexReload();

		idDiferencaAtual = idDiferencaSelecionada;
		
		$("#dialogRateioDiferencas", lancamentoRateioController.workspace).dialog({
			resizable: false,
			height: 370,
			width: 557,
			modal: true,
			buttons: [
				{
					id: "btConfirmarRateio",
					text: "Confirmar",
					click: function() {
						lancamentoRateioController.cadastrasRateioCotas();
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
			},
			form: $("#dialogRateioDiferencas", this.workspace).parents("form")
		});     
	},

	executarPreProcessamentoRateio : function(resultado) {

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

			var chamadaMetodoObterQuantidadeReparteCota = 'lancamentoRateioController.obterQuantidadeReparteCota(' + row.cell.idDiferenca + ', \'#numeroCota' + index  + '\', ' + index + ');';

			var parametroPesquisaCota = '\'#numeroCota' + index + '\', \'#nomeCota' + index + '\', true, function() {' + chamadaMetodoObterQuantidadeReparteCota + '}, null';

			var parametroAutoCompleteCota = '\'#nomeCota' + index + '\', true';

			var inputId = '<input name="id" type="hidden" value="' + row.cell.id + '" />';
			
			var inputIdDiferenca = '<input name="idDiferenca" type="hidden" value="' + row.cell.idDiferenca + '" />';
			
			var inputNumeroCota = '<input id="numeroCota' + index + '" name="numeroCota" type="text" style="width:80px; float:left; margin-right:5px;" onchange="lancamentoRateioController.pesquisarPorNumeroCota(' + parametroPesquisaCota + ');" value="' + numeroCota + '" />';

			var inputNomeCota = '<input id="nomeCota' + index + '" name="nomeCota" type="text" style="width:220px;" onkeyup="lancamentoRateioController.autoCompletarPorNome(' + parametroAutoCompleteCota + ');" onblur="lancamentoRateioController.pesquisarPorNomeCota(' + parametroPesquisaCota + ')" value="' + nomeCota + '" />';

			var inputReparteCota = '<input id="qtdeReparteCota' + index + '" name="qtdeReparteCota" type="hidden" value="' + reparteCota + '" />';
			
			var spanReparteCota = '<span id="spanQtdeReparteCota' + index + '">' + reparteCota + '</span>';
			
			var inputQtdeRateio = '<input id="quantidadeRateio' + index + '" name="quantidadeRateio" type="text" style="width:60px; text-align:center;" value="' + quantidade + '" />';

			row.cell.numeroCota = inputId + inputIdDiferenca + inputNumeroCota;
			row.cell.nomeCota = inputNomeCota;
			row.cell.reparteCota = inputReparteCota + spanReparteCota;
			row.cell.quantidade = inputQtdeRateio;
		});
		
		lancamentoRateioController.reprocessarDadosRateio(null, true);
		
		return resultado;
	},

	obterQuantidadeReparteCota : function(idDiferenca, idCampoNumeroCota, index) {

		var data = [
				{
					name: 'idDiferenca', value: idDiferenca
				},
				{
					name: 'numeroCota', value: $(idCampoNumeroCota).val()
				}
			];
		
		$.postJSON(
			contextPath + "/estoque/diferenca/lancamento/rateio/obterQuantidadeReparte", 
			data,
			function(qtdeReparteCota) {

				$("#qtdeReparteCota" + index, lancamentoRateioController.workspace).val(qtdeReparteCota);
				$("#spanQtdeReparteCota" + index, lancamentoRateioController.workspace).html(qtdeReparteCota);
				
				$('#quantidadeRateio' + index, lancamentoRateioController.workspace).focus();
				
				lancamentoRateioController.reprocessarDadosRateio(index);
			},
			null, 
			true
		);
	},

	reprocessarDadosRateio : function(index, limpar) {

		var campoQtdeRateio = $('#quantidadeRateio' + index, lancamentoRateioController.workspace);
		
		if (campoQtdeRateio && limpar) {

			campoQtdeRateio.val("");

			campoQtdeRateio.focus();
		}

		var campoQtdeReparteCota = $('#qtdeReparteCota' + index, lancamentoRateioController.workspace);

		if (campoQtdeReparteCota && limpar) {

			campoQtdeReparteCota.val("");
		}

		var campoSpanQtdeReparteCota = $('#spanQtdeReparteCota' + index, lancamentoRateioController.workspace);

		if (campoSpanQtdeReparteCota && limpar) {

			campoSpanQtdeReparteCota.text("");
		}

		var linhaDaGrid = $(".gridRateioDiferencas tr", lancamentoRateioController.workspace).eq(index);

		if (linhaDaGrid) {

			linhaDaGrid.removeClass('linhaComErro');
		}
		
		var somaQtdeRateio = $("input[id^='qtdeReparteCota']", lancamentoRateioController.workspace).sum();
		
		$("#totalRateio", lancamentoRateioController.workspace).text(somaQtdeRateio);
		
		if (somaQtdeRateio == 0) {
		
			$("#totalRateio", lancamentoRateioController.workspace).text("");
		}
	},

	cadastrasRateioCotas : function() {

		var listaRateioCotas = obterListaRateioCotas();

		$.postJSON(
			contextPath + "/estoque/diferenca/lancamento/cadastrarRateioCotas", 
			listaRateioCotas + 'idDiferenca=' + idDiferencaAtual,
			function(result) {
				$("#dialogRateioDiferencas", lancamentoRateioController.workspace).dialog("close");
			},
			lancamentoRateioController.tratarErroCadastroNovosRateios, 
			true
		);
	},

	tratarErroCadastroNovosRateios : function(jsonData) {

		if (!jsonData || !jsonData.mensagens) {

			return;
		}

		var dadosValidacao = jsonData.mensagens.dados;
		
		var linhasDaGrid = $(".gridRateioDiferencas tr", lancamentoRateioController.workspace);

		$.each(linhasDaGrid, function(index, value) {

			var linha = $(value);

			if (dadosValidacao 
					&& ($.inArray(index, dadosValidacao) > -1)) {

				linha.removeClass('erow').addClass('linhaComErro');
				
			} else {

				linha.removeClass('linhaComErro');					
			}
		});
	},
	
	obterListaRateioCotas : function() {

		var linhasDaGrid = $(".gridRateioDiferencas tr", lancamentoRateioController.workspace);

		var listaRateioCotas = "";

		$.each(linhasDaGrid, function(index, value) {

			var linha = $(value);
			
			var colunaNumeroCota = linha.find("td")[0];
			var colunaNomeCota = linha.find("td")[1];
			var colunaReparteCota = linha.find("td")[2];
			var colunaQtdeRateio = linha.find("td")[3]

			var id = $(colunaNumeroCota, lancamentoRateioController.workspace).find("div").find('input[name="id"]').val();
			
			var idDiferenca = 
				$(colunaNumeroCota, lancamentoRateioController.workspace).find("div").find('input[name="idDiferenca"]').val();
				
			var numeroCota = 
				$(colunaNumeroCota, lancamentoRateioController.workspace).find("div").find('input[name="numeroCota"]').val();
			
			var nomeCota = 
				$(colunaNomeCota, lancamentoRateioController.workspace).find("div").find('input[name="nomeCota"]').val();

			var reparteCota = 
				$(colunaReparteCota, lancamentoRateioController.workspace).find("div").find('input[name="qtdeReparteCota"]').val();

			var qtdeRateio =
				$(colunaQtdeRateio, lancamentoRateioController.workspace).find("div").find('input[name="quantidadeRateio"]').val();
			
			if (lancamentoRateioController.isAtributosRateioVazios(numeroCota, nomeCota, reparteCota, qtdeRateio)) {

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
	},

	isAtributosRateioVazios : function(numeroCota, nomeCota, reparteCota, qtdeRateio) {

		if (!$.trim(numeroCota) 
				&& !$.trim(nomeCota)
				&& !$.trim(reparteCota) 
				&& !$.trim(qtdeRateio)) {

			return true;
		}
	},

	formatarCamposRateio : function() {

		$("input[name='numeroCota']", lancamentoRateioController.workspace).numeric();
		$("input[name='quantidadeRateio']", lancamentoRateioController.workspace).numeric();
		
		$("input[name='nomeCota']", lancamentoRateioController.workspace).autocomplete({source: ""});
	}
	
}, BaseController);
