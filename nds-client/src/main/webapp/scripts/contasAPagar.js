var contasAPagarController = $.extend(true, {
	
	path : contextPath + '/financeiro/contasAPagar/',


	init : function() {

		$( "#contasAPagar_Filtro_De" ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$( "#contasAPagar_Filtro_Ate" ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true
		});
		
		this.initcontasAPagarListaProdutosGrid();		
		this.initGridPesquisarPorProduto();
		this.initGridPesquisarPorFornecedor();
		this.initGridParciais();
		this.initGridConsignado();
	},
	
	
	verificarCheck : function() {
		
		var todosChecados = true;
		
		$(".contasApagarCheck").each(function(index, element) {
			if (!element.checked) {
				todosChecados = false;
			}
		});
		
		$("#contasAPagarCheckSelecionarTodos").get(0).checked = todosChecados;
	},
	
	
	checkAll : function (check) {
		
		$(".contasApagarCheck").each(function(index, element) {
			element.checked = check.checked;
		});
	},
	
	/*
	 * *********************
	 * Carregamento FlexGrid
	 * *********************
	 * */
	
	pesquisar : function(){
	
		var params = $("#contasAPagarForm", this.workspace).serialize();
		
		if ($("#contasAPagarRadioDistribuidor").get(0).checked) {
			
			$.postJSON(
				contasAPagarController.path + 'pesquisarPorFornecedor.json?' + params,
				null,
				function(result) {
					
					$("#contasAPagar_gridFornecedorTotalBruto").html(result.totalBruto);
					$("#contasAPagar_gridFornecedorTotalDesconto").html(result.totalDesconto);
					$("#contasAPagar_gridFornecedorSaldo").html(result.saldo);
					
					$(".porDistrFornecedorGrid", contasAPagarController.workspace).flexAddData({rows: toFlexiGridObject(result.grid), page : 1, total : result.totalGrid});
				},
				null,
				true
			);

			$('.gridDistrib').show();
			
		}
		else if ($("#contasAPagarRadioProduto").get(0).checked) {
			
			$.postJSON(
				contasAPagarController.path + 'pesquisarPorProduto.json?' + params + '&' + contasAPagarController.obterSelecaoColunaCheckProdutoEdicao(),
				null,
				function(result) {
					
					$("#contasAPagar_gridProdutoTotalPagto").html(result.totalPagto);
					$("#contasAPagar_gridProdutoTotalDesconto").html(result.totalDesconto);
					$("#contasAPagar_gridProdutoValorLiquido").html(result.valorLiquido);
					
					$(".porProdutosGrid", contasAPagarController.workspace).flexAddData({rows: toFlexiGridObject(result.grid), page : 1, total : result.totalGrid});
				},
				null,
				true
			);
			
			$('.gridProduto').show();
		}
	},
	
	
	pesquisarProdutoEdicao : function(){
		
		var params = $("#contasAPagarPesquisaProdutoEdicaoForm").serialize();
		
		$(".contasAPagarListaProdutosGrid").flexOptions({
			url : this.path + 'pesquisarProduto.json?' + params, 
			preProcess : contasAPagarController.montaColunaCheckProdutoEdicao,
			newp : 1
		});

		$(".contasAPagarListaProdutosGrid").flexReload();
	},
	
	
	/*
	 * *************************
	 * Parametros de envio Pesquisa por produto
	 * *************************
	 * */
	obterSelecaoColunaCheckProdutoEdicao : function () {
		
		var dados ="";

		$("input[type=checkbox][name='checkProdutoContasAPagar']:checked").each(function(i,element) {
			if(dados!="") {
				dados+="&";
			}
			var produtoEdicaoId = element.value;
			dados+='filtro.produtoEdicaoIDs='+produtoEdicaoId+'"';
		});
		
		return dados;
	},
	
	
	
	/*
	 * *************************
	 * Pre Carregamento FlexGrid
	 * *************************
	 * */
	
	
	montaColunaCheckProdutoEdicao : function(data) {
		
		$.each(data.rows, function(index, value) {
			
			var checkbox = '<input type="checkbox" value="'+ value.cell.produtoEdicaoID + '" name="checkProdutoContasAPagar" class="contasApagarCheck"  style="float:left;"/>';
			value.cell.sel = checkbox;
		});
		 
		return data;
	},
	

	insereLinksContasAPagarPorDistribuidores : function(data) {
		
		$.each(data.rows, function(index, value) {
			
			var linkConsignado = '<a href="javascript:;" onclick="contasAPagarController.popup_consignado();" title="Detalhe Consignado">'+value.cell.consignado+'</a>';
			
			var linkEncalhe = '<a href="javascript:;" onclick="contasAPagarController.popup_encalhe();" title="Detalhe Consignado">'+value.cell.encalhe+'</a>';
			
			var linkFS = '<a href="javascript:;" onclick="contasAPagarController.popup_faltasSobras();" title="Detalhe Consignado">'+value.cell.faltasSobras+'</a>';
						
			value.cell.consignado = linkConsignado;
			value.cell.encalhe = linkEncalhe;
			value.cell.faltasSobras = linkFS;
		});
	
		return data;
	},
	
	
	insereLinksContasAPagarPorProdutos : function(data) {	
		
		$.each(data.rows, function(index, value) {
			
			var params = "'" + value.cell.produtoEdicaoId + "', '" 
							 + value.cell.codigo          + "', '"
							 + value.cell.produto         + "', '"
							 + value.cell.edicao          + "', '"
			                 + value.cell.fornecedor      + "', '" 
			                 + value.cell.dataLcto        + "', '" 
			                 + value.cell.dataFinal       + "'";
			
			var linkTipo = '<a href="javascript:;" onclick="contasAPagarController.popup_detalhes(' + params + ');" title="Detalhe Consignado">' + value.cell.tipo + '</a>';
			value.cell.tipo = linkTipo;
		});
	
		return data;
	},
	
	
	/*
	 * *************************
	 * Popups
	 * *************************
	 * */
	
	popup_pesq_produto : function () {
		
		$( "#dialog-pesq-produto-contasAPagar" ).dialog({
			resizable: false,
			height:'auto',
			width:600,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	},
	
	
	popup_detalhes : function (produtoEdicaoId, codigo, produto, edicao, fornecedor, dataLcto, dataFinal) {
		
		// popula cabecalho
		$("#contasAPagar_popupTipo_codigo").html(codigo);
		$("#contasAPagar_popupTipo_produto").html(produto);
		$("#contasAPagar_popupTipo_edicao").html(edicao);
		$("#contasAPagar_popupTipo_fornecedor").html(fornecedor);
		$("#contasAPagar_popupTipo_dataLcto").html(dataLcto);
		$("#contasAPagar_popupTipo_dataFinal").html(dataFinal);
	
		// preenche grid
		var params = $("#contasAPagarPesquisaProdutoEdicaoForm").serialize();
		
		$(".contasAPagar_parciaispopGrid").flexOptions({
			url : this.path + 'pesquisarParcial.json?' + params, 
			newp : 1
		});

		$(".contasAPagar_parciaispopGrid").flexReload();
		
		// abre popup
		$("#dialog-contasAPagar-tipo").dialog({
			resizable: false,
			height:500,
			width:950,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				
				},
			 }
		});
	},
	
	
	popup_consignado : function() {
		
		var params = $("#contasAPagarForm").serialize();
		
		$.postJSON(
			contasAPagarController.path + 'pesquisarConsignado.json?' + params,
			null,
			function(result) {
				
				// TODO: tabela totais
				
				$(".contasAPagar-consignadoGrid").flexAddData({rows: toFlexiGridObject(result.grid), page : 1, total : result.totalGrid});
			},
			null,
			true
		);
	
		// abre popup
		$("#dialog-contasAPagar-consignado").dialog({
			resizable: false,
			height:480,
			width:940,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					$(".grids").show();
				},
			}
		});
	},
	
	
	
	
