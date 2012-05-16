/**
 * @param idCota
 * @returns {NotaPromissoria}
 */
function NotaPromissoria(idCota, cotaGarantia) {
	this.idCota = idCota;
	this.bindEvents();
	if (cotaGarantia && cotaGarantia.notaPromissoria) {
		this.notaPromissoria = cotaGarantia.notaPromissoria;
		this.dataBind();
	}
	this.toggle();
};
NotaPromissoria.prototype.path = contextPath + "/cadastro/garantia/";
NotaPromissoria.prototype.notaPromissoria = {
	id : null,
	vencimento : null,
	valor : null,
	valorExtenso : null
};
NotaPromissoria.prototype.salva = function(callBack) {
	this.dataUnBind();
	var postData = this.processPost('notaPromissoria', this.notaPromissoria);
	postData['idCota'] = this.idCota;

	$.postJSON(this.path + 'salvaNotaPromissoria.json', postData,
			function(data) {
				var tipoMensagem = data.tipoMensagem;
				var listaMensagens = data.listaMensagens;

				if (tipoMensagem && listaMensagens) {
					exibirMensagem(tipoMensagem, listaMensagens);
				}
				callBack();

			}, null, true);
	return false;
};

NotaPromissoria.prototype.processPost = function(objectName, object) {
	var obj = {};
	for ( var propriedade in object) {
		obj[objectName + '.' + propriedade] = object[propriedade];
	}
	return obj;
};

NotaPromissoria.prototype.get = function() {
	var _this = this;
	$.postJSON(this.path + 'getByCota.json', {
		'idCota' : this.idCota
	}, function(data) {

		var tipoMensagem = data.tipoMensagem;
		var listaMensagens = data.listaMensagens;
		if (tipoMensagem && listaMensagens) {
			exibirMensagem(tipoMensagem, listaMensagens);
		} else if (data && data.notaPromissoria) {
			_this.notaPromissoria = data.cotaGarantia.notaPromissoria;
			_this.dataBind();
		}

	}, null, true);
};

NotaPromissoria.prototype.toggle = function() {
	$('#cotaGarantiaNotaPromissoriaPanel').toggle();
};

NotaPromissoria.prototype.dataBind = function() {
	$("#cotaGarantiaNotaPromissoriaId").html(this.notaPromissoria.id);
	$("#cotaGarantiaNotaPromissoriaVencimento").val(
			this.notaPromissoria.vencimento);
	$("#cotaGarantiaNotaPromissoriaValor").val(this.notaPromissoria.valor*100);
	$("#cotaGarantiaNotaPromissoriaValor").priceFormat({
		allowNegative : true,
		centsSeparator : ',',
		thousandsSeparator : '.'
	});
	$("#cotaGarantiaNotaPromissoriavalorExtenso").val(
			this.notaPromissoria.valorExtenso);
};

NotaPromissoria.prototype.dataUnBind = function() {
	this.notaPromissoria.vencimento = $(
			"#cotaGarantiaNotaPromissoriaVencimento").val();
	this.notaPromissoria.valor = $("#cotaGarantiaNotaPromissoriaValor")
			.unmask() / 100;
	this.notaPromissoria.valorExtenso = $(
			"#cotaGarantiaNotaPromissoriavalorExtenso").val();
};

NotaPromissoria.prototype.bindEvents = function() {
	var _this = this;
	$("#cotaGarantiaNotaPromissoriaVencimento").mask("99/99/9999");
	$("#cotaGarantiaNotaPromissoriaValor").priceFormat({
		allowNegative : true,
		centsSeparator : ',',
		thousandsSeparator : '.'
	});
	$("#cotaGarantiaNotaPromissoriaImprimir").bind('click', function() {
		_this.salva(function() {
			_this.imprimi();
		});
	});
};

NotaPromissoria.prototype.imprimi = function() {
	window.open(this.path + 'impriNotaPromissoria/' + this.notaPromissoria.id);
};

// **************** CHEQUE CAUCAO PROTOTYPE ********************//
/**
 * @param idCota
 * @returns {ChequeCaucao}
 */
