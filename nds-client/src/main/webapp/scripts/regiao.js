var regiaoController = $.extend(true, {
	cotaAtual : '',
	numCotas : null,
	
	init : function() {
	
		$("#faixaGrid", regiaoController.workspace).flexigrid({
			preProcess : regiaoController.executarPreProcessFaixaGrid,
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeCota',
				width : 320,
				sortable : true,
				align : 'left'
			}, {
				display : 'Status',
				name : 'tipoStatus',
				width : 130,
				sortable : true,
				align : 'left'
			}, {
				display : ' ',
				name : 'sel',
				width : 20,
				sortable : true,
				align : 'center'
			} ],
			sortname : "numeroCota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 600,
			height : 200,
		});

		$(".lstCotasGrid").flexigrid({
			url : '../xml/cotasLst-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 60,
				sortable : true,
				align : 'left',
			}, {
				display : 'Nome',
				name : 'nome',
				width : 300,
				sortable : true,
				align : 'left'
			}, {
				display : 'Status',
				name : 'status',
				width : 150,
				sortable : true,
				align : 'left'
			}, {
				display : '',
				name : 'sel',
				width : 20,
				sortable : true,
				align : 'center'
			} ],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 600,
			height : 200
		});

		$(".lstProdutosGrid").flexigrid({
			url : '../xml/nMaioresLst-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Edição',
				name : 'edicao',
				width : 80,
				sortable : true,
				align : 'left',
			}, {
				display : 'Data de Lançamento',
				name : 'dtLancamento',
				width : 130,
				sortable : true,
				align : 'left'
			}, {
				display : 'Status',
				name : 'status',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Classificação',
				name : 'classificação',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Capa',
				name : 'capa',
				width : 40,
				sortable : true,
				align : 'center'
			}, {
				display : '',
				name : 'sel',
				width : 30,
				sortable : true,
				align : 'center'
			} ],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 600,
			height : 200
		});

		$(".nMaioresGrid").flexigrid({
			url : '../xml/nMaiores-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left',
			}, {
				display : 'Produto',
				name : 'produto',
				width : 250,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Classificação',
				name : 'classificacao',
				width : 115,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 30,
				sortable : true,
				align : 'center'
			} ],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 600,
			height : 250
		});
		
		$(".segmentosGrid").flexigrid({
			url : '../xml/segmentoB-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'Nome',
				width : 220,
				sortable : true,
				align : 'left'
			}, {
				display : 'Tipo de PDV',
				name : 'tipoPdv',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Status',
				name : 'Status',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : ' ',
				name : 'sel',
				width : 20,
				sortable : true,
				align : 'center'
			} ],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 600,
			height : 200
		});

		$(".classificacaoGrid").flexigrid({
			url : '../xml/segmentos-xml.xml',
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
				width : 150,
				sortable : true,
				align : 'left'
			}, {
				display : '',
				name : 'sel',
				width : 30,
				sortable : true,
				align : 'center'
			} ],
			width : 300,
			height : 235
		});

		$(".addCotasGrid").flexigrid({
			url : '../xml/addCotas-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Região',
				name : 'regiao',
				width : 280,
				sortable : true,
				align : 'left'
			}, {
				display : 'Fixa',
				name : 'fixa',
				width : 30,
				sortable : true,
				align : 'center'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 30,
				sortable : true,
				align : 'center'
			} ],
			sortname : "regiao",
			sortorder : "asc",
			useRp : true,
			rp : 15,
			width : 400,
			height : 200
		});

		$(".regioesCadastradasGrid", regiaoController.workspace).flexigrid({
			preProcess: regiaoController.executarPreProcessamentoTelaManutencao,
			dataType : 'json',
			colModel : [ {
				display : 'Região',
				name : 'nomeRegiao',
				width : 210,
				sortable : true,
				align : 'left'
			}, {
				display : 'Usuário',
				name : 'nomeUsuario',
				width : 150,
				sortable : true,
				align : 'left'
			}, {
				display : 'Data',
				name : 'dataAlteracao',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Fixa',
				name : 'isFixa',
				width : 30,
				sortable : true,
				align : 'center'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 30,
				sortable : true,
				align : 'center'
			} ],
			sortname : "regiao",
			sortorder : "asc",
			width : 600,
			height : 200
		});

		$(".cotasRegiaoGrid", regiaoController.workspace).flexigrid({
			preProcess: regiaoController.executarPreProcessamentoCotasDaRegiao,
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
				width : 130,
				sortable : true,
				align : 'left'
			}, {
				display : 'Tipo PDV',
				name : 'tipoPDV',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Status',
				name : 'tipoStatus',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Bairro',
				name : 'bairro',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Cidade',
				name : 'cidade',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : 'Faturamento R$',
				name : 'faturamento',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Usuário',
				name : 'nomeUsuario',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Data',
				name : 'data',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Hora',
				name : 'hora',
				width : 33,
				sortable : true,
				align : 'center'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 25,
				sortable : true,
				align : 'center'
			} ],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
	},

	
	// PreProcess - .cotasRegiaoGrid
	executarPreProcessamentoCotasDaRegiao : function (resultado){
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".grids", regiaoController.workspace).hide();

			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {
			
			var linkDetalhe = '<a href="javascript:;" onclick="regiaoController.remove_cotas_grid('+row.cell.registroCotaRegiaoId+');" style="cursor:pointer">' +
								'<img src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0" />'+	
							   '</a>';
			
			row.cell.acao = linkDetalhe;
			
		});
		
		$(".grids", regiaoController.workspace).show();
		
		return resultado;
	},
	
	// -- PRE PROCESS'S --
	
	//PreProcess TELA MANUTENCAO
	executarPreProcessamentoTelaManutencao : function (resultado){
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".grids", regiaoController.workspace).hide();

			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {
			
			var linkDetalhe = '<a href="javascript:;" onclick="regiaoController.excluirRegiao('+row.cell.id+');" style="cursor:pointer">' +
								'<img src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0" />'+	
							   '</a>';
			
			row.cell.acao = linkDetalhe;
			
			var checkBox = null;
			if(row.cell.isFixa){
				checkBox = '<input type="checkbox" name="fixa" id="fixa" checked>';				
			}else{
				checkBox = '<input type="checkbox" name="fixa" id="fixa">';
			}
			row.cell.isFixa = checkBox;
			
		});
		
		$(".grids", regiaoController.workspace).show();
		
		return resultado;
	},
	
	// -- FUNCTIONS --

	// FUNCTIONS - CARREGAR REGIAO
	
	carregarRegiao : function() {
		// $( "#dialog:ui-dialog" ).dialog( "destroy" );

		$(".regioesCadastradasGrid", this.workspace).flexOptions({
			url: contextPath + "/distribuicao/regiao/carregarRegiao",
			dataType : 'json',
		});
			
		$(".regioesCadastradasGrid", this.workspace).flexReload();		
		
		$("#dialog-novo").dialog({
			resizable : false,
			height : 400,
			width : 650,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					$(this).dialog("close");

					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			}
		});
	},

	// FUNCTION - ADD COTAS

	// POSSÍVELMENTE INUTILIZADA
	
	addCotas : function() {
		// $( "#dialog:ui-dialog" ).dialog( "destroy" );

		$("#dialog-cotas").dialog({
			resizable : false,
			height : 550,
			width : 650,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					$(this).dialog("close");
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			}
		});
	},

	
	// FUNCTION - EDITAR REGIAO
	
	editarRegiao : function(){
		var regiao = $("#comboRegioes option:selected").val();
		
		//alert(regiao);
		
		$(".cotasRegiaoGrid", this.workspace).flexOptions({
			url: contextPath + "/distribuicao/regiao/carregarCotasRegiao",
			dataType : 'json',
			params:[{
				name : 'filtro.id', value:regiao
			}]
		});
			
		$(".cotasRegiaoGrid", this.workspace).flexReload();
			
	},
	
	// FUNCTION - COTAS DA REGIAO
	
	cotasDaRegiao : function(){
		var regiao = $("#comboRegioes option:selected").val();
		
		//alert(regiao);
		
		$(".cotasRegiaoGrid", this.workspace).flexOptions({
			url: contextPath + "/distribuicao/regiao/carregarCotasRegiao",
			dataType : 'json',
			params:[{
				name : 'filtro.id', value:regiao
			}]
		});
			
		$(".cotasRegiaoGrid", this.workspace).flexReload();
		$(".regioesCadastradasGrid", this.workspace).flexReload();
	},
	
	// FUNCTION - ADD NOVA REGIAO
	
	addNovaRegiao : function() {
		// $( "#dialog:ui-dialog" ).dialog( "destroy" );

		$("#dialog-addRegiao").dialog({

					resizable : false,
					height : 'auto',
					width : 350,
					modal : true,

					buttons : {
						"Confirmar" : function() {
							$(this).dialog("close");
							mostrar();
							
							var nome = $("#nomeRegiao").val();
							var isFixa = $('#regiaoIsFixa').is(':checked');
							
							var data = [ {
								name : 'nome', value : nome
							}, {
								name : 'isFixa', value : isFixa
							} ];

							// Chamada para a controller
							$.postJSON(contextPath
									+ "/distribuicao/regiao/salvarRegiao",
									data, function(result) {
									});
							$("#dialog-addRegiao", this.workspace).flexReload();
						},
						"Cancelar" : function() {
							$(this).dialog("close");
						}
					}
				});

		$("#regiaoIsFixa", this.regiaoController.workspace).val(0);
		if (document.dialog - addRegiao.regiaoIsFixa.checked) {
			$("#regiaoIsFixa", this.regiaoController.workspace).val(1);
		}
	},
	
	// FUNCTION - ADD COTA
	
	addCota : function() {
		// $( "#dialog:ui-dialog" ).dialog( "destroy" );

		$("#dialog-addCota").dialog({
					resizable : false,
					height : 'auto',
					width : 750,
					modal : true,

					buttons : {
						"Confirmar" : function() {
							$(this).dialog("close");
							},
						"Cancelar" : function() {
							$(this).dialog("close");
						}
					}
				});
	},	
	
	// FUNCTION - REGIAO AUTOMÁTICA
	
	regiaoAutomatica : function() {
		// $( "#dialog:ui-dialog" ).dialog( "destroy" );

		$("#dialog-regiaoAutomatica").dialog({
			resizable : false,
			height : 400,
			width : 400,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					$(this).dialog("close");
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			}
		});
	},

	
	// FUNCTION - EXCLUIR REGIÃO
	
	excluirRegiao : function(id) {
		// $( "#dialog:ui-dialog" ).dialog( "destroy" );
//		alert (id);
		$("#dialog-excluir").dialog({
			resizable : false,
			height : 170,
			width : 380,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					$.postJSON(contextPath + "/distribuicao/regiao/excluirRegiao", 
							{id:id},
							function(result) {
//									$(".regioesCadastradasGrid", this.workspace).flexReload(),
									$("#dialog-excluir", this.workspace).dialog("close");

									var tipoMensagem = result.tipoMensagem;
									var listaMensagens = result.listaMensagens;
									
									if (tipoMensagem && listaMensagens) {
										
										exibirMensagem(tipoMensagem, listaMensagens);
									}
											
									$(".regioesCadastradasGrid", this.workspace).flexReload();
							   },
							   null,
							   true
					);
					
					$(".regioesCadastradasGrid", this.workspace).flexOptions({
						url: contextPath + "/distribuicao/regiao/carregarRegiao",
						dataType : 'json'
					});
						
					$(".regioesCadastradasGrid", this.workspace).flexReload();		
					
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			},
			
			beforeClose: function() {
				clearMessageDialogTimeout('dialogMensagemNovo');
			},
//			form: $("#dialog-excluir", regiaoController.workspace).parents("form")
		});
	},
	
	
	// FUNCTION - ADD EM LOTE
	
	add_lote : function() {
		$("#dialog-lote").dialog({
			resizable : false,
			height : 320,
			width : 190,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					$(this).dialog("close");
					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			}
		});

	},
	
	// FUNCTION - ADD PRODUTOS

	add_produtos : function() {
		$("#dialog-addNMaiores").dialog({
			resizable : false,
			height : 520,
			width : 645,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					$(this).dialog("close");

					$("#effect").show("highlight", {}, 1000, callback);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			}
		});

	},

