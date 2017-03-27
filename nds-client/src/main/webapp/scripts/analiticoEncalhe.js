var analiticoEncalheController = $.extend(true, {
	
	path : contextPath + '/devolucao/fechamentoEncalhe/',
	
	init : function() {
		
		$("#analiticoEncalhe_dataEncalhe").datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$("#analiticoEncalhe_dataEncalhe").val(fechamentoEncalheController.vDataEncalhe);
		
		this.initGridFechamentoAnalitico();
		this.initGridHistoricoEncalhe();
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
				width : 40,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeCota',
				width : 250,
				sortable : true,
				align : 'left'
			}, {
				display : 'Box Encalhe',
				name : 'boxEncalhe',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Total R$',
				name : 'total',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Cobrança',
				name : 'statusCobranca',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Usuário',
				name : 'usuario',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Início',
				name : 'inicio',
				width : 60,
				sortable : true,
				align : 'center'
					
			}, {
				display : 'Fim',
				name : 'fim',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Detalhes',
				name : 'acao',
				width : 70,
				sortable : true,
				align : 'center'			
			}],
			sortname : "numeroCota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});
	},
	
	initGridHistoricoEncalhe : function() {
	    //GRID DE HISTORICO DE ENCALHE
		$(function() {
			$(".dadosHistoricoEncalheGrid", fechamentoEncalheController.workspace).flexigrid({
				preProcess: fechamentoEncalheController.getDataFromResultDivida,
				dataType : 'json',
				colModel : [ {
					display : 'Usuario',
					name : 'usuario',
					width : 180,
					sortable : true,
					align : 'left'
				},{
					display : 'Box Encalhe',
					name : 'boxEncalhe',
					width : 200,
					sortable : true,
					align : 'left'
				},{
					display : 'Inicio',
					name : 'inicio',
					width : 40,
					sortable : true,
					align : 'center'
				},{
					display : 'Fim',
					name : 'fim',
					width : 40,
					sortable : true,
					align : 'center'
				},{
					display : 'Total Encalhe',
					name : 'valorEncalheFormatado',
					width : 70,
					sortable : true,
					align : 'center'
				}],
				width : 620,
				height : 160
			});
		});
	},
	
	preprocessamentoGrid : function(resultado) {
		
		var dataEncalhe = $('#analiticoEncalhe_dataEncalhe',this.workspace).val();
		
		$('#valorTotalAnalitico', workspace).html(resultado.valorTotalAnalitico);
		
		$('#totalCotaAnalitico', workspace).html(resultado.qtdCotas);
		
		$.each(resultado.tableModel.rows, function(index, value) {
			var id = value.cell.id;
			
			var acao = '<a href="javascript:;" onclick="analiticoEncalheController.popup_detalhes(' + value.cell.numeroCota + ', ' + dataEncalhe +');""><img src="' + contextPath + '/images/ico_detalhes.png" border="0" /></a>';
			
			value.cell.acao = acao;
		});
		
		return resultado.tableModel;
	},
	
	//POPUPS
    popup_detalhes : function (numeroCota, dataOperacao) {
    	
    	analiticoEncalheController.obterHistoricosConferenciaEncalhe(numeroCota, dataOperacao);
    	
		$( "#dialog-historico-detalhes", analiticoEncalheController.workspace ).dialog({
			resizable: false,
			width:650,
			height:350,
			modal: true,
			buttons:[ 
	          {
		           id:"bt_fechar",
		           text:"Fechar", 
		           click: function() {
		        	   
		        	   $(this).dialog( "close" );
		           }
	           }
	        ],
			form: $("dialog-historico-detalhes", this.workspace).parents("form")
		});
		$("#dialog-historico-detalhes", this.workspace).dialog("close");
		$(".grids", fechamentoEncalheController.workspace).show();
	},
	
	obterHistoricosConferenciaEncalhe : function(numeroCota, dataOperacao){
		
		var dataEncalhe = $('#analiticoEncalhe_dataEncalhe',this.workspace).val();
		
		$(".dadosHistoricoEncalheGrid", analiticoEncalheController.workspace).flexOptions({
			url: this.path + "obterHistoricosConferenciaEncalhe.json",
			params: [
			         {name:'numeroCota', value: numeroCota},
			         {name:'dataEncalhe', value: dataEncalhe}
			        ] ,
			        newp: 1
		});
		$(".dadosHistoricoEncalheGrid", analiticoEncalheController.workspace).flexReload();
		$(".grids", fechamentoEncalheController.workspace).show();
	},
	
}, BaseController);
//@ sourceURL=analiticoEncalhe.js