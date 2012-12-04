var relatorioTiposProdutosController = $.extend(true, {
	
	path : contextPath + '/lancamento/relatorioTiposProdutos/',


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
		
		$("#dateLanctoDe", this.workspace).mask("99/99/9999");
		$("#dateLanctoAte", this.workspace).mask("99/99/9999");
		$("#dateRecoltoDe", this.workspace).mask("99/99/9999");
		$("#dateRecoltoAte", this.workspace).mask("99/99/9999");
		
		this.initGrid();
	},
	
	
	pesquisar : function() {

		var params = $("#relatorioTiposProdutosForm", this.workspace).serialize();
		
		$(".tiposProdutosGrid", this.workspace).flexOptions({
			url : this.path + 'pesquisar.json?' + params, 
			newp : 1,
			preProcess: relatorioTiposProdutosController.processarRetornoPesquisa
		});
		
		$(".tiposProdutosGrid", this.workspace).flexReload();
	},
	
	processarRetornoPesquisa : function(result) {
		
		if (result.mensagens) {

			exibirMensagem(
					result.mensagens.tipoMensagem, 
					result.mensagens.listaMensagens
			);
			
			$(".grids", relatorioTiposProdutosController.workspace).hide();

			return result;
		}
		
		$.each(result.rows, function(index, row) {
	
			if(!row.cell.nomeCota){
				row.cell.nomeCota = "";
			}
			
			if(!row.cell.precoCapa){
				row.cell.precoCapa = "0,00";
			}
			
		});
		
		$(".grids", relatorioTiposProdutosController.workspace).show();

		return result;
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

//@ sourceURL=relatorioTiposProdutos.js