function ChequeCaucao(idCota, cotaGarantia) {
	this.idCota = idCota;
	this.cotaGarantia = cotaGarantia;
	this.bindEvents();
	this.get();
	this.toggle();
};
ChequeCaucao.prototype.path = contextPath + "/cadastro/garantia/";
ChequeCaucao.prototype.chequeCaucao = {
	id : null,
	numeroBanco : null,
	nomeBanco : null,
	agencia : null,
	dvAgencia : null,
	conta : null,
	dvConta : null,
	valor : null,
	numeroCheque : null,
	emissao : null,
	validade : null,
	correntista : null
};

ChequeCaucao.prototype.processPost = function(objectName, object) {
	var obj = {};
	for ( var propriedade in object) {
		obj[objectName + '.' + propriedade] = object[propriedade];
	}
	return obj;
};

ChequeCaucao.prototype.get = function() {

	var _this = this;
	$.postJSON(this.path + 'getByCota.json', {
		'idCota' : this.idCota
	}, function(data) {

		var tipoMensagem = data.tipoMensagem;
		var listaMensagens = data.listaMensagens;
		if (tipoMensagem && listaMensagens) {
			exibirMensagem(tipoMensagem, listaMensagens);
		} else if (data && data.chequeCaucao) {
			_this.chequeCaucao = data.cotaGarantia.chequeCaucao;
			_this.dataBind();
		}

	}, null, true);
};

ChequeCaucao.prototype.salva = function(callBack) {
	this.dataUnBind();
	var postData = this.processPost('chequeCaucao', this.chequeCaucao);
	postData['idCota'] = this.idCota;

	$.postJSON(this.path + 'salvaChequeCaucao.json', postData, function(data) {
		var tipoMensagem = data.tipoMensagem;
		var listaMensagens = data.listaMensagens;

		if (tipoMensagem && listaMensagens) {
			exibirMensagem(tipoMensagem, listaMensagens);
		}
		if (callBack) {
			callBack();
		}

	}, null, true);
	return false;
};

ChequeCaucao.prototype.toggle = function() {
	$('#cotaGarantiaChequeCaucaoPanel').toggle();
};

ChequeCaucao.prototype.dataBind = function() {

	$("#cotaGarantiaChequeCaucaoNumeroBanco")
			.val(this.chequeCaucao.numeroBanco);
	$("#cotaGarantiaChequeCaucaoNomeBanco").val(this.chequeCaucao.nomeBanco);
	$("#cotaGarantiaChequeCaucaoAgencia").val(this.chequeCaucao.agencia);
	$("#cotaGarantiaChequeCaucaoDvAgencia").val(this.chequeCaucao.dvAgencia);
	$("#cotaGarantiaChequeCaucaoConta").val(this.chequeCaucao.conta);
	$("#cotaGarantiaChequeCaucaoDvConta").val(this.chequeCaucao.dvConta);
	$("#cotaGarantiaChequeCaucaoNumeroCheque").val(
			this.chequeCaucao.numeroCheque);
	$("#cotaGarantiaChequeCaucaoValor").val(this.chequeCaucao.valor);
	$("#cotaGarantiaChequeCaucaoValor").priceFormat({
		allowNegative : true,
		centsSeparator : ',',
		thousandsSeparator : '.'
	});
	$("#cotaGarantiaChequeCaucaoEmissao").val(this.chequeCaucao.emissao);
	$("#cotaGarantiaChequeCaucaoValidade").val(this.chequeCaucao.validade);
	$("#cotaGarantiaChequeCaucaoCorrentista")
			.val(this.chequeCaucao.correntista);

};

