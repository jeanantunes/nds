<<<<<<< HEAD
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
			display : 'Série',
			name : 'serie',
			width : 50,
			sortable : true,
			align : 'left'
		}, {
			display : 'Nota de Envio',
			name : 'numeroNotaEnvio',
			width : 50,
			sortable : true,
			align : 'left'
		}, {
			display : 'Data de Emissão',
			name : 'dataEmissao',
			width : 100,
			sortable : true,
			align : 'center'
		}, {
			display : 'Data de Expedição',
			name : 'dataExpedicao',
			width : 100,
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
			name : 'valorTotalNota',
			width : 80,
			sortable : true,
			align : 'right'
		}, {
			display : 'Nota Recebida',
			name : 'notaRecebida',
			width : 90,
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
		
		$(".areaBts", consultaNotasController.workspace).hide();
		$(".grids", consultaNotasController.workspace).hide();
		$(".fieldFiltro", consultaNotasController.workspace).css('margin-top','0px');

		return;
	}

	var i;

	for (i = 0; i < data.rows.length; i++) {
		
		data.rows[i].cell.numeroNotaEnvio = 
			(data.rows[i].cell.numeroNotaEnvio == undefined) ? "" : data.rows[i].cell.numeroNotaEnvio;
		
		data.rows[i].cell.acao = '<a href="javascript:;" onclick="consultaNotasController.pesquisarDetalhesNota('
				+ data.rows[i].cell.id
				+ ')" '
				+ ' style="cursor:pointer;border:0px" title="Visualizar Detalhes">'
				+ '<img src="' + contextPath + '/images/ico_detalhes.png" border="0px"/>'
				+ '</a>';
	}

	if ($(".grids", consultaNotasController.workspace).css('display') == 'none') {

		$(".grids", consultaNotasController.workspace).show();
		$(".areaBts", consultaNotasController.workspace).show();
		$(".fieldFiltro", consultaNotasController.workspace).css('margin-top','27px');
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
					width : 70,
					sortable : true,
					align : 'left'
				}, {
					display : 'Produto',
					name : 'nomeProduto',
					width : 150,
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
					width : 80,
					sortable : true,
					align : 'right'
				} ],
				width : 715,
				height : 230,
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
		height : 400,
		width : 750,
		modal : true,
		buttons : {
			"Fechar" : function() {
				$(this).dialog("close");
			},
		},
		form: $("#dialog-novo", consultaNotasController.workspace).parents("form")
	});
}

=======
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
			display : 'Série',
			name : 'serie',
			width : 50,
			sortable : true,
			align : 'left'
		}, {
			display : 'Nota de Envio',
			name : 'numeroNotaEnvio',
			width : 50,
			sortable : true,
			align : 'left'
		}, {
			display : 'Data de Emissão',
			name : 'dataEmissao',
			width : 100,
			sortable : true,
			align : 'center'
		}, {
			display : 'Data de Expedição',
			name : 'dataExpedicao',
			width : 100,
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
			name : 'valorTotalNota',
			width : 80,
			sortable : true,
			align : 'right'
		}, {
			display : 'Nota Recebida',
			name : 'notaRecebida',
			width : 90,
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
		
		$(".areaBts", consultaNotasController.workspace).hide();
		$(".grids", consultaNotasController.workspace).hide();
		$(".fieldFiltro", consultaNotasController.workspace).css('margin-top','0px');

		return;
	}

	var i;

	for (i = 0; i < data.rows.length; i++) {
		
		data.rows[i].cell.numeroNotaEnvio = 
			(data.rows[i].cell.numeroNotaEnvio == undefined) ? "" : data.rows[i].cell.numeroNotaEnvio;
		
		data.rows[i].cell.acao = '<a href="javascript:;" onclick="consultaNotasController.pesquisarDetalhesNota('
				+ data.rows[i].cell.id
				+ ')" '
				+ ' style="cursor:pointer;border:0px" title="Visualizar Detalhes">'
				+ '<img src="' + contextPath + '/images/ico_detalhes.png" border="0px"/>'
				+ '</a>';
	}

	if ($(".grids", consultaNotasController.workspace).css('display') == 'none') {

		$(".grids", consultaNotasController.workspace).show();
		$(".areaBts", consultaNotasController.workspace).show();
		$(".fieldFiltro", consultaNotasController.workspace).css('margin-top','27px');
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
					width : 70,
					sortable : true,
					align : 'left'
				}, {
					display : 'Produto',
					name : 'nomeProduto',
					width : 150,
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
					width : 80,
					sortable : true,
					align : 'right'
				} ],
				width : 715,
				height : 230,
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
		height : 400,
		width : 750,
		modal : true,
		buttons : {
			"Fechar" : function() {
				$(this).dialog("close");
			},
		},
		form: $("#dialog-novo", consultaNotasController.workspace).parents("form")
	});
}

>>>>>>> refs/remotes/DGBTi/fase2
}, BaseController);
