var produtoEdicao = {
	
	//Pesquisa por código de produto
	pesquisarPorCodigoProduto : function(idCodigo, idProduto, isFromModal, successCallBack, errorCallBack) {
		var codigoProduto = $(idCodigo).val();
		
		codigoProduto = $.trim(codigoProduto);
		
		$(idCodigo).val(codigoProduto);
		
		$(idProduto).val("");
		
		if (codigoProduto && codigoProduto.length > 0) {
			
			$.postJSON(contextPath + "/produto/pesquisarPorCodigoProduto",
					   "codigoProduto=" + codigoProduto,
					   function(result) { produtoEdicao.pesquisarPorCodigoSuccessCallBack(result, idProduto, successCallBack); },
					   function() { produtoEdicao.pesquisarPorCodigoErrorCallBack(idCodigo, errorCallBack); }, isFromModal);
		
		} else {
		
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},
	
	//Pesquisa por código de produto
	pesquisarPorCodigoProdutoAutoCompleteEdicao : function(idCodigo, idProduto, isFromModal, successCallBack, errorCallBack) {
		var codigoProduto = $(idCodigo).val();
		
		codigoProduto = $.trim(codigoProduto);
		
		$(idCodigo).val(codigoProduto);
		
		$(idProduto).val("");
		
		if (codigoProduto && codigoProduto.length > 0) {
			
			$.postJSON(contextPath + "/produto/pesquisarPorCodigoProduto",
					   "codigoProduto=" + codigoProduto,
					   function(result) { produtoEdicao.pesquisarPorCodigoSuccessCallBack(result, idProduto, successCallBack, idCodigo, isFromModal); },
					   null, isFromModal);
		
		} else {
		
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},

	pesquisarPorCodigoSuccessCallBack : function(result, idProduto, successCallBack, idCodigo, isFromModal) {
		
		$(idProduto).val(result.nome);
		
		produtoEdicao.pesquisaRealizada = true;
		
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
	autoCompletarPorNomeProduto : function(idProduto, isFromModal) {
		
		produtoEdicao.pesquisaRealizada = false;
		
		var nomeProduto = $(idProduto).val();
		
		if (nomeProduto && nomeProduto.length > 2) {
			$.postJSON(contextPath + "/produto/autoCompletarPorPorNomeProduto", "nomeProduto=" + nomeProduto,
					   function(result) { produtoEdicao.exibirAutoComplete(result, idProduto); },
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
				produtoEdicao.descricaoAtribuida = false;
			},
			close : function(event, ui) {
				produtoEdicao.descricaoAtribuida = true;
			},
			select : function(event, ui) {
				produtoEdicao.descricaoAtribuida = true;
			},
			minLength: 4,
			delay : 0,
		});
	},
	
	//Pesquisar por nome do produto
	pesquisarPorNomeProduto : function(idCodigo, idProduto, isFromModal, successCallBack, errorCallBack) {
		
		setTimeout(function() { clearInterval(produtoEdicao.intervalo); }, 10 * 1000);
		
		produtoEdicao.intervalo = setInterval(function() {
			
			if (produtoEdicao.descricaoAtribuida) {
				
				if (produtoEdicao.pesquisaRealizada) {
					
					clearInterval(produtoEdicao.intervalo);
					
					return;
				}
				
				produtoEdicao.pesquisarPorNomeProdutoAposIntervalo(idCodigo, idProduto,
															isFromModal, successCallBack, errorCallBack);
			}
			
		}, 100);
	},
	
	pesquisarPorNomeProdutoAposIntervalo : function(idCodigo, idProduto, isFromModal, successCallBack, errorCallBack) {
		
		clearInterval(produtoEdicao.intervalo);
		
		produtoEdicao.pesquisaRealizada = true;
		
		var nomeProduto = $(idProduto).val();
		
		nomeProduto = $.trim(nomeProduto);
		
		$(idCodigo).val("");
		
		if (nomeProduto && nomeProduto.length > 0) {
			$.postJSON(contextPath + "/produto/pesquisarPorNomeProduto", "nomeProduto=" + nomeProduto,
					   function(result) { produtoEdicao.pesquisarPorNomeSuccessCallBack(result, idCodigo, idProduto, successCallBack); },
					   function() { produtoEdicao.pesquisarPorNomeErrorCallBack(idCodigo, idProduto, errorCallBack); }, isFromModal);
		} else {
			
			if (errorCallBack) {
				errorCallBack();
			}
		}
	},
	
	pesquisarPorNomeSuccessCallBack : function(result, idCodigo, idProduto, successCallBack) {
		if (result != "") {
			$(idCodigo).val(result.codigo);
			$(idProduto).val(result.nome);
			
			if (successCallBack) {
				successCallBack();
			}
		}
	},
	
	pesquisarPorNomeErrorCallBack : function(idCodigo, idProduto, errorCallBack) {
		$(idProduto).val("");
		$(idProduto).focus();
		
		if (errorCallBack) {
			errorCallBack();
		}
	},	

};