ChequeCaucao.prototype.dataUnBind = function() {

	this.chequeCaucao.numeroBanco = $("#cotaGarantiaChequeCaucaoNumeroBanco")
			.val();
	this.chequeCaucao.nomeBanco = $("#cotaGarantiaChequeCaucaoNomeBanco").val();
	this.chequeCaucao.agencia = $("#cotaGarantiaChequeCaucaoAgencia").val();
	this.chequeCaucao.dvAgencia = $("#cotaGarantiaChequeCaucaoDvAgencia").val();
	this.chequeCaucao.conta = $("#cotaGarantiaChequeCaucaoConta").val();
	this.chequeCaucao.dvConta = $("#cotaGarantiaChequeCaucaoDvConta").val();
	this.chequeCaucao.numeroCheque = $("#cotaGarantiaChequeCaucaoNumeroCheque")
			.val();
	this.chequeCaucao.valor = $("#cotaGarantiaChequeCaucaoValor").unmask() / 100;
	this.chequeCaucao.emissao = $("#cotaGarantiaChequeCaucaoEmissao").val();
	this.chequeCaucao.validade = $("#cotaGarantiaChequeCaucaoValidade").val();
	this.chequeCaucao.correntista = $("#cotaGarantiaChequeCaucaoCorrentista")
			.val();

};

ChequeCaucao.prototype.bindEvents = function() {
	$("#cotaGarantiaChequeCaucaoAgencia").numeric();
	$("#cotaGarantiaChequeCaucaoConta").numeric();
	$("#cotaGarantiaChequeCaucaoEmissao").mask("99/99/9999");
	$("#cotaGarantiaChequeCaucaoValidade").mask("99/99/9999");
	$("#cotaGarantiaChequeCaucaoValor").priceFormat({
		allowNegative : true,
		centsSeparator : ',',
		thousandsSeparator : '.'
	});

};

// **************** IMOVEL PROTOTYPE ********************//
/**
 * @param idCota
 * @returns {Imovel}
 */
function Imovel(idCota, cotaGarantia) {
	this.idCota = idCota;
	this.cotaGarantia = cotaGarantia;
	this.bindEvents();
	this.get();
	this.toggle();
	this.initGrid();
	this.rows = new Array;
	this.itemEdicao = null;
};

Imovel.prototype.path = contextPath + "/cadastro/garantia/";
Imovel.prototype.imovel = {
	id : null,
	proprietario : null,
	endereco : null,
	numeroRegistro : null,
	valor : null,
	observacao : null
};

Imovel.prototype.processPost = function(objectName, object) {
	var obj = {};
	for ( var propriedade in object) {
		obj[objectName + '.' + propriedade] = object[propriedade];
	}
	return obj;
};

Imovel.prototype.processListPost = function(listaName, lista) {
	var obj = {};

	for ( var i = 0; i < lista.length; i++) {

		var object = lista[i];
		for ( var propriedade in object) {
			obj[listaName + '[' + i + '].' + propriedade] = object[propriedade];
		}
	}

	return obj;
};

Imovel.prototype.toggle = function() {
	$('#cotaGarantiaImovelPanel').toggle();
};

Imovel.prototype.get = function() {
	var _this = this;
	$.postJSON(this.path + 'getByCota.json', {
		'idCota' : this.idCota
	}, function(data) {

		var tipoMensagem = data.tipoMensagem;
		var listaMensagens = data.listaMensagens;
		if (tipoMensagem && listaMensagens) {
			exibirMensagem(tipoMensagem, listaMensagens);
		} else if (data && data.imovel) {
			_this.imovel = data.cotaGarantia.imovel;
			_this.dataBind();
		}

	}, null, true);
};

Imovel.prototype.incluirImovel = function(callBack) {
	var _this = this;
	this.dataUnBind();
	var postData = this.processPost('imovel', this.imovel);
	postData['idCota'] = this.idCota;

	$.postJSON(this.path + 'incluirImovel.json', postData, function(data) {
		var tipoMensagem = data.tipoMensagem;
		var listaMensagens = data.listaMensagens;

		if (tipoMensagem && listaMensagens) {
			exibirMensagem(tipoMensagem, listaMensagens);
		} else {

			var novoImovel = data.imovel;
			var rows = _this.rows;

			if (_this.itemEdicao == null || _this.itemEdicao < 0) {
				rows.push({
					"id" : rows.length,
					"cell" : novoImovel
				});
			} else {
				rows.slice(_this.itemEdicao, 1);
				rows[_this.itemEdicao] = {
					"id" : _this.itemEdicao,
					"cell" : novoImovel
				};
			}

			$(".cotaGarantiaImovelGrid").flexAddData({
				rows : rows,
				page : 1,
				total : 1
			});

			_this.limparForm();
		}

		if (callBack) {
			callBack();
		}

	}, null, true);
};

