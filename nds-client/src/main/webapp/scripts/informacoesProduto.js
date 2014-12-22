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
			width : 90,
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
			display : 'Componente',
			name : 'componente',
			width : 150,
			sortable : true,
			align : 'left'
		},{
			display : 'Nome Item',
			name : 'nomeItem',
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
		width : 400,
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
			
			row.cell.periodo = (row.cell.periodo) ? row.cell.periodo : "";
			
			row.cell.tipoClassificacaoProdutoDescricao = (row.cell.tipoClassificacaoProdutoDescricao) ? row.cell.tipoClassificacaoProdutoDescricao : "";
			
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

		if(typeof matrizDistribuicao != 'undefined') {
			
			switch (matrizDistribuicao.tabSomarCopiarEstudos) {
			case 'somar':
				$('#workspace').tabs("remove", $('#workspace').tabs('option', 'selected'));
				$('#somarEstudo-estudoPesquisa').val(numeroEstudo).change();
				return;
			case 'copiar':
				$('#workspace').tabs("remove", $('#workspace').tabs('option', 'selected'));
				$('#copiarEstudo-estudoPesquisa').val(numeroEstudo).change();
				return;
			case 'complementar':
				$('#workspace').tabs("remove", $('#workspace').tabs('option', 'selected'));
				$('#codigoEstudo').val(numeroEstudo).blur();
				return;
			}
		}

        var matriz = [],
            url = contextPath + "/distribuicao/analiseEstudo/obterMatrizDistribuicaoPorEstudo",
            dadosResumo = {};

        $.postJSON(url,
            [{name : "id" , value : numeroEstudo}],
            function(response){
                // CALLBACK
                // ONSUCESS
                matriz.push({name: "selecionado.classificacao",  value: response.classificacao});
                matriz.push({name: "selecionado.nomeProduto",    value: response.nomeProduto});
                matriz.push({name: "selecionado.codigoProduto",  value: response.codigoProduto});
                matriz.push({name: "selecionado.dataLcto",       value: response.dataLancto});
                matriz.push({name: "selecionado.edicao",         value: response.numeroEdicao});
                matriz.push({name: "selecionado.estudo",         value: response.idEstudo});
                matriz.push({name: "selecionado.idLancamento",   value: response.idLancamento});
                matriz.push({name: "selecionado.estudoLiberado", value: (response.liberado != "")});

                $('#workspace').tabs({load : function(event, ui) {

                    histogramaPosEstudoController.dadosResumo = dadosResumo;
                    histogramaPosEstudoController.matrizSelecionado = matriz;
                    histogramaPosEstudoController.popularFieldsetHistogramaPreAnalise(matriz);

                    $('#workspace').tabs({load : function(event, ui) {}});
                }});

                var parametros = '?codigoProduto='+ response.codigoProduto +'&edicao='+ response.numeroEdicao;
                $('#workspace').tabs('addTab', 'Histograma Pré Análise', contextPath + '/matrizDistribuicao/histogramaPosEstudo' + parametros);
            }
        );

		//$('#workspace').tabs("remove", $('#workspace').tabs('option', 'selected'));
	},
	
	
	validarCamposVaziosGrid : function (row){
		
		if(row.cell.percentualAbrangencia == undefined){
			row.cell.percentualAbrangencia = "";
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
			open: informacoesProdutoController.openDetalhe(codProd, numeroEdicao), //CAPA
//			open: informacoesProdutoController.detalhes_ReparteDistribuido(codProd),
			open: informacoesProdutoController.detalhes_estudo(estudo,codProd, numeroEdicao),
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
		
		var precoCapa = "#inf-produto-precoCapa";
		informacoesProdutoController.validarCamposVazios(result.precoVenda, precoCapa);
		$(precoCapa).val(floatToPrice($(precoCapa).val()));
		
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
	
	detalhes_estudo : function (estudo,codProd, numeroEdicao){
		
		$.postJSON(contextPath + "/distribuicao/informacoesProduto/buscarReparteSobra",
				{
			"idEstudo":estudo,
			"codigoProduto":codProd,
			"numeroEdicao":numeroEdicao
				},
				function(result) {
					informacoesProdutoController.dadosReparteSobra(result);
				});
	},
	
	dadosReparteSobra : function(result){
		
		var sgAbrang = "#sugeridoAbrang";
		informacoesProdutoController.validarCamposVazios(result.abrangenciaSugerida, sgAbrang);
		informacoesProdutoController.formatarCasasDecimais(result.abrangenciaSugerida, sgAbrang);
		
		var apAbrang = "#apuradaAbrang"; 
		informacoesProdutoController.validarCamposVazios(result.abrangenciaEstudo, apAbrang);
		informacoesProdutoController.formatarCasasDecimais(result.abrangenciaEstudo, apAbrang);
		
		var sgMin = "#sugeridoMinimo";
		informacoesProdutoController.validarCamposVazios(result.qtdReparteMinimoSugerido, sgMin);
		informacoesProdutoController.formatarCasasDecimais(result.qtdReparteMinimoSugerido, sgMin);
		
		var estudoMin = "#estudoMinimo";
		informacoesProdutoController.validarCamposVazios(result.qtdReparteMinimoEstudo, estudoMin);
		informacoesProdutoController.formatarCasasDecimais(result.qtdReparteMinimoEstudo, estudoMin);
		
		var repTotal = "#reparteTotal";
		informacoesProdutoController.validarCamposVazios(result.qtdReparteDistribuidor, repTotal);
		informacoesProdutoController.formatarCasasDecimais(result.qtdReparteDistribuidor, repTotal);
		
		var repProm = "#repartePromocional";
		informacoesProdutoController.validarCamposVazios(result.qtdRepartePromocional, repProm);
		informacoesProdutoController.formatarCasasDecimais(result.qtdRepartePromocional, repProm);
		
		var sobra = "#sobra";
		informacoesProdutoController.validarCamposVazios(result.qtdSobraEstudo, sobra);
		informacoesProdutoController.formatarCasasDecimais(result.qtdSobraEstudo, sobra);
		
		var repDist = "#reparteDistribuido";
		informacoesProdutoController.validarCamposVazios(result.qtdReparteDistribuidoEstudo, repDist);
		informacoesProdutoController.formatarCasasDecimais(result.qtdReparteDistribuidoEstudo, repDist);

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