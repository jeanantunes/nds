/**
 * @param idCota
 * @returns {NotaPromissoria}
 */
function NotaPromissoria(idCota) {
	this.bindEvents();
	this.get();
	//this.toggle();
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
			//exibirMensagem(tipoMensagem, listaMensagens);
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
	this.bindEvents();
	this.idCota = 1;
	this.editor = null;
};
TipoCotaGarantia.prototype.path = contextPath + "/cadastro/garantia/";
TipoCotaGarantia.prototype.tipo = {
	'FIADOR' : {
		label : 'Fiador',
		class : null
	},
	'CHEQUE_CAUCAO' : {
		label : 'Cheque Caução',
		class : null
	},
	'IMOVEL' : {
		label : 'Imóvel',
		class : null
	},
	'NOTA_PROMISSORIA' : {
		label : 'Nota Promissória',
		class : NotaPromissoria
	},
	'CAUCAO_LIQUIDA' : {
		label : 'Caução Liquida',
		class : null
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
		try {
			// for IE earlier than version 8
			select.add(option, select.options[null]);
		} catch (e) {
			select.add(option, null);
		}

	}
};

TipoCotaGarantia.prototype.bindEvents = function() {
	var _this =  this;
	$("#tipoGarantiaSelect").bind('change', function() {
		var valor =  $(this).val();
		if(valor.length > 0 ){
			var obj = _this.tipo[valor];
			_this.editor = new obj.class(_this.idCota);
			_this.editor.toggle();
		}
	});
};
