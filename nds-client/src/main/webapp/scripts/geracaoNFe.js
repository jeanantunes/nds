function GeracaoNFeController() {
	this.initGrid();
	this.init();
	this.mapCotasSuspensas = new Object();
}

GeracaoNFeController.prototype.path = contextPath + '/expedicao/geracaoNFe/';
GeracaoNFeController.prototype.init = function() {
	var _this = this;
	
	
	
	$("#selectFornecedores", this.workspace).multiselect({
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

	$("#btnPesquisar", this.workspace).click(function() {
		_this.pequisar();
	});

	$("#btnGerar", this.workspace).click(function() {
		_this.btnGerarOnClick();
	});

	$("#btnImprimirXLS", this.workspace).click(function() {
		_this.imprimir('XLS');
	});
	$("#btnImprimirPDF", this.workspace).click(function() {
		_this.imprimir('PDF');
	});

	$('#checkboxCheckAllCotasSuspensas', this.workspace).change(
			function() {
				$(".checkboxCheckCotasSuspensas", this.workspace).attr('checked',
						$(this).attr('checked') == 'checked');
			});

	this.$dialogCotasSuspensas = $('#dialogCotasSuspensas', this.workspace).dialog({
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
			"Cancelar o Envio" : function (){
				_this.$dialogCotasSuspensasConfirmar.dialog("open");
			},
			"Cancelar" : function() {
				$(this).dialog("close");
			}
		}
	});
	
	this.$dialogCotasSuspensasConfirmar = $('#dialogCotasSuspensasConfirmar', this.workspace).dialog({
		autoOpen : false,
		resizable : false,
		width : 400,
		modal : true,
		buttons : {
			"Confirmar" : function() {
				$(this).dialog("close");
				_this.transferirSuplementar();
			},
			"Cancelar" : function() {
				$(this).dialog("close");
			}
		}
	});
	
	var dataEmissao = formatDateToString(new Date());
	$("#datepickerEmissao", this.workspace).val(dataEmissao);
};
GeracaoNFeController.prototype.gerar = function() {
	var params = this.getParams();
	var todas = $('#checkboxCheckAllCotasSuspensas', this.workspace).checked;
	
	var cotasSuspensas = $(".checkboxCheckCotasSuspensas", this.workspace);
	
	var listaCotasSuspensas = new Array;
	
	for (var index in cotasSuspensas) {
		if (cotasSuspensas[index].checked) {
			listaCotasSuspensas.push(cotasSuspensas[index].value);
		}
	}
	
	params = serializeArrayToPost('idCotasSuspensas', listaCotasSuspensas, params);
	params['dataEmissao'] = $("#datepickerEmissao", this.workspace).val();
	params['todasCotasSuspensa'] = todas;
	
	var _this = this;
	
	$.postJSON(this.path + 'gerar.json', params, function(data) {
		var tipoMensagem = data.tipoMensagem;
		var listaMensagens = data.listaMensagens;

		if (tipoMensagem && listaMensagens) {
			exibirMensagemDialog(tipoMensagem, listaMensagens, "");
		}
		
		_this.gridReaload(_this.$gridNFe,'busca.json');
		
	});
};

GeracaoNFeController.prototype.transferirSuplementar = function() {
	var params = [];
	
	var cotasSelecionadas = $(".checkboxCheckCotasSuspensas", GeracaoNFeController.workspace);
	
	for (var index in cotasSelecionadas) {
		
		if (cotasSelecionadas[index].checked) {
			
			params.push({name:"idsCota", value: cotasSelecionadas[index].value});
		}
	}
	
	var _this = this;
	
	$.postJSON(this.path + 'transferirSuplementar.json', params, function(data) {
		var tipoMensagem = data.tipoMensagem;
		var listaMensagens = data.listaMensagens;

		if (tipoMensagem && listaMensagens) {
			exibirMensagemDialog(tipoMensagem, listaMensagens, "");
		}
		
		_this.$dialogCotasSuspensas.dialog("close");
		_this.gridReaload(_this.$gridNFe,'busca.json');
		
	});
};

GeracaoNFeController.prototype.imprimir = function(fileType) {
	var params = this.getParams();
	
	var todas = $('#checkboxCheckAllCotasSuspensas', this.workspace).checked;
	
	var cotasSuspensas = $(".checkboxCheckCotasSuspensas", this.workspace);
	
	var listaCotasSuspensas = new Array;
	
	for (var index in cotasSuspensas) {
		if (cotasSuspensas[index].checked) {
			listaCotasSuspensas.push(cotasSuspensas[index].value);
		}
	}
	
	params = serializeArrayToPost('idCotasSuspensas', listaCotasSuspensas, params);
	params['dataEmissao'] = $("#datepickerEmissao", this.workspace).val();
	params['todasCotasSuspensa'] = todas;
	params['fileType'] = fileType;

	$.fileDownload(this.path + 'exportar', {
		httpMethod : "POST",
		data : params
	});
};

GeracaoNFeController.prototype.getParams = function() {
	var params = {
		"intervaloBoxDe" : $("#inputIntervaloBoxDe", this.workspace).val(),
		"intervaloBoxAte" : $("#inputIntervaloBoxAte", this.workspace).val(),
		"intervalorCotaDe" : $("#inputIntervaloCotaDe", this.workspace).val(),
		"intervaloCotaAte" : $("#inputIntervaloCotaAte", this.workspace).val(),
		"intervaloDateMovimentoDe" : $("#datepickerIntervaloMovimentoDe", this.workspace)
				.val(),
		"intervaloDateMovimentoAte" : $("#datepickerIntervaloMovimentoAte", this.workspace)
				.val(),
		"tipoNotaFiscal" : $("#selectTipoNotaFiscal", this.workspace).val()
	};
	var listaFornecedores = $("#selectFornecedores", this.workspace).val();
	if (listaFornecedores) {
		params = serializeArrayToPost('listIdFornecedor', listaFornecedores,
				params);
	}
	return params;
};

GeracaoNFeController.prototype.btnGerarOnClick = function() {
	this.mapCotasSuspensas = new Object();
	
	$('#checkboxCheckAllCotasSuspensas', this.workspace).attr('checked', false);
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
		} else {
			_this.gerar();
		}

	});
};

