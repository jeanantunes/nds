var cotaBaseController = $.extend(true, {
	
	init : function(){
		
		
		$( "#periodoDe" ).datepicker({
			showOn: "button",
			buttonImage:  contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#periodoAte" ).datepicker({
			showOn: "button",
			buttonImage:  contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		
		
		$(".consultaSegmentosGrid").flexigrid({
			url : '../xml/consultaSegmento-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Segmento',
				name : 'segmento',
				width : 210,
				sortable : true,
				align : 'left'
			}],
			width : 245,
			height : 200
		});
		
		

	$(".pdvCotaGrid").flexigrid({
			url : '../xml/pdvCota-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome PDV',
				name : 'nomePdv',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Endereço',
				name : 'endereco',
				width : 290,
				sortable : true,
				align : 'left'
			},  {
				display : 'Reparte',
				name : 'reparte',
				width : 40,
				sortable : true,
				align : 'center'
			},  {
				display : '',
				name : 'sel',
				width : 20,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 600,
			height : 200
		});


	$(".novaEquivalenteGrid").flexigrid({
			url : '../xml/novaEquivalente-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 30,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : 'Tipo Pdv',
				name : 'tipoPdv',
				width : 75,
				sortable : true,
				align : 'left'
			},  {
				display : 'Bairro',
				name : 'bairro',
				width : 70,
				sortable : true,
				align : 'left'
			},  {
				display : 'Cidade',
				name : 'cidade',
				width : 70,
				sortable : true,
				align : 'left'
			},  {
				display : 'G.Fluxo/Área Infl.',
				name : 'geradorFluxoArea',
				width : 90,
				sortable : true,
				align : 'left'
			},  {
				display : 'Fat. Médio R$',
				name : 'faturamentoMedio',
				width : 70,
				sortable : true,
				align : 'right'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 600,
			height : 200
		});
	$(".novaEquivalenteGrid").flexigrid({
			url : '../xml/novaEquivalente-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 30,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : 'Tipo Pdv',
				name : 'tipoPdv',
				width : 75,
				sortable : true,
				align : 'left'
			},  {
				display : 'Bairro',
				name : 'bairro',
				width : 70,
				sortable : true,
				align : 'left'
			},  {
				display : 'Cidade',
				name : 'cidade',
				width : 70,
				sortable : true,
				align : 'left'
			},  {
				display : 'G.Fluxo/Área Infl.',
				name : 'geradorFluxoArea',
				width : 90,
				sortable : true,
				align : 'left'
			},  {
				display : 'Fat. Médio R$',
				name : 'faturamentoMedio',
				width : 70,
				sortable : true,
				align : 'right'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 600,
			height : 200
		});
	$(".consultaEquivalentesDetalheGrid").flexigrid({
			url : '../xml/consultaEquivalentesDetalhe-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Ajuste',
				name : 'ajuste',
				width : 85,
				sortable : true,
				align : 'center'
			}, {
				display : 'Equivalente 1',
				name : 'equiv1',
				width : 240,
				sortable : true,
				align : 'left'
			}, {
				display : 'Equivalente 2',
				name : 'equiv2',
				width : 240,
				sortable : true,
				align : 'left'
			}, {
				display : 'Equivalente 3',
				name : 'equiv3',
				width : 240,
				sortable : true,
				align : 'left'
			}],
			sortname : "ajuste",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 875,
			height : 250
		});
	$(".consultaEquivalentesGrid").flexigrid({
			url : '../xml/consultaEquivalentesA-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'Nome',
				width : 205,
				sortable : true,
				align : 'left'
			},  {
				display : 'Tipo PDV',
				name : 'tipoPdv',
				width : 100,
				sortable : true,
				align : 'left'
			},  {
				display : 'Situação',
				name : 'situacao',
				width : 100,
				sortable : true,
				align : 'left'
			},  {
				display : 'Início',
				name : 'dtInicio',
				width : 100,
				sortable : true,
				align : 'center'
			},  {
				display : 'Fim',
				name : 'dtFim',
				width : 100,
				sortable : true,
				align : 'center'
			},  {
				display : 'Dias Faltantes',
				name : 'diasFaltantes',
				width : 100,
				sortable : true,
				align : 'center'
			},  {
				display : 'Ação',
				name : '',
				width : 75,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 240
		});
	$(".rankPublicacaoGrid").flexigrid({
			url : '../xml/rankPublicacao-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : 'Publicação',
				name : 'publicacao',
				width : 200,
				sortable : true,
				align : 'left'
			}, {
				display : 'Segmento',
				name : 'segmento',
				width : 190,
				sortable : true,
				align : 'left'
			},  {
				display : '',
				name : 'sel',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 600,
			height : 200
		});
	
	$(".rankSegmentoGrid").flexigrid({
			url : '../xml/rankSegmento-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Ordem',
				name : 'ordem',
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : 'Segmento',
				name : 'segmento',
				width : 340,
				sortable : true,
				align : 'left'
			}, {
				display : '',
				name : 'sel',
				width : 20,
				sortable : true,
				align : 'center'
			},  {
				display : 'Ajuste',
				name : 'ajuste',
				width : 60,
				sortable : true,
				align : 'center'
			}],
			sortname : "ranking",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 600,
			height : 200
		});
	$(".pesqBancasGrid").flexigrid({
			url : '../xml/pesqBancas-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'Nome',
				width : 400,
				sortable : true,
				align : 'left'
			},  {
				display : '',
				name : 'sel',
				width : 30,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 600,
			height : 200
		});
	$(".cotasEquivalentesGrid", cotaBaseController.workspace).flexigrid({
		preProcess: cotaBaseController.executarPreProcessamento,
		dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeCota',
				width : 135,
				sortable : true,
				align : 'left'
			},  {
				display : 'Tipo PDV',
				name : 'tipoPDV',
				width : 90,
				sortable : true,
				align : 'left'
			},  {
				display : 'Bairro',
				name : 'bairro',
				width : 100,
				sortable : true,
				align : 'left'
			},  {
				display : 'Cidade',
				name : 'cidade',
				width : 100,
				sortable : true,
				align : 'left'
			},  {
				display : 'Gerador de Fluxo',
				name : 'geradorDeFluxo',
				width : 90,
				sortable : true,
				align : 'left'
			},  {
				display : 'Área Influência',
				name : 'areaInfluencia',
				width : 80,
				sortable : true,
				align : 'left'
			},  {
				display : 'Faturamento Médio R$',
				name : 'faturamentoFormatado',
				width : 120,
				sortable : true,
				align : 'right'
			},  {
				display : 'Ação',
				name : '',
				width : 25,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			width : 960,
			height : 160
		});
	},
	
	executarPreProcessamento : function(resultado) {
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".grids", vendaProdutoController.workspace).hide();

			return resultado;
		}
		
		return cotaBaseController.prepararGridPrincipal(resultado);
	},
	
	prepararGridPrincipal : function(resultado){
		
		$.each(resultado.rows, function(index, row) {
			
			if(row.cell.numeroCota == null ){
				var inputNumeroCota ='<input type="text" id="numeroCotaGrid0" name="numeroCotaGrid" style="width:55px; float:left; margin-right:10px;"  />';
				
				var inputNomeCota='<input type="text" id="nomeCotaGrid0" name="nomeCotaGrid" style="width:120px; float:left; margin-right:10px;"/>';				
				
				row.cell.numeroCota = inputNumeroCota;
				row.cell.nomeCota = inputNomeCota;
			}
		
		});
			
		return resultado;
	},
	
	porSegmento : function(){
		$('.classPublicacoes').hide();
		$('.classSegmento').show();
	},
	
	porPublicacao : function (){
		$('.classPublicacoes').show();
		$('.classSegmento').hide();
	},
	
	mostraPesqGeral : function (){
		
		if(document.getElementById('isGeral').checked){
			$('.pesqGeral').show();
			$('.pesqNormal').hide();
		}else{
			$('.pesqGeral').hide();
			$('.pesqNormal').show();
			
		}
	},
	
	mostrar_geral : function (){
		
		$('.pesqGeralGrid').show();
		$('.pesqCotasGrid').hide();
	},
	
	mostrar_normal : function (){
		
		$('.pesqGeralGrid', cotaBaseController.workspace).hide();
		$('.pesqCotasGrid' , cotaBaseController.workspace).show();
		
		var numeroCota = $('#idCota').val();
		
		$("#cotasEquivalentesGrid", cotaBaseController.workspace).flexOptions({
			url: contextPath + "/cadastro/cotaBase/pesquisarCotasBase",
			dataType : 'json',
			params: [{name: 'numeroCota' , value: numeroCota}]
		});
		
		$("#cotasEquivalentesGrid", cotaBaseController.workspace).flexReload();

	},
	
	
	
	popup_novoEquivalente : function() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );

		$( "#dialog-novo-equivale" ).dialog({
			resizable: false,
			height:500,
			width:650,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	},
	
	incluirSegmento : function() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );

		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:500,
			width:650,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	},		

	cancelarPeso : function() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );

		$( "#dialog-cancelar" ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	},
	excluirPeso : function() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );

		$( "#dialog-excluir" ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	},		
	
	confirmarPeso : function () {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );

		$( "#dialog-confirm" ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	},	

	informarPeso : function() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );

		$( "#dialog-peso" ).dialog({
			resizable: false,
			height:450,
			width:650,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	},		


	fotoPdv : function() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );

		$( "#dialog-foto-pdv" ).dialog({
			resizable: false,
			height:540,
			width:670,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				},
			}
		});
	},

	definicaoReparte : function(){
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );

		$( "#dialog-defineReparte" ).dialog({
			resizable: false,
			height:590,
			width:650,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	},

	detalhesEquivalente : function() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );

		$( "#dialog-detail" ).dialog({
			resizable: false,
			height:520,
			width:930,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				},
			}
		});
	},
	
	segmentosNaoRecebidos : function(){
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );

		$( "#dialog-segmentos" ).dialog({
			resizable: false,
			height:370,
			width:290,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				},
			}
		});
	},
	
	//Pesquisa por número da cota
	pesquisarPorNumeroCota : function(idCampoNumeroCota, idCampoNomeCota, isFromModal, successCallBack, errorCallBack) {
		
		var numeroCota = $(idCampoNumeroCota, pesquisaCota.workspace).val();

		numeroCota = $.trim(numeroCota);
		
		$(idCampoNomeCota, pesquisaCota.workspace).val("");
		
		if (numeroCota && numeroCota.length > 0) {
			
			$.postJSON(contextPath + "/cadastro/cotaBase/pesquisarCotaNova",
					{numeroCota:numeroCota},
				function(result) { 
						cotaBaseController.pesquisarPorNumeroSuccessCallBack(result, idCampoNomeCota, successCallBack); 
				},
				function() {
					cotaBaseController.pesquisarPorNumeroErrorCallBack(idCampoNumeroCota, errorCallBack);					
				}, 
				isFromModal
			);

		} else {
			
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},
	
	//Success callback para pesquisa por número da cota
	pesquisarPorNumeroSuccessCallBack : function(result, idCampoNomeCota, successCallBack) {

		pesquisaCota.pesquisaRealizada = true;
		
		$(idCampoNomeCota, pesquisaCota.workspace).val(result.nomeCota);
		
		$("#tipoPDV", cotaBaseController.workspace).val(result.tipoPDV);
		
		$("#bairro", cotaBaseController.workspace).val(result.bairro);
		
		$("#cidade", cotaBaseController.workspace).val(result.cidade);
		
		$("#geradorFluxo", cotaBaseController.workspace).val(result.geradorDeFluxo);
		
		$("#areaInfluencia", cotaBaseController.workspace).val(result.areaInfluencia);
		
		if (successCallBack) {
			
			successCallBack();
		}
	},
	
	//Error callback para pesquisa por número da cota
	pesquisarPorNumeroErrorCallBack : function(idCampoNumeroCota, errorCallBack) {
		
		$(idCampoNumeroCota, pesquisaCota.workspace).val("");
		
		$(idCampoNumeroCota, pesquisaCota.workspace).focus();
		
		if (errorCallBack) {
			
			errorCallBack();
		}
	}
	
}, BaseController);
//@ sourceURL=cotaBase.js