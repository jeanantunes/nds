function TipoProduto() {

	this.initGrid();
	this.bindEvents();
	
	this.ncm = {
		id : null,
		codigo : null
	};
	
	this.tipoProduto = {
		id : null,
		codigo : null,
		codigoNBM : null,
		ncm : null,
		descricao : null,
		grupoProduto : null
	};
	
	$('.file-export').hide();
};

TipoProduto.prototype.path = contextPath + "/administracao/tipoProduto/";

/**
 * inicia a flexiGrid
 */
TipoProduto.prototype.initGrid = function() {

	$(".tipoProdutoGrid")
			.flexigrid(
					{

						preProcess : function(data) {
							
							var mensagens = data.mensagens ? data.mensagens : data;
							
							var tipoMensagem = mensagens.tipoMensagem;
							
							var listaMensagens = mensagens.listaMensagens;
							
							if (tipoMensagem && listaMensagens) {
								exibirMensagem(tipoMensagem, listaMensagens, "");
							
							} else {
								$.each(data.rows,
												function(index, value) {
													var id = value.cell.id;
													var acao = '<a href="javascript:;" onclick="tipoProduto.edita('
															+ id
															+ ');" ><img src="'
															+ contextPath
															+ '/images/ico_editar.gif" border="0" hspace="5" /></a>';
													acao += '<a href="javascript:;" onclick="tipoProduto.remove('
															+ id
															+ ');" ><img src="'
															+ contextPath
															+ '/images/ico_excluir.gif" hspace="5" border="0" /></a>';

													value.cell.acao = acao;
													
													if (!value.cell.codigo) {
														value.cell.codigo = "";
													}
																
													value.cell.ncm = value.cell.ncm.codigo;
								
													if (!value.cell.codigoNBM) {
														value.cell.codigoNBM = "";
													}	
													
												});
								$(".tipoProdutoGrid").flexReload();
								return data;
							}
						},
						dataType : 'json',
						colModel : [ {
							display : 'Código',
							name : 'codigo',
							width : 150,
							sortable : true,
							align : 'left'
						}, {
							display : 'Tipo de Produto',
							name : 'descricao',
							width : 300,
							sortable : true,
							align : 'left'
						}, {
							display : 'Código NCM',
							name : 'ncm',
							width : 150,
							sortable : true,
							align : 'left'
						}, {
							display : 'Código NBM',
							name : 'codigoNBM',
							width : 150,
							sortable : true,
							align : 'left'
						}, {
							display : 'Ação',
							name : 'acao',
							width : 115,
							sortable : false,
							align : 'center'
						} ],

						sortname : "codigo",
						sortorder : "asc",
						usepager : true,
						useRp : true,
						rp : 15,
						showTableToggleBtn : true,
						width : 960,
						height : 255
					});

	$(".tipoProdutoGrid").flexOptions({
		"url" : this.path + 'busca.json',
		params : [ {
			name : "codigo",
			value : $("#tipoProdutoPesquisaCodigo").val()
		}, {
			name : "codigoNCM",
			value : $("#tipoProdutoPesquisaCodigoNCM").val()
		}, {
			name : "codigoNBM",
			value : $("#tipoProdutoPesquisaCodigoNBM").val()
		}, {
			name : "descricao",
			value : $("#tipoProdutoPesquisaDescricao").val()
		} ]
	});
};

TipoProduto.prototype.edita = function(id) {

	var postData = ({
		"id" : id
	});

	var _this = this;

	$.postJSON(this.path + 'getById.json', postData, function(data) {

		var tipoMensagem = data.tipoMensagem;
		var listaMensagens = data.listaMensagens;

		if (tipoMensagem && listaMensagens) {
			exibirMensagem(tipoMensagem, listaMensagens, "");

		} else {
			
			_this.novo("Edita Tipo Produto", true);
			_this.tipoProduto = data.tipoProduto;
			_this.ncm.codigo = data.tipoProduto.codigoNCM;
			_this.ncm.id = data.tipoProduto.idNcm;
			_this.tipoProduto.ncm = _this.ncm;
			_this.dataBind();
			
		}

	}, null, true);

	return false;
};

/**
 * recupera dados do javascript para a tela
 */
TipoProduto.prototype.dataBind = function() {
	$("#tipoProdutoNovoNBM").val(this.tipoProduto.codigoNBM);
	$("#tipoProdutoNovoNCM").val("");
	if(this.tipoProduto.ncm !=null){
	    $("#tipoProdutoNovoNCM").val(this.tipoProduto.ncm.codigo);
	}
	$("#tipoProdutoNovoCodigo").val(this.tipoProduto.codigo);
	$("#tipoProdutoNovoDescricao").val(this.tipoProduto.descricao);
};

/**
 * recupera dados da tela pro objeto javascript
 */
TipoProduto.prototype.dataUnBind = function() {
	this.tipoProduto.codigoNBM = $("#tipoProdutoNovoNBM").val();
	this.ncm.codigo = $("#tipoProdutoNovoNCM").val();
	this.tipoProduto.ncm = this.ncm;
	this.tipoProduto.codigo = $("#tipoProdutoNovoCodigo").val();
	this.tipoProduto.descricao = $("#tipoProdutoNovoDescricao").val();
};

/**
 * pesquisa tipo produto
 */
