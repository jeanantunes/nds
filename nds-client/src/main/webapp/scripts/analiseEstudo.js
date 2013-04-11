var analiseEstudoController = $.extend(true, {

init : function() {
	 $(".estudosGrid").flexigrid({
		preProcess : analiseEstudoController.executarPreProcessEstudosGrid,
		dataType : 'json',
		colModel : [ {
			display : 'Estudo',
			name : 'numeroEstudo',
			width : 80,
			sortable : true,
			align : 'left'
		}, {
			display : 'Código',
			name : 'codigoProduto',
			width : 60,
			sortable : true,
			align : 'left'
		}, {
			display : 'Produto',
			name : 'nomeProduto',
			width : 180,
			sortable : true,
			align : 'left'
		}, {
			display : 'Edição',
			name : 'numeroEdicaoProduto',
			width : 50,
			sortable : true,
			align : 'left'
		}, {
			display : 'Classificação',
			name : 'descicaoTpClassifProd',
			width : 150,
			sortable : true,
			align : 'left'
		}, {
			display : 'Período',
			name : 'codPeriodoProd',
			width : 50,
			sortable : true,
			align : 'center'
		}, {
			display : 'Tela de Análise',
			name : 'telaAnalise',
			width : 160,
			sortable : true,
			align : 'left'
		}, {
			display : 'Status',
			name : 'statusEstudo',
			width : 100,
			sortable : true,
			align : 'left'
		}],
		sortname : "box",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 950,
		height : 200
	});
},

	executarPreProcessEstudosGrid : function(resultado){
	
	if (resultado.mensagens) {
		exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
		);
		
		return resultado;
	}
	
	$.each(resultado.rows, function(index, row) {
		
		var analise = '<select name="select" id="select" style="width:140px;" onchange="analiseEstudoController.redirectToTelaAnalise('+ row.cell.numeroEstudo +',event);"> <option selected="selected">Selecione...</option> <option value="normal">Normal</option> <option value="parcial">Parcial</option>';
		
		row.cell.telaAnalise = analise;
	});
	
	$(".grids", analiseEstudoController.workspace).show();
	
	return resultado;
},

	carregarEstudos : function() {
		var data = [ 
			    {name : 'filtro.numEstudo', value : $("#idEstudo").val() }, 
			    {name : 'filtro.codigoProduto', value : $("#codProduto").val() }, 
				{name : 'filtro.nome', value : $("#produto").val() }, 
				{name : 'filtro.numeroEdicao', value : $("#edicaoProd").val() }, 
				{name : 'filtro.idTipoClassificacaoProduto', value : $("#comboClassificacao").val() }
			 ];
		
		$(".estudosGrid", this.workspace).flexOptions({url: contextPath + "/distribuicao/analiseEstudo/buscarEstudos", 
			params: data});
		
		$(".estudosGrid", this.workspace).flexReload();	
	},

	redirectToTelaAnalise : function(numeroEstudo, event){
		
		// Obter matriz de distribuição
		var matriz=[],
			url = contextPath + "/distribuicao/analiseEstudo/obterMatrizDistribuicaoPorEstudo";
		
		$.postJSON(
			url,
			[{name : "id" , value : numeroEstudo}],
			function(response){
				
				// CALLBACK
				// ONSUCESS
				matriz.push({name : "selecionado.classificacao" , value : response.classificacao});
				matriz.push({name : "selecionado.nomeProduto" , value : response.nomeProduto});
				matriz.push({name : "selecionado.codigoProduto" , value : response.codigoProduto});
				matriz.push({name : "selecionado.dataLcto" , value : response.dataLancto});
				matriz.push({name : "selecionado.edicao" , value : response.numeroEdicao});
				matriz.push({name : "selecionado.estudo" , value : response.idEstudo});
				matriz.push({name : "selecionado.idLancamento" , value : response.idLancamento});
//				matriz.push({name : "selecionado.lncto" , value : response.lancto});
//				matriz.push({name : "selecionado.repDistrib" , value : response.reparte});
//				matriz.push({name : "selecionado.sobra" , value : 0);
//				matriz.push({name : "selecionado.tipoSegmentoProduto" , value : response.);
//				matriz.push({name : "selecionado.periodicidadeProduto" , value : response.periodo});
				matriz.push({name : "selecionado.estudoLiberado" , value : (response.liberado == "" ? false : true)});

				
				// CARREGAR TELA EMS 2022
				$.get(
						contextPath + '/distribuicao/histogramaPosEstudo/index', //url
						null, // parametros
						function(html){ // onSucessCallBack
							$('#AnaliseEstudoMainContent').hide();
							$('#histogramaPosEstudoContent').html(html);
							$('#histogramaPosEstudoContent').show();

							histogramaPosEstudoController.matrizSelecionado = matriz;
							histogramaPosEstudoController.popularFieldsetHistogramaPreAnalise(matriz);
							histogramaPosEstudoController.modoAnalise = $(event.target).val();
				});
			}
		);
		
	},
	
}, BaseController);
//@ sourceURL=analiseEstudo.js

