var contextPath;

var edicoesFechadasController = $.extend(true, {
	init : function(path) {
		this.contextPath = path;
		this.initGridEdicoesFechadasGrid();
		this.initGridDetalheEdicoesFechadas();
		this.bindButtons();

		$("#dataDe").datepicker({
			showOn : "button",
			buttonImage : contextPath + "/images/calendar.gif",
			buttonImageOnly : true,
			dateFormat : 'dd/mm/yy',
			defaultDate : new Date()
		});

		$("#dataDe").mask("99/99/9999");

		$("#dataAte").datepicker({
			showOn : "button",
			buttonImage : contextPath + "/images/calendar.gif",
			buttonImageOnly : true,
			dateFormat : 'dd/mm/yy',
			defaultDate : new Date()
		});

		$("#dataAte").mask("99/99/9999");
	
	},
	bindButtons : function() {
		$("#btnPesquisar").click(function() {
			edicoesFechadasController.pesquisar();
			$(".grids").show();
		});
	},
	initGridEdicoesFechadasGrid : function() {
		$(".consultaEdicoesFechadasGrid").flexigrid({
			preProcess : edicoesFechadasController.executarPreProcessamento,
			dataType : 'json',
			colModel : [ {
				display : 'Código',
				name : 'codigoProduto',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 190,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicaoProduto',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Fornecedor',
				name : 'nomeFornecedor',
				width : 180,
				sortable : true,
				align : 'left'
			}, {
				display : 'Lançamento',
				name : 'dataLancamento',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Recolhimento',
				name : 'dataRecolhimento',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Parcial',
				name : 'parcial',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Saldo',
				name : 'saldo',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 30,
				sortable : false,
				align : 'center'
			} ],
			sortname : "codigoProduto",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});
	},
	initGridDetalheEdicoesFechadas : function() {
		$(".detalheEdicoesFechadasGrid").flexigrid({
			preProcess: edicoesFechadasController.executarPreProcessamentoPopUp,
			dataType : 'json',
			colModel : [ {
				display : 'Data',
				name : 'dataInclusao',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Movimento',
				name : 'movimento',
				width : 200,
				sortable : true,
				align : 'left'
			}, {
				display : 'Entrada',
				name : 'entrada',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Saída',
				name : 'saida',
				width : 100,
				sortable : true,
				align : 'center'
			}, {
				display : 'Saldo Parcial',
				name : 'parcial',
				width : 110,
				sortable : true,
				align : 'right'
			} ],
			sortname : "data",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 700,
			height : 180
		});
	},
	executarPreProcessamentoPopUp : function(resultado) {
		var dadosPesquisa = {page: 0, total: 0};
		var saldoTotalExtratoEdicao = 0.0;
		var destacarValorSaldoTotal = "";
		
		if(typeof resultado.mensagens == "object") {
			exibirMensagem(resultado.mensagens.tipoMensagem, resultado.mensagens.listaMensagens);
		} else {
			$.each(resultado, function(index, value) {
				  if(value[0] == "gridResult") {
					  dadosPesquisa = edicoesFechadasController.destacarValorParcial(value[1]);
				  } else if(value[0] == "saldoTotalExtratoEdicao") {
					  saldoTotalExtratoEdicao = value[1];
				  } else if(value[0] == "destacarSaldoTotalExtratoEdicao") {
					  destacarValorSaldoTotal = value[1];
				  } 
			});
		}
		
		if(destacarValorSaldoTotal == "S") {
			$("#saldoEstoque").css("color", "red");
			$("#saldoEstoque").html(saldoTotalExtratoEdicao); 
		} else {
			$("#saldoEstoque").css("color", "black");
			$("#saldoEstoque").html(saldoTotalExtratoEdicao); 
		}
		return dadosPesquisa;
	},
	destacarValorParcial : function(data) {
		$.each(data.rows, function(index, value) {
			var destacarValorNegativo = value.cell[5];
			var valorParcial =  value.cell[4];
			if(destacarValorNegativo == "S") {
				value.cell[4] = '<span style="color: red">'+valorParcial+'</span>';
			} else {
				value.cell[4] = '<span style="color: black">'+valorParcial+'</span>';
			}
		});
		return data;
	},	
	executarPreProcessamento : function(resultado) {
		if (typeof resultado.mensagens == "object") {
			exibirMensagem(resultado.mensagens.tipoMensagem,
					resultado.mensagens.listaMensagens);
			$(".grids").hide();
			return resultado;
		}

		$("#totalEdicoesFechadasSaldo").html(resultado.saldoTotal);

		var tableModel = resultado.tableModel;

		$.each(tableModel.rows, function(index, row) {
			var linkAcao = '<a href="javascript:;" onclick="edicoesFechadasController.abrirPopUpSaldoEdicoesFechadas(' + row.cell.codigoProduto + ', ' + row.cell.edicaoProduto + ');"  style="cursor:pointer">'
					+ '<img title="Ação" src="' + contextPath
					+ '/images/ico_detalhes.png" hspace="5" border="0px" />'
					+ '</a>';

			row.cell.dataRecolhimento = row.cell.dataRecolhimento.$;
			row.cell.dataLancamento = row.cell.dataLancamento.$;
			row.cell.parcial = ((row.cell.parcial == true) ? "S" : "N");
			row.cell.saldo = row.cell.saldo;
			row.cell.acao = linkAcao;
		});

		$(".grids").show();
		return tableModel;

	},
	abrirPopUpSaldoEdicoesFechadas : function(codigoProduto, edicaoProduto) {
		$(".detalheEdicoesFechadasGrid").flexOptions({
			url: contextPath + '/estoque/extratoEdicao/pesquisaExtratoEdicao',
			params: [
		         {name:'codigoProduto', value: codigoProduto},
		         {name:'numeroEdicao', value: edicaoProduto},
		         {name:'nomeProduto', value: ""},
		         {name:'precoCapa', value: ""},
		         {name:'nomeFornecedor', value: ""}
		    ],
		    newp: 1,
		});
		
		$(".detalheEdicoesFechadasGrid").flexReload();
		edicoesFechadasController.popup_detalhes();			
	},
	pesquisar : function() {
		var dataDe = $("#dataDe").val();
		var dataAte = $("#dataAte").val();
		var selectFornecedor = $("select#fornecedor").val();

		$(".consultaEdicoesFechadasGrid").flexOptions({
			url : contextPath + '/estoque/edicoesFechadas/pesquisar',
			params : [ {
				name : 'dataDe',
				value : dataDe
			}, {
				name : 'dataAte',
				value : dataAte
			}, {
				name : 'codigoFornecedor',
				value : selectFornecedor
			} ],
			dataType : 'json'
		});
		$(".consultaEdicoesFechadasGrid").flexReload();
	},
	popup_detalhes : function() {
		$("#dialog-detalhes").dialog({
			resizable : false,
			height : 440,
			width : 750,
			modal : true,
			buttons : {
				"Fechar" : function() {
					$(this).dialog("close");
				},
			}
		});
	}
}, BaseController);
