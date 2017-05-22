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
		$("#divCalendarInterfaceExecucao").hide();
		
		if (codigoDistribuidor == "") {
			$( "#calendarInterfaceExecucao").hide();
			$(".areaBts", painelProcessamentoController.workspace).hide();
			$(".grids", painelProcessamentoController.workspace).hide();
			$("#divProcessamento", painelProcessamentoController.workspace).show();
			$("#divMicrodistribuicao", painelProcessamentoController.workspace).hide();
			
			return;
		}
		
		if (codigoDistribuidor == "mi") {
			$( "#calendarInterfaceExecucao").show();
			$(".areaBts", painelProcessamentoController.workspace).show();
			$(".grids", painelProcessamentoController.workspace).show();
			$("#divProcessamento", painelProcessamentoController.workspace).hide();
			$("#divCalendarInterfaceExecucao").show();
		//	$( "#calendarInterfaceExecucao").show();
			//$("#divMicrodistribuicao", painelProcessamentoController.workspace).hide();
			$(".painelInterfaceGrid", painelProcessamentoController.workspace).flexOptions({
				url : contextPath + '/administracao/painelProcessamento/pesquisarInterfacesMicroDistribuicao',
				params: [],
				newp: 1,
			});
			$(".painelInterfaceGrid", painelProcessamentoController.workspace).flexReload();
			
			
			return;
		}
		
		$( "#calendarInterfaceExecucao").hide();
		$("#divProcessamento", painelProcessamentoController.workspace).hide();
		$("#divMicrodistribuicao", painelProcessamentoController.workspace).hide();
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
			
			if(row.cell.tipoInterfaceExecucao=='microDistribuicao'){
				btReprocessamento = "<a href='javascript:;' onclick='painelProcessamentoController.reprocessarInterfaceMicroDistribuicao(\"" + row.cell.nome+ "\")'><img border='0' style='margin-right:10px;' src= " + contextPath + "/images/bt_devolucao.png /></href>";
				brDetalhes 		  = "<a href='javascript:;' onclick='painelProcessamentoController.abrirPopUpDetalhesInterfaceMicroDistribuicao(" + row.cell.idInterface+ ", \"" + row.cell.dataProcessmento + "\", \"" + row.cell.idLogExecucao + "\", \"" + row.cell.horaProcessamento + "\")'><img border='0' src= " + contextPath + "/images/ico_detalhes.png /></href>";
			}else{
				btReprocessamento = "<a href='javascript:;' onclick='painelProcessamentoController.reprocessarInterface(\"" + row.cell.idInterface + "\")'><img border='0' style='margin-right:10px;' src= " + contextPath + "/images/bt_devolucao.png /></href>";
				brDetalhes 		  = "<a href='javascript:;' onclick='painelProcessamentoController.abrirPopUpDetalhesInterfaceProcessamento(" + row.cell.idLogProcessamento + ", \"" + row.cell.dataProcessmento + "\", \"" + row.cell.idLogExecucao + "\", \"" + row.cell.horaProcessamento + "\")'><img border='0' src= " + contextPath + "/images/ico_detalhes.png /></href>";
			}
			
			
			

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
	
	abrirPopUpDetalhesInterfaceMicroDistribuicao : function(idInterface, dataProcessamento, idLogExecucao, horaProcessamento) {
		$(".detalheProcessamentoGrid", painelProcessamentoController.workspace).flexOptions({
			url: contextPath + '/administracao/painelProcessamento/pesquisarDetalhesInterfaceMicroDistribuicao',
			params: [
		         {name:'idInterface', value: idInterface},
		          {name:'dataProcessamento', value: dataProcessamento},
		         {name:'idLogExecucao', value: idLogExecucao}
		    ],
		    newp: 1,
		});
		
		$(".detalheProcessamentoGrid", painelProcessamentoController.workspace).flexReload();
		painelProcessamentoController.popup(idInterface, dataProcessamento, horaProcessamento);			
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
	
   reprocessarInterfaceMicroDistribuicao : function(nomeInterface) {
	   
	   
	 var dataInterfaceExecucao =  $( "#calendarInterfaceExecucao" ).datepicker({
		   dateFormat: 'dd/mm/yy',
		   showOn: "button",
           buttonImage: "http://jqueryui.com/resources/demos/datepicker/images/calendar.gif",
           buttonImageOnly: true,
           buttonText: "Selecione uma data"
       });
	   
	   
		$( "#dialog-excutarInterface" ).dialog({
			resizable: false,
			height:'auto',
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					
					
					var data = [{name: 'dataInterfaceExecucao', value: dataInterfaceExecucao.val()},
					{name: 'nomeInterface', value: nomeInterface}];
					
					$.postJSON(contextPath + "/administracao/painelProcessamento/downloadArquivo",
							   data,
							   function (result) {
									exibirMensagem(result.tipoMensagem, result.listaMensagens);
									$(".painelInterfaceGrid", painelProcessamentoController.workspace).flexReload();
								});
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
		$(".areaBts", painelProcessamentoController.workspace).flexReload();
		$(".grids", painelProcessamentoController.workspace).flexReload();
		$("#divProcessamento", painelProcessamentoController.workspace).flexReload();
		$(".painelInterfaceGrid", painelProcessamentoController.workspace).flexReload();
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
					
					if(resultado.tipoMensagem == "SUCCESS"){
					
						$("#dialog-infoExportarCobranca").dialog({
							resizable : false,
							height : 150,
							width : 450,
							modal : true,
							open:function(){
								$("#labelExportarCobranca").html(resultado.listaMensagens[0]);
							},
						});
						
					}else{
						exibirMensagem(resultado.tipoMensagem, resultado.listaMensagens);
					}
			   	}
		);
	},
	
	processarCobrancaConsolidada : function() {
		
		$("#dialog-processarCobranca").dialog({
			resizable : false,
			height : 250,
			width : 450,
			modal : true,
			open:function(){
				$("#datepickerDataCobanca").datepicker({
					showOn: "button",
					dateFormat: 'dd/mm/yy',
					buttonImage: contextPath + "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
					buttonImageOnly: true
				});
				$("#datepickerDataCobanca").val("");
			},
			buttons : {
				"Processar" : function() {
					
					var dataProcessamento = $("#datepickerDataCobanca").val();
					var data = [{name:'data', value:dataProcessamento}];
					
					
					$.postJSON(contextPath + "/administracao/painelProcessamento/processarCobrancaConsolidada",
								data,
								function (resultado) {
									exibirMensagem(resultado.tipoMensagem, resultado.listaMensagens);
							   	});
					$(this).dialog("close");
				},
				"Cancelar" : function() {
					$(this).dialog("close");
				}
			}
		});
		
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
		$(".painelInterfaceGrid", painelProcessamentoController.workspace).flexReload();
	},
	
	processarArquivosMatriz : function() {
		
		var data = {};
		
		$.postJSON(contextPath + "/administracao/painelProcessamento/uploadArquivo",
				   data,
				   function (resultado) {
				
						
					}
				);
	}

}, BaseController);
//@ sourceURL=painelProcessamento.js