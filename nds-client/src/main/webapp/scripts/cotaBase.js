var cotaBaseController = $.extend(true, {
	
	init : function(){
		
		$("#indiceAjuste").mask("9.9");

		
		$(".consultaSegmentosGrid").flexigrid({			
			dataType : 'json',
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
		
	$(".consultaEquivalentesDetalheGrid").flexigrid({			
			dataType : 'json',
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
		preProcess: cotaBaseController.executarPreProcessamentoGridPesquisaGeral,
		dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeCota',
				width : 205,
				sortable : true,
				align : 'left'
			},  {
				display : 'Tipo PDV',
				name : 'tipoPDV',
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
				name : 'dtInicioFormatado',
				width : 100,
				sortable : true,
				align : 'center'
			},  {
				display : 'Fim',
				name : 'dtFinalFormatado',
				width : 100,
				sortable : true,
				align : 'center'
			},  {
				display : 'Dias Faltantes',
				name : 'diasRestantes',
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
				name : 'acao',
				width : 25,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			width : 960,
			height : 160
		});
	
	$(".cotasEquivalentesBGrid").flexigrid({
		preProcess : null,
		dataType : 'json',
		colModel : [ {
			display : 'Cota',
			name : 'numeroCota',
			width : 50,
			sortable : true,
			align : 'left'
		}, {
			display : 'Nome',
			name : 'nomeCota',
			width : 130,
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
			width : 90,
			sortable : true,
			align : 'left'
		},  {
			display : 'Gerador de Fluxo',
			name : 'geradorDeFluxo',
			width : 90,
			sortable : true,
			align : 'left'
		},  {
			display : 'Área Influencia',
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
			display : 'Data Alteração',
			name : 'dtAlteracao',
			width : 80,
			sortable : true,
			align : 'center'
		}],
		sortname : "codigo",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 960,
		height : 160
	});
	},
	
	executarPreProcessamentoGridPesquisaGeral : function(resultado){
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".grids", cotaBaseController.workspace).hide();

			return resultado;
		}
		
		$(".grids", cotaBaseController.workspace).show();
		
		return resultado;
		
	},
	
	executarPreProcessamento : function(resultado) {
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".grids", cotaBaseController.workspace).hide();

			return resultado;
		}
		
		return cotaBaseController.prepararGridPrincipal(resultado);
	},
	
	prepararGridPrincipal : function(resultado){
		var aux = 0;
		$.each(resultado.rows, function(index, row) {
			
			if(row.cell.numeroCota == null ){								
				
				row.cell.numeroCota = cotaBaseController.gerarInputNumeroCota(resultado, index) ;
				row.cell.nomeCota = '<div style="text-align: left; width: 90px;" id="nomeCotaGrid'+index+'" ></div>';
				row.cell.tipoPDV = '<div style="text-align: left; width: 90px;" id="tipoPDVGrid'+index+'" ></div>';
				row.cell.bairro = '<div style="text-align: left; width: 90px;" id="bairroGrid'+index+'" ></div>';
				row.cell.cidade = '<div style="text-align: left; width: 90px;" id="cidadeGrid'+index+'" ></div>';
				row.cell.geradorDeFluxo = '<div style="text-align: left; width: 90px;" id="geradorDeFluxoGrid'+index+'" ></div>';
				row.cell.areaInfluencia = '<div style="text-align: left; width: 90px;" id="areaInfluenciaGrid'+index+'" ></div>';
				row.cell.faturamentoFormatado = '<div style="text-align: right; width: 120px;" id="faturamentoGrid'+index+'" ></div>';
				row.cell.acao = '';
			}else{
				row.cell.nomeCota = '<div style="text-align: left; width: 90px;" id="nomeCotaGrid'+index+'" >'+
									'<a href="javascript:;" onClick="cotaBaseController.fotoPdv('+row.cell.numeroCota+')">'+row.cell.nomeCota+'</a>'+
									'</div>';
				aux++;
				$("#indiceAjuste").val(row.cell.indiceAjuste);
				$("#indiceAjuste").mask("9.9");
				row.cell.acao = '<a href="javascript:;" style="margin-right: 5px;cursor:pointer"  onclick="cotaBaseController.excluirPeso('+ row.cell.idCota +');">' +
								'<img src="'+ contextPath +'/images/ico_excluir.gif" hspace="5" border="0px" title="Excluir Cota" />' +
								'</a>';				
			}
			if(aux === 0){
				$("#indiceAjuste").val("");
				$("#indiceAjuste").mask("9.9");
			}		
		});
			
		return resultado;
	},	
	
	
	gerarInputNumeroCota : function(resultado, index){
		
		var valor = "";
		if(resultado && resultado.numeroCota){
			valor = resultado.numeroCota;
		}
		
		var parametroPesquisaCota ='\'#numeroCotaGrid'+ index+ '\',' + index;
		
		var inputNumeroCota ='<input type="text" id="numeroCotaGrid'+index+'" name="numeroCotaGrid" value="'+valor+'" onchange="cotaBaseController.pesquisarCota('+parametroPesquisaCota+')" style="width:55px; float:left; margin-right:10px;"  />';
		
		return inputNumeroCota;
	},
	
	pesquisarCota : function(numeroCota, index) {
		
		if($(numeroCota).val().length == 0){
 			return;
 		}
 		
 		$.postJSON(contextPath + "/cadastro/cotaBase/obterCota",
				{numeroCota:$(numeroCota).val()}, 
				function(result){
					
					cotaBaseController.atribuirDadosCota(result,index);						
 					
 				}, function(result){
					
					//Verifica mensagens de erro do retorno da chamada ao controller.
					if (result.mensagens) {

						exibirMensagemDialog(
								result.mensagens.tipoMensagem, 
								result.mensagens.listaMensagens,""
						);
					}
				}, true,null
		);
	},
	
	atribuirDadosCota:function(resultado, index){
		
		var linkFotoPDV = '<a href="javascript:;" onClick="cotaBaseController.fotoPdv('+resultado.numeroCota+')">'+resultado.nomeCota+'</a>';
		$("#nomeCotaGrid"+index, cotaBaseController.workspace).html(linkFotoPDV);
		
 		$("#tipoPDVGrid"+index, cotaBaseController.workspace).text(resultado.tipoPDV);
 		$("#bairroGrid"+index, cotaBaseController.workspace).text(resultado.bairro);
 		$("#cidadeGrid"+index, cotaBaseController.workspace).text(resultado.cidade);
 		
 		$("#geradorDeFluxoGrid"+index, cotaBaseController.workspace).text(resultado.geradorDeFluxo);
 		$("#areaInfluenciaGrid"+index, cotaBaseController.workspace).text(resultado.areaInfluencia);
 		$("#faturamentoGrid"+index, cotaBaseController.workspace).text(resultado.faturamentoMedio);
 		

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
		$('.historicoGrid').hide();
		
		var numeroCota = $('#idCota').val();
		
		$("#consultaEquivalentesGrid", cotaBaseController.workspace).flexOptions({
			url: contextPath + "/cadastro/cotaBase/pesquisarCotasBasePesquisaGeral",
			dataType : 'json',
			params: [{name: 'numeroCota' , value: numeroCota}]
		});
		
		$("#consultaEquivalentesGrid", cotaBaseController.workspace).flexReload();
	},
	
	mostrar_normal : function (){
		
		$('.pesqGeralGrid', cotaBaseController.workspace).hide();
		$('.pesqCotasGrid', cotaBaseController.workspace).show();
		$('.historicoGrid', cotaBaseController.workspace).hide();
		
		var numeroCota = $('#idCota').val();
		
		$("#cotasEquivalentesGrid", cotaBaseController.workspace).flexOptions({
			url: contextPath + "/cadastro/cotaBase/pesquisarCotasBase",
			dataType : 'json',
			params: [{name: 'numeroCota' , value: numeroCota}]
		});
		
		$("#cotasEquivalentesGrid", cotaBaseController.workspace).flexReload();
		
	},
	
	
	
	popup_novoEquivalente : function() {

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
	excluirPeso : function(idCota) {

		$( "#dialog-excluir" ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					
					var data = [
					            {name:"numeroCotaNova", value:$('#idCota', cotaBaseController.workspace).val()},
					            {name:"idCotaBase", value:idCota}];
					
					
					$.postJSON(contextPath + "/cadastro/cotaBase/excluirCotaBase", data,
							function(result){
								if (result.tipoMensagem && result.listaMensagens) {
									exibirMensagemDialog(
											result.tipoMensagem, 
											result.listaMensagens,""
									);
								}
								cotaBaseController.mostrar_normal();
			 				}, function(result){								
			 					if (result.tipoMensagem && result.listaMensagens) {
									exibirMensagemDialog(
											result.tipoMensagem, 
											result.listaMensagens,""
									);
								}
							});
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	},		
	
	confirmarPeso : function (){
		
		var indiceAjuste = $("#indiceAjuste").val();
		if(indiceAjuste < 0.5 || indiceAjuste > 1.5){		
			var erros = new Array();
			erros[0] = "O Índice deve estar entre 0.5 até 1.5.";
			exibirMensagemDialog('WARNING',	erros,"");			
			return;
		}

		$( "#dialog-confirm" ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {					
					$( this ).dialog( "close" );
					var dto = [];
					var inputNumeroCota = $("#numeroCotaGrid0").val();
					if(inputNumeroCota){
						dto.push({name:'numerosDeCotasBase', value: inputNumeroCota});
					}
					inputNumeroCota = $("#numeroCotaGrid1").val();
					if(inputNumeroCota){
						dto.push({name:'numerosDeCotasBase', value: inputNumeroCota});
					}
					inputNumeroCota = $("#numeroCotaGrid2").val();
					if(inputNumeroCota){
						dto.push({name:'numerosDeCotasBase', value: inputNumeroCota});
					}
					dto.push({name : 'idCotaNova' , value : $("#idCota").val()});
					dto.push({name : 'indiceAjuste' , value : $("#indiceAjuste").val()});
					
					$.postJSON(contextPath + "/cadastro/cotaBase/confirmarCotasBase",
							dto, 
							function(result){
								if (result.tipoMensagem && result.listaMensagens) {
									exibirMensagemDialog(
											result.tipoMensagem, 
											result.listaMensagens,""
									);
								}
								
								cotaBaseController.mostrar_normal();
								cotaBaseController.pesquisarPorNumeroCota('#idCota', '#nomeCota');
			 				}, function(result){								
			 					if (result.tipoMensagem && result.listaMensagens) {
									exibirMensagemDialog(
											result.tipoMensagem, 
											result.listaMensagens,""
									);
								}
							}, true,null
					);
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	},
	
	mostrarHistorico : function(){
		
		$('.pesqGeralGrid', cotaBaseController.workspace).hide();
		$('.pesqCotasGrid' , cotaBaseController.workspace).hide();
		$('.historicoGrid' , cotaBaseController.workspace).show();
		
		var idCota = $("#idCota").val();
		
		$("#cotasEquivalentesBGrid", cotaBaseController.workspace).flexOptions({
			url: contextPath + "/cadastro/cotaBase/obterCotasDoHistorico",
			dataType : 'json',
			params: [{name: 'numeroCota' , value: idCota}]
		});
		
		$("#cotasEquivalentesBGrid", cotaBaseController.workspace).flexReload();		
		
		
	},
	
	botaoVoltarHistoricio : function(){
		
		$('.pesqGeralGrid', cotaBaseController.workspace).hide();
		$('.pesqCotasGrid' , cotaBaseController.workspace).show();
		$('.historicoGrid' , cotaBaseController.workspace).hide();
		
		
	},

	informarPeso : function() {

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


	fotoPdv : function(numeroCota) {
		
		$.postJSON(contextPath + "/cadastro/cotaBase/obterIdPDVPrincipal",
				{"numeroCota":numeroCota}, 
				function(idPDVPrincipal){
					
					$.postJSON(contextPath + "/cadastro/pdv/editar",
							[{name:"idPdv", value:idPDVPrincipal},
							 {name:"idCota", value:null},
							 {name:"modoTela", value: ModoTela.CADASTRO_COTA}], 
							 function(result){						
								if(result.pdvDTO.pathImagem) {
				                    $("#idImagem", this.workspace).attr("src",contextPath + "/" + result.pdvDTO.pathImagem);
				                }
						
					},null,true);
					
					
 				}, function(result){
 					
				}, true,null
		);
		

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
		
		cotaBaseController.limparDadosDoFiltro();
		
		if (numeroCota && numeroCota.length > 0) {
			
			$.postJSON(contextPath + "/cadastro/cotaBase/pesquisarCotaNova",
					{numeroCota:numeroCota},
				function(result) {						
					cotaBaseController.pesquisarPorNumeroSuccessCallBack(result, idCampoNomeCota, successCallBack);
					$('.pesqCotasGrid' , cotaBaseController.workspace).hide();
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
		
		$("#diasRestantes", cotaBaseController.workspace).val(result.diasRestantes);
		
		$("#geradorFluxo", cotaBaseController.workspace).val(result.geradorDeFluxo);
		
		$("#areaInfluencia", cotaBaseController.workspace).val(result.areaInfluencia);
		
		$("#periodoDe", cotaBaseController.workspace).val(result.dataInicialFormatado);
		
		$("#periodoAte", cotaBaseController.workspace).val(result.dataFinalFormatado);
		
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
	},
	
	limparDadosDoFiltro : function(){
		
		$("#tipoPDV", cotaBaseController.workspace).val("");
		
		$("#bairro", cotaBaseController.workspace).val("");
		
		$("#cidade", cotaBaseController.workspace).val("");
		
		$("#diasRestantes", cotaBaseController.workspace).val("");
		
		$("#diasRestantes", cotaBaseController.workspace).val("");
		
		$("#geradorFluxo", cotaBaseController.workspace).val("");
		
		$("#areaInfluencia", cotaBaseController.workspace).val("");
		
		$("#periodoDe", cotaBaseController.workspace).val("");
		
		$("#periodoAte", cotaBaseController.workspace).val("");
		
		
	}
	
}, BaseController);
//@ sourceURL=cotaBase.js