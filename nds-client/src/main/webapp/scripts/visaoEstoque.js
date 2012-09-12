var visaoEstoqueController = $.extend(true, {	

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
			//visaoEstoqueController.pesquisar();
			$(".grids").show();
			$("#fileExport").show();
		});
	},
	
	
	pesquisar : function() {
		
		var dataMovimento = $("#dataMovimento").val();
		var idFornecedor = $("#idFornecedor").val();
		
		$(".visaoEstoqueGrid", this.workspace).flexOptions({
			"url" : this.path + 'busca.json', 
			params: [{name:'filtro.dataMovimento', value: dataMovimento },
			         {name:'filtro.idFornecedor', value: idFornecedor }],			
			newp:1
		});
		$(".visaoEstoqueGrid").flexReload();
	},
	
	
	initGridVisaoEstoque : function () {
		$(".visaoEstoqueGrid").flexigrid({
			dataType : 'xml',
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
				name : 'total',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Ação',
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