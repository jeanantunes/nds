var relatorioGarantiasController = $.extend(true, {
	
	path : contextPath + '/financeiro/relatorioGarantias/',

	init : function() {

		this.initRelatorioGarantiaGrid();
		this.initRelatorioTodasGarantiasDetalheGrid();
		this.initRelatorioTodasGarantiasGrid();

	},
	
	
	showGridTodasGarantias : function(){
		
		$('#garantiasEspecificas',this.workspace).hide();
		$('#todasGarantias',this.workspace).show(); 	 
		
	},
	
	showGridGarantiaEspecifica : function(){
		
		$('#todasGarantias',this.workspace).hide(); 
		$('#garantiasEspecificas',this.workspace).show();
		
	},
	
	

	pesquisar : function(){
		
		var params = $("#relatorioGarantiasForm", this.workspace).serialize();
		
		var garantiaSelecionada = $('#selectTipoGarantia option:selected').text();
		
		if(garantiaSelecionada == "Todas"){
			
			this.pesquisarTodasGarantias(params);

		}
		else{
			
			this.pesquisarPorGarantia(params);
			
		}	
		
},
		

	/*
	 * *********************
	 * Carregamento FlexGrid
	 * *********************
	 * */

	pesquisarTodasGarantias : function(params){
		
		$(".relatorioTodasGarantiasGrid").flexOptions({
			url : this.path + 'pesquisarTodasGarantias.json?' + params,
			preProcess : relatorioGarantiasController.montaColunaDetalhesGarantia,
			newp : 1
		});
		
		$(".relatorioTodasGarantiasGrid").flexReload();
		
		
		this.showGridTodasGarantias();
	
},

	pesquisarPorGarantia : function(params){
		
		$(".relatorioGarantiaGrid").flexOptions({
			url : this.path + 'pesquisarGarantia.json?' + params,
			preProcess : relatorioGarantiasController.validarSelecaoComboFiltro,
			newp : 1
		});
		
		$(".relatorioGarantiaGrid").flexReload();
		
		this.showGridGarantiaEspecifica();

	
},

	
	/*
	 * *************************
	 * Pre Carregamento FlexGrid
	 * *************************
	 * */
	
	validarSelecaoComboFiltro : function(data){
		
		if (data.mensagens) {
			exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
		} 
	    
		return data;
	},
	
	inserirTotalDetalheGrid : function(data){
			
			var total = '0,00';
			$.each(data.rows, function(index, value) {
				total = sumPrice(total,value.cell.vlrGarantia);
			});
			
			$("#totalGarantia",this.workspace).html(total);	
	    
		return data;
	},
	

	
	montaColunaDetalhesGarantia : function(data) {
		
		if (data.mensagens) {
			
			exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
			
		} else { 
			
			var garantiaSelecionada = $('#selectStatusGarantia option:selected').text();
			
			$.each(data.rows, function(index, value) {
								
				var link_detalhes = '<a title="Ver Detalhes" onclick="relatorioGarantiasController.popup_detalhe_Garantia(\'' + value.cell.tipoGarantia + '\', \'' + garantiaSelecionada + '\');" href="javascript:;"><img src="' + contextPath + '/images/ico_detalhes.png" alt="Detalhes" border="0" /></a>';
				value.cell.detalhe = link_detalhes;
			});
	
		}
		
		return data;
	},
	
	
	/*
	 * *************************
	 * Popups
	 * *************************
	 * */
	
	
	popup_detalhe_Garantia : function (tipoGarantia,tipoStatus) {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		
		var params = 'filtro.tipoGarantia='+tipoGarantia+'&filtro.statusGarantia='+tipoStatus;
		
		//alert(params);
		
		$(".garantiaDetalheGrid").flexOptions({
			url : this.path + 'pesquisarGarantia.json?'+ params,
			preProcess : relatorioGarantiasController.inserirTotalDetalheGrid,
			newp : 1
		});
		
		$(".garantiaDetalheGrid").flexReload();
			
	
		$( "#dialog-detalhe-garantia" ).dialog({
			resizable: false,
			height:420,
			width:860,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
				}
			}
		});
	},
	
	
	/*
	 * *************************
	 * Configurações do Grid
	 * *************************
	 * */
	
	
	initRelatorioTodasGarantiasGrid : function(){
		$(".relatorioTodasGarantiasGrid").flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Tipo de Garantia',
				name : 'tipoGarantia',
				width : 320,
				sortable : true,
				align : 'left'
			}, {
				display : 'Qtde. Cotas',
				name : 'qtdCotas',
				width : 250,
				sortable : true,
				align : 'center'
			}, {
				display : 'Valor Total R$',
				name : 'vlrTotal',
				width : 250,
				sortable : true,
				align : 'right'
			}, {
				display : 'Detalhe',
				name : 'detalhe',
				width : 70,
				sortable : true,
				align : 'center'
			}],
			sortname : "tipoGarantia",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
		
	},
	
	
	initRelatorioTodasGarantiasDetalheGrid : function(){
		$(".relatorioGarantiaGrid").flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 250,
				sortable : true,
				align : 'left'
			}, {
				display : 'Garantia',
				name : 'garantia',
				width : 155,
				sortable : true,
				align : 'left'
			}, {
				display : 'Vencimento',
				name : 'vencto',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Valor Garantia R$',
				name : 'vlrGarantia',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Faturamento',
				name : 'faturamento',
				width : 120,
				sortable : true,
				align : 'right'
			}, {
				display : '% Garantia s/Fat.',
				name : 'garantiaFaturamento',
				width : 100,
				sortable : true,
				align : 'right'
			}],
			width : 960,
			height : 255
		});
		
	},
	
	initRelatorioGarantiaGrid : function(){
		$(".garantiaDetalheGrid").flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 170,
				sortable : true,
				align : 'left'
			}, {
				display : 'Garantia',
				name : 'garantia',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Vencimento',
				name : 'vencto',
				width : 65,
				sortable : true,
				align : 'center'
			}, {
				display : 'Valor Garantia R$',
				name : 'vlrGarantia',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Faturamento',
				name : 'faturamento',
				width : 120,
				sortable : true,
				align : 'right'
			}, {
				display : '% Garantia s/Fat.',
				name : 'garantiaFaturamento',
				width : 100,
				sortable : true,
				align : 'right'
			}],
			width : 800,
			height : 245
		});		
	},
	
}, BaseController);