Imovel.prototype.salva = function(callBack) {
	var listaImoveis = new Array;

	for ( var index in this.rows) {
		var imovel = this.rows[index].cell;
		listaImoveis.push(imovel);
	}

	var postData = this.processListPost('listaImoveis', listaImoveis);
	postData['idCota'] = this.idCota;

	$.postJSON(this.path + 'salvaImovel.json', postData, function(data) {
		var tipoMensagem = data.tipoMensagem;
		var listaMensagens = data.listaMensagens;

		if (tipoMensagem && listaMensagens) {
			exibirMensagem(tipoMensagem, listaMensagens);
		}
		if (callBack) {
			callBack();
		}

	}, null, true);
	return false;
};

Imovel.prototype.limparForm = function() {
	this.imovel.proprietario = "";
	this.imovel.endereco = "";
	this.imovel.numeroRegistro = "";
	this.imovel.valor = "";
	this.imovel.observacao = "";
	this.itemEdicao = null;
	this.dataBind();
	$("#cotaGarantiaImovelSalvaEdicao").hide();
	$("#cotaGarantiaImovelIncluirNovo").show();
};

Imovel.prototype.dataBind = function() {
	$("#cotaGarantiaImovelProprietario").val(this.imovel.proprietario);
	$("#cotaGarantiaImovelEndereco").val(this.imovel.endereco);
	$("#cotaGarantiaImovelNumeroRegistro").val(this.imovel.numeroRegistro);
	$("#cotaGarantiaImovelValor").val(this.imovel.valor);
	$("#cotaGarantiaImovelValor").priceFormat({
		allowNegative : true,
		centsSeparator : ',',
		thousandsSeparator : '.'
	});
	$("#cotaGarantiaImovelObservacao").val(this.imovel.observacao);

};

Imovel.prototype.dataUnBind = function() {
	this.imovel.proprietario = $("#cotaGarantiaImovelProprietario").val();
	this.imovel.endereco = $("#cotaGarantiaImovelEndereco").val();
	this.imovel.numeroRegistro = $("#cotaGarantiaImovelNumeroRegistro").val();
	this.imovel.valor = $("#cotaGarantiaImovelValor").unmask() / 100;
	this.imovel.observacao = $("#cotaGarantiaImovelObservacao").val();
};

Imovel.prototype.bindEvents = function() {
	var _this = this;

	$("#cotaGarantiaImovelIncluirNovo").click(function() {
		_this.incluirImovel();
	});

	$("#cotaGarantiaImovelSalvaEdicao").click(function() {
		_this.incluirImovel();
	});

	$("#cotaGarantiaImovelValor").priceFormat({
		allowNegative : true,
		centsSeparator : ',',
		thousandsSeparator : '.'
	});
};

Imovel.prototype.edita = function(id) {
	this.imovel = this.rows[id].cell;
	this.itemEdicao = id;
	this.dataBind();
	$("#cotaGarantiaImovelSalvaEdicao").show();
	$("#cotaGarantiaImovelIncluirNovo").hide();
};

Imovel.prototype.remove = function(id) {

	var _this = this;

	$("#dialog-excluir-imovel").dialog({
		resizable : false,
		height : 'auto',
		width : 380,
		modal : true,
		buttons : {
			"Confirmar" : function() {

				_this.rows.splice(id, 1);

				var lista = new Array;

				for ( var index in _this.rows) {
					lista.push({
						"id" : lista.length,
						"cell" : _this.rows[index].cell
					});
				}

				_this.rows = lista;

				$(".cotaGarantiaImovelGrid").flexAddData({
					rows : _this.rows,
					page : 1,
					total : 1
				});
				$(this).dialog("close");
			},
			"Cancelar" : function() {
				$(this).dialog("close");
			}
		}
	});

};

