var consultaNotasController = $.extend(true, {

init : function() {
	
	this.linhasDestacadas = [],
	
	$("#btnPesquisar", consultaNotasController.workspace).keypress(function(event) {

		var keynum = 0;

		if (window.event) {

			keynum = event.keyCode;

		} else if (event.which) {

			keynum = event.which;
		}

		if (keynum == 13) {
			consultaNotasController.pesquisarNotas();
		}
	});

	$("#notasSemFisicoGrid", consultaNotasController.workspace).flexigrid({
		preProcess : consultaNotasController.processarResultadoConsultaNF,
		dataType : 'json',
		colModel : [ {
			display : 'Nº NF',
			name : 'numero',
			width : 50,
			sortable : true,
			align : 'left'
		}, {
			display : 'Série',
			name : 'serie',
			width : 30,
			sortable : true,
			align : 'left'
		}, {
			display : 'Nota de Envio',
			name : 'numeroNotaEnvio',
			width : 70,
			sortable : true,
			align : 'left'
		}, {
			display : 'Chave Acesso',
			name : 'chaveAcesso',
			width : 210,
			sortable : true,
			align : 'left'
		},{
			display : 'Emissão',
			name : 'dataEmissao',
			width : 60,
			sortable : true,
			align : 'center'
		}, {
			display : 'Expedição',
			name : 'dataExpedicao',
			width : 60,
			sortable : true,
			align : 'center'
		}, {
			display : 'Fornecedor',
			name : 'razaoSocial',
			width : 60,
			sortable : true,
			align : 'left'
		}, {
			display : 'Valor Capa R$',
			name : 'valorTotalNota',
			width : 70,
			sortable : true,
			align : 'right'
		}, {
			display : 'Valor C/Desc R$',
			name : 'valorTotalNotaComDesconto',
			width : 90,
			sortable : true,
			align : 'right'
		}, {
			display : 'Nota Recebida',
			name : 'notaRecebida',
			width : 80,
			sortable : true,
			align : 'center'
		}, {
			display : "Ação",
			name : 'acao',
			width : 30,
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

	$("#dataNFDe", consultaNotasController.workspace).datepicker({
		showOn : "button",
		buttonImage : contextPath + "/images/calendar.gif",
		buttonImageOnly : true,
		dateFormat : 'dd/mm/yy',
		defaultDate : new Date()
	});

	$("#dataNFDe", consultaNotasController.workspace).mask("99/99/9999");

	$("#dataNFAte", consultaNotasController.workspace).datepicker({
		showOn : "button",
		buttonImage : contextPath + "/images/calendar.gif",
		buttonImageOnly : true,
		dateFormat : 'dd/mm/yy',
		defaultDate : new Date()
	});

	$("#dataNFAte", consultaNotasController.workspace).mask("99/99/9999");
	
},	
	
processarResultadoConsultaNF : function (data) {

	if ($("#dataNFDe", consultaNotasController.workspace).val() == "" && $("#dataNFAte", consultaNotasController.workspace).val() == "") {

		var dataAtual = $.format.date(new Date(), "dd/MM/yyyy");

		$("#dataNFDe", consultaNotasController.workspace).val(dataAtual);
		$("#dataNFAte", consultaNotasController.workspace).val(dataAtual);
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
				
		if(!data.rows[i].cell.chaveAcesso){
			data.rows[i].cell.chaveAcesso="";
		}
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
				onSuccess: consultaNotasController.onSuccesPesquisa,
				dataType : 'json',
				colModel : [ {
					display : 'Código',
					name : 'codigoProduto',
					width : 45,
					sortable : true,
					align : 'left'
				}, {
					display : 'Produto',
					name : 'nomeProduto',
					width : 170,
					sortable : true,
					align : 'left'
				}, {
					display : 'Edição',
					name : 'numeroEdicao',
					width : 40,
					sortable : true,
					align : 'center'
				}, {
					display : 'Preço Capa R$',
					name : 'precoVenda',
					width : 80,
					sortable : true,
					align : 'right'
				}, {
					display : 'Preço C/Desc R$',
					name : 'precoComDesconto',
					width : 90,
					sortable : false,
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
					name : 'valorTotal',
					width : 55,
					sortable : true,
					align : 'right'
				}, {
					display : 'Total C/Desc R$',
					name : 'valorTotalComDesconto',
					width : 80,
					sortable : false,
					align : 'right'
				} ],
				width : 840,
				height : 230,
				sortname : "codigoItem",
				sortorder : "asc",
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
	
	consultaNotasController.linhasDestacadas = [];
	$.each(data.tableModel.rows,
		function(index, item){
			if (item.cell[9] == "true"){
				consultaNotasController.linhasDestacadas.push(item.id);
			}
		}
	);

	var jsonData = jQuery.toJSON(data);

	var result = jQuery.evalJSON(jsonData);

	$("#totalExemplares", consultaNotasController.workspace).html(result.totalExemplares);
	$("#totalSumarizado", consultaNotasController.workspace).html("R$ " + result.totalSumarizado);
	$("#totalSumarizadoComDesconto", consultaNotasController.workspace).html("R$ " + result.totalSumarizadoComDesconto);

	consultaNotasController.popup();

	return result.tableModel;
},

popup : function() {

	$("#dialog-novo", consultaNotasController.workspace).dialog({
		resizable : false,
		height : 400,
		width : 860,
		modal : true,
		buttons : {
			"Fechar" : function() {
				$(this).dialog("close");
			},
		},
		form: $("#dialog-novo", consultaNotasController.workspace).parents("form")
	});
},

onSuccesPesquisa : function(){
	$.each(consultaNotasController.linhasDestacadas,
		function (index, item){	    	
			$('#row' + item, consultaNotasController.workspace).removeClass("erow").addClass("gridLinhaDestacada");
		}
	);
}

}, BaseController);

//@ sourceURL=scriptConsultaNota.js