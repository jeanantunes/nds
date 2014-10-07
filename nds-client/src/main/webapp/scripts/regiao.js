var regiaoController = $.extend(true, {
	cotaAtual : '',
	numCotas : null,
	tableResultNMaiores : "", 
	init : function() {
		
		$('#numeroCota').change(function (){
			pesquisaCota.pesquisarPorNumeroCota('#numeroCota', '#nomeCota');
		});
	
		$(".faixaGrid", regiaoController.workspace).flexigrid({
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
				display : 'Adicionar',
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

		$("#lstCotasRankingGrid").flexigrid({
			preProcess : regiaoController.executarPreProcessSelCotas,
			dataType : 'json',
			colModel : [ {
				display : 'Cota',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left',
			}, {
				display : 'Nome',
				name : 'nomePessoa',
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
				display : 'Adicionar',
				name : 'sel',
				width : 20,
				sortable : true,
				align : 'center'
			} ],
			sortname : "codigo",
			sortorder : "asc",
			width : 600,
			height : 200
			
		});

		$("#lstProdutosGrid").flexigrid({
			preProcess : regiaoController.executarPreProcessLstProdutosGrid,
			dataType : 'json',
			colModel : [{
				display : 'add',
				name : 'sel',
				width : 30,
				sortable : true,
				align : 'center'
			}, {
				display : 'Código',
				name : 'codProduto',
				width : 60,
				sortable : true,
				align : 'left',
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 50,
				sortable : true,
				align : 'left',
			}, {
				display : 'Classificação',
				name : 'descricaoClassificacao',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Venda',
				name : 'venda',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Data Lanc.',
				name : 'dataLcto',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Data Rec.',
				name : 'dataRcto',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Status',
				name : 'status',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Capa',
				name : 'capa',
				width : 40,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 680,
			height : 200
		});

		$("#nMaioresGrid").flexigrid({
//			preProcess : regiaoController.executarPreProcessNMaioresGrid,
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codProduto',
				width : 60,
				sortable : true,
				align : 'left',
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 250,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Classificação',
				name : 'descricaoClassificacao',
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
			preProcess : regiaoController.executarPreProcessSegmentosGrid,
			dataType : 'json',
			colModel : [ {
				display : 'Número',
				name : 'numeroCota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeCota',
				width : 220,
				sortable : true,
				align : 'left'
			}, {
				display : 'Tipo de PDV',
				name : 'tipoPDV',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Status',
				name : 'tipoStatus',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Add',
				name : 'sel',
				width : 20,
				sortable : true,
				align : 'center'
			} ],
			sortname : "codigo",
			sortorder : "asc",
			width : 600,
			height : 200
		});

		$(".classificacaoGrid").flexigrid({
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
		if(!resultado.rows.length){
			$('#spanArquivoRegiaoCadastradas').hide();
			$('#spanImprimirRegiaoCadastradas').hide();			
		}else {
			$('#spanArquivoRegiaoCadastradas').show();
			$('#spanImprimirRegiaoCadastradas').show();
		}
		
		$.each(resultado.rows, function(index, row) {
			
			var linkDetalhe = '<a href="javascript:;" isEdicao="true" onclick="regiaoController.remove_cotas_grid('+row.cell.registroCotaRegiaoId+');" style="cursor:pointer">' +
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
			var checkBox = null;
			
			if(row.cell.idRegiao){
				
				if(row.cell.isFixa){
					checkBox = '<input type="checkbox" name="fixa" onchange="regiaoController.alterarRegiao('+row.cell.idRegiao+')" value="'+row.cell.isFixa+'" id="fixa" checked>';				
				}else{
					checkBox = '<input type="checkbox" name="fixa" onchange="regiaoController.alterarRegiao('+row.cell.idRegiao+')" value="'+row.cell.isFixa+'" id="fixa">';
				}
				
				row.cell.isFixa = checkBox;
				
				var linkExcluir = '<a href="javascript:;" onclick="regiaoController.excluirRegiao('+row.cell.idRegiao+');" style="cursor:pointer">' +
				'<img src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0" />'+	
				'</a>';
				row.cell.acao = linkExcluir;
			}
						
		});
		
		$(".grids", regiaoController.workspace).show();
		
		return resultado;
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
			
			var checkAll;
			
			if($("#todos").is(":checked")){
				checkAll = '<input type="checkbox" name="cotaSelected" id="cotaSelected" checked value='+row.cell.numeroCota+'>';
			}else{
				checkAll = '<input type="checkbox" name="cotaSelected" id="cotaSelected" unchecked value='+row.cell.numeroCota+'>';
			}

			row.cell.sel = checkAll;
			
		});
		
		$(".grids", regiaoController.workspace).show();
		
		return resultado;
		
	},
	
	rankingResult:new Array(),

	//PreProcess ranking Nmaiores
	executarPreProcessSelCotas : function(resultado){
		
		if (resultado.mensagens) {
			exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
			);
			
			$("#lstCotasRankingGrid", regiaoController.workspace).hide();
			
			return resultado;
		}
		
		//Resultado da pesquisa
		regiaoController.rankingResult = resultado.rows;
		
		$.each(resultado.rows, function(index, row) {
			
			var checkAll;
			
			if($("#selTodasCotas").is(":checked")){
				checkAll = '<input type="checkbox" name="rankingCotaSelected" id="rankingCotaSelected" checked value='+row.cell.numeroCota+'>';
			}else{
				checkAll = '<input type="checkbox" name="rankingCotaSelected" id="rankingCotaSelected" unchecked value='+row.cell.numeroCota+'>';
			}
			
			row.cell.sel = checkAll;
			
		});
		
		$("#lstCotasRankingGrid", regiaoController.workspace).show();
		
		regiaoController.tableResultNMaiores = resultado;
		
		return resultado;
		
	},

	// PREPROCESS REGI�O AUTOM�TICA - SEGMENTO
	
	executarPreProcessSegmentosGrid : function (resultado){
		if (resultado.mensagens) {
			
			exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
			);
			
			$(".porSegmento", regiaoController.workspace).hide();
			
			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {
			
			var checkAll = '<input type="checkbox" name="cotaSegmentoSelected" id="cotaSegmentoSelected" checked value='+row.cell.numeroCota+'>';
			
			row.cell.sel = checkAll;
			
		});
		
		$(".porSegmento", regiaoController.workspace).show();
		
		return resultado;
	},
	
	
	// PREPROCESS REGIAO AUTOMATICA - N_MAIORES - Lista Produtos
	
	produtosResult:new Array(),
	executarPreProcessLstProdutosGrid : function (resultado){
		if (resultado.mensagens) {
			
			exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
			);
			
			$("#lstProdutosGrid", regiaoController.workspace).hide();
			
			return resultado;
		}
		
		var codProduto = $('#idCodigo').val();
		
		//Resultado da pesquisa
		
		regiaoController.produtosResult = resultado.rows;
		
		$.each(resultado.rows, function(index, row) {
			
			var checkAll;
			
			if($("#selTodosProdutos").is(":checked")){
				checkAll = '<input type="checkbox" name="prodNMaioresSelected" id="prodNMaioresSelected" checked value='+index+'>';
			}else{
				checkAll = '<input type="checkbox" name="prodNMaioresSelected" id="prodNMaioresSelected" unchecked value='+index+'>';
			}
			
			if(row.cell.reparte == undefined){
				row.cell.reparte = '';
			}
			
			if(row.cell.venda == undefined){
				row.cell.venda = '';
			}
			
			row.cell.sel = checkAll;
			
			var capa = '<a href="javascript:;" onmouseover="regiaoController.popUpCapaOpen('+codProduto+','+row.cell.numeroEdicao+', event);" onmouseout="regiaoController.popUpCapaClose(event);" style="cursor:pointer">'+ 
					   '<img src="' + contextPath + '/images/ico_detalhes.png" hspace="5" border="0" />'+
					   '</a>'; 
			
			row.cell.capa = capa;
			
		});
		
		$("#lstProdutosGrid", regiaoController.workspace).show();
		
		return resultado;
	},
	
	executarPreProcessNMaioresGrid : function (resultado){
		if (resultado.mensagens) {
			
			exibirMensagem(
					resultado.mensagens.tipoMensagem, 
					resultado.mensagens.listaMensagens
			);
			
			$("#nMaioresGrid", regiaoController.workspace).hide();
			
			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {
			
			var linkDetalhe = '<a href="javascript:;" onclick="regiaoController.remove_cotas_grid('+a+');" style="cursor:pointer">' +
								'<img src="' + contextPath + '/images/ico_excluir.gif" hspace="5" border="0" />'+	
							   '</a>';
			
			row.cell.acao = linkDetalhe;
			
		});
		
		$("#nMaioresGrid", regiaoController.workspace).show();
		
		return resultado;
	},

	
	// -- FUNCTIONS --
	

	//	FUNCTIONS - REGIÃO
	
	
	carregarRegiao : function (){
		var regiao = $("#comboRegioes option:selected").val();
		
		
		if (regiao != "Selecione..."){
			
		$(".cotasRegiaoGrid", this.workspace).flexOptions({
			url: contextPath + "/distribuicao/regiao/carregarCotasRegiao",
			dataType : 'json',
			params:[{
				name : 'filtro.id', value:regiao
			}]
			
		});
		
		$(".cotasRegiaoGrid", this.workspace).flexReload();
		}
	},
	

	// FUNCTIONS - Manuten��o Regi�o

	manutencaoRegiao : function() {
		
		$(".regioesCadastradasGrid", this.workspace).flexOptions({
			url: contextPath + "/distribuicao/regiao/carregarRegiao",
			dataType : 'json'
		});
		$(".regioesCadastradasGrid").flexReload();		
		
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
	
	
	// FUNCTIONS - Alterar REGIAO

	alterarRegiao : function(id) {

		var idFixo = id;
		if(idFixo == id){
			idFixo = id;
		}
		
		$("#dialog-alterarRegiao").dialog({
			resizable : false,
			height : 170,
			width : 380,
			modal : true,
			
			buttons : {
				"Confirmar" : function(idFixo) {
					$.postJSON(contextPath + "/distribuicao/regiao/alterarRegiao", 
							{id:id},
							function(result) {
									var tipoMensagem = result.tipoMensagem;
									var listaMensagens = result.listaMensagens;
									
									if (tipoMensagem && listaMensagens) {
										
										exibirMensagem(tipoMensagem, listaMensagens);
									}
									$(".regioesCadastradasGrid").flexReload();
							   },
							   null,
							   true
					);
					$("#dialog-alterarRegiao").dialog("close");
				},
				"Cancelar" : function() {
					$("#dialog-alterarRegiao").dialog("close");
				}
			},
			beforeClose: function() {
				clearMessageDialogTimeout('dialogMensagemNovo');
			},
		});
	},
	
	
	// FUNCTION - CARREGAR COMBO REGI�O
	cotasDaRegiao : function(){
		var regiao = $("#comboRegioes option:selected").val();
		
		$(".cotasRegiaoGrid", this.workspace).flexOptions({
			url: contextPath + "/distribuicao/regiao/carregarCotasRegiao",
			dataType : 'json',
			params:[{
				name : 'filtro.id', value:regiao
			}]
		});
			
		$(".cotasRegiaoGrid").flexReload();		
	},
	
	
	// FUNCTION - ADD NOVA REGIAO
	addNovaRegiao : function() {

		$("#nomeRegiao").val("");
		$("#regiaoIsFixa").removeAttr('checked');
		
		var idRegiaoCadastrada = "";
		
		$("#dialog-addRegiao").dialog({

					resizable : false,
					height : 'auto',
					width : 350,
					modal : true,

					buttons : {
						"Confirmar" : function() {
							$(this).dialog("close");
							
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
									data, 
									function(result) 
									{
										var options = "<option selected=selected value=0>Selecione...</option>";
						                
										$.each(result.listaRegiao, function(key, tipoRegiao){
						                    options += '<option value="' + tipoRegiao.idRegiao + '">' + tipoRegiao.nomeRegiao + '</option>';
						                    
						                    if(tipoRegiao.nomeRegiao == nome){
						                    	idRegiaoCadastrada = tipoRegiao.idRegiao;
						                    }
						                 });
										
						                 $("#comboRegioes").html(options);
						                 
						                 if(idRegiaoCadastrada != ""){
						                	 $("#comboRegioes").val(idRegiaoCadastrada);
						                	 regiaoController.carregarRegiao();
						                 }
									});
							$(".grids", regiaoController.workspace).hide();
							
						},
						"Cancelar" : function() {
							$(this).dialog("close");
						}
					}
				});

	},
	

	
	// FUNCTION - EXCLUIR REGI�O
	excluirRegiao : function(id) {

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

									var tipoMensagem = result.tipoMensagem;
									var listaMensagens = result.listaMensagens;
									
									if (tipoMensagem && listaMensagens) {
										
										exibirMensagem(tipoMensagem, listaMensagens);
									}
					                 
									$("#comboRegioes option[value='"+id+"']").remove();
					                 
									$("#dialog-excluir").dialog("close");
									$("#dialog-novo").dialog("close");
									$(".grids", regiaoController.workspace).hide();
							   },
							   null,
							   true
					);
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			},
			
			beforeClose: function() {
				clearMessageDialogTimeout('dialogMensagemNovo');
			},
		});
	},
	
	
// FUNCTION - REGIAO AUTOM�TICA - Dialog Principal
	
	regiaoAutomatica : function() {

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
	
	// FUNCTION - REGIÃO AUTOMÁTICA - GRID PRINCIPAL
	addCotasRegAutomatica : function() {
		
		$('#qtdCotasRanking').val("");
		
		$("#dialog-cotas").dialog({
			resizable : false,
			height : 550,
			width : 650,
			modal : true,
			open:function(){
				produtosEscolhidosArray=new Array();
				var clearData = {
				        total: 0,    
				        page:1,
				        rows: []
				};
				
				
				$("#nMaioresGrid").flexAddData(clearData);
			},
			buttons : {
//				"Confirmar" : function() {
//					var idRegiaoSelecionada = $('#comboRegioes option:selected', regiaoController.workspace).val();
//
//					$(".regioesCadastradasGrid", regiaoController.workspace).flexOptions({
//						url: contextPath + "/distribuicao/regiao/carregarCotasRegiao",
//						dataType : 'json',
//							params: [{name: "filtro.id", value: idRegiaoSelecionada}]
//					});
//
//					$(".regioesCadastradasGrid", regiaoController.workspace).flexReload();
//					$(".faixaGrid", regiaoController.workspace).flexReload();
//					$(this).dialog("close");
//
//					regiaoController.limparCamposAddAutomatica();
//
//					//limpando grid utilizado
//					clearData = {
//					        total: 0,
//					        page:1,
//					        rows: []
//					};
//
//					$("#nMaioresGrid").flexAddData(clearData);
//
//				},
				"Voltar" : function() {
					$(this).dialog("close");
					regiaoController.limparCamposAddAutomatica();

					$("#nMaioresGrid").flexAddData(data);
					
					//limpando grid utilizado
					clearData = {
					        total: 0,    
					        page:1,
					        rows: []
					};
					
					
					$("#nMaioresGrid").flexAddData(clearData);
					
				}
			}
		});
	},
	
	limparCamposAddAutomatica : function (){
		
		regiaoController.filtroInit();
		
		$("#radio").attr('checked', true);
		$("#radio2").attr('checked', false);
		$("#radio3").attr('checked', false);
		$("#cepInicialPart1").val("");
		$("#cepInicialPart2").val("");
		$("#cepFinalPart1").val("");
		$("#cepFinalPart2").val("");
		$("#qtdCotas").val("");
		$("#comboSegmento").val("");
		
	},
 	

	// FUNCTIONS - CARREGAR SEGMENTOS
	
	carregarSegmento : function() {

		$(".segmentosGrid", this.workspace).flexOptions({
			url: contextPath + "/distribuicao/regiao/carregarSegmentos",
			dataType : 'json',
		});
	},

	
	filtroNMaiores : function(){
		$('#lstProdutosGrid').show();
		
		$("#lstProdutosGrid").flexOptions({
			url: contextPath + "/distribuicao/regiao/buscarProduto",
			dataType : 'json',
			params : regiaoController.obterFiltroProdNMaiores(), newp: 1
		});
		
		$("#lstProdutosGrid").flexReload();
		
	},
	
	obterFiltroProdNMaiores : function(){

		var data = [];
		
		var codigo = $("#idCodigo").val();
		var nomeProduto = $("#nomeProduto").val();
		var classificacao = $("#comboClassificacao").val();
		
		data.push({name : 'filtro.codigoProduto', value:codigo});
		data.push({name : 'filtro.nome', value:nomeProduto});
		
		if(classificacao != "Selecione..."){
			data.push({name : 'filtro.idTipoClassificacaoProduto', value:classificacao});
		}else{
			classificacao = 0;
			data.push({name : 'filtro.idTipoClassificacaoProduto', value:classificacao});
		}
		
		return data;
	},
	
	//FUNCTIONS ADD REGI�O AUTOM�TICA  
	
	// FUNCTION - ADD EM LOTE
	
	cotasLote : function() {
		var idRegiaoSelecionada = $('#comboRegioes option:selected', regiaoController.workspace).val();
		
		$("#dialog-lote").dialog({
			resizable : false,
			height : 250,
			width : 350,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					$(this).dialog("close");
					
					$("#arquivoUpLoad").ajaxSubmit({
						beforeSubmit: function(arr, formData, options) {
						},
						success: function(responseText, statusText, xhr, $form)  { 
							var mensagens = (responseText.mensagens) ? responseText.mensagens : responseText.result;   
							var tipoMensagem = mensagens.tipoMensagem;
							var listaMensagens = mensagens.listaMensagens;

							if (tipoMensagem && listaMensagens) {
								
								if (tipoMensagem != 'SUCCESS') {
									
									exibirMensagemDialog(tipoMensagem, listaMensagens, 'dialogMensagemNovo');
								}
								$("#dialog-lote").dialog( "close" );
								regiaoController.cotasDaRegiao();
								exibirMensagem(tipoMensagem, listaMensagens);	
							}
						}, 
						url:  contextPath + '/distribuicao/regiao/addLote',
						type: 'POST',
						dataType: 'json',
						data: { idRegiao : idRegiaoSelecionada }
					});
					
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			}
		});

	},
	
	// FUNCTION - ADD PRODUTOS

	add_produtos : function() {
		
		$("#idCodigo").val("");
		$("#nomeProduto").val("");
		$("#comboClassificacao").val("");
		
		$('#selTodosProdutos').attr('checked', false);
		
		$('#lstProdutosGrid').hide();
		
		$("#dialog-addNMaiores").dialog({
			resizable : false,
			height : 520,
			width : 710,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					
					$("#lstProdutosGrid input[type=checkbox][name=prodNMaioresSelected]:checked").each(function() {
						validatorProdEscolhidos.push(regiaoController.produtosResult[$(this).val()]);
					});
					
					
					if(validatorProdEscolhidos.length == 0){
						var erros = new Array();
				           erros[0] = "Nenhum produto selecionado. Selecione no mínimo 01 produto para inserção ou clique em cancelar para fechar a janela.";
				           exibirMensagemDialog('WARNING',   erros,"");
				           
				           validatorProdEscolhidos.length= 0;
				           return;
					}else{
						
					if((validatorProdEscolhidos.length > 0) && (validatorProdEscolhidos.length <= 6)){
					
					$("#lstProdutosGrid input[type=checkbox][name=prodNMaioresSelected]:checked").each(function() {
						produtosEscolhidosArray.push(regiaoController.produtosResult[$(this).val()]);
					});
					
					for(var i=0; i < produtosEscolhidosArray.length; i++){
						produtosEscolhidosArray[i].cell.acao="<input type='image' class='btnExcluir' onclick='removeProdutoEscohido(this.value);' value='"+i+"' style='cursor:pointer' src='" + contextPath + "/images/ico_excluir.gif'/>";
					}
					
						var data = {
								total: produtosEscolhidosArray.length,    
								page:1,
								rows: produtosEscolhidosArray
						};
						
						
						$("#nMaioresGrid").flexAddData(data);
				
						validatorProdEscolhidos.length= 0;
						
						$(this).dialog("close");
					
					}else{
						var erros = new Array();
				           erros[0] = "Selecione no máximo 06 edicões.";
				           exibirMensagemDialog('WARNING',   erros,"");
				           
				           validatorProdEscolhidos.length= 0;
				           return;
				           
					}
					}
					
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			},
			beforeClose: function() {
				clearData = {
				        total: 0,
				        page:1,
				        rows: []
				};
				$("#lstProdutosGrid").flexAddData(clearData);
			}
		});

	},

	
	add_cotas : function() {
		
		$("#dialog-confirmacao").dialog({
			resizable : false,
			height : 150,
			width : 200,
			modal : true,
			buttons : {"Confirmar" : function() {
					$(this).dialog("close");
					$("#effect").show("highlight", {}, 1000, callback);
					
					var idRegiaoSelecionada = $('#comboRegioes option:selected', regiaoController.workspace).val();
					var cotas = [];

					$("input[type=checkbox][name='cotaSelected']:checked").each(function() {
						if(this.value.length>0)
							cotas.push({name:'cotas', value:this.value});
					});

					
					if(cotas.length > 0){
				
						// adicionando a regiao
						cotas.push({name:'idRegiao', value: idRegiaoSelecionada});
						
						$.postJSON(contextPath + "/distribuicao/regiao/incluirCota",
								cotas, 
								function(result) {
							
							var tipoMensagem = result.tipoMensagem;
							var listaMensagens = result.listaMensagens;
							
							if (tipoMensagem && listaMensagens) 
								exibirMensagem(tipoMensagem, listaMensagens);
							
							regiaoController.cotasDaRegiao();
						},
						
						function(result) {
							
							regiaoController.cotasDaRegiao();
						}
						
						);
						
					}else{
						var erros = new Array();
				           erros[0] = "Nenhuma cota selecionada. Volte e selecione uma cota para adição!";
				           exibirMensagemDialog('WARNING',   erros,"");

				           this.closeDialogPopUpSegmento1 = false;

				           return;
					}
			},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			}
		});

	},
	
	add_cotas_Segmento : function() {
		$("#dialog-confirmacao").dialog({
			resizable : false,
			height : 150,
			width : 200,
			modal : true,
			buttons : {"Confirmar" : function() {
					$(this).dialog("close");
					$("#effect").show("highlight", {}, 1000, callback);
					var idRegiaoSelecionada = $('#comboRegioes option:selected', regiaoController.workspace).val();
					var cotas = [];

					$("input[type=checkbox][name='cotaSegmentoSelected']:checked").each(function() {
						if(this.value.length>0)
							cotas.push({name:'cotas', value:this.value});
					});

					if(cotas.length > 0){
						
						// adicionando a regi�o
						cotas.push({name:'idRegiao', value: idRegiaoSelecionada});
						
						$.postJSON(contextPath + "/distribuicao/regiao/incluirCota",
								cotas, 
								function(result) {

							var tipoMensagem = result.tipoMensagem;
							var listaMensagens = result.listaMensagens;
							
							if (tipoMensagem && listaMensagens) 
								exibirMensagem(tipoMensagem, listaMensagens);
							
							regiaoController.cotasDaRegiao();
						},
						
							function(result) {
							
								regiaoController.cotasDaRegiao();
							}
						
						);
						
					}else{
						var erros = new Array();
				           erros[0] = "Nenhuma cota selecionada. Volte e selecione uma cota para adição!";
				           exibirMensagemDialog('WARNING',   erros,"");

				           this.closeDialogPopUpSegmento1 = false;

				           return;
					}
					
			},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			}
		});

	},
	
	
	// add Produtos no Grid NMaiores, para pesquisa de cotas 
	addProdutosParaPesquisa : function() {
		$("#dialog-AddProdutos").dialog({
			resizable : false,
			height : 150,
			width : 200,
			modal : true,
			buttons : {"Confirmar" : function() {
					$(this).dialog("close");
					$("#effect").show("highlight", {}, 1000, callback);
					
					var produtos = [];

					$("input[type=checkbox][name='prodNMaioresSelected']:checked").each(function() {
						if(this.value.length>0)
							produtos.push({name:'Produtos', value:this.value});
					});

					// adicionando a regi�o
					cotas.push({name:'idRegiao', value: idRegiaoSelecionada});
					
					$.postJSON(contextPath + "/distribuicao/regiao/incluirCota",
							cotas, 
							function(result) {

						var tipoMensagem = result.tipoMensagem;
						var listaMensagens = result.listaMensagens;
						
						if (tipoMensagem && listaMensagens) 
							exibirMensagem(tipoMensagem, listaMensagens);
						
						regiaoController.cotasDaRegiao();
					},
					function(result) {
						
						regiaoController.cotasDaRegiao();
					});
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			}
		});

	},
	
	
	// FUNCTION - VERIFICA SE A REGI�O � FIXA 	
	isFixa : function(campo) {
		
		var elemento = $("#" + campo, regiaoController.workspace);
		
		if(elemento.attr('type') == 'ckckbox') {
			return (elemento.attr('checked') == 'checked') ;
		} else {
			return elemento.val();
		}
		
	},

	// FILTROS REGI�O AUTOM�TICA

	filtroInit: function() {
		$('.porCep').hide();
		$('.porSegmento').hide();
		$('.gridfaixaCep').hide();
		$('.gridNMaiores').hide();
		$('.gridsegmentos').hide();
		
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
		
		$("#qtdCotasRanking").val("");
	},

	// FUNCTION - FILTRO POR SEGMENTO
	filtroPorSegmento : function() {
		$('.porCep').hide();
		$('.porSegmento').show();
		$('.gridfaixaCep').hide();
		$('.gridNMaiores').hide();
		$('.gridsegmentos').hide();
	
	},
	
	// REGI�O AUTOM�TICA GRID'S
	// FUNCTION - MOSTRAR POR CEP
	
	
// FUNCTION - MOSTRAR POR N-MAIORES
	mostrarPorNMaiores : function() {
		$('.gridfaixaCep').hide();
		$('.gridNMaiores').show();
		$('.gridsegmentos').hide();
	},
	

	validarDadosParaRanking : function() {
		
		$('#numeroCota').val("");
		$('#nomeCota').val("");
		
		var isValid = true;
		var codValidado = new Array();
		var edicaoValidada = new Array();
		
		if((produtosEscolhidosArray.length == 0) || (produtosEscolhidosArray == "")){
			 var erros = new Array();
	           erros[0] = "Inclua no mínimo 01 produto.";
	           exibirMensagemDialog('WARNING',   erros,"");

	           return;
		}
		
		if(produtosEscolhidosArray.length <= 6){
			
			for(var i=0; i < produtosEscolhidosArray.length; i++){
				
				for(var u=i+1; u < produtosEscolhidosArray.length; u++){
					
					if(produtosEscolhidosArray[i].cell.numeroEdicao == produtosEscolhidosArray[u].cell.numeroEdicao){
						isValid = false;
					}
				}
				codValidado[i] = produtosEscolhidosArray[i].cell.codProduto;
				edicaoValidada[i] = produtosEscolhidosArray[i].cell.numeroEdicao;
			}
		
		}else{
			isValid = false;
		}
		
		
		if (isValid == true){
			
			regiaoController.rankingNMaiores(codValidado, edicaoValidada);
			
		}else{
			 var erros = new Array();
	           erros[0] = "Os produtos selecionados só podem ser até 6 edições do mesmo produto ou até 6 produtos com edições diferentes";
	           exibirMensagemDialog('WARNING',   erros,"");

	           return;
	           isValid = true;
		}
	},	
	
	
	rankingNMaiores : function(codValidado, edicaoValidada) {
			
			var limitePesquisa = $("#qtdCotasRanking").val();
			
			if((limitePesquisa < 0) || (limitePesquisa == "")){
				var erros = new Array();
		           erros[0] = "Insira a quantidade de cotas.";
		           exibirMensagemDialog('WARNING',   erros,"");

		           return;
			}else{
			
			$("#lstCotasRankingGrid").flexOptions({
				url: contextPath + "/distribuicao/regiao/rankingCota",
				dataType : 'json',
				params: [{name: "filtro.codigoProduto", value: codValidado},
				         {name: "filtro.numeroEdicao", value: edicaoValidada},
				         {name: "filtro.limitePesquisa", value: limitePesquisa}]
			});
			
				$("#lstCotasRankingGrid").flexReload();
			
			$("#dialog-rankingCotas").dialog({
				resizable : false,
				height : 520,
				width : 645,
				modal : true,
				buttons : {
					"Confirmar" : function() {
						
						var idRegiaoSelecionada = $('#comboRegioes option:selected', regiaoController.workspace).val();
						var cotas = new Array();

						$("input[type=checkbox][name='rankingCotaSelected']:checked").each(function() {
							cotas.push({name:'cotas', value: $(this).val()});
						});
						
						if(cotas.length > 0){
							
							// adicionando a regi�o
							cotas.push({name:'idRegiao', value: idRegiaoSelecionada});
							
							$.postJSON(contextPath + "/distribuicao/regiao/incluirCota",
									cotas, 
									function(result) {
								
								var tipoMensagem = result.tipoMensagem;
								var listaMensagens = result.listaMensagens;
								
								if (tipoMensagem && listaMensagens) 
									exibirMensagem(tipoMensagem, listaMensagens);
								
								regiaoController.cotasDaRegiao();
								$("#dialog-rankingCotas").dialog("close");
							},
							function(result) {
								
								regiaoController.cotasDaRegiao();
							});
						}else{
							var erros = new Array();
					           erros[0] = "Nenhuma cota selecionada. Volte e selecione uma cota para adição!";
					           exibirMensagemDialog('WARNING',   erros,"");
					           return;
						}
						
						cotasRankingNMaioresArray.length = 0;
						
					},
					"Cancelar" : function() {
						$(this).dialog("close");
						
						cotasRankingNMaioresArray.length = 0;
					}
				}
			});
			}
	},
	
	
	filtroCotaRanking : function (){
		
		var numCota = $("#numeroCota").val();
		var cotaEncontrada = "";
		
		for(var i=0; i<regiaoController.tableResultNMaiores.total; i++){
		
			if(regiaoController.tableResultNMaiores.rows[i].cell.numeroCota == numCota){
				cotaEncontrada = regiaoController.tableResultNMaiores.rows[i].cell.numeroCota;
			}
		}
		
		if (cotaEncontrada != ""){
			
			$("#lstCotasRankingGrid").flexOptions({
				url: contextPath + "/distribuicao/regiao/filtroRankingCota",
				dataType : 'json',
				params: [{name: "numCota", value: cotaEncontrada}]
			});
			
				$("#lstCotasRankingGrid").flexReload();
			
		}else{
			 var erros = new Array();
	           erros[0] = "Cota não encontrada";
	           exibirMensagemDialog('WARNING',   erros,"");

	           return;
		}
		cotaEncontrada.length = 0;
		
	},
	
	
	// FUNCTION - MOSTRAR POR SEGMENTO

	mostrarPorSegmento : function() {
		$('.gridsegmentos').show();
		$("#todosSegmento").attr('checked', true);
	
			var data = [];
			
			var segmento = $("#comboSegmento option:selected").val();
			var limite = $("#qtdCotas").val();
			
			if((segmento == "") || (segmento == "Selecione...") || (limite == "")){
				
				var erros = new Array();
				erros[0] = "O segmento e a quantidade de cotas são obrigatórios.";
				exibirMensagemDialog('WARNING',   erros,"");
				
				$('.gridsegmentos').hide();
				return;

			}else{
			
				data.push({name:'filtro.idSegmento', value: segmento});
				data.push({name:'filtro.limiteBuscaPorSegmento', value: limite});
		
				$("#segmentosGrid").flexOptions({
					url: contextPath + "/distribuicao/regiao/buscarPorSegmento",
					dataType : 'json',
					params : data
				});

				$("#segmentosGrid").flexReload();
			}
		
	},
	
	mostrarPorCep : function() {
		$('.gridfaixaCep').show();
		$('.gridNMaiores').hide();
		$('.gridsegmentos').hide();
		
		$("#faixaGrid").flexOptions({
			url: contextPath + "/distribuicao/regiao/buscarPorCep",
			dataType : 'json',
			params : regiaoController.obterRangeDeCep()
		});
		
		$("#faixaGrid").flexReload();	
		
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
	
	// FUNCTION - SEGMENTOS
	comboSegmento : function(){
		var segmento = $("#comboSegmento option:selected").val();
		
		$(".cotasRegiaoGrid", this.workspace).flexOptions({
			url: contextPath + "/distribuicao/regiao/carregarCotasRegiao",
			dataType : 'json',
			params:[{
				name : 'filtro.id', value:segmento
			}]
		});
			
		$(".cotasRegiaoGrid", this.workspace).flexReload();
		$(".regioesCadastradasGrid", this.workspace).flexReload();
	},
	

	// FUNCTION - POP UP DETALHES
	
	popup_detalhes : function() {

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

	// FUNCTION - ADD COTAS NA REGI�O [bot�o add Cotas]
	
	popupAddCotaRegiao : function(evitarReset) {
		
		if(!evitarReset) {
			$('#idCotas tr', regiaoController.workspace).remove();
			regiaoController.gerarLinhaCota('','');
		}
		
		$( "#dialog-addCota").dialog({
			resizable: false,
			height:'auto',
			width:530,
			modal: true,
			buttons: {

				// CONFIRMAR ADI��O DE COTA NA REGIAO
				
				"Confirmar": function() {
					$(this).dialog("close");
					mostrar();
					
					var idRegiaoSelecionada = $('#comboRegioes option:selected', regiaoController.workspace).val();
					var cotas = [];

					$('.cotaOrigem').each(function() {
						if(this.value.length>0)
							cotas.push({name:'cotas', value:this.value});
					});
					
					// adicionando ao array cotas.
					cotas.push({name:'idRegiao', value: idRegiaoSelecionada});
					
					if(cotas.length === 0) {
						exibirMensagemDialog("WARNING",["Selecione uma ou mais cotas."]);	
						return;
					}
					
					$.postJSON(contextPath + "/distribuicao/regiao/incluirCota",
							cotas, function(result) {
						
						var tipoMensagem = result.tipoMensagem;
						var listaMensagens = result.listaMensagens;
						
						if (tipoMensagem && listaMensagens) {
							
							exibirMensagem(tipoMensagem, listaMensagens);
						}
						
						regiaoController.cotasDaRegiao();
						
					},
					
					function(result) {
						
						regiaoController.cotasDaRegiao();
					}
					
					);
					
					$( this ).dialog( "close" );
					$("#idCotas").html("");
				},
				"Cancelar": function() {
					
					$("#idCotas").html("");
					$( this ).dialog( "close" );
				}				
			},
		});
	},
	
	// FUNCTION - PEGAR DADOS VIA JSON, PARA ENVIAR PRA CONTROLLER
	
	getDados : function(){
		var data = [];
		data.push({name:'idRegiao',		value: regiaoController.get("idRegiaoSelecionada")});
		data.push({name:'cotasCadastradasNaRegiao',		value: regiaoController.get("cotas")});
	},
	
	
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
			
			var idRegiao = $('#comboRegioes').val();
			
			$.postJSON(contextPath + "/distribuicao/regiao/validarRegiaoCota",
					{cota:num, idRegiao:idRegiao}, function(result) {
				
				var tipoMensagem = result.tipoMensagem;
				var listaMensagens = result.listaMensagens;
				
				if (tipoMensagem && listaMensagens) {
					
					exibirMensagem(tipoMensagem, listaMensagens);
				}
				
			});
			
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
	 
	
	checkAll : function() {
		var valor = $("#todos").is(":checked");
		$("input[type=checkbox][name='cotaSelected']").attr("checked", valor);
	},
	
	checkAllSegmento : function() {
		var valor = $("#todosSegmento").is(":checked");
		$("input[type=checkbox][name='cotaSegmentoSelected']").attr("checked", valor);
	},
	
	checkAllNMaiores : function() {
		var valor = $("#selTodosProdutos").is(":checked");
		$("input[type=checkbox][name='prodNMaioresSelected']").attr("checked", valor);
	},
	
	checkAllRankingNMaiores : function() {
		var valor = $("#selTodasCotas").is(":checked");
		$("input[type=checkbox][name='rankingCotaSelected']").attr("checked", valor);
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
		   open: regiaoController.open(event, produto),
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
	
	//Remover cota da Regiao
	remove_cotas_grid : function(id) {
		
		regiao = $("#comboRegioes option:selected").val();
		
		$("#dialog-excluirCota").dialog({
			resizable : false,
			height : 170,
			width : 380,
			modal : true,
			buttons : {
				"Confirmar" : function() {
					$.postJSON(contextPath + "/distribuicao/regiao/excluirCotaDaRegiao", 
							{id:id},
							function(result) {
									$("#dialog-excluirCota", this.workspace).dialog("close");

									var tipoMensagem = result.tipoMensagem;
									var listaMensagens = result.listaMensagens;
									
									if (tipoMensagem && listaMensagens) {
										
										exibirMensagem(tipoMensagem, listaMensagens);
									}
											
									$(".regioesCadastradasGrid", regiaoController.workspace).flexOptions({
										url: contextPath + "/distribuicao/regiao/carregarCotasRegiao",
										dataType : 'json',
										params:[{
											name : 'filtro.id', value:regiao
										}]
									});
										
									$(".cotasRegiaoGrid").flexReload();
									
							},null);
									$(this).dialog("close");
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			},
			beforeClose: function() {
				clearMessageDialogTimeout('dialogMensagemNovo');
			},
			form: $("#dialog-excluirCota", this.workspace).parents("form"),
		});
	}
}, BaseController);
//@ sourceURL=regiao.js
