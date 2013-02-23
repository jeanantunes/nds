var informacoesProdutoController = $.extend(true, {

init : function() {

$(".produtosInfosGrid").flexigrid({
		preProcess : informacoesProdutoController.executarPreProcessProdutosInfosGrid,
		dataType : 'json',
		colModel : [{
			display : 'Edição',
			name : 'numeroEdicao',
			width : 40,
			sortable : true,
			align : 'left'
		},
		{
			display : 'Nome',
			name : 'nomeProduto',
			width : 40,
			sortable : true,
			align : 'center'
		},{
			display : 'Período',
			name : 'periodo',
			width : 40,
			sortable : true,
			align : 'center'
		},{
			display : 'Preço R$',
			name : 'preco',
			width : 50,
			sortable : true,
			align : 'right'
		},{
			display : 'Status',
			name : 'status',
			width : 70,
			sortable : true,
			align : 'left'
		},{
			display : 'Rep.',
			name : 'reparteDistribuido',
			width : 25,
			sortable : true,
			align : 'center'
		},{
			display : 'Vda',
			name : 'venda',
			width : 25,
			sortable : true,
			align : 'center'
		},{
			display : 'Abrang.',
			name : 'percentualAbrangencia',
			width : 40,
			sortable : true,
			align : 'center'
		},{
			display : 'Data Lcto',
			name : 'dataLcto',
			width : 50,
			sortable : true,
			align : 'center'
		},{
			display : 'Data Rclto',
			name : 'dataRcto',
			width : 50,
			sortable : true,
			align : 'center'
		},{
			display : 'Tipo Distrib.',
			name : 'tipoDistribuicaoGhoma',
			width : 60,
			sortable : true,
			align : 'left'
		},{
			display : 'Algoritmo',
			name : 'algoritmo',
			width : 50,
			sortable : true,
			align : 'left'
		},{
			display : 'Rep. Min.',
			name : 'reparteMinimoGhoma',
			width : 45,
			sortable : true,
			align : 'center'
		},{
			display : 'Estudo',
			name : 'estudo',
			width : 40,
			sortable : true,
			align : 'left'
		},{
			display : 'Usuário',
			name : 'nomeUsuario',
			width : 50,
			sortable : true,
			align : 'left'
		},{
			display : 'Data',
			name : 'dataInser',
			width : 54,
			sortable : true,
			align : 'center'
		},{
			display : 'Hora',
			name : 'hora',
			width : 25,
			sortable : true,
			align : 'center'
		},{
			display : 'Ação',
			name : 'acao',
			width : 22,
			sortable : true,
			align : 'center'
		}],
		sortname : "edicao",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 1020,
		height : 255
	});	
	
$(".editorBaseApuradaGrid").flexigrid({
		preProcess : informacoesProdutoController.executarPreProcessEditorBaseApuradaGrid,
		dataType : 'json',
		colModel : [ {
			display : 'Cód.',
			name : 'codProduto',
			width : 30,
			sortable : true,
			align : 'left'
		}, {
			display : 'Produto',
			name : 'nomeProduto',
			width : 80,
			sortable : true,
			align : 'left'
		}, {
			display : 'Edição',
			name : 'numeroEdicao',
			width : 40,
			sortable : true,
			align : 'left'
		}, {
			display : 'Peso',
			name : 'peso',
			width : 30,
			sortable : true,
			align : 'left'
		}],
		width : 250,
		height : 140
	});

$(".editorBaseGrid").flexigrid({
		preProcess : informacoesProdutoController.executarPreProcessEditorBaseGrid,
		dataType : 'json',
		colModel : [ {
			display : 'Cód.',
			name : 'codProduto',
			width : 30,
			sortable : true,
			align : 'left'
		}, {
			display : 'Produto',
			name : 'nomeProduto',
			width : 80,
			sortable : true,
			align : 'left'
		}, {
			display : 'Edição',
			name : 'numeroEdicao',
			width : 40,
			sortable : true,
			align : 'left'
		}, {
			display : 'Peso',
			name : 'peso',
			width : 30,
			sortable : true,
			align : 'left'
		}],
		width : 250,
		height : 140
	});

$(".itensRegioesEspecificasGrid").flexigrid({
		//url : '../xml/itensRegioesEspecificas-xml.xml',
		//dataType : 'xml',
		colModel : [ {
			display : 'Nome Item',
			name : 'nomeItem',
			width : 150,
			sortable : true,
			align : 'left'
		},{
			display : 'Qtde',
			name : 'qtde',
			width : 25,
			sortable : true,
			align : 'center'
		},{
			display : '%',
			name : 'perc',
			width : 20,
			sortable : true,
			align : 'right'
		}],
		width : 250,
		height : 80
	});

//informacoesProdutoController.carregarProdutosCadastrados();

},


//PREPROCESS	

	// Preprocess do faixaGrid
	executarPreProcessProdutosInfosGrid : function(resultado){
		
		if (resultado.mensagens) {
			exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
			);
			
			return resultado;
		}
	
		$.each(resultado.rows, function(index, row) {
			
			var detalhes = '<a href="javascript:;" onclick="informacoesProdutoController.pop_detalhes('+"'"+row.cell.codProduto+"'"+','+row.cell.numeroEdicao+');" style="cursor:pointer">' +
						'<img src="' + contextPath + '/images/ico_detalhes.png" hspace="5" border="0" />'+
		   	 			'</a>';
			
			row.cell.acao = detalhes;
			
			
			var capa = '<a href="javascript:;" onmouseover="informacoesProdutoController.popUpCapaOpen('+"'"+row.cell.codProduto+"'"+','+row.cell.numeroEdicao+', event);" onmouseout="informacoesProdutoController.popUpCapaClose(event);" style="cursor:pointer">'+row.cell.numeroEdicao+'</a>'; 
//						<img onmouseover="historicoVendaController.popUpCapaOpen("",2410, event);" onmouseout="historicoVendaController.popUpCapaClose(event);" style="cursor:pointer" src="images/ico_detalhes.png">
			
			row.cell.numeroEdicao = capa;
			
			
		});
		
		$(".grids", informacoesProdutoController.workspace).show();
		return resultado;
	},



	executarPreProcessEditorBaseApuradaGrid : function(resultado){
		
		if (resultado.mensagens) {
			exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
			);
			
			return resultado;
		}
	
		$(".grids", informacoesProdutoController.workspace).show();
		return resultado;
	},


	executarPreProcessEditorBaseGrid : function(resultado){
		
		if (resultado.mensagens) {
			exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
			);
			
			return resultado;
		}
	
		$(".grids", informacoesProdutoController.workspace).show();
		return resultado;
	},