//	add_cotas : function() {
//		$("#dialog-pesqCotas").dialog({
//			resizable : false,
//			height : 520,
//			width : 645,
//			modal : true,
//			buttons : {"Confirmar" : function() {
//					$(this).dialog("close");
//					$("#effect").show("highlight", {}, 1000, callback);
//					
//					var numero = $("#numeroCota").val();
//					var nome = $("#nomeCota").val();
//					
//					var data = [ {
//						name : 'nome', value : nome}, 
//						{name : 'numero', value : numero} ];
//				
//			},
//				"Cancelar" : function() {
//					$(this).dialog("close");
//				}
//			}
//		});
//
//	},
	
//	add_cotas : function() {
//		$("#dialog-addCota").dialog({});
// 
//	},
	
	// FUNCTION - VERIFICA SE A REGIÃO É FIXA 	
	
	isFixa : function(campo) {
		
		var elemento = $("#" + campo, regiaoController.workspace);
		
		if(elemento.attr('type') == 'checkbox') {
			return (elemento.attr('checked') == 'checked') ;
		} else {
			return elemento.val();
		}
		
	},

	// FUNCTION - FILTRO POR CEP
	
	filtroPorCep : function() {
		$('.porCep').show();
		$('.porSegmento').hide();

		$('.gridfaixaCep').hide();
		$('.gridNMaiores').hide();
		$('.gridsegmentos').hide();
	},
	
	// FUNCTION - FILTRO POR N-MAIORES

	filtroPorNMaiores : function() {
		$('.porCep').hide();
		$('.porSegmento').hide();

		$('.gridfaixaCep').hide();
		$('.gridNMaiores').show();
		$('.gridsegmentos').hide();
	},

	// FUNCTION - FILTRO POR SEGMENTO
	
	filtroPorSegmento : function() {
		$('.porCep').hide();
		$('.porSegmento').show();

		$('.gridfaixaCep').hide();
		$('.gridNMaiores').hide();
		$('.gridsegmentos').hide();
	},

	// Preprocess do faixaGrid
	executarPreProcessFaixaGrid : function(resultado){
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".grids", regiaoController.workspace).hide();

			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {
			
			var checkBox = '<input type="checkbox" name="cotaSelected" value="'+ row.cell.cotaId +'" />';
			
			row.cell.sel = checkBox;
			
		});
		
		$(".grids", regiaoController.workspace).show();
		
		return resultado;
		
	},
	
	// FUNCTION - MOSTRAR POR CEP
	
	mostrarPorCep : function() {
		$('.gridfaixaCep').show();
		$('.gridNMaiores').hide();
		$('.gridsegmentos').hide();
		
		
//		$("#faixaGrid", regiaoController.workspace).flexOptions({
//			url: contextPath + "/distribuicao/regiao/buscarPorCep",
//			dataType : 'json',
//			params : regiaoController.obterRangeDeCep()
//		});
//		
//		$("#faixaGrid", regiaoController.workspace).flexReload();	
		
		$.postJSON(contextPath + "/distribuicao/regiao/buscarPorCep", 
				regiaoController.obterRangeDeCep(),
				function(result) {
//						var tipoMensagem = result.tipoMensagem;
//						var listaMensagens = result.listaMensagens;
//						
//						if (tipoMensagem && listaMensagens) {
//							
//							exibirMensagem(tipoMensagem, listaMensagens);
//						}
						
						var json = JSON.stringify(result);
												
						$(".faixaGrid", regiaoController.workspace).flexAddData(json);
		
						$("#faixaGrid", regiaoController.workspace).flexReload();
						
				   },
				   null,
				   true
		);
	},
	
	// FUNCTION - OBTER RANGE DE CEP'S
	
	obterRangeDeCep : function(){
		
		var data = [];
		
		// Obter o cep inicial e final
		var cepInicialPart1 = $('#cepInicialPart1').val();
		var cepInicialPart2 = $('#cepInicialPart2').val();
		
		var cepFinalPart1 = $('#cepFinalPart1').val();
		var cepFinalPart2 = $('#cepFinalPart2').val();
		
		var cepInicial = cepInicialPart1 + cepInicialPart2;
		var cepFinal = cepFinalPart1 + cepFinalPart2;
		
		data.push({name:'filtro.cepInicial', value: cepInicial});
		data.push({name:'filtro.cepFinal', value: cepFinal});
		
		return data;
	},
	
	// FUNCTION - MOSTRAR POR N-MAIORES
	
	mostrarPorNMaiores : function() {
		$('.gridfaixaCep').hide();
		$('.gridNMaiores').show();
		$('.gridsegmentos').hide();
	},
	
	// FUNCTION - MOSTRAR POR SEGMENTO

	mostrarPorSegmento : function() {
		$('.gridfaixaCep').hide();
		$('.gridNMaiores').hide();
		$('.gridsegmentos').show();
	},

	// FUNCTION - POP UP DETALHES
	
	popup_detalhes : function() {
		// $( "#dialog:ui-dialog" ).dialog( "destroy" );

		$("#dialog-detalhes").dialog({
			resizable : false,
			height : 'auto',
			width : 'auto',
			modal : false
		});
	},
	
	// FUNCTION - POP UP DETALHES CLOSE
	
	popup_detalhes_close : function() {
		$("#dialog-detalhes").dialog("close");

	},

	// FUNCTION - ADD COTAS NO GRID
	
	add_cotas_grid : function() {
//		$('.cotasRegiaoGrid #row2', this.workspace).show();
//		$('.cotasRegiaoGrid #row4').show();
		
	},
	
	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	// FUNCTION - ADD COTAS NA REGIÃO
	
	popupAddCotaRegiao : function(evitarReset) {
		
		if(!evitarReset) {
			$('#idCotas tr', regiaoController.workspace).remove();
			regiaoController.gerarLinhaCota('','');
		}
		
		$( "#dialog-addCota", regiaoController.workspace).dialog({
			resizable: false,
			height:'auto',
			width:530,
			modal: true,
			buttons: {
				/*
				"Confirmar" : function() {
							$(this).dialog("close");
							mostrar();
							
							var nome = $("#nomeRegiao").val();
							var isFixa = $('#regiaoIsFixa').is(':checked');
							
							var data = [ {
								name : 'nome', value : nome
							}, {
								name : 'isFixa', value : isFixa
							} ];

							// Chamada para a controller
							$.postJSON(contextPath
									+ "/distribuicao/regiao/salvarRegiao",
									data, function(result) {
									});
							$("#dialog-addRegiao", this.workspace).flexReload();
						},
				 */
				
				/*
				 * regiaoController.numCotas = cotas;
		
		var parametros = [];
		var numero;
		var idRegicaoSelecionada;
		
		$.each(cotas, function(index, num) {			
			//parametros.push({name:'numCotas['+ index +']', value: num});
			numero = parametros.push({name : 'numeroCota', value : num});
			idRegicaoSelecionada = $('#comboRegioes option:selected', regiaoController.workspace).val();
	  	});
				 */

				// FUNCTION - CONFIRMAR ADIÇÃO DE COTA NA REGIAO
				
				"Confirmar": function() {
					$(this).dialog("close");
					mostrar();
					
					var idRegiaoSelecionada = $('#comboRegioes option:selected', regiaoController.workspace).val();
					var cotas = [];

					$('.cotaOrigem').each(function() {
						if(this.value.length>0)
							cotas.push({name:'numeroCota', value:this.value});
					});
					
					// adicionando a região
					cotas.push({name:'idRegiao', value: idRegiaoSelecionada});
					
//					var data = [{
//						name: 'idRegiaoSelecionada', value :  idRegiaoSelecionada
//					},{
//						name: 'cotas', value : cotas
//					}];
					
					if(cotas.length === 0) {
						exibirMensagemDialog("WARNING",["Selecione uma ou mais cotas."]);	
						return;
					}
					
					$.postJSON(contextPath + "/distribuicao/regiao/addCotaNaRegiao",
							cotas, function(result) {
						
					});
					
//					$(".regioesCadastradasGrid", this.workspace).flexOptions({
//						url: contextPath + "/distribuicao/regiao/carregarRegiao",
//						dataType : 'json'
//					});
//						
//					$(".regioesCadastradasGrid", this.workspace).flexReload();
					
					$( this ).dialog( "close" );
					
				},
				"Cancelar": function() {
										
					$( this ).dialog( "close" );
				}				
			},
//			form: $("#dialog-addCota", cotaAusenteController.workspace).parents("form")
		});
		$(".regioesCadastradasGrid", regiaoController.workspace).flexReload();
	},
	
	// FUNCTION - PEGAR DADOS VIA JSON, PARA ENVIAR PRA CONTROLLER
	
	getDados : function(){
		var data = [];
		data.push({name:'idRegiao',		value: regiaoController.get("idRegiaoSelecionada")});
		data.push({name:'cotasCadastradasNaRegiao',		value: regiaoController.get("cotas")});
	},
	
	
