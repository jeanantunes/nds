/**
 * 
 */
function GeracaoNFe() {
	this.initGrid();
	this.init();
	this.mapCotasSuspensas = new Object();
}

GeracaoNFe.prototype.path = contextPath + '/expedicao/geracaoNFe/';
GeracaoNFe.prototype.init = function() {
	var _this = this;
	
	
	
	$("#selectFornecedores").multiselect({
		selectedList : 6
	});

	$(
			"#datepickerIntervaloMovimentoDe, #datepickerIntervaloMovimentoAte,#datepickerEmissao")
			.datepicker(
					{
						showOn : "button",
						buttonImage : contextPath
								+ "/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
						buttonImageOnly : true
					});

	$("#btnPesquisar").click(function() {
		_this.pequisar();
	});

	$("#btnGerar").click(function() {
		_this.btnGerarOnClick();
	});

	$("#btnImprimirXLS").click(function() {
		_this.imprimir('XLS');
	});
	$("#btnImprimirPDF").click(function() {
		_this.imprimir('PDF');
	});

	$('#checkboxCheckAllCotasSuspensas').change(
			function() {
				$(".checkboxCheckCotasSuspensas").attr('checked',
						$(this).attr('checked') == 'checked');
			});

	this.$dialogCotasSuspensas = $('#dialogCotasSuspensas').dialog({
		autoOpen : false,
		resizable : false,
		height : 400,
		width : 630,
		modal : true,
		buttons : {
			"Confirma a Geração" : function() {
				$(this).dialog("close");
				_this.gerar();
			},
			"Cancelar" : function() {
				$(this).dialog("close");
			}
		}
	});
	
	var dataEmissao = formatDateToString(new Date());
	$("#datepickerEmissao").val(dataEmissao);
};
GeracaoNFe.prototype.gerar = function() {
	var params = this.getParams();
	var todas = $('#checkboxCheckAllCotasSuspensas').attr('checked') == 'checked';
	if (todas) {
		var listCotasSuspensas = $.map(this.mapCotasSuspensas, function(value,
				index) {
			if (value) {
				return index;
			}
		});
		params = serializeArrayToPost('idCotasSuspensas', listCotasSuspensas,
				params);
	}
	params['dataEmissao'] = $("#datepickerEmissao").val();
	params['todasCotasSuspensa'] = todas;

	$.postJSON(this.path + 'gerar.json', params, function(data) {
		var tipoMensagem = data.tipoMensagem;
		var listaMensagens = data.listaMensagens;

		if (tipoMensagem && listaMensagens) {
			exibirMensagemDialog(tipoMensagem, listaMensagens, "");
		}

	}, null, true);
};
GeracaoNFe.prototype.imprimir = function(fileType) {
	var params = this.getParams();
	var todas = $('#checkboxCheckAllCotasSuspensas').attr('checked') == 'checked';
	if (todas) {
		var listCotasSuspensas = $.map(this.mapCotasSuspensas, function(value,
				index) {
			if (value) {
				return index;
			}
		});
		params = serializeArrayToPost('idCotasSuspensas', listCotasSuspensas,
				params);
	}
	params['dataEmissao'] = $("#datepickerEmissao").val();
	params['todasCotasSuspensa'] = todas;
	params['fileType'] = fileType;

	$.fileDownload(this.path + 'exportar', {
		httpMethod : "POST",
		data : params
	});
};

GeracaoNFe.prototype.getParams = function() {
	var params = {
		"intervaloBoxDe" : $("#inputIntervaloBoxDe").val(),
		"intervaloBoxAte" : $("#inputIntervaloBoxAte").val(),
		"intervalorCotaDe" : $("#inputIntervaloCotaDe").val(),
		"intervaloCotaAte" : $("#inputIntervaloCotaAte").val(),
		"intervaloDateMovimentoDe" : $("#datepickerIntervaloMovimentoDe")
				.val(),
		"intervaloDateMovimentoAte" : $("#datepickerIntervaloMovimentoAte")
				.val(),
		"tipoNotaFiscal" : $("#selectTipoNotaFiscal").val()
	};
	var listaFornecedores = $("#selectFornecedores").val();
	if (listaFornecedores) {
		params = serializeArrayToPost('listIdFornecedor', listaFornecedores,
				params);
	}
	return params;
};

GeracaoNFe.prototype.btnGerarOnClick = function() {
	this.mapCotasSuspensas = new Object();
	$('#checkboxCheckAllCotasSuspensas').attr('checked', false);
	var _this = this;
	var params = this.getParams();
	$.postJSON(this.path + 'hasCotasSuspensas.json', params, function(data) {
		var tipoMensagem = data.tipoMensagem;
		var listaMensagens = data.listaMensagens;

		if (tipoMensagem && listaMensagens) {
			exibirMensagemDialog(tipoMensagem, listaMensagens, "");
		}
		if (data.cotasSuspensas) {
			_this.$dialogCotasSuspensas.dialog("open");
			_this.gridReaload(_this.$gridCotasSuspensas,
					'buscaCotasSuspensas.json');
		}

	}, null, true);
};