/*
	
		
	function popup_encalhe() {
			//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		
			$( "#dialog-encalhe" ).dialog({
				resizable: false,
				height:460,
				width:860,
				modal: true,
				buttons: {
					"Fechar": function() {
						$( this ).dialog( "close" );
						
						$(".grids").show();
						
					}
				}
			});
		};

	function popup_faltasSobras() {
			//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		
			$( "#dialog-encalhe_2" ).dialog({
				resizable: false,
				height:460,
				width:860,
				modal: true,
				buttons: {
					"Fechar": function() {
						$( this ).dialog( "close" );
						
						$(".grids").show();
						
					}
				}
			});
		};

*/	
	/*
	 * *************************
	 * Configurações do Grid
	 * *************************
	 * */
	initcontasAPagarListaProdutosGrid : function(){
		$(".contasAPagarListaProdutosGrid").flexigrid({
			dataType : 'json',
			colModel : [ {
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
				width : 40,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço Capa R$',
				name : 'precoCapa',
				width : 80,
				sortable : true,
				align : 'right',
			}, {
				display : 'Fornecedor',
				name : 'fornecedor',
				width : 100,
				sortable : true,
				align : 'left',
			}, {
				display : 'Editor',
				name : 'editor',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : '',
				name : 'sel',
				width : 20,
				sortable : false,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			width : 550,
			height : 200
		});
	},
	
	
	initGridPesquisarPorFornecedor : function(){
	
	$(".porDistrFornecedorGrid").flexigrid({
		dataType : 'json',
		colModel : [ {
			display : 'Data',
			name : 'data',
			width : 100,
			sortable : true,
			align : 'left'
		}, {
			display : 'Consignado R$',
			name : 'consignado',
			width : 110,
			sortable : true,
			align : 'right'
		}, {
			display : 'Suplementação R$',
			name : 'suplementacao',
			width : 100,
			sortable : true,
			align : 'right'
		}, {
			display : 'Encalhe R$',
			name : 'encalhe',
			width : 110,
			sortable : true,
			align : 'right',
		}, {
			display : 'Venda R$',
			name : 'venda',
			width : 100,
			sortable : true,
			align : 'right',
		}, {
			display : 'Faltas Sobras R$',
			name : 'faltasSobras',
			width : 110,
			sortable : true,
			align : 'right'
		}, {
			display : 'Deb/Cred R$',
			name : 'debCredito',
			width : 110,
			sortable : true,
			align : 'right'
		}, {
			display : 'Saldo a Pagar R$',
			name : 'saldoPagar',
			width : 100,
			sortable : true,
			align : 'right'
		}],
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		sortname : "data",
		sortorder : "asc",
		width : 960,
		height : 255
	});
	
	},
	
	
	initGridPesquisarPorProduto : function(){
		
		$(".porProdutosGrid").flexigrid({
			dataType : 'json',
			preProcess : contasAPagarController.insereLinksContasAPagarPorProdutos,
			colModel : [ {
				display : 'Rclt',
				name : 'rctl',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Código',
				name : 'codigo',
				width : 45,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 130,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 50,
				sortable : true,
				align : 'left',
			}, {
				display : 'Tipo',
				name : 'tipo',
				width : 40,
				sortable : true,
				align : 'center',
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 60,
				sortable : true,
				align : 'center',
			}, {
				display : 'Suplementação',
				name : 'suplementacao',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Encalhe',
				name : 'encalhe',
				width : 60,
				sortable : true,
				align : 'center',
			}, {
				display : 'Venda',
				name : 'venda',
				width : 40,
				sortable : true,
				align : 'center',
			}, {
				display : 'Faltas/Sobras',
				name : 'faltasSobras',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Deb/Cred.',
				name : 'debitosCreditos',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Saldo a Pagar R$',
				name : 'saldoAPagar',
				width : 100,
				sortable : true,
				align : 'right'
			}],
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			sortname : "data",
			sortorder : "asc",
			width : 960,
			height : 255
		});
	},
	
	
	
	initGridParciais : function () {
		
		$(".contasAPagar_parciaispopGrid").flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Lcto',
				name : 'dtLancamento',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Rclt',
				name : 'dtRecolhimento',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Suplementação',
				name : 'suplementacao',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Encalhe',
				name : 'encalhe',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda',
				name : 'venda',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : '% Venda',
				name : 'percVenda',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda CE',
				name : 'vendaCE',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Reparte Acum.',
				name : 'reparteAcumulado',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda Acum.',
				name : 'vendaAcumulada',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : '% Venda Acum.',
				name : 'percVendaAcumulada',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'N° NF-e',
				name : 'numNfe',
				width : 50,
				sortable : true,
				align : 'left'
			}],
			sortname : "dtLancamento",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 900,
			height : 200
		});
	},
	

	initGridConsignado : function() {
		$(".contasAPagar-consignadoGrid").flexigrid({
			dataType : 'json',
			colModel : [ {
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
				width : 40,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço Capa R$',
				name : 'precoCapa',
				width : 80,
				sortable : true,
				align : 'right',
			}, {
				display : 'Preço c/ Desc. R$',
				name : 'desconto',
				width : 60,
				sortable : true,
				align : 'right',
			}, {
				display : 'Reparte Sug.',
				name : 'reparte',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Reparte Final',
				name : 'reparteFinal',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Dif.',
				name : 'diferenca',
				width : 30,
				sortable : true,
				align : 'center'
			}, {
				display : 'Motivo',
				name : 'motivo',
				width : 40,
				sortable : true,
				align : 'left'
			}, {
				display : 'Fornecedor',
				name : 'fornecedor',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor R$',
				name : 'vlr',
				width : 40,
				sortable : true,
				align : 'right'
			}, {
				display : 'Valor c/Desc R$',
				name : 'vlrDesc',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : 'N° NF-e',
				name : 'numNfe',
				width : 57,
				sortable : true,
				align : 'left'
			}],
			sortname : "codigo",
			sortorder : "asc",
			width : 895,
			height : 200
		});
	},
	
}, BaseController);


