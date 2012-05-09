/**
 * @param idCota
 * @returns {NotaPromissoria}
 */
function NotaPromissoria(idCota) {
	this.bindEvents();
	this.get();
	this.toggle();
	this.idCota = idCota;
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
	$.getJSON(this.path + 'getByCota.json', {
		'idCota' : this.idCota
	}, function(data) {
		var tipoMensagem = data.tipoMensagem;
		var listaMensagens = data.listaMensagens;

		if (tipoMensagem && listaMensagens) {
			exibirMensagem(tipoMensagem, listaMensagens);
		} else {
			_this.notaPromissoria = data.cotaGarantia.notaPromissoria;
			_this.dataBind();
		}

	},null, true);
};

NotaPromissoria.prototype.toggle = function() {
	$('#cotaGarantiaNotaPromissoriaPanel').toggle();
};

NotaPromissoria.prototype.dataBind = function() {
	$("#cotaGarantiaNotaPromissoriaId").html(this.notaPromissoria.id);
	$("#cotaGarantiaNotaPromissoriaVencimento").val(
			this.notaPromissoria.vencimento.$);
	$("#cotaGarantiaNotaPromissoriaValor").val(this.notaPromissoria.valor);
	$("#cotaGarantiaNotaPromissoriavalorExtenso").val(
			this.notaPromissoria.valorExtenso);
};

NotaPromissoria.prototype.dataUnBind = function() {
	this.notaPromissoria.vencimento = $(
			"#cotaGarantiaNotaPromissoriaVencimento").val();
	this.notaPromissoria.valor = $("#cotaGarantiaNotaPromissoriaValor").val();
	this.notaPromissoria.valorExtenso = $(
			"#cotaGarantiaNotaPromissoriavalorExtenso").val();
};

NotaPromissoria.prototype.bindEvents = function() {
	var _this = this;
	$("#cotaGarantiaNotaPromissoriaVencimento").mask("99/99/9999");
	$("#cotaGarantiaNotaPromissoriaImprimir").bind('click', function() {
		_this.salva(function() {
			_this.imprimi();
		});
	});
};

NotaPromissoria.prototype.imprimi = function() {
	window.open(this.path + 'impriNotaPromissoria/' + this.notaPromissoria.id);
};

function TipoCotaGarantia() {
	this.get();
	this.idCota = 1;
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
		controller : null
	},
	'IMOVEL' : {
		label : 'Imóvel',
		class : null
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

	},null,true);
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
		select.add(option, null);
	}
	
	//this.bindEvents();
};

TipoCotaGarantia.prototype.bindEvents = function() {
	var _this =  this;
	$("#tipoGarantiaSelect").change(function(eventObject) {
		var valor =  $(this).val();
		if(valor.length > 0 ){
			_this.changeController(valor);
		}
	}).change();
};

TipoCotaGarantia.prototype.changeController =  function(tipo){
	
	console.log(tipo);
	if(this.controller ){
		this.controller.toggle();
		this.controller = null;
	}
	
	var obj = this.tipo[tipo].controller;
	this.controller = new obj(this.idCota);	
};
