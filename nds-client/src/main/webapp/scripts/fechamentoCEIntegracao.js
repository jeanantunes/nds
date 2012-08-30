var fechamentoCEIntegracaoController = $.extend(true, {
	
	init : function(){
		fechamentoCEIntegracaoController.initGrid();
		fechamentoCEIntegracaoController.bindButtons();
	},
	
	bindButtons : function(){
		$("#btnPesquisar", fechamentoCEIntegracaoController.workspace).click(function() {
			fechamentoCEIntegracaoController.pesquisaPrincipal();
			$(".grids", fechamentoCEIntegracaoController.worspace).show();
		});
	},
	initGrid : function(){	
		$(".fechamentoCeGrid", fechamentoCEIntegracaoController.workspace).flexigrid({
			preProcess : function(resultado) {
				
				if (resultado.mensagens) {
					exibirMensagem(resultado.mensagens.tipoMensagem, resultado.mensagens.listaMensagens);
					$(".grids", fechamentoCEIntegracaoController.workspace).hide();
					return resultado;
				}
				
				$(".grids", fechamentoCEIntegracaoController.workspace).show();
				return resultado;
			  },
			dataType : 'json',
			colModel : [ {
				display : 'Seq',
				name : 'sequencial',
				width : 30,
				sortable : true,
				align : 'left'
			},  {
				display : 'Código',
				name : 'codigo',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 200,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Tipo',
				name : 'tipoFormatado',
				width : 50,
				sortable : true,
				align : 'center'
			},  {
				display : 'Reparte',
				name : 'reparte',
				width : 80,
				sortable : true,
				align : 'center'
			},  {
				display : 'Encalhe',
				name : 'encalhe',
				width : 80,
				sortable : true,
				align : 'center'
			},  {
				display : 'Venda',
				name : 'venda',
				width : 80,
				sortable : true,
				align : 'center'
			},  {
				display : 'Preço Capa R$',
				name : 'precoCapa',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Valor Venda R$',
				name : 'valorVendaFormatado',
				width : 80,
				sortable : true,
				align : 'right'
			}],
			sortname : "qtde",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});
		
	},
	
	pesquisaPrincipal : function(){
		var idFornecedor = $("#idFornecedor", fechamentoCEIntegracaoController.workspace).val();
		var semana = $("#semana", fechamentoCEIntegracaoController.workspace).val();
		$(".fechamentoCeGrid", fechamentoCEIntegracaoController.workspace).flexOptions({
			url: contextPath + '/devolucao/fechamentoCEIntegracao/pesquisaPrincipal',
			dataType : 'json',
			params: [
			         {name:'filtro.idFornecedor' , value:idFornecedor},
			         {name:'filtro.semana' , value:semana}
			         ]
		});
		
		$(".fechamentoCeGrid").flexReload();
		fechamentoCEIntegracaoController.popularTotal();
	},
	
	popularTotal : function(){
		alert("POPULAR TOTAL");
//		$.postJSON(
//				contextPath + '/devolucao/fechamentoCEIntegracao/buscarTotalGeralCota',
//				[{name:'filtro.idCota', value:$('#codigoCota', consultaConsignadoCotaController.workspace).val()},
//				{name:'filtro.idFornecedor', value:$('#idFornecedor', consultaConsignadoCotaController.workspace).val()}],
//				function(result) {
//					var idFornecedor = $('#idFornecedor', consultaConsignadoCotaController.workspace).val();				
//					if( idFornecedor != "0"){
//						$('.tabelaGeralDetalhado', consultaConsignadoCotaController.workspace).hide();
//						$('.tabelaGeralPorFornecedor', consultaConsignadoCotaController.workspace).show();
//						$("#totalGeralPorFornecedor", consultaConsignadoCotaController.workspace).html(" <table width='190' border='0' cellspacing='1' cellpadding='1' align='right'>" +
//								"<tr> <td style='border-top:1px solid #000;''><strong>Total Geral:</strong></td>" +
//	        						" <td style='border-top:1px solid #000;'>&nbsp;</td> "+
//	        						" <td style='border-top:1px solid #000;' align='right'><strong>"+result+"</strong></td></tr>");
//					}else{					
//						$("#totalGeralCota", consultaConsignadoCotaController.workspace).html(" <table width='190' border='0' cellspacing='1' cellpadding='1' align='right'>" +
//													"<tr> <td style='border-top:1px solid #000;''><strong>Total Geral:</strong></td>" +
//				                						" <td style='border-top:1px solid #000;'>&nbsp;</td> "+
//				                						" <td style='border-top:1px solid #000;' align='right'><strong>"+result+"</strong></td></tr>");					
//					} 
//					
//				},
//				null,
//				true
//			);
	}
	
	
	
}, BaseController);