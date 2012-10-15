var impressaoNfeController = $.extend(true, {
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
			height : 180
		});

		$(".impressaoGrid", impressaoNfeController.workspace).flexigrid({
			preProcess: null,
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
				name : 'notaImpressao',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : '',
				name : ' ',
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
			$("#dialog-pesqProdutos").dialog( "open" );
			//impressaoNfeController.exibirDialogPesquisarProdutos();
			return false;
		})

		$(".menu_produtos", impressaoNfeController.workspace).mouseleave(function() {
			$(".menu_produtos", impressaoNfeController.workspace).hide();
		});		
		
		$(".grids").show();
		
		this.criarDialogPesquisarProdutos();
	},
	
	criarDialogPesquisarProdutos : function() {		
		
		$( "#dialog-pesqProdutos", impressaoNfeController.workspace ).dialog({
			resizable: false,
			height:450,
			width:540,
			modal: true,
			autoOpen: false,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				},
			},
			form: $("#dialog-pesqProdutos", this.workspace).parents("div")
		});
	},
	
	prepararJSONPesquisaProdutos : function(data) {

		$.each(data.rows, function() {
			this.cell.sel = '<input type="checkbox" name="codigoProduto" id="codigoProduto_'+ this.cell.codigoProduto +'" value="'+ this.cell.codigoProduto +'" />';
        });
		return data;
	},
		
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
	    	if(this.id != "selecionarTodosFornecedores") {	 
	    		params.push({
					'name' : "filtro.idsFornecedores[]",
					'value' : this.value
				});
	    	}
	    });
	    
	    var inputs = $('#menuProdutos :checkbox');	    
	    inputs.each(function(index) {
	    	if(this.id != "selecionarTodosProdutos") {	 
	    		params.push({
					'name' : "filtro.codigosProdutos[]",
					'value' : this.value
				});
	    	}
	    });
			    
		$(".impressaoGrid", impressaoNfeController.workspace).flexOptions({
			preProcess: impressaoNfeController.executarPreProcessamento,
			url: contextPath + "/nfe/impressaoNFE/pesquisarImpressaoNFE",
			dataType : 'json',
			params: params
		});
		
		$(".impressaoGrid", impressaoNfeController.workspace).flexReload();
		
	},
	
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
	
	confirmar : function(){
		$(".dados", impressaoNfeController.workspace).show();
	},
	
	pesqEncalhe : function(){
		$(".dadosFiltro", impressaoNfeController.workspace).show();
	},
	
	mostrar_nfes : function() {
		$(".nfes", impressaoNfeController.workspace).show();
	},
	
	checkTodosFornecedores : function() {
		
		var inputs = $('#menuFornecedor :checkbox');

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
		
	checkTodosProdutos : function(checked) {
		$('.produtosPesqGrid :input', impressaoNfeController.workspace).each(function(k, v) {
			impressaoNfeController.marcarTodosProdutos(v, checked);
		});
		
	},
	
	marcarTodosProdutos : function(v, checked) {
		v.checked = checked;
	},
	
	filtrarProdutos : function(codigoProduto, nomeProduto) {
		params = [ 	{name:'codigoProduto', value:$('#dialog-pesqProdutos-codigoProduto', impressaoNfeController.workspace).val()},
		         	{name:'nomeProduto', value:$('#dialog-pesqProdutos-nomeProduto', impressaoNfeController.workspace).val()},
		         ]
		$(".produtosPesqGrid", impressaoNfeController.workspace).flexOptions({params: params}).flexReload();
	},

}, BaseController);
//@sourceURL=impressaoNfe.js