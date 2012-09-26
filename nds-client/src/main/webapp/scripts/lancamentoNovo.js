var lancamentoNovoController = $.extend(true, {

	ultimaLinhaPreenchida : "",
	valorDiferencaDirecionadaEstoque:0,
	idDiferenca:null,
	redirecionarProdutosEstoque:false,
	tipoEstoqueSelecionado:null,

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
	
	popupNovasDiferencas : function() {
		
		$("#codigoProdutoInput", lancamentoNovoController.workspace).val("");
		$("#nomeProdutoInput", lancamentoNovoController.workspace).val("");
		$("#edicaoProdutoInput", lancamentoNovoController.workspace).val("");
		$("#precoCapaProduto", lancamentoNovoController.workspace).text("");
		$("#reparteProduto", lancamentoNovoController.workspace).text("");
		$("#diferencaProdutoInput", lancamentoNovoController.workspace).val("");
		
		$("#fieldCota", lancamentoNovoController.workspace).hide();
		$(".trCotas", lancamentoNovoController.workspace).remove();

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

		$("#tipoDiferenca", lancamentoNovoController.workspace).removeAttr("disabled");
		$("#checkboxLancCota", lancamentoNovoController.workspace).removeAttr("disabled");
		$("#codigoProdutoInput", lancamentoNovoController.workspace).removeAttr("disabled");
		$("#nomeProdutoInput", lancamentoNovoController.workspace).removeAttr("disabled");
		$("#edicaoProdutoInput", lancamentoNovoController.workspace).removeAttr("disabled");
		$("#diferencaProdutoInput", lancamentoNovoController.workspace).removeAttr("disabled");
		
	
		lancamentoNovoController.redirecionarProdutosEstoque = false;
		
		lancamentoNovoController.idDiferenca = null;
		
		lancamentoNovoController.openModalDiferenca();
		
		lancamentoNovoController.tratarVisualizacaoOpcaoEstoque("FALTA_DE");
	},
	
	editarDiferenca:function(idDiferenca){
		
		$.postJSON(
			contextPath + "/estoque/diferenca/lancamento/rateio/buscarDiferenca", 
			[{name:"idDiferenca", value:idDiferenca}],
			function(result) {
				
				var diferenca = result.diferenca;
				
				$("#tipoDiferenca", lancamentoNovoController.workspace).attr("disabled", "disabled");
				$("#checkboxLancCota", lancamentoNovoController.workspace).attr("disabled", "disabled");
				$("#codigoProdutoInput", lancamentoNovoController.workspace).attr("disabled", "disabled");
				$("#nomeProdutoInput", lancamentoNovoController.workspace).attr("disabled", "disabled");
				$("#edicaoProdutoInput", lancamentoNovoController.workspace).attr("disabled", "disabled");
				
				$("#reparteProduto", lancamentoNovoController.workspace).text(diferenca.qtdeEstoqueAtual);
				$("#tipoDiferenca", lancamentoNovoController.workspace).val(diferenca.tipoDiferenca);
				$("#idProdutoEdicao", lancamentoNovoController.workspace).val(result.idProdutoEdicao);
				
				lancamentoNovoController.idDiferenca = idDiferenca;
				
				if(diferenca.automatica && diferenca.automatica == true){
					
					$("#diferencaProdutoInput", lancamentoNovoController.workspace).attr("disabled", "disabled");
				}
				else{
					$("#diferencaProdutoInput", lancamentoNovoController.workspace).removeAttr("disabled");
				}
				
				if (diferenca.codigoProduto){
					
					$("#codigoProdutoInput", lancamentoNovoController.workspace).val(diferenca.codigoProduto);
				}
				
				if (diferenca.descricaoProduto){
					
					$("#nomeProdutoInput", lancamentoNovoController.workspace).val(diferenca.descricaoProduto);
				}
				
				if (diferenca.numeroEdicao && diferenca.numeroEdicao){
					
					$("#edicaoProdutoInput", lancamentoNovoController.workspace).val(diferenca.numeroEdicao);
				}
				
				if (!diferenca.precoVenda){
					
					$("#precoCapaProduto", lancamentoNovoController.workspace).text("0,00");
					
				} else{
					
					$("#precoCapaProduto", lancamentoNovoController.workspace).text(diferenca.precoVenda);
				}
				
				if (diferenca.quantidade){
					
					$("#diferencaProdutoInput", lancamentoNovoController.workspace).val(diferenca.quantidade);
					
				} else {
					
					$("#diferencaProdutoInput", lancamentoNovoController.workspace).val(0);
				}
				
				if(diferenca.tipoDirecionamento == 'COTA'){
					
					$("#paraCota").check();
					
					if(result.rateios){
						
						lancamentoNovoController.renderizarlistaRateio(result.rateios);
					}
				}
				
				if(diferenca.tipoDirecionamento == 'ESTOQUE'){
					
					 $("#paraEstoque").check();
					 lancamentoNovoController.paraEstoque(true);
				}
				
				lancamentoNovoController.tratarVisualizacaoOpcaoEstoque(diferenca.tipoDiferenca);	
			},
			null, 
			true
		);
	
		lancamentoNovoController.openModalDiferenca();
	},
	
	renderizarlistaRateio:function(result){
		
		$(".trCotas", lancamentoNovoController.workspace).remove();
			
			$.each(result, function(linhaAtual, value) {
					
				if(linhaAtual > 0){
					
					  var tr = $('<tr class="trCotas" id="trCota'+ (linhaAtual + 1) +'" style="'+ ((linhaAtual +1) % 2 == 0 ? "background: #F5F5F5;" : "") +'">' +
								'<td><input type="text" name="cotaInput" maxlength="255" id="cotaInput'+ (linhaAtual +1) +'" onblur="pesquisaCotaLancamentoFaltasSobras.pesquisarPorNumeroCota(cotaInput'+ (linhaAtual +1) +', nomeInput'+ (linhaAtual +1) +', true, lancamentoNovoController.buscarReparteAtualCota('+ (linhaAtual +1) +'),lancamentoNovoController.erroPesquisaCota('+(linhaAtual +1)+'));" style="width:60px;" />'
								+'<input type="hidden" name="rateioIDInputHidden"  id="rateioIDInputHidden'+ (linhaAtual +1) +' " />'
								+'</td>' +
								'<td>'+
								     '<input type="text" name="nomeInput" maxlength="255" id="nomeInput'+ (linhaAtual+1) +'" style="width:180px;" '+
								         ' onkeyup="pesquisaCotaLancamentoFaltasSobras.autoCompletarPorNome(nomeInput'+ (linhaAtual+1) +');" ' +
								         ' onblur="pesquisaCotaLancamentoFaltasSobras.pesquisarPorNomeCota(cotaInput'+ (linhaAtual+1) +', nomeInput'+ (linhaAtual+1) +', lancamentoNovoController.buscarReparteAtualCota('+ (linhaAtual+1) +'),lancamentoNovoController.erroPesquisaCota('+(linhaAtual+1)+'));" ' +
								     '/>'+
								'</td>' +
								'<td align="center" id="reparteText'+ (linhaAtual+1) +'"></td>' +
								'<td align="center">' +
								     '<input type="text" name="diferencaInput" maxlength="255" id="diferencaInput'+ (linhaAtual+1) +'" style="width:80px; text-align:center;" onblur="lancamentoNovoController.adicionarLinhaCota('+ (linhaAtual+1) +');" onchange="lancamentoNovoController.calcularReparteAtual('+ (linhaAtual+1) +')"/>' +
								'</td>' +
								'<td id="reparteAtualText'+ (linhaAtual+1) +'" align="center"></td></tr>'
						);
						
						$("#grid_1", lancamentoNovoController.workspace).append(tr);		
				}
							
				$("#cotaInput"+ (linhaAtual+1), lancamentoNovoController.workspace).numeric();
				
				$("#diferencaInput" + (linhaAtual+1), lancamentoNovoController.workspace).numeric();
					
				$("#cotaInput" + (linhaAtual+1), lancamentoNovoController.workspace).val(value.numeroCota);
					
				$("#nomeInput" + (linhaAtual+1), lancamentoNovoController.workspace).val(value.nomeCota);
					
				$("#reparteText" + (linhaAtual+1), lancamentoNovoController.workspace).text(value.reparteCota);
					
				$("#diferencaInput" + (linhaAtual+1), lancamentoNovoController.workspace).val(value.quantidade);
					
				$("#reparteAtualText" + (linhaAtual+1), lancamentoNovoController.workspace).text(value.reparteAtualCota);
				
				$("#rateioIDInputHidden" + (linhaAtual+1), lancamentoNovoController.workspace).val(value.idRateio);
				
			});
			
			$("#fieldCota", lancamentoNovoController.workspace).show();
	},
	
	openModalDiferenca:function(){
		
		$("#dialogNovasDiferencas", lancamentoNovoController.workspace).dialog({
			resizable: false,
			height:570,
			width:640,
			modal: true,
			buttons: {
				"Confirmar": function() {
					
					lancamentoNovoController.processarNovaDiferenca();
					
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
				' id="inputDiferencaProduto'+ index +'" onchange="lancamentoNovoController.alterarReparteAtual('+ index +');" />';
		});
		
		return resultado;
	},

	cadastrarNovasDiferencas : function(isBotaoIncluirNovo) {
		
		var tipoDiferenca = $("#tipoDiferenca", lancamentoNovoController.workspace).val();
		
		var lancamentoPorCota = $('#checkboxLancCota', lancamentoNovoController.workspace).attr('checked') ? true : false;
		
		//lançamento por produto
		var codigoProduto = $("#codigoProdutoInput", lancamentoNovoController.workspace).val();
		
		var edicaoProduto = $("#edicaoProdutoInput", lancamentoNovoController.workspace).val();
		
		var diferenca = $("#diferencaProdutoInput", lancamentoNovoController.workspace).val();
		
		var direcionadoParaEstoque = $('#paraEstoque', lancamentoNovoController.workspace).attr('checked') ? true : false;
		
		var reparteAtual = $("#reparteProduto", lancamentoNovoController.workspace).html();
		
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
				 {name: "reparteAtual", value: reparteAtual},
				 {name: "redirecionarProdutosEstoque", value: lancamentoNovoController.redirecionarProdutosEstoque},
				 {name: "idDiferenca", value:lancamentoNovoController.idDiferenca},
				 {name: "tipoEstoque", value:lancamentoNovoController.tipoEstoqueSelecionado}
		 ];
		
		var linhasDaGrid = $('#grid_1 tr',this.workspace);
		
		var qntReparteRateio = 0;
		
		$.each(linhasDaGrid, function(index, value) {

			var linha = $(value);
			
			var numeroCota = $(linha.find("td")[0],this.workspace).find('input[name="cotaInput"]').val();
			
			var idRateioCota =  $(linha.find("td")[0],this.workspace).find('input[name="rateioIDInputHidden"]').val();
			
			var nomeCota = $(linha.find("td")[1],this.workspace).find('input[name="nomeInput"]').val();
			
			var reparte = $(linha.find("td")[2],this.workspace).html();
			
			var diferenca = $(linha.find("td")[3],this.workspace).find('input[name="diferencaInput"]').val();
			
			var reparteAtual = $(linha.find("td")[4],this.workspace).html();
			
			if( numeroCota == undefined || numeroCota == '' 
					|| diferenca == undefined || diferenca == ''){
				
				return;
			}
			
			data.push({name: "rateioCotas["+index+"].id", value: index});
			data.push({name: "rateioCotas["+index+"].idDiferenca", value:lancamentoNovoController.idDiferenca});
			data.push({name: "rateioCotas["+index+"].numeroCota", value: numeroCota });
			data.push({name: "rateioCotas["+index+"].nomeCota", value: nomeCota});
			data.push({name: "rateioCotas["+index+"].reparteCota", value: reparte });
			data.push({name: "rateioCotas["+index+"].quantidade", value: diferenca});
			data.push({name: "rateioCotas["+index+"].reparteAtualCota", value: reparteAtual});
			data.push({name: "rateioCotas["+index+"].idRateio", value: idRateioCota });
			
			qntReparteRateio += eval(diferenca);
		});
		
		data.push({name: "qntReparteRateio", value: qntReparteRateio});
		
		var linhasDaGrid = $('.lanctoFaltasSobrasCota_3Grid tr',this.workspace);
		
		$.each(linhasDaGrid, function(index, value) {

			var linha = $(value);
			
			var codigoProduto = $(linha.find("td")[0],this.workspace).find("div").find("div").html();
			
			var nomeProduto = $(linha.find("td")[1],this.workspace).find("div").html();
			
			var numeroEdicao = $(linha.find("td")[2],this.workspace).find("div").html();
			
			var reparte = $(linha.find("td")[4],this.workspace).find("div").find("div").html();
			
			var diferenca = $(linha.find("td")[5],this.workspace).find('input[name="diferencaProduto"]').val();
			
			var reparteAtual = $(linha.find("td")[6],this.workspace).find("div").find("div").html();
			
			if( codigoProduto == undefined || codigoProduto == '' 
					|| diferenca == undefined || diferenca == ''
						|| numeroEdicao == undefined || numeroEdicao == ''){
				
				return;
			}
			
			data.push({name: "diferencasProdutos["+index+"].id", value: index});
			data.push({name: "diferencasProdutos["+index+"].codigoProduto", value: codigoProduto});
			data.push({name: "diferencasProdutos["+index+"].nomeProduto", value: nomeProduto});
			data.push({name: "diferencasProdutos["+index+"].numeroEdicao", value: numeroEdicao});
			data.push({name: "diferencasProdutos["+index+"].qtdeEstoque", value: reparte});
			data.push({name: "diferencasProdutos["+index+"].quantidade", value: diferenca});
			data.push({name: "diferencasProdutos["+index+"].qtdeEstoqueAtual", value:reparteAtual});
			data.push({name: "diferencasProdutos["+index+"].tipoDiferenca", value:tipoDiferenca});

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
				
				if(!isBotaoIncluirNovo){
					$("#dialogNovasDiferencas", lancamentoNovoController.workspace).dialog("close");
				}
				else{
					lancamentoNovoController.popupNovasDiferencas();
				}
				
				$("#dialogConfirmacaoDirecionamentoDiferencaProdutoCota", lancamentoNovoController.workspace).dialog("close");
				
			},
			function(result){
				lancamentoNovoController.redirecionarProdutosEstoque = false;
				lancamentoNovoController.tratarErroCadastroNovasDiferencas(result);
				$("#dialogConfirmacaoDirecionamentoDiferencaProdutoCota", lancamentoNovoController.workspace).dialog("close");
			},
			true
		);
	},
	
	tratarErroCadastroNovasDiferencas : function(jsonData) {

		if (!jsonData || !jsonData.mensagens) {

			return;
		}

		var dadosValidacao = jsonData.mensagens.dados;
		
		var linhasDaGrid = $("#grid_1 tr", lancamentoNovoController.workspace);

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
			
			lancamentoNovoController.limparCotas();
			$("#fieldCota", lancamentoNovoController.workspace).hide();
			
		} else {
			
			var idProdutoEdicao = $("#idProdutoEdicao", lancamentoNovoController.workspace).val();
			
			if(!idProdutoEdicao) {
				exibirMensagemDialog('WARNING', ['Produto Edição não selecionado.'],'');			
				$('#paraCota').attr('checked',false);
				$("#fieldCota", lancamentoNovoController.workspace).hide();
				$(".trCotas", lancamentoNovoController.workspace).remove();				
				return;
			}
			
			$("#fieldCota", lancamentoNovoController.workspace).show();
		}
	},
	
	erroPesquisaCota:function(indiceLinha){
		
		var linhaGrid = $('#grid_1 tr',this.workspace)[indiceLinha];
		
		var linha = $(linhaGrid);
		
		$(linha.find("td")[2],this.workspace).html("");
		
		$(linha.find("td")[3],this.workspace).find('input[name="diferencaInput"]').val("");
		
		$(linha.find("td")[4],this.workspace).html("");
		
	},
	
	adicionarLinhaCota : function(linhaAtual){
		
		if ($('#trCota' + (linhaAtual + 1), lancamentoNovoController.workspace).length == 0 && $('#cotaInput' + (linhaAtual)).val() != ""){
			
			var tr = $('<tr class="trCotas" id="trCota'+ (linhaAtual + 1) +'" style="'+ ((linhaAtual + 1) % 2 == 0 ? "background: #F5F5F5;" : "") +'">' +
					'<td><input type="text" name="cotaInput" maxlength="255" id="cotaInput'+ (linhaAtual + 1) +'" onblur="pesquisaCotaLancamentoFaltasSobras.pesquisarPorNumeroCota(cotaInput'+ (linhaAtual + 1) +', nomeInput'+ (linhaAtual + 1) +', true, lancamentoNovoController.buscarReparteAtualCota('+ (linhaAtual + 1) +'),lancamentoNovoController.erroPesquisaCota('+(linhaAtual + 1)+'));" style="width:60px;" /></td>' +
					'<td>'+
					     '<input type="text" name="nomeInput" maxlength="255" id="nomeInput'+ (linhaAtual + 1) +'" style="width:180px;" '+
					         ' onkeyup="pesquisaCotaLancamentoFaltasSobras.autoCompletarPorNome(nomeInput'+ (linhaAtual + 1) +');" ' +
					         ' onblur="pesquisaCotaLancamentoFaltasSobras.pesquisarPorNomeCota(cotaInput'+ (linhaAtual + 1) +', nomeInput'+ (linhaAtual + 1) +', lancamentoNovoController.buscarReparteAtualCota('+ (linhaAtual + 1) +'),lancamentoNovoController.erroPesquisaCota('+(linhaAtual + 1)+'));" ' +
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
		
		var idProdutoEdicao = $("#idProdutoEdicao", lancamentoNovoController.workspace).val();
		
		if(!idProdutoEdicao) {
			exibirMensagemDialog('WARNING', ['Produto Edição não selecionado.'],'');			
			return;
		}
		setTimeout(
				function(){
					if ($("#cotaInput" + idDiv, lancamentoNovoController.workspace).val() != ""){
						$.postJSON(
							contextPath + "/estoque/diferenca/lancamento/rateio/buscarReparteCotaPreco",
							[
							 	{name: "idProdutoEdicao", value: idProdutoEdicao},
							 	{name: "numeroCota", value: $("#cotaInput" + idDiv, lancamentoNovoController.workspace).val()}
							],
							function(result) {
								$("#reparteText" + idDiv, lancamentoNovoController.workspace).text(result[0]);
							},
							null,
							true,
							''
						);
					}
				}
		);
	},
		
	buscarPrecoProdutoEdicao : function(){
		
		$("#idProdutoEdicao", lancamentoNovoController.workspace).val(null);
		
		lancamentoNovoController.limparCotas();
		
		$.postJSON(
			contextPath + "/estoque/diferenca/lancamento/rateio/buscarPrecoProdutoEdicao",
			[
			 	{name: "codigoProduto", value: $("#codigoProdutoInput", lancamentoNovoController.workspace).val()},
			 	{name: "numeroEdicao", value: $("#edicaoProdutoInput", lancamentoNovoController.workspace).val()}
			],
			function(result) {
				$("#precoCapaProduto", lancamentoNovoController.workspace).text(result[0]);
				$("#idProdutoEdicao", lancamentoNovoController.workspace).val(result[1]);
				
				lancamentoNovoController.verificarTipoEstoque(result[2]);
				
			},
			null,
			true,
			''
		);
	},
	
	limparCotas : function() {
		
		$(".trCotas", lancamentoNovoController.workspace).remove();		
		$('#cotaInput1', lancamentoNovoController.workspace).val('');
		$('#nomeInput1', lancamentoNovoController.workspace).val('');
		$('#diferencaInput1', lancamentoNovoController.workspace).val('');
		$('#reparteText1', lancamentoNovoController.workspace).text('');
		$('#reparteAtualText1', lancamentoNovoController.workspace).text('');
	},
	
	limparProduto : function() {
		$("#precoCapaProduto", lancamentoNovoController.workspace).text('');
		$("#reparteProduto", lancamentoNovoController.workspace).text('');
		$("#diferencaProdutoInput", lancamentoNovoController.workspace).val('');
		
		lancamentoNovoController.limparCotas();
	},
	
	verificarTipoEstoque : function(estoques) {
				
		if(estoques.length == 1) {
			lancamentoNovoController.tipoEstoqueSelecionado = estoques[0].nameEnum;;
			$("#reparteProduto", lancamentoNovoController.workspace).text(estoques[0].qtde);
		
		} else  {
			
			$( "#selectTipoEstoque").clear();
						
			$.each(estoques, function(index, item){
				$( "#selectTipoEstoque").append('<option enum="'+item.nameEnum+'" valor="'+item.qtde+'">'+item.desc+'</option>');
			});
			
			lancamentoNovoController.atualizarQuantidade();
			
			$( "#dialog-tipo-estoque", this.workspace).dialog({
				resizable: false,
				height:160,
				width:330,
				modal: true,
				buttons: {
					"Confirmar": function() {
						
						var qtdeEstoque = $( "#selectTipoEstoque :selected").attr('valor');
						
						$("#reparteProduto", lancamentoNovoController.workspace).text(qtdeEstoque);
						
						lancamentoNovoController.tipoEstoqueSelecionado = $( "#selectTipoEstoque :selected").attr('enum');
						
						$('#diferencaProdutoInput').focus();
						
						$( this ).dialog( "close" );
					},
					"Cancelar": function() {
						$( this ).dialog( "close" );
					}
				},
				form: $("#dialog-tipo-estoque", this.workspace).parents("form")
			});
		}
		
	},
	
	atualizarQuantidade : function() {		
		 var qtde = $( "#selectTipoEstoque :selected").attr('valor');
		 $('#qtdeTipoDialog').val(qtde);
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
	},
	
	tratarVisualizacaoOpcaoEstoque:function(value){
		
		if(value == 'FALTA_EM'){
			 $(".view-estouque",this.workspace).hide();
			 $("#paraCota").check();
			 lancamentoNovoController.paraEstoque(false);
		}
		else{
			$(".view-estouque",this.workspace).show();
		}
	},
	
	incluirNovo:function(){
		
		lancamentoNovoController.processarNovaDiferenca(true);
	},
	
	processarNovaDiferenca:function(isBotaoNovaDiferenca){
		
		if($("#paraCota").is(":checked")){
			
			if($("#tipoDiferenca", lancamentoNovoController.workspace).val()  == 'FALTA_EM'){
				
				lancamentoNovoController.cadastrarNovasDiferencas(isBotaoNovaDiferenca);
			}
			else{
				
				if (lancamentoNovoController.validarDirecionamentoDiferencaProdutoCotas()){
					
					lancamentoNovoController.openDialogDirecionamentoDiferencaProdutoCota(isBotaoNovaDiferenca);
				}
				else{
					
					lancamentoNovoController.cadastrarNovasDiferencas(isBotaoNovaDiferenca);
				}
			}
		}
		else{
			
			lancamentoNovoController.cadastrarNovasDiferencas(isBotaoNovaDiferenca);
		}
		
	},
	
	validarDirecionamentoDiferencaProdutoCotas:function(){
	
		var qntDiferencaProdutos = eval( $("#diferencaProdutoInput", this.workspace).val());	
		
		var qntDiferencasCota = 0;
		
		var linhasDaGrid = $('#grid_1 tr',this.workspace);
		
		$.each(linhasDaGrid, function(index, value) {

			var linha = $(value);
			
			var numeroCota = $(linha.find("td")[0],this.workspace).find('input[name="cotaInput"]').val();
			
			var diferenca = $(linha.find("td")[3],this.workspace).find('input[name="diferencaInput"]').val();
			
			if( numeroCota == undefined || numeroCota == '' 
					|| diferenca == undefined || diferenca == ''){
				
				return;
			}
			
			qntDiferencasCota += eval(diferenca);
		});
		
		return (qntDiferencaProdutos > qntDiferencasCota);
		
	},
	
	openDialogDirecionamentoDiferencaProdutoCota:function(isBotaoIncluirNovo){
		
		$("#dialogConfirmacaoDirecionamentoDiferencaProdutoCota", lancamentoNovoController.workspace).dialog({
			resizable: false,
			height:'auto',
			width:300,
			modal: true,
			buttons: 
			{
				"Confirmar": function() {
					
					lancamentoNovoController.redirecionarProdutosEstoque = true;
					lancamentoNovoController.cadastrarNovasDiferencas(isBotaoIncluirNovo);
					
				}, "Cancelar": function() {
					lancamentoNovoController.redirecionarProdutosEstoque = false;
					$(this).dialog("close");
				}
			},
			form: $("#dialogConfirmacaoDirecionamentoDiferencaProdutoCota", this.workspace).parents("form")
		});
		
		$("#dialogConfirmacaoDirecionamentoDiferencaProdutoCota", lancamentoNovoController.workspace).show();
	},
	
}, BaseController);

//@ sourceURL=lancamentoNovo.js
