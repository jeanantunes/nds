var movimentoFinanceiroCotaController = $.extend(true, {
	
	dataOperacaoDistribuidor: null,
	
	init : function() {
		
		$("#filtroNumCota", movimentoFinanceiroCotaController.workspace).numeric();
		
		$("#descricaoCota", movimentoFinanceiroCotaController.workspace).autocomplete({source: ""});

		$( "#dtPostergada", movimentoFinanceiroCotaController.workspace ).datepicker({
			
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		movimentoFinanceiroCotaController.dataOperacaoDistribuidor = $("#dataBaixa", movimentoFinanceiroCotaController.workspace).datepicker("getDate");
		
		movimentoFinanceiroCotaController.iniciarGridCotasAVista();
	},
	
	iniciarGridCotasAVista : function() {
		
		$(".cotasAVista").flexigrid({
			preProcess: movimentoFinanceiroCotaController.getDataFromResult,
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			},{
				display : 'Nome',
				name : 'nomeCota',
				width : 135,
				sortable : true,
				align : 'left'
			} ],
			sortname : "numeroCota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 750,
			height : 220
		});
	},

},

BaseController);

//@ sourceURL=movimentoFinanceiroCota.js