//Funções internas

function pesqDistribuidor(){
	$('.distrFornecedor').show();
	$('.filtroFornecedor').show();
	$('.porProdutos').hide();
	$('.filtroProduto').hide();
	$('.filtroBusca').show();
	$('.grids').show();
}

function pesqProduto(){
	$('.distrFornecedor').hide();
	$('.filtroFornecedor').hide();
	$('.porProdutos').show();
	$('.filtroProduto').show();
	$('.filtroBusca').show();
	$('.grids').show();
}

function gridDistrib(){
	$('.gridDistrib').show();
	$('.gridProduto').hide();
	}
function gridProduto(){
	$('.gridDistrib').hide();
	$('.gridProduto').show();
	}





	


	/*		
function popup_contaCorrente() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-conta" ).dialog({
			resizable: false,
			height:340,
			width:660,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
					$(".grids").show();
					
				}
			}
		});
	};	
function popup_encargos() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-encargos" ).dialog({
			resizable: false,
			height:'auto',
			width:450,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
					$(".grids").show();
					
				}
			}
		});
	};
	function popup_email() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-email" ).dialog({
			resizable: false,
			height:400,
			width:490,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
					
				}
				
				
				
			}
		});
	};
	
	function detalheVenda() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-detalhe-venda" ).dialog({
			resizable: false,
			height:420,
			width:650,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
				},
			}
		});
	};
	
	*/
	
	



//janelas de detalhamento de busca 
/*

	
function popup_edit_produto() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-edit-produto" ).dialog({
			resizable: false,
			height:360,
			width:500,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	
	
function popup_num_nota() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-num-nota" ).dialog({
			resizable: false,
			height:'auto',
			width:350,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
*/