TipoProduto.prototype.pesquisa = function() {
	$(".grids").show();
	$(".tipoProdutoGrid").flexOptions({
		"url" : this.path + 'busca.json',
		params : [ {
			name : "codigo",
			value : $("#tipoProdutoPesquisaCodigo").val()
		}, {
			name : "codigoNCM",
			value : $("#tipoProdutoPesquisaCodigoNCM").val()
		}, {
			name : "codigoNBM",
			value : $("#tipoProdutoPesquisaCodigoNBM").val()
		}, {
			name : "descricao",
			value : $("#tipoProdutoPesquisaDescricao").val()
		} ],
		newp : 1
	});
	$(".tipoProdutoGrid").flexReload();
	$('.file-export').show();
};


/**
 * envia requisição para salvar o tipo de produto
 */
TipoProduto.prototype.salva = function() {

	this.dataUnBind();

	var postData = serializeObjectToPost("tipoProduto", this.tipoProduto);
	
	$.postJSON(this.path + 'salva.json', postData, function(data) {
		
		var mensagens = (data.mensagens) ? data.mensagens : data;
		var tipoMensagem = mensagens.tipoMensagem;
		var listaMensagens = mensagens.listaMensagens;
		
		if (tipoMensagem && listaMensagens) {
					
			if (tipoMensagem === "SUCCESS") {
			
				tipoProduto.limparForm();
				tipoProduto.pesquisa();
				$("#dialog-novo").dialog("close");
				exibirMensagem(tipoMensagem, listaMensagens);
			} 
		}

	}, function(data) {
		
		console.log(data);
		var mensagens = (data.mensagens) ? data.mensagens : data;
		var tipoMensagem = mensagens.tipoMensagem;
		var listaMensagens = mensagens.listaMensagens;
		
		if (tipoMensagem && listaMensagens) {

			exibirMensagemDialog(tipoMensagem, listaMensagens, "");
		}
		
	}, true);
};

TipoProduto.prototype.getCodigoSugerido = function(callBack) {
	
	$.postJSON(this.path + 'getCodigoSugerido.json', null,function(data) {
		
		var mensagens = (data.mensagens)?data.mensagens:data;
		var tipoMensagem = mensagens.tipoMensagem;
		var listaMensagens = mensagens.listaMensagens;

		if (tipoMensagem && listaMensagens) {
			
			exibirMensagem(tipoMensagem, listaMensagens, "");
		} else {
			tipoProduto.tipoProduto.codigo = data.codigo;
		}
		
		if(callBack){
			callBack();
		}

	});
	
	return false;
};

TipoProduto.prototype.limparForm = function() {
	var _this = this;
	this.tipoProduto.id = null;
	this.tipoProduto.codigo = null;
	this.tipoProduto.codigoNBM = null;
	this.tipoProduto.descricao = null;
	this.tipoProduto.grupoProduto = null;
	this.ncm.id = null;
	this.ncm.codigo = null;
	this.tipoProduto.ncm = this.ncm;
	this.dataBind();
};


/**
 * exibe um confirme para o usuario confirmar a remocao de um tipo produto
 * 
 * @param id
 */
TipoProduto.prototype.remove = function(id) {

	$("#dialog-excluir").dialog({
		resizable : false,
		modal : true,
		title : "Excluir Tipo Produto",
		buttons : {

			"Confirmar" : function() {
				$(this).dialog("close");
				tipoProduto.excluir(id);

			},
			"Cancelar" : function() {
				$(this).dialog("close");
			}
		},
		beforeClose: function() {
			clearMessageDialogTimeout();
		}
	});

	return false;
};

TipoProduto.prototype.excluir = function(id) {

	var postData = ({
		"id" : id
	});

	$.postJSON(this.path + 'remove.json', postData,function(data) {
			
		var mensagens = (data.mensagens)?data.mensagens:data;
		var tipoMensagem = mensagens.tipoMensagem;
		var listaMensagens = mensagens.listaMensagens;

		if (tipoMensagem && listaMensagens) {
			
			exibirMensagem(tipoMensagem, listaMensagens, "");
		} else {

			$(".tipoProdutoGrid").flexReload();
		}

	});
};

/**
 * atribui eventos aos elementos
 */
TipoProduto.prototype.bindEvents = function() {

	var _this = this;

	$("#tipoProdutoPesquisaButton").click(function() {
		_this.pesquisa();
	});

	$("#tipoProdutoNovoButton").click(function() {
		_this.novo();
	});
	
	$("#tipoProdutoNovoCodigo").numeric();
	$("#tipoProdutoPesquisaCodigo").numeric();
};

/**
 * abre o dialog para inserir um novo tipo de produto
 * 
 * @param title
 */
TipoProduto.prototype.novo = function(title, edita) {

	this.limparForm();
	
	if (!edita) {
		this.getCodigoSugerido(function(){_this.dataBind();});
	}
	
	if (!title) {
		title = "Inclui Tipo Produto";
	}
	
	var _this = this;
	$("#dialog-novo").dialog({
		resizable : false,
		width : 500,
		modal : true,
		title : title,
		buttons : {
			"Confirmar" : function() {
				
				_this.salva();

			},
			"Cancelar" : function() {
				$(this).dialog("close");
			}
		},
		beforeClose: function() {
			clearMessageDialogTimeout();
		}
	});
};
