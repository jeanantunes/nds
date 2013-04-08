var informacoesProdutoController = $.extend(true, {

init : function() {
	
	var T = this;
	
	$("#apuradaAbrang").mask("99.9");

$(".produtosInfosGrid").flexigrid({
		preProcess : informacoesProdutoController.executarPreProcessProdutosInfosGrid,
		dataType : 'json',
		colModel : [{
			display : 'Edição',
			name : 'numeroEdicao',
			width : 40,
			sortable : true,
			align : 'left'
		},{
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
			display : 'Algoritmo',
			name : 'algoritmo',
			width : 50,
			sortable : true,
			align : 'left'
		},{
			display : 'Rep. Min.',
			name : 'reparteMinimo',
			width : 45,
			sortable : true,
			align : 'center'
		},{
			display : 'Estudo',
			name : 'estudo' ,
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
			name : 'dataAlteracao',
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
			name : 'codigoProduto',
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
			name : 'codigoProduto',
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
	preProcess : informacoesProdutoController.executarPreProcessItemRegiao,
	dataType : 'json',
		colModel : [ {
			display : 'Nome Item',
			name : 'nomeItemRegiao',
			width : 150,
			sortable : true,
			align : 'left'
		},{
			display : 'Qtde',
			name : 'quantidade',
			width : 25,
			sortable : true,
			align : 'center'
		},{
			display : '%',
			name : 'bonificacao',
			width : 20,
			sortable : true,
			align : 'right'
		}],
		width : 250,
		height : 80
	});

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
			
			var detalhes = '<a href="javascript:;" onclick="informacoesProdutoController.pop_detalhes('+"'"+row.cell.codProduto+"'"+','+row.cell.numeroEdicao+','+row.cell.estudo+');" style="cursor:pointer">' +
						'<img src="' + contextPath + '/images/ico_detalhes.png" hspace="5" border="0" />'+
		   	 			'</a>';
			
			row.cell.acao = detalhes;
			
			
			var capa = '<a href="javascript:;" onmouseover="informacoesProdutoController.popUpCapaOpen('+"'"+row.cell.codProduto+"'"+','+row.cell.numeroEdicao+', event);" onmouseout="informacoesProdutoController.popUpCapaClose(event);" style="cursor:pointer">'+row.cell.numeroEdicao+'</a>'; 
			
			row.cell.numeroEdicao = capa;
			
			var numeroEstudo = '<a href="javascript:;" onclick="informacoesProdutoController.recuperarNumeroEstudo(' + row.cell.estudo + ')">' + row.cell.estudo +'</a>'
			row.cell.estudo = numeroEstudo;
			
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
	
	executarPreProcessItemRegiao : function(resultado){
		
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
	
	recuperarNumeroEstudo: function(numeroEstudo){
		
		if($("#codigoEstudo").val()==undefined && $(".pesquisaEstudo").val() == undefined){
			
			return;
		}
		
		if ($(".pesquisaEstudo").val() != undefined) {
			
			$(".pesquisaEstudo").trigger(jQuery.Event("change"));
			$(".pesquisaEstudo").val(numeroEstudo);
			$(".pesquisaEstudo").focus();
			
		}
		else {
			
			$("#codigoEstudo").val(numeroEstudo);
			$("#codigoEstudo").focus();
		}
		
		$('#workspace').tabs("remove", $('#workspace').tabs('option', 'selected'));
	},
	
	filtroPrincipal : function(){
		var codigo = $("#idCodigo").val();
		var nomeProduto = $("#nomeProduto").val();
		var classificacao = $("#comboClassificacao").val();
		
		$(".produtosInfosGrid", this.workspace).flexOptions({
			url: contextPath + "/distribuicao/informacoesProduto/buscarProduto",
			dataType : 'json',
			params:[
			        {name : 'filtro.codProduto', value:codigo},
			        {name : 'filtro.nomeProduto', value:nomeProduto},
			        {name : 'filtro.idTipoClassificacaoProd', value:classificacao}
			        ]
		});
			
		$(".produtosInfosGrid", this.workspace).flexReload();		
	},
	
	
	pop_detalhes : function (codProd, numeroEdicao, estudo){
	
		$( "#dialog-detalhe" ).dialog({
			resizable: false,
			height:570,
			width:950,
			modal: true,
			open: informacoesProdutoController.baseSugerida(estudo),
			open: informacoesProdutoController.itensRegiao(),
			open: informacoesProdutoController.baseEstudo(estudo),
			open: informacoesProdutoController.caracteristicasProduto(codProd, numeroEdicao),
			open: informacoesProdutoController.openDetalhe(codProd, numeroEdicao),
			open: informacoesProdutoController.detalhes_MinimoEAbrangencia(estudo, codProd, numeroEdicao),
			open: informacoesProdutoController.detalhes_ReparteTotalEPromocional(codProd, numeroEdicao),
			open: informacoesProdutoController.detalhes_ReparteDistribuido(codProd),
			open: informacoesProdutoController.detalhes_ReparteSobra(estudo),
			open: informacoesProdutoController.detalhes_venda(codProd, numeroEdicao),
			
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					  $("#imagemCapaDetalhe")
				      .attr("src",contextPath + "/images/no_image.jpeg");
				},
			}
		});
	},
	
	baseSugerida : function(estudo){
		
		$(".editorBaseGrid").flexOptions({
			url: contextPath + "/distribuicao/informacoesProduto/buscarBaseSugerida",
			dataType : 'json',
			params:[{
				name : 'idEstudo', value:estudo
			}]
		});
		$(".editorBaseGrid").flexReload();		
	},
	
	itensRegiao : function(codProd){
		
		$(".itensRegioesEspecificasGrid").flexOptions({
			url: contextPath + "/distribuicao/informacoesProduto/buscarItemRegiao",
			dataType : 'json',
		});
		$(".itensRegioesEspecificasGrid").flexReload();		
	},
	
	baseEstudo : function(estudo){
		
		$(".editorBaseApuradaGrid").flexOptions({
			url: contextPath + "/distribuicao/informacoesProduto/buscarBaseEstudo",
			dataType : 'json',
			params:[{
				name : 'idEstudo', value:estudo
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
	
	caracteristicasProd : function(result){
		$("#precoCapa").val(result.precoVenda).disable();;
		$("#pctPadrao").val(result.pacotePadrao).disable();;
		$("#chamadaCapa").val(result.chamadaCapa).disable();;
		$("#boletimInfor").val(result.boletimInformativo).disable();;
		$("#nomeComercial").val(result.nomeComercial).disable();;
		
	},
	
	detalhes_MinimoEAbrangencia : function (estudo, codProd, numeroEdicao){
		
		$.postJSON(contextPath + "/distribuicao/informacoesProduto/buscarAbrangenciaEMinimo",
				{
			"idEstudo":estudo,
			"codProduto":codProd,
			"numEdicao":numeroEdicao
				},
				function(result) {
					informacoesProdutoController.dadosMinimoEAbrangencia(result);
				});
		
	},
	
	dadosMinimoEAbrangencia : function(result){
		
		$("#sugeridoAbrang").val(result.abrangenciaSugerida).disable();
		$("#apuradaAbrang").val(result.abrangenciaApurada).disable();
		$("#sugeridoMinimo").val(result.minimoSugerido).disable();
		$("#estudoMinimo").val(result.minimoEstudoId).disable();
	},
	
	
	detalhes_ReparteTotalEPromocional : function (codProd, numeroEdicao){
		
		$.postJSON(contextPath + "/distribuicao/informacoesProduto/buscarRepartesTotalEPromocional",
				{
			"codProduto":codProd,
			"numEdicao":numeroEdicao
				},
				function(result) {
					informacoesProdutoController.dadosReparte(result);
				});
	},
	
	dadosReparte : function(result){
		
		$("#reparteTotal").val(result.reparteTotal).disable();
		$("#repartePromocional").val(result.repartePromocional).disable();
	},
	
	detalhes_ReparteSobra : function (estudo){
		
		$.postJSON(contextPath + "/distribuicao/informacoesProduto/buscarReparteSobra",
				{
			"idEstudo":estudo,
				},
				function(result) {
					informacoesProdutoController.dadosReparteSobra(result);
				});
	},
	
	dadosReparteSobra : function(result){
		
		$("#sobra").val(result.sobra).disable();
	},
	
	detalhes_ReparteDistribuido : function (codProd){
		
		$.postJSON(contextPath + "/distribuicao/informacoesProduto/buscarReparteDist",
				{
			"codProduto":codProd,
				},
				function(result) {
					informacoesProdutoController.dadosReparteDistribuido(result);
				});
	},
	
	dadosReparteDistribuido : function(result){
		
		$("#reparteDistribuido").val(result).disable();
	},
	
	detalhes_venda : function (codProd, numeroEdicao){
		
		$.postJSON(contextPath + "/distribuicao/informacoesProduto/buscarVendas",
				{
			"codProduto":codProd,
			"numEdicao":numeroEdicao
				},
				function(result) {
					informacoesProdutoController.dadosVendas(result);
				});
	},
	
	dadosVendas : function(result){
		
		$("#venda").val(result.totalVenda).disable();
		$("#porcentagemVenda").val(result.porcentagemDeVenda).disable();
	},
	
	pop_capa : function(){
	
		$( "#dialog-capa" ).dialog({
			resizable: false,
			height:'auto',
			width:'auto',
			modal: false
		});
	},

	  
	  popUpCapaOpen : function popUpCapaOpen(codigoProduto, numeroEdicao, event) {
		 
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
		   
}, BaseController);
//@ sourceURL=informacoesProduto.js