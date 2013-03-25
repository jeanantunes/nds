var distribuicaoManualController = $.extend(true, {
	
	totalDistribuido : 0,
	totalADistribuir : 0,
	
	init : function() {
		this.configGrid();
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
	
	calcularPercEstoque : function(index) {
		
	},
	
	criarInputNumeroCota : function(resultado, index){
		var valor = "";
		if(resultado && resultado.numeroCota){
			valor = resultado.numeroCota;
		}
		var parametroPesquisaCota ='\'#numeroCotaGrid'+ index+ '\',' + index;
		var inputNumeroCota = '<div><input type="text" id="numeroCotaGrid'+ index +'" name="numeroCotaGrid" value="'+ valor
								+ '" onchange="distribuicaoManualController.pesquisarCota('+ parametroPesquisaCota
								+ ')" class="inputGridCota" /></div>';
		return inputNumeroCota;
	},
	
	criarInputReparte : function(resultado, index){
		var valor = 0;
		if(resultado && resultado.reparte){
			valor = resultado.reparte;
		}
		var inputReparte = '<div><input type="text" id="reparteGrid'+ index +'" name="reparteGrid" value="'+ valor
								+ '" onchange="distribuicaoManualController.calcularPercEstoque('+ index
								+ ')" class="inputGridCota" /></div>';
		return inputReparte;
	},
	
	pesquisarCota : function(numeroCota, index) {
		
		if($(numeroCota).val().trim().length == 0){
 			return;
 		}
 		$.postJSON(contextPath + "/distribuicaoManual/consultarCota",
				{numeroCota : $(numeroCota).val().trim()},
				function(result){
					$("#nomeCotaGrid"+ index, distribuicaoManualController.workspace).text(result.nomeCota);
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
	
	refreshGrid : function(resultado) {
		$.each(resultado.rows, function(index, row) {
			if (row.cell.numeroCota == null) {								
				row.cell.numeroCota = distribuicaoManualController.criarInputNumeroCota(resultado, index) ;
				row.cell.nomeCota = '<div class="textoGridCota" id="nomeCotaGrid'+index+'" ></div>';
				row.cell.reparte = distribuicaoManualController.criarInputReparte(resultado, index);
				row.cell.percEstoque = '<div class="textoGridCota" id="percEstoqueGrid'+index+'" ></div>';
			} else {
				row.cell.numeroCota = distribuicaoManualController.criarInputNumeroCota(resultado, index) ;
				row.cell.nomeCota = '<div class="textoGridCota" id="nomeCotaGrid'+index+'" >'+ row.cell.nomeCota +'</div>';
				row.cell.reparte = distribuicaoManualController.criarInputReparte(resultado, index);
				row.cell.percEstoque = '<div class="textoGridCota" id="percEstoqueGrid'+index+'" ></div>';
			}		
		});
		return resultado;
	},
	
	configGrid : function () {
		$(".estudosManuaisGrid", distribuicaoManualController.workspace).flexigrid({
			preProcess: distribuicaoManualController.refreshGrid,
			url: pathTela +'/distribuicaoManual/carregarGridVazia',
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
				width : 400,
				height : 270
			});
	}
});

//@ sourceURL=distribuicaoManual.js