Imovel.prototype.initGrid = function() {

	$(".cotaGarantiaImovelGrid")
			.flexigrid(
					{

						preProcess : function(data) {
							if (typeof data.mensagens == "object") {

								exibirMensagem(data.mensagens.tipoMensagem,
										data.mensagens.listaMensagens);

							} else {
								$
										.each(
												data.rows,
												function(index, value) {

													var idImovel = value.id;

													var acao = '<a href="javascript:;" onclick="tipoCotaGarantia.controller.edita('
															+ idImovel
															+ ');" ><img src="'
															+ contextPath
															+ '/images/ico_editar.gif" border="0" hspace="5" /></a>';
													acao += '<a href="javascript:;" onclick="tipoCotaGarantia.controller.remove('
															+ idImovel
															+ ');" ><img src="'
															+ contextPath
															+ '/images/ico_excluir.gif" hspace="5" border="0" /></a>';

													value.cell.acao = acao;
												});

								return data;
							}
						},

						dataType : 'json',
						colModel : [ {
							display : 'Proprietário',
							name : 'proprietario',
							width : 115,
							sortable : true,
							align : 'left'
						}, {
							display : 'Endereço',
							name : 'endereco',
							width : 150,
							sortable : true,
							align : 'left'
						}, {
							display : 'N° Registro',
							name : 'numeroRegistro',
							width : 80,
							sortable : true,
							align : 'left'
						}, {
							display : 'Valor R$',
							name : 'valor',
							width : 80,
							sortable : true,
							align : 'right'
						}, {
							display : 'Observação',
							name : 'observacao',
							width : 180,
							sortable : true,
							align : 'left'
						}, {
							display : 'Ação',
							name : 'acao',
							width : 60,
							sortable : false,
							align : 'center'
						}

						],

						width : 740,
						height : 150,
						sortorder : "asc",
						sortname : "",
						singleSelect : true
					});
};



// **************** TIPO GARANTIA PROTOTYPE ********************//
function TipoCotaGarantia() {
	this.get();
	this.controller = null;
	this.newControllerType = null;
	this.controllerType = null;
	var _this = this;
	this.confirmDialog = new ConfirmDialog('Ao mudar de garantia, os dados da garantia anterior serão perdidos.</br>Deseja continuar?', function() {		
		 _this.changeController(_this.newControllerType);
		 return true;
	}, function() {
		 $("#tipoGarantiaSelect").val(_this.controllerType);
	});	
};
TipoCotaGarantia.prototype.path = contextPath + "/cadastro/garantia/";
TipoCotaGarantia.prototype.tipo = {
	'FIADOR' : {
		label : 'Fiador',
		controller : Fiador
	},
	'CHEQUE_CAUCAO' : {
		label : 'Cheque Caução',
		controller : ChequeCaucao
	},
	'IMOVEL' : {
		label : 'Imóvel',
		controller : Imovel
	},
	'NOTA_PROMISSORIA' : {
		label : 'Nota Promissória',
		controller : NotaPromissoria
	},
	'CAUCAO_LIQUIDA' : {
		label : 'Caução Liquida',
		controller : null
	}
};
TipoCotaGarantia.prototype.get = function() {
	var _this = this;
	$.getJSON(this.path + 'getTiposGarantia.json', null, function(data) {
		var tipoMensagem = data.tipoMensagem;
		var listaMensagens = data.listaMensagens;

		if (tipoMensagem && listaMensagens) {
			exibirMensagem(tipoMensagem, listaMensagens);
		} else {
			_this.bindData(data);
		}

	}, null, true);
};

