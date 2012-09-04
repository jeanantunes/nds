var lancamentoNovoController = $.extend(true, {

	ultimaLinhaPreenchida : "",

	init : function () {
		$("#dateNotaEnvio", lancamentoNovoController.workspace).datepicker({
			showOn : "button",
			buttonImage : contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly : true,
			dateFormat: "dd/mm/yy"
		});
		
		$("#edicaoProdutoInput", lancamentoNovoController.workspace).numeric();
		$("#diferencaProdutoInput", lancamentoNovoController.workspace).numeric();
		$("#cotaInput1", lancamentoNovoController.workspace).numeric();
		$("#diferencaInput1", lancamentoNovoController.workspace).numeric();
		$("#cotaInputNota", lancamentoNovoController.workspace).numeric();
		$("#dateNotaEnvio", lancamentoNovoController.workspace).mask("99/99/9999");
		
		$(".lanctoFaltasSobrasCota_3Grid", lancamentoNovoController.workspace).flexigrid({
			preProcess: lancamentoNovoController.executarPreProcessamentoNovo,
			onSuccess: function(){$("[name=diferencaProduto]", lancamentoNovoController.workspace).numeric();},
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigoProduto',
				width : 50,
				sortable : false,
				align : 'left'
			},{
				display : 'Produto',
				name : 'descricaoProduto',
				width : 90,
				sortable : false,
				align : 'left'
			},{
				display : 'Edição',
				name : 'numeroEdicao',
				width : 50,
				sortable : false,
				align : 'center'
			}, {
				display : 'Preço Capa R$',
				name : 'precoVenda',
				width : 75,
				sortable : false,
				align : 'right'
			}, {
				display : 'Reparte Total',
				name : 'reparte',
				width : 70,
				sortable : false,
				align : 'center'
			}, {
				display : 'Diferença',
				name : 'valorTotalDiferenca',
				width : 55,
				sortable : false,
				align : 'center'
			}, {
				display : 'Reparte Atual',
				name : 'qtdeEstoqueAtual',
				width : 80,
				sortable : false,
				align : 'center'
			}],
			width : 585,
			height : 180,
			disableSelect: true
		});
		
	},
	
	popupNovasDiferencas : function(idDiferenca) {
		
		$("#codigoProdutoInput", lancamentoNovoController.workspace).val("");
		$("#nomeProdutoInput", lancamentoNovoController.workspace).val("");
		$("#edicaoProdutoInput", lancamentoNovoController.workspace).val("");
		$("#precoCapaProduto", lancamentoNovoController.workspace).text("");
		$("#reparteProduto", lancamentoNovoController.workspace).text("");
		$("#diferencaProdutoInput", lancamentoNovoController.workspace).val("");
		$("#paraEstoque", lancamentoNovoController.workspace).check();
		
		$(".trCotas", lancamentoNovoController.workspace).remove();
		$("#cotaInput1", lancamentoNovoController.workspace).val("");
		$("#nomeInput1", lancamentoNovoController.workspace).val("");
		$("#reparteText1", lancamentoNovoController.workspace).text("");
		$("#diferencaInput1", lancamentoNovoController.workspace).val("");
		$("#reparteAtualText1", lancamentoNovoController.workspace).text("");
		$("#checkboxLancCota", lancamentoNovoController.workspace).uncheck();
		
		$(".prodSemCota", lancamentoNovoController.workspace).show();
		$(".prodComCota", lancamentoNovoController.workspace).hide();
		
		$("#dateNotaEnvio", lancamentoNovoController.workspace).val("");
		$("#cotaInputNota", lancamentoNovoController.workspace).val("");
		$("#nomeCotaNota", lancamentoNovoController.workspace).val("");
		
		$("#divPesquisaProdutosNota", lancamentoNovoController.workspace).hide();
		
		if (idDiferenca){
			
			$("#checkboxLancCota", lancamentoNovoController.workspace).attr("disabled", "disabled");
		} else {
			
			$("#checkboxLancCota", lancamentoNovoController.workspace).removeAttr("disabled");
		}
		
		$.postJSON(
			contextPath + "/estoque/diferenca/lancamento/rateio/buscarDiferenca", 
			[{name:"idDiferenca", value:idDiferenca}],
			function(result) {
				
				var readonly = false;
				
				if (result.codigoProduto){
					
					$("#codigoProdutoInput", lancamentoNovoController.workspace).val(result.codigoProduto);
					$("#codigoProdutoInput", lancamentoNovoController.workspace).attr("readonly", "readonly");
					
					readonly = true;
				}
				
				if (result.descricaoProduto){
					
					$("#nomeProdutoInput", lancamentoNovoController.workspace).val(result.descricaoProduto);
					
					if (readonly){
						$("#nomeProdutoInput", lancamentoNovoController.workspace).attr("readonly", "readonly");
					}
				}
				
				if (result.numeroEdicao && result.numeroEdicao){
					
					$("#edicaoProdutoInput", lancamentoNovoController.workspace).val(result.numeroEdicao);
					
					if (readonly){
						$("#edicaoProdutoInput", lancamentoNovoController.workspace).attr("readonly", "readonly");
					}
				}
				
				if (!result.precoVenda){
					
					$("#precoCapaProduto", lancamentoNovoController.workspace).text("0,00");
				} else {
					
					$("#precoCapaProduto", lancamentoNovoController.workspace).text(result.precoVenda);
				}
				
				if (result.qtde){
					
					$("#reparteProduto", lancamentoNovoController.workspace).text(parseInt(result.quantidade).toFixed(0));
				} else {
					
					$("#reparteProduto", lancamentoNovoController.workspace).text("0");
				}
				
				if (result.diferenca){
					
					$("#diferencaProdutoInput", lancamentoNovoController.workspace).val(result.diferenca);
				} else {
					
					$("#diferencaProdutoInput", lancamentoNovoController.workspace).val(0);
				}
			},
			null, 
			true
		);
		
		$("#dialogNovasDiferencas", lancamentoNovoController.workspace).dialog({
			resizable: false,
			height:570,
			width:640,
			modal: true,
			buttons: {
				"Confirmar": function() {
					cadastrarNovasDiferencas();
				},
				"Cancelar": function() {
					$("#gridNovasDiferencas", lancamentoNovoController.workspace).flexAddData({rows:[]});
					$(this).dialog("close");
				}
			},
			beforeClose: function() {
				clearMessageDialogTimeout();
			},
			form: $("#dialogNovasDiferencas", this.workspace).parents("form")
		});
	},
	
	lanctoPorCotaProduto : function(){
		
		if ($(".prodComCota", lancamentoNovoController.workspace).css("display") == "block"){
			
			$(".prodComCota", lancamentoNovoController.workspace).hide();
		} else {
			
			$(".prodComCota", lancamentoNovoController.workspace).show();
			
			$("#ui-dialog-title-dialogNovasDiferencas", lancamentoNovoController.workspace).text("Lançamento Faltas e Sobras - Cota");
		}
		
		if ($(".prodSemCota", lancamentoNovoController.workspace).css("display") == "block"){
			
			$(".prodSemCota", lancamentoNovoController.workspace).hide();
		} else {
			
			$(".prodSemCota", lancamentoNovoController.workspace).show();
			
			$("#ui-dialog-title-dialogNovasDiferencas", lancamentoNovoController.workspace).text("Lançamento Faltas e Sobras - Produto");
		}
	},

	executarPreProcessamentoNovo : function(resultado) {
		
		if (resultado.mensagens) {

			exibirMensagemDialog(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);

			return resultado;
		}

		$.each(resultado.rows, function(index, row) {
			
			row.cell.codigoProduto = '<div name="codigoProdutoNota">'+ row.cell.codigoProduto +'</div>';
			
			row.cell.reparte = '<div id="reparte'+ index +'">'+ row.cell.quantidade +'</div>';
			row.cell.qtdeEstoqueAtual = '<div id="qtdTotal'+ index +'">'+ row.cell.quantidade +'</div>';
			
			row.cell.valorTotalDiferenca = 
				'<input type="text" name="diferencaProduto" style="width:50px; value="0" text-align: center; margin-right:10px;" maxlenght="255" '+
				' id="inputDiferencaProduto'+ index +'" onchange="alterarReparteAtual('+ index +');" />';
		});
		
		return resultado;
	},

	cadastrarNovasDiferencas : function() {
		
		var tipoDiferenca = $("#tipoDiferenca", lancamentoNovoController.workspace).val();
		
		var lancamentoPorCota = $('#checkboxLancCota', lancamentoNovoController.workspace).attr('checked') ? true : false;
		
		//lançamento por produto
		var codigoProduto = $("#codigoProdutoInput", lancamentoNovoController.workspace).val();
		
		var edicaoProduto = $("#edicaoProdutoInput", lancamentoNovoController.workspace).val();
		
		var diferenca = $("#diferencaProdutoInput", lancamentoNovoController.workspace).val();
		
		var direcionadoParaEstoque = $('#paraEstoque', lancamentoNovoController.workspace).attr('checked') ? true : false;
		
		//lançamento por cota
		var dataNotaEnvio = $("#dateNotaEnvio", lancamentoNovoController.workspace).val();
		
		var numeroCota = $("#cotaInputNota", lancamentoNovoController.workspace).val();
		
		var data = [
				 {name: "tipoDiferenca", value: tipoDiferenca},
				 {name: "lancamentoPorCota", value: lancamentoPorCota},
				 {name: "codigoProduto", value: codigoProduto},
				 {name: "edicaoProduto", value: edicaoProduto},
				 {name: "diferenca", value: diferenca},
				 {name: "direcionadoParaEstoque", value: direcionadoParaEstoque},
				 {name: "dataNotaEnvio", value: dataNotaEnvio},
				 {name: "numeroCota", value: numeroCota},
				 
		 ];
		 
		var _numerosCotas = $("[name=cotaInput]", lancamentoNovoController.workspace);
		
		$.each(_numerosCotas, function(index, row) {
			
			if (row.value != ""){
				
				data.push({name: "listaNumeroCota", value: row.value});
			}
		});
		
		var _diferencas = $("[name=diferencaInput]", lancamentoNovoController.workspace);
		
		$.each(_diferencas, function(index, row) {
			
			if (row.value != ""){
				
				data.push({name: "diferencas", value: row.value});
			}
		});
		
		var _codigoProdutoProduto = $("[name=codigoProdutoNota]", lancamentoNovoController.workspace);
		
		$.each(_codigoProdutoProduto, function(index, row) {
			
			if (row.textContent != ""){
				
				data.push({name: "listaCodigoProduto", value: row.textContent});
			}
		});
		
		var _diferencasProdutosNota = $("[name=diferencaProduto]", lancamentoNovoController.workspace);
		
		$.each(_diferencasProdutosNota, function(index, row) {
			
			if (row.value != ""){
				
				data.push({name: "valorDiferencasProdNota", value: row.value});
			}
		});
		
		$.postJSON(
			contextPath + "/estoque/diferenca/lancamento/cadastrarNovasDiferencas", 
			data,
			function(result) {

				$("#gridLancamentos", lancamentoNovoController.workspace).flexOptions({
					url : contextPath + '/estoque/diferenca/lancamento/pesquisa/novos',
					params: "dataMovimento=" + $("#datePickerDataMovimento", lancamentoNovoController.workspace).val() + "&" + tipoDiferenca
				});
				
				$("#gridLancamentos", lancamentoNovoController.workspace).flexReload();

				$("#dialogNovasDiferencas", lancamentoNovoController.workspace).dialog("close");
			},
			 
			true
		);
	},
	
	tratarErroCadastroNovasDiferencas : function(jsonData) {

		if (!jsonData || !jsonData.mensagens) {

			return;
		}

		var dadosValidacao = jsonData.mensagens.dados;
		
		var linhasDaGrid = $(".gridNovasDiferencas tr", lancamentoNovoController.workspace);

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

	obterDadosProduto : function(idCodigoProduto, idEdicaoProduto) {

		codigoProduto = $(idCodigoProduto).val();

		edicaoProduto = $(idEdicaoProduto).val();

		var data = "codigoProduto=" + codigoProduto
				 + "&numeroEdicao=" + edicaoProduto;
		
		$.postJSON(
			contextPath + "/produto/obterProdutoEdicao", 
			data,
			function(produtoEdicao) {

				$("#precoVenda" + ultimaLinhaPreenchida, lancamentoNovoController.workspace).val(produtoEdicao.precoVenda);
				
				$("#precoVendaFormatado" + ultimaLinhaPreenchida, lancamentoNovoController.workspace).text(produtoEdicao.precoVenda);
				$("#precoVendaFormatado" + ultimaLinhaPreenchida, lancamentoNovoController.workspace).formatCurrency({region: 'pt-BR', decimalSymbol: ',', symbol: ''});

				$("#totalPrecoVendaDiferencas", lancamentoNovoController.workspace).text(($("input[id^='precoVenda']").sum()));
				$("#totalPrecoVendaDiferencas", lancamentoNovoController.workspace).formatCurrency({region: 'pt-BR', decimalSymbol: ',', symbol: ''});

				lancamentoNovoController.obterEstoqueProduto(produtoEdicao);
			},
			null, 
			true
		);
	},

	obterEstoqueProduto : function(produtoEdicao) {

		if (!produtoEdicao) {

			return;
		}

		var data = "idProdutoEdicao=" + produtoEdicao.id;
		
		$.postJSON(
			contextPath + "/produto/obterEstoque", 
			data,
			function(estoqueProduto) {
				
				if (estoqueProduto) {
					$("#qtdeRecebimentoFisico" + ultimaLinhaPreenchida, lancamentoNovoController.workspace).val(estoqueProduto.qtde);
					$("#qtdeRecebimentoFisicoFormatado" + ultimaLinhaPreenchida, lancamentoNovoController.workspace).text(estoqueProduto.qtde);
					$("#totalRecebimentoFisico", lancamentoNovoController.workspace).text(($("input[id^='qtdeRecebimentoFisico']", lancamentoNovoController.workspace).sum()));
				}
			},
			null, 
			true
		);
	},

	isAtributosDiferencaVazios : function(codigoProduto, descricaoProduto, numeroEdicao, qtdeDiferenca) {

		if (!$.trim(codigoProduto) 
				&& !$.trim(descricaoProduto)
				&& !$.trim(numeroEdicao) 
				&& !$.trim(qtdeDiferenca)) {

			return true;
		}
	},

	formatarCampos : function() {

		$("input[name='codigoProduto']", lancamentoNovoController.workspace).numeric();
		$("input[name='numeroEdicao']", lancamentoNovoController.workspace).numeric();
		$("input[name='qtdeDiferenca']", lancamentoNovoController.workspace).numeric();
		
		$("input[name='descricaoProduto']", lancamentoNovoController.workspace).autocomplete({source: ""});
	},
	
	paraEstoque : function(param){
		
		if (param){
			
			$("#fieldCota", lancamentoNovoController.workspace).hide("slow");
			$(".trCotas", lancamentoNovoController.workspace).remove();
		} else {
			
			$("#fieldCota", lancamentoNovoController.workspace).show("slow");
		}
	},
	
	adicionarLinhaCota : function(linhaAtual){
		
		if ($('#trCota' + (linhaAtual + 1), lancamentoNovoController.workspace).length == 0 && $('#cotaInput' + (linhaAtual)).val() != ""){
			
			var tr = $('<tr class="trCotas" id="trCota'+ (linhaAtual + 1) +'" style="'+ ((linhaAtual + 1) % 2 == 0 ? "background: #F5F5F5;" : "") +'">' +
					'<td><input type="text" name="cotaInput" maxlength="255" id="cotaInput'+ (linhaAtual + 1) +'" onblur="pesquisaCotaLancamentoFaltasSobras.pesquisarPorNumeroCota(cotaInput'+ (linhaAtual + 1) +', nomeInput'+ (linhaAtual + 1) +', true, lancamentoNovoController.buscarReparteAtualCota('+ (linhaAtual + 1) +'));" style="width:60px;" /></td>' +
					'<td>'+
					     '<input type="text" name="nomeInput" maxlength="255" id="nomeInput'+ (linhaAtual + 1) +'" style="width:180px;" '+
					         ' onkeyup="pesquisaCotaLancamentoFaltasSobras.autoCompletarPorNome(nomeInput'+ (linhaAtual + 1) +');" ' +
					         ' onblur="pesquisaCotaLancamentoFaltasSobras.pesquisarPorNomeCota(cotaInput'+ (linhaAtual + 1) +', nomeInput'+ (linhaAtual + 1) +', lancamentoNovoController.buscarReparteAtualCota('+ (linhaAtual + 1) +'));" ' +
					     '/>'+
					'</td>' +
					'<td align="center" id="reparteText'+ (linhaAtual + 1) +'"></td>' +
					'<td align="center">' +
					     '<input type="text" name="diferencaInput" maxlength="255" id="diferencaInput'+ (linhaAtual + 1) +'" style="width:80px; text-align:center;" onblur="lancamentoNovoController.adicionarLinhaCota('+ (linhaAtual + 1) +');" onchange="lancamentoNovoController.calcularReparteAtual('+ (linhaAtual + 1) +')"/>' +
					'</td>' +
					'<td id="reparteAtualText'+ (linhaAtual + 1) +'" align="center"></td></tr>'
			);
			
			$("#grid_1", lancamentoNovoController.workspace).append(tr);
			
			$("#cotaInput" + (linhaAtual + 1), lancamentoNovoController.workspace).focus();
			
			$("#cotaInput"+ (linhaAtual + 1), lancamentoNovoController.workspace).numeric();
			$("#diferencaInput" + (linhaAtual + 1), lancamentoNovoController.workspace).numeric();
		}
	},
	
	buscarReparteAtualCota : function(idDiv){
		
		$("#diferencaInput" + idDiv, lancamentoNovoController.workspace).focus();
		
		setTimeout(
				function(){
					if ($("#cotaInput" + idDiv, lancamentoNovoController.workspace).val() != ""){
						$.postJSON(
							contextPath + "/estoque/diferenca/lancamento/rateio/buscarReparteCotaPreco",
							[
							 	{name: "idProdutoEdicao", value: $("#codigoProduto", lancamentoNovoController.workspace).val()},
							 	{name: "numeroCota", value: $("#cotaInput" + idDiv, lancamentoNovoController.workspace).val()}
							],
							function(result) {
								$("#reparteText" + idDiv, lancamentoNovoController.workspace).text(result[0]);
							},
							true
						);
					}
				}, 
				500
		);
	},
	
	buscarPrecoProdutoEdicao : function(){
		
		$.postJSON(
			contextPath + "/estoque/diferenca/lancamento/rateio/buscarPrecoProdutoEdicao",
			[
			 	{name: "codigoProduto", value: $("#codigoProdutoInput", lancamentoNovoController.workspace).val()},
			 	{name: "numeroEdicao", value: $("#edicaoProdutoInput", lancamentoNovoController.workspace).val()}
			],
			function(result) {
				$("#reparteProduto", lancamentoNovoController.workspace).text(result[0]);
				$("#precoCapaProduto", lancamentoNovoController.workspace).text(result[1]);
			},
			true
		);
	},
	
	calcularReparteAtual : function(idDiv){
		
		if ($("#diferencaInput" + idDiv, lancamentoNovoController.workspace).val() == ""){
			
			$("#diferencaInput" + idDiv, lancamentoNovoController.workspace).val(0);
		}
		
		if ($("#tipoDiferenca", lancamentoNovoController.workspace).val() == "SOBRA_DE" || $("#tipoDiferenca", lancamentoNovoController.workspace).val() == "SOBRA_EM"){
			
			$("#reparteAtualText" + idDiv, lancamentoNovoController.workspace).text(parseInt($("#reparteText" + idDiv, lancamentoNovoController.workspace).text()) + parseInt($("#diferencaInput" + idDiv, lancamentoNovoController.workspace).val()));
		} else {
			
			$("#reparteAtualText" + idDiv, lancamentoNovoController.workspace).text(parseInt($("#reparteText" + idDiv, lancamentoNovoController.workspace).text()) - parseInt($("#diferencaInput" + idDiv, lancamentoNovoController.workspace).val()));
		}
	},
	
	pesquisarProdutosNota : function(){
		$(".lanctoFaltasSobrasCota_3Grid", lancamentoNovoController.workspace).flexOptions({
			"url" : contextPath + "/estoque/diferenca/lancamento/rateio/buscarProdutosCotaNota",
			params : [ {
				name : "dateNotaEnvio",
				value : $("#dateNotaEnvio", lancamentoNovoController.workspace).val()
			}, {
				name : "numeroCota",
				value : $("#cotaInputNota", lancamentoNovoController.workspace).val()
			}],
		});
		
		$(".lanctoFaltasSobrasCota_3Grid", lancamentoNovoController.workspace).flexReload();
		
		$("#divPesquisaProdutosNota", lancamentoNovoController.workspace).show();
	},
	
	alterarReparteAtual : function(indexDiv){
		
		if ($("#tipoDiferenca", lancamentoNovoController.workspace).val() == "SOBRA_DE" || $("#tipoDiferenca", lancamentoNovoController.workspace).val() == "SOBRA_EM"){
			
			$("#qtdTotal" + indexDiv, lancamentoNovoController.workspace).text(
				parseInt($("#reparte" + indexDiv, lancamentoNovoController.workspace).text()) + parseInt($("#inputDiferencaProduto" + indexDiv, lancamentoNovoController.workspace).val())
			);
		} else {
			
			$("#qtdTotal" + indexDiv, lancamentoNovoController.workspace).text(
				parseInt($("#reparte" + indexDiv, lancamentoNovoController.workspace).text()) - parseInt($("#inputDiferencaProduto" + indexDiv, lancamentoNovoController.workspace).val())
			);
		}
	}
	
}, BaseController);