//	carregarProdutosCadastrados : function() {
//		
//		$(".produtosInfosGrid", this.workspace).flexOptions({
//			url: contextPath + "/distribuicao/informacoesProduto/buscarProduto",
//			dataType : 'json'
//		});
//		$(".produtosInfosGrid", this.workspace).flexReload();		
//	},

	filtroPrincipal : function(){
		var codigo = $("#idCodigo").val();
//		var nomeProduto = $("# ").val();
//		var classificacao = $("# ").val();
		
		$(".produtosInfosGrid", this.workspace).flexOptions({
			url: contextPath + "/distribuicao/informacoesProduto/buscarProduto",
			dataType : 'json',
			params:[{
				name : 'filtro.codProduto', value:codigo
			}]
		});
			
		$(".produtosInfosGrid", this.workspace).flexReload();		
	},
	
	pop_detalhes : function (codProd, numeroEdicao){
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		
	
		$( "#dialog-detalhe" ).dialog({
			resizable: false,
			height:570,
			width:950,
			modal: true,
			open: informacoesProdutoController.baseSugerida(codProd),
			open: informacoesProdutoController.baseEstudo(codProd),
			open: informacoesProdutoController.caracteristicasProduto(codProd, numeroEdicao),
			open: informacoesProdutoController.openDetalhe(codProd, numeroEdicao),
			
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					  $("#imagemCapaDetalhe")
				      .attr("src",contextPath + "/images/no_image.jpeg");
				},
			}
		});
	},
	
	baseSugerida : function(codProd){
		
		$(".editorBaseGrid").flexOptions({
			url: contextPath + "/distribuicao/informacoesProduto/buscarBaseSugerida",
			dataType : 'json',
			params:[{
				name : 'codProd', value:codProd
			}]
		});
		$(".editorBaseGrid").flexReload();		
	},
	
	baseEstudo : function(codProd){
		
		$(".editorBaseApuradaGrid").flexOptions({
			url: contextPath + "/distribuicao/informacoesProduto/buscarBaseEstudo",
			dataType : 'json',
			params:[{
				name : 'codProd', value:codProd
			}]
		});
		$(".editorBaseApuradaGrid").flexReload();		
	},
	
	caracteristicasProduto : function (codProd, numeroEdicao){
		
		$.postJSON(contextPath + "/distribuicao/informacoesProduto/buscarCaracteristicaProduto",
				{
				"codProd":codProd,
				"numEdicao":numeroEdicao
				},
				function(result) {
					informacoesProdutoController.caracteristicasProd(result);
				});
		
	},
	
	/*
	 * pesquisarPorCodigoProduto : function(idCodigo, idProduto, isFromModal, successCallBack, errorCallBack) {
		var codigoProduto = $(idCodigo,this.workspace).val();

		codigoProduto = $.trim(codigoProduto);

		$(idCodigo,this.workspace).val(codigoProduto);

		$(idProduto,this.workspace).val("");

		if (codigoProduto && codigoProduto.length > 0) {

			$.postJSON(contextPath + "/produto/pesquisarPorCodigoProduto",
					{codigoProduto:codigoProduto},
					function(result) { vendaProdutoController.pesquisarPorCodigoSuccessCallBack(result, idProduto, successCallBack); },
					function() { vendaProdutoController.pesquisarPorCodigoErrorCallBack(idCodigo, errorCallBack); }, isFromModal);

		} else {

			if (errorCallBack) {
				errorCallBack();
			}
		}
	},
	 */
	
	caracteristicasProd : function(result){
		
		$("#precoCapa").val(result.precoVenda).disable();;
		$("#pctPadrao").val(result.pacotePadrao).disable();;
		$("#chamadaCapa").val(result.chamadaCapa).disable();;
		$("#boletimInfor").val(result.boletimInformativo).disable();;
		$("#nomeComercial").val(result.nomeComercial).disable();;
		
		
		/*
		 * 
		$(".editorBaseApuradaGrid").flexOptions({
			url: contextPath + "/distribuicao/informacoesProduto/buscarCaracteristicas",
			dataType : 'json',
			params:[{
				name : 'codProd', value:codProd
			}]
		});
		
		$("#chamadaCapa").val('listaCaracteristicas.chamadaCapa');
		 */
	},
	
	pop_capa : function(){
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-capa" ).dialog({
			resizable: false,
			height:'auto',
			width:'auto',
			modal: false
		});
	},

	  
	  popUpCapaOpen : function popUpCapaOpen(codigoProduto, numeroEdicao, event) {
		  //$( "#dialog:ui-dialog" ).dialog( "destroy" );
		 
		  produto = {
		    codigoProduto : codigoProduto,
		    numeroEdicao : numeroEdicao
		  },
		  
		  $( "#dialog-detalhes" ).dialog({
		   resizable: false,
		   height:'auto',
		   width:'auto',
		   modal: false,
		   open: informacoesProdutoController.open(event, produto),
		   close : function(){
		    $( "#dialog-detalhes" ).dialog( "close" );
		   },
		   position: { my: "left", at: "right", of: event.target }
		  });
		  
		 },
		 
		 
		 open : function(event,produto) {
		 
		    var randomnumber=Math.floor(Math.random()*11);
		    
		    $("#imagemCapaEdicao")
		      .attr("src",contextPath
		          + "/capa/getCapaEdicaoJson?random="+randomnumber+"&codigoProduto="
		          + produto.codigoProduto
		          + "&numeroEdicao="
		          + produto.numeroEdicao);
		    console.log($("#imagemCapaEdicao").attr("src"));
		   },
		   
		   	   
		   popUpCapaClose : function popUpCapaClose() {
			   $( "#dialog-detalhes" ).dialog( "close" );
		   },
		   
		   
		   openDetalhe : function(codProd, numeroEd) {
				 
			    var randomnumber=Math.floor(Math.random()*11);
			    
			    $("#imagemCapaDetalhe")
			      .attr("src",contextPath
			          + "/capa/getCapaEdicaoJson?random="+randomnumber+"&codigoProduto="
			          + codProd
			          + "&numeroEdicao="
			          + numeroEd);
			   },
		   
		   
	  /*
	   * 
$(function() {
		$( "#tabsRanking" ).tabs();
	}
),
	   */
}, BaseController);
//@ sourceURL=informacoesProduto.js

