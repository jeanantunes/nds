var histogramaPosEstudoController = $.extend(true, {
init:function(){
	
	$('#botaoVoltarMatrizDistribuicao').click(function(){
		$('#matrizDistribuicaoContent').show();
		$('#histogramaPosEstudoContent').hide();
	});
	
	$(".baseSugeridaGrid").flexigrid({
		dataType : 'json',
		colModel : [ {
			display : 'Código',
			name : 'codigo',
			width : 60,
			sortable : true,
			align : 'left'
		},{
			display : 'Produto',
			name : 'produto',
			width : 95,
			sortable : true,
			align : 'left'
		}, {
			display : 'Edição',
			name : 'edicao',
			width : 50,
			sortable : true,
			align : 'left'
		}, {
			display : 'Peso',
			name : 'peso',
			width : 25,
			sortable : true,
			align : 'right'
		}],
		width : 300,
		height : 180
	});
	$(".baseEstudoGrid").flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			},{
				display : 'Produto',
				name : 'produto',
				width : 95,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Peso',
				name : 'peso',
				width : 25,
				sortable : true,
				align : 'right'
			}],
			width : 300,
			height : 180
		});
	$(".faixasReparteGrid").flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Faixa de Reparte Dê',
				name : 'faixaReparteDe',
				width : 130,
				sortable : true,
				align : 'left'
			},{
				display : 'Faixa de Reparte Até',
				name : 'faixaReparteAte',
				width : 130,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			width : 350,
			height : 180
		});
		
	$(".estudosAnaliseGrid").flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Faixa de Reparte',
				name : 'faixaReparte',
				width : 170,
				sortable : true,
				align : 'left'
			}, {
				display : 'Rep. Total',
				name : 'repTotal',
				width : 65,
				sortable : true,
				align : 'right'
			}, {
				display : 'Rep. Médio',
				name : 'repMedio',
				width : 65,
				sortable : true,
				align : 'right'
			}, {
				display : 'Vda Nominal',
				name : 'vdaTotal',
				width : 65,
				sortable : true,
				align : 'right'
			}, {
				display : 'Vda Média',
				name : 'vdaMedio',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : '% Vda',
				name : 'percVenda',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Enc. Médio',
				name : 'EncalheMedio',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. Reparte',
				name : 'partReparte',
				width : 65,
				sortable : true,
				align : 'right'
			}, {
				display : 'Qtde. Cotas',
				name : 'qtdeCotas',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Rep Menor Vda',
				name : 'reparteMenorVenda',
				width : 80,
				sortable : true,
				align : 'right'
			}],
			sortname : "faixaReparte",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 150
		});
	},

	popularFieldsetHistogramaPreAnalise : function popularFieldsetHistogramaPreAnalise(selecionado){
		
		url = contextPath + "/distribuicao/histogramaPosEstudo/carregarDadosFieldsetHistogramaPreAnalise";
		
		$.postJSON(
			 url,
			 selecionado,
			 function onSucessCallBack(jsonData){
				 if (jsonData) {
					 $('#codigoProdutoFs').html(jsonData.codigoProduto);
					 $('#nomeProdutoFs').html(jsonData.nomeProduto);
					 $('#edicaoProdutoFs').html(jsonData.edicao);
					 $('#classificacaoProdutoFs').html(jsonData.classificacao);
					 $('#segmentoFs').html(jsonData.tipoSegmentoProduto.descricao);
					 $('#codigoEstudoFs').html(jsonData.estudo);
					 $('#periodoFs').html(jsonData.periodicidadeProduto);

					 if (jsonData.estudoLiberado) {
						 $('#estudoLiberadoFs').show();
						 $('#estudoLiberadoFs').attr('src', "images/ico_check.gif");
					 }else {
						 $('#estudoLiberadoFs').hide();
					}
				 }
			 }
		);
	}
		



}, BaseController);
//@ sourceURL=histogramaPosEstudo.js