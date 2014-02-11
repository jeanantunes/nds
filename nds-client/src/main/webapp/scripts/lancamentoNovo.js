var lancamentoNovoController = $.extend(true, {

	ultimaLinhaPreenchida : "",
	valorDiferencaDirecionadaEstoque:0,
	idDiferenca:null,
	redirecionarProdutosEstoque:false,
	tipoEstoqueSelecionado:null,
	houveAlteracaoLancamentos:false,

	init : function () {
		$("#dateNotaEnvio", lancamentoNovoController.workspace).datepicker({
			showOn : "button",
			buttonImage : contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly : true,
			dateFormat: "dd/mm/yy"
		});
		
		$("#edicaoProdutoInput", lancamentoNovoController.workspace).numeric();
		$("#diferencaProdutoInput", lancamentoNovoController.workspace).justInput(/[0-9]/);
		$("#cotaInput1", lancamentoNovoController.workspace).numeric();
		$("#cotaInputAlteracaoReparte", lancamentoNovoController.workspace).numeric();
		$("#diferencaInput1", lancamentoNovoController.workspace).justInput(/[0-9]/);
		$("#cotaInputNota", lancamentoNovoController.workspace).numeric();
		$("#dateNotaEnvio", lancamentoNovoController.workspace).mask("99/99/9999");
		
		$("#diferencaProdutoInput",lancamentoNovoController.workspace).keyup(function(e){
		  if (!isNaN(parseInt(this.value)) && parseInt(this.value) == 0){ 
			this.value = "";
		  }else{
			return;
		  }
		});
		
		$("#diferencaInput1",lancamentoNovoController.workspace).keyup(function(e){
		  if (!isNaN(parseInt(this.value)) && parseInt(this.value) == 0){ 
			this.value = "";
		  }else{
			return;
		  }
		});
	
		$(".lanctoFaltasSobrasCota_3Grid", lancamentoNovoController.workspace).flexigrid({
			preProcess: lancamentoNovoController.executarPreProcessamentoNovo,
			onSuccess: function(){
				$(".maskDiferencaProduto", lancamentoNovoController.workspace).justInput(/[0-9]/);
				$(".maskDiferencaProduto",lancamentoNovoController.workspace ).keyup(function(e){
				  if (!isNaN(parseInt(this.value)) && parseInt(this.value) == 0){ 
					this.value = "";
				  }else{
					return;
				  }
				});
			},
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigoProduto',
				width : 60,
				sortable : false,
				align : 'left'
			},{
				display : 'Produto',
				name : 'descricaoProduto',
				width : 100,
				sortable : false,
				align : 'left'
			},{
				display : 'Edição',
				name : 'numeroEdicao',
				width : 40,
				sortable : false,
				align : 'center'
			}, {
				display : 'Preço Venda R$',
				name : 'precoVenda',
				width : 80,
				sortable : false,
				align : 'right'
			}, {
				display : 'Pct Padrão',
				name : 'pacotePadrao',
				width : 55,
				sortable : false,
				align : 'center'
			}, {
				display : 'Reparte Total',
				name : 'reparte',
				width :70,
				sortable : false,
				align : 'center'
			}, {
				display : 'Diferença',
				name : 'valorTotalDiferenca',
				width : 60,
				sortable : false,
				align : 'center'
			}, {
				display : 'Reparte Atual',
				name : 'qtdeEstoqueAtual',
				width : 70,
				sortable : false,
				align : 'center'
			}],
			width : 650,
			height : 180,
			disableSelect: true
		});
		
	},
	
	resetarCamposTela:function(){
		
		$("#codigoProdutoInput", lancamentoNovoController.workspace).val("");
		$("#nomeProdutoInput", lancamentoNovoController.workspace).val("");
		$("#edicaoProdutoInput", lancamentoNovoController.workspace).val("");
		$("#precoCapaProduto", lancamentoNovoController.workspace).text("");
		$("#pacotePadrao", lancamentoNovoController.workspace).text("");
		$("#reparteProduto", lancamentoNovoController.workspace).text("");
		$("#alteracaoReparteProduto", lancamentoNovoController.workspace).text("");
		$("#diferencaProdutoInput", lancamentoNovoController.workspace).val("");
		
		$("#codigoProdutoInputAlteracaoReparte", lancamentoNovoController.workspace).val("");
		$("#nomeProdutoInputAlteracaoReparte", lancamentoNovoController.workspace).val("");
		$("#edicaoProdutoInputAlteracaoReparte", lancamentoNovoController.workspace).val("");
		$("#diferencaProdutoInputAlteracaoReparte", lancamentoNovoController.workspace).val("");
		
		$("#fieldCota", lancamentoNovoController.workspace).hide();
		$(".trCotas", lancamentoNovoController.workspace).remove();

		$("#paraEstoque", lancamentoNovoController.workspace).check();
		
		$(".trCotas", lancamentoNovoController.workspace).remove();
		$("#cotaInput1", lancamentoNovoController.workspace).val("");
		$('#rateioIDInputHidden1', lancamentoNovoController.workspace).val("");
		$("#nomeInput1", lancamentoNovoController.workspace).val("");
		$("#cotaInputAlteracaoReparte", lancamentoNovoController.workspace).val("");
		$('#rateioIDInputHiddenAlteracaoReparte', lancamentoNovoController.workspace).val("");
		$("#nomeInputAlteracaoReparte", lancamentoNovoController.workspace).val("");
		$("#reparteText1", lancamentoNovoController.workspace).text("");
		$("#diferencaInput1", lancamentoNovoController.workspace).val("");
		$("#reparteAtualText1", lancamentoNovoController.workspace).text("");
		$("#checkboxLancCota", lancamentoNovoController.workspace).uncheck();
		$("#tipoDiferenca", lancamentoNovoController.workspace).val("");
		
		$(".prodSemCota", lancamentoNovoController.workspace).show();
		$(".prodComCota", lancamentoNovoController.workspace).hide();
		
		$("#dateNotaEnvio", lancamentoNovoController.workspace).val("");
		$("#cotaInputNota", lancamentoNovoController.workspace).val("");
		$("#nomeCotaNota", lancamentoNovoController.workspace).val("");
		
		$("#nomeEstoqueSpan", lancamentoNovoController.workspace).html("Estoque");
		
		$("#divPesquisaProdutosNota", lancamentoNovoController.workspace).hide();

		$("#tipoDiferenca", lancamentoNovoController.workspace).removeAttr("disabled");
		$("#checkboxLancCota", lancamentoNovoController.workspace).removeAttr("disabled");
		$("#codigoProdutoInput", lancamentoNovoController.workspace).removeAttr("disabled");
		$("#nomeProdutoInput", lancamentoNovoController.workspace).removeAttr("disabled");
		$("#edicaoProdutoInput", lancamentoNovoController.workspace).removeAttr("disabled");
		$("#diferencaProdutoInput", lancamentoNovoController.workspace).removeAttr("disabled");
		$(".viewNotaEnvio", lancamentoNovoController.workspace).removeAttr("disabled");
		$("#incluirNovosProduto", lancamentoNovoController.workspace).show();
		$("#divDataNotaEnvio",lancamentoNovoController.workspace).find("img").show();
		$("#viewIncluirNovaDiferenca", lancamentoNovoController.workspace).show();
	},
	
	popupNovasDiferencas : function() {
		
		lancamentoNovoController.resetarCamposTela();
		
		lancamentoNovoController.redirecionarProdutosEstoque = false;
		
		lancamentoNovoController.idDiferenca = null;
		
		lancamentoNovoController.openModalDiferenca();
		
		var tipoDiferenca = $("#tipoDiferenca", lancamentoNovoController.workspace).val();
		
		lancamentoNovoController.tratarVisualizacaoOpcaoEstoque(tipoDiferenca);
	},
	
	editarDiferenca:function(idDiferenca){
		
		$.postJSON(
			contextPath + "/estoque/diferenca/lancamento/rateio/buscarDiferenca", 
			[{name:"idDiferenca", value:idDiferenca}],
			function(result) {
				
				var diferenca = result.diferenca;
				
				if (diferenca.tipoDiferenca == 'SOBRA_DE_DIRECIONADA_COTA') {
					
					$("#tipoDiferenca", lancamentoNovoController.workspace).val('SOBRA_DE');
					
				} else if (diferenca.tipoDiferenca == 'SOBRA_EM_DIRECIONADA_COTA') {
					
					$("#tipoDiferenca", lancamentoNovoController.workspace).val('SOBRA_EM');
					
				} else {
				
					$("#tipoDiferenca", lancamentoNovoController.workspace).val(diferenca.tipoDiferenca);
				}
				
				$("#idProdutoEdicao", lancamentoNovoController.workspace).val(result.idProdutoEdicao);
				
				lancamentoNovoController.idDiferenca = idDiferenca;
				
				if (lancamentoNovoController.isTipoDiferencaAlteracaoReparte(diferenca.tipoDiferenca)) {
					
					$("#tipoDiferenca", lancamentoNovoController.workspace).val('ALTERACAO_REPARTE_PARA_LANCAMENTO');
					
					lancamentoNovoController.carregarEdicaoAlteracaoReparte(result);
					
				} else {
				
					if(diferenca.tipoDirecionamento == 'COTA'){
						
						lancamentoNovoController.carregarEdicaoDirecionamentoCota(result);
						$("#reparteProduto", lancamentoNovoController.workspace).text('');
					}
					else if(diferenca.tipoDirecionamento == 'ESTOQUE'){
						
						lancamentoNovoController.carregarEdicaoDirecionamentoEstoque(result);
						$("#reparteProduto", lancamentoNovoController.workspace).text(diferenca.qtdeEstoqueAtual);
					}
					else if(diferenca.tipoDirecionamento == 'NOTA'){
						
						lancamentoNovoController.carregarEdicaoDirecionamentoNota(result);
						$("#reparteProduto", lancamentoNovoController.workspace).text(diferenca.qtdeEstoqueAtual);
					}
				}
				
				lancamentoNovoController.desabilitarCamposEdicaoDiferenca();				
			},
			null, 
			true
		);
	
		lancamentoNovoController.openModalDiferenca();
	},
	
	isTipoDiferencaAlteracaoReparte : function(tipoDiferenca) {
		
		return /^ALTERACAO_REPARTE?/.test(tipoDiferenca);
	},
	
	desabilitarCamposEdicaoDiferenca:function(){
		
		$("#tipoDiferenca", lancamentoNovoController.workspace).attr("disabled", "disabled");
		$("#checkboxLancCota", lancamentoNovoController.workspace).attr("disabled", "disabled");
		$("#codigoProdutoInput", lancamentoNovoController.workspace).attr("disabled", "disabled");
		$("#nomeProdutoInput", lancamentoNovoController.workspace).attr("disabled", "disabled");
		$("#edicaoProdutoInput", lancamentoNovoController.workspace).attr("disabled", "disabled");
		$("#viewIncluirNovaDiferenca", lancamentoNovoController.workspace).hide();
		$(".viewNotaEnvio", lancamentoNovoController.workspace).attr("disabled","disabled");
		
	},
	
	carregarProdutoAlteracaoReparte : function(diferenca) {
		
		$(".alteracaoReparte #diferencaProdutoInput", lancamentoNovoController.workspace).removeAttr("disabled");
		
		if (diferenca.codigoProduto){
			
			$(".alteracaoReparte #codigoProdutoInput", lancamentoNovoController.workspace).val(diferenca.codigoProduto);
		}
		
		if (diferenca.descricaoProduto){
			
			$(".alteracaoReparte #nomeProdutoInput", lancamentoNovoController.workspace).val(diferenca.descricaoProduto);
		}
		
		if (diferenca.numeroEdicao && diferenca.numeroEdicao){
			
			$(".alteracaoReparte #edicaoProdutoInput", lancamentoNovoController.workspace).val(diferenca.numeroEdicao);
		}
		
		if (!diferenca.precoVenda){
			
			$(".alteracaoReparte #precoCapaProduto", lancamentoNovoController.workspace).text("0,00");
			
		} else{
			
			$(".alteracaoReparte #precoCapaProduto", lancamentoNovoController.workspace).text(diferenca.precoVenda);
		}
		
		if (diferenca.pacotePadrao){
			
			$(".alteracaoReparte #pacotePadrao", lancamentoNovoController.workspace).text(diferenca.pacotePadrao);
		}
		
		if (diferenca.quantidade){
			
			$(".alteracaoReparte #diferencaProdutoInput", lancamentoNovoController.workspace).val(diferenca.quantidade);
			
		} else {
			
			$(".alteracaoReparte #diferencaProdutoInput", lancamentoNovoController.workspace).val(0);
		}
		
		lancamentoNovoController.buscarDadosAlteracaoReparte(diferenca.tipoEstoque);
	},
	
	carregarDiferencaProduto:function(diferenca){
		
		$("#diferencaProdutoInput", lancamentoNovoController.workspace).removeAttr("disabled");
		
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
		
		if (diferenca.pacotePadrao){
			
			$("#pacotePadrao", lancamentoNovoController.workspace).text(diferenca.pacotePadrao);
		}
		
		if (diferenca.quantidade){
			
			$("#diferencaProdutoInput", lancamentoNovoController.workspace).val(diferenca.quantidade);
			
		} else {
			
			$("#diferencaProdutoInput", lancamentoNovoController.workspace).val(0);
		}
	},
	
	carregarEdicaoAlteracaoReparte : function(result) {
		
		lancamentoNovoController.limparCotas();
		
		$("#ui-dialog-title-dialogNovasDiferencas", lancamentoNovoController.workspace).text("Lançamento Faltas e Sobras - Produto");
		
		lancamentoNovoController.tratarVisualizacaoOpcaoEstoque('ALTERACAO_REPARTE');		

		$.each(result.rateios, function(linhaAtual, rateio) {
			
			$("#cotaInputAlteracaoReparte", lancamentoNovoController.workspace).val(rateio.numeroCota);
			$("#nomeInputAlteracaoReparte", lancamentoNovoController.workspace).val(rateio.nomeCota);
			$("#rateioIDInputHiddenAlteracaoReparte", lancamentoNovoController.workspace).val(rateio.idRateio);
		});
		
		lancamentoNovoController.carregarProdutoAlteracaoReparte(result.diferenca);
	},
	
	carregarEdicaoDirecionamentoCota:function(result){
		lancamentoNovoController.limparCotas();
		
		lancamentoNovoController.carregarDiferencaProduto(result.diferenca);
		
		$("#checkboxLancCota", lancamentoNovoController.workspace).uncheck();
		
		$(".prodComCota", lancamentoNovoController.workspace).hide();
		$(".prodSemCota", lancamentoNovoController.workspace).show();
		$("#ui-dialog-title-dialogNovasDiferencas", lancamentoNovoController.workspace).text("Lançamento Faltas e Sobras - Produto");
		
		$("#paraCota",lancamentoNovoController.workspace).check();
		
		if(result.rateios){
			
			lancamentoNovoController.renderizarlistaRateio(result.rateios);
		}
	},
	
	carregarEdicaoDirecionamentoEstoque:function(result){
		
		lancamentoNovoController.carregarDiferencaProduto(result.diferenca);
		
		$("#checkboxLancCota", lancamentoNovoController.workspace).uncheck();
		
		$(".prodComCota", lancamentoNovoController.workspace).hide();
		$(".prodSemCota", lancamentoNovoController.workspace).show();
		$("#ui-dialog-title-dialogNovasDiferencas", lancamentoNovoController.workspace).text("Lançamento Faltas e Sobras - Produto");
		
		$("#paraEstoque",lancamentoNovoController.workspace).check();
		
		var tipoEstoque = result.diferenca != null ? "(" + result.diferenca.descricaoTipoEstoque + ")" : "";
		$("#nomeEstoqueSpan").html("Estoque " + tipoEstoque);

		lancamentoNovoController.paraEstoque(true);
		
		lancamentoNovoController.tratarVisualizacaoOpcaoEstoque(result.diferenca.tipoDiferenca);
	},
	
	carregarEdicaoDirecionamentoNota:function(result){
		
		$("#checkboxLancCota", lancamentoNovoController.workspace).check();
		
		$(".prodComCota", lancamentoNovoController.workspace).show();
		$(".prodSemCota", lancamentoNovoController.workspace).hide();
		$("#ui-dialog-title-dialogNovasDiferencas", lancamentoNovoController.workspace).text("Lançamento Faltas e Sobras - Cota");
		
		if(result.rateios){
			
			var rateio = result.rateios[0];
			
			$("#dateNotaEnvio", lancamentoNovoController.workspace).val(rateio.dataEnvioNota);
			$("#cotaInputNota", lancamentoNovoController.workspace).val(rateio.numeroCota);
			$("#nomeCotaNota", lancamentoNovoController.workspace).val(rateio.nomeCota);
			$("#rateioIDInputHiddenNota", lancamentoNovoController.workspace).val(rateio.idRateio);
			
			$(".viewNotaEnvio", lancamentoNovoController.workspace).attr("disabled", "disabled");
			$("#incluirNovosProduto", lancamentoNovoController.workspace).hide();
			$("#divDataNotaEnvio",lancamentoNovoController.workspace).find("img").hide();
		
		}
	
		lancamentoNovoController.carregarProdutoEdicaoNotaEnvio(result.diferenca);
		
		lancamentoNovoController.alterarReparteAtual(0);
		
		lancamentoNovoController.recalcularReparteAtualNotaEnvio();
	},
	
	renderizarlistaRateio:function(result){
		
		$(".trCotas", lancamentoNovoController.workspace).remove();
			
			$.each(result, function(linhaAtual, value) {
					
				if(linhaAtual > 0){
					
					  var tr = $('<tr class="trCotas" id="trCota'+ (linhaAtual + 1) +'" style="'+ ((linhaAtual +1) % 2 == 0 ? "background: #F5F5F5;" : "") +'">' +
								'<td><input type="text" name="cotaInput" maxlength="10" id="cotaInput'+ (linhaAtual +1) +'" onblur="pesquisaCotaLancamentoFaltasSobras.pesquisarPorNumeroCota(cotaInput'+ (linhaAtual +1) +', nomeInput'+ (linhaAtual +1) +', true, lancamentoNovoController.buscarReparteAtualCota('+ (linhaAtual +1)+'),lancamentoNovoController.erroPesquisaCota,'+(linhaAtual +1)+');" style="width:60px;" />'
								+'<input type="hidden" name="rateioIDInputHidden"  id="rateioIDInputHidden'+ (linhaAtual +1) +' " />'
								+'</td>' +
								'<td>'+
								     '<input type="text" name="nomeInput" maxlength="255" id="nomeInput'+ (linhaAtual+1) +'" style="width:300px;" '+
								         ' onkeyup="pesquisaCotaLancamentoFaltasSobras.autoCompletarPorNome(nomeInput'+ (linhaAtual+1) +');" ' +
								         ' onblur="pesquisaCotaLancamentoFaltasSobras.pesquisarPorNomeCota(cotaInput'+ (linhaAtual+1) +', nomeInput'+ (linhaAtual+1) +', lancamentoNovoController.buscarReparteAtualCota('+ (linhaAtual + 1)+'),lancamentoNovoController.erroPesquisaCota,'+(linhaAtual+1)+');" ' +
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
				
				$("#diferencaInput" + (linhaAtual+1), lancamentoNovoController.workspace).justInput(/[0-9]/);
				
				$("#cotaInput" + (linhaAtual+1), lancamentoNovoController.workspace).val(value.numeroCota);
					
				$("#nomeInput" + (linhaAtual+1), lancamentoNovoController.workspace).val(value.nomeCota);
					
				$("#reparteText" + (linhaAtual+1), lancamentoNovoController.workspace).text(value.reparteCota);
					
				$("#diferencaInput" + (linhaAtual+1), lancamentoNovoController.workspace).val(value.quantidade);
					
				$("#reparteAtualText" + (linhaAtual+1), lancamentoNovoController.workspace).text(value.reparteAtualCota);
				
				$("#rateioIDInputHidden" + (linhaAtual+1), lancamentoNovoController.workspace).val(value.idRateio);
				
			});
			
			$("input[name='diferencaInput']",lancamentoNovoController.workspace).keyup(function(e){
			  if (!isNaN(parseInt(this.value)) && parseInt(this.value) == 0){ 
				this.value = "";
			  }else{
				return;
			  }
			});
			
			$("#fieldCota", lancamentoNovoController.workspace).show();
			
			lancamentoNovoController.recalcularReparteAtualRateio();
	},
	
	openModalDiferenca:function(){
		
		$("#dialogNovasDiferencas", lancamentoNovoController.workspace).dialog({
			resizable: false,
			height:570,
			width:690,
			modal: true,
			buttons: {
				"Confirmar": function() {
					
					lancamentoNovoController.processarNovaDiferenca();
					
					lancamentoNovoController.houveAlteracaoLancamentos = true;
				},
				"Cancelar": function() {
					$("#gridNovasDiferencas", lancamentoNovoController.workspace).flexAddData({rows:[]});
					$(this).dialog("close");
					
					lancamentoNovoController.houveAlteracaoLancamentos = false;
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
			
			var valueDiferenca = '';
			
			if(row.cell.quantidade && row.cell.quantidade!= '' ){
			
				valueDiferenca = row.cell.quantidade; 
			}
			
			var valueEstoqueAtual = row.cell.qtdeEstoque;
			
			if(row.cell.qtdeEstoqueAtual && (row.cell.qtdeEstoqueAtual!= '' || row.cell.qtdeEstoqueAtual!= null)){
				
				valueEstoqueAtual = row.cell.qtdeEstoqueAtual;
			}
			
			row.cell.descricaoProduto = '<div>'+ row.cell.descricaoProduto +'</div>';
			row.cell.precoVenda = '<div>'+ row.cell.precoVenda +'</div>';
			row.cell.codigoProduto = '<div name="codigoProdutoNota" id="codigoProdutoNota'+ index +'">'+ row.cell.codigoProduto +'</div>';
			row.cell.pacotePadrao = '<div id="pacotePadrao'+ index +'">'+ row.cell.pacotePadrao +'</div>';
			row.cell.reparte = '<div id="reparte'+ index +'">'+ row.cell.qtdeEstoque +'</div>';
			row.cell.qtdeEstoqueAtual = '<div id="qtdTotal'+ index +'">'+ valueEstoqueAtual +'</div>';
			row.cell.valorTotalDiferenca = 
				'<input type="text" value="'+valueDiferenca+'" name="diferencaProduto" class="maskDiferencaProduto" style="width:45px; value="0" text-align: center; margin-right:10px;" maxlenght="255" '+
				' id="inputDiferencaProduto'+ index +'" onchange="lancamentoNovoController.alterarReparteAtual('+ index +');" />';
			row.cell.valorTotalDiferenca = '<div>'+ row.cell.valorTotalDiferenca +'</div>';
			row.cell.numeroEdicao = '<div>'+ row.cell.numeroEdicao +'</div>';
			
		});
		
		return resultado;
	},

	cadastrarNovasDiferencas : function(isBotaoIncluirNovo) {
		
		var lancamentoPorCota = $('#checkboxLancCota', lancamentoNovoController.workspace).attr('checked') ? true : false;
		
		if(lancamentoPorCota == true){
			
			lancamentoNovoController.cadastrarDiferencaNotaEnvio(isBotaoIncluirNovo);
		}
		else{
			
			lancamentoNovoController.cadastrarDiferencaEstoqueRateio(isBotaoIncluirNovo);
		}
	},
	
	cadastrarDiferencaEstoqueRateio:function (isBotaoIncluirNovo){
		
		var tipoDiferenca = $("#tipoDiferenca", lancamentoNovoController.workspace).val();
		
		var sufixoAlteracaoReparte = "";
		
		if (lancamentoNovoController.isTipoDiferencaAlteracaoReparte(tipoDiferenca)) {
			sufixoAlteracaoReparte = "AlteracaoReparte";
		}
		
		var codigoProduto = $("#codigoProdutoInput"+sufixoAlteracaoReparte, lancamentoNovoController.workspace).val();
		
		var edicaoProduto = $("#edicaoProdutoInput"+sufixoAlteracaoReparte, lancamentoNovoController.workspace).val();
		
		var diferenca = $("#diferencaProdutoInput"+sufixoAlteracaoReparte, lancamentoNovoController.workspace).val();
		
		var direcionadoParaEstoque = $('#paraEstoque', lancamentoNovoController.workspace).attr('checked') ? true : false;
		
		var reparteAtual = $("#reparteProduto", lancamentoNovoController.workspace).html();
		
		var pacotePadrao = $("#pacotePadrao", lancamentoNovoController.workspace).html();
		
		var tipoEstoque = lancamentoNovoController.tipoEstoqueSelecionado;
		
		if (lancamentoNovoController.isTipoDiferencaAlteracaoReparte(tipoDiferenca)) {
			
			tipoEstoque = $("#selectTipoEstoqueAlteracaoReparte", lancamentoNovoController.workspace).val();
			direcionadoParaEstoque = false;
			reparteAtual = $("#alteracaoReparteProduto", lancamentoNovoController.workspace).html();
			
			if (reparteAtual == 0 || reparteAtual == null || reparteAtual == '') {
				
				exibirMensagemDialog('WARNING', ['Não há reparte deste produto para esta cota.'], '');
				return;
			}
		}
		
		var data = [
				 {name: "codigoProduto", value: codigoProduto},
				 {name: "edicaoProduto", value: edicaoProduto},
				 {name: "diferenca", value: diferenca},
				 {name: "direcionadoParaEstoque", value: direcionadoParaEstoque},
				 {name: "reparteAtual", value: reparteAtual},
				 {name: "idDiferenca", value:lancamentoNovoController.idDiferenca},
				 {name: "tipoEstoque", value:tipoEstoque},
				 {name: "pacotePadrao", value:pacotePadrao}
		 ];
		
		var qntReparteRateio = 0;
		
		if (lancamentoNovoController.isTipoDiferencaAlteracaoReparte(tipoDiferenca)) {
			
			var linhasDaGrid = $('#gridAlteracaoReparteCota tr', this.workspace);
			
			$.each(linhasDaGrid, function(index, value) {

				var linha = $(value);
				
				var numeroCota = $(linha.find("td")[0],this.workspace).find('input[id="cotaInputAlteracaoReparte"]').val();
				
				var idRateioCota =  $(linha.find("td")[0],this.workspace).find('input[id="rateioIDInputHiddenAlteracaoReparte"]').val();
				
				var nomeCota = $(linha.find("td")[1],this.workspace).find('input[id="nomeInputAlteracaoReparte"]').val();
				
				var reparte = $(linha.find("td")[2],this.workspace).html();
				
				var diferenca = $("#diferencaProdutoInput", this.workspace).val();
	
				var reparteAtual = $(linha.find("td")[4],this.workspace).html();
				
				reparte = $("#alteracaoReparteProduto", this.workspace).html();
				
				if (numeroCota == undefined 
						|| numeroCota == '' 
						|| diferenca == undefined 
						|| diferenca == '') {
					
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
			
		} else {
		
			var linhasDaGrid = $('#grid_1 tr',this.workspace);
			
			$.each(linhasDaGrid, function(index, value) {
	
				var linha = $(value);
				
				var numeroCota = $(linha.find("td")[0],this.workspace).find('input[name="cotaInput"]').val();
				
				var idRateioCota =  $(linha.find("td")[0],this.workspace).find('input[name="rateioIDInputHidden"]').val();
				
				var nomeCota = $(linha.find("td")[1],this.workspace).find('input[name="nomeInput"]').val();
				
				var reparte = $(linha.find("td")[2],this.workspace).html();
				
				var diferenca = $(linha.find("td")[3],this.workspace).find('input[name="diferencaInput"]').val();
				
				if (lancamentoNovoController.isTipoDiferencaAlteracaoReparte(tipoDiferenca)) {
					
					diferenca = $("#diferencaProdutoInput", this.workspace).val();
					reparte = $("#alteracaoReparteProduto", this.workspace).html();
				}
				
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
		}
		
		data.push({name: "qntReparteRateio", value: qntReparteRateio});
		
		if (direcionadoParaEstoque
				|| tipoDiferenca == 'FALTA_DE'
				|| tipoDiferenca == 'FALTA_EM'
				|| lancamentoNovoController.isTipoDiferencaAlteracaoReparte(tipoDiferenca)) {
			
			data.push({name: "tipoDiferenca", value: tipoDiferenca});
			
		} else if (tipoDiferenca == 'SOBRA_DE') {
			
			data.push({name: "tipoDiferenca", value: 'SOBRA_DE_DIRECIONADA_COTA'});
			
		} else if (tipoDiferenca == 'SOBRA_EM') {
			
			data.push({name: "tipoDiferenca", value: 'SOBRA_EM_DIRECIONADA_COTA'});
		}
		
		$.postJSON(
			contextPath + "/estoque/diferenca/lancamento/cadastrarNovasDiferencas", 
			data,
			function(result) {

				lancamentoNovoController.processamentoSucessoCadastroNovaDiferenca(isBotaoIncluirNovo);
				
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
	
	cadastrarDiferencaNotaEnvio:function(isBotaoIncluirNovo){
		
		var tipoDiferenca = $("#tipoDiferenca", lancamentoNovoController.workspace).val();
		
		var dataNotaEnvio = $("#dateNotaEnvio", lancamentoNovoController.workspace).val();
		
		var numeroCota = $("#cotaInputNota", lancamentoNovoController.workspace).val();
		
		var nomeCota =  $("#nomeCotaNota", lancamentoNovoController.workspace).val(); 
	
		var data = [
					 {name: "tipoDiferenca", value: tipoDiferenca},
					 {name: "dataNotaEnvio", value: dataNotaEnvio},
					 {name: "numeroCota", value: numeroCota},
					 {name: "nomeCota", value: nomeCota},
					 {name: "idDiferenca", value:lancamentoNovoController.idDiferenca}
			 ];
		
		var linhasDaGrid = $('.lanctoFaltasSobrasCota_3Grid tr',this.workspace);
		
		$.each(linhasDaGrid, function(index, value) {

			var linha = $(value);
			
			var codigoProduto = $(linha.find("td")[0],this.workspace).find("div").find("div").html();
			
			var nomeProduto = $(linha.find("td")[1],this.workspace).find("div").find("div").html();
			
			var numeroEdicao = $(linha.find("td")[2],this.workspace).find("div").find("div").html();
			
			var pacotePadrao = $(linha.find("td")[4],this.workspace).find("div").find("div").html();
			
			var reparte = $(linha.find("td")[5],this.workspace).find("div").find("div").html();
			
			var diferenca = $(linha.find("td")[6],this.workspace).find("div").find('input[name="diferencaProduto"]').val();
			
			var reparteAtual = $(linha.find("td")[7],this.workspace).find("div").find("div").html();
			
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
			data.push({name: "diferencasProdutos["+index+"].pacotePadrao", value:pacotePadrao});

		});
		
		$.postJSON(
				contextPath + "/estoque/diferenca/lancamento/cadastrarNovasDiferencasNotaEnvio", 
				data,
				function(result) {
					
					lancamentoNovoController.processamentoSucessoCadastroNovaDiferenca(isBotaoIncluirNovo );
				},
				function(result){
					
					lancamentoNovoController.tratarErroCadastroNovasDiferencasEnvioNota(result);

				},
				true
			);
	},
	
	processamentoSucessoCadastroNovaDiferenca:function(isBotaoIncluirNovo ){
		
		var data = [
					 {name: "tipoDiferenca", value: $("#selectTiposDiferenca", lancamentoNovoController.workspace).val()},
					 {name: "dataMovimento", value: $("#datePickerDataMovimento", lancamentoNovoController.workspace).val()},
					];
		
		$("#gridLancamentos", lancamentoNovoController.workspace).flexOptions({
			url : contextPath + '/estoque/diferenca/lancamento/pesquisa/novos',
			params: data
		});
		
		$("#gridLancamentos", lancamentoNovoController.workspace).flexReload();
		
		if(!isBotaoIncluirNovo){
			$("#dialogNovasDiferencas", lancamentoNovoController.workspace).dialog("close");
		}
		else{
			
			var tipoDiferencaAnterior = $("#tipoDiferenca", lancamentoNovoController.workspace).val();
			
			lancamentoNovoController.popupNovasDiferencas();
			
			$("#tipoDiferenca", lancamentoNovoController.workspace).val(tipoDiferencaAnterior);
		}
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
	
	tratarErroCadastroNovasDiferencasEnvioNota : function(jsonData) {

		if (!jsonData || !jsonData.mensagens) {

			return;
		}

		var dadosValidacao = jsonData.mensagens.dados;
		
		var linhasDaGrid = $(".lanctoFaltasSobrasCota_3Grid tr", lancamentoNovoController.workspace);

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

		var data = {'filtro.codigo':$(idCodigoProduto).val(), numeroEdicao: $(idEdicaoProduto).val()};
		
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

		var data = {idProdutoEdicao:produtoEdicao.id};
		
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
			
			if(!idProdutoEdicao && idProdutoEdicao.length > 0) {
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
					'<td><input type="text" name="cotaInput" maxlength="10" id="cotaInput'+ (linhaAtual + 1) +'" onblur="pesquisaCotaLancamentoFaltasSobras.pesquisarPorNumeroCota(cotaInput'+ (linhaAtual + 1) +', nomeInput'+ (linhaAtual + 1) +', true, lancamentoNovoController.buscarReparteAtualCota,lancamentoNovoController.erroPesquisaCota,'+(linhaAtual + 1)+');" style="width:60px;" /></td>' +
					'<td>'+
					     '<input type="text" name="nomeInput" maxlength="255" id="nomeInput'+ (linhaAtual + 1) +'" style="width:300px;" '+
					         ' onkeyup="pesquisaCotaLancamentoFaltasSobras.autoCompletarPorNome(nomeInput'+ (linhaAtual + 1) +');" ' +
					         ' onblur="pesquisaCotaLancamentoFaltasSobras.pesquisarPorNomeCota(cotaInput'+ (linhaAtual + 1) +', nomeInput'+ (linhaAtual + 1) +', lancamentoNovoController.buscarReparteAtualCota,lancamentoNovoController.erroPesquisaCota,'+(linhaAtual + 1)+');" ' +
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
			$("#diferencaInput" + (linhaAtual + 1), lancamentoNovoController.workspace).justInput(/[0-9]/);
			
			$("#diferencaInput" + (linhaAtual + 1),lancamentoNovoController.workspace).keyup(function(e){
			  if (!isNaN(parseInt(this.value)) && parseInt(this.value) == 0){ 
				this.value = "";
			  }else{
				return;
			  }
			});
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
								
								if($("#cotaInput" + idDiv, lancamentoNovoController.workspace).val() == ''){
									$("#cotaInput" + idDiv, lancamentoNovoController.workspace).focus();
									return;
								}
								
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
		
		var direcionadoParaEstoque = $('#paraEstoque', lancamentoNovoController.workspace).attr('checked') ? true : false;
		
		$.postJSON(
			contextPath + "/estoque/diferenca/lancamento/rateio/buscarPrecoProdutoEdicao",
			[
			 	{name: "codigoProduto", value: $("#codigoProdutoInput", lancamentoNovoController.workspace).val()},
			 	{name: "numeroEdicao", value: $("#edicaoProdutoInput", lancamentoNovoController.workspace).val()},
			 	{name: "direcionadoParaEstoque", value:direcionadoParaEstoque}
			],
			function(result) {
				$("#precoCapaProduto", lancamentoNovoController.workspace).text(result[0]);
				$("#pacotePadrao", lancamentoNovoController.workspace).text(result[1]);
				$("#idProdutoEdicao", lancamentoNovoController.workspace).val(result[2]);
				
				if ($("#paraEstoque").is(":checked")) {
					
					lancamentoNovoController.verificarTipoEstoque(result[3]);
					
				} else {
					
					lancamentoNovoController.tipoEstoqueSelecionado = 'LANCAMENTO';
				}
				
			},
			function (){
				$("#edicaoProdutoInput", lancamentoNovoController.workspace).val("");
				$("#precoCapaProduto", lancamentoNovoController.workspace).text("");
				$("#pacotePadrao", lancamentoNovoController.workspace).text("");
				$("#idProdutoEdicao", lancamentoNovoController.workspace).val("");
			},
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
		$('#cotaInputAlteracaoReparte', lancamentoNovoController.workspace).val('');
		$('#nomeInputAlteracaoReparte', lancamentoNovoController.workspace).val('');
	},
	
	limparProduto : function() {
		$("#precoCapaProduto", lancamentoNovoController.workspace).text('');
		$("#pacotePadrao", lancamentoNovoController.workspace).text('');
		$("#reparteProduto", lancamentoNovoController.workspace).text('');
		$("#diferencaProdutoInput", lancamentoNovoController.workspace).val('');
		
		lancamentoNovoController.limparCotas();
	},
	
	verificarTipoEstoque : function(estoques) {
		
		if (estoques.length == 0) {
			
			lancamentoNovoController.tipoEstoqueSelecionado = 'LANCAMENTO';
			
			$("#reparteProduto", lancamentoNovoController.workspace).text(0);
		
		} else if (estoques.length == 1) {
			
			lancamentoNovoController.tipoEstoqueSelecionado = estoques[0].nameEnum;
			
			$("#reparteProduto", lancamentoNovoController.workspace).text(estoques[0].qtde);
		
		} else  {
			
			$( "#selectTipoEstoque").clear();
			
			lancamentoNovoController.tipoEstoqueSelecionado = null;
						
			$.each(estoques, function(index, item){
				$( "#selectTipoEstoque").append('<option enum="'+item.nameEnum+'" valor="'+item.qtde+'">'+item.desc+'</option>');
			});
			
			lancamentoNovoController.atualizarQuantidade();
			
			$( "#dialog-tipo-estoque", this.workspace).dialog({
				resizable: false,
				height:160,
				width:330,
				modal: true,
				closeOnEscape: false,
			    open: function(event, ui) { $(".ui-dialog-titlebar-close", $(this).parent()).hide(); },
				buttons: {
					"Confirmar": function() {
						
						var qtdeEstoque = $( "#selectTipoEstoque :selected").attr('valor');
						
						$("#reparteProduto", lancamentoNovoController.workspace).text(qtdeEstoque);
						
						lancamentoNovoController.tipoEstoqueSelecionado = $( "#selectTipoEstoque :selected").attr('enum');
						
						$('#diferencaProdutoInput').focus();
						
						$( this ).dialog( "close" );
					},
					"Cancelar": function() {
						
						lancamentoNovoController.popupConfirmacaoEstoque();
					}
				},
				form: $("#dialog-tipo-estoque", this.workspace).parents("form")
			});
		}
		
	},
	
	popupConfirmacaoEstoque : function() {
		
		if (!lancamentoNovoController.tipoEstoqueSelecionado) {
			
			$( "#dialog-selecionar-tipo-estoque", this.workspace).dialog({
				resizable: false,
				modal: true,
				closeOnEscape: false,
				open: function(event, ui) { $(".ui-dialog-titlebar-close", $(this).parent()).hide(); },
				buttons: {
					"Sim": function() {
						
						if (!lancamentoNovoController.tipoEstoqueSelecionado) {
							
							lancamentoNovoController.tipoEstoqueSelecionado = "LANCAMENTO";
							
							var reparteAtual = $( "#selectTipoEstoque option[enum='LANCAMENTO']").attr("valor");
							
							$("#reparteProduto", lancamentoNovoController.workspace).text(reparteAtual);
						}
						
						$( this ).dialog( "close" );
						
						$("#dialog-tipo-estoque", this.workspace).dialog("close");
					},
					"Não": function() {
						
						lancamentoNovoController.limparProduto();
						
						$("#codigoProdutoInput", lancamentoNovoController.workspace).val('');
						$("#nomeProdutoInput", lancamentoNovoController.workspace).val('');
						$("#edicaoProdutoInput", lancamentoNovoController.workspace).val('');
						
						$("#dialog-tipo-estoque", this.workspace).dialog("close");
						
						$(this).dialog("close");
						
					}
				},
				form: $("#dialog-selecionar-tipo-estoque", this.workspace).parents("form")
			});
		}
	},
	
	atualizarQuantidade : function() {		
		 var qtde = $( "#selectTipoEstoque :selected").attr('valor');
		 $('#qtdeTipoDialog').val(qtde);
	},
	
	calcularReparteAtual : function(index){
		
		var valorReparte = lancamentoNovoController.obterValorReparteAtual(index);
				
		if(valorReparte<0){
			var numeroCota = $("#cotaInput" + index, lancamentoNovoController.workspace).val();
			$("#diferencaInput" + index, lancamentoNovoController.workspace).val("");
			valorReparte = "";
			exibirMensagemDialog('WARNING', ['O campo[Diferença não pode ser maior que o campo [Reparte] da Cota número['+numeroCota+']'],'');
		}
		
		$("#reparteAtualText" + index, lancamentoNovoController.workspace).text(valorReparte);
	},
	
	carregarProdutoEdicaoNotaEnvio:function(diferenca){
		
		var rows = [];
        
        rows.push({"id":1,"cell":diferenca});

        $(".lanctoFaltasSobrasCota_3Grid", lancamentoNovoController.workspace).flexAddData({rows:rows,page:1,total:1});

		$("#divPesquisaProdutosNota", lancamentoNovoController.workspace).show();
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
		
		var valorDiferencaProduto = 0;
		
		if(!$("#inputDiferencaProduto" + indexDiv, lancamentoNovoController.workspace).val().trim() == ''){
			
			valorDiferencaProduto = parseInt($("#inputDiferencaProduto" + indexDiv, lancamentoNovoController.workspace).val());
		}
		
		var valorReparte = parseInt($("#reparte" + indexDiv, lancamentoNovoController.workspace).text());
		
		var valorReparteAtual;
		
		if ($("#tipoDiferenca", lancamentoNovoController.workspace).val() == "SOBRA_DE"
			|| $("#tipoDiferenca", lancamentoNovoController.workspace).val() == "SOBRA_EM"
			|| $("#tipoDiferenca", lancamentoNovoController.workspace).val() == "SOBRA_EM_DIRECIONADA_COTA"
			|| $("#tipoDiferenca", lancamentoNovoController.workspace).val() == "SOBRA_DE_DIRECIONADA_COTA") {
			
			valorReparteAtual = valorReparte + valorDiferencaProduto;
			
		} else if ($("#tipoDiferenca", lancamentoNovoController.workspace).val() == "FALTA_DE"
			|| $("#tipoDiferenca", lancamentoNovoController.workspace).val() == "FALTA_EM") {
			
			valorReparteAtual = valorReparte - valorDiferencaProduto;

			if(valorReparteAtual<0){
				$("#inputDiferencaProduto" + indexDiv, lancamentoNovoController.workspace).val("");
				valorReparteAtual = "";
				var codigoProduto = $("#codigoProdutoNota" + indexDiv, lancamentoNovoController.workspace).text();
				exibirMensagemDialog('WARNING', ['O campo[Diferença não pode ser maior que o campo [Reparte Total] da Produro com código['+codigoProduto+']'],'');
			}
		}
		
		$("#qtdTotal" + indexDiv, lancamentoNovoController.workspace).text(valorReparteAtual);
	},
	
	tratarVisualizacaoOpcaoEstoque:function(value) {

		if (value == 'FALTA_DE' || value == 'SOBRA_DE') {
			
			$(".view-estouque",this.workspace).show();
			$(".view-cota", this.workspace).hide();
			$(".lctoPorCota", this.workspace).show();
			$(".prodSemCota", this.workspace).show();
			$(".alteracaoReparte", this.workspace).hide();
			$("#paraEstoque", this.workspace).check();
			
		} else if (lancamentoNovoController.isTipoDiferencaAlteracaoReparte(value)) {
			
			$(".view-estouque",this.workspace).hide();
			$(".view-cota", this.workspace).hide();
			$(".prodSemCota", this.workspace).hide();
			$(".prodComCota", this.workspace).hide();
			$(".lctoPorCota", this.workspace).hide();
			$(".alteracaoReparte", this.workspace).show();
			
			lancamentoNovoController.limparCamposProdutoAlteracaoReparte();
			
			lancamentoNovoController.buscarEstoquesAlteracaoReparte();
			
		} else {
			
			$(".view-cota", this.workspace).show();
			$(".prodSemCota", this.workspace).show();
			$("#paraEstoque", this.workspace).check();
			$(".lctoPorCota", this.workspace).show();
			$(".alteracaoReparte", this.workspace).hide();
		}
	},
	
	limparCamposProdutoAlteracaoReparte : function() {
		
		$('#codigoProdutoInput', this.workspace).val("");
		$('#nomeProdutoInput', this.workspace).val("");
		$('#edicaoProdutoInput', this.workspace).val("");
		$('#diferencaProdutoInput', this.workspace).val("");
		$('#alteracaoReparteProduto', this.workspace).text("");
		$('#saldoConsignado', this.workspace).text("");
	},
	
	atualizarSaldoConsignado : function() {
		
		var reparte = 0;
		
		if ($('#alteracaoReparteProduto', this.workspace).text().trim() != '') {
			
			reparte = eval($('#alteracaoReparteProduto', this.workspace).text());
		}
		
		var reparteDevolvido = 0;
		
		if ($('#diferencaProdutoInputAlteracaoReparte', this.workspace).val() != 0) {
			
			reparteDevolvido = eval($('#diferencaProdutoInputAlteracaoReparte', this.workspace).val());
		}
		
		var saldoConsignado = reparte - reparteDevolvido;
		
		$('#saldoConsignado', this.workspace).text(saldoConsignado);
	},
	
	buscarEstoquesAlteracaoReparte : function() {
		
		$.postJSON(
			contextPath + "/estoque/diferenca/lancamento/buscarEstoquesAlteracaoReparte",
			null,
			function(result) {
				
				$("#selectTipoEstoqueAlteracaoReparte").clear();
							
				$.each(result, function(index, item){
					$("#selectTipoEstoqueAlteracaoReparte").append('<option value="'+item.nameEnum+'">'+item.desc+'</option>');
				});
			},
			null,
			true
		);
	},
	
	buscarReparteCotaProduto : function() {
		
		$.postJSON(
			contextPath + "/estoque/diferenca/lancamento/buscarReparteCotaProduto",
			[
			 	{name: "codigoProduto", value: $("#codigoProdutoInputAlteracaoReparte", lancamentoNovoController.workspace).val()},
			 	{name: "numeroEdicao", value: $("#edicaoProdutoInputAlteracaoReparte", lancamentoNovoController.workspace).val()},
			 	{name: "numeroCota", value: $("#cotaInputAlteracaoReparte", lancamentoNovoController.workspace).val()}
			],
			function(result) {
				
				$("#alteracaoReparteProduto", lancamentoNovoController.workspace).text(result);
				$("#diferencaProdutoInput", lancamentoNovoController.workspace).focus();
			},
			null,
			true,
			''
		);
	},
	
	atualizarTipoEstoqueSelecionado : function() {
		
		lancamentoNovoController.tipoEstoqueSelecionado = $("#selectTipoEstoqueAlteracaoReparte", lancamentoNovoController.workspace).val();
	},
	
	buscarDadosAlteracaoReparte : function(tipoEstoque) {
		
		$.postJSON(
			contextPath + "/estoque/diferenca/lancamento/buscarReparteCotaProduto",
			[
			 	{name: "codigoProduto", value: $("#codigoProdutoInputAlteracaoReparte", lancamentoNovoController.workspace).val()},
			 	{name: "numeroEdicao", value: $("#edicaoProdutoInputAlteracaoReparte", lancamentoNovoController.workspace).val()},
			 	{name: "numeroCota", value: $("#cotaInputAlteracaoReparte", lancamentoNovoController.workspace).val()}
			],
			function(result) {
				
				$("#alteracaoReparteProduto", lancamentoNovoController.workspace).text(result);
				$("#diferencaProdutoInputAlteracaoReparte", lancamentoNovoController.workspace).focus();
				
				lancamentoNovoController.atualizarSaldoConsignado();
				
				$("#selectTipoEstoqueAlteracaoReparte", lancamentoNovoController.workspace).val(tipoEstoque);
			},
			null,
			true,
			''
		);
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
	
	recalcularReparteAtual:function(){
		
		var isLancamentoCota =
			$("#checkboxLancCota", lancamentoNovoController.workspace).is(":checked");
		
		if (isLancamentoCota) {
			
			lancamentoNovoController.recalcularReparteAtualNotaEnvio();
			
		} else {
			
			var direcionarCota = $('#paraCota', lancamentoNovoController.workspace).is(":checked");
			
			if (direcionarCota) {
				lancamentoNovoController.recalcularReparteAtualRateio();
			}
		}
	},
	
	recalcularReparteAtualRateio:function(){
		
		var linhasDaGrid = $('#grid_1 tr',this.workspace);
		
		var mensagens =[];
		
		$.each(linhasDaGrid, function(index, value) {
			
			var linha = $(value);
			
			var numeroCota = $(linha.find("td")[0],this.workspace).find('input[name="cotaInput"]').val();
			
			var diferenca = $(linha.find("td")[3],this.workspace).find('input[name="diferencaInput"]').val();
			
			if( numeroCota == undefined || numeroCota == '' 
					|| diferenca == undefined || diferenca == ''){
				
				return;
			}
			
			var valorReparte = lancamentoNovoController.obterValorReparteAtual(index);
			
			if(valorReparte<0){
				$("#diferencaInput" + index, lancamentoNovoController.workspace).val("");
				valorReparte = "";
				mensagens.push('O campo[Diferença não pode ser maior que o campo [Reparte] da Cota número['+numeroCota+']');
			}
			
			$("#reparteAtualText" + index, lancamentoNovoController.workspace).text(valorReparte);
			
		});
		
		if(mensagens.length > 0){
			exibirMensagemDialog('WARNING', mensagens,'');
		}
	},
	
	obterValorReparteAtual:function(index){
		
		var numeroCota = $("#cotaInput" + index, lancamentoNovoController.workspace).val();
		
		if( numeroCota == undefined || numeroCota == '' ){	
			return;
		}
		
		if ($("#reparteText" + index, lancamentoNovoController.workspace).text() == ""){
			
			$("#reparteText" + index, lancamentoNovoController.workspace).text(0);
		}
	
		if ($("#diferencaInput" + index, lancamentoNovoController.workspace).val() == ""){
			
			$("#diferencaInput" + index, lancamentoNovoController.workspace).val(0);
		}
		
		var valorReparteAtual = 0;
		
		var valorReparteRateio = eval($("#reparteText" + index, lancamentoNovoController.workspace).text());
		
		var valorDiferenca = eval($("#diferencaInput" + index, lancamentoNovoController.workspace).val());
		
		var tipoDiferenca = $("#tipoDiferenca", lancamentoNovoController.workspace).val();
		
		if (tipoDiferenca == "SOBRA_DE" || tipoDiferenca == "SOBRA_EM"
			|| tipoDiferenca == "SOBRA_DE_DIRECIONADA_COTA" || tipoDiferenca == "SOBRA_EM_DIRECIONADA_COTA"){
			
			valorReparteAtual = valorReparteRateio +  valorDiferenca;
			
		} else {

			valorReparteAtual = valorReparteRateio -  valorDiferenca;
		}
		
		return valorReparteAtual;
	},
	
	recalcularReparteAtualNotaEnvio:function(){
		
		var linhasDaGrid = $('.lanctoFaltasSobrasCota_3Grid tr',this.workspace);
		
		var mensagens =[];
		
		$.each(linhasDaGrid, function(index, value) {
			
			if($("#inputDiferencaProduto" + index, lancamentoNovoController.workspace).val()!=""){
				
				var valorReparteAtual = 0;
				
				var valorReparteProduto = eval($("#reparte" + index, lancamentoNovoController.workspace).text());
				
				var valorDiferenca = eval($("#inputDiferencaProduto" + index, lancamentoNovoController.workspace).val());
				
				var tipoDiferenca = $("#tipoDiferenca", lancamentoNovoController.workspace).val();
				
				if (tipoDiferenca == "SOBRA_DE" || tipoDiferenca == "SOBRA_EM"){
					valorReparteAtual = valorReparteProduto +  valorDiferenca;
				} else {
					
					valorReparteAtual = valorReparteProduto -  valorDiferenca;
					
					if(valorReparteAtual<0){
						$("#inputDiferencaProduto" + index, lancamentoNovoController.workspace).val("");
						valorReparteAtual = "";
						var codigoProduto = $("#codigoProdutoNota" + index, lancamentoNovoController.workspace).text();
						mensagens.push('O campo[Diferença não pode ser maior que o campo [Reparte Total] da Produro com código['+codigoProduto+']');
					}
				}
				
				$("#qtdTotal" + index, lancamentoNovoController.workspace).text(valorReparteAtual);
			}
			
		});
		
		if(mensagens.length > 0){
			exibirMensagemDialog('WARNING', mensagens,'');
		}
	}
	
}, BaseController);

//@ sourceURL=lancamentoNovo.js