GeracaoNFe.prototype.pequisar = function() {
	this.gridReaload(this.$gridNFe, 'busca.json');
	$(".grids").show();
};

GeracaoNFe.prototype.gridReaload = function(grid, uri) {
	var params = [ {
		name : "tipoNotaFiscal",
		value : 1
	} ];

	if ($("#inputIntervaloBoxDe").val().length > 0) {
		params.push({
			name : "intervaloBoxDe",
			value : $("#inputIntervaloBoxDe").val()
		});
	}

	if ($("#inputIntervaloBoxAte").val().length > 0) {
		params.push({
			name : "intervaloBoxAte",
			value : $("#inputIntervaloBoxAte").val()
		});
	}

	if ($("#inputIntervaloCotaDe").val().length > 0) {
		params.push({
			name : "intervaloCotaDe",
			value : parseInt($("#inputIntervaloCotaDe").val())
		});
	}

	if ($("#inputIntervaloCotaAte").val().length > 0) {
		params.push({
			name : "intervaloCotaAte",
			value : parseInt($("#inputIntervaloCotaAte").val())
		});
	}

	if ($("#datepickerIntervaloMovimentoDe").val().length > 0) {
		params.push({
			name : "intervaloDateMovimentoDe",
			value : $("#datepickerIntervaloMovimentoDe").val()
		});
	}

	if ($("#datepickerIntervaloMovimentoAte").val().length > 0) {
		params.push({
			name : "intervaloDateMovimentoAte",
			value : $("#datepickerIntervaloMovimentoAte").val()
		});
	}
	
	if (($("#inputIntervaloCotaDe").val().length > 0)
			&& ($("#inputIntervaloCotaAte").val().length > 0) 
			&& (($("#datepickerIntervaloMovimentoDe").val().length == 0) 
					||  ($("#datepickerIntervaloMovimentoAte").val().length == 0))) {
		exibirMensagem("WARNING", ["Quando haver intervalo de [Cota], deve haver também intervalo de [Data de Movimento]"], "");
		return;
	}
	
	var listaFornecedores = $("#selectFornecedores").val();
	if (listaFornecedores) {
		$.each(listaFornecedores, function(index, value) {
			params.push({
				'name' : "listIdFornecedor[]",
				'value' : value
			});
		});
	}

	grid.flexOptions({
		"url" : this.path + uri,
		params : params,
		newp : 1
	});
	grid.flexReload();
};

GeracaoNFe.prototype.initGrid = function() {
	var _this = this;
	this.$gridNFe = $("#gridNFe").flexigrid(
			{
				preProcess : function(data) {
					if (typeof data.mensagens == "object") {

						exibirMensagem(data.mensagens.tipoMensagem,
								data.mensagens.listaMensagens);

					} else {
						return data;
					}
				},
				dataType : 'json',
				colModel : [ {
					display : 'Cota',
					name : 'numeroCota',
					width : 50,
					sortable : true,
					align : 'left'
				}, {
					display : 'Nome',
					name : 'nomeCota',
					width : 750,
					sortable : true,
					align : 'left'
				}, {
					display : 'Total Exemplares',
					name : 'exemplares',
					width : 100,
					sortable : true,
					align : 'center'
				} ],
				sortname : "numeroCota",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 960,
				height : 180
			});
	this.$gridCotasSuspensas = $("#gridCotasSuspensas").flexigrid(
			{
				preProcess : function(data) {
					if (typeof data.mensagens == "object") {

						exibirMensagemDialog(data.mensagens.tipoMensagem,
								data.mensagens.listaMensagens);

					} else {
						return data;
					}
				},
				dataType : 'json',
				colModel : [ {
					display : 'Cota',
					name : 'numeroCota',
					width : 70,
					sortable : true,
					align : 'left'
				}, {
					display : 'Nome',
					name : 'nomeCota',
					width : 290,
					sortable : true,
					align : 'left'
				}, {
					display : 'Total Exemplares',
					name : 'exemplares',
					width : 100,
					sortable : true,
					align : 'center'
				}, {
					display : '',
					name : 'idCota',
					width : 40,
					sortable : false,
					align : 'center',
					process : function(tdDiv, pid) {
						_this.processCotasSuspensas(tdDiv, pid);
					}
				} ],
				sortname : "codigo",
				sortorder : "asc",
				usepager : true,
				useRp : true,
				rp : 15,
				showTableToggleBtn : true,
				width : 570,
				height : 180
			});
};

GeracaoNFe.prototype.processCotasSuspensas = function(tdDiv, pid) {
	var idCota = tdDiv.innerHTML;
	var _this = this;

	var checkBox = document.createElement("input");
	checkBox.type = "checkbox";

	checkBox.className = "checkboxCheckCotasSuspensas";

	checkBox.value = idCota;

	$(checkBox)
			.change(
					function() {
						_this.mapCotasSuspensas[idCota + ''] = $(this).attr(
								'checked') == 'checked';
					});

	$(tdDiv).empty();
	tdDiv.appendChild(checkBox);
};