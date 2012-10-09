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
			$(".porDistrFornecedorGrid", this.workspace).flexOptions({
				url : this.path + 'pesquisar.json?' + params, 
				//preProcess : contasAPagarController.insereLinksContasAPagarPorDistribuidores,
				newp : 1
			});

			//$(".porProdutosGrid", this.workspace).flexReload();
			$('.gridDistrib').show();
			
		}
		else if ($("#contasAPagarRadioProduto").get(0).checked) {
			
			$(".porProdutosGrid", this.workspace).flexOptions({
				url : this.path + 'pesquisar.json?' + params, 
				//preProcess : contasAPagarController.insereLinksContasAPagarPorProdutos,
				newp : 1
			});

			//$(".porProdutosGrid", this.workspace).flexReload();
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
	 * Pre Carregamento FlexGrid
	 * *************************
	 * */
	
	/*mantemSelecaoColunaCheckProdutoEdicao : function (data){
		
		
		$.each(data.rows, function(index, value) {
			
			if(value.cell.sel.checked)
			var selecionados;

		}
		 
		return data;
		
	}*/
	
	montaColunaCheckProdutoEdicao : function(data) {
		
		$.each(data.rows, function(index, value) {
			
			var checkbox = '<input type="checkbox" id="contasAPagarCheckSelecionarTodos" name="selecionado"  style="float:left;"/>';
						
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
		
		//Codigo de Exemplo
		
		/*$.each(data.rows, function(index, value) {
			
			var linkConsignado = '<a href="javascript:;" onclick="contasAPagarController.popup_consignado();" title="Detalhe Consignado">'+value.cell.consignado+'</a>';
			
			var linkEncalhe = '<a href="javascript:;" onclick="contasAPagarController.popup_encalhe();" title="Detalhe Consignado">'+value.cell.encalhe+'</a>';
			
			var linkFS = '<a href="javascript:;" onclick="contasAPagarController.popup_faltasSobras();" title="Detalhe Consignado">'+value.cell.faltasSobras+'</a>';
						
			value.cell.consignado = linkConsignado;
			value.cell.encalhe = linkEncalhe;
			value.cell.faltasSobras = linkFS;
		});
	
		return data;*/
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
				sortable : true,
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
			colModel : [ {
				display : 'Rclt',
				name : 'data',
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
				name : 'debitoCredito',
				width : 60,
				sortable : true,
				align : 'center'
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


function popup_consignado() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:490,
			width:890,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
					$(".grids").show();
					
				},
				
			}
		});
	};
	
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
	
	function popup_detalhes() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-detalhes-tipo" ).dialog({
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
function popup_consignado() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-consignado" ).dialog({
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
	};
	
	
	
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




