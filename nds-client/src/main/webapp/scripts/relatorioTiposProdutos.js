var relatorioTiposProdutosController = $.extend(true, {
	
	path : contextPath + '/lancamento/relatorioTiposProdutos',


	init : function() {
		
		$("#dateLanctoDe", this.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$("#dateLanctoAte", this.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$("#dateRecoltoDe", this.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$("#dateRecoltoAte", this.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		this.initGrid();
	},
	
	
	pesquisar : function() {
		
		var params = $("#relatorioTiposProdutosForm", this.workspace).serialize();
		
		$(".tiposProdutosGrid", this.workspace).flexOptions({
			url : this.path + 'pesquisar.json?' + params, 
			newp : 1
		});
		
		$(".tiposProdutosGrid", this.workspace).flexReload();
	},
	
	
	initGrid : function() {
		
		$(".tiposProdutosGrid", this.workspace).flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 220,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Preço Capa R$',
				name : 'precoCapa',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Faturamento R$',
				name : 'faturamento',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Tipo de Produto',
				name : 'tipoProduto',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Lcto',
				name : 'lancamento',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Rclt',
				name : 'recolhimento',
				width : 80,
				sortable : true,
				align : 'center'
			}],
			sortname : "edicao",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
	}
	
	
}, BaseController);