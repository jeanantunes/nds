var produto = {
	
	pesquisarPorCodigoProduto : function() {
		var codigoProduto = $("#codigo").val();
		
		$("#produto").val("");
		$("#edicao").val("");
		$("#edicao").attr("disabled", "disabled");
		
		if(typeof limparCamposPrePesquisaProduto == "function") {
			limparCamposPrePesquisaProduto();
		}
		
		if (codigoProduto && codigoProduto.length > 0) {
			$("#produto").val("");
			$("#edicao").val("");
			
			$.postJSON(contextPath + "/produto/pesquisarPorCodigoProduto",
					   "codigoProduto=" + codigoProduto, this.exibirNomeProduto, null);
		}
	},

	exibirNomeProduto : function(result) {
		$("#edicao").removeAttr("disabled");
		$("#produto").val(result.nome);
		$("#edicao").focus();
		
		produto.chamarFuncaoCallBackPesquisaProduto();
	},

	pesquisarPorNomeProduto : function() {
		var produto = $("#produto").val();
		
		if(typeof limparCamposPrePesquisaProduto == "function") {
			limparCamposPrePesquisaProduto();
		}
		
		if (produto && produto.length > 0) {
			$.postJSON(contextPath + "/produto/pesquisarPorNomeProduto",
					   "nomeProduto=" + produto, this.exibirAutoComplete);
		}
	},

	exibirAutoComplete : function(result) {
		$("#produto").autocomplete({
			source: result,
			select: function(event, ui) {
				produto.completarPesquisa(ui.item.chave);
			}
		});
	},

	completarPesquisa : function(chave){
		$("#edicao").removeAttr("disabled");
		$("#edicao").val("");
		$("#codigo").val(chave.codigo);
		$("#edicao").focus();
		
		produto.chamarFuncaoCallBackPesquisaProduto();
	},

	validarNumEdicao : function() {
		var codigoProduto = $("#codigo").val();
		var numeroEdicao = $("#edicao").val();
		
		if(typeof limparCamposPrePesquisaEdicao == "function") {
			limparCamposPrePesquisaEdicao();
		}
		
		if (codigoProduto && codigoProduto.length > 0
				&& numeroEdicao && numeroEdicao.length > 0) {
			
			var data = "codigoProduto=" + codigoProduto +
			   		   "&numeroEdicao=" + numeroEdicao;
			
			$.postJSON(contextPath + "/produto/validarNumeroEdicao",
					   data, produto.chamarFuncaoCallBackValidacaoEdicao, this.tratarErroValidacao);
		}
	},

	tratarErroValidacao : function() {
		$("#edicao").val("");
		$("#edicao").focus();
	},

	chamarFuncaoCallBackPesquisaProduto : function() {
		if (typeof pesquisarProdutoCallBack == 'function') {
			pesquisarProdutoCallBack();
		}
	},

	chamarFuncaoCallBackValidacaoEdicao : function() {
		if (typeof validarEdicaoCallBack == 'function') {
			validarEdicaoCallBack();
		}
	},
	
};