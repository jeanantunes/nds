/**
 * @param idCota
 * @returns {NotaPromissoria}
 */
function NotaPromissoria(idCota, cotaGarantia) {
	this.idCota = idCota;
	this.bindEvents();
	if (cotaGarantia && cotaGarantia.notaPromissoria) {
		this.notaPromissoria = cotaGarantia.notaPromissoria;
		
	}else{
		this.notaPromissoria = {
			id : null,
			vencimento : null,
			valor : null,
			valorExtenso : null
		};
	}
	this.dataBind();
	this.toggle();
};
NotaPromissoria.prototype.path = contextPath + "/cadastro/garantia/";
NotaPromissoria.prototype.salva = function(callBack) {
	this.dataUnBind();
	var postData = serializeObjectToPost('notaPromissoria', this.notaPromissoria);
	postData['idCota'] = this.idCota;

	$.postJSON(this.path + 'salvaNotaPromissoria.json', postData,
			function(data) {
				var tipoMensagem = data.tipoMensagem;
				var listaMensagens = data.listaMensagens;

				if (tipoMensagem && listaMensagens) {
					exibirMensagemDialog(tipoMensagem, listaMensagens,"");
				}
				if(callBack){
					callBack();
				}
				

			}, null, true);
	return false;
};

NotaPromissoria.prototype.get = function() {
	var _this = this;
	$.postJSON(this.path + 'getByCota.json', {
		'idCota' : this.idCota
	}, function(data) {

		var tipoMensagem = data.tipoMensagem;
		var listaMensagens = data.listaMensagens;
		if (tipoMensagem && listaMensagens) {
			exibirMensagemDialog(tipoMensagem, listaMensagens,"");
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
		if(_this.notaPromissoria.id){
			_this.imprimi();
		}else{
			exibirMensagemDialog('WARNING', ['Salve a Nota antes de imprimir.'], "");
		}
	});
};

NotaPromissoria.prototype.destroy = function() {
	$("#cotaGarantiaNotaPromissoriaImprimir").unbind('click');
};


NotaPromissoria.prototype.imprimi = function() {
	window.open(this.path + 'impriNotaPromissoria/' + this.idCota);
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
	if(cotaGarantia && cotaGarantia.cheque){
		this.chequeCaucao = cotaGarantia.cheque;
	}else{
		this.chequeCaucao ={
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
	}
	this.dataBind();
	this.toggle();
	this.initUploadForm();
};
ChequeCaucao.prototype.path = contextPath + "/cadastro/garantia/";


ChequeCaucao.prototype.get = function() {

	var _this = this;
	$.postJSON(this.path + 'getByCota.json', {
		'idCota' : this.idCota
	}, function(data) {

		var tipoMensagem = data.tipoMensagem;
		var listaMensagens = data.listaMensagens;
		if (tipoMensagem && listaMensagens) {
			exibirMensagemDialog(tipoMensagem, listaMensagens,"");
		} else if (data && data.cotaGarantia) {
			_this.chequeCaucao = data.cotaGarantia.cheque;
			_this.dataBind();
		}

	}, null, true);
};

ChequeCaucao.prototype.salva = function(callBack) {
	this.dataUnBind();
	var _this = this;
	var postData = serializeObjectToPost('chequeCaucao', this.chequeCaucao);
	postData['idCota'] = this.idCota;

	$.postJSON(this.path + 'salvaChequeCaucao.json', postData, function(data) {
		var tipoMensagem = data.tipoMensagem;
		var listaMensagens = data.listaMensagens;

		if (tipoMensagem && listaMensagens) {
			exibirMensagemDialog(tipoMensagem, listaMensagens,"");
		}
		if (callBack) {
			callBack();
		}
		_this.get();

	}, null, true);
	return false;
};

ChequeCaucao.prototype.toggle = function() {
	$('#cotaGarantiaChequeCaucaoPanel').toggle();
};


ChequeCaucao.prototype.initUploadForm = function() {
	   	 var _this = this;
	    $('#cotaGarantiaChequeCaucaoFormUpload').bind('submit', function(e) {
            e.preventDefault(); // <-- important
            $(this).ajaxSubmit({   
            	beforeSubmit: function(arr, formData, options) {
            	  if(_this.chequeCaucao.id){
        		        if (!$('#cotaGarantiaChequeCaucaoUpload').val()) { 
        		        	exibirMensagemDialog('WARNING', ['Escolha um arquivo para ser enviado.'], "");
        		            return false; 
        		        } 
            		  
            		  return true;
            	  }else{
            		  exibirMensagemDialog('WARNING', ['Cheque deve estar salvo antes do envio.'], "");
            		  return false;
            	  }  
            	},
    	        success:function(responseText, statusText, xhr, $form)  { 
    	        	var mensagens = (responseText.mensagens)?responseText.mensagens:responseText.result;   
    	        	var tipoMensagem = mensagens.tipoMensagem;
    	    		var listaMensagens = mensagens.listaMensagens;

    	    		if (tipoMensagem && listaMensagens) {
    	    			exibirMensagemDialog(tipoMensagem, listaMensagens,"");
    	    		}    	    	    
    	    	    _this.loadImage();
    	    	} ,
    	        url:  _this.path + 'uploadCheque',         
    	        type: 'POST',
    	        dataType:  'json',
    	        data: {idCheque:_this.chequeCaucao.id}
    	    });
        });
	   
	
	
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
	
	if(this.chequeCaucao.id){
		this.loadImage();
	}

};

ChequeCaucao.prototype.loadImage = function(){
	 $("#cotaGarantiaChequeCaucaoImagem").empty();
	 $("#cotaGarantiaChequeCaucaoImagemPanel").toggle(false);
	var imgPath = this.path + 'getImageCheque?idCheque='+this.chequeCaucao.id;
	var img = null;
	img = $("<img />").attr('src', imgPath)
    .load(function() {
       if (!(!this.complete || typeof this.naturalWidth == "undefined" || this.naturalWidth == 0)) {
    	   $("#cotaGarantiaChequeCaucaoImagemPanel").toggle(true);
           $("#cotaGarantiaChequeCaucaoImagem").append(img);
       }
    });
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
	
	if(!cotaGarantia){
		cotaGarantia = new Object();
	}
	this.cotaGarantia = cotaGarantia;
	this.bindEvents();
	this.toggle();
	this.initGrid();
	this.itemEdicao = null;
	this.popularGrid();
	this.imovel = {
			id : null,
			proprietario : null,
			endereco : null,
			numeroRegistro : null,
			valor : null,
			observacao : null
		};
};

Imovel.prototype.path = contextPath + "/cadastro/garantia/";

Imovel.prototype.toggle = function() {
	$('#cotaGarantiaImovelPanel').toggle();
};

Imovel.prototype.incluirImovel = function(callBack) {
	
	this.dataUnBind();
	var postData = serializeObjectToPost('imovel', this.imovel);
	var _this = this;
	
	$.postJSON(this.path + 'incluirImovel.json', postData, function(data) {
		
		var tipoMensagem = data.tipoMensagem;
		var listaMensagens = data.listaMensagens;

		if (tipoMensagem && listaMensagens) {
			exibirMensagemDialog(tipoMensagem, listaMensagens,"");
		
		} else {

			var novoImovel = data.imovel;
				
			if (_this.itemEdicao == null || _this.itemEdicao < 0) {
				_this.cotaGarantia.imoveis.push(novoImovel);
				
			} else {
				_this.cotaGarantia.imoveis.slice(_this.itemEdicao, 1);
				_this.cotaGarantia.imoveis[_this.itemEdicao] = novoImovel;
			}
			
			_this.limparForm();
			_this.popularGrid();
		}

		if (callBack) {
			callBack();
		}

	}, null, true);
};

Imovel.prototype.salva = function(callBack) {

	var postData = serializeArrayToPost('listaImoveis', this.cotaGarantia.imoveis);
	postData['idCota'] = this.idCota;

	$.postJSON(this.path + 'salvaImovel.json', postData, function(data) {
		var tipoMensagem = data.tipoMensagem;
		var listaMensagens = data.listaMensagens;

		if (tipoMensagem && listaMensagens) {
			exibirMensagemDialog(tipoMensagem, listaMensagens,"");
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

Imovel.prototype.popularGrid = function() {	
	if (!(this.cotaGarantia && this.cotaGarantia.imoveis)) {
		this.cotaGarantia.imoveis =  new Array();
	} 
	
	this.grid.flexAddData({
		rows : toFlexiGridObject(this.cotaGarantia.imoveis),
		page : 1,
		total : 1
	});
};

Imovel.prototype.dataUnBind = function() {
	this.imovel = new Object();
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

Imovel.prototype.destroy = function() {
	$("#cotaGarantiaImovelIncluirNovo").unbind('click');
	$("#cotaGarantiaImovelSalvaEdicao").unbind('click');
};

Imovel.prototype.edita = function(id) {
	this.imovel = this.cotaGarantia.imoveis[id];
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

				_this.cotaGarantia.imoveis.splice(id, 1);

				_this.popularGrid();
				
				$(this).dialog("close");
			},
			"Cancelar" : function() {
				$(this).dialog("close");
			}
		}
	});

};

Imovel.prototype.initGrid = function() {

	$(".cotaGarantiaImovelGrid").empty();	
	this.grid = $("<div></div>");
	$(".cotaGarantiaImovelGrid").append(this.grid);
	this.grid.flexigrid(
					{

						preProcess : function(data) {
							if (typeof data.mensagens == "object") {

								exibirMensagemDialog(data.mensagens.tipoMensagem,
										data.mensagens.listaMensagens,"");

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
								$(".cotaGarantiaImovelGrid").flexReload();
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
		controller : CaucaoLiquida
	},
	'ANTECEDENCIA_VALIDADE' : {
		label : 'Caução Liquida',
		controller : CaucaoLiquida
	},
	'OUTROS' : {
		label : 'Caução Liquida',
		controller : CaucaoLiquida
	}
};
TipoCotaGarantia.prototype.get = function() {
	var _this = this;
	$.getJSON(this.path + 'getTiposGarantia.json', null, function(data) {
		var tipoMensagem = data.tipoMensagem;
		var listaMensagens = data.listaMensagens;

		if (tipoMensagem && listaMensagens) {
			exibirMensagemDialog(tipoMensagem, listaMensagens,"");
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
			exibirMensagemDialog(tipoMensagem, listaMensagens,"");
		} else if (data && data.cotaGarantia) {
			_this.cotaGarantia = data.cotaGarantia;
			_this.cotaGarantia.controllerType = data.tipo;
			_this.changeController(data.tipo);
			$("#tipoGarantiaSelect").val(data.tipo);
		} else if (!data || !data.cotaGarantia) {
			_this.changeController(null);
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
		this.confirmDialog.open();
	} else {
		this.newControllerType = null;
		this.changeController(newControllerType);
	}
};

TipoCotaGarantia.prototype.changeController = function(newControllerType) {
	if (this.controller) {
		this.controller.toggle();
		if(this.controller.destroy){
			this.controller.destroy();
		}		
		delete this.controller;
	}
	if(newControllerType){		
	
		var obj = this.tipo[newControllerType].controller;
		this.controller = new obj(this.getIdCota(), this.cotaGarantia);
		this.controllerType = newControllerType;
	}else{
		$("#tipoGarantiaSelect").val("");
		this.controller = null;
		this.controllerType = null;
		if (this.cotaGarantia) {
			this.cotaGarantia = null;
		}
	}

};

TipoCotaGarantia.prototype.getIdCota = function() {
	return MANTER_COTA.idCota;
};

TipoCotaGarantia.prototype.salva = function(callBack) {
	var retorno = false;
	var _this = this;
	if(this.controller){
		retorno = this.controller.salva(function(){
			_this.getData();
			if(callBack){
				callBack();
			}
		});
	}
	
	return retorno;
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
			 $("#cotaGarantiaFiadorSearchDoc").val("");
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
			_this.getFiador(null, removeSpecialCharacteres($("#cotaGarantiaFiadorSearchDoc").val()));
		}
	});
	
	$("#cotaGarantiaFiadorSearchDoc").blur(function() {
		
		if ( $("#cotaGarantiaFiadorSearchDoc").val() != "" ) { 
			$("#cotaGarantiaFiadorSearchName").val("");
			_this.getFiador(null, removeSpecialCharacteres($("#cotaGarantiaFiadorSearchDoc").val()));
		}
	
	});

};
Fiador.prototype.getFiador = function(idFiador, documento) {
	var _this = this;
	var param = {};
	if (idFiador) {
		param.idFiador = idFiador;
	} else if (documento) {
		param.documento =  documento;
	}
	;

	$.postJSON(this.path + "getFiador.json", param, function(data) {
		if (data === "NotFound") {
			_this.confirma();
			_this.toggleDados(false);
			_this.fiador = null;
		} else if (data.tipoMensagem && data.listaMensagens) {
			exibirMensagemDialog(data.tipoMensagem, data.listaMensagens,"");
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
	if (this.fiador.pessoa.razaoSocial) {
		
		nome = this.fiador.pessoa.razaoSocial;
		doc = this.fiador.pessoa.cnpj;
	} else {
		nome = this.fiador.pessoa.nome;
		doc = this.fiador.pessoa.cpf;
	}
	
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

Fiador.prototype.destroy = function() {
	this.toggleDados(false);
};

Fiador.prototype.salva = function(callBack) {
	var _this = this;
	$.postJSON(this.path + "salvaFiador.json", {
		idFiador : this.fiador.id,
		idCota : this._idCota
	}, function(data) {
		if (data.tipoMensagem && data.listaMensagens) {
			exibirMensagemDialog(data.tipoMensagem, data.listaMensagens,"");
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
			exibirMensagemDialog(tipoMensagem, listaMensagens,"");
		} else if (data && data.fiador) {
			_this.getFiador(data.fiador.id, null);
		}

	}, null, true);

};

//**************** CAUCAO LIQUIDA PROTOTYPE ********************//
function CaucaoLiquida(idCota, cotaGarantia) {
	
	this.idCota = idCota;
	this.bindEvents();
	this.toggle();
	this.initGrid();
	this.cotaGarantia = cotaGarantia;
	this.listNovosCalcao = new Array();
	this.rows = new Array();
	this.popularGrid();
	this.caucaoLiquida = {
			id:null,
			valor:null,
			atualizacao:null,
			indiceReajuste:null
		};
}
CaucaoLiquida.prototype.path = contextPath + "/cadastro/garantia/";

CaucaoLiquida.prototype.toggle = function(showOrHide) {
	$('#cotaGarantiaCaucaoLiquida').toggle(showOrHide);
};

CaucaoLiquida.prototype.formatDate = function(data) {
	var dia = data.getDate();
	var mes = data.getMonth()+1;
	var ano = data.getFullYear();
	
	return dia+"/"+mes+"/"+ano;
};

CaucaoLiquida.prototype.dataUnBind = function() {	
	this.caucaoLiquida = new Object();
	this.caucaoLiquida.id = null;
	this.caucaoLiquida.valor = $("#cotaGarantiaCaucaoLiquidaValor").unmask() / 100;
	$("#cotaGarantiaCaucaoLiquidaValor").val(null);
	this.caucaoLiquida.indiceReajuste = 0.0;
	this.caucaoLiquida.atualizacao = this.formatDate(new Date());
};

CaucaoLiquida.prototype.incluirCaucao = function(callBack) {
	
	this.dataUnBind();
	if(!this.caucaoLiquida.valor || this.caucaoLiquida.valor <= 0){
		exibirMensagemDialog('ERROR', ['O preenchimento do campo [Valor R$] é obrigatório'], '');		
	}else{		
		this.listNovosCalcao.unshift(this.caucaoLiquida);		
		this.popularGrid();
	}
};

CaucaoLiquida.prototype.popularGrid = function() {
	
	var salvas;
	if (this.cotaGarantia && this.cotaGarantia.caucaoLiquidas) {
		salvas = this.cotaGarantia.caucaoLiquidas;
	}else{
		salvas = new Array();
	}	
	
	this.rows  = this.listNovosCalcao.concat(salvas);
	
	this.grid.flexAddData({
		rows : toFlexiGridObject(this.rows),
		page : 1,
		total : 1
	});
	
	
};

CaucaoLiquida.prototype.salva = function(callBack) {
	
	var postData = serializeArrayToPost('listaCaucaoLiquida', this.listNovosCalcao);
		
	postData['idCota'] = this.idCota;
	
	$.postJSON(this.path + 'salvaCaucaoLiquida.json', postData,
			function(data) {
				var tipoMensagem = data.tipoMensagem;
				var listaMensagens = data.listaMensagens;

				if (tipoMensagem && listaMensagens) {
					exibirMensagemDialog(tipoMensagem, listaMensagens,"");
				}
				if(callBack){
					callBack();
				}

			}, null, true);
};

CaucaoLiquida.prototype.resgatarValorCaucao = function() {
	
	var _this = this;
	
	$("#dialog-confirma-resgate").dialog({
		resizable : false,
		height : 'auto',
		width : 'auto',
		modal : true,
		buttons : {
			"Confirmar" : function() {

				var caucaoLiquida = {
						id:null,
						atualizacao : _this.formatDate(new Date()),
						indiceReajuste: 0,
						valor:0
				};

				_this.listNovosCalcao.unshift(caucaoLiquida);		
				_this.popularGrid();
				
				$(this).dialog("close");
			},
			"Cancelar" : function() {
				$(this).dialog("close");
			}
		}
	});
	
};

CaucaoLiquida.prototype.bindEvents = function() {
	
	var _this = this;
	
	$("#cotaGarantiaCaucaoLiquidaIncluir").click(function(){
		
		_this.incluirCaucao();
	});
	
	$("#cotaGarantiaCaucaoLiquidaResgatar").click(function(){
		if (_this.rows[0].valor > 0) {
			_this.resgatarValorCaucao();
		}
	});
	
	$("#cotaGarantiaCaucaoLiquidaValor").priceFormat({
		allowNegative : true,
		centsSeparator : ',',
		thousandsSeparator : '.'
	});
};

CaucaoLiquida.prototype.destroy = function() {
	$("#cotaGarantiaCaucaoLiquidaIncluir").unbind('click');
	$("#cotaGarantiaCaucaoLiquidaResgatar").unbind('click');
};

CaucaoLiquida.prototype.initGrid = function() {
	$("#cotaGarantiaCaucaoLiquidaGrid").empty();	
	this.grid = $("<div></div>");
	$("#cotaGarantiaCaucaoLiquidaGrid").append(this.grid);
	this.grid.flexigrid({
		
		dataType : 'json',
		colModel : [ {
			display : 'Data',
			name : 'atualizacao',
			width : 270,
			sortable : false,
			align : 'center'

		},{
			display : 'Índice de Reajuste',
			name : 'indiceReajuste',
			width : 270,
			sortable : false,
			align : 'center'

		},{

			display : 'Valor R$',
			name : 'valor',
			width : 140,
			sortable : false,
			align : 'right'

		}],
		width : 740,
		height : 150
	});
	
};


