var analiticoEncalheController = $.extend(true, {
	
	path : contextPath + '/devolucao/fechamentoEncalhe/',


	init : function() {
		
		$("#analiticoEncalhe_dataEncalhe").datepicker({
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
		
		$(".fechamentoAnaliticoGrid", this.workspace).flexReload();
		$('#divAnaliticoEncalheGrid', this.workspace).show();
		
		analiticoDataEncalhe = $('#analiticoEncalhe_dataEncalhe',this.workspace).val();
		analiticoFornecedorId = $('#selectFornecedor', this.workspace).val();
		analiticoBoxId = $('#selectBoxEncalhe', this.workspace).val();
		
	},
	
	
	limpaGridPesquisa : function() {

		$(".fechamentoAnaliticoGrid", this.workspace).clear();
		$('#divAnaliticoEncalheGrid', this.workspace).css("display", "none");
	},
	
	
	// TODO: rever
	imprimirArquivo : function(fileType) {
		
		window.location = contextPath + "/devolucao/fechamentoEncalhe/imprimirArquivoAnaliticoEncalhe?"
			+ "filtro.dataEncalhe=" + analiticoDataEncalhe
			+ "&filtro.fornecedorId="+ analiticoFornecedorId
			+ "&filtro.boxId=" + analiticoBoxId
			+ "&sortname=" + $(".fechamentoAnaliticoGrid", this.workspace).flexGetSortName()
			+ "&sortorder=" + $(".fechamentoAnaliticoGrid", this.workspace).getSortOrder()
			+ "&rp=" + $(".fechamentoAnaliticoGrid", this.workspace).flexGetRowsPerPage()
			+ "&page=" + $(".fechamentoAnaliticoGrid", this.workspace).flexGetPageNumber()
			+ "&fileType=" + fileType;

		return false;
	},

	 
	initGridFechamentoAnalitico : function() {
		
		$(".fechamentoAnaliticoGrid").flexigrid({
			preProcess: analiticoEncalheController.preprocessamentoGrid,
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeCota',
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
				name : 'statusCobranca',
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
	
	
	
	preprocessamentoGrid : function(resultado) {
		

		var total = "0,00";
		$.each(resultado.rows, function(index, row) {
			total = sumPrice(total,row.cell.total );
			
		});
		
		if (resultado.rows.length == 0){
			$('#valorTotal').html("0,00");
			$('#totalCota').html("0");
		} else {
			$('#valorTotal').html(total);
			$('#totalCota').html(resultado.rows.length);
		}
		
		return resultado;
	},
	
	
	
}, BaseController);

//@ sourceURL=analiticoEncalhe.js