//	getDados : function() {
//		
//		var data = [];
//		
//		data.push({name:'filtro.codigo',		value: vendaProdutoController.get("codigo")});
//		data.push({name:'filtro.nomeProduto',		value: vendaProdutoController.get("produto")});
//		data.push({name:'filtro.edicao',		value: vendaProdutoController.get("edicoes")});
//		data.push({name:'filtro.idFornecedor',		value: vendaProdutoController.get("idFornecedor")});		
//		data.push({name:'filtro.nomeFornecedor',	value: $('#idFornecedor option:selected', vendaProdutoController.workspace).text()});
//		
//		return data;
//	},
	
	// FUNCTION - GERAR LINHA NO POP UP DE ADD COTA
	
	gerarLinhaCota : function(num, nome) {		
		
		regiaoController.cotaAtual = '';
		
		var tabRateios = $("#idCotas");	
		
		tabRateios.append(regiaoController.getNovaLinhaCota(num,nome));
		
	},
	
	// FUNCTION - GERAR NOVA LINHA NO POP UP
	
	getNovaLinhaCota : function(num, nome) {
		
		var novaLinha = $(document.createElement("TR") , regiaoController.workspace);
		
		novaLinha.attr('id','idLinhaCota' + num);
		
		var labelNum = $(document.createElement("TD") , regiaoController.workspace);
		var labelNome = $(document.createElement("TD"), regiaoController.workspace);
				
		var numCota = $(document.createElement("TD"), regiaoController.workspace);
		var nomeCota = $(document.createElement("TD"), regiaoController.workspace);
		
		labelNum.text('Cota:');
						
		numCota.append(regiaoController.getInput(
				num,
				"idNumCota"+num ,
				"80px",
				null,
				null,
				"pesquisaCota.pesquisarPorNumeroCota('#idNumCota"+num+"', '#idNomeCota"+num+"',true,regiaoController.processarLinhaAlterada)",
				null,
				"cotaOrigem",
				"regiaoController.setNum(this)",
				"num", num));

		labelNome.text('Nome:');
				
		nomeCota.append(regiaoController.getInput(
				nome,
				"idNomeCota"+num ,
				"280px",
				null,
				"if(regiaoController.deletarInputVazio(this))return; pesquisaCota.pesquisarPorNomeCota('#idNumCota"+num+"', '#idNomeCota"+num+"',true, regiaoController.processarLinhaAlterada);", 
				null,
				"pesquisaCota.autoCompletarPorNome('#idNomeCota"+num+"')",
				null,
				"regiaoController.setNum(this)",
				"num", num));
				
		
		novaLinha.append(labelNum);
		novaLinha.append(numCota);
		novaLinha.append(labelNome);
		novaLinha.append(nomeCota);
		
		$("#idNumCota"+num, regiaoController.workspace).numeric();
		$("#idNomeCota"+num, regiaoController.workspace).autocomplete({source: ""});
		
		return novaLinha;
	},
	
	// FUNCTION - DELETAR LINHA VAZIA DO POP UP DE ADICIONAR COTA
	
	deletarInputVazio : function(elemento) {
		
		if(elemento.value.length===0) {
			regiaoController.processarLinhaAlterada();
			return true;
		} else {
			return false; 
		}
	},
	
	// FUNCTION - SET NUM
	
	setNum : function(elemento) {
		regiaoController.cotaAtual = elemento.getAttribute('num');
	},
	
	// FUNCTION - CONFIRMAR AUSENCIA DA COTA
	
	popupConfirmaAusenciaCota : function(cotas) {
		
		regiaoController.numCotas = cotas;
		
		var parametros = [];
		var numero;
		var idRegicaoSelecionada;
		
		$.each(cotas, function(index, num) {			
			//parametros.push({name:'numCotas['+ index +']', value: num});
			numero = parametros.push({name : 'numeroCota', value : num});
			idRegicaoSelecionada = $('#comboRegioes option:selected', regiaoController.workspace).val();
	  	});
		//JSON
	},
	
