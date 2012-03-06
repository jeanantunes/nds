var produto = {
	
	pesquisarPorCodigoProduto : function(idCodigo, idProduto, idEdicao, pesquisarPorCodigoCallBack) {
		var codigoProduto = $(idCodigo).val();
		
		$(idProduto).val("");
		$(idEdicao).val("");
		$(idEdicao).attr("disabled", "disabled");
		
		if (codigoProduto && codigoProduto.length > 0) {
			$(idProduto).val("");
			$(idEdicao).val("");
			
			$.postJSON(contextPath + "/produto/pesquisarPorCodigoProduto",
					   "codigoProduto=" + codigoProduto,
					   function(result) { produto.exibirNomeProduto(result, idProduto, idEdicao, pesquisarPorCodigoCallBack); }, null);
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

	pesquisarPorNomeProduto : function(idCodigo, idProduto, idEdicao, pesquisarPorNomeCallBack) {
		var nomeProduto = $(idProduto).val();
		
		if (nomeProduto && nomeProduto.length > 0) {
			$.postJSON(contextPath + "/produto/pesquisarPorNomeProduto", "nomeProduto=" + nomeProduto,
					   function(result) { produto.exibirAutoComplete(result, idCodigo, idProduto, idEdicao, pesquisarPorNomeCallBack); });
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

	validarNumEdicao : function(idCodigo, idEdicao, pesquisarPorCodigoCallBack) {
		var codigoProduto = $(idCodigo).val();
		var numeroEdicao = $(idEdicao).val();
		
		if (codigoProduto && codigoProduto.length > 0
				&& numeroEdicao && numeroEdicao.length > 0) {
			
			var data = "codigoProduto=" + codigoProduto +
			   		   "&numeroEdicao=" + numeroEdicao;
			
			$.postJSON(contextPath + "/produto/validarNumeroEdicao",
					   data, pesquisarPorCodigoCallBack,
					   function() { produto.tratarErroValidacao(idEdicao); });
		}
	},

	tratarErroValidacao : function(idEdicao) {
		$(idEdicao).val("");
		$(idEdicao).focus();
	},
	
};