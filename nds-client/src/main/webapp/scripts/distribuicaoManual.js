var distribuicaoManual = $.extend(true, {
	
	rowCount : 0,
	workspace : null,
	exibindoMensagem : false,
	hiddenIdCota : '<input type="hidden" id="idCotaGrid#index" value="#valor"/>',
	hiddenStatusCota : '<input type="hidden" id="statusCotaGrid#index" value="#valor"/>',
	inputNomeCota : '<div><input type="text" class="inputNomeCotaGrid" id="nomeCotaGrid#index" value="#valor" onblur="distribuicaoManual.checaSeRemoveu(\'#nomeCotaGrid#index\', #index)"></div>',
	inputPercEstoque : '<div class="textoGridCota" id="percEstoqueGrid#index" >#valor</div>',
	inputReparte : '<div><input type="text" class="inputGridCota" id="reparteGrid#index" name="reparteGrid" value="#valor" onchange="distribuicaoManual.calcularPercEstoque(#index)" class="inputGridCota" /></div>',
	inputNumeroCota : '<div><input type="text" class="inputGridCota" id="numeroCotaGrid#index" name="numeroCotaGrid" value="#valor" onchange="distribuicaoManual.pesquisarCota(\'#numeroCotaGrid#index\', #index)" /></div>',
	
	init : function() {
		distribuicaoManual.workspace = $('.estudosManuaisGrid');
		this.configGrid();
		distribuicaoManual.workspace.find('tbody').append(distribuicaoManual.construirLinhaVazia());
		distribuicaoManual.rowCount++;
		this.atualizarTotalDistribuido(0);
	},
	
	voltar : function() {
		distribuicaoManual.confirmar("#dialog-voltar", function() {
			$(".ui-tabs-selected").find("span").click();
			$("a[href='"+ pathTela +"/matrizDistribuicao']").click();
		});
	},
	
	cancelar : function() {
		distribuicaoManual.confirmar("#dialog-cancelar-estudo", function() {
			$(".ui-tabs-selected").find("span").click();
			$("a[href='"+ pathTela +"/matrizDistribuicao']").click();
		});
	},
	
	confirmar : function(dialogId, callbackFunction, errorCallback) {
		$(dialogId).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					callbackFunction();
					$(this).dialog("close");
				},
				"Cancelar": function() {
					errorCallback();
					$(this).dialog("close");
				}
			}
		});
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
		if (((repCota / totalGeral) * 100).toFixed(2) >= 5) {
			$("#reparteGrid"+ index, distribuicaoManual.workspace).css("background-color", "#FFFF00");
		} else {
			$("#reparteGrid"+ index, distribuicaoManual.workspace).css("background-color", "#FFFFFF");
		}
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
	
	existeLinhaVazia : function() {
		for (var i = 0; i < distribuicaoManual.rowCount; i++) {
			if (!$('#numeroCotaGrid'+ i, distribuicaoManual.workspace).val()
					&& ($('#numeroCotaGrid'+ i, distribuicaoManual.workspace).val() != undefined)
					&& !$('#nomeCotaGrid'+ i, distribuicaoManual.workspace).val()
					&& ($('#nomeCotaGrid'+ i, distribuicaoManual.workspace).val() != undefined)) {
				return true;
			}
		}
		return false;
	},
	
	construirLinhaVazia : function() {
		if (!distribuicaoManual.existeLinhaVazia()) {
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
		}
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
	
	limparLinha : function(index) {
		$("#numeroCotaGrid"+ index, distribuicaoManual.workspace).val('');
		$("#nomeCotaGrid"+ index, distribuicaoManual.workspace).val('');
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
						var callback = function() {
							$('#row'+ (index + 1), distribuicaoManual.workspace).append(
									distribuicaoManual.hiddenIdCota.replace(/#valor/g, result.idCota).replace(/#index/g, index));
							$('#row'+ (index + 1), distribuicaoManual.workspace).append(
									distribuicaoManual.hiddenStatusCota.replace(/#valor/g, result.status).replace(/#index/g, index));
							$('#nomeCotaGrid'+ index, distribuicaoManual.workspace).val(result.nomePessoa);
							$('#reparteGrid'+ index, distribuicaoManual.workspace).focus();
							distribuicaoManual.construirLinhaVazia();
							distribuicaoManual.exibindoMensagem = false;
						};
						if (result.status == 'SUSPENSO') {
							distribuicaoManual.exibindoMensagem = true;
							distribuicaoManual.confirmar('#dialog-status-suspenso', callback, function() {
								distribuicaoManual.limparLinha(index);
								distribuicaoManual.exibindoMensagem = false;
							});
						} else {
							callback();
						}
	 				},
	 				function(result){
						//Verifica mensagens de erro do retorno da chamada ao controller.
						if (result.mensagens) {
							$('#numeroCotaGrid'+ index, distribuicaoManual.workspace).val('');
							$('#numeroCotaGrid'+ index, distribuicaoManual.workspace).focus();
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
		if (!distribuicaoManual.exibindoMensagem) {
			if (($('#numeroCotaGrid'+ index, distribuicaoManual.workspace).val()) &&
					(($(idCaixaTexto, distribuicaoManual.workspace).val() == null) ||
					($(idCaixaTexto, distribuicaoManual.workspace).val() == ''))) {
				$("#row"+ (index + 1), distribuicaoManual.workspace).remove();
				distribuicaoManual.conferirTotais();
			}
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
				$('#row'+ (index + 1), distribuicaoManual.workspace).append(
						distribuicaoManual.hiddenIdCota.replace(/#valor/g, ui.item.chave.idCota).replace(/#index/g, index));
				$('#row'+ (index + 1), distribuicaoManual.workspace).append(
						distribuicaoManual.hiddenStatusCota.replace(/#valor/g, ui.item.chave.status).replace(/#index/g, index));
				$('#numeroCotaGrid'+ index, distribuicaoManual.workspace).val(ui.item.value);
				$('#reparteGrid'+ index, distribuicaoManual.workspace).focus();
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
		var callbackFunction = function() {
			var data = [];
			data.push({name: 'estudoDTO.produtoEdicaoId', value: $('#idProdutoEdicao').val()});
			data.push({name: 'estudoDTO.reparteDistribuir', value: $('#reparteInicial').val()});
			data.push({name: 'estudoDTO.dataLancamento', value: $('#dataLancamento').html()});
			for (var i = 0; i < distribuicaoManual.rowCount; i++) {
				if ($("#reparteGrid"+ i, distribuicaoManual.workspace).val() && ($("#reparteGrid"+ i, distribuicaoManual.workspace).val() > 0)) {
					data.push({name: "estudoCotasDTO["+ i +"].idCota", value: $("#numeroCotaGrid"+ i, distribuicaoManual.workspace).val()});
					data.push({name: "estudoCotasDTO["+ i +"].qtdeEfetiva", value: $("#reparteGrid"+ i, distribuicaoManual.workspace).val()});
				}
			}
			$.postJSON(contextPath +"/distribuicaoManual/gravarEstudo",
					data,
					function(result){
						$('#estudo').html(result.long);
						distribuicaoManual.bloquearCampos();
						exibirMensagemDialog("SUCCESS", ["Operação realizada com sucesso!"], "");
	 				},
	 				function(result){
						if (result.mensagens) {
							exibirMensagemDialog(result.mensagens.tipoMensagem, result.mensagens.listaMensagens, "");
						}
					}, true, null
			);
		};
		if (parseInt($('#repDistribuir').html()) > 0) {
			distribuicaoManual.confirmar("#dialog-saldo", callbackFunction);
		} else {
			callbackFunction();
		}
	},
	
	bloquearCampos : function() {
		for (var i = 0; i < distribuicaoManual.rowCount; i++) {
			if ($("#reparteGrid"+ i, distribuicaoManual.workspace).val()) {
				$("#numeroCotaGrid"+ i, distribuicaoManual.workspace).attr("disabled", true).css("background-color", "#f0f0f0");
				$("#nomeCotaGrid"+ i, distribuicaoManual.workspace).attr("disabled", true).css("background-color", "#f0f0f0");
				$("#reparteGrid"+ i, distribuicaoManual.workspace).attr("disabled", true).css("background-color", "#f0f0f0");
			}
		}
	},
	
	analisar : function() {
		//testa se registro selecionado possui estudo gerado
		if ($('#estudo').html() == null || $('#estudo').html() == "") {
			exibirMensagem("WARNING",["Gere o estudo antes de fazer a análise."]);
			return;
		} else {
			// Deve ir direto para EMS 2031
			matrizDistribuicao.redirectToTelaAnalise('#distribuicaoManualContent','#distribuicaoManualTelaAnalise', $('#estudo').html());
		}
	}
	
});

//@ sourceURL=distribuicaoManual.js