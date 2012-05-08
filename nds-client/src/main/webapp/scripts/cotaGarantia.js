
/**
 * @param idCota
 * @returns {NotaPromissoria}
 */
function NotaPromissoria(idCota) {
	this.idCota = idCota;

	this.path = contextPath + "/cadastro/garantia/";

	this.notaPromissoria = {
		id : null,
		vencimento : null,
		valor : null,
		valorExtenso : null
	};
	this.salva = function(callBack) {
		this.dataUnBind();
		var postData = this
				.processPost('notaPromissoria', this.notaPromissoria);
		postData['idCota'] = this.idCota;

		$.postJSON(this.path + 'salvaNotaPromissoria.json', postData, function(
				data) {
			var tipoMensagem = data.tipoMensagem;
			var listaMensagens = data.listaMensagens;

			if (tipoMensagem && listaMensagens) {
				exibirMensagem(tipoMensagem, listaMensagens);
			}
			callBack();

		}, null, true);
	};

	this.processPost = function(objectName, object) {
		var obj = {};
		for ( var propriedade in object) {
			obj[objectName + '.' + propriedade] = object[propriedade];
		}
		return obj;
	};

	this.get = function() {

		var _this = this;
		$.getJSON(this.path + 'getByCota.json', {
			'idCota' : this.idCota
		}, function(data) {

			console.log(data);
			var tipoMensagem = data.tipoMensagem;
			var listaMensagens = data.listaMensagens;

			if (tipoMensagem && listaMensagens) {
				exibirMensagem(tipoMensagem, listaMensagens);
			} else {
				_this.notaPromissoria = data.cotaGarantia.notaPromissoria;
				_this.dataBind();
			}

		});
	};

	this.toggle = function() {
		$('#cotaGarantiaNotaPromissoriaPanel').toggle();
	};

	this.dataBind = function() {
		$("#cotaGarantiaNotaPromissoriaId").html(this.notaPromissoria.id);
		$("#cotaGarantiaNotaPromissoriaVencimento").val(
				this.notaPromissoria.vencimento.$);
		$("#cotaGarantiaNotaPromissoriaValor").val(this.notaPromissoria.valor);
		$("#cotaGarantiaNotaPromissoriavalorExtenso").val(
				this.notaPromissoria.valorExtenso);
	};

	this.dataUnBind = function() {
		this.notaPromissoria.vencimento = $(
				"#cotaGarantiaNotaPromissoriaVencimento").val();
		this.notaPromissoria.valor = $("#cotaGarantiaNotaPromissoriaValor")
				.val();
		this.notaPromissoria.valorExtenso = $(
				"#cotaGarantiaNotaPromissoriavalorExtenso").val();
	};

	this.bindEvents = function() {
		var _this = this;
		$("#cotaGarantiaNotaPromissoriaVencimento").mask("99/99/9999");
		$("#cotaGarantiaNotaPromissoriaImprimir").bind('click', function() {
			_this.salva(function() {
				_this.imprimi();
			});
		});
	};

	this.imprimi = function() {
		window.open(this.path + 'impriNotaPromissoria/'
				+ this.notaPromissoria.id);
	};

	this.bindEvents();
	this.get();
	this.toggle();
}

function TipoCotaGarantia() {
	
	this.tipo = {'FIADOR':'Fiador', 'CHEQUE_CAUCAO':'Cheque Cau &ccedil;&atilde;o', 'IMOVEL':'Im&oacute;vel', 'NOTA_PROMISSORIA':'Nota Promiss&oacute;ria', 'CAUCAO_LIQUIDA':'Cau &ccedil;&atilde;o Liquida'};
	
	
	this.get = function() {
		$.getJSON(this.path + 'getTiposGarantia.json', function(data) {

			console.log(data);
			var tipoMensagem = data.tipoMensagem;
			var listaMensagens = data.listaMensagens;

			if (tipoMensagem && listaMensagens) {
				exibirMensagem(tipoMensagem, listaMensagens);
			} else {
				
			}

		});
	};
	
	this.bindData =  function(data){
		var select = document.getElementById("tipoGarantiaSelect");
		$.each(data, function(index, tipo) {
			var option = document.createElement("option");
			
			option.text = this.tipo[tipo];
			option.value = tipo;
			try {
				// for IE earlier than version 8
				select.add(option, select.options[null]);
			} catch (e) {
				select.add(option, null);
			}

		});

	};
}
