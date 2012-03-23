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
		}
	},

	pesquisarPorCodigoSuccessCallBack : function(result, idProduto, idEdicao, successCallBack) {
		$(idEdicao).removeAttr("disabled");
		$(idProduto).val(result.nome);
		$(idEdicao).focus();
		
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
		var nomeProduto = $(idProduto).val();
		
		if (nomeProduto && nomeProduto.length > 2) {
			$.postJSON(contextPath + "/produto/autoCompletarPorPorNomeProduto", "nomeProduto=" + nomeProduto,
					   function(result) { produto.exibirAutoComplete(result, idProduto); },
					   null, isFromModal);
		}
	},
	
	exibirAutoComplete : function(result, idProduto) {
		$(idProduto).autocomplete({
			source: result,
		});
	},
	
	//Pesquisar por nome do produto
	pesquisarPorNomeProduto : function(idCodigo, idProduto, idEdicao, isFromModal, successCallBack, errorCallBack) {
		
		//Seta um timed out para esperar o auto complet do campo 
		//de nome do produto ser preenchido pelo jQuery
		setTimeout(function() { produto.pesquisarPorNomeProdutoAposTimedOut(idCodigo, idProduto, idEdicao,
																	        isFromModal, successCallBack, errorCallBack); },  200);
	},
	
	pesquisarPorNomeProdutoAposTimedOut : function(idCodigo, idProduto, idEdicao, isFromModal, successCallBack, errorCallBack) {
		var nomeProduto = $(idProduto).val();
		
		nomeProduto = $.trim(nomeProduto);
		
		$(idCodigo).val("");
		$(idEdicao).val("");
		$(idEdicao).attr("disabled", "disabled");
		
		if (nomeProduto && nomeProduto.length > 0) {
			$.postJSON(contextPath + "/produto/pesquisarPorNomeProduto", "nomeProduto=" + nomeProduto,
					   function(result) { produto.pesquisarPorNomeSuccessCallBack(result, idCodigo, idProduto, idEdicao, successCallBack); },
					   function() { produto.pesquisarPorNomeErrorCallBack(idCodigo, idProduto, idEdicao, errorCallBack); }, isFromModal);
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