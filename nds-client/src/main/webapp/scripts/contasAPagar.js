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
		this.initGridEncalhe();
		this.initGridFaltasSobras();
	},
	
	
	pesqDistribuidor : function() {
		$('.distrFornecedor', this.workspace).show();
		$('.filtroFornecedor', this.workspace).show();
		$('.porProdutos', this.workspace).hide();
		$('.filtroProduto', this.workspace).hide();
		$('.filtroBusca', this.workspace).show();
		$('.grids', this.workspace).show();
	},
	

	pesqProduto : function() {
		$('.distrFornecedor', this.workspace).hide();
		$('.filtroFornecedor', this.workspace).hide();
		$('.porProdutos', this.workspace).show();
		$('.filtroProduto', this.workspace).show();
		$('.filtroBusca', this.workspace).show();
		$('.grids', this.workspace).show();
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
	
		var params = $("#contasAPagarForm", this.workspace).serializeObject();
		
		if ($("#contasAPagarRadioDistribuidor").get(0).checked) {
			this.pesquisarPorFornecedor(params);	
		} 
		
		else if ($("#contasAPagarRadioProduto").get(0).checked) {
			this.pesquisarPorProduto(params);
		}
	},
	
	
	pesquisarPorProduto : function(params) {
		
		var url = contasAPagarController.path + 'pesquisarPorProduto.json';
		params = serializeArrayToPost('filtro.produtoEdicaoIDs', contasAPagarController.obterSelecaoColunaCheckProdutoEdicao(), params);
		
		$(".porProdutosGrid").flexOptions({
			url : url,
			params: serializeParamsToFlexiGridPost(params),
			preProcess : contasAPagarController.insereLinksContasAPagarPorProdutos,
			newp : 1
		});
		
		params['filtro.primeiraCarga'] = true;
		
		$.postJSON(
			url,
			params,
			function(result) {
				
				$("#contasAPagar_gridProdutoTotalPagto").html(result.totalPagto);
				$("#contasAPagar_gridProdutoTotalDesconto").html(result.totalDesconto);
				$("#contasAPagar_gridProdutoValorLiquido").html(result.valorLiquido);
				
				$(".porProdutosGrid", contasAPagarController.workspace).flexAddData({rows: toFlexiGridObject(result.grid), page: 1, total: result.totalGrid});
			},
			null,
			true
		);
		
		$('.gridProduto').show();
	},
	
	
	pesquisarPorFornecedor : function (params) {
		
		params['filtro.primeiraCarga'] = true;
		var url = contasAPagarController.path + 'pesquisarPorFornecedor.json'; 
		
		$(".porDistrFornecedorGrid").flexOptions({
			url : url,
			params: serializeParamsToFlexiGridPost(params),
			preProcess : contasAPagarController.insereLinksContasAPagarPorDistribuidores,
			newp : 1
		});

		$.postJSON(
			url,
			params,
			function(result) {
				
				$("#contasAPagar_gridFornecedorTotalBruto").html(result.totalBruto);
				$("#contasAPagar_gridFornecedorTotalDesconto").html(result.totalDesconto);
				$("#contasAPagar_gridFornecedorSaldo").html(result.saldo);
				
				$(".porDistrFornecedorGrid", contasAPagarController.workspace).flexAddData({rows: toFlexiGridObject(result.grid), page: 1, total: result.totalGrid});
			},
			null,
			true
		);

		$('.gridDistrib').show();
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
		
		var produtoEdicaoIDs = new Array();

		$("input[type=checkbox][name='checkProdutoContasAPagar']:checked").each(function(i,element) {			
			produtoEdicaoIDs.push(element.value);
		});
		
		return produtoEdicaoIDs;
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
			
			if (value.cell.consignado != "0,00") {
				var linkConsignado = '<a href="javascript:;" onclick="contasAPagarController.popup_consignado(\'' + value.cell.data + '\');" title="Detalhe Consignado">'+value.cell.consignado+'</a>';
				value.cell.consignado = linkConsignado;
			}
		
			if (value.cell.encalhe != "0,00") {
				var linkEncalhe = '<a href="javascript:;" onclick="contasAPagarController.popup_encalhe(\'' + value.cell.data + '\');" title="Detalhe Encalhe">'+value.cell.encalhe+'</a>';
				value.cell.encalhe = linkEncalhe;
			}
			
			if (value.cell.faltasSobras != "0,00") {
				var linkFS = '<a href="javascript:;" onclick="contasAPagarController.popup_faltasSobras(\'' + value.cell.data + '\');" title="Detalhe Faltas e Sobras">'+value.cell.faltasSobras+'</a>';
				value.cell.faltasSobras = linkFS;
			}
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
	
	
	montaTabelaTotaisDistribuidores : function(table, data) {
		
		for(var i=0; i<data.length; i++) {
		
			var tr = table.insertRow(-1);
			
			var td1 = tr.insertCell(0);
			td1.style.width = 53;
			td1.innerHTML = '<strong>' + data[i].nome + '</strong>';
			
			var td2 = tr.insertCell(1);
			td2.style.width = 92;
			td2.style.textAlign = 'right';
			td2.innerHTML = '<strong>' + data[i].total + '</strong>';
			
			var td3 = tr.insertCell(2);
			td3.style.width = 10;
			td3.innerHTML = '&nbsp;';
		}
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
	
	
	popup_consignado : function(data) {
		
		$("#contasAPagar_dataDetalhe").val(data);
		var params = $("#contasAPagarForm").serializeObject();
		
		$.postJSON(
			contasAPagarController.path + 'pesquisarConsignado.json',
			params,
			function(result) {
				contasAPagarController.montaTabelaTotaisDistribuidores($("#contasAPagar_table_popupConsignado").get(0), result.totalDistrib);
				$(".contasAPagar-consignadoGrid").flexAddData({rows: toFlexiGridObject(result.grid), page : 1, total : 1});
			},
			null,
			true
		);
		
		$("#contasAPagar_legend_popupConsignado").html(data);
	
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
	

	popup_encalhe : function(data) {
		
		$("#contasAPagar_dataDetalhe").val(data);
		var params = $("#contasAPagarForm").serialize();
		
		$.postJSON(
			this.path + "pesquisarEncalhe.json?" + params,
			null,
			function(result) {
				contasAPagarController.montaTabelaTotaisDistribuidores($("#contasAPagar_table_popupEncalhe").get(0), result.totalDistrib);
				$(".contasAPagar_EncalheGrid").flexAddData({rows: toFlexiGridObject(result.grid), page : 1, total : 1});
			}
		);
		
		$("#contasAPagar_legend_popupEncalhe").html(data);
		
		$("#contasAPagar_popupEncalhe").dialog({
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
	},
	
	
	popup_faltasSobras : function(data) {
		
		$("#contasAPagar_dataDetalhe").val(data);
		var params = $("#contasAPagarForm").serialize();
		
		$.postJSON(
			this.path + "pesquisarFaltasSobras.json?" + params,
			null,
			function(result) {
				contasAPagarController.montaTabelaTotaisDistribuidores($("#contasAPagar_table_popupFaltasSobras").get(0), result.totalDistrib);
				$(".contasAPagar_faltasSobrasGrid").flexAddData({rows: toFlexiGridObject(result.grid), page : 1, total : 1});
			}
		);
		
		$("#contasAPagar_legend_popupFaltasSobras").html(data);
	
		$("#contasAPagar_popupFaltasSobras").dialog({
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
				name : 'debitoCredito',
				width : 110,
				sortable : true,
				align : 'right'
			}, {
				display : 'Saldo a Pagar R$',
				name : 'saldo',
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
				name : 'lcto',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Rclt',
				name : 'rclt',
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
				name : 'pctVenda',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda CE',
				name : 'vendaCe',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Reparte Acum.',
				name : 'reparteAcum',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda Acum.',
				name : 'vendaAcum',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : '% Venda Acum.',
				name : 'pctVendaAcum',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'N° NF-e',
				name : 'nfe',
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
				name : 'precoComDesconto',
				width : 60,
				sortable : true,
				align : 'right',
			}, {
				display : 'Reparte Sug.',
				name : 'reparteSugerido',
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
				name : 'valor',
				width : 40,
				sortable : true,
				align : 'right'
			}, {
				display : 'Valor c/Desc R$',
				name : 'valorComDesconto',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : 'N° NF-e',
				name : 'nfe',
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
	
	
	initGridEncalhe : function() {
		$(".contasAPagar_encalheGrid").flexigrid({
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
				width : 130,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço Capa R$',
				name : 'valor',
				width : 95,
				sortable : true,
				align : 'right'
			}, {
				display : 'Preço c/ Desc. R$',
				name : 'desconto',
				width : 95,
				sortable : true,
				align : 'right'
			}, {
				display : 'Encalhe',
				name : 'encalhe',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Fornecedor',
				name : 'fornecedor',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor R$',
				name : 'total',
				width : 75,
				sortable : true,
				align : 'right',
			}],
			sortname : "Nome",
			sortorder : "asc",
			width : 800,
			height : 200
		});
	},
	
	
	initGridFaltasSobras : function () {
		$(".contasAPagar_faltasSobrasGrid").flexigrid({
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
				width : 150,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço de Capa R$',
				name : 'precoCapa',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Preço c/ Desc. R$',
				name : 'precoComDesconto',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : 'Box',
				name : 'box',
				width : 40,
				sortable : true,
				align : 'center'
			}, {
				display : 'Exemplares',
				name : 'exemplares',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Fornecedor',
				name : 'fornecedor',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 70,
				sortable : true,
				align : 'right',
			}],
			sortname : "codigo",
			sortorder : "asc",
			width : 800,
			height : 200
		});
	},
	
}, BaseController);

