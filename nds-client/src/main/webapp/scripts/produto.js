var produto = {
	
	pesquisarPorCodigoProduto : function(idCodigo, idProduto, idEdicao, isFromModal, pesquisarPorCodigoCallBack) {
		var codigoProduto = $(idCodigo).val();
		
		$(idProduto).val("");
		$(idEdicao).val("");
		$(idEdicao).attr("disabled", "disabled");
		
		if (codigoProduto && codigoProduto.length > 0) {
			$(idProduto).val("");
			$(idEdicao).val("");
			
			$.postJSON(contextPath + "/produto/pesquisarPorCodigoProduto",
					   "codigoProduto=" + codigoProduto,
					   function(result) { produto.exibirNomeProduto(result, idProduto, idEdicao, pesquisarPorCodigoCallBack); }, null, isFromModal);
		}
	},

	exibirNomeProduto : function(result, idProduto, idEdicao, pesquisarPorCodigoCallBack) {
		$(idEdicao).removeAttr("disabled");
		$(idProduto).val(result.nome);
		$(idEdicao).focus();
		
		if (pesquisarPorCodigoCallBack) {
			pesquisarPorCodigoCallBack();
		}
	},

	pesquisarPorNomeProduto : function(idCodigo, idProduto, idEdicao, isFromModal, pesquisarPorNomeCallBack) {
		var nomeProduto = $(idProduto).val();
		
		if (nomeProduto && nomeProduto.length > 2) {
			$.postJSON(contextPath + "/produto/pesquisarPorNomeProduto", "nomeProduto=" + nomeProduto,
					   function(result) { produto.exibirAutoComplete(result, idCodigo, idProduto, idEdicao, pesquisarPorNomeCallBack); }, null, isFromModal);
		}
	},

	exibirAutoComplete : function(result, idCodigo, idProduto, idEdicao, pesquisarPorNomeCallBack) {
		$(idProduto).autocomplete({
			source: result,
			select: function(event, ui) {
				produto.completarPesquisa(ui.item.chave, idCodigo, idEdicao, pesquisarPorNomeCallBack);
			}
		});
	},

	completarPesquisa : function(chave, idCodigo, idEdicao, pesquisarPorNomeCallBack) {
		$(idEdicao).removeAttr("disabled");
		$(idEdicao).val("");
		$(idCodigo).val(chave.codigo);
		$(idEdicao).focus();
		
		if (pesquisarPorNomeCallBack) {
			pesquisarPorNomeCallBack();
		}
	},

	validarNumEdicao : function(idCodigo, idEdicao, isFromModal, pesquisarPorCodigoCallBack) {
		var codigoProduto = $(idCodigo).val();
		var numeroEdicao = $(idEdicao).val();
		
		if (codigoProduto && codigoProduto.length > 0
				&& numeroEdicao && numeroEdicao.length > 0) {
			
			var data = "codigoProduto=" + codigoProduto +
			   		   "&numeroEdicao=" + numeroEdicao;
			
			$.postJSON(contextPath + "/produto/validarNumeroEdicao",
					   data, pesquisarPorCodigoCallBack,
					   function() { produto.tratarErroValidacao(idEdicao); }, isFromModal);
		}
	},

	tratarErroValidacao : function(idEdicao) {
		$(idEdicao).val("");
		$(idEdicao).focus();
	},
	
};