var relatorioGarantiasController = $.extend(true, {
	
	path : contextPath + '/financeiro/relatorioGarantias/',
	
	dataFaturamento: '',

	init : function() {

		this.initRelatorioGarantiaGrid();
		this.initRelatorioTodasGarantiasDetalheGrid();
		this.initRelatorioTodasGarantiasGrid();

		
	},
	
	hideGrids : function(){
		$('#garantiasEspecificas',this.workspace).hide();
		$('#todasGarantias',this.workspace).hide();
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
		
	
},

	pesquisarPorGarantia : function(params){
		
        $.postJSON(
                this.path + 'pesquisarGarantia.json?' + params,
                null,
                function(result) {
                       
                       if (result.mensagens) {
                              exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
                              relatorioGarantiasController.hideGrids();
                       }  else{
                              $(".relatorioGarantiaGrid").flexAddData(result);
                              $("#garantiasEspecificas th[abbr='faturamento'] >div").html("Faturamento " + result.rows[0].cell.baseCalculo);
                              relatorioGarantiasController.showGridGarantiaEspecifica();
                       }
                },
                null,
                true
         );


	
},

	
	/*
	 * *************************
	 * Pre Carregamento FlexGrid
	 * *************************
	 * */
	
	inserirTotalDetalheGrid : function(result){
			
		var total = 0;
		var garantia = 0;
		  
        $.each(result.rows, function(index, value) {
      	  
      	  garantia = removeMascaraPriceFormat(value.cell.vlrGarantia);
      	  total += intValue(garantia);
      	  
        });

        $("#valorTotalGarantiaslHidden", relatorioGarantiasController.workspace).val(total);
        
        $("#valorTotalGarantiaslHidden", relatorioGarantiasController.workspace).priceFormat({
  	      allowNegative: true,
  	      centsSeparator: ',',
  	      thousandsSeparator: '.'
        });  

        $("#totalGarantia").html( $("#valorTotalGarantiaslHidden", relatorioGarantiasController.workspace).val()); 

	},
	

	
	montaColunaDetalhesGarantia : function(data) {
		
		if (data.mensagens) {
			
			exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
			
			relatorioGarantiasController.hideGrids();
			
		} else { 
			
			var garantiaSelecionada = $('#selectStatusGarantia option:selected').val();
			
			$.each(data.rows, function(index, value) {
								
				var link_detalhes = '<a title="Ver Detalhes" onclick="relatorioGarantiasController.popup_detalhe_Garantia(\'' + value.cell.tpGarantia.key + '\', \'' + garantiaSelecionada + '\');" href="javascript:;"><img src="' + contextPath + '/images/ico_detalhes.png" alt="Detalhes" border="0" /></a>';
				value.cell.detalhe = link_detalhes;
			});
			relatorioGarantiasController.showGridTodasGarantias();
	
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
		
		$.postJSON(
                this.path + 'pesquisarGarantia.json?' + params,
                null,
                function(result) {
                       
                       if (result.mensagens) {
                              exibirMensagem(result.mensagens.tipoMensagem, result.mensagens.listaMensagens);
                              relatorioGarantiasController.hideGrids();
                              
                       }  else{
                              $(".garantiaDetalheGrid").flexAddData(result);
                              $("#dialog-detalhe-garantia th[abbr='faturamento'] >div").html("Faturamento " + result.rows[0].cell.baseCalculo);
                               
                              relatorioGarantiasController.inserirTotalDetalheGrid(result);
                              
                       }
                },
                null,
                true
         );
		
	
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

//@ sourceURL=relatorioGarantias.js
