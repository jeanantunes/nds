$(document).ready(function() {

	$('#pesquisarVisaoEstoqueForm').on('submit', function() {return false;});
	
	$(document.body).keydown(function(e) {	
		
		var eventoJs = e;
		var keycode = e.which;
		
		if (window.event) {
			eventoJs = window.event;
			keycode = eventoJs.keyCode;
		}

		if(keycode == 13) {
			e.preventDefault();
			visaoEstoqueController.pesquisar();
			$(".grids", this.workspace).show();
		}
		
	});
		
});

var visaoEstoqueController = $.extend(true, {
	
	path : contextPath + '/estoque/visaoEstoque/',

	visaoEstoqueTransferenciaArray : {}, 
	
	visaoEstoqueInventarioArray : {}, 
	
	tipoEstoque: null,
	
	init : function() {
		
		$("#visaoEstoque_filtro_dataMovimentacao").datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true
		});
		$(".input-date").mask("99/99/9999");
		
		$("#btnPesquisarVisaoEstoque", this.workspace).click(function() {
			visaoEstoqueController.pesquisar();
			$(".grids", this.workspace).show();
		});
		
		visaoEstoqueController.initGridVisaoEstoque();
		visaoEstoqueController.initGridVisaoEstoqueDetalhe();
		visaoEstoqueController.initGridVisaoEstoqueDetalheJuramentado();
		visaoEstoqueController.initGridVisaoEstoqueTransferencia();
		visaoEstoqueController.initGridVisaoEstoqueInventario();
		
		$(".areaBts", this.workspace).hide();
	},
	
	
	pesquisar : function() {

		var formData = [
		        {name:'filtro.tipoEstoque', value: $("#visaoEstoque_filtro_tipoEstoque", this.workspace).val()},
		        {name:'filtro.tipoEstoqueSelecionado', value: $("#visaoEstoque_filtro_tipoEstoqueSelecionado", this.workspace).val()},
		        {name:'filtro.dataMovimentacaoStr', value: $("#visaoEstoque_filtro_dataMovimentacao", this.workspace).val()},
		        {name:'filtro.idFornecedor', value: $("#visaoEstoque_filtro_idFornecedor", this.workspace).val()}
		];
		
		$(".visaoEstoqueGrid", this.workspace).flexOptions({
			
			url : visaoEstoqueController.path + 'pesquisar.json',
			params: formData,
			preProcess : visaoEstoqueController.montaColunaAcao,
			newp : 1,
			onSuccess : function(result) {
				if (result.listaMensagens) {
					
					exibirMensagem(
						result.tipoMensagem, 
						result.listaMensagens
					);
				}
			}
		});
		
		$(".visaoEstoqueGrid", this.workspace).flexReload();
		
		$(".areaBts", this.workspace).show();
	},
	
	
	imprimirConferenciaCega : function() {
		
		var params = visaoEstoqueController.parametrosInventario();
		var serializedForm = $("#pesquisarVisaoEstoqueForm", this.workspace).serialize();
		
		$.post(
			this.path + 'gerarDadosConferenciaCega?' + serializedForm,
			params,
			function() {
				
				//window.open(contextPath + "/devolucao/fechamentoEncalhe/imprimirBoletosCotasAusentes",
				window.open(visaoEstoqueController.path + 'exportarConferenciaCega?fileType=PDF');
			}
		);
	},
	
	
	montaColunaAcao : function(data) {
	
		$.each(data.rows, function(index, value) {
			
			var acao = '<a href="javascript:;"  onclick="visaoEstoqueController.popup_detalhe(\'' + value.cell.tipoEstoque + '\', \'' + value.cell.estoque + '\');" title="Ver Detalhes"><img src="' + contextPath + '/images/ico_detalhes.png" alt="Detalhes" border="0" /></a>    ';
			
			if (value.cell.tipoEstoque != "LANCAMENTO_JURAMENTADO") {
				acao += '<a href="javascript:;" isEdicao="true" onClick="visaoEstoqueController.popup_transferencia(\'' + value.cell.tipoEstoque + '\', \'' + value.cell.estoque + '\');" title="Transferir Estoque"><img src="' + contextPath + '/images/ico_negociar.png" hspace="5" border="0" alt="Transferir" /></a>    ' +
						'<a href="javascript:;" isEdicao="true" onClick="visaoEstoqueController.popup_inventario(\'' + value.cell.tipoEstoque + '\', \'' + value.cell.estoque + '\');" title="Inventário Estoque"><img src="' + contextPath + '/images/bt_expedicao.png" hspace="5" border="0" alt="Inventário" /></a>';
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
		
		data = data.listDetalhe;
		
		$.each(data.rows, function(index, value) {
			
			if (!value.cell.precoCapa) {
				value.cell.precoCapa = "";
			}
			
			transferir = -1;
			produtoEdicaoId = visaoEstoqueController.visaoEstoqueTransferenciaArray[value.cell.produtoEdicaoId];
			if(produtoEdicaoId) {
				transferir = produtoEdicaoId;
			}
			
			value.cell.estoque = value.cell.qtde;
			value.cell.transferir = '<input type="text" id="inputVisaoEstoqueTransferencia_' + value.cell.produtoEdicaoId + '" '+ (transferir != -1 ? 'value="'+ transferir +'"': '') +' style="width:80px; text-align:center;" onchange="visaoEstoqueController.ajustarSaldo(this,'+ value.cell.produtoEdicaoId +')" onkeyup="visaoEstoqueController.ajustarSaldo(this,'+ value.cell.produtoEdicaoId +')"/>';
			
		});
		
		return data;
	},
	
	
	montaInputInventario : function(data) {
		
		data = data.listDetalhe;
		
		$.each(data.rows, function(index, value) {
			
			if (!value.cell.precoCapa) {
				value.cell.precoCapa = "";
			}
			
			inventario = -1;
			produtoEdicaoId = visaoEstoqueController.visaoEstoqueInventarioArray[value.cell.produtoEdicaoId];
			if(produtoEdicaoId) {
				inventario = produtoEdicaoId;
			}
			
			value.cell.diferenca = '<div abbr="diferenca">' + value.cell.qtde + '</div>';
			value.cell.estoque = '<input type="text" class="inputVisaoEstoqueInventario" id="inputVisaoEstoqueInventario_' + value.cell.produtoEdicaoId + '" '+ (inventario != -1 ? 'value="'+ visaoEstoqueController.visaoEstoqueInventarioArray[value.cell.produtoEdicaoId]['inventario'] +'"': '') +' style="width:80px; text-align:center;" onchange="visaoEstoqueController.ajustarDiferenca(this,'+ value.cell.produtoEdicaoId +')"/>';
			
		});
		
		return data;
	},
	
	
	ajustarSaldo : function(element, produtoEdicaoId) {
		
		var tr = element.parentNode.parentNode.parentNode;
		var qtdeTransferir = parseInt($.trim(element.value) == "" ? 0 : element.value); 
		var qtde = parseInt($('td[abbr="qtde"] >div', tr).html()); 
		
		if(qtdeTransferir > 0 && qtdeTransferir <= qtde) {
			$('td[abbr="estoque"] >div', tr).html(qtde - qtdeTransferir);
			visaoEstoqueController.visaoEstoqueTransferenciaArray[produtoEdicaoId] = qtdeTransferir;
		} else {
			$('td[abbr="estoque"] >div', tr).html(qtde);
			$('td[abbr="transferir"] >div>input', tr).attr('value', '');
			delete visaoEstoqueController.visaoEstoqueTransferenciaArray[produtoEdicaoId];
		}
		
	},
	
	
	ajustarDiferenca : function(element, produtoEdicaoId) {
		
		var tr = element.parentNode.parentNode.parentNode;
		var qtdeInventario = parseInt($.trim(element.value) == "" ? -1 : element.value); 
		var qtde = parseInt($('td[abbr="qtde"] >div', tr).html()); 
		
		if(!isNaN(qtdeInventario) && qtdeInventario > -1) {
			$('div[abbr="diferenca"]', tr).html(qtdeInventario - qtde);
			visaoEstoqueController.visaoEstoqueInventarioArray[produtoEdicaoId] = {};
			visaoEstoqueController.visaoEstoqueInventarioArray[produtoEdicaoId]['inventario'] = (qtdeInventario);
			visaoEstoqueController.visaoEstoqueInventarioArray[produtoEdicaoId]['diferenca']  = (qtdeInventario - qtde);
		} else {
			$('div[abbr="diferenca"]', tr).attr('value', (!isNaN(qtdeInventario)) ? (qtdeInventario - qtde) : qtde);
			$('div[abbr="diferenca"]', tr).html(((!isNaN(qtdeInventario) && qtdeInventario > 0) ? (qtdeInventario - qtde) : qtde));
			element.value = '';
			delete visaoEstoqueController.visaoEstoqueInventarioArray[produtoEdicaoId];
		}
		
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
			function(result) {

				visaoEstoqueController.visaoEstoqueTransferenciaArray = {};
				
				if (result.listaMensagens) {
				
					exibirMensagem(
						result.tipoMensagem, 
						result.listaMensagens
					);
				}
				
				$(".visaoEstoqueGrid", this.workspace).flexReload();
				
				//$('#dialog-visaoEstoque-transferencia').dialog('close');
			}
		);
	},
	
	
	parametrosTransferencia : function() {
		
		var dados = "";
		var index = 0;
		
		$.each( visaoEstoqueController.visaoEstoqueTransferenciaArray, function(produtoEdicaoId, qtde) {
			
			dados += '{name:"filtro.listaTransferencia['+index+'].produtoEdicaoId", value: '+ produtoEdicaoId +'}, {name:"filtro.listaTransferencia['+index+'].qtde", value: '+ ((qtde != "") ? qtde : 0) +'},';
			index++;
			
		});

		var params = '['+dados+']';
		return eval(params);
	},
	
	
	confirmaInventario : function() {
		
		var params = visaoEstoqueController.parametrosInventario();
		
		$.postJSON(
			this.path + 'inventario?' + $('#pesquisarVisaoEstoqueForm').serialize(),
			params,
			function() {
				visaoEstoqueController.visaoEstoqueInventarioArray = {};
				$('#dialog-visaoEstoque-inventario').dialog('close');
				visaoEstoqueController.pesquisar();
			}
		);
		
		$('#dialog-visaoEstoque-inventario-confirm').dialog('close');
		
	},
	
	
	parametrosInventario : function() {
		
		var dados ="";
		var index = 0;
		
		$.each( visaoEstoqueController.visaoEstoqueInventarioArray, function(produtoEdicaoId, qtde) {
			
			dados += '{name:"filtro.listaTransferencia['+index+'].produtoEdicaoId", value: '+ produtoEdicaoId +'}, {name:"filtro.listaTransferencia['+index+'].qtde", value: '+ (qtde['diferenca']) +'},';
			index++;
			
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
		
		visaoEstoqueController.tipoEstoque = tipoEstoque;
		
		var params = $("#pesquisarVisaoEstoqueForm", this.workspace).serialize();
		
		$("." + grid).flexOptions({
			url : this.path + 'pesquisarDetalhe.json?' + params,
			preProcess : visaoEstoqueController.preProcessPopupDetalhe,
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
	
	preProcessPopupDetalhe : function(data) {
	
		if (visaoEstoqueController.tipoEstoque != 'LANCAMENTO_JURAMENTADO') {

			visaoEstoqueController.toggleFlexiGridColumn("visaoEstoqueDetalheGrid", "valor", (!data.isBuscaHistorico));
		}
		
		data = data.listDetalhe;
		
		$.each(data.rows, function(index, value) {
			
			if (!value.cell.valor) {
				value.cell.valor = "";
			}
			
			if (!value.cell.precoCapa) {
				value.cell.precoCapa = "";
			}
		});
		
		return data;
	},
	
	toggleFlexiGridColumn: function(gridClassName, column, visible) {
	
		var grid = $("." + gridClassName).closest(".flexigrid");
		
		var colHeader = $('th[abbr="' + column + '"]', grid);
		var colIndex = $(colHeader).attr('axis').replace(/col/, "");
		
		$(colHeader).toggle(visible);
		
		$('tbody tr', grid).each(function () {
			$('td:eq(' + colIndex + ')', this).toggle(visible);
		});
	},
	
	popup_transferencia : function(tipoEstoque, estoque) {
		
		visaoEstoqueController.montaSelectIncluirEstoque(tipoEstoque);
		
		$("#visaoEstoque_transferencia_estoqueSelecionado", this.workspace).html(estoque);
		$("#visaoEstoque_transferencia_dataMovimentacao", this.workspace).html($("#visaoEstoque_filtro_dataMovimentacao").val());
		
		$("#visaoEstoque_filtro_tipoEstoque", this.workspace).val(tipoEstoque);
		
		$("#visaoEstoqueCheckSelecionarTodos", this.workspace).uncheck();
		
		var params = $("#pesquisarVisaoEstoqueForm", this.workspace).serialize();
		
		params +="&filtro.paginar=false";
		
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
					
					$("#effect").show("highlight", {}, 1000, callback);
					
					visaoEstoqueController.confirmarTransferencia();
					
					$(this).dialog("close");
					
				},
				"Cancelar": function() {
					visaoEstoqueController.visaoEstoqueTransferenciaArray = {};
					$(this).dialog("close");
				},
			},
			form: $("#dialog-visaoEstoque-transferencia", visaoEstoqueController.workspace).parents("form"),
			close: function(ev, ui) { visaoEstoqueController.visaoEstoqueTransferenciaArray = {}; },
		});
	},
	
	
	popup_inventario : function(tipoEstoque, estoque) {
		
		$("#visaoEstoque_inventario_estoqueSelecionado", this.workspace).html(estoque);
		$("#visaoEstoque_inventario_dataMovimentacao", this.workspace).html($("#visaoEstoque_filtro_dataMovimentacao").val());
		
		$("#visaoEstoque_filtro_tipoEstoque", this.workspace).val(tipoEstoque);
		
		var params = $("#pesquisarVisaoEstoqueForm", this.workspace).serialize();
		
		params +="&filtro.paginar=true";
		
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
					visaoEstoqueController.visaoEstoqueInventarioArray = {};
					$( this ).dialog( "close" );
				},
			},
			form: $("#dialog-visaoEstoque-inventario", visaoEstoqueController.workspace).parents("form"),
			close: function(ev, ui) { visaoEstoqueController.visaoEstoqueInventarioArray = {}; },
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
					visaoEstoqueController.visaoEstoqueInventarioArray = {};
				},
			},
			form: $("#dialog-visaoEstoque-inventario-confirm", visaoEstoqueController.workspace).parents("form"),
			close: function(ev, ui) { visaoEstoqueController.visaoEstoqueInventarioArray = {};},
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
			height : 200,
			
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
			sortname : "codigo",
			sortorder : "asc",
			width : 795,
			height : 200,
			usepager : true,
			useRp : true,
			rp : 15
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
			sortname : "codigo",
			sortorder : "asc",
			width : 795,
			height : 200,
			usepager : true,
			useRp : true,
			rp : 15
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
			}],
			width : 870,
			height : 200,
			usepager : true,
			useRp : true,
			rpOptions: [50, 100, 200],
			rp: 50,
			sortname : "codigo",
			sortorder : "asc"
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
				sortable : false,
				align : 'center'
			}, {
				display : 'Diferença',
				name : 'diferenca',
				width : 90,
				sortable : false,
				align : 'center'
			}],
			width : 870,
			height : 300,
			usepager : true,
			useRp : true,
			rpOptions: [50, 100, 200],
			rp: 50,
			sortname : "codigo",
			sortorder : "asc"
		});
	},
	
		
}, BaseController);

//@ sourceURL=visaoEstoque.js