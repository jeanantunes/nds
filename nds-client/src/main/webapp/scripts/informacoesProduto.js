var informacoesProdutoController = $.extend(true, {

init : function() {
	
	var T = this;
	
	$("#apuradaAbrang").mask("99.9");
	
$(".produtosInfosGrid").flexigrid({
		preProcess : informacoesProdutoController.executarPreProcessProdutosInfosGrid,
		dataType : 'json',
		colModel : [{
			display : 'Codigo',
			name : 'codProduto',
			width : 55,
			sortable : true,
			align : 'center'
		},{
			display : 'Edição',
			name : 'numeroEdicao',
			width : 40,
			sortable : true,
			align : 'center'
		},{
			display : 'Nome',
			name : 'nomeProduto',
			width : 110,
			sortable : true,
			align : 'center'
		},{
			display : 'Classificação',
			name : 'tipoClassificacaoProdutoDescricao',
			width : 70,
			sortable : true,
			align : 'center'
		},{
			display : 'Período',
			name : 'periodo',
			width : 50,
			sortable : true,
			align : 'center'
		},{
			display : 'Preço R$',
			name : 'preco',
			width : 50,
			sortable : true,
			align : 'center'
		},{
			display : 'Status',
			name : 'status',
			width : 70,
			sortable : true,
			align : 'center'
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
			width : 55,
			sortable : true,
			align : 'center'
		},{
			display : 'Data Rclto',
			name : 'dataRcto',
			width : 55,
			sortable : true,
			align : 'center'
		},{
			display : 'Algoritmo',
			name : 'algoritmo',
			width : 50,
			sortable : true,
			align : 'center'
		},{
			display : 'Rep. Min.',
			name : 'reparteMinimo',
			width : 45,
			sortable : true,
			align : 'center'
		},{
			display : 'Estudo',
			name : 'estudo' ,
			width : 60,
			sortable : true,
			align : 'center'
		},{
			display : 'Usuário',
			name : 'nomeUsuario',
			width : 50,
			sortable : true,
			align : 'center'
		},{
			display : 'Data',
			name : 'dataAlteracao',
			width : 55,
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
		width : 980,
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
			name : 'qtdReparteMin',
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
			
			var numeroEstudo = '<a href="javascript:;" onclick="informacoesProdutoController.recuperarNumeroEstudo(' + row.cell.estudo + ')">' + row.cell.estudo +'</a>';
			row.cell.estudo = numeroEstudo;
			
			//Validando campos vazios
			informacoesProdutoController.validarCamposVaziosGrid(row);
			
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
		
		if(informacoesProdutoController.targetRecuperarEstudo){
			
			$(informacoesProdutoController.targetRecuperarEstudo).val(numeroEstudo);
			if(informacoesProdutoController.methodEval){
				window.eval('$(informacoesProdutoController.targetRecuperarEstudo).'+informacoesProdutoController.methodEval+'();');
			}else{
				$(informacoesProdutoController.targetRecuperarEstudo).blur();
				
			}
		}else if ($(".pesquisaEstudo").val() != undefined) {
			
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
	
	
	validarCamposVaziosGrid : function (row){
		
		if(row.cell.reparteMinimo == 0){
		   row.cell.reparteMinimo = "";
		}

		if(row.cell.percentualAbrangencia == undefined){
			row.cell.percentualAbrangencia = "";
		}
		
		if(row.cell.venda == 0){
			row.cell.venda = "";
		}
		
		if(row.cell.reparteDistribuido == 0){
			row.cell.reparteDistribuido = "";
		}
		
		if(row.cell.status == undefined){
			row.cell.status = "";
		}
		
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
			open: informacoesProdutoController.itensRegiao(estudo),
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
	
	itensRegiao : function(estudo){
		
		$(".itensRegioesEspecificasGrid").flexOptions({
			url: contextPath + "/distribuicao/informacoesProduto/buscarItemRegiao",
			dataType : 'json',
			params:[{
				name : 'idEstudo', value:estudo
			}]
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
		
		var precoCapa = "#precoCapa";
		informacoesProdutoController.validarCamposVazios(result.precoVenda, precoCapa);
		informacoesProdutoController.formatarCasasDecimais(result.precoVenda, precoCapa);
		
		var pctPadrao = "#pctPadrao";
		informacoesProdutoController.validarCamposVazios(result.pacotePadrao, pctPadrao);
		informacoesProdutoController.formatarCasasDecimais(result.pacotePadrao, pctPadrao);
		
		var chamCapa = "#chamadaCapa";
		informacoesProdutoController.validarCamposVazios(result.chamadaCapa, chamCapa);
		
		var boletimInfo = "#boletimInfor";
		informacoesProdutoController.validarCamposVazios(result.boletimInformativo, boletimInfo);
		
		var nmComer = "#nomeComercial";
		informacoesProdutoController.validarCamposVazios(result.nomeComercial, nmComer);
		
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
		
		
		var sgAbrang = "#sugeridoAbrang";
		informacoesProdutoController.validarCamposVazios(result.abrangenciaSugerida, sgAbrang);
		informacoesProdutoController.formatarCasasDecimais(result.abrangenciaSugerida, sgAbrang);

		var apAbrang = "#apuradaAbrang"; 
		informacoesProdutoController.validarCamposVazios(result.abrangenciaApurada, apAbrang);
		informacoesProdutoController.formatarCasasDecimais(result.abrangenciaApurada, apAbrang);
		
		var sgMin = "#sugeridoMinimo";
		informacoesProdutoController.validarCamposVazios(result.minimoSugerido, sgMin);
		informacoesProdutoController.formatarCasasDecimais(result.minimoSugerido, sgMin);
		
		var estudoMin = "#estudoMinimo";
		informacoesProdutoController.validarCamposVazios(result.minimoEstudoId, estudoMin);
		informacoesProdutoController.formatarCasasDecimais(result.minimoEstudoId, estudoMin);
		

	},
	
	validarCamposVazios : function (valor, campo){
		if((valor==0) || (valor==undefined)){
			valor = "";
			$(campo).val(valor).disable();
		}else{
			$(campo).val(valor).disable();
		}
	},
	
	//Formata os números excluindo as casas decimais.
	formatarCasasDecimais : function (valor, campo){
		if((valor>0) || (valor!=undefined)){
			var valorFormatado = parseFloat(valor).toFixed(0); 
			$(campo).val(valorFormatado).disable();
		} 
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
		
		var repTotal = "#reparteTotal";
		informacoesProdutoController.validarCamposVazios(result.reparteTotal, repTotal);
		informacoesProdutoController.formatarCasasDecimais(result.reparteTotal, repTotal);
		
		var repProm = "#repartePromocional";
		informacoesProdutoController.validarCamposVazios(result.repartePromocional, repProm);
		informacoesProdutoController.formatarCasasDecimais(result.repartePromocional, repProm);
		
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
		
		var sobra = "#sobra";
		informacoesProdutoController.validarCamposVazios(result, sobra);
		informacoesProdutoController.formatarCasasDecimais(result, sobra);
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
		
		var repDist = "#reparteDistribuido";
		informacoesProdutoController.validarCamposVazios(result, repDist);
		informacoesProdutoController.formatarCasasDecimais(result, repDist);
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
		
		var venda = "#venda";
		informacoesProdutoController.validarCamposVazios(result.totalVenda, venda);
		informacoesProdutoController.formatarCasasDecimais(result.totalVenda, venda);
		
		var porcVenda = "#porcentagemVenda";
		informacoesProdutoController.validarCamposVazios(result.porcentagemDeVenda, porcVenda);
		informacoesProdutoController.formatarCasasDecimais(result.porcentagemDeVenda, porcVenda);
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