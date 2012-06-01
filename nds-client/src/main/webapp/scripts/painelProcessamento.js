var contextPath;

$(function() {
	$( "#tabPainel" ).tabs();
});

var painelProcessamentoController = {
	init : function(path) {
		this.contextPath = path;
		this.initGridDetalheInterfaceGrid();
		this.initGridDetalheProcessamentoGrid();
		this.initGridPainelInterfaceGrid();
		this.initGridPainelProcessamentoGrid();
		this.pesquisarInterfaces();
		//this.bindButtons();
	},
	initGridDetalheInterfaceGrid : function() {
		$(".detalheInterfaceGrid").flexigrid({
			preProcess : painelProcessamentoController.executarPreProcessamentoInterfaceGrid,
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
			width : 700,
			height : 200
		});
	},
	initGridDetalheProcessamentoGrid : function() {
		$(".detalheProcessamentoGrid").flexigrid({
			preProcess : painelProcessamentoController.executarPreProcessamentoGrid,
			dataType : 'json',
			colModel : [ {
				display : 'Tipo do Erro',
				name : 'tipoErro',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Mensagem Usuário',
				name : 'msgUsuario',
				width : 330,
				sortable : true,
				align : 'left'
			}, {
				display : 'Mensagem de Sistema',
				name : 'msgSistema',
				width : 210,
				sortable : true,
				align : 'left'
			}],
			width : 700,
			height : 200
		});
	},
	initGridPainelProcessamentoGrid : function() {
		$(".painelProcessamentoGrid").flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Processos',
				name : 'processos',
				width : 519,
				sortable : true,
				align : 'left'
			}, {
				display : 'Status',
				name : 'Status',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Processamento',
				name : 'dtProcessamento',
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
		$(".painelInterfaceGrid").flexigrid({
			dataType : 'json',
			colModel : [ {
				display : 'Interface',
				name : 'interface',
				width : 215,
				sortable : true,
				align : 'left'
			}, {
				display : 'Arquivo',
				name : 'arquivo',
				width : 180,
				sortable : true,
				align : 'left'
			}, {
				display : 'Status',
				name : 'Status',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Data Processamento',
				name : 'dtProcessamento',
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
	popup : function() {
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:430,
			width:750,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );					
				}
			}
		});
	},
	popup_detalhes : function() {
		$( "#dialog-detalhes" ).dialog({
			resizable: false,
			height:430,
			width:750,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );					
				}
			}
		});
	},
	popup_sistema : function() {
		$( "#dialog-operacional" ).dialog({
			resizable: false,
			height:'auto',
			width:400,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );					
				}
			}
		});
	},
	pesquisarInterfaces : function() {
		alert("yeah1");
		$(".painelInterfaceGrid").flexOptions({
			url : contextPath + '/administracao/painelProcessamento/pesquisarInterfaces',
			params: [],
			newp: 1,
		});
		$(".painelInterfaceGrid").flexReload();
	},
	pesquisarProcessos : function() {
		alert("yeah2");
		$(".painelProcessamentoGrid").flexOptions({
			url : contextPath + '/administracao/painelProcessamento/pesquisarProcessos',
			params: [],
			newp : 1,
		});
		$(".painelProcessamentoGrid").flexReload();
	},
	executarPreProcessamentoInterfaceGrid : function() {
		alert("1");
	},
	executarPreProcessamentoGrid : function() {
		alert("2");
	}
}
