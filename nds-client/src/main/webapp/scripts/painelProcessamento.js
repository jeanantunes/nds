var painelProcessamentoController = $.extend(true, {
	init : function(path) {
		this.contextPath = path;
		this.initGridDetalheInterfaceGrid();
		this.initGridDetalheProcessamentoGrid();
		this.initGridPainelInterfaceGrid();
		this.initGridPainelProcessamentoGrid();
		this.pesquisarInterfaces();
		this.bindButtonsInterfaces();
		$( "#tabPainel", painelProcessamentoController.workspace ).tabs();
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
				display : 'Tipo do Erro',
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
			sortname : "tipoErro",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,			
			width : 700,
			height : 200
		});
	},
	initGridPainelProcessamentoGrid : function() {
		$(".painelProcessamentoGrid", painelProcessamentoController.workspace).flexigrid({
			preProcess : painelProcessamentoController.executarPreProcessamentoGrid,
			dataType : 'json',
			colModel : [ {
				display : 'Processos',
				name : 'nome',
				width : 519,
				sortable : true,
				align : 'left'
			}, {
				display : 'Status',
				name : 'status',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Processamento',
				name : 'dataProcessmento',
				width : 130,
				sortable : true,
				align : 'center'
			}, {
				display : 'Hora Processamento',
				name : 'horaProcessamento',
				width : 130,
				sortable : true,
				align : 'center'
			}],
			sortname : "nome",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 920,
			height : 250
		});
	},
	initGridPainelInterfaceGrid : function() {
		$(".painelInterfaceGrid", painelProcessamentoController.workspace).flexigrid({
			preProcess: painelProcessamentoController.executarPreProcessamentoInterfaceGrid,
			dataType : 'json',
			colModel : [ {
				display : 'Interface',
				name : 'nome',
				width : 215,
				sortable : true,
				align : 'left'
			}, {
				display : 'Arquivo',
				name : 'extensaoArquivo',
				width : 180,
				sortable : true,
				align : 'left'
			}, {
				display : 'Status',
				name : 'status',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Processamento',
				name : 'dataProcessmento',
				width : 130,
				sortable : true,
				align : 'center'
			}, {
				display : 'Hora Processamento',
				name : 'horaProcessamento',
				width : 130,
				sortable : true,
				align : 'center'
			}, {
				display : 'Reprocessar',
				name : 'reprocessar',
				width : 100,
				sortable : false,
				align : 'center'
			}],
			sortname : "nome",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 920,
			height : 250
		});
	},
	popup : function() {
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
	pesquisarInterfaces : function() {
		painelProcessamentoController.bindButtonsInterfaces();
		$(".painelInterfaceGrid", painelProcessamentoController.workspace).flexOptions({
			url : contextPath + '/administracao/painelProcessamento/pesquisarInterfaces',
			params: [],
			newp: 1,
		});
		$(".painelInterfaceGrid", painelProcessamentoController.workspace).flexReload();
	},
	pesquisarProcessos : function() {
		painelProcessamentoController.bindButtonsProcessos();
		$(".painelProcessamentoGrid", painelProcessamentoController.workspace).flexOptions({
			url : contextPath + '/administracao/painelProcessamento/pesquisarProcessos',
			params: [],
			newp : 1,
		});
		$(".painelProcessamentoGrid", painelProcessamentoController.workspace).flexReload();
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

			btReprocessamento = "<a href='javascript:;' onclick='painelProcessamentoController.reprocessarInterface(\"" + row.cell.nome + "\")'><img src= " + contextPath + "/images/bt_devolucao.png /></href>";
			brDetalhes 		  = "<a href='javascript:;' onclick='painelProcessamentoController.abrirPopUpDetalhesInterfaceProcessamento(" + row.cell.idLogProcessamento + ")'><img src= " + contextPath + "/images/ico_detalhes.png /></href>";
			row.cell.reprocessar = btReprocessamento + brDetalhes;

			row.cell.nome = "<a href='javascript:;' onclick='painelProcessamentoController.abrirPopUpDetalhesInterface(" + row.cell.idLogProcessamento + ")'>" + row.cell.nome + "</href>";
			
			if (row.cell.status == 'S' || row.cell.status == 'A')
				row.cell.status = "<img src= " + contextPath + "/images/ico_operando.png />";
			else if (row.cell.status == 'F')
				row.cell.status = "<img src= " + contextPath + "/images/ico_offline.png />";
			else // Não processado
				row.cell.status = "<img src= " + contextPath + "/images/ico_encerrado.png />";
			
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
			row.cell.numeroLinha = "Linha: " + row.cell.numeroLinha;
		});
		
		return resultado;
	},
	abrirPopUpDetalhesInterfaceProcessamento : function(idLogProcessamento) {
		$(".detalheProcessamentoGrid", painelProcessamentoController.workspace).flexOptions({
			url: contextPath + '/administracao/painelProcessamento/pesquisarDetalhesInterfaceProcessamento',
			params: [
		         {name:'idLogProcessamento', value: idLogProcessamento}
		    ],
		    newp: 1,
		});
		
		$(".detalheProcessamentoGrid", painelProcessamentoController.workspace).flexReload();
		painelProcessamentoController.popup();			
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
	reprocessarInterface : function(classeInterface) {
		var data = [{name: 'classeInterface', value: classeInterface}];
		$.postJSON(contextPath + "/administracao/painelProcessamento/executarInterface",
				   data,
				   function (resultado) {
					   exibirMensagem(resultado.tipoMensagem, 
					   				  resultado.listaMensagens);
				   });
	}

}, BaseController);
