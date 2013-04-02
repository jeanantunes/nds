var distribuicaoManual = $.extend(true, {
	
	totalDistribuido : 0,
	totalADistribuir : 0,
	rowCount : 0,
	inputNomeCota : '<div class="textoGridCota" id="nomeCotaGrid#index" >#valor</div>',
	inputPercEstoque : '<div class="textoGridCota" id="percEstoqueGrid#index" >#valor</div>',
	inputReparte : '<div><input type="text" id="reparteGrid#index" name="reparteGrid" value="#valor" onchange="distribuicaoManual.calcularPercEstoque(#index)" class="inputGridCota" /></div>',
	inputNumeroCota : '<div><input type="text" id="numeroCotaGrid#index" name="numeroCotaGrid" value="#valor" onchange="distribuicaoManual.pesquisarCota(\'#numeroCotaGrid#index\', #index)" class="inputGridCota" /></div>',
	
	init : function() {
		this.configGrid();
		$(".estudosManuaisGrid", distribuicaoManual.workspace).find('tbody').append(distribuicaoManual.construirLinhaVazia());
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

	somarReparteDistribuido : function() {
		
	},
	
	calcularPercEstoque : function(index) {
		$("#percEstoqueGrid"+ index, distribuicaoManual.workspace).html('0');
		var repCota = parseInt($("#reparteGrid"+ index, distribuicaoManual.workspace).val());
		var repDistrib = parseInt($('#repDistribuir').html());
		var totalDistribuido = distribuicaoManual.somarReparteDistribuido;
		if (repCota < repDistrib) {
			totalDistribuido += repCota;
			if (totalDistribuido <= repDistrib) {
				$("#percEstoqueGrid"+ index, distribuicaoManual.workspace).html(Math.floor((repCota / repDistrib) * 100), 1);
				$('#totalDistribuido').html(totalDistribuido);
			} else {
				$("#reparteGrid"+ index, distribuicaoManual.workspace).val('');
				exibirMensagemDialog('ERROR', ['Você não possui saldo suficiente para distribuir essa quantidade para a cota, reveja os valores.'], '');
				$("#reparteGrid"+ index, distribuicaoManual.workspace).focus();
			}
		} else {
			$("#reparteGrid"+ index, distribuicaoManual.workspace).val('');
			exibirMensagemDialog('ERROR', ['O reparte da cota deve ser menor que o Total a Distribuir.'], '');
			$("#reparteGrid"+ index, distribuicaoManual.workspace).focus();
		}
	},
	
	construirLinhaVazia : function() {
		var linhaVazia = '';
		if (!$('.estudosManuaisGrid', distribuicaoManual.workspace).find('tbody')[0]) {
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
		if (!$('.estudosManuaisGrid', distribuicaoManual.workspace).find('tbody')[0]) {
			linhaVazia += '</tbody>';
			$('.estudosManuaisGrid', distribuicaoManual.workspace).append(linhaVazia);
		} else {
			$('.estudosManuaisGrid', distribuicaoManual.workspace).find('tbody').append(linhaVazia);
		}
		distribuicaoManual.rowCount++;
	},
	
	pesquisarCota : function(numeroCota, index) {
		
		if($(numeroCota).val().trim().length == 0){
			$("#row"+ (index + 1), distribuicaoManual.workspace)[0].remove();
 			return;
 		}
 		$.postJSON(contextPath + "/distribuicaoManual/consultarCota",
				{numeroCota : $(numeroCota).val().trim()},
				function(result){
					$("#nomeCotaGrid"+ index, distribuicaoManual.workspace).text(result.nomePessoa);
					distribuicaoManual.construirLinhaVazia();
 				},
 				function(result){
					//Verifica mensagens de erro do retorno da chamada ao controller.
					if (result.mensagens) {
						exibirMensagemDialog(
								result.mensagens.tipoMensagem,
								result.mensagens.listaMensagens,""
						);
					}
				}, true, null
		);
	},
	
	configGrid : function () {
		$(".estudosManuaisGrid", distribuicaoManual.workspace).flexigrid({
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
	}
});

//@ sourceURL=distribuicaoManual.js