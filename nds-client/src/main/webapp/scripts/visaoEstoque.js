var visaoEstoqueController = $.extend(true, {	
	
	path : contextPath + '/estoque/visaoEstoque/',

	init : function() {
		$("#dataMovimento").datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true
		});
		visaoEstoqueController.bindButtons();
		visaoEstoqueController.initGridVisaoEstoque();
	},
	
	
	bindButtons : function() {
		
		$("#btnPesquisar", this.workspace).click(function() {
			visaoEstoqueController.pesquisar();
			$(".grids").show();
		});
	},
	
	
	pesquisar : function() {
		
		var params = $("#pesquisarVisaoEstoqueForm", this.workspace).serialize();
		
		$(".visaoEstoqueGrid", this.workspace).flexOptions({
			url : this.path + 'pesquisar.json?' + params, 
			newp:1
		});
		$(".visaoEstoqueGrid").flexReload();
	},
	
	
	montaColunaAcao : function(data) {
		$.each(data.rows, function(index, value) {
			var acao = "teste";
			value.cell.acao = acao;	
		});
	},
	
	initGridVisaoEstoque : function () {
		$(".visaoEstoqueGrid").flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Estoque',
				name : 'estoque',
				width : 400,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produtos',
				name : 'produtos',
				width : 150,
				sortable : true,
				align : 'center'
			}, {
				display : 'Exemplares',
				name : 'exemplares',
				width : 150,
				sortable : true,
				align : 'center'
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'A&ccedil;&atilde;o',
				name : 'acao',
				width : 80,
				sortable : true,
				align : 'left'
			}],
			sortname : "estoque",
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