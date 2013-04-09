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
		
		var analise = '<select name="select" id="select" onchange="analiseEstudoController.carregarTela('+row.cell.numeroEstudo+', this)">'+
					  '<option selected="selected">Selecione...</option> <option value="Normal">Normal</option> <option value="Parcial">Parcial</option></select>';
		
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
	
	
	carregarTela : function (numeroEstudo, select){
		
		if((select.value == "Normal") || (select.value == "Parcial")){
			alert("numeroEstudo "+numeroEstudo+" select.value"+select.value);
		}
	}

}, BaseController);
//@ sourceURL=analiseEstudo.js

