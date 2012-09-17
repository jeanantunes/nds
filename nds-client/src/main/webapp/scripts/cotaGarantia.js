var _workspace = "";


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
	if (tipoCotaGarantia.isModoTelaCadastroCota()) {
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
    } else {
        this.chequeCaucao = cotaGarantia;
    }
	this.dataBind();
	this.toggle();
	this.initUploadForm();
};
ChequeCaucao.prototype.path = contextPath + "/cadastro/garantia/";


ChequeCaucao.prototype.get = function() {

	var _this = this;
    var param = [{name: 'idCota', value: this.idCota}, {name: 'modoTela', value: tipoCotaGarantia.getModoTela().value}];
	$.postJSON(this.path + 'getByCota.json', param, function(data) {

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
	
	if (tipoCotaGarantia.isModoTelaCadastroCota()) {
        if(this.chequeCaucao.id){
            this.loadImage();
        }
    } else {
        this.loadImage();
    }

};

ChequeCaucao.prototype.loadImage = function(){
	 $("#cotaGarantiaChequeCaucaoImagem").empty();
	 $("#cotaGarantiaChequeCaucaoImagemPanel").toggle(false);
    var imgPath = this.path + 'getImageCheque?modoTela=' + tipoCotaGarantia.getModoTela().value;
    if (tipoCotaGarantia.isModoTelaCadastroCota()) {
        imgPath+= '&idCheque=' + this.chequeCaucao.id;
    } else {
        imgPath+= '&idCota=' + tipoCotaGarantia.getIdCota() + '&idHistorico=' + tipoCotaGarantia.getIdHistorico();
    }
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

	$("#dialog-excluir-imovel", _workspace).dialog({
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
		},
		form: $("#workspaceCota", _workspace)
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
															+ '/images/ico_editar.gif" border="0" style="margin-right:10px;" /></a>';
													acao += '<a href="javascript:;" onclick="tipoCotaGarantia.controller.remove('
															+ idImovel
															+ ');" ><img src="'
															+ contextPath
															+ '/images/ico_excluir.gif" border="0" /></a>';

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
function TipoCotaGarantia(workspace) {
	_workspace = workspace;
	this.get();
	this.controller = null;
	this.newControllerType = null;
	this.controllerType = null;
	var _this = this;
	this.confirmDialog = new ConfirmDialog('Ao mudar de garantia, os dados da garantia anterior serão perdidos.</br>Deseja continuar?', function() {
		 _this.changeController(_this.newControllerType);
		 return true;
	}, function() {
		 $("#tipoGarantiaSelect", _workspace).val(_this.controllerType);
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
		label : 'Antecedência Validade',
		controller : CaucaoLiquida
	},
	'OUTROS' : {
		label : 'Outros',
		controller : Outros
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
    var param = [{name: 'idCota', value: this.getIdCota()},
        {name: 'modoTela', value: this.getModoTela().value},
        {name: 'idHistorico', value: this.getIdHistorico()}]
	$.postJSON(this.path + 'getByCota.json', param, function(data) {
		var tipoMensagem = data.tipoMensagem;
		var listaMensagens = data.listaMensagens;
		if (tipoMensagem && listaMensagens) {
			exibirMensagemDialog(tipoMensagem, listaMensagens,"");
		} else if (data && data.cotaGarantia) {
			_this.cotaGarantia = data.cotaGarantia;
			_this.cotaGarantia.controllerType = data.tipo;
			_this.changeController(data.tipo);
			$("#tipoGarantiaSelect", _workspace).val(data.tipo);
			
			_this.configTipoCotaGarantia(data.tipo,_this.getIdCota());
			
		} else if (!data || !data.cotaGarantia) {
			_this.changeController(null);
		}

	}, null, true);
};

TipoCotaGarantia.prototype.configTipoCotaGarantia = function(tipo,idCota) {
	
	if (tipo=="CAUCAO_LIQUIDA"){
		CaucaoLiquida.prototype.obterCaucaoLiquida(idCota);
	}
};

TipoCotaGarantia.prototype.bindData = function(data) {

	var select = $("#tipoGarantiaSelect", _workspace);

	for ( var index in data) {
		var tipo = data[index];
		var option = document.createElement("option");
			
		option.text = this.tipo[tipo].label;
		
		option.value = tipo;
		
		try {
			$(select).append(option, select.options[null]);
		} catch (e) {
			$(select).append(option, null);
		}
	}
	
};

TipoCotaGarantia.prototype.bindTiposCobrancaCotaGarantia = function() {

	$.getJSON(this.path + 'getTiposCobrancaCotaGarantia.json', 
			null, 
			function(data) {
		
				var tipoMensagem = data.tipoMensagem;
				var listaMensagens = data.listaMensagens;
		
				if (tipoMensagem && listaMensagens) {
					exibirMensagemDialog(tipoMensagem, listaMensagens,"");
				} else {
					var select = $("#tipoCobranca", _workspace);
					
					$(select).find('option')
					         .remove()
					         .end()
					         .append('<option value="">Selecione</option>')
					         .val('Selecione');

					for ( var index in data) {
						var tipo = data[index];
						var option = document.createElement("option");
							
						option.text = tipo.value.$;
						
						option.value = tipo.key.$;

						try {
							$(select).append(option, select.options[null]);
						} catch (e) {
							$(select).append(option, null);
						}
					}
				}
			}, 
			null, 
			true);

};

TipoCotaGarantia.prototype.onOpen = function(tipoCotaSelecionada) {
	
	this.getData();
	
	this.bindTiposCobrancaCotaGarantia();
	
	if (tipoCotaSelecionada == "JURIDICA"){
		$("#cotaGarantiaClassificacaoCota", this.workspace).val($("#classificacaoSelecionada :selected", this.workspace).text());
	}
	else{
		$("#cotaGarantiaClassificacaoCota", this.workspace).val($("#classificacaoSelecionadaCPF :selected", this.workspace).text());
	}	
};

TipoCotaGarantia.prototype.bindEvents = function() {
	var _this = this;
	$("#tipoGarantiaSelect", _workspace).change(function(eventObject) {
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
		//$("#tipoGarantiaSelect", _workspace)[0].selectedIndex;
		//$("#tipoGarantiaSelect", _workspace).val("");
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

TipoCotaGarantia.prototype.getModoTela = function() {
    return MANTER_COTA.modoTela;
};

TipoCotaGarantia.prototype.getIdHistorico = function() {
    return MANTER_COTA.idHistorico;
};

TipoCotaGarantia.prototype.isModoTelaCadastroCota = function() {
    return MANTER_COTA.isModoTelaCadastroCota();
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

    if (tipoCotaGarantia.isModoTelaCadastroCota()) {
        if (cotaGarantia && cotaGarantia.fiador) {
            this.fiador = cotaGarantia.fiador;
            this.bindData();
            this.toggleDados(true);
        }
    } else {
        this.fiador = cotaGarantia;
        this.bindData();
        this.toggleDados(true);
    }


};

Fiador.prototype.path = contextPath + "/cadastro/garantia/";
Fiador.prototype.toggle = function() {
	$('#cotaGarantiaFiadorPanel', _workspace).toggle();
};

Fiador.prototype.bindEvents = function() {
	var _this = this;
	$("#cotaGarantiaFiadorSearchName", _workspace).autocomplete({
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
			 $("#cotaGarantiaFiadorSearchDoc", _workspace).val("");
			_this.getFiador(ui.item.key, null);
		},
		open : function() {
			$(this).removeClass("ui-corner-all").addClass("ui-corner-top");
		},
		close : function() {
			$(this).removeClass("ui-corner-top").addClass("ui-corner-all");
		}
	});

	
	$("#cotaGarantiaFiadorSearchDoc", _workspace).keypress(function(e) {
		if ((e.which && e.which == 13) || (e.keyCode && e.keyCode == 13)) {
			$("#cotaGarantiaFiadorSearchName", _workspace).val("");
			_this.getFiador(null, removeSpecialCharacteres($("#cotaGarantiaFiadorSearchDoc", _workspace).val()));
		}
	});
	
	$("#cotaGarantiaFiadorSearchDoc", _workspace).blur(function() {
		
		if ( $("#cotaGarantiaFiadorSearchDoc", _workspace).val() != "" ) {
			$("#cotaGarantiaFiadorSearchName", _workspace).val("");
			_this.getFiador(null, removeSpecialCharacteres($("#cotaGarantiaFiadorSearchDoc", _workspace).val()));
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
			_this.enderecoFiador = null;
		} else {
			_this.fiador = data.fiador;
			_this.enderecoFiador =data.endereco;
			_this.bindData();
			_this.toggleDados(true);
		}
	}, null, true);
};

Fiador.prototype.confirma = function() {
	this.$dialog.dialog('open');
};

Fiador.prototype.toggleDados = function(showOrHide) {
	$('#cotaGarantiaFiadorDadosPanel', _workspace).toggle(showOrHide);
};

Fiador.prototype.bindData = function() {

	var nome;
	var doc;
	if (this.fiador.pessoa) {
        if (this.fiador.pessoa.razaoSocial) {

            nome = this.fiador.pessoa.razaoSocial;
            doc = this.fiador.pessoa.cnpj;
        } else {
            nome = this.fiador.pessoa.nome;
            doc = this.fiador.pessoa.cpf;
        }
    } else {
        nome = this.fiador.nome;
        doc = this.fiador.documento;
    }

	$("#cotaGarantiaFiadorNome", _workspace).html(nome);
	$("#cotaGarantiaFiadorDoc", _workspace).html(doc);

    var strEndereco = '';
	var endereco = '';

    if (this.fiador.enderecoPrincipal) {
        endereco = this.fiador.enderecoPrincipal;
    } else {
        for (var i in this.fiador.enderecoFiador) {
            if (this.fiador.enderecoFiador[i].principal) {
                endereco = this.fiador.enderecoFiador[i].endereco;
            }
        }
    }

	if (endereco) {
        strEndereco = endereco.tipoLogradouro + ' ' + endereco.logradouro
            + ', ' + endereco.numero + ' - ' + endereco.bairro + ' - '
            + endereco.cidade + '/' + endereco.uf;
    }

	$("#cotaGarantiaFiadorEndereco", _workspace).html(strEndereco);
	var telefone = '';

    if (this.fiador.telefonePrincipal) {
        telefone = this.fiador.telefonePrincipal;
    } else {
       for ( var i in this.fiador.telefonesFiador) {
            if (this.fiador.telefonesFiador[i].principal) {
                telefone = this.fiador.telefonesFiador[i].telefone;
            }
        }
    }


	$("#cotaGarantiaFiadorTelefone", _workspace).html(
			'(' + telefone.ddd + ') ' + telefone.numero);

	var rows = new Array();
	for ( var id in this.fiador.garantias) {
		rows[id] = {
			"id" : id,
			"cell" : this.fiador.garantias[id]
		};
	}

	$("#cotaGarantiaFiadorGarantiasGrid", _workspace).flexAddData({
		rows : rows,
		page : 1,
		total : 1
	});
};

Fiador.prototype.initGrid = function() {
	$("#cotaGarantiaFiadorGarantiasGrid", _workspace).flexigrid({
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
			esp : ""
		};
}
CaucaoLiquida.prototype.path = contextPath + "/cadastro/garantia/";

CaucaoLiquida.prototype.toggle = function(showOrHide) {
	$('#cotaGarantiaCaucaoLiquida', _workspace).toggle(showOrHide);
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
	
	var vr = this.getValorCaucaoLiquida($("#tipoCobranca", _workspace).val());
	vr = removeMascaraPriceFormat(vr);
	this.caucaoLiquida.valor =  vr / 100;
	this.caucaoLiquida.esp = "";
	
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

CaucaoLiquida.prototype.replaceAll = function(string, token, newtoken) {
	while (string.indexOf(token) != -1) {
 		string = string.replace(token, newtoken);
	}
	return string;
};

CaucaoLiquida.prototype.preparaValor = function(vr){
	
	if(vr.substr(vr.length-3,1)==","){
		vr = this.replaceAll(vr,".","");
		vr = this.replaceAll(vr,",",".");
	}
	if(vr.substr(vr.length-3,1)=="."){
		vr = this.replaceAll(vr,",","");
	}
	return vr;
};

CaucaoLiquida.prototype.calculaValorParcela = function(){
	
	var vrBoleto = removeMascaraPriceFormat($("#valorBoleto",_workspace).val());
	var qtdParcelas = removeMascaraPriceFormat($("#qtdParcelaBoleto",_workspace).val());
	
	var valorParcela = Math.round(vrBoleto / qtdParcelas);
	$("#valorParcelaBoleto").val(valorParcela);
	$("#valorParcelaBoleto", _workspace).priceFormat({
		allowNegative : true,
		centsSeparator : ',',
		thousandsSeparator : '.'
	});
};

CaucaoLiquida.prototype.getValorCaucaoLiquida = function(tipo){
	var valor = "";

	if (tipo=='BOLETO'){
		valor = $("#valorBoleto", _workspace).val();
    }
	else if (tipo=='DEPOSITO_TRANSFERENCIA'){
		valor = $("#valorDeposito", _workspace).val();
	}    
    else if (tipo=='DINHEIRO'){
		valor = $("#valorDinheiro", _workspace).val();
	}   
	else if (tipo=='DESCONTO_COTA'){
		valor = $("#valorDesconto", _workspace).val();
	}    

	return this.preparaValor(valor);
};

CaucaoLiquida.prototype.adicionaDecimais = function(id){
	if ($("#"+id, _workspace).val().indexOf(".") < 0){
		$("#"+id, _workspace).val($("#"+id, _workspace).val() + "00");
	}
	
	$("#"+id, _workspace).priceFormat({
		centsSeparator : ',',
		thousandsSeparator : '.'
	});
}

CaucaoLiquida.prototype.setValorCaucaoLiquida = function(tipo,valor){

	if (tipo=='BOLETO'){
		 $("#valorBoleto", _workspace).val(valor);
		 this.adicionaDecimais("valorBoleto");
    }
	else if (tipo=='DEPOSITO_TRANSFERENCIA'){
		$("#valorDeposito", _workspace).val(valor);
		this.adicionaDecimais("valorDeposito");
	}    
    else if (tipo=='DINHEIRO'){
		$("#valorDinheiro", _workspace).val(valor);
		this.adicionaDecimais("valorDinheiro");
	}  
	else if (tipo=='DESCONTO_COTA'){
		$("#valorDesconto", _workspace).val(valor);
		this.adicionaDecimais("valorDesconto");
	}     
};

CaucaoLiquida.prototype.salva = function(callBack) {
	
	this.incluirCaucao();
	
	var postData = serializeArrayToPost('listaCaucaoLiquida', this.listNovosCalcao);
	postData['idCota'] = this.idCota;
	
	var tipoCobranca        = $("#tipoCobranca", _workspace).val();
	
	//FORMA BOLETO
	var tipoFormaCobranca   = $("#tipoFormaCobranca", _workspace).val();
	var diaDoMes            = $("#diaDoMes", _workspace).val();
	var primeiroDiaQuinzenal= $("#primeiroDiaQuinzenal", _workspace).val();
	var segundoDiaQuinzenal = $("#segundoDiaQuinzenal", _workspace).val();
	var qtdeParcelas        = $("#qtdParcelaBoleto", _workspace).val();
	var valorParcela        = $("#valorParcelaBoleto", _workspace).val();
	
	$("#PS", _workspace).val(0);
	if ($("#PS", this.workspace).is(":checked")) {
		$("#PS", _workspace).val(1);
	}
	var segunda = $("#PS", _workspace).val();
	
	$("#PT", _workspace).val(0);
	if ($("#PT", this.workspace).is(":checked")) {
		$("#PT", _workspace).val(1);
	}
	var terca = $("#PT", _workspace).val();
	
	$("#PQ", _workspace).val(0);
	if ($("#PQ", this.workspace).is(":checked")) {
		$("#PQ", _workspace).val(1);
	}
	var quarta = $("#PQ", _workspace).val();
	
	$("#PQu", _workspace).val(0);
	if ($("#PQu", this.workspace).is(":checked")) {
		$("#PQu", _workspace).val(1);
	}
	var quinta = $("#PQu", _workspace).val();
	
	$("#PSex", _workspace).val(0);
	if ($("#PSex", this.workspace).is(":checked")) {
		$("#PSex", _workspace).val(1);
	}
	var sexta  = $("#PSex", _workspace).val();
	
	$("#PSab", _workspace).val(0);
	if ($("#PSab", this.workspace).is(":checked")) {
		$("#PSab", _workspace).val(1);
	}
	var sabado = $("#PSab", _workspace).val();
	
	$("#PDom", _workspace).val(0);
	if ($("#PDom", this.workspace).is(":checked")) {
		$("#PDom", _workspace).val(1);
	}
	var domingo  = $("#PDom", _workspace).val();
	
	//FORMA DESCONTO
	var valorDescontoAtual  = $("#valorDescontoAtual", _workspace).val();
	var utilizarDesconto    = $("#utilizarDesconto", _workspace).val();
	var descontoCotaDesconto= $("#descontoCotaDesconto", _workspace).val();
	
	//INFORMACOES ADICIONAIS DE DEPOSITO
	var numBanco            = $("#numBancoDeposito", _workspace).val();
	var nomeBanco           = $("#nomeBancoDeposito", _workspace).val();
	var agencia             = $("#agenciaDeposito", _workspace).val();
	var conta               = $("#contaDeposito", _workspace).val();
	var nomeCorrentista     = $("#nomeCorrentistaDeposito", _workspace).val();
	
	var valor               = this.getValorCaucaoLiquida(tipoCobranca);
	
	   
	postData['formaCobranca.tipoCobranca'] = tipoCobranca; 
	
	//FORMA BOLETO
	postData['formaCobranca.tipoFormaCobranca'] = tipoFormaCobranca;        
	postData['formaCobranca.domingo'] = domingo;    
	postData['formaCobranca.segunda'] = segunda;            
	postData['formaCobranca.terca'] = terca;            
	postData['formaCobranca.quarta'] = quarta;            
	postData['formaCobranca.quinta'] = quinta;            
	postData['formaCobranca.sexta'] = sexta;            
	postData['formaCobranca.sabado'] = sabado;
	postData['formaCobranca.diaDoMes'] = diaDoMes;
	postData['formaCobranca.primeiroDiaQuinzenal'] = primeiroDiaQuinzenal;
	postData['formaCobranca.segundoDiaQuinzenal'] = segundoDiaQuinzenal;
	postData['formaCobranca.qtdeParcelas'] = qtdeParcelas;
	postData['formaCobranca.valorParcela'] = this.preparaValor(valorParcela);
	
	//FORMA DESCONTO
	postData['formaCobranca.valorDescontoAtual'] = this.preparaValor(valorDescontoAtual);
	postData['formaCobranca.utilizarDesconto'] = this.preparaValor(utilizarDesconto);
	postData['formaCobranca.descontoCotaDesconto'] = this.preparaValor(descontoCotaDesconto);
	
	//INFORMACOES ADICIONAIS DE DEPOSITO
	postData['formaCobranca.numBanco'] = numBanco;        
	postData['formaCobranca.nomeBanco'] = nomeBanco;          
	postData['formaCobranca.agencia'] = agencia;               
	postData['formaCobranca.conta'] = conta;              
	postData['formaCobranca.nomeCorrentista'] = nomeCorrentista;
	
	postData['formaCobranca.valor'] = valor;
	
	$.postJSON(this.path + 'salvaCaucaoLiquida.json', postData,
			function(data) {
				var tipoMensagem = data.tipoMensagem;
				var listaMensagens = data.listaMensagens;

				if (tipoMensagem && listaMensagens) {
					
					exibirMensagemDialog(
						tipoMensagem, 
						listaMensagens,
						"dialog-cota"
					);
					
				}
				if(callBack){
					callBack();
				}

			}, 
			function(data){
				if (data.mensagens) {

					exibirMensagemDialog(
						data.mensagens.tipoMensagem, 
						data.mensagens.listaMensagens,
						"dialog-cota"
					);
				}	
			}, 
			true,
			"dialog-cota");
};

CaucaoLiquida.prototype.resgatarValorCaucao = function() {
	
	var _this = this;
	
	$("#dialog-confirma-resgate", _workspace).dialog({
		resizable : false,
		height : 'auto',
		width : 'auto',
		modal : true,
		buttons : {
			"Confirmar" : function() {

				var caucaoLiquida = {
						id:null,
						atualizacao : _this.formatDate(new Date()),
						esp : "",
						valor:0
				};

				_this.listNovosCalcao.unshift(caucaoLiquida);		
				_this.popularGrid();
				
				$(this).dialog("close");
			},
			"Cancelar" : function() {
				$(this).dialog("close");
			}
		},
		form: $("#workspaceCota", _workspace)
	});
	
};

CaucaoLiquida.prototype.bindEvents = function() {
	
	var _this = this;
	
	$("#cotaGarantiaCaucaoLiquidaResgatar", _workspace).click(function(){
		if (_this.rows[0].valor > 0) {
			_this.resgatarValorCaucao();
		}
	});

	$("#valorBoleto", _workspace).priceFormat({
		allowNegative : true,
		centsSeparator : ',',
		thousandsSeparator : '.'
	});
	
	$("#qtdParcelaBoleto", _workspace).numeric();
	
	$("#valorParcelaBoleto", _workspace).priceFormat({
		allowNegative : true,
		centsSeparator : ',',
		thousandsSeparator : '.'
	});
	
	$("#numBancoDeposito", _workspace).numeric();
	$("#agenciaDeposito", _workspace).numeric();
	$("#contaDeposito", _workspace).numeric();
	
	$("#valorDesconto", _workspace).priceFormat({
		allowNegative : true,
		centsSeparator : ',',
		thousandsSeparator : '.'
	});
	
	$("#valorDescontoAtual", _workspace).priceFormat({
		allowNegative : true,
		centsSeparator : ',',
		thousandsSeparator : '.'
	});
	
	$("#utilizarDesconto", _workspace).priceFormat({
		allowNegative : true,
		centsSeparator : ',',
		thousandsSeparator : '.'
	});
	
	$("#descontoCotaDesconto", _workspace).priceFormat({
		allowNegative : true,
		centsSeparator : ',',
		thousandsSeparator : '.'
	});
	
	$("#valorDeposito", _workspace).priceFormat({
		allowNegative : true,
		centsSeparator : ',',
		thousandsSeparator : '.'
	});
	
	$("#valorDinheiro", _workspace).priceFormat({
		allowNegative : true,
		centsSeparator : ',',
		thousandsSeparator : '.'
	});

};

CaucaoLiquida.prototype.destroy = function() {
	$("#cotaGarantiaCaucaoLiquidaResgatar", _workspace).unbind('click');
};

CaucaoLiquida.prototype.initGrid = function() {
	$("#cotaGarantiaCaucaoLiquidaGrid", _workspace).empty();	
	this.grid = $("<div></div>");
	$("#cotaGarantiaCaucaoLiquidaGrid", _workspace).append(this.grid);
	this.grid.flexigrid({
		preProcess: CaucaoLiquida.prototype.getDataFromResultGrid,
		dataType : 'json',
		colModel : [ {
			display : 'Data',
			name : 'atualizacao',
			width : 225,
			sortable : false,
			align : 'center'
		},{
			display : 'Valor R$',
			name : 'valor',
			width : 125,
			sortable : false,
			align : 'right'
		},{
			display : '',
			name : 'esp',
			width : 325,
			sortable : false,
			align : 'center'
		}],
		width : 740,
		height : 150
	});
};

CaucaoLiquida.prototype.getDataFromResultGrid = function(data){
	$.each(data.rows, function(index, row) {
		row.cell.esp = "";
	});
	return data;
};

CaucaoLiquida.prototype.opcaoPagto = function(op){
	
	if (op=='BOLETO'){
		$('#divFormaBoleto', _workspace).show();
		$('#divFormaDeposito', _workspace).hide();
		$('#divFormaDinheiro', _workspace).hide();
		$('#divFormaDesconto', _workspace).hide();
    }
	else if (op=='DEPOSITO_TRANSFERENCIA'){
		$('#divFormaBoleto', _workspace).hide();
		$('#divFormaDeposito', _workspace).show();
		$('#divFormaDinheiro', _workspace).hide();
		$('#divFormaDesconto', _workspace).hide();
	}    
	else if (op=='DINHEIRO'){
		$('#divFormaBoleto', _workspace).hide();
		$('#divFormaDeposito', _workspace).hide();
		$('#divFormaDinheiro', _workspace).show();
		$('#divFormaDesconto', _workspace).hide();
	}    
	else if (op=='DESCONTO_COTA'){
		$('#divFormaBoleto', _workspace).hide();
		$('#divFormaDeposito', _workspace).hide();
		$('#divFormaDinheiro', _workspace).hide();
		$('#divFormaDesconto', _workspace).show();
	}    
	else{
		$('#divFormaBoleto', _workspace).hide();
		$('#divFormaDeposito', _workspace).hide();
		$('#divFormaDinheiro', _workspace).hide();
		$('#divFormaDesconto', _workspace).hide();
	}
	
};

CaucaoLiquida.prototype.mostraDiario = function(){
	$("#tipoFormaCobranca", _workspace).val('DIARIA');
	$("#semanal", _workspace).attr("checked", false);
	$("#quinzenal", _workspace).attr("checked", false);
	$("#mensal", _workspace).attr("checked", false);
	$( ".semanal" , _workspace).hide();
	$( ".quinzenal" , _workspace).hide();
	$( ".mensal" , _workspace).hide();
	$( ".diario", _workspace).show();
};

CaucaoLiquida.prototype.mostraQuinzenal = function(){
	$("#tipoFormaCobranca", _workspace).val('QUINZENAL');
	$("#diario", _workspace).attr("checked", false);
	$("#semanal", _workspace).attr("checked", false);
	$("#mensal", _workspace).attr("checked", false);
	$( ".diario" , _workspace).hide();
	$( ".semanal" , _workspace).hide();
	$( ".mensal" , _workspace).hide();
	$( ".quinzenal", _workspace).show();
};

CaucaoLiquida.prototype.mostraSemanal = function(){
	$("#tipoFormaCobranca", _workspace).val('SEMANAL');
	$("#diario", _workspace).attr("checked", false);
	$("#quinzenal", _workspace).attr("checked", false);
	$("#mensal", _workspace).attr("checked", false);
	$( ".diario" , _workspace).hide();
	$( ".quinzenal" , _workspace).hide();
	$( ".mensal" , _workspace).hide();
	$( ".semanal" , _workspace).show();
};
	
CaucaoLiquida.prototype.mostraMensal = function(){
	$("#tipoFormaCobranca", _workspace).val('MENSAL');
	$("#diario", _workspace).attr("checked", false);
	$("#semanal", _workspace).attr("checked", false);
	$("#quinzenal", _workspace).attr("checked", false);
	$( ".diario" , _workspace).hide();
	$( ".semanal" , _workspace).hide();
	$( ".quinzenal" , _workspace).hide();
	$( ".mensal" , _workspace).show();
};

CaucaoLiquida.prototype.opcaoTipoFormaCobranca = function(op){
	if (op=='SEMANAL'){
		$("#semanal", _workspace).attr("checked", true);
		$("#mensal", _workspace).attr("checked", false);
		$("#diario", _workspace).attr("checked", false);
		$("#quinzenal", _workspace).attr("checked", false);
		this.mostraSemanal();
    }
	else if (op=='MENSAL'){
		$("#semanal", _workspace).attr("checked", false);
		$("#mensal", _workspace).attr("checked", true);
		$("#diario", _workspace).attr("checked", false);
		$("#quinzenal", _workspace).attr("checked", false);
		this.mostraMensal();
	}    
	else if (op=='DIARIA'){
		$("#semanal", _workspace).attr("checked", false);
		$("#mensal", _workspace).attr("checked", false);
		$("#diario", _workspace).attr("checked", true);
		$("#quinzenal", _workspace).attr("checked", false);
		this.mostraDiario();
	}    
	else if (op=='QUINZENAL'){
		$("#semanal", _workspace).attr("checked", false);
		$("#mensal", _workspace).attr("checked", false);
		$("#diario", _workspace).attr("checked", false);
		$("#quinzenal", _workspace).attr("checked", true);
		this.mostraQuinzenal();
	}    
};


//OBTEM UM PARÂMETRO PARA ALTERAÇÃO
CaucaoLiquida.prototype.obterCaucaoLiquida = function(idCota){
	var data = [{name: 'idCota', value: idCota}];
	$.postJSON(this.path + 'getCaucaoLiquidaByCota.json',
			   data,
			   this.sucessCallbackObterCaucaoLiquida, 
			   null,
			   true);
};


CaucaoLiquida.prototype.sucessCallbackObterCaucaoLiquida = function(resultado) {
	
	var _this = this;
	
	$("#tipoCobranca", _workspace).val(resultado.tipoCobranca);
	
	CaucaoLiquida.prototype.opcaoPagto(resultado.tipoCobranca);
	CaucaoLiquida.prototype.opcaoTipoFormaCobranca(resultado.tipoFormaCobranca);
	
	//FORMA BOLETO
	$("#diaDoMes", _workspace).val(resultado.diaDoMes);
	$("#primeiroDiaQuinzenal", _workspace).val(resultado.primeiroDiaQuinzenal);
	$("#segundoDiaQuinzenal", _workspace).val(resultado.segundoDiaQuinzenal);
	    
	$("#PS", _workspace).attr('checked',resultado.segunda);
	$("#PT", _workspace).attr('checked',resultado.terca);
	$("#PQ", _workspace).attr('checked',resultado.quarta);
	$("#PQu", _workspace).attr('checked',resultado.quinta);
	$("#PSex", _workspace).attr('checked',resultado.sexta);
	$("#PSab", _workspace).attr('checked',resultado.sabado);
	$("#PDom", _workspace).attr('checked',resultado.domingo);
	
	$("#qtdParcelaBoleto", _workspace).val(resultado.qtdeParcelas);
	$("#valorParcelaBoleto", _workspace).val(resultado.valorParcela);
	CaucaoLiquida.prototype.adicionaDecimais("valorParcelaBoleto");
	
	//FORMA DESCONTO
	$("#valorDescontoAtual", _workspace).val(resultado.valorDescontoAtual);
	$("#utilizarDesconto", _workspace).val(resultado.utilizarDesconto);
	$("#descontoCotaDesconto", _workspace).val(resultado.descontoCotaDesconto);

	//INFORMACOES ADICIONAIS DEPOSITO
	$("#numBancoDeposito", _workspace).val(resultado.numBanco);
	$("#nomeBancoDeposito", _workspace).val(resultado.nomeBanco);
	$("#agenciaDeposito", _workspace).val(resultado.agencia);
	$("#contaDeposito", _workspace).val(resultado.conta);
	$("#nomeCorrentistaDeposito", _workspace).val(resultado.nomeCorrentista);
	
	CaucaoLiquida.prototype.setValorCaucaoLiquida(resultado.tipoCobranca, resultado.valor);
	
	CaucaoLiquida.prototype.opcaoPagto(resultado.tipoCobranca);
};



//**************** OUTROS PROTOTYPE ********************//
function Outros(idCota, cotaGarantia) {
	
	this.idCota = idCota;

	if(!cotaGarantia){
		cotaGarantia = new Object();
	}
	this.cotaGarantia = cotaGarantia;
	this.bindEvents();
	this.toggle();
	this.initGrid();
	this.itemEdicao = null;
	
	this.rows = new Array();
	this.popularGrid();
	this.outro = {
			id:null,
			descricao:null,
			valor:null,
			validade:null
		};
}
Outros.prototype.path = contextPath + "/cadastro/garantia/";

Outros.prototype.toggle = function(showOrHide) {
	$('#cotaGarantiaOutros').toggle(showOrHide);
};

Outros.prototype.formatDate = function(data) {
	var dia = data.getDate();
	var mes = data.getMonth()+1;
	var ano = data.getFullYear();
	
	return dia+"/"+mes+"/"+ano;
};

Outros.prototype.dataUnBind = function() {	
	
	this.outro = new Object();
	this.outro.id = null;
	this.outro.descricao = $("#descricaoCotaGarantiaOutros").val();
	this.outro.valor = $("#valorCotaGarantiaOutros").unmask() / 100;
	this.outro.validade = $("#validadeCotaGarantiaOutros").val();
	
};

Outros.prototype.incluirOutros = function(callBack) {
	
	this.dataUnBind();
	
	var postData = serializeObjectToPost('garantiaCotaOutros', this.outro);
	var _this = this;
	
	$.postJSON(this.path + 'incluirOutro.json', postData, function(data) {
		
		var tipoMensagem = data.tipoMensagem;
		var listaMensagens = data.listaMensagens;

		if (tipoMensagem && listaMensagens) {
			exibirMensagemDialog(tipoMensagem, listaMensagens,"");
		
		} else {

			var novoOutro = data.outro;
				
			if (_this.itemEdicao == null || _this.itemEdicao < 0) {
				_this.cotaGarantia.outros.push(novoOutro);
				
			} else {
				_this.cotaGarantia.outros.slice(_this.itemEdicao, 1);
				_this.cotaGarantia.outros[_this.itemEdicao] = novoOutro;
			}
			
			_this.limparForm();
			_this.popularGrid();
		}

		if (callBack) {
			callBack();
		}

	}, null, true);
};

Outros.prototype.limparForm = function() {	
	
	this.outro.id = "";
	this.outro.descricao = "";
	this.outro.validade = "";
	this.outro.valor = "";
	this.itemEdicao = null;
	
	this.dataBind();
	$("#cotaGarantiaOutrosSalvaEdicao").hide();
	$("#cotaGarantiaOutrosIncluirNovo").show();
};

Outros.prototype.dataBind = function() {
	
	$("#descricaoCotaGarantiaOutros").val(this.outro.descricao);
	$("#valorCotaGarantiaOutros").val(this.outro.valor);
	$("#validadeCotaGarantiaOutros").val(this.outro.validade);
	
	$("#valorCotaGarantiaOutros").priceFormat({
		allowNegative : true,
		centsSeparator : ',',
		thousandsSeparator : '.'
	});
	
	$("#validadeCotaGarantiaOutros").mask("99/99/9999");
	
};


Outros.prototype.popularGrid = function() {
	
	if (!(this.cotaGarantia && this.cotaGarantia.outros)) {
		this.cotaGarantia.outros =  new Array();
	} 
	
	this.grid.flexAddData({
		rows : toFlexiGridObject(this.cotaGarantia.outros),
		page : 1,
		total : 1
	});
	
	
};

Outros.prototype.salva = function(callBack) {
	
	var postData = serializeArrayToPost('listaOutros', this.cotaGarantia.outros);
		
	postData['idCota'] = this.idCota;
	
	$.postJSON(this.path + 'salvaOutros.json', postData,
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

Outros.prototype.bindEvents = function() {
	
	var _this = this;

	$("#cotaGarantiaOutrosIncluirNovo").click(function() {
		_this.incluirOutros();			 
	});

	$("#cotaGarantiaOutrosSalvaEdicao").click(function() {
		_this.incluirOutros();
	});
	
	$("#validadeCotaGarantiaOutros").mask("99/99/9999");
	
	$("#valorCotaGarantiaOutros").priceFormat({
		allowNegative : true,
		centsSeparator : ',',
		thousandsSeparator : '.'
	});
};

Outros.prototype.destroy = function() {

	$("#cotaGarantiaOutrosIncluir").unbind('click');
	
};

Outros.prototype.edita = function(id) {
	
	this.outro = this.cotaGarantia.outros[id];
	
	this.itemEdicao = id;
	
	this.dataBind();
	
	$("#cotaGarantiaOutrosSalvaEdicao").show();
	
	$("#cotaGarantiaOutrosIncluirNovo").hide();
	
};

Outros.prototype.remove = function(id) {

	var _this = this;

	$("#dialog-excluir-outros").dialog({
		resizable : false,
		height : 'auto',
		width : 380,
		modal : true,
		buttons : {
			"Confirmar" : function() {

				_this.cotaGarantia.outros.splice(id, 1);

				_this.popularGrid();
				
				$(this).dialog("close");
			},
			"Cancelar" : function() {
				$(this).dialog("close");
			}
		}
	});

};

Outros.prototype.initGrid = function() {
	$("#cotaGarantiaOutrosGrid").empty();	
	this.grid = $("<div></div>");
	$("#cotaGarantiaOutrosGrid").append(this.grid);
	this.grid.flexigrid({
		
		preProcess : function(data) {
			if (typeof data.mensagens == "object") {

				exibirMensagemDialog(data.mensagens.tipoMensagem,
						data.mensagens.listaMensagens,"");

			} else {
				$
						.each(
								data.rows,
								function(index, value) {
									
									var idOutros = value.id;

									var acao = '<a href="javascript:;" onclick="tipoCotaGarantia.controller.edita('
											+ idOutros
											+ ');" ><img src="'
											+ contextPath
											+ '/images/ico_editar.gif" border="0" style="margin-right:10px;" /></a>';
									acao += '<a href="javascript:;" onclick="tipoCotaGarantia.controller.remove('
											+ idOutros
											+ ');" ><img src="'
											+ contextPath
											+ '/images/ico_excluir.gif" border="0" /></a>';

									value.cell.acao = acao;
								});
				$(".cotaGarantiaOutrosGrid").flexReload();
				return data;
			}
		},

		
		dataType : 'json',
		colModel : [ {
			display : 'Descrição',
			name : 'descricao',
			width : 270,
			sortable : false,
			align : 'center'
		},{
			display : 'Valor R$',
			name : 'valor',
			width : 140,
			sortable : false,
			align : 'right'
		},{
			display : 'Validade',
			name : 'validade',
			width : 270,
			sortable : false,
			align : 'center'

		},{
			display : 'Ação',
			name : 'acao',
			width : 270,
			sortable : false,
			align : 'center'

		}],
		width : 740,
		height : 150
	});
	
};



//@ sourceURL=scriptCotaGarantia.js