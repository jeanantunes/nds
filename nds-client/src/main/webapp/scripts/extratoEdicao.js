var jsExtratoEdicao = $.extend(true, {

	pesquisaProdutoExtratoEdicao : null, 
	
	init : function(pesquisa) {
		
		this.pesquisaProdutoExtratoEdicao = pesquisa;
		
		$("#edicao", jsExtratoEdicao.workspace).numeric();
		
		$("#idProdutoExtratoEdicao", jsExtratoEdicao.workspace).autocomplete({
			source: []
		});
		
		jsExtratoEdicao.carregarExtratoEdicaoGrid();
		
		$(".areaBts",jsExtratoEdicao.workspace).hide();
		
	},
	
	pesquisarExtratoEdicao : function() {
		
		var numeroEdicao = jQuery("#edicao", jsExtratoEdicao.workspace).val();
		var codigoProduto = jQuery("#codigo", jsExtratoEdicao.workspace).val();
		var nomeProduto = jQuery("#idProdutoExtratoEdicao", jsExtratoEdicao.workspace).val();
		var precoCapa = jQuery("#extrato-edicao-precoCapa", jsExtratoEdicao.workspace).val();
		var nomeFornecedor = jQuery("#nomeFornecedor", jsExtratoEdicao.workspace).val();
		
		$(".extratoEdicaoGrid", jsExtratoEdicao.workspace).flexOptions({
			url: contextPath + '/estoque/extratoEdicao/pesquisaExtratoEdicao',
			preProcess: jsExtratoEdicao.getDataFromResult,
			dataType : 'json',
			params:[
				{name:'filtro.codigo', value: codigoProduto},
				{name:'filtro.nome', value: nomeProduto},
				{name:'filtro.fornecedor', value: nomeFornecedor},
				{name:'numeroEdicao', value: numeroEdicao},
		        {name:'precoCapa', value: precoCapa}
			]
		});
		
		$(".extratoEdicaoGrid", jsExtratoEdicao.workspace).flexReload();
		
		$(".areaBts",jsExtratoEdicao.workspace).show();

	},	
	
	pesquisarNomeFornecedor : function() {
		
		var data = {codigo: $("#codigo", jsExtratoEdicao.workspace).val()};
		
		$.postJSON(contextPath + '/estoque/extratoEdicao/obterFornecedorDeProduto', data, 
				function(result){
			
			$("#nomeFornecedor", jsExtratoEdicao.workspace).val(result);
			
		});
		
	},
	
	
	pesquisarPrecoCapa : function() {
		
		var data = {'filtro.codigo': $("#codigo", jsExtratoEdicao.workspace).val(),
		  		  	edicao: $("#edicao", jsExtratoEdicao.workspace).val()};
		
		$.postJSON(contextPath + '/estoque/extratoEdicao/obterProdutoEdicao', data, function(result){
			$("#extrato-edicao-precoCapa", jsExtratoEdicao.workspace).val(floatToPrice(result));
		});
		
	},
		
	pesquisarProdutoPorCodigo : function() {

		$("#nomeFornecedor", jsExtratoEdicao.workspace).val("");
		$("#extrato-edicao-precoCapa", jsExtratoEdicao.workspace).val("");
		
		pesquisaProdutoExtratoEdicao.pesquisarPorCodigoProduto('#codigo', '#idProdutoExtratoEdicao', '#edicao', false, jsExtratoEdicao.pesquisarProdutoCallBack);	
	},

	pesquisarProdutoPorNome : function() {

		$("#nomeFornecedor", jsExtratoEdicao.workspace).val("");
		$("#extrato-edicao-precoCapa", jsExtratoEdicao.workspace).val("");
		
		pesquisaProdutoExtratoEdicao.pesquisarPorNomeProduto('#codigo', '#idProdutoExtratoEdicao', '#edicao', false, jsExtratoEdicao.pesquisarProdutoCallBack);
	},

	validarNumeroEdicao : function() {

		$("#extrato-edicao-precoCapa", jsExtratoEdicao.workspace).val("");
		
		pesquisaProdutoExtratoEdicao.validarNumEdicao('#codigo', '#edicao', false, jsExtratoEdicao.validarEdicaoCallBack);
	},
	
	validarEdicaoCallBack : function() {
		
		jsExtratoEdicao.pesquisarPrecoCapa();
			
	},
	
	pesquisarProdutoCallBack : function() {
		
		jsExtratoEdicao.pesquisarNomeFornecedor();
		
	},
		
	getDataFromResult : function(data) {
		
		jsExtratoEdicao.dadosPesquisa = {page: 0, total: 0};
		
		jsExtratoEdicao.saldoTotalExtratoEdicao = 0.0;
		
		var destacarValorSaldoTotal;
		
		if(typeof data.mensagens == "object") {
		
			$(".grids", jsExtratoEdicao.workspace).hide();
			
			exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
		
		} else {
			
			$.each(data, function(index, value) {
				
				  if(value[0] == "gridResult") {
					  
					  jsExtratoEdicao.dadosPesquisa = jsExtratoEdicao.destacarValorParcial(value[1]);
					  
				  } else if(value[0] == "saldoTotalExtratoEdicao") {
					  
					  jsExtratoEdicao.saldoTotalExtratoEdicao = value[1];
					  
				  } else if(value[0] == "destacarSaldoTotalExtratoEdicao") {
					  
					  destacarValorSaldoTotal = value[1];
					  
				  } 
				  
			});
			
			$(".grids", jsExtratoEdicao.workspace).show();
			
		}
		
		if(destacarValorSaldoTotal == "S") {
			$("#saldoTotalExtratoEdicao", jsExtratoEdicao.workspace).css("color", "red");
			$("#saldoTotalExtratoEdicao", jsExtratoEdicao.workspace).html(jsExtratoEdicao.saldoTotalExtratoEdicao); 
		} else {
			$("#saldoTotalExtratoEdicao", jsExtratoEdicao.workspace).css("color", "black");
			$("#saldoTotalExtratoEdicao", jsExtratoEdicao.workspace).html(jsExtratoEdicao.saldoTotalExtratoEdicao); 
		}
		
		
		return jsExtratoEdicao.dadosPesquisa;
				
	},
	
	destacarValorParcial : function(data) {
		
		$.each(data.rows, function(index, value) {
				
				var destacarValorNegativo = value.cell[5];
				
				var valorParcial =  value.cell[4];
				
				if(destacarValorNegativo == "S") {
					
					value.cell[4] = '<span style="color: red">'+valorParcial+'</span>';
					
				} else {
					
					value.cell[4] = '<span style="color: black">'+valorParcial+'</span>';
					
				}
					
				
				
		});
		  
		return data;
		
	},
	
	carregarExtratoEdicaoGrid: function() {
		
		$(".extratoEdicaoGrid", jsExtratoEdicao.workspace)
				.flexigrid(
						{	
							preProcess: jsExtratoEdicao.getDataFromResult,
							dataType : 'json',
							colModel : [ 
							    {
								display : 'Data',
								name : 'dataInclusao',
								width : 120,
								sortable : false,
								align : 'center'
							}, {
								display : 'Movimento',
								name : 'movimento',
								width : 370,
								sortable : false,
								align : 'left'
							}, {
								display : 'Entrada',
								name : 'entrada',
								width : 120,
								sortable : false,
								align : 'center'
							}, {
								display : 'Saída',
								name : 'saida',
								width : 120,
								sortable : false,
								align : 'center'
							}, {
								display : 'Parcial',
								name : 'parcial',
								width : 120,
								sortable : false,
								align : 'center'
							} ],

							showTableToggleBtn : true,
							width : 960,
							height : 260,
							sortname : "dataInclusao",
							sortorder : "asc",
							usepager : true,
							useRp : true,
							rp : 100000,
							showTableToggleBtn : true
						});

	}
	
}, BaseController);

//@ sourceURL=extratoEdicao.js
