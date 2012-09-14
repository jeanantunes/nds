var visaoEstoqueController = $.extend(true, {	
	
	path : contextPath + '/estoque/visaoEstoque/',

	
	init : function() {
		
		$("#dataMovimento").datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true
		});

		$("#btnPesquisar", this.workspace).click(function() {
			visaoEstoqueController.pesquisar();
			$(".grids").show();
		});
		
		visaoEstoqueController.initGridVisaoEstoque();
		visaoEstoqueController.initGridVisaoEstoqueLancto();
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
			var acao = '<a href="javascript:;" onclick="visaoEstoqueController.popup_lancamento(\'' + value.cell.estoque + '\');" titile="Ver Detalhes"><img src="' + contextPath + '/images/ico_detalhes.png" alt="Detalhes" border="0" /></a>    ';
			
			if (value.cell.estoque != "Lançamento Juramentado") {
				acao += '<a href="javascript:;" onClick="visaoEstoqueController.popup_transferencia(\'' + value.cell.estoque + '\');" title="Transferir Estoque"><img src="' + contextPath + '/images/ico_negociar.png" hspace="5" border="0" alt="Transferir" /></a>    ' +
						'<a href="javascript:;" onClick="visaoEstoqueController.popup_inventario(\'' + value.cell.estoque + '\');" title="Inventário Estoque"><img src="' + contextPath + '/images/bt_expedicao.png" hspace="5" border="0" alt="Inventário" /></a>';
			}
			
			value.cell.acao = acao;
		});
		return data;
	},
	
	
	popup_lancamento : function(estoque) {

		$( "#dialog-lancamento" ).dialog({
			resizable: false,
			height:380,
			width:850,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	},
	
	
	initGridVisaoEstoque : function () {
		$(".visaoEstoqueGrid").flexigrid({
			preProcess :  visaoEstoqueController.montaColunaAcao,
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
	
	
	initGridVisaoEstoqueLancto : function() {
		$(".visaoEstoqueLanctoGrid").flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 40,
				sortable : true,
				align : 'left'
			},{
				display : 'Nome',
				name : 'nome',
				width : 100,
				sortable : true,
				align : 'left'
			},{
				display : 'Código',
				name : 'codigo',
				width : 40,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço Capa R$',
				name : 'precoCapa',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Lcto',
				name : 'dtLancto',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Rclto',
				name : 'dtRecolto',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Qtde',
				name : 'qtde',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Valor R$',
				name : 'total',
				width : 80,
				sortable : true,
				align : 'right'
			}],
			width : 795,
			height : 200
		});
	},
	
		
}, BaseController);