var produto = {
	
	//Pesquisa por código de produto
	pesquisarPorCodigoProduto : function(idCodigo, idProduto, idEdicao, isFromModal, successCallBack, errorCallBack) {
		var codigoProduto = $(idCodigo).val();
		
		codigoProduto = $.trim(codigoProduto);
		
		$(idCodigo).val(codigoProduto);
		
		$(idProduto).val("");
		$(idEdicao).val("");
		$(idEdicao).attr("disabled", "disabled");
		
		if (codigoProduto && codigoProduto.length > 0) {
			
			$.postJSON(contextPath + "/produto/pesquisarPorCodigoProduto",
					   "codigoProduto=" + codigoProduto,
					   function(result) { produto.pesquisarPorCodigoSuccessCallBack(result, idProduto, idEdicao, successCallBack); },
					   function() { produto.pesquisarPorCodigoErrorCallBack(idCodigo, errorCallBack); }, isFromModal);
		
		} else {
		
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},
	
	//Pesquisa por código de produto
	pesquisarPorCodigoProdutoAutoCompleteEdicao : function(idCodigo, idProduto, idEdicao, isFromModal, successCallBack, errorCallBack) {
		var codigoProduto = $(idCodigo).val();
		
		codigoProduto = $.trim(codigoProduto);
		
		$(idCodigo).val(codigoProduto);
		
		$(idProduto).val("");
		$(idEdicao).val("");
		
		if (codigoProduto && codigoProduto.length > 0) {
			
			$.postJSON(contextPath + "/produto/pesquisarPorCodigoProduto",
					   "codigoProduto=" + codigoProduto,
					   function(result) { produto.pesquisarPorCodigoSuccessCallBack(result, idProduto, idEdicao, successCallBack, idCodigo, isFromModal); },
					   function() { produto.pesquisarPorCodigoErrorCallBack(idCodigo, errorCallBack); }, isFromModal);
		
		} else {
		
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},

	pesquisarPorCodigoSuccessCallBack : function(result, idProduto, idEdicao, successCallBack,idCodigo, isFromModal) {
		
		if(idCodigo) {
			produto.autoCompletarEdicaoPorProduto(idCodigo, idEdicao, isFromModal);
		} else {
		
			$(idEdicao).removeAttr("disabled");
			$(idProduto).val(result.nome);
			$(idEdicao).focus();
		}
		
		produto.pesquisaRealizada = true;
		
		if (successCallBack) {
			successCallBack();
		}
	},
	
	pesquisarPorCodigoErrorCallBack : function(idCodigo, errorCallBack) {
		$(idCodigo).val("");
		$(idCodigo).focus();
		
		if (errorCallBack) {
			errorCallBack();
		}
	},
	
	//Mostrar auto complete por nome do produto
	autoCompletarEdicaoPorProduto : function(idCodigoProduto, idEdicao, isFromModal) {
		
		produto.pesquisaRealizada = false;
		
		var codigoProduto = $(idCodigoProduto).val();
		
		$.postJSON(contextPath + "/produto/autoCompletarEdicaoPorProduto", "codigoProduto=" + codigoProduto,
					   function(result) { produto.exibirAutoComplete(result, idEdicao); },
					   null, isFromModal);
		
	},
	
	//Mostrar auto complete por nome do produto
	autoCompletarPorNomeProduto : function(idProduto, isFromModal) {
		
		produto.pesquisaRealizada = false;
		
		var nomeProduto = $(idProduto).val();
		
		if (nomeProduto && nomeProduto.length > 2) {
			$.postJSON(contextPath + "/produto/autoCompletarPorPorNomeProduto", "nomeProduto=" + nomeProduto,
					   function(result) { produto.exibirAutoComplete(result, idProduto); },
					   null, isFromModal);
		}
	},
	
	descricaoAtribuida : true,
	
	pesquisaRealizada : false,
	
	intervalo : null,
	
	exibirAutoComplete : function(result, idProduto) {
		
		$(idProduto).autocomplete({
			source : result,
			focus : function(event, ui) {
				produto.descricaoAtribuida = false;
			},
			close : function(event, ui) {
				produto.descricaoAtribuida = true;
			},
			select : function(event, ui) {
				produto.descricaoAtribuida = true;
			},
			minLength: 4,
			delay : 0,
		});
	},
	
	exibirAutoCompleteEdicao : function(result, idEdicao) {
		
		$(idEdicao).autocomplete({
			source : result,
			minLength: 0,
			delay : 0,
		});
	},
	
	//Pesquisar por nome do produto
	pesquisarPorNomeProduto : function(idCodigo, idProduto, idEdicao, isFromModal, successCallBack, errorCallBack) {
		
		setTimeout(function() { clearInterval(produto.intervalo); }, 10 * 1000);
		
		produto.intervalo = setInterval(function() {
			
			if (produto.descricaoAtribuida) {
				
				if (produto.pesquisaRealizada) {
					
					clearInterval(produto.intervalo);
					
					return;
				}
				
				produto.pesquisarPorNomeProdutoAposIntervalo(idCodigo, idProduto, idEdicao,
															isFromModal, successCallBack, errorCallBack);
			}
			
		}, 100);
	},
	
	pesquisarPorNomeProdutoAposIntervalo : function(idCodigo, idProduto, idEdicao, isFromModal, successCallBack, errorCallBack) {
		
		clearInterval(produto.intervalo);
		
		produto.pesquisaRealizada = true;
		
		var nomeProduto = $(idProduto).val();
		
		nomeProduto = $.trim(nomeProduto);
		
		$(idCodigo).val("");
		$(idEdicao).val("");
		$(idEdicao).attr("disabled", "disabled");
		
		if (nomeProduto && nomeProduto.length > 0) {
			$.postJSON(contextPath + "/produto/pesquisarPorNomeProduto", "nomeProduto=" + nomeProduto,
					   function(result) { produto.pesquisarPorNomeSuccessCallBack(result, idCodigo, idProduto, idEdicao, successCallBack); },
					   function() { produto.pesquisarPorNomeErrorCallBack(idCodigo, idProduto, idEdicao, errorCallBack); }, isFromModal);
		} else {
			
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},
	
	pesquisarPorNomeSuccessCallBack : function(result, idCodigo, idProduto, idEdicao, successCallBack) {
		if (result != "") {
			$(idCodigo).val(result.codigo);
			$(idProduto).val(result.nome);
			
			$(idEdicao).removeAttr("disabled");
			$(idEdicao).focus();
			
			if (successCallBack) {
				successCallBack();
			}
		}
	},
	
	pesquisarPorNomeErrorCallBack : function(idCodigo, idProduto, idEdicao, errorCallBack) {
		$(idProduto).val("");
		$(idProduto).focus();
		
		if (errorCallBack) {
			errorCallBack();
		}
	},	

	//Validação do número da edição
	validarNumEdicao : function(idCodigo, idEdicao, isFromModal, successCallBack, errorCallBack) {
		var codigoProduto = $(idCodigo).val();
		var numeroEdicao = $(idEdicao).val();
		
		if (codigoProduto && codigoProduto.length > 0
				&& numeroEdicao && numeroEdicao.length > 0) {
			
			var data = "codigoProduto=" + codigoProduto +
			   		   "&numeroEdicao=" + numeroEdicao;

			$.postJSON(contextPath + "/produto/validarNumeroEdicao",
					data, function(result) { produto.validaNumeroEdicaoSucessoCallBack(idCodigo, idEdicao, successCallBack); },
					function() { produto.validarNumeroEdicaoErrorCallBack(idEdicao, errorCallBack); }, isFromModal);
		} else {
			
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},

	validaNumeroEdicaoSucessoCallBack : function(idCodigo, idEdicao, successCallBack) {
		if (successCallBack) {
			successCallBack(idCodigo, idEdicao);
		}
	},
	
	validarNumeroEdicaoErrorCallBack : function(idEdicao, errorCallBack) {
		$(idEdicao).val("");
		$(idEdicao).focus();
		
		if (errorCallBack) {
			errorCallBack();
		}
	},
	
};