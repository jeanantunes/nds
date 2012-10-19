var impressaoNfeController = $.extend(true, {

	filtroProdutos : [],

	init : function() {
		$( "#dataEmissao", impressaoNfeController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#dataMovimentoInicial", impressaoNfeController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#dataMovimentoFinal", impressaoNfeController.workspace ).datepicker({
			showOn: "button",
			buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$( "#dataEmissao", impressaoNfeController.workspace ).mask("99/99/9999");
		$( "#dataMovimentoInicial", impressaoNfeController.workspace ).mask("99/99/9999");
		$( "#dataMovimentoFinal", impressaoNfeController.workspace ).mask("99/99/9999");

		$(".produtosPesqGrid", impressaoNfeController.workspace).flexigrid({
			preProcess : impressaoNfeController.prepararJSONPesquisaProdutos,
			url : contextPath + "/nfe/impressaoNFE/pesquisarProdutosImpressaoNFE",
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigoProduto',
				width : 80,
				sortable : true,
				align : 'left',
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 340,
				sortable : true,
				align : 'left',
			}, {
				display : '',
				name : 'sel',
				width : 20,
				sortable : false,
				align : 'center',
			}],
			showToggleBtn : false,
			sortname : "codigoProduto",
			sortorder : "asc",
			usepager : false,
			useRp : true,
			rp : 15,
			width : 500,
			height : 130
		});

		$(".produtosAdicionadosPesqGrid", impressaoNfeController.workspace).flexigrid({
			preProcess : impressaoNfeController.prepararJSONPesquisaProdutosFiltrados,
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigoProduto',
				width : 80,
				sortable : false,
				align : 'left',
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 340,
				sortable : false,
				align : 'left',
			}, {
				display : '',
				name : 'sel',
				width : 20,
				sortable : false,
				align : 'center',
			}],
			showToggleBtn : false,
			showTableToggleBtn : false,
			onChangeSort: true,
			sortname : "codigoProduto",
			sortorder : "asc",
			usepager : false,
			useRp : false,
			rp : 15,
			width : 500,
			height : 100,
			onChangeSort: function(name, order) {
				impressaoNfeController.sortGrid(".produtosAdicionadosPesqGrid", order); 
			},
		});

		$(".impressaoGrid", impressaoNfeController.workspace).flexigrid({
			preProcess : null,
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'idCota',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeCota',
				width : 465,
				sortable : true,
				align : 'left'
			}, {
				display : 'Total Exemplares',
				name : 'totalExemplares',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Total R$',
				name : 'vlrTotal',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Total Desc. R$',
				name : 'vlrTotalDesconto',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Impressão',
				name : 'notaImpressa',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : '',
				name : 'sel',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			sortname : "idCota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rpOptions: [10, 15, 20, 30, 50],
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});

		$("#selFornecedor", impressaoNfeController.workspace).click(function() {
			$(".menu_fornecedor", impressaoNfeController.workspace).show().fadeIn("fast");
		})

		$(".menu_fornecedor", impressaoNfeController.workspace).mouseleave(function() {
			$(".menu_fornecedor", impressaoNfeController.workspace).hide();
		});

		$("#selProdutos", impressaoNfeController.workspace).click(function() {
			$("#dialog-pesqProdutos", this.workspace).dialog( "open" );
		})

		$(".menu_produtos", impressaoNfeController.workspace).mouseleave(function() {
			$(".menu_produtos", impressaoNfeController.workspace).hide();
		});		

		//$(".grids").show();

		this.criarDialogPesquisarProdutos();
	},

	criarDialogPesquisarProdutos : function() {		

		$("#dialog-pesqProdutos", this.workspace).dialog({
			resizable: false,
			height:550,
			width:540,
			modal: true,
			autoOpen: false,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "destroy" );
				},
			},
			div: $("#dialog-pesqProdutos", this.workspace).parents("div")
		});

	},

	prepararJSONPesquisaProdutos : function(data) {

		$.each(data.rows, function() {
			checked = false;
			for(i=0; i < impressaoNfeController.filtroProdutos.length; i++) {
				if(impressaoNfeController.filtroProdutos[i]['cell']['codigoProduto'] == this.cell.codigoProduto) {
					checked = true;
				}
			}
			this.cell.sel = '<input type="checkbox" '+ (checked ? 'checked=checked':'') +' name="codigoProduto" id="codigoProduto_'+ this.cell.codigoProduto +'" value="'+ this.cell.codigoProduto +'" alt="'+ this.cell.nomeProduto + '"  onclick="impressaoNfeController.adicionarAosProdutosFiltradosERecarregar(this.checked, '+ this.cell.codigoProduto +', \''+ this.cell.nomeProduto +'\')" />';
		});
		return data;
	},

	prepararJSONPesquisaProdutosFiltrados : function(data) {

		$.each(data.rows, function() {
			this.cell.sel = '<img style="cursor: pointer;" src="'+ contextPath +'/images/ico_excluir.gif" alt="Excluir do filtro" title="Excluir do filtro" name="removerProduto" id="removerProduto_'+ this.cell.codigoProduto +'" value="'+ this.cell.codigoProduto + '" onclick="impressaoNfeController.removerDosProdutosFiltrado('+ this.cell.codigoProduto +')" />';
		});
		return data;
	},

	/**
	 * Retorna as cotas com NFe disponíveis para emissão baseado nos filtros selecionados
	 */
	pesquisar : function() {

		params = [ 	{name:'filtro.tipoNFe', value:$('#tipoNFe', impressaoNfeController.workspace).val()},
		           	{name:'filtro.dataMovimentoInicial', value:$('#dataMovimentoInicial', impressaoNfeController.workspace).val()},
		           	{name:'filtro.dataMovimentoFinal', value:$('#dataMovimentoFinal', impressaoNfeController.workspace).val()},
		           	{name:'filtro.dataEmissao', value:$('#dataEmissao', impressaoNfeController.workspace).val()},
		           	{name:'filtro.idRoteiro', value:$('#idRoteiro', impressaoNfeController.workspace).val()},
		           	{name:'filtro.idRota', value:$('#idRota option:selected', impressaoNfeController.workspace).val()},
		           	{name:'filtro.idCotaInicial', value:$('#idCotaInicial', impressaoNfeController.workspace).val()},
		           	{name:'filtro.idCotaFinal', value:$('#idCotaFinal', impressaoNfeController.workspace).val()},
		           	{name:'filtro.idBoxInicial', value:$('#idBoxInicial', impressaoNfeController.workspace).val()},
		           	{name:'filtro.idBoxFinal', value:$('#idBoxFinal', impressaoNfeController.workspace).val()}		         	
		           	];

		var inputs = $('#menuFornecedores :checkbox');
		var values = [];
		inputs.each(function(index) {
			if(this.id != "selecionarTodosFornecedores" && this.checked) {	 
				params.push({
					'name' : "filtro.idsFornecedores[]",
					'value' : this.value
				});
			}
		});

		for(i = 0; i < impressaoNfeController.filtroProdutos.length; i++) {

			params.push({
				'name' : 'filtro.codigosProdutos[]',
				'value' : impressaoNfeController.filtroProdutos[i]['cell']['codigoProduto']
			});

		}


		$(".impressaoGrid", impressaoNfeController.workspace).flexOptions({
			preProcess: impressaoNfeController.executarPreProcessamento,
			url: contextPath + "/nfe/impressaoNFE/pesquisarImpressaoNFE",
			dataType : 'json',
			params: params
		});

		$(".impressaoGrid", impressaoNfeController.workspace).flexReload();

	},

	/**
	 * Carrega o combo de rotas baseado no roteiro selecionado 
	 */
	carregarRotas : function() {
		$.postJSON(contextPath + "/nfe/impressaoNFE/carregarRotasImpressaoNFE"
				, {idRoteiro: $('#idRoteiro', impressaoNfeController.workspace).val()}
		, function(data) {

			var tipoMensagem = data.tipoMensagem;
			var listaMensagens = data.listaMensagens;

			if (tipoMensagem && listaMensagens) {
				exibirMensagemDialog(tipoMensagem, listaMensagens, "");
			}

			$('#idRota').find('option').remove().end();

			$("#idRota").append($("<option />").val(-1).text("Selecione..."));
			$.each(data, function(index, item) {
				$("#idRota").append($("<option />").val(item["key"]["$"]).text(item["value"]["$"]));
			});
		}
		);
	},

	executarPreProcessamento : function(resultado) {

		if (resultado.mensagens) {

			exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
			);

			$(".grids", impressaoNfeController.workspace).hide();

			return resultado;
		}

		$.each(resultado.rows, function() {

			for(i=0; i < impressaoNfeController.filtroProdutos.length; i++) {
				if(impressaoNfeController.filtroProdutos[i]['cell']['codigoProduto'] == this.cell.codigoProduto) {
					checked = true;
				}
			}

			if(this.cell.notaImpressa)
				this.cell.notaImpressa = '<img src="'+ contextPath +'/images/ico_check.gif" alt="Nota Impressa" title="Nota Impressa" id="imgNotaImpress_'+ this.cell.idCota +'" />';
			else
				this.cell.notaImpressa = '';

			this.cell.sel = '<input type="checkbox" name="imprimirNFe" id="imprimirNFe_'+ this.cell.idCota +'" value="'+ this.cell.idCota + '" onclick="impressaoNfeController.adicionarAsNFesAImprimir(this.checked, '+ this.cell.idCota +')" />';
		});

		$(".grids", impressaoNfeController.workspace).show();

		return resultado;
	},

	/**
	 * Imprime o resultado da pesquisa no formato de arquivo parametrizado
	 * 
	 * @param fileType - tipo do arquivo para impressão
	 */
	imprimir : function(fileType) {

		var params = {"fileType":fileType};

		$.fileDownload(this.path + 'exportar', {
			httpMethod : "POST",
			data : params
		});
	},
	
	/**
	 * Marca todas as cotas do grid
	 */
	checkTodasAsCotas : function() {

		var inputs = $('.impressaoGrid :checkbox', impressaoNfeController.workspace);

		var values = {};
		inputs.each(function(index) {
			if(this.id != "selTodasAsCotas") {

				if($("#selTodasAsCotas").is(':checked'))
					this.checked = true;
				else
					this.checked = false;

			}
		});
	},

	/**
	 * Marca todos os fornecedores do grid
	 */
	checkTodosFornecedores : function() {

		var inputs = $('#menuFornecedores :checkbox', impressaoNfeController.workspace);

		var values = {};
		inputs.each(function(index) {
			if(this.id != "selecionarTodosFornecedores") {

				if($("#selecionarTodosFornecedores").is(':checked'))
					this.checked = true;
				else
					this.checked = false;

			}
		});
	},

	/**
	 * Marca todos os produtos do grid
	 */
	checkTodosProdutos : function(checked) {

		$('.produtosPesqGrid :input').each(function(k, v) {
			v.checked = checked;
			impressaoNfeController.adicionarAosProdutosFiltrados(checked, v.value, v.alt);
		});

		var data = {
				page:1,
				total: impressaoNfeController.filtroProdutos.length,
				rows: impressaoNfeController.filtroProdutos
		}

		impressaoNfeController.atualizarProdutosFiltrados( data );

	},
	
	filtrarProdutos : function(codigoProduto, nomeProduto) {
		params = [ 	{name:'codigoProduto', value:$('#dialog-pesqProdutos-codigoProduto').val()},
		           	{name:'nomeProduto', value:$('#dialog-pesqProdutos-nomeProduto').val()},
		           	]
		$(".produtosPesqGrid").flexOptions({params: params}).flexReload();
	},

	adicionarAosProdutosFiltradosERecarregar : function(check, codigoProduto, nomeProduto) {

		impressaoNfeController.atualizarProdutosFiltrados( impressaoNfeController.adicionarAosProdutosFiltrados(check, codigoProduto, nomeProduto) )

	},

	adicionarAosProdutosFiltrados : function(check, codigoProduto, nomeProduto) {

		if(check) {
			impressaoNfeController.filtroProdutos.push({'cell' : {
				'codigoProduto' : codigoProduto,
				'nomeProduto' : nomeProduto
			}
			});
		} else {
			for(i=0; i< impressaoNfeController.filtroProdutos.length; i++) {
				if(impressaoNfeController.filtroProdutos[i]['cell']['codigoProduto'] == codigoProduto) {
					impressaoNfeController.filtroProdutos.splice(i, 1);
				}
			}
		}

		var data = {
				page:1,
				total: impressaoNfeController.filtroProdutos.length,
				rows: impressaoNfeController.filtroProdutos
		}

		return data;
	},

	removerDosProdutosFiltrado : function(codigoProduto) {

		for(i=0; i< impressaoNfeController.filtroProdutos.length; i++) {
			if(impressaoNfeController.filtroProdutos[i]['cell']['codigoProduto'] == codigoProduto) {
				impressaoNfeController.filtroProdutos.splice(i, 1);
			}
		}

		var data = {
				page:1,
				total: impressaoNfeController.filtroProdutos.length,
				rows: impressaoNfeController.filtroProdutos
		}

		var inputs = $(".produtosPesqGrid :checkbox");
		var values = [];
		inputs.each(function(index) {
			if(this.value == codigoProduto) {	 
				this.checked = false;
			}
		});

		impressaoNfeController.atualizarProdutosFiltrados( data );

	},

	atualizarProdutosFiltrados : function(data) {
		$(".produtosAdicionadosPesqGrid").flexAddData(data);
		$(".produtosAdicionadosPesqGrid").flexReload();
	},

	adicionarAsNFesAImprimir : function() {

	},
	
	sortGrid : function(table, order) { 
		console.log(order);
		// Remove all characters in c from s. 
		var stripChar = function(s, c) { 
			var r = ""; 
			for(var i = 0; i < s.length; i++) { 
				r += c.indexOf(s.charAt(i))>=0 ? "" : s.charAt(i); 
			} 
			return r; 
		} 

		// Test for characters accepted in numeric values. 
		var isNumeric = function(s) { 
			var valid = "0123456789.,- "; 
			var result = true; 
			var c; 
			for(var i = 0; i < s.length && result; i++) { 
				c= s.charAt(i); 
				if(valid.indexOf(c) <= -1) { 
					result = false; 
				} 
			} 
			return result; 
		} 

		// Sort table rows. 
		var asc = order == "asc"; 
		console.log($(table));
		var rows = $(table).find("tbody > tr").get(); 
		var column = $(table).parent(".bDiv").siblings(".hDiv").find("table tr th").index($("th.sorted", ".flexigrid:has(" + table + ")"));
		rows.sort(function(a, b) { 
			var keyA = $(asc? a : b).children("td").eq(column).text().toUpperCase(); 
			var keyB = $(asc? b : a).children("td").eq(column).text().toUpperCase(); 
			if((isNumeric(keyA)||keyA.length<1) && (isNumeric(keyB) || keyB.length<1)) { 
				keyA = stripChar(keyA,", "); 
				keyB = stripChar(keyB,", "); 
				if(keyA.length < 1) keyA = 0; 
				if(keyB.length < 1) keyB = 0; 
				keyA = new Number(parseFloat(keyA)); 
				keyB = new Number(parseFloat(keyB)); 
			} 
			return keyA>keyB ? 1 : keyA<keyB ? -1 : 0; 
		}); 

		// Rebuild the table body. 
		$.each(rows, function(index, row) { 
			$(table).children("tbody").append(row); 
		}); 

		// Fix styles 
		$(table).find("tr").removeClass("erow");  // Clear the striping. 
		$(table).find("tr:odd").addClass("erow"); // Add striping to odd numbered rows.
		$(table).find("td.sorted").removeClass("sorted"); // Clear sorted class from table cells. 
		$(table).find("tr").each( function(){ 
			$(this).find("td:nth(" + column + ")").addClass("sorted");  //Add sorted class to sorted column cells. 
		}); 
	},


}, BaseController);
//@ sourceURL=impressaoNfe.js