TipoCotaGarantia.prototype.getData = function() {
	var _this = this;
	$.postJSON(this.path + 'getByCota.json', {
		'idCota' : this.getIdCota()
	}, function(data) {
		var tipoMensagem = data.tipoMensagem;
		var listaMensagens = data.listaMensagens;
		if (tipoMensagem && listaMensagens) {
			exibirMensagem(tipoMensagem, listaMensagens);
		} else if (data && data.cotaGarantia) {
			_this.cotaGarantia = data.cotaGarantia;
			_this.cotaGarantia.controllerType = data.tipo;
			_this.changeController(data.tipo);
			$("#tipoGarantiaSelect").val(data.tipo);
		}

	}, null, true);
};
TipoCotaGarantia.prototype.bindData = function(data) {
	var select = document.getElementById("tipoGarantiaSelect");
	for ( var index = select.options.length; index > 0; index--) {
		select.remove(index);
	}

	for ( var index in data) {
		var tipo = data[index];
		var option = document.createElement("option");
		option.text = this.tipo[tipo].label;
		option.value = tipo;
		try {
			select.add(option, select.options[null]);
		} catch (e) {
			select.add(option, null);
		}
	}
};

TipoCotaGarantia.prototype.onOpen = function() {
	this.getData();
};

TipoCotaGarantia.prototype.bindEvents = function() {
	var _this = this;
	$("#tipoGarantiaSelect").change(function(eventObject) {
		var valor = $(this).val();
		if (valor.length > 0) {
			_this.changeController(valor);
		}
	}).change();
};

TipoCotaGarantia.prototype.onChange = function(newControllerType) {
	if (this.cotaGarantia && this.cotaGarantia.id && this.cotaGarantia.controllerType == this.controllerType) {
		this.newControllerType = newControllerType;
		// TODO: validar no servidor.
		this.confirmDialog.open();
	} else {
		this.newControllerType = null;
		this.changeController(newControllerType);
	}
};

TipoCotaGarantia.prototype.changeController = function(newControllerType) {
	if (this.controller) {
		this.controller.toggle();
		this.controller = null;
	}
	var obj = this.tipo[newControllerType].controller;
	this.controller = new obj(this.getIdCota(), this.cotaGarantia);
	this.controllerType = newControllerType;

};

TipoCotaGarantia.prototype.getIdCota = function() {
	return MANTER_COTA.idCota;
};

// **************** FIADOR PROTOTYPE ********************//
function Fiador(idCota, cotaGarantia) {
	this._idCota = idCota;
	this.bindEvents();
	this.toggle();
	this.initGrid();
	this.$dialog = $('<div></div>').html(
			'Fiador não encontrado. Deseja cadastrar um?').dialog({
		autoOpen : false,
		title : 'Fiador não encontrado',
		resizable : false,
		height : 140,
		modal : true,
		buttons : {
			"Ir para Cadastro Fiador" : function() {
				location.replace(contextPath + '/cadastro/fiador');
			},
			Cancel : function() {
				$(this).dialog("close");
			}
		}
	});

	if (cotaGarantia && cotaGarantia.fiador) {
		this.fiador = cotaGarantia.fiador;
		this.bindData();
		this.toggleDados(true);
	}

};
Fiador.prototype.path = contextPath + "/cadastro/garantia/";
Fiador.prototype.toggle = function() {
	$('#cotaGarantiaFiadorPanel').toggle();
};

Fiador.prototype.bindEvents = function() {
	var _this = this;
	$("#cotaGarantiaFiadorSearchName").autocomplete({
		source : function(request, response) {
			$.postJSON(_this.path + 'buscaFiador.json', {
				nome : request.term,
				maxResults : 6
			}, function(data) {
				response($.map(data.items, function(item) {
					return {
						label : item.value.$,
						value : item.value.$,
						key : item.key.$
					};
				}));

			}, null, true);

		},
		minLength : 3,
		select : function(event, ui) {
			_this.getFiador(ui.item.key, null);
		},
		open : function() {
			$(this).removeClass("ui-corner-all").addClass("ui-corner-top");
		},
		close : function() {
			$(this).removeClass("ui-corner-top").addClass("ui-corner-all");
		}
	});

	$("#cotaGarantiaFiadorSearchDoc").keypress(function(e) {
		if ((e.which && e.which == 13) || (e.keyCode && e.keyCode == 13)) {
			$("#cotaGarantiaFiadorSearchName").val("");
			_this.getFiador(null, $("#cotaGarantiaFiadorSearchDoc").val());
		}
	});

};
Fiador.prototype.getFiador = function(idFiador, documento) {
	var _this = this;
	var param = {};
	if (idFiador) {
		param.idFiador = idFiador;
	} else if (documento) {
		param.documento = documento;
	}
	;

	$.postJSON(this.path + "getFiador.json", param, function(data) {
		if (data === "NotFound") {
			_this.confirma();
			_this.toggleDados(false);
			_this.fiador = null;
		} else if (data.tipoMensagem && data.listaMensagens) {
			exibirMensagem(data.tipoMensagem, data.listaMensagens);
			_this.toggleDados(false);
			_this.fiador = null;
		} else {
			_this.fiador = data;
			_this.bindData();
			_this.toggleDados(true);
		}
	}, null, true);
};

Fiador.prototype.confirma = function() {
	this.$dialog.dialog('open');
};

Fiador.prototype.toggleDados = function(showOrHide) {
	$('#cotaGarantiaFiadorDadosPanel').toggle(showOrHide);
};
Fiador.prototype.bindData = function() {

	var nome;
	var doc;
	if (this.fiador.pessoa.nome) {
		nome = this.fiador.pessoa.nome;
		doc = this.fiador.pessoa.cpf;
	} else {
		nome = this.fiador.pessoa.razaoSocial;
		doc = this.fiador.pessoa.cnpj;
	}
	console.log();
	$("#cotaGarantiaFiadorNome").html(nome);
	$("#cotaGarantiaFiadorDoc").html(doc);

	var endereco = this.fiador.pessoa.enderecos[0];
	var strEndereco = endereco.tipoLogradouro + ' ' + endereco.logradouro
			+ ', ' + endereco.numero + ' - ' + endereco.bairro + ' - '
			+ endereco.cidade + '/' + endereco.uf;

	$("#cotaGarantiaFiadorEndereco").html(strEndereco);
	var telefone = '';

	for ( var i in this.fiador.telefonesFiador) {
		if (this.fiador.telefonesFiador[i].principal) {
			telefone = this.fiador.telefonesFiador[i];
		}
	}

	$("#cotaGarantiaFiadorTelefone").html(
			'(' + telefone.telefone.ddd + ') ' + telefone.telefone.numero);

	var rows = new Array();
	for ( var id in this.fiador.garantias) {
		rows[id] = {
			"id" : id,
			"cell" : this.fiador.garantias[id]
		};
	}

	$("#cotaGarantiaFiadorGarantiasGrid").flexAddData({
		rows : rows,
		page : 1,
		total : 1
	});
};
Fiador.prototype.initGrid = function() {
	$("#cotaGarantiaFiadorGarantiasGrid").flexigrid({
		dataType : 'json',
		colModel : [ {
			display : 'Descrição',
			name : 'descricao',
			width : 550,
			sortable : false,
			align : 'left'

		}, {

			display : 'Valor R$',
			name : 'valor',
			width : 140,
			sortable : false,
			align : 'right'

		} ],
		width : 740,
		height : 150
	});
};
Fiador.prototype.salva = function(callBack) {
	var _this = this;
	$.postJSON(this.path + "salvaFiador.json", {
		idFiador : this.fiador.id,
		idCota : this._idCota
	}, function(data) {
		if (data.tipoMensagem && data.listaMensagens) {
			exibirMensagem(data.tipoMensagem, data.listaMensagens);
			_this.get();
		}
		if(callBack){
			callBack();
		}
	}, null, true);
	return false;
};
Fiador.prototype.get = function() {
	var _this = this;
	$.postJSON(this.path + 'getByCota.json', {
		'idCota' : this._idCota
	}, function(data) {

		var tipoMensagem = data.tipoMensagem;
		var listaMensagens = data.listaMensagens;
		if (tipoMensagem && listaMensagens) {
			exibirMensagem(tipoMensagem, listaMensagens);
		} else if (data && data.fiador) {
			_this.getFiador(data.fiador.id, null);
		}

	}, null, true);

};