GeracaoNFeController.prototype.pequisar = function() {
	this.gridReaload(this.$gridNFe, 'busca.json');
	$(".grids").show();
};

GeracaoNFeController.prototype.gridReaload = function(grid, uri) {
	var params = [ {
		name : "tipoNotaFiscal",
		value : $("#selectTipoNotaFiscal", this.workspace).val()
	} ];

	if ($("#inputIntervaloBoxDe", this.workspace).val().length > 0) {
		params.push({
			name : "intervaloBoxDe",
			value : $("#inputIntervaloBoxDe", this.workspace).val()
		});
	}

	if ($("#inputIntervaloBoxAte", this.workspace).val().length > 0) {
		params.push({
			name : "intervaloBoxAte",
			value : $("#inputIntervaloBoxAte", this.workspace).val()
		});
	}

	if ($("#inputIntervaloCotaDe", this.workspace).val().length > 0) {
		params.push({
			name : "intervaloCotaDe",
			value : parseInt($("#inputIntervaloCotaDe", this.workspace).val())
		});
	}

	if ($("#inputIntervaloCotaAte", this.workspace).val().length > 0) {
		params.push({
			name : "intervaloCotaAte",
			value : parseInt($("#inputIntervaloCotaAte", this.workspace).val())
		});
	}

	if ($("#datepickerIntervaloMovimentoDe", this.workspace).val().length > 0) {
		params.push({
			name : "intervaloDateMovimentoDe",
			value : $("#datepickerIntervaloMovimentoDe", this.workspace).val()
		});
	}

	if ($("#datepickerIntervaloMovimentoAte", this.workspace).val().length > 0) {
		params.push({
			name : "intervaloDateMovimentoAte",
			value : $("#datepickerIntervaloMovimentoAte", this.workspace).val()
		});
	}
	
	if (($("#inputIntervaloCotaDe", this.workspace).val().length > 0)
			&& ($("#inputIntervaloCotaAte", this.workspace).val().length > 0) 
			&& (($("#datepickerIntervaloMovimentoDe", this.workspace).val().length == 0) 
					||  ($("#datepickerIntervaloMovimentoAte", this.workspace).val().length == 0))) {
		exibirMensagem("WARNING", ["Quando haver intervalo de [Cota], deve haver também intervalo de [Data de Movimento]"], "");
		return;
	}
	
	var listaFornecedores = $("#selectFornecedores", this.workspace).val();
	if (listaFornecedores) {
		$.each(listaFornecedores, function(index, value) {
			params.push({
				'name' : "listIdFornecedor[]",
				'value' : value
			});
		});
	}
	
	params.push({
		'name' : "idRoteiro",
		'value' : $("#listRoteiro", this.workspace).val()
	});
	
	params.push({
		'name' : "idRota",
		'value' : $("#listRota", this.workspace).val()
	});

	grid.flexOptions({
		"url" : this.path + uri,
		params : params,
		newp : 1
	});
	grid.flexReload();
};

GeracaoNFeController.prototype.initGrid = function() {
	var _this = this;
	this.$gridNFe = $("#gridNFe", this.workspace).flexigrid(
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
					width : 525,
					sortable : true,
					align : 'left'
				}, {
					display : 'Total Exemplares',
					name : 'exemplares',
					width : 110,
					sortable : true,
					align : 'center'
				}, {
					display : 'Total R$',
					name : 'total',
					width : 90,
					sortable : true,
					align : 'right'
				}, {
					display : 'Total Desconto R$',
					name : 'totalDesconto',
					width : 105,
					sortable : true,
					align : 'right'
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
	this.$gridCotasSuspensas = $("#gridCotasSuspensas", this.workspace).flexigrid(
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

GeracaoNFeController.prototype.processCotasSuspensas = function(tdDiv, pid) {
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