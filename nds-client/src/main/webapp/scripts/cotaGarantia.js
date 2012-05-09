/**
 * @param idCota
 * @returns {NotaPromissoria}
 */
function NotaPromissoria(idCota) {
	this.idCota = idCota;
	this.bindEvents();
	this.get();
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
		} else if (data.cotaGarantia && data.cotaGarantia.notaPromissoria) {
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
			this.notaPromissoria.vencimento.$);
	$("#cotaGarantiaNotaPromissoriaValor").val(this.notaPromissoria.valor);
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

/**
 * @param idCota
 * @returns {ChequeCaucao}
 */
function ChequeCaucao(idCota) {
	this.idCota = idCota;
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
	dvConta :null,
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
		} else if (data.cotaGarantia && data.cotaGarantia.chequeCaucao) {
			_this.chequeCaucao = data.cotaGarantia.chequeCaucao;
			_this.dataBind();
		}

	}, null, true);
};

ChequeCaucao.prototype.salva = function(callBack) {
	this.dataUnBind();
	var postData = this.processPost('chequeCaucao', this.chequeCaucao);
	postData['idCota'] = this.idCota;

	$.postJSON(this.path + 'salvaChequeCaucao.json', postData,
			function(data) {
				var tipoMensagem = data.tipoMensagem;
				var listaMensagens = data.listaMensagens;

				if (tipoMensagem && listaMensagens) {
					exibirMensagem(tipoMensagem, listaMensagens);
				}
				if(callBack){
					callBack();
				}

			}, null, true);
};


ChequeCaucao.prototype.toggle = function() {
	$('#cotaGarantiaChequeCaucaoPanel').toggle();
};

ChequeCaucao.prototype.dataBind = function() {
	
	$("#cotaGarantiaChequeCaucaoNumeroBanco").val(this.chequeCaucao.numeroBanco);
	$("#cotaGarantiaChequeCaucaoNomeBanco").val(this.chequeCaucao.nomeBanco);
	$("#cotaGarantiaChequeCaucaoAgencia").val(this.chequeCaucao.agencia);
	$("#cotaGarantiaChequeCaucaoDvAgencia").val(this.chequeCaucao.dvAgencia);
	$("#cotaGarantiaChequeCaucaoConta").val(this.chequeCaucao.conta);
	$("#cotaGarantiaChequeCaucaoDvConta").val(this.chequeCaucao.dvConta);
	$("#cotaGarantiaChequeCaucaoNumeroCheque").val(this.chequeCaucao.numeroCheque);
	$("#cotaGarantiaChequeCaucaoValor").val(this.chequeCaucao.valor);
	$("#cotaGarantiaChequeCaucaoValor").priceFormat({
		allowNegative : true,
		centsSeparator : ',',
		thousandsSeparator : '.'
	});
	$("#cotaGarantiaChequeCaucaoEmissao").val(this.chequeCaucao.emissao);
	$("#cotaGarantiaChequeCaucaoValidade").val(this.chequeCaucao.validade);
	$("#cotaGarantiaChequeCaucaoCorrentista").val(this.chequeCaucao.correntista);
	
};

ChequeCaucao.prototype.dataUnBind = function() {
	
	this.chequeCaucao.numeroBanco = $("#cotaGarantiaChequeCaucaoNumeroBanco").val();
	this.chequeCaucao.nomeBanco = $("#cotaGarantiaChequeCaucaoNomeBanco").val();
	this.chequeCaucao.agencia = $("#cotaGarantiaChequeCaucaoAgencia").val();
	this.chequeCaucao.dvAgencia = $("#cotaGarantiaChequeCaucaoDvAgencia").val();
	this.chequeCaucao.conta = $("#cotaGarantiaChequeCaucaoConta").val();
	this.chequeCaucao.dvConta = $("#cotaGarantiaChequeCaucaoDvConta").val();
	this.chequeCaucao.numeroCheque = $("#cotaGarantiaChequeCaucaoNumeroCheque").val();
	this.chequeCaucao.valor = $("#cotaGarantiaChequeCaucaoValor").unmask() / 100;;
	this.chequeCaucao.emissao = $("#cotaGarantiaChequeCaucaoEmissao").val();
	this.chequeCaucao.validade = $("#cotaGarantiaChequeCaucaoValidade").val();
	this.chequeCaucao.correntista = $("#cotaGarantiaChequeCaucaoCorrentista").val();
	
};

ChequeCaucao.prototype.bindEvents = function() {
	$("#cotaGarantiaChequeCaucaoEmissao").mask("99/99/9999");
	$("#cotaGarantiaChequeCaucaoValidade").mask("99/99/9999");
	$("#cotaGarantiaChequeCaucaoValor").priceFormat({
		allowNegative : true,
		centsSeparator : ',',
		thousandsSeparator : '.'
	});
	
};

function TipoCotaGarantia() {
	this.get();
	this.controller = null;
};
TipoCotaGarantia.prototype.path = contextPath + "/cadastro/garantia/";
TipoCotaGarantia.prototype.tipo = {
	'FIADOR' : {
		label : 'Fiador',
		controller : null
	},
	'CHEQUE_CAUCAO' : {
		label : 'Cheque Caução',
		controller : ChequeCaucao
	},
	'IMOVEL' : {
		label : 'Imóvel',
		controller : null
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

	// this.bindEvents();
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

TipoCotaGarantia.prototype.changeController = function(tipo) {

	console.log(tipo);
	if (this.controller) {
		this.controller.toggle();
		this.controller = null;
	}

	var obj = this.tipo[tipo].controller;
	this.controller = new obj(this.getIdCota());
};

TipoCotaGarantia.prototype.getIdCota = function() {
	return $('#_idCotaRef').val();
};
