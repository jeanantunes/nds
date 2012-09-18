var visaoEstoqueController = $.extend(true, {
	
	path : contextPath + '/estoque/visaoEstoque/',


	init : function() {
		
		$("#visaoEstoque_filtro_dataMovimentacao").datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$("#btnPesquisar", this.workspace).click(function() {
			visaoEstoqueController.pesquisar();
			$(".grids").show();
		});
		
		visaoEstoqueController.initGridVisaoEstoque();
		visaoEstoqueController.initGridVisaoEstoqueDetalhe();
		visaoEstoqueController.initGridVisaoEstoqueDetalheJuramentado();
		visaoEstoqueController.initGridVisaoEstoqueTransferencia();
		visaoEstoqueController.initGridVisaoEstoqueInventario();
	},
	
	
	pesquisar : function() {
		
		var params = $("#pesquisarVisaoEstoqueForm", this.workspace).serialize();
		
		$(".visaoEstoqueGrid", this.workspace).flexOptions({
			url : this.path + 'pesquisar.json?' + params, 
			preProcess : visaoEstoqueController.montaColunaAcao,
			newp : 1
		});
		
		$(".visaoEstoqueGrid").flexReload();
	},
	
	
	montaColunaAcao : function(data) {
	
		$.each(data.rows, function(index, value) {
			
			var acao = '<a href="javascript:;" onclick="visaoEstoqueController.popup_detalhe(\'' + value.cell.estoque + '\');" titile="Ver Detalhes"><img src="' + contextPath + '/images/ico_detalhes.png" alt="Detalhes" border="0" /></a>    ';
			
			if (value.cell.estoque != "Lançamento Juramentado") {
				acao += '<a href="javascript:;" onClick="visaoEstoqueController.popup_transferencia(\'' + value.cell.estoque + '\');" title="Transferir Estoque"><img src="' + contextPath + '/images/ico_negociar.png" hspace="5" border="0" alt="Transferir" /></a>    ' +
						'<a href="javascript:;" onClick="visaoEstoqueController.popup_inventario(\'' + value.cell.estoque + '\');" title="Inventário Estoque"><img src="' + contextPath + '/images/bt_expedicao.png" hspace="5" border="0" alt="Inventário" /></a>';
			}
			
			value.cell.acao = acao;
		});
	
		return data;
	},
	
	
	montaSelectIncluirEstoque : function(estoque) {
		
		var select = $("#visaoEstoque_selectIncluirEstoque").get(0);
		
		while (select.options.length > 0) {
			select.remove(0);
		}
		
		if (estoque != "Lançamento") {
			select.add(new Option("Lançamento", "Lançamento"), null);
		}
		if (estoque != "Suplementar") {
			select.add(new Option("Suplementar", "Suplementar"), null);
		}
		if (estoque != "Recolhimento") {
			select.add(new Option("Recolhimento", "Recolhimento"), null);
		}
		if (estoque != "Produtos Danificados") {
			select.add(new Option("Produtos Danificados", "Produtos Danificados"), null);
		}
	},
	
	
	montaInputTransferencia : function(data) {
		
		$.each(data.rows, function(index, value) {
				
			value.cell.transferir = '<input type="text" style="width:80px; text-align:center;" name="transferir_' + value.cell.produtoEdicaoId + '"/>';
			value.cell.check = '<input type="checkbox" class="visaoEstoqueCheck" name="checkTransferir" value="' + value.cell.produtoEdicaoId + '" onclick="visaoEstoqueController.verificarCheck();" />';
		});
		
		return data;
	},
	
	
	verificarCheck : function() {
		
		var todosChecados = true;
		
		$(".visaoEstoqueCheck").each(function(index, element) {
			if (!element.checked) {
				todosChecados = false;
			}
		});
		
		$("#visaoEstoqueCheckSelecionarTodos").get(0).checked = todosChecados;
	},
	
	
	checkAll : function (check) {
		
		$(".visaoEstoqueCheck").each(function(index, element) {
			element.checked = check.checked;
		});
	},
	
	
	confirmarTransferencia : function() {
		
		
	},
	
	
	popup_detalhe : function(estoque) {
		
		var div = 'dialog-detalhe';
		var grid = 'visaoEstoqueDetalheGrid';
		
		if (estoque == 'Lançamento Juramentado') {
			div = 'dialog-detalhe-juramentado';
			grid = 'visaoEstoqueDetalheJuramentadoGrid';
		} else {
			$("#visaoEstoque_detalhe_estoque").html(estoque);
		}
		
		$("#visaoEstoque_filtro_tipoEstoque").val(estoque);
		
		var params = $("#pesquisarVisaoEstoqueForm", this.workspace).serialize();
		
		$("." + grid, this.workspace).flexOptions({
			url : this.path + 'pesquisarDetalhe.json?' + params, 
			newp:1
		});
		
		$("." + grid).flexReload();
		
		$("#" + div).dialog({
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
	
	
	popup_transferencia : function(estoque) {
		
		visaoEstoqueController.montaSelectIncluirEstoque(estoque);
		
		$("#visaoEstoque_transferencia_estoqueSelecionado").html(estoque);
		$("#visaoEstoque_transferencia_dataMovimentacao").html($("#visaoEstoque_filtro_dataMovimentacao").val());
		
		var params = $("#pesquisarVisaoEstoqueForm", this.workspace).serialize();
		
		$(".visaoEstoqueTransferenciaGrid", this.workspace).flexOptions({
			url : this.path + 'pesquisarTransferencia.json?' + params, 
			preProcess : visaoEstoqueController.montaInputTransferencia,
			newp:1
		});
		
		$(".visaoEstoqueTransferenciaGrid").flexReload();
		
		$("#dialog-transferencia").dialog({
			resizable: false,
			height:480,
			width:930,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$(this).dialog("close");
					$("#effect").show("highlight", {}, 1000, callback);
					visaoEstoqueController.confirmarTransferencia();
				},
				"Cancelar": function() {
					$(this).dialog("close");
				},
			},
		});
	},
	
	
	popup_inventario : function(estoque) {
		
		$("#visaoEstoque_inventario_estoqueSelecionado").html(estoque);
		$("#visaoEstoque_inventario_dataMovimentacao").html($("#visaoEstoque_filtro_dataMovimentacao").val());
		
		var params = $("#pesquisarVisaoEstoqueForm", this.workspace).serialize();
		
		$(".visaoEstoqueInventarioGrid", this.workspace).flexOptions({
			url : this.path + 'pesquisarInventario.json?' + params, 
			newp:1
		});
		
		$(".visaoEstoqueInventarioGrid").flexReload();
		
		$( "#dialog-inventario" ).dialog({
			resizable: false,
			height:480,
			width:930,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
					popConfirmaEstoque();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				},
			},
			
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
				name : 'valorFormatado',
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
	
	
	initGridVisaoEstoqueDetalhe : function() {
		$(".visaoEstoqueDetalheGrid").flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 160,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço Capa R$',
				name : 'precoCapa',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Lcto',
				name : 'dtLancto',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Rclto',
				name : 'dtRecolto',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Qtde',
				name : 'qtde',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Valor R$',
				name : 'valorFormatado',
				width : 80,
				sortable : true,
				align : 'right'
			}],
			width : 795,
			height : 200
		});
	},
	
	
	initGridVisaoEstoqueDetalheJuramentado : function() {
		$(".visaoEstoqueDetalheJuramentadoGrid").flexigrid({
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
	
	
	initGridVisaoEstoqueTransferencia : function() {
		$(".visaoEstoqueTransferenciaGrid").flexigrid({
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
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço Capa R$',
				name : 'precoCapa',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Dt. Lancto',
				name : 'lcto',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Dt. Recolto',
				name : 'rclto',
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
				display : 'Transferir (exes.)',
				name : 'transferir',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Saldo Estoque',
				name : 'estoque',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : '',
				name : 'check',
				width : 50,
				sortable : true,
				align : 'center'
			}],
			width : 870,
			height : 200
		});
	},
	
	
	initGridVisaoEstoqueInventario : function() {
		$(".visaoEstoqueInventarioGrid").flexigrid({
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
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço Capa R$',
				name : 'precoCapa',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Dt. Lancto',
				name : 'dtLancto',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Dt. Recolto',
				name : 'dtRecolto',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Qtde',
				name : 'qtde',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Estoque',
				name : 'estoque',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Diferença',
				name : 'Diferenca',
				width : 90,
				sortable : true,
				align : 'center'
			}],
			width : 870,
			height : 300
		});
	},
	
		
}, BaseController);