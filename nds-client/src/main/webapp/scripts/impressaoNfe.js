var impressaoNfeController = $.extend(true, {
	init : function() {
		$( "#datepickerDe", impressaoNfeController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#datepickerAte", impressaoNfeController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#datepickerMovDe", impressaoNfeController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#datepickerMovAte", impressaoNfeController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});

		$(".impressaoGrid", impressaoNfeController.workspace).flexigrid({
			preProcess: null,
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
				width : 465,
				sortable : true,
				align : 'left'
			}, {
				display : 'Total Exemplares',
				name : 'totalExemplares',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Total R$',
				name : 'vlrTotal',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Total Desc. R$',
				name : 'totalDesc',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Impressão',
				name : 'impressao',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : '',
				name : ' ',
				width : 30,
				sortable : true,
				align : 'center'
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
		
		$(".cotasSuspensasGrid", impressaoNfeController.workspace).flexigrid({
			preProcess: null,
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 290,
				sortable : true,
				align : 'left'
			}, {
				display : 'Total Exemplares',
				name : 'totalExemplares',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : '',
				name : 'sel',
				width : 40,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 570,
			height : 180
		});
		
		$("#selFornecedor", impressaoNfeController.workspace).click(function() {
			$(".menu_fornecedor", impressaoNfeController.workspace).show().fadeIn("fast");
		})

		$(".menu_fornecedor", impressaoNfeController.workspace).mouseleave(function() {
			$(".menu_fornecedor", impressaoNfeController.workspace).hide();
		});

		$("#selProdutos", impressaoNfeController.workspace).click(function() {
			$(".menu_produtos", impressaoNfeController.workspace).show().fadeIn("fast");
		})

		$(".menu_produtos", impressaoNfeController.workspace).mouseleave(function() {
			$(".menu_produtos", impressaoNfeController.workspace).hide();
		});		
		
	},
	
	pesquisar : function(){
		
		$(".impressaoGrid", impressaoNfeController.workspace).flexOptions({
			url: contextPath + "/nfe/impressaoNFE/pesquisarImpressaoNFE",
			dataType : 'json',
			params: [
						{name:'filtro.idRoteiro', value:$('#idRoteiro', impressaoNfeController.workspace).val()}
						]
		});
		
		$(".impressaoGrid", impressaoNfeController.workspace).flexReload();

	},
	
	executarPreProcessamento : function(resultado) {
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".grids", impressaoNfeController.workspace).hide();

			return resultado;
		}
		
		$(".grids", impressaoNfeController.workspace).show();
		
		return resultado;
	},
	
	confirmar : function(){
		$(".dados", impressaoNfeController.workspace).show();
	},
	
	pesqEncalhe : function(){
		$(".dadosFiltro", impressaoNfeController.workspace).show();
	},
	
	mostrar_nfes : function() {
		$(".nfes", impressaoNfeController.workspace).show();
	}

}, BaseController);