/* exemplo COTA AUSENTE
 * 	popupConfirmaAusenciaCota : function(cotas) {
		
		cotaAusenteController.numCotasAusente = cotas;
		
		var parametros = [];
		
		$.each(cotas, function(index, num) {			
			parametros.push({name:'numCotas['+ index +']', value: num});
	  	});
		
		$( "#dialog-confirm", cotaAusenteController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:350,
			modal: true,
			buttons: {
				"Suplementar": function() {
					
					$.postJSON(contextPath + "/cotaAusente/enviarParaSuplementar", 
							parametros, 
							function(result){
								$( "#dialog-confirm", cotaAusenteController.workspace ).dialog("close");
								if(result[1]!='SUCCESS')
									cotaAusenteController.popupNovaCotaAusente(true);
								else
									$('#idCotas tr').remove();
								
								cotaAusenteController.retornoEnvioSuplementar(result);
									
							}, null);									
				},
				"Redistribuir": function() {
					
					$.postJSON(contextPath + "/cotaAusente/carregarDadosRateio", 
							parametros, 
							cotaAusenteController.popupRateio);
					
					$( "#dialog-confirm", cotaAusenteController.workspace ).dialog("close");
				}				
			},
			form: $("#dialog-confirm", cotaAusenteController.workspace ).parents("form")
		});
	},
 */
	
	
	
	getInput : function(value,id, width,textAlign,onblur,onchange,onkeyup, classe, onfocusin, attr, attrValue) {
		
		
		var input = document.createElement("INPUT");
		input.type="text";
		input.name=id;
		input.id=id;
		input.value=value;
		input.style.setProperty("width",width);
				
		if(classe)
			input.setAttribute("class",classe);
		
		if(textAlign) {
			input.style.setProperty("text-align",textAlign);
		}
		
		if(onblur) {
			input.setAttribute("onblur",onblur);
		}
		
		if(onchange) {
			input.setAttribute("onchange",onchange);
		}
		
		if(onkeyup) {
			input.setAttribute("onkeyup",onkeyup);
		}
		
		if(onfocusin) {
			input.setAttribute("onfocusin",onfocusin);
		}
		
		if(attr) {
			input.setAttribute(attr,attrValue);
		}			
		
		return input;
	},
	
	processarLinhaAlterada : function() {
		
		var atual = regiaoController.cotaAtual;
		
		var num = $('#idNumCota' + atual).val();
		var nome = $('#idNomeCota' + atual).val();
		
		var isNew = atual.length === 0;
		
		var nomePreenchido = nome.length != 0;
		
		if( nomePreenchido ) {
			
			$("#idLinhaCota" + atual).remove();
			
			var cotaJaExiste = $("#idLinhaCota" + num , regiaoController.workspace).length>0;
			
			if( cotaJaExiste ) {				
				exibirMensagemDialog("WARNING",["Cota já foi selecionada."]);
				$('#idNumCota' + atual , regiaoController.workspace).val('');
				$('#idNomeCota' + atual , regiaoController.workspace).val('');
			}
			
			regiaoController.gerarLinhaCota(num,nome);
			
			var existeNovo = $( '#idNumCota').length > 0;
			
			if ( existeNovo)
				$( '#idNumCota', regiaoController.workspace).focus();
			else 			
				regiaoController.gerarLinhaCota('','');
						
		} else {
			
			if(!isNew) {
				$("#idLinhaCota" + atual,  regiaoController.workspace).remove();
				regiaoController.cotaAtual = '';
			}
		}
						
		$('#idNumCota').focus();
	},
	
	
	/*(
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	remove_cotas_grid : function(id) {
		
		regiao = $("#comboRegioes option:selected").val();
		
		$("#dialog-excluir").dialog({
			resizable : false,
			height : 170,
			width : 380,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					$.postJSON(contextPath + "/distribuicao/regiao/excluirRegistroCotaRegiao", 
							{id:id},
							function(result) {
//									$(".regioesCadastradasGrid", this.workspace).flexReload(),
									$("#dialog-excluir", this.workspace).dialog("close");

									var tipoMensagem = result.tipoMensagem;
									var listaMensagens = result.listaMensagens;
									
									if (tipoMensagem && listaMensagens) {
										
										exibirMensagem(tipoMensagem, listaMensagens);
									}
											
									$(".cotasRegiaoGrid", this.workspace).flexReload();
							},null);
					
					$(".regioesCadastradasGrid", regiaoController.workspace).flexOptions({
						url: contextPath + "/distribuicao/regiao/carregarCotasRegiao",
						dataType : 'json',
						params:[{
							name : 'filtro.id', value:regiao
						}]
					});
						
					$(".regioesCadastradasGrid", regiaoController.workspace).flexReload();		
					
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			},
			beforeClose: function() {
				clearMessageDialogTimeout('dialogMensagemNovo');
			},
			form: $("#dialog-excluir", this.workspace).parents("form"),
		});
	}
}, BaseController);
//@ sourceURL=regiao.js

