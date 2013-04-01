var distribuicaoManual = $.extend(true, {
	
	rowCount : 0,
	workspace : null,
	inputNomeCota : '<div><input type="text" class="inputNomeCotaGrid" id="nomeCotaGrid#index" value="#valor" onblur="distribuicaoManual.checaSeRemoveu(\'#nomeCotaGrid#index\', #index)"></div>',
	inputPercEstoque : '<div class="textoGridCota" id="percEstoqueGrid#index" >#valor</div>',
	inputReparte : '<div><input type="text" id="reparteGrid#index" name="reparteGrid" value="#valor" onchange="distribuicaoManual.calcularPercEstoque(#index)" class="inputGridCota" /></div>',
	inputNumeroCota : '<div><input type="text" id="numeroCotaGrid#index" name="numeroCotaGrid" value="#valor" onchange="distribuicaoManual.pesquisarCota(\'#numeroCotaGrid#index\', #index)" class="inputGridCota" /></div>',
	
	init : function() {
		distribuicaoManual.workspace = $('.estudosManuaisGrid');
		this.configGrid();
		distribuicaoManual.workspace.find('tbody').append(distribuicaoManual.construirLinhaVazia());
		distribuicaoManual.rowCount++;
		this.atualizarTotalDistribuido(0);
	},
	
	voltar : function() {
		$(".ui-tabs-selected").find("span").click();
		$("a[href='"+ pathTela +"/matrizDistribuicao']").click();
	},
	
	atualizarTotalDistribuido : function(valor) {
		totalDistribuido = valor;
		$('#totalDistribuido').html(valor);
	},

	somarReparteDistribuido : function(index) {
		var soma = 0;
		for (var i = 0; i < distribuicaoManual.rowCount; i++) {
			if (i != index) {
				if ($("#reparteGrid"+ i, distribuicaoManual.workspace).val()) {
					soma += parseInt($("#reparteGrid"+ i, distribuicaoManual.workspace).val());
				}
			}
		}
		return soma;
	},
	
	calcularPercEstoque : function(index) {
		$("#percEstoqueGrid"+ index, distribuicaoManual.workspace).html('0');
		var totalGeral = parseInt($("#reparteInicial").val());
		var repCota = parseInt($("#reparteGrid"+ index, distribuicaoManual.workspace).val());
		var totalDistribuido = distribuicaoManual.somarReparteDistribuido(index);
		if (repCota <= totalGeral) {
			var repDistrib = totalGeral - totalDistribuido;
			if (repCota <= repDistrib) {
				$("#percEstoqueGrid"+ index, distribuicaoManual.workspace).html(((repCota / totalGeral) * 100).toFixed(2).replace('.', ','));
				totalDistribuido += repCota;
				$('#totalDistribuido').html(totalDistribuido);
				repDistrib -= repCota;
				$('#repDistribuir').html(repDistrib);
			} else {
				$("#reparteGrid"+ index, distribuicaoManual.workspace).val('0');
				exibirMensagemDialog('ERROR', ['Você não possui saldo suficiente para distribuir essa quantidade para a cota, reveja os valores.'], '');
				$("#reparteGrid"+ index, distribuicaoManual.workspace).focus();
			}
		} else {
			$("#reparteGrid"+ index, distribuicaoManual.workspace).val('0');
			exibirMensagemDialog('ERROR', ['O reparte da cota deve ser menor que o Total de Reparte a Distribuir.'], '');
			$("#reparteGrid"+ index, distribuicaoManual.workspace).focus();
		}
	},
	
	construirLinhaVazia : function() {
		var linhaVazia = '';
		if (!distribuicaoManual.workspace.find('tbody')[0]) {
			linhaVazia += '<tbody>';
		}
		linhaVazia += '<tr id="row'+ (distribuicaoManual.rowCount + 1) +'"><td align="left" abbr="numeroCota"><div style="text-align: left; width: 90px;">';
		linhaVazia += distribuicaoManual.inputNumeroCota.replace(/#index/g, distribuicaoManual.rowCount).replace(/#valor/g, '');
		linhaVazia += '</div></td><td align="left" abbr="nomeCota"><div style="text-align: left; width: 135px;">';
		linhaVazia += distribuicaoManual.inputNomeCota.replace(/#index/g, distribuicaoManual.rowCount).replace(/#valor/g, '');
		linhaVazia += '</div></td><td align="center" abbr="reparte"><div style="text-align: center; width: 65px;">';
		linhaVazia += distribuicaoManual.inputReparte.replace(/#index/g, distribuicaoManual.rowCount).replace(/#valor/g, '0');
		linhaVazia += '</div></td><td align="center" abbr="percEstoque"><div style="text-align: center; width: 80px;">';
		linhaVazia += distribuicaoManual.inputPercEstoque.replace(/#index/g, distribuicaoManual.rowCount).replace(/#valor/g, '0');
		linhaVazia += '</div></td></tr>';
		if (!distribuicaoManual.workspace.find('tbody')[0]) {
			linhaVazia += '</tbody>';
			distribuicaoManual.workspace.append(linhaVazia);
		} else {
			distribuicaoManual.workspace.find('tbody').append(linhaVazia);
		}
		this.configAutoComplete('#nomeCotaGrid'+ distribuicaoManual.rowCount, distribuicaoManual.rowCount);
		distribuicaoManual.rowCount++;
	},
	
	cotaJaExiste : function(numeroCota, index) {
		for (var i = 0; i < distribuicaoManual.rowCount; i++) {
			if (i != index) {
				var cota = $("#numeroCotaGrid"+ i, distribuicaoManual.workspace).val();
				if ((cota) && (cota == numeroCota)) {
					return true;
				}
			}
		}
		return false;
	},
	
	pesquisarCota : function(idObjetoCota, index) {
		
		var numeroCota = $(idObjetoCota).val().trim();
		if (!distribuicaoManual.cotaJaExiste(numeroCota, index)) {
			if(numeroCota.length == 0){
				$("#row"+ (index + 1), distribuicaoManual.workspace).remove();
				distribuicaoManual.conferirTotais();
	 			return;	
	 		}
	 		$.postJSON(contextPath + "/distribuicaoManual/consultarCotaPorNumero",
					{numeroCota : numeroCota},
					function(result){
						$("#nomeCotaGrid"+ index, distribuicaoManual.workspace).val(result.nomePessoa);
						distribuicaoManual.construirLinhaVazia();
	 				},
	 				function(result){
						//Verifica mensagens de erro do retorno da chamada ao controller.
						if (result.mensagens) {
							exibirMensagemDialog(result.mensagens.tipoMensagem, result.mensagens.listaMensagens, "");
						}
					}, true, null
			);
		} else {
			$("#numeroCotaGrid"+ index, distribuicaoManual.workspace).val('');
			exibirMensagemDialog('ERROR', ['A cota de número '+ numeroCota +' já foi inserida anteriormente.'], '');
		}
	},
	
	configGrid : function () {
		distribuicaoManual.workspace.flexigrid({
			dataType : 'json',
				colModel : [ {
					display : 'Cota',
					name : 'numeroCota',
					width : 90,
					sortable : true,
					align : 'left'
				}, {
					display : 'Nome',
					name : 'nomeCota',
					width : 135,
					sortable : true,
					align : 'left'
				}, {
					display : 'Reparte',
					name : 'reparte',
					width : 65,
					sortable : true,
					align : 'center'
				}, {
					display : '% do Estoque',
					name : 'percEstoque',
					width : 80,
					sortable : true,
					align : 'center'
				}],
				width : 447,
				height : 270
			});
	},

	checaSeRemoveu : function(idCaixaTexto, index) {
		if (($('#numeroCotaGrid'+ index, distribuicaoManual.workspace).val()) &&
				(($(idCaixaTexto, distribuicaoManual.workspace).val() == null) ||
				($(idCaixaTexto, distribuicaoManual.workspace).val() == ''))) {
			$("#row"+ (index + 1), distribuicaoManual.workspace).remove();
			distribuicaoManual.conferirTotais();
		}
	},
	
	conferirTotais : function() {
		var totalGeral = parseInt($("#reparteInicial").val());
		var totalDistribuido = distribuicaoManual.somarReparteDistribuido(-1);
		$('#totalDistribuido').html(totalDistribuido);
		$('#repDistribuir').html(totalGeral - totalDistribuido);
	},
	
	configAutoComplete : function(idCaixaTexto, index) {

		$(idCaixaTexto, distribuicaoManual.workspace).autocomplete({
			source : function(request, response) {
				$.ajax({
					url: contextPath + "/distribuicaoManual/consultarCotaPorNome",
					type: 'POST',
					dataType: "json",
					data: {nomeCota: request.term},
					success: function(result) {
						response(result.result);
					}});
			},
			select : function(event, ui) {
				$('#numeroCotaGrid'+ index, distribuicaoManual.workspace).val(ui.item.value);
				distribuicaoManual.construirLinhaVazia();
			},
			focus: function( event, ui ) {
				$('#nomeCotaGrid'+ index, distribuicaoManual.workspace).val(ui.item.label);
			},
			minLength: 3,
			delay : 800,
		});
	},
	
	gerarEstudo : function() {
		var data = [];
		data.push({'estudoDTO.produtoEdicaoId': $('#idProdutoEdicao').val()});
		data.push({'estudoDTO.reparteDistribuir': $('#reparteInicial').val()});
		data.push({'estudoDTO.dataLancamento': $('#dataLancamento').html()});
		for (var i = 0; i < distribuicaoManual.rowCount; i++) {
			if ($("#reparteGrid"+ i, distribuicaoManual.workspace).val()) {
				var temp = "estudoCotasDTO[";
				data.push({temp.concat(i, "].idCota"): $("#numeroCotaGrid"+ i, distribuicaoManual.workspace).val()});
				data.push({temp.concat(i, "].qtdeEfetiva"): $("#reparteGrid"+ i, distribuicaoManual.workspace).val()});
			}
		}
		$.postJSON(contextPath +"/distribuicaoManual/gravarEstudo",
				data,
				function(result){
					console.log(result);
					exibirMensagemDialog("SUCCESS", ["Operação realizada com sucesso!"], "");
 				},
 				function(result){
					if (result.mensagens) {
						exibirMensagemDialog(result.mensagens.tipoMensagem, result.mensagens.listaMensagens, "");
					}
				}, true, null
		);
	}
});

//@ sourceURL=distribuicaoManual.js