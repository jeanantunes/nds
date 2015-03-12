var retornoNFEController  = $.extend(true, {

	path : contextPath +"/nfe/retornoNFe/",
	
	init : function() {
		this.initFlexiGrids();
		this.initFiltroDatas();
	},

	initFlexiGrids : function() {
		
		$("#retornoNfe-flexigrid-pesquisa", retornoNFEController.workspace).flexigrid({
			colModel : [{
				display : 'Num. Total de Arquivos',
				name : 'numeroTotalArquivos',
				width : 150,
				sortable : false,
				align : 'center',
			}, {
				display : 'Num. NF-e',
				name : 'numeroNotasAprovadas',
				width : 150,
				sortable : false,
				align : 'center',
			}, {
				display : 'Erros Consis.',
				name : 'numeroNotasRejeitadas',
				width : 150,
				sortable : false,
				align : 'center',
			}],
			dataType : 'json',
			sortorder : "asc",
			usepager : false,
			useRp : false,
			rp : 15,
			showTableToggleBtn : false,
			width : 550,
			height : 100
			
		});
		
	},
	
	initFiltroDatas : function() {
		$.postJSON(contextPath + '/cadastro/distribuidor/obterDataDistribuidor', null, 
				function(result) {
					$("#retornoNFEDataReferencia", this.workspace).val(result);
		        }
		);
		
		$( "#retornoNFEDataReferencia", retornoNFEController.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$('#retornoNFEDataReferencia', retornoNFEController.workspace).mask("99/99/9999");
	},
	
	pesquisar : function() {
		
		var dataReferencia = $("#retornoNFEDataReferencia", this.workspace).val();
		var params = [];
		
		if(!dataReferencia) {
			exibirMensagem("WARNING", ["O campo [Date de Referência] é obrigatório"], "");
			return false;
		} else {

			params.push({name : "dataReferencia", value : dataReferencia});
			
			$("#retornoNfe-flexigrid-pesquisa", retornoNFEController.workspace).flexOptions({
				dataType : 'json',
				url: contextPath + "/nfe/retornoNFe/pesquisarArquivos.json",
				params: params
			});
			
			$("#retornoNfe-flexigrid-pesquisa", retornoNFEController.workspace).flexReload();
			$(".grids").show();
			
		}
	},
	

}, BaseController);
//@ sourceURL=retornoNFE.js