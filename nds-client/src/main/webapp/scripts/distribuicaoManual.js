var distribuicaoManualController = $.extend(true, {
	
	init : function() {
		this.initGrid();
	},
	
	initGrid : function () {
		$(".estudosManuaisGrid", distribuicaoManualController.workspace).flexigrid({
			preProcess: distribuicaoManualController.executarPreProcessamento,
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
					width : 55,
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