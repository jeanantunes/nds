var contasAPagarController = $.extend(true, {
	
	path : contextPath + '/financeiro/contasAPagar/',

	init : function() {
		
		$("#contasAPagar_Filtro_De", this.workspace).mask("99/99/9999");
		$("#contasAPagar_Filtro_Ate", this.workspace).mask("99/99/9999");
		$("#contasAPagar_Filtro_Ce", this.workspace).numeric();
		
		$("#conta-pagar-produto", this.workspace).autocomplete({source: ''});
		$("#edicao", this.workspace).numeric();
		
		$("#contasAPagar_Filtro_De", this.workspace).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$("#contasAPagar_Filtro_Ate", this.workspace).datepicker({
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
		this.initGridDiferencas();
		
		$('#contasAPagar_areaBts', this.workspace).hide();
	},
	
	
	pesqDistribuidor : function() {
		$('#contasAPagar_distrFornecedor', this.workspace).show();
		$('#contasAPagar_filtroFornecedor', this.workspace).show();
		$('#contasAPagar_porProdutos', this.workspace).hide();
		$('#contasAPagar_filtroProduto', this.workspace).hide();
		$('.filtroBusca', this.workspace).show();
		$('.grids', this.workspace).show();
	},
	

	pesqProduto : function() {
		$('#contasAPagar_distrFornecedor', this.workspace).hide();
		$('#contasAPagar_filtroFornecedor', this.workspace).hide();
		$('#contasAPagar_porProdutos', this.workspace).show();
		$('#contasAPagar_filtroProduto', this.workspace).show();
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
		
		$(".contasApagarCheck", this.workspace).each(function(index, element) {
			element.checked = check.checked;
		});
	},
	
	
	/*
	 * *********************
	 * Autocomplete
	 * *********************
	 * */
	
	pesquisarProdutoPorCodigo : function() {
		pesquisaProdutoCAP.pesquisarPorCodigoProduto('#codigo', '#conta-pagar-produto', '#edicao', false);	
	},
	
	
	pesquisarProdutoPorNome : function() {
		pesquisaProdutoCAP.pesquisarPorNomeProduto('#codigo', '#conta-pagar-produto', '#edicao', false);
	},
	
	pesquisarProdutoPorCodigoConsignado : function() {
		pesquisaProdutoCAP.pesquisarPorCodigoProduto('#codigoConsignado', '#produtoConsignado', '#edicaoConsignado', false);	
	},
	
	
	pesquisarProdutoPorNomeConsignado : function() {
		pesquisaProdutoCAP.pesquisarPorNomeProduto('#codigoConsignado', '#produtoConsignado', '#edicaoConsignado', false);
	},
	
	/*
	 * *********************
	 * Carregamento FlexGrid
	 * *********************
	 * */
	
	pesquisar : function(){
	
		var params = $("#contasAPagarForm", this.workspace).serializeObject();
		
		if ($("#contasAPagarRadioDistribuidor", this.workspace).get(0).checked) {
			this.pesquisarPorFornecedor(params);	
		} 
		
		else if ($("#contasAPagarRadioProduto", this.workspace).get(0).checked) {
			
			this.pesquisarPorProduto(params);
		}
	},
	
	
	pesquisarPorProduto : function(params) {
		
		var url = contasAPagarController.path + 'pesquisarPorProduto.json';
		params = serializeArrayToPost('filtro.produtoEdicaoIDs', contasAPagarController.obterSelecaoColunaCheckProdutoEdicao(), params);
		
		$("#contasAPagar_porProdutosGrid", this.workspace).flexOptions({
			url : url,
			params: serializeParamsToFlexiGridPost(params),
			preProcess : contasAPagarController.insereLinksContasAPagarPorProdutos,
			newp : 1
		});
		
		params['filtro.primeiraCarga'] = true;
		params['rp'] = 15;
		
		$.postJSON(
			url,
			params,
			function(result) {
				
				$("#contasAPagar_gridProdutoTotalPagto", contasAPagarController.workspace).html(result.totalPagto);
				$("#contasAPagar_gridProdutoTotalDesconto", contasAPagarController.workspace).html(result.totalDesconto);
				$("#contasAPagar_gridProdutoValorLiquido", contasAPagarController.workspace).html(result.valorLiquido);
				$("#contasAPagar_porProdutosGrid", contasAPagarController.workspace).flexAddData(
						{rows: toFlexiGridObject(result.grid), page: 1, total: result.totalGrid});
				$('#contasAPagar_gridProduto').show();
				$('#contasAPagar_areaBts').show();
				
				$(".pesquisaContasPagar", contasAPagarController.workspace).show();
			},
			function(result) {
				
				$('#contasAPagar_gridProduto', contasAPagarController.workspace).hide();
				$('#contasAPagar_areaBts', contasAPagarController.workspace).hide();
			},
			true
		);
		
		
	},
	
	
	pesquisarPorFornecedor : function (params) {
		
		params['filtro.primeiraCarga'] = true;
		params['page'] = 1;
		params['rp'] = 15;
		
		$.each($("input:checked[name='filtro.idsFornecedores']", this.workspace), function(index, item){
			params['filtro.idsFornecedores['+index+']'] = item.value;
		});
		
		var url = contasAPagarController.path + 'pesquisarPorFornecedor.json'; 
		
		$(".porDistrFornecedorGrid", this.workspace).flexOptions({
			url : url,
			params: serializeParamsToFlexiGridPost(params),
			newp : 1
		});

		$.postJSON(
			url,
			params,
			function(result) {
				
				$("#contasAPagar_gridFornecedorTotalBruto", contasAPagarController.workspace).html(result.totalBruto);
				$("#contasAPagar_gridFornecedorTotalDesconto", contasAPagarController.workspace).html(result.totalDesconto);
				$("#contasAPagar_gridFornecedorSaldo", contasAPagarController.workspace).html(result.saldo);
				$(".porDistrFornecedorGrid", contasAPagarController.workspace).flexAddData(
						{rows: toFlexiGridObject(result.grid), page: 1, total: result.totalGrid});
				$('#contasAPagar_gridDistrib', contasAPagarController.workspace).show();
				$('#contasAPagar_areaBts', contasAPagarController.workspace).show();
				
				$(".pesquisaContasPagar", contasAPagarController.workspace).show();
				
				params['filtro.primeiraCarga'] = false;
				$(".porDistrFornecedorGrid", this.workspace).flexOptions({
					params: serializeParamsToFlexiGridPost(params)
				});
			},
			function(result) {
				
				$('#contasAPagar_gridDistrib', contasAPagarController.workspace).hide();
				$('#contasAPagar_areaBts', contasAPagarController.workspace).hide();
				
				params['filtro.primeiraCarga'] = false;
				$(".porDistrFornecedorGrid", this.workspace).flexOptions({
					params: serializeParamsToFlexiGridPost(params)
				});
			},
			true
		);

		
	},
	
	
	pesquisarProdutoEdicao : function(){
		
		var params = $("#form-pesq-produto-contasAPagar", this.workspace).serialize();
		
		$(".contasAPagarListaProdutosGrid", this.workspace).flexOptions({
			url : this.path + 'pesquisarProduto.json?' + params, 
			preProcess : contasAPagarController.montaColunaCheckProdutoEdicao,
			newp : 1
		});

		$(".contasAPagarListaProdutosGrid", this.workspace).flexReload();
		
		$('#contasAPagar_areaBts', this.workspace).show();
	},
	
	
	/*
	 * *************************
	 * Parametros de envio Pesquisa por produto
	 * *************************
	 * */
	obterSelecaoColunaCheckProdutoEdicao : function () {
		
		var produtoEdicaoIDs = new Array();

		$("input[type=checkbox][name='checkProdutoContasAPagar']:checked", this.workspace).each(function(i,element) {			
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
			
			if(!value.cell.editor) {
				value.cell.editor = "nenhum";
			}
			
			var checkbox = '<input type="checkbox" value="'+ value.cell.produtoEdicaoID + 
				'" name="checkProdutoContasAPagar" class="contasApagarCheck"  style="float:left;"/>';
			value.cell.sel = checkbox;
		});
		 
		return data;
	},
	

	insereLinksContasAPagarPorDistribuidores : function(data) {
		
		$.each(data.rows, function(index, value) {
			
			if (value.cell.consignado != "0,00" && value.cell.consignado != "0.00") {
				var linkConsignado = '<a href="javascript:;" onclick="contasAPagarController.popup_consignado(\'' + value.cell.data + '\');" title="Detalhe Consignado">'+value.cell.consignado+'</a>';
				value.cell.consignado = linkConsignado;
			}
		
			if (value.cell.encalhe != "0,00" && value.cell.encalhe != "0.00") {
				var linkEncalhe = '<a href="javascript:;" onclick="contasAPagarController.popup_encalhe(\'' + value.cell.data + '\');" title="Detalhe Encalhe">'+value.cell.encalhe+'</a>';
				value.cell.encalhe = linkEncalhe;
			}
			
			if (value.cell.faltasSobras != "0,00" && value.cell.faltasSobras != "0.00") {
				var linkFS = '<a href="javascript:;" onclick="contasAPagarController.popup_faltasSobras(\'' + value.cell.data + '\');" title="Detalhe Faltas e Sobras">'+value.cell.faltasSobras+'</a>';
				value.cell.faltasSobras = linkFS;
			}
		});
	
		return data;
	},
	
	
	insereLinksContasAPagarPorProdutos : function(data) {	
		
		$.each(data.rows, function(index, value) {
			
			if (value.cell.tipo != "N"){
				
				var params = "'" + value.cell.produtoEdicaoId + "', '" 
				 	+ value.cell.codigo          + "', '"
				 	+ value.cell.produto         + "', '"
				 	+ value.cell.edicao          + "', '"
				 	+ value.cell.fornecedor      + "', '" 
				 	+ value.cell.rctl	         + "'";
				
				value.cell.tipo = '<a href="javascript:;" onclick="contasAPagarController.popup_detalhes(' + params + ');" title="Detalhe Consignado">' + value.cell.tipo + '</a>';
			}
			
		});
	
		return data;
	},
	
	
	montaTabelaTotaisDistribuidores : function(table, data) {
		
		while (table.rows.length > 0) {
			table.deleteRow(-1);
		}
		
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
		
		$( "#dialog-pesq-produto-contasAPagar", this.workspace).dialog({
			resizable: false,
			height:'auto',
			width:600,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				}
			},
			form: $("#form-pesq-produto-contasAPagar", this.workspace)
		});
	},
	
	
	popup_detalhes : function (produtoEdicaoId, codigo, produto, edicao, fornecedor, dataLcto) {
		
		// popula cabecalho
		$("#contasAPagar_popupTipo_codigo", this.workspace).html(codigo);
		$("#contasAPagar_popupTipo_produto", this.workspace).html(produto);
		$("#contasAPagar_popupTipo_edicao", this.workspace).html(edicao);
		$("#contasAPagar_popupTipo_fornecedor", this.workspace).html(fornecedor);
		
		$.postJSON(
			contasAPagarController.path + 'pesquisarParcial.json',
			[{name:'filtro.produto', value:codigo}, {name:'filtro.dataDetalhe', value:dataLcto}, {name:'page', value:1}],
			function(result) {
				
				$(".contasAPagar_parciaispopGrid", contasAPagarController.workspace).flexAddData(
						{rows: (result.rows), page : result.page, total : result.total});
				
				$("#dialog-contasAPagar-tipo", contasAPagarController.workspace).dialog({
					resizable: false,
					height:535,
					width:955,
					modal: true,
					buttons: {
						"Fechar": function() {
							$( this ).dialog( "close" );
						
						},
					 },
					 form: $("#form-contasAPagar-tipo", contasAPagarController.workspace)
				});
			}
		);
	},
	
	
	popup_consignado : function(data) {
		
		if (data){
			$("#contasAPagar_dataDetalhe", this.workspace).val(data);
		}
		
		var params = $("#contasAPagarForm", this.workspace).serializeArray();
		
		params.push({name: 'filtro.produtoConsignado', value: $("#produtoConsignado", this.workspace).val()});
		params.push({name: 'filtro.edicaoConsignado',  value: $("#edicaoConsignado", this.workspace).val()});
		
		$.postJSON(
			contasAPagarController.path + 'pesquisarConsignado.json',
			params,
			function(result) {
				contasAPagarController.montaTabelaTotaisDistribuidores(
						$("#contasAPagar_table_popupConsignado", contasAPagarController.workspace).get(0), result.totalDistrib);
				$(".contasAPagar-consignadoGrid", contasAPagarController.workspace).flexAddData(
						{rows: contasAPagarController.montarLinkDiferencas(toFlexiGridObject(result.grid), data), page : 1, total : 1});
				
				if (data){
					$("#contasAPagar_legend_popupConsignado", contasAPagarController.workspace).html(data);
				}
				
				$("#dialog-contasAPagar-consignado", contasAPagarController.workspace).dialog({
					resizable: false,
					height:480,
					width:940,
					modal: true,
					buttons: {
						"Fechar": function() {
							$( this ).dialog( "close" );
						},
					},
					form: $("#form-contasAPagar-consignado", contasAPagarController.workspace)
				});
			}
		);
	},
	
	montarLinkDiferencas : function(grid, data){
		
		$.each(grid, function(index, element){
			
			if (element.cell.diferenca && element.cell.diferenca != 0){
				
				element.cell.diferenca = 
					'<a href="javascript:;" onclick="contasAPagarController.exibirDiferencas('
					.concat("'").concat(element.cell.codigo).concat("'")
					.concat(",")
					.concat(element.cell.edicao)
					.concat(",")
					.concat("'").concat(data).concat("'")
					.concat(",")
					.concat("'").concat(element.cell.produto).concat("'")
					.concat(');">')
					.concat('<img style="width:15px; height:15px;" src="')
					.concat(contextPath)
					.concat('/images/bt_expedicao.png">')
					.concat('</img>')
					.concat('</a>');
			}
		});
		
		return grid;
	},
	
	exibirDiferencas : function(codigoProduto, numeroEdicao, data, nomeProd){
		
		$.postJSON(
			contasAPagarController.path + 'pesquisarDiferencas',
			[{name:'codigoProduto', value:codigoProduto},
			 {name:'numeroEdicao', value:numeroEdicao},
			 {name:'data', value:data}],
			function(result) {
				
				$("#legend_diferencas", contasAPagarController.workspace).text(
						codigoProduto + " - " + nomeProd + ": " + numeroEdicao);
				
				$("#grid_diferencas", contasAPagarController.workspace).flexAddData(
						{rows: result.rows, page: 1, total: result.rows.length});
				
				$("#dialog-diferencas", contasAPagarController.workspace).dialog({
					resizable: false,
					height:360,
					width:300,
					modal: true,
					buttons: {
						"Fechar": function() {
							$( this ).dialog( "close" );
						},
					},
					form: $("#form-diferencas", contasAPagarController.workspace)
				});
			}
		);
	},

	popup_encalhe : function(data) {
		
		$("#contasAPagar_dataDetalhe", this.workspace).val(data);
		var params = $("#contasAPagarForm", this.workspace).serialize();
		
		$.postJSON(
			this.path + "pesquisarEncalhe.json?" + params,
			null,
			function(result) {
				contasAPagarController.montaTabelaTotaisDistribuidores(
						$("#contasAPagar_table_popupEncalhe", contasAPagarController.workspace).get(0), result.totalDistrib);
				$(".contasAPagar_encalheGrid", contasAPagarController.workspace).flexAddData(
						{rows: toFlexiGridObject(result.grid), page : 1, total : 1});
				
				$("#contasAPagar_legend_popupEncalhe", contasAPagarController.workspace).html(data);
				
				$("#contasAPagar_popupEncalhe", contasAPagarController.workspace).dialog({
					resizable: false,
					height:460,
					width:860,
					modal: true,
					buttons: {
						"Fechar": function() {
							$( this ).dialog( "close" );
						}
					},
					form: $("#form-contasAPagar_popupEncalhe", contasAPagarController.workspace)
				});
			}
		);
	},
	
	
	popup_faltasSobras : function(data) {
		
		$("#contasAPagar_dataDetalhe", this.workspace).val(data);
		var params = $("#contasAPagarForm", this.workspace).serialize();
		
		$.postJSON(
			this.path + "pesquisarFaltasSobras.json?" + params,
			null,
			function(result) {
				contasAPagarController.montaTabelaTotaisDistribuidores(
						$("#contasAPagar_table_popupFaltasSobras", contasAPagarController.workspace).get(0), result.totalDistrib);
				$(".contasAPagar_faltasSobrasGrid", contasAPagarController.workspace).flexAddData(
						{rows: toFlexiGridObject(result.grid), page : 1, total : 1});
				
				$("#contasAPagar_legend_popupFaltasSobras", contasAPagarController.workspace).html(data);
				
				$("#contasAPagar_popupFaltasSobras", contasAPagarController.workspace).dialog({
					resizable: false,
					height:460,
					width:860,
					modal: true,
					buttons: {
						"Fechar": function() {
							$( this ).dialog( "close" );
							$(".grids", contasAPagarController.workspace).show();
						}
					},
					form: $("#form-contasAPagar_popupFaltasSobras", contasAPagarController.workspace)
				});
			}
		);
	},
	
	preProcessFaltasSobras : function(data){
		
		$.each(data.rows, function(index, item){
			
			if (!item.cell.box){
				item.cell.box = '';
			}
		});
		
		return data;
	},
	
	limparCampoSemanaCE : function(){
		
		$("#contasAPagar_Filtro_Ce", this.workspace).val("");
	},
	
	calcularPeriodoCE : function(){
		
		$.postJSON(
			this.path + "calcularPeriodoCE.json",
			[{name:'semanaCE', value:$("#contasAPagar_Filtro_Ce", this.workspace).val()}],
			function(result) {
				
				$("#contasAPagar_Filtro_De", this.workspace).val(result.de.$);
				$("#contasAPagar_Filtro_Ate", this.workspace).val(result.ate.$);
			}
		);
	},
	
	/*
	 * *************************
	 * Configurações do Grid
	 * *************************
	 * */
	initcontasAPagarListaProdutosGrid : function(){
		$(".contasAPagarListaProdutosGrid", contasAPagarController.workspace).flexigrid({
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
			sortname : "edicao",
			sortorder : "asc",
			width : 550,
			height : 200
		});
	},
	
	
	initGridPesquisarPorFornecedor : function(){
	
		$(".porDistrFornecedorGrid", contasAPagarController.workspace).flexigrid({
			preProcess : contasAPagarController.insereLinksContasAPagarPorDistribuidores,
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
				display : 'Estoque R$',
				name : 'estoque',
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
				display : 'Faltas/Sobras R$',
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
		
		$("#contasAPagar_porProdutosGrid", contasAPagarController.workspace).flexigrid({
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
				display : 'Estoque',
				name : 'estoque',
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
			sortname : "rctl",
			sortorder : "desc",
			width : 960,
			height : 255
		});
	},
	
	
	
	initGridParciais : function () {
		
		$(".contasAPagar_parciaispopGrid", contasAPagarController.workspace).flexigrid({
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
			sortname : "lcto",
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
		$(".contasAPagar-consignadoGrid", contasAPagarController.workspace).flexigrid({
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
				display : 'Fornecedor',
				name : 'fornecedor',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 50,
				sortable : true,
				align : 'right'
			}, {
				display : 'Valor c/Desc R$',
				name : 'valorComDesconto',
				width : 70,
				sortable : true,
				align : 'right'
			}],
			sortname : "codigo",
			sortorder : "asc",
			width : 895,
			height : 200
		});
	},
	
	
	initGridEncalhe : function() {
		$(".contasAPagar_encalheGrid", contasAPagarController.workspace).flexigrid({
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
				name : 'precoCapa',
				width : 95,
				sortable : true,
				align : 'right'
			}, {
				display : 'Preço c/ Desc. R$',
				name : 'precoComDesconto',
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
				name : 'valor',
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
		$(".contasAPagar_faltasSobrasGrid", contasAPagarController.workspace).flexigrid({
			preProcess : contasAPagarController.preProcessFaltasSobras,
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
	
	initGridDiferencas : function(){
		$("#grid_diferencas", contasAPagarController.workspace).flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Quantidade',
				name : 'diferenca',
				width : 60,
				sortable : false,
				align : 'left'
			}, {
				display : 'Motivo',
				name : 'motivo',
				width : 120,
				sortable : false,
				align : 'left'
			}]
		});
	},
	
	exportarPesquisaPrincipal: function(tipoArquivo){
		
		if ($("#contasAPagarRadioProduto", this.workspace).is(":checked")){
			
			window.open(contasAPagarController.path + "exportPesquisarPorProduto?fileType=" + tipoArquivo, "_blank");
		} else {
			
			window.open(contasAPagarController.path + "exportPesquisarPorDistribuidor?fileType=" + tipoArquivo, "_blank");
		}
	},
	
	exportPesquisarParcial : function(tipoArquivo){
		
		window.open(
			contasAPagarController.path + "exportPesquisarParcial?fileType=" + tipoArquivo +
				encodeURI("&codigoProduto=" + $("#contasAPagar_popupTipo_codigo", contasAPagarController.workspace).text() +
				"&nomeProduto=" + $("#contasAPagar_popupTipo_produto", contasAPagarController.workspace).text() +
				"&edicao=" + $("#contasAPagar_popupTipo_edicao", contasAPagarController.workspace).text() +
				"&nomeFornecedor=" + $("#contasAPagar_popupTipo_fornecedor", contasAPagarController.workspace).text()), 
			"_blank");
	}
	
}, BaseController);

//@ sourceURL=contasAPagar.js
