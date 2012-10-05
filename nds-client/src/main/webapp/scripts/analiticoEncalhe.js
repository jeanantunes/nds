var analiticoEncalheController = $.extend(true, {
	
	path : contextPath + '/devolucao/fechamentoEncalhe/',


	init : function() {
		
		$("#datepickerDe", analiticoEncalheController.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		this.initGridFechamentoAnalitico();
	},
	
	
	pesquisar : function() {
		
		var params = $("#formAnaliticoEncalhe", this.workspace).serialize();
		
		$(".fechamentoAnaliticoGrid", this.workspace).flexOptions({
			url : this.path + 'pesquisarAnalitico.json?' + params, 
			newp : 1
		});
		
		$(".visaoEstoqueGrid", this.workspace).flexReload();
		$('#divAnaliticoEncalheGrid', this.workspace).show();
	},
	
	
	limpaGridPesquisa : function() {

		$(".fechamentoAnaliticoGrid", analiticoEncalheController.workspace).clear();
		$('#divAnaliticoEncalheGrid', analiticoEncalheController.workspace).css("display", "none");
	},
	
	
	// TODO: rever
	imprimirArquivo : function(fileType) {

		var dataEncalhe = $("#datepickerDe", fechamentoEncalheController.workspace).val();
		
		window.location = contextPath + "/devolucao/fechamentoEncalhe/imprimirArquivo?"
			+ "dataEncalhe=" + vDataEncalhe
			+ "&fornecedorId="+ vFornecedorId
			+ "&boxId=" + vBoxId
			+ "&sortname=" + $(".fechamentoGrid", fechamentoEncalheController.workspace).flexGetSortName()
			+ "&sortorder=" + $(".fechamentoGrid", fechamentoEncalheController.workspace).getSortOrder()
			+ "&rp=" + $(".fechamentoGrid", fechamentoEncalheController.workspace).flexGetRowsPerPage()
			+ "&page=" + $(".fechamentoGrid", fechamentoEncalheController.workspace).flexGetPageNumber()
			+ "&fileType=" + fileType;

		return false;
	},

	 
	initGridFechamentoAnalitico : function() {
		
		$(".fechamentoAnaliticoGrid").flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 400,
				sortable : true,
				align : 'left'
			}, {
				display : 'Box Encalhe',
				name : 'boxEncalhe',
				width : 140,
				sortable : true,
				align : 'left'
			}, {
				display : 'Total R$',
				name : 'total',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Cobrança',
				name : 'cobranca',
				width : 180,
				sortable : true,
				align : 'left'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});
	},
	
	
}, BaseController);
