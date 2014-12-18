var relatorioVendasController = $.extend(true, {
	init : function() {
		
		$("#rel-vendas-numeroCota", relatorioVendasController.workspace).numeric();
		
		$("#datepickerDe", relatorioVendasController.workspace).datepicker({
			showOn : "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly : true,
			dateFormat: 'dd/mm/yy',
			defaultDate: new Date()
		});
		
		$("#datepickerDe", relatorioVendasController.workspace).mask("99/99/9999");
	
		$("#datepickerAte", relatorioVendasController.workspace).datepicker({
			showOn : "button",
			buttonImage: contextPath + "/images/calendar.gif",
			buttonImageOnly : true,
			dateFormat: 'dd/mm/yy',
			defaultDate: new Date()
		});
		
		$("#datepickerAte", relatorioVendasController.workspace).mask("99/99/9999");
		
		$("#edicaoProduto", relatorioVendasController.workspace).justInput(/[0-9;]/);
		
		$(".popEditorGrid", relatorioVendasController.workspace).flexigrid({
			preProcess: relatorioVendasController.executarPreProcessamentoPopUp,
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigoProduto',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 160,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicaoProduto',
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
				display : 'Venda Exs.',
				name : 'vendaExemplares',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : '% Venda',
				name : 'porcentagemVendaFormatado',
				width : 50,
				sortable : true,
				align : 'right'
			}, {
				display : 'Faturamento R$',
				name : 'faturamentoFormatado',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Margem Cota R$',
				name : 'margemCotaFormatado',
				width : 85,
				sortable : true,
				align : 'right'
			}, {
				display : 'Margem Distr R$',
				name : 'margemDistribuidorFormatado',
				width : 85,
				sortable : true,
				align : 'right'
			} ],
			sortname : "codigoProduto",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			width : 800,
			height : 275
		});

		$(".abcEditorGrid", relatorioVendasController.workspace).flexigrid({
			preProcess: relatorioVendasController.executarPreProcessamentoEditor,
			dataType : 'json',
			colModel : [ {
				display : 'Ranking',
				name : 'rkEditor',
				width : 50,
				sortable : true,
				align : 'left'
			},{
				display : 'Código',
				name : 'codigoEditor',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Editor',
				name : 'nomeEditor',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : 'Reparte',
				name : 'reparteFormatado',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda Exs.',
				name : 'vendaExemplaresFormatado',
				width : 65,
				sortable : true,
				align : 'center'
			}, {
				display : '% Venda Exs.',
				name : 'porcentagemVendaExemplaresFormatado',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Faturamento Capa R$',
				name : 'faturamentoCapaFormatado',
				width : 110,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. %',
				name : 'participacaoFormatado',
				width : 40,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. Acum. %',
				name : 'participacaoAcumuladaFormatado',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Mg Distrib R$',
				name : 'valorMargemDistribuidorFormatado',
				width : 65,
				sortable : true,
				align : 'right'
			}, {
				display : 'Mg Distrib %',
				name : 'porcentagemMargemDistribuidorFormatado',
				width : 65,
				sortable : true,
				align : 'right'
			},{
				display : 'Hist.',
				name : 'hist',
				width : 30,
				sortable : false,
				align : 'center'
			} ],
			sortname : "faturamentoCapaFormatado",
			sortorder : "desc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});

		$(".abcDistribuidorGrid", relatorioVendasController.workspace).flexigrid({
			preProcess: relatorioVendasController.executarPreProcessamentoDistribuidor,
			dataType : 'json',
			colModel : [{ 
				display : 'Ranking',
				name : 'rkCota',
				width : 50,
				sortable : true,
				align : 'left'
			},{
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeCota',
				width : 150,
				sortable : true,
				align : 'left'
			}, {
				display : 'Município',
				name : 'municipio',
				width : 160,
				sortable : true,
				align : 'left'
			}, {
				display : 'Qtde PDVs',
				name : 'quantidadePdvs',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda Exs.',
				name : 'vendaExemplaresFormatado',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Faturamento Capa R$',
				name : 'faturamentoCapaFormatado',
				width : 120,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. %',
				name : 'participacaoFormatado',
				width : 52,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. Acum. %',
				name : 'participacaoAcumuladaFormatado',
				width : 90,
				sortable : true,
				align : 'right'
			} ],
			sortname : "faturamentoCapaFormatado",
			sortorder : "desc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});

		$(".abcProdutoGrid", relatorioVendasController.workspace).flexigrid({
			preProcess: relatorioVendasController.executarPreProcessamentoProduto,
			dataType : 'json',
			colModel : [{ 
				display : 'Rk.Prod.',
				name : 'rkProduto',
				width : 50,
				sortable : true,
				align : 'left'
			},{
				display : 'Rk.Cota.',
				name : 'rkCota',
				width : 50,
				sortable : true,
				align : 'left'
			},  {
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeCota',
				width : 145,
				sortable : true,
				align : 'left'
			}, {
				display : 'Município',
				name : 'municipio',
				width : 160,
				sortable : true,
				align : 'left'
			}, {
				display : 'Qtde PDVs',
				name : 'quantidadePdvs',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda Exs.',
				name : 'vendaExemplaresFormatado',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Faturamento Capa R$',
				name : 'faturamentoCapaFormatado',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. %',
				name : 'participacaoFormatado',
				width : 40,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. Acum. %',
				name : 'participacaoAcumuladaFormatado',
				width : 70,
				sortable : true,
				align : 'right'
			} ],
			sortname : "faturamentoCapaFormatado",
			sortorder : "desc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});

		$(".abcCotaGrid",relatorioVendasController.workspace).flexigrid({
			preProcess: relatorioVendasController.executarPreProcessamentoCota,
			dataType : 'json',
			colModel : [ { 
				display : 'Ranking',
				name : 'rkProduto',
				width : 50,
				sortable : true,
				align : 'left'
			},{
				display : 'Código',
				name : 'codigoProduto',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 170,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicaoProduto',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Reparte',
				name : 'reparteFormatado',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda Exs.',
				name : 'vendaExemplaresFormatado',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda %',
				name : 'porcentagemVendaFormatado',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Faturamento R$',
				name : 'faturamentoFormatado',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. %',
				name : 'participacaoFormatado',
				width : 50,
				sortable : true,
				align : 'right'
			}, {
				display : 'Part. Acum. %',
				name : 'participacaoAcumuladaFormatado',
				width : 70,
				sortable : true,
				align : 'right'
			} ],
			sortname : "faturamento",
			sortorder : "desc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
				
		$(".segmentacaoGrid",relatorioVendasController.workspace).flexigrid({
			preProcess: relatorioVendasController.executarPreProcessamentoSegmentacao,
			dataType : 'json',
			colModel : [ { 
				display : 'Ranking',
				name : 'ranking',
				width : 50,
				sortable : false,
				align : 'left'
			},{
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : false,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeCota',
				width : 500,
				sortable : false,
				align : 'left'
			}, {
				display : 'Faturamento Capa R$',
				name : 'faturamentoCapaFormatado',
				width : 130,
				sortable : false,
				align : 'right'
			}, {
				display : 'Part. %',
				name : 'participacaoFormatado',
				width : 50,
				sortable : false,
				align : 'right'
			}, {
				display : 'Part. Acum. %',
				name : 'participacaoAcumuladaFormatado',
				width : 70,
				sortable : true,
				align : 'right'
			} ],
			sortname : "ranking",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
		
		$(document).ready(function(){
			
			focusSelectRefField($("#filtro_distrib"));
			
			$(document.body).keydown(function(e) {
				
				if(keyEventEnterAux(e)){
					relatorioVendasController.pesquisar();
				}
				
				return true;
			});
		});
		
		$(".areaBts", relatorioVendasController.workspace).hide();
		
	},
	
	validarPesquisaEdicaoAvancada:function(){
		
		var retorno = true;
		
		if($("#edicaoProduto").val().trim().length < 1){
			return retorno;
		}
		
		var dados  = $("#edicaoProduto").val().split(";");
		
		$.each(dados, function(index, value) { 
			if((value.trim().length < 1) || !isNumeric(value)){
				retorno = false;
				return; 
			}
		});
		return retorno;
	},
	
	atribuirNumerosEdicaoPesquisaAvancada:function(params){
		
		var dados  = $("#edicaoProduto").val().split(";");
	
		$.each(dados, function(index, valor) { 
			if(valor.trim().length > 0){
				params.push({name:"edicaoProduto",value:valor});
			}
		});
		
		return params;
	},
	
	pesquisar : function() {

		var dataDe = $("#datepickerDe", relatorioVendasController.workspace).val();
		var dataAte = $("#datepickerAte", relatorioVendasController.workspace).val();

		if ($('#filtro_distrib', relatorioVendasController.workspace).attr("checked") == "checked") {
			$(".abcDistribuidorGrid", relatorioVendasController.workspace).flexOptions({
				url: contextPath + "/lancamento/relatorioVendas/pesquisarCurvaABCDistribuidor",
				params: [
			         {name:'dataDe', value: dataDe},
			         {name:'dataAte', value: dataAte}
			    ],
			    newp: 1,
			});
			
			$(".abcDistribuidorGrid", relatorioVendasController.workspace).flexReload();
			$(".areaBts", relatorioVendasController.workspace).show();
			relatorioVendasController.mostra_distrib();
			
		} else if ($('#filtro_editor', relatorioVendasController.workspace).attr("checked") == "checked") {
			$(".abcEditorGrid", relatorioVendasController.workspace).flexOptions({
				url: contextPath + "/lancamento/relatorioVendas/pesquisarCurvaABCEditor",
				params: [
			         {name:'dataDe', value: dataDe},
			         {name:'dataAte', value: dataAte},
			    ],
			    newp: 1,
			});
			
			$(".abcEditorGrid", relatorioVendasController.workspace).flexReload();
			$(".areaBts", relatorioVendasController.workspace).show();
			relatorioVendasController.mostra_editor();			
			
		} else if ($('#filtro_produto', relatorioVendasController.workspace).attr("checked") == "checked") {
			
			var codigoProduto=$('#codigoProdutoListaProduto', relatorioVendasController.workspace).val();
			var nomeProduto=$('#nomeProdutoListaProduto', relatorioVendasController.workspace).val();
			
			$(".abcProdutoGrid", relatorioVendasController.workspace).flexOptions({
				url: contextPath + "/lancamento/relatorioVendas/pesquisarCurvaABCProduto",
				params: [
			         {name:'dataDe', value: dataDe},
			         {name:'dataAte', value: dataAte},
			         {name:'codigoProduto', value: codigoProduto},
			         {name:'nomeProduto', value: nomeProduto}
			    ],
			    newp: 1,
			});
			
			$(".abcProdutoGrid", relatorioVendasController.workspace).flexReload();
			$(".areaBts", relatorioVendasController.workspace).show();
			relatorioVendasController.mostra_produto();
			
		} else if ($('#filtro_cota', relatorioVendasController.workspace).attr("checked") == "checked") {
			
			var codigoCota=$('#numeroCotaListaCota', relatorioVendasController.workspace).val();
			var nomeCota=$('#nomeCotaListaCota', relatorioVendasController.workspace).val();
			
			$(".abcCotaGrid", relatorioVendasController.workspace).flexOptions({
				url: contextPath + "/lancamento/relatorioVendas/pesquisarCurvaABCCota",
				params: [
			         {name:'dataDe', value: dataDe},
			         {name:'dataAte', value: dataAte},
			         {name:'codigoCota', value: codigoCota},
			         {name:'nomeCota', value: nomeCota}
			    ],
			    newp: 1,
			});
			
			$(".abcCotaGrid", relatorioVendasController.workspace).flexReload();
			$(".areaBts", relatorioVendasController.workspace).show();
			relatorioVendasController.mostra_cota();					
		
		} else if ($('#filtro_segmentacao', relatorioVendasController.workspace).attr("checked") == "checked") {
			
			var idSegmentacao = $('#selectSegmentacao', relatorioVendasController.workspace).val();
			var descricaoSegmento = $('#selectSegmentacao option:selected', relatorioVendasController.workspace).text();
			
			$(".segmentacaoGrid", relatorioVendasController.workspace).flexOptions({
				url: contextPath + "/lancamento/relatorioVendas/pesquisarRankingSegmentacao",
				params: [
			         {name:'dataDe', value: dataDe},
			         {name:'dataAte', value: dataAte},
			         {name:'idSegmentacao', value: idSegmentacao},
			         {name:'descricaoSegmento', value: descricaoSegmento}
			    ],
			    newp: 1,
			});
			$(".segmentacaoGrid", relatorioVendasController.workspace).flexReload();
			$(".areaBts", relatorioVendasController.workspace).show();
		}
	},
	
	pesquisarAvancada : function() {
		
		if (!relatorioVendasController.validarPesquisaEdicaoAvancada()){
			
			exibirMensagem("ERROR", 
						  ['Formato do campo Edição inválido! O campo Edição aceita números separados por ";". Exemplo: 99;00;11']);
			return;
		}
		
		var dataDe = $("#datepickerDe", relatorioVendasController.workspace).val();
		var dataAte = $("#datepickerAte", relatorioVendasController.workspace).val();

		var selectFornecedor = $("select#selectFornecedor", relatorioVendasController.workspace).val();
		var codigoProduto    = $("#rel-vendas-codigoProduto", relatorioVendasController.workspace).val();
		var nomeProduto      = $("#rel-vendas-nomeProduto", relatorioVendasController.workspace).val();
		var selectEditor     = $("select#selectEditor", relatorioVendasController.workspace).val();
		var numerocota       = $("#rel-vendas-numeroCota", relatorioVendasController.workspace).val();
		var nomeCota         = $("#rel-vendas-nomeCota", relatorioVendasController.workspace).val();
		var selectMunicipio  = $("select#selectMunicipio", relatorioVendasController.workspace).val();

		var params = [
		         {name:'dataDe', value: dataDe},
		         {name:'dataAte', value: dataAte},
		         {name:'codigoFornecedor', value: selectFornecedor},
		         {name:'codigoEditor', value: selectEditor},
		         {name:'codigoCota', value: numerocota},
		         {name:'nomeCota', value: nomeCota},
		         {name:'municipio', value: selectMunicipio}
		    ];
		
		params = (relatorioVendasController.atribuirNumerosEdicaoPesquisaAvancada(params));
		
		if ($('#filtro_distrib', relatorioVendasController.workspace).attr("checked") == "checked") {
			
			$(".abcDistribuidorGrid", relatorioVendasController.workspace).flexOptions({
				url: contextPath + "/lancamento/relatorioVendas/pesquisarCurvaABCDistribuidorAvancada",
				params:params,
			    newp: 1,
			});
			
			$(".abcDistribuidorGrid", relatorioVendasController.workspace).flexReload();
			$(".areaBts", relatorioVendasController.workspace).show();
			relatorioVendasController.mostra_distrib();
			
		} else if ($('#filtro_editor', relatorioVendasController.workspace).attr("checked") == "checked") {
			
			$(".abcEditorGrid", relatorioVendasController.workspace).flexOptions({
				url: contextPath + "/lancamento/relatorioVendas/pesquisarCurvaABCEditorAvancada",
				params: params,
			    newp: 1,
			});
			
			$(".abcEditorGrid", relatorioVendasController.workspace).flexReload();
			$(".areaBts", relatorioVendasController.workspace).show();
			relatorioVendasController.mostra_editor();			
			
		} else if ($('#filtro_produto', relatorioVendasController.workspace).attr("checked") == "checked") {
			
			if ($('#codigoProdutoListaProduto', relatorioVendasController.workspace).val() != "") {
				$("#rel-vendas-codigoProduto", relatorioVendasController.workspace).val('');
				codigoProduto=$('#codigoProdutoListaProduto', relatorioVendasController.workspace).val();
			}
			
			if ($('#nomeProdutoListaProduto', relatorioVendasController.workspace).val() != "") {
				$("#rel-vendas-nomeProduto", relatorioVendasController.workspace).val('');
				nomeProduto=$('#nomeProdutoListaProduto', relatorioVendasController.workspace).val();
			}
			
	        params.push({name:'codigoProduto', value: codigoProduto});
	        
	        params.push({name:'nomeProduto', value: nomeProduto});

			
			$(".abcProdutoGrid", relatorioVendasController.workspace).flexOptions({
				url: contextPath + "/lancamento/relatorioVendas/pesquisarCurvaABCProdutoAvancada",
				params:params,
			    newp: 1,
			});
			
			$(".abcProdutoGrid", relatorioVendasController.workspace).flexReload();
			$(".areaBts", relatorioVendasController.workspace).show();
			relatorioVendasController.mostra_produto();
			
		} else if ($('#filtro_cota', relatorioVendasController.workspace).attr("checked") == "checked") {
			
			if ($('#numeroCotaListaCota', relatorioVendasController.workspace).val() != "") {
				numerocota=$('#numeroCotaListaCota', relatorioVendasController.workspace).val();
			}
			if ($('#nomeCotaListaCota', relatorioVendasController.workspace).val() != "") {
				nomeCota=$('#nomeCotaListaCota', relatorioVendasController.workspace).val();
			}
			
			$(".abcCotaGrid", relatorioVendasController.workspace).flexOptions({
				url: contextPath + "/lancamento/relatorioVendas/pesquisarCurvaABCCotaAvancada",
				params: params,
			    newp: 1,
			});
			
			$(".abcCotaGrid", relatorioVendasController.workspace).flexReload();
			$(".areaBts", relatorioVendasController.workspace).show();
			relatorioVendasController.mostra_cota();			
			
		}
		
	},
	
	abrirPopUpHistoricoEditor : function(dataDe, dataAte, codigoEditora) {
		$(".popEditorGrid",relatorioVendasController.workspace).flexOptions({
			url: contextPath + "/lancamento/relatorioVendas/pesquisarHistoricoEditor",
			params: [
		         {name:'dataDe', value: dataDe},
		         {name:'dataAte', value: dataAte},
		         {name:'codigoEditor', value: codigoEditora}
		    ],
		    newp: 1,
		});
		
		$(".popEditorGrid", relatorioVendasController.workspace).flexReload();
		relatorioVendasController.popup_editor();			
	
	},
	
	mostra_pesq_avancada : function() {
		$('#pesquisaAvancada', relatorioVendasController.workspace).fadeIn("slow");
	},
	
	esconde_pesq_avancada : function() {
		$('#pesquisaAvancada', relatorioVendasController.workspace).fadeOut("slow");
	},
	
	limparFiltros: function() {
		$(".filtro", relatorioVendasController.workspace).find("input").val("");
		$(".filtro", relatorioVendasController.workspace).find("select").val(0);
	},

	mostra_distrib : function() {
		$('#relatorioDistribuidor', relatorioVendasController.workspace).show();
		$('#relatorioEditor', relatorioVendasController.workspace).hide();
		$('#relatorioProduto', relatorioVendasController.workspace).hide();
		$('#relatorioCota', relatorioVendasController.workspace).hide();
		$('.linhaCota', relatorioVendasController.workspace).hide();
		$('.linhaProduto', relatorioVendasController.workspace).hide();
		$('#relatorioSegmentacao', relatorioVendasController.workspace).hide();
		$('.linhaSegmentacao', relatorioVendasController.workspace).hide();
		
		var pathExportacaoRelatorioPDF = $("#pathExportarRelatorioVendas", relatorioVendasController.workspace).val() + 'fileType=PDF&tipoRelatorio=1';
		var pathExportacaoRelatorioXLS = $("#pathExportarRelatorioVendas", relatorioVendasController.workspace).val() + 'fileType=XLS&tipoRelatorio=1';
			
		$(".impressaoPDFRelatorioVendas").attr("href", pathExportacaoRelatorioPDF);
		$(".impressaoXLSRelatorioVendas").attr("href", pathExportacaoRelatorioXLS);
		
	},
	
	mostra_editor : function() {
		$('#relatorioDistribuidor', relatorioVendasController.workspace).hide();
		$('#relatorioEditor', relatorioVendasController.workspace).show();
		$('#relatorioProduto', relatorioVendasController.workspace).hide();
		$('#relatorioCota', relatorioVendasController.workspace).hide();
		$('.linhaCota', relatorioVendasController.workspace).hide();
		$('.linhaProduto', relatorioVendasController.workspace).hide();
		$('#relatorioSegmentacao', relatorioVendasController.workspace).hide();
		$('.linhaSegmentacao', relatorioVendasController.workspace).hide();
		
		var pathExportacaoRelatorioPDF = $("#pathExportarRelatorioVendas", relatorioVendasController.workspace).val() + 'fileType=PDF&tipoRelatorio=2';
		var pathExportacaoRelatorioXLS = $("#pathExportarRelatorioVendas", relatorioVendasController.workspace).val() + 'fileType=XLS&tipoRelatorio=2';
			
		$(".impressaoPDFRelatorioVendas").attr("href", pathExportacaoRelatorioPDF);
		$(".impressaoXLSRelatorioVendas").attr("href", pathExportacaoRelatorioXLS);
		
	},
	
	mostra_produto : function() {
		$('#relatorioDistribuidor', relatorioVendasController.workspace).hide();
		$('#relatorioEditor', relatorioVendasController.workspace).hide();
		$('#relatorioProduto', relatorioVendasController.workspace).show();
		$('#relatorioCota', relatorioVendasController.workspace).hide();
		$('#relatorioSegmentacao', relatorioVendasController.workspace).hide();
		$('.linhaCota', relatorioVendasController.workspace).hide();
		$('.linhaProduto', relatorioVendasController.workspace).show();
		$('.linhaSegmentacao', relatorioVendasController.workspace).hide();

		var pathExportacaoRelatorioPDF = $("#pathExportarRelatorioVendas", relatorioVendasController.workspace).val() + 'fileType=PDF&tipoRelatorio=3';
		var pathExportacaoRelatorioXLS = $("#pathExportarRelatorioVendas", relatorioVendasController.workspace).val() + 'fileType=XLS&tipoRelatorio=3';
			
		$(".impressaoPDFRelatorioVendas").attr("href", pathExportacaoRelatorioPDF);
		$(".impressaoXLSRelatorioVendas").attr("href", pathExportacaoRelatorioXLS);
		
	},
	
	mostra_cota : function() {
		$('#relatorioDistribuidor', relatorioVendasController.workspace).hide();
		$('#relatorioEditor', relatorioVendasController.workspace).hide();
		$('#relatorioProduto', relatorioVendasController.workspace).hide();
		$('#relatorioCota', relatorioVendasController.workspace).show();
		$('#relatorioSegmentacao', relatorioVendasController.workspace).hide();
		$('.linhaCota', relatorioVendasController.workspace).show();
		$('.linhaProduto', relatorioVendasController.workspace).hide();
		$('.linhaSegmentacao', relatorioVendasController.workspace).hide();
		
		var pathExportacaoRelatorioPDF = $("#pathExportarRelatorioVendas", relatorioVendasController.workspace).val() + 'fileType=PDF&tipoRelatorio=4';
		var pathExportacaoRelatorioXLS = $("#pathExportarRelatorioVendas", relatorioVendasController.workspace).val() + 'fileType=XLS&tipoRelatorio=4';
			
		$(".impressaoPDFRelatorioVendas").attr("href", pathExportacaoRelatorioPDF);
		$(".impressaoXLSRelatorioVendas").attr("href", pathExportacaoRelatorioXLS);
		
	},
	
	mostra_segmentacao: function() {
		
		$('#relatorioDistribuidor', relatorioVendasController.workspace).hide();
		$('#relatorioEditor', relatorioVendasController.workspace).hide();
		$('#relatorioProduto', relatorioVendasController.workspace).hide();
		$('#relatorioCota', relatorioVendasController.workspace).hide();
		$('.linhaCota', relatorioVendasController.workspace).hide();
		$('.linhaProduto', relatorioVendasController.workspace).hide();
		
		$('#relatorioSegmentacao', relatorioVendasController.workspace).show();
		$('.linhaSegmentacao', relatorioVendasController.workspace).show();
		
		var pathExportacaoRelatorioPDF = $("#pathExportarRelatorioVendas", relatorioVendasController.workspace).val() + 'fileType=PDF&tipoRelatorio=6';
		var pathExportacaoRelatorioXLS = $("#pathExportarRelatorioVendas", relatorioVendasController.workspace).val() + 'fileType=XLS&tipoRelatorio=6';

		$(".impressaoPDFRelatorioVendas").attr("href", pathExportacaoRelatorioPDF);
		$(".impressaoXLSRelatorioVendas").attr("href", pathExportacaoRelatorioXLS);
	},

	popup_editor : function() {
		$("#dialog-editor", relatorioVendasController.workspace).dialog({
			resizable : false,
			height : 500,
			width : 850,
			modal : true,
			buttons : {
				"Fechar" : function() {
					$(this).dialog("close");

				},

			},
			form: $("#dialog-editor", this.workspace).parents("form")
		});
	},
	
	executarPreProcessamentoDistribuidor : function(resultado) {
		if (resultado.mensagens) {
			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			$(".grids", relatorioVendasController.workspace).hide();
			
			return resultado;
		}

		$("#qtdeTotalVendaExemplaresDistribuidor", relatorioVendasController.workspace).html(resultado.totalVendaExemplaresFormatado);
		$("#totalFaturamentoCapaDistribuidor", relatorioVendasController.workspace).html("R$ " + resultado.totalFaturamentoFormatado);
		
		$(".grids", relatorioVendasController.workspace).show();
		
		return resultado.tableModel;
	},

	executarPreProcessamentoEditor : function(resultado) {
		if (resultado.mensagens) {
			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			$(".grids", relatorioVendasController.workspace).hide();
			return resultado;
		}

		$("#qtdeTotalVendaExemplaresEditor", relatorioVendasController.workspace).html(resultado.totalVendaExemplaresFormatado);
		$("#totalFaturamentoCapaEditor", relatorioVendasController.workspace).html("R$ " + resultado.totalFaturamentoFormatado);
		
		$.each(resultado.tableModel.rows, function(index, row) {
			
			var linkHistorico = '<a href="javascript:;" onclick="relatorioVendasController.abrirPopUpHistoricoEditor( \'' + row.cell.dataDe + '\', \'' + row.cell.dataAte + '\', ' + row.cell.codigoEditor + ');" style="cursor:pointer">' +
					     	  	'<img title="Histórico" src="' + contextPath + '/images/ico_detalhes.png" hspace="5" border="0px" />' +
					  		    '</a>';
					  		    
			row.cell.hist = linkHistorico;
		});
		
		$(".grids", relatorioVendasController.workspace).show();
		return resultado.tableModel;
	},

	executarPreProcessamentoProduto : function(resultado) {
		if (resultado.mensagens) {
			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			$(".grids", relatorioVendasController.workspace).hide();
			return resultado;
		}

		$("#qtdeTotalVendaExemplaresProduto", relatorioVendasController.workspace).html(resultado.totalVendaExemplaresFormatado);
		$("#totalFaturamentoCapaProduto", relatorioVendasController.workspace).html("R$ " + resultado.totalFaturamentoFormatado);
		
		$(".grids", relatorioVendasController.workspace).show();
		return resultado.tableModel;
	},

	executarPreProcessamentoCota : function(resultado) {
		if (resultado.mensagens) {
			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			$(".grids", relatorioVendasController.workspace).hide();
			return resultado;
		}

		$("#qtdeTotalVendaExemplaresCota", relatorioVendasController.workspace).html(resultado.totalVendaExemplaresFormatado);
		$("#totalFaturamentoCapaCota", relatorioVendasController.workspace).html("R$ " + resultado.totalFaturamentoFormatado);
		
		$(".grids", relatorioVendasController.workspace).show();
		return resultado.tableModel;
	},

	executarPreProcessamentoSegmentacao: function(result) {
		
		var tableModel = null;
		var totalFaturamentoCapa = 0;
		
		$.each(result, function(index, item) {
			
			if (item[0] === 'tableModel') {
				tableModel = item[1];
			} else if (item[0] === 'totalFaturamentoCapa') {
				totalFaturamentoCapa = item[1];
			}
		});
		
		$("#totalFaturamentoCapaSegmento", relatorioVendasController.workspace).html(totalFaturamentoCapa);
		$(".grids", relatorioVendasController.workspace).show();
		return tableModel;
	},
	
	executarPreProcessamentoPopUp : function(resultado) {
		if (resultado.mensagens) {
			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			$(".dialog-editor", relatorioVendasController.workspace).hide();
			return resultado;
		}

		$("#nomeEditorPopUp", relatorioVendasController.workspace).html(resultado.rows[0].cell.nomeEditor);
		
		$(".dialog-editor", relatorioVendasController.workspace).show();
		return resultado;
	}
	
}, BaseController);

//@ sourceURL=relatorioVendas.js