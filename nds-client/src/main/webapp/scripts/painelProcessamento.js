var painelProcessamentoController = $.extend(true, {
	init : function(path) {
		this.contextPath = path;
		this.initGridDetalheInterfaceGrid();
		this.initGridDetalheProcessamentoGrid();
		this.initGridPainelInterfaceGrid();
		this.alterarProcessamento();
		this.bindButtonsInterfaces();
	},
	initGridDetalheInterfaceGrid : function() {
		$(".detalheInterfaceGrid", painelProcessamentoController.workspace).flexigrid({
			preProcess : painelProcessamentoController.executarPreInterface,
			dataType : 'json',
			colModel : [ {
				display : 'Ação',
				name : 'acao',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Local',
				name : 'local',
				width : 330,
				sortable : true,
				align : 'left'
			}, {
				display : 'Detalhe',
				name : 'detalhe',
				width : 210,
				sortable : true,
				align : 'left'
			}],
			sortname : "acao",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,			
			width : 700,
			height : 200
		});
	},
	initGridDetalheProcessamentoGrid : function() {
		$(".detalheProcessamentoGrid", painelProcessamentoController.workspace).flexigrid({
			preProcess : painelProcessamentoController.executarPreInterfaceProcessamento,
			dataType : 'json',
			colModel : [ {
				display : 'Estado',
				name : 'tipoErro',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Mensagem Usuário',
				name : 'mensagem',
				width : 330,
				sortable : true,
				align : 'left'
			}, {
				display : 'Mensagem de Sistema',
				name : 'numeroLinha',
				width : 210,
				sortable : true,
				align : 'left'
			}],
			sortname : "Default",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,			
			width : 700,
			height : 200
		});
	},
	initGridPainelInterfaceGrid : function() {
		$(".painelInterfaceGrid", painelProcessamentoController.workspace).flexigrid({
			preProcess: painelProcessamentoController.executarPreProcessamentoInterfaceGrid,
			dataType : 'json',
			colModel : [ {
				display : 'Interface',
				name : 'descricaoInterface',
				width : 245,
				sortable : false,
				align : 'left'
			}, {
				display : 'Arquivo',
				name : 'nomeArquivo',
				width : 100,
				sortable : false,
				align : 'left'
			}, {
				display : 'Extensão',
				name : 'extensaoArquivo',
				width : 60,
				sortable : false,
				align : 'left'
			}, {
				display : 'Status',
				name : 'status',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Processamento',
				name : 'dataProcessmento',
				width : 120,
				sortable : true,
				align : 'center'
			}, {
				display : 'Hora Processamento',
				name : 'horaProcessamento',
				width : 140,
				sortable : false,
				align : 'center'
			}, {
				display : 'Reprocessar',
				name : 'reprocessar',
				width : 95,
				sortable : false,
				align : 'center'
			}],
			sortname : "descricaoInterface",
			sortorder : "asc",
			usepager : false,
			useRp : false,
			rp : 0,
			showTableToggleBtn : true,
			width : 930,
			height : 'auto'
		});
	},
	popup : function(idLogProcessamento, dataProcessamento, horaProcessamento) {
		$("#nomeInterface").html(idLogProcessamento);
		$("#dataProcessamento").html(dataProcessamento);
		$("#horaProcessamento").html(horaProcessamento);
		$( "#dialog-novo", painelProcessamentoController.workspace ).dialog({
			resizable: false,
			height:430,
			width:750,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );					
				}
			},
			form: $("#dialog-novo", this.workspace).parents("form")
		});
	},
	popup_detalhes : function() {
		$( "#dialog-detalhes", painelProcessamentoController.workspace ).dialog({
			resizable: false,
			height:430,
			width:750,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );					
				}
			},
			form: $("#dialog-detalhes", this.workspace).parents("form")
		});
	},
	popup_sistema : function() {
		$( "#dialog-operacional", painelProcessamentoController.workspace ).dialog({
			resizable: false,
			height:'auto',
			width:400,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );					
				}
			},
			form: $("#dialog-operacional", this.workspace).parents("form")
		});
	},
	
	alterarProcessamento : function() {
		
		painelProcessamentoController.bindButtonsInterfaces();
		
		var codigoDistribuidor = $("#toggleFornecedores :radio:checked").val();
		
		if (codigoDistribuidor == "") {
			
			$(".areaBts", painelProcessamentoController.workspace).hide();
			$(".grids", painelProcessamentoController.workspace).hide();
			$("#divProcessamento", painelProcessamentoController.workspace).show();
			
			return;
		}
		
		
		$("#divProcessamento", painelProcessamentoController.workspace).hide();
		$(".areaBts", painelProcessamentoController.workspace).show();
		$(".grids", painelProcessamentoController.workspace).show();
		
		$(".painelInterfaceGrid", painelProcessamentoController.workspace).flexOptions({
			url : contextPath + '/administracao/painelProcessamento/pesquisarInterfaces',
			params: [{name: 'codigoDistribuidor', value: codigoDistribuidor}],
			newp: 1,
		});
		$(".painelInterfaceGrid", painelProcessamentoController.workspace).flexReload();
	},
	
	executarPreProcessamentoInterfaceGrid : function(resultado) {
		
		if (resultado.mensagens) {
			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			return resultado;
		}
		
		var btReprocessamento = "";
		var brDetalhes        = "";
		
		$.each(resultado.rows, function(index, row) {

			btReprocessamento = "<a href='javascript:;' onclick='painelProcessamentoController.reprocessarInterface(\"" + row.cell.idInterface + "\")'><img border='0' style='margin-right:10px;' src= " + contextPath + "/images/bt_devolucao.png /></href>";
			brDetalhes 		  = "<a href='javascript:;' onclick='painelProcessamentoController.abrirPopUpDetalhesInterfaceProcessamento(" + row.cell.idLogProcessamento + ", \"" + row.cell.dataProcessmento + "\", \"" + row.cell.idLogExecucao + "\", \"" + row.cell.horaProcessamento + "\")'><img border='0' src= " + contextPath + "/images/ico_detalhes.png /></href>";

			if(row.cell.idLogProcessamento != "" && row.cell.dataProcessmento != "" && row.cell.idLogExecucao != "" && row.cell.status != 'S' && row.cell.status != 'V'){
				row.cell.reprocessar = btReprocessamento + brDetalhes;
			}else{
				row.cell.reprocessar = btReprocessamento;
			}

			//row.cell.nome = "<a href='javascript:;' onclick='painelProcessamentoController.abrirPopUpDetalhesInterface(" + row.cell.idLogProcessamento + ")'>" + row.cell.nome + "</href>";
			
			if (row.cell.status == 'S' || row.cell.status == 'A')//Sucesso ou Aviso
				row.cell.status = "<img src= " + contextPath + "/images/ico_operando.png />";
			else if (row.cell.status == 'F')//Falha 
				row.cell.status = "<img src= " + contextPath + "/images/ico_offline.png />";
			else if (row.cell.status == 'V')//Vazio Sem informacoes
				row.cell.status = "<img src= " + contextPath + "/images/ico_semdados.png />";
			else if (row.cell.status == 'E')//Erro
				row.cell.status = "<img src= " + contextPath + "/images/ico_encerrado.png />";
			else // Não processado
				row.cell.status = "<img src= " + contextPath + "/images/ico_semdados.png />";
			
		});
		return resultado;
	},
	abrirPopUpDetalhesInterface : function(idLogProcessamento) {
		$(".detalheInterfaceGrid", painelProcessamentoController.workspace).flexOptions({
			url: contextPath + '/administracao/painelProcessamento/pesquisarDetalhesInterface',
			params: [
		         {name:'idLogProcessamento', value: idLogProcessamento}
		    ],
		    newp: 1,
		});
		
		$(".detalheInterfaceGrid", painelProcessamentoController.workspace).flexReload();
		painelProcessamentoController.popup_detalhes();			
	},
	executarPreInterface : function (resultado) {

		if (resultado.mensagens) {
			$("#dialog-detalhes", painelProcessamentoController.workspace).dialog("close");
			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			return resultado;
		}

		return resultado;
	},
	executarPreInterfaceProcessamento : function(resultado) {
		
		if (resultado.mensagens) {
			$("#dialog-novo", painelProcessamentoController.workspace).dialog("close");
			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {
			
			row.cell.mensagem = row.cell.mensagem || ""; 
			
			if(row.cell.numeroLinha ){ 
				row.cell.numeroLinha = "Linha: " + row.cell.numeroLinha;
			}
			else{
				row.cell.numeroLinha="";
			}
		});
		
		return resultado;
	},
	abrirPopUpDetalhesInterfaceProcessamento : function(idLogProcessamento, dataProcessamento, idLogExecucao, horaProcessamento) {
		$(".detalheProcessamentoGrid", painelProcessamentoController.workspace).flexOptions({
			url: contextPath + '/administracao/painelProcessamento/pesquisarDetalhesInterfaceProcessamento',
			params: [
		         {name:'idLogProcessamento', value: idLogProcessamento},
		          {name:'dataProcessamento', value: dataProcessamento},
		         {name:'idLogExecucao', value: idLogExecucao}
		    ],
		    newp: 1,
		});
		
		$(".detalheProcessamentoGrid", painelProcessamentoController.workspace).flexReload();
		painelProcessamentoController.popup(idLogProcessamento, dataProcessamento, horaProcessamento);			
	},
	executarPreProcessamentoGrid : function(resultado) {
		
		if (resultado.mensagens) {
			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			return resultado;
		}
		
		$.each(resultado.rows, function(index, row) {
			if (row.cell.isSistemaOperacional) {
				row.cell.nome = "<a href='javascript:;' onclick='painelProcessamentoController.abrirPopUpOperacional()'>" + row.cell.nome + "</a>";
			}

			if (row.cell.status == 'S')
				row.cell.status = "<img src= " + contextPath + "/images/ico_operando.png />";
			else // Não processado
				row.cell.status = "<img src= " + contextPath + "/images/ico_encerrado.png />";		
		});
		
		return resultado;
	},
	abrirPopUpOperacional : function() {
		$.postJSON(contextPath + "/administracao/painelProcessamento/buscarEstadoOperacional",
				null,
				function(result) {
					$("#statusSistemaOperacional", painelProcessamentoController.workspace).text(result);
				},
				null,
				true);

		painelProcessamentoController.popup_sistema();
	},
	bindButtonsInterfaces : function() {
		$("#btnReprocessarTodas", painelProcessamentoController.workspace).click(function() {
			painelProcessamentoController.reprocessarInterfacesEmOrdem();
		});
		$("#btnGerarXLS", painelProcessamentoController.workspace).click(function() {
			window.location = contextPath + "/administracao/painelProcessamento/exportar?fileType=XLS&tipoRelatorio=1";
		});
		$("#btnGerarPDF", painelProcessamentoController.workspace).click(function() {
			window.location = contextPath + "/administracao/painelProcessamento/exportar?fileType=PDF&tipoRelatorio=1";
		});	
	},
	bindButtonsProcessos : function() {
		$("#btnGerarXLS", painelProcessamentoController.workspace).click(function() {
			window.location = contextPath + "/administracao/painelProcessamento/exportar?fileType=XLS&tipoRelatorio=2";
		});
		$("#btnGerarPDF", painelProcessamentoController.workspace).click(function() {
			window.location = contextPath + "/administracao/painelProcessamento/exportar?fileType=PDF&tipoRelatorio=2";
		});
	},
	reprocessarInterface : function(idInterface) {
		
		$( "#dialog-excutarInterface" ).dialog({
			resizable: false,
			height:'auto',
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					
					var data = [{name: 'idInterface', value: idInterface}];
					$.postJSON(contextPath + "/administracao/painelProcessamento/executarInterface",
							   data,
							   function (resultado) {

									exibirMensagem(resultado.tipoMensagem, resultado.listaMensagens);
						
									$(".painelInterfaceGrid", painelProcessamentoController.workspace).flexOptions({
										url : contextPath + '/administracao/painelProcessamento/pesquisarInterfaces',
										params: [],
										newp: 1,
									});
									$(".painelInterfaceGrid", painelProcessamentoController.workspace).flexReload();
						
							   },function (resultado) {
								   	$(".painelInterfaceGrid", painelProcessamentoController.workspace).flexOptions({
										url : contextPath + '/administracao/painelProcessamento/pesquisarInterfaces',
										params: [],
										newp: 1,
									});
									$(".painelInterfaceGrid", painelProcessamentoController.workspace).flexReload();
							   });
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	},
	
	gerarRankingSegmento : function() {
		
		$.postJSON(contextPath + "/administracao/painelProcessamento/gerarRankingSegmento",
				null,
				function (resultado) {
				
					exibirMensagem(resultado.tipoMensagem, resultado.listaMensagens);
			   	}
		);
	},
	
	gerarRankingFaturamento : function() {
		
		$.postJSON(contextPath + "/administracao/painelProcessamento/gerarRankingFaturamento",
				null,
				function (resultado) {
				
					exibirMensagem(resultado.tipoMensagem, resultado.listaMensagens);
			   	}
		);
	},
	processarInterfaceDevolucaoFornecedor : function() {
		
		$.postJSON(contextPath + "/administracao/painelProcessamento/processarInterfaceDevolucaoFornecedor",
				null,
				function (resultado) {
				
					exibirMensagem(resultado.tipoMensagem, resultado.listaMensagens);
			   	}
		);
	},
	
	exportarCobranca : function() {
		
		$.postJSON(contextPath + "/administracao/painelProcessamento/exportarCobranca",
				null,
				function (resultado) {
					exibirMensagem(resultado.tipoMensagem, resultado.listaMensagens);
			   	}
		);
	},
	
	processarCobrancaConsolidada : function() {
		
		$.postJSON(contextPath + "/administracao/painelProcessamento/processarCobrancaConsolidada",
				null,
				function (resultado) {
					exibirMensagem(resultado.tipoMensagem, resultado.listaMensagens);
			   	}
		);
	},
	
	reprocessarInterfacesEmOrdem : function() {
		
		$( "#dialog-excutarInterfacesEmOrdem" ).dialog({
			resizable: false,
			height:'auto',
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					
					var data = {};
					$.postJSON(contextPath + "/administracao/painelProcessamento/executarTodasInterfacesEmOrdem",
					   data,
					   function (resultado) {
					
							exibirMensagem(resultado.tipoMensagem, resultado.listaMensagens);
						
							$(".painelInterfaceGrid", painelProcessamentoController.workspace).flexOptions({
								url : contextPath + '/administracao/painelProcessamento/pesquisarInterfaces',
								params: [],
								newp: 1,
							});
							$(".painelInterfaceGrid", painelProcessamentoController.workspace).flexReload();
						
						}
					);
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
		
	}

}, BaseController);
//@ sourceURL=painelProcessamento.js