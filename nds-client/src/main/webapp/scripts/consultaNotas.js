var consultaNotasController = $.extend(true, {

init : function() {
	
	$("#btnPesquisar", consultaNotasController.workspace).keypress(function(event) {

		var keynum;

		if (window.event) {

			keynum = event.keyCode

		} else if (event.which) {

			keynum = event.which
		}

		if (keynum == 13) {
			consultaNotasController.pesquisarNotas();
		}
	});

	$("#notasSemFisicoGrid", consultaNotasController.workspace).flexigrid({
		preProcess : consultaNotasController.processarResultadoConsultaNF,
		dataType : 'json',
		colModel : [ {
			display : 'Número da Nota',
			name : 'numero',
			width : 100,
			sortable : true,
			align : 'left'
		}, {
			display : 'Data de Emissão',
			name : 'dataEmissao',
			width : 120,
			sortable : true,
			align : 'center'
		}, {
			display : 'Data de Expedição',
			name : 'dataExpedicao',
			width : 120,
			sortable : true,
			align : 'center'
		}, {
			display : 'Tipo',
			name : 'descricao',
			width : 150,
			sortable : true,
			align : 'left'
		}, {
			display : 'Fornecedor',
			name : 'razaoSocial',
			width : 100,
			sortable : true,
			align : 'left'
		}, {
			display : 'Valor R$',
			name : 'valor',
			width : 80,
			sortable : false,
			align : 'right'
		}, {
			display : 'Nota Recebida',
			name : 'dataRecebimento',
			width : 110,
			sortable : true,
			align : 'center'
		}, {
			display : "Ação",
			name : 'acao',
			width : 60,
			sortable : false,
			align : 'center'
		} ],
		sortname : "numero, dataEmissao",
		sortorder : "asc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 960,
		height : 180,
		singleSelect : true
	});

	$("#datepickerDe", consultaNotasController.workspace).datepicker({
		showOn : "button",
		buttonImage : contextPath + "/images/calendar.gif",
		buttonImageOnly : true,
		dateFormat : 'dd/mm/yy',
		defaultDate : new Date()
	});

	$("#datepickerDe", consultaNotasController.workspace).mask("99/99/9999");

	$("#datepickerAte", consultaNotasController.workspace).datepicker({
		showOn : "button",
		buttonImage : contextPath + "/images/calendar.gif",
		buttonImageOnly : true,
		dateFormat : 'dd/mm/yy',
		defaultDate : new Date()
	});

	$("#datepickerAte", consultaNotasController.workspace).mask("99/99/9999");
	
},	
	
processarResultadoConsultaNF : function (data) {

	if ($("#datepickerDe", consultaNotasController.workspace).val() == "" && $("#datepickerAte", consultaNotasController.workspace).val() == "") {

		var dataAtual = $.format.date(new Date(), "dd/MM/yyyy");

		$("#datepickerDe", consultaNotasController.workspace).val(dataAtual);
		$("#datepickerAte", consultaNotasController.workspace).val(dataAtual);
	}

	if (data.mensagens) {

		exibirMensagem(data.mensagens.tipoMensagem,
				data.mensagens.listaMensagens);

		$(".grids", consultaNotasController.workspace).hide();

		return;
	}

	var i;

	for (i = 0; i < data.rows.length; i++) {

		var lastIndex = data.rows[i].cell.length - 1;

		data.rows[i].cell[lastIndex - 1] = '<a href="javascript:;" onclick="consultaNotasController.pesquisarDetalhesNota('
				+ data.rows[i].cell[lastIndex]
				+ ')" '
				+ ' style="cursor:pointer;border:0px" title="Visualizar Detalhes">'
				+ '<img src="' + contextPath + '/images/ico_detalhes.png" border="0px"/>'
				+ '</a>';
	}

	if ($(".grids", consultaNotasController.workspace).css('display') == 'none') {

		$(".grids", consultaNotasController.workspace).show();
	}

	return data;
},

pesquisarNotas : function() {

	var formData = $('#formPesquisaNotas', consultaNotasController.workspace).serializeArray();

	$("#notasSemFisicoGrid", consultaNotasController.workspace).flexOptions({
		url : contextPath + "/estoque/consultaNotas/pesquisarNotas",
		params : formData,
		newp : 1
	});

	$("#notasSemFisicoGrid", consultaNotasController.workspace).flexReload();
},

pesquisarDetalhesNota : function(idNota) {

	$("#notasSemFisicoDetalheGrid", consultaNotasController.workspace).flexigrid(
			{
				url : contextPath + "/estoque/consultaNotas/pesquisarDetalhesNotaFiscal",
				preProcess : consultaNotasController.montarGridComRodape,
				dataType : 'json',
				colModel : [ {
					display : 'Código',
					name : 'codigoItem',
					width : 40,
					sortable : true,
					align : 'left'
				}, {
					display : 'Produto',
					name : 'nomeProduto',
					width : 100,
					sortable : true,
					align : 'left'
				}, {
					display : 'Edição',
					name : 'numeroEdicao',
					width : 70,
					sortable : true,
					align : 'center'
				}, {
					display : 'Preço de Venda R$',
					name : 'precoCapa',
					width : 100,
					sortable : true,
					align : 'right'
				}, {
					display : 'Exemplares',
					name : 'quantidadeExemplares',
					width : 60,
					sortable : true,
					align : 'center'
				}, {
					display : 'Sobras / Faltas',
					name : 'sobrasFaltas',
					width : 80,
					sortable : true,
					align : 'center'
				}, {
					display : 'Total R$',
					name : 'total',
					width : 60,
					sortable : true,
					align : 'right'
				} ],
				width : 600,
				height : 200,
				params : [ {
					name : 'idNota',
					value : idNota
				} ],
				resizable : false
			});

	$("#notasSemFisicoDetalheGrid", consultaNotasController.workspace)
			.flexOptions(
					{
						url : contextPath + "/estoque/consultaNotas/pesquisarDetalhesNotaFiscal",
						params : [ {
							name : 'idNota',
							value : idNota
						} ]
					});

	$("#notasSemFisicoDetalheGrid", consultaNotasController.workspace).flexReload();
},

montarGridComRodape : function(data) {

	if (data.mensagens) {
		exibirMensagem(data.mensagens.tipoMensagem,
				data.mensagens.listaMensagens);

		return;
	}

	var jsonData = jQuery.toJSON(data);

	var result = jQuery.evalJSON(jsonData);

	$("#totalExemplares", consultaNotasController.workspace).html(result.totalExemplares);
	$("#totalSumarizado", consultaNotasController.workspace).html("R$ " + result.totalSumarizado);

	consultaNotasController.popup();

	return result.tableModel;
},

popup : function() {

	$("#dialog-novo", consultaNotasController.workspace).dialog({
		resizable : false,
		height : 370,
		width : 630,
		modal : true,
		buttons : {
			"Fechar" : function() {
				$(this).dialog("close");
			},
		},
		form: $("#dialog-novo", consultaNotasController.workspace).parents("form")
	});
}

}, BaseController);