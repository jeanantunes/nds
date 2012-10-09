var visaoEstoqueController = $.extend(true, {
	
	path : contextPath + '/estoque/visaoEstoque/',


	init : function() {
		
		$("#visaoEstoque_filtro_dataMovimentacao").datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$("#btnPesquisarVisaoEstoque", this.workspace).click(function() {
			visaoEstoqueController.pesquisar();
			$(".grids", this.workspace).show();
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
		
		$(".visaoEstoqueGrid", this.workspace).flexReload();
	},
	
	
	imprimirConferenciaCega : function() {
		
		var params = visaoEstoqueController.parametrosInventario();
		var serializedForm = $("#pesquisarVisaoEstoqueForm", this.workspace).serialize();
		
		$.post(
			this.path + 'gerarDadosConferenciaCega?' + serializedForm,
			params,
			function() {
				window.location = visaoEstoqueController.path + 'exportarConferenciaCega?fileType=PDF';
			}
		);
	},
	
	
	montaColunaAcao : function(data) {
	
		$.each(data.rows, function(index, value) {
			
			var acao = '<a href="javascript:;" onclick="visaoEstoqueController.popup_detalhe(\'' + value.cell.tipoEstoque + '\', \'' + value.cell.estoque + '\');" title="Ver Detalhes"><img src="' + contextPath + '/images/ico_detalhes.png" alt="Detalhes" border="0" /></a>    ';
			
			if (value.cell.tipoEstoque != "LANCAMENTO_JURAMENTADO") {
				acao += '<a href="javascript:;" onClick="visaoEstoqueController.popup_transferencia(\'' + value.cell.tipoEstoque + '\', \'' + value.cell.estoque + '\');" title="Transferir Estoque"><img src="' + contextPath + '/images/ico_negociar.png" hspace="5" border="0" alt="Transferir" /></a>    ' +
						'<a href="javascript:;" onClick="visaoEstoqueController.popup_inventario(\'' + value.cell.tipoEstoque + '\', \'' + value.cell.estoque + '\');" title="Inventário Estoque"><img src="' + contextPath + '/images/bt_expedicao.png" hspace="5" border="0" alt="Inventário" /></a>';
			}
			
			value.cell.acao = acao;
		});
	
		return data;
	},
	
	
	montaSelectIncluirEstoque : function(tipoEstoque) {
		
		var select = $("#visaoEstoque_selectIncluirEstoque").get(0);
		
		while (select.options.length > 0) {
			select.remove(0);
		}
		
		if (tipoEstoque != "LANCAMENTO") {
			select.add(new Option("Lançamento", "LANCAMENTO"), null);
		}
		if (tipoEstoque != "SUPLEMENTAR") {
			select.add(new Option("Suplementar","SUPLEMENTAR"), null);
		}
		if (tipoEstoque != "RECOLHIMENTO") {
			select.add(new Option("Recolhimento", "RECOLHIMENTO"), null);
		}
		if (tipoEstoque != "PRODUTOS_DANIFICADOS") {
			select.add(new Option("Produtos Danificados", "PRODUTOS_DANIFICADOS"), null);
		}
	},
	
	
	montaInputTransferencia : function(data) {
		
		$.each(data.rows, function(index, value) {
				
			value.cell.estoque = value.cell.qtde;
			value.cell.transferir = '<input type="text" id="inputVisaoEstoqueTransferencia_' + value.cell.produtoEdicaoId + '" style="width:80px; text-align:center;" onchange="visaoEstoqueController.ajustarSaldo(this)"/>';
			value.cell.check = '<input type="checkbox" class="visaoEstoqueCheck" name="checkVisaoEstoqueTransferir" value="' + value.cell.produtoEdicaoId + '" onclick="visaoEstoqueController.verificarCheck();" />';
		});
		
		return data;
	},
	
	
	montaInputInventario : function(data) {
		
		$.each(data.rows, function(index, value) {
				
			value.cell.diferenca = value.cell.qtde;
			value.cell.estoque = '<input type="text" class="inputVisaoEstoqueInventario" id="inputVisaoEstoqueInventario_' + value.cell.produtoEdicaoId + '" style="width:80px; text-align:center;" onchange="visaoEstoqueController.ajustarDiferenca(this)"/>';
		});
		
		return data;
	},
	
	
	ajustarSaldo : function(element) {
		
		var tr = element.parentNode.parentNode.parentNode;
		var qtdeTransferir = parseInt($.trim(element.value) == "" ? 0 : element.value); 
		var qtde = parseInt($('td[abbr="qtde"] >div', tr).html()); 
		
		$('td[abbr="estoque"] >div', tr).html(qtde - qtdeTransferir);
	},
	
	
	ajustarDiferenca : function(element) {
		
		var tr = element.parentNode.parentNode.parentNode;
		var qtdeInventario = parseInt($.trim(element.value) == "" ? 0 : element.value); 
		var qtde = parseInt($('td[abbr="qtde"] >div', tr).html()); 
		
		$('td[abbr="diferenca"] >div', tr).html(qtde - qtdeInventario);
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
		
		$('#visaoEstoque_filtro_tipoEstoqueSelecionado').val($('#visaoEstoque_selectIncluirEstoque').val());
		
		var params = visaoEstoqueController.parametrosTransferencia();
		
		$.postJSON(
			this.path + 'transferir?' + $('#pesquisarVisaoEstoqueForm').serialize(),
			params,
			function() {
				$('#dialog-visaoEstoque-transferencia').dialog('close');
				visaoEstoqueController.pesquisar();
			}
		);
	},
	
	
	parametrosTransferencia : function() {
		
		var dados ="";
		var index = 0;
		$("input[type=checkbox][name='checkVisaoEstoqueTransferir']:checked").each(function(i, element) {
			if (dados != ""){
				dados+=",";
			}
				
			var produtoEdicaoId = element.value;
			var qtde = $('#inputVisaoEstoqueTransferencia_' + element.value).val();
			
			dados+='{name:"filtro.listaTransferencia['+index+'].produtoEdicaoId",value:'+produtoEdicaoId+'}, {name:"filtro.listaTransferencia['+index+'].qtde",value:'+qtde+'}';
			index++;
		});

		var params = '['+dados+']';
		return eval(params);
	},
	
	
	confirmaInventario : function() {
		
		$('#dialog-visaoEstoque-inventario-confirm').dialog('close');
		
		var params = visaoEstoqueController.parametrosInventario();
		
		$.postJSON(
			this.path + 'inventario?' + $('#pesquisarVisaoEstoqueForm').serialize(),
			params,
			function() {
				$('#dialog-visaoEstoque-inventario').dialog('close');
				visaoEstoqueController.pesquisar();
			}
		);
	},
	
	
	parametrosInventario : function() {
		
		var dados ="";
		var index = 0;
		$(".inputVisaoEstoqueInventario").each(function(i, element) {
			
			if (element.value != "") {
			
				if (dados != ""){
					dados+=",";
				}
					
				var produtoEdicaoId = element.id.substring(element.id.lastIndexOf("_")+1);

				var tr = element.parentNode.parentNode.parentNode;
				var qtde = $('td[abbr="estoque"] >div', tr).html();
				
				dados+='{name:"filtro.listaTransferencia['+index+'].produtoEdicaoId",value:'+produtoEdicaoId+'}, {name:"filtro.listaTransferencia['+index+'].qtde",value:'+qtde+'}';
				index++;
			}
		});

		var params = '['+dados+']';
		return eval(params);
	},
	
	
	popup_detalhe : function(tipoEstoque, estoque) {
		
		var div = 'dialog-visaoEstoque-detalhe';
		var grid = 'visaoEstoqueDetalheGrid';
		
		if (tipoEstoque == 'LANCAMENTO_JURAMENTADO') {
			div = 'dialog-visaoEstoque-detalhe-juramentado';
			grid = 'visaoEstoqueDetalheJuramentadoGrid';
		} else {
			$("#visaoEstoque_detalhe_estoque", this.workspace).html(estoque);
		}
		
		$("#visaoEstoque_filtro_tipoEstoque", this.workspace).val(tipoEstoque);
		
		var params = $("#pesquisarVisaoEstoqueForm", this.workspace).serialize();
		
		$("." + grid).flexOptions({
			url : this.path + 'pesquisarDetalhe.json?' + params, 
			newp:1
		});
		
		$("." + grid).flexReload();
		
		var idDiv = "#" + div;
		
		$(idDiv).dialog({
			resizable: false,
			height:380,
			width:850,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $(idDiv, visaoEstoqueController.workspace).parents("form")
		});
	},
	
	
	popup_transferencia : function(tipoEstoque, estoque) {
		
		visaoEstoqueController.montaSelectIncluirEstoque(tipoEstoque);
		
		$("#visaoEstoque_transferencia_estoqueSelecionado", this.workspace).html(estoque);
		$("#visaoEstoque_transferencia_dataMovimentacao", this.workspace).html($("#visaoEstoque_filtro_dataMovimentacao").val());
		
		$("#visaoEstoque_filtro_tipoEstoque", this.workspace).val(tipoEstoque);
		
		var params = $("#pesquisarVisaoEstoqueForm", this.workspace).serialize();
		
		$(".visaoEstoqueTransferenciaGrid", this.workspace).flexOptions({
			url : this.path + 'pesquisarDetalhe.json?' + params, 
			preProcess : visaoEstoqueController.montaInputTransferencia,
			newp:1
		});
		
		$(".visaoEstoqueTransferenciaGrid", this.workspace).flexReload();
		
		$("#dialog-visaoEstoque-transferencia", this.workspace).dialog({
			resizable: false,
			height:480,
			width:930,
			modal: true,
			buttons: {
				"Confirmar": function() {
					//$(this).dialog("close");
					$("#effect").show("highlight", {}, 1000, callback);
					visaoEstoqueController.confirmarTransferencia();
				},
				"Cancelar": function() {
					$(this).dialog("close");
				},
			},
			form: $("#dialog-visaoEstoque-transferencia", visaoEstoqueController.workspace).parents("form")
		});
	},
	
	
	popup_inventario : function(tipoEstoque, estoque) {
		
		$("#visaoEstoque_inventario_estoqueSelecionado", this.workspace).html(estoque);
		$("#visaoEstoque_inventario_dataMovimentacao", this.workspace).html($("#visaoEstoque_filtro_dataMovimentacao").val());
		
		$("#visaoEstoque_filtro_tipoEstoque", this.workspace).val(tipoEstoque);
		
		var params = $("#pesquisarVisaoEstoqueForm", this.workspace).serialize();
		
		$(".visaoEstoqueInventarioGrid", this.workspace).flexOptions({
			url : this.path + 'pesquisarDetalhe.json?' + params,
			preProcess : visaoEstoqueController.montaInputInventario,
			newp:1
		});
		
		$(".visaoEstoqueInventarioGrid", this.workspace).flexReload();
		
		$( "#dialog-visaoEstoque-inventario", this.workspace).dialog({
			resizable: false,
			height:480,
			width:930,
			modal: true,
			buttons: {
				"Confirmar": function() {
					//$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
					visaoEstoqueController.popupConfirmaInventario();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				},
			},
			form: $("#dialog-visaoEstoque-inventario", visaoEstoqueController.workspace).parents("form")
		});
	},
	
	
	popupConfirmaInventario : function() {
		
		$("#dialog-visaoEstoque-inventario-confirm", this.workspace).dialog({
			resizable: false,
			height:'auto',
			width:340,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$("#effect").show("highlight", {}, 1000, callback);
					visaoEstoqueController.confirmaInventario();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				},
			},
			form: $("#dialog-visaoEstoque-inventario-confirm", visaoEstoqueController.workspace).parents("form")
		});
	},
	
	
	initGridVisaoEstoque : function () {
		$(".visaoEstoqueGrid").flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Estoque',
				name : 'estoque',
				width : 400,
				align : 'left'
			}, {
				display : 'Produtos',
				name : 'produtos',
				width : 150,
				align : 'center'
			}, {
				display : 'Exemplares',
				name : 'exemplares',
				width : 150,
				align : 'center'
			}, {
				display : 'Valor R$',
				name : 'valorFormatado',
				width : 100,
				align : 'right'
			}, {
				display : 'A&ccedil;&atilde;o',
				name : 'acao',
				width : 80,
				align : 'left'
			}],
			width : 960,
			height : 200
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
				name : 'lcto',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Rclto',
				name : 'rclto',
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
				name : 'valor',
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
				name : 'lcto',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Rclto',
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
				display : 'Valor R$',
				name : 'valor',
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
				name : 'lcto',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Dt. Recolto',
				name : 'rclto',
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
				name : 'diferenca',
				width : 90,
				sortable : true,
				align : 'center'
			}],
			width : 870,
			height : 300
		});
	},
	
		
}, BaseController);