var analiseEstudoController = $.extend(true, {

init : function() {
	 $(".estudosGrid").flexigrid({
		dataType : 'xml',
		colModel : [ {
			display : 'Estudo',
			name : 'estudo',
			width : 80,
			sortable : true,
			align : 'left'
		}, {
			display : 'Código',
			name : 'codigo',
			width : 60,
			sortable : true,
			align : 'left'
		}, {
			display : 'Produto',
			name : 'produto',
			width : 180,
			sortable : true,
			align : 'left'
		}, {
			display : 'Edição',
			name : 'edicao',
			width : 50,
			sortable : true,
			align : 'left'
		}, {
			display : 'Classificação',
			name : 'classificacao',
			width : 150,
			sortable : true,
			align : 'left'
		}, {
			display : 'Período',
			name : 'periodo',
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
			name : 'status',
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
	
	
	}, BaseController);
//@ sourceURL=analiseEstudo